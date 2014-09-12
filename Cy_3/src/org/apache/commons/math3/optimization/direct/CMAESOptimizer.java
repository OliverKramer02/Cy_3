/*    1:     */ package org.apache.commons.math3.optimization.direct;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Arrays;
/*    5:     */ import java.util.List;
/*    6:     */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*    7:     */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*    8:     */ import org.apache.commons.math3.exception.MathUnsupportedOperationException;
/*    9:     */ import org.apache.commons.math3.exception.NotPositiveException;
/*   10:     */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   11:     */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   12:     */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   13:     */ import org.apache.commons.math3.linear.EigenDecomposition;
/*   14:     */ import org.apache.commons.math3.linear.MatrixUtils;
/*   15:     */ import org.apache.commons.math3.linear.RealMatrix;
/*   16:     */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   17:     */ import org.apache.commons.math3.optimization.GoalType;
/*   18:     */ import org.apache.commons.math3.optimization.MultivariateOptimizer;
/*   19:     */ import org.apache.commons.math3.optimization.PointValuePair;
/*   20:     */ import org.apache.commons.math3.optimization.SimpleValueChecker;
/*   21:     */ import org.apache.commons.math3.random.MersenneTwister;
/*   22:     */ import org.apache.commons.math3.random.RandomGenerator;
/*   23:     */ import org.apache.commons.math3.util.MathArrays;
/*   24:     */ 
/*   25:     */ public class CMAESOptimizer
/*   26:     */   extends BaseAbstractMultivariateSimpleBoundsOptimizer<MultivariateFunction>
/*   27:     */   implements MultivariateOptimizer
/*   28:     */ {
/*   29:     */   public static final int DEFAULT_CHECKFEASABLECOUNT = 0;
/*   30:     */   public static final double DEFAULT_STOPFITNESS = 0.0D;
/*   31:     */   public static final boolean DEFAULT_ISACTIVECMA = true;
/*   32:     */   public static final int DEFAULT_MAXITERATIONS = 30000;
/*   33:     */   public static final int DEFAULT_DIAGONALONLY = 0;
/*   34:  97 */   public static final RandomGenerator DEFAULT_RANDOMGENERATOR = new MersenneTwister();
/*   35:     */   private int lambda;
/*   36:     */   private boolean isActiveCMA;
/*   37:     */   private int checkFeasableCount;
/*   38:     */   private double[][] boundaries;
/*   39:     */   private double[] inputSigma;
/*   40:     */   private int dimension;
/*   41: 143 */   private int diagonalOnly = 0;
/*   42: 145 */   private boolean isMinimize = true;
/*   43: 147 */   private boolean generateStatistics = false;
/*   44:     */   private int maxIterations;
/*   45:     */   private double stopFitness;
/*   46:     */   private double stopTolUpX;
/*   47:     */   private double stopTolX;
/*   48:     */   private double stopTolFun;
/*   49:     */   private double stopTolHistFun;
/*   50:     */   private int mu;
/*   51:     */   private double logMu2;
/*   52:     */   private RealMatrix weights;
/*   53:     */   private double mueff;
/*   54:     */   private double sigma;
/*   55:     */   private double cc;
/*   56:     */   private double cs;
/*   57:     */   private double damps;
/*   58:     */   private double ccov1;
/*   59:     */   private double ccovmu;
/*   60:     */   private double chiN;
/*   61:     */   private double ccov1Sep;
/*   62:     */   private double ccovmuSep;
/*   63:     */   private RealMatrix xmean;
/*   64:     */   private RealMatrix pc;
/*   65:     */   private RealMatrix ps;
/*   66:     */   private double normps;
/*   67:     */   private RealMatrix B;
/*   68:     */   private RealMatrix D;
/*   69:     */   private RealMatrix BD;
/*   70:     */   private RealMatrix diagD;
/*   71:     */   private RealMatrix C;
/*   72:     */   private RealMatrix diagC;
/*   73:     */   private int iterations;
/*   74:     */   private double[] fitnessHistory;
/*   75:     */   private int historySize;
/*   76:     */   private RandomGenerator random;
/*   77: 226 */   private List<Double> statisticsSigmaHistory = new ArrayList();
/*   78: 228 */   private List<RealMatrix> statisticsMeanHistory = new ArrayList();
/*   79: 230 */   private List<Double> statisticsFitnessHistory = new ArrayList();
/*   80: 232 */   private List<RealMatrix> statisticsDHistory = new ArrayList();
/*   81:     */   
/*   82:     */   public CMAESOptimizer()
/*   83:     */   {
/*   84: 238 */     this(0);
/*   85:     */   }
/*   86:     */   
/*   87:     */   public CMAESOptimizer(int lambda)
/*   88:     */   {
/*   89: 245 */     this(lambda, null, 30000, 0.0D, true, 0, 0, DEFAULT_RANDOMGENERATOR, false);
/*   90:     */   }
/*   91:     */   
/*   92:     */   public CMAESOptimizer(int lambda, double[] inputSigma)
/*   93:     */   {
/*   94: 255 */     this(lambda, inputSigma, 30000, 0.0D, true, 0, 0, DEFAULT_RANDOMGENERATOR, false);
/*   95:     */   }
/*   96:     */   
/*   97:     */   public CMAESOptimizer(int lambda, double[] inputSigma, int maxIterations, double stopFitness, boolean isActiveCMA, int diagonalOnly, int checkFeasableCount, RandomGenerator random, boolean generateStatistics)
/*   98:     */   {
/*   99: 278 */     this(lambda, inputSigma, maxIterations, stopFitness, isActiveCMA, diagonalOnly, checkFeasableCount, random, generateStatistics, new SimpleValueChecker());
/*  100:     */   }
/*  101:     */   
/*  102:     */   public CMAESOptimizer(int lambda, double[] inputSigma, int maxIterations, double stopFitness, boolean isActiveCMA, int diagonalOnly, int checkFeasableCount, RandomGenerator random, boolean generateStatistics, ConvergenceChecker<PointValuePair> checker)
/*  103:     */   {
/*  104: 303 */     super(checker);
/*  105: 304 */     this.lambda = lambda;
/*  106: 305 */     this.inputSigma = (inputSigma == null ? null : (double[])inputSigma.clone());
/*  107: 306 */     this.maxIterations = maxIterations;
/*  108: 307 */     this.stopFitness = stopFitness;
/*  109: 308 */     this.isActiveCMA = isActiveCMA;
/*  110: 309 */     this.diagonalOnly = diagonalOnly;
/*  111: 310 */     this.checkFeasableCount = checkFeasableCount;
/*  112: 311 */     this.random = random;
/*  113: 312 */     this.generateStatistics = generateStatistics;
/*  114:     */   }
/*  115:     */   
/*  116:     */   public List<Double> getStatisticsSigmaHistory()
/*  117:     */   {
/*  118: 319 */     return this.statisticsSigmaHistory;
/*  119:     */   }
/*  120:     */   
/*  121:     */   public List<RealMatrix> getStatisticsMeanHistory()
/*  122:     */   {
/*  123: 326 */     return this.statisticsMeanHistory;
/*  124:     */   }
/*  125:     */   
/*  126:     */   public List<Double> getStatisticsFitnessHistory()
/*  127:     */   {
/*  128: 333 */     return this.statisticsFitnessHistory;
/*  129:     */   }
/*  130:     */   
/*  131:     */   public List<RealMatrix> getStatisticsDHistory()
/*  132:     */   {
/*  133: 340 */     return this.statisticsDHistory;
/*  134:     */   }
/*  135:     */   
/*  136:     */   protected PointValuePair doOptimize()
/*  137:     */   {
/*  138: 346 */     checkParameters();
/*  139:     */     
/*  140: 348 */     this.isMinimize = getGoalType().equals(GoalType.MINIMIZE);
/*  141: 349 */     FitnessFunction fitfun = new FitnessFunction();
/*  142: 350 */     double[] guess = fitfun.encode(getStartPoint());
/*  143:     */     
/*  144: 352 */     this.dimension = guess.length;
/*  145: 353 */     initializeCMA(guess);
/*  146: 354 */     this.iterations = 0;
/*  147: 355 */     double bestValue = fitfun.value(guess);
/*  148: 356 */     push(this.fitnessHistory, bestValue);
/*  149: 357 */     PointValuePair optimum = new PointValuePair(getStartPoint(), this.isMinimize ? bestValue : -bestValue);
/*  150:     */     
/*  151: 359 */     PointValuePair lastResult = null;
/*  152: 364 */     for (this.iterations = 1; this.iterations <= this.maxIterations; this.iterations += 1)
/*  153:     */     {
/*  154: 366 */       RealMatrix arz = randn1(this.dimension, this.lambda);
/*  155: 367 */       RealMatrix arx = zeros(this.dimension, this.lambda);
/*  156: 368 */       double[] fitness = new double[this.lambda];
/*  157: 370 */       for (int k = 0; k < this.lambda; k++)
/*  158:     */       {
/*  159: 371 */         RealMatrix arxk = null;
/*  160: 372 */         for (int i = 0; i < this.checkFeasableCount + 1; i++)
/*  161:     */         {
/*  162: 373 */           if (this.diagonalOnly <= 0) {
/*  163: 374 */             arxk = this.xmean.add(this.BD.multiply(arz.getColumnMatrix(k)).scalarMultiply(this.sigma));
/*  164:     */           } else {
/*  165: 377 */             arxk = this.xmean.add(times(this.diagD, arz.getColumnMatrix(k)).scalarMultiply(this.sigma));
/*  166:     */           }
/*  167: 380 */           if ((i >= this.checkFeasableCount) || (fitfun.isFeasible(arxk.getColumn(0)))) {
/*  168:     */             break;
/*  169:     */           }
/*  170: 384 */           arz.setColumn(k, randn(this.dimension));
/*  171:     */         }
/*  172: 386 */         copyColumn(arxk, 0, arx, k);
/*  173:     */         try
/*  174:     */         {
/*  175: 388 */           fitness[k] = fitfun.value(arx.getColumn(k));
/*  176:     */         }
/*  177:     */         catch (TooManyEvaluationsException e)
/*  178:     */         {
/*  179:     */           break;
/*  180:     */         }
/*  181:     */       }
/*  182: 394 */       int[] arindex = sortedIndices(fitness);
/*  183:     */       
/*  184: 396 */       RealMatrix xold = this.xmean;
/*  185: 397 */       RealMatrix bestArx = selectColumns(arx, MathArrays.copyOf(arindex, this.mu));
/*  186: 398 */       this.xmean = bestArx.multiply(this.weights);
/*  187: 399 */       RealMatrix bestArz = selectColumns(arz, MathArrays.copyOf(arindex, this.mu));
/*  188: 400 */       RealMatrix zmean = bestArz.multiply(this.weights);
/*  189: 401 */       boolean hsig = updateEvolutionPaths(zmean, xold);
/*  190: 402 */       if (this.diagonalOnly <= 0) {
/*  191: 403 */         updateCovariance(hsig, bestArx, arz, arindex, xold);
/*  192:     */       } else {
/*  193: 405 */         updateCovarianceDiagonalOnly(hsig, bestArz, xold);
/*  194:     */       }
/*  195: 408 */       this.sigma *= Math.exp(Math.min(1.0D, (this.normps / this.chiN - 1.0D) * this.cs / this.damps));
/*  196: 409 */       double bestFitness = fitness[arindex[0]];
/*  197: 410 */       double worstFitness = fitness[arindex[(arindex.length - 1)]];
/*  198: 411 */       if (bestValue > bestFitness)
/*  199:     */       {
/*  200: 412 */         bestValue = bestFitness;
/*  201: 413 */         lastResult = optimum;
/*  202: 414 */         optimum = new PointValuePair(fitfun.decode(bestArx.getColumn(0)), this.isMinimize ? bestFitness : -bestFitness);
/*  203: 417 */         if ((getConvergenceChecker() != null) && (lastResult != null) && 
/*  204: 418 */           (getConvergenceChecker().converged(this.iterations, optimum, lastResult))) {
/*  205:     */           break;
/*  206:     */         }
/*  207:     */       }
/*  208: 425 */       if (this.stopFitness != 0.0D) {
/*  209: 426 */         if (bestFitness < (this.isMinimize ? this.stopFitness : -this.stopFitness)) {
/*  210:     */           break;
/*  211:     */         }
/*  212:     */       }
/*  213: 430 */       double[] sqrtDiagC = sqrt(this.diagC).getColumn(0);
/*  214: 431 */       double[] pcCol = this.pc.getColumn(0);
/*  215: 432 */       for (int i = 0; i < this.dimension; i++)
/*  216:     */       {
/*  217: 433 */         if (this.sigma * Math.max(Math.abs(pcCol[i]), sqrtDiagC[i]) > this.stopTolX) {
/*  218:     */           break;
/*  219:     */         }
/*  220: 436 */         if (i >= this.dimension - 1) {
/*  221:     */           break;
/*  222:     */         }
/*  223:     */       }
/*  224: 440 */       for (int i = 0; i < this.dimension; i++) {
/*  225: 441 */         if (this.sigma * sqrtDiagC[i] > this.stopTolUpX) {
/*  226:     */           break;
/*  227:     */         }
/*  228:     */       }
/*  229: 445 */       double historyBest = min(this.fitnessHistory);
/*  230: 446 */       double historyWorst = max(this.fitnessHistory);
/*  231: 447 */       if ((this.iterations > 2) && (Math.max(historyWorst, worstFitness) - Math.min(historyBest, bestFitness) < this.stopTolFun)) {
/*  232:     */         break;
/*  233:     */       }
/*  234: 451 */       if ((this.iterations > this.fitnessHistory.length) && (historyWorst - historyBest < this.stopTolHistFun)) {
/*  235:     */         break;
/*  236:     */       }
/*  237: 456 */       if (max(this.diagD) / min(this.diagD) > 10000000.0D) {
/*  238:     */         break;
/*  239:     */       }
/*  240: 460 */       if (getConvergenceChecker() != null)
/*  241:     */       {
/*  242: 461 */         PointValuePair current = new PointValuePair(bestArx.getColumn(0), this.isMinimize ? bestFitness : -bestFitness);
/*  243: 464 */         if ((lastResult != null) && (getConvergenceChecker().converged(this.iterations, current, lastResult))) {
/*  244:     */           break;
/*  245:     */         }
/*  246: 468 */         lastResult = current;
/*  247:     */       }
/*  248: 471 */       if (bestValue == fitness[arindex[((int)(0.1D + this.lambda / 4.0D))]]) {
/*  249: 472 */         this.sigma *= Math.exp(0.2D + this.cs / this.damps);
/*  250:     */       }
/*  251: 474 */       if ((this.iterations > 2) && (Math.max(historyWorst, bestFitness) - Math.min(historyBest, bestFitness) == 0.0D)) {
/*  252: 476 */         this.sigma *= Math.exp(0.2D + this.cs / this.damps);
/*  253:     */       }
/*  254: 479 */       push(this.fitnessHistory, bestFitness);
/*  255: 480 */       fitfun.setValueRange(worstFitness - bestFitness);
/*  256: 481 */       if (this.generateStatistics)
/*  257:     */       {
/*  258: 482 */         this.statisticsSigmaHistory.add(Double.valueOf(this.sigma));
/*  259: 483 */         this.statisticsFitnessHistory.add(Double.valueOf(bestFitness));
/*  260: 484 */         this.statisticsMeanHistory.add(this.xmean.transpose());
/*  261: 485 */         this.statisticsDHistory.add(this.diagD.transpose().scalarMultiply(100000.0D));
/*  262:     */       }
/*  263:     */     }
/*  264:     */     label1175:
/*  265: 488 */     return optimum;
/*  266:     */   }
/*  267:     */   
/*  268:     */   private void checkParameters()
/*  269:     */   {
/*  270: 495 */     double[] init = getStartPoint();
/*  271: 496 */     double[] lB = getLowerBound();
/*  272: 497 */     double[] uB = getUpperBound();
/*  273:     */     
/*  274:     */ 
/*  275: 500 */     boolean hasFiniteBounds = false;
/*  276: 501 */     for (int i = 0; i < lB.length; i++) {
/*  277: 502 */       if ((!Double.isInfinite(lB[i])) || (!Double.isInfinite(uB[i])))
/*  278:     */       {
/*  279: 504 */         hasFiniteBounds = true;
/*  280: 505 */         break;
/*  281:     */       }
/*  282:     */     }
/*  283: 509 */     boolean hasInfiniteBounds = false;
/*  284: 510 */     if (hasFiniteBounds)
/*  285:     */     {
/*  286: 511 */       for (int i = 0; i < lB.length; i++) {
/*  287: 512 */         if ((Double.isInfinite(lB[i])) || (Double.isInfinite(uB[i])))
/*  288:     */         {
/*  289: 514 */           hasInfiniteBounds = true;
/*  290: 515 */           break;
/*  291:     */         }
/*  292:     */       }
/*  293: 519 */       if (hasInfiniteBounds) {
/*  294: 522 */         throw new MathUnsupportedOperationException();
/*  295:     */       }
/*  296: 525 */       this.boundaries = new double[2][];
/*  297: 526 */       this.boundaries[0] = lB;
/*  298: 527 */       this.boundaries[1] = uB;
/*  299:     */     }
/*  300:     */     else
/*  301:     */     {
/*  302: 531 */       this.boundaries = ((double[][])null);
/*  303:     */     }
/*  304: 534 */     if (this.inputSigma != null)
/*  305:     */     {
/*  306: 535 */       if (this.inputSigma.length != init.length) {
/*  307: 536 */         throw new DimensionMismatchException(this.inputSigma.length, init.length);
/*  308:     */       }
/*  309: 538 */       for (int i = 0; i < init.length; i++)
/*  310:     */       {
/*  311: 539 */         if (this.inputSigma[i] < 0.0D) {
/*  312: 540 */           throw new NotPositiveException(Double.valueOf(this.inputSigma[i]));
/*  313:     */         }
/*  314: 542 */         if ((this.boundaries != null) && 
/*  315: 543 */           (this.inputSigma[i] > this.boundaries[1][i] - this.boundaries[0][i])) {
/*  316: 544 */           throw new OutOfRangeException(Double.valueOf(this.inputSigma[i]), Integer.valueOf(0), Double.valueOf(this.boundaries[1][i] - this.boundaries[0][i]));
/*  317:     */         }
/*  318:     */       }
/*  319:     */     }
/*  320:     */   }
/*  321:     */   
/*  322:     */   private void initializeCMA(double[] guess)
/*  323:     */   {
/*  324: 557 */     if (this.lambda <= 0) {
/*  325: 558 */       this.lambda = (4 + (int)(3.0D * Math.log(this.dimension)));
/*  326:     */     }
/*  327: 561 */     double[][] sigmaArray = new double[guess.length][1];
/*  328: 562 */     for (int i = 0; i < guess.length; i++)
/*  329:     */     {
/*  330: 563 */       double range = this.boundaries == null ? 1.0D : this.boundaries[1][i] - this.boundaries[0][i];
/*  331: 564 */       sigmaArray[i][0] = ((this.inputSigma == null ? 0.3D : this.inputSigma[i]) / range);
/*  332:     */     }
/*  333: 566 */     RealMatrix insigma = new Array2DRowRealMatrix(sigmaArray, false);
/*  334: 567 */     this.sigma = max(insigma);
/*  335:     */     
/*  336:     */ 
/*  337: 570 */     this.stopTolUpX = (1000.0D * max(insigma));
/*  338: 571 */     this.stopTolX = (9.999999999999999E-012D * max(insigma));
/*  339: 572 */     this.stopTolFun = 1.0E-012D;
/*  340: 573 */     this.stopTolHistFun = 1.0E-013D;
/*  341:     */     
/*  342:     */ 
/*  343: 576 */     this.mu = (this.lambda / 2);
/*  344: 577 */     this.logMu2 = Math.log(this.mu + 0.5D);
/*  345: 578 */     this.weights = log(sequence(1.0D, this.mu, 1.0D)).scalarMultiply(-1.0D).scalarAdd(this.logMu2);
/*  346: 579 */     double sumw = 0.0D;
/*  347: 580 */     double sumwq = 0.0D;
/*  348: 581 */     for (int i = 0; i < this.mu; i++)
/*  349:     */     {
/*  350: 582 */       double w = this.weights.getEntry(i, 0);
/*  351: 583 */       sumw += w;
/*  352: 584 */       sumwq += w * w;
/*  353:     */     }
/*  354: 586 */     this.weights = this.weights.scalarMultiply(1.0D / sumw);
/*  355: 587 */     this.mueff = (sumw * sumw / sumwq);
/*  356:     */     
/*  357:     */ 
/*  358: 590 */     this.cc = ((4.0D + this.mueff / this.dimension) / (this.dimension + 4.0D + 2.0D * this.mueff / this.dimension));
/*  359:     */     
/*  360: 592 */     this.cs = ((this.mueff + 2.0D) / (this.dimension + this.mueff + 3.0D));
/*  361: 593 */     this.damps = ((1.0D + 2.0D * Math.max(0.0D, Math.sqrt((this.mueff - 1.0D) / (this.dimension + 1.0D)) - 1.0D)) * Math.max(0.3D, 1.0D - this.dimension / (1.0E-006D + Math.min(this.maxIterations, getMaxEvaluations() / this.lambda))) + this.cs);
/*  362:     */     
/*  363:     */ 
/*  364:     */ 
/*  365:     */ 
/*  366: 598 */     this.ccov1 = (2.0D / ((this.dimension + 1.3D) * (this.dimension + 1.3D) + this.mueff));
/*  367: 599 */     this.ccovmu = Math.min(1.0D - this.ccov1, 2.0D * (this.mueff - 2.0D + 1.0D / this.mueff) / ((this.dimension + 2.0D) * (this.dimension + 2.0D) + this.mueff));
/*  368:     */     
/*  369: 601 */     this.ccov1Sep = Math.min(1.0D, this.ccov1 * (this.dimension + 1.5D) / 3.0D);
/*  370: 602 */     this.ccovmuSep = Math.min(1.0D - this.ccov1, this.ccovmu * (this.dimension + 1.5D) / 3.0D);
/*  371: 603 */     this.chiN = (Math.sqrt(this.dimension) * (1.0D - 1.0D / (4.0D * this.dimension) + 1.0D / (21.0D * this.dimension * this.dimension)));
/*  372:     */     
/*  373:     */ 
/*  374: 606 */     this.xmean = MatrixUtils.createColumnRealMatrix(guess);
/*  375:     */     
/*  376: 608 */     this.diagD = insigma.scalarMultiply(1.0D / this.sigma);
/*  377: 609 */     this.diagC = square(this.diagD);
/*  378: 610 */     this.pc = zeros(this.dimension, 1);
/*  379: 611 */     this.ps = zeros(this.dimension, 1);
/*  380: 612 */     this.normps = this.ps.getFrobeniusNorm();
/*  381:     */     
/*  382: 614 */     this.B = eye(this.dimension, this.dimension);
/*  383: 615 */     this.D = ones(this.dimension, 1);
/*  384: 616 */     this.BD = times(this.B, repmat(this.diagD.transpose(), this.dimension, 1));
/*  385: 617 */     this.C = this.B.multiply(diag(square(this.D)).multiply(this.B.transpose()));
/*  386: 618 */     this.historySize = (10 + (int)(30.0D * this.dimension / this.lambda));
/*  387: 619 */     this.fitnessHistory = new double[this.historySize];
/*  388: 620 */     for (int i = 0; i < this.historySize; i++) {
/*  389: 621 */       this.fitnessHistory[i] = 1.7976931348623157E+308D;
/*  390:     */     }
/*  391:     */   }
/*  392:     */   
/*  393:     */   private boolean updateEvolutionPaths(RealMatrix zmean, RealMatrix xold)
/*  394:     */   {
/*  395: 634 */     this.ps = this.ps.scalarMultiply(1.0D - this.cs).add(this.B.multiply(zmean).scalarMultiply(Math.sqrt(this.cs * (2.0D - this.cs) * this.mueff)));
/*  396:     */     
/*  397:     */ 
/*  398: 637 */     this.normps = this.ps.getFrobeniusNorm();
/*  399: 638 */     boolean hsig = this.normps / Math.sqrt(1.0D - Math.pow(1.0D - this.cs, 2.0D * this.iterations)) / this.chiN < 1.4D + 2.0D / (this.dimension + 1.0D);
/*  400:     */     
/*  401:     */ 
/*  402: 641 */     this.pc = this.pc.scalarMultiply(1.0D - this.cc);
/*  403: 642 */     if (hsig) {
/*  404: 643 */       this.pc = this.pc.add(this.xmean.subtract(xold).scalarMultiply(Math.sqrt(this.cc * (2.0D - this.cc) * this.mueff) / this.sigma));
/*  405:     */     }
/*  406: 646 */     return hsig;
/*  407:     */   }
/*  408:     */   
/*  409:     */   private void updateCovarianceDiagonalOnly(boolean hsig, RealMatrix bestArz, RealMatrix xold)
/*  410:     */   {
/*  411: 661 */     double oldFac = hsig ? 0.0D : this.ccov1Sep * this.cc * (2.0D - this.cc);
/*  412: 662 */     oldFac += 1.0D - this.ccov1Sep - this.ccovmuSep;
/*  413: 663 */     this.diagC = this.diagC.scalarMultiply(oldFac).add(square(this.pc).scalarMultiply(this.ccov1Sep)).add(times(this.diagC, square(bestArz).multiply(this.weights)).scalarMultiply(this.ccovmuSep));
/*  414:     */     
/*  415:     */ 
/*  416:     */ 
/*  417:     */ 
/*  418:     */ 
/*  419: 669 */     this.diagD = sqrt(this.diagC);
/*  420: 670 */     if ((this.diagonalOnly > 1) && (this.iterations > this.diagonalOnly))
/*  421:     */     {
/*  422: 672 */       this.diagonalOnly = 0;
/*  423: 673 */       this.B = eye(this.dimension, this.dimension);
/*  424: 674 */       this.BD = diag(this.diagD);
/*  425: 675 */       this.C = diag(this.diagC);
/*  426:     */     }
/*  427:     */   }
/*  428:     */   
/*  429:     */   private void updateCovariance(boolean hsig, RealMatrix bestArx, RealMatrix arz, int[] arindex, RealMatrix xold)
/*  430:     */   {
/*  431: 692 */     double negccov = 0.0D;
/*  432: 693 */     if (this.ccov1 + this.ccovmu > 0.0D)
/*  433:     */     {
/*  434: 694 */       RealMatrix arpos = bestArx.subtract(repmat(xold, 1, this.mu)).scalarMultiply(1.0D / this.sigma);
/*  435:     */       
/*  436: 696 */       RealMatrix roneu = this.pc.multiply(this.pc.transpose()).scalarMultiply(this.ccov1);
/*  437:     */       
/*  438:     */ 
/*  439: 699 */       double oldFac = hsig ? 0.0D : this.ccov1 * this.cc * (2.0D - this.cc);
/*  440: 700 */       oldFac += 1.0D - this.ccov1 - this.ccovmu;
/*  441: 701 */       if (this.isActiveCMA)
/*  442:     */       {
/*  443: 703 */         negccov = (1.0D - this.ccovmu) * 0.25D * this.mueff / (Math.pow(this.dimension + 2.0D, 1.5D) + 2.0D * this.mueff);
/*  444:     */         
/*  445: 705 */         double negminresidualvariance = 0.66D;
/*  446:     */         
/*  447:     */ 
/*  448: 708 */         double negalphaold = 0.5D;
/*  449:     */         
/*  450:     */ 
/*  451: 711 */         int[] arReverseIndex = reverse(arindex);
/*  452: 712 */         RealMatrix arzneg = selectColumns(arz, MathArrays.copyOf(arReverseIndex, this.mu));
/*  453:     */         
/*  454: 714 */         RealMatrix arnorms = sqrt(sumRows(square(arzneg)));
/*  455: 715 */         int[] idxnorms = sortedIndices(arnorms.getRow(0));
/*  456: 716 */         RealMatrix arnormsSorted = selectColumns(arnorms, idxnorms);
/*  457: 717 */         int[] idxReverse = reverse(idxnorms);
/*  458: 718 */         RealMatrix arnormsReverse = selectColumns(arnorms, idxReverse);
/*  459: 719 */         arnorms = divide(arnormsReverse, arnormsSorted);
/*  460: 720 */         int[] idxInv = inverse(idxnorms);
/*  461: 721 */         RealMatrix arnormsInv = selectColumns(arnorms, idxInv);
/*  462:     */         
/*  463: 723 */         double negcovMax = (1.0D - negminresidualvariance) / square(arnormsInv).multiply(this.weights).getEntry(0, 0);
/*  464: 725 */         if (negccov > negcovMax) {
/*  465: 726 */           negccov = negcovMax;
/*  466:     */         }
/*  467: 728 */         arzneg = times(arzneg, repmat(arnormsInv, this.dimension, 1));
/*  468: 729 */         RealMatrix artmp = this.BD.multiply(arzneg);
/*  469: 730 */         RealMatrix Cneg = artmp.multiply(diag(this.weights)).multiply(artmp.transpose());
/*  470:     */         
/*  471: 732 */         oldFac += negalphaold * negccov;
/*  472: 733 */         this.C = this.C.scalarMultiply(oldFac).add(roneu).add(arpos.scalarMultiply(this.ccovmu + (1.0D - negalphaold) * negccov).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose()))).subtract(Cneg.scalarMultiply(negccov));
/*  473:     */       }
/*  474:     */       else
/*  475:     */       {
/*  476: 746 */         this.C = this.C.scalarMultiply(oldFac).add(roneu).add(arpos.scalarMultiply(this.ccovmu).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose())));
/*  477:     */       }
/*  478:     */     }
/*  479: 755 */     updateBD(negccov);
/*  480:     */   }
/*  481:     */   
/*  482:     */   private void updateBD(double negccov)
/*  483:     */   {
/*  484: 764 */     if ((this.ccov1 + this.ccovmu + negccov > 0.0D) && (this.iterations % 1.0D / (this.ccov1 + this.ccovmu + negccov) / this.dimension / 10.0D < 1.0D))
/*  485:     */     {
/*  486: 767 */       this.C = triu(this.C, 0).add(triu(this.C, 1).transpose());
/*  487:     */       
/*  488: 769 */       EigenDecomposition eig = new EigenDecomposition(this.C, 1.0D);
/*  489: 770 */       this.B = eig.getV();
/*  490: 771 */       this.D = eig.getD();
/*  491: 772 */       this.diagD = diag(this.D);
/*  492: 773 */       if (min(this.diagD) <= 0.0D)
/*  493:     */       {
/*  494: 774 */         for (int i = 0; i < this.dimension; i++) {
/*  495: 775 */           if (this.diagD.getEntry(i, 0) < 0.0D) {
/*  496: 776 */             this.diagD.setEntry(i, 0, 0.0D);
/*  497:     */           }
/*  498:     */         }
/*  499: 779 */         double tfac = max(this.diagD) / 100000000000000.0D;
/*  500: 780 */         this.C = this.C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac));
/*  501: 781 */         this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac));
/*  502:     */       }
/*  503: 783 */       if (max(this.diagD) > 100000000000000.0D * min(this.diagD))
/*  504:     */       {
/*  505: 784 */         double tfac = max(this.diagD) / 100000000000000.0D - min(this.diagD);
/*  506: 785 */         this.C = this.C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac));
/*  507: 786 */         this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac));
/*  508:     */       }
/*  509: 788 */       this.diagC = diag(this.C);
/*  510: 789 */       this.diagD = sqrt(this.diagD);
/*  511: 790 */       this.BD = times(this.B, repmat(this.diagD.transpose(), this.dimension, 1));
/*  512:     */     }
/*  513:     */   }
/*  514:     */   
/*  515:     */   private static void push(double[] vals, double val)
/*  516:     */   {
/*  517: 801 */     for (int i = vals.length - 1; i > 0; i--) {
/*  518: 802 */       vals[i] = vals[(i - 1)];
/*  519:     */     }
/*  520: 804 */     vals[0] = val;
/*  521:     */   }
/*  522:     */   
/*  523:     */   private int[] sortedIndices(double[] doubles)
/*  524:     */   {
/*  525: 814 */     DoubleIndex[] dis = new DoubleIndex[doubles.length];
/*  526: 815 */     for (int i = 0; i < doubles.length; i++) {
/*  527: 816 */       dis[i] = new DoubleIndex(doubles[i], i);
/*  528:     */     }
/*  529: 818 */     Arrays.sort(dis);
/*  530: 819 */     int[] indices = new int[doubles.length];
/*  531: 820 */     for (int i = 0; i < doubles.length; i++) {
/*  532: 821 */       indices[i] = dis[i].index;
/*  533:     */     }
/*  534: 823 */     return indices;
/*  535:     */   }
/*  536:     */   
/*  537:     */   private static class DoubleIndex
/*  538:     */     implements Comparable<DoubleIndex>
/*  539:     */   {
/*  540:     */     private double value;
/*  541:     */     private int index;
/*  542:     */     
/*  543:     */     DoubleIndex(double value, int index)
/*  544:     */     {
/*  545: 841 */       this.value = value;
/*  546: 842 */       this.index = index;
/*  547:     */     }
/*  548:     */     
/*  549:     */     public int compareTo(DoubleIndex o)
/*  550:     */     {
/*  551: 847 */       return Double.compare(this.value, o.value);
/*  552:     */     }
/*  553:     */     
/*  554:     */     public boolean equals(Object other)
/*  555:     */     {
/*  556: 854 */       if (this == other) {
/*  557: 855 */         return true;
/*  558:     */       }
/*  559: 858 */       if ((other instanceof DoubleIndex)) {
/*  560: 859 */         return Double.compare(this.value, ((DoubleIndex)other).value) == 0;
/*  561:     */       }
/*  562: 862 */       return false;
/*  563:     */     }
/*  564:     */     
/*  565:     */     public int hashCode()
/*  566:     */     {
/*  567: 869 */       long bits = Double.doubleToLongBits(this.value);
/*  568: 870 */       return (int)((0x15F34E ^ bits >>> 32 ^ bits) & 0xFFFFFFFF);
/*  569:     */     }
/*  570:     */   }
/*  571:     */   
/*  572:     */   private class FitnessFunction
/*  573:     */   {
/*  574:     */     private double valueRange;
/*  575:     */     private boolean isRepairMode;
/*  576:     */     
/*  577:     */     public FitnessFunction()
/*  578:     */     {
/*  579: 892 */       this.valueRange = 1.0D;
/*  580: 893 */       this.isRepairMode = true;
/*  581:     */     }
/*  582:     */     
/*  583:     */     public double[] encode(double[] x)
/*  584:     */     {
/*  585: 901 */       if (CMAESOptimizer.this.boundaries == null) {
/*  586: 902 */         return x;
/*  587:     */       }
/*  588: 904 */       double[] res = new double[x.length];
/*  589: 905 */       for (int i = 0; i < x.length; i++)
/*  590:     */       {
/*  591: 906 */         double diff = CMAESOptimizer.this.boundaries[1][i] - CMAESOptimizer.this.boundaries[0][i];
/*  592: 907 */         res[i] = ((x[i] - CMAESOptimizer.this.boundaries[0][i]) / diff);
/*  593:     */       }
/*  594: 909 */       return res;
/*  595:     */     }
/*  596:     */     
/*  597:     */     public double[] decode(double[] x)
/*  598:     */     {
/*  599: 917 */       if (CMAESOptimizer.this.boundaries == null) {
/*  600: 918 */         return x;
/*  601:     */       }
/*  602: 920 */       double[] res = new double[x.length];
/*  603: 921 */       for (int i = 0; i < x.length; i++)
/*  604:     */       {
/*  605: 922 */         double diff = CMAESOptimizer.this.boundaries[1][i] - CMAESOptimizer.this.boundaries[0][i];
/*  606: 923 */         res[i] = (diff * x[i] + CMAESOptimizer.this.boundaries[0][i]);
/*  607:     */       }
/*  608: 925 */       return res;
/*  609:     */     }
/*  610:     */     
/*  611:     */     public double value(double[] point)
/*  612:     */     {
/*  613:     */       double value;
/*  614:     */       
/*  615: 934 */       if ((CMAESOptimizer.this.boundaries != null) && (this.isRepairMode))
/*  616:     */       {
/*  617: 935 */         double[] repaired = repair(point);
/*  618: 936 */         value = CMAESOptimizer.this.computeObjectiveValue(decode(repaired)) + penalty(point, repaired);
/*  619:     */       }
/*  620:     */       else
/*  621:     */       {
/*  622: 940 */         value = CMAESOptimizer.this.computeObjectiveValue(decode(point));
/*  623:     */       }
/*  624: 943 */       return CMAESOptimizer.this.isMinimize ? value : -value;
/*  625:     */     }
/*  626:     */     
/*  627:     */     public boolean isFeasible(double[] x)
/*  628:     */     {
/*  629: 951 */       if (CMAESOptimizer.this.boundaries == null) {
/*  630: 952 */         return true;
/*  631:     */       }
/*  632: 954 */       for (int i = 0; i < x.length; i++)
/*  633:     */       {
/*  634: 955 */         if (x[i] < 0.0D) {
/*  635: 956 */           return false;
/*  636:     */         }
/*  637: 958 */         if (x[i] > 1.0D) {
/*  638: 959 */           return false;
/*  639:     */         }
/*  640:     */       }
/*  641: 962 */       return true;
/*  642:     */     }
/*  643:     */     
/*  644:     */     public void setValueRange(double valueRange)
/*  645:     */     {
/*  646: 969 */       this.valueRange = valueRange;
/*  647:     */     }
/*  648:     */     
/*  649:     */     private double[] repair(double[] x)
/*  650:     */     {
/*  651: 977 */       double[] repaired = new double[x.length];
/*  652: 978 */       for (int i = 0; i < x.length; i++) {
/*  653: 979 */         if (x[i] < 0.0D) {
/*  654: 980 */           repaired[i] = 0.0D;
/*  655: 981 */         } else if (x[i] > 1.0D) {
/*  656: 982 */           repaired[i] = 1.0D;
/*  657:     */         } else {
/*  658: 984 */           repaired[i] = x[i];
/*  659:     */         }
/*  660:     */       }
/*  661: 987 */       return repaired;
/*  662:     */     }
/*  663:     */     
/*  664:     */     private double penalty(double[] x, double[] repaired)
/*  665:     */     {
/*  666: 996 */       double penalty = 0.0D;
/*  667: 997 */       for (int i = 0; i < x.length; i++)
/*  668:     */       {
/*  669: 998 */         double diff = Math.abs(x[i] - repaired[i]);
/*  670: 999 */         penalty += diff * this.valueRange;
/*  671:     */       }
/*  672:1001 */       return CMAESOptimizer.this.isMinimize ? penalty : -penalty;
/*  673:     */     }
/*  674:     */   }
/*  675:     */   
/*  676:     */   private static RealMatrix log(RealMatrix m)
/*  677:     */   {
/*  678:1012 */     double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
/*  679:1013 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  680:1014 */       for (int c = 0; c < m.getColumnDimension(); c++) {
/*  681:1015 */         d[r][c] = Math.log(m.getEntry(r, c));
/*  682:     */       }
/*  683:     */     }
/*  684:1018 */     return new Array2DRowRealMatrix(d, false);
/*  685:     */   }
/*  686:     */   
/*  687:     */   private static RealMatrix sqrt(RealMatrix m)
/*  688:     */   {
/*  689:1027 */     double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
/*  690:1028 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  691:1029 */       for (int c = 0; c < m.getColumnDimension(); c++) {
/*  692:1030 */         d[r][c] = Math.sqrt(m.getEntry(r, c));
/*  693:     */       }
/*  694:     */     }
/*  695:1033 */     return new Array2DRowRealMatrix(d, false);
/*  696:     */   }
/*  697:     */   
/*  698:     */   private static RealMatrix square(RealMatrix m)
/*  699:     */   {
/*  700:1041 */     double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
/*  701:1042 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  702:1043 */       for (int c = 0; c < m.getColumnDimension(); c++)
/*  703:     */       {
/*  704:1044 */         double e = m.getEntry(r, c);
/*  705:1045 */         d[r][c] = (e * e);
/*  706:     */       }
/*  707:     */     }
/*  708:1048 */     return new Array2DRowRealMatrix(d, false);
/*  709:     */   }
/*  710:     */   
/*  711:     */   private static RealMatrix times(RealMatrix m, RealMatrix n)
/*  712:     */   {
/*  713:1057 */     double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
/*  714:1058 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  715:1059 */       for (int c = 0; c < m.getColumnDimension(); c++) {
/*  716:1060 */         d[r][c] = (m.getEntry(r, c) * n.getEntry(r, c));
/*  717:     */       }
/*  718:     */     }
/*  719:1063 */     return new Array2DRowRealMatrix(d, false);
/*  720:     */   }
/*  721:     */   
/*  722:     */   private static RealMatrix divide(RealMatrix m, RealMatrix n)
/*  723:     */   {
/*  724:1072 */     double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
/*  725:1073 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  726:1074 */       for (int c = 0; c < m.getColumnDimension(); c++) {
/*  727:1075 */         d[r][c] = (m.getEntry(r, c) / n.getEntry(r, c));
/*  728:     */       }
/*  729:     */     }
/*  730:1078 */     return new Array2DRowRealMatrix(d, false);
/*  731:     */   }
/*  732:     */   
/*  733:     */   private static RealMatrix selectColumns(RealMatrix m, int[] cols)
/*  734:     */   {
/*  735:1087 */     double[][] d = new double[m.getRowDimension()][cols.length];
/*  736:1088 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  737:1089 */       for (int c = 0; c < cols.length; c++) {
/*  738:1090 */         d[r][c] = m.getEntry(r, cols[c]);
/*  739:     */       }
/*  740:     */     }
/*  741:1093 */     return new Array2DRowRealMatrix(d, false);
/*  742:     */   }
/*  743:     */   
/*  744:     */   private static RealMatrix triu(RealMatrix m, int k)
/*  745:     */   {
/*  746:1102 */     double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
/*  747:1103 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  748:1104 */       for (int c = 0; c < m.getColumnDimension(); c++) {
/*  749:1105 */         d[r][c] = (r <= c - k ? m.getEntry(r, c) : 0.0D);
/*  750:     */       }
/*  751:     */     }
/*  752:1108 */     return new Array2DRowRealMatrix(d, false);
/*  753:     */   }
/*  754:     */   
/*  755:     */   private static RealMatrix sumRows(RealMatrix m)
/*  756:     */   {
/*  757:1116 */     double[][] d = new double[1][m.getColumnDimension()];
/*  758:1117 */     for (int c = 0; c < m.getColumnDimension(); c++)
/*  759:     */     {
/*  760:1118 */       double sum = 0.0D;
/*  761:1119 */       for (int r = 0; r < m.getRowDimension(); r++) {
/*  762:1120 */         sum += m.getEntry(r, c);
/*  763:     */       }
/*  764:1122 */       d[0][c] = sum;
/*  765:     */     }
/*  766:1124 */     return new Array2DRowRealMatrix(d, false);
/*  767:     */   }
/*  768:     */   
/*  769:     */   private static RealMatrix diag(RealMatrix m)
/*  770:     */   {
/*  771:1133 */     if (m.getColumnDimension() == 1)
/*  772:     */     {
/*  773:1134 */       double[][] d = new double[m.getRowDimension()][m.getRowDimension()];
/*  774:1135 */       for (int i = 0; i < m.getRowDimension(); i++) {
/*  775:1136 */         d[i][i] = m.getEntry(i, 0);
/*  776:     */       }
/*  777:1138 */       return new Array2DRowRealMatrix(d, false);
/*  778:     */     }
/*  779:1140 */     double[][] d = new double[m.getRowDimension()][1];
/*  780:1141 */     for (int i = 0; i < m.getColumnDimension(); i++) {
/*  781:1142 */       d[i][0] = m.getEntry(i, i);
/*  782:     */     }
/*  783:1144 */     return new Array2DRowRealMatrix(d, false);
/*  784:     */   }
/*  785:     */   
/*  786:     */   private static void copyColumn(RealMatrix m1, int col1, RealMatrix m2, int col2)
/*  787:     */   {
/*  788:1157 */     for (int i = 0; i < m1.getRowDimension(); i++) {
/*  789:1158 */       m2.setEntry(i, col2, m1.getEntry(i, col1));
/*  790:     */     }
/*  791:     */   }
/*  792:     */   
/*  793:     */   private static RealMatrix ones(int n, int m)
/*  794:     */   {
/*  795:1168 */     double[][] d = new double[n][m];
/*  796:1169 */     for (int r = 0; r < n; r++) {
/*  797:1170 */       Arrays.fill(d[r], 1.0D);
/*  798:     */     }
/*  799:1172 */     return new Array2DRowRealMatrix(d, false);
/*  800:     */   }
/*  801:     */   
/*  802:     */   private static RealMatrix eye(int n, int m)
/*  803:     */   {
/*  804:1181 */     double[][] d = new double[n][m];
/*  805:1182 */     for (int r = 0; r < n; r++) {
/*  806:1183 */       if (r < m) {
/*  807:1184 */         d[r][r] = 1.0D;
/*  808:     */       }
/*  809:     */     }
/*  810:1187 */     return new Array2DRowRealMatrix(d, false);
/*  811:     */   }
/*  812:     */   
/*  813:     */   private static RealMatrix zeros(int n, int m)
/*  814:     */   {
/*  815:1196 */     return new Array2DRowRealMatrix(n, m);
/*  816:     */   }
/*  817:     */   
/*  818:     */   private static RealMatrix repmat(RealMatrix mat, int n, int m)
/*  819:     */   {
/*  820:1206 */     int rd = mat.getRowDimension();
/*  821:1207 */     int cd = mat.getColumnDimension();
/*  822:1208 */     double[][] d = new double[n * rd][m * cd];
/*  823:1209 */     for (int r = 0; r < n * rd; r++) {
/*  824:1210 */       for (int c = 0; c < m * cd; c++) {
/*  825:1211 */         d[r][c] = mat.getEntry(r % rd, c % cd);
/*  826:     */       }
/*  827:     */     }
/*  828:1214 */     return new Array2DRowRealMatrix(d, false);
/*  829:     */   }
/*  830:     */   
/*  831:     */   private static RealMatrix sequence(double start, double end, double step)
/*  832:     */   {
/*  833:1224 */     int size = (int)((end - start) / step + 1.0D);
/*  834:1225 */     double[][] d = new double[size][1];
/*  835:1226 */     double value = start;
/*  836:1227 */     for (int r = 0; r < size; r++)
/*  837:     */     {
/*  838:1228 */       d[r][0] = value;
/*  839:1229 */       value += step;
/*  840:     */     }
/*  841:1231 */     return new Array2DRowRealMatrix(d, false);
/*  842:     */   }
/*  843:     */   
/*  844:     */   private static double max(RealMatrix m)
/*  845:     */   {
/*  846:1239 */     double max = -1.797693134862316E+307D;
/*  847:1240 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  848:1241 */       for (int c = 0; c < m.getColumnDimension(); c++)
/*  849:     */       {
/*  850:1242 */         double e = m.getEntry(r, c);
/*  851:1243 */         if (max < e) {
/*  852:1244 */           max = e;
/*  853:     */         }
/*  854:     */       }
/*  855:     */     }
/*  856:1248 */     return max;
/*  857:     */   }
/*  858:     */   
/*  859:     */   private static double min(RealMatrix m)
/*  860:     */   {
/*  861:1256 */     double min = 1.7976931348623157E+308D;
/*  862:1257 */     for (int r = 0; r < m.getRowDimension(); r++) {
/*  863:1258 */       for (int c = 0; c < m.getColumnDimension(); c++)
/*  864:     */       {
/*  865:1259 */         double e = m.getEntry(r, c);
/*  866:1260 */         if (min > e) {
/*  867:1261 */           min = e;
/*  868:     */         }
/*  869:     */       }
/*  870:     */     }
/*  871:1265 */     return min;
/*  872:     */   }
/*  873:     */   
/*  874:     */   private static double max(double[] m)
/*  875:     */   {
/*  876:1273 */     double max = -1.797693134862316E+307D;
/*  877:1274 */     for (int r = 0; r < m.length; r++) {
/*  878:1275 */       if (max < m[r]) {
/*  879:1276 */         max = m[r];
/*  880:     */       }
/*  881:     */     }
/*  882:1279 */     return max;
/*  883:     */   }
/*  884:     */   
/*  885:     */   private static double min(double[] m)
/*  886:     */   {
/*  887:1287 */     double min = 1.7976931348623157E+308D;
/*  888:1288 */     for (int r = 0; r < m.length; r++) {
/*  889:1289 */       if (min > m[r]) {
/*  890:1290 */         min = m[r];
/*  891:     */       }
/*  892:     */     }
/*  893:1293 */     return min;
/*  894:     */   }
/*  895:     */   
/*  896:     */   private static int[] inverse(int[] indices)
/*  897:     */   {
/*  898:1301 */     int[] inverse = new int[indices.length];
/*  899:1302 */     for (int i = 0; i < indices.length; i++) {
/*  900:1303 */       inverse[indices[i]] = i;
/*  901:     */     }
/*  902:1305 */     return inverse;
/*  903:     */   }
/*  904:     */   
/*  905:     */   private static int[] reverse(int[] indices)
/*  906:     */   {
/*  907:1313 */     int[] reverse = new int[indices.length];
/*  908:1314 */     for (int i = 0; i < indices.length; i++) {
/*  909:1315 */       reverse[i] = indices[(indices.length - i - 1)];
/*  910:     */     }
/*  911:1317 */     return reverse;
/*  912:     */   }
/*  913:     */   
/*  914:     */   private double[] randn(int size)
/*  915:     */   {
/*  916:1325 */     double[] randn = new double[size];
/*  917:1326 */     for (int i = 0; i < size; i++) {
/*  918:1327 */       randn[i] = this.random.nextGaussian();
/*  919:     */     }
/*  920:1329 */     return randn;
/*  921:     */   }
/*  922:     */   
/*  923:     */   private RealMatrix randn1(int size, int popSize)
/*  924:     */   {
/*  925:1338 */     double[][] d = new double[size][popSize];
/*  926:1339 */     for (int r = 0; r < size; r++) {
/*  927:1340 */       for (int c = 0; c < popSize; c++) {
/*  928:1341 */         d[r][c] = this.random.nextGaussian();
/*  929:     */       }
/*  930:     */     }
/*  931:1344 */     return new Array2DRowRealMatrix(d, false);
/*  932:     */   }
/*  933:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.CMAESOptimizer
 * JD-Core Version:    0.7.0.1
 */
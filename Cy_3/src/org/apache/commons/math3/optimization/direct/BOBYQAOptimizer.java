/*    1:     */ package org.apache.commons.math3.optimization.direct;
/*    2:     */ 
/*    3:     */ import java.util.Arrays;
/*    4:     */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*    5:     */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*    6:     */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*    7:     */ import org.apache.commons.math3.exception.OutOfRangeException;
/*    8:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*    9:     */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   10:     */ import org.apache.commons.math3.linear.ArrayRealVector;
/*   11:     */ import org.apache.commons.math3.linear.RealVector;
/*   12:     */ import org.apache.commons.math3.optimization.GoalType;
/*   13:     */ import org.apache.commons.math3.optimization.MultivariateOptimizer;
/*   14:     */ import org.apache.commons.math3.optimization.PointValuePair;
/*   15:     */ 
/*   16:     */ public class BOBYQAOptimizer
/*   17:     */   extends BaseAbstractMultivariateSimpleBoundsOptimizer<MultivariateFunction>
/*   18:     */   implements MultivariateOptimizer
/*   19:     */ {
/*   20:     */   public static final int MINIMUM_PROBLEM_DIMENSION = 2;
/*   21:     */   public static final double DEFAULT_INITIAL_RADIUS = 10.0D;
/*   22:     */   public static final double DEFAULT_STOPPING_RADIUS = 1.0E-008D;
/*   23:     */   private static final double ZERO = 0.0D;
/*   24:     */   private static final double ONE = 1.0D;
/*   25:     */   private static final double TWO = 2.0D;
/*   26:     */   private static final double TEN = 10.0D;
/*   27:     */   private static final double SIXTEEN = 16.0D;
/*   28:     */   private static final double TWO_HUNDRED_FIFTY = 250.0D;
/*   29:     */   private static final double MINUS_ONE = -1.0D;
/*   30:     */   private static final double HALF = 0.5D;
/*   31:     */   private static final double ONE_OVER_FOUR = 0.25D;
/*   32:     */   private static final double ONE_OVER_EIGHT = 0.125D;
/*   33:     */   private static final double ONE_OVER_TEN = 0.1D;
/*   34:     */   private static final double ONE_OVER_A_THOUSAND = 0.001D;
/*   35:     */   private final int numberOfInterpolationPoints;
/*   36:     */   private double initialTrustRegionRadius;
/*   37:     */   private final double stoppingTrustRegionRadius;
/*   38:     */   private boolean isMinimize;
/*   39:     */   private ArrayRealVector currentBest;
/*   40:     */   private double[] boundDifference;
/*   41:     */   private int trustRegionCenterInterpolationPointIndex;
/*   42:     */   private Array2DRowRealMatrix bMatrix;
/*   43:     */   private Array2DRowRealMatrix zMatrix;
/*   44:     */   private Array2DRowRealMatrix interpolationPoints;
/*   45:     */   private ArrayRealVector originShift;
/*   46:     */   private ArrayRealVector fAtInterpolationPoints;
/*   47:     */   private ArrayRealVector trustRegionCenterOffset;
/*   48:     */   private ArrayRealVector gradientAtTrustRegionCenter;
/*   49:     */   private ArrayRealVector lowerDifference;
/*   50:     */   private ArrayRealVector upperDifference;
/*   51:     */   private ArrayRealVector modelSecondDerivativesParameters;
/*   52:     */   private ArrayRealVector newPoint;
/*   53:     */   private ArrayRealVector alternativeNewPoint;
/*   54:     */   private ArrayRealVector trialStepPoint;
/*   55:     */   private ArrayRealVector lagrangeValuesAtNewPoint;
/*   56:     */   private ArrayRealVector modelSecondDerivativesValues;
/*   57:     */   
/*   58:     */   public BOBYQAOptimizer(int numberOfInterpolationPoints)
/*   59:     */   {
/*   60: 212 */     this(numberOfInterpolationPoints, 10.0D, 1.0E-008D);
/*   61:     */   }
/*   62:     */   
/*   63:     */   public BOBYQAOptimizer(int numberOfInterpolationPoints, double initialTrustRegionRadius, double stoppingTrustRegionRadius)
/*   64:     */   {
/*   65: 228 */     this.numberOfInterpolationPoints = numberOfInterpolationPoints;
/*   66: 229 */     this.initialTrustRegionRadius = initialTrustRegionRadius;
/*   67: 230 */     this.stoppingTrustRegionRadius = stoppingTrustRegionRadius;
/*   68:     */   }
/*   69:     */   
/*   70:     */   protected PointValuePair doOptimize()
/*   71:     */   {
/*   72: 236 */     double[] lowerBound = getLowerBound();
/*   73: 237 */     double[] upperBound = getUpperBound();
/*   74:     */     
/*   75:     */ 
/*   76: 240 */     setup(lowerBound, upperBound);
/*   77:     */     
/*   78: 242 */     this.isMinimize = (getGoalType() == GoalType.MINIMIZE);
/*   79: 243 */     this.currentBest = new ArrayRealVector(getStartPoint());
/*   80:     */     
/*   81: 245 */     double value = bobyqa(lowerBound, upperBound);
/*   82:     */     
/*   83: 247 */     return new PointValuePair(this.currentBest.getDataRef(), this.isMinimize ? value : -value);
/*   84:     */   }
/*   85:     */   
/*   86:     */   private double bobyqa(double[] lowerBound, double[] upperBound)
/*   87:     */   {
/*   88: 288 */     printMethod();
/*   89:     */     
/*   90: 290 */     int n = this.currentBest.getDimension();
/*   91: 299 */     for (int j = 0; j < n; j++)
/*   92:     */     {
/*   93: 300 */       double boundDiff = this.boundDifference[j];
/*   94: 301 */       this.lowerDifference.setEntry(j, lowerBound[j] - this.currentBest.getEntry(j));
/*   95: 302 */       this.upperDifference.setEntry(j, upperBound[j] - this.currentBest.getEntry(j));
/*   96: 303 */       if (this.lowerDifference.getEntry(j) >= -this.initialTrustRegionRadius)
/*   97:     */       {
/*   98: 304 */         if (this.lowerDifference.getEntry(j) >= 0.0D)
/*   99:     */         {
/*  100: 305 */           this.currentBest.setEntry(j, lowerBound[j]);
/*  101: 306 */           this.lowerDifference.setEntry(j, 0.0D);
/*  102: 307 */           this.upperDifference.setEntry(j, boundDiff);
/*  103:     */         }
/*  104:     */         else
/*  105:     */         {
/*  106: 309 */           this.currentBest.setEntry(j, lowerBound[j] + this.initialTrustRegionRadius);
/*  107: 310 */           this.lowerDifference.setEntry(j, -this.initialTrustRegionRadius);
/*  108:     */           
/*  109: 312 */           double deltaOne = upperBound[j] - this.currentBest.getEntry(j);
/*  110: 313 */           this.upperDifference.setEntry(j, Math.max(deltaOne, this.initialTrustRegionRadius));
/*  111:     */         }
/*  112:     */       }
/*  113: 315 */       else if (this.upperDifference.getEntry(j) <= this.initialTrustRegionRadius) {
/*  114: 316 */         if (this.upperDifference.getEntry(j) <= 0.0D)
/*  115:     */         {
/*  116: 317 */           this.currentBest.setEntry(j, upperBound[j]);
/*  117: 318 */           this.lowerDifference.setEntry(j, -boundDiff);
/*  118: 319 */           this.upperDifference.setEntry(j, 0.0D);
/*  119:     */         }
/*  120:     */         else
/*  121:     */         {
/*  122: 321 */           this.currentBest.setEntry(j, upperBound[j] - this.initialTrustRegionRadius);
/*  123:     */           
/*  124: 323 */           double deltaOne = lowerBound[j] - this.currentBest.getEntry(j);
/*  125: 324 */           double deltaTwo = -this.initialTrustRegionRadius;
/*  126: 325 */           this.lowerDifference.setEntry(j, Math.min(deltaOne, deltaTwo));
/*  127: 326 */           this.upperDifference.setEntry(j, this.initialTrustRegionRadius);
/*  128:     */         }
/*  129:     */       }
/*  130:     */     }
/*  131: 333 */     return bobyqb(lowerBound, upperBound);
/*  132:     */   }
/*  133:     */   
/*  134:     */   private double bobyqb(double[] lowerBound, double[] upperBound)
/*  135:     */   {
/*  136: 377 */     printMethod();
/*  137:     */     
/*  138: 379 */     int n = this.currentBest.getDimension();
/*  139: 380 */     int npt = this.numberOfInterpolationPoints;
/*  140: 381 */     int np = n + 1;
/*  141: 382 */     int nptm = npt - np;
/*  142: 383 */     int nh = n * np / 2;
/*  143:     */     
/*  144: 385 */     ArrayRealVector work1 = new ArrayRealVector(n);
/*  145: 386 */     ArrayRealVector work2 = new ArrayRealVector(npt);
/*  146: 387 */     ArrayRealVector work3 = new ArrayRealVector(npt);
/*  147:     */     
/*  148: 389 */     double cauchy = (0.0D / 0.0D);
/*  149: 390 */     double alpha = (0.0D / 0.0D);
/*  150: 391 */     double dsq = (0.0D / 0.0D);
/*  151: 392 */     double crvmin = (0.0D / 0.0D);
/*  152:     */     
/*  153:     */ 
/*  154:     */ 
/*  155:     */ 
/*  156:     */ 
/*  157:     */ 
/*  158:     */ 
/*  159:     */ 
/*  160:     */ 
/*  161:     */ 
/*  162:     */ 
/*  163:     */ 
/*  164:     */ 
/*  165: 406 */     this.trustRegionCenterInterpolationPointIndex = 0;
/*  166:     */     
/*  167: 408 */     prelim(lowerBound, upperBound);
/*  168: 409 */     double xoptsq = 0.0D;
/*  169: 410 */     for (int i = 0; i < n; i++)
/*  170:     */     {
/*  171: 411 */       this.trustRegionCenterOffset.setEntry(i, this.interpolationPoints.getEntry(this.trustRegionCenterInterpolationPointIndex, i));
/*  172:     */       
/*  173: 413 */       double deltaOne = this.trustRegionCenterOffset.getEntry(i);
/*  174: 414 */       xoptsq += deltaOne * deltaOne;
/*  175:     */     }
/*  176: 416 */     double fsave = this.fAtInterpolationPoints.getEntry(0);
/*  177: 417 */     int kbase = 0;
/*  178:     */     
/*  179:     */ 
/*  180:     */ 
/*  181: 421 */     int ntrits = 0;
/*  182: 422 */     int itest = 0;
/*  183: 423 */     int knew = 0;
/*  184: 424 */     int nfsav = getEvaluations();
/*  185: 425 */     double rho = this.initialTrustRegionRadius;
/*  186: 426 */     double delta = rho;
/*  187: 427 */     double diffa = 0.0D;
/*  188: 428 */     double diffb = 0.0D;
/*  189: 429 */     double diffc = 0.0D;
/*  190: 430 */     double f = 0.0D;
/*  191: 431 */     double beta = 0.0D;
/*  192: 432 */     double adelt = 0.0D;
/*  193: 433 */     double denom = 0.0D;
/*  194: 434 */     double ratio = 0.0D;
/*  195: 435 */     double dnorm = 0.0D;
/*  196: 436 */     double scaden = 0.0D;
/*  197: 437 */     double biglsq = 0.0D;
/*  198: 438 */     double distsq = 0.0D;
/*  199:     */     
/*  200:     */ 
/*  201:     */ 
/*  202:     */ 
/*  203: 443 */     int state = 20;
/*  204:     */     for (;;)
/*  205:     */     {
/*  206: 444 */       switch (state)
/*  207:     */       {
/*  208:     */       case 20: 
/*  209: 446 */         printState(20);
/*  210: 447 */         if (this.trustRegionCenterInterpolationPointIndex != 0)
/*  211:     */         {
/*  212: 448 */           int ih = 0;
/*  213: 449 */           for (int j = 0; j < n; j++) {
/*  214: 450 */             for (int i = 0; i <= j; i++)
/*  215:     */             {
/*  216: 451 */               if (i < j) {
/*  217: 452 */                 this.gradientAtTrustRegionCenter.setEntry(j, this.gradientAtTrustRegionCenter.getEntry(j) + this.modelSecondDerivativesValues.getEntry(ih) * this.trustRegionCenterOffset.getEntry(i));
/*  218:     */               }
/*  219: 454 */               this.gradientAtTrustRegionCenter.setEntry(i, this.gradientAtTrustRegionCenter.getEntry(i) + this.modelSecondDerivativesValues.getEntry(ih) * this.trustRegionCenterOffset.getEntry(j));
/*  220: 455 */               ih++;
/*  221:     */             }
/*  222:     */           }
/*  223: 458 */           if (getEvaluations() > npt) {
/*  224: 459 */             for (int k = 0; k < npt; k++)
/*  225:     */             {
/*  226: 460 */               double temp = 0.0D;
/*  227: 461 */               for (int j = 0; j < n; j++) {
/*  228: 462 */                 temp += this.interpolationPoints.getEntry(k, j) * this.trustRegionCenterOffset.getEntry(j);
/*  229:     */               }
/*  230: 464 */               temp *= this.modelSecondDerivativesParameters.getEntry(k);
/*  231: 465 */               for (int i = 0; i < n; i++) {
/*  232: 466 */                 this.gradientAtTrustRegionCenter.setEntry(i, this.gradientAtTrustRegionCenter.getEntry(i) + temp * this.interpolationPoints.getEntry(k, i));
/*  233:     */               }
/*  234:     */             }
/*  235:     */           }
/*  236:     */         }
/*  237:     */       case 60: 
/*  238: 482 */         printState(60);
/*  239: 483 */         ArrayRealVector gnew = new ArrayRealVector(n);
/*  240: 484 */         ArrayRealVector xbdi = new ArrayRealVector(n);
/*  241: 485 */         ArrayRealVector s = new ArrayRealVector(n);
/*  242: 486 */         ArrayRealVector hs = new ArrayRealVector(n);
/*  243: 487 */         ArrayRealVector hred = new ArrayRealVector(n);
/*  244:     */         
/*  245: 489 */         double[] dsqCrvmin = trsbox(delta, gnew, xbdi, s, hs, hred);
/*  246:     */         
/*  247: 491 */         dsq = dsqCrvmin[0];
/*  248: 492 */         crvmin = dsqCrvmin[1];
/*  249:     */         
/*  250:     */ 
/*  251: 495 */         double deltaOne = delta;
/*  252: 496 */         double deltaTwo = Math.sqrt(dsq);
/*  253: 497 */         dnorm = Math.min(deltaOne, deltaTwo);
/*  254: 498 */         if (dnorm < 0.5D * rho)
/*  255:     */         {
/*  256: 499 */           ntrits = -1;
/*  257:     */           
/*  258: 501 */           deltaOne = 10.0D * rho;
/*  259: 502 */           distsq = deltaOne * deltaOne;
/*  260: 503 */           if (getEvaluations() <= nfsav + 2)
/*  261:     */           {
/*  262: 504 */             state = 650;
/*  263:     */           }
/*  264:     */           else
/*  265:     */           {
/*  266: 514 */             deltaOne = Math.max(diffa, diffb);
/*  267: 515 */             double errbig = Math.max(deltaOne, diffc);
/*  268: 516 */             double frhosq = rho * 0.125D * rho;
/*  269: 517 */             if ((crvmin > 0.0D) && (errbig > frhosq * crvmin))
/*  270:     */             {
/*  271: 519 */               state = 650;
/*  272:     */             }
/*  273:     */             else
/*  274:     */             {
/*  275: 521 */               double bdtol = errbig / rho;
/*  276: 522 */               for (int j = 0; j < n; j++)
/*  277:     */               {
/*  278: 523 */                 double bdtest = bdtol;
/*  279: 524 */                 if (this.newPoint.getEntry(j) == this.lowerDifference.getEntry(j)) {
/*  280: 525 */                   bdtest = work1.getEntry(j);
/*  281:     */                 }
/*  282: 527 */                 if (this.newPoint.getEntry(j) == this.upperDifference.getEntry(j)) {
/*  283: 528 */                   bdtest = -work1.getEntry(j);
/*  284:     */                 }
/*  285: 530 */                 if (bdtest < bdtol)
/*  286:     */                 {
/*  287: 531 */                   double curv = this.modelSecondDerivativesValues.getEntry((j + j * j) / 2);
/*  288: 532 */                   for (int k = 0; k < npt; k++)
/*  289:     */                   {
/*  290: 534 */                     double d1 = this.interpolationPoints.getEntry(k, j);
/*  291: 535 */                     curv += this.modelSecondDerivativesParameters.getEntry(k) * (d1 * d1);
/*  292:     */                   }
/*  293: 537 */                   bdtest += 0.5D * curv * rho;
/*  294: 538 */                   if (bdtest < bdtol)
/*  295:     */                   {
/*  296: 539 */                     state = 650; break;
/*  297:     */                   }
/*  298:     */                 }
/*  299:     */               }
/*  300: 544 */               state = 680;
/*  301:     */             }
/*  302:     */           }
/*  303:     */         }
/*  304:     */         else
/*  305:     */         {
/*  306: 546 */           ntrits++;
/*  307:     */         }
/*  308:     */         break;
/*  309:     */       case 90: 
/*  310: 556 */         printState(90);
/*  311: 557 */         if (dsq <= xoptsq * 0.001D)
/*  312:     */         {
/*  313: 558 */           double fracsq = xoptsq * 0.25D;
/*  314: 559 */           double sumpq = 0.0D;
/*  315: 562 */           for (int k = 0; k < npt; k++)
/*  316:     */           {
/*  317: 563 */             sumpq += this.modelSecondDerivativesParameters.getEntry(k);
/*  318: 564 */             double sum = -0.5D * xoptsq;
/*  319: 565 */             for (int i = 0; i < n; i++) {
/*  320: 566 */               sum += this.interpolationPoints.getEntry(k, i) * this.trustRegionCenterOffset.getEntry(i);
/*  321:     */             }
/*  322: 569 */             work2.setEntry(k, sum);
/*  323: 570 */             double temp = fracsq - 0.5D * sum;
/*  324: 571 */             for (int i = 0; i < n; i++)
/*  325:     */             {
/*  326: 572 */               work1.setEntry(i, this.bMatrix.getEntry(k, i));
/*  327: 573 */               this.lagrangeValuesAtNewPoint.setEntry(i, sum * this.interpolationPoints.getEntry(k, i) + temp * this.trustRegionCenterOffset.getEntry(i));
/*  328: 574 */               int ip = npt + i;
/*  329: 575 */               for (int j = 0; j <= i; j++) {
/*  330: 576 */                 this.bMatrix.setEntry(ip, j, this.bMatrix.getEntry(ip, j) + work1.getEntry(i) * this.lagrangeValuesAtNewPoint.getEntry(j) + this.lagrangeValuesAtNewPoint.getEntry(i) * work1.getEntry(j));
/*  331:     */               }
/*  332:     */             }
/*  333:     */           }
/*  334: 586 */           for (int m = 0; m < nptm; m++)
/*  335:     */           {
/*  336: 587 */             double sumz = 0.0D;
/*  337: 588 */             double sumw = 0.0D;
/*  338: 589 */             for (int k = 0; k < npt; k++)
/*  339:     */             {
/*  340: 590 */               sumz += this.zMatrix.getEntry(k, m);
/*  341: 591 */               this.lagrangeValuesAtNewPoint.setEntry(k, work2.getEntry(k) * this.zMatrix.getEntry(k, m));
/*  342: 592 */               sumw += this.lagrangeValuesAtNewPoint.getEntry(k);
/*  343:     */             }
/*  344: 594 */             for (int j = 0; j < n; j++)
/*  345:     */             {
/*  346: 595 */               double sum = (fracsq * sumz - 0.5D * sumw) * this.trustRegionCenterOffset.getEntry(j);
/*  347: 596 */               for (int k = 0; k < npt; k++) {
/*  348: 597 */                 sum += this.lagrangeValuesAtNewPoint.getEntry(k) * this.interpolationPoints.getEntry(k, j);
/*  349:     */               }
/*  350: 599 */               work1.setEntry(j, sum);
/*  351: 600 */               for (int k = 0; k < npt; k++) {
/*  352: 601 */                 this.bMatrix.setEntry(k, j, this.bMatrix.getEntry(k, j) + sum * this.zMatrix.getEntry(k, m));
/*  353:     */               }
/*  354:     */             }
/*  355: 606 */             for (int i = 0; i < n; i++)
/*  356:     */             {
/*  357: 607 */               int ip = i + npt;
/*  358: 608 */               double temp = work1.getEntry(i);
/*  359: 609 */               for (int j = 0; j <= i; j++) {
/*  360: 610 */                 this.bMatrix.setEntry(ip, j, this.bMatrix.getEntry(ip, j) + temp * work1.getEntry(j));
/*  361:     */               }
/*  362:     */             }
/*  363:     */           }
/*  364: 620 */           int ih = 0;
/*  365: 621 */           for (int j = 0; j < n; j++)
/*  366:     */           {
/*  367: 622 */             work1.setEntry(j, -0.5D * sumpq * this.trustRegionCenterOffset.getEntry(j));
/*  368: 623 */             for (int k = 0; k < npt; k++)
/*  369:     */             {
/*  370: 624 */               work1.setEntry(j, work1.getEntry(j) + this.modelSecondDerivativesParameters.getEntry(k) * this.interpolationPoints.getEntry(k, j));
/*  371: 625 */               this.interpolationPoints.setEntry(k, j, this.interpolationPoints.getEntry(k, j) - this.trustRegionCenterOffset.getEntry(j));
/*  372:     */             }
/*  373: 627 */             for (int i = 0; i <= j; i++)
/*  374:     */             {
/*  375: 628 */               this.modelSecondDerivativesValues.setEntry(ih, this.modelSecondDerivativesValues.getEntry(ih) + work1.getEntry(i) * this.trustRegionCenterOffset.getEntry(j) + this.trustRegionCenterOffset.getEntry(i) * work1.getEntry(j));
/*  376:     */               
/*  377:     */ 
/*  378:     */ 
/*  379: 632 */               this.bMatrix.setEntry(npt + i, j, this.bMatrix.getEntry(npt + j, i));
/*  380: 633 */               ih++;
/*  381:     */             }
/*  382:     */           }
/*  383: 636 */           for (int i = 0; i < n; i++)
/*  384:     */           {
/*  385: 637 */             this.originShift.setEntry(i, this.originShift.getEntry(i) + this.trustRegionCenterOffset.getEntry(i));
/*  386: 638 */             this.newPoint.setEntry(i, this.newPoint.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/*  387: 639 */             this.lowerDifference.setEntry(i, this.lowerDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/*  388: 640 */             this.upperDifference.setEntry(i, this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/*  389: 641 */             this.trustRegionCenterOffset.setEntry(i, 0.0D);
/*  390:     */           }
/*  391: 643 */           xoptsq = 0.0D;
/*  392:     */         }
/*  393: 645 */         if (ntrits == 0) {
/*  394: 646 */           state = 210;
/*  395:     */         } else {
/*  396: 648 */           state = 230;
/*  397:     */         }
/*  398:     */         break;
/*  399:     */       case 210: 
/*  400: 661 */         printState(210);
/*  401:     */         
/*  402:     */ 
/*  403:     */ 
/*  404:     */ 
/*  405:     */ 
/*  406:     */ 
/*  407:     */ 
/*  408:     */ 
/*  409:     */ 
/*  410:     */ 
/*  411:     */ 
/*  412: 673 */         double[] alphaCauchy = altmov(knew, adelt);
/*  413: 674 */         alpha = alphaCauchy[0];
/*  414: 675 */         cauchy = alphaCauchy[1];
/*  415: 677 */         for (int i = 0; i < n; i++) {
/*  416: 678 */           this.trialStepPoint.setEntry(i, this.newPoint.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/*  417:     */         }
/*  418:     */       case 230: 
/*  419: 687 */         printState(230);
/*  420: 688 */         for (int k = 0; k < npt; k++)
/*  421:     */         {
/*  422: 689 */           double suma = 0.0D;
/*  423: 690 */           double sumb = 0.0D;
/*  424: 691 */           double sum = 0.0D;
/*  425: 692 */           for (int j = 0; j < n; j++)
/*  426:     */           {
/*  427: 693 */             suma += this.interpolationPoints.getEntry(k, j) * this.trialStepPoint.getEntry(j);
/*  428: 694 */             sumb += this.interpolationPoints.getEntry(k, j) * this.trustRegionCenterOffset.getEntry(j);
/*  429: 695 */             sum += this.bMatrix.getEntry(k, j) * this.trialStepPoint.getEntry(j);
/*  430:     */           }
/*  431: 697 */           work3.setEntry(k, suma * (0.5D * suma + sumb));
/*  432: 698 */           this.lagrangeValuesAtNewPoint.setEntry(k, sum);
/*  433: 699 */           work2.setEntry(k, suma);
/*  434:     */         }
/*  435: 701 */         beta = 0.0D;
/*  436: 702 */         for (int m = 0; m < nptm; m++)
/*  437:     */         {
/*  438: 703 */           double sum = 0.0D;
/*  439: 704 */           for (int k = 0; k < npt; k++) {
/*  440: 705 */             sum += this.zMatrix.getEntry(k, m) * work3.getEntry(k);
/*  441:     */           }
/*  442: 707 */           beta -= sum * sum;
/*  443: 708 */           for (int k = 0; k < npt; k++) {
/*  444: 709 */             this.lagrangeValuesAtNewPoint.setEntry(k, this.lagrangeValuesAtNewPoint.getEntry(k) + sum * this.zMatrix.getEntry(k, m));
/*  445:     */           }
/*  446:     */         }
/*  447: 712 */         dsq = 0.0D;
/*  448: 713 */         double bsum = 0.0D;
/*  449: 714 */         double dx = 0.0D;
/*  450: 715 */         for (int j = 0; j < n; j++)
/*  451:     */         {
/*  452: 717 */           double d1 = this.trialStepPoint.getEntry(j);
/*  453: 718 */           dsq += d1 * d1;
/*  454: 719 */           double sum = 0.0D;
/*  455: 720 */           for (int k = 0; k < npt; k++) {
/*  456: 721 */             sum += work3.getEntry(k) * this.bMatrix.getEntry(k, j);
/*  457:     */           }
/*  458: 723 */           bsum += sum * this.trialStepPoint.getEntry(j);
/*  459: 724 */           int jp = npt + j;
/*  460: 725 */           for (int i = 0; i < n; i++) {
/*  461: 726 */             sum += this.bMatrix.getEntry(jp, i) * this.trialStepPoint.getEntry(i);
/*  462:     */           }
/*  463: 728 */           this.lagrangeValuesAtNewPoint.setEntry(jp, sum);
/*  464: 729 */           bsum += sum * this.trialStepPoint.getEntry(j);
/*  465: 730 */           dx += this.trialStepPoint.getEntry(j) * this.trustRegionCenterOffset.getEntry(j);
/*  466:     */         }
/*  467: 733 */         beta = dx * dx + dsq * (xoptsq + dx + dx + 0.5D * dsq) + beta - bsum;
/*  468:     */         
/*  469:     */ 
/*  470:     */ 
/*  471: 737 */         this.lagrangeValuesAtNewPoint.setEntry(this.trustRegionCenterInterpolationPointIndex, this.lagrangeValuesAtNewPoint.getEntry(this.trustRegionCenterInterpolationPointIndex) + 1.0D);
/*  472: 744 */         if (ntrits == 0)
/*  473:     */         {
/*  474: 746 */           double d1 = this.lagrangeValuesAtNewPoint.getEntry(knew);
/*  475: 747 */           denom = d1 * d1 + alpha * beta;
/*  476: 748 */           if ((denom < cauchy) && (cauchy > 0.0D))
/*  477:     */           {
/*  478: 749 */             for (int i = 0; i < n; i++)
/*  479:     */             {
/*  480: 750 */               this.newPoint.setEntry(i, this.alternativeNewPoint.getEntry(i));
/*  481: 751 */               this.trialStepPoint.setEntry(i, this.newPoint.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/*  482:     */             }
/*  483: 753 */             cauchy = 0.0D;
/*  484: 754 */             state = 230; continue;
/*  485:     */           }
/*  486:     */         }
/*  487:     */         else
/*  488:     */         {
/*  489: 763 */           double delsq = delta * delta;
/*  490: 764 */           scaden = 0.0D;
/*  491: 765 */           biglsq = 0.0D;
/*  492: 766 */           knew = 0;
/*  493: 767 */           for (int k = 0; k < npt; k++) {
/*  494: 768 */             if (k != this.trustRegionCenterInterpolationPointIndex)
/*  495:     */             {
/*  496: 771 */               double hdiag = 0.0D;
/*  497: 772 */               for (int m = 0; m < nptm; m++)
/*  498:     */               {
/*  499: 774 */                 double d1 = this.zMatrix.getEntry(k, m);
/*  500: 775 */                 hdiag += d1 * d1;
/*  501:     */               }
/*  502: 778 */               double d2 = this.lagrangeValuesAtNewPoint.getEntry(k);
/*  503: 779 */               double den = beta * hdiag + d2 * d2;
/*  504: 780 */               distsq = 0.0D;
/*  505: 781 */               for (int j = 0; j < n; j++)
/*  506:     */               {
/*  507: 783 */                 double d3 = this.interpolationPoints.getEntry(k, j) - this.trustRegionCenterOffset.getEntry(j);
/*  508: 784 */                 distsq += d3 * d3;
/*  509:     */               }
/*  510: 788 */               double d4 = distsq / delsq;
/*  511: 789 */               double temp = Math.max(1.0D, d4 * d4);
/*  512: 790 */               if (temp * den > scaden)
/*  513:     */               {
/*  514: 791 */                 scaden = temp * den;
/*  515: 792 */                 knew = k;
/*  516: 793 */                 denom = den;
/*  517:     */               }
/*  518: 797 */               double d5 = this.lagrangeValuesAtNewPoint.getEntry(k);
/*  519: 798 */               biglsq = Math.max(biglsq, temp * (d5 * d5));
/*  520:     */             }
/*  521:     */           }
/*  522:     */         }
/*  523:     */       case 360: 
/*  524: 810 */         printState(360);
/*  525: 811 */         for (int i = 0; i < n; i++)
/*  526:     */         {
/*  527: 814 */           double d3 = lowerBound[i];
/*  528: 815 */           double d4 = this.originShift.getEntry(i) + this.newPoint.getEntry(i);
/*  529: 816 */           double d1 = Math.max(d3, d4);
/*  530: 817 */           double d2 = upperBound[i];
/*  531: 818 */           this.currentBest.setEntry(i, Math.min(d1, d2));
/*  532: 819 */           if (this.newPoint.getEntry(i) == this.lowerDifference.getEntry(i)) {
/*  533: 820 */             this.currentBest.setEntry(i, lowerBound[i]);
/*  534:     */           }
/*  535: 822 */           if (this.newPoint.getEntry(i) == this.upperDifference.getEntry(i)) {
/*  536: 823 */             this.currentBest.setEntry(i, upperBound[i]);
/*  537:     */           }
/*  538:     */         }
/*  539: 827 */         f = computeObjectiveValue(this.currentBest.toArray());
/*  540: 829 */         if (!this.isMinimize) {
/*  541: 830 */           f = -f;
/*  542:     */         }
/*  543: 831 */         if (ntrits == -1)
/*  544:     */         {
/*  545: 832 */           fsave = f;
/*  546: 833 */           state = 720;
/*  547:     */         }
/*  548:     */         else
/*  549:     */         {
/*  550: 839 */           double fopt = this.fAtInterpolationPoints.getEntry(this.trustRegionCenterInterpolationPointIndex);
/*  551: 840 */           double vquad = 0.0D;
/*  552: 841 */           int ih = 0;
/*  553: 842 */           for (int j = 0; j < n; j++)
/*  554:     */           {
/*  555: 843 */             vquad += this.trialStepPoint.getEntry(j) * this.gradientAtTrustRegionCenter.getEntry(j);
/*  556: 844 */             for (int i = 0; i <= j; i++)
/*  557:     */             {
/*  558: 845 */               double temp = this.trialStepPoint.getEntry(i) * this.trialStepPoint.getEntry(j);
/*  559: 846 */               if (i == j) {
/*  560: 847 */                 temp *= 0.5D;
/*  561:     */               }
/*  562: 849 */               vquad += this.modelSecondDerivativesValues.getEntry(ih) * temp;
/*  563: 850 */               ih++;
/*  564:     */             }
/*  565:     */           }
/*  566: 853 */           for (int k = 0; k < npt; k++)
/*  567:     */           {
/*  568: 855 */             double d1 = work2.getEntry(k);
/*  569: 856 */             double d2 = d1 * d1;
/*  570: 857 */             vquad += 0.5D * this.modelSecondDerivativesParameters.getEntry(k) * d2;
/*  571:     */           }
/*  572: 859 */           double diff = f - fopt - vquad;
/*  573: 860 */           diffc = diffb;
/*  574: 861 */           diffb = diffa;
/*  575: 862 */           diffa = Math.abs(diff);
/*  576: 863 */           if (dnorm > rho) {
/*  577: 864 */             nfsav = getEvaluations();
/*  578:     */           }
/*  579: 869 */           if (ntrits > 0)
/*  580:     */           {
/*  581: 870 */             if (vquad >= 0.0D) {
/*  582: 871 */               throw new MathIllegalStateException(LocalizedFormats.TRUST_REGION_STEP_FAILED, new Object[] { Double.valueOf(vquad) });
/*  583:     */             }
/*  584: 873 */             ratio = (f - fopt) / vquad;
/*  585: 874 */             double hDelta = 0.5D * delta;
/*  586: 875 */             if (ratio <= 0.1D) {
/*  587: 877 */               delta = Math.min(hDelta, dnorm);
/*  588: 878 */             } else if (ratio <= 0.7D) {
/*  589: 880 */               delta = Math.max(hDelta, dnorm);
/*  590:     */             } else {
/*  591: 883 */               delta = Math.max(hDelta, 2.0D * dnorm);
/*  592:     */             }
/*  593: 885 */             if (delta <= rho * 1.5D) {
/*  594: 886 */               delta = rho;
/*  595:     */             }
/*  596: 891 */             if (f < fopt)
/*  597:     */             {
/*  598: 892 */               int ksav = knew;
/*  599: 893 */               double densav = denom;
/*  600: 894 */               double delsq = delta * delta;
/*  601: 895 */               scaden = 0.0D;
/*  602: 896 */               biglsq = 0.0D;
/*  603: 897 */               knew = 0;
/*  604: 898 */               for (int k = 0; k < npt; k++)
/*  605:     */               {
/*  606: 899 */                 double hdiag = 0.0D;
/*  607: 900 */                 for (int m = 0; m < nptm; m++)
/*  608:     */                 {
/*  609: 902 */                   double d1 = this.zMatrix.getEntry(k, m);
/*  610: 903 */                   hdiag += d1 * d1;
/*  611:     */                 }
/*  612: 906 */                 double d1 = this.lagrangeValuesAtNewPoint.getEntry(k);
/*  613: 907 */                 double den = beta * hdiag + d1 * d1;
/*  614: 908 */                 distsq = 0.0D;
/*  615: 909 */                 for (int j = 0; j < n; j++)
/*  616:     */                 {
/*  617: 911 */                   double d2 = this.interpolationPoints.getEntry(k, j) - this.newPoint.getEntry(j);
/*  618: 912 */                   distsq += d2 * d2;
/*  619:     */                 }
/*  620: 916 */                 double d3 = distsq / delsq;
/*  621: 917 */                 double temp = Math.max(1.0D, d3 * d3);
/*  622: 918 */                 if (temp * den > scaden)
/*  623:     */                 {
/*  624: 919 */                   scaden = temp * den;
/*  625: 920 */                   knew = k;
/*  626: 921 */                   denom = den;
/*  627:     */                 }
/*  628: 925 */                 double d4 = this.lagrangeValuesAtNewPoint.getEntry(k);
/*  629: 926 */                 double d5 = temp * (d4 * d4);
/*  630: 927 */                 biglsq = Math.max(biglsq, d5);
/*  631:     */               }
/*  632: 929 */               if (scaden <= 0.5D * biglsq)
/*  633:     */               {
/*  634: 930 */                 knew = ksav;
/*  635: 931 */                 denom = densav;
/*  636:     */               }
/*  637:     */             }
/*  638:     */           }
/*  639: 939 */           update(beta, denom, knew);
/*  640:     */           
/*  641: 941 */           ih = 0;
/*  642: 942 */           double pqold = this.modelSecondDerivativesParameters.getEntry(knew);
/*  643: 943 */           this.modelSecondDerivativesParameters.setEntry(knew, 0.0D);
/*  644: 944 */           for (int i = 0; i < n; i++)
/*  645:     */           {
/*  646: 945 */             double temp = pqold * this.interpolationPoints.getEntry(knew, i);
/*  647: 946 */             for (int j = 0; j <= i; j++)
/*  648:     */             {
/*  649: 947 */               this.modelSecondDerivativesValues.setEntry(ih, this.modelSecondDerivativesValues.getEntry(ih) + temp * this.interpolationPoints.getEntry(knew, j));
/*  650: 948 */               ih++;
/*  651:     */             }
/*  652:     */           }
/*  653: 951 */           for (int m = 0; m < nptm; m++)
/*  654:     */           {
/*  655: 952 */             double temp = diff * this.zMatrix.getEntry(knew, m);
/*  656: 953 */             for (int k = 0; k < npt; k++) {
/*  657: 954 */               this.modelSecondDerivativesParameters.setEntry(k, this.modelSecondDerivativesParameters.getEntry(k) + temp * this.zMatrix.getEntry(k, m));
/*  658:     */             }
/*  659:     */           }
/*  660: 961 */           this.fAtInterpolationPoints.setEntry(knew, f);
/*  661: 962 */           for (int i = 0; i < n; i++)
/*  662:     */           {
/*  663: 963 */             this.interpolationPoints.setEntry(knew, i, this.newPoint.getEntry(i));
/*  664: 964 */             work1.setEntry(i, this.bMatrix.getEntry(knew, i));
/*  665:     */           }
/*  666: 966 */           for (int k = 0; k < npt; k++)
/*  667:     */           {
/*  668: 967 */             double suma = 0.0D;
/*  669: 968 */             for (int m = 0; m < nptm; m++) {
/*  670: 969 */               suma += this.zMatrix.getEntry(knew, m) * this.zMatrix.getEntry(k, m);
/*  671:     */             }
/*  672: 971 */             double sumb = 0.0D;
/*  673: 972 */             for (int j = 0; j < n; j++) {
/*  674: 973 */               sumb += this.interpolationPoints.getEntry(k, j) * this.trustRegionCenterOffset.getEntry(j);
/*  675:     */             }
/*  676: 975 */             double temp = suma * sumb;
/*  677: 976 */             for (int i = 0; i < n; i++) {
/*  678: 977 */               work1.setEntry(i, work1.getEntry(i) + temp * this.interpolationPoints.getEntry(k, i));
/*  679:     */             }
/*  680:     */           }
/*  681: 980 */           for (int i = 0; i < n; i++) {
/*  682: 981 */             this.gradientAtTrustRegionCenter.setEntry(i, this.gradientAtTrustRegionCenter.getEntry(i) + diff * work1.getEntry(i));
/*  683:     */           }
/*  684: 986 */           if (f < fopt)
/*  685:     */           {
/*  686: 987 */             this.trustRegionCenterInterpolationPointIndex = knew;
/*  687: 988 */             xoptsq = 0.0D;
/*  688: 989 */             ih = 0;
/*  689: 990 */             for (int j = 0; j < n; j++)
/*  690:     */             {
/*  691: 991 */               this.trustRegionCenterOffset.setEntry(j, this.newPoint.getEntry(j));
/*  692:     */               
/*  693: 993 */               double d1 = this.trustRegionCenterOffset.getEntry(j);
/*  694: 994 */               xoptsq += d1 * d1;
/*  695: 995 */               for (int i = 0; i <= j; i++)
/*  696:     */               {
/*  697: 996 */                 if (i < j) {
/*  698: 997 */                   this.gradientAtTrustRegionCenter.setEntry(j, this.gradientAtTrustRegionCenter.getEntry(j) + this.modelSecondDerivativesValues.getEntry(ih) * this.trialStepPoint.getEntry(i));
/*  699:     */                 }
/*  700: 999 */                 this.gradientAtTrustRegionCenter.setEntry(i, this.gradientAtTrustRegionCenter.getEntry(i) + this.modelSecondDerivativesValues.getEntry(ih) * this.trialStepPoint.getEntry(j));
/*  701:1000 */                 ih++;
/*  702:     */               }
/*  703:     */             }
/*  704:1003 */             for (int k = 0; k < npt; k++)
/*  705:     */             {
/*  706:1004 */               double temp = 0.0D;
/*  707:1005 */               for (int j = 0; j < n; j++) {
/*  708:1006 */                 temp += this.interpolationPoints.getEntry(k, j) * this.trialStepPoint.getEntry(j);
/*  709:     */               }
/*  710:1008 */               temp *= this.modelSecondDerivativesParameters.getEntry(k);
/*  711:1009 */               for (int i = 0; i < n; i++) {
/*  712:1010 */                 this.gradientAtTrustRegionCenter.setEntry(i, this.gradientAtTrustRegionCenter.getEntry(i) + temp * this.interpolationPoints.getEntry(k, i));
/*  713:     */               }
/*  714:     */             }
/*  715:     */           }
/*  716:1019 */           if (ntrits > 0)
/*  717:     */           {
/*  718:1020 */             for (int k = 0; k < npt; k++)
/*  719:     */             {
/*  720:1021 */               this.lagrangeValuesAtNewPoint.setEntry(k, this.fAtInterpolationPoints.getEntry(k) - this.fAtInterpolationPoints.getEntry(this.trustRegionCenterInterpolationPointIndex));
/*  721:1022 */               work3.setEntry(k, 0.0D);
/*  722:     */             }
/*  723:1024 */             for (int j = 0; j < nptm; j++)
/*  724:     */             {
/*  725:1025 */               double sum = 0.0D;
/*  726:1026 */               for (int k = 0; k < npt; k++) {
/*  727:1027 */                 sum += this.zMatrix.getEntry(k, j) * this.lagrangeValuesAtNewPoint.getEntry(k);
/*  728:     */               }
/*  729:1029 */               for (int k = 0; k < npt; k++) {
/*  730:1030 */                 work3.setEntry(k, work3.getEntry(k) + sum * this.zMatrix.getEntry(k, j));
/*  731:     */               }
/*  732:     */             }
/*  733:1033 */             for (int k = 0; k < npt; k++)
/*  734:     */             {
/*  735:1034 */               double sum = 0.0D;
/*  736:1035 */               for (int j = 0; j < n; j++) {
/*  737:1036 */                 sum += this.interpolationPoints.getEntry(k, j) * this.trustRegionCenterOffset.getEntry(j);
/*  738:     */               }
/*  739:1038 */               work2.setEntry(k, work3.getEntry(k));
/*  740:1039 */               work3.setEntry(k, sum * work3.getEntry(k));
/*  741:     */             }
/*  742:1041 */             double gqsq = 0.0D;
/*  743:1042 */             double gisq = 0.0D;
/*  744:1043 */             for (int i = 0; i < n; i++)
/*  745:     */             {
/*  746:1044 */               double sum = 0.0D;
/*  747:1045 */               for (int k = 0; k < npt; k++) {
/*  748:1046 */                 sum += this.bMatrix.getEntry(k, i) * this.lagrangeValuesAtNewPoint.getEntry(k) + this.interpolationPoints.getEntry(k, i) * work3.getEntry(k);
/*  749:     */               }
/*  750:1049 */               if (this.trustRegionCenterOffset.getEntry(i) == this.lowerDifference.getEntry(i))
/*  751:     */               {
/*  752:1052 */                 double d1 = Math.min(0.0D, this.gradientAtTrustRegionCenter.getEntry(i));
/*  753:1053 */                 gqsq += d1 * d1;
/*  754:     */                 
/*  755:1055 */                 double d2 = Math.min(0.0D, sum);
/*  756:1056 */                 gisq += d2 * d2;
/*  757:     */               }
/*  758:1057 */               else if (this.trustRegionCenterOffset.getEntry(i) == this.upperDifference.getEntry(i))
/*  759:     */               {
/*  760:1060 */                 double d1 = Math.max(0.0D, this.gradientAtTrustRegionCenter.getEntry(i));
/*  761:1061 */                 gqsq += d1 * d1;
/*  762:     */                 
/*  763:1063 */                 double d2 = Math.max(0.0D, sum);
/*  764:1064 */                 gisq += d2 * d2;
/*  765:     */               }
/*  766:     */               else
/*  767:     */               {
/*  768:1067 */                 double d1 = this.gradientAtTrustRegionCenter.getEntry(i);
/*  769:1068 */                 gqsq += d1 * d1;
/*  770:1069 */                 gisq += sum * sum;
/*  771:     */               }
/*  772:1071 */               this.lagrangeValuesAtNewPoint.setEntry(npt + i, sum);
/*  773:     */             }
/*  774:1077 */             itest++;
/*  775:1078 */             if (gqsq < 10.0D * gisq) {
/*  776:1079 */               itest = 0;
/*  777:     */             }
/*  778:1081 */             if (itest >= 3)
/*  779:     */             {
/*  780:1082 */               int i = 0;
/*  781:1082 */               for (int max = Math.max(npt, nh); i < max; i++)
/*  782:     */               {
/*  783:1083 */                 if (i < n) {
/*  784:1084 */                   this.gradientAtTrustRegionCenter.setEntry(i, this.lagrangeValuesAtNewPoint.getEntry(npt + i));
/*  785:     */                 }
/*  786:1086 */                 if (i < npt) {
/*  787:1087 */                   this.modelSecondDerivativesParameters.setEntry(i, work2.getEntry(i));
/*  788:     */                 }
/*  789:1089 */                 if (i < nh) {
/*  790:1090 */                   this.modelSecondDerivativesValues.setEntry(i, 0.0D);
/*  791:     */                 }
/*  792:1092 */                 itest = 0;
/*  793:     */               }
/*  794:     */             }
/*  795:     */           }
/*  796:1101 */           if (ntrits == 0)
/*  797:     */           {
/*  798:1102 */             state = 60;
/*  799:     */           }
/*  800:1104 */           else if (f <= fopt + 0.1D * vquad)
/*  801:     */           {
/*  802:1105 */             state = 60;
/*  803:     */           }
/*  804:     */           else
/*  805:     */           {
/*  806:1113 */             double d1 = 2.0D * delta;
/*  807:     */             
/*  808:1115 */             double d2 = 10.0D * rho;
/*  809:1116 */             distsq = Math.max(d1 * d1, d2 * d2);
/*  810:     */           }
/*  811:     */         }
/*  812:     */         break;
/*  813:     */       case 650: 
/*  814:1119 */         printState(650);
/*  815:1120 */         knew = -1;
/*  816:1121 */         for (int k = 0; k < npt; k++)
/*  817:     */         {
/*  818:1122 */           double sum = 0.0D;
/*  819:1123 */           for (int j = 0; j < n; j++)
/*  820:     */           {
/*  821:1125 */             double d1 = this.interpolationPoints.getEntry(k, j) - this.trustRegionCenterOffset.getEntry(j);
/*  822:1126 */             sum += d1 * d1;
/*  823:     */           }
/*  824:1128 */           if (sum > distsq)
/*  825:     */           {
/*  826:1129 */             knew = k;
/*  827:1130 */             distsq = sum;
/*  828:     */           }
/*  829:     */         }
/*  830:1140 */         if (knew >= 0)
/*  831:     */         {
/*  832:1141 */           double dist = Math.sqrt(distsq);
/*  833:1142 */           if (ntrits == -1)
/*  834:     */           {
/*  835:1144 */             delta = Math.min(0.1D * delta, 0.5D * dist);
/*  836:1145 */             if (delta <= rho * 1.5D) {
/*  837:1146 */               delta = rho;
/*  838:     */             }
/*  839:     */           }
/*  840:1149 */           ntrits = 0;
/*  841:     */           
/*  842:     */ 
/*  843:1152 */           double d1 = Math.min(0.1D * dist, delta);
/*  844:1153 */           adelt = Math.max(d1, rho);
/*  845:1154 */           dsq = adelt * adelt;
/*  846:1155 */           state = 90;
/*  847:     */         }
/*  848:1157 */         else if (ntrits == -1)
/*  849:     */         {
/*  850:1158 */           state = 680;
/*  851:     */         }
/*  852:1160 */         else if (ratio > 0.0D)
/*  853:     */         {
/*  854:1161 */           state = 60;
/*  855:     */         }
/*  856:1163 */         else if (Math.max(delta, dnorm) > rho)
/*  857:     */         {
/*  858:1164 */           state = 60;
/*  859:     */         }
/*  860:     */         break;
/*  861:     */       case 680: 
/*  862:1171 */         printState(680);
/*  863:1172 */         if (rho > this.stoppingTrustRegionRadius)
/*  864:     */         {
/*  865:1173 */           delta = 0.5D * rho;
/*  866:1174 */           ratio = rho / this.stoppingTrustRegionRadius;
/*  867:1175 */           if (ratio <= 16.0D) {
/*  868:1176 */             rho = this.stoppingTrustRegionRadius;
/*  869:1177 */           } else if (ratio <= 250.0D) {
/*  870:1178 */             rho = Math.sqrt(ratio) * this.stoppingTrustRegionRadius;
/*  871:     */           } else {
/*  872:1180 */             rho *= 0.1D;
/*  873:     */           }
/*  874:1182 */           delta = Math.max(delta, rho);
/*  875:1183 */           ntrits = 0;
/*  876:1184 */           nfsav = getEvaluations();
/*  877:1185 */           state = 60;
/*  878:     */         }
/*  879:     */         else
/*  880:     */         {
/*  881:1191 */           if (ntrits != -1) {
/*  882:     */             break;
/*  883:     */           }
/*  884:1192 */           state = 360;
/*  885:     */         }
/*  886:     */         break;
/*  887:     */       }
/*  888:     */     }   }
/*  914:     */   
/*  915:     */   private double[] altmov(int knew, double adelt)
/*  916:     */   {
/*  917:1261 */     printMethod();
/*  918:     */     
/*  919:1263 */     int n = this.currentBest.getDimension();
/*  920:1264 */     int npt = this.numberOfInterpolationPoints;
/*  921:     */     
/*  922:1266 */     ArrayRealVector glag = new ArrayRealVector(n);
/*  923:1267 */     ArrayRealVector hcol = new ArrayRealVector(npt);
/*  924:     */     
/*  925:1269 */     ArrayRealVector work1 = new ArrayRealVector(n);
/*  926:1270 */     ArrayRealVector work2 = new ArrayRealVector(n);
/*  927:1272 */     for (int k = 0; k < npt; k++) {
/*  928:1273 */       hcol.setEntry(k, 0.0D);
/*  929:     */     }
/*  930:1275 */     int j = 0;
/*  931:1275 */     for (int max = npt - n - 1; j < max; j++)
/*  932:     */     {
/*  933:1276 */       double tmp = this.zMatrix.getEntry(knew, j);
/*  934:1277 */       for (int k = 0; k < npt; k++) {
/*  935:1278 */         hcol.setEntry(k, hcol.getEntry(k) + tmp * this.zMatrix.getEntry(k, j));
/*  936:     */       }
/*  937:     */     }
/*  938:1281 */     double alpha = hcol.getEntry(knew);
/*  939:1282 */     double ha = 0.5D * alpha;
/*  940:1286 */     for (int i = 0; i < n; i++) {
/*  941:1287 */       glag.setEntry(i, this.bMatrix.getEntry(knew, i));
/*  942:     */     }
/*  943:1289 */     for (int k = 0; k < npt; k++)
/*  944:     */     {
/*  945:1290 */       double tmp = 0.0D;
/*  946:1291 */       for (int j1 = 0; j1 < n; j1++) {
/*  947:1292 */         tmp += this.interpolationPoints.getEntry(k, j1) * this.trustRegionCenterOffset.getEntry(j1);
/*  948:     */       }
/*  949:1294 */       tmp *= hcol.getEntry(k);
/*  950:1295 */       for (int i = 0; i < n; i++) {
/*  951:1296 */         glag.setEntry(i, glag.getEntry(i) + tmp * this.interpolationPoints.getEntry(k, i));
/*  952:     */       }
/*  953:     */     }
/*  954:1306 */     double presav = 0.0D;
/*  955:1307 */     double step = (0.0D / 0.0D);
/*  956:1308 */     int ksav = 0;
/*  957:1309 */     int ibdsav = 0;
/*  958:1310 */     double stpsav = 0.0D;
/*  959:1311 */     for (int k = 0; k < npt; k++) {
/*  960:1312 */       if (k != this.trustRegionCenterInterpolationPointIndex)
/*  961:     */       {
/*  962:1315 */         double dderiv = 0.0D;
/*  963:1316 */         double distsq = 0.0D;
/*  964:1317 */         for (int i = 0; i < n; i++)
/*  965:     */         {
/*  966:1318 */           double tmp = this.interpolationPoints.getEntry(k, i) - this.trustRegionCenterOffset.getEntry(i);
/*  967:1319 */           dderiv += glag.getEntry(i) * tmp;
/*  968:1320 */           distsq += tmp * tmp;
/*  969:     */         }
/*  970:1322 */         double subd = adelt / Math.sqrt(distsq);
/*  971:1323 */         double slbd = -subd;
/*  972:1324 */         int ilbd = 0;
/*  973:1325 */         int iubd = 0;
/*  974:1326 */         double sumin = Math.min(1.0D, subd);
/*  975:1330 */         for (int i = 0; i < n; i++)
/*  976:     */         {
/*  977:1331 */           double tmp = this.interpolationPoints.getEntry(k, i) - this.trustRegionCenterOffset.getEntry(i);
/*  978:1332 */           if (tmp > 0.0D)
/*  979:     */           {
/*  980:1333 */             if (slbd * tmp < this.lowerDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i))
/*  981:     */             {
/*  982:1334 */               slbd = (this.lowerDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i)) / tmp;
/*  983:1335 */               ilbd = -i - 1;
/*  984:     */             }
/*  985:1337 */             if (subd * tmp > this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i))
/*  986:     */             {
/*  987:1339 */               subd = Math.max(sumin, (this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i)) / tmp);
/*  988:     */               
/*  989:1341 */               iubd = i + 1;
/*  990:     */             }
/*  991:     */           }
/*  992:1343 */           else if (tmp < 0.0D)
/*  993:     */           {
/*  994:1344 */             if (slbd * tmp > this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i))
/*  995:     */             {
/*  996:1345 */               slbd = (this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i)) / tmp;
/*  997:1346 */               ilbd = i + 1;
/*  998:     */             }
/*  999:1348 */             if (subd * tmp < this.lowerDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i))
/* 1000:     */             {
/* 1001:1350 */               subd = Math.max(sumin, (this.lowerDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i)) / tmp);
/* 1002:     */               
/* 1003:1352 */               iubd = -i - 1;
/* 1004:     */             }
/* 1005:     */           }
/* 1006:     */         }
/* 1007:1360 */         step = slbd;
/* 1008:1361 */         int isbd = ilbd;
/* 1009:1362 */         double vlag = (0.0D / 0.0D);
/* 1010:1363 */         if (k == knew)
/* 1011:     */         {
/* 1012:1364 */           double diff = dderiv - 1.0D;
/* 1013:1365 */           vlag = slbd * (dderiv - slbd * diff);
/* 1014:1366 */           double d1 = subd * (dderiv - subd * diff);
/* 1015:1367 */           if (Math.abs(d1) > Math.abs(vlag))
/* 1016:     */           {
/* 1017:1368 */             step = subd;
/* 1018:1369 */             vlag = d1;
/* 1019:1370 */             isbd = iubd;
/* 1020:     */           }
/* 1021:1372 */           double d2 = 0.5D * dderiv;
/* 1022:1373 */           double d3 = d2 - diff * slbd;
/* 1023:1374 */           double d4 = d2 - diff * subd;
/* 1024:1375 */           if (d3 * d4 < 0.0D)
/* 1025:     */           {
/* 1026:1376 */             double d5 = d2 * d2 / diff;
/* 1027:1377 */             if (Math.abs(d5) > Math.abs(vlag))
/* 1028:     */             {
/* 1029:1378 */               step = d2 / diff;
/* 1030:1379 */               vlag = d5;
/* 1031:1380 */               isbd = 0;
/* 1032:     */             }
/* 1033:     */           }
/* 1034:     */         }
/* 1035:     */         else
/* 1036:     */         {
/* 1037:1387 */           vlag = slbd * (1.0D - slbd);
/* 1038:1388 */           double tmp = subd * (1.0D - subd);
/* 1039:1389 */           if (Math.abs(tmp) > Math.abs(vlag))
/* 1040:     */           {
/* 1041:1390 */             step = subd;
/* 1042:1391 */             vlag = tmp;
/* 1043:1392 */             isbd = iubd;
/* 1044:     */           }
/* 1045:1394 */           if ((subd > 0.5D) && 
/* 1046:1395 */             (Math.abs(vlag) < 0.25D))
/* 1047:     */           {
/* 1048:1396 */             step = 0.5D;
/* 1049:1397 */             vlag = 0.25D;
/* 1050:1398 */             isbd = 0;
/* 1051:     */           }
/* 1052:1401 */           vlag *= dderiv;
/* 1053:     */         }
/* 1054:1406 */         double tmp = step * (1.0D - step) * distsq;
/* 1055:1407 */         double predsq = vlag * vlag * (vlag * vlag + ha * tmp * tmp);
/* 1056:1408 */         if (predsq > presav)
/* 1057:     */         {
/* 1058:1409 */           presav = predsq;
/* 1059:1410 */           ksav = k;
/* 1060:1411 */           stpsav = step;
/* 1061:1412 */           ibdsav = isbd;
/* 1062:     */         }
/* 1063:     */       }
/* 1064:     */     }
/* 1065:1418 */     for (int i = 0; i < n; i++)
/* 1066:     */     {
/* 1067:1419 */       double tmp = this.trustRegionCenterOffset.getEntry(i) + stpsav * (this.interpolationPoints.getEntry(ksav, i) - this.trustRegionCenterOffset.getEntry(i));
/* 1068:1420 */       this.newPoint.setEntry(i, Math.max(this.lowerDifference.getEntry(i), Math.min(this.upperDifference.getEntry(i), tmp)));
/* 1069:     */     }
/* 1070:1423 */     if (ibdsav < 0) {
/* 1071:1424 */       this.newPoint.setEntry(-ibdsav - 1, this.lowerDifference.getEntry(-ibdsav - 1));
/* 1072:     */     }
/* 1073:1426 */     if (ibdsav > 0) {
/* 1074:1427 */       this.newPoint.setEntry(ibdsav - 1, this.upperDifference.getEntry(ibdsav - 1));
/* 1075:     */     }
/* 1076:1434 */     double bigstp = adelt + adelt;
/* 1077:1435 */     int iflag = 0;
/* 1078:1436 */     double cauchy = (0.0D / 0.0D);
/* 1079:1437 */     double csave = 0.0D;
/* 1080:     */     for (;;)
/* 1081:     */     {
/* 1082:1439 */       double wfixsq = 0.0D;
/* 1083:1440 */       double ggfree = 0.0D;
/* 1084:1441 */       for (int i = 0; i < n; i++)
/* 1085:     */       {
/* 1086:1442 */         double glagValue = glag.getEntry(i);
/* 1087:1443 */         work1.setEntry(i, 0.0D);
/* 1088:1444 */         if ((Math.min(this.trustRegionCenterOffset.getEntry(i) - this.lowerDifference.getEntry(i), glagValue) > 0.0D) || (Math.max(this.trustRegionCenterOffset.getEntry(i) - this.upperDifference.getEntry(i), glagValue) < 0.0D))
/* 1089:     */         {
/* 1090:1446 */           work1.setEntry(i, bigstp);
/* 1091:     */           
/* 1092:1448 */           ggfree += glagValue * glagValue;
/* 1093:     */         }
/* 1094:     */       }
/* 1095:1451 */       if (ggfree == 0.0D) {
/* 1096:1452 */         return new double[] { alpha, 0.0D };
/* 1097:     */       }
/* 1098:1456 */       double tmp1 = adelt * adelt - wfixsq;
/* 1099:1457 */       if (tmp1 > 0.0D)
/* 1100:     */       {
/* 1101:1458 */         step = Math.sqrt(tmp1 / ggfree);
/* 1102:1459 */         ggfree = 0.0D;
/* 1103:1460 */         for (int i = 0; i < n; i++) {
/* 1104:1461 */           if (work1.getEntry(i) == bigstp)
/* 1105:     */           {
/* 1106:1462 */             double tmp2 = this.trustRegionCenterOffset.getEntry(i) - step * glag.getEntry(i);
/* 1107:1463 */             if (tmp2 <= this.lowerDifference.getEntry(i))
/* 1108:     */             {
/* 1109:1464 */               work1.setEntry(i, this.lowerDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/* 1110:     */               
/* 1111:1466 */               double d1 = work1.getEntry(i);
/* 1112:1467 */               wfixsq += d1 * d1;
/* 1113:     */             }
/* 1114:1468 */             else if (tmp2 >= this.upperDifference.getEntry(i))
/* 1115:     */             {
/* 1116:1469 */               work1.setEntry(i, this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/* 1117:     */               
/* 1118:1471 */               double d1 = work1.getEntry(i);
/* 1119:1472 */               wfixsq += d1 * d1;
/* 1120:     */             }
/* 1121:     */             else
/* 1122:     */             {
/* 1123:1475 */               double d1 = glag.getEntry(i);
/* 1124:1476 */               ggfree += d1 * d1;
/* 1125:     */             }
/* 1126:     */           }
/* 1127:     */         }
/* 1128:     */       }
/* 1129:1485 */       double gw = 0.0D;
/* 1130:1486 */       for (int i = 0; i < n; i++)
/* 1131:     */       {
/* 1132:1487 */         double glagValue = glag.getEntry(i);
/* 1133:1488 */         if (work1.getEntry(i) == bigstp)
/* 1134:     */         {
/* 1135:1489 */           work1.setEntry(i, -step * glagValue);
/* 1136:1490 */           double min = Math.min(this.upperDifference.getEntry(i), this.trustRegionCenterOffset.getEntry(i) + work1.getEntry(i));
/* 1137:     */           
/* 1138:1492 */           this.alternativeNewPoint.setEntry(i, Math.max(this.lowerDifference.getEntry(i), min));
/* 1139:     */         }
/* 1140:1493 */         else if (work1.getEntry(i) == 0.0D)
/* 1141:     */         {
/* 1142:1494 */           this.alternativeNewPoint.setEntry(i, this.trustRegionCenterOffset.getEntry(i));
/* 1143:     */         }
/* 1144:1495 */         else if (glagValue > 0.0D)
/* 1145:     */         {
/* 1146:1496 */           this.alternativeNewPoint.setEntry(i, this.lowerDifference.getEntry(i));
/* 1147:     */         }
/* 1148:     */         else
/* 1149:     */         {
/* 1150:1498 */           this.alternativeNewPoint.setEntry(i, this.upperDifference.getEntry(i));
/* 1151:     */         }
/* 1152:1500 */         gw += glagValue * work1.getEntry(i);
/* 1153:     */       }
/* 1154:1508 */       double curv = 0.0D;
/* 1155:1509 */       for (int k = 0; k < npt; k++)
/* 1156:     */       {
/* 1157:1510 */         double tmp = 0.0D;
/* 1158:1511 */         for (int j1 = 0; j1 < n; j1++) {
/* 1159:1512 */           tmp += this.interpolationPoints.getEntry(k, j1) * work1.getEntry(j1);
/* 1160:     */         }
/* 1161:1514 */         curv += hcol.getEntry(k) * tmp * tmp;
/* 1162:     */       }
/* 1163:1516 */       if (iflag == 1) {
/* 1164:1517 */         curv = -curv;
/* 1165:     */       }
/* 1166:1519 */       if ((curv > -gw) && (curv < -gw * (1.0D + Math.sqrt(2.0D))))
/* 1167:     */       {
/* 1168:1521 */         double scale = -gw / curv;
/* 1169:1522 */         for (int i = 0; i < n; i++)
/* 1170:     */         {
/* 1171:1523 */           double tmp = this.trustRegionCenterOffset.getEntry(i) + scale * work1.getEntry(i);
/* 1172:1524 */           this.alternativeNewPoint.setEntry(i, Math.max(this.lowerDifference.getEntry(i), Math.min(this.upperDifference.getEntry(i), tmp)));
/* 1173:     */         }
/* 1174:1528 */         double d1 = 0.5D * gw * scale;
/* 1175:1529 */         cauchy = d1 * d1;
/* 1176:     */       }
/* 1177:     */       else
/* 1178:     */       {
/* 1179:1532 */         double d1 = gw + 0.5D * curv;
/* 1180:1533 */         cauchy = d1 * d1;
/* 1181:     */       }
/* 1182:1540 */       if (iflag != 0) {
/* 1183:     */         break;
/* 1184:     */       }
/* 1185:1541 */       for (int i = 0; i < n; i++)
/* 1186:     */       {
/* 1187:1542 */         glag.setEntry(i, -glag.getEntry(i));
/* 1188:1543 */         work2.setEntry(i, this.alternativeNewPoint.getEntry(i));
/* 1189:     */       }
/* 1190:1545 */       csave = cauchy;
/* 1191:1546 */       iflag = 1;
/* 1192:     */     }
/* 1193:1551 */     if (csave > cauchy)
/* 1194:     */     {
/* 1195:1552 */       for (int i = 0; i < n; i++) {
/* 1196:1553 */         this.alternativeNewPoint.setEntry(i, work2.getEntry(i));
/* 1197:     */       }
/* 1198:1555 */       cauchy = csave;
/* 1199:     */     }
/* 1200:1558 */     return new double[] { alpha, cauchy };
/* 1201:     */   }
/* 1202:     */   
/* 1203:     */   private void prelim(double[] lowerBound, double[] upperBound)
/* 1204:     */   {
/* 1205:1585 */     printMethod();
/* 1206:     */     
/* 1207:1587 */     int n = this.currentBest.getDimension();
/* 1208:1588 */     int npt = this.numberOfInterpolationPoints;
/* 1209:1589 */     int ndim = this.bMatrix.getRowDimension();
/* 1210:     */     
/* 1211:1591 */     double rhosq = this.initialTrustRegionRadius * this.initialTrustRegionRadius;
/* 1212:1592 */     double recip = 1.0D / rhosq;
/* 1213:1593 */     int np = n + 1;
/* 1214:1598 */     for (int j = 0; j < n; j++)
/* 1215:     */     {
/* 1216:1599 */       this.originShift.setEntry(j, this.currentBest.getEntry(j));
/* 1217:1600 */       for (int k = 0; k < npt; k++) {
/* 1218:1601 */         this.interpolationPoints.setEntry(k, j, 0.0D);
/* 1219:     */       }
/* 1220:1603 */       for (int i = 0; i < ndim; i++) {
/* 1221:1604 */         this.bMatrix.setEntry(i, j, 0.0D);
/* 1222:     */       }
/* 1223:     */     }
/* 1224:1607 */     int i = 0;
/* 1225:1607 */     for (int max = n * np / 2; i < max; i++) {
/* 1226:1608 */       this.modelSecondDerivativesValues.setEntry(i, 0.0D);
/* 1227:     */     }
/* 1228:1610 */     for (int k = 0; k < npt; k++)
/* 1229:     */     {
/* 1230:1611 */       this.modelSecondDerivativesParameters.setEntry(k, 0.0D);
/* 1231:1612 */       int j = 0;
/* 1232:1612 */       for (int max = npt - np; j < max; j++) {
/* 1233:1613 */         this.zMatrix.setEntry(k, j, 0.0D);
/* 1234:     */       }
/* 1235:     */     }
/* 1236:1621 */     int ipt = 0;
/* 1237:1622 */     int jpt = 0;
/* 1238:1623 */     double fbeg = (0.0D / 0.0D);
/* 1239:     */     do
/* 1240:     */     {
/* 1241:1625 */       int nfm = getEvaluations();
/* 1242:1626 */       int nfx = nfm - n;
/* 1243:1627 */       int nfmm = nfm - 1;
/* 1244:1628 */       int nfxm = nfx - 1;
/* 1245:1629 */       double stepa = 0.0D;
/* 1246:1630 */       double stepb = 0.0D;
/* 1247:1631 */       if (nfm <= 2 * n)
/* 1248:     */       {
/* 1249:1632 */         if ((nfm >= 1) && (nfm <= n))
/* 1250:     */         {
/* 1251:1634 */           stepa = this.initialTrustRegionRadius;
/* 1252:1635 */           if (this.upperDifference.getEntry(nfmm) == 0.0D) {
/* 1253:1636 */             stepa = -stepa;
/* 1254:     */           }
/* 1255:1639 */           this.interpolationPoints.setEntry(nfm, nfmm, stepa);
/* 1256:     */         }
/* 1257:1640 */         else if (nfm > n)
/* 1258:     */         {
/* 1259:1641 */           stepa = this.interpolationPoints.getEntry(nfx, nfxm);
/* 1260:1642 */           stepb = -this.initialTrustRegionRadius;
/* 1261:1643 */           if (this.lowerDifference.getEntry(nfxm) == 0.0D) {
/* 1262:1644 */             stepb = Math.min(2.0D * this.initialTrustRegionRadius, this.upperDifference.getEntry(nfxm));
/* 1263:     */           }
/* 1264:1647 */           if (this.upperDifference.getEntry(nfxm) == 0.0D) {
/* 1265:1648 */             stepb = Math.max(-2.0D * this.initialTrustRegionRadius, this.lowerDifference.getEntry(nfxm));
/* 1266:     */           }
/* 1267:1651 */           this.interpolationPoints.setEntry(nfm, nfxm, stepb);
/* 1268:     */         }
/* 1269:     */       }
/* 1270:     */       else
/* 1271:     */       {
/* 1272:1654 */         int tmp1 = (nfm - np) / n;
/* 1273:1655 */         jpt = nfm - tmp1 * n - n;
/* 1274:1656 */         ipt = jpt + tmp1;
/* 1275:1657 */         if (ipt > n)
/* 1276:     */         {
/* 1277:1658 */           int tmp2 = jpt;
/* 1278:1659 */           jpt = ipt - n;
/* 1279:1660 */           ipt = tmp2;
/* 1280:     */         }
/* 1281:1663 */         int iptMinus1 = ipt - 1;
/* 1282:1664 */         int jptMinus1 = jpt - 1;
/* 1283:1665 */         this.interpolationPoints.setEntry(nfm, iptMinus1, this.interpolationPoints.getEntry(ipt, iptMinus1));
/* 1284:1666 */         this.interpolationPoints.setEntry(nfm, jptMinus1, this.interpolationPoints.getEntry(jpt, jptMinus1));
/* 1285:     */       }
/* 1286:1672 */       for (int j = 0; j < n; j++)
/* 1287:     */       {
/* 1288:1673 */         this.currentBest.setEntry(j, Math.min(Math.max(lowerBound[j], this.originShift.getEntry(j) + this.interpolationPoints.getEntry(nfm, j)), upperBound[j]));
/* 1289:1676 */         if (this.interpolationPoints.getEntry(nfm, j) == this.lowerDifference.getEntry(j)) {
/* 1290:1677 */           this.currentBest.setEntry(j, lowerBound[j]);
/* 1291:     */         }
/* 1292:1679 */         if (this.interpolationPoints.getEntry(nfm, j) == this.upperDifference.getEntry(j)) {
/* 1293:1680 */           this.currentBest.setEntry(j, upperBound[j]);
/* 1294:     */         }
/* 1295:     */       }
/* 1296:1684 */       double objectiveValue = computeObjectiveValue(this.currentBest.toArray());
/* 1297:1685 */       double f = this.isMinimize ? objectiveValue : -objectiveValue;
/* 1298:1686 */       int numEval = getEvaluations();
/* 1299:1687 */       this.fAtInterpolationPoints.setEntry(nfm, f);
/* 1300:1689 */       if (numEval == 1)
/* 1301:     */       {
/* 1302:1690 */         fbeg = f;
/* 1303:1691 */         this.trustRegionCenterInterpolationPointIndex = 0;
/* 1304:     */       }
/* 1305:1692 */       else if (f < this.fAtInterpolationPoints.getEntry(this.trustRegionCenterInterpolationPointIndex))
/* 1306:     */       {
/* 1307:1693 */         this.trustRegionCenterInterpolationPointIndex = nfm;
/* 1308:     */       }
/* 1309:1702 */       if (numEval <= 2 * n + 1)
/* 1310:     */       {
/* 1311:1703 */         if ((numEval >= 2) && (numEval <= n + 1))
/* 1312:     */         {
/* 1313:1705 */           this.gradientAtTrustRegionCenter.setEntry(nfmm, (f - fbeg) / stepa);
/* 1314:1706 */           if (npt < numEval + n)
/* 1315:     */           {
/* 1316:1707 */             double oneOverStepA = 1.0D / stepa;
/* 1317:1708 */             this.bMatrix.setEntry(0, nfmm, -oneOverStepA);
/* 1318:1709 */             this.bMatrix.setEntry(nfm, nfmm, oneOverStepA);
/* 1319:1710 */             this.bMatrix.setEntry(npt + nfmm, nfmm, -0.5D * rhosq);
/* 1320:     */           }
/* 1321:     */         }
/* 1322:1713 */         else if (numEval >= n + 2)
/* 1323:     */         {
/* 1324:1714 */           int ih = nfx * (nfx + 1) / 2 - 1;
/* 1325:1715 */           double tmp = (f - fbeg) / stepb;
/* 1326:1716 */           double diff = stepb - stepa;
/* 1327:1717 */           this.modelSecondDerivativesValues.setEntry(ih, 2.0D * (tmp - this.gradientAtTrustRegionCenter.getEntry(nfxm)) / diff);
/* 1328:1718 */           this.gradientAtTrustRegionCenter.setEntry(nfxm, (this.gradientAtTrustRegionCenter.getEntry(nfxm) * stepb - tmp * stepa) / diff);
/* 1329:1719 */           if ((stepa * stepb < 0.0D) && 
/* 1330:1720 */             (f < this.fAtInterpolationPoints.getEntry(nfm - n)))
/* 1331:     */           {
/* 1332:1721 */             this.fAtInterpolationPoints.setEntry(nfm, this.fAtInterpolationPoints.getEntry(nfm - n));
/* 1333:1722 */             this.fAtInterpolationPoints.setEntry(nfm - n, f);
/* 1334:1723 */             if (this.trustRegionCenterInterpolationPointIndex == nfm) {
/* 1335:1724 */               this.trustRegionCenterInterpolationPointIndex = (nfm - n);
/* 1336:     */             }
/* 1337:1726 */             this.interpolationPoints.setEntry(nfm - n, nfxm, stepb);
/* 1338:1727 */             this.interpolationPoints.setEntry(nfm, nfxm, stepa);
/* 1339:     */           }
/* 1340:1730 */           this.bMatrix.setEntry(0, nfxm, -(stepa + stepb) / (stepa * stepb));
/* 1341:1731 */           this.bMatrix.setEntry(nfm, nfxm, -0.5D / this.interpolationPoints.getEntry(nfm - n, nfxm));
/* 1342:1732 */           this.bMatrix.setEntry(nfm - n, nfxm, -this.bMatrix.getEntry(0, nfxm) - this.bMatrix.getEntry(nfm, nfxm));
/* 1343:     */           
/* 1344:1734 */           this.zMatrix.setEntry(0, nfxm, Math.sqrt(2.0D) / (stepa * stepb));
/* 1345:1735 */           this.zMatrix.setEntry(nfm, nfxm, Math.sqrt(0.5D) / rhosq);
/* 1346:     */           
/* 1347:1737 */           this.zMatrix.setEntry(nfm - n, nfxm, -this.zMatrix.getEntry(0, nfxm) - this.zMatrix.getEntry(nfm, nfxm));
/* 1348:     */         }
/* 1349:     */       }
/* 1350:     */       else
/* 1351:     */       {
/* 1352:1745 */         this.zMatrix.setEntry(0, nfxm, recip);
/* 1353:1746 */         this.zMatrix.setEntry(nfm, nfxm, recip);
/* 1354:1747 */         this.zMatrix.setEntry(ipt, nfxm, -recip);
/* 1355:1748 */         this.zMatrix.setEntry(jpt, nfxm, -recip);
/* 1356:     */         
/* 1357:1750 */         int ih = ipt * (ipt - 1) / 2 + jpt - 1;
/* 1358:1751 */         double tmp = this.interpolationPoints.getEntry(nfm, ipt - 1) * this.interpolationPoints.getEntry(nfm, jpt - 1);
/* 1359:1752 */         this.modelSecondDerivativesValues.setEntry(ih, (fbeg - this.fAtInterpolationPoints.getEntry(ipt) - this.fAtInterpolationPoints.getEntry(jpt) + f) / tmp);
/* 1360:     */       }
/* 1361:1755 */     } while (getEvaluations() < npt);
/* 1362:     */   }
/* 1363:     */   
/* 1364:     */   private double[] trsbox(double delta, ArrayRealVector gnew, ArrayRealVector xbdi, ArrayRealVector s, ArrayRealVector hs, ArrayRealVector hred)
/* 1365:     */   {
/* 1366:1813 */     printMethod();
/* 1367:     */     
/* 1368:1815 */     int n = this.currentBest.getDimension();
/* 1369:1816 */     int npt = this.numberOfInterpolationPoints;
/* 1370:     */     
/* 1371:1818 */     double dsq = (0.0D / 0.0D);
/* 1372:1819 */     double crvmin = (0.0D / 0.0D);
/* 1373:     */     
/* 1374:     */ 
/* 1375:     */ 
/* 1376:     */ 
/* 1377:1824 */     double beta = 0.0D;
/* 1378:1825 */     int iact = -1;
/* 1379:1826 */     int nact = 0;
/* 1380:1827 */     double angt = 0.0D;
/* 1381:     */     
/* 1382:1829 */     double temp = 0.0D;double xsav = 0.0D;double xsum = 0.0D;double angbd = 0.0D;double dredg = 0.0D;double sredg = 0.0D;
/* 1383:     */     
/* 1384:1831 */     double resid = 0.0D;double delsq = 0.0D;double ggsav = 0.0D;double tempa = 0.0D;double tempb = 0.0D;
/* 1385:1832 */     double redmax = 0.0D;double dredsq = 0.0D;double redsav = 0.0D;double gredsq = 0.0D;double rednew = 0.0D;
/* 1386:1833 */     int itcsav = 0;
/* 1387:1834 */     double rdprev = 0.0D;double rdnext = 0.0D;double stplen = 0.0D;double stepsq = 0.0D;
/* 1388:1835 */     int itermax = 0;
/* 1389:     */     
/* 1390:     */ 
/* 1391:     */ 
/* 1392:     */ 
/* 1393:     */ 
/* 1394:     */ 
/* 1395:     */ 
/* 1396:     */ 
/* 1397:     */ 
/* 1398:     */ 
/* 1399:     */ 
/* 1400:     */ 
/* 1401:1848 */     int iterc = 0;
/* 1402:1849 */     nact = 0;
/* 1403:1850 */     for (int i = 0; i < n; i++)
/* 1404:     */     {
/* 1405:1851 */       xbdi.setEntry(i, 0.0D);
/* 1406:1852 */       if (this.trustRegionCenterOffset.getEntry(i) <= this.lowerDifference.getEntry(i))
/* 1407:     */       {
/* 1408:1853 */         if (this.gradientAtTrustRegionCenter.getEntry(i) >= 0.0D) {
/* 1409:1854 */           xbdi.setEntry(i, -1.0D);
/* 1410:     */         }
/* 1411:     */       }
/* 1412:1856 */       else if ((this.trustRegionCenterOffset.getEntry(i) >= this.upperDifference.getEntry(i)) && 
/* 1413:1857 */         (this.gradientAtTrustRegionCenter.getEntry(i) <= 0.0D)) {
/* 1414:1858 */         xbdi.setEntry(i, 1.0D);
/* 1415:     */       }
/* 1416:1861 */       if (xbdi.getEntry(i) != 0.0D) {
/* 1417:1862 */         nact++;
/* 1418:     */       }
/* 1419:1864 */       this.trialStepPoint.setEntry(i, 0.0D);
/* 1420:1865 */       gnew.setEntry(i, this.gradientAtTrustRegionCenter.getEntry(i));
/* 1421:     */     }
/* 1422:1867 */     delsq = delta * delta;
/* 1423:1868 */     double qred = 0.0D;
/* 1424:1869 */     crvmin = -1.0D;
/* 1425:     */     
/* 1426:     */ 
/* 1427:     */ 
/* 1428:     */ 
/* 1429:     */ 
/* 1430:     */ 
/* 1431:     */ 
/* 1432:1877 */     int state = 20;
/* 1433:     */     for (;;)
/* 1434:     */     {
/* 1435:     */       double shs;
/* 1436:     */       double sdec;
/* 1437:1879 */       switch (state)
/* 1438:     */       {
/* 1439:     */       case 20: 
/* 1440:1881 */         printState(20);
/* 1441:1882 */         beta = 0.0D;
/* 1442:     */       case 30: 
/* 1443:1885 */         printState(30);
/* 1444:1886 */         stepsq = 0.0D;
/* 1445:1887 */         for (int i = 0; i < n; i++)
/* 1446:     */         {
/* 1447:1888 */           if (xbdi.getEntry(i) != 0.0D) {
/* 1448:1889 */             s.setEntry(i, 0.0D);
/* 1449:1890 */           } else if (beta == 0.0D) {
/* 1450:1891 */             s.setEntry(i, -gnew.getEntry(i));
/* 1451:     */           } else {
/* 1452:1893 */             s.setEntry(i, beta * s.getEntry(i) - gnew.getEntry(i));
/* 1453:     */           }
/* 1454:1896 */           double d1 = s.getEntry(i);
/* 1455:1897 */           stepsq += d1 * d1;
/* 1456:     */         }
/* 1457:1899 */         if (stepsq == 0.0D)
/* 1458:     */         {
/* 1459:1900 */           state = 190;
/* 1460:     */         }
/* 1461:     */         else
/* 1462:     */         {
/* 1463:1902 */           if (beta == 0.0D)
/* 1464:     */           {
/* 1465:1903 */             gredsq = stepsq;
/* 1466:1904 */             itermax = iterc + n - nact;
/* 1467:     */           }
/* 1468:1906 */           if (gredsq * delsq <= qred * 0.0001D * qred) {
/* 1469:1907 */             state = 190;
/* 1470:     */           } else {
/* 1471:1915 */             state = 210;
/* 1472:     */           }
/* 1473:     */         }
/* 1474:     */         break;
/* 1475:     */       case 50: 
/* 1476:1918 */         printState(50);
/* 1477:1919 */         resid = delsq;
/* 1478:1920 */         double ds = 0.0D;
/* 1479:1921 */         shs = 0.0D;
/* 1480:1922 */         for (int i = 0; i < n; i++) {
/* 1481:1923 */           if (xbdi.getEntry(i) == 0.0D)
/* 1482:     */           {
/* 1483:1925 */             double d1 = this.trialStepPoint.getEntry(i);
/* 1484:1926 */             resid -= d1 * d1;
/* 1485:1927 */             ds += s.getEntry(i) * this.trialStepPoint.getEntry(i);
/* 1486:1928 */             shs += s.getEntry(i) * hs.getEntry(i);
/* 1487:     */           }
/* 1488:     */         }
/* 1489:1931 */         if (resid <= 0.0D)
/* 1490:     */         {
/* 1491:1932 */           state = 90;
/* 1492:     */         }
/* 1493:     */         else
/* 1494:     */         {
/* 1495:1934 */           temp = Math.sqrt(stepsq * resid + ds * ds);
/* 1496:     */           double blen;
/* 1497:     */          
/* 1498:1935 */           if (ds < 0.0D) {
/* 1499:1936 */             blen = (temp - ds) / stepsq;
/* 1500:     */           } else {
/* 1501:1938 */             blen = resid / (temp + ds);
/* 1502:     */           }
/* 1503:1940 */           stplen = blen;
/* 1504:1941 */           if (shs > 0.0D) {
/* 1505:1943 */             stplen = Math.min(blen, gredsq / shs);
/* 1506:     */           }
/* 1507:1949 */           iact = -1;
/* 1508:1950 */           for (int i = 0; i < n; i++) {
/* 1509:1951 */             if (s.getEntry(i) != 0.0D)
/* 1510:     */             {
/* 1511:1952 */               xsum = this.trustRegionCenterOffset.getEntry(i) + this.trialStepPoint.getEntry(i);
/* 1512:1953 */               if (s.getEntry(i) > 0.0D) {
/* 1513:1954 */                 temp = (this.upperDifference.getEntry(i) - xsum) / s.getEntry(i);
/* 1514:     */               } else {
/* 1515:1956 */                 temp = (this.lowerDifference.getEntry(i) - xsum) / s.getEntry(i);
/* 1516:     */               }
/* 1517:1958 */               if (temp < stplen)
/* 1518:     */               {
/* 1519:1959 */                 stplen = temp;
/* 1520:1960 */                 iact = i;
/* 1521:     */               }
/* 1522:     */             }
/* 1523:     */           }
/* 1524:1967 */           sdec = 0.0D;
/* 1525:1968 */           if (stplen > 0.0D)
/* 1526:     */           {
/* 1527:1969 */             iterc++;
/* 1528:1970 */             temp = shs / stepsq;
/* 1529:1971 */             if ((iact == -1) && (temp > 0.0D))
/* 1530:     */             {
/* 1531:1972 */               crvmin = Math.min(crvmin, temp);
/* 1532:1973 */               if (crvmin == -1.0D) {
/* 1533:1974 */                 crvmin = temp;
/* 1534:     */               }
/* 1535:     */             }
/* 1536:1977 */             ggsav = gredsq;
/* 1537:1978 */             gredsq = 0.0D;
/* 1538:1979 */             for (int i = 0; i < n; i++)
/* 1539:     */             {
/* 1540:1980 */               gnew.setEntry(i, gnew.getEntry(i) + stplen * hs.getEntry(i));
/* 1541:1981 */               if (xbdi.getEntry(i) == 0.0D)
/* 1542:     */               {
/* 1543:1983 */                 double d1 = gnew.getEntry(i);
/* 1544:1984 */                 gredsq += d1 * d1;
/* 1545:     */               }
/* 1546:1986 */               this.trialStepPoint.setEntry(i, this.trialStepPoint.getEntry(i) + stplen * s.getEntry(i));
/* 1547:     */             }
/* 1548:1989 */             double d1 = stplen * (ggsav - 0.5D * stplen * shs);
/* 1549:1990 */             sdec = Math.max(d1, 0.0D);
/* 1550:1991 */             qred += sdec;
/* 1551:     */           }
/* 1552:1996 */           if (iact >= 0)
/* 1553:     */           {
/* 1554:1997 */             nact++;
/* 1555:1998 */             xbdi.setEntry(iact, 1.0D);
/* 1556:1999 */             if (s.getEntry(iact) < 0.0D) {
/* 1557:2000 */               xbdi.setEntry(iact, -1.0D);
/* 1558:     */             }
/* 1559:2003 */             double d1 = this.trialStepPoint.getEntry(iact);
/* 1560:2004 */             delsq -= d1 * d1;
/* 1561:2005 */             if (delsq <= 0.0D) {
/* 1562:2006 */               state = 190;
/* 1563:     */             } else {
/* 1564:2008 */               state = 20;
/* 1565:     */             }
/* 1566:     */           }
/* 1567:2014 */           else if (stplen < blen)
/* 1568:     */           {
/* 1569:2015 */             if (iterc == itermax)
/* 1570:     */             {
/* 1571:2016 */               state = 190; continue;
/* 1572:     */             }
/* 1573:2018 */             if (sdec <= qred * 0.01D)
/* 1574:     */             {
/* 1575:2019 */               state = 190; continue;
/* 1576:     */             }
/* 1577:2021 */             beta = gredsq / ggsav;
/* 1578:2022 */             state = 30;
/* 1579:     */           }
/* 1580:     */         }
/* 1581:     */         break;
/* 1582:     */       case 90: 
/* 1583:2026 */         printState(90);
/* 1584:2027 */         crvmin = 0.0D;
/* 1585:     */       case 100: 
/* 1586:2035 */         printState(100);
/* 1587:2036 */         if (nact >= n - 1)
/* 1588:     */         {
/* 1589:2037 */           state = 190;
/* 1590:     */         }
/* 1591:     */         else
/* 1592:     */         {
/* 1593:2039 */           dredsq = 0.0D;
/* 1594:2040 */           dredg = 0.0D;
/* 1595:2041 */           gredsq = 0.0D;
/* 1596:2042 */           for (int i = 0; i < n; i++) {
/* 1597:2043 */             if (xbdi.getEntry(i) == 0.0D)
/* 1598:     */             {
/* 1599:2045 */               double d1 = this.trialStepPoint.getEntry(i);
/* 1600:2046 */               dredsq += d1 * d1;
/* 1601:2047 */               dredg += this.trialStepPoint.getEntry(i) * gnew.getEntry(i);
/* 1602:     */               
/* 1603:2049 */               d1 = gnew.getEntry(i);
/* 1604:2050 */               gredsq += d1 * d1;
/* 1605:2051 */               s.setEntry(i, this.trialStepPoint.getEntry(i));
/* 1606:     */             }
/* 1607:     */             else
/* 1608:     */             {
/* 1609:2053 */               s.setEntry(i, 0.0D);
/* 1610:     */             }
/* 1611:     */           }
/* 1612:2056 */           itcsav = iterc;
/* 1613:2057 */           state = 210;
/* 1614:     */         }
/* 1615:     */         break;
/* 1616:     */       case 120: 
/* 1617:2062 */         printState(120);
/* 1618:2063 */         iterc++;
/* 1619:2064 */         temp = gredsq * dredsq - dredg * dredg;
/* 1620:2065 */         if (temp <= qred * 0.0001D * qred)
/* 1621:     */         {
/* 1622:2066 */           state = 190;
/* 1623:     */         }
/* 1624:     */         else
/* 1625:     */         {
/* 1626:2068 */           temp = Math.sqrt(temp);
/* 1627:2069 */           for (int i = 0; i < n; i++) {
/* 1628:2070 */             if (xbdi.getEntry(i) == 0.0D) {
/* 1629:2071 */               s.setEntry(i, (dredg * this.trialStepPoint.getEntry(i) - dredsq * gnew.getEntry(i)) / temp);
/* 1630:     */             } else {
/* 1631:2073 */               s.setEntry(i, 0.0D);
/* 1632:     */             }
/* 1633:     */           }
/* 1634:2076 */           sredg = -temp;
/* 1635:     */           
/* 1636:     */ 
/* 1637:     */ 
/* 1638:     */ 
/* 1639:     */ 
/* 1640:     */ 
/* 1641:2083 */           angbd = 1.0D;
/* 1642:2084 */           iact = -1;
/* 1643:2085 */           for (int i = 0; i < n; i++) {
/* 1644:2086 */             if (xbdi.getEntry(i) == 0.0D)
/* 1645:     */             {
/* 1646:2087 */               tempa = this.trustRegionCenterOffset.getEntry(i) + this.trialStepPoint.getEntry(i) - this.lowerDifference.getEntry(i);
/* 1647:2088 */               tempb = this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i) - this.trialStepPoint.getEntry(i);
/* 1648:2089 */               if (tempa <= 0.0D)
/* 1649:     */               {
/* 1650:2090 */                 nact++;
/* 1651:2091 */                 xbdi.setEntry(i, -1.0D);
/* 1652:2092 */                 state = 100; break;
/* 1653:     */               }
/* 1654:2093 */               if (tempb <= 0.0D)
/* 1655:     */               {
/* 1656:2094 */                 nact++;
/* 1657:2095 */                 xbdi.setEntry(i, 1.0D);
/* 1658:2096 */                 state = 100; break;
/* 1659:     */               }
/* 1660:2099 */               double d1 = this.trialStepPoint.getEntry(i);
/* 1661:     */               
/* 1662:2101 */               double d2 = s.getEntry(i);
/* 1663:2102 */               double ssq = d1 * d1 + d2 * d2;
/* 1664:     */               
/* 1665:2104 */               d1 = this.trustRegionCenterOffset.getEntry(i) - this.lowerDifference.getEntry(i);
/* 1666:2105 */               temp = ssq - d1 * d1;
/* 1667:2106 */               if (temp > 0.0D)
/* 1668:     */               {
/* 1669:2107 */                 temp = Math.sqrt(temp) - s.getEntry(i);
/* 1670:2108 */                 if (angbd * temp > tempa)
/* 1671:     */                 {
/* 1672:2109 */                   angbd = tempa / temp;
/* 1673:2110 */                   iact = i;
/* 1674:2111 */                   xsav = -1.0D;
/* 1675:     */                 }
/* 1676:     */               }
/* 1677:2115 */               d1 = this.upperDifference.getEntry(i) - this.trustRegionCenterOffset.getEntry(i);
/* 1678:2116 */               temp = ssq - d1 * d1;
/* 1679:2117 */               if (temp > 0.0D)
/* 1680:     */               {
/* 1681:2118 */                 temp = Math.sqrt(temp) + s.getEntry(i);
/* 1682:2119 */                 if (angbd * temp > tempb)
/* 1683:     */                 {
/* 1684:2120 */                   angbd = tempb / temp;
/* 1685:2121 */                   iact = i;
/* 1686:2122 */                   xsav = 1.0D;
/* 1687:     */                 }
/* 1688:     */               }
/* 1689:     */             }
/* 1690:     */           }
/* 1691:2130 */           state = 210;
/* 1692:     */         }
/* 1693:     */         break;
/* 1694:     */       case 150: 
/* 1695:2133 */         printState(150);
/* 1696:2134 */         shs = 0.0D;
/* 1697:2135 */         double dhs = 0.0D;
/* 1698:2136 */         double dhd = 0.0D;
/* 1699:2137 */         for (int i = 0; i < n; i++) {
/* 1700:2138 */           if (xbdi.getEntry(i) == 0.0D)
/* 1701:     */           {
/* 1702:2139 */             shs += s.getEntry(i) * hs.getEntry(i);
/* 1703:2140 */             dhs += this.trialStepPoint.getEntry(i) * hs.getEntry(i);
/* 1704:2141 */             dhd += this.trialStepPoint.getEntry(i) * hred.getEntry(i);
/* 1705:     */           }
/* 1706:     */         }
/* 1707:2149 */         redmax = 0.0D;
/* 1708:2150 */         int isav = -1;
/* 1709:2151 */         redsav = 0.0D;
/* 1710:2152 */         int iu = (int)(angbd * 17.0D + 3.1D);
/* 1711:2153 */         for (int i = 0; i < iu; i++)
/* 1712:     */         {
/* 1713:2154 */           angt = angbd * i / iu;
/* 1714:2155 */           double sth = (angt + angt) / (1.0D + angt * angt);
/* 1715:2156 */           temp = shs + angt * (angt * dhd - dhs - dhs);
/* 1716:2157 */           rednew = sth * (angt * dredg - sredg - 0.5D * sth * temp);
/* 1717:2158 */           if (rednew > redmax)
/* 1718:     */           {
/* 1719:2159 */             redmax = rednew;
/* 1720:2160 */             isav = i;
/* 1721:2161 */             rdprev = redsav;
/* 1722:     */           }
/* 1723:2162 */           else if (i == isav + 1)
/* 1724:     */           {
/* 1725:2163 */             rdnext = rednew;
/* 1726:     */           }
/* 1727:2165 */           redsav = rednew;
/* 1728:     */         }
/* 1729:2171 */         if (isav < 0)
/* 1730:     */         {
/* 1731:2172 */           state = 190;
/* 1732:     */         }
/* 1733:     */         else
/* 1734:     */         {
/* 1735:2174 */           if (isav < iu)
/* 1736:     */           {
/* 1737:2175 */             temp = (rdnext - rdprev) / (redmax + redmax - rdprev - rdnext);
/* 1738:2176 */             angt = angbd * (isav + 0.5D * temp) / iu;
/* 1739:     */           }
/* 1740:2178 */           double cth = (1.0D - angt * angt) / (1.0D + angt * angt);
/* 1741:2179 */           double sth = (angt + angt) / (1.0D + angt * angt);
/* 1742:2180 */           temp = shs + angt * (angt * dhd - dhs - dhs);
/* 1743:2181 */           sdec = sth * (angt * dredg - sredg - 0.5D * sth * temp);
/* 1744:2182 */           if (sdec <= 0.0D)
/* 1745:     */           {
/* 1746:2183 */             state = 190;
/* 1747:     */           }
/* 1748:     */           else
/* 1749:     */           {
/* 1750:2190 */             dredg = 0.0D;
/* 1751:2191 */             gredsq = 0.0D;
/* 1752:2192 */             for (int i = 0; i < n; i++)
/* 1753:     */             {
/* 1754:2193 */               gnew.setEntry(i, gnew.getEntry(i) + (cth - 1.0D) * hred.getEntry(i) + sth * hs.getEntry(i));
/* 1755:2194 */               if (xbdi.getEntry(i) == 0.0D)
/* 1756:     */               {
/* 1757:2195 */                 this.trialStepPoint.setEntry(i, cth * this.trialStepPoint.getEntry(i) + sth * s.getEntry(i));
/* 1758:2196 */                 dredg += this.trialStepPoint.getEntry(i) * gnew.getEntry(i);
/* 1759:     */                 
/* 1760:2198 */                 double d1 = gnew.getEntry(i);
/* 1761:2199 */                 gredsq += d1 * d1;
/* 1762:     */               }
/* 1763:2201 */               hred.setEntry(i, cth * hred.getEntry(i) + sth * hs.getEntry(i));
/* 1764:     */             }
/* 1765:2203 */             qred += sdec;
/* 1766:2204 */             if ((iact >= 0) && (isav == iu))
/* 1767:     */             {
/* 1768:2205 */               nact++;
/* 1769:2206 */               xbdi.setEntry(iact, xsav);
/* 1770:2207 */               state = 100;
/* 1771:     */             }
/* 1772:2213 */             else if (sdec > qred * 0.01D)
/* 1773:     */             {
/* 1774:2214 */               state = 120;
/* 1775:     */             }
/* 1776:     */           }
/* 1777:     */         }
/* 1778:     */         break;
/* 1779:     */       case 190: 
/* 1780:2218 */         printState(190);
/* 1781:2219 */         dsq = 0.0D;
/* 1782:2220 */         for (int i = 0; i < n; i++)
/* 1783:     */         {
/* 1784:2223 */           double min = Math.min(this.trustRegionCenterOffset.getEntry(i) + this.trialStepPoint.getEntry(i), this.upperDifference.getEntry(i));
/* 1785:     */           
/* 1786:2225 */           this.newPoint.setEntry(i, Math.max(min, this.lowerDifference.getEntry(i)));
/* 1787:2226 */           if (xbdi.getEntry(i) == -1.0D) {
/* 1788:2227 */             this.newPoint.setEntry(i, this.lowerDifference.getEntry(i));
/* 1789:     */           }
/* 1790:2229 */           if (xbdi.getEntry(i) == 1.0D) {
/* 1791:2230 */             this.newPoint.setEntry(i, this.upperDifference.getEntry(i));
/* 1792:     */           }
/* 1793:2232 */           this.trialStepPoint.setEntry(i, this.newPoint.getEntry(i) - this.trustRegionCenterOffset.getEntry(i));
/* 1794:     */           
/* 1795:2234 */           double d1 = this.trialStepPoint.getEntry(i);
/* 1796:2235 */           dsq += d1 * d1;
/* 1797:     */         }
/* 1798:2237 */         return new double[] { dsq, crvmin };
/* 1799:     */       case 210: 
/* 1800:2244 */         printState(210);
/* 1801:2245 */         int ih = 0;
/* 1802:2246 */         for (int j = 0; j < n; j++)
/* 1803:     */         {
/* 1804:2247 */           hs.setEntry(j, 0.0D);
/* 1805:2248 */           for (int i = 0; i <= j; i++)
/* 1806:     */           {
/* 1807:2249 */             if (i < j) {
/* 1808:2250 */               hs.setEntry(j, hs.getEntry(j) + this.modelSecondDerivativesValues.getEntry(ih) * s.getEntry(i));
/* 1809:     */             }
/* 1810:2252 */             hs.setEntry(i, hs.getEntry(i) + this.modelSecondDerivativesValues.getEntry(ih) * s.getEntry(j));
/* 1811:2253 */             ih++;
/* 1812:     */           }
/* 1813:     */         }
/* 1814:2256 */         RealVector tmp = this.interpolationPoints.operate(s).ebeMultiply(this.modelSecondDerivativesParameters);
/* 1815:2257 */         for (int k = 0; k < npt; k++) {
/* 1816:2258 */           if (this.modelSecondDerivativesParameters.getEntry(k) != 0.0D) {
/* 1817:2259 */             for (int i = 0; i < n; i++) {
/* 1818:2260 */               hs.setEntry(i, hs.getEntry(i) + tmp.getEntry(k) * this.interpolationPoints.getEntry(k, i));
/* 1819:     */             }
/* 1820:     */           }
/* 1821:     */         }
/* 1822:2264 */         if (crvmin != 0.0D)
/* 1823:     */         {
/* 1824:2265 */           state = 50;
/* 1825:     */         }
/* 1826:2267 */         else if (iterc > itcsav)
/* 1827:     */         {
/* 1828:2268 */           state = 150;
/* 1829:     */         }
/* 1830:     */         else
/* 1831:     */         {
/* 1832:2270 */           for (int i = 0; i < n; i++) {
/* 1833:2271 */             hred.setEntry(i, hs.getEntry(i));
/* 1834:     */           }
/* 1835:2273 */           state = 120;
/* 1836:     */         }
/* 1837:     */         break;
/* 1838:     */       }
/* 1839:     */     }   }
/* 1842:     */   
/* 1843:     */   private void update(double beta, double denom, int knew)
/* 1844:     */   {
/* 1845:2301 */     printMethod();
/* 1846:     */     
/* 1847:2303 */     int n = this.currentBest.getDimension();
/* 1848:2304 */     int npt = this.numberOfInterpolationPoints;
/* 1849:2305 */     int nptm = npt - n - 1;
/* 1850:     */     
/* 1851:     */ 
/* 1852:2308 */     ArrayRealVector work = new ArrayRealVector(npt + n);
/* 1853:     */     
/* 1854:2310 */     double ztest = 0.0D;
/* 1855:2311 */     for (int k = 0; k < npt; k++) {
/* 1856:2312 */       for (int j = 0; j < nptm; j++) {
/* 1857:2314 */         ztest = Math.max(ztest, Math.abs(this.zMatrix.getEntry(k, j)));
/* 1858:     */       }
/* 1859:     */     }
/* 1860:2317 */     ztest *= 1.0E-020D;
/* 1861:2321 */     for (int j = 1; j < nptm; j++)
/* 1862:     */     {
/* 1863:2322 */       double d1 = this.zMatrix.getEntry(knew, j);
/* 1864:2323 */       if (Math.abs(d1) > ztest)
/* 1865:     */       {
/* 1866:2325 */         double d2 = this.zMatrix.getEntry(knew, 0);
/* 1867:     */         
/* 1868:2327 */         double d3 = this.zMatrix.getEntry(knew, j);
/* 1869:2328 */         double d4 = Math.sqrt(d2 * d2 + d3 * d3);
/* 1870:2329 */         double d5 = this.zMatrix.getEntry(knew, 0) / d4;
/* 1871:2330 */         double d6 = this.zMatrix.getEntry(knew, j) / d4;
/* 1872:2331 */         for (int i = 0; i < npt; i++)
/* 1873:     */         {
/* 1874:2332 */           double d7 = d5 * this.zMatrix.getEntry(i, 0) + d6 * this.zMatrix.getEntry(i, j);
/* 1875:2333 */           this.zMatrix.setEntry(i, j, d5 * this.zMatrix.getEntry(i, j) - d6 * this.zMatrix.getEntry(i, 0));
/* 1876:2334 */           this.zMatrix.setEntry(i, 0, d7);
/* 1877:     */         }
/* 1878:     */       }
/* 1879:2337 */       this.zMatrix.setEntry(knew, j, 0.0D);
/* 1880:     */     }
/* 1881:2343 */     for (int i = 0; i < npt; i++) {
/* 1882:2344 */       work.setEntry(i, this.zMatrix.getEntry(knew, 0) * this.zMatrix.getEntry(i, 0));
/* 1883:     */     }
/* 1884:2346 */     double alpha = work.getEntry(knew);
/* 1885:2347 */     double tau = this.lagrangeValuesAtNewPoint.getEntry(knew);
/* 1886:2348 */     this.lagrangeValuesAtNewPoint.setEntry(knew, this.lagrangeValuesAtNewPoint.getEntry(knew) - 1.0D);
/* 1887:     */     
/* 1888:     */ 
/* 1889:     */ 
/* 1890:2352 */     double sqrtDenom = Math.sqrt(denom);
/* 1891:2353 */     double d1 = tau / sqrtDenom;
/* 1892:2354 */     double d2 = this.zMatrix.getEntry(knew, 0) / sqrtDenom;
/* 1893:2355 */     for (int i = 0; i < npt; i++) {
/* 1894:2356 */       this.zMatrix.setEntry(i, 0, d1 * this.zMatrix.getEntry(i, 0) - d2 * this.lagrangeValuesAtNewPoint.getEntry(i));
/* 1895:     */     }
/* 1896:2362 */     for (int j = 0; j < n; j++)
/* 1897:     */     {
/* 1898:2363 */       int jp = npt + j;
/* 1899:2364 */       work.setEntry(jp, this.bMatrix.getEntry(knew, j));
/* 1900:2365 */       double d3 = (alpha * this.lagrangeValuesAtNewPoint.getEntry(jp) - tau * work.getEntry(jp)) / denom;
/* 1901:2366 */       double d4 = (-beta * work.getEntry(jp) - tau * this.lagrangeValuesAtNewPoint.getEntry(jp)) / denom;
/* 1902:2367 */       for (int i = 0; i <= jp; i++)
/* 1903:     */       {
/* 1904:2368 */         this.bMatrix.setEntry(i, j, this.bMatrix.getEntry(i, j) + d3 * this.lagrangeValuesAtNewPoint.getEntry(i) + d4 * work.getEntry(i));
/* 1905:2370 */         if (i >= npt) {
/* 1906:2371 */           this.bMatrix.setEntry(jp, i - npt, this.bMatrix.getEntry(i, j));
/* 1907:     */         }
/* 1908:     */       }
/* 1909:     */     }
/* 1910:     */   }
/* 1911:     */   
/* 1912:     */   private void setup(double[] lowerBound, double[] upperBound)
/* 1913:     */   {
/* 1914:2385 */     printMethod();
/* 1915:     */     
/* 1916:2387 */     double[] init = getStartPoint();
/* 1917:2388 */     int dimension = init.length;
/* 1918:2391 */     if (dimension < 2) {
/* 1919:2392 */       throw new NumberIsTooSmallException(Integer.valueOf(dimension), Integer.valueOf(2), true);
/* 1920:     */     }
/* 1921:2395 */     int[] nPointsInterval = { dimension + 2, (dimension + 2) * (dimension + 1) / 2 };
/* 1922:2396 */     if ((this.numberOfInterpolationPoints < nPointsInterval[0]) || (this.numberOfInterpolationPoints > nPointsInterval[1])) {
/* 1923:2398 */       throw new OutOfRangeException(LocalizedFormats.NUMBER_OF_INTERPOLATION_POINTS, Integer.valueOf(this.numberOfInterpolationPoints), Integer.valueOf(nPointsInterval[0]), Integer.valueOf(nPointsInterval[1]));
/* 1924:     */     }
/* 1925:2405 */     this.boundDifference = new double[dimension];
/* 1926:     */     
/* 1927:2407 */     double requiredMinDiff = 2.0D * this.initialTrustRegionRadius;
/* 1928:2408 */     double minDiff = (1.0D / 0.0D);
/* 1929:2409 */     for (int i = 0; i < dimension; i++)
/* 1930:     */     {
/* 1931:2410 */       this.boundDifference[i] = (upperBound[i] - lowerBound[i]);
/* 1932:2411 */       minDiff = Math.min(minDiff, this.boundDifference[i]);
/* 1933:     */     }
/* 1934:2413 */     if (minDiff < requiredMinDiff) {
/* 1935:2414 */       this.initialTrustRegionRadius = (minDiff / 3.0D);
/* 1936:     */     }
/* 1937:2418 */     this.bMatrix = new Array2DRowRealMatrix(dimension + this.numberOfInterpolationPoints, dimension);
/* 1938:     */     
/* 1939:2420 */     this.zMatrix = new Array2DRowRealMatrix(this.numberOfInterpolationPoints, this.numberOfInterpolationPoints - dimension - 1);
/* 1940:     */     
/* 1941:2422 */     this.interpolationPoints = new Array2DRowRealMatrix(this.numberOfInterpolationPoints, dimension);
/* 1942:     */     
/* 1943:2424 */     this.originShift = new ArrayRealVector(dimension);
/* 1944:2425 */     this.fAtInterpolationPoints = new ArrayRealVector(this.numberOfInterpolationPoints);
/* 1945:2426 */     this.trustRegionCenterOffset = new ArrayRealVector(dimension);
/* 1946:2427 */     this.gradientAtTrustRegionCenter = new ArrayRealVector(dimension);
/* 1947:2428 */     this.lowerDifference = new ArrayRealVector(dimension);
/* 1948:2429 */     this.upperDifference = new ArrayRealVector(dimension);
/* 1949:2430 */     this.modelSecondDerivativesParameters = new ArrayRealVector(this.numberOfInterpolationPoints);
/* 1950:2431 */     this.newPoint = new ArrayRealVector(dimension);
/* 1951:2432 */     this.alternativeNewPoint = new ArrayRealVector(dimension);
/* 1952:2433 */     this.trialStepPoint = new ArrayRealVector(dimension);
/* 1953:2434 */     this.lagrangeValuesAtNewPoint = new ArrayRealVector(dimension + this.numberOfInterpolationPoints);
/* 1954:2435 */     this.modelSecondDerivativesValues = new ArrayRealVector(dimension * (dimension + 1) / 2);
/* 1955:     */   }
/* 1956:     */   
/* 1957:     */   private static double[] fillNewArray(int n, double value)
/* 1958:     */   {
/* 1959:2448 */     double[] ds = new double[n];
/* 1960:2449 */     Arrays.fill(ds, value);
/* 1961:2450 */     return ds;
/* 1962:     */   }
/* 1963:     */   
/* 1964:     */   private static String caller(int n)
/* 1965:     */   {
/* 1966:2455 */     Throwable t = new Throwable();
/* 1967:2456 */     StackTraceElement[] elements = t.getStackTrace();
/* 1968:2457 */     StackTraceElement e = elements[n];
/* 1969:2458 */     return e.getMethodName() + " (at line " + e.getLineNumber() + ")";
/* 1970:     */   }
/* 1971:     */   
/* 1972:     */   private static void printState(int s) {}
/* 1973:     */   
/* 1974:     */   private static void printMethod() {}
/* 1975:     */   
/* 1976:     */   private static class PathIsExploredException
/* 1977:     */     extends RuntimeException
/* 1978:     */   {
/* 1979:     */     private static final long serialVersionUID = 745350979634801853L;
/* 1980:     */     private static final String PATH_IS_EXPLORED = "If this exception is thrown, just remove it from the code";
/* 1981:     */     
/* 1982:     */     PathIsExploredException()
/* 1983:     */     {
/* 1984:2480 */       super();
/* 1985:     */     }
/* 1986:     */   }
/* 1987:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.BOBYQAOptimizer
 * JD-Core Version:    0.7.0.1
 */
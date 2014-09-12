/*    1:     */ package org.apache.commons.math3.linear;
/*    2:     */ 
/*    3:     */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*    4:     */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*    5:     */ import org.apache.commons.math3.exception.NullArgumentException;
/*    6:     */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*    7:     */ import org.apache.commons.math3.util.FastMath;
/*    8:     */ import org.apache.commons.math3.util.IterationManager;
/*    9:     */ import org.apache.commons.math3.util.MathUtils;
/*   10:     */ 
/*   11:     */ public class SymmLQ
/*   12:     */   extends PreconditionedIterativeLinearSolver
/*   13:     */ {
/*   14:     */   private final double delta;
/*   15:     */   private final boolean check;
/*   16:     */   private static final String VECTOR2 = "vector2";
/*   17:     */   private static final String VECTOR1 = "vector1";
/*   18:     */   private static final String VECTOR = "vector";
/*   19:     */   private static final String THRESHOLD = "threshold";
/*   20:     */   private static final String OPERATOR = "operator";
/*   21:     */   
/*   22:     */   private class State
/*   23:     */   {
/*   24:     */     private final RealLinearOperator a;
/*   25:     */     private final RealVector b;
/*   26:     */     private double beta;
/*   27:     */     private double beta1;
/*   28:     */     private double bstep;
/*   29:     */     private double cgnorm;
/*   30:     */     private double dbar;
/*   31:     */     private double gammaZeta;
/*   32:     */     private double gbar;
/*   33:     */     private double gmax;
/*   34:     */     private double gmin;
/*   35:     */     private final boolean goodb;
/*   36:     */     private boolean hasConverged;
/*   37:     */     private double lqnorm;
/*   38:     */     private final RealLinearOperator minv;
/*   39:     */     private double minusEpsZeta;
/*   40:     */     private final RealVector minvb;
/*   41:     */     private double oldb;
/*   42:     */     private RealVector r1;
/*   43:     */     private RealVector r2;
/*   44:     */     private final double shift;
/*   45:     */     private double snprod;
/*   46:     */     private double tnorm;
/*   47:     */     private RealVector wbar;
/*   48:     */     private final RealVector x;
/*   49:     */     private RealVector y;
/*   50:     */     private double ynorm2;
/*   51:     */     
/*   52:     */     public State(RealLinearOperator a, RealLinearOperator minv, RealVector b, RealVector x, boolean goodb, double shift)
/*   53:     */     {
/*   54: 345 */       this.a = a;
/*   55: 346 */       this.minv = minv;
/*   56: 347 */       this.b = b;
/*   57: 348 */       this.x = x;
/*   58: 349 */       this.goodb = goodb;
/*   59: 350 */       this.shift = shift;
/*   60: 351 */       this.minvb = (minv == null ? b : minv.operate(b));
/*   61: 352 */       this.hasConverged = false;
/*   62: 353 */       init();
/*   63:     */     }
/*   64:     */     
/*   65:     */     public void refine(RealVector xRefined)
/*   66:     */     {
/*   67: 364 */       int n = this.x.getDimension();
/*   68: 365 */       if (this.lqnorm < this.cgnorm)
/*   69:     */       {
/*   70: 366 */         if (!this.goodb)
/*   71:     */         {
/*   72: 367 */           xRefined.setSubVector(0, this.x);
/*   73:     */         }
/*   74:     */         else
/*   75:     */         {
/*   76: 369 */           double step = this.bstep / this.beta1;
/*   77: 370 */           for (int i = 0; i < n; i++)
/*   78:     */           {
/*   79: 371 */             double bi = this.minvb.getEntry(i);
/*   80: 372 */             double xi = this.x.getEntry(i);
/*   81: 373 */             xRefined.setEntry(i, xi + step * bi);
/*   82:     */           }
/*   83:     */         }
/*   84:     */       }
/*   85:     */       else
/*   86:     */       {
/*   87: 377 */         double anorm = FastMath.sqrt(this.tnorm);
/*   88: 378 */         double diag = this.gbar == 0.0D ? anorm * SymmLQ.MACH_PREC : this.gbar;
/*   89: 379 */         double zbar = this.gammaZeta / diag;
/*   90: 380 */         double step = (this.bstep + this.snprod * zbar) / this.beta1;
/*   91: 382 */         if (!this.goodb) {
/*   92: 383 */           for (int i = 0; i < n; i++)
/*   93:     */           {
/*   94: 384 */             double xi = this.x.getEntry(i);
/*   95: 385 */             double wi = this.wbar.getEntry(i);
/*   96: 386 */             xRefined.setEntry(i, xi + zbar * wi);
/*   97:     */           }
/*   98:     */         } else {
/*   99: 389 */           for (int i = 0; i < n; i++)
/*  100:     */           {
/*  101: 390 */             double xi = this.x.getEntry(i);
/*  102: 391 */             double wi = this.wbar.getEntry(i);
/*  103: 392 */             double bi = this.minvb.getEntry(i);
/*  104: 393 */             xRefined.setEntry(i, xi + zbar * wi + step * bi);
/*  105:     */           }
/*  106:     */         }
/*  107:     */       }
/*  108:     */     }
/*  109:     */     
/*  110:     */     private void init()
/*  111:     */     {
/*  112: 405 */       this.x.set(0.0D);
/*  113:     */       
/*  114:     */ 
/*  115:     */ 
/*  116:     */ 
/*  117: 410 */       this.r1 = this.b.copy();
/*  118: 411 */       this.y = (this.minv == null ? this.b.copy() : this.minv.operate(this.r1));
/*  119: 412 */       if ((this.minv != null) && (SymmLQ.this.check)) {
/*  120: 413 */         SymmLQ.checkSymmetry(this.minv, this.r1, this.y, this.minv.operate(this.y));
/*  121:     */       }
/*  122: 416 */       this.beta1 = this.r1.dotProduct(this.y);
/*  123: 417 */       if (this.beta1 < 0.0D) {
/*  124: 418 */         SymmLQ.throwNPDLOException(this.minv, this.y);
/*  125:     */       }
/*  126: 420 */       if (this.beta1 == 0.0D) {
/*  127: 422 */         return;
/*  128:     */       }
/*  129: 424 */       this.beta1 = FastMath.sqrt(this.beta1);
/*  130:     */       
/*  131:     */ 
/*  132:     */ 
/*  133:     */ 
/*  134:     */ 
/*  135: 430 */       RealVector v = this.y.mapMultiply(1.0D / this.beta1);
/*  136: 431 */       this.y = this.a.operate(v);
/*  137: 432 */       if (SymmLQ.this.check) {
/*  138: 433 */         SymmLQ.checkSymmetry(this.a, v, this.y, this.a.operate(this.y));
/*  139:     */       }
/*  140: 439 */       SymmLQ.daxpy(-this.shift, v, this.y);
/*  141: 440 */       double alpha = v.dotProduct(this.y);
/*  142: 441 */       SymmLQ.daxpy(-alpha / this.beta1, this.r1, this.y);
/*  143:     */       
/*  144:     */ 
/*  145:     */ 
/*  146:     */ 
/*  147:     */ 
/*  148:     */ 
/*  149: 448 */       double vty = v.dotProduct(this.y);
/*  150: 449 */       double vtv = v.dotProduct(v);
/*  151: 450 */       SymmLQ.daxpy(-vty / vtv, v, this.y);
/*  152: 451 */       this.r2 = this.y.copy();
/*  153: 452 */       if (this.minv != null) {
/*  154: 453 */         this.y = this.minv.operate(this.r2);
/*  155:     */       }
/*  156: 455 */       this.oldb = this.beta1;
/*  157: 456 */       this.beta = this.r2.dotProduct(this.y);
/*  158: 457 */       if (this.beta < 0.0D) {
/*  159: 458 */         SymmLQ.throwNPDLOException(this.minv, this.y);
/*  160:     */       }
/*  161: 460 */       this.beta = FastMath.sqrt(this.beta);
/*  162:     */       
/*  163:     */ 
/*  164:     */ 
/*  165:     */ 
/*  166:     */ 
/*  167:     */ 
/*  168:     */ 
/*  169: 468 */       this.cgnorm = this.beta1;
/*  170: 469 */       this.gbar = alpha;
/*  171: 470 */       this.dbar = this.beta;
/*  172: 471 */       this.gammaZeta = this.beta1;
/*  173: 472 */       this.minusEpsZeta = 0.0D;
/*  174: 473 */       this.bstep = 0.0D;
/*  175: 474 */       this.snprod = 1.0D;
/*  176: 475 */       this.tnorm = (alpha * alpha + this.beta * this.beta);
/*  177: 476 */       this.ynorm2 = 0.0D;
/*  178: 477 */       this.gmax = (FastMath.abs(alpha) + SymmLQ.MACH_PREC);
/*  179: 478 */       this.gmin = this.gmax;
/*  180: 480 */       if (this.goodb)
/*  181:     */       {
/*  182: 481 */         this.wbar = new ArrayRealVector(this.a.getRowDimension());
/*  183: 482 */         this.wbar.set(0.0D);
/*  184:     */       }
/*  185:     */       else
/*  186:     */       {
/*  187: 484 */         this.wbar = v;
/*  188:     */       }
/*  189: 486 */       updateNorms();
/*  190:     */     }
/*  191:     */     
/*  192:     */     private void update()
/*  193:     */     {
/*  194: 496 */       RealVector v = this.y.mapMultiply(1.0D / this.beta);
/*  195: 497 */       this.y = this.a.operate(v);
/*  196: 498 */       SymmLQ.daxpbypz(-this.shift, v, -this.beta / this.oldb, this.r1, this.y);
/*  197: 499 */       double alpha = v.dotProduct(this.y);
/*  198:     */       
/*  199:     */ 
/*  200:     */ 
/*  201:     */ 
/*  202:     */ 
/*  203:     */ 
/*  204:     */ 
/*  205:     */ 
/*  206:     */ 
/*  207:     */ 
/*  208: 510 */       SymmLQ.daxpy(-alpha / this.beta, this.r2, this.y);
/*  209:     */       
/*  210:     */ 
/*  211:     */ 
/*  212:     */ 
/*  213:     */ 
/*  214:     */ 
/*  215:     */ 
/*  216:     */ 
/*  217:     */ 
/*  218:     */ 
/*  219:     */ 
/*  220:     */ 
/*  221:     */ 
/*  222: 524 */       this.r1 = this.r2;
/*  223: 525 */       this.r2 = this.y;
/*  224: 526 */       if (this.minv != null) {
/*  225: 527 */         this.y = this.minv.operate(this.r2);
/*  226:     */       }
/*  227: 529 */       this.oldb = this.beta;
/*  228: 530 */       this.beta = this.r2.dotProduct(this.y);
/*  229: 531 */       if (this.beta < 0.0D) {
/*  230: 532 */         SymmLQ.throwNPDLOException(this.minv, this.y);
/*  231:     */       }
/*  232: 534 */       this.beta = FastMath.sqrt(this.beta);
/*  233:     */       
/*  234:     */ 
/*  235:     */ 
/*  236:     */ 
/*  237:     */ 
/*  238:     */ 
/*  239:     */ 
/*  240:     */ 
/*  241: 543 */       this.tnorm += alpha * alpha + this.oldb * this.oldb + this.beta * this.beta;
/*  242:     */       
/*  243:     */ 
/*  244:     */ 
/*  245:     */ 
/*  246:     */ 
/*  247:     */ 
/*  248:     */ 
/*  249: 551 */       double gamma = FastMath.sqrt(this.gbar * this.gbar + this.oldb * this.oldb);
/*  250: 552 */       double c = this.gbar / gamma;
/*  251: 553 */       double s = this.oldb / gamma;
/*  252:     */       
/*  253:     */ 
/*  254:     */ 
/*  255:     */ 
/*  256:     */ 
/*  257:     */ 
/*  258:     */ 
/*  259:     */ 
/*  260:     */ 
/*  261: 563 */       double deltak = c * this.dbar + s * alpha;
/*  262: 564 */       this.gbar = (s * this.dbar - c * alpha);
/*  263: 565 */       double eps = s * this.beta;
/*  264: 566 */       this.dbar = (-c * this.beta);
/*  265: 567 */       double zeta = this.gammaZeta / gamma;
/*  266:     */       
/*  267:     */ 
/*  268:     */ 
/*  269:     */ 
/*  270:     */ 
/*  271:     */ 
/*  272:     */ 
/*  273:     */ 
/*  274: 576 */       double zetaC = zeta * c;
/*  275: 577 */       double zetaS = zeta * s;
/*  276: 578 */       int n = this.x.getDimension();
/*  277: 579 */       for (int i = 0; i < n; i++)
/*  278:     */       {
/*  279: 580 */         double xi = this.x.getEntry(i);
/*  280: 581 */         double vi = v.getEntry(i);
/*  281: 582 */         double wi = this.wbar.getEntry(i);
/*  282: 583 */         this.x.setEntry(i, xi + wi * zetaC + vi * zetaS);
/*  283: 584 */         this.wbar.setEntry(i, wi * s - vi * c);
/*  284:     */       }
/*  285: 592 */       this.bstep += this.snprod * c * zeta;
/*  286: 593 */       this.snprod *= s;
/*  287: 594 */       this.gmax = FastMath.max(this.gmax, gamma);
/*  288: 595 */       this.gmin = FastMath.min(this.gmin, gamma);
/*  289: 596 */       this.ynorm2 += zeta * zeta;
/*  290: 597 */       this.gammaZeta = (this.minusEpsZeta - deltak * zeta);
/*  291: 598 */       this.minusEpsZeta = (-eps * zeta);
/*  292:     */       
/*  293:     */ 
/*  294:     */ 
/*  295:     */ 
/*  296:     */ 
/*  297:     */ 
/*  298:     */ 
/*  299:     */ 
/*  300:     */ 
/*  301:     */ 
/*  302:     */ 
/*  303:     */ 
/*  304: 611 */       updateNorms();
/*  305:     */     }
/*  306:     */     
/*  307:     */     private void updateNorms()
/*  308:     */     {
/*  309: 619 */       double anorm = FastMath.sqrt(this.tnorm);
/*  310: 620 */       double ynorm = FastMath.sqrt(this.ynorm2);
/*  311: 621 */       double epsa = anorm * SymmLQ.MACH_PREC;
/*  312: 622 */       double epsx = anorm * ynorm * SymmLQ.MACH_PREC;
/*  313: 623 */       double epsr = anorm * ynorm * SymmLQ.this.delta;
/*  314: 624 */       double diag = this.gbar == 0.0D ? epsa : this.gbar;
/*  315: 625 */       this.lqnorm = FastMath.sqrt(this.gammaZeta * this.gammaZeta + this.minusEpsZeta * this.minusEpsZeta);
/*  316:     */       
/*  317: 627 */       double qrnorm = this.snprod * this.beta1;
/*  318: 628 */       this.cgnorm = (qrnorm * this.beta / FastMath.abs(diag));
/*  319:     */       double acond;
/*  320:     */  
/*  321: 637 */       if (this.lqnorm <= this.cgnorm) {
/*  322: 638 */         acond = this.gmax / this.gmin;
/*  323:     */       } else {
/*  324: 640 */         acond = this.gmax / FastMath.min(this.gmin, FastMath.abs(diag));
/*  325:     */       }
/*  326: 642 */       if (acond * SymmLQ.MACH_PREC >= 0.1D) {
/*  327: 643 */         throw new IllConditionedOperatorException(acond);
/*  328:     */       }
/*  329: 645 */       if (this.beta1 <= epsx) {
/*  330: 650 */         throw new SingularOperatorException();
/*  331:     */       }
/*  332: 652 */       this.hasConverged = ((this.cgnorm <= epsx) || (this.cgnorm <= epsr));
/*  333:     */     }
/*  334:     */
public int access$800(State state) {
	// TODO Auto-generated method stub
	return 0;
}
public RealVector access$900(State state) {
	// TODO Auto-generated method stub
	return null;
}
public DimensionMismatchException access$1000(State state) {
	// TODO Auto-generated method stub
	return null;
}   }
/*  335:     */   
/*  336:     */   private static class SymmLQEvent
/*  337:     */     extends IterativeLinearSolverEvent
/*  338:     */   {
/*  339:     */     private static final long serialVersionUID = 2012012801L;
/*  340:     */     private final transient SymmLQ.State state;
/*  341:     */     
/*  342:     */     public SymmLQEvent(SymmLQ source, SymmLQ.State state)
/*  343:     */     {
/*  344: 681 */       super(source.getIterationManager().getIterations(), 0);
/*  345: 682 */       this.state = state;
/*  346:     */     }
/*  347:     */     
/*  348:     */     public int getIterations()
/*  349:     */     {
/*  350: 688 */       return ((SymmLQ)getSource()).getIterationManager().getIterations();
/*  351:     */     }
/*  352:     */     
/*  353:     */     public double getNormOfResidual()
/*  354:     */     {
	return iterations;
/*  355: 694 */      // return FastMath.min(SymmLQ.State.access$800(this.state), SymmLQ.State.access$800(this.state));
/*  356:     */     }
/*  357:     */     
/*  358:     */     public RealVector getRightHandSideVector()
/*  359:     */     {
	return null;
/*  360: 700 */      // return RealVector.unmodifiableRealVector(SymmLQ.State.access$900(this.state));
/*  361:     */     }
/*  362:     */     
/*  363:     */     public RealVector getSolution()
/*  364:     */     {
double[] n = null;
/*  365: 706 */       //int n = SymmLQ.State.access$1000(this.state).getDimension();
/*  366: 707 */       RealVector x = new ArrayRealVector(n);
/*  367: 708 */       this.state.refine(x);
/*  368: 709 */       return x;
/*  369:     */     }
/*  370:     */   }
/*  371:     */   
/*  372:     */   public SymmLQ(int maxIterations, double delta, boolean check)
/*  373:     */   {
/*  374: 755 */     super(maxIterations);
/*  375: 756 */     this.delta = delta;
/*  376: 757 */     this.check = check;
/*  377:     */   }
/*  378:     */   
/*  379:     */   public SymmLQ(IterationManager manager, double delta, boolean check)
/*  380:     */   {
/*  381: 773 */     super(manager);
/*  382: 774 */     this.delta = delta;
/*  383: 775 */     this.check = check;
/*  384:     */   }
/*  385:     */   
/*  386: 779 */   private static final double MACH_PREC = Math.ulp(1.0D);
/*  387: 780 */   private static final double CBRT_MACH_PREC = Math.cbrt(MACH_PREC);
/*  388:     */   
/*  389:     */   private static void checkSymmetry(RealLinearOperator l, RealVector x, RealVector y, RealVector z)
/*  390:     */     throws NonSelfAdjointOperatorException
/*  391:     */   {
/*  392: 799 */     double s = y.dotProduct(y);
/*  393: 800 */     double t = x.dotProduct(z);
/*  394: 801 */     double epsa = (s + MACH_PREC) * CBRT_MACH_PREC;
/*  395: 802 */     if (FastMath.abs(s - t) > epsa)
/*  396:     */     {
/*  397: 804 */       NonSelfAdjointOperatorException e = new NonSelfAdjointOperatorException();
/*  398: 805 */       ExceptionContext context = e.getContext();
/*  399: 806 */       context.setValue("operator", l);
/*  400: 807 */       context.setValue("vector1", x);
/*  401: 808 */       context.setValue("vector2", y);
/*  402: 809 */       context.setValue("threshold", Double.valueOf(epsa));
/*  403: 810 */       throw e;
/*  404:     */     }
/*  405:     */   }
/*  406:     */   
/*  407:     */   private static void daxpbypz(double a, RealVector x, double b, RealVector y, RealVector z)
/*  408:     */   {
/*  409: 827 */     int n = z.getDimension();
/*  410: 828 */     for (int i = 0; i < n; i++)
/*  411:     */     {
/*  412: 830 */       double zi = a * x.getEntry(i) + b * y.getEntry(i) + z.getEntry(i);
/*  413: 831 */       z.setEntry(i, zi);
/*  414:     */     }
/*  415:     */   }
/*  416:     */   
/*  417:     */   private static void daxpy(double a, RealVector x, RealVector y)
/*  418:     */   {
/*  419: 846 */     int n = x.getDimension();
/*  420: 847 */     for (int i = 0; i < n; i++) {
/*  421: 848 */       y.setEntry(i, a * x.getEntry(i) + y.getEntry(i));
/*  422:     */     }
/*  423:     */   }
/*  424:     */   
/*  425:     */   private static void throwNPDLOException(RealLinearOperator l, RealVector v)
/*  426:     */     throws NonPositiveDefiniteOperatorException
/*  427:     */   {
/*  428: 863 */     NonPositiveDefiniteOperatorException e = new NonPositiveDefiniteOperatorException();
/*  429: 864 */     ExceptionContext context = e.getContext();
/*  430: 865 */     context.setValue("operator", l);
/*  431: 866 */     context.setValue("vector", v);
/*  432: 867 */     throw e;
/*  433:     */   }
/*  434:     */   
/*  435:     */   public final boolean getCheck()
/*  436:     */   {
/*  437: 877 */     return this.check;
/*  438:     */   }
/*  439:     */   
/*  440:     */   public RealVector solve(RealLinearOperator a, RealLinearOperator minv, RealVector b)
/*  441:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException
/*  442:     */   {
/*  443: 896 */     MathUtils.checkNotNull(a);
/*  444: 897 */     RealVector x = new ArrayRealVector(a.getColumnDimension());
/*  445: 898 */     return solveInPlace(a, minv, b, x, false, 0.0D);
/*  446:     */   }
/*  447:     */   
/*  448:     */   public RealVector solve(RealLinearOperator a, RealLinearOperator minv, RealVector b, boolean goodb, double shift)
/*  449:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException
/*  450:     */   {
/*  451: 949 */     MathUtils.checkNotNull(a);
/*  452: 950 */     RealVector x = new ArrayRealVector(a.getColumnDimension());
/*  453: 951 */     return solveInPlace(a, minv, b, x, goodb, shift);
/*  454:     */   }
/*  455:     */   
/*  456:     */   public RealVector solve(RealLinearOperator a, RealLinearOperator minv, RealVector b, RealVector x)
/*  457:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException, MaxCountExceededException
/*  458:     */   {
/*  459: 972 */     MathUtils.checkNotNull(x);
/*  460: 973 */     return solveInPlace(a, minv, b, x.copy(), false, 0.0D);
/*  461:     */   }
/*  462:     */   
/*  463:     */   public RealVector solve(RealLinearOperator a, RealVector b)
/*  464:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException
/*  465:     */   {
/*  466: 988 */     MathUtils.checkNotNull(a);
/*  467: 989 */     RealVector x = new ArrayRealVector(a.getColumnDimension());
/*  468: 990 */     x.set(0.0D);
/*  469: 991 */     return solveInPlace(a, null, b, x, false, 0.0D);
/*  470:     */   }
/*  471:     */   
/*  472:     */   public RealVector solve(RealLinearOperator a, RealVector b, boolean goodb, double shift)
/*  473:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException
/*  474:     */   {
/*  475:1034 */     MathUtils.checkNotNull(a);
/*  476:1035 */     RealVector x = new ArrayRealVector(a.getColumnDimension());
/*  477:1036 */     return solveInPlace(a, null, b, x, goodb, shift);
/*  478:     */   }
/*  479:     */   
/*  480:     */   public RealVector solve(RealLinearOperator a, RealVector b, RealVector x)
/*  481:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException
/*  482:     */   {
/*  483:1054 */     MathUtils.checkNotNull(x);
/*  484:1055 */     return solveInPlace(a, null, b, x.copy(), false, 0.0D);
/*  485:     */   }
/*  486:     */   
/*  487:     */   public RealVector solveInPlace(RealLinearOperator a, RealLinearOperator minv, RealVector b, RealVector x)
/*  488:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException, MaxCountExceededException
/*  489:     */   {
/*  490:1076 */     return solveInPlace(a, minv, b, x, false, 0.0D);
/*  491:     */   }
/*  492:     */   
/*  493:     */   public RealVector solveInPlace(RealLinearOperator a, RealLinearOperator minv, RealVector b, RealVector x, boolean goodb, double shift)
/*  494:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException, MaxCountExceededException
/*  495:     */   {
/*  496:1130 */     checkParameters(a, minv, b, x);
/*  497:     */     
/*  498:1132 */     IterationManager manager = getIterationManager();
/*  499:     */     
/*  500:1134 */     manager.resetIterationCount();
/*  501:1135 */     manager.incrementIterationCount();
/*  502:     */     
/*  503:1137 */     State state = new State(a, minv, b, x, goodb, shift);
/*  504:1138 */     IterativeLinearSolverEvent event = new SymmLQEvent(this, state);
/*  505:1139 */     if (state.beta1 == 0.0D)
/*  506:     */     {
/*  507:1141 */       manager.fireTerminationEvent(event);
/*  508:1142 */       return x;
/*  509:     */     }
/*  510:1146 */     boolean earlyStop = (state.beta < MACH_PREC) || (state.hasConverged);
/*  511:1147 */     manager.fireInitializationEvent(event);
/*  512:1148 */     if (!earlyStop) {
/*  513:     */       do
/*  514:     */       {
/*  515:1150 */         manager.incrementIterationCount();
/*  516:1151 */         manager.fireIterationStartedEvent(event);
/*  517:1152 */         state.update();
/*  518:1153 */         manager.fireIterationPerformedEvent(event);
/*  519:1154 */       } while (!state.hasConverged);
/*  520:     */     }
/*  521:1156 */     state.refine(x);
/*  522:     */     
/*  523:     */ 
/*  524:     */ 
/*  525:     */ 
/*  526:     */ 
/*  527:1162 */     state.bstep = 0.0D;
/*  528:1163 */     state.gammaZeta = 0.0D;
/*  529:1164 */     manager.fireTerminationEvent(event);
/*  530:1165 */     return x;
/*  531:     */   }
/*  532:     */   
/*  533:     */   public RealVector solveInPlace(RealLinearOperator a, RealVector b, RealVector x)
/*  534:     */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException
/*  535:     */   {
/*  536:1183 */     return solveInPlace(a, null, b, x, false, 0.0D);
/*  537:     */   }
/*  538:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.SymmLQ
 * JD-Core Version:    0.7.0.1
 */
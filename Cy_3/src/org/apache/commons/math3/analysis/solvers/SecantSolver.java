/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class SecantSolver
/*   6:    */   extends AbstractUnivariateSolver
/*   7:    */ {
/*   8:    */   protected static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*   9:    */   
/*  10:    */   public SecantSolver()
/*  11:    */   {
/*  12: 48 */     super(1.0E-006D);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public SecantSolver(double absoluteAccuracy)
/*  16:    */   {
/*  17: 57 */     super(absoluteAccuracy);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public SecantSolver(double relativeAccuracy, double absoluteAccuracy)
/*  21:    */   {
/*  22: 68 */     super(relativeAccuracy, absoluteAccuracy);
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected final double doSolve()
/*  26:    */   {
/*  27: 75 */     double x0 = getMin();
/*  28: 76 */     double x1 = getMax();
/*  29: 77 */     double f0 = computeObjectiveValue(x0);
/*  30: 78 */     double f1 = computeObjectiveValue(x1);
/*  31: 83 */     if (f0 == 0.0D) {
/*  32: 84 */       return x0;
/*  33:    */     }
/*  34: 86 */     if (f1 == 0.0D) {
/*  35: 87 */       return x1;
/*  36:    */     }
/*  37: 91 */     verifyBracketing(x0, x1);
/*  38:    */     
/*  39:    */ 
/*  40: 94 */     double ftol = getFunctionValueAccuracy();
/*  41: 95 */     double atol = getAbsoluteAccuracy();
/*  42: 96 */     double rtol = getRelativeAccuracy();
/*  43:    */     for (;;)
/*  44:    */     {
/*  45:101 */       double x = x1 - f1 * (x1 - x0) / (f1 - f0);
/*  46:102 */       double fx = computeObjectiveValue(x);
/*  47:107 */       if (fx == 0.0D) {
/*  48:108 */         return x;
/*  49:    */       }
/*  50:112 */       x0 = x1;
/*  51:113 */       f0 = f1;
/*  52:114 */       x1 = x;
/*  53:115 */       f1 = fx;
/*  54:120 */       if (FastMath.abs(f1) <= ftol) {
/*  55:121 */         return x1;
/*  56:    */       }
/*  57:126 */       if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
/*  58:127 */         return x1;
/*  59:    */       }
/*  60:    */     }
/*  61:    */   }
/*  62:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.SecantSolver
 * JD-Core Version:    0.7.0.1
 */
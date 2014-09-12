/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ import org.apache.commons.math3.util.FastMath;
/*  5:   */ 
/*  6:   */ public class NewtonSolver
/*  7:   */   extends AbstractDifferentiableUnivariateSolver
/*  8:   */ {
/*  9:   */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/* 10:   */   
/* 11:   */   public NewtonSolver()
/* 12:   */   {
/* 13:39 */     this(1.0E-006D);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public NewtonSolver(double absoluteAccuracy)
/* 17:   */   {
/* 18:47 */     super(absoluteAccuracy);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public double solve(int maxEval, DifferentiableUnivariateFunction f, double min, double max)
/* 22:   */   {
/* 23:66 */     return super.solve(maxEval, f, UnivariateSolverUtils.midpoint(min, max));
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected double doSolve()
/* 27:   */   {
/* 28:74 */     double startValue = getStartValue();
/* 29:75 */     double absoluteAccuracy = getAbsoluteAccuracy();
/* 30:   */     
/* 31:77 */     double x0 = startValue;
/* 32:   */     for (;;)
/* 33:   */     {
/* 34:80 */       double x1 = x0 - computeObjectiveValue(x0) / computeDerivativeObjectiveValue(x0);
/* 35:81 */       if (FastMath.abs(x1 - x0) <= absoluteAccuracy) {
/* 36:82 */         return x1;
/* 37:   */       }
/* 38:85 */       x0 = x1;
/* 39:   */     }
/* 40:   */   }
/* 41:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.NewtonSolver
 * JD-Core Version:    0.7.0.1
 */
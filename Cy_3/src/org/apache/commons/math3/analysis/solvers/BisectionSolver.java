/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.FastMath;
/*  4:   */ 
/*  5:   */ public class BisectionSolver
/*  6:   */   extends AbstractUnivariateSolver
/*  7:   */ {
/*  8:   */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*  9:   */   
/* 10:   */   public BisectionSolver()
/* 11:   */   {
/* 12:37 */     this(1.0E-006D);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public BisectionSolver(double absoluteAccuracy)
/* 16:   */   {
/* 17:45 */     super(absoluteAccuracy);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public BisectionSolver(double relativeAccuracy, double absoluteAccuracy)
/* 21:   */   {
/* 22:55 */     super(relativeAccuracy, absoluteAccuracy);
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected double doSolve()
/* 26:   */   {
/* 27:63 */     double min = getMin();
/* 28:64 */     double max = getMax();
/* 29:65 */     verifyInterval(min, max);
/* 30:66 */     double absoluteAccuracy = getAbsoluteAccuracy();
/* 31:   */     do
/* 32:   */     {
/* 33:72 */       double m = UnivariateSolverUtils.midpoint(min, max);
/* 34:73 */       double fmin = computeObjectiveValue(min);
/* 35:74 */       double fm = computeObjectiveValue(m);
/* 36:76 */       if (fm * fmin > 0.0D) {
/* 37:78 */         min = m;
/* 38:   */       } else {
/* 39:81 */         max = m;
/* 40:   */       }
/* 41:84 */     } while (FastMath.abs(max - min) > absoluteAccuracy);
/* 42:85 */     double m = UnivariateSolverUtils.midpoint(min, max);
/* 43:86 */     return m;
/* 44:   */   }
/* 45:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.BisectionSolver
 * JD-Core Version:    0.7.0.1
 */
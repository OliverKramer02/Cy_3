/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ public class RegulaFalsiSolver
/*  4:   */   extends BaseSecantSolver
/*  5:   */ {
/*  6:   */   public RegulaFalsiSolver()
/*  7:   */   {
/*  8:60 */     super(1.0E-006D, BaseSecantSolver.Method.REGULA_FALSI);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public RegulaFalsiSolver(double absoluteAccuracy)
/* 12:   */   {
/* 13:69 */     super(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public RegulaFalsiSolver(double relativeAccuracy, double absoluteAccuracy)
/* 17:   */   {
/* 18:80 */     super(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public RegulaFalsiSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/* 22:   */   {
/* 23:93 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
/* 24:   */   }
/* 25:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver
 * JD-Core Version:    0.7.0.1
 */
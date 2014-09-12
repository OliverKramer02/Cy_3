/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ public class IllinoisSolver
/*  4:   */   extends BaseSecantSolver
/*  5:   */ {
/*  6:   */   public IllinoisSolver()
/*  7:   */   {
/*  8:48 */     super(1.0E-006D, BaseSecantSolver.Method.ILLINOIS);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public IllinoisSolver(double absoluteAccuracy)
/* 12:   */   {
/* 13:57 */     super(absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public IllinoisSolver(double relativeAccuracy, double absoluteAccuracy)
/* 17:   */   {
/* 18:68 */     super(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public IllinoisSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/* 22:   */   {
/* 23:81 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
/* 24:   */   }
/* 25:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.IllinoisSolver
 * JD-Core Version:    0.7.0.1
 */
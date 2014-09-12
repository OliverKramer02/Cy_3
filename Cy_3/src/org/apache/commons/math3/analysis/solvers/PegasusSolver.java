/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ public class PegasusSolver
/*  4:   */   extends BaseSecantSolver
/*  5:   */ {
/*  6:   */   public PegasusSolver()
/*  7:   */   {
/*  8:50 */     super(1.0E-006D, BaseSecantSolver.Method.PEGASUS);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public PegasusSolver(double absoluteAccuracy)
/* 12:   */   {
/* 13:59 */     super(absoluteAccuracy, BaseSecantSolver.Method.PEGASUS);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public PegasusSolver(double relativeAccuracy, double absoluteAccuracy)
/* 17:   */   {
/* 18:70 */     super(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.PEGASUS);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public PegasusSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/* 22:   */   {
/* 23:83 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
/* 24:   */   }
/* 25:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.PegasusSolver
 * JD-Core Version:    0.7.0.1
 */
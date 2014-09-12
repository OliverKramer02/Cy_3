/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  4:   */ 
/*  5:   */ public abstract class AbstractUnivariateSolver
/*  6:   */   extends BaseAbstractUnivariateSolver<UnivariateFunction>
/*  7:   */   implements UnivariateSolver
/*  8:   */ {
/*  9:   */   protected AbstractUnivariateSolver(double absoluteAccuracy)
/* 10:   */   {
/* 11:37 */     super(absoluteAccuracy);
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected AbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy)
/* 15:   */   {
/* 16:47 */     super(relativeAccuracy, absoluteAccuracy);
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected AbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/* 20:   */   {
/* 21:59 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
/* 22:   */   }
/* 23:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.AbstractUnivariateSolver
 * JD-Core Version:    0.7.0.1
 */
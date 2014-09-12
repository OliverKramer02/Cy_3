/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  5:   */ 
/*  6:   */ public abstract class AbstractDifferentiableUnivariateSolver
/*  7:   */   extends BaseAbstractUnivariateSolver<DifferentiableUnivariateFunction>
/*  8:   */   implements DifferentiableUnivariateSolver
/*  9:   */ {
/* 10:   */   private UnivariateFunction functionDerivative;
/* 11:   */   
/* 12:   */   protected AbstractDifferentiableUnivariateSolver(double absoluteAccuracy)
/* 13:   */   {
/* 14:42 */     super(absoluteAccuracy);
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected AbstractDifferentiableUnivariateSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/* 18:   */   {
/* 19:55 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected double computeDerivativeObjectiveValue(double point)
/* 23:   */   {
/* 24:67 */     incrementEvaluationCount();
/* 25:68 */     return this.functionDerivative.value(point);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected void setup(int maxEval, DifferentiableUnivariateFunction f, double min, double max, double startValue)
/* 29:   */   {
/* 30:77 */     super.setup(maxEval, f, min, max, startValue);
/* 31:78 */     this.functionDerivative = f.derivative();
/* 32:   */   }
/* 33:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.AbstractDifferentiableUnivariateSolver
 * JD-Core Version:    0.7.0.1
 */
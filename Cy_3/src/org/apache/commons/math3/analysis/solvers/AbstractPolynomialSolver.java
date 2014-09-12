/*  1:   */ package org.apache.commons.math3.analysis.solvers;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
/*  4:   */ 
/*  5:   */ public abstract class AbstractPolynomialSolver
/*  6:   */   extends BaseAbstractUnivariateSolver<PolynomialFunction>
/*  7:   */   implements PolynomialSolver
/*  8:   */ {
/*  9:   */   private PolynomialFunction polynomialFunction;
/* 10:   */   
/* 11:   */   protected AbstractPolynomialSolver(double absoluteAccuracy)
/* 12:   */   {
/* 13:40 */     super(absoluteAccuracy);
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected AbstractPolynomialSolver(double relativeAccuracy, double absoluteAccuracy)
/* 17:   */   {
/* 18:50 */     super(relativeAccuracy, absoluteAccuracy);
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected AbstractPolynomialSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/* 22:   */   {
/* 23:62 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected void setup(int maxEval, PolynomialFunction f, double min, double max, double startValue)
/* 27:   */   {
/* 28:71 */     super.setup(maxEval, f, min, max, startValue);
/* 29:72 */     this.polynomialFunction = f;
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected double[] getCoefficients()
/* 33:   */   {
/* 34:79 */     return this.polynomialFunction.getCoefficients();
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.AbstractPolynomialSolver
 * JD-Core Version:    0.7.0.1
 */
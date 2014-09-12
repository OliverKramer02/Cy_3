/*  1:   */ package org.apache.commons.math3.optimization.fitting;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction.Parametric;
/*  4:   */ import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
/*  5:   */ 
/*  6:   */ public class PolynomialFitter
/*  7:   */   extends CurveFitter
/*  8:   */ {
/*  9:   */   private final int degree;
/* 10:   */   
/* 11:   */   public PolynomialFitter(int degree, DifferentiableMultivariateVectorOptimizer optimizer)
/* 12:   */   {
/* 13:44 */     super(optimizer);
/* 14:45 */     this.degree = degree;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public double[] fit()
/* 18:   */   {
/* 19:56 */     return fit(new PolynomialFunction.Parametric(), new double[this.degree + 1]);
/* 20:   */   }
/* 21:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.fitting.PolynomialFitter
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.analysis.interpolation;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
/*  4:   */ import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
/*  5:   */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  6:   */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  7:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  8:   */ import org.apache.commons.math3.util.MathArrays;
/*  9:   */ 
/* 10:   */ public class LinearInterpolator
/* 11:   */   implements UnivariateInterpolator
/* 12:   */ {
/* 13:   */   public PolynomialSplineFunction interpolate(double[] x, double[] y)
/* 14:   */   {
/* 15:44 */     if (x.length != y.length) {
/* 16:45 */       throw new DimensionMismatchException(x.length, y.length);
/* 17:   */     }
/* 18:48 */     if (x.length < 2) {
/* 19:49 */       throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(x.length), Integer.valueOf(2), true);
/* 20:   */     }
/* 21:54 */     int n = x.length - 1;
/* 22:   */     
/* 23:56 */     MathArrays.checkOrder(x);
/* 24:   */     
/* 25:   */ 
/* 26:59 */     double[] m = new double[n];
/* 27:60 */     for (int i = 0; i < n; i++) {
/* 28:61 */       m[i] = ((y[(i + 1)] - y[i]) / (x[(i + 1)] - x[i]));
/* 29:   */     }
/* 30:64 */     PolynomialFunction[] polynomials = new PolynomialFunction[n];
/* 31:65 */     double[] coefficients = new double[2];
/* 32:66 */     for (int i = 0; i < n; i++)
/* 33:   */     {
/* 34:67 */       coefficients[0] = y[i];
/* 35:68 */       coefficients[1] = m[i];
/* 36:69 */       polynomials[i] = new PolynomialFunction(coefficients);
/* 37:   */     }
/* 38:72 */     return new PolynomialSplineFunction(x, polynomials);
/* 39:   */   }
/* 40:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.LinearInterpolator
 * JD-Core Version:    0.7.0.1
 */
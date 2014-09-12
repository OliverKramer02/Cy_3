/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
/*   4:    */ import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.util.MathArrays;
/*   9:    */ 
/*  10:    */ public class SplineInterpolator
/*  11:    */   implements UnivariateInterpolator
/*  12:    */ {
/*  13:    */   public PolynomialSplineFunction interpolate(double[] x, double[] y)
/*  14:    */   {
/*  15: 68 */     if (x.length != y.length) {
/*  16: 69 */       throw new DimensionMismatchException(x.length, y.length);
/*  17:    */     }
/*  18: 72 */     if (x.length < 3) {
/*  19: 73 */       throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(x.length), Integer.valueOf(3), true);
/*  20:    */     }
/*  21: 78 */     int n = x.length - 1;
/*  22:    */     
/*  23: 80 */     MathArrays.checkOrder(x);
/*  24:    */     
/*  25:    */ 
/*  26: 83 */     double[] h = new double[n];
/*  27: 84 */     for (int i = 0; i < n; i++) {
/*  28: 85 */       h[i] = (x[(i + 1)] - x[i]);
/*  29:    */     }
/*  30: 88 */     double[] mu = new double[n];
/*  31: 89 */     double[] z = new double[n + 1];
/*  32: 90 */     mu[0] = 0.0D;
/*  33: 91 */     z[0] = 0.0D;
/*  34: 92 */     double g = 0.0D;
/*  35: 93 */     for (int i = 1; i < n; i++)
/*  36:    */     {
/*  37: 94 */       g = 2.0D * (x[(i + 1)] - x[(i - 1)]) - h[(i - 1)] * mu[(i - 1)];
/*  38: 95 */       h[i] /= g;
/*  39: 96 */       z[i] = ((3.0D * (y[(i + 1)] * h[(i - 1)] - y[i] * (x[(i + 1)] - x[(i - 1)]) + y[(i - 1)] * h[i]) / (h[(i - 1)] * h[i]) - h[(i - 1)] * z[(i - 1)]) / g);
/*  40:    */     }
/*  41:101 */     double[] b = new double[n];
/*  42:102 */     double[] c = new double[n + 1];
/*  43:103 */     double[] d = new double[n];
/*  44:    */     
/*  45:105 */     z[n] = 0.0D;
/*  46:106 */     c[n] = 0.0D;
/*  47:108 */     for (int j = n - 1; j >= 0; j--)
/*  48:    */     {
/*  49:109 */       z[j] -= mu[j] * c[(j + 1)];
/*  50:110 */       b[j] = ((y[(j + 1)] - y[j]) / h[j] - h[j] * (c[(j + 1)] + 2.0D * c[j]) / 3.0D);
/*  51:111 */       d[j] = ((c[(j + 1)] - c[j]) / (3.0D * h[j]));
/*  52:    */     }
/*  53:114 */     PolynomialFunction[] polynomials = new PolynomialFunction[n];
/*  54:115 */     double[] coefficients = new double[4];
/*  55:116 */     for (int i = 0; i < n; i++)
/*  56:    */     {
/*  57:117 */       coefficients[0] = y[i];
/*  58:118 */       coefficients[1] = b[i];
/*  59:119 */       coefficients[2] = c[i];
/*  60:120 */       coefficients[3] = d[i];
/*  61:121 */       polynomials[i] = new PolynomialFunction(coefficients);
/*  62:    */     }
/*  63:124 */     return new PolynomialSplineFunction(x, polynomials);
/*  64:    */   }
/*  65:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.SplineInterpolator
 * JD-Core Version:    0.7.0.1
 */
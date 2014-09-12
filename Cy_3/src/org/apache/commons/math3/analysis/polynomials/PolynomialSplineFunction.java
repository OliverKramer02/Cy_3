/*   1:    */ package org.apache.commons.math3.analysis.polynomials;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   9:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  10:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  11:    */ import org.apache.commons.math3.util.MathArrays;
/*  12:    */ 
/*  13:    */ public class PolynomialSplineFunction
/*  14:    */   implements DifferentiableUnivariateFunction
/*  15:    */ {
/*  16:    */   private final double[] knots;
/*  17:    */   private final PolynomialFunction[] polynomials;
/*  18:    */   private final int n;
/*  19:    */   
/*  20:    */   public PolynomialSplineFunction(double[] knots, PolynomialFunction[] polynomials)
/*  21:    */   {
/*  22:101 */     if ((knots == null) || (polynomials == null)) {
/*  23:103 */       throw new NullArgumentException();
/*  24:    */     }
/*  25:105 */     if (knots.length < 2) {
/*  26:106 */       throw new NumberIsTooSmallException(LocalizedFormats.NOT_ENOUGH_POINTS_IN_SPLINE_PARTITION, Integer.valueOf(2), Integer.valueOf(knots.length), false);
/*  27:    */     }
/*  28:109 */     if (knots.length - 1 != polynomials.length) {
/*  29:110 */       throw new DimensionMismatchException(polynomials.length, knots.length);
/*  30:    */     }
/*  31:112 */     MathArrays.checkOrder(knots);
/*  32:    */     
/*  33:114 */     this.n = (knots.length - 1);
/*  34:115 */     this.knots = new double[this.n + 1];
/*  35:116 */     System.arraycopy(knots, 0, this.knots, 0, this.n + 1);
/*  36:117 */     this.polynomials = new PolynomialFunction[this.n];
/*  37:118 */     System.arraycopy(polynomials, 0, this.polynomials, 0, this.n);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public double value(double v)
/*  41:    */   {
/*  42:133 */     if ((v < this.knots[0]) || (v > this.knots[this.n])) {
/*  43:134 */       throw new OutOfRangeException(Double.valueOf(v), Double.valueOf(this.knots[0]), Double.valueOf(this.knots[this.n]));
/*  44:    */     }
/*  45:136 */     int i = Arrays.binarySearch(this.knots, v);
/*  46:137 */     if (i < 0) {
/*  47:138 */       i = -i - 2;
/*  48:    */     }
/*  49:143 */     if (i >= this.polynomials.length) {
/*  50:144 */       i--;
/*  51:    */     }
/*  52:146 */     return this.polynomials[i].value(v - this.knots[i]);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public UnivariateFunction derivative()
/*  56:    */   {
/*  57:155 */     return polynomialSplineDerivative();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public PolynomialSplineFunction polynomialSplineDerivative()
/*  61:    */   {
/*  62:164 */     PolynomialFunction[] derivativePolynomials = new PolynomialFunction[this.n];
/*  63:165 */     for (int i = 0; i < this.n; i++) {
/*  64:166 */       derivativePolynomials[i] = this.polynomials[i].polynomialDerivative();
/*  65:    */     }
/*  66:168 */     return new PolynomialSplineFunction(this.knots, derivativePolynomials);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getN()
/*  70:    */   {
/*  71:178 */     return this.n;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public PolynomialFunction[] getPolynomials()
/*  75:    */   {
/*  76:189 */     PolynomialFunction[] p = new PolynomialFunction[this.n];
/*  77:190 */     System.arraycopy(this.polynomials, 0, p, 0, this.n);
/*  78:191 */     return p;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public double[] getKnots()
/*  82:    */   {
/*  83:202 */     double[] out = new double[this.n + 1];
/*  84:203 */     System.arraycopy(this.knots, 0, out, 0, this.n + 1);
/*  85:204 */     return out;
/*  86:    */   }
/*  87:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction
 * JD-Core Version:    0.7.0.1
 */
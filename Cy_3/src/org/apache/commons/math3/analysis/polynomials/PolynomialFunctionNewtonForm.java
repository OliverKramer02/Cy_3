/*   1:    */ package org.apache.commons.math3.analysis.polynomials;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NoDataException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ 
/*   8:    */ public class PolynomialFunctionNewtonForm
/*   9:    */   implements UnivariateFunction
/*  10:    */ {
/*  11:    */   private double[] coefficients;
/*  12:    */   private final double[] c;
/*  13:    */   private final double[] a;
/*  14:    */   private boolean coefficientsComputed;
/*  15:    */   
/*  16:    */   public PolynomialFunctionNewtonForm(double[] a, double[] c)
/*  17:    */   {
/*  18: 79 */     verifyInputArray(a, c);
/*  19: 80 */     this.a = new double[a.length];
/*  20: 81 */     this.c = new double[c.length];
/*  21: 82 */     System.arraycopy(a, 0, this.a, 0, a.length);
/*  22: 83 */     System.arraycopy(c, 0, this.c, 0, c.length);
/*  23: 84 */     this.coefficientsComputed = false;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public double value(double z)
/*  27:    */   {
/*  28: 94 */     return evaluate(this.a, this.c, z);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int degree()
/*  32:    */   {
/*  33:103 */     return this.c.length;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public double[] getNewtonCoefficients()
/*  37:    */   {
/*  38:114 */     double[] out = new double[this.a.length];
/*  39:115 */     System.arraycopy(this.a, 0, out, 0, this.a.length);
/*  40:116 */     return out;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double[] getCenters()
/*  44:    */   {
/*  45:127 */     double[] out = new double[this.c.length];
/*  46:128 */     System.arraycopy(this.c, 0, out, 0, this.c.length);
/*  47:129 */     return out;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double[] getCoefficients()
/*  51:    */   {
/*  52:140 */     if (!this.coefficientsComputed) {
/*  53:141 */       computeCoefficients();
/*  54:    */     }
/*  55:143 */     double[] out = new double[this.coefficients.length];
/*  56:144 */     System.arraycopy(this.coefficients, 0, out, 0, this.coefficients.length);
/*  57:145 */     return out;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static double evaluate(double[] a, double[] c, double z)
/*  61:    */   {
/*  62:164 */     verifyInputArray(a, c);
/*  63:    */     
/*  64:166 */     int n = c.length;
/*  65:167 */     double value = a[n];
/*  66:168 */     for (int i = n - 1; i >= 0; i--) {
/*  67:169 */       value = a[i] + (z - c[i]) * value;
/*  68:    */     }
/*  69:172 */     return value;
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected void computeCoefficients()
/*  73:    */   {
/*  74:180 */     int n = degree();
/*  75:    */     
/*  76:182 */     this.coefficients = new double[n + 1];
/*  77:183 */     for (int i = 0; i <= n; i++) {
/*  78:184 */       this.coefficients[i] = 0.0D;
/*  79:    */     }
/*  80:187 */     this.coefficients[0] = this.a[n];
/*  81:188 */     for (int i = n - 1; i >= 0; i--)
/*  82:    */     {
/*  83:189 */       for (int j = n - i; j > 0; j--) {
/*  84:190 */         this.coefficients[j] = (this.coefficients[(j - 1)] - this.c[i] * this.coefficients[j]);
/*  85:    */       }
/*  86:192 */       this.coefficients[0] = (this.a[i] - this.c[i] * this.coefficients[0]);
/*  87:    */     }
/*  88:195 */     this.coefficientsComputed = true;
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected static void verifyInputArray(double[] a, double[] c)
/*  92:    */   {
/*  93:215 */     if ((a.length == 0) || (c.length == 0)) {
/*  94:217 */       throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
/*  95:    */     }
/*  96:219 */     if (a.length != c.length + 1) {
/*  97:220 */       throw new DimensionMismatchException(LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_1, a.length, c.length);
/*  98:    */     }
/*  99:    */   }
/* 100:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm
 * JD-Core Version:    0.7.0.1
 */
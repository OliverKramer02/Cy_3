/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
/*   5:    */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm;
/*   6:    */ 
/*   7:    */ public class DividedDifferenceInterpolator
/*   8:    */   implements UnivariateInterpolator, Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 107049519551235069L;
/*  11:    */   
/*  12:    */   public PolynomialFunctionNewtonForm interpolate(double[] x, double[] y)
/*  13:    */   {
/*  14: 60 */     PolynomialFunctionLagrangeForm.verifyInterpolationArray(x, y, true);
/*  15:    */     
/*  16:    */ 
/*  17:    */ 
/*  18:    */ 
/*  19:    */ 
/*  20:    */ 
/*  21:    */ 
/*  22:    */ 
/*  23:    */ 
/*  24: 70 */     double[] c = new double[x.length - 1];
/*  25: 71 */     System.arraycopy(x, 0, c, 0, c.length);
/*  26:    */     
/*  27: 73 */     double[] a = computeDividedDifference(x, y);
/*  28: 74 */     return new PolynomialFunctionNewtonForm(a, c);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected static double[] computeDividedDifference(double[] x, double[] y)
/*  32:    */   {
/*  33: 98 */     PolynomialFunctionLagrangeForm.verifyInterpolationArray(x, y, true);
/*  34:    */     
/*  35:100 */     double[] divdiff = (double[])y.clone();
/*  36:    */     
/*  37:102 */     int n = x.length;
/*  38:103 */     double[] a = new double[n];
/*  39:104 */     a[0] = divdiff[0];
/*  40:105 */     for (int i = 1; i < n; i++)
/*  41:    */     {
/*  42:106 */       for (int j = 0; j < n - i; j++)
/*  43:    */       {
/*  44:107 */         double denominator = x[(j + i)] - x[j];
/*  45:108 */         divdiff[j] = ((divdiff[(j + 1)] - divdiff[j]) / denominator);
/*  46:    */       }
/*  47:110 */       a[i] = divdiff[0];
/*  48:    */     }
/*  49:113 */     return a;
/*  50:    */   }
/*  51:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.DividedDifferenceInterpolator
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   5:    */ import org.apache.commons.math3.util.MathArrays;
/*   6:    */ import org.apache.commons.math3.util.MathUtils;
/*   7:    */ 
/*   8:    */ public class UnivariatePeriodicInterpolator
/*   9:    */   implements UnivariateInterpolator
/*  10:    */ {
/*  11:    */   public static final int DEFAULT_EXTEND = 5;
/*  12:    */   private final UnivariateInterpolator interpolator;
/*  13:    */   private final double period;
/*  14:    */   private final int extend;
/*  15:    */   
/*  16:    */   public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator, double period, int extend)
/*  17:    */   {
/*  18: 59 */     this.interpolator = interpolator;
/*  19: 60 */     this.period = period;
/*  20: 61 */     this.extend = extend;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator, double period)
/*  24:    */   {
/*  25: 74 */     this(interpolator, period, 5);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public UnivariateFunction interpolate(double[] xval, double[] yval)
/*  29:    */   {
/*  30: 85 */     if (xval.length < this.extend) {
/*  31: 86 */       throw new NumberIsTooSmallException(Integer.valueOf(xval.length), Integer.valueOf(this.extend), true);
/*  32:    */     }
/*  33: 89 */     MathArrays.checkOrder(xval);
/*  34: 90 */     final double offset = xval[0];
/*  35:    */     
/*  36: 92 */     int len = xval.length + this.extend * 2;
/*  37: 93 */     double[] x = new double[len];
/*  38: 94 */     double[] y = new double[len];
/*  39: 95 */     for (int i = 0; i < xval.length; i++)
/*  40:    */     {
/*  41: 96 */       int index = i + this.extend;
/*  42: 97 */       x[index] = MathUtils.reduce(xval[i], this.period, offset);
/*  43: 98 */       y[index] = yval[i];
/*  44:    */     }
/*  45:102 */     for (int i = 0; i < this.extend; i++)
/*  46:    */     {
/*  47:103 */       int index = xval.length - this.extend + i;
/*  48:104 */       x[i] = (MathUtils.reduce(xval[index], this.period, offset) - this.period);
/*  49:105 */       y[i] = yval[index];
/*  50:    */       
/*  51:107 */       index = len - this.extend + i;
/*  52:108 */       x[index] = (MathUtils.reduce(xval[i], this.period, offset) + this.period);
/*  53:109 */       y[index] = yval[i];
/*  54:    */     }
/*  55:112 */     MathArrays.sortInPlace(x, new double[][] { y });
/*  56:    */     
/*  57:114 */     final UnivariateFunction f = this.interpolator.interpolate(x, y);
return new UnivariateFunction()
/*  59:    */     {
/*  60:    */       public double value(double x)
/*  61:    */       {
/*  62:117 */         return f.value(MathUtils.reduce(x, UnivariatePeriodicInterpolator.this.period, offset));
/*  63:    */       }
/*  64:    */     };   }
/*  66:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.UnivariatePeriodicInterpolator
 * JD-Core Version:    0.7.0.1
 */
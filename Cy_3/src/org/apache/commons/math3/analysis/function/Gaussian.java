/*   1:    */ package org.apache.commons.math3.analysis.function;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class Gaussian
/*  12:    */   implements DifferentiableUnivariateFunction
/*  13:    */ {
/*  14:    */   private final double mean;
/*  15:    */   private final double i2s2;
/*  16:    */   private final double norm;
/*  17:    */   
/*  18:    */   public Gaussian(double norm, double mean, double sigma)
/*  19:    */   {
/*  20: 54 */     if (sigma <= 0.0D) {
/*  21: 55 */       throw new NotStrictlyPositiveException(Double.valueOf(sigma));
/*  22:    */     }
/*  23: 58 */     this.norm = norm;
/*  24: 59 */     this.mean = mean;
/*  25: 60 */     this.i2s2 = (1.0D / (2.0D * sigma * sigma));
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Gaussian(double mean, double sigma)
/*  29:    */   {
/*  30: 72 */     this(1.0D / (sigma * FastMath.sqrt(6.283185307179586D)), mean, sigma);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Gaussian()
/*  34:    */   {
/*  35: 79 */     this(0.0D, 1.0D);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public double value(double x)
/*  39:    */   {
/*  40: 84 */     return value(x - this.mean, this.norm, this.i2s2);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public UnivariateFunction derivative()
/*  44:    */   {
return new UnivariateFunction()
/*  46:    */     {
/*  47:    */       public double value(double x)
/*  48:    */       {
/*  49: 92 */         double diff = x - Gaussian.this.mean;
/*  50: 93 */         double g = Gaussian.value(diff, Gaussian.this.norm, Gaussian.this.i2s2);
/*  51: 95 */         if (g == 0.0D) {
/*  52: 97 */           return 0.0D;
/*  53:    */         }
/*  54: 99 */         return -2.0D * diff * Gaussian.this.i2s2 * g;
/*  55:    */       }
/*  56:    */     };   }
/*  58:    */   
/*  59:    */   public static class Parametric
/*  60:    */     implements ParametricUnivariateFunction
/*  61:    */   {
/*  62:    */     public double value(double x, double... param)
/*  63:    */     {
/*  64:127 */       validateParameters(param);
/*  65:    */       
/*  66:129 */       double diff = x - param[1];
/*  67:130 */       double i2s2 = 1.0D / (2.0D * param[2] * param[2]);
/*  68:131 */       return Gaussian.value(diff, param[0], i2s2);
/*  69:    */     }
/*  70:    */     
/*  71:    */     public double[] gradient(double x, double... param)
/*  72:    */     {
/*  73:149 */       validateParameters(param);
/*  74:    */       
/*  75:151 */       double norm = param[0];
/*  76:152 */       double diff = x - param[1];
/*  77:153 */       double sigma = param[2];
/*  78:154 */       double i2s2 = 1.0D / (2.0D * sigma * sigma);
/*  79:    */       
/*  80:156 */       double n = Gaussian.value(diff, 1.0D, i2s2);
/*  81:157 */       double m = norm * n * 2.0D * i2s2 * diff;
/*  82:158 */       double s = m * diff / sigma;
/*  83:    */       
/*  84:160 */       return new double[] { n, m, s };
/*  85:    */     }
/*  86:    */     
/*  87:    */     private void validateParameters(double[] param)
/*  88:    */     {
/*  89:175 */       if (param == null) {
/*  90:176 */         throw new NullArgumentException();
/*  91:    */       }
/*  92:178 */       if (param.length != 3) {
/*  93:179 */         throw new DimensionMismatchException(param.length, 3);
/*  94:    */       }
/*  95:181 */       if (param[2] <= 0.0D) {
/*  96:182 */         throw new NotStrictlyPositiveException(Double.valueOf(param[2]));
/*  97:    */       }
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   private static double value(double xMinusMean, double norm, double i2s2)
/* 102:    */   {
/* 103:196 */     return norm * FastMath.exp(-xMinusMean * xMinusMean * i2s2);
/* 104:    */   }
/* 105:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Gaussian
 * JD-Core Version:    0.7.0.1
 */
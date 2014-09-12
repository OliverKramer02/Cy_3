/*   1:    */ package org.apache.commons.math3.special;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.ContinuedFraction;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public class Beta
/*   7:    */ {
/*   8:    */   private static final double DEFAULT_EPSILON = 1.0E-014D;
/*   9:    */   
/*  10:    */   public static double regularizedBeta(double x, double a, double b)
/*  11:    */   {
/*  12: 50 */     return regularizedBeta(x, a, b, 1.0E-014D, 2147483647);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public static double regularizedBeta(double x, double a, double b, double epsilon)
/*  16:    */   {
/*  17: 71 */     return regularizedBeta(x, a, b, epsilon, 2147483647);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static double regularizedBeta(double x, double a, double b, int maxIterations)
/*  21:    */   {
/*  22: 88 */     return regularizedBeta(x, a, b, 1.0E-014D, maxIterations);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static double regularizedBeta(double x, double a, double b, double epsilon, int maxIterations)
/*  26:    */   {
/*  27:    */     double ret;
/*  28:    */     
/*  29:120 */     if ((Double.isNaN(x)) || (Double.isNaN(a)) || (Double.isNaN(b)) || (x < 0.0D) || (x > 1.0D) || (a <= 0.0D) || (b <= 0.0D))
/*  30:    */     {
/*  31:127 */       ret = (0.0D / 0.0D);
/*  32:    */     }
/*  33:    */     else
/*  34:    */     {
/*  35:    */       
/*  36:128 */       if (x > (a + 1.0D) / (a + b + 2.0D))
/*  37:    */       {
/*  38:129 */         ret = 1.0D - regularizedBeta(1.0D - x, b, a, epsilon, maxIterations);
/*  39:    */       }
/*  40:    */       else
/*  41:    */       {
/*  42:131 */         ContinuedFraction fraction = new ContinuedFraction()
/*  43:    */         {
private double val$a;
private double val$b;
/*  44:    */           protected double getB(int n, double x)
/*  45:    */           {
/*  46:    */             double ret;
/*  47:    */        
/*  48:137 */             if (n % 2 == 0)
/*  49:    */             {
/*  50:138 */               double m = n / 2.0D;
/*  51:139 */               ret = m * (this.val$b - m) * x / ((this.val$a + 2.0D * m - 1.0D) * (this.val$a + 2.0D * m));
/*  52:    */             }
/*  53:    */             else
/*  54:    */             {
/*  55:142 */               double m = (n - 1.0D) / 2.0D;
/*  56:143 */               ret = -((this.val$a + m) * (this.val$a + this.val$b + m) * x) / ((this.val$a + 2.0D * m) * (this.val$a + 2.0D * m + 1.0D));
/*  57:    */             }
/*  58:146 */             return ret;
/*  59:    */           }
/*  60:    */           
/*  61:    */           protected double getA(int n, double x)
/*  62:    */           {
/*  63:151 */             return 1.0D;
/*  64:    */           }
/*  65:153 */         };
/*  66:154 */         ret = FastMath.exp(a * FastMath.log(x) + b * FastMath.log(1.0D - x) - FastMath.log(a) - logBeta(a, b, epsilon, maxIterations)) * 1.0D / fraction.evaluate(x, epsilon, maxIterations);
/*  67:    */       }
/*  68:    */     }
/*  69:159 */     return ret;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static double logBeta(double a, double b)
/*  73:    */   {
/*  74:170 */     return logBeta(a, b, 1.0E-014D, 2147483647);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static double logBeta(double a, double b, double epsilon, int maxIterations)
/*  78:    */   {
/*  79:    */     double ret;
/*  80:    */     
/*  81:195 */     if ((Double.isNaN(a)) || (Double.isNaN(b)) || (a <= 0.0D) || (b <= 0.0D)) {
/*  82:199 */       ret = (0.0D / 0.0D);
/*  83:    */     } else {
/*  84:201 */       ret = Gamma.logGamma(a) + Gamma.logGamma(b) - Gamma.logGamma(a + b);
/*  85:    */     }
/*  86:205 */     return ret;
/*  87:    */   }
/*  88:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.special.Beta
 * JD-Core Version:    0.7.0.1
 */
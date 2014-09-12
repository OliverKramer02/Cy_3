/*   1:    */ package org.apache.commons.math3.special;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   4:    */ import org.apache.commons.math3.util.ContinuedFraction;
/*   5:    */ import org.apache.commons.math3.util.FastMath;
/*   6:    */ 
/*   7:    */ public class Gamma
/*   8:    */ {
/*   9:    */   public static final double GAMMA = 0.5772156649015329D;
/*  10:    */   private static final double DEFAULT_EPSILON = 1.0E-014D;
/*  11: 38 */   private static final double[] LANCZOS = { 0.9999999999999971D, 57.156235665862923D, -59.597960355475493D, 14.136097974741746D, -0.4919138160976202D, 3.399464998481189E-005D, 4.652362892704858E-005D, -9.837447530487957E-005D, 0.0001580887032249125D, -0.0002102644417241049D, 0.0002174396181152127D, -0.0001643181065367639D, 8.441822398385275E-005D, -2.619083840158141E-005D, 3.689918265953163E-006D };
/*  12: 56 */   private static final double HALF_LOG_2_PI = 0.5D * FastMath.log(6.283185307179586D);
/*  13:    */   private static final double C_LIMIT = 49.0D;
/*  14:    */   private static final double S_LIMIT = 1.E-005D;
/*  15:    */   
/*  16:    */   public static double logGamma(double x)
/*  17:    */   {
/*  18:    */     double ret;
/*  19:    */   
/*  20: 88 */     if ((Double.isNaN(x)) || (x <= 0.0D))
/*  21:    */     {
/*  22: 89 */       ret = (0.0D / 0.0D);
/*  23:    */     }
/*  24:    */     else
/*  25:    */     {
/*  26: 91 */       double g = 4.7421875D;
/*  27:    */       
/*  28: 93 */       double sum = 0.0D;
/*  29: 94 */       for (int i = LANCZOS.length - 1; i > 0; i--) {
/*  30: 95 */         sum += LANCZOS[i] / (x + i);
/*  31:    */       }
/*  32: 97 */       sum += LANCZOS[0];
/*  33:    */       
/*  34: 99 */       double tmp = x + g + 0.5D;
/*  35:100 */       ret = (x + 0.5D) * FastMath.log(tmp) - tmp + HALF_LOG_2_PI + FastMath.log(sum / x);
/*  36:    */     }
/*  37:104 */     return ret;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static double regularizedGammaP(double a, double x)
/*  41:    */   {
/*  42:116 */     return regularizedGammaP(a, x, 1.0E-014D, 2147483647);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static double regularizedGammaP(double a, double x, double epsilon, int maxIterations)
/*  46:    */   {
/*  47:    */     double ret;
/*  48:    */    
/*  49:153 */     if ((Double.isNaN(a)) || (Double.isNaN(x)) || (a <= 0.0D) || (x < 0.0D))
/*  50:    */     {
/*  51:154 */       ret = (0.0D / 0.0D);
/*  52:    */     }
/*  53:    */     else
/*  54:    */     {
/*  55:    */       
/*  56:155 */       if (x == 0.0D)
/*  57:    */       {
/*  58:156 */         ret = 0.0D;
/*  59:    */       }
/*  60:    */       else
/*  61:    */       {
/*  62:    */      
/*  63:157 */         if (x >= a + 1.0D)
/*  64:    */         {
/*  65:160 */           ret = 1.0D - regularizedGammaQ(a, x, epsilon, maxIterations);
/*  66:    */         }
/*  67:    */         else
/*  68:    */         {
/*  69:163 */           double n = 0.0D;
/*  70:164 */           double an = 1.0D / a;
/*  71:165 */           double sum = an;
/*  72:167 */           while ((FastMath.abs(an / sum) > epsilon) && (n < maxIterations) && (sum < (1.0D / 0.0D)))
/*  73:    */           {
/*  74:170 */             n += 1.0D;
/*  75:171 */             an *= x / (a + n);
/*  76:    */             
/*  77:    */ 
/*  78:174 */             sum += an;
/*  79:    */           }
/*  80:176 */           if (n >= maxIterations) {
/*  81:177 */             throw new MaxCountExceededException(Integer.valueOf(maxIterations));
/*  82:    */           }
/*  83:    */          
/*  84:178 */           if (Double.isInfinite(sum)) {
/*  85:179 */             ret = 1.0D;
/*  86:    */           } else {
/*  87:181 */             ret = FastMath.exp(-x + a * FastMath.log(x) - logGamma(a)) * sum;
/*  88:    */           }
/*  89:    */         }
/*  90:    */       }
/*  91:    */     }
/*  92:185 */     return ret;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static double regularizedGammaQ(double a, double x)
/*  96:    */   {
/*  97:197 */     return regularizedGammaQ(a, x, 1.0E-014D, 2147483647);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static double regularizedGammaQ(double a, double x, double epsilon, int maxIterations)
/* 101:    */   {
/* 102:    */     double ret;
/* 103:    */  
/* 104:231 */     if ((Double.isNaN(a)) || (Double.isNaN(x)) || (a <= 0.0D) || (x < 0.0D))
/* 105:    */     {
/* 106:232 */       ret = (0.0D / 0.0D);
/* 107:    */     }
/* 108:    */     else
/* 109:    */     {
/* 110:    */     
/* 111:233 */       if (x == 0.0D)
/* 112:    */       {
/* 113:234 */         ret = 1.0D;
/* 114:    */       }
/* 115:    */       else
/* 116:    */       {
/* 117:    */        
/* 118:235 */         if (x < a + 1.0D)
/* 119:    */         {
/* 120:238 */           ret = 1.0D - regularizedGammaP(a, x, epsilon, maxIterations);
/* 121:    */         }
/* 122:    */         else
/* 123:    */         {
/* 124:241 */           ContinuedFraction cf = new ContinuedFraction()
/* 125:    */           {
private double val$a;
/* 126:    */             protected double getA(int n, double x)
/* 127:    */             {
/* 128:245 */               return 2.0D * n + 1.0D - this.val$a + x;
/* 129:    */             }
/* 130:    */             
/* 131:    */             protected double getB(int n, double x)
/* 132:    */             {
/* 133:250 */               return n * (this.val$a - n);
/* 134:    */             }
/* 135:253 */           };
/* 136:254 */           ret = 1.0D / cf.evaluate(x, epsilon, maxIterations);
/* 137:255 */           ret = FastMath.exp(-x + a * FastMath.log(x) - logGamma(a)) * ret;
/* 138:    */         }
/* 139:    */       }
/* 140:    */     }
/* 141:258 */     return ret;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public static double digamma(double x)
/* 145:    */   {
/* 146:283 */     if ((x > 0.0D) && (x <= 1.E-005D)) {
/* 147:286 */       return -0.5772156649015329D - 1.0D / x;
/* 148:    */     }
/* 149:289 */     if (x >= 49.0D)
/* 150:    */     {
/* 151:291 */       double inv = 1.0D / (x * x);
/* 152:    */       
/* 153:    */ 
/* 154:    */ 
/* 155:295 */       return FastMath.log(x) - 0.5D / x - inv * (0.08333333333333333D + inv * (0.008333333333333333D - inv / 252.0D));
/* 156:    */     }
/* 157:298 */     return digamma(x + 1.0D) - 1.0D / x;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public static double trigamma(double x)
/* 161:    */   {
/* 162:313 */     if ((x > 0.0D) && (x <= 1.E-005D)) {
/* 163:314 */       return 1.0D / (x * x);
/* 164:    */     }
/* 165:317 */     if (x >= 49.0D)
/* 166:    */     {
/* 167:318 */       double inv = 1.0D / (x * x);
/* 168:    */       
/* 169:    */ 
/* 170:    */ 
/* 171:    */ 
/* 172:323 */       return 1.0D / x + inv / 2.0D + inv / x * (0.1666666666666667D - inv * (0.03333333333333333D + inv / 42.0D));
/* 173:    */     }
/* 174:326 */     return trigamma(x + 1.0D) + 1.0D / (x * x);
/* 175:    */   }
/* 176:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.special.Gamma
 * JD-Core Version:    0.7.0.1
 */
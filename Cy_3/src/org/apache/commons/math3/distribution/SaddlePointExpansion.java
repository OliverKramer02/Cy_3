/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.special.Gamma;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ final class SaddlePointExpansion
/*   7:    */ {
/*   8: 49 */   private static final double HALF_LOG_2_PI = 0.5D * FastMath.log(6.283185307179586D);
/*   9: 52 */   private static final double[] EXACT_STIRLING_ERRORS = { 0.0D, 0.1534264097200274D, 0.08106146679532726D, 0.05481412105191765D, 0.0413406959554093D, 0.03316287351993629D, 0.02767792568499834D, 0.0237461636562975D, 0.02079067210376509D, 0.01848845053267319D, 0.01664469118982119D, 0.01513497322191738D, 0.01387612882307075D, 0.01281046524292023D, 0.0118967099458918D, 0.01110455975820692D, 0.0104112652619721D, 0.009799416126158804D, 0.009255462182712733D, 0.008768700134139386D, 0.008330563433362871D, 0.00793411456431402D, 0.007573675487951841D, 0.00724455430132038D, 0.00694284010720953D, 0.006665247032707682D, 0.006408994188004207D, 0.006171712263039458D, 0.005951370112758848D, 0.005746216513010116D, 0.005554733551962801D };
/*  10:    */   
/*  11:    */   static double getStirlingError(double z)
/*  12:    */   {
/*  13:    */     double ret;
/*  14:    */     
/*  15:109 */     if (z < 15.0D)
/*  16:    */     {
/*  17:110 */       double z2 = 2.0D * z;
/*  18:    */      
/*  19:111 */       if (FastMath.floor(z2) == z2) {
/*  20:112 */         ret = EXACT_STIRLING_ERRORS[((int)z2)];
/*  21:    */       } else {
/*  22:114 */         ret = Gamma.logGamma(z + 1.0D) - (z + 0.5D) * FastMath.log(z) + z - HALF_LOG_2_PI;
/*  23:    */       }
/*  24:    */     }
/*  25:    */     else
/*  26:    */     {
/*  27:118 */       double z2 = z * z;
/*  28:119 */       ret = (0.08333333333333333D - (0.002777777777777778D - (0.0007936507936507937D - (0.0005952380952380953D - 0.0008417508417508417D / z2) / z2) / z2) / z2) / z;
/*  29:    */     }
/*  30:126 */     return ret;
/*  31:    */   }
/*  32:    */   
/*  33:    */   static double getDeviancePart(double x, double mu)
/*  34:    */   {
/*  35:    */     double ret;
/*  36:    */    
/*  37:147 */     if (FastMath.abs(x - mu) < 0.1D * (x + mu))
/*  38:    */     {
/*  39:148 */       double d = x - mu;
/*  40:149 */       double v = d / (x + mu);
/*  41:150 */       double s1 = v * d;
/*  42:151 */       double s = (0.0D / 0.0D);
/*  43:152 */       double ej = 2.0D * x * v;
/*  44:153 */       v *= v;
/*  45:154 */       int j = 1;
/*  46:155 */       while (s1 != s)
/*  47:    */       {
/*  48:156 */         s = s1;
/*  49:157 */         ej *= v;
/*  50:158 */         s1 = s + ej / (j * 2 + 1);
/*  51:159 */         j++;
/*  52:    */       }
/*  53:161 */       ret = s1;
/*  54:    */     }
/*  55:    */     else
/*  56:    */     {
/*  57:163 */       ret = x * FastMath.log(x / mu) + mu - x;
/*  58:    */     }
/*  59:165 */     return ret;
/*  60:    */   }
/*  61:    */   
/*  62:    */   static double logBinomialProbability(int x, int n, double p, double q)
/*  63:    */   {
/*  64:    */     double ret;
/*  65:    */    
/*  66:180 */     if (x == 0)
/*  67:    */     {
/*  68:    */   
/*  69:181 */       if (p < 0.1D) {
/*  70:182 */         ret = -getDeviancePart(n, n * q) - n * p;
/*  71:    */       } else {
/*  72:184 */         ret = n * FastMath.log(q);
/*  73:    */       }
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77:    */      
/*  78:186 */       if (x == n)
/*  79:    */       {
/*  80:    */        
/*  81:187 */         if (q < 0.1D) {
/*  82:188 */           ret = -getDeviancePart(n, n * p) - n * q;
/*  83:    */         } else {
/*  84:190 */           ret = n * FastMath.log(p);
/*  85:    */         }
/*  86:    */       }
/*  87:    */       else
/*  88:    */       {
/*  89:193 */         ret = getStirlingError(n) - getStirlingError(x) - getStirlingError(n - x) - getDeviancePart(x, n * p) - getDeviancePart(n - x, n * q);
/*  90:    */         
/*  91:    */ 
/*  92:196 */         double f = 6.283185307179586D * x * (n - x) / n;
/*  93:197 */         ret = -0.5D * FastMath.log(f) + ret;
/*  94:    */       }
/*  95:    */     }
/*  96:199 */     return ret;
/*  97:    */   }
/*  98:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.SaddlePointExpansion
 * JD-Core Version:    0.7.0.1
 */
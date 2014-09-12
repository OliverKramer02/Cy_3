/*   1:    */ package org.apache.commons.math3.special;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class Erf
/*   6:    */ {
/*   7:    */   private static final double X_CRIT = 0.4769362762044697D;
/*   8:    */   
/*   9:    */   public static double erf(double x)
/*  10:    */   {
/*  11: 67 */     if (FastMath.abs(x) > 40.0D) {
/*  12: 68 */       return x > 0.0D ? 1.0D : -1.0D;
/*  13:    */     }
/*  14: 70 */     double ret = Gamma.regularizedGammaP(0.5D, x * x, 1.E-015D, 10000);
/*  15: 71 */     return x < 0.0D ? -ret : ret;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public static double erfc(double x)
/*  19:    */   {
/*  20: 98 */     if (FastMath.abs(x) > 40.0D) {
/*  21: 99 */       return x > 0.0D ? 0.0D : 2.0D;
/*  22:    */     }
/*  23:101 */     double ret = Gamma.regularizedGammaQ(0.5D, x * x, 1.E-015D, 10000);
/*  24:102 */     return x < 0.0D ? 2.0D - ret : ret;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static double erf(double x1, double x2)
/*  28:    */   {
/*  29:116 */     if (x1 > x2) {
/*  30:117 */       return -erf(x2, x1);
/*  31:    */     }
/*  32:120 */     return (x2 > 0.4769362762044697D) && (x1 > 0.0D) ? erfc(x1) - erfc(x2) : x1 < -0.4769362762044697D ? erf(x2) - erf(x1) : x2 < 0.0D ? erfc(-x2) - erfc(-x1) : erf(x2) - erf(x1);
/*  33:    */   }
/*  34:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.special.Erf
 * JD-Core Version:    0.7.0.1
 */
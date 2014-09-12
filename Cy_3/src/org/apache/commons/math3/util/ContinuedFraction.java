/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ 
/*   7:    */ public abstract class ContinuedFraction
/*   8:    */ {
/*   9:    */   private static final double DEFAULT_EPSILON = 1.0E-008D;
/*  10:    */   
/*  11:    */   protected abstract double getA(int paramInt, double paramDouble);
/*  12:    */   
/*  13:    */   protected abstract double getB(int paramInt, double paramDouble);
/*  14:    */   
/*  15:    */   public double evaluate(double x)
/*  16:    */   {
/*  17: 73 */     return evaluate(x, 1.0E-008D, 2147483647);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public double evaluate(double x, double epsilon)
/*  21:    */   {
/*  22: 84 */     return evaluate(x, epsilon, 2147483647);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public double evaluate(double x, int maxIterations)
/*  26:    */   {
/*  27: 95 */     return evaluate(x, 1.0E-008D, maxIterations);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public double evaluate(double x, double epsilon, int maxIterations)
/*  31:    */   {
/*  32:125 */     double p0 = 1.0D;
/*  33:126 */     double p1 = getA(0, x);
/*  34:127 */     double q0 = 0.0D;
/*  35:128 */     double q1 = 1.0D;
/*  36:129 */     double c = p1 / q1;
/*  37:130 */     int n = 0;
/*  38:131 */     double relativeError = 1.7976931348623157E+308D;
/*  39:132 */     while ((n < maxIterations) && (relativeError > epsilon))
/*  40:    */     {
/*  41:133 */       n++;
/*  42:134 */       double a = getA(n, x);
/*  43:135 */       double b = getB(n, x);
/*  44:136 */       double p2 = a * p1 + b * p0;
/*  45:137 */       double q2 = a * q1 + b * q0;
/*  46:138 */       boolean infinite = false;
/*  47:139 */       if ((Double.isInfinite(p2)) || (Double.isInfinite(q2)))
/*  48:    */       {
/*  49:145 */         double scaleFactor = 1.0D;
/*  50:146 */         double lastScaleFactor = 1.0D;
/*  51:147 */         int maxPower = 5;
/*  52:148 */         double scale = FastMath.max(a, b);
/*  53:149 */         if (scale <= 0.0D) {
/*  54:150 */           throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, new Object[] { Double.valueOf(x) });
/*  55:    */         }
/*  56:153 */         infinite = true;
/*  57:154 */         for (int i = 0; i < 5; i++)
/*  58:    */         {
/*  59:155 */           lastScaleFactor = scaleFactor;
/*  60:156 */           scaleFactor *= scale;
/*  61:157 */           if ((a != 0.0D) && (a > b))
/*  62:    */           {
/*  63:158 */             p2 = p1 / lastScaleFactor + b / scaleFactor * p0;
/*  64:159 */             q2 = q1 / lastScaleFactor + b / scaleFactor * q0;
/*  65:    */           }
/*  66:160 */           else if (b != 0.0D)
/*  67:    */           {
/*  68:161 */             p2 = a / scaleFactor * p1 + p0 / lastScaleFactor;
/*  69:162 */             q2 = a / scaleFactor * q1 + q0 / lastScaleFactor;
/*  70:    */           }
/*  71:164 */           infinite = (Double.isInfinite(p2)) || (Double.isInfinite(q2));
/*  72:165 */           if (!infinite) {
/*  73:    */             break;
/*  74:    */           }
/*  75:    */         }
/*  76:    */       }
/*  77:171 */       if (infinite) {
/*  78:173 */         throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, new Object[] { Double.valueOf(x) });
/*  79:    */       }
/*  80:177 */       double r = p2 / q2;
/*  81:179 */       if (Double.isNaN(r)) {
/*  82:180 */         throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, new Object[] { Double.valueOf(x) });
/*  83:    */       }
/*  84:183 */       relativeError = FastMath.abs(r / c - 1.0D);
/*  85:    */       
/*  86:    */ 
/*  87:186 */       c = p2 / q2;
/*  88:187 */       p0 = p1;
/*  89:188 */       p1 = p2;
/*  90:189 */       q0 = q1;
/*  91:190 */       q1 = q2;
/*  92:    */     }
/*  93:193 */     if (n >= maxIterations) {
/*  94:194 */       throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, Integer.valueOf(maxIterations), new Object[] { Double.valueOf(x) });
/*  95:    */     }
/*  96:198 */     return c;
/*  97:    */   }
/*  98:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.ContinuedFraction
 * JD-Core Version:    0.7.0.1
 */
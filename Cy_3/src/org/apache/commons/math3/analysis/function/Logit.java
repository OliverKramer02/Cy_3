/*   1:    */ package org.apache.commons.math3.analysis.function;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class Logit
/*  12:    */   implements DifferentiableUnivariateFunction
/*  13:    */ {
/*  14:    */   private final double lo;
/*  15:    */   private final double hi;
/*  16:    */   
/*  17:    */   public Logit()
/*  18:    */   {
/*  19: 47 */     this(0.0D, 1.0D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Logit(double lo, double hi)
/*  23:    */   {
/*  24: 58 */     this.lo = lo;
/*  25: 59 */     this.hi = hi;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public double value(double x)
/*  29:    */   {
/*  30: 64 */     return value(x, this.lo, this.hi);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public UnivariateFunction derivative()
/*  34:    */   {
return new UnivariateFunction()
/*  36:    */     {
/*  37:    */       public double value(double x)
/*  38:    */       {
/*  39: 72 */         return (Logit.this.hi - Logit.this.lo) / ((x - Logit.this.lo) * (Logit.this.hi - x));
/*  40:    */       }
/*  41:    */     };   }
/*  43:    */   
/*  44:    */   public static class Parametric
/*  45:    */     implements ParametricUnivariateFunction
/*  46:    */   {
/*  47:    */     public double value(double x, double... param)
/*  48:    */     {
/*  49: 97 */       validateParameters(param);
/*  50: 98 */       return Logit.value(x, param[0], param[1]);
/*  51:    */     }
/*  52:    */     
/*  53:    */     public double[] gradient(double x, double... param)
/*  54:    */     {
/*  55:115 */       validateParameters(param);
/*  56:    */       
/*  57:117 */       double lo = param[0];
/*  58:118 */       double hi = param[1];
/*  59:    */       
/*  60:120 */       return new double[] { 1.0D / (lo - x), 1.0D / (hi - x) };
/*  61:    */     }
/*  62:    */     
/*  63:    */     private void validateParameters(double[] param)
/*  64:    */     {
/*  65:134 */       if (param == null) {
/*  66:135 */         throw new NullArgumentException();
/*  67:    */       }
/*  68:137 */       if (param.length != 2) {
/*  69:138 */         throw new DimensionMismatchException(param.length, 2);
/*  70:    */       }
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   private static double value(double x, double lo, double hi)
/*  75:    */   {
/*  76:152 */     if ((x < lo) || (x > hi)) {
/*  77:153 */       throw new OutOfRangeException(Double.valueOf(x), Double.valueOf(lo), Double.valueOf(hi));
/*  78:    */     }
/*  79:155 */     return FastMath.log((x - lo) / (hi - x));
/*  80:    */   }
/*  81:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Logit
 * JD-Core Version:    0.7.0.1
 */
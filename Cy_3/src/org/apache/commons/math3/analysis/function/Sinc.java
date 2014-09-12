/*   1:    */ package org.apache.commons.math3.analysis.function;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   5:    */ import org.apache.commons.math3.util.FastMath;
/*   6:    */ 
/*   7:    */ public class Sinc
/*   8:    */   implements DifferentiableUnivariateFunction
/*   9:    */ {
/*  10:    */   private static final double SHORTCUT = 1.E-009D;
/*  11:    */   private final boolean normalized;
/*  12:    */   
/*  13:    */   public Sinc()
/*  14:    */   {
/*  15: 49 */     this(false);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public Sinc(boolean normalized)
/*  19:    */   {
/*  20: 59 */     this.normalized = normalized;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double value(double x)
/*  24:    */   {
/*  25: 64 */     if (this.normalized)
/*  26:    */     {
/*  27: 65 */       double piTimesX = 3.141592653589793D * x;
/*  28: 66 */       return sinc(piTimesX);
/*  29:    */     }
/*  30: 68 */     return sinc(x);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public UnivariateFunction derivative()
/*  34:    */   {
/*  35: 74 */     if (this.normalized) {
/*  36: 75 */       new UnivariateFunction()
/*  37:    */       {
/*  38:    */         public double value(double x)
/*  39:    */         {
/*  40: 78 */           double piTimesX = 3.141592653589793D * x;
/*  41: 79 */           return Sinc.sincDerivative(piTimesX);
/*  42:    */         }
/*  43:    */       };
/*  44:    */     }
return new UnivariateFunction()
/*  46:    */     {
/*  47:    */       public double value(double x)
/*  48:    */       {
/*  49: 86 */         return Sinc.sincDerivative(x);
/*  50:    */       }
/*  51:    */     };   }
/*  53:    */   
/*  54:    */   private static double sinc(double x)
/*  55:    */   {
/*  56:101 */     return FastMath.abs(x) < 1.E-009D ? 1.0D : FastMath.sin(x) / x;
/*  57:    */   }
/*  58:    */   
/*  59:    */   private static double sincDerivative(double x)
/*  60:    */   {
/*  61:114 */     return FastMath.abs(x) < 1.E-009D ? 0.0D : (FastMath.cos(x) - FastMath.sin(x) / x) / x;
/*  62:    */   }
/*  63:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Sinc
 * JD-Core Version:    0.7.0.1
 */
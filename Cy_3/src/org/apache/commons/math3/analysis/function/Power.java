/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  5:   */ import org.apache.commons.math3.util.FastMath;
/*  6:   */ 
/*  7:   */ public class Power
/*  8:   */   implements DifferentiableUnivariateFunction
/*  9:   */ {
/* 10:   */   private final double p;
/* 11:   */   
/* 12:   */   public Power(double p)
/* 13:   */   {
/* 14:38 */     this.p = p;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public double value(double x)
/* 18:   */   {
/* 19:43 */     return FastMath.pow(x, this.p);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public UnivariateFunction derivative()
/* 23:   */   {
return new UnivariateFunction()
/* 25:   */     {
/* 26:   */       public double value(double x)
/* 27:   */       {
/* 28:51 */         return Power.this.p * FastMath.pow(x, Power.this.p - 1.0D);
/* 29:   */       }
/* 30:   */     };   }
/* 32:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Power
 * JD-Core Version:    0.7.0.1
 */
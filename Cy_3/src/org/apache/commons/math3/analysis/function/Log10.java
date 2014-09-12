/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  5:   */ import org.apache.commons.math3.util.FastMath;
/*  6:   */ 
/*  7:   */ public class Log10
/*  8:   */   implements DifferentiableUnivariateFunction
/*  9:   */ {
/* 10:32 */   private static final double LN_10 = FastMath.log(10.0D);
/* 11:   */   
/* 12:   */   public double value(double x)
/* 13:   */   {
/* 14:36 */     return FastMath.log10(x);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public UnivariateFunction derivative()
/* 18:   */   {
return new UnivariateFunction()
/* 20:   */     {
/* 21:   */       public double value(double x)
/* 22:   */       {
/* 23:44 */         return 1.0D / (x * Log10.LN_10);
/* 24:   */       }
/* 25:   */     };   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Log10
 * JD-Core Version:    0.7.0.1
 */
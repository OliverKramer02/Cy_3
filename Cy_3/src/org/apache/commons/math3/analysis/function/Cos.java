/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  5:   */ import org.apache.commons.math3.util.FastMath;
/*  6:   */ 
/*  7:   */ public class Cos
/*  8:   */   implements DifferentiableUnivariateFunction
/*  9:   */ {
/* 10:   */   public double value(double x)
/* 11:   */   {
/* 12:33 */     return FastMath.cos(x);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public UnivariateFunction derivative()
/* 16:   */   {
return new UnivariateFunction()
/* 18:   */     {
/* 19:   */       public double value(double x)
/* 20:   */       {
/* 21:41 */         return -FastMath.sin(x);
/* 22:   */       }
/* 23:   */     };   }
/* 25:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Cos
 * JD-Core Version:    0.7.0.1
 */
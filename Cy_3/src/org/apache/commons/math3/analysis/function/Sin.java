/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ import org.apache.commons.math3.util.FastMath;
/*  5:   */ 
/*  6:   */ public class Sin
/*  7:   */   implements DifferentiableUnivariateFunction
/*  8:   */ {
/*  9:   */   public double value(double x)
/* 10:   */   {
/* 11:32 */     return FastMath.sin(x);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public DifferentiableUnivariateFunction derivative()
/* 15:   */   {
/* 16:37 */     return new Cos();
/* 17:   */   }
/* 18:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Sin
 * JD-Core Version:    0.7.0.1
 */
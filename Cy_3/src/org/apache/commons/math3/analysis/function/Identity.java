/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ 
/*  5:   */ public class Identity
/*  6:   */   implements DifferentiableUnivariateFunction
/*  7:   */ {
/*  8:   */   public double value(double x)
/*  9:   */   {
/* 10:31 */     return x;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public DifferentiableUnivariateFunction derivative()
/* 14:   */   {
/* 15:36 */     return new Constant(1.0D);
/* 16:   */   }
/* 17:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Identity
 * JD-Core Version:    0.7.0.1
 */
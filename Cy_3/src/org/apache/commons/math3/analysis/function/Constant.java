/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ 
/*  5:   */ public class Constant
/*  6:   */   implements DifferentiableUnivariateFunction
/*  7:   */ {
/*  8:   */   private final double c;
/*  9:   */   
/* 10:   */   public Constant(double c)
/* 11:   */   {
/* 12:36 */     this.c = c;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public double value(double x)
/* 16:   */   {
/* 17:41 */     return this.c;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public DifferentiableUnivariateFunction derivative()
/* 21:   */   {
/* 22:46 */     return new Constant(0.0D);
/* 23:   */   }
/* 24:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Constant
 * JD-Core Version:    0.7.0.1
 */
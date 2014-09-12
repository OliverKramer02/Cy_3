/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*  4:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  5:   */ 
/*  6:   */ public class Inverse
/*  7:   */   implements DifferentiableUnivariateFunction
/*  8:   */ {
/*  9:   */   public double value(double x)
/* 10:   */   {
/* 11:32 */     return 1.0D / x;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public UnivariateFunction derivative()
/* 15:   */   {
return new UnivariateFunction()
/* 17:   */     {
/* 18:   */       public double value(double x)
/* 19:   */       {
/* 20:40 */         return -1.0D / (x * x);
/* 21:   */       }
/* 22:   */     };   }
/* 24:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Inverse
 * JD-Core Version:    0.7.0.1
 */
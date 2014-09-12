/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  4:   */ import org.apache.commons.math3.util.FastMath;
/*  5:   */ 
/*  6:   */ public class Ulp
/*  7:   */   implements UnivariateFunction
/*  8:   */ {
/*  9:   */   public double value(double x)
/* 10:   */   {
/* 11:32 */     return FastMath.ulp(x);
/* 12:   */   }
/* 13:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Ulp
 * JD-Core Version:    0.7.0.1
 */
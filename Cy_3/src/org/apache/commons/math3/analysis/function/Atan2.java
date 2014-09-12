/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.BivariateFunction;
/*  4:   */ import org.apache.commons.math3.util.FastMath;
/*  5:   */ 
/*  6:   */ public class Atan2
/*  7:   */   implements BivariateFunction
/*  8:   */ {
/*  9:   */   public double value(double x, double y)
/* 10:   */   {
/* 11:32 */     return FastMath.atan2(x, y);
/* 12:   */   }
/* 13:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Atan2
 * JD-Core Version:    0.7.0.1
 */
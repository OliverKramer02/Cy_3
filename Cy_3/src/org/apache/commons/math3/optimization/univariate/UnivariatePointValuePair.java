/*  1:   */ package org.apache.commons.math3.optimization.univariate;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ 
/*  5:   */ public class UnivariatePointValuePair
/*  6:   */   implements Serializable
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 1003888396256744753L;
/*  9:   */   private final double point;
/* 10:   */   private final double value;
/* 11:   */   
/* 12:   */   public UnivariatePointValuePair(double point, double value)
/* 13:   */   {
/* 14:46 */     this.point = point;
/* 15:47 */     this.value = value;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public double getPoint()
/* 19:   */   {
/* 20:56 */     return this.point;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public double getValue()
/* 24:   */   {
/* 25:65 */     return this.value;
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair
 * JD-Core Version:    0.7.0.1
 */
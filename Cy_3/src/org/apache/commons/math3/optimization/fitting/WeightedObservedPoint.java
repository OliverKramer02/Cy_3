/*  1:   */ package org.apache.commons.math3.optimization.fitting;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ 
/*  5:   */ public class WeightedObservedPoint
/*  6:   */   implements Serializable
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 5306874947404636157L;
/*  9:   */   private final double weight;
/* 10:   */   private final double x;
/* 11:   */   private final double y;
/* 12:   */   
/* 13:   */   public WeightedObservedPoint(double weight, double x, double y)
/* 14:   */   {
/* 15:48 */     this.weight = weight;
/* 16:49 */     this.x = x;
/* 17:50 */     this.y = y;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public double getWeight()
/* 21:   */   {
/* 22:57 */     return this.weight;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public double getX()
/* 26:   */   {
/* 27:64 */     return this.x;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public double getY()
/* 31:   */   {
/* 32:71 */     return this.y;
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.fitting.WeightedObservedPoint
 * JD-Core Version:    0.7.0.1
 */
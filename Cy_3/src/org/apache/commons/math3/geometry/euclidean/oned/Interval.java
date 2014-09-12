/*  1:   */ package org.apache.commons.math3.geometry.euclidean.oned;
/*  2:   */ 
/*  3:   */ public class Interval
/*  4:   */ {
/*  5:   */   private final double lower;
/*  6:   */   private final double upper;
/*  7:   */   
/*  8:   */   public Interval(double lower, double upper)
/*  9:   */   {
/* 10:38 */     this.lower = lower;
/* 11:39 */     this.upper = upper;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public double getLower()
/* 15:   */   {
/* 16:46 */     return this.lower;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public double getUpper()
/* 20:   */   {
/* 21:53 */     return this.upper;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public double getLength()
/* 25:   */   {
/* 26:60 */     return this.upper - this.lower;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public double getMidPoint()
/* 30:   */   {
/* 31:67 */     return 0.5D * (this.lower + this.upper);
/* 32:   */   }
/* 33:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.oned.Interval
 * JD-Core Version:    0.7.0.1
 */
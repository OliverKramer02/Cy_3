/*  1:   */ package org.apache.commons.math3.optimization;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.Pair;
/*  4:   */ 
/*  5:   */ public class PointValuePair
/*  6:   */   extends Pair<double[], Double>
/*  7:   */ {
/*  8:   */   public PointValuePair(double[] point, double value)
/*  9:   */   {
/* 10:41 */     this(point, value, true);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public PointValuePair(double[] point, double value, boolean copyArray)
/* 14:   */   {
/* 15:55 */     super(copyArray ? (double[])point.clone() : point == null ? null : point, Double.valueOf(value));
/* 16:   */   }
/* 17:   */   
/* 18:   */   public double[] getPoint()
/* 19:   */   {
/* 20:67 */     double[] p = (double[])getKey();
/* 21:68 */     return p == null ? null : (double[])p.clone();
/* 22:   */   }
/* 23:   */   
/* 24:   */   public double[] getPointRef()
/* 25:   */   {
/* 26:77 */     return (double[])getKey();
/* 27:   */   }
/* 28:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.PointValuePair
 * JD-Core Version:    0.7.0.1
 */
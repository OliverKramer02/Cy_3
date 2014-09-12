/*   1:    */ package org.apache.commons.math3.stat.clustering;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Collection;
/*   5:    */ import org.apache.commons.math3.util.MathArrays;
/*   6:    */ 
/*   7:    */ public class EuclideanIntegerPoint
/*   8:    */   implements Clusterable<EuclideanIntegerPoint>, Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 3946024775784901369L;
/*  11:    */   private final int[] point;
/*  12:    */   
/*  13:    */   public EuclideanIntegerPoint(int[] point)
/*  14:    */   {
/*  15: 44 */     this.point = point;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public int[] getPoint()
/*  19:    */   {
/*  20: 52 */     return this.point;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double distanceFrom(EuclideanIntegerPoint p)
/*  24:    */   {
/*  25: 57 */     return MathArrays.distance(this.point, p.getPoint());
/*  26:    */   }
/*  27:    */   
/*  28:    */   public EuclideanIntegerPoint centroidOf(Collection<EuclideanIntegerPoint> points)
/*  29:    */   {
/*  30: 62 */     int[] centroid = new int[getPoint().length];
/*  31: 63 */     for (EuclideanIntegerPoint p : points) {
/*  32: 64 */       for (int i = 0; i < centroid.length; i++) {
/*  33: 65 */         centroid[i] += p.getPoint()[i];
/*  34:    */       }
/*  35:    */     }
/*  36: 68 */     for (int i = 0; i < centroid.length; i++) {
/*  37: 69 */       centroid[i] /= points.size();
/*  38:    */     }
/*  39: 71 */     return new EuclideanIntegerPoint(centroid);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean equals(Object other)
/*  43:    */   {
/*  44: 77 */     if (!(other instanceof EuclideanIntegerPoint)) {
/*  45: 78 */       return false;
/*  46:    */     }
/*  47: 80 */     int[] otherPoint = ((EuclideanIntegerPoint)other).getPoint();
/*  48: 81 */     if (this.point.length != otherPoint.length) {
/*  49: 82 */       return false;
/*  50:    */     }
/*  51: 84 */     for (int i = 0; i < this.point.length; i++) {
/*  52: 85 */       if (this.point[i] != otherPoint[i]) {
/*  53: 86 */         return false;
/*  54:    */       }
/*  55:    */     }
/*  56: 89 */     return true;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int hashCode()
/*  60:    */   {
/*  61: 95 */     int hashCode = 0;
/*  62: 96 */     int[] arr$ = this.point;int len$ = arr$.length;
/*  63: 96 */     for (int i$ = 0; i$ < len$; i$++)
/*  64:    */     {
/*  65: 96 */       Integer i = Integer.valueOf(arr$[i$]);
/*  66: 97 */       hashCode += i.hashCode() * 13 + 7;
/*  67:    */     }
/*  68: 99 */     return hashCode;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String toString()
/*  72:    */   {
/*  73:108 */     StringBuilder buff = new StringBuilder("(");
/*  74:109 */     int[] coordinates = getPoint();
/*  75:110 */     for (int i = 0; i < coordinates.length; i++)
/*  76:    */     {
/*  77:111 */       buff.append(coordinates[i]);
/*  78:112 */       if (i < coordinates.length - 1) {
/*  79:113 */         buff.append(",");
/*  80:    */       }
/*  81:    */     }
/*  82:116 */     buff.append(")");
/*  83:117 */     return buff.toString();
/*  84:    */   }
/*  85:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.clustering.EuclideanIntegerPoint
 * JD-Core Version:    0.7.0.1
 */
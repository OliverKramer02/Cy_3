/*  1:   */ package org.apache.commons.math3.stat.clustering;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ import java.util.List;
/*  6:   */ 
/*  7:   */ public class Cluster<T extends Clusterable<T>>
/*  8:   */   implements Serializable
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = -3442297081515880464L;
/* 11:   */   private final List<T> points;
/* 12:   */   private final T center;
/* 13:   */   
/* 14:   */   public Cluster(T center)
/* 15:   */   {
/* 16:46 */     this.center = center;
/* 17:47 */     this.points = new ArrayList();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void addPoint(T point)
/* 21:   */   {
/* 22:55 */     this.points.add(point);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public List<T> getPoints()
/* 26:   */   {
/* 27:63 */     return this.points;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public T getCenter()
/* 31:   */   {
/* 32:71 */     return this.center;
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.clustering.Cluster
 * JD-Core Version:    0.7.0.1
 */
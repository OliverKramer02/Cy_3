/*  1:   */ package org.apache.commons.math3.geometry.euclidean.twod;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.geometry.Space;
/*  5:   */ import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
/*  6:   */ 
/*  7:   */ public class Euclidean2D
/*  8:   */   implements Serializable, Space
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 4793432849757649566L;
/* 11:   */   
/* 12:   */   public static Euclidean2D getInstance()
/* 13:   */   {
/* 14:44 */     return LazyHolder.INSTANCE;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public int getDimension()
/* 18:   */   {
/* 19:49 */     return 2;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Euclidean1D getSubSpace()
/* 23:   */   {
/* 24:54 */     return Euclidean1D.getInstance();
/* 25:   */   }
/* 26:   */   
/* 27:   */   private static class LazyHolder
/* 28:   */   {
/* 29:63 */     private static final Euclidean2D INSTANCE = new Euclidean2D();
/* 30:   */   }
/* 31:   */   
/* 32:   */   private Object readResolve()
/* 33:   */   {
/* 34:72 */     return LazyHolder.INSTANCE;
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D
 * JD-Core Version:    0.7.0.1
 */
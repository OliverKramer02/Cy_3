/*  1:   */ package org.apache.commons.math3.geometry.euclidean.threed;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.geometry.Space;
/*  5:   */ import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
/*  6:   */ 
/*  7:   */ public class Euclidean3D
/*  8:   */   implements Serializable, Space
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 6249091865814886817L;
/* 11:   */   
/* 12:   */   public static Euclidean3D getInstance()
/* 13:   */   {
/* 14:44 */     return LazyHolder.INSTANCE;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public int getDimension()
/* 18:   */   {
/* 19:49 */     return 3;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Euclidean2D getSubSpace()
/* 23:   */   {
/* 24:54 */     return Euclidean2D.getInstance();
/* 25:   */   }
/* 26:   */   
/* 27:   */   private static class LazyHolder
/* 28:   */   {
/* 29:63 */     private static final Euclidean3D INSTANCE = new Euclidean3D();
/* 30:   */   }
/* 31:   */   
/* 32:   */   private Object readResolve()
/* 33:   */   {
/* 34:72 */     return LazyHolder.INSTANCE;
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D
 * JD-Core Version:    0.7.0.1
 */
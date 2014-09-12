/*  1:   */ package org.apache.commons.math3.geometry.euclidean.oned;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.exception.MathUnsupportedOperationException;
/*  5:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  6:   */ import org.apache.commons.math3.geometry.Space;
/*  7:   */ 
/*  8:   */ public class Euclidean1D
/*  9:   */   implements Serializable, Space
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = -1178039568877797126L;
/* 12:   */   
/* 13:   */   public static Euclidean1D getInstance()
/* 14:   */   {
/* 15:45 */     return LazyHolder.INSTANCE;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public int getDimension()
/* 19:   */   {
/* 20:50 */     return 1;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Space getSubSpace()
/* 24:   */     throws MathUnsupportedOperationException
/* 25:   */   {
/* 26:62 */     throw new MathUnsupportedOperationException(LocalizedFormats.NOT_SUPPORTED_IN_DIMENSION_N, new Object[] { Integer.valueOf(1) });
/* 27:   */   }
/* 28:   */   
/* 29:   */   private static class LazyHolder
/* 30:   */   {
/* 31:71 */     private static final Euclidean1D INSTANCE = new Euclidean1D();
/* 32:   */   }
/* 33:   */   
/* 34:   */   private Object readResolve()
/* 35:   */   {
/* 36:80 */     return LazyHolder.INSTANCE;
/* 37:   */   }
/* 38:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D
 * JD-Core Version:    0.7.0.1
 */
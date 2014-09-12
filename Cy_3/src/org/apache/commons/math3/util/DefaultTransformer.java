/*  1:   */ package org.apache.commons.math3.util;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  5:   */ import org.apache.commons.math3.exception.NullArgumentException;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ 
/*  8:   */ public class DefaultTransformer
/*  9:   */   implements NumberTransformer, Serializable
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = 4019938025047800455L;
/* 12:   */   
/* 13:   */   public double transform(Object o)
/* 14:   */     throws NullArgumentException, MathIllegalArgumentException
/* 15:   */   {
/* 16:50 */     if (o == null) {
/* 17:51 */       throw new NullArgumentException(LocalizedFormats.OBJECT_TRANSFORMATION, new Object[0]);
/* 18:   */     }
/* 19:54 */     if ((o instanceof Number)) {
/* 20:55 */       return ((Number)o).doubleValue();
/* 21:   */     }
/* 22:   */     try
/* 23:   */     {
/* 24:59 */       return Double.valueOf(o.toString()).doubleValue();
/* 25:   */     }
/* 26:   */     catch (NumberFormatException e)
/* 27:   */     {
/* 28:61 */       throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_TRANSFORM_TO_DOUBLE, new Object[] { o.toString() });
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean equals(Object other)
/* 33:   */   {
/* 34:69 */     if (this == other) {
/* 35:70 */       return true;
/* 36:   */     }
/* 37:72 */     if (other == null) {
/* 38:73 */       return false;
/* 39:   */     }
/* 40:75 */     return other instanceof DefaultTransformer;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int hashCode()
/* 44:   */   {
/* 45:82 */     return 401993047;
/* 46:   */   }
/* 47:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.DefaultTransformer
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.complex;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ import org.apache.commons.math3.util.FastMath;
/*  6:   */ 
/*  7:   */ public class ComplexUtils
/*  8:   */ {
/*  9:   */   public static Complex polar2Complex(double r, double theta)
/* 10:   */   {
/* 11:65 */     if (r < 0.0D) {
/* 12:66 */       throw new MathIllegalArgumentException(LocalizedFormats.NEGATIVE_COMPLEX_MODULE, new Object[] { Double.valueOf(r) });
/* 13:   */     }
/* 14:69 */     return new Complex(r * FastMath.cos(theta), r * FastMath.sin(theta));
/* 15:   */   }
/* 16:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.complex.ComplexUtils
 * JD-Core Version:    0.7.0.1
 */
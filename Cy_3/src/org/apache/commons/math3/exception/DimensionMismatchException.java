/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class DimensionMismatchException
/*  7:   */   extends MathIllegalNumberException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -8415396756375798143L;
/* 10:   */   private final int dimension;
/* 11:   */   
/* 12:   */   public DimensionMismatchException(Localizable specific, int wrong, int expected)
/* 13:   */   {
/* 14:44 */     super(specific, Integer.valueOf(wrong), new Object[] { Integer.valueOf(expected) });
/* 15:45 */     this.dimension = expected;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public DimensionMismatchException(int wrong, int expected)
/* 19:   */   {
/* 20:56 */     this(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, wrong, expected);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getDimension()
/* 24:   */   {
/* 25:63 */     return this.dimension;
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.DimensionMismatchException
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class MultiDimensionMismatchException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -8415396756375798143L;
/* 10:   */   private final Integer[] wrong;
/* 11:   */   private final Integer[] expected;
/* 12:   */   
/* 13:   */   public MultiDimensionMismatchException(Integer[] wrong, Integer[] expected)
/* 14:   */   {
/* 15:45 */     this(LocalizedFormats.DIMENSIONS_MISMATCH, wrong, expected);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MultiDimensionMismatchException(Localizable specific, Integer[] wrong, Integer[] expected)
/* 19:   */   {
/* 20:59 */     super(specific, new Object[] { wrong, expected });
/* 21:60 */     this.wrong = ((Integer[])wrong.clone());
/* 22:61 */     this.expected = ((Integer[])expected.clone());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Integer[] getWrongDimensions()
/* 26:   */   {
/* 27:68 */     return (Integer[])this.wrong.clone();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Integer[] getExpectedDimensions()
/* 31:   */   {
/* 32:74 */     return (Integer[])this.expected.clone();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getWrongDimension(int index)
/* 36:   */   {
/* 37:82 */     return this.wrong[index].intValue();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public int getExpectedDimension(int index)
/* 41:   */   {
/* 42:89 */     return this.expected[index].intValue();
/* 43:   */   }
/* 44:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MultiDimensionMismatchException
 * JD-Core Version:    0.7.0.1
 */
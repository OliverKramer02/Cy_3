/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NonSquareMatrixException
/*  7:   */   extends DimensionMismatchException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -660069396594485772L;
/* 10:   */   
/* 11:   */   public NonSquareMatrixException(int wrong, int expected)
/* 12:   */   {
/* 13:40 */     super(LocalizedFormats.NON_SQUARE_MATRIX, wrong, expected);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.NonSquareMatrixException
 * JD-Core Version:    0.7.0.1
 */
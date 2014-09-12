/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NonSquareOperatorException
/*  7:   */   extends DimensionMismatchException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -4145007524150846242L;
/* 10:   */   
/* 11:   */   public NonSquareOperatorException(int wrong, int expected)
/* 12:   */   {
/* 13:39 */     super(LocalizedFormats.NON_SQUARE_OPERATOR, wrong, expected);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.NonSquareOperatorException
 * JD-Core Version:    0.7.0.1
 */
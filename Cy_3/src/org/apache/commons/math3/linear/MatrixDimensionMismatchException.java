/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MultiDimensionMismatchException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class MatrixDimensionMismatchException
/*  7:   */   extends MultiDimensionMismatchException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -8415396756375798143L;
/* 10:   */   
/* 11:   */   public MatrixDimensionMismatchException(int wrongRowDim, int wrongColDim, int expectedRowDim, int expectedColDim)
/* 12:   */   {
/* 13:45 */     super(LocalizedFormats.DIMENSIONS_MISMATCH_2x2, new Integer[] { Integer.valueOf(wrongRowDim), Integer.valueOf(wrongColDim) }, new Integer[] { Integer.valueOf(expectedRowDim), Integer.valueOf(expectedColDim) });
/* 14:   */   }
/* 15:   */   
/* 16:   */   public int getWrongRowDimension()
/* 17:   */   {
/* 18:54 */     return getWrongDimension(0);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int getExpectedRowDimension()
/* 22:   */   {
/* 23:60 */     return getExpectedDimension(0);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public int getWrongColumnDimension()
/* 27:   */   {
/* 28:66 */     return getWrongDimension(1);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public int getExpectedColumnDimension()
/* 32:   */   {
/* 33:72 */     return getExpectedDimension(1);
/* 34:   */   }
/* 35:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.MatrixDimensionMismatchException
 * JD-Core Version:    0.7.0.1
 */
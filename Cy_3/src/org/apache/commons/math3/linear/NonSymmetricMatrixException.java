/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NonSymmetricMatrixException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -7518495577824189882L;
/* 10:   */   private final int row;
/* 11:   */   private final int column;
/* 12:   */   private final double threshold;
/* 13:   */   
/* 14:   */   public NonSymmetricMatrixException(int row, int column, double threshold)
/* 15:   */   {
/* 16:48 */     super(LocalizedFormats.NON_SYMMETRIC_MATRIX, new Object[] { Integer.valueOf(row), Integer.valueOf(column), Double.valueOf(threshold) });
/* 17:49 */     this.row = row;
/* 18:50 */     this.column = column;
/* 19:51 */     this.threshold = threshold;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public int getRow()
/* 23:   */   {
/* 24:58 */     return this.row;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public int getColumn()
/* 28:   */   {
/* 29:64 */     return this.column;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public double getThreshold()
/* 33:   */   {
/* 34:70 */     return this.threshold;
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.NonSymmetricMatrixException
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ public class DefaultRealMatrixChangingVisitor
/*  4:   */   implements RealMatrixChangingVisitor
/*  5:   */ {
/*  6:   */   public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {}
/*  7:   */   
/*  8:   */   public double visit(int row, int column, double value)
/*  9:   */   {
/* 10:38 */     return value;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public double end()
/* 14:   */   {
/* 15:43 */     return 0.0D;
/* 16:   */   }
/* 17:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.DefaultRealMatrixChangingVisitor
 * JD-Core Version:    0.7.0.1
 */
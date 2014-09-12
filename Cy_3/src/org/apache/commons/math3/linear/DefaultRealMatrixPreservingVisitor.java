/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ public class DefaultRealMatrixPreservingVisitor
/*  4:   */   implements RealMatrixPreservingVisitor
/*  5:   */ {
/*  6:   */   public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {}
/*  7:   */   
/*  8:   */   public void visit(int row, int column, double value) {}
/*  9:   */   
/* 10:   */   public double end()
/* 11:   */   {
/* 12:41 */     return 0.0D;
/* 13:   */   }
/* 14:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor
 * JD-Core Version:    0.7.0.1
 */
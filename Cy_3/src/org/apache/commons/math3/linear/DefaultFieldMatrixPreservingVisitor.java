/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.FieldElement;
/*  4:   */ 
/*  5:   */ public class DefaultFieldMatrixPreservingVisitor<T extends FieldElement<T>>
/*  6:   */   implements FieldMatrixPreservingVisitor<T>
/*  7:   */ {
/*  8:   */   private final T zero;
/*  9:   */   
/* 10:   */   public DefaultFieldMatrixPreservingVisitor(T zero)
/* 11:   */   {
/* 12:42 */     this.zero = zero;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {}
/* 16:   */   
/* 17:   */   public void visit(int row, int column, T value) {}
/* 18:   */   
/* 19:   */   public T end()
/* 20:   */   {
/* 21:55 */     return this.zero;
/* 22:   */   }
/* 23:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor
 * JD-Core Version:    0.7.0.1
 */
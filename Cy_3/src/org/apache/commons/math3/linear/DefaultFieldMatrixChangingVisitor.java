/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.FieldElement;
/*  4:   */ 
/*  5:   */ public class DefaultFieldMatrixChangingVisitor<T extends FieldElement<T>>
/*  6:   */   implements FieldMatrixChangingVisitor<T>
/*  7:   */ {
/*  8:   */   private final T zero;
/*  9:   */   
/* 10:   */   public DefaultFieldMatrixChangingVisitor(T zero)
/* 11:   */   {
/* 12:42 */     this.zero = zero;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {}
/* 16:   */   
/* 17:   */   public T visit(int row, int column, T value)
/* 18:   */   {
/* 19:52 */     return value;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public T end()
/* 23:   */   {
/* 24:57 */     return this.zero;
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.DefaultFieldMatrixChangingVisitor
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.linear;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

public abstract interface FieldMatrix<T extends FieldElement<T>>
  extends AnyMatrix
{
  public abstract Field<T> getField();
  
  public abstract FieldMatrix<T> createMatrix(int paramInt1, int paramInt2);
  
  public abstract FieldMatrix<T> copy();
  
  public abstract FieldMatrix<T> add(FieldMatrix<T> paramFieldMatrix);
  
  public abstract FieldMatrix<T> subtract(FieldMatrix<T> paramFieldMatrix);
  
  public abstract FieldMatrix<T> scalarAdd(T paramT);
  
  public abstract FieldMatrix<T> scalarMultiply(T paramT);
  
  public abstract FieldMatrix<T> multiply(FieldMatrix<T> paramFieldMatrix);
  
  public abstract FieldMatrix<T> preMultiply(FieldMatrix<T> paramFieldMatrix);
  
  public abstract FieldMatrix<T> power(int paramInt);
  
  public abstract T[][] getData();
  
  public abstract FieldMatrix<T> getSubMatrix(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract FieldMatrix<T> getSubMatrix(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
  
  public abstract void copySubMatrix(int paramInt1, int paramInt2, int paramInt3, int paramInt4, T[][] paramArrayOfT);
  
  public abstract void copySubMatrix(int[] paramArrayOfInt1, int[] paramArrayOfInt2, T[][] paramArrayOfT);
  
  public abstract void setSubMatrix(T[][] paramArrayOfT, int paramInt1, int paramInt2);
  
  public abstract FieldMatrix<T> getRowMatrix(int paramInt);
  
  public abstract void setRowMatrix(int paramInt, FieldMatrix<T> paramFieldMatrix);
  
  public abstract FieldMatrix<T> getColumnMatrix(int paramInt);
  
  public abstract void setColumnMatrix(int paramInt, FieldMatrix<T> paramFieldMatrix);
  
  public abstract FieldVector<T> getRowVector(int paramInt);
  
  public abstract void setRowVector(int paramInt, FieldVector<T> paramFieldVector);
  
  public abstract FieldVector<T> getColumnVector(int paramInt);
  
  public abstract void setColumnVector(int paramInt, FieldVector<T> paramFieldVector);
  
  public abstract T[] getRow(int paramInt);
  
  public abstract void setRow(int paramInt, T[] paramArrayOfT);
  
  public abstract T[] getColumn(int paramInt);
  
  public abstract void setColumn(int paramInt, T[] paramArrayOfT);
  
  public abstract T getEntry(int paramInt1, int paramInt2);
  
  public abstract void setEntry(int paramInt1, int paramInt2, T paramT);
  
  public abstract void addToEntry(int paramInt1, int paramInt2, T paramT);
  
  public abstract void multiplyEntry(int paramInt1, int paramInt2, T paramT);
  
  public abstract FieldMatrix<T> transpose();
  
  public abstract T getTrace();
  
  public abstract T[] operate(T[] paramArrayOfT);
  
  public abstract FieldVector<T> operate(FieldVector<T> paramFieldVector);
  
  public abstract T[] preMultiply(T[] paramArrayOfT);
  
  public abstract FieldVector<T> preMultiply(FieldVector<T> paramFieldVector);
  
  public abstract T walkInRowOrder(FieldMatrixChangingVisitor<T> paramFieldMatrixChangingVisitor);
  
  public abstract T walkInRowOrder(FieldMatrixPreservingVisitor<T> paramFieldMatrixPreservingVisitor);
  
  public abstract T walkInRowOrder(FieldMatrixChangingVisitor<T> paramFieldMatrixChangingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract T walkInRowOrder(FieldMatrixPreservingVisitor<T> paramFieldMatrixPreservingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract T walkInColumnOrder(FieldMatrixChangingVisitor<T> paramFieldMatrixChangingVisitor);
  
  public abstract T walkInColumnOrder(FieldMatrixPreservingVisitor<T> paramFieldMatrixPreservingVisitor);
  
  public abstract T walkInColumnOrder(FieldMatrixChangingVisitor<T> paramFieldMatrixChangingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract T walkInColumnOrder(FieldMatrixPreservingVisitor<T> paramFieldMatrixPreservingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> paramFieldMatrixChangingVisitor);
  
  public abstract T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> paramFieldMatrixPreservingVisitor);
  
  public abstract T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> paramFieldMatrixChangingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> paramFieldMatrixPreservingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.FieldMatrix
 * JD-Core Version:    0.7.0.1
 */
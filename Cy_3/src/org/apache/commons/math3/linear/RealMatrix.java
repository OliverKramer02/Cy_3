package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;

public abstract interface RealMatrix
  extends AnyMatrix
{
  public abstract RealMatrix createMatrix(int paramInt1, int paramInt2);
  
  public abstract RealMatrix copy();
  
  public abstract RealMatrix add(RealMatrix paramRealMatrix);
  
  public abstract RealMatrix subtract(RealMatrix paramRealMatrix);
  
  public abstract RealMatrix scalarAdd(double paramDouble);
  
  public abstract RealMatrix scalarMultiply(double paramDouble);
  
  public abstract RealMatrix multiply(RealMatrix paramRealMatrix);
  
  public abstract RealMatrix preMultiply(RealMatrix paramRealMatrix);
  
  public abstract RealMatrix power(int paramInt);
  
  public abstract double[][] getData();
  
  public abstract double getNorm();
  
  public abstract double getFrobeniusNorm();
  
  public abstract RealMatrix getSubMatrix(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract RealMatrix getSubMatrix(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
  
  public abstract void copySubMatrix(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double[][] paramArrayOfDouble);
  
  public abstract void copySubMatrix(int[] paramArrayOfInt1, int[] paramArrayOfInt2, double[][] paramArrayOfDouble);
  
  public abstract void setSubMatrix(double[][] paramArrayOfDouble, int paramInt1, int paramInt2)
    throws ZeroException, OutOfRangeException, DimensionMismatchException, NullArgumentException;
  
  public abstract RealMatrix getRowMatrix(int paramInt);
  
  public abstract void setRowMatrix(int paramInt, RealMatrix paramRealMatrix);
  
  public abstract RealMatrix getColumnMatrix(int paramInt);
  
  public abstract void setColumnMatrix(int paramInt, RealMatrix paramRealMatrix);
  
  public abstract RealVector getRowVector(int paramInt);
  
  public abstract void setRowVector(int paramInt, RealVector paramRealVector);
  
  public abstract RealVector getColumnVector(int paramInt);
  
  public abstract void setColumnVector(int paramInt, RealVector paramRealVector);
  
  public abstract double[] getRow(int paramInt);
  
  public abstract void setRow(int paramInt, double[] paramArrayOfDouble);
  
  public abstract double[] getColumn(int paramInt);
  
  public abstract void setColumn(int paramInt, double[] paramArrayOfDouble);
  
  public abstract double getEntry(int paramInt1, int paramInt2);
  
  public abstract void setEntry(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract void addToEntry(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract void multiplyEntry(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract RealMatrix transpose();
  
  public abstract double getTrace();
  
  public abstract double[] operate(double[] paramArrayOfDouble);
  
  public abstract RealVector operate(RealVector paramRealVector);
  
  public abstract double[] preMultiply(double[] paramArrayOfDouble);
  
  public abstract RealVector preMultiply(RealVector paramRealVector);
  
  public abstract double walkInRowOrder(RealMatrixChangingVisitor paramRealMatrixChangingVisitor);
  
  public abstract double walkInRowOrder(RealMatrixPreservingVisitor paramRealMatrixPreservingVisitor);
  
  public abstract double walkInRowOrder(RealMatrixChangingVisitor paramRealMatrixChangingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract double walkInRowOrder(RealMatrixPreservingVisitor paramRealMatrixPreservingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract double walkInColumnOrder(RealMatrixChangingVisitor paramRealMatrixChangingVisitor);
  
  public abstract double walkInColumnOrder(RealMatrixPreservingVisitor paramRealMatrixPreservingVisitor);
  
  public abstract double walkInColumnOrder(RealMatrixChangingVisitor paramRealMatrixChangingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract double walkInColumnOrder(RealMatrixPreservingVisitor paramRealMatrixPreservingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract double walkInOptimizedOrder(RealMatrixChangingVisitor paramRealMatrixChangingVisitor);
  
  public abstract double walkInOptimizedOrder(RealMatrixPreservingVisitor paramRealMatrixPreservingVisitor);
  
  public abstract double walkInOptimizedOrder(RealMatrixChangingVisitor paramRealMatrixChangingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract double walkInOptimizedOrder(RealMatrixPreservingVisitor paramRealMatrixPreservingVisitor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.RealMatrix
 * JD-Core Version:    0.7.0.1
 */
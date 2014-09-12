package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

public abstract interface FieldDecompositionSolver<T extends FieldElement<T>>
{
  public abstract FieldVector<T> solve(FieldVector<T> paramFieldVector);
  
  public abstract FieldMatrix<T> solve(FieldMatrix<T> paramFieldMatrix);
  
  public abstract boolean isNonSingular();
  
  public abstract FieldMatrix<T> getInverse();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.FieldDecompositionSolver
 * JD-Core Version:    0.7.0.1
 */
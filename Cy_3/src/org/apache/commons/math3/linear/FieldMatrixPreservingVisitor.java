package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

public abstract interface FieldMatrixPreservingVisitor<T extends FieldElement<?>>
{
  public abstract void start(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public abstract void visit(int paramInt1, int paramInt2, T paramT);
  
  public abstract T end();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
 * JD-Core Version:    0.7.0.1
 */
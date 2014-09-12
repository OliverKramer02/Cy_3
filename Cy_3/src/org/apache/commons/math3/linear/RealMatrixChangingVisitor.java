package org.apache.commons.math3.linear;

public abstract interface RealMatrixChangingVisitor
{
  public abstract void start(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public abstract double visit(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract double end();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.RealMatrixChangingVisitor
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.linear;

public abstract interface DecompositionSolver
{
  public abstract RealVector solve(RealVector paramRealVector);
  
  public abstract RealMatrix solve(RealMatrix paramRealMatrix);
  
  public abstract boolean isNonSingular();
  
  public abstract RealMatrix getInverse();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.DecompositionSolver
 * JD-Core Version:    0.7.0.1
 */
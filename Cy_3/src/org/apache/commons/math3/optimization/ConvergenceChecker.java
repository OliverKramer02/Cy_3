package org.apache.commons.math3.optimization;

public abstract interface ConvergenceChecker<PAIR>
{
  public abstract boolean converged(int paramInt, PAIR paramPAIR1, PAIR paramPAIR2);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.ConvergenceChecker
 * JD-Core Version:    0.7.0.1
 */
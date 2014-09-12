package org.apache.commons.math3.optimization;

public abstract interface BaseOptimizer<PAIR>
{
  public abstract int getMaxEvaluations();
  
  public abstract int getEvaluations();
  
  public abstract ConvergenceChecker<PAIR> getConvergenceChecker();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.BaseOptimizer
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;

public abstract interface BaseUnivariateSolver<FUNC extends UnivariateFunction>
{
  public abstract int getMaxEvaluations();
  
  public abstract int getEvaluations();
  
  public abstract double getAbsoluteAccuracy();
  
  public abstract double getRelativeAccuracy();
  
  public abstract double getFunctionValueAccuracy();
  
  public abstract double solve(int paramInt, FUNC paramFUNC, double paramDouble1, double paramDouble2);
  
  public abstract double solve(int paramInt, FUNC paramFUNC, double paramDouble1, double paramDouble2, double paramDouble3);
  
  public abstract double solve(int paramInt, FUNC paramFUNC, double paramDouble);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
 * JD-Core Version:    0.7.0.1
 */
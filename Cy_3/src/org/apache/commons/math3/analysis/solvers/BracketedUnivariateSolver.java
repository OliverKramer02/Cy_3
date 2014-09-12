package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;

public abstract interface BracketedUnivariateSolver<FUNC extends UnivariateFunction>
  extends BaseUnivariateSolver<FUNC>
{
  public abstract double solve(int paramInt, FUNC paramFUNC, double paramDouble1, double paramDouble2, AllowedSolution paramAllowedSolution);
  
  public abstract double solve(int paramInt, FUNC paramFUNC, double paramDouble1, double paramDouble2, double paramDouble3, AllowedSolution paramAllowedSolution);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver
 * JD-Core Version:    0.7.0.1
 */
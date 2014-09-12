package org.apache.commons.math3.analysis;

public abstract interface ParametricUnivariateFunction
{
  public abstract double value(double paramDouble, double... paramVarArgs);
  
  public abstract double[] gradient(double paramDouble, double... paramVarArgs);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.ParametricUnivariateFunction
 * JD-Core Version:    0.7.0.1
 */
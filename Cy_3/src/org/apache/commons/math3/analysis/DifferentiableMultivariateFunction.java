package org.apache.commons.math3.analysis;

public abstract interface DifferentiableMultivariateFunction
  extends MultivariateFunction
{
  public abstract MultivariateFunction partialDerivative(int paramInt);
  
  public abstract MultivariateVectorFunction gradient();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.DifferentiableMultivariateFunction
 * JD-Core Version:    0.7.0.1
 */
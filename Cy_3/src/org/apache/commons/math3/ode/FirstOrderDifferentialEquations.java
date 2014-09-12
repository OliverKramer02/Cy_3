package org.apache.commons.math3.ode;

public abstract interface FirstOrderDifferentialEquations
{
  public abstract int getDimension();
  
  public abstract void computeDerivatives(double paramDouble, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.FirstOrderDifferentialEquations
 * JD-Core Version:    0.7.0.1
 */
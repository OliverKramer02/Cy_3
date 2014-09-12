package org.apache.commons.math3.ode;

public abstract interface MainStateJacobianProvider
  extends FirstOrderDifferentialEquations
{
  public abstract void computeMainStateJacobian(double paramDouble, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[][] paramArrayOfDouble);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.MainStateJacobianProvider
 * JD-Core Version:    0.7.0.1
 */
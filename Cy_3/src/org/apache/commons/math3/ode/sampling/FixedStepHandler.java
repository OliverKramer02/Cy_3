package org.apache.commons.math3.ode.sampling;

public abstract interface FixedStepHandler
{
  public abstract void init(double paramDouble1, double[] paramArrayOfDouble, double paramDouble2);
  
  public abstract void handleStep(double paramDouble, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, boolean paramBoolean);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.sampling.FixedStepHandler
 * JD-Core Version:    0.7.0.1
 */
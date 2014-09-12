package org.apache.commons.math3.ode.sampling;

import java.io.Externalizable;

public abstract interface StepInterpolator
  extends Externalizable
{
  public abstract double getPreviousTime();
  
  public abstract double getCurrentTime();
  
  public abstract double getInterpolatedTime();
  
  public abstract void setInterpolatedTime(double paramDouble);
  
  public abstract double[] getInterpolatedState();
  
  public abstract double[] getInterpolatedDerivatives();
  
  public abstract double[] getInterpolatedSecondaryState(int paramInt);
  
  public abstract double[] getInterpolatedSecondaryDerivatives(int paramInt);
  
  public abstract boolean isForward();
  
  public abstract StepInterpolator copy();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.sampling.StepInterpolator
 * JD-Core Version:    0.7.0.1
 */
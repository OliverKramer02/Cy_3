package org.apache.commons.math3.ode;

public abstract interface ParameterizedODE
  extends Parameterizable
{
  public abstract double getParameter(String paramString)
    throws IllegalArgumentException;
  
  public abstract void setParameter(String paramString, double paramDouble)
    throws IllegalArgumentException;
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ParameterizedODE
 * JD-Core Version:    0.7.0.1
 */
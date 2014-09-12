package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

public abstract interface ParameterJacobianProvider
  extends Parameterizable
{
  public abstract void computeParameterJacobian(double paramDouble, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, String paramString, double[] paramArrayOfDouble3)
    throws MathIllegalArgumentException;
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ParameterJacobianProvider
 * JD-Core Version:    0.7.0.1
 */
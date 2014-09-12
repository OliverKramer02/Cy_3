package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;

public abstract interface SecondOrderIntegrator
  extends ODEIntegrator
{
  public abstract void integrate(SecondOrderDifferentialEquations paramSecondOrderDifferentialEquations, double paramDouble1, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double paramDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4)
    throws MathIllegalStateException, MathIllegalArgumentException;
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.SecondOrderIntegrator
 * JD-Core Version:    0.7.0.1
 */
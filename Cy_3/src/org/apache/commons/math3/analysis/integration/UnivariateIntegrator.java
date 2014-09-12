package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;

public abstract interface UnivariateIntegrator
{
  public abstract double getRelativeAccuracy();
  
  public abstract double getAbsoluteAccuracy();
  
  public abstract int getMinimalIterationCount();
  
  public abstract int getMaximalIterationCount();
  
  public abstract double integrate(int paramInt, UnivariateFunction paramUnivariateFunction, double paramDouble1, double paramDouble2)
    throws TooManyEvaluationsException, MaxCountExceededException, MathIllegalArgumentException, NullArgumentException;
  
  public abstract int getEvaluations();
  
  public abstract int getIterations();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.integration.UnivariateIntegrator
 * JD-Core Version:    0.7.0.1
 */
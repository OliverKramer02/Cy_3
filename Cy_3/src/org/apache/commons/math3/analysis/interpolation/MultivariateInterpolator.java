package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.MultivariateFunction;

public abstract interface MultivariateInterpolator
{
  public abstract MultivariateFunction interpolate(double[][] paramArrayOfDouble, double[] paramArrayOfDouble1);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.MultivariateInterpolator
 * JD-Core Version:    0.7.0.1
 */
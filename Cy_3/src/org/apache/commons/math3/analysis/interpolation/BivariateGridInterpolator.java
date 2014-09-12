package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.BivariateFunction;

public abstract interface BivariateGridInterpolator
{
  public abstract BivariateFunction interpolate(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[][] paramArrayOfDouble);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.TrivariateFunction;

public abstract interface TrivariateGridInterpolator
{
  public abstract TrivariateFunction interpolate(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[][][] paramArrayOfDouble);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.TrivariateGridInterpolator
 * JD-Core Version:    0.7.0.1
 */
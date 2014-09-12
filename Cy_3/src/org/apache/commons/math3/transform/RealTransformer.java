package org.apache.commons.math3.transform;

import org.apache.commons.math3.analysis.UnivariateFunction;

public abstract interface RealTransformer
{
  public abstract double[] transform(double[] paramArrayOfDouble, TransformType paramTransformType);
  
  public abstract double[] transform(UnivariateFunction paramUnivariateFunction, double paramDouble1, double paramDouble2, int paramInt, TransformType paramTransformType);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.transform.RealTransformer
 * JD-Core Version:    0.7.0.1
 */
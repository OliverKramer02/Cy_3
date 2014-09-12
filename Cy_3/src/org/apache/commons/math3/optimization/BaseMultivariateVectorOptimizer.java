package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;

public abstract interface BaseMultivariateVectorOptimizer<FUNC extends MultivariateVectorFunction>
  extends BaseOptimizer<PointVectorValuePair>
{
  public abstract PointVectorValuePair optimize(int paramInt, FUNC paramFUNC, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.BaseMultivariateVectorOptimizer
 * JD-Core Version:    0.7.0.1
 */
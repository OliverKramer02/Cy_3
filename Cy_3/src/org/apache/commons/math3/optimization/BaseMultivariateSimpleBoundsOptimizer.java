package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.MultivariateFunction;

public abstract interface BaseMultivariateSimpleBoundsOptimizer<FUNC extends MultivariateFunction>
  extends BaseMultivariateOptimizer<FUNC>
{
  public abstract PointValuePair optimize(int paramInt, FUNC paramFUNC, GoalType paramGoalType, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.BaseMultivariateSimpleBoundsOptimizer
 * JD-Core Version:    0.7.0.1
 */
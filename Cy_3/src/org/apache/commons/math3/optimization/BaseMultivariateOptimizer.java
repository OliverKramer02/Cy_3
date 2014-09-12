package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.MultivariateFunction;

public abstract interface BaseMultivariateOptimizer<FUNC extends MultivariateFunction>
  extends BaseOptimizer<PointValuePair>
{
  public abstract PointValuePair optimize(int paramInt, FUNC paramFUNC, GoalType paramGoalType, double[] paramArrayOfDouble);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.BaseMultivariateOptimizer
 * JD-Core Version:    0.7.0.1
 */
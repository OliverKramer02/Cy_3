package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.GoalType;

public abstract interface BaseUnivariateOptimizer<FUNC extends UnivariateFunction>
  extends BaseOptimizer<UnivariatePointValuePair>
{
  public abstract UnivariatePointValuePair optimize(int paramInt, FUNC paramFUNC, GoalType paramGoalType, double paramDouble1, double paramDouble2);
  
  public abstract UnivariatePointValuePair optimize(int paramInt, FUNC paramFUNC, GoalType paramGoalType, double paramDouble1, double paramDouble2, double paramDouble3);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.univariate.BaseUnivariateOptimizer
 * JD-Core Version:    0.7.0.1
 */
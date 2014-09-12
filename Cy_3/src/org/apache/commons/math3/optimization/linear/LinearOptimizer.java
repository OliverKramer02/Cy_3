package org.apache.commons.math3.optimization.linear;

import java.util.Collection;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;

public abstract interface LinearOptimizer
{
  public abstract void setMaxIterations(int paramInt);
  
  public abstract int getMaxIterations();
  
  public abstract int getIterations();
  
  public abstract PointValuePair optimize(LinearObjectiveFunction paramLinearObjectiveFunction, Collection<LinearConstraint> paramCollection, GoalType paramGoalType, boolean paramBoolean)
    throws MathIllegalStateException;
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.LinearOptimizer
 * JD-Core Version:    0.7.0.1
 */
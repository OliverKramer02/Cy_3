package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

public abstract interface RealDistribution
{
  public abstract double probability(double paramDouble);
  
  public abstract double density(double paramDouble);
  
  public abstract double cumulativeProbability(double paramDouble);
  
  public abstract double cumulativeProbability(double paramDouble1, double paramDouble2)
    throws NumberIsTooLargeException;
  
  public abstract double inverseCumulativeProbability(double paramDouble)
    throws OutOfRangeException;
  
  public abstract double getNumericalMean();
  
  public abstract double getNumericalVariance();
  
  public abstract double getSupportLowerBound();
  
  public abstract double getSupportUpperBound();
  
  public abstract boolean isSupportLowerBoundInclusive();
  
  public abstract boolean isSupportUpperBoundInclusive();
  
  public abstract boolean isSupportConnected();
  
  public abstract void reseedRandomGenerator(long paramLong);
  
  public abstract double sample();
  
  public abstract double[] sample(int paramInt);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.RealDistribution
 * JD-Core Version:    0.7.0.1
 */
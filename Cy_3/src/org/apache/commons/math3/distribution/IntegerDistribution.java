package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

public abstract interface IntegerDistribution
{
  public abstract double probability(int paramInt);
  
  public abstract double cumulativeProbability(int paramInt);
  
  public abstract double cumulativeProbability(int paramInt1, int paramInt2)
    throws NumberIsTooLargeException;
  
  public abstract int inverseCumulativeProbability(double paramDouble)
    throws OutOfRangeException;
  
  public abstract double getNumericalMean();
  
  public abstract double getNumericalVariance();
  
  public abstract int getSupportLowerBound();
  
  public abstract int getSupportUpperBound();
  
  public abstract boolean isSupportConnected();
  
  public abstract void reseedRandomGenerator(long paramLong);
  
  public abstract int sample();
  
  public abstract int[] sample(int paramInt);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.IntegerDistribution
 * JD-Core Version:    0.7.0.1
 */
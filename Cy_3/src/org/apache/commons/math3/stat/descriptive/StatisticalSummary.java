package org.apache.commons.math3.stat.descriptive;

public abstract interface StatisticalSummary
{
  public abstract double getMean();
  
  public abstract double getVariance();
  
  public abstract double getStandardDeviation();
  
  public abstract double getMax();
  
  public abstract double getMin();
  
  public abstract long getN();
  
  public abstract double getSum();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.StatisticalSummary
 * JD-Core Version:    0.7.0.1
 */
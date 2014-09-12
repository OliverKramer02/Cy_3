package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.linear.RealMatrix;

public abstract interface StatisticalMultivariateSummary
{
  public abstract int getDimension();
  
  public abstract double[] getMean();
  
  public abstract RealMatrix getCovariance();
  
  public abstract double[] getStandardDeviation();
  
  public abstract double[] getMax();
  
  public abstract double[] getMin();
  
  public abstract long getN();
  
  public abstract double[] getGeometricMean();
  
  public abstract double[] getSum();
  
  public abstract double[] getSumSq();
  
  public abstract double[] getSumLog();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
 * JD-Core Version:    0.7.0.1
 */
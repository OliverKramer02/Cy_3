package org.apache.commons.math3.stat.descriptive;

public abstract interface StorelessUnivariateStatistic
  extends UnivariateStatistic
{
  public abstract void increment(double paramDouble);
  
  public abstract void incrementAll(double[] paramArrayOfDouble);
  
  public abstract void incrementAll(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  
  public abstract double getResult();
  
  public abstract long getN();
  
  public abstract void clear();
  
  public abstract StorelessUnivariateStatistic copy();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
 * JD-Core Version:    0.7.0.1
 */
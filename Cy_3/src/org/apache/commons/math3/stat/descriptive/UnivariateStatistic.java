package org.apache.commons.math3.stat.descriptive;

public abstract interface UnivariateStatistic
{
  public abstract double evaluate(double[] paramArrayOfDouble);
  
  public abstract double evaluate(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  
  public abstract UnivariateStatistic copy();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.UnivariateStatistic
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.stat.descriptive;

public abstract interface WeightedEvaluation
{
  public abstract double evaluate(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
  
  public abstract double evaluate(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int paramInt1, int paramInt2);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.WeightedEvaluation
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.stat.regression;

public abstract interface UpdatingMultipleLinearRegression
{
  public abstract boolean hasIntercept();
  
  public abstract long getN();
  
  public abstract void addObservation(double[] paramArrayOfDouble, double paramDouble)
    throws ModelSpecificationException;
  
  public abstract void addObservations(double[][] paramArrayOfDouble, double[] paramArrayOfDouble1)
    throws ModelSpecificationException;
  
  public abstract void clear();
  
  public abstract RegressionResults regress()
    throws ModelSpecificationException;
  
  public abstract RegressionResults regress(int[] paramArrayOfInt)
    throws ModelSpecificationException;
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
 * JD-Core Version:    0.7.0.1
 */
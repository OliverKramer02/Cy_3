package org.apache.commons.math3.analysis;

public abstract interface MultivariateMatrixFunction
{
  public abstract double[][] value(double[] paramArrayOfDouble)
    throws IllegalArgumentException;
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.MultivariateMatrixFunction
 * JD-Core Version:    0.7.0.1
 */
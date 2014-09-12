package org.apache.commons.math3.optimization.general;

public abstract interface Preconditioner
{
  public abstract double[] precondition(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.general.Preconditioner
 * JD-Core Version:    0.7.0.1
 */
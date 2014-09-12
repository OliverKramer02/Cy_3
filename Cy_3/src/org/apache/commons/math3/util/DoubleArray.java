package org.apache.commons.math3.util;

public abstract interface DoubleArray
{
  public abstract int getNumElements();
  
  public abstract double getElement(int paramInt);
  
  public abstract void setElement(int paramInt, double paramDouble);
  
  public abstract void addElement(double paramDouble);
  
  public abstract void addElements(double[] paramArrayOfDouble);
  
  public abstract double addElementRolling(double paramDouble);
  
  public abstract double[] getElements();
  
  public abstract void clear();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.DoubleArray
 * JD-Core Version:    0.7.0.1
 */
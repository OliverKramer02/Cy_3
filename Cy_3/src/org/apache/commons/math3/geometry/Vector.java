package org.apache.commons.math3.geometry;

import java.io.Serializable;
import java.text.NumberFormat;

public abstract interface Vector<S extends Space>
  extends Serializable
{
  public abstract Space getSpace();
  
  public abstract Vector<S> getZero();
  
  public abstract double getNorm1();
  
  public abstract double getNorm();
  
  public abstract double getNormSq();
  
  public abstract double getNormInf();
  
  public abstract Vector<S> add(Vector<S> paramVector);
  
  public abstract Vector<S> add(double paramDouble, Vector<S> paramVector);
  
  public abstract Vector<S> subtract(Vector<S> paramVector);
  
  public abstract Vector<S> subtract(double paramDouble, Vector<S> paramVector);
  
  public abstract Vector<S> negate();
  
  public abstract Vector<S> normalize();
  
  public abstract Vector<S> scalarMultiply(double paramDouble);
  
  public abstract boolean isNaN();
  
  public abstract boolean isInfinite();
  
  public abstract double distance1(Vector<S> paramVector);
  
  public abstract double distance(Vector<S> paramVector);
  
  public abstract double distanceInf(Vector<S> paramVector);
  
  public abstract double distanceSq(Vector<S> paramVector);
  
  public abstract double dotProduct(Vector<S> paramVector);
  
  public abstract String toString(NumberFormat paramNumberFormat);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.Vector
 * JD-Core Version:    0.7.0.1
 */
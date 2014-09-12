package org.apache.commons.math3.geometry;

import java.io.Serializable;

public abstract interface Space
  extends Serializable
{
  public abstract int getDimension();
  
  public abstract Space getSubSpace();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.Space
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;

public abstract interface Hyperplane<S extends Space>
{
  public abstract Hyperplane<S> copySelf();
  
  public abstract double getOffset(Vector<S> paramVector);
  
  public abstract boolean sameOrientationAs(Hyperplane<S> paramHyperplane);
  
  public abstract SubHyperplane<S> wholeHyperplane();
  
  public abstract Region<S> wholeSpace();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.Hyperplane
 * JD-Core Version:    0.7.0.1
 */
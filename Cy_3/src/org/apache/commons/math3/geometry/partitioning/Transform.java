package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;

public abstract interface Transform<S extends Space, T extends Space>
{
  public abstract Vector<S> apply(Vector<S> paramVector);
  
  public abstract Hyperplane<S> apply(Hyperplane<S> paramHyperplane);
  
  public abstract SubHyperplane<T> apply(SubHyperplane<T> paramSubHyperplane, Hyperplane<S> paramHyperplane1, Hyperplane<S> paramHyperplane2);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.Transform
 * JD-Core Version:    0.7.0.1
 */
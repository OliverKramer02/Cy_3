package org.apache.commons.math3.stat.clustering;

import java.util.Collection;

public abstract interface Clusterable<T>
{
  public abstract double distanceFrom(T paramT);
  
  public abstract T centroidOf(Collection<T> paramCollection);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.clustering.Clusterable
 * JD-Core Version:    0.7.0.1
 */
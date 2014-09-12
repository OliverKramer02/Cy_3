package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;

public abstract interface Embedding<S extends Space, T extends Space>
{
  public abstract Vector<T> toSubSpace(Vector<S> paramVector);
  
  public abstract Vector<S> toSpace(Vector<T> paramVector);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.Embedding
 * JD-Core Version:    0.7.0.1
 */
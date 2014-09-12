package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

public abstract interface NumberTransformer
{
  public abstract double transform(Object paramObject)
    throws MathIllegalArgumentException;
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.NumberTransformer
 * JD-Core Version:    0.7.0.1
 */
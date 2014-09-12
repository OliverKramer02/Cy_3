package org.apache.commons.math3;

public abstract interface Field<T>
{
  public abstract T getZero();
  
  public abstract T getOne();
  
  public abstract Class<? extends FieldElement<T>> getRuntimeClass();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.Field
 * JD-Core Version:    0.7.0.1
 */
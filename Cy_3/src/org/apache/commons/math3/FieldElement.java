package org.apache.commons.math3;

public abstract interface FieldElement<T>
{
  public abstract T add(T paramT);
  
  public abstract T subtract(T paramT);
  
  public abstract T negate();
  
  public abstract T multiply(int paramInt);
  
  public abstract T multiply(T paramT);
  
  public abstract T divide(T paramT);
  
  public abstract T reciprocal();
  
  public abstract Field<T> getField();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.FieldElement
 * JD-Core Version:    0.7.0.1
 */
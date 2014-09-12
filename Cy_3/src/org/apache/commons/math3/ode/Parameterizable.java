package org.apache.commons.math3.ode;

import java.util.Collection;

public abstract interface Parameterizable
{
  public abstract Collection<String> getParametersNames();
  
  public abstract boolean isSupported(String paramString);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.Parameterizable
 * JD-Core Version:    0.7.0.1
 */
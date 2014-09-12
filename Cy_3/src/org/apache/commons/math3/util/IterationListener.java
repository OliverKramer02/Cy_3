package org.apache.commons.math3.util;

import java.util.EventListener;

public abstract interface IterationListener
  extends EventListener
{
  public abstract void initializationPerformed(IterationEvent paramIterationEvent);
  
  public abstract void iterationPerformed(IterationEvent paramIterationEvent);
  
  public abstract void iterationStarted(IterationEvent paramIterationEvent);
  
  public abstract void terminationPerformed(IterationEvent paramIterationEvent);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.IterationListener
 * JD-Core Version:    0.7.0.1
 */
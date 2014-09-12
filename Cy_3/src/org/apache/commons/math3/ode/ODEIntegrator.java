package org.apache.commons.math3.ode;

import java.util.Collection;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;

public abstract interface ODEIntegrator
{
  public abstract String getName();
  
  public abstract void addStepHandler(StepHandler paramStepHandler);
  
  public abstract Collection<StepHandler> getStepHandlers();
  
  public abstract void clearStepHandlers();
  
  public abstract void addEventHandler(EventHandler paramEventHandler, double paramDouble1, double paramDouble2, int paramInt);
  
  public abstract void addEventHandler(EventHandler paramEventHandler, double paramDouble1, double paramDouble2, int paramInt, UnivariateSolver paramUnivariateSolver);
  
  public abstract Collection<EventHandler> getEventHandlers();
  
  public abstract void clearEventHandlers();
  
  public abstract double getCurrentStepStart();
  
  public abstract double getCurrentSignedStepsize();
  
  public abstract void setMaxEvaluations(int paramInt);
  
  public abstract int getMaxEvaluations();
  
  public abstract int getEvaluations();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ODEIntegrator
 * JD-Core Version:    0.7.0.1
 */
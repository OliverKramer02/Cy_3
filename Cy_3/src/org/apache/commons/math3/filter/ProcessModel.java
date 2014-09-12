package org.apache.commons.math3.filter;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public abstract interface ProcessModel
{
  public abstract RealMatrix getStateTransitionMatrix();
  
  public abstract RealMatrix getControlMatrix();
  
  public abstract RealMatrix getProcessNoise();
  
  public abstract RealVector getInitialStateEstimate();
  
  public abstract RealMatrix getInitialErrorCovariance();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.filter.ProcessModel
 * JD-Core Version:    0.7.0.1
 */
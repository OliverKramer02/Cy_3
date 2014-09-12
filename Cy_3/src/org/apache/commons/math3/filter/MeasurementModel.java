package org.apache.commons.math3.filter;

import org.apache.commons.math3.linear.RealMatrix;

public abstract interface MeasurementModel
{
  public abstract RealMatrix getMeasurementMatrix();
  
  public abstract RealMatrix getMeasurementNoise();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.filter.MeasurementModel
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.stat.regression;

public abstract interface MultipleLinearRegression
{
  public abstract double[] estimateRegressionParameters();
  
  public abstract double[][] estimateRegressionParametersVariance();
  
  public abstract double[] estimateResiduals();
  
  public abstract double estimateRegressandVariance();
  
  public abstract double[] estimateRegressionParametersStandardErrors();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.MultipleLinearRegression
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.optimization.fitting;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
/*   6:    */ import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
/*   7:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
/*   8:    */ import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
/*   9:    */ import org.apache.commons.math3.optimization.PointVectorValuePair;
/*  10:    */ 
/*  11:    */ public class CurveFitter
/*  12:    */ {
/*  13:    */   private final DifferentiableMultivariateVectorOptimizer optimizer;
/*  14:    */   private final List<WeightedObservedPoint> observations;
/*  15:    */   
/*  16:    */   public CurveFitter(DifferentiableMultivariateVectorOptimizer optimizer)
/*  17:    */   {
/*  18: 52 */     this.optimizer = optimizer;
/*  19: 53 */     this.observations = new ArrayList();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void addObservedPoint(double x, double y)
/*  23:    */   {
/*  24: 67 */     addObservedPoint(1.0D, x, y);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void addObservedPoint(double weight, double x, double y)
/*  28:    */   {
/*  29: 80 */     this.observations.add(new WeightedObservedPoint(weight, x, y));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void addObservedPoint(WeightedObservedPoint observed)
/*  33:    */   {
/*  34: 90 */     this.observations.add(observed);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public WeightedObservedPoint[] getObservations()
/*  38:    */   {
/*  39:100 */     return (WeightedObservedPoint[])this.observations.toArray(new WeightedObservedPoint[this.observations.size()]);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void clearObservations()
/*  43:    */   {
/*  44:107 */     this.observations.clear();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double[] fit(ParametricUnivariateFunction f, double[] initialGuess)
/*  48:    */   {
/*  49:124 */     return fit(2147483647, f, initialGuess);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public double[] fit(int maxEval, ParametricUnivariateFunction f, double[] initialGuess)
/*  53:    */   {
/*  54:147 */     double[] target = new double[this.observations.size()];
/*  55:148 */     double[] weights = new double[this.observations.size()];
/*  56:149 */     int i = 0;
/*  57:150 */     for (WeightedObservedPoint point : this.observations)
/*  58:    */     {
/*  59:151 */       target[i] = point.getY();
/*  60:152 */       weights[i] = point.getWeight();
/*  61:153 */       i++;
/*  62:    */     }
/*  63:157 */     PointVectorValuePair optimum = this.optimizer.optimize(maxEval, new TheoreticalValuesFunction(f), target, weights, initialGuess);
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:162 */     return optimum.getPointRef();
/*  69:    */   }
/*  70:    */   
/*  71:    */   private class TheoreticalValuesFunction
/*  72:    */     implements DifferentiableMultivariateVectorFunction
/*  73:    */   {
/*  74:    */     private final ParametricUnivariateFunction f;
/*  75:    */     
/*  76:    */     public TheoreticalValuesFunction(ParametricUnivariateFunction f)
/*  77:    */     {
/*  78:175 */       this.f = f;
/*  79:    */     }
/*  80:    */     
/*  81:    */     public MultivariateMatrixFunction jacobian()
/*  82:    */     {
return new MultivariateMatrixFunction()
/*  84:    */       {
/*  85:    */         public double[][] value(double[] point)
/*  86:    */         {
/*  87:182 */           double[][] jacobian = new double[CurveFitter.this.observations.size()][];
/*  88:    */           
/*  89:184 */           int i = 0;
/*  90:185 */           for (WeightedObservedPoint observed : CurveFitter.this.observations) {
/*  91:186 */             jacobian[(i++)] = CurveFitter.TheoreticalValuesFunction.this.f.gradient(observed.getX(), point);
/*  92:    */           }
/*  93:189 */           return jacobian;
/*  94:    */         }
/*  95:    */       };     }
/*  97:    */     
/*  98:    */     public double[] value(double[] point)
/*  99:    */     {
/* 100:197 */       double[] values = new double[CurveFitter.this.observations.size()];
/* 101:198 */       int i = 0;
/* 102:199 */       for (WeightedObservedPoint observed : CurveFitter.this.observations) {
/* 103:200 */         values[(i++)] = this.f.value(observed.getX(), point);
/* 104:    */       }
/* 105:203 */       return values;
/* 106:    */     }
/* 107:    */   }
/* 108:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.fitting.CurveFitter
 * JD-Core Version:    0.7.0.1
 */
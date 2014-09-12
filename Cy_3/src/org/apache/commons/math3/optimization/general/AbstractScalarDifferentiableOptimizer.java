/*  1:   */ package org.apache.commons.math3.optimization.general;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableMultivariateFunction;
/*  4:   */ import org.apache.commons.math3.analysis.MultivariateVectorFunction;
/*  5:   */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*  6:   */ import org.apache.commons.math3.optimization.DifferentiableMultivariateOptimizer;
/*  7:   */ import org.apache.commons.math3.optimization.GoalType;
/*  8:   */ import org.apache.commons.math3.optimization.PointValuePair;
/*  9:   */ import org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer;
/* 10:   */ 
/* 11:   */ public abstract class AbstractScalarDifferentiableOptimizer
/* 12:   */   extends BaseAbstractMultivariateOptimizer<DifferentiableMultivariateFunction>
/* 13:   */   implements DifferentiableMultivariateOptimizer
/* 14:   */ {
/* 15:   */   private MultivariateVectorFunction gradient;
/* 16:   */   
/* 17:   */   protected AbstractScalarDifferentiableOptimizer() {}
/* 18:   */   
/* 19:   */   protected AbstractScalarDifferentiableOptimizer(ConvergenceChecker<PointValuePair> checker)
/* 20:   */   {
/* 21:55 */     super(checker);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected double[] computeObjectiveGradient(double[] evaluationPoint)
/* 25:   */   {
/* 26:67 */     return this.gradient.value(evaluationPoint);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public PointValuePair optimize(int maxEval, DifferentiableMultivariateFunction f, GoalType goalType, double[] startPoint)
/* 30:   */   {
/* 31:77 */     this.gradient = f.gradient();
/* 32:   */     
/* 33:79 */     return super.optimize(maxEval, f, goalType, startPoint);
/* 34:   */   }
/* 35:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.general.AbstractScalarDifferentiableOptimizer
 * JD-Core Version:    0.7.0.1
 */
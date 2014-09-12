/*  1:   */ package org.apache.commons.math3.optimization;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
/*  4:   */ import org.apache.commons.math3.random.RandomVectorGenerator;
/*  5:   */ 
/*  6:   */ public class DifferentiableMultivariateVectorMultiStartOptimizer
/*  7:   */   extends BaseMultivariateVectorMultiStartOptimizer<DifferentiableMultivariateVectorFunction>
/*  8:   */   implements DifferentiableMultivariateVectorOptimizer
/*  9:   */ {
/* 10:   */   public DifferentiableMultivariateVectorMultiStartOptimizer(DifferentiableMultivariateVectorOptimizer optimizer, int starts, RandomVectorGenerator generator)
/* 11:   */   {
/* 12:50 */     super(optimizer, starts, generator);
/* 13:   */   }
/* 14:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.DifferentiableMultivariateVectorMultiStartOptimizer
 * JD-Core Version:    0.7.0.1
 */
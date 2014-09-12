/*  1:   */ package org.apache.commons.math3.optimization;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*  4:   */ import org.apache.commons.math3.random.RandomVectorGenerator;
/*  5:   */ 
/*  6:   */ public class MultivariateMultiStartOptimizer
/*  7:   */   extends BaseMultivariateMultiStartOptimizer<MultivariateFunction>
/*  8:   */   implements MultivariateOptimizer
/*  9:   */ {
/* 10:   */   public MultivariateMultiStartOptimizer(MultivariateOptimizer optimizer, int starts, RandomVectorGenerator generator)
/* 11:   */   {
/* 12:49 */     super(optimizer, starts, generator);
/* 13:   */   }
/* 14:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.MultivariateMultiStartOptimizer
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.random;
/*  2:   */ 
/*  3:   */ public class GaussianRandomGenerator
/*  4:   */   implements NormalizedRandomGenerator
/*  5:   */ {
/*  6:   */   private final RandomGenerator generator;
/*  7:   */   
/*  8:   */   public GaussianRandomGenerator(RandomGenerator generator)
/*  9:   */   {
/* 10:37 */     this.generator = generator;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public double nextNormalizedDouble()
/* 14:   */   {
/* 15:44 */     return this.generator.nextGaussian();
/* 16:   */   }
/* 17:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.GaussianRandomGenerator
 * JD-Core Version:    0.7.0.1
 */
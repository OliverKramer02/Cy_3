/*  1:   */ package org.apache.commons.math3.random;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.FastMath;
/*  4:   */ 
/*  5:   */ public class UniformRandomGenerator
/*  6:   */   implements NormalizedRandomGenerator
/*  7:   */ {
/*  8:37 */   private static final double SQRT3 = FastMath.sqrt(3.0D);
/*  9:   */   private final RandomGenerator generator;
/* 10:   */   
/* 11:   */   public UniformRandomGenerator(RandomGenerator generator)
/* 12:   */   {
/* 13:46 */     this.generator = generator;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public double nextNormalizedDouble()
/* 17:   */   {
/* 18:55 */     return SQRT3 * (2.0D * this.generator.nextDouble() - 1.0D);
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.UniformRandomGenerator
 * JD-Core Version:    0.7.0.1
 */
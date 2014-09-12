/*  1:   */ package org.apache.commons.math3.random;
/*  2:   */ 
/*  3:   */ import java.util.Arrays;
/*  4:   */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  5:   */ 
/*  6:   */ public class UncorrelatedRandomVectorGenerator
/*  7:   */   implements RandomVectorGenerator
/*  8:   */ {
/*  9:   */   private final NormalizedRandomGenerator generator;
/* 10:   */   private final double[] mean;
/* 11:   */   private final double[] standardDeviation;
/* 12:   */   
/* 13:   */   public UncorrelatedRandomVectorGenerator(double[] mean, double[] standardDeviation, NormalizedRandomGenerator generator)
/* 14:   */   {
/* 15:56 */     if (mean.length != standardDeviation.length) {
/* 16:57 */       throw new DimensionMismatchException(mean.length, standardDeviation.length);
/* 17:   */     }
/* 18:59 */     this.mean = ((double[])mean.clone());
/* 19:60 */     this.standardDeviation = ((double[])standardDeviation.clone());
/* 20:61 */     this.generator = generator;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public UncorrelatedRandomVectorGenerator(int dimension, NormalizedRandomGenerator generator)
/* 24:   */   {
/* 25:73 */     this.mean = new double[dimension];
/* 26:74 */     this.standardDeviation = new double[dimension];
/* 27:75 */     Arrays.fill(this.standardDeviation, 1.0D);
/* 28:76 */     this.generator = generator;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public double[] nextVector()
/* 32:   */   {
/* 33:84 */     double[] random = new double[this.mean.length];
/* 34:85 */     for (int i = 0; i < random.length; i++) {
/* 35:86 */       random[i] = (this.mean[i] + this.standardDeviation[i] * this.generator.nextNormalizedDouble());
/* 36:   */     }
/* 37:89 */     return random;
/* 38:   */   }
/* 39:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator
 * JD-Core Version:    0.7.0.1
 */
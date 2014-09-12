/*  1:   */ package org.apache.commons.math3.random;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.FastMath;
/*  4:   */ 
/*  5:   */ public class UnitSphereRandomVectorGenerator
/*  6:   */   implements RandomVectorGenerator
/*  7:   */ {
/*  8:   */   private final RandomGenerator rand;
/*  9:   */   private final int dimension;
/* 10:   */   
/* 11:   */   public UnitSphereRandomVectorGenerator(int dimension, RandomGenerator rand)
/* 12:   */   {
/* 13:47 */     this.dimension = dimension;
/* 14:48 */     this.rand = rand;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public UnitSphereRandomVectorGenerator(int dimension)
/* 18:   */   {
/* 19:57 */     this(dimension, new MersenneTwister());
/* 20:   */   }
/* 21:   */   
/* 22:   */   public double[] nextVector()
/* 23:   */   {
/* 24:63 */     double[] v = new double[this.dimension];
/* 25:   */     double normSq;
/* 26:   */     do
/* 27:   */     {
/* 28:67 */       normSq = 0.0D;
/* 29:68 */       for (int i = 0; i < this.dimension; i++)
/* 30:   */       {
/* 31:69 */         double comp = 2.0D * this.rand.nextDouble() - 1.0D;
/* 32:70 */         v[i] = comp;
/* 33:71 */         normSq += comp * comp;
/* 34:   */       }
/* 35:73 */     } while (normSq > 1.0D);
/* 36:75 */     double f = 1.0D / FastMath.sqrt(normSq);
/* 37:76 */     for (int i = 0; i < this.dimension; i++) {
/* 38:77 */       v[i] *= f;
/* 39:   */     }
/* 40:80 */     return v;
/* 41:   */   }
/* 42:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.UnitSphereRandomVectorGenerator
 * JD-Core Version:    0.7.0.1
 */
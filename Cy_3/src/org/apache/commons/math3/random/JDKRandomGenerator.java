/*  1:   */ package org.apache.commons.math3.random;
/*  2:   */ 
/*  3:   */ import java.util.Random;
/*  4:   */ 
/*  5:   */ public class JDKRandomGenerator
/*  6:   */   extends Random
/*  7:   */   implements RandomGenerator
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -7745277476784028798L;
/* 10:   */   
/* 11:   */   public void setSeed(int seed)
/* 12:   */   {
/* 13:35 */     setSeed(seed);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void setSeed(int[] seed)
/* 17:   */   {
/* 18:41 */     long prime = 4294967291L;
/* 19:   */     
/* 20:43 */     long combined = 0L;
/* 21:44 */     for (int s : seed) {
/* 22:45 */       combined = combined * 4294967291L + s;
/* 23:   */     }
/* 24:47 */     setSeed(combined);
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.JDKRandomGenerator
 * JD-Core Version:    0.7.0.1
 */
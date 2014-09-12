/*  1:   */ package org.apache.commons.math3.genetics;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  4:   */ 
/*  5:   */ public class FixedGenerationCount
/*  6:   */   implements StoppingCondition
/*  7:   */ {
/*  8:33 */   private int numGenerations = 0;
/*  9:   */   private final int maxGenerations;
/* 10:   */   
/* 11:   */   public FixedGenerationCount(int maxGenerations)
/* 12:   */   {
/* 13:45 */     if (maxGenerations <= 0) {
/* 14:46 */       throw new NumberIsTooSmallException(Integer.valueOf(maxGenerations), Integer.valueOf(1), true);
/* 15:   */     }
/* 16:48 */     this.maxGenerations = maxGenerations;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean isSatisfied(Population population)
/* 20:   */   {
/* 21:60 */     if (this.numGenerations < this.maxGenerations)
/* 22:   */     {
/* 23:61 */       this.numGenerations += 1;
/* 24:62 */       return false;
/* 25:   */     }
/* 26:64 */     return true;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public int getNumGenerations()
/* 30:   */   {
/* 31:71 */     return this.numGenerations;
/* 32:   */   }
/* 33:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.FixedGenerationCount
 * JD-Core Version:    0.7.0.1
 */
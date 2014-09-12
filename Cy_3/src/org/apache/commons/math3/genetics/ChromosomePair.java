/*  1:   */ package org.apache.commons.math3.genetics;
/*  2:   */ 
/*  3:   */ public class ChromosomePair
/*  4:   */ {
/*  5:   */   private final Chromosome first;
/*  6:   */   private final Chromosome second;
/*  7:   */   
/*  8:   */   public ChromosomePair(Chromosome c1, Chromosome c2)
/*  9:   */   {
/* 10:40 */     this.first = c1;
/* 11:41 */     this.second = c2;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public Chromosome getFirst()
/* 15:   */   {
/* 16:50 */     return this.first;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Chromosome getSecond()
/* 20:   */   {
/* 21:59 */     return this.second;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String toString()
/* 25:   */   {
/* 26:67 */     return String.format("(%s,%s)", new Object[] { getFirst(), getSecond() });
/* 27:   */   }
/* 28:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.ChromosomePair
 * JD-Core Version:    0.7.0.1
 */
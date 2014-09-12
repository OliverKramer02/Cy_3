/*  1:   */ package org.apache.commons.math3.ode.sampling;
/*  2:   */ 
/*  3:   */ public enum StepNormalizerBounds
/*  4:   */ {
/*  5:32 */   NEITHER(false, false),  FIRST(true, false),  LAST(false, true),  BOTH(true, true);
/*  6:   */   
/*  7:   */   private final boolean first;
/*  8:   */   private final boolean last;
/*  9:   */   
/* 10:   */   private StepNormalizerBounds(boolean first, boolean last)
/* 11:   */   {
/* 12:61 */     this.first = first;
/* 13:62 */     this.last = last;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean firstIncluded()
/* 17:   */   {
/* 18:72 */     return this.first;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean lastIncluded()
/* 22:   */   {
/* 23:82 */     return this.last;
/* 24:   */   }
/* 25:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.sampling.StepNormalizerBounds
 * JD-Core Version:    0.7.0.1
 */
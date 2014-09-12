/*  1:   */ package org.apache.commons.math3.optimization;
/*  2:   */ 
/*  3:   */ public abstract class AbstractConvergenceChecker<PAIR>
/*  4:   */   implements ConvergenceChecker<PAIR>
/*  5:   */ {
/*  6:   */   private static final double DEFAULT_RELATIVE_THRESHOLD = 1.110223024625157E-014D;
/*  7:   */   private static final double DEFAULT_ABSOLUTE_THRESHOLD = 2.225073858507201E-306D;
/*  8:   */   private final double relativeThreshold;
/*  9:   */   private final double absoluteThreshold;
/* 10:   */   
/* 11:   */   public AbstractConvergenceChecker()
/* 12:   */   {
/* 13:53 */     this.relativeThreshold = 1.110223024625157E-014D;
/* 14:54 */     this.absoluteThreshold = 2.225073858507201E-306D;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public AbstractConvergenceChecker(double relativeThreshold, double absoluteThreshold)
/* 18:   */   {
/* 19:65 */     this.relativeThreshold = relativeThreshold;
/* 20:66 */     this.absoluteThreshold = absoluteThreshold;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public double getRelativeThreshold()
/* 24:   */   {
/* 25:73 */     return this.relativeThreshold;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public double getAbsoluteThreshold()
/* 29:   */   {
/* 30:80 */     return this.absoluteThreshold;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public abstract boolean converged(int paramInt, PAIR paramPAIR1, PAIR paramPAIR2);
/* 34:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.AbstractConvergenceChecker
 * JD-Core Version:    0.7.0.1
 */
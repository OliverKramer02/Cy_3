/*  1:   */ package org.apache.commons.math3.ode.sampling;
/*  2:   */ 
/*  3:   */ public class DummyStepHandler
/*  4:   */   implements StepHandler
/*  5:   */ {
/*  6:   */   public static DummyStepHandler getInstance()
/*  7:   */   {
/*  8:50 */     return LazyHolder.INSTANCE;
/*  9:   */   }
/* 10:   */   
/* 11:   */   public void init(double t0, double[] y0, double t) {}
/* 12:   */   
/* 13:   */   public void handleStep(StepInterpolator interpolator, boolean isLast) {}
/* 14:   */   
/* 15:   */   private static class LazyHolder
/* 16:   */   {
/* 17:78 */     private static final DummyStepHandler INSTANCE = new DummyStepHandler();
/* 18:   */   }
/* 19:   */   
/* 20:   */   private Object readResolve()
/* 21:   */   {
/* 22:87 */     return LazyHolder.INSTANCE;
/* 23:   */   }
/* 24:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.sampling.DummyStepHandler
 * JD-Core Version:    0.7.0.1
 */
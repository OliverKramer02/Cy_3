/*  1:   */ package org.apache.commons.math3.ode.events;
/*  2:   */ 
/*  3:   */ public abstract interface EventHandler
/*  4:   */ {
/*  5:   */   public abstract void init(double paramDouble1, double[] paramArrayOfDouble, double paramDouble2);
/*  6:   */   
/*  7:   */   public abstract double g(double paramDouble, double[] paramArrayOfDouble);
/*  8:   */   
/*  9:   */   public abstract Action eventOccurred(double paramDouble, double[] paramArrayOfDouble, boolean paramBoolean);
/* 10:   */   
/* 11:   */   public abstract void resetState(double paramDouble, double[] paramArrayOfDouble);
/* 12:   */   
/* 13:   */   public static enum Action
/* 14:   */   {
/* 15:60 */     STOP,  RESET_STATE,  RESET_DERIVATIVES,  CONTINUE;
/* 16:   */     
/* 17:   */     private Action() {}
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.events.EventHandler
 * JD-Core Version:    0.7.0.1
 */
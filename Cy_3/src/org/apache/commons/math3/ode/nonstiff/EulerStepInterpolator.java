/*  1:   */ package org.apache.commons.math3.ode.nonstiff;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*  4:   */ 
/*  5:   */ class EulerStepInterpolator
/*  6:   */   extends RungeKuttaStepInterpolator
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 20111120L;
/*  9:   */   
/* 10:   */   public EulerStepInterpolator() {}
/* 11:   */   
/* 12:   */   public EulerStepInterpolator(EulerStepInterpolator interpolator)
/* 13:   */   {
/* 14:72 */     super(interpolator);
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected StepInterpolator doCopy()
/* 18:   */   {
/* 19:78 */     return new EulerStepInterpolator(this);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/* 23:   */   {
/* 24:86 */     if ((this.previousState != null) && (theta <= 0.5D))
/* 25:   */     {
/* 26:87 */       for (int i = 0; i < this.interpolatedState.length; i++) {
/* 27:88 */         this.interpolatedState[i] = (this.previousState[i] + theta * this.h * this.yDotK[0][i]);
/* 28:   */       }
/* 29:90 */       System.arraycopy(this.yDotK[0], 0, this.interpolatedDerivatives, 0, this.interpolatedDerivatives.length);
/* 30:   */     }
/* 31:   */     else
/* 32:   */     {
/* 33:92 */       for (int i = 0; i < this.interpolatedState.length; i++) {
/* 34:93 */         this.interpolatedState[i] = (this.currentState[i] - oneMinusThetaH * this.yDotK[0][i]);
/* 35:   */       }
/* 36:95 */       System.arraycopy(this.yDotK[0], 0, this.interpolatedDerivatives, 0, this.interpolatedDerivatives.length);
/* 37:   */     }
/* 38:   */   }
/* 39:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.EulerStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   4:    */ 
/*   5:    */ class MidpointStepInterpolator
/*   6:    */   extends RungeKuttaStepInterpolator
/*   7:    */ {
/*   8:    */   private static final long serialVersionUID = 20111120L;
/*   9:    */   
/*  10:    */   public MidpointStepInterpolator() {}
/*  11:    */   
/*  12:    */   public MidpointStepInterpolator(MidpointStepInterpolator interpolator)
/*  13:    */   {
/*  14: 74 */     super(interpolator);
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected StepInterpolator doCopy()
/*  18:    */   {
/*  19: 80 */     return new MidpointStepInterpolator(this);
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/*  23:    */   {
/*  24: 89 */     double coeffDot2 = 2.0D * theta;
/*  25: 90 */     double coeffDot1 = 1.0D - coeffDot2;
/*  26: 92 */     if ((this.previousState != null) && (theta <= 0.5D))
/*  27:    */     {
/*  28: 93 */       double coeff1 = theta * oneMinusThetaH;
/*  29: 94 */       double coeff2 = theta * theta * this.h;
/*  30: 95 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  31:    */       {
/*  32: 96 */         double yDot1 = this.yDotK[0][i];
/*  33: 97 */         double yDot2 = this.yDotK[1][i];
/*  34: 98 */         this.interpolatedState[i] = (this.previousState[i] + coeff1 * yDot1 + coeff2 * yDot2);
/*  35: 99 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot2 * yDot2);
/*  36:    */       }
/*  37:    */     }
/*  38:    */     else
/*  39:    */     {
/*  40:102 */       double coeff1 = oneMinusThetaH * theta;
/*  41:103 */       double coeff2 = oneMinusThetaH * (1.0D + theta);
/*  42:104 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  43:    */       {
/*  44:105 */         double yDot1 = this.yDotK[0][i];
/*  45:106 */         double yDot2 = this.yDotK[1][i];
/*  46:107 */         this.interpolatedState[i] = (this.currentState[i] + coeff1 * yDot1 - coeff2 * yDot2);
/*  47:108 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot2 * yDot2);
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.MidpointStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
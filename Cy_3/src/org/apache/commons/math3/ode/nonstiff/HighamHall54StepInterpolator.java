/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   4:    */ 
/*   5:    */ class HighamHall54StepInterpolator
/*   6:    */   extends RungeKuttaStepInterpolator
/*   7:    */ {
/*   8:    */   private static final long serialVersionUID = 20111120L;
/*   9:    */   
/*  10:    */   public HighamHall54StepInterpolator() {}
/*  11:    */   
/*  12:    */   public HighamHall54StepInterpolator(HighamHall54StepInterpolator interpolator)
/*  13:    */   {
/*  14: 59 */     super(interpolator);
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected StepInterpolator doCopy()
/*  18:    */   {
/*  19: 65 */     return new HighamHall54StepInterpolator(this);
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/*  23:    */   {
/*  24: 74 */     double bDot0 = 1.0D + theta * (-7.5D + theta * (16.0D - 10.0D * theta));
/*  25: 75 */     double bDot2 = theta * (28.6875D + theta * (-91.125D + 67.5D * theta));
/*  26: 76 */     double bDot3 = theta * (-44.0D + theta * (152.0D - 120.0D * theta));
/*  27: 77 */     double bDot4 = theta * (23.4375D + theta * (-78.125D + 62.5D * theta));
/*  28: 78 */     double bDot5 = theta * 5.0D / 8.0D * (2.0D * theta - 1.0D);
/*  29: 80 */     if ((this.previousState != null) && (theta <= 0.5D))
/*  30:    */     {
/*  31: 81 */       double hTheta = this.h * theta;
/*  32: 82 */       double b0 = hTheta * (1.0D + theta * (-3.75D + theta * (5.333333333333333D - 2.5D * theta)));
/*  33: 83 */       double b2 = hTheta * (theta * (14.34375D + theta * (-30.375D + theta * 135.0D / 8.0D)));
/*  34: 84 */       double b3 = hTheta * (theta * (-22.0D + theta * (50.666666666666664D + theta * -30.0D)));
/*  35: 85 */       double b4 = hTheta * (theta * (11.71875D + theta * (-26.041666666666668D + theta * 125.0D / 8.0D)));
/*  36: 86 */       double b5 = hTheta * (theta * (-0.3125D + theta * 5.0D / 12.0D));
/*  37: 87 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  38:    */       {
/*  39: 88 */         double yDot0 = this.yDotK[0][i];
/*  40: 89 */         double yDot2 = this.yDotK[2][i];
/*  41: 90 */         double yDot3 = this.yDotK[3][i];
/*  42: 91 */         double yDot4 = this.yDotK[4][i];
/*  43: 92 */         double yDot5 = this.yDotK[5][i];
/*  44: 93 */         this.interpolatedState[i] = (this.previousState[i] + b0 * yDot0 + b2 * yDot2 + b3 * yDot3 + b4 * yDot4 + b5 * yDot5);
/*  45:    */         
/*  46: 95 */         this.interpolatedDerivatives[i] = (bDot0 * yDot0 + bDot2 * yDot2 + bDot3 * yDot3 + bDot4 * yDot4 + bDot5 * yDot5);
/*  47:    */       }
/*  48:    */     }
/*  49:    */     else
/*  50:    */     {
/*  51: 99 */       double theta2 = theta * theta;
/*  52:100 */       double b0 = this.h * (-0.08333333333333333D + theta * (1.0D + theta * (-3.75D + theta * (5.333333333333333D + theta * -5.0D / 2.0D))));
/*  53:101 */       double b2 = this.h * (-0.84375D + theta2 * (14.34375D + theta * (-30.375D + theta * 135.0D / 8.0D)));
/*  54:102 */       double b3 = this.h * (1.333333333333333D + theta2 * (-22.0D + theta * (50.666666666666664D + theta * -30.0D)));
/*  55:103 */       double b4 = this.h * (-1.302083333333333D + theta2 * (11.71875D + theta * (-26.041666666666668D + theta * 125.0D / 8.0D)));
/*  56:104 */       double b5 = this.h * (-0.1041666666666667D + theta2 * (-0.3125D + theta * 5.0D / 12.0D));
/*  57:105 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  58:    */       {
/*  59:106 */         double yDot0 = this.yDotK[0][i];
/*  60:107 */         double yDot2 = this.yDotK[2][i];
/*  61:108 */         double yDot3 = this.yDotK[3][i];
/*  62:109 */         double yDot4 = this.yDotK[4][i];
/*  63:110 */         double yDot5 = this.yDotK[5][i];
/*  64:111 */         this.interpolatedState[i] = (this.currentState[i] + b0 * yDot0 + b2 * yDot2 + b3 * yDot3 + b4 * yDot4 + b5 * yDot5);
/*  65:    */         
/*  66:113 */         this.interpolatedDerivatives[i] = (bDot0 * yDot0 + bDot2 * yDot2 + bDot3 * yDot3 + bDot4 * yDot4 + bDot5 * yDot5);
/*  67:    */       }
/*  68:    */     }
/*  69:    */   }
/*  70:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.HighamHall54StepInterpolator
 * JD-Core Version:    0.7.0.1
 */
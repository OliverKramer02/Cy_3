/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   4:    */ 
/*   5:    */ class ThreeEighthesStepInterpolator
/*   6:    */   extends RungeKuttaStepInterpolator
/*   7:    */ {
/*   8:    */   private static final long serialVersionUID = 20111120L;
/*   9:    */   
/*  10:    */   public ThreeEighthesStepInterpolator() {}
/*  11:    */   
/*  12:    */   public ThreeEighthesStepInterpolator(ThreeEighthesStepInterpolator interpolator)
/*  13:    */   {
/*  14: 84 */     super(interpolator);
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected StepInterpolator doCopy()
/*  18:    */   {
/*  19: 90 */     return new ThreeEighthesStepInterpolator(this);
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/*  23:    */   {
/*  24: 99 */     double coeffDot3 = 0.75D * theta;
/*  25:100 */     double coeffDot1 = coeffDot3 * (4.0D * theta - 5.0D) + 1.0D;
/*  26:101 */     double coeffDot2 = coeffDot3 * (5.0D - 6.0D * theta);
/*  27:102 */     double coeffDot4 = coeffDot3 * (2.0D * theta - 1.0D);
/*  28:104 */     if ((this.previousState != null) && (theta <= 0.5D))
/*  29:    */     {
/*  30:105 */       double s = theta * this.h / 8.0D;
/*  31:106 */       double fourTheta2 = 4.0D * theta * theta;
/*  32:107 */       double coeff1 = s * (8.0D - 15.0D * theta + 2.0D * fourTheta2);
/*  33:108 */       double coeff2 = 3.0D * s * (5.0D * theta - fourTheta2);
/*  34:109 */       double coeff3 = 3.0D * s * theta;
/*  35:110 */       double coeff4 = s * (-3.0D * theta + fourTheta2);
/*  36:111 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  37:    */       {
/*  38:112 */         double yDot1 = this.yDotK[0][i];
/*  39:113 */         double yDot2 = this.yDotK[1][i];
/*  40:114 */         double yDot3 = this.yDotK[2][i];
/*  41:115 */         double yDot4 = this.yDotK[3][i];
/*  42:116 */         this.interpolatedState[i] = (this.previousState[i] + coeff1 * yDot1 + coeff2 * yDot2 + coeff3 * yDot3 + coeff4 * yDot4);
/*  43:    */         
/*  44:118 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot2 * yDot2 + coeffDot3 * yDot3 + coeffDot4 * yDot4);
/*  45:    */       }
/*  46:    */     }
/*  47:    */     else
/*  48:    */     {
/*  49:123 */       double s = oneMinusThetaH / 8.0D;
/*  50:124 */       double fourTheta2 = 4.0D * theta * theta;
/*  51:125 */       double coeff1 = s * (1.0D - 7.0D * theta + 2.0D * fourTheta2);
/*  52:126 */       double coeff2 = 3.0D * s * (1.0D + theta - fourTheta2);
/*  53:127 */       double coeff3 = 3.0D * s * (1.0D + theta);
/*  54:128 */       double coeff4 = s * (1.0D + theta + fourTheta2);
/*  55:129 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  56:    */       {
/*  57:130 */         double yDot1 = this.yDotK[0][i];
/*  58:131 */         double yDot2 = this.yDotK[1][i];
/*  59:132 */         double yDot3 = this.yDotK[2][i];
/*  60:133 */         double yDot4 = this.yDotK[3][i];
/*  61:134 */         this.interpolatedState[i] = (this.currentState[i] - coeff1 * yDot1 - coeff2 * yDot2 - coeff3 * yDot3 - coeff4 * yDot4);
/*  62:    */         
/*  63:136 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot2 * yDot2 + coeffDot3 * yDot3 + coeffDot4 * yDot4);
/*  64:    */       }
/*  65:    */     }
/*  66:    */   }
/*  67:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.ThreeEighthesStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
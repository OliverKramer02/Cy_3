/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   4:    */ 
/*   5:    */ class ClassicalRungeKuttaStepInterpolator
/*   6:    */   extends RungeKuttaStepInterpolator
/*   7:    */ {
/*   8:    */   private static final long serialVersionUID = 20111120L;
/*   9:    */   
/*  10:    */   public ClassicalRungeKuttaStepInterpolator() {}
/*  11:    */   
/*  12:    */   public ClassicalRungeKuttaStepInterpolator(ClassicalRungeKuttaStepInterpolator interpolator)
/*  13:    */   {
/*  14: 81 */     super(interpolator);
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected StepInterpolator doCopy()
/*  18:    */   {
/*  19: 87 */     return new ClassicalRungeKuttaStepInterpolator(this);
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/*  23:    */   {
/*  24: 95 */     double oneMinusTheta = 1.0D - theta;
/*  25: 96 */     double oneMinus2Theta = 1.0D - 2.0D * theta;
/*  26: 97 */     double coeffDot1 = oneMinusTheta * oneMinus2Theta;
/*  27: 98 */     double coeffDot23 = 2.0D * theta * oneMinusTheta;
/*  28: 99 */     double coeffDot4 = -theta * oneMinus2Theta;
/*  29:100 */     if ((this.previousState != null) && (theta <= 0.5D))
/*  30:    */     {
/*  31:101 */       double fourTheta2 = 4.0D * theta * theta;
/*  32:102 */       double s = theta * this.h / 6.0D;
/*  33:103 */       double coeff1 = s * (6.0D - 9.0D * theta + fourTheta2);
/*  34:104 */       double coeff23 = s * (6.0D * theta - fourTheta2);
/*  35:105 */       double coeff4 = s * (-3.0D * theta + fourTheta2);
/*  36:106 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  37:    */       {
/*  38:107 */         double yDot1 = this.yDotK[0][i];
/*  39:108 */         double yDot23 = this.yDotK[1][i] + this.yDotK[2][i];
/*  40:109 */         double yDot4 = this.yDotK[3][i];
/*  41:110 */         this.interpolatedState[i] = (this.previousState[i] + coeff1 * yDot1 + coeff23 * yDot23 + coeff4 * yDot4);
/*  42:    */         
/*  43:112 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot23 * yDot23 + coeffDot4 * yDot4);
/*  44:    */       }
/*  45:    */     }
/*  46:    */     else
/*  47:    */     {
/*  48:116 */       double fourTheta = 4.0D * theta;
/*  49:117 */       double s = oneMinusThetaH / 6.0D;
/*  50:118 */       double coeff1 = s * ((-fourTheta + 5.0D) * theta - 1.0D);
/*  51:119 */       double coeff23 = s * ((fourTheta - 2.0D) * theta - 2.0D);
/*  52:120 */       double coeff4 = s * ((-fourTheta - 1.0D) * theta - 1.0D);
/*  53:121 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  54:    */       {
/*  55:122 */         double yDot1 = this.yDotK[0][i];
/*  56:123 */         double yDot23 = this.yDotK[1][i] + this.yDotK[2][i];
/*  57:124 */         double yDot4 = this.yDotK[3][i];
/*  58:125 */         this.interpolatedState[i] = (this.currentState[i] + coeff1 * yDot1 + coeff23 * yDot23 + coeff4 * yDot4);
/*  59:    */         
/*  60:127 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot23 * yDot23 + coeffDot4 * yDot4);
/*  61:    */       }
/*  62:    */     }
/*  63:    */   }
/*  64:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
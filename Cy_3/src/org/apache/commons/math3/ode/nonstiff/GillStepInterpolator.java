/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ class GillStepInterpolator
/*   7:    */   extends RungeKuttaStepInterpolator
/*   8:    */ {
/*   9: 60 */   private static final double ONE_MINUS_INV_SQRT_2 = 1.0D - FastMath.sqrt(0.5D);
/*  10: 63 */   private static final double ONE_PLUS_INV_SQRT_2 = 1.0D + FastMath.sqrt(0.5D);
/*  11:    */   private static final long serialVersionUID = 20111120L;
/*  12:    */   
/*  13:    */   public GillStepInterpolator() {}
/*  14:    */   
/*  15:    */   public GillStepInterpolator(GillStepInterpolator interpolator)
/*  16:    */   {
/*  17: 88 */     super(interpolator);
/*  18:    */   }
/*  19:    */   
/*  20:    */   protected StepInterpolator doCopy()
/*  21:    */   {
/*  22: 94 */     return new GillStepInterpolator(this);
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/*  26:    */   {
/*  27:103 */     double twoTheta = 2.0D * theta;
/*  28:104 */     double fourTheta2 = twoTheta * twoTheta;
/*  29:105 */     double coeffDot1 = theta * (twoTheta - 3.0D) + 1.0D;
/*  30:106 */     double cDot23 = twoTheta * (1.0D - theta);
/*  31:107 */     double coeffDot2 = cDot23 * ONE_MINUS_INV_SQRT_2;
/*  32:108 */     double coeffDot3 = cDot23 * ONE_PLUS_INV_SQRT_2;
/*  33:109 */     double coeffDot4 = theta * (twoTheta - 1.0D);
/*  34:111 */     if ((this.previousState != null) && (theta <= 0.5D))
/*  35:    */     {
/*  36:112 */       double s = theta * this.h / 6.0D;
/*  37:113 */       double c23 = s * (6.0D * theta - fourTheta2);
/*  38:114 */       double coeff1 = s * (6.0D - 9.0D * theta + fourTheta2);
/*  39:115 */       double coeff2 = c23 * ONE_MINUS_INV_SQRT_2;
/*  40:116 */       double coeff3 = c23 * ONE_PLUS_INV_SQRT_2;
/*  41:117 */       double coeff4 = s * (-3.0D * theta + fourTheta2);
/*  42:118 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  43:    */       {
/*  44:119 */         double yDot1 = this.yDotK[0][i];
/*  45:120 */         double yDot2 = this.yDotK[1][i];
/*  46:121 */         double yDot3 = this.yDotK[2][i];
/*  47:122 */         double yDot4 = this.yDotK[3][i];
/*  48:123 */         this.interpolatedState[i] = (this.previousState[i] + coeff1 * yDot1 + coeff2 * yDot2 + coeff3 * yDot3 + coeff4 * yDot4);
/*  49:    */         
/*  50:125 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot2 * yDot2 + coeffDot3 * yDot3 + coeffDot4 * yDot4);
/*  51:    */       }
/*  52:    */     }
/*  53:    */     else
/*  54:    */     {
/*  55:129 */       double s = oneMinusThetaH / 6.0D;
/*  56:130 */       double c23 = s * (2.0D + twoTheta - fourTheta2);
/*  57:131 */       double coeff1 = s * (1.0D - 5.0D * theta + fourTheta2);
/*  58:132 */       double coeff2 = c23 * ONE_MINUS_INV_SQRT_2;
/*  59:133 */       double coeff3 = c23 * ONE_PLUS_INV_SQRT_2;
/*  60:134 */       double coeff4 = s * (1.0D + theta + fourTheta2);
/*  61:135 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  62:    */       {
/*  63:136 */         double yDot1 = this.yDotK[0][i];
/*  64:137 */         double yDot2 = this.yDotK[1][i];
/*  65:138 */         double yDot3 = this.yDotK[2][i];
/*  66:139 */         double yDot4 = this.yDotK[3][i];
/*  67:140 */         this.interpolatedState[i] = (this.currentState[i] - coeff1 * yDot1 - coeff2 * yDot2 - coeff3 * yDot3 - coeff4 * yDot4);
/*  68:    */         
/*  69:142 */         this.interpolatedDerivatives[i] = (coeffDot1 * yDot1 + coeffDot2 * yDot2 + coeffDot3 * yDot3 + coeffDot4 * yDot4);
/*  70:    */       }
/*  71:    */     }
/*  72:    */   }
/*  73:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.GillStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
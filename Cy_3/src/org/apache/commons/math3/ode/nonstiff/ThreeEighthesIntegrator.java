/*  1:   */ package org.apache.commons.math3.ode.nonstiff;
/*  2:   */ 
/*  3:   */ public class ThreeEighthesIntegrator
/*  4:   */   extends RungeKuttaIntegrator
/*  5:   */ {
/*  6:48 */   private static final double[] STATIC_C = { 0.3333333333333333D, 0.6666666666666666D, 1.0D };
/*  7:53 */   private static final double[][] STATIC_A = { { 0.3333333333333333D }, { -0.3333333333333333D, 1.0D }, { 1.0D, -1.0D, 1.0D } };
/*  8:60 */   private static final double[] STATIC_B = { 0.125D, 0.375D, 0.375D, 0.125D };
/*  9:   */   
/* 10:   */   public ThreeEighthesIntegrator(double step)
/* 11:   */   {
/* 12:69 */     super("3/8", STATIC_C, STATIC_A, STATIC_B, new ThreeEighthesStepInterpolator(), step);
/* 13:   */   }
/* 14:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator
 * JD-Core Version:    0.7.0.1
 */
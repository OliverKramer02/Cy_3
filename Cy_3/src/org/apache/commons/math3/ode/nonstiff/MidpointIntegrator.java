/*  1:   */ package org.apache.commons.math3.ode.nonstiff;
/*  2:   */ 
/*  3:   */ public class MidpointIntegrator
/*  4:   */   extends RungeKuttaIntegrator
/*  5:   */ {
/*  6:46 */   private static final double[] STATIC_C = { 0.5D };
/*  7:51 */   private static final double[][] STATIC_A = { { 0.5D } };
/*  8:56 */   private static final double[] STATIC_B = { 0.0D, 1.0D };
/*  9:   */   
/* 10:   */   public MidpointIntegrator(double step)
/* 11:   */   {
/* 12:65 */     super("midpoint", STATIC_C, STATIC_A, STATIC_B, new MidpointStepInterpolator(), step);
/* 13:   */   }
/* 14:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.MidpointIntegrator
 * JD-Core Version:    0.7.0.1
 */
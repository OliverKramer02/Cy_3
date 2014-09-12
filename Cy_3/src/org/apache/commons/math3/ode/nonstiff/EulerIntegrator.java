/*  1:   */ package org.apache.commons.math3.ode.nonstiff;
/*  2:   */ 
/*  3:   */ public class EulerIntegrator
/*  4:   */   extends RungeKuttaIntegrator
/*  5:   */ {
/*  6:52 */   private static final double[] STATIC_C = new double[0];
/*  7:56 */   private static final double[][] STATIC_A = new double[0][];
/*  8:60 */   private static final double[] STATIC_B = { 1.0D };
/*  9:   */   
/* 10:   */   public EulerIntegrator(double step)
/* 11:   */   {
/* 12:69 */     super("Euler", STATIC_C, STATIC_A, STATIC_B, new EulerStepInterpolator(), step);
/* 13:   */   }
/* 14:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.EulerIntegrator
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.ode.nonstiff;
/*  2:   */ 
/*  3:   */ public class ClassicalRungeKuttaIntegrator
/*  4:   */   extends RungeKuttaIntegrator
/*  5:   */ {
/*  6:49 */   private static final double[] STATIC_C = { 0.5D, 0.5D, 1.0D };
/*  7:54 */   private static final double[][] STATIC_A = { { 0.5D }, { 0.0D, 0.5D }, { 0.0D, 0.0D, 1.0D } };
/*  8:61 */   private static final double[] STATIC_B = { 0.1666666666666667D, 0.3333333333333333D, 0.3333333333333333D, 0.1666666666666667D };
/*  9:   */   
/* 10:   */   public ClassicalRungeKuttaIntegrator(double step)
/* 11:   */   {
/* 12:71 */     super("classical Runge-Kutta", STATIC_C, STATIC_A, STATIC_B, new ClassicalRungeKuttaStepInterpolator(), step);
/* 13:   */   }
/* 14:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator
 * JD-Core Version:    0.7.0.1
 */
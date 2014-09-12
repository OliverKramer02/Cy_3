/*  1:   */ package org.apache.commons.math3.ode.nonstiff;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.FastMath;
/*  4:   */ 
/*  5:   */ public class GillIntegrator
/*  6:   */   extends RungeKuttaIntegrator
/*  7:   */ {
/*  8:50 */   private static final double[] STATIC_C = { 0.5D, 0.5D, 1.0D };
/*  9:55 */   private static final double[][] STATIC_A = { { 0.5D }, { (FastMath.sqrt(2.0D) - 1.0D) / 2.0D, (2.0D - FastMath.sqrt(2.0D)) / 2.0D }, { 0.0D, -FastMath.sqrt(2.0D) / 2.0D, (2.0D + FastMath.sqrt(2.0D)) / 2.0D } };
/* 10:62 */   private static final double[] STATIC_B = { 0.1666666666666667D, (2.0D - FastMath.sqrt(2.0D)) / 6.0D, (2.0D + FastMath.sqrt(2.0D)) / 6.0D, 0.1666666666666667D };
/* 11:   */   
/* 12:   */   public GillIntegrator(double step)
/* 13:   */   {
/* 14:71 */     super("Gill", STATIC_C, STATIC_A, STATIC_B, new GillStepInterpolator(), step);
/* 15:   */   }
/* 16:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.GillIntegrator
 * JD-Core Version:    0.7.0.1
 */
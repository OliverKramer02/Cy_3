/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class HighamHall54Integrator
/*   6:    */   extends EmbeddedRungeKuttaIntegrator
/*   7:    */ {
/*   8:    */   private static final String METHOD_NAME = "Higham-Hall 5(4)";
/*   9: 43 */   private static final double[] STATIC_C = { 0.2222222222222222D, 0.3333333333333333D, 0.5D, 0.6D, 1.0D, 1.0D };
/*  10: 48 */   private static final double[][] STATIC_A = { { 0.2222222222222222D }, { 0.08333333333333333D, 0.25D }, { 0.125D, 0.0D, 0.375D }, { 0.182D, -0.27D, 0.624D, 0.064D }, { -0.55D, 1.35D, 2.4D, -7.2D, 5.0D }, { 0.08333333333333333D, 0.0D, 0.84375D, -1.333333333333333D, 1.302083333333333D, 0.1041666666666667D } };
/*  11: 58 */   private static final double[] STATIC_B = { 0.08333333333333333D, 0.0D, 0.84375D, -1.333333333333333D, 1.302083333333333D, 0.1041666666666667D, 0.0D };
/*  12: 63 */   private static final double[] STATIC_E = { -0.05D, 0.0D, 0.50625D, -1.2D, 0.78125D, 0.0625D, -0.1D };
/*  13:    */   
/*  14:    */   public HighamHall54Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  15:    */   {
/*  16: 81 */     super("Higham-Hall 5(4)", false, STATIC_C, STATIC_A, STATIC_B, new HighamHall54StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public HighamHall54Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  20:    */   {
/*  21: 99 */     super("Higham-Hall 5(4)", false, STATIC_C, STATIC_A, STATIC_B, new HighamHall54StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public int getOrder()
/*  25:    */   {
/*  26:106 */     return 5;
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h)
/*  30:    */   {
/*  31:115 */     double error = 0.0D;
/*  32:117 */     for (int j = 0; j < this.mainSetDimension; j++)
/*  33:    */     {
/*  34:118 */       double errSum = STATIC_E[0] * yDotK[0][j];
/*  35:119 */       for (int l = 1; l < STATIC_E.length; l++) {
/*  36:120 */         errSum += STATIC_E[l] * yDotK[l][j];
/*  37:    */       }
/*  38:123 */       double yScale = FastMath.max(FastMath.abs(y0[j]), FastMath.abs(y1[j]));
/*  39:124 */       double tol = this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + this.scalRelativeTolerance * yScale : this.vecAbsoluteTolerance[j] + this.vecRelativeTolerance[j] * yScale;
/*  40:    */       
/*  41:    */ 
/*  42:127 */       double ratio = h * errSum / tol;
/*  43:128 */       error += ratio * ratio;
/*  44:    */     }
/*  45:132 */     return FastMath.sqrt(error / this.mainSetDimension);
/*  46:    */   }
/*  47:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator
 * JD-Core Version:    0.7.0.1
 */
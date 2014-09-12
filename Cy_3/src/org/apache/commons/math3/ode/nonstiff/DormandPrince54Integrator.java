/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class DormandPrince54Integrator
/*   6:    */   extends EmbeddedRungeKuttaIntegrator
/*   7:    */ {
/*   8:    */   private static final String METHOD_NAME = "Dormand-Prince 5(4)";
/*   9: 55 */   private static final double[] STATIC_C = { 0.2D, 0.3D, 0.8D, 0.8888888888888888D, 1.0D, 1.0D };
/*  10: 60 */   private static final double[][] STATIC_A = { { 0.2D }, { 0.075D, 0.225D }, { 0.9777777777777778D, -3.733333333333333D, 3.555555555555555D }, { 2.952598689224204D, -11.595793324188385D, 9.822892851699436D, -0.2908093278463649D }, { 2.846275252525253D, -10.757575757575758D, 8.906422717743473D, 0.2784090909090909D, -0.273531303602058D }, { 0.09114583333333333D, 0.0D, 0.4492362982929021D, 0.6510416666666666D, -0.322376179245283D, 0.130952380952381D } };
/*  11: 70 */   private static final double[] STATIC_B = { 0.09114583333333333D, 0.0D, 0.4492362982929021D, 0.6510416666666666D, -0.322376179245283D, 0.130952380952381D, 0.0D };
/*  12:    */   private static final double E1 = 0.001232638888888889D;
/*  13:    */   private static final double E3 = -0.004252770290506139D;
/*  14:    */   private static final double E4 = 0.03697916666666667D;
/*  15:    */   private static final double E5 = -0.0508637971698113D;
/*  16:    */   private static final double E6 = 0.0419047619047619D;
/*  17:    */   private static final double E7 = -0.025D;
/*  18:    */   
/*  19:    */   public DormandPrince54Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  20:    */   {
/*  21:108 */     super("Dormand-Prince 5(4)", true, STATIC_C, STATIC_A, STATIC_B, new DormandPrince54StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public DormandPrince54Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  25:    */   {
/*  26:126 */     super("Dormand-Prince 5(4)", true, STATIC_C, STATIC_A, STATIC_B, new DormandPrince54StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public int getOrder()
/*  30:    */   {
/*  31:133 */     return 5;
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h)
/*  35:    */   {
/*  36:142 */     double error = 0.0D;
/*  37:144 */     for (int j = 0; j < this.mainSetDimension; j++)
/*  38:    */     {
/*  39:145 */       double errSum = 0.001232638888888889D * yDotK[0][j] + -0.004252770290506139D * yDotK[2][j] + 0.03697916666666667D * yDotK[3][j] + -0.0508637971698113D * yDotK[4][j] + 0.0419047619047619D * yDotK[5][j] + -0.025D * yDotK[6][j];
/*  40:    */       
/*  41:    */ 
/*  42:    */ 
/*  43:149 */       double yScale = FastMath.max(FastMath.abs(y0[j]), FastMath.abs(y1[j]));
/*  44:150 */       double tol = this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + this.scalRelativeTolerance * yScale : this.vecAbsoluteTolerance[j] + this.vecRelativeTolerance[j] * yScale;
/*  45:    */       
/*  46:    */ 
/*  47:153 */       double ratio = h * errSum / tol;
/*  48:154 */       error += ratio * ratio;
/*  49:    */     }
/*  50:158 */     return FastMath.sqrt(error / this.mainSetDimension);
/*  51:    */   }
/*  52:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator
 * JD-Core Version:    0.7.0.1
 */
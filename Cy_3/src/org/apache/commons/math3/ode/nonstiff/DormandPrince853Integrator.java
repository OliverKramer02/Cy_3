/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class DormandPrince853Integrator
/*   6:    */   extends EmbeddedRungeKuttaIntegrator
/*   7:    */ {
/*   8:    */   private static final String METHOD_NAME = "Dormand-Prince 8 (5, 3)";
/*   9: 63 */   private static final double[] STATIC_C = { (12.0D - 2.0D * FastMath.sqrt(6.0D)) / 135.0D, (6.0D - FastMath.sqrt(6.0D)) / 45.0D, (6.0D - FastMath.sqrt(6.0D)) / 30.0D, (6.0D + FastMath.sqrt(6.0D)) / 30.0D, 0.3333333333333333D, 0.25D, 0.3076923076923077D, 0.651282051282051D, 0.6D, 0.857142857142857D, 1.0D, 1.0D };
/*  10: 70 */   private static final double[][] STATIC_A = { { (12.0D - 2.0D * FastMath.sqrt(6.0D)) / 135.0D }, { (6.0D - FastMath.sqrt(6.0D)) / 180.0D, (6.0D - FastMath.sqrt(6.0D)) / 60.0D }, { (6.0D - FastMath.sqrt(6.0D)) / 120.0D, 0.0D, (6.0D - FastMath.sqrt(6.0D)) / 40.0D }, { (462.0D + 107.0D * FastMath.sqrt(6.0D)) / 3000.0D, 0.0D, (-402.0D - 197.0D * FastMath.sqrt(6.0D)) / 1000.0D, (168.0D + 73.0D * FastMath.sqrt(6.0D)) / 375.0D }, { 0.03703703703703704D, 0.0D, 0.0D, (16.0D + FastMath.sqrt(6.0D)) / 108.0D, (16.0D - FastMath.sqrt(6.0D)) / 108.0D }, { 0.037109375D, 0.0D, 0.0D, (118.0D + 23.0D * FastMath.sqrt(6.0D)) / 1024.0D, (118.0D - 23.0D * FastMath.sqrt(6.0D)) / 1024.0D, -0.017578125D }, { 0.03709200011850479D, 0.0D, 0.0D, (51544.0D + 4784.0D * FastMath.sqrt(6.0D)) / 371293.0D, (51544.0D - 4784.0D * FastMath.sqrt(6.0D)) / 371293.0D, -0.0153194377486244D, 0.008273789163814023D }, { 0.6241109587160757D, 0.0D, 0.0D, (-1324889724104.0D - 318801444819.0D * FastMath.sqrt(6.0D)) / 626556937500.0D, (-1324889724104.0D + 318801444819.0D * FastMath.sqrt(6.0D)) / 626556937500.0D, 27.59209969944671D, 20.154067550477894D, -43.489884181069961D }, { 0.4776625364382643D, 0.0D, 0.0D, (-4521408.0D - 1137963.0D * FastMath.sqrt(6.0D)) / 2937500.0D, (-4521408.0D + 1137963.0D * FastMath.sqrt(6.0D)) / 2937500.0D, 21.230051448181193D, 15.279233632882423D, -33.288210968984863D, -0.02033120170850863D }, { -0.937142430085987D, 0.0D, 0.0D, (354216.0D + 94326.0D * FastMath.sqrt(6.0D)) / 112847.0D, (354216.0D - 94326.0D * FastMath.sqrt(6.0D)) / 112847.0D, -8.149787010746927D, -18.520065659996959D, 22.739487099350505D, 2.493605552679652D, -3.04676447189822D }, { 2.273310147516538D, 0.0D, 0.0D, (-3457480.0D - 960905.0D * FastMath.sqrt(6.0D)) / 551636.0D, (-3457480.0D + 960905.0D * FastMath.sqrt(6.0D)) / 551636.0D, -17.958931863118799D, 27.94888452941996D, -2.858998277135024D, -8.872856933530629D, 12.360567175794303D, 0.6433927460157636D }, { 0.05429373411656877D, 0.0D, 0.0D, 0.0D, 0.0D, 4.450312892752409D, 1.8915178993145D, -5.801203960010585D, 0.31116436695782D, -0.1521609496625161D, 0.2013654008040303D, 0.04471061572777259D } };
/*  11:137 */   private static final double[] STATIC_B = { 0.05429373411656877D, 0.0D, 0.0D, 0.0D, 0.0D, 4.450312892752409D, 1.8915178993145D, -5.801203960010585D, 0.31116436695782D, -0.1521609496625161D, 0.2013654008040303D, 0.04471061572777259D, 0.0D };
/*  12:    */   private static final double E1_01 = 0.0131200449941949D;
/*  13:    */   private static final double E1_06 = -1.225156446376204D;
/*  14:    */   private static final double E1_07 = -0.49575894965725D;
/*  15:    */   private static final double E1_08 = 1.664377182454986D;
/*  16:    */   private static final double E1_09 = -0.3503288487499737D;
/*  17:    */   private static final double E1_10 = 0.3341791187130175D;
/*  18:    */   private static final double E1_11 = 0.0819232064851157D;
/*  19:    */   private static final double E1_12 = -0.02235530786388629D;
/*  20:    */   private static final double E2_01 = -0.1898007540724076D;
/*  21:    */   private static final double E2_06 = 4.450312892752409D;
/*  22:    */   private static final double E2_07 = 1.8915178993145D;
/*  23:    */   private static final double E2_08 = -5.801203960010585D;
/*  24:    */   private static final double E2_09 = -0.422682321323792D;
/*  25:    */   private static final double E2_10 = -0.1521609496625161D;
/*  26:    */   private static final double E2_11 = 0.2013654008040303D;
/*  27:    */   private static final double E2_12 = 0.02265179219836083D;
/*  28:    */   
/*  29:    */   public DormandPrince853Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  30:    */   {
/*  31:220 */     super("Dormand-Prince 8 (5, 3)", true, STATIC_C, STATIC_A, STATIC_B, new DormandPrince853StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public DormandPrince853Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  35:    */   {
/*  36:239 */     super("Dormand-Prince 8 (5, 3)", true, STATIC_C, STATIC_A, STATIC_B, new DormandPrince853StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public int getOrder()
/*  40:    */   {
/*  41:247 */     return 8;
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h)
/*  45:    */   {
/*  46:255 */     double error1 = 0.0D;
/*  47:256 */     double error2 = 0.0D;
/*  48:258 */     for (int j = 0; j < this.mainSetDimension; j++)
/*  49:    */     {
/*  50:259 */       double errSum1 = 0.0131200449941949D * yDotK[0][j] + -1.225156446376204D * yDotK[5][j] + -0.49575894965725D * yDotK[6][j] + 1.664377182454986D * yDotK[7][j] + -0.3503288487499737D * yDotK[8][j] + 0.3341791187130175D * yDotK[9][j] + 0.0819232064851157D * yDotK[10][j] + -0.02235530786388629D * yDotK[11][j];
/*  51:    */       
/*  52:    */ 
/*  53:    */ 
/*  54:263 */       double errSum2 = -0.1898007540724076D * yDotK[0][j] + 4.450312892752409D * yDotK[5][j] + 1.8915178993145D * yDotK[6][j] + -5.801203960010585D * yDotK[7][j] + -0.422682321323792D * yDotK[8][j] + -0.1521609496625161D * yDotK[9][j] + 0.2013654008040303D * yDotK[10][j] + 0.02265179219836083D * yDotK[11][j];
/*  55:    */       
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59:268 */       double yScale = FastMath.max(FastMath.abs(y0[j]), FastMath.abs(y1[j]));
/*  60:269 */       double tol = this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + this.scalRelativeTolerance * yScale : this.vecAbsoluteTolerance[j] + this.vecRelativeTolerance[j] * yScale;
/*  61:    */       
/*  62:    */ 
/*  63:272 */       double ratio1 = errSum1 / tol;
/*  64:273 */       error1 += ratio1 * ratio1;
/*  65:274 */       double ratio2 = errSum2 / tol;
/*  66:275 */       error2 += ratio2 * ratio2;
/*  67:    */     }
/*  68:278 */     double den = error1 + 0.01D * error2;
/*  69:279 */     if (den <= 0.0D) {
/*  70:280 */       den = 1.0D;
/*  71:    */     }
/*  72:283 */     return FastMath.abs(h) * error1 / FastMath.sqrt(this.mainSetDimension * den);
/*  73:    */   }
/*  74:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator
 * JD-Core Version:    0.7.0.1
 */
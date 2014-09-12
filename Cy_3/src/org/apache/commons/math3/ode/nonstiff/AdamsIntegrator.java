/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   6:    */ import org.apache.commons.math3.ode.ExpandableStatefulODE;
/*   7:    */ import org.apache.commons.math3.ode.MultistepIntegrator;
/*   8:    */ 
/*   9:    */ public abstract class AdamsIntegrator
/*  10:    */   extends MultistepIntegrator
/*  11:    */ {
/*  12:    */   private final AdamsNordsieckTransformer transformer;
/*  13:    */   
/*  14:    */   public AdamsIntegrator(String name, int nSteps, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  15:    */     throws IllegalArgumentException
/*  16:    */   {
/*  17: 57 */     super(name, nSteps, order, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  18:    */     
/*  19: 59 */     this.transformer = AdamsNordsieckTransformer.getInstance(nSteps);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public AdamsIntegrator(String name, int nSteps, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  23:    */     throws IllegalArgumentException
/*  24:    */   {
/*  25: 82 */     super(name, nSteps, order, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  26:    */     
/*  27: 84 */     this.transformer = AdamsNordsieckTransformer.getInstance(nSteps);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public abstract void integrate(ExpandableStatefulODE paramExpandableStatefulODE, double paramDouble)
/*  31:    */     throws MathIllegalStateException, MathIllegalArgumentException;
/*  32:    */   
/*  33:    */   protected Array2DRowRealMatrix initializeHighOrderDerivatives(double h, double[] t, double[][] y, double[][] yDot)
/*  34:    */   {
/*  35: 97 */     return this.transformer.initializeHighOrderDerivatives(h, t, y, yDot);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Array2DRowRealMatrix updateHighOrderDerivativesPhase1(Array2DRowRealMatrix highOrder)
/*  39:    */   {
/*  40:112 */     return this.transformer.updateHighOrderDerivativesPhase1(highOrder);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void updateHighOrderDerivativesPhase2(double[] start, double[] end, Array2DRowRealMatrix highOrder)
/*  44:    */   {
/*  45:131 */     this.transformer.updateHighOrderDerivativesPhase2(start, end, highOrder);
/*  46:    */   }
/*  47:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.AdamsIntegrator
 * JD-Core Version:    0.7.0.1
 */
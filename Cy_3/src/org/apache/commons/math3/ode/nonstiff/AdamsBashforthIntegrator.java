/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   6:    */ import org.apache.commons.math3.ode.ExpandableStatefulODE;
/*   7:    */ import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class AdamsBashforthIntegrator
/*  11:    */   extends AdamsIntegrator
/*  12:    */ {
/*  13:    */   private static final String METHOD_NAME = "Adams-Bashforth";
/*  14:    */   
/*  15:    */   public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  16:    */     throws IllegalArgumentException
/*  17:    */   {
/*  18:163 */     super("Adams-Bashforth", nSteps, nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  22:    */     throws IllegalArgumentException
/*  23:    */   {
/*  24:185 */     super("Adams-Bashforth", nSteps, nSteps, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void integrate(ExpandableStatefulODE equations, double t)
/*  28:    */     throws MathIllegalStateException, MathIllegalArgumentException
/*  29:    */   {
/*  30:194 */     sanityChecks(equations, t);
/*  31:195 */     setEquations(equations);
/*  32:196 */     boolean forward = t > equations.getTime();
/*  33:    */     
/*  34:    */ 
/*  35:199 */     double[] y0 = equations.getCompleteState();
/*  36:200 */     double[] y = (double[])y0.clone();
/*  37:201 */     double[] yDot = new double[y.length];
/*  38:    */     
/*  39:    */ 
/*  40:204 */     NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
/*  41:205 */     interpolator.reinitialize(y, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
/*  42:    */     
/*  43:    */ 
/*  44:    */ 
/*  45:209 */     initIntegration(equations.getTime(), y0, t);
/*  46:    */     
/*  47:    */ 
/*  48:212 */     start(equations.getTime(), y, t);
/*  49:213 */     interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
/*  50:214 */     interpolator.storeTime(this.stepStart);
/*  51:215 */     int lastRow = this.nordsieck.getRowDimension() - 1;
/*  52:    */     
/*  53:    */ 
/*  54:218 */     double hNew = this.stepSize;
/*  55:219 */     interpolator.rescale(hNew);
/*  56:    */     
/*  57:    */ 
/*  58:222 */     this.isLastStep = false;
/*  59:    */     do
/*  60:    */     {
/*  61:225 */       double error = 10.0D;
/*  62:226 */       while (error >= 1.0D)
/*  63:    */       {
/*  64:228 */         this.stepSize = hNew;
/*  65:    */         
/*  66:    */ 
/*  67:231 */         error = 0.0D;
/*  68:232 */         for (int i = 0; i < this.mainSetDimension; i++)
/*  69:    */         {
/*  70:233 */           double yScale = FastMath.abs(y[i]);
/*  71:234 */           double tol = this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + this.scalRelativeTolerance * yScale : this.vecAbsoluteTolerance[i] + this.vecRelativeTolerance[i] * yScale;
/*  72:    */           
/*  73:    */ 
/*  74:237 */           double ratio = this.nordsieck.getEntry(lastRow, i) / tol;
/*  75:238 */           error += ratio * ratio;
/*  76:    */         }
/*  77:240 */         error = FastMath.sqrt(error / this.mainSetDimension);
/*  78:242 */         if (error >= 1.0D)
/*  79:    */         {
/*  80:244 */           double factor = computeStepGrowShrinkFactor(error);
/*  81:245 */           hNew = filterStep(this.stepSize * factor, forward, false);
/*  82:246 */           interpolator.rescale(hNew);
/*  83:    */         }
/*  84:    */       }
/*  85:252 */       double stepEnd = this.stepStart + this.stepSize;
/*  86:253 */       interpolator.shift();
/*  87:254 */       interpolator.setInterpolatedTime(stepEnd);
/*  88:255 */       System.arraycopy(interpolator.getInterpolatedState(), 0, y, 0, y0.length);
/*  89:    */       
/*  90:    */ 
/*  91:258 */       computeDerivatives(stepEnd, y, yDot);
/*  92:    */       
/*  93:    */ 
/*  94:261 */       double[] predictedScaled = new double[y0.length];
/*  95:262 */       for (int j = 0; j < y0.length; j++) {
/*  96:263 */         predictedScaled[j] = (this.stepSize * yDot[j]);
/*  97:    */       }
/*  98:265 */       Array2DRowRealMatrix nordsieckTmp = updateHighOrderDerivativesPhase1(this.nordsieck);
/*  99:266 */       updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, nordsieckTmp);
/* 100:267 */       interpolator.reinitialize(stepEnd, this.stepSize, predictedScaled, nordsieckTmp);
/* 101:    */       
/* 102:    */ 
/* 103:270 */       interpolator.storeTime(stepEnd);
/* 104:271 */       this.stepStart = acceptStep(interpolator, y, yDot, t);
/* 105:272 */       this.scaled = predictedScaled;
/* 106:273 */       this.nordsieck = nordsieckTmp;
/* 107:274 */       interpolator.reinitialize(stepEnd, this.stepSize, this.scaled, this.nordsieck);
/* 108:276 */       if (!this.isLastStep)
/* 109:    */       {
/* 110:279 */         interpolator.storeTime(this.stepStart);
/* 111:281 */         if (this.resetOccurred)
/* 112:    */         {
/* 113:284 */           start(this.stepStart, y, t);
/* 114:285 */           interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
/* 115:    */         }
/* 116:289 */         double factor = computeStepGrowShrinkFactor(error);
/* 117:290 */         double scaledH = this.stepSize * factor;
/* 118:291 */         double nextT = this.stepStart + scaledH;
/* 119:292 */         boolean nextIsLast = nextT >= t;
/* 120:293 */         hNew = filterStep(scaledH, forward, nextIsLast);
/* 121:    */         
/* 122:295 */         double filteredNextT = this.stepStart + hNew;
/* 123:296 */         boolean filteredNextIsLast = filteredNextT >= t;
/* 124:297 */         if (filteredNextIsLast) {
/* 125:298 */           hNew = t - this.stepStart;
/* 126:    */         }
/* 127:301 */         interpolator.rescale(hNew);
/* 128:    */       }
/* 129:305 */     } while (!this.isLastStep);
/* 130:308 */     equations.setTime(this.stepStart);
/* 131:309 */     equations.setCompleteState(y);
/* 132:    */     
/* 133:311 */     resetInternalState();
/* 134:    */   }
/* 135:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator
 * JD-Core Version:    0.7.0.1
 */
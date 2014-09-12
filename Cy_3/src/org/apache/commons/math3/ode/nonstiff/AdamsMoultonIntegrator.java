/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   6:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   7:    */ import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
/*   8:    */ import org.apache.commons.math3.ode.ExpandableStatefulODE;
/*   9:    */ import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*  11:    */ 
/*  12:    */ public class AdamsMoultonIntegrator
/*  13:    */   extends AdamsIntegrator
/*  14:    */ {
/*  15:    */   private static final String METHOD_NAME = "Adams-Moulton";
/*  16:    */   
/*  17:    */   public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  18:    */     throws IllegalArgumentException
/*  19:    */   {
/*  20:179 */     super("Adams-Moulton", nSteps, nSteps + 1, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  24:    */     throws IllegalArgumentException
/*  25:    */   {
/*  26:201 */     super("Adams-Moulton", nSteps, nSteps + 1, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void integrate(ExpandableStatefulODE equations, double t)
/*  30:    */     throws MathIllegalStateException, MathIllegalArgumentException
/*  31:    */   {
/*  32:211 */     sanityChecks(equations, t);
/*  33:212 */     setEquations(equations);
/*  34:213 */     boolean forward = t > equations.getTime();
/*  35:    */     
/*  36:    */ 
/*  37:216 */     double[] y0 = equations.getCompleteState();
/*  38:217 */     double[] y = (double[])y0.clone();
/*  39:218 */     double[] yDot = new double[y.length];
/*  40:219 */     double[] yTmp = new double[y.length];
/*  41:220 */     double[] predictedScaled = new double[y.length];
/*  42:221 */     Array2DRowRealMatrix nordsieckTmp = null;
/*  43:    */     
/*  44:    */ 
/*  45:224 */     NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
/*  46:225 */     interpolator.reinitialize(y, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
/*  47:    */     
/*  48:    */ 
/*  49:    */ 
/*  50:229 */     initIntegration(equations.getTime(), y0, t);
/*  51:    */     
/*  52:    */ 
/*  53:232 */     start(equations.getTime(), y, t);
/*  54:233 */     interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
/*  55:234 */     interpolator.storeTime(this.stepStart);
/*  56:    */     
/*  57:236 */     double hNew = this.stepSize;
/*  58:237 */     interpolator.rescale(hNew);
/*  59:    */     
/*  60:239 */     this.isLastStep = false;
/*  61:    */     do
/*  62:    */     {
/*  63:242 */       double error = 10.0D;
/*  64:243 */       while (error >= 1.0D)
/*  65:    */       {
/*  66:245 */         this.stepSize = hNew;
/*  67:    */         
/*  68:    */ 
/*  69:248 */         double stepEnd = this.stepStart + this.stepSize;
/*  70:249 */         interpolator.setInterpolatedTime(stepEnd);
/*  71:250 */         System.arraycopy(interpolator.getInterpolatedState(), 0, yTmp, 0, y0.length);
/*  72:    */         
/*  73:    */ 
/*  74:253 */         computeDerivatives(stepEnd, yTmp, yDot);
/*  75:256 */         for (int j = 0; j < y0.length; j++) {
/*  76:257 */           predictedScaled[j] = (this.stepSize * yDot[j]);
/*  77:    */         }
/*  78:259 */         nordsieckTmp = updateHighOrderDerivativesPhase1(this.nordsieck);
/*  79:260 */         updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, nordsieckTmp);
/*  80:    */         
/*  81:    */ 
/*  82:263 */         error = nordsieckTmp.walkInOptimizedOrder(new Corrector(y, predictedScaled, yTmp));
/*  83:265 */         if (error >= 1.0D)
/*  84:    */         {
/*  85:267 */           double factor = computeStepGrowShrinkFactor(error);
/*  86:268 */           hNew = filterStep(this.stepSize * factor, forward, false);
/*  87:269 */           interpolator.rescale(hNew);
/*  88:    */         }
/*  89:    */       }
/*  90:274 */       double stepEnd = this.stepStart + this.stepSize;
/*  91:275 */       computeDerivatives(stepEnd, yTmp, yDot);
/*  92:    */       
/*  93:    */ 
/*  94:278 */       double[] correctedScaled = new double[y0.length];
/*  95:279 */       for (int j = 0; j < y0.length; j++) {
/*  96:280 */         correctedScaled[j] = (this.stepSize * yDot[j]);
/*  97:    */       }
/*  98:282 */       updateHighOrderDerivativesPhase2(predictedScaled, correctedScaled, nordsieckTmp);
/*  99:    */       
/* 100:    */ 
/* 101:285 */       System.arraycopy(yTmp, 0, y, 0, y.length);
/* 102:286 */       interpolator.reinitialize(stepEnd, this.stepSize, correctedScaled, nordsieckTmp);
/* 103:287 */       interpolator.storeTime(this.stepStart);
/* 104:288 */       interpolator.shift();
/* 105:289 */       interpolator.storeTime(stepEnd);
/* 106:290 */       this.stepStart = acceptStep(interpolator, y, yDot, t);
/* 107:291 */       this.scaled = correctedScaled;
/* 108:292 */       this.nordsieck = nordsieckTmp;
/* 109:294 */       if (!this.isLastStep)
/* 110:    */       {
/* 111:297 */         interpolator.storeTime(this.stepStart);
/* 112:299 */         if (this.resetOccurred)
/* 113:    */         {
/* 114:302 */           start(this.stepStart, y, t);
/* 115:303 */           interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
/* 116:    */         }
/* 117:308 */         double factor = computeStepGrowShrinkFactor(error);
/* 118:309 */         double scaledH = this.stepSize * factor;
/* 119:310 */         double nextT = this.stepStart + scaledH;
/* 120:311 */         boolean nextIsLast = nextT >= t;
/* 121:312 */         hNew = filterStep(scaledH, forward, nextIsLast);
/* 122:    */         
/* 123:314 */         double filteredNextT = this.stepStart + hNew;
/* 124:315 */         boolean filteredNextIsLast = filteredNextT >= t;
/* 125:316 */         if (filteredNextIsLast) {
/* 126:317 */           hNew = t - this.stepStart;
/* 127:    */         }
/* 128:320 */         interpolator.rescale(hNew);
/* 129:    */       }
/* 130:323 */     } while (!this.isLastStep);
/* 131:326 */     equations.setTime(this.stepStart);
/* 132:327 */     equations.setCompleteState(y);
/* 133:    */     
/* 134:329 */     resetInternalState();
/* 135:    */   }
/* 136:    */   
/* 137:    */   private class Corrector
/* 138:    */     implements RealMatrixPreservingVisitor
/* 139:    */   {
/* 140:    */     private final double[] previous;
/* 141:    */     private final double[] scaled;
/* 142:    */     private final double[] before;
/* 143:    */     private final double[] after;
/* 144:    */     
/* 145:    */     public Corrector(double[] previous, double[] scaled, double[] state)
/* 146:    */     {
/* 147:361 */       this.previous = previous;
/* 148:362 */       this.scaled = scaled;
/* 149:363 */       this.after = state;
/* 150:364 */       this.before = ((double[])state.clone());
/* 151:    */     }
/* 152:    */     
/* 153:    */     public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn)
/* 154:    */     {
/* 155:370 */       Arrays.fill(this.after, 0.0D);
/* 156:    */     }
/* 157:    */     
/* 158:    */     public void visit(int row, int column, double value)
/* 159:    */     {
/* 160:375 */       if ((row & 0x1) == 0) {
/* 161:376 */         this.after[column] -= value;
/* 162:    */       } else {
/* 163:378 */         this.after[column] += value;
/* 164:    */       }
/* 165:    */     }
/* 166:    */     
/* 167:    */     public double end()
/* 168:    */     {
/* 169:393 */       double error = 0.0D;
/* 170:394 */       for (int i = 0; i < this.after.length; i++)
/* 171:    */       {
/* 172:395 */         this.after[i] += this.previous[i] + this.scaled[i];
/* 173:396 */         if (i < AdamsMoultonIntegrator.this.mainSetDimension)
/* 174:    */         {
/* 175:397 */           double yScale = FastMath.max(FastMath.abs(this.previous[i]), FastMath.abs(this.after[i]));
/* 176:398 */           double tol = AdamsMoultonIntegrator.this.vecAbsoluteTolerance == null ? AdamsMoultonIntegrator.this.scalAbsoluteTolerance + AdamsMoultonIntegrator.this.scalRelativeTolerance * yScale : AdamsMoultonIntegrator.this.vecAbsoluteTolerance[i] + AdamsMoultonIntegrator.this.vecRelativeTolerance[i] * yScale;
/* 177:    */           
/* 178:    */ 
/* 179:401 */           double ratio = (this.after[i] - this.before[i]) / tol;
/* 180:402 */           error += ratio * ratio;
/* 181:    */         }
/* 182:    */       }
/* 183:406 */       return FastMath.sqrt(error / AdamsMoultonIntegrator.this.mainSetDimension);
/* 184:    */     }
/* 185:    */   }
/* 186:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.AdamsMoultonIntegrator
 * JD-Core Version:    0.7.0.1
 */
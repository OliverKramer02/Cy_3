/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.ode.AbstractIntegrator;
/*   9:    */ import org.apache.commons.math3.ode.EquationsMapper;
/*  10:    */ import org.apache.commons.math3.ode.ExpandableStatefulODE;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public abstract class AdaptiveStepsizeIntegrator
/*  14:    */   extends AbstractIntegrator
/*  15:    */ {
/*  16:    */   protected double scalAbsoluteTolerance;
/*  17:    */   protected double scalRelativeTolerance;
/*  18:    */   protected double[] vecAbsoluteTolerance;
/*  19:    */   protected double[] vecRelativeTolerance;
/*  20:    */   protected int mainSetDimension;
/*  21:    */   private double initialStep;
/*  22:    */   private double minStep;
/*  23:    */   private double maxStep;
/*  24:    */   
/*  25:    */   public AdaptiveStepsizeIntegrator(String name, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  26:    */   {
/*  27:110 */     super(name);
/*  28:111 */     setStepSizeControl(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  29:112 */     resetInternalState();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public AdaptiveStepsizeIntegrator(String name, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  33:    */   {
/*  34:133 */     super(name);
/*  35:134 */     setStepSizeControl(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  36:135 */     resetInternalState();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setStepSizeControl(double minimalStep, double maximalStep, double absoluteTolerance, double relativeTolerance)
/*  40:    */   {
/*  41:157 */     this.minStep = FastMath.abs(minimalStep);
/*  42:158 */     this.maxStep = FastMath.abs(maximalStep);
/*  43:159 */     this.initialStep = -1.0D;
/*  44:    */     
/*  45:161 */     this.scalAbsoluteTolerance = absoluteTolerance;
/*  46:162 */     this.scalRelativeTolerance = relativeTolerance;
/*  47:163 */     this.vecAbsoluteTolerance = null;
/*  48:164 */     this.vecRelativeTolerance = null;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setStepSizeControl(double minimalStep, double maximalStep, double[] absoluteTolerance, double[] relativeTolerance)
/*  52:    */   {
/*  53:186 */     this.minStep = FastMath.abs(minimalStep);
/*  54:187 */     this.maxStep = FastMath.abs(maximalStep);
/*  55:188 */     this.initialStep = -1.0D;
/*  56:    */     
/*  57:190 */     this.scalAbsoluteTolerance = 0.0D;
/*  58:191 */     this.scalRelativeTolerance = 0.0D;
/*  59:192 */     this.vecAbsoluteTolerance = ((double[])absoluteTolerance.clone());
/*  60:193 */     this.vecRelativeTolerance = ((double[])relativeTolerance.clone());
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setInitialStepSize(double initialStepSize)
/*  64:    */   {
/*  65:209 */     if ((initialStepSize < this.minStep) || (initialStepSize > this.maxStep)) {
/*  66:210 */       this.initialStep = -1.0D;
/*  67:    */     } else {
/*  68:212 */       this.initialStep = initialStepSize;
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected void sanityChecks(ExpandableStatefulODE equations, double t)
/*  73:    */     throws DimensionMismatchException, NumberIsTooSmallException
/*  74:    */   {
/*  75:221 */     super.sanityChecks(equations, t);
/*  76:    */     
/*  77:223 */     this.mainSetDimension = equations.getPrimaryMapper().getDimension();
/*  78:225 */     if ((this.vecAbsoluteTolerance != null) && (this.vecAbsoluteTolerance.length != this.mainSetDimension)) {
/*  79:226 */       throw new DimensionMismatchException(this.mainSetDimension, this.vecAbsoluteTolerance.length);
/*  80:    */     }
/*  81:229 */     if ((this.vecRelativeTolerance != null) && (this.vecRelativeTolerance.length != this.mainSetDimension)) {
/*  82:230 */       throw new DimensionMismatchException(this.mainSetDimension, this.vecRelativeTolerance.length);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public double initializeStep(boolean forward, int order, double[] scale, double t0, double[] y0, double[] yDot0, double[] y1, double[] yDot1)
/*  87:    */   {
/*  88:250 */     if (this.initialStep > 0.0D) {
/*  89:252 */       return forward ? this.initialStep : -this.initialStep;
/*  90:    */     }
/*  91:258 */     double yOnScale2 = 0.0D;
/*  92:259 */     double yDotOnScale2 = 0.0D;
/*  93:260 */     for (int j = 0; j < scale.length; j++)
/*  94:    */     {
/*  95:261 */       double ratio = y0[j] / scale[j];
/*  96:262 */       yOnScale2 += ratio * ratio;
/*  97:263 */       ratio = yDot0[j] / scale[j];
/*  98:264 */       yDotOnScale2 += ratio * ratio;
/*  99:    */     }
/* 100:267 */     double h = (yOnScale2 < 1.0E-010D) || (yDotOnScale2 < 1.0E-010D) ? 1.0E-006D : 0.01D * FastMath.sqrt(yOnScale2 / yDotOnScale2);
/* 101:269 */     if (!forward) {
/* 102:270 */       h = -h;
/* 103:    */     }
/* 104:274 */     for (int j = 0; j < y0.length; j++) {
/* 105:275 */       y0[j] += h * yDot0[j];
/* 106:    */     }
/* 107:277 */     computeDerivatives(t0 + h, y1, yDot1);
/* 108:    */     
/* 109:    */ 
/* 110:280 */     double yDDotOnScale = 0.0D;
/* 111:281 */     for (int j = 0; j < scale.length; j++)
/* 112:    */     {
/* 113:282 */       double ratio = (yDot1[j] - yDot0[j]) / scale[j];
/* 114:283 */       yDDotOnScale += ratio * ratio;
/* 115:    */     }
/* 116:285 */     yDDotOnScale = FastMath.sqrt(yDDotOnScale) / h;
/* 117:    */     
/* 118:    */ 
/* 119:    */ 
/* 120:289 */     double maxInv2 = FastMath.max(FastMath.sqrt(yDotOnScale2), yDDotOnScale);
/* 121:290 */     double h1 = maxInv2 < 1.E-015D ? FastMath.max(1.0E-006D, 0.001D * FastMath.abs(h)) : FastMath.pow(0.01D / maxInv2, 1.0D / order);
/* 122:    */     
/* 123:    */ 
/* 124:293 */     h = FastMath.min(100.0D * FastMath.abs(h), h1);
/* 125:294 */     h = FastMath.max(h, 1.0E-012D * FastMath.abs(t0));
/* 126:295 */     if (h < getMinStep()) {
/* 127:296 */       h = getMinStep();
/* 128:    */     }
/* 129:298 */     if (h > getMaxStep()) {
/* 130:299 */       h = getMaxStep();
/* 131:    */     }
/* 132:301 */     if (!forward) {
/* 133:302 */       h = -h;
/* 134:    */     }
/* 135:305 */     return h;
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected double filterStep(double h, boolean forward, boolean acceptSmall)
/* 139:    */     throws NumberIsTooSmallException
/* 140:    */   {
/* 141:321 */     double filteredH = h;
/* 142:322 */     if (FastMath.abs(h) < this.minStep) {
/* 143:323 */       if (acceptSmall) {
/* 144:324 */         filteredH = forward ? this.minStep : -this.minStep;
/* 145:    */       } else {
/* 146:326 */         throw new NumberIsTooSmallException(LocalizedFormats.MINIMAL_STEPSIZE_REACHED_DURING_INTEGRATION, Double.valueOf(FastMath.abs(h)), Double.valueOf(this.minStep), true);
/* 147:    */       }
/* 148:    */     }
/* 149:331 */     if (filteredH > this.maxStep) {
/* 150:332 */       filteredH = this.maxStep;
/* 151:333 */     } else if (filteredH < -this.maxStep) {
/* 152:334 */       filteredH = -this.maxStep;
/* 153:    */     }
/* 154:337 */     return filteredH;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public abstract void integrate(ExpandableStatefulODE paramExpandableStatefulODE, double paramDouble)
/* 158:    */     throws MathIllegalStateException, MathIllegalArgumentException;
/* 159:    */   
/* 160:    */   public double getCurrentStepStart()
/* 161:    */   {
/* 162:349 */     return this.stepStart;
/* 163:    */   }
/* 164:    */   
/* 165:    */   protected void resetInternalState()
/* 166:    */   {
/* 167:354 */     this.stepStart = (0.0D / 0.0D);
/* 168:355 */     this.stepSize = FastMath.sqrt(this.minStep * this.maxStep);
/* 169:    */   }
/* 170:    */   
/* 171:    */   public double getMinStep()
/* 172:    */   {
/* 173:362 */     return this.minStep;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public double getMaxStep()
/* 177:    */   {
/* 178:369 */     return this.maxStep;
/* 179:    */   }
/* 180:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator
 * JD-Core Version:    0.7.0.1
 */
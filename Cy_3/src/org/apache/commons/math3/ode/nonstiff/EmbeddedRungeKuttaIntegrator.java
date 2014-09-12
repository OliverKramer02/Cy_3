/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.ode.ExpandableStatefulODE;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ 
/*   8:    */ public abstract class EmbeddedRungeKuttaIntegrator
/*   9:    */   extends AdaptiveStepsizeIntegrator
/*  10:    */ {
/*  11:    */   private final boolean fsal;
/*  12:    */   private final double[] c;
/*  13:    */   private final double[][] a;
/*  14:    */   private final double[] b;
/*  15:    */   private final RungeKuttaStepInterpolator prototype;
/*  16:    */   private final double exp;
/*  17:    */   private double safety;
/*  18:    */   private double minReduction;
/*  19:    */   private double maxGrowth;
/*  20:    */   
/*  21:    */   protected EmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  22:    */   {
/*  23:115 */     super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  24:    */     
/*  25:117 */     this.fsal = fsal;
/*  26:118 */     this.c = c;
/*  27:119 */     this.a = a;
/*  28:120 */     this.b = b;
/*  29:121 */     this.prototype = prototype;
/*  30:    */     
/*  31:123 */     this.exp = (-1.0D / getOrder());
/*  32:    */     
/*  33:    */ 
/*  34:126 */     setSafety(0.9D);
/*  35:127 */     setMinReduction(0.2D);
/*  36:128 */     setMaxGrowth(10.0D);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected EmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  40:    */   {
/*  41:153 */     super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  42:    */     
/*  43:155 */     this.fsal = fsal;
/*  44:156 */     this.c = c;
/*  45:157 */     this.a = a;
/*  46:158 */     this.b = b;
/*  47:159 */     this.prototype = prototype;
/*  48:    */     
/*  49:161 */     this.exp = (-1.0D / getOrder());
/*  50:    */     
/*  51:    */ 
/*  52:164 */     setSafety(0.9D);
/*  53:165 */     setMinReduction(0.2D);
/*  54:166 */     setMaxGrowth(10.0D);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public abstract int getOrder();
/*  58:    */   
/*  59:    */   public double getSafety()
/*  60:    */   {
/*  61:179 */     return this.safety;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setSafety(double safety)
/*  65:    */   {
/*  66:186 */     this.safety = safety;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void integrate(ExpandableStatefulODE equations, double t)
/*  70:    */     throws MathIllegalStateException, MathIllegalArgumentException
/*  71:    */   {
/*  72:194 */     sanityChecks(equations, t);
/*  73:195 */     setEquations(equations);
/*  74:196 */     boolean forward = t > equations.getTime();
/*  75:    */     
/*  76:    */ 
/*  77:199 */     double[] y0 = equations.getCompleteState();
/*  78:200 */     double[] y = (double[])y0.clone();
/*  79:201 */     int stages = this.c.length + 1;
/*  80:202 */     double[][] yDotK = new double[stages][y.length];
/*  81:203 */     double[] yTmp = (double[])y0.clone();
/*  82:204 */     double[] yDotTmp = new double[y.length];
/*  83:    */     
/*  84:    */ 
/*  85:207 */     RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator)this.prototype.copy();
/*  86:208 */     interpolator.reinitialize(this, yTmp, yDotK, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
/*  87:    */     
/*  88:210 */     interpolator.storeTime(equations.getTime());
/*  89:    */     
/*  90:    */ 
/*  91:213 */     this.stepStart = equations.getTime();
/*  92:214 */     double hNew = 0.0D;
/*  93:215 */     boolean firstTime = true;
/*  94:216 */     initIntegration(equations.getTime(), y0, t);
/*  95:    */     
/*  96:    */ 
/*  97:219 */     this.isLastStep = false;
/*  98:    */     do
/*  99:    */     {
/* 100:222 */       interpolator.shift();
/* 101:    */       
/* 102:    */ 
/* 103:225 */       double error = 10.0D;
/* 104:226 */       while (error >= 1.0D)
/* 105:    */       {
/* 106:228 */         if ((firstTime) || (!this.fsal)) {
/* 107:230 */           computeDerivatives(this.stepStart, y, yDotK[0]);
/* 108:    */         }
/* 109:233 */         if (firstTime)
/* 110:    */         {
/* 111:234 */           double[] scale = new double[this.mainSetDimension];
/* 112:235 */           if (this.vecAbsoluteTolerance == null) {
/* 113:236 */             for (int i = 0; i < scale.length; i++) {
/* 114:237 */               scale[i] = (this.scalAbsoluteTolerance + this.scalRelativeTolerance * FastMath.abs(y[i]));
/* 115:    */             }
/* 116:    */           } else {
/* 117:240 */             for (int i = 0; i < scale.length; i++) {
/* 118:241 */               scale[i] = (this.vecAbsoluteTolerance[i] + this.vecRelativeTolerance[i] * FastMath.abs(y[i]));
/* 119:    */             }
/* 120:    */           }
/* 121:244 */           hNew = initializeStep(forward, getOrder(), scale, this.stepStart, y, yDotK[0], yTmp, yDotK[1]);
/* 122:    */           
/* 123:246 */           firstTime = false;
/* 124:    */         }
/* 125:249 */         this.stepSize = hNew;
/* 126:250 */         if (forward)
/* 127:    */         {
/* 128:251 */           if (this.stepStart + this.stepSize >= t) {
/* 129:252 */             this.stepSize = (t - this.stepStart);
/* 130:    */           }
/* 131:    */         }
/* 132:255 */         else if (this.stepStart + this.stepSize <= t) {
/* 133:256 */           this.stepSize = (t - this.stepStart);
/* 134:    */         }
/* 135:261 */         for (int k = 1; k < stages; k++)
/* 136:    */         {
/* 137:263 */           for (int j = 0; j < y0.length; j++)
/* 138:    */           {
/* 139:264 */             double sum = this.a[(k - 1)][0] * yDotK[0][j];
/* 140:265 */             for (int l = 1; l < k; l++) {
/* 141:266 */               sum += this.a[(k - 1)][l] * yDotK[l][j];
/* 142:    */             }
/* 143:268 */             y[j] += this.stepSize * sum;
/* 144:    */           }
/* 145:271 */           computeDerivatives(this.stepStart + this.c[(k - 1)] * this.stepSize, yTmp, yDotK[k]);
/* 146:    */         }
/* 147:276 */         for (int j = 0; j < y0.length; j++)
/* 148:    */         {
/* 149:277 */           double sum = this.b[0] * yDotK[0][j];
/* 150:278 */           for (int l = 1; l < stages; l++) {
/* 151:279 */             sum += this.b[l] * yDotK[l][j];
/* 152:    */           }
/* 153:281 */           y[j] += this.stepSize * sum;
/* 154:    */         }
/* 155:285 */         error = estimateError(yDotK, y, yTmp, this.stepSize);
/* 156:286 */         if (error >= 1.0D)
/* 157:    */         {
/* 158:288 */           double factor = FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error, this.exp)));
/* 159:    */           
/* 160:    */ 
/* 161:291 */           hNew = filterStep(this.stepSize * factor, forward, false);
/* 162:    */         }
/* 163:    */       }
/* 164:297 */       interpolator.storeTime(this.stepStart + this.stepSize);
/* 165:298 */       System.arraycopy(yTmp, 0, y, 0, y0.length);
/* 166:299 */       System.arraycopy(yDotK[(stages - 1)], 0, yDotTmp, 0, y0.length);
/* 167:300 */       this.stepStart = acceptStep(interpolator, y, yDotTmp, t);
/* 168:301 */       System.arraycopy(y, 0, yTmp, 0, y.length);
/* 169:303 */       if (!this.isLastStep)
/* 170:    */       {
/* 171:306 */         interpolator.storeTime(this.stepStart);
/* 172:308 */         if (this.fsal) {
/* 173:310 */           System.arraycopy(yDotTmp, 0, yDotK[0], 0, y0.length);
/* 174:    */         }
/* 175:314 */         double factor = FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error, this.exp)));
/* 176:    */         
/* 177:316 */         double scaledH = this.stepSize * factor;
/* 178:317 */         double nextT = this.stepStart + scaledH;
/* 179:318 */         boolean nextIsLast = nextT >= t;
/* 180:319 */         hNew = filterStep(scaledH, forward, nextIsLast);
/* 181:    */         
/* 182:321 */         double filteredNextT = this.stepStart + hNew;
/* 183:322 */         boolean filteredNextIsLast = filteredNextT >= t;
/* 184:323 */         if (filteredNextIsLast) {
/* 185:324 */           hNew = t - this.stepStart;
/* 186:    */         }
/* 187:    */       }
/* 188:329 */     } while (!this.isLastStep);
/* 189:332 */     equations.setTime(this.stepStart);
/* 190:333 */     equations.setCompleteState(y);
/* 191:    */     
/* 192:335 */     resetInternalState();
/* 193:    */   }
/* 194:    */   
/* 195:    */   public double getMinReduction()
/* 196:    */   {
/* 197:343 */     return this.minReduction;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void setMinReduction(double minReduction)
/* 201:    */   {
/* 202:350 */     this.minReduction = minReduction;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public double getMaxGrowth()
/* 206:    */   {
/* 207:357 */     return this.maxGrowth;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public void setMaxGrowth(double maxGrowth)
/* 211:    */   {
/* 212:364 */     this.maxGrowth = maxGrowth;
/* 213:    */   }
/* 214:    */   
/* 215:    */   protected abstract double estimateError(double[][] paramArrayOfDouble, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double paramDouble);
/* 216:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator
 * JD-Core Version:    0.7.0.1
 */
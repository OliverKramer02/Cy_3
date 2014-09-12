/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInput;
/*   5:    */ import java.io.ObjectOutput;
/*   6:    */ import org.apache.commons.math3.ode.AbstractIntegrator;
/*   7:    */ import org.apache.commons.math3.ode.EquationsMapper;
/*   8:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   9:    */ 
/*  10:    */ class DormandPrince853StepInterpolator
/*  11:    */   extends RungeKuttaStepInterpolator
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = 20111120L;
/*  14:    */   private static final double B_01 = 0.05429373411656877D;
/*  15:    */   private static final double B_06 = 4.450312892752409D;
/*  16:    */   private static final double B_07 = 1.8915178993145D;
/*  17:    */   private static final double B_08 = -5.801203960010585D;
/*  18:    */   private static final double B_09 = 0.31116436695782D;
/*  19:    */   private static final double B_10 = -0.1521609496625161D;
/*  20:    */   private static final double B_11 = 0.2013654008040303D;
/*  21:    */   private static final double B_12 = 0.04471061572777259D;
/*  22:    */   private static final double C14 = 0.1D;
/*  23:    */   private static final double K14_01 = 0.001873768166479189D;
/*  24:    */   private static final double K14_06 = -4.450312892752409D;
/*  25:    */   private static final double K14_07 = -1.638017689097876D;
/*  26:    */   private static final double K14_08 = 5.554964922539782D;
/*  27:    */   private static final double K14_09 = -0.4353557902216363D;
/*  28:    */   private static final double K14_10 = 0.3054527479412817D;
/*  29:    */   private static final double K14_11 = -0.1931643485083956D;
/*  30:    */   private static final double K14_12 = -0.03714271806722689D;
/*  31:    */   private static final double K14_13 = -0.008298D;
/*  32:    */   private static final double C15 = 0.2D;
/*  33:    */   private static final double K15_01 = -0.02245908595306662D;
/*  34:    */   private static final double K15_06 = -4.422011983080043D;
/*  35:    */   private static final double K15_07 = -1.837975911007062D;
/*  36:    */   private static final double K15_08 = 5.746280211439194D;
/*  37:    */   private static final double K15_09 = -0.31116436695782D;
/*  38:    */   private static final double K15_10 = 0.1521609496625161D;
/*  39:    */   private static final double K15_11 = -0.2014737481327276D;
/*  40:    */   private static final double K15_12 = -0.04432804463693693D;
/*  41:    */   private static final double K15_13 = -0.0003404650086874046D;
/*  42:    */   private static final double K15_14 = 0.141312443674633D;
/*  43:    */   private static final double C16 = 0.7777777777777778D;
/*  44:    */   private static final double K16_01 = -0.4831900357003607D;
/*  45:    */   private static final double K16_06 = -9.147934308113573D;
/*  46:    */   private static final double K16_07 = 5.791903296748099D;
/*  47:    */   private static final double K16_08 = 9.870193778407696D;
/*  48:    */   private static final double K16_09 = 0.04556282049746119D;
/*  49:    */   private static final double K16_10 = 0.1521609496625161D;
/*  50:    */   private static final double K16_11 = -0.2013654008040303D;
/*  51:    */   private static final double K16_12 = -0.04471061572777259D;
/*  52:    */   private static final double K16_13 = -0.001399024165159015D;
/*  53:    */   private static final double K16_14 = 2.947514789152772D;
/*  54:    */   private static final double K16_15 = -9.15095847217987D;
/*  55:180 */   private static final double[][] D = { { -8.428938276109014D, 0.5667149535193777D, -3.068949945949892D, 2.38466765651207D, 2.117034582445029D, -0.871391583777973D, 2.240437430260788D, 0.6315787787694688D, -0.08899033645133331D, 18.148505520854727D, -9.194632392478356D, -4.436036387594894D }, { 10.427508642579134D, 242.28349177525817D, 165.20045171727028D, -374.5467547226902D, -22.113666853125302D, 7.733432668472264D, -30.674084731089398D, -9.332130526430229D, 15.697238121770845D, -31.139403219565178D, -9.352924358844479D, 35.816841486394082D }, { 19.985053242002433D, -387.03730874935178D, -189.17813819516758D, 527.80815920542364D, -11.573902539959631D, 6.8812326946963D, -1.000605096691084D, 0.7777137798053443D, -2.778205752353508D, -60.196695231264123D, 84.320405506677162D, 11.992291136182789D }, { -25.69393346270375D, -154.18974869023643D, -231.5293791760455D, 357.63911791061412D, 93.405324183624302D, -37.458323136451632D, 104.0996495089623D, 29.840293426660502D, -43.533456590011141D, 96.324553959188279D, -39.177261675615441D, -149.72683625798564D } };
/*  56:    */   private double[][] yDotKLast;
/*  57:    */   private double[][] v;
/*  58:    */   private boolean vectorsInitialized;
/*  59:    */   
/*  60:    */   public DormandPrince853StepInterpolator()
/*  61:    */   {
/*  62:232 */     this.yDotKLast = ((double[][])null);
/*  63:233 */     this.v = ((double[][])null);
/*  64:234 */     this.vectorsInitialized = false;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public DormandPrince853StepInterpolator(DormandPrince853StepInterpolator interpolator)
/*  68:    */   {
/*  69:244 */     super(interpolator);
/*  70:246 */     if (interpolator.currentState == null)
/*  71:    */     {
/*  72:248 */       this.yDotKLast = ((double[][])null);
/*  73:249 */       this.v = ((double[][])null);
/*  74:250 */       this.vectorsInitialized = false;
/*  75:    */     }
/*  76:    */     else
/*  77:    */     {
/*  78:254 */       int dimension = interpolator.currentState.length;
/*  79:    */       
/*  80:256 */       this.yDotKLast = new double[3][];
/*  81:257 */       for (int k = 0; k < this.yDotKLast.length; k++)
/*  82:    */       {
/*  83:258 */         this.yDotKLast[k] = new double[dimension];
/*  84:259 */         System.arraycopy(interpolator.yDotKLast[k], 0, this.yDotKLast[k], 0, dimension);
/*  85:    */       }
/*  86:263 */       this.v = new double[7][];
/*  87:264 */       for (int k = 0; k < this.v.length; k++)
/*  88:    */       {
/*  89:265 */         this.v[k] = new double[dimension];
/*  90:266 */         System.arraycopy(interpolator.v[k], 0, this.v[k], 0, dimension);
/*  91:    */       }
/*  92:269 */       this.vectorsInitialized = interpolator.vectorsInitialized;
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected StepInterpolator doCopy()
/*  97:    */   {
/*  98:278 */     return new DormandPrince853StepInterpolator(this);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void reinitialize(AbstractIntegrator integrator, double[] y, double[][] yDotK, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers)
/* 102:    */   {
/* 103:288 */     super.reinitialize(integrator, y, yDotK, forward, primaryMapper, secondaryMappers);
/* 104:    */     
/* 105:290 */     int dimension = this.currentState.length;
/* 106:    */     
/* 107:292 */     this.yDotKLast = new double[3][];
/* 108:293 */     for (int k = 0; k < this.yDotKLast.length; k++) {
/* 109:294 */       this.yDotKLast[k] = new double[dimension];
/* 110:    */     }
/* 111:297 */     this.v = new double[7][];
/* 112:298 */     for (int k = 0; k < this.v.length; k++) {
/* 113:299 */       this.v[k] = new double[dimension];
/* 114:    */     }
/* 115:302 */     this.vectorsInitialized = false;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void storeTime(double t)
/* 119:    */   {
/* 120:309 */     super.storeTime(t);
/* 121:310 */     this.vectorsInitialized = false;
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/* 125:    */   {
/* 126:318 */     if (!this.vectorsInitialized)
/* 127:    */     {
/* 128:320 */       if (this.v == null)
/* 129:    */       {
/* 130:321 */         this.v = new double[7][];
/* 131:322 */         for (int k = 0; k < 7; k++) {
/* 132:323 */           this.v[k] = new double[this.interpolatedState.length];
/* 133:    */         }
/* 134:    */       }
/* 135:328 */       finalizeStep();
/* 136:331 */       for (int i = 0; i < this.interpolatedState.length; i++)
/* 137:    */       {
/* 138:332 */         double yDot1 = this.yDotK[0][i];
/* 139:333 */         double yDot6 = this.yDotK[5][i];
/* 140:334 */         double yDot7 = this.yDotK[6][i];
/* 141:335 */         double yDot8 = this.yDotK[7][i];
/* 142:336 */         double yDot9 = this.yDotK[8][i];
/* 143:337 */         double yDot10 = this.yDotK[9][i];
/* 144:338 */         double yDot11 = this.yDotK[10][i];
/* 145:339 */         double yDot12 = this.yDotK[11][i];
/* 146:340 */         double yDot13 = this.yDotK[12][i];
/* 147:341 */         double yDot14 = this.yDotKLast[0][i];
/* 148:342 */         double yDot15 = this.yDotKLast[1][i];
/* 149:343 */         double yDot16 = this.yDotKLast[2][i];
/* 150:344 */         this.v[0][i] = (0.05429373411656877D * yDot1 + 4.450312892752409D * yDot6 + 1.8915178993145D * yDot7 + -5.801203960010585D * yDot8 + 0.31116436695782D * yDot9 + -0.1521609496625161D * yDot10 + 0.2013654008040303D * yDot11 + 0.04471061572777259D * yDot12);
/* 151:    */         
/* 152:    */ 
/* 153:347 */         this.v[1][i] = (yDot1 - this.v[0][i]);
/* 154:348 */         this.v[2][i] = (this.v[0][i] - this.v[1][i] - this.yDotK[12][i]);
/* 155:349 */         for (int k = 0; k < D.length; k++) {
/* 156:350 */           this.v[(k + 3)][i] = (D[k][0] * yDot1 + D[k][1] * yDot6 + D[k][2] * yDot7 + D[k][3] * yDot8 + D[k][4] * yDot9 + D[k][5] * yDot10 + D[k][6] * yDot11 + D[k][7] * yDot12 + D[k][8] * yDot13 + D[k][9] * yDot14 + D[k][10] * yDot15 + D[k][11] * yDot16);
/* 157:    */         }
/* 158:    */       }
/* 159:357 */       this.vectorsInitialized = true;
/* 160:    */     }
/* 161:361 */     double eta = 1.0D - theta;
/* 162:362 */     double twoTheta = 2.0D * theta;
/* 163:363 */     double theta2 = theta * theta;
/* 164:364 */     double dot1 = 1.0D - twoTheta;
/* 165:365 */     double dot2 = theta * (2.0D - 3.0D * theta);
/* 166:366 */     double dot3 = twoTheta * (1.0D + theta * (twoTheta - 3.0D));
/* 167:367 */     double dot4 = theta2 * (3.0D + theta * (5.0D * theta - 8.0D));
/* 168:368 */     double dot5 = theta2 * (3.0D + theta * (-12.0D + theta * (15.0D - 6.0D * theta)));
/* 169:369 */     double dot6 = theta2 * theta * (4.0D + theta * (-15.0D + theta * (18.0D - 7.0D * theta)));
/* 170:371 */     if ((this.previousState != null) && (theta <= 0.5D)) {
/* 171:372 */       for (int i = 0; i < this.interpolatedState.length; i++)
/* 172:    */       {
/* 173:373 */         this.interpolatedState[i] = (this.previousState[i] + theta * this.h * (this.v[0][i] + eta * (this.v[1][i] + theta * (this.v[2][i] + eta * (this.v[3][i] + theta * (this.v[4][i] + eta * (this.v[5][i] + theta * this.v[6][i])))))));
/* 174:    */         
/* 175:    */ 
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:    */ 
/* 180:    */ 
/* 181:381 */         this.interpolatedDerivatives[i] = (this.v[0][i] + dot1 * this.v[1][i] + dot2 * this.v[2][i] + dot3 * this.v[3][i] + dot4 * this.v[4][i] + dot5 * this.v[5][i] + dot6 * this.v[6][i]);
/* 182:    */       }
/* 183:    */     } else {
/* 184:386 */       for (int i = 0; i < this.interpolatedState.length; i++)
/* 185:    */       {
/* 186:387 */         this.interpolatedState[i] = (this.currentState[i] - oneMinusThetaH * (this.v[0][i] - theta * (this.v[1][i] + theta * (this.v[2][i] + eta * (this.v[3][i] + theta * (this.v[4][i] + eta * (this.v[5][i] + theta * this.v[6][i])))))));
/* 187:    */         
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:    */ 
/* 192:    */ 
/* 193:    */ 
/* 194:395 */         this.interpolatedDerivatives[i] = (this.v[0][i] + dot1 * this.v[1][i] + dot2 * this.v[2][i] + dot3 * this.v[3][i] + dot4 * this.v[4][i] + dot5 * this.v[5][i] + dot6 * this.v[6][i]);
/* 195:    */       }
/* 196:    */     }
/* 197:    */   }
/* 198:    */   
/* 199:    */   protected void doFinalize()
/* 200:    */   {
/* 201:407 */     if (this.currentState == null) {
/* 202:409 */       return;
/* 203:    */     }
/* 204:413 */     double[] yTmp = new double[this.currentState.length];
/* 205:414 */     double pT = getGlobalPreviousTime();
/* 206:417 */     for (int j = 0; j < this.currentState.length; j++)
/* 207:    */     {
/* 208:418 */       double s = 0.001873768166479189D * this.yDotK[0][j] + -4.450312892752409D * this.yDotK[5][j] + -1.638017689097876D * this.yDotK[6][j] + 5.554964922539782D * this.yDotK[7][j] + -0.4353557902216363D * this.yDotK[8][j] + 0.3054527479412817D * this.yDotK[9][j] + -0.1931643485083956D * this.yDotK[10][j] + -0.03714271806722689D * this.yDotK[11][j] + -0.008298D * this.yDotK[12][j];
/* 209:    */       
/* 210:    */ 
/* 211:421 */       yTmp[j] = (this.currentState[j] + this.h * s);
/* 212:    */     }
/* 213:423 */     this.integrator.computeDerivatives(pT + 0.1D * this.h, yTmp, this.yDotKLast[0]);
/* 214:426 */     for (int j = 0; j < this.currentState.length; j++)
/* 215:    */     {
/* 216:427 */       double s = -0.02245908595306662D * this.yDotK[0][j] + -4.422011983080043D * this.yDotK[5][j] + -1.837975911007062D * this.yDotK[6][j] + 5.746280211439194D * this.yDotK[7][j] + -0.31116436695782D * this.yDotK[8][j] + 0.1521609496625161D * this.yDotK[9][j] + -0.2014737481327276D * this.yDotK[10][j] + -0.04432804463693693D * this.yDotK[11][j] + -0.0003404650086874046D * this.yDotK[12][j] + 0.141312443674633D * this.yDotKLast[0][j];
/* 217:    */       
/* 218:    */ 
/* 219:    */ 
/* 220:431 */       yTmp[j] = (this.currentState[j] + this.h * s);
/* 221:    */     }
/* 222:433 */     this.integrator.computeDerivatives(pT + 0.2D * this.h, yTmp, this.yDotKLast[1]);
/* 223:436 */     for (int j = 0; j < this.currentState.length; j++)
/* 224:    */     {
/* 225:437 */       double s = -0.4831900357003607D * this.yDotK[0][j] + -9.147934308113573D * this.yDotK[5][j] + 5.791903296748099D * this.yDotK[6][j] + 9.870193778407696D * this.yDotK[7][j] + 0.04556282049746119D * this.yDotK[8][j] + 0.1521609496625161D * this.yDotK[9][j] + -0.2013654008040303D * this.yDotK[10][j] + -0.04471061572777259D * this.yDotK[11][j] + -0.001399024165159015D * this.yDotK[12][j] + 2.947514789152772D * this.yDotKLast[0][j] + -9.15095847217987D * this.yDotKLast[1][j];
/* 226:    */       
/* 227:    */ 
/* 228:    */ 
/* 229:441 */       yTmp[j] = (this.currentState[j] + this.h * s);
/* 230:    */     }
/* 231:443 */     this.integrator.computeDerivatives(pT + 0.7777777777777778D * this.h, yTmp, this.yDotKLast[2]);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void writeExternal(ObjectOutput out)
/* 235:    */     throws IOException
/* 236:    */   {
/* 237:453 */     finalizeStep();
/* 238:    */     
/* 239:455 */     int dimension = this.currentState == null ? -1 : this.currentState.length;
/* 240:456 */     out.writeInt(dimension);
/* 241:457 */     for (int i = 0; i < dimension; i++)
/* 242:    */     {
/* 243:458 */       out.writeDouble(this.yDotKLast[0][i]);
/* 244:459 */       out.writeDouble(this.yDotKLast[1][i]);
/* 245:460 */       out.writeDouble(this.yDotKLast[2][i]);
/* 246:    */     }
/* 247:464 */     super.writeExternal(out);
/* 248:    */   }
/* 249:    */   
/* 250:    */   public void readExternal(ObjectInput in)
/* 251:    */     throws IOException, ClassNotFoundException
/* 252:    */   {
/* 253:474 */     this.yDotKLast = new double[3][];
/* 254:475 */     int dimension = in.readInt();
/* 255:476 */     this.yDotKLast[0] = (dimension < 0 ? null : new double[dimension]);
/* 256:477 */     this.yDotKLast[1] = (dimension < 0 ? null : new double[dimension]);
/* 257:478 */     this.yDotKLast[2] = (dimension < 0 ? null : new double[dimension]);
/* 258:480 */     for (int i = 0; i < dimension; i++)
/* 259:    */     {
/* 260:481 */       this.yDotKLast[0][i] = in.readDouble();
/* 261:482 */       this.yDotKLast[1][i] = in.readDouble();
/* 262:483 */       this.yDotKLast[2][i] = in.readDouble();
/* 263:    */     }
/* 264:487 */     super.readExternal(in);
/* 265:    */   }
/* 266:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.DormandPrince853StepInterpolator
 * JD-Core Version:    0.7.0.1
 */
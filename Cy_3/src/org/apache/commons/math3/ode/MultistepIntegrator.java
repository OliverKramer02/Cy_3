/*   1:    */ package org.apache.commons.math3.ode;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   7:    */ import org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator;
/*   8:    */ import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
/*   9:    */ import org.apache.commons.math3.ode.sampling.StepHandler;
/*  10:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public abstract class MultistepIntegrator
/*  14:    */   extends AdaptiveStepsizeIntegrator
/*  15:    */ {
/*  16:    */   protected double[] scaled;
/*  17:    */   protected Array2DRowRealMatrix nordsieck;
/*  18:    */   private FirstOrderIntegrator starter;
/*  19:    */   private final int nSteps;
/*  20:    */   private double exp;
/*  21:    */   private double safety;
/*  22:    */   private double minReduction;
/*  23:    */   private double maxGrowth;
/*  24:    */   
/*  25:    */   protected MultistepIntegrator(String name, int nSteps, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  26:    */   {
/*  27:114 */     super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  28:116 */     if (nSteps <= 1) {
/*  29:117 */       throw new MathIllegalArgumentException(LocalizedFormats.INTEGRATION_METHOD_NEEDS_AT_LEAST_TWO_PREVIOUS_POINTS, new Object[] { name });
/*  30:    */     }
/*  31:122 */     this.starter = new DormandPrince853Integrator(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  32:    */     
/*  33:    */ 
/*  34:125 */     this.nSteps = nSteps;
/*  35:    */     
/*  36:127 */     this.exp = (-1.0D / order);
/*  37:    */     
/*  38:    */ 
/*  39:130 */     setSafety(0.9D);
/*  40:131 */     setMinReduction(0.2D);
/*  41:132 */     setMaxGrowth(FastMath.pow(2.0D, -this.exp));
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected MultistepIntegrator(String name, int nSteps, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  45:    */   {
/*  46:160 */     super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  47:161 */     this.starter = new DormandPrince853Integrator(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  48:    */     
/*  49:    */ 
/*  50:164 */     this.nSteps = nSteps;
/*  51:    */     
/*  52:166 */     this.exp = (-1.0D / order);
/*  53:    */     
/*  54:    */ 
/*  55:169 */     setSafety(0.9D);
/*  56:170 */     setMinReduction(0.2D);
/*  57:171 */     setMaxGrowth(FastMath.pow(2.0D, -this.exp));
/*  58:    */   }
/*  59:    */   
/*  60:    */   public ODEIntegrator getStarterIntegrator()
/*  61:    */   {
/*  62:180 */     return this.starter;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setStarterIntegrator(FirstOrderIntegrator starterIntegrator)
/*  66:    */   {
/*  67:191 */     this.starter = starterIntegrator;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected void start(double t0, double[] y0, double t)
/*  71:    */     throws MathIllegalStateException
/*  72:    */   {
/*  73:215 */     this.starter.clearEventHandlers();
/*  74:216 */     this.starter.clearStepHandlers();
/*  75:    */     
/*  76:    */ 
/*  77:219 */     this.starter.addStepHandler(new NordsieckInitializer(this.nSteps, y0.length));
/*  78:    */     try
/*  79:    */     {
/*  80:223 */       this.starter.integrate(new CountingDifferentialEquations(y0.length), t0, y0, t, new double[y0.length]);
/*  81:    */     }
/*  82:    */     catch (InitializationCompletedMarkerException icme) {}
/*  83:230 */     this.starter.clearStepHandlers();
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected abstract Array2DRowRealMatrix initializeHighOrderDerivatives(double paramDouble, double[] paramArrayOfDouble, double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2);
/*  87:    */   
/*  88:    */   public double getMinReduction()
/*  89:    */   {
/*  90:250 */     return this.minReduction;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setMinReduction(double minReduction)
/*  94:    */   {
/*  95:257 */     this.minReduction = minReduction;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public double getMaxGrowth()
/*  99:    */   {
/* 100:264 */     return this.maxGrowth;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setMaxGrowth(double maxGrowth)
/* 104:    */   {
/* 105:271 */     this.maxGrowth = maxGrowth;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public double getSafety()
/* 109:    */   {
/* 110:278 */     return this.safety;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setSafety(double safety)
/* 114:    */   {
/* 115:285 */     this.safety = safety;
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected double computeStepGrowShrinkFactor(double error)
/* 119:    */   {
/* 120:293 */     return FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error, this.exp)));
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static abstract interface NordsieckTransformer
/* 124:    */   {
/* 125:    */     public abstract Array2DRowRealMatrix initializeHighOrderDerivatives(double paramDouble, double[] paramArrayOfDouble, double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2);
/* 126:    */   }
/* 127:    */   
/* 128:    */   private class NordsieckInitializer
/* 129:    */     implements StepHandler
/* 130:    */   {
/* 131:    */     private int count;
/* 132:    */     private final double[] t;
/* 133:    */     private final double[][] y;
/* 134:    */     private final double[][] yDot;
/* 135:    */     
/* 136:    */     public NordsieckInitializer(int nSteps, int n)
/* 137:    */     {
/* 138:331 */       this.count = 0;
/* 139:332 */       this.t = new double[nSteps];
/* 140:333 */       this.y = new double[nSteps][n];
/* 141:334 */       this.yDot = new double[nSteps][n];
/* 142:    */     }
/* 143:    */     
/* 144:    */     public void handleStep(StepInterpolator interpolator, boolean isLast)
/* 145:    */     {
/* 146:340 */       double prev = interpolator.getPreviousTime();
/* 147:341 */       double curr = interpolator.getCurrentTime();
/* 148:343 */       if (this.count == 0)
/* 149:    */       {
/* 150:345 */         interpolator.setInterpolatedTime(prev);
/* 151:346 */         this.t[0] = prev;
/* 152:347 */         System.arraycopy(interpolator.getInterpolatedState(), 0, this.y[0], 0, this.y[0].length);
/* 153:    */         
/* 154:349 */         System.arraycopy(interpolator.getInterpolatedDerivatives(), 0, this.yDot[0], 0, this.yDot[0].length);
/* 155:    */       }
/* 156:354 */       this.count += 1;
/* 157:355 */       interpolator.setInterpolatedTime(curr);
/* 158:356 */       this.t[this.count] = curr;
/* 159:357 */       System.arraycopy(interpolator.getInterpolatedState(), 0, this.y[this.count], 0, this.y[this.count].length);
/* 160:    */       
/* 161:359 */       System.arraycopy(interpolator.getInterpolatedDerivatives(), 0, this.yDot[this.count], 0, this.yDot[this.count].length);
/* 162:362 */       if (this.count == this.t.length - 1)
/* 163:    */       {
/* 164:365 */         MultistepIntegrator.this.stepStart = this.t[0];
/* 165:366 */         MultistepIntegrator.this.stepSize = ((this.t[(this.t.length - 1)] - this.t[0]) / (this.t.length - 1));
/* 166:    */         
/* 167:    */ 
/* 168:369 */         MultistepIntegrator.this.scaled = ((double[])this.yDot[0].clone());
/* 169:370 */         for (int j = 0; j < MultistepIntegrator.this.scaled.length; j++) {
/* 170:371 */           MultistepIntegrator.this.scaled[j] *= MultistepIntegrator.this.stepSize;
/* 171:    */         }
/* 172:375 */         MultistepIntegrator.this.nordsieck = MultistepIntegrator.this.initializeHighOrderDerivatives(MultistepIntegrator.this.stepSize, this.t, this.y, this.yDot);
/* 173:    */         
/* 174:    */ 
/* 175:378 */         throw new MultistepIntegrator.InitializationCompletedMarkerException();
/* 176:    */       }
/* 177:    */     }
/* 178:    */     
/* 179:    */     public void init(double t0, double[] y0, double time) {}
/* 180:    */   }
/* 181:    */   
/* 182:    */   private static class InitializationCompletedMarkerException
/* 183:    */     extends RuntimeException
/* 184:    */   {
/* 185:    */     private static final long serialVersionUID = -1914085471038046418L;
/* 186:    */     
/* 187:    */     public InitializationCompletedMarkerException()
/* 188:    */     {
/* 189:400 */       super();
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   private class CountingDifferentialEquations
/* 194:    */     implements FirstOrderDifferentialEquations
/* 195:    */   {
/* 196:    */     private final int dimension;
/* 197:    */     
/* 198:    */     public CountingDifferentialEquations(int dimension)
/* 199:    */     {
/* 200:415 */       this.dimension = dimension;
/* 201:    */     }
/* 202:    */     
/* 203:    */     public void computeDerivatives(double t, double[] y, double[] dot)
/* 204:    */     {
/* 205:420 */       MultistepIntegrator.this.computeDerivatives(t, y, dot);
/* 206:    */     }
/* 207:    */     
/* 208:    */     public int getDimension()
/* 209:    */     {
/* 210:425 */       return this.dimension;
/* 211:    */     }
/* 212:    */   }
/* 213:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.MultistepIntegrator
 * JD-Core Version:    0.7.0.1
 */
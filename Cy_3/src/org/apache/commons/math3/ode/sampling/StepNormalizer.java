/*   1:    */ package org.apache.commons.math3.ode.sampling;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ import org.apache.commons.math3.util.Precision;
/*   5:    */ 
/*   6:    */ public class StepNormalizer
/*   7:    */   implements StepHandler
/*   8:    */ {
/*   9:    */   private double h;
/*  10:    */   private final FixedStepHandler handler;
/*  11:    */   private double firstTime;
/*  12:    */   private double lastTime;
/*  13:    */   private double[] lastState;
/*  14:    */   private double[] lastDerivatives;
/*  15:    */   private boolean forward;
/*  16:    */   private final StepNormalizerBounds bounds;
/*  17:    */   private final StepNormalizerMode mode;
/*  18:    */   
/*  19:    */   public StepNormalizer(double h, FixedStepHandler handler)
/*  20:    */   {
/*  21:126 */     this(h, handler, StepNormalizerMode.INCREMENT, StepNormalizerBounds.FIRST);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public StepNormalizer(double h, FixedStepHandler handler, StepNormalizerMode mode)
/*  25:    */   {
/*  26:139 */     this(h, handler, mode, StepNormalizerBounds.FIRST);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public StepNormalizer(double h, FixedStepHandler handler, StepNormalizerBounds bounds)
/*  30:    */   {
/*  31:151 */     this(h, handler, StepNormalizerMode.INCREMENT, bounds);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public StepNormalizer(double h, FixedStepHandler handler, StepNormalizerMode mode, StepNormalizerBounds bounds)
/*  35:    */   {
/*  36:164 */     this.h = FastMath.abs(h);
/*  37:165 */     this.handler = handler;
/*  38:166 */     this.mode = mode;
/*  39:167 */     this.bounds = bounds;
/*  40:168 */     this.firstTime = (0.0D / 0.0D);
/*  41:169 */     this.lastTime = (0.0D / 0.0D);
/*  42:170 */     this.lastState = null;
/*  43:171 */     this.lastDerivatives = null;
/*  44:172 */     this.forward = true;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void init(double t0, double[] y0, double t)
/*  48:    */   {
/*  49:178 */     this.firstTime = (0.0D / 0.0D);
/*  50:179 */     this.lastTime = (0.0D / 0.0D);
/*  51:180 */     this.lastState = null;
/*  52:181 */     this.lastDerivatives = null;
/*  53:182 */     this.forward = true;
/*  54:    */     
/*  55:    */ 
/*  56:185 */     this.handler.init(t0, y0, t);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void handleStep(StepInterpolator interpolator, boolean isLast)
/*  60:    */   {
/*  61:202 */     if (this.lastState == null)
/*  62:    */     {
/*  63:203 */       this.firstTime = interpolator.getPreviousTime();
/*  64:204 */       this.lastTime = interpolator.getPreviousTime();
/*  65:205 */       interpolator.setInterpolatedTime(this.lastTime);
/*  66:206 */       this.lastState = ((double[])interpolator.getInterpolatedState().clone());
/*  67:207 */       this.lastDerivatives = ((double[])interpolator.getInterpolatedDerivatives().clone());
/*  68:    */       
/*  69:    */ 
/*  70:210 */       this.forward = (interpolator.getCurrentTime() >= this.lastTime);
/*  71:211 */       if (!this.forward) {
/*  72:212 */         this.h = (-this.h);
/*  73:    */       }
/*  74:    */     }
/*  75:217 */     double nextTime = this.mode == StepNormalizerMode.INCREMENT ? this.lastTime + this.h : (FastMath.floor(this.lastTime / this.h) + 1.0D) * this.h;
/*  76:220 */     if ((this.mode == StepNormalizerMode.MULTIPLES) && (Precision.equals(nextTime, this.lastTime, 1))) {
/*  77:222 */       nextTime += this.h;
/*  78:    */     }
/*  79:226 */     boolean nextInStep = isNextInStep(nextTime, interpolator);
/*  80:227 */     while (nextInStep)
/*  81:    */     {
/*  82:229 */       doNormalizedStep(false);
/*  83:    */       
/*  84:    */ 
/*  85:232 */       storeStep(interpolator, nextTime);
/*  86:    */       
/*  87:    */ 
/*  88:235 */       nextTime += this.h;
/*  89:236 */       nextInStep = isNextInStep(nextTime, interpolator);
/*  90:    */     }
/*  91:239 */     if (isLast)
/*  92:    */     {
/*  93:243 */       boolean addLast = (this.bounds.lastIncluded()) && (this.lastTime != interpolator.getCurrentTime());
/*  94:    */       
/*  95:245 */       doNormalizedStep(!addLast);
/*  96:246 */       if (addLast)
/*  97:    */       {
/*  98:247 */         storeStep(interpolator, interpolator.getCurrentTime());
/*  99:248 */         doNormalizedStep(true);
/* 100:    */       }
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   private boolean isNextInStep(double nextTime, StepInterpolator interpolator)
/* 105:    */   {
/* 106:264 */     return nextTime <= interpolator.getCurrentTime();
/* 107:    */   }
/* 108:    */   
/* 109:    */   private void doNormalizedStep(boolean isLast)
/* 110:    */   {
/* 111:274 */     if ((!this.bounds.firstIncluded()) && (this.firstTime == this.lastTime)) {
/* 112:275 */       return;
/* 113:    */     }
/* 114:277 */     this.handler.handleStep(this.lastTime, this.lastState, this.lastDerivatives, isLast);
/* 115:    */   }
/* 116:    */   
/* 117:    */   private void storeStep(StepInterpolator interpolator, double t)
/* 118:    */   {
/* 119:287 */     this.lastTime = t;
/* 120:288 */     interpolator.setInterpolatedTime(this.lastTime);
/* 121:289 */     System.arraycopy(interpolator.getInterpolatedState(), 0, this.lastState, 0, this.lastState.length);
/* 122:    */     
/* 123:291 */     System.arraycopy(interpolator.getInterpolatedDerivatives(), 0, this.lastDerivatives, 0, this.lastDerivatives.length);
/* 124:    */   }
/* 125:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.sampling.StepNormalizer
 * JD-Core Version:    0.7.0.1
 */
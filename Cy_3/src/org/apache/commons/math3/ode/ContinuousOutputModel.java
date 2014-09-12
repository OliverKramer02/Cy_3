/*   1:    */ package org.apache.commons.math3.ode;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.ode.sampling.StepHandler;
/*  10:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public class ContinuousOutputModel
/*  14:    */   implements StepHandler, Serializable
/*  15:    */ {
/*  16:    */   private static final long serialVersionUID = -1417964919405031606L;
/*  17:    */   private double initialTime;
/*  18:    */   private double finalTime;
/*  19:    */   private boolean forward;
/*  20:    */   private int index;
/*  21:    */   private List<StepInterpolator> steps;
/*  22:    */   
/*  23:    */   public ContinuousOutputModel()
/*  24:    */   {
/*  25:114 */     this.steps = new ArrayList();
/*  26:115 */     this.initialTime = (0.0D / 0.0D);
/*  27:116 */     this.finalTime = (0.0D / 0.0D);
/*  28:117 */     this.forward = true;
/*  29:118 */     this.index = 0;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void append(ContinuousOutputModel model)
/*  33:    */     throws MathIllegalArgumentException
/*  34:    */   {
/*  35:130 */     if (model.steps.size() == 0) {
/*  36:131 */       return;
/*  37:    */     }
/*  38:134 */     if (this.steps.size() == 0)
/*  39:    */     {
/*  40:135 */       this.initialTime = model.initialTime;
/*  41:136 */       this.forward = model.forward;
/*  42:    */     }
/*  43:    */     else
/*  44:    */     {
/*  45:139 */       if (getInterpolatedState().length != model.getInterpolatedState().length) {
/*  46:140 */         throw new DimensionMismatchException(model.getInterpolatedState().length, getInterpolatedState().length);
/*  47:    */       }
/*  48:144 */       if ((this.forward ^ model.forward)) {
/*  49:145 */         throw new MathIllegalArgumentException(LocalizedFormats.PROPAGATION_DIRECTION_MISMATCH, new Object[0]);
/*  50:    */       }
/*  51:148 */       StepInterpolator lastInterpolator = (StepInterpolator)this.steps.get(this.index);
/*  52:149 */       double current = lastInterpolator.getCurrentTime();
/*  53:150 */       double previous = lastInterpolator.getPreviousTime();
/*  54:151 */       double step = current - previous;
/*  55:152 */       double gap = model.getInitialTime() - current;
/*  56:153 */       if (FastMath.abs(gap) > 0.001D * FastMath.abs(step)) {
/*  57:154 */         throw new MathIllegalArgumentException(LocalizedFormats.HOLE_BETWEEN_MODELS_TIME_RANGES, new Object[] { Double.valueOf(FastMath.abs(gap)) });
/*  58:    */       }
/*  59:    */     }
/*  60:160 */     for (StepInterpolator interpolator : model.steps) {
/*  61:161 */       this.steps.add(interpolator.copy());
/*  62:    */     }
/*  63:164 */     this.index = (this.steps.size() - 1);
/*  64:165 */     this.finalTime = ((StepInterpolator)this.steps.get(this.index)).getCurrentTime();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void init(double t0, double[] y0, double t)
/*  68:    */   {
/*  69:171 */     this.initialTime = (0.0D / 0.0D);
/*  70:172 */     this.finalTime = (0.0D / 0.0D);
/*  71:173 */     this.forward = true;
/*  72:174 */     this.index = 0;
/*  73:175 */     this.steps.clear();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void handleStep(StepInterpolator interpolator, boolean isLast)
/*  77:    */   {
/*  78:186 */     if (this.steps.size() == 0)
/*  79:    */     {
/*  80:187 */       this.initialTime = interpolator.getPreviousTime();
/*  81:188 */       this.forward = interpolator.isForward();
/*  82:    */     }
/*  83:191 */     this.steps.add(interpolator.copy());
/*  84:193 */     if (isLast)
/*  85:    */     {
/*  86:194 */       this.finalTime = interpolator.getCurrentTime();
/*  87:195 */       this.index = (this.steps.size() - 1);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public double getInitialTime()
/*  92:    */   {
/*  93:205 */     return this.initialTime;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public double getFinalTime()
/*  97:    */   {
/*  98:213 */     return this.finalTime;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public double getInterpolatedTime()
/* 102:    */   {
/* 103:223 */     return ((StepInterpolator)this.steps.get(this.index)).getInterpolatedTime();
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setInterpolatedTime(double time)
/* 107:    */   {
/* 108:241 */     int iMin = 0;
/* 109:242 */     StepInterpolator sMin = (StepInterpolator)this.steps.get(iMin);
/* 110:243 */     double tMin = 0.5D * (sMin.getPreviousTime() + sMin.getCurrentTime());
/* 111:    */     
/* 112:245 */     int iMax = this.steps.size() - 1;
/* 113:246 */     StepInterpolator sMax = (StepInterpolator)this.steps.get(iMax);
/* 114:247 */     double tMax = 0.5D * (sMax.getPreviousTime() + sMax.getCurrentTime());
/* 115:251 */     if (locatePoint(time, sMin) <= 0)
/* 116:    */     {
/* 117:252 */       this.index = iMin;
/* 118:253 */       sMin.setInterpolatedTime(time);
/* 119:254 */       return;
/* 120:    */     }
/* 121:256 */     if (locatePoint(time, sMax) >= 0)
/* 122:    */     {
/* 123:257 */       this.index = iMax;
/* 124:258 */       sMax.setInterpolatedTime(time);
/* 125:259 */       return;
/* 126:    */     }
/* 127:263 */     while (iMax - iMin > 5)
/* 128:    */     {
/* 129:266 */       StepInterpolator si = (StepInterpolator)this.steps.get(this.index);
/* 130:267 */       int location = locatePoint(time, si);
/* 131:268 */       if (location < 0)
/* 132:    */       {
/* 133:269 */         iMax = this.index;
/* 134:270 */         tMax = 0.5D * (si.getPreviousTime() + si.getCurrentTime());
/* 135:    */       }
/* 136:271 */       else if (location > 0)
/* 137:    */       {
/* 138:272 */         iMin = this.index;
/* 139:273 */         tMin = 0.5D * (si.getPreviousTime() + si.getCurrentTime());
/* 140:    */       }
/* 141:    */       else
/* 142:    */       {
/* 143:276 */         si.setInterpolatedTime(time);
/* 144:277 */         return;
/* 145:    */       }
/* 146:281 */       int iMed = (iMin + iMax) / 2;
/* 147:282 */       StepInterpolator sMed = (StepInterpolator)this.steps.get(iMed);
/* 148:283 */       double tMed = 0.5D * (sMed.getPreviousTime() + sMed.getCurrentTime());
/* 149:285 */       if ((FastMath.abs(tMed - tMin) < 1.0E-006D) || (FastMath.abs(tMax - tMed) < 1.0E-006D))
/* 150:    */       {
/* 151:287 */         this.index = iMed;
/* 152:    */       }
/* 153:    */       else
/* 154:    */       {
/* 155:292 */         double d12 = tMax - tMed;
/* 156:293 */         double d23 = tMed - tMin;
/* 157:294 */         double d13 = tMax - tMin;
/* 158:295 */         double dt1 = time - tMax;
/* 159:296 */         double dt2 = time - tMed;
/* 160:297 */         double dt3 = time - tMin;
/* 161:298 */         double iLagrange = (dt2 * dt3 * d23 * iMax - dt1 * dt3 * d13 * iMed + dt1 * dt2 * d12 * iMin) / (d12 * d23 * d13);
/* 162:    */         
/* 163:    */ 
/* 164:    */ 
/* 165:302 */         this.index = ((int)FastMath.rint(iLagrange));
/* 166:    */       }
/* 167:306 */       int low = FastMath.max(iMin + 1, (9 * iMin + iMax) / 10);
/* 168:307 */       int high = FastMath.min(iMax - 1, (iMin + 9 * iMax) / 10);
/* 169:308 */       if (this.index < low) {
/* 170:309 */         this.index = low;
/* 171:310 */       } else if (this.index > high) {
/* 172:311 */         this.index = high;
/* 173:    */       }
/* 174:    */     }
/* 175:317 */     this.index = iMin;
/* 176:318 */     while ((this.index <= iMax) && (locatePoint(time, (StepInterpolator)this.steps.get(this.index)) > 0)) {
/* 177:319 */       this.index += 1;
/* 178:    */     }
/* 179:322 */     ((StepInterpolator)this.steps.get(this.index)).setInterpolatedTime(time);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public double[] getInterpolatedState()
/* 183:    */   {
/* 184:331 */     return ((StepInterpolator)this.steps.get(this.index)).getInterpolatedState();
/* 185:    */   }
/* 186:    */   
/* 187:    */   private int locatePoint(double time, StepInterpolator interval)
/* 188:    */   {
/* 189:342 */     if (this.forward)
/* 190:    */     {
/* 191:343 */       if (time < interval.getPreviousTime()) {
/* 192:344 */         return -1;
/* 193:    */       }
/* 194:345 */       if (time > interval.getCurrentTime()) {
/* 195:346 */         return 1;
/* 196:    */       }
/* 197:348 */       return 0;
/* 198:    */     }
/* 199:351 */     if (time > interval.getPreviousTime()) {
/* 200:352 */       return -1;
/* 201:    */     }
/* 202:353 */     if (time < interval.getCurrentTime()) {
/* 203:354 */       return 1;
/* 204:    */     }
/* 205:356 */     return 0;
/* 206:    */   }
/* 207:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ContinuousOutputModel
 * JD-Core Version:    0.7.0.1
 */
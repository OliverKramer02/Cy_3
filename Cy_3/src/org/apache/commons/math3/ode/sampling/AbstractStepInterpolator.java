/*   1:    */ package org.apache.commons.math3.ode.sampling;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInput;
/*   5:    */ import java.io.ObjectOutput;
/*   6:    */ import org.apache.commons.math3.ode.EquationsMapper;
/*   7:    */ 
/*   8:    */ public abstract class AbstractStepInterpolator
/*   9:    */   implements StepInterpolator
/*  10:    */ {
/*  11:    */   protected double h;
/*  12:    */   protected double[] currentState;
/*  13:    */   protected double interpolatedTime;
/*  14:    */   protected double[] interpolatedState;
/*  15:    */   protected double[] interpolatedDerivatives;
/*  16:    */   protected double[] interpolatedPrimaryState;
/*  17:    */   protected double[] interpolatedPrimaryDerivatives;
/*  18:    */   protected double[][] interpolatedSecondaryState;
/*  19:    */   protected double[][] interpolatedSecondaryDerivatives;
/*  20:    */   private double globalPreviousTime;
/*  21:    */   private double globalCurrentTime;
/*  22:    */   private double softPreviousTime;
/*  23:    */   private double softCurrentTime;
/*  24:    */   private boolean finalized;
/*  25:    */   private boolean forward;
/*  26:    */   private boolean dirtyState;
/*  27:    */   private EquationsMapper primaryMapper;
/*  28:    */   private EquationsMapper[] secondaryMappers;
/*  29:    */   
/*  30:    */   protected AbstractStepInterpolator()
/*  31:    */   {
/*  32:112 */     this.globalPreviousTime = (0.0D / 0.0D);
/*  33:113 */     this.globalCurrentTime = (0.0D / 0.0D);
/*  34:114 */     this.softPreviousTime = (0.0D / 0.0D);
/*  35:115 */     this.softCurrentTime = (0.0D / 0.0D);
/*  36:116 */     this.h = (0.0D / 0.0D);
/*  37:117 */     this.interpolatedTime = (0.0D / 0.0D);
/*  38:118 */     this.currentState = null;
/*  39:119 */     this.finalized = false;
/*  40:120 */     this.forward = true;
/*  41:121 */     this.dirtyState = true;
/*  42:122 */     this.primaryMapper = null;
/*  43:123 */     this.secondaryMappers = null;
/*  44:124 */     allocateInterpolatedArrays(-1);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected AbstractStepInterpolator(double[] y, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers)
/*  48:    */   {
/*  49:138 */     this.globalPreviousTime = (0.0D / 0.0D);
/*  50:139 */     this.globalCurrentTime = (0.0D / 0.0D);
/*  51:140 */     this.softPreviousTime = (0.0D / 0.0D);
/*  52:141 */     this.softCurrentTime = (0.0D / 0.0D);
/*  53:142 */     this.h = (0.0D / 0.0D);
/*  54:143 */     this.interpolatedTime = (0.0D / 0.0D);
/*  55:144 */     this.currentState = y;
/*  56:145 */     this.finalized = false;
/*  57:146 */     this.forward = forward;
/*  58:147 */     this.dirtyState = true;
/*  59:148 */     this.primaryMapper = primaryMapper;
/*  60:149 */     this.secondaryMappers = (secondaryMappers == null ? null : (EquationsMapper[])secondaryMappers.clone());
/*  61:150 */     allocateInterpolatedArrays(y.length);
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected AbstractStepInterpolator(AbstractStepInterpolator interpolator)
/*  65:    */   {
/*  66:173 */     this.globalPreviousTime = interpolator.globalPreviousTime;
/*  67:174 */     this.globalCurrentTime = interpolator.globalCurrentTime;
/*  68:175 */     this.softPreviousTime = interpolator.softPreviousTime;
/*  69:176 */     this.softCurrentTime = interpolator.softCurrentTime;
/*  70:177 */     this.h = interpolator.h;
/*  71:178 */     this.interpolatedTime = interpolator.interpolatedTime;
/*  72:180 */     if (interpolator.currentState == null)
/*  73:    */     {
/*  74:181 */       this.currentState = null;
/*  75:182 */       this.primaryMapper = null;
/*  76:183 */       this.secondaryMappers = null;
/*  77:184 */       allocateInterpolatedArrays(-1);
/*  78:    */     }
/*  79:    */     else
/*  80:    */     {
/*  81:186 */       this.currentState = ((double[])interpolator.currentState.clone());
/*  82:187 */       this.interpolatedState = ((double[])interpolator.interpolatedState.clone());
/*  83:188 */       this.interpolatedDerivatives = ((double[])interpolator.interpolatedDerivatives.clone());
/*  84:189 */       this.interpolatedPrimaryState = ((double[])interpolator.interpolatedPrimaryState.clone());
/*  85:190 */       this.interpolatedPrimaryDerivatives = ((double[])interpolator.interpolatedPrimaryDerivatives.clone());
/*  86:191 */       this.interpolatedSecondaryState = new double[interpolator.interpolatedSecondaryState.length][];
/*  87:192 */       this.interpolatedSecondaryDerivatives = new double[interpolator.interpolatedSecondaryDerivatives.length][];
/*  88:193 */       for (int i = 0; i < this.interpolatedSecondaryState.length; i++)
/*  89:    */       {
/*  90:194 */         this.interpolatedSecondaryState[i] = ((double[])interpolator.interpolatedSecondaryState[i].clone());
/*  91:195 */         this.interpolatedSecondaryDerivatives[i] = ((double[])interpolator.interpolatedSecondaryDerivatives[i].clone());
/*  92:    */       }
/*  93:    */     }
/*  94:199 */     this.finalized = interpolator.finalized;
/*  95:200 */     this.forward = interpolator.forward;
/*  96:201 */     this.dirtyState = interpolator.dirtyState;
/*  97:202 */     this.primaryMapper = interpolator.primaryMapper;
/*  98:203 */     this.secondaryMappers = (interpolator.secondaryMappers == null ? null : (EquationsMapper[])interpolator.secondaryMappers.clone());
/*  99:    */   }
/* 100:    */   
/* 101:    */   private void allocateInterpolatedArrays(int dimension)
/* 102:    */   {
/* 103:212 */     if (dimension < 0)
/* 104:    */     {
/* 105:213 */       this.interpolatedState = null;
/* 106:214 */       this.interpolatedDerivatives = null;
/* 107:215 */       this.interpolatedPrimaryState = null;
/* 108:216 */       this.interpolatedPrimaryDerivatives = null;
/* 109:217 */       this.interpolatedSecondaryState = ((double[][])null);
/* 110:218 */       this.interpolatedSecondaryDerivatives = ((double[][])null);
/* 111:    */     }
/* 112:    */     else
/* 113:    */     {
/* 114:220 */       this.interpolatedState = new double[dimension];
/* 115:221 */       this.interpolatedDerivatives = new double[dimension];
/* 116:222 */       this.interpolatedPrimaryState = new double[this.primaryMapper.getDimension()];
/* 117:223 */       this.interpolatedPrimaryDerivatives = new double[this.primaryMapper.getDimension()];
/* 118:224 */       if (this.secondaryMappers == null)
/* 119:    */       {
/* 120:225 */         this.interpolatedSecondaryState = ((double[][])null);
/* 121:226 */         this.interpolatedSecondaryDerivatives = ((double[][])null);
/* 122:    */       }
/* 123:    */       else
/* 124:    */       {
/* 125:228 */         this.interpolatedSecondaryState = new double[this.secondaryMappers.length][];
/* 126:229 */         this.interpolatedSecondaryDerivatives = new double[this.secondaryMappers.length][];
/* 127:230 */         for (int i = 0; i < this.secondaryMappers.length; i++)
/* 128:    */         {
/* 129:231 */           this.interpolatedSecondaryState[i] = new double[this.secondaryMappers[i].getDimension()];
/* 130:232 */           this.interpolatedSecondaryDerivatives[i] = new double[this.secondaryMappers[i].getDimension()];
/* 131:    */         }
/* 132:    */       }
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   protected void reinitialize(double[] y, boolean isForward, EquationsMapper primary, EquationsMapper[] secondary)
/* 137:    */   {
/* 138:248 */     this.globalPreviousTime = (0.0D / 0.0D);
/* 139:249 */     this.globalCurrentTime = (0.0D / 0.0D);
/* 140:250 */     this.softPreviousTime = (0.0D / 0.0D);
/* 141:251 */     this.softCurrentTime = (0.0D / 0.0D);
/* 142:252 */     this.h = (0.0D / 0.0D);
/* 143:253 */     this.interpolatedTime = (0.0D / 0.0D);
/* 144:254 */     this.currentState = y;
/* 145:255 */     this.finalized = false;
/* 146:256 */     this.forward = isForward;
/* 147:257 */     this.dirtyState = true;
/* 148:258 */     this.primaryMapper = primary;
/* 149:259 */     this.secondaryMappers = ((EquationsMapper[])secondary.clone());
/* 150:260 */     allocateInterpolatedArrays(y.length);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public StepInterpolator copy()
/* 154:    */   {
/* 155:268 */     finalizeStep();
/* 156:    */     
/* 157:    */ 
/* 158:271 */     return doCopy();
/* 159:    */   }
/* 160:    */   
/* 161:    */   protected abstract StepInterpolator doCopy();
/* 162:    */   
/* 163:    */   public void shift()
/* 164:    */   {
/* 165:289 */     this.globalPreviousTime = this.globalCurrentTime;
/* 166:290 */     this.softPreviousTime = this.globalPreviousTime;
/* 167:291 */     this.softCurrentTime = this.globalCurrentTime;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void storeTime(double t)
/* 171:    */   {
/* 172:299 */     this.globalCurrentTime = t;
/* 173:300 */     this.softCurrentTime = this.globalCurrentTime;
/* 174:301 */     this.h = (this.globalCurrentTime - this.globalPreviousTime);
/* 175:302 */     setInterpolatedTime(t);
/* 176:    */     
/* 177:    */ 
/* 178:305 */     this.finalized = false;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void setSoftPreviousTime(double softPreviousTime)
/* 182:    */   {
/* 183:320 */     this.softPreviousTime = softPreviousTime;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setSoftCurrentTime(double softCurrentTime)
/* 187:    */   {
/* 188:334 */     this.softCurrentTime = softCurrentTime;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public double getGlobalPreviousTime()
/* 192:    */   {
/* 193:342 */     return this.globalPreviousTime;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public double getGlobalCurrentTime()
/* 197:    */   {
/* 198:350 */     return this.globalCurrentTime;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public double getPreviousTime()
/* 202:    */   {
/* 203:359 */     return this.softPreviousTime;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public double getCurrentTime()
/* 207:    */   {
/* 208:368 */     return this.softCurrentTime;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public double getInterpolatedTime()
/* 212:    */   {
/* 213:373 */     return this.interpolatedTime;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setInterpolatedTime(double time)
/* 217:    */   {
/* 218:378 */     this.interpolatedTime = time;
/* 219:379 */     this.dirtyState = true;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public boolean isForward()
/* 223:    */   {
/* 224:384 */     return this.forward;
/* 225:    */   }
/* 226:    */   
/* 227:    */   protected abstract void computeInterpolatedStateAndDerivatives(double paramDouble1, double paramDouble2);
/* 228:    */   
/* 229:    */   private void evaluateCompleteInterpolatedState()
/* 230:    */   {
/* 231:402 */     if (this.dirtyState)
/* 232:    */     {
/* 233:403 */       double oneMinusThetaH = this.globalCurrentTime - this.interpolatedTime;
/* 234:404 */       double theta = this.h == 0.0D ? 0.0D : (this.h - oneMinusThetaH) / this.h;
/* 235:405 */       computeInterpolatedStateAndDerivatives(theta, oneMinusThetaH);
/* 236:406 */       this.dirtyState = false;
/* 237:    */     }
/* 238:    */   }
/* 239:    */   
/* 240:    */   public double[] getInterpolatedState()
/* 241:    */   {
/* 242:412 */     evaluateCompleteInterpolatedState();
/* 243:413 */     this.primaryMapper.extractEquationData(this.interpolatedState, this.interpolatedPrimaryState);
/* 244:    */     
/* 245:415 */     return this.interpolatedPrimaryState;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public double[] getInterpolatedDerivatives()
/* 249:    */   {
/* 250:420 */     evaluateCompleteInterpolatedState();
/* 251:421 */     this.primaryMapper.extractEquationData(this.interpolatedDerivatives, this.interpolatedPrimaryDerivatives);
/* 252:    */     
/* 253:423 */     return this.interpolatedPrimaryDerivatives;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public double[] getInterpolatedSecondaryState(int index)
/* 257:    */   {
/* 258:428 */     evaluateCompleteInterpolatedState();
/* 259:429 */     this.secondaryMappers[index].extractEquationData(this.interpolatedState, this.interpolatedSecondaryState[index]);
/* 260:    */     
/* 261:431 */     return this.interpolatedSecondaryState[index];
/* 262:    */   }
/* 263:    */   
/* 264:    */   public double[] getInterpolatedSecondaryDerivatives(int index)
/* 265:    */   {
/* 266:436 */     evaluateCompleteInterpolatedState();
/* 267:437 */     this.secondaryMappers[index].extractEquationData(this.interpolatedDerivatives, this.interpolatedSecondaryDerivatives[index]);
/* 268:    */     
/* 269:439 */     return this.interpolatedSecondaryDerivatives[index];
/* 270:    */   }
/* 271:    */   
/* 272:    */   public final void finalizeStep()
/* 273:    */   {
/* 274:482 */     if (!this.finalized)
/* 275:    */     {
/* 276:483 */       doFinalize();
/* 277:484 */       this.finalized = true;
/* 278:    */     }
/* 279:    */   }
/* 280:    */   
/* 281:    */   protected void doFinalize() {}
/* 282:    */   
/* 283:    */   public abstract void writeExternal(ObjectOutput paramObjectOutput)
/* 284:    */     throws IOException;
/* 285:    */   
/* 286:    */   public abstract void readExternal(ObjectInput paramObjectInput)
/* 287:    */     throws IOException, ClassNotFoundException;
/* 288:    */   
/* 289:    */   protected void writeBaseExternal(ObjectOutput out)
/* 290:    */     throws IOException
/* 291:    */   {
/* 292:512 */     if (this.currentState == null) {
/* 293:513 */       out.writeInt(-1);
/* 294:    */     } else {
/* 295:515 */       out.writeInt(this.currentState.length);
/* 296:    */     }
/* 297:517 */     out.writeDouble(this.globalPreviousTime);
/* 298:518 */     out.writeDouble(this.globalCurrentTime);
/* 299:519 */     out.writeDouble(this.softPreviousTime);
/* 300:520 */     out.writeDouble(this.softCurrentTime);
/* 301:521 */     out.writeDouble(this.h);
/* 302:522 */     out.writeBoolean(this.forward);
/* 303:523 */     out.writeObject(this.primaryMapper);
/* 304:524 */     out.write(this.secondaryMappers.length);
/* 305:525 */     for (EquationsMapper mapper : this.secondaryMappers) {
/* 306:526 */       out.writeObject(mapper);
/* 307:    */     }
/* 308:529 */     if (this.currentState != null) {
/* 309:530 */       for (int i = 0; i < this.currentState.length; i++) {
/* 310:531 */         out.writeDouble(this.currentState[i]);
/* 311:    */       }
/* 312:    */     }
/* 313:535 */     out.writeDouble(this.interpolatedTime);
/* 314:    */     
/* 315:    */ 
/* 316:    */ 
/* 317:    */ 
/* 318:    */ 
/* 319:541 */     finalizeStep();
/* 320:    */   }
/* 321:    */   
/* 322:    */   protected double readBaseExternal(ObjectInput in)
/* 323:    */     throws IOException, ClassNotFoundException
/* 324:    */   {
/* 325:559 */     int dimension = in.readInt();
/* 326:560 */     this.globalPreviousTime = in.readDouble();
/* 327:561 */     this.globalCurrentTime = in.readDouble();
/* 328:562 */     this.softPreviousTime = in.readDouble();
/* 329:563 */     this.softCurrentTime = in.readDouble();
/* 330:564 */     this.h = in.readDouble();
/* 331:565 */     this.forward = in.readBoolean();
/* 332:566 */     this.primaryMapper = ((EquationsMapper)in.readObject());
/* 333:567 */     this.secondaryMappers = new EquationsMapper[in.read()];
/* 334:568 */     for (int i = 0; i < this.secondaryMappers.length; i++) {
/* 335:569 */       this.secondaryMappers[i] = ((EquationsMapper)in.readObject());
/* 336:    */     }
/* 337:571 */     this.dirtyState = true;
/* 338:573 */     if (dimension < 0)
/* 339:    */     {
/* 340:574 */       this.currentState = null;
/* 341:    */     }
/* 342:    */     else
/* 343:    */     {
/* 344:576 */       this.currentState = new double[dimension];
/* 345:577 */       for (int i = 0; i < this.currentState.length; i++) {
/* 346:578 */         this.currentState[i] = in.readDouble();
/* 347:    */       }
/* 348:    */     }
/* 349:583 */     this.interpolatedTime = (0.0D / 0.0D);
/* 350:584 */     allocateInterpolatedArrays(dimension);
/* 351:    */     
/* 352:586 */     this.finalized = true;
/* 353:    */     
/* 354:588 */     return in.readDouble();
/* 355:    */   }
/* 356:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
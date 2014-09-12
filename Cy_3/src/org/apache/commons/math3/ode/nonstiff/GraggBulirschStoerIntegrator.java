/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   6:    */ import org.apache.commons.math3.ode.ExpandableStatefulODE;
/*   7:    */ import org.apache.commons.math3.ode.events.EventHandler;
/*   8:    */ import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
/*   9:    */ import org.apache.commons.math3.ode.sampling.StepHandler;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*  11:    */ 
/*  12:    */ public class GraggBulirschStoerIntegrator
/*  13:    */   extends AdaptiveStepsizeIntegrator
/*  14:    */ {
/*  15:    */   private static final String METHOD_NAME = "Gragg-Bulirsch-Stoer";
/*  16:    */   private int maxOrder;
/*  17:    */   private int[] sequence;
/*  18:    */   private int[] costPerStep;
/*  19:    */   private double[] costPerTimeUnit;
/*  20:    */   private double[] optimalStep;
/*  21:    */   private double[][] coeff;
/*  22:    */   private boolean performTest;
/*  23:    */   private int maxChecks;
/*  24:    */   private int maxIter;
/*  25:    */   private double stabilityReduction;
/*  26:    */   private double stepControl1;
/*  27:    */   private double stepControl2;
/*  28:    */   private double stepControl3;
/*  29:    */   private double stepControl4;
/*  30:    */   private double orderControl1;
/*  31:    */   private double orderControl2;
/*  32:    */   private boolean useInterpolationError;
/*  33:    */   private int mudif;
/*  34:    */   
/*  35:    */   public GraggBulirschStoerIntegrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance)
/*  36:    */   {
/*  37:170 */     super("Gragg-Bulirsch-Stoer", minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
/*  38:    */     
/*  39:172 */     setStabilityCheck(true, -1, -1, -1.0D);
/*  40:173 */     setControlFactors(-1.0D, -1.0D, -1.0D, -1.0D);
/*  41:174 */     setOrderControl(-1, -1.0D, -1.0D);
/*  42:175 */     setInterpolationControl(true, -1);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public GraggBulirschStoerIntegrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance)
/*  46:    */   {
/*  47:192 */     super("Gragg-Bulirsch-Stoer", minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
/*  48:    */     
/*  49:194 */     setStabilityCheck(true, -1, -1, -1.0D);
/*  50:195 */     setControlFactors(-1.0D, -1.0D, -1.0D, -1.0D);
/*  51:196 */     setOrderControl(-1, -1.0D, -1.0D);
/*  52:197 */     setInterpolationControl(true, -1);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setStabilityCheck(boolean performStabilityCheck, int maxNumIter, int maxNumChecks, double stepsizeReductionFactor)
/*  56:    */   {
/*  57:222 */     this.performTest = performStabilityCheck;
/*  58:223 */     this.maxIter = (maxNumIter <= 0 ? 2 : maxNumIter);
/*  59:224 */     this.maxChecks = (maxNumChecks <= 0 ? 1 : maxNumChecks);
/*  60:226 */     if ((stepsizeReductionFactor < 0.0001D) || (stepsizeReductionFactor > 0.9999D)) {
/*  61:227 */       this.stabilityReduction = 0.5D;
/*  62:    */     } else {
/*  63:229 */       this.stabilityReduction = stepsizeReductionFactor;
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setControlFactors(double control1, double control2, double control3, double control4)
/*  68:    */   {
/*  69:261 */     if ((control1 < 0.0001D) || (control1 > 0.9999D)) {
/*  70:262 */       this.stepControl1 = 0.65D;
/*  71:    */     } else {
/*  72:264 */       this.stepControl1 = control1;
/*  73:    */     }
/*  74:267 */     if ((control2 < 0.0001D) || (control2 > 0.9999D)) {
/*  75:268 */       this.stepControl2 = 0.94D;
/*  76:    */     } else {
/*  77:270 */       this.stepControl2 = control2;
/*  78:    */     }
/*  79:273 */     if ((control3 < 0.0001D) || (control3 > 0.9999D)) {
/*  80:274 */       this.stepControl3 = 0.02D;
/*  81:    */     } else {
/*  82:276 */       this.stepControl3 = control3;
/*  83:    */     }
/*  84:279 */     if ((control4 < 1.0001D) || (control4 > 999.89999999999998D)) {
/*  85:280 */       this.stepControl4 = 4.0D;
/*  86:    */     } else {
/*  87:282 */       this.stepControl4 = control4;
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setOrderControl(int maximalOrder, double control1, double control2)
/*  92:    */   {
/*  93:313 */     if ((maximalOrder <= 6) || (maximalOrder % 2 != 0)) {
/*  94:314 */       this.maxOrder = 18;
/*  95:    */     }
/*  96:317 */     if ((control1 < 0.0001D) || (control1 > 0.9999D)) {
/*  97:318 */       this.orderControl1 = 0.8D;
/*  98:    */     } else {
/*  99:320 */       this.orderControl1 = control1;
/* 100:    */     }
/* 101:323 */     if ((control2 < 0.0001D) || (control2 > 0.9999D)) {
/* 102:324 */       this.orderControl2 = 0.9D;
/* 103:    */     } else {
/* 104:326 */       this.orderControl2 = control2;
/* 105:    */     }
/* 106:330 */     initializeArrays();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void addStepHandler(StepHandler handler)
/* 110:    */   {
/* 111:338 */     super.addStepHandler(handler);
/* 112:    */     
/* 113:    */ 
/* 114:341 */     initializeArrays();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void addEventHandler(EventHandler function, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver)
/* 118:    */   {
/* 119:352 */     super.addEventHandler(function, maxCheckInterval, convergence, maxIterationCount, solver);
/* 120:    */     
/* 121:    */ 
/* 122:    */ 
/* 123:356 */     initializeArrays();
/* 124:    */   }
/* 125:    */   
/* 126:    */   private void initializeArrays()
/* 127:    */   {
/* 128:363 */     int size = this.maxOrder / 2;
/* 129:365 */     if ((this.sequence == null) || (this.sequence.length != size))
/* 130:    */     {
/* 131:367 */       this.sequence = new int[size];
/* 132:368 */       this.costPerStep = new int[size];
/* 133:369 */       this.coeff = new double[size][];
/* 134:370 */       this.costPerTimeUnit = new double[size];
/* 135:371 */       this.optimalStep = new double[size];
/* 136:    */     }
/* 137:375 */     for (int k = 0; k < size; k++) {
/* 138:376 */       this.sequence[k] = (4 * k + 2);
/* 139:    */     }
/* 140:381 */     this.costPerStep[0] = (this.sequence[0] + 1);
/* 141:382 */     for (int k = 1; k < size; k++) {
/* 142:383 */       this.costPerStep[k] = (this.costPerStep[(k - 1)] + this.sequence[k]);
/* 143:    */     }
/* 144:387 */     for (int k = 0; k < size; k++)
/* 145:    */     {
/* 146:388 */       this.coeff[k] = (k > 0 ? new double[k] : null);
/* 147:389 */       for (int l = 0; l < k; l++)
/* 148:    */       {
/* 149:390 */         double ratio = this.sequence[k] / this.sequence[(k - l - 1)];
/* 150:391 */         this.coeff[k][l] = (1.0D / (ratio * ratio - 1.0D));
/* 151:    */       }
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setInterpolationControl(boolean useInterpolationErrorForControl, int mudifControlParameter)
/* 156:    */   {
/* 157:410 */     this.useInterpolationError = useInterpolationErrorForControl;
/* 158:412 */     if ((mudifControlParameter <= 0) || (mudifControlParameter >= 7)) {
/* 159:413 */       this.mudif = 4;
/* 160:    */     } else {
/* 161:415 */       this.mudif = mudifControlParameter;
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   private void rescale(double[] y1, double[] y2, double[] scale)
/* 166:    */   {
/* 167:426 */     if (this.vecAbsoluteTolerance == null) {
/* 168:427 */       for (int i = 0; i < scale.length; i++)
/* 169:    */       {
/* 170:428 */         double yi = FastMath.max(FastMath.abs(y1[i]), FastMath.abs(y2[i]));
/* 171:429 */         scale[i] = (this.scalAbsoluteTolerance + this.scalRelativeTolerance * yi);
/* 172:    */       }
/* 173:    */     } else {
/* 174:432 */       for (int i = 0; i < scale.length; i++)
/* 175:    */       {
/* 176:433 */         double yi = FastMath.max(FastMath.abs(y1[i]), FastMath.abs(y2[i]));
/* 177:434 */         scale[i] = (this.vecAbsoluteTolerance[i] + this.vecRelativeTolerance[i] * yi);
/* 178:    */       }
/* 179:    */     }
/* 180:    */   }
/* 181:    */   
/* 182:    */   private boolean tryStep(double t0, double[] y0, double step, int k, double[] scale, double[][] f, double[] yMiddle, double[] yEnd, double[] yTmp)
/* 183:    */   {
/* 184:459 */     int n = this.sequence[k];
/* 185:460 */     double subStep = step / n;
/* 186:461 */     double subStep2 = 2.0D * subStep;
/* 187:    */     
/* 188:    */ 
/* 189:464 */     double t = t0 + subStep;
/* 190:465 */     for (int i = 0; i < y0.length; i++)
/* 191:    */     {
/* 192:466 */       yTmp[i] = y0[i];
/* 193:467 */       y0[i] += subStep * f[0][i];
/* 194:    */     }
/* 195:469 */     computeDerivatives(t, yEnd, f[1]);
/* 196:472 */     for (int j = 1; j < n; j++)
/* 197:    */     {
/* 198:474 */       if (2 * j == n) {
/* 199:476 */         System.arraycopy(yEnd, 0, yMiddle, 0, y0.length);
/* 200:    */       }
/* 201:479 */       t += subStep;
/* 202:480 */       for (int i = 0; i < y0.length; i++)
/* 203:    */       {
/* 204:481 */         double middle = yEnd[i];
/* 205:482 */         yTmp[i] += subStep2 * f[j][i];
/* 206:483 */         yTmp[i] = middle;
/* 207:    */       }
/* 208:486 */       computeDerivatives(t, yEnd, f[(j + 1)]);
/* 209:489 */       if ((this.performTest) && (j <= this.maxChecks) && (k < this.maxIter))
/* 210:    */       {
/* 211:490 */         double initialNorm = 0.0D;
/* 212:491 */         for (int l = 0; l < scale.length; l++)
/* 213:    */         {
/* 214:492 */           double ratio = f[0][l] / scale[l];
/* 215:493 */           initialNorm += ratio * ratio;
/* 216:    */         }
/* 217:495 */         double deltaNorm = 0.0D;
/* 218:496 */         for (int l = 0; l < scale.length; l++)
/* 219:    */         {
/* 220:497 */           double ratio = (f[(j + 1)][l] - f[0][l]) / scale[l];
/* 221:498 */           deltaNorm += ratio * ratio;
/* 222:    */         }
/* 223:500 */         if (deltaNorm > 4.0D * FastMath.max(1.E-015D, initialNorm)) {
/* 224:501 */           return false;
/* 225:    */         }
/* 226:    */       }
/* 227:    */     }
/* 228:508 */     for (int i = 0; i < y0.length; i++) {
/* 229:509 */       yEnd[i] = (0.5D * (yTmp[i] + yEnd[i] + subStep * f[n][i]));
/* 230:    */     }
/* 231:512 */     return true;
/* 232:    */   }
/* 233:    */   
/* 234:    */   private void extrapolate(int offset, int k, double[][] diag, double[] last)
/* 235:    */   {
/* 236:527 */     for (int j = 1; j < k; j++) {
/* 237:528 */       for (int i = 0; i < last.length; i++) {
/* 238:530 */         diag[(k - j - 1)][i] = (diag[(k - j)][i] + this.coeff[(k + offset)][(j - 1)] * (diag[(k - j)][i] - diag[(k - j - 1)][i]));
/* 239:    */       }
/* 240:    */     }
/* 241:536 */     for (int i = 0; i < last.length; i++) {
/* 242:538 */       last[i] = (diag[0][i] + this.coeff[(k + offset)][(k - 1)] * (diag[0][i] - last[i]));
/* 243:    */     }
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void integrate(ExpandableStatefulODE equations, double t)
/* 247:    */     throws MathIllegalStateException, MathIllegalArgumentException
/* 248:    */   {
/* 249:547 */     sanityChecks(equations, t);
/* 250:548 */     setEquations(equations);
/* 251:549 */     boolean forward = t > equations.getTime();
/* 252:    */     
/* 253:    */ 
/* 254:552 */     double[] y0 = equations.getCompleteState();
/* 255:553 */     double[] y = (double[])y0.clone();
/* 256:554 */     double[] yDot0 = new double[y.length];
/* 257:555 */     double[] y1 = new double[y.length];
/* 258:556 */     double[] yTmp = new double[y.length];
/* 259:557 */     double[] yTmpDot = new double[y.length];
/* 260:    */     
/* 261:559 */     double[][] diagonal = new double[this.sequence.length - 1][];
/* 262:560 */     double[][] y1Diag = new double[this.sequence.length - 1][];
/* 263:561 */     for (int k = 0; k < this.sequence.length - 1; k++)
/* 264:    */     {
/* 265:562 */       diagonal[k] = new double[y.length];
/* 266:563 */       y1Diag[k] = new double[y.length];
/* 267:    */     }
/* 268:566 */     double[][][] fk = new double[this.sequence.length][][];
/* 269:567 */     for (int k = 0; k < this.sequence.length; k++)
/* 270:    */     {
/* 271:569 */       fk[k] = new double[this.sequence[k] + 1][];
/* 272:    */       
/* 273:    */ 
/* 274:572 */       fk[k][0] = yDot0;
/* 275:574 */       for (int l = 0; l < this.sequence[k]; l++) {
/* 276:575 */         fk[k][(l + 1)] = new double[y0.length];
/* 277:    */       }
/* 278:    */     }
/* 279:580 */     if (y != y0) {
/* 280:581 */       System.arraycopy(y0, 0, y, 0, y0.length);
/* 281:    */     }
/* 282:584 */     double[] yDot1 = new double[y0.length];
/* 283:585 */     double[][] yMidDots = new double[1 + 2 * this.sequence.length][y0.length];
/* 284:    */     
/* 285:    */ 
/* 286:588 */     double[] scale = new double[this.mainSetDimension];
/* 287:589 */     rescale(y, y, scale);
/* 288:    */     
/* 289:    */ 
/* 290:592 */     double tol = this.vecRelativeTolerance == null ? this.scalRelativeTolerance : this.vecRelativeTolerance[0];
/* 291:    */     
/* 292:594 */     double log10R = FastMath.log10(FastMath.max(1.0E-010D, tol));
/* 293:595 */     int targetIter = FastMath.max(1, FastMath.min(this.sequence.length - 2, (int)FastMath.floor(0.5D - 0.6D * log10R)));
/* 294:    */     
/* 295:    */ 
/* 296:    */ 
/* 297:    */ 
/* 298:600 */     AbstractStepInterpolator interpolator = new GraggBulirschStoerStepInterpolator(y, yDot0, y1, yDot1, yMidDots, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
/* 299:    */     
/* 300:    */ 
/* 301:    */ 
/* 302:    */ 
/* 303:    */ 
/* 304:606 */     interpolator.storeTime(equations.getTime());
/* 305:    */     
/* 306:608 */     this.stepStart = equations.getTime();
/* 307:609 */     double hNew = 0.0D;
/* 308:610 */     double maxError = 1.7976931348623157E+308D;
/* 309:611 */     boolean previousRejected = false;
/* 310:612 */     boolean firstTime = true;
/* 311:613 */     boolean newStep = true;
/* 312:614 */     boolean firstStepAlreadyComputed = false;
/* 313:615 */     initIntegration(equations.getTime(), y0, t);
/* 314:616 */     this.costPerTimeUnit[0] = 0.0D;
/* 315:617 */     this.isLastStep = false;
/* 316:    */     do
/* 317:    */     {
/* 318:621 */       boolean reject = false;
/* 319:623 */       if (newStep)
/* 320:    */       {
/* 321:625 */         interpolator.shift();
/* 322:628 */         if (!firstStepAlreadyComputed) {
/* 323:629 */           computeDerivatives(this.stepStart, y, yDot0);
/* 324:    */         }
/* 325:632 */         if (firstTime) {
/* 326:633 */           hNew = initializeStep(forward, 2 * targetIter + 1, scale, this.stepStart, y, yDot0, yTmp, yTmpDot);
/* 327:    */         }
/* 328:637 */         newStep = false;
/* 329:    */       }
/* 330:641 */       this.stepSize = hNew;
/* 331:644 */       if (((forward) && (this.stepStart + this.stepSize > t)) || ((!forward) && (this.stepStart + this.stepSize < t))) {
/* 332:646 */         this.stepSize = (t - this.stepStart);
/* 333:    */       }
/* 334:648 */       double nextT = this.stepStart + this.stepSize;
/* 335:649 */       this.isLastStep = (nextT >= t);
/* 336:    */       
/* 337:    */ 
/* 338:652 */       int k = -1;
/* 339:653 */       for (boolean loop = true; loop;)
/* 340:    */       {
/* 341:655 */         k++;
/* 342:658 */         if (!tryStep(this.stepStart, y, this.stepSize, k, scale, fk[k], k == 0 ? yMidDots[0] : diagonal[(k - 1)], k == 0 ? y1 : y1Diag[(k - 1)], yTmp))
/* 343:    */         {
/* 344:664 */           hNew = FastMath.abs(filterStep(this.stepSize * this.stabilityReduction, forward, false));
/* 345:665 */           reject = true;
/* 346:666 */           loop = false;
/* 347:    */         }
/* 348:671 */         else if (k > 0)
/* 349:    */         {
/* 350:675 */           extrapolate(0, k, y1Diag, y1);
/* 351:676 */           rescale(y, y1, scale);
/* 352:    */           
/* 353:    */ 
/* 354:679 */           double error = 0.0D;
/* 355:680 */           for (int j = 0; j < this.mainSetDimension; j++)
/* 356:    */           {
/* 357:681 */             double e = FastMath.abs(y1[j] - y1Diag[0][j]) / scale[j];
/* 358:682 */             error += e * e;
/* 359:    */           }
/* 360:684 */           error = FastMath.sqrt(error / this.mainSetDimension);
/* 361:686 */           if ((error > 1000000000000000.0D) || ((k > 1) && (error > maxError)))
/* 362:    */           {
/* 363:688 */             hNew = FastMath.abs(filterStep(this.stepSize * this.stabilityReduction, forward, false));
/* 364:689 */             reject = true;
/* 365:690 */             loop = false;
/* 366:    */           }
/* 367:    */           else
/* 368:    */           {
/* 369:693 */             maxError = FastMath.max(4.0D * error, 1.0D);
/* 370:    */             
/* 371:    */ 
/* 372:696 */             double exp = 1.0D / (2 * k + 1);
/* 373:697 */             double fac = this.stepControl2 / FastMath.pow(error / this.stepControl1, exp);
/* 374:698 */             double pow = FastMath.pow(this.stepControl3, exp);
/* 375:699 */             fac = FastMath.max(pow / this.stepControl4, FastMath.min(1.0D / pow, fac));
/* 376:700 */             this.optimalStep[k] = FastMath.abs(filterStep(this.stepSize * fac, forward, true));
/* 377:701 */             this.costPerTimeUnit[k] = (this.costPerStep[k] / this.optimalStep[k]);
/* 378:704 */             switch (k - targetIter)
/* 379:    */             {
/* 380:    */             case -1: 
/* 381:707 */               if ((targetIter > 1) && (!previousRejected)) {
/* 382:710 */                 if (error <= 1.0D)
/* 383:    */                 {
/* 384:712 */                   loop = false;
/* 385:    */                 }
/* 386:    */                 else
/* 387:    */                 {
/* 388:717 */                   double ratio = this.sequence[targetIter] * this.sequence[(targetIter + 1)] / (this.sequence[0] * this.sequence[0]);
/* 389:719 */                   if (error > ratio * ratio)
/* 390:    */                   {
/* 391:722 */                     reject = true;
/* 392:723 */                     loop = false;
/* 393:724 */                     targetIter = k;
/* 394:725 */                     if ((targetIter > 1) && (this.costPerTimeUnit[(targetIter - 1)] < this.orderControl1 * this.costPerTimeUnit[targetIter])) {
/* 395:728 */                       targetIter--;
/* 396:    */                     }
/* 397:730 */                     hNew = this.optimalStep[targetIter];
/* 398:    */                   }
/* 399:    */                 }
/* 400:    */               }
/* 401:732 */               break;
/* 402:    */             case 0: 
/* 403:737 */               if (error <= 1.0D)
/* 404:    */               {
/* 405:739 */                 loop = false;
/* 406:    */               }
/* 407:    */               else
/* 408:    */               {
/* 409:744 */                 double ratio = this.sequence[(k + 1)] / this.sequence[0];
/* 410:745 */                 if (error > ratio * ratio)
/* 411:    */                 {
/* 412:748 */                   reject = true;
/* 413:749 */                   loop = false;
/* 414:750 */                   if ((targetIter > 1) && (this.costPerTimeUnit[(targetIter - 1)] < this.orderControl1 * this.costPerTimeUnit[targetIter])) {
/* 415:753 */                     targetIter--;
/* 416:    */                   }
/* 417:755 */                   hNew = this.optimalStep[targetIter];
/* 418:    */                 }
/* 419:    */               }
/* 420:758 */               break;
/* 421:    */             case 1: 
/* 422:761 */               if (error > 1.0D)
/* 423:    */               {
/* 424:762 */                 reject = true;
/* 425:763 */                 if ((targetIter > 1) && (this.costPerTimeUnit[(targetIter - 1)] < this.orderControl1 * this.costPerTimeUnit[targetIter])) {
/* 426:766 */                   targetIter--;
/* 427:    */                 }
/* 428:768 */                 hNew = this.optimalStep[targetIter];
/* 429:    */               }
/* 430:770 */               loop = false;
/* 431:771 */               break;
/* 432:    */             default: 
/* 433:774 */               if (((firstTime) || (this.isLastStep)) && (error <= 1.0D)) {
/* 434:775 */                 loop = false;
/* 435:    */               }
/* 436:    */               break;
/* 437:    */             }
/* 438:    */           }
/* 439:    */         }
/* 440:    */       }
/* 441:786 */       if (!reject) {
/* 442:788 */         computeDerivatives(this.stepStart + this.stepSize, y1, yDot1);
/* 443:    */       }
/* 444:792 */       double hInt = getMaxStep();
/* 445:793 */       if (!reject)
/* 446:    */       {
/* 447:796 */         for (int j = 1; j <= k; j++) {
/* 448:797 */           extrapolate(0, j, diagonal, yMidDots[0]);
/* 449:    */         }
/* 450:800 */         int mu = 2 * k - this.mudif + 3;
/* 451:802 */         for (int l = 0; l < mu; l++)
/* 452:    */         {
/* 453:805 */           int l2 = l / 2;
/* 454:806 */           double factor = FastMath.pow(0.5D * this.sequence[l2], l);
/* 455:807 */           int middleIndex = fk[l2].length / 2;
/* 456:808 */           for (int i = 0; i < y0.length; i++) {
/* 457:809 */             yMidDots[(l + 1)][i] = (factor * fk[l2][(middleIndex + l)][i]);
/* 458:    */           }
/* 459:811 */           for (int j = 1; j <= k - l2; j++)
/* 460:    */           {
/* 461:812 */             factor = FastMath.pow(0.5D * this.sequence[(j + l2)], l);
/* 462:813 */             middleIndex = fk[(l2 + j)].length / 2;
/* 463:814 */             for (int i = 0; i < y0.length; i++) {
/* 464:815 */               diagonal[(j - 1)][i] = (factor * fk[(l2 + j)][(middleIndex + l)][i]);
/* 465:    */             }
/* 466:817 */             extrapolate(l2, j, diagonal, yMidDots[(l + 1)]);
/* 467:    */           }
/* 468:819 */           for (int i = 0; i < y0.length; i++) {
/* 469:820 */             yMidDots[(l + 1)][i] *= this.stepSize;
/* 470:    */           }
/* 471:824 */           for (int j = (l + 1) / 2; j <= k; j++) {
/* 472:825 */             for (int m = fk[j].length - 1; m >= 2 * (l + 1); m--) {
/* 473:826 */               for (int i = 0; i < y0.length; i++) {
/* 474:827 */                 fk[j][m][i] -= fk[j][(m - 2)][i];
/* 475:    */               }
/* 476:    */             }
/* 477:    */           }
/* 478:    */         }
/* 479:834 */         if (mu >= 0)
/* 480:    */         {
/* 481:837 */           GraggBulirschStoerStepInterpolator gbsInterpolator = (GraggBulirschStoerStepInterpolator)interpolator;
/* 482:    */           
/* 483:839 */           gbsInterpolator.computeCoefficients(mu, this.stepSize);
/* 484:841 */           if (this.useInterpolationError)
/* 485:    */           {
/* 486:843 */             double interpError = gbsInterpolator.estimateError(scale);
/* 487:844 */             hInt = FastMath.abs(this.stepSize / FastMath.max(FastMath.pow(interpError, 1.0D / (mu + 4)), 0.01D));
/* 488:846 */             if (interpError > 10.0D)
/* 489:    */             {
/* 490:847 */               hNew = hInt;
/* 491:848 */               reject = true;
/* 492:    */             }
/* 493:    */           }
/* 494:    */         }
/* 495:    */       }
/* 496:856 */       if (!reject)
/* 497:    */       {
/* 498:859 */         interpolator.storeTime(this.stepStart + this.stepSize);
/* 499:860 */         this.stepStart = acceptStep(interpolator, y1, yDot1, t);
/* 500:    */         
/* 501:    */ 
/* 502:863 */         interpolator.storeTime(this.stepStart);
/* 503:864 */         System.arraycopy(y1, 0, y, 0, y0.length);
/* 504:865 */         System.arraycopy(yDot1, 0, yDot0, 0, y0.length);
/* 505:866 */         firstStepAlreadyComputed = true;
/* 506:    */         int optimalIter;
/* 507:869 */         if (k == 1)
/* 508:    */         {
/* 509:870 */           optimalIter = 2;
/* 510:871 */           if (previousRejected) {
/* 511:872 */             optimalIter = 1;
/* 512:    */           }
/* 513:    */         }
/* 514:874 */         else if (k <= targetIter)
/* 515:    */         {
/* 516:875 */           optimalIter = k;
/* 517:876 */           if (this.costPerTimeUnit[(k - 1)] < this.orderControl1 * this.costPerTimeUnit[k]) {
/* 518:877 */             optimalIter = k - 1;
/* 519:878 */           } else if (this.costPerTimeUnit[k] < this.orderControl2 * this.costPerTimeUnit[(k - 1)]) {
/* 520:879 */             optimalIter = FastMath.min(k + 1, this.sequence.length - 2);
/* 521:    */           }
/* 522:    */         }
/* 523:    */         else
/* 524:    */         {
/* 525:882 */           optimalIter = k - 1;
/* 526:883 */           if ((k > 2) && (this.costPerTimeUnit[(k - 2)] < this.orderControl1 * this.costPerTimeUnit[(k - 1)])) {
/* 527:885 */             optimalIter = k - 2;
/* 528:    */           }
/* 529:887 */           if (this.costPerTimeUnit[k] < this.orderControl2 * this.costPerTimeUnit[optimalIter]) {
/* 530:888 */             optimalIter = FastMath.min(k, this.sequence.length - 2);
/* 531:    */           }
/* 532:    */         }
/* 533:892 */         if (previousRejected)
/* 534:    */         {
/* 535:895 */           targetIter = FastMath.min(optimalIter, k);
/* 536:896 */           hNew = FastMath.min(FastMath.abs(this.stepSize), this.optimalStep[targetIter]);
/* 537:    */         }
/* 538:    */         else
/* 539:    */         {
/* 540:899 */           if (optimalIter <= k) {
/* 541:900 */             hNew = this.optimalStep[optimalIter];
/* 542:902 */           } else if ((k < targetIter) && (this.costPerTimeUnit[k] < this.orderControl2 * this.costPerTimeUnit[(k - 1)])) {
/* 543:904 */             hNew = filterStep(this.optimalStep[k] * this.costPerStep[(optimalIter + 1)] / this.costPerStep[k], forward, false);
/* 544:    */           } else {
/* 545:907 */             hNew = filterStep(this.optimalStep[k] * this.costPerStep[optimalIter] / this.costPerStep[k], forward, false);
/* 546:    */           }
/* 547:912 */           targetIter = optimalIter;
/* 548:    */         }
/* 549:916 */         newStep = true;
/* 550:    */       }
/* 551:920 */       hNew = FastMath.min(hNew, hInt);
/* 552:921 */       if (!forward) {
/* 553:922 */         hNew = -hNew;
/* 554:    */       }
/* 555:925 */       firstTime = false;
/* 556:927 */       if (reject)
/* 557:    */       {
/* 558:928 */         this.isLastStep = false;
/* 559:929 */         previousRejected = true;
/* 560:    */       }
/* 561:    */       else
/* 562:    */       {
/* 563:931 */         previousRejected = false;
/* 564:    */       }
/* 565:934 */     } while (!this.isLastStep);
/* 566:937 */     equations.setTime(this.stepStart);
/* 567:938 */     equations.setCompleteState(y);
/* 568:    */     
/* 569:940 */     resetInternalState();
/* 570:    */   }
/* 571:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator
 * JD-Core Version:    0.7.0.1
 */
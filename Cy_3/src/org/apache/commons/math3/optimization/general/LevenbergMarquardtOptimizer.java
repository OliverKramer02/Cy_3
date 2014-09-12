/*   1:    */ package org.apache.commons.math3.optimization.general;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   7:    */ import org.apache.commons.math3.optimization.PointVectorValuePair;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class LevenbergMarquardtOptimizer
/*  11:    */   extends AbstractLeastSquaresOptimizer
/*  12:    */ {
/*  13:    */   private int solvedCols;
/*  14:    */   private double[] diagR;
/*  15:    */   private double[] jacNorm;
/*  16:    */   private double[] beta;
/*  17:    */   private int[] permutation;
/*  18:    */   private int rank;
/*  19:    */   private double lmPar;
/*  20:    */   private double[] lmDir;
/*  21:    */   private final double initialStepBoundFactor;
/*  22:    */   private final double costRelativeTolerance;
/*  23:    */   private final double parRelativeTolerance;
/*  24:    */   private final double orthoTolerance;
/*  25:    */   private final double qrRankingThreshold;
/*  26:    */   
/*  27:    */   public LevenbergMarquardtOptimizer()
/*  28:    */   {
/*  29:151 */     this(100.0D, 1.0E-010D, 1.0E-010D, 1.0E-010D, 2.225073858507201E-308D);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public LevenbergMarquardtOptimizer(ConvergenceChecker<PointVectorValuePair> checker)
/*  33:    */   {
/*  34:170 */     this(100.0D, checker, 1.0E-010D, 1.0E-010D, 1.0E-010D, 2.225073858507201E-308D);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public LevenbergMarquardtOptimizer(double initialStepBoundFactor, ConvergenceChecker<PointVectorValuePair> checker, double costRelativeTolerance, double parRelativeTolerance, double orthoTolerance, double threshold)
/*  38:    */   {
/*  39:201 */     super(checker);
/*  40:202 */     this.initialStepBoundFactor = initialStepBoundFactor;
/*  41:203 */     this.costRelativeTolerance = costRelativeTolerance;
/*  42:204 */     this.parRelativeTolerance = parRelativeTolerance;
/*  43:205 */     this.orthoTolerance = orthoTolerance;
/*  44:206 */     this.qrRankingThreshold = threshold;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public LevenbergMarquardtOptimizer(double costRelativeTolerance, double parRelativeTolerance, double orthoTolerance)
/*  48:    */   {
/*  49:230 */     this(100.0D, costRelativeTolerance, parRelativeTolerance, orthoTolerance, 2.225073858507201E-308D);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public LevenbergMarquardtOptimizer(double initialStepBoundFactor, double costRelativeTolerance, double parRelativeTolerance, double orthoTolerance, double threshold)
/*  53:    */   {
/*  54:263 */     this.initialStepBoundFactor = initialStepBoundFactor;
/*  55:264 */     this.costRelativeTolerance = costRelativeTolerance;
/*  56:265 */     this.parRelativeTolerance = parRelativeTolerance;
/*  57:266 */     this.orthoTolerance = orthoTolerance;
/*  58:267 */     this.qrRankingThreshold = threshold;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected PointVectorValuePair doOptimize()
/*  62:    */   {
/*  63:274 */     this.solvedCols = FastMath.min(this.rows, this.cols);
/*  64:275 */     this.diagR = new double[this.cols];
/*  65:276 */     this.jacNorm = new double[this.cols];
/*  66:277 */     this.beta = new double[this.cols];
/*  67:278 */     this.permutation = new int[this.cols];
/*  68:279 */     this.lmDir = new double[this.cols];
/*  69:    */     
/*  70:    */ 
/*  71:282 */     double delta = 0.0D;
/*  72:283 */     double xNorm = 0.0D;
/*  73:284 */     double[] diag = new double[this.cols];
/*  74:285 */     double[] oldX = new double[this.cols];
/*  75:286 */     double[] oldRes = new double[this.rows];
/*  76:287 */     double[] oldObj = new double[this.rows];
/*  77:288 */     double[] qtf = new double[this.rows];
/*  78:289 */     double[] work1 = new double[this.cols];
/*  79:290 */     double[] work2 = new double[this.cols];
/*  80:291 */     double[] work3 = new double[this.cols];
/*  81:    */     
/*  82:    */ 
/*  83:294 */     updateResidualsAndCost();
/*  84:    */     
/*  85:    */ 
/*  86:297 */     this.lmPar = 0.0D;
/*  87:298 */     boolean firstIteration = true;
/*  88:299 */     PointVectorValuePair current = new PointVectorValuePair(this.point, this.objective);
/*  89:300 */     int iter = 0;
/*  90:301 */     ConvergenceChecker<PointVectorValuePair> checker = getConvergenceChecker();
/*  91:    */     PointVectorValuePair previous;
/*  92:    */     double maxCosine;
/*  93:    */     double ratio;
/*  94:    */     for (;;)
/*  95:    */     {
/*  96:303 */       iter++;
/*  97:305 */       for (int i = 0; i < this.rows; i++) {
/*  98:306 */         qtf[i] = this.weightedResiduals[i];
/*  99:    */       }
/* 100:310 */       previous = current;
/* 101:311 */       updateJacobian();
/* 102:312 */       qrDecomposition();
/* 103:    */       
/* 104:    */ 
/* 105:315 */       qTy(qtf);
/* 106:318 */       for (int k = 0; k < this.solvedCols; k++)
/* 107:    */       {
/* 108:319 */         int pk = this.permutation[k];
/* 109:320 */         this.weightedResidualJacobian[k][pk] = this.diagR[pk];
/* 110:    */       }
/* 111:323 */       if (firstIteration)
/* 112:    */       {
/* 113:326 */         xNorm = 0.0D;
/* 114:327 */         for (int k = 0; k < this.cols; k++)
/* 115:    */         {
/* 116:328 */           double dk = this.jacNorm[k];
/* 117:329 */           if (dk == 0.0D) {
/* 118:330 */             dk = 1.0D;
/* 119:    */           }
/* 120:332 */           double xk = dk * this.point[k];
/* 121:333 */           xNorm += xk * xk;
/* 122:334 */           diag[k] = dk;
/* 123:    */         }
/* 124:336 */         xNorm = FastMath.sqrt(xNorm);
/* 125:    */         
/* 126:    */ 
/* 127:339 */         delta = xNorm == 0.0D ? this.initialStepBoundFactor : this.initialStepBoundFactor * xNorm;
/* 128:    */       }
/* 129:343 */       maxCosine = 0.0D;
/* 130:344 */       if (this.cost != 0.0D) {
/* 131:345 */         for (int j = 0; j < this.solvedCols; j++)
/* 132:    */         {
/* 133:346 */           int pj = this.permutation[j];
/* 134:347 */           double s = this.jacNorm[pj];
/* 135:348 */           if (s != 0.0D)
/* 136:    */           {
/* 137:349 */             double sum = 0.0D;
/* 138:350 */             for (int i = 0; i <= j; i++) {
/* 139:351 */               sum += this.weightedResidualJacobian[i][pj] * qtf[i];
/* 140:    */             }
/* 141:353 */             maxCosine = FastMath.max(maxCosine, FastMath.abs(sum) / (s * this.cost));
/* 142:    */           }
/* 143:    */         }
/* 144:    */       }
/* 145:357 */       if (maxCosine <= this.orthoTolerance)
/* 146:    */       {
/* 147:359 */         updateResidualsAndCost();
/* 148:360 */         current = new PointVectorValuePair(this.point, this.objective);
/* 149:361 */         return current;
/* 150:    */       }
/* 151:365 */       for (int j = 0; j < this.cols; j++) {
/* 152:366 */         diag[j] = FastMath.max(diag[j], this.jacNorm[j]);
/* 153:    */       }
/* 154:370 */       for (ratio = 0.0D; ratio < 0.0001D;)
/* 155:    */       {
/* 156:373 */         for (int j = 0; j < this.solvedCols; j++)
/* 157:    */         {
/* 158:374 */           int pj = this.permutation[j];
/* 159:375 */           oldX[pj] = this.point[pj];
/* 160:    */         }
/* 161:377 */         double previousCost = this.cost;
/* 162:378 */         double[] tmpVec = this.weightedResiduals;
/* 163:379 */         this.weightedResiduals = oldRes;
/* 164:380 */         oldRes = tmpVec;
/* 165:381 */         tmpVec = this.objective;
/* 166:382 */         this.objective = oldObj;
/* 167:383 */         oldObj = tmpVec;
/* 168:    */         
/* 169:    */ 
/* 170:386 */         determineLMParameter(qtf, delta, diag, work1, work2, work3);
/* 171:    */         
/* 172:    */ 
/* 173:389 */         double lmNorm = 0.0D;
/* 174:390 */         for (int j = 0; j < this.solvedCols; j++)
/* 175:    */         {
/* 176:391 */           int pj = this.permutation[j];
/* 177:392 */           this.lmDir[pj] = (-this.lmDir[pj]);
/* 178:393 */           this.point[pj] = (oldX[pj] + this.lmDir[pj]);
/* 179:394 */           double s = diag[pj] * this.lmDir[pj];
/* 180:395 */           lmNorm += s * s;
/* 181:    */         }
/* 182:397 */         lmNorm = FastMath.sqrt(lmNorm);
/* 183:399 */         if (firstIteration) {
/* 184:400 */           delta = FastMath.min(delta, lmNorm);
/* 185:    */         }
/* 186:404 */         updateResidualsAndCost();
/* 187:    */         
/* 188:    */ 
/* 189:407 */         double actRed = -1.0D;
/* 190:408 */         if (0.1D * this.cost < previousCost)
/* 191:    */         {
/* 192:409 */           double r = this.cost / previousCost;
/* 193:410 */           actRed = 1.0D - r * r;
/* 194:    */         }
/* 195:415 */         for (int j = 0; j < this.solvedCols; j++)
/* 196:    */         {
/* 197:416 */           int pj = this.permutation[j];
/* 198:417 */           double dirJ = this.lmDir[pj];
/* 199:418 */           work1[j] = 0.0D;
/* 200:419 */           for (int i = 0; i <= j; i++) {
/* 201:420 */             work1[i] += this.weightedResidualJacobian[i][pj] * dirJ;
/* 202:    */           }
/* 203:    */         }
/* 204:423 */         double coeff1 = 0.0D;
/* 205:424 */         for (int j = 0; j < this.solvedCols; j++) {
/* 206:425 */           coeff1 += work1[j] * work1[j];
/* 207:    */         }
/* 208:427 */         double pc2 = previousCost * previousCost;
/* 209:428 */         coeff1 /= pc2;
/* 210:429 */         double coeff2 = this.lmPar * lmNorm * lmNorm / pc2;
/* 211:430 */         double preRed = coeff1 + 2.0D * coeff2;
/* 212:431 */         double dirDer = -(coeff1 + coeff2);
/* 213:    */         
/* 214:    */ 
/* 215:434 */         ratio = preRed == 0.0D ? 0.0D : actRed / preRed;
/* 216:437 */         if (ratio <= 0.25D)
/* 217:    */         {
/* 218:438 */           double tmp = actRed < 0.0D ? 0.5D * dirDer / (dirDer + 0.5D * actRed) : 0.5D;
/* 219:440 */           if ((0.1D * this.cost >= previousCost) || (tmp < 0.1D)) {
/* 220:441 */             tmp = 0.1D;
/* 221:    */           }
/* 222:443 */           delta = tmp * FastMath.min(delta, 10.0D * lmNorm);
/* 223:444 */           this.lmPar /= tmp;
/* 224:    */         }
/* 225:445 */         else if ((this.lmPar == 0.0D) || (ratio >= 0.75D))
/* 226:    */         {
/* 227:446 */           delta = 2.0D * lmNorm;
/* 228:447 */           this.lmPar *= 0.5D;
/* 229:    */         }
/* 230:451 */         if (ratio >= 0.0001D)
/* 231:    */         {
/* 232:453 */           firstIteration = false;
/* 233:454 */           xNorm = 0.0D;
/* 234:455 */           for (int k = 0; k < this.cols; k++)
/* 235:    */           {
/* 236:456 */             double xK = diag[k] * this.point[k];
/* 237:457 */             xNorm += xK * xK;
/* 238:    */           }
/* 239:459 */           xNorm = FastMath.sqrt(xNorm);
/* 240:460 */           current = new PointVectorValuePair(this.point, this.objective);
/* 241:463 */           if (checker != null) {
/* 242:465 */             if (checker.converged(iter, previous, current)) {
/* 243:466 */               return current;
/* 244:    */             }
/* 245:    */           }
/* 246:    */         }
/* 247:    */         else
/* 248:    */         {
/* 249:471 */           this.cost = previousCost;
/* 250:472 */           for (int j = 0; j < this.solvedCols; j++)
/* 251:    */           {
/* 252:473 */             int pj = this.permutation[j];
/* 253:474 */             this.point[pj] = oldX[pj];
/* 254:    */           }
/* 255:476 */           tmpVec = this.weightedResiduals;
/* 256:477 */           this.weightedResiduals = oldRes;
/* 257:478 */           oldRes = tmpVec;
/* 258:479 */           tmpVec = this.objective;
/* 259:480 */           this.objective = oldObj;
/* 260:481 */           oldObj = tmpVec;
/* 261:    */         }
/* 262:485 */         if (((FastMath.abs(actRed) <= this.costRelativeTolerance) && (preRed <= this.costRelativeTolerance) && (ratio <= 2.0D)) || (delta <= this.parRelativeTolerance * xNorm)) {
/* 263:489 */           return current;
/* 264:    */         }
/* 265:494 */         if ((FastMath.abs(actRed) <= 2.2204E-016D) && (preRed <= 2.2204E-016D) && (ratio <= 2.0D)) {
/* 266:495 */           throw new ConvergenceException(LocalizedFormats.TOO_SMALL_COST_RELATIVE_TOLERANCE, new Object[] { Double.valueOf(this.costRelativeTolerance) });
/* 267:    */         }
/* 268:497 */         if (delta <= 2.2204E-016D * xNorm) {
/* 269:498 */           throw new ConvergenceException(LocalizedFormats.TOO_SMALL_PARAMETERS_RELATIVE_TOLERANCE, new Object[] { Double.valueOf(this.parRelativeTolerance) });
/* 270:    */         }
/* 271:500 */         if (maxCosine <= 2.2204E-016D) {
/* 272:501 */           throw new ConvergenceException(LocalizedFormats.TOO_SMALL_ORTHOGONALITY_TOLERANCE, new Object[] { Double.valueOf(this.orthoTolerance) });
/* 273:    */         }
/* 274:    */       }
/* 275:    */     }
/* 276:    */   }
/* 277:    */   
/* 278:    */   private void determineLMParameter(double[] qy, double delta, double[] diag, double[] work1, double[] work2, double[] work3)
/* 279:    */   {
/* 280:535 */     for (int j = 0; j < this.rank; j++) {
/* 281:536 */       this.lmDir[this.permutation[j]] = qy[j];
/* 282:    */     }
/* 283:538 */     for (int j = this.rank; j < this.cols; j++) {
/* 284:539 */       this.lmDir[this.permutation[j]] = 0.0D;
/* 285:    */     }
/* 286:541 */     for (int k = this.rank - 1; k >= 0; k--)
/* 287:    */     {
/* 288:542 */       int pk = this.permutation[k];
/* 289:543 */       double ypk = this.lmDir[pk] / this.diagR[pk];
/* 290:544 */       for (int i = 0; i < k; i++) {
/* 291:545 */         this.lmDir[this.permutation[i]] -= ypk * this.weightedResidualJacobian[i][pk];
/* 292:    */       }
/* 293:547 */       this.lmDir[pk] = ypk;
/* 294:    */     }
/* 295:552 */     double dxNorm = 0.0D;
/* 296:553 */     for (int j = 0; j < this.solvedCols; j++)
/* 297:    */     {
/* 298:554 */       int pj = this.permutation[j];
/* 299:555 */       double s = diag[pj] * this.lmDir[pj];
/* 300:556 */       work1[pj] = s;
/* 301:557 */       dxNorm += s * s;
/* 302:    */     }
/* 303:559 */     dxNorm = FastMath.sqrt(dxNorm);
/* 304:560 */     double fp = dxNorm - delta;
/* 305:561 */     if (fp <= 0.1D * delta)
/* 306:    */     {
/* 307:562 */       this.lmPar = 0.0D;
/* 308:563 */       return;
/* 309:    */     }
/* 310:570 */     double parl = 0.0D;
/* 311:571 */     if (this.rank == this.solvedCols)
/* 312:    */     {
/* 313:572 */       for (int j = 0; j < this.solvedCols; j++)
/* 314:    */       {
/* 315:573 */         int pj = this.permutation[j];
/* 316:574 */         work1[pj] *= diag[pj] / dxNorm;
/* 317:    */       }
/* 318:576 */       double sum2 = 0.0D;
/* 319:577 */       for (int j = 0; j < this.solvedCols; j++)
/* 320:    */       {
/* 321:578 */         int pj = this.permutation[j];
/* 322:579 */         double sum = 0.0D;
/* 323:580 */         for (int i = 0; i < j; i++) {
/* 324:581 */           sum += this.weightedResidualJacobian[i][pj] * work1[this.permutation[i]];
/* 325:    */         }
/* 326:583 */         double s = (work1[pj] - sum) / this.diagR[pj];
/* 327:584 */         work1[pj] = s;
/* 328:585 */         sum2 += s * s;
/* 329:    */       }
/* 330:587 */       parl = fp / (delta * sum2);
/* 331:    */     }
/* 332:591 */     double sum2 = 0.0D;
/* 333:592 */     for (int j = 0; j < this.solvedCols; j++)
/* 334:    */     {
/* 335:593 */       int pj = this.permutation[j];
/* 336:594 */       double sum = 0.0D;
/* 337:595 */       for (int i = 0; i <= j; i++) {
/* 338:596 */         sum += this.weightedResidualJacobian[i][pj] * qy[i];
/* 339:    */       }
/* 340:598 */       sum /= diag[pj];
/* 341:599 */       sum2 += sum * sum;
/* 342:    */     }
/* 343:601 */     double gNorm = FastMath.sqrt(sum2);
/* 344:602 */     double paru = gNorm / delta;
/* 345:603 */     if (paru == 0.0D) {
/* 346:605 */       paru = 2.2251E-308D / FastMath.min(delta, 0.1D);
/* 347:    */     }
/* 348:610 */     this.lmPar = FastMath.min(paru, FastMath.max(this.lmPar, parl));
/* 349:611 */     if (this.lmPar == 0.0D) {
/* 350:612 */       this.lmPar = (gNorm / dxNorm);
/* 351:    */     }
/* 352:615 */     for (int countdown = 10; countdown >= 0; countdown--)
/* 353:    */     {
/* 354:618 */       if (this.lmPar == 0.0D) {
/* 355:619 */         this.lmPar = FastMath.max(2.2251E-308D, 0.001D * paru);
/* 356:    */       }
/* 357:621 */       double sPar = FastMath.sqrt(this.lmPar);
/* 358:622 */       for (int j = 0; j < this.solvedCols; j++)
/* 359:    */       {
/* 360:623 */         int pj = this.permutation[j];
/* 361:624 */         work1[pj] = (sPar * diag[pj]);
/* 362:    */       }
/* 363:626 */       determineLMDirection(qy, work1, work2, work3);
/* 364:    */       
/* 365:628 */       dxNorm = 0.0D;
/* 366:629 */       for (int j = 0; j < this.solvedCols; j++)
/* 367:    */       {
/* 368:630 */         int pj = this.permutation[j];
/* 369:631 */         double s = diag[pj] * this.lmDir[pj];
/* 370:632 */         work3[pj] = s;
/* 371:633 */         dxNorm += s * s;
/* 372:    */       }
/* 373:635 */       dxNorm = FastMath.sqrt(dxNorm);
/* 374:636 */       double previousFP = fp;
/* 375:637 */       fp = dxNorm - delta;
/* 376:641 */       if ((FastMath.abs(fp) <= 0.1D * delta) || ((parl == 0.0D) && (fp <= previousFP) && (previousFP < 0.0D))) {
/* 377:643 */         return;
/* 378:    */       }
/* 379:647 */       for (int j = 0; j < this.solvedCols; j++)
/* 380:    */       {
/* 381:648 */         int pj = this.permutation[j];
/* 382:649 */         work1[pj] = (work3[pj] * diag[pj] / dxNorm);
/* 383:    */       }
/* 384:651 */       for (int j = 0; j < this.solvedCols; j++)
/* 385:    */       {
/* 386:652 */         int pj = this.permutation[j];
/* 387:653 */         work1[pj] /= work2[j];
/* 388:654 */         double tmp = work1[pj];
/* 389:655 */         for (int i = j + 1; i < this.solvedCols; i++) {
/* 390:656 */           work1[this.permutation[i]] -= this.weightedResidualJacobian[i][pj] * tmp;
/* 391:    */         }
/* 392:    */       }
/* 393:659 */       sum2 = 0.0D;
/* 394:660 */       for (int j = 0; j < this.solvedCols; j++)
/* 395:    */       {
/* 396:661 */         double s = work1[this.permutation[j]];
/* 397:662 */         sum2 += s * s;
/* 398:    */       }
/* 399:664 */       double correction = fp / (delta * sum2);
/* 400:667 */       if (fp > 0.0D) {
/* 401:668 */         parl = FastMath.max(parl, this.lmPar);
/* 402:669 */       } else if (fp < 0.0D) {
/* 403:670 */         paru = FastMath.min(paru, this.lmPar);
/* 404:    */       }
/* 405:674 */       this.lmPar = FastMath.max(parl, this.lmPar + correction);
/* 406:    */     }
/* 407:    */   }
/* 408:    */   
/* 409:    */   private void determineLMDirection(double[] qy, double[] diag, double[] lmDiag, double[] work)
/* 410:    */   {
/* 411:704 */     for (int j = 0; j < this.solvedCols; j++)
/* 412:    */     {
/* 413:705 */       int pj = this.permutation[j];
/* 414:706 */       for (int i = j + 1; i < this.solvedCols; i++) {
/* 415:707 */         this.weightedResidualJacobian[i][pj] = this.weightedResidualJacobian[j][this.permutation[i]];
/* 416:    */       }
/* 417:709 */       this.lmDir[j] = this.diagR[pj];
/* 418:710 */       work[j] = qy[j];
/* 419:    */     }
/* 420:714 */     for (int j = 0; j < this.solvedCols; j++)
/* 421:    */     {
/* 422:718 */       int pj = this.permutation[j];
/* 423:719 */       double dpj = diag[pj];
/* 424:720 */       if (dpj != 0.0D) {
/* 425:721 */         Arrays.fill(lmDiag, j + 1, lmDiag.length, 0.0D);
/* 426:    */       }
/* 427:723 */       lmDiag[j] = dpj;
/* 428:    */       
/* 429:    */ 
/* 430:    */ 
/* 431:    */ 
/* 432:728 */       double qtbpj = 0.0D;
/* 433:729 */       for (int k = j; k < this.solvedCols; k++)
/* 434:    */       {
/* 435:730 */         int pk = this.permutation[k];
/* 436:734 */         if (lmDiag[k] != 0.0D)
/* 437:    */         {
/* 438:738 */           double rkk = this.weightedResidualJacobian[k][pk];
/* 439:    */           double cos;
/* 440:    */          
/* 441:    */           double sin;
/* 442:739 */           if (FastMath.abs(rkk) < FastMath.abs(lmDiag[k]))
/* 443:    */           {
/* 444:740 */             double cotan = rkk / lmDiag[k];
/* 445:741 */             sin = 1.0D / FastMath.sqrt(1.0D + cotan * cotan);
/* 446:742 */             cos = sin * cotan;
/* 447:    */           }
/* 448:    */           else
/* 449:    */           {
/* 450:744 */             double tan = lmDiag[k] / rkk;
/* 451:745 */             cos = 1.0D / FastMath.sqrt(1.0D + tan * tan);
/* 452:746 */             sin = cos * tan;
/* 453:    */           }
/* 454:751 */           this.weightedResidualJacobian[k][pk] = (cos * rkk + sin * lmDiag[k]);
/* 455:752 */           double temp = cos * work[k] + sin * qtbpj;
/* 456:753 */           qtbpj = -sin * work[k] + cos * qtbpj;
/* 457:754 */           work[k] = temp;
/* 458:757 */           for (int i = k + 1; i < this.solvedCols; i++)
/* 459:    */           {
/* 460:758 */             double rik = this.weightedResidualJacobian[i][pk];
/* 461:759 */             double temp2 = cos * rik + sin * lmDiag[i];
/* 462:760 */             lmDiag[i] = (-sin * rik + cos * lmDiag[i]);
/* 463:761 */             this.weightedResidualJacobian[i][pk] = temp2;
/* 464:    */           }
/* 465:    */         }
/* 466:    */       }
/* 467:768 */       lmDiag[j] = this.weightedResidualJacobian[j][this.permutation[j]];
/* 468:769 */       this.weightedResidualJacobian[j][this.permutation[j]] = this.lmDir[j];
/* 469:    */     }
/* 470:774 */     int nSing = this.solvedCols;
/* 471:775 */     for (int j = 0; j < this.solvedCols; j++)
/* 472:    */     {
/* 473:776 */       if ((lmDiag[j] == 0.0D) && (nSing == this.solvedCols)) {
/* 474:777 */         nSing = j;
/* 475:    */       }
/* 476:779 */       if (nSing < this.solvedCols) {
/* 477:780 */         work[j] = 0.0D;
/* 478:    */       }
/* 479:    */     }
/* 480:783 */     if (nSing > 0) {
/* 481:784 */       for (int j = nSing - 1; j >= 0; j--)
/* 482:    */       {
/* 483:785 */         int pj = this.permutation[j];
/* 484:786 */         double sum = 0.0D;
/* 485:787 */         for (int i = j + 1; i < nSing; i++) {
/* 486:788 */           sum += this.weightedResidualJacobian[i][pj] * work[i];
/* 487:    */         }
/* 488:790 */         work[j] = ((work[j] - sum) / lmDiag[j]);
/* 489:    */       }
/* 490:    */     }
/* 491:795 */     for (int j = 0; j < this.lmDir.length; j++) {
/* 492:796 */       this.lmDir[this.permutation[j]] = work[j];
/* 493:    */     }
/* 494:    */   }
/* 495:    */   
/* 496:    */   private void qrDecomposition()
/* 497:    */     throws ConvergenceException
/* 498:    */   {
/* 499:825 */     for (int k = 0; k < this.cols; k++)
/* 500:    */     {
/* 501:826 */       this.permutation[k] = k;
/* 502:827 */       double norm2 = 0.0D;
/* 503:828 */       for (int i = 0; i < this.weightedResidualJacobian.length; i++)
/* 504:    */       {
/* 505:829 */         double akk = this.weightedResidualJacobian[i][k];
/* 506:830 */         norm2 += akk * akk;
/* 507:    */       }
/* 508:832 */       this.jacNorm[k] = FastMath.sqrt(norm2);
/* 509:    */     }
/* 510:836 */     for (int k = 0; k < this.cols; k++)
/* 511:    */     {
/* 512:839 */       int nextColumn = -1;
/* 513:840 */       double ak2 = (-1.0D / 0.0D);
/* 514:841 */       for (int i = k; i < this.cols; i++)
/* 515:    */       {
/* 516:842 */         double norm2 = 0.0D;
/* 517:843 */         for (int j = k; j < this.weightedResidualJacobian.length; j++)
/* 518:    */         {
/* 519:844 */           double aki = this.weightedResidualJacobian[j][this.permutation[i]];
/* 520:845 */           norm2 += aki * aki;
/* 521:    */         }
/* 522:847 */         if ((Double.isInfinite(norm2)) || (Double.isNaN(norm2))) {
/* 523:848 */           throw new ConvergenceException(LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN, new Object[] { Integer.valueOf(this.rows), Integer.valueOf(this.cols) });
/* 524:    */         }
/* 525:851 */         if (norm2 > ak2)
/* 526:    */         {
/* 527:852 */           nextColumn = i;
/* 528:853 */           ak2 = norm2;
/* 529:    */         }
/* 530:    */       }
/* 531:856 */       if (ak2 <= this.qrRankingThreshold)
/* 532:    */       {
/* 533:857 */         this.rank = k;
/* 534:858 */         return;
/* 535:    */       }
/* 536:860 */       int pk = this.permutation[nextColumn];
/* 537:861 */       this.permutation[nextColumn] = this.permutation[k];
/* 538:862 */       this.permutation[k] = pk;
/* 539:    */       
/* 540:    */ 
/* 541:865 */       double akk = this.weightedResidualJacobian[k][pk];
/* 542:866 */       double alpha = akk > 0.0D ? -FastMath.sqrt(ak2) : FastMath.sqrt(ak2);
/* 543:867 */       double betak = 1.0D / (ak2 - akk * alpha);
/* 544:868 */       this.beta[pk] = betak;
/* 545:    */       
/* 546:    */ 
/* 547:871 */       this.diagR[pk] = alpha;
/* 548:872 */       this.weightedResidualJacobian[k][pk] -= alpha;
/* 549:875 */       for (int dk = this.cols - 1 - k; dk > 0; dk--)
/* 550:    */       {
/* 551:876 */         double gamma = 0.0D;
/* 552:877 */         for (int j = k; j < this.weightedResidualJacobian.length; j++) {
/* 553:878 */           gamma += this.weightedResidualJacobian[j][pk] * this.weightedResidualJacobian[j][this.permutation[(k + dk)]];
/* 554:    */         }
/* 555:880 */         gamma *= betak;
/* 556:881 */         for (int j = k; j < this.weightedResidualJacobian.length; j++) {
/* 557:882 */           this.weightedResidualJacobian[j][this.permutation[(k + dk)]] -= gamma * this.weightedResidualJacobian[j][pk];
/* 558:    */         }
/* 559:    */       }
/* 560:    */     }
/* 561:886 */     this.rank = this.solvedCols;
/* 562:    */   }
/* 563:    */   
/* 564:    */   private void qTy(double[] y)
/* 565:    */   {
/* 566:895 */     for (int k = 0; k < this.cols; k++)
/* 567:    */     {
/* 568:896 */       int pk = this.permutation[k];
/* 569:897 */       double gamma = 0.0D;
/* 570:898 */       for (int i = k; i < this.rows; i++) {
/* 571:899 */         gamma += this.weightedResidualJacobian[i][pk] * y[i];
/* 572:    */       }
/* 573:901 */       gamma *= this.beta[pk];
/* 574:902 */       for (int i = k; i < this.rows; i++) {
/* 575:903 */         y[i] -= gamma * this.weightedResidualJacobian[i][pk];
/* 576:    */       }
/* 577:    */     }
/* 578:    */   }
/* 579:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer
 * JD-Core Version:    0.7.0.1
 */
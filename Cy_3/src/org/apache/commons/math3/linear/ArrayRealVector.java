/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   7:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   8:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   9:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ import org.apache.commons.math3.util.FastMath;
/*  13:    */ import org.apache.commons.math3.util.MathUtils;
/*  14:    */ 
/*  15:    */ public class ArrayRealVector
/*  16:    */   extends RealVector
/*  17:    */   implements Serializable
/*  18:    */ {
/*  19:    */   private static final long serialVersionUID = -1097961340710804027L;
/*  20: 41 */   private static final RealVectorFormat DEFAULT_FORMAT =null ;
/*  21:    */   private double[] data;
/*  22:    */   
/*  23:    */   public ArrayRealVector()
/*  24:    */   {
/*  25: 55 */     this.data = new double[0];
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ArrayRealVector(int size)
/*  29:    */   {
/*  30: 64 */     this.data = new double[size];
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ArrayRealVector(int size, double preset)
/*  34:    */   {
/*  35: 74 */     this.data = new double[size];
/*  36: 75 */     Arrays.fill(this.data, preset);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public ArrayRealVector(double[] d)
/*  40:    */   {
/*  41: 85 */     this.data = ((double[])d.clone());
/*  42:    */   }
/*  43:    */   
/*  44:    */   public ArrayRealVector(double[] d, boolean copyArray)
/*  45:    */   {
/*  46:103 */     if (d == null) {
/*  47:104 */       throw new NullArgumentException();
/*  48:    */     }
/*  49:106 */     this.data = (copyArray ? (double[])d.clone() : d);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public ArrayRealVector(double[] d, int pos, int size)
/*  53:    */   {
/*  54:120 */     if (d == null) {
/*  55:121 */       throw new NullArgumentException();
/*  56:    */     }
/*  57:123 */     if (d.length < pos + size) {
/*  58:124 */       throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d.length), true);
/*  59:    */     }
/*  60:126 */     this.data = new double[size];
/*  61:127 */     System.arraycopy(d, pos, this.data, 0, size);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public ArrayRealVector(Double[] d)
/*  65:    */   {
/*  66:136 */     this.data = new double[d.length];
/*  67:137 */     for (int i = 0; i < d.length; i++) {
/*  68:138 */       this.data[i] = d[i].doubleValue();
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public ArrayRealVector(Double[] d, int pos, int size)
/*  73:    */   {
/*  74:153 */     if (d == null) {
/*  75:154 */       throw new NullArgumentException();
/*  76:    */     }
/*  77:156 */     if (d.length < pos + size) {
/*  78:157 */       throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d.length), true);
/*  79:    */     }
/*  80:159 */     this.data = new double[size];
/*  81:160 */     for (int i = pos; i < pos + size; i++) {
/*  82:161 */       this.data[(i - pos)] = d[i].doubleValue();
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public ArrayRealVector(RealVector v)
/*  87:    */   {
/*  88:172 */     if (v == null) {
/*  89:173 */       throw new NullArgumentException();
/*  90:    */     }
/*  91:175 */     this.data = new double[v.getDimension()];
/*  92:176 */     for (int i = 0; i < this.data.length; i++) {
/*  93:177 */       this.data[i] = v.getEntry(i);
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public ArrayRealVector(ArrayRealVector v)
/*  98:    */   {
/*  99:188 */     this(v, true);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public ArrayRealVector(ArrayRealVector v, boolean deep)
/* 103:    */   {
/* 104:199 */     this.data = (deep ? (double[])v.data.clone() : v.data);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public ArrayRealVector(ArrayRealVector v1, ArrayRealVector v2)
/* 108:    */   {
/* 109:208 */     this.data = new double[v1.data.length + v2.data.length];
/* 110:209 */     System.arraycopy(v1.data, 0, this.data, 0, v1.data.length);
/* 111:210 */     System.arraycopy(v2.data, 0, this.data, v1.data.length, v2.data.length);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public ArrayRealVector(ArrayRealVector v1, RealVector v2)
/* 115:    */   {
/* 116:219 */     int l1 = v1.data.length;
/* 117:220 */     int l2 = v2.getDimension();
/* 118:221 */     this.data = new double[l1 + l2];
/* 119:222 */     System.arraycopy(v1.data, 0, this.data, 0, l1);
/* 120:223 */     for (int i = 0; i < l2; i++) {
/* 121:224 */       this.data[(l1 + i)] = v2.getEntry(i);
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public ArrayRealVector(RealVector v1, ArrayRealVector v2)
/* 126:    */   {
/* 127:234 */     int l1 = v1.getDimension();
/* 128:235 */     int l2 = v2.data.length;
/* 129:236 */     this.data = new double[l1 + l2];
/* 130:237 */     for (int i = 0; i < l1; i++) {
/* 131:238 */       this.data[i] = v1.getEntry(i);
/* 132:    */     }
/* 133:240 */     System.arraycopy(v2.data, 0, this.data, l1, l2);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public ArrayRealVector(ArrayRealVector v1, double[] v2)
/* 137:    */   {
/* 138:249 */     int l1 = v1.getDimension();
/* 139:250 */     int l2 = v2.length;
/* 140:251 */     this.data = new double[l1 + l2];
/* 141:252 */     System.arraycopy(v1.data, 0, this.data, 0, l1);
/* 142:253 */     System.arraycopy(v2, 0, this.data, l1, l2);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public ArrayRealVector(double[] v1, ArrayRealVector v2)
/* 146:    */   {
/* 147:262 */     int l1 = v1.length;
/* 148:263 */     int l2 = v2.getDimension();
/* 149:264 */     this.data = new double[l1 + l2];
/* 150:265 */     System.arraycopy(v1, 0, this.data, 0, l1);
/* 151:266 */     System.arraycopy(v2.data, 0, this.data, l1, l2);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public ArrayRealVector(double[] v1, double[] v2)
/* 155:    */   {
/* 156:275 */     int l1 = v1.length;
/* 157:276 */     int l2 = v2.length;
/* 158:277 */     this.data = new double[l1 + l2];
/* 159:278 */     System.arraycopy(v1, 0, this.data, 0, l1);
/* 160:279 */     System.arraycopy(v2, 0, this.data, l1, l2);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public ArrayRealVector copy()
/* 164:    */   {
/* 165:285 */     return new ArrayRealVector(this, true);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public ArrayRealVector add(RealVector v)
/* 169:    */   {
/* 170:291 */     if ((v instanceof ArrayRealVector))
/* 171:    */     {
/* 172:292 */       double[] vData = ((ArrayRealVector)v).data;
/* 173:293 */       int dim = vData.length;
/* 174:294 */       checkVectorDimensions(dim);
/* 175:295 */       ArrayRealVector result = new ArrayRealVector(dim);
/* 176:296 */       double[] resultData = result.data;
/* 177:297 */       for (int i = 0; i < dim; i++) {
/* 178:298 */         resultData[i] = (this.data[i] + vData[i]);
/* 179:    */       }
/* 180:300 */       return result;
/* 181:    */     }
/* 182:302 */     checkVectorDimensions(v);
/* 183:303 */     double[] out = (double[])this.data.clone();
/* 184:304 */     Iterator<RealVector.Entry> it = v.sparseIterator();
/* 185:    */     RealVector.Entry e;
/* 186:306 */     while ((it.hasNext()) && ((e = (RealVector.Entry)it.next()) != null)) {
/* 187:307 */       out[e.getIndex()] += e.getValue();
/* 188:    */     }
/* 189:309 */     return new ArrayRealVector(out, false);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public ArrayRealVector subtract(RealVector v)
/* 193:    */   {
/* 194:316 */     if ((v instanceof ArrayRealVector))
/* 195:    */     {
/* 196:317 */       double[] vData = ((ArrayRealVector)v).data;
/* 197:318 */       int dim = vData.length;
/* 198:319 */       checkVectorDimensions(dim);
/* 199:320 */       ArrayRealVector result = new ArrayRealVector(dim);
/* 200:321 */       double[] resultData = result.data;
/* 201:322 */       for (int i = 0; i < dim; i++) {
/* 202:323 */         resultData[i] = (this.data[i] - vData[i]);
/* 203:    */       }
/* 204:325 */       return result;
/* 205:    */     }
/* 206:327 */     checkVectorDimensions(v);
/* 207:328 */     double[] out = (double[])this.data.clone();
/* 208:329 */     Iterator<RealVector.Entry> it = v.sparseIterator();
/* 209:    */     RealVector.Entry e;
/* 210:331 */     while ((it.hasNext()) && ((e = (RealVector.Entry)it.next()) != null)) {
/* 211:332 */       out[e.getIndex()] -= e.getValue();
/* 212:    */     }
/* 213:334 */     return new ArrayRealVector(out, false);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public ArrayRealVector map(UnivariateFunction function)
/* 217:    */   {
/* 218:341 */     return copy().mapToSelf(function);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public ArrayRealVector mapToSelf(UnivariateFunction function)
/* 222:    */   {
/* 223:347 */     for (int i = 0; i < this.data.length; i++) {
/* 224:348 */       this.data[i] = function.value(this.data[i]);
/* 225:    */     }
/* 226:350 */     return this;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public RealVector mapAddToSelf(double d)
/* 230:    */   {
/* 231:356 */     for (int i = 0; i < this.data.length; i++) {
/* 232:357 */       this.data[i] += d;
/* 233:    */     }
/* 234:359 */     return this;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public RealVector mapSubtractToSelf(double d)
/* 238:    */   {
/* 239:365 */     for (int i = 0; i < this.data.length; i++) {
/* 240:366 */       this.data[i] -= d;
/* 241:    */     }
/* 242:368 */     return this;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public RealVector mapMultiplyToSelf(double d)
/* 246:    */   {
/* 247:374 */     for (int i = 0; i < this.data.length; i++) {
/* 248:375 */       this.data[i] *= d;
/* 249:    */     }
/* 250:377 */     return this;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public RealVector mapDivideToSelf(double d)
/* 254:    */   {
/* 255:383 */     for (int i = 0; i < this.data.length; i++) {
/* 256:384 */       this.data[i] /= d;
/* 257:    */     }
/* 258:386 */     return this;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public ArrayRealVector ebeMultiply(RealVector v)
/* 262:    */   {
/* 263:392 */     if ((v instanceof ArrayRealVector))
/* 264:    */     {
/* 265:393 */       double[] vData = ((ArrayRealVector)v).data;
/* 266:394 */       int dim = vData.length;
/* 267:395 */       checkVectorDimensions(dim);
/* 268:396 */       ArrayRealVector result = new ArrayRealVector(dim);
/* 269:397 */       double[] resultData = result.data;
/* 270:398 */       for (int i = 0; i < dim; i++) {
/* 271:399 */         resultData[i] = (this.data[i] * vData[i]);
/* 272:    */       }
/* 273:401 */       return result;
/* 274:    */     }
/* 275:403 */     checkVectorDimensions(v);
/* 276:404 */     double[] out = (double[])this.data.clone();
/* 277:405 */     for (int i = 0; i < this.data.length; i++) {
/* 278:406 */       out[i] *= v.getEntry(i);
/* 279:    */     }
/* 280:408 */     return new ArrayRealVector(out, false);
/* 281:    */   }
/* 282:    */   
/* 283:    */   public ArrayRealVector ebeDivide(RealVector v)
/* 284:    */   {
/* 285:415 */     if ((v instanceof ArrayRealVector))
/* 286:    */     {
/* 287:416 */       double[] vData = ((ArrayRealVector)v).data;
/* 288:417 */       int dim = vData.length;
/* 289:418 */       checkVectorDimensions(dim);
/* 290:419 */       ArrayRealVector result = new ArrayRealVector(dim);
/* 291:420 */       double[] resultData = result.data;
/* 292:421 */       for (int i = 0; i < dim; i++) {
/* 293:422 */         resultData[i] = (this.data[i] / vData[i]);
/* 294:    */       }
/* 295:424 */       return result;
/* 296:    */     }
/* 297:426 */     checkVectorDimensions(v);
/* 298:427 */     double[] out = (double[])this.data.clone();
/* 299:428 */     for (int i = 0; i < this.data.length; i++) {
/* 300:429 */       out[i] /= v.getEntry(i);
/* 301:    */     }
/* 302:431 */     return new ArrayRealVector(out, false);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public double[] getDataRef()
/* 306:    */   {
/* 307:442 */     return this.data;
/* 308:    */   }
/* 309:    */   
/* 310:    */   public double dotProduct(RealVector v)
/* 311:    */   {
/* 312:448 */     if ((v instanceof ArrayRealVector))
/* 313:    */     {
/* 314:449 */       double[] vData = ((ArrayRealVector)v).data;
/* 315:450 */       checkVectorDimensions(vData.length);
/* 316:451 */       double dot = 0.0D;
/* 317:452 */       for (int i = 0; i < this.data.length; i++) {
/* 318:453 */         dot += this.data[i] * vData[i];
/* 319:    */       }
/* 320:455 */       return dot;
/* 321:    */     }
/* 322:457 */     checkVectorDimensions(v);
/* 323:458 */     double dot = 0.0D;
/* 324:459 */     Iterator<RealVector.Entry> it = v.sparseIterator();
/* 325:    */     RealVector.Entry e;
/* 326:461 */     while ((it.hasNext()) && ((e = (RealVector.Entry)it.next()) != null)) {
/* 327:462 */       dot += this.data[e.getIndex()] * e.getValue();
/* 328:    */     }
/* 329:464 */     return dot;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public double getNorm()
/* 333:    */   {
/* 334:471 */     double sum = 0.0D;
/* 335:472 */     for (double a : this.data) {
/* 336:473 */       sum += a * a;
/* 337:    */     }
/* 338:475 */     return FastMath.sqrt(sum);
/* 339:    */   }
/* 340:    */   
/* 341:    */   public double getL1Norm()
/* 342:    */   {
/* 343:481 */     double sum = 0.0D;
/* 344:482 */     for (double a : this.data) {
/* 345:483 */       sum += FastMath.abs(a);
/* 346:    */     }
/* 347:485 */     return sum;
/* 348:    */   }
/* 349:    */   
/* 350:    */   public double getLInfNorm()
/* 351:    */   {
/* 352:491 */     double max = 0.0D;
/* 353:492 */     for (double a : this.data) {
/* 354:493 */       max = FastMath.max(max, FastMath.abs(a));
/* 355:    */     }
/* 356:495 */     return max;
/* 357:    */   }
/* 358:    */   
/* 359:    */   public double getDistance(RealVector v)
/* 360:    */   {
/* 361:501 */     if ((v instanceof ArrayRealVector))
/* 362:    */     {
/* 363:502 */       double[] vData = ((ArrayRealVector)v).data;
/* 364:503 */       checkVectorDimensions(vData.length);
/* 365:504 */       double sum = 0.0D;
/* 366:505 */       for (int i = 0; i < this.data.length; i++)
/* 367:    */       {
/* 368:506 */         double delta = this.data[i] - vData[i];
/* 369:507 */         sum += delta * delta;
/* 370:    */       }
/* 371:509 */       return FastMath.sqrt(sum);
/* 372:    */     }
/* 373:511 */     checkVectorDimensions(v);
/* 374:512 */     double sum = 0.0D;
/* 375:513 */     for (int i = 0; i < this.data.length; i++)
/* 376:    */     {
/* 377:514 */       double delta = this.data[i] - v.getEntry(i);
/* 378:515 */       sum += delta * delta;
/* 379:    */     }
/* 380:517 */     return FastMath.sqrt(sum);
/* 381:    */   }
/* 382:    */   
/* 383:    */   public double getL1Distance(RealVector v)
/* 384:    */   {
/* 385:524 */     if ((v instanceof ArrayRealVector))
/* 386:    */     {
/* 387:525 */       double[] vData = ((ArrayRealVector)v).data;
/* 388:526 */       checkVectorDimensions(vData.length);
/* 389:527 */       double sum = 0.0D;
/* 390:528 */       for (int i = 0; i < this.data.length; i++)
/* 391:    */       {
/* 392:529 */         double delta = this.data[i] - vData[i];
/* 393:530 */         sum += FastMath.abs(delta);
/* 394:    */       }
/* 395:532 */       return sum;
/* 396:    */     }
/* 397:534 */     checkVectorDimensions(v);
/* 398:535 */     double sum = 0.0D;
/* 399:536 */     for (int i = 0; i < this.data.length; i++)
/* 400:    */     {
/* 401:537 */       double delta = this.data[i] - v.getEntry(i);
/* 402:538 */       sum += FastMath.abs(delta);
/* 403:    */     }
/* 404:540 */     return sum;
/* 405:    */   }
/* 406:    */   
/* 407:    */   public double getLInfDistance(RealVector v)
/* 408:    */   {
/* 409:547 */     if ((v instanceof ArrayRealVector))
/* 410:    */     {
/* 411:548 */       double[] vData = ((ArrayRealVector)v).data;
/* 412:549 */       checkVectorDimensions(vData.length);
/* 413:550 */       double max = 0.0D;
/* 414:551 */       for (int i = 0; i < this.data.length; i++)
/* 415:    */       {
/* 416:552 */         double delta = this.data[i] - vData[i];
/* 417:553 */         max = FastMath.max(max, FastMath.abs(delta));
/* 418:    */       }
/* 419:555 */       return max;
/* 420:    */     }
/* 421:557 */     checkVectorDimensions(v);
/* 422:558 */     double max = 0.0D;
/* 423:559 */     for (int i = 0; i < this.data.length; i++)
/* 424:    */     {
/* 425:560 */       double delta = this.data[i] - v.getEntry(i);
/* 426:561 */       max = FastMath.max(max, FastMath.abs(delta));
/* 427:    */     }
/* 428:563 */     return max;
/* 429:    */   }
/* 430:    */   
/* 431:    */   public RealVector unitVector()
/* 432:    */   {
/* 433:570 */     double norm = getNorm();
/* 434:571 */     if (norm == 0.0D) {
/* 435:572 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
/* 436:    */     }
/* 437:574 */     return mapDivide(norm);
/* 438:    */   }
/* 439:    */   
/* 440:    */   public void unitize()
/* 441:    */   {
/* 442:580 */     double norm = getNorm();
/* 443:581 */     if (norm == 0.0D) {
/* 444:582 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
/* 445:    */     }
/* 446:584 */     mapDivideToSelf(norm);
/* 447:    */   }
/* 448:    */   
/* 449:    */   public RealVector projection(RealVector v)
/* 450:    */   {
/* 451:590 */     return v.mapMultiply(dotProduct(v) / v.dotProduct(v));
/* 452:    */   }
/* 453:    */   
/* 454:    */   public RealMatrix outerProduct(RealVector v)
/* 455:    */   {
/* 456:596 */     if ((v instanceof ArrayRealVector))
/* 457:    */     {
/* 458:597 */       double[] vData = ((ArrayRealVector)v).data;
/* 459:598 */       int m = this.data.length;
/* 460:599 */       int n = vData.length;
/* 461:600 */       RealMatrix out = MatrixUtils.createRealMatrix(m, n);
/* 462:601 */       for (int i = 0; i < m; i++) {
/* 463:602 */         for (int j = 0; j < n; j++) {
/* 464:603 */           out.setEntry(i, j, this.data[i] * vData[j]);
/* 465:    */         }
/* 466:    */       }
/* 467:606 */       return out;
/* 468:    */     }
/* 469:608 */     int m = this.data.length;
/* 470:609 */     int n = v.getDimension();
/* 471:610 */     RealMatrix out = MatrixUtils.createRealMatrix(m, n);
/* 472:611 */     for (int i = 0; i < m; i++) {
/* 473:612 */       for (int j = 0; j < n; j++) {
/* 474:613 */         out.setEntry(i, j, this.data[i] * v.getEntry(j));
/* 475:    */       }
/* 476:    */     }
/* 477:616 */     return out;
/* 478:    */   }
/* 479:    */   
/* 480:    */   public double getEntry(int index)
/* 481:    */   {
/* 482:623 */     return this.data[index];
/* 483:    */   }
/* 484:    */   
/* 485:    */   public int getDimension()
/* 486:    */   {
/* 487:629 */     return this.data.length;
/* 488:    */   }
/* 489:    */   
/* 490:    */   public RealVector append(RealVector v)
/* 491:    */   {
/* 492:    */     try
/* 493:    */     {
/* 494:636 */       return new ArrayRealVector(this, (ArrayRealVector)v);
/* 495:    */     }
/* 496:    */     catch (ClassCastException cce) {}
/* 497:638 */     return new ArrayRealVector(this, v);
/* 498:    */   }
/* 499:    */   
/* 500:    */   public ArrayRealVector append(ArrayRealVector v)
/* 501:    */   {
/* 502:649 */     return new ArrayRealVector(this, v);
/* 503:    */   }
/* 504:    */   
/* 505:    */   public RealVector append(double in)
/* 506:    */   {
/* 507:655 */     double[] out = new double[this.data.length + 1];
/* 508:656 */     System.arraycopy(this.data, 0, out, 0, this.data.length);
/* 509:657 */     out[this.data.length] = in;
/* 510:658 */     return new ArrayRealVector(out, false);
/* 511:    */   }
/* 512:    */   
/* 513:    */   public RealVector getSubVector(int index, int n)
/* 514:    */   {
/* 515:664 */     ArrayRealVector out = new ArrayRealVector(n);
/* 516:    */     try
/* 517:    */     {
/* 518:666 */       System.arraycopy(this.data, index, out.data, 0, n);
/* 519:    */     }
/* 520:    */     catch (IndexOutOfBoundsException e)
/* 521:    */     {
/* 522:668 */       checkIndex(index);
/* 523:669 */       checkIndex(index + n - 1);
/* 524:    */     }
/* 525:671 */     return out;
/* 526:    */   }
/* 527:    */   
/* 528:    */   public void setEntry(int index, double value)
/* 529:    */   {
/* 530:    */     try
/* 531:    */     {
/* 532:678 */       this.data[index] = value;
/* 533:    */     }
/* 534:    */     catch (IndexOutOfBoundsException e)
/* 535:    */     {
/* 536:680 */       checkIndex(index);
/* 537:    */     }
/* 538:    */   }
/* 539:    */   
/* 540:    */   public void addToEntry(int index, double increment)
/* 541:    */   {
/* 542:687 */     this.data[index] += increment;
/* 543:    */   }
/* 544:    */   
/* 545:    */   public void setSubVector(int index, RealVector v)
/* 546:    */   {
/* 547:693 */     if ((v instanceof ArrayRealVector)) {
/* 548:694 */       setSubVector(index, ((ArrayRealVector)v).data);
/* 549:    */     } else {
/* 550:    */       try
/* 551:    */       {
/* 552:697 */         for (int i = index; i < index + v.getDimension(); i++) {
/* 553:698 */           this.data[i] = v.getEntry(i - index);
/* 554:    */         }
/* 555:    */       }
/* 556:    */       catch (IndexOutOfBoundsException e)
/* 557:    */       {
/* 558:701 */         checkIndex(index);
/* 559:702 */         checkIndex(index + v.getDimension() - 1);
/* 560:    */       }
/* 561:    */     }
/* 562:    */   }
/* 563:    */   
/* 564:    */   public void setSubVector(int index, double[] v)
/* 565:    */   {
/* 566:    */     try
/* 567:    */     {
/* 568:717 */       System.arraycopy(v, 0, this.data, index, v.length);
/* 569:    */     }
/* 570:    */     catch (IndexOutOfBoundsException e)
/* 571:    */     {
/* 572:719 */       checkIndex(index);
/* 573:720 */       checkIndex(index + v.length - 1);
/* 574:    */     }
/* 575:    */   }
/* 576:    */   
/* 577:    */   public void set(double value)
/* 578:    */   {
/* 579:727 */     Arrays.fill(this.data, value);
/* 580:    */   }
/* 581:    */   
/* 582:    */   public double[] toArray()
/* 583:    */   {
/* 584:733 */     return (double[])this.data.clone();
/* 585:    */   }
/* 586:    */   
/* 587:    */   public String toString()
/* 588:    */   {
/* 589:739 */     return DEFAULT_FORMAT.format(this);
/* 590:    */   }
/* 591:    */   
/* 592:    */   protected void checkVectorDimensions(RealVector v)
/* 593:    */   {
/* 594:751 */     checkVectorDimensions(v.getDimension());
/* 595:    */   }
/* 596:    */   
/* 597:    */   protected void checkVectorDimensions(int n)
/* 598:    */   {
/* 599:763 */     if (this.data.length != n) {
/* 600:764 */       throw new DimensionMismatchException(this.data.length, n);
/* 601:    */     }
/* 602:    */   }
/* 603:    */   
/* 604:    */   public boolean isNaN()
/* 605:    */   {
/* 606:776 */     for (double v : this.data) {
/* 607:777 */       if (Double.isNaN(v)) {
/* 608:778 */         return true;
/* 609:    */       }
/* 610:    */     }
/* 611:781 */     return false;
/* 612:    */   }
/* 613:    */   
/* 614:    */   public boolean isInfinite()
/* 615:    */   {
/* 616:793 */     if (isNaN()) {
/* 617:794 */       return false;
/* 618:    */     }
/* 619:797 */     for (double v : this.data) {
/* 620:798 */       if (Double.isInfinite(v)) {
/* 621:799 */         return true;
/* 622:    */       }
/* 623:    */     }
/* 624:803 */     return false;
/* 625:    */   }
/* 626:    */   
/* 627:    */   public boolean equals(Object other)
/* 628:    */   {
/* 629:822 */     if (this == other) {
/* 630:823 */       return true;
/* 631:    */     }
/* 632:826 */     if ((other == null) || (!(other instanceof RealVector))) {
/* 633:827 */       return false;
/* 634:    */     }
/* 635:830 */     RealVector rhs = (RealVector)other;
/* 636:831 */     if (this.data.length != rhs.getDimension()) {
/* 637:832 */       return false;
/* 638:    */     }
/* 639:835 */     if (rhs.isNaN()) {
/* 640:836 */       return isNaN();
/* 641:    */     }
/* 642:839 */     for (int i = 0; i < this.data.length; i++) {
/* 643:840 */       if (this.data[i] != rhs.getEntry(i)) {
/* 644:841 */         return false;
/* 645:    */       }
/* 646:    */     }
/* 647:844 */     return true;
/* 648:    */   }
/* 649:    */   
/* 650:    */   public int hashCode()
/* 651:    */   {
/* 652:855 */     if (isNaN()) {
/* 653:856 */       return 9;
/* 654:    */     }
/* 655:858 */     return MathUtils.hash(this.data);
/* 656:    */   }
/* 657:    */   
/* 658:    */   public ArrayRealVector combine(double a, double b, RealVector y)
/* 659:    */   {
/* 660:864 */     return copy().combineToSelf(a, b, y);
/* 661:    */   }
/* 662:    */   
/* 663:    */   public ArrayRealVector combineToSelf(double a, double b, RealVector y)
/* 664:    */   {
/* 665:870 */     if ((y instanceof ArrayRealVector))
/* 666:    */     {
/* 667:871 */       double[] yData = ((ArrayRealVector)y).data;
/* 668:872 */       checkVectorDimensions(yData.length);
/* 669:873 */       for (int i = 0; i < this.data.length; i++) {
/* 670:874 */         this.data[i] = (a * this.data[i] + b * yData[i]);
/* 671:    */       }
/* 672:    */     }
/* 673:    */     else
/* 674:    */     {
/* 675:877 */       checkVectorDimensions(y);
/* 676:878 */       for (int i = 0; i < this.data.length; i++) {
/* 677:879 */         this.data[i] = (a * this.data[i] + b * y.getEntry(i));
/* 678:    */       }
/* 679:    */     }
/* 680:882 */     return this;
/* 681:    */   }
/* 682:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.ArrayRealVector
 * JD-Core Version:    0.7.0.1
 */
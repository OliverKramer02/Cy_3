/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
/*  10:    */ 
/*  11:    */ public class OpenMapRealVector
/*  12:    */   extends SparseRealVector
/*  13:    */   implements Serializable
/*  14:    */ {
/*  15:    */   public static final double DEFAULT_ZERO_TOLERANCE = 1.0E-012D;
/*  16:    */   private static final long serialVersionUID = 8772222695580707260L;
/*  17:    */   private final OpenIntToDoubleHashMap entries;
/*  18:    */   private final int virtualSize;
/*  19:    */   private final double epsilon;
/*  20:    */   
/*  21:    */   public OpenMapRealVector()
/*  22:    */   {
/*  23: 55 */     this(0, 1.0E-012D);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public OpenMapRealVector(int dimension)
/*  27:    */   {
/*  28: 64 */     this(dimension, 1.0E-012D);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public OpenMapRealVector(int dimension, double epsilon)
/*  32:    */   {
/*  33: 74 */     this.virtualSize = dimension;
/*  34: 75 */     this.entries = new OpenIntToDoubleHashMap(0.0D);
/*  35: 76 */     this.epsilon = epsilon;
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected OpenMapRealVector(OpenMapRealVector v, int resize)
/*  39:    */   {
/*  40: 86 */     this.virtualSize = (v.getDimension() + resize);
/*  41: 87 */     this.entries = new OpenIntToDoubleHashMap(v.entries);
/*  42: 88 */     this.epsilon = v.epsilon;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public OpenMapRealVector(int dimension, int expectedSize)
/*  46:    */   {
/*  47: 98 */     this(dimension, expectedSize, 1.0E-012D);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public OpenMapRealVector(int dimension, int expectedSize, double epsilon)
/*  51:    */   {
/*  52:110 */     this.virtualSize = dimension;
/*  53:111 */     this.entries = new OpenIntToDoubleHashMap(expectedSize, 0.0D);
/*  54:112 */     this.epsilon = epsilon;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public OpenMapRealVector(double[] values)
/*  58:    */   {
/*  59:122 */     this(values, 1.0E-012D);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public OpenMapRealVector(double[] values, double epsilon)
/*  63:    */   {
/*  64:133 */     this.virtualSize = values.length;
/*  65:134 */     this.entries = new OpenIntToDoubleHashMap(0.0D);
/*  66:135 */     this.epsilon = epsilon;
/*  67:136 */     for (int key = 0; key < values.length; key++)
/*  68:    */     {
/*  69:137 */       double value = values[key];
/*  70:138 */       if (!isDefaultValue(value)) {
/*  71:139 */         this.entries.put(key, value);
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public OpenMapRealVector(Double[] values)
/*  77:    */   {
/*  78:151 */     this(values, 1.0E-012D);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public OpenMapRealVector(Double[] values, double epsilon)
/*  82:    */   {
/*  83:162 */     this.virtualSize = values.length;
/*  84:163 */     this.entries = new OpenIntToDoubleHashMap(0.0D);
/*  85:164 */     this.epsilon = epsilon;
/*  86:165 */     for (int key = 0; key < values.length; key++)
/*  87:    */     {
/*  88:166 */       double value = values[key].doubleValue();
/*  89:167 */       if (!isDefaultValue(value)) {
/*  90:168 */         this.entries.put(key, value);
/*  91:    */       }
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public OpenMapRealVector(OpenMapRealVector v)
/*  96:    */   {
/*  97:179 */     this.virtualSize = v.getDimension();
/*  98:180 */     this.entries = new OpenIntToDoubleHashMap(v.getEntries());
/*  99:181 */     this.epsilon = v.epsilon;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public OpenMapRealVector(RealVector v)
/* 103:    */   {
/* 104:190 */     this.virtualSize = v.getDimension();
/* 105:191 */     this.entries = new OpenIntToDoubleHashMap(0.0D);
/* 106:192 */     this.epsilon = 1.0E-012D;
/* 107:193 */     for (int key = 0; key < this.virtualSize; key++)
/* 108:    */     {
/* 109:194 */       double value = v.getEntry(key);
/* 110:195 */       if (!isDefaultValue(value)) {
/* 111:196 */         this.entries.put(key, value);
/* 112:    */       }
/* 113:    */     }
/* 114:    */   }
/* 115:    */   
/* 116:    */   private OpenIntToDoubleHashMap getEntries()
/* 117:    */   {
/* 118:207 */     return this.entries;
/* 119:    */   }
/* 120:    */   
/* 121:    */   protected boolean isDefaultValue(double value)
/* 122:    */   {
/* 123:219 */     return FastMath.abs(value) < this.epsilon;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public RealVector add(RealVector v)
/* 127:    */   {
/* 128:225 */     checkVectorDimensions(v.getDimension());
/* 129:226 */     if ((v instanceof OpenMapRealVector)) {
/* 130:227 */       return add((OpenMapRealVector)v);
/* 131:    */     }
/* 132:229 */     return super.add(v);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public OpenMapRealVector add(OpenMapRealVector v)
/* 136:    */   {
/* 137:243 */     checkVectorDimensions(v.getDimension());
/* 138:244 */     boolean copyThis = this.entries.size() > v.entries.size();
/* 139:245 */     OpenMapRealVector res = copyThis ? copy() : v.copy();
/* 140:246 */     OpenIntToDoubleHashMap.Iterator iter = copyThis ? v.entries.iterator() : this.entries.iterator();
/* 141:247 */     OpenIntToDoubleHashMap randomAccess = copyThis ? this.entries : v.entries;
/* 142:248 */     while (iter.hasNext())
/* 143:    */     {
/* 144:249 */       iter.advance();
/* 145:250 */       int key = iter.key();
/* 146:251 */       if (randomAccess.containsKey(key)) {
/* 147:252 */         res.setEntry(key, randomAccess.get(key) + iter.value());
/* 148:    */       } else {
/* 149:254 */         res.setEntry(key, iter.value());
/* 150:    */       }
/* 151:    */     }
/* 152:257 */     return res;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public OpenMapRealVector append(OpenMapRealVector v)
/* 156:    */   {
/* 157:266 */     OpenMapRealVector res = new OpenMapRealVector(this, v.getDimension());
/* 158:267 */     OpenIntToDoubleHashMap.Iterator iter = v.entries.iterator();
/* 159:268 */     while (iter.hasNext())
/* 160:    */     {
/* 161:269 */       iter.advance();
/* 162:270 */       res.setEntry(iter.key() + this.virtualSize, iter.value());
/* 163:    */     }
/* 164:272 */     return res;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public OpenMapRealVector append(RealVector v)
/* 168:    */   {
/* 169:278 */     if ((v instanceof OpenMapRealVector)) {
/* 170:279 */       return append((OpenMapRealVector)v);
/* 171:    */     }
/* 172:281 */     OpenMapRealVector res = new OpenMapRealVector(this, v.getDimension());
/* 173:282 */     for (int i = 0; i < v.getDimension(); i++) {
/* 174:283 */       res.setEntry(i + this.virtualSize, v.getEntry(i));
/* 175:    */     }
/* 176:285 */     return res;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public OpenMapRealVector append(double d)
/* 180:    */   {
/* 181:292 */     OpenMapRealVector res = new OpenMapRealVector(this, 1);
/* 182:293 */     res.setEntry(this.virtualSize, d);
/* 183:294 */     return res;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public OpenMapRealVector copy()
/* 187:    */   {
/* 188:303 */     return new OpenMapRealVector(this);
/* 189:    */   }
/* 190:    */   
/* 191:    */   public double dotProduct(OpenMapRealVector v)
/* 192:    */   {
/* 193:316 */     checkVectorDimensions(v.getDimension());
/* 194:317 */     boolean thisIsSmaller = this.entries.size() < v.entries.size();
/* 195:318 */     OpenIntToDoubleHashMap.Iterator iter = thisIsSmaller ? this.entries.iterator() : v.entries.iterator();
/* 196:319 */     OpenIntToDoubleHashMap larger = thisIsSmaller ? v.entries : this.entries;
/* 197:320 */     double d = 0.0D;
/* 198:321 */     while (iter.hasNext())
/* 199:    */     {
/* 200:322 */       iter.advance();
/* 201:323 */       d += iter.value() * larger.get(iter.key());
/* 202:    */     }
/* 203:325 */     return d;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public double dotProduct(RealVector v)
/* 207:    */   {
/* 208:331 */     if ((v instanceof OpenMapRealVector)) {
/* 209:332 */       return dotProduct((OpenMapRealVector)v);
/* 210:    */     }
/* 211:334 */     return super.dotProduct(v);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public OpenMapRealVector ebeDivide(RealVector v)
/* 215:    */   {
/* 216:341 */     checkVectorDimensions(v.getDimension());
/* 217:342 */     OpenMapRealVector res = new OpenMapRealVector(this);
/* 218:343 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 219:344 */     while (iter.hasNext())
/* 220:    */     {
/* 221:345 */       iter.advance();
/* 222:346 */       res.setEntry(iter.key(), iter.value() / v.getEntry(iter.key()));
/* 223:    */     }
/* 224:348 */     return res;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public OpenMapRealVector ebeMultiply(RealVector v)
/* 228:    */   {
/* 229:354 */     checkVectorDimensions(v.getDimension());
/* 230:355 */     OpenMapRealVector res = new OpenMapRealVector(this);
/* 231:356 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 232:357 */     while (iter.hasNext())
/* 233:    */     {
/* 234:358 */       iter.advance();
/* 235:359 */       res.setEntry(iter.key(), iter.value() * v.getEntry(iter.key()));
/* 236:    */     }
/* 237:361 */     return res;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public OpenMapRealVector getSubVector(int index, int n)
/* 241:    */   {
/* 242:367 */     checkIndex(index);
/* 243:368 */     checkIndex(index + n - 1);
/* 244:369 */     OpenMapRealVector res = new OpenMapRealVector(n);
/* 245:370 */     int end = index + n;
/* 246:371 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 247:372 */     while (iter.hasNext())
/* 248:    */     {
/* 249:373 */       iter.advance();
/* 250:374 */       int key = iter.key();
/* 251:375 */       if ((key >= index) && (key < end)) {
/* 252:376 */         res.setEntry(key - index, iter.value());
/* 253:    */       }
/* 254:    */     }
/* 255:379 */     return res;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public int getDimension()
/* 259:    */   {
/* 260:385 */     return this.virtualSize;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public double getDistance(OpenMapRealVector v)
/* 264:    */   {
/* 265:397 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 266:398 */     double res = 0.0D;
/* 267:399 */     while (iter.hasNext())
/* 268:    */     {
/* 269:400 */       iter.advance();
/* 270:401 */       int key = iter.key();
/* 271:    */       
/* 272:403 */       double delta = iter.value() - v.getEntry(key);
/* 273:404 */       res += delta * delta;
/* 274:    */     }
/* 275:406 */     iter = v.getEntries().iterator();
/* 276:407 */     while (iter.hasNext())
/* 277:    */     {
/* 278:408 */       iter.advance();
/* 279:409 */       int key = iter.key();
/* 280:410 */       if (!this.entries.containsKey(key))
/* 281:    */       {
/* 282:411 */         double value = iter.value();
/* 283:412 */         res += value * value;
/* 284:    */       }
/* 285:    */     }
/* 286:415 */     return FastMath.sqrt(res);
/* 287:    */   }
/* 288:    */   
/* 289:    */   public double getDistance(RealVector v)
/* 290:    */   {
/* 291:421 */     checkVectorDimensions(v.getDimension());
/* 292:422 */     if ((v instanceof OpenMapRealVector)) {
/* 293:423 */       return getDistance((OpenMapRealVector)v);
/* 294:    */     }
/* 295:425 */     return super.getDistance(v);
/* 296:    */   }
/* 297:    */   
/* 298:    */   public double getEntry(int index)
/* 299:    */   {
/* 300:432 */     checkIndex(index);
/* 301:433 */     return this.entries.get(index);
/* 302:    */   }
/* 303:    */   
/* 304:    */   public double getL1Distance(OpenMapRealVector v)
/* 305:    */   {
/* 306:446 */     double max = 0.0D;
/* 307:447 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 308:448 */     while (iter.hasNext())
/* 309:    */     {
/* 310:449 */       iter.advance();
/* 311:450 */       double delta = FastMath.abs(iter.value() - v.getEntry(iter.key()));
/* 312:451 */       max += delta;
/* 313:    */     }
/* 314:453 */     iter = v.getEntries().iterator();
/* 315:454 */     while (iter.hasNext())
/* 316:    */     {
/* 317:455 */       iter.advance();
/* 318:456 */       int key = iter.key();
/* 319:457 */       if (!this.entries.containsKey(key))
/* 320:    */       {
/* 321:458 */         double delta = FastMath.abs(iter.value());
/* 322:459 */         max += FastMath.abs(delta);
/* 323:    */       }
/* 324:    */     }
/* 325:462 */     return max;
/* 326:    */   }
/* 327:    */   
/* 328:    */   public double getL1Distance(RealVector v)
/* 329:    */   {
/* 330:468 */     checkVectorDimensions(v.getDimension());
/* 331:469 */     if ((v instanceof OpenMapRealVector)) {
/* 332:470 */       return getL1Distance((OpenMapRealVector)v);
/* 333:    */     }
/* 334:472 */     return super.getL1Distance(v);
/* 335:    */   }
/* 336:    */   
/* 337:    */   private double getLInfDistance(OpenMapRealVector v)
/* 338:    */   {
/* 339:483 */     double max = 0.0D;
/* 340:484 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 341:485 */     while (iter.hasNext())
/* 342:    */     {
/* 343:486 */       iter.advance();
/* 344:487 */       double delta = FastMath.abs(iter.value() - v.getEntry(iter.key()));
/* 345:488 */       if (delta > max) {
/* 346:489 */         max = delta;
/* 347:    */       }
/* 348:    */     }
/* 349:492 */     iter = v.getEntries().iterator();
/* 350:493 */     while (iter.hasNext())
/* 351:    */     {
/* 352:494 */       iter.advance();
/* 353:495 */       int key = iter.key();
/* 354:496 */       if ((!this.entries.containsKey(key)) && 
/* 355:497 */         (iter.value() > max)) {
/* 356:498 */         max = iter.value();
/* 357:    */       }
/* 358:    */     }
/* 359:502 */     return max;
/* 360:    */   }
/* 361:    */   
/* 362:    */   public double getLInfDistance(RealVector v)
/* 363:    */   {
/* 364:508 */     checkVectorDimensions(v.getDimension());
/* 365:509 */     if ((v instanceof OpenMapRealVector)) {
/* 366:510 */       return getLInfDistance((OpenMapRealVector)v);
/* 367:    */     }
/* 368:512 */     return super.getLInfDistance(v);
/* 369:    */   }
/* 370:    */   
/* 371:    */   public boolean isInfinite()
/* 372:    */   {
/* 373:519 */     boolean infiniteFound = false;
/* 374:520 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 375:521 */     while (iter.hasNext())
/* 376:    */     {
/* 377:522 */       iter.advance();
/* 378:523 */       double value = iter.value();
/* 379:524 */       if (Double.isNaN(value)) {
/* 380:525 */         return false;
/* 381:    */       }
/* 382:527 */       if (Double.isInfinite(value)) {
/* 383:528 */         infiniteFound = true;
/* 384:    */       }
/* 385:    */     }
/* 386:531 */     return infiniteFound;
/* 387:    */   }
/* 388:    */   
/* 389:    */   public boolean isNaN()
/* 390:    */   {
/* 391:537 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 392:538 */     while (iter.hasNext())
/* 393:    */     {
/* 394:539 */       iter.advance();
/* 395:540 */       if (Double.isNaN(iter.value())) {
/* 396:541 */         return true;
/* 397:    */       }
/* 398:    */     }
/* 399:544 */     return false;
/* 400:    */   }
/* 401:    */   
/* 402:    */   public OpenMapRealVector mapAdd(double d)
/* 403:    */   {
/* 404:550 */     return copy().mapAddToSelf(d);
/* 405:    */   }
/* 406:    */   
/* 407:    */   public OpenMapRealVector mapAddToSelf(double d)
/* 408:    */   {
/* 409:556 */     for (int i = 0; i < this.virtualSize; i++) {
/* 410:557 */       setEntry(i, getEntry(i) + d);
/* 411:    */     }
/* 412:559 */     return this;
/* 413:    */   }
/* 414:    */   
/* 415:    */   public RealVector projection(RealVector v)
/* 416:    */   {
/* 417:565 */     checkVectorDimensions(v.getDimension());
/* 418:566 */     return v.mapMultiply(dotProduct(v) / v.dotProduct(v));
/* 419:    */   }
/* 420:    */   
/* 421:    */   public void setEntry(int index, double value)
/* 422:    */   {
/* 423:572 */     checkIndex(index);
/* 424:573 */     if (!isDefaultValue(value)) {
/* 425:574 */       this.entries.put(index, value);
/* 426:575 */     } else if (this.entries.containsKey(index)) {
/* 427:576 */       this.entries.remove(index);
/* 428:    */     }
/* 429:    */   }
/* 430:    */   
/* 431:    */   public void setSubVector(int index, RealVector v)
/* 432:    */   {
/* 433:583 */     checkIndex(index);
/* 434:584 */     checkIndex(index + v.getDimension() - 1);
/* 435:585 */     for (int i = 0; i < v.getDimension(); i++) {
/* 436:586 */       setEntry(i + index, v.getEntry(i));
/* 437:    */     }
/* 438:    */   }
/* 439:    */   
/* 440:    */   public void set(double value)
/* 441:    */   {
/* 442:593 */     for (int i = 0; i < this.virtualSize; i++) {
/* 443:594 */       setEntry(i, value);
/* 444:    */     }
/* 445:    */   }
/* 446:    */   
/* 447:    */   public OpenMapRealVector subtract(OpenMapRealVector v)
/* 448:    */   {
/* 449:607 */     checkVectorDimensions(v.getDimension());
/* 450:608 */     OpenMapRealVector res = copy();
/* 451:609 */     OpenIntToDoubleHashMap.Iterator iter = v.getEntries().iterator();
/* 452:610 */     while (iter.hasNext())
/* 453:    */     {
/* 454:611 */       iter.advance();
/* 455:612 */       int key = iter.key();
/* 456:613 */       if (this.entries.containsKey(key)) {
/* 457:614 */         res.setEntry(key, this.entries.get(key) - iter.value());
/* 458:    */       } else {
/* 459:616 */         res.setEntry(key, -iter.value());
/* 460:    */       }
/* 461:    */     }
/* 462:619 */     return res;
/* 463:    */   }
/* 464:    */   
/* 465:    */   public RealVector subtract(RealVector v)
/* 466:    */   {
/* 467:625 */     checkVectorDimensions(v.getDimension());
/* 468:626 */     if ((v instanceof OpenMapRealVector)) {
/* 469:627 */       return subtract((OpenMapRealVector)v);
/* 470:    */     }
/* 471:629 */     return super.subtract(v);
/* 472:    */   }
/* 473:    */   
/* 474:    */   public OpenMapRealVector unitVector()
/* 475:    */   {
/* 476:636 */     OpenMapRealVector res = copy();
/* 477:637 */     res.unitize();
/* 478:638 */     return res;
/* 479:    */   }
/* 480:    */   
/* 481:    */   public void unitize()
/* 482:    */   {
/* 483:644 */     double norm = getNorm();
/* 484:645 */     if (isDefaultValue(norm)) {
/* 485:646 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
/* 486:    */     }
/* 487:648 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 488:649 */     while (iter.hasNext())
/* 489:    */     {
/* 490:650 */       iter.advance();
/* 491:651 */       this.entries.put(iter.key(), iter.value() / norm);
/* 492:    */     }
/* 493:    */   }
/* 494:    */   
/* 495:    */   public double[] toArray()
/* 496:    */   {
/* 497:658 */     double[] res = new double[this.virtualSize];
/* 498:659 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 499:660 */     while (iter.hasNext())
/* 500:    */     {
/* 501:661 */       iter.advance();
/* 502:662 */       res[iter.key()] = iter.value();
/* 503:    */     }
/* 504:664 */     return res;
/* 505:    */   }
/* 506:    */   
/* 507:    */   public int hashCode()
/* 508:    */   {
/* 509:675 */     int prime = 31;
/* 510:676 */     int result = 1;
/* 511:    */     
/* 512:678 */     long temp = Double.doubleToLongBits(this.epsilon);
/* 513:679 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/* 514:680 */     result = 31 * result + this.virtualSize;
/* 515:681 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 516:682 */     while (iter.hasNext())
/* 517:    */     {
/* 518:683 */       iter.advance();
/* 519:684 */       temp = Double.doubleToLongBits(iter.value());
/* 520:685 */       result = 31 * result + (int)(temp ^ temp >> 32);
/* 521:    */     }
/* 522:687 */     return result;
/* 523:    */   }
/* 524:    */   
/* 525:    */   public boolean equals(Object obj)
/* 526:    */   {
/* 527:698 */     if (this == obj) {
/* 528:699 */       return true;
/* 529:    */     }
/* 530:701 */     if (!(obj instanceof OpenMapRealVector)) {
/* 531:702 */       return false;
/* 532:    */     }
/* 533:704 */     OpenMapRealVector other = (OpenMapRealVector)obj;
/* 534:705 */     if (this.virtualSize != other.virtualSize) {
/* 535:706 */       return false;
/* 536:    */     }
/* 537:708 */     if (Double.doubleToLongBits(this.epsilon) != Double.doubleToLongBits(other.epsilon)) {
/* 538:710 */       return false;
/* 539:    */     }
/* 540:712 */     OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
/* 541:713 */     while (iter.hasNext())
/* 542:    */     {
/* 543:714 */       iter.advance();
/* 544:715 */       double test = other.getEntry(iter.key());
/* 545:716 */       if (Double.doubleToLongBits(test) != Double.doubleToLongBits(iter.value())) {
/* 546:717 */         return false;
/* 547:    */       }
/* 548:    */     }
/* 549:720 */     iter = other.getEntries().iterator();
/* 550:721 */     while (iter.hasNext())
/* 551:    */     {
/* 552:722 */       iter.advance();
/* 553:723 */       double test = iter.value();
/* 554:724 */       if (Double.doubleToLongBits(test) != Double.doubleToLongBits(getEntry(iter.key()))) {
/* 555:725 */         return false;
/* 556:    */       }
/* 557:    */     }
/* 558:728 */     return true;
/* 559:    */   }
/* 560:    */   
/* 561:    */   public double getSparsity()
/* 562:    */   {
/* 563:737 */     return this.entries.size() / getDimension();
/* 564:    */   }
/* 565:    */   
/* 566:    */   public Iterator<RealVector.Entry> sparseIterator()
/* 567:    */   {
/* 568:743 */     return new OpenMapSparseIterator();
/* 569:    */   }
/* 570:    */   
/* 571:    */   protected class OpenMapEntry
/* 572:    */     extends RealVector.Entry
/* 573:    */   {
/* 574:    */     private final OpenIntToDoubleHashMap.Iterator iter;
/* 575:    */     
/* 576:    */     protected OpenMapEntry(OpenIntToDoubleHashMap.Iterator iter)
/* 577:    */     {
/* 578:760 */       super();
/* 579:761 */       this.iter = iter;
/* 580:    */     }
/* 581:    */     
/* 582:    */     public double getValue()
/* 583:    */     {
/* 584:767 */       return this.iter.value();
/* 585:    */     }
/* 586:    */     
/* 587:    */     public void setValue(double value)
/* 588:    */     {
/* 589:773 */       OpenMapRealVector.this.entries.put(this.iter.key(), value);
/* 590:    */     }
/* 591:    */     
/* 592:    */     public int getIndex()
/* 593:    */     {
/* 594:779 */       return this.iter.key();
/* 595:    */     }
/* 596:    */   }
/* 597:    */   
/* 598:    */   protected class OpenMapSparseIterator
/* 599:    */     implements Iterator<RealVector.Entry>
/* 600:    */   {
/* 601:    */     private final OpenIntToDoubleHashMap.Iterator iter;
/* 602:    */     private final RealVector.Entry current;
/* 603:    */     
/* 604:    */     protected OpenMapSparseIterator()
/* 605:    */     {
/* 606:797 */       this.iter = OpenMapRealVector.this.entries.iterator();
/* 607:798 */       this.current = new OpenMapRealVector.OpenMapEntry(this.iter);
/* 608:    */     }
/* 609:    */     
/* 610:    */     public boolean hasNext()
/* 611:    */     {
/* 612:803 */       return this.iter.hasNext();
/* 613:    */     }
/* 614:    */     
/* 615:    */     public RealVector.Entry next()
/* 616:    */     {
/* 617:808 */       this.iter.advance();
/* 618:809 */       return this.current;
/* 619:    */     }
/* 620:    */     
/* 621:    */     public void remove()
/* 622:    */     {
/* 623:814 */       throw new UnsupportedOperationException("Not supported");
/* 624:    */     }
/* 625:    */   }
/* 626:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.OpenMapRealVector
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.NoSuchElementException;

/*   6:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   7:    */ import org.apache.commons.math3.analysis.function.Add;
/*   8:    */ import org.apache.commons.math3.analysis.function.Divide;
/*   9:    */ import org.apache.commons.math3.analysis.function.Multiply;
/*  10:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  11:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*  12:    */ import org.apache.commons.math3.exception.MathUnsupportedOperationException;
/*  13:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  14:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  15:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*  16:    */ 
/*  17:    */ public abstract class RealVector
/*  18:    */ {
/*  19:    */   public abstract int getDimension();
/*  20:    */   
/*  21:    */   public abstract double getEntry(int paramInt);
/*  22:    */   
/*  23:    */   public abstract void setEntry(int paramInt, double paramDouble);
/*  24:    */   
/*  25:    */   public void addToEntry(int index, double increment)
/*  26:    */   {
/*  27: 98 */     setEntry(index, getEntry(index) + increment);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public abstract RealVector append(RealVector paramRealVector);
/*  31:    */   
/*  32:    */   public abstract RealVector append(double paramDouble);
/*  33:    */   
/*  34:    */   public abstract RealVector getSubVector(int paramInt1, int paramInt2);
/*  35:    */   
/*  36:    */   public abstract void setSubVector(int paramInt, RealVector paramRealVector);
/*  37:    */   
/*  38:    */   public abstract boolean isNaN();
/*  39:    */   
/*  40:    */   public abstract boolean isInfinite();
/*  41:    */   
/*  42:    */   protected void checkVectorDimensions(RealVector v)
/*  43:    */   {
/*  44:162 */     checkVectorDimensions(v.getDimension());
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected void checkVectorDimensions(int n)
/*  48:    */   {
/*  49:173 */     int d = getDimension();
/*  50:174 */     if (d != n) {
/*  51:175 */       throw new DimensionMismatchException(d, n);
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected void checkIndex(int index)
/*  56:    */   {
/*  57:186 */     if ((index < 0) || (index >= getDimension())) {
/*  58:188 */       throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(getDimension() - 1));
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public RealVector add(RealVector v)
/*  63:    */   {
/*  64:203 */     RealVector result = v.copy();
/*  65:204 */     Iterator<Entry> it = sparseIterator();
/*  66:    */     Entry e;
/*  67:206 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null))
/*  68:    */     {
/*  69:207 */       int index = e.getIndex();
/*  70:208 */       result.setEntry(index, e.getValue() + result.getEntry(index));
/*  71:    */     }
/*  72:210 */     return result;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public RealVector subtract(RealVector v)
/*  76:    */   {
/*  77:223 */     RealVector result = v.copy();
/*  78:224 */     Iterator<Entry> it = sparseIterator();
/*  79:    */     Entry e;
/*  80:226 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null))
/*  81:    */     {
/*  82:227 */       int index = e.getIndex();
/*  83:228 */       result.setEntry(index, e.getValue() - result.getEntry(index));
/*  84:    */     }
/*  85:230 */     return result;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public RealVector mapAdd(double d)
/*  89:    */   {
/*  90:241 */     return copy().mapAddToSelf(d);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public RealVector mapAddToSelf(double d)
/*  94:    */   {
/*  95:252 */     if (d != 0.0D) {
/*  96:253 */    //   return mapToSelf(FunctionUtils.fix2ndArgument(new Add(), d));
/*  97:    */     }
/*  98:255 */     return this;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public abstract RealVector copy();
/* 102:    */   
/* 103:    */   public double dotProduct(RealVector v)
/* 104:    */   {
/* 105:274 */     checkVectorDimensions(v);
/* 106:275 */     double d = 0.0D;
/* 107:276 */     Iterator<Entry> it = sparseIterator();
/* 108:    */     Entry e;
/* 109:278 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null)) {
/* 110:279 */       d += e.getValue() * v.getEntry(e.getIndex());
/* 111:    */     }
/* 112:281 */     return d;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public double cosine(RealVector v)
/* 116:    */   {
/* 117:292 */     double norm = getNorm();
/* 118:293 */     double vNorm = v.getNorm();
/* 119:295 */     if ((norm == 0.0D) || (vNorm == 0.0D)) {
/* 120:297 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
/* 121:    */     }
/* 122:299 */     return dotProduct(v) / (norm * vNorm);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public abstract RealVector ebeDivide(RealVector paramRealVector);
/* 126:    */   
/* 127:    */   public abstract RealVector ebeMultiply(RealVector paramRealVector);
/* 128:    */   
/* 129:    */   public double getDistance(RealVector v)
/* 130:    */   {
/* 131:337 */     checkVectorDimensions(v);
/* 132:338 */     double d = 0.0D;
/* 133:339 */     Iterator<Entry> it = iterator();
/* 134:    */     Entry e;
/* 135:341 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null))
/* 136:    */     {
/* 137:342 */       double diff = e.getValue() - v.getEntry(e.getIndex());
/* 138:343 */       d += diff * diff;
/* 139:    */     }
/* 140:345 */     return FastMath.sqrt(d);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public double getNorm()
/* 144:    */   {
/* 145:359 */     double sum = 0.0D;
/* 146:360 */     Iterator<Entry> it = sparseIterator();
/* 147:    */     Entry e;
/* 148:362 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null))
/* 149:    */     {
/* 150:363 */       double value = e.getValue();
/* 151:364 */       sum += value * value;
/* 152:    */     }
/* 153:366 */     return FastMath.sqrt(sum);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public double getL1Norm()
/* 157:    */   {
/* 158:380 */     double norm = 0.0D;
/* 159:381 */     Iterator<Entry> it = sparseIterator();
/* 160:    */     Entry e;
/* 161:383 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null)) {
/* 162:384 */       norm += FastMath.abs(e.getValue());
/* 163:    */     }
/* 164:386 */     return norm;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public double getLInfNorm()
/* 168:    */   {
/* 169:400 */     double norm = 0.0D;
/* 170:401 */     Iterator<Entry> it = sparseIterator();
/* 171:    */     Entry e;
/* 172:403 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null)) {
/* 173:404 */       norm = FastMath.max(norm, FastMath.abs(e.getValue()));
/* 174:    */     }
/* 175:406 */     return norm;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public double getL1Distance(RealVector v)
/* 179:    */   {
/* 180:421 */     checkVectorDimensions(v);
/* 181:422 */     double d = 0.0D;
/* 182:423 */     Iterator<Entry> it = iterator();
/* 183:    */     Entry e;
/* 184:425 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null)) {
/* 185:426 */       d += FastMath.abs(e.getValue() - v.getEntry(e.getIndex()));
/* 186:    */     }
/* 187:428 */     return d;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public double getLInfDistance(RealVector v)
/* 191:    */   {
/* 192:446 */     checkVectorDimensions(v);
/* 193:447 */     double d = 0.0D;
/* 194:448 */     Iterator<Entry> it = iterator();
/* 195:    */     Entry e;
/* 196:450 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null)) {
/* 197:451 */       d = FastMath.max(FastMath.abs(e.getValue() - v.getEntry(e.getIndex())), d);
/* 198:    */     }
/* 199:453 */     return d;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public int getMinIndex()
/* 203:    */   {
/* 204:463 */     int minIndex = -1;
/* 205:464 */     double minValue = (1.0D / 0.0D);
/* 206:465 */     Iterator<Entry> iterator = iterator();
/* 207:466 */     while (iterator.hasNext())
/* 208:    */     {
/* 209:467 */       Entry entry = (Entry)iterator.next();
/* 210:468 */       if (entry.getValue() <= minValue)
/* 211:    */       {
/* 212:469 */         minIndex = entry.getIndex();
/* 213:470 */         minValue = entry.getValue();
/* 214:    */       }
/* 215:    */     }
/* 216:473 */     return minIndex;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public double getMinValue()
/* 220:    */   {
/* 221:483 */     int minIndex = getMinIndex();
/* 222:484 */     return minIndex < 0 ? (0.0D / 0.0D) : getEntry(minIndex);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public int getMaxIndex()
/* 226:    */   {
/* 227:494 */     int maxIndex = -1;
/* 228:495 */     double maxValue = (-1.0D / 0.0D);
/* 229:496 */     Iterator<Entry> iterator = iterator();
/* 230:497 */     while (iterator.hasNext())
/* 231:    */     {
/* 232:498 */       Entry entry = (Entry)iterator.next();
/* 233:499 */       if (entry.getValue() >= maxValue)
/* 234:    */       {
/* 235:500 */         maxIndex = entry.getIndex();
/* 236:501 */         maxValue = entry.getValue();
/* 237:    */       }
/* 238:    */     }
/* 239:504 */     return maxIndex;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public double getMaxValue()
/* 243:    */   {
/* 244:514 */     int maxIndex = getMaxIndex();
/* 245:515 */     return maxIndex < 0 ? (0.0D / 0.0D) : getEntry(maxIndex);
/* 246:    */   }
/* 247:    */   
/* 248:    */   public RealVector mapMultiply(double d)
/* 249:    */   {
/* 250:527 */     return copy().mapMultiplyToSelf(d);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public RealVector mapMultiplyToSelf(double d)
/* 254:    */   {
	return null;
/* 255:538 */     //return mapToSelf(.fix2ndArgument(new Multiply(), d));
/* 256:    */   }
/* 257:    */   
/* 258:    */   public RealVector mapSubtract(double d)
/* 259:    */   {
/* 260:549 */     return copy().mapSubtractToSelf(d);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public RealVector mapSubtractToSelf(double d)
/* 264:    */   {
/* 265:560 */     return mapAddToSelf(-d);
/* 266:    */   }
/* 267:    */   
/* 268:    */   public RealVector mapDivide(double d)
/* 269:    */   {
/* 270:571 */     return copy().mapDivideToSelf(d);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public RealVector mapDivideToSelf(double d)
/* 274:    */   {
	return null;
/* 275:582 */   //  return mapToSelf(FunctionUtils.fix2ndArgument(new Divide(), d));
/* 276:    */   }
/* 277:    */   
/* 278:    */   public RealMatrix outerProduct(RealVector v)
/* 279:    */   {
/* 280:    */     RealMatrix product;
/* 281:    */     
/* 282:593 */     if (((v instanceof SparseRealVector)) || ((this instanceof SparseRealVector))) {
/* 283:594 */       product = new OpenMapRealMatrix(getDimension(), v.getDimension());
/* 284:    */     } else {
/* 285:597 */       product = new Array2DRowRealMatrix(getDimension(), v.getDimension());
/* 286:    */     }
/* 287:600 */     Iterator<Entry> thisIt = sparseIterator();
/* 288:601 */     Entry thisE = null;
/* 289:602 */     for (; (thisIt.hasNext()) && ((thisE = (Entry)thisIt.next()) != null);)
/* 290:    */     {
/* 291:603 */       Iterator<Entry> otherIt = v.sparseIterator();
/* 292:604 */       Entry otherE = null;
/* 293:605 */       if ((otherIt.hasNext()) && ((otherE = (Entry)otherIt.next()) != null)) {
/* 294:606 */         product.setEntry(thisE.getIndex(), otherE.getIndex(), thisE.getValue() * otherE.getValue());
/* 295:    */       }
/* 296:    */     }
/* 297:611 */     return product;
/* 298:    */   }
/* 299:    */   
/* 300:    */   public abstract RealVector projection(RealVector paramRealVector);
/* 301:    */   
/* 302:    */   public void set(double value)
/* 303:    */   {
/* 304:631 */     Iterator<Entry> it = iterator();
/* 305:632 */     Entry e = null;
/* 306:633 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null)) {
/* 307:634 */       e.setValue(value);
/* 308:    */     }
/* 309:    */   }
/* 310:    */   
/* 311:    */   public double[] toArray()
/* 312:    */   {
/* 313:646 */     int dim = getDimension();
/* 314:647 */     double[] values = new double[dim];
/* 315:648 */     for (int i = 0; i < dim; i++) {
/* 316:649 */       values[i] = getEntry(i);
/* 317:    */     }
/* 318:651 */     return values;
/* 319:    */   }
/* 320:    */   
/* 321:    */   public RealVector unitVector()
/* 322:    */   {
/* 323:662 */     RealVector copy = copy();
/* 324:663 */     copy.unitize();
/* 325:664 */     return copy;
/* 326:    */   }
/* 327:    */   
/* 328:    */   public void unitize()
/* 329:    */   {
/* 330:675 */     mapDivideToSelf(getNorm());
/* 331:    */   }
/* 332:    */   
/* 333:    */   public Iterator<Entry> sparseIterator()
/* 334:    */   {
/* 335:690 */     return new SparseEntryIterator();
/* 336:    */   }
/* 337:    */   
/* 338:    */   public Iterator<Entry> iterator()
/* 339:    */   {
/* 340:700 */     final int dim = getDimension();
return new Iterator()
/* 342:    */     {
/* 343:704 */       private int i = 0;
/* 344:707 */       private RealVector.Entry e = new RealVector.Entry();
/* 345:    */       
/* 346:    */       public boolean hasNext()
/* 347:    */       {
/* 348:711 */         return this.i < dim;
/* 349:    */       }
/* 350:    */       
/* 351:    */       public RealVector.Entry next()
/* 352:    */       {
/* 353:716 */         this.e.setIndex(this.i++);
/* 354:717 */         return this.e;
/* 355:    */       }
/* 356:    */       
/* 357:    */       public void remove()
/* 358:    */       {
/* 359:722 */         throw new MathUnsupportedOperationException();
/* 360:    */       }
/* 361:    */     };   }
/* 363:    */   
/* 364:    */   public RealVector map(UnivariateFunction function)
/* 365:    */   {
/* 366:738 */     return copy().mapToSelf(function);
/* 367:    */   }
/* 368:    */   
/* 369:    */   public RealVector mapToSelf(UnivariateFunction function)
/* 370:    */   {
/* 371:755 */     Iterator<Entry> it = function.value(0.0D) == 0.0D ? sparseIterator() : iterator();
/* 372:    */     Entry e;
/* 373:757 */     while ((it.hasNext()) && ((e = (Entry)it.next()) != null)) {
/* 374:758 */       e.setValue(function.value(e.getValue()));
/* 375:    */     }
/* 376:760 */     return this;
/* 377:    */   }
/* 378:    */   
/* 379:    */   public RealVector combine(double a, double b, RealVector y)
/* 380:    */   {
/* 381:777 */     return copy().combineToSelf(a, b, y);
/* 382:    */   }
/* 383:    */   
/* 384:    */   public RealVector combineToSelf(double a, double b, RealVector y)
/* 385:    */   {
/* 386:793 */     checkVectorDimensions(y);
/* 387:794 */     for (int i = 0; i < getDimension(); i++)
/* 388:    */     {
/* 389:795 */       double xi = getEntry(i);
/* 390:796 */       double yi = y.getEntry(i);
/* 391:797 */       setEntry(i, a * xi + b * yi);
/* 392:    */     }
/* 393:799 */     return this;
/* 394:    */   }
/* 395:    */   
/* 396:    */   protected class Entry
/* 397:    */   {
/* 398:    */     private int index;
/* 399:    */     
/* 400:    */     public Entry()
/* 401:    */     {
/* 402:811 */       setIndex(0);
/* 403:    */     }
/* 404:    */     
/* 405:    */     public double getValue()
/* 406:    */     {
/* 407:820 */       return RealVector.this.getEntry(getIndex());
/* 408:    */     }
/* 409:    */     
/* 410:    */     public void setValue(double value)
/* 411:    */     {
/* 412:829 */       RealVector.this.setEntry(getIndex(), value);
/* 413:    */     }
/* 414:    */     
/* 415:    */     public int getIndex()
/* 416:    */     {
/* 417:838 */       return this.index;
/* 418:    */     }
/* 419:    */     
/* 420:    */     public void setIndex(int index)
/* 421:    */     {
/* 422:847 */       this.index = index;
/* 423:    */     }
/* 424:    */   }
/* 425:    */   
/* 426:    */   protected class SparseEntryIterator
/* 427:    */     implements Iterator<RealVector.Entry>
/* 428:    */   {
/* 429:    */     private final int dim;
/* 430:    */     private RealVector.Entry current;
/* 431:    */     private RealVector.Entry next;
/* 432:    */     
/* 433:    */     protected SparseEntryIterator()
/* 434:    */     {
/* 435:874 */       this.dim = RealVector.this.getDimension();
/* 436:875 */       this.current = new RealVector.Entry();
/* 437:876 */       this.next = new RealVector.Entry();
/* 438:877 */       if (this.next.getValue() == 0.0D) {
/* 439:878 */         advance(this.next);
/* 440:    */       }
/* 441:    */     }
/* 442:    */     
/* 443:    */     protected void advance(RealVector.Entry e)
/* 444:    */     {
/* 445:888 */       if (e == null) {
/* 446:    */         return;
/* 447:    */       }
/* 448:    */       do
/* 449:    */       {
/* 450:892 */         e.setIndex(e.getIndex() + 1);
/* 451:893 */       } while ((e.getIndex() < this.dim) && (e.getValue() == 0.0D));
/* 452:894 */       if (e.getIndex() >= this.dim) {
/* 453:895 */         e.setIndex(-1);
/* 454:    */       }
/* 455:    */     }
/* 456:    */     
/* 457:    */     public boolean hasNext()
/* 458:    */     {
/* 459:901 */       return this.next.getIndex() >= 0;
/* 460:    */     }
/* 461:    */     
/* 462:    */     public RealVector.Entry next()
/* 463:    */     {
/* 464:906 */       int index = this.next.getIndex();
/* 465:907 */       if (index < 0) {
/* 466:908 */         throw new NoSuchElementException();
/* 467:    */       }
/* 468:910 */       this.current.setIndex(index);
/* 469:911 */       advance(this.next);
/* 470:912 */       return this.current;
/* 471:    */     }
/* 472:    */     
/* 473:    */     public void remove()
/* 474:    */     {
/* 475:917 */       throw new MathUnsupportedOperationException();
/* 476:    */     }
/* 477:    */   }
/* 478:    */   
/* 479:    */   public static RealVector unmodifiableRealVector(RealVector v)
/* 480:    */   {
return new RealVector()
/* 482:    */     {
private Object val$v;
/* 483:    */       public RealVector mapToSelf(UnivariateFunction function)
/* 484:    */       {
/* 485:953 */         throw new MathUnsupportedOperationException();
/* 486:    */       }
/* 487:    */       
/* 488:    */       public RealVector map(UnivariateFunction function)
/* 489:    */       {
/* 490:959 */         return ((RealVector) this.val$v).map(function);
/* 491:    */       }
/* 492:    */       
/* 493:    */       public Iterator<RealVector.Entry> iterator()
/* 494:    */       {
/* 495:965 */         final Iterator<RealVector.Entry> i = ((RealVector) this.val$v).sparseIterator();
return new Iterator()
/* 497:    */         {
/* 498:968 */           private final UnmodifiableEntry e = new UnmodifiableEntry();
/* 499:    */           
/* 500:    */           public boolean hasNext()
/* 501:    */           {
/* 502:972 */             return i.hasNext();
/* 503:    */           }
/* 504:    */           
/* 505:    */           public RealVector.Entry next()
/* 506:    */           {
/* 507:977 */             this.e.setIndex(((RealVector.Entry)i.next()).getIndex());
/* 508:978 */             return this.e;
/* 509:    */           }
/* 510:    */           
/* 511:    */           public void remove()
/* 512:    */           {
/* 513:983 */             throw new MathUnsupportedOperationException();
/* 514:    */           }
/* 515:    */         };       }
/* 517:    */       
/* 518:    */       public Iterator<RealVector.Entry> sparseIterator()
/* 519:    */       {
/* 520:991 */         final Iterator<RealVector.Entry> i = ((RealVector) this.val$v).sparseIterator();
return new Iterator()
/* 523:    */         {
/* 524:995 */           private final UnmodifiableEntry e = new UnmodifiableEntry();
/* 525:    */           
/* 526:    */           public boolean hasNext()
/* 527:    */           {
/* 528:999 */             return i.hasNext();
/* 529:    */           }
/* 530:    */           
/* 531:    */           public RealVector.Entry next()
/* 532:    */           {
/* 533::04 */             this.e.setIndex(((RealVector.Entry)i.next()).getIndex());
/* 534::05 */             return this.e;
/* 535:    */           }
/* 536:    */           
/* 537:    */           public void remove()
/* 538:    */           {
/* 539::10 */             throw new MathUnsupportedOperationException();
/* 540:    */           }
/* 541:    */         };       }
/* 543:    */       
/* 544:    */       public RealVector copy()
/* 545:    */       {
/* 546::18 */         return ((RealVector) this.val$v).copy();
/* 547:    */       }
/* 548:    */       
/* 549:    */       public RealVector add(RealVector w)
/* 550:    */       {
/* 551::24 */         return ((RealVector) this.val$v).add(w);
/* 552:    */       }
/* 553:    */       
/* 554:    */       public RealVector subtract(RealVector w)
/* 555:    */       {
/* 556::30 */         return ((RealVector) this.val$v).subtract(w);
/* 557:    */       }
/* 558:    */       
/* 559:    */       public RealVector mapAdd(double d)
/* 560:    */       {
/* 561::36 */         return ((RealVector) this.val$v).mapAdd(d);
/* 562:    */       }
/* 563:    */       
/* 564:    */       public RealVector mapAddToSelf(double d)
/* 565:    */       {
/* 566::42 */         throw new MathUnsupportedOperationException();
/* 567:    */       }
/* 568:    */       
/* 569:    */       public RealVector mapSubtract(double d)
/* 570:    */       {
/* 571::48 */         return ((RealVector) this.val$v).mapSubtract(d);
/* 572:    */       }
/* 573:    */       
/* 574:    */       public RealVector mapSubtractToSelf(double d)
/* 575:    */       {
/* 576::54 */         throw new MathUnsupportedOperationException();
/* 577:    */       }
/* 578:    */       
/* 579:    */       public RealVector mapMultiply(double d)
/* 580:    */       {
/* 581::60 */         return ((RealVector) this.val$v).mapMultiply(d);
/* 582:    */       }
/* 583:    */       
/* 584:    */       public RealVector mapMultiplyToSelf(double d)
/* 585:    */       {
/* 586::66 */         throw new MathUnsupportedOperationException();
/* 587:    */       }
/* 588:    */       
/* 589:    */       public RealVector mapDivide(double d)
/* 590:    */       {
/* 591::72 */         return ((RealVector) this.val$v).mapDivide(d);
/* 592:    */       }
/* 593:    */       
/* 594:    */       public RealVector mapDivideToSelf(double d)
/* 595:    */       {
/* 596::78 */         throw new MathUnsupportedOperationException();
/* 597:    */       }
/* 598:    */       
/* 599:    */       public RealVector ebeMultiply(RealVector w)
/* 600:    */       {
/* 601::84 */         return ((RealVector) this.val$v).ebeMultiply(w);
/* 602:    */       }
/* 603:    */       
/* 604:    */       public RealVector ebeDivide(RealVector w)
/* 605:    */       {
/* 606::90 */         return ((RealVector) this.val$v).ebeDivide(w);
/* 607:    */       }
/* 608:    */       
/* 609:    */       public double dotProduct(RealVector w)
/* 610:    */       {
/* 611::96 */         return ((RealVector) this.val$v).dotProduct(w);
/* 612:    */       }
/* 613:    */       
/* 614:    */       public double cosine(RealVector w)
/* 615:    */       {
/* 616:;02 */         return ((RealVector) this.val$v).cosine(w);
/* 617:    */       }
/* 618:    */       
/* 619:    */       public double getNorm()
/* 620:    */       {
/* 621:;08 */         return ((RealVector) this.val$v).getNorm();
/* 622:    */       }
/* 623:    */       
/* 624:    */       public double getL1Norm()
/* 625:    */       {
/* 626:;14 */         return ((RealVector) this.val$v).getL1Norm();
/* 627:    */       }
/* 628:    */       
/* 629:    */       public double getLInfNorm()
/* 630:    */       {
/* 631:;20 */         return ((RealVector) this.val$v).getLInfNorm();
/* 632:    */       }
/* 633:    */       
/* 634:    */       public double getDistance(RealVector w)
/* 635:    */       {
/* 636:;26 */         return ((RealVector) this.val$v).getDistance(w);
/* 637:    */       }
/* 638:    */       
/* 639:    */       public double getL1Distance(RealVector w)
/* 640:    */       {
/* 641:;32 */         return ((RealVector) this.val$v).getL1Distance(w);
/* 642:    */       }
/* 643:    */       
/* 644:    */       public double getLInfDistance(RealVector w)
/* 645:    */       {
/* 646:;38 */         return ((RealVector) this.val$v).getLInfDistance(w);
/* 647:    */       }
/* 648:    */       
/* 649:    */       public RealVector unitVector()
/* 650:    */       {
/* 651:;44 */         return ((RealVector) this.val$v).unitVector();
/* 652:    */       }
/* 653:    */       
/* 654:    */       public void unitize()
/* 655:    */       {
/* 656:;50 */         throw new MathUnsupportedOperationException();
/* 657:    */       }
/* 658:    */       
/* 659:    */       public RealVector projection(RealVector w)
/* 660:    */       {
/* 661:;56 */         return ((RealVector) this.val$v).projection(w);
/* 662:    */       }
/* 663:    */       
/* 664:    */       public RealMatrix outerProduct(RealVector w)
/* 665:    */       {
/* 666:;62 */         return ((RealVector) this.val$v).outerProduct(w);
/* 667:    */       }
/* 668:    */       
/* 669:    */       public double getEntry(int index)
/* 670:    */       {
/* 671:;68 */         return ((RealVector) this.val$v).getEntry(index);
/* 672:    */       }
/* 673:    */       
/* 674:    */       public void setEntry(int index, double value)
/* 675:    */       {
/* 676:;74 */         throw new MathUnsupportedOperationException();
/* 677:    */       }
/* 678:    */       
/* 679:    */       public void addToEntry(int index, double value)
/* 680:    */       {
/* 681:;80 */         throw new MathUnsupportedOperationException();
/* 682:    */       }
/* 683:    */       
/* 684:    */       public int getDimension()
/* 685:    */       {
/* 686:;86 */         return ((RealVector) this.val$v).getDimension();
/* 687:    */       }
/* 688:    */       
/* 689:    */       public RealVector append(RealVector w)
/* 690:    */       {
/* 691:;92 */         return ((RealVector) this.val$v).append(w);
/* 692:    */       }
/* 693:    */       
/* 694:    */       public RealVector append(double d)
/* 695:    */       {
/* 696:;98 */         return ((RealVector) this.val$v).append(d);
/* 697:    */       }
/* 698:    */       
/* 699:    */       public RealVector getSubVector(int index, int n)
/* 700:    */       {
/* 701:<04 */         return ((RealVector) this.val$v).getSubVector(index, n);
/* 702:    */       }
/* 703:    */       
/* 704:    */       public void setSubVector(int index, RealVector w)
/* 705:    */       {
/* 706:<10 */         throw new MathUnsupportedOperationException();
/* 707:    */       }
/* 708:    */       
/* 709:    */       public void set(double value)
/* 710:    */       {
/* 711:<16 */         throw new MathUnsupportedOperationException();
/* 712:    */       }
/* 713:    */       
/* 714:    */       public double[] toArray()
/* 715:    */       {
/* 716:<22 */         return ((RealVector) this.val$v).toArray();
/* 717:    */       }
/* 718:    */       
/* 719:    */       public boolean isNaN()
/* 720:    */       {
/* 721:<28 */         return ((RealVector) this.val$v).isNaN();
/* 722:    */       }
/* 723:    */       
/* 724:    */       public boolean isInfinite()
/* 725:    */       {
/* 726:<34 */         return ((RealVector) this.val$v).isInfinite();
/* 727:    */       }
/* 728:    */       
/* 729:    */       public RealVector combine(double a, double b, RealVector y)
/* 730:    */       {
/* 731:<40 */         return ((RealVector) this.val$v).combine(a, b, y);
/* 732:    */       }
/* 733:    */       
/* 734:    */       public RealVector combineToSelf(double a, double b, RealVector y)
/* 735:    */       {
/* 736:<46 */         throw new MathUnsupportedOperationException();
/* 737:    */       }
/* 738:    */       
/* 739:    */       class UnmodifiableEntry
/* 740:    */         extends RealVector.Entry
/* 741:    */       {
private Object val$v;
/* 742:    */         UnmodifiableEntry()
/* 743:    */         {
/* 744:<50 */           super();
/* 745:    */         }
/* 746:    */         
/* 747:    */         public double getValue()
/* 748:    */         {
/* 749:<54 */           return ((RealVector) this.val$v).getEntry(getIndex());
/* 750:    */         }
/* 751:    */         
/* 752:    */         public void setValue(double value)
/* 753:    */         {
/* 754:<60 */           throw new MathUnsupportedOperationException();
/* 755:    */         }
/* 756:    */       }
/* 757:    */     };   }
/* 759:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.RealVector
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.Array;
/*   5:    */ import org.apache.commons.math3.Field;
/*   6:    */ import org.apache.commons.math3.FieldElement;
/*   7:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   8:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   9:    */ import org.apache.commons.math3.util.OpenIntToFieldHashMap;
/*  10:    */ import org.apache.commons.math3.util.OpenIntToFieldHashMap.Iterator;
/*  11:    */ 
/*  12:    */ public class SparseFieldVector<T extends FieldElement<T>>
/*  13:    */   implements FieldVector<T>, Serializable
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = 7841233292190413362L;
/*  16:    */   private final Field<T> field;
/*  17:    */   private final OpenIntToFieldHashMap<T> entries;
/*  18:    */   private final int virtualSize;
/*  19:    */   
/*  20:    */   public SparseFieldVector(Field<T> field)
/*  21:    */   {
/*  22: 55 */     this(field, 0);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SparseFieldVector(Field<T> field, int dimension)
/*  26:    */   {
/*  27: 66 */     this.field = field;
/*  28: 67 */     this.virtualSize = dimension;
/*  29: 68 */     this.entries = new OpenIntToFieldHashMap(field);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected SparseFieldVector(SparseFieldVector<T> v, int resize)
/*  33:    */   {
/*  34: 78 */     this.field = v.field;
/*  35: 79 */     this.virtualSize = (v.getDimension() + resize);
/*  36: 80 */     this.entries = new OpenIntToFieldHashMap(v.entries);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public SparseFieldVector(Field<T> field, int dimension, int expectedSize)
/*  40:    */   {
/*  41: 92 */     this.field = field;
/*  42: 93 */     this.virtualSize = dimension;
/*  43: 94 */     this.entries = new OpenIntToFieldHashMap(field, expectedSize);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public SparseFieldVector(Field<T> field, T[] values)
/*  47:    */   {
/*  48:105 */     this.field = field;
/*  49:106 */     this.virtualSize = values.length;
/*  50:107 */     this.entries = new OpenIntToFieldHashMap(field);
/*  51:108 */     for (int key = 0; key < values.length; key++)
/*  52:    */     {
/*  53:109 */       T value = values[key];
/*  54:110 */       this.entries.put(key, value);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public SparseFieldVector(SparseFieldVector<T> v)
/*  59:    */   {
/*  60:120 */     this.field = v.field;
/*  61:121 */     this.virtualSize = v.getDimension();
/*  62:122 */     this.entries = new OpenIntToFieldHashMap(v.getEntries());
/*  63:    */   }
/*  64:    */   
/*  65:    */   private OpenIntToFieldHashMap<T> getEntries()
/*  66:    */   {
/*  67:131 */     return this.entries;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public FieldVector<T> add(SparseFieldVector<T> v)
/*  71:    */   {
/*  72:143 */     checkVectorDimensions(v.getDimension());
/*  73:144 */     SparseFieldVector<T> res = (SparseFieldVector)copy();
/*  74:145 */     OpenIntToFieldHashMap<T>.Iterator iter = v.getEntries().iterator();
/*  75:146 */     while (iter.hasNext())
/*  76:    */     {
/*  77:147 */       iter.advance();
/*  78:148 */       int key = iter.key();
/*  79:149 */       T value = iter.value();
/*  80:150 */       if (this.entries.containsKey(key)) {
/*  81:151 */         res.setEntry(key, (T)this.entries.get(key).add(value));
/*  82:    */       } else {
/*  83:153 */         res.setEntry(key, value);
/*  84:    */       }
/*  85:    */     }
/*  86:156 */     return res;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public FieldVector<T> append(SparseFieldVector<T> v)
/*  90:    */   {
/*  91:167 */     SparseFieldVector<T> res = new SparseFieldVector(this, v.getDimension());
/*  92:168 */     OpenIntToFieldHashMap<T>.Iterator iter = v.entries.iterator();
/*  93:169 */     while (iter.hasNext())
/*  94:    */     {
/*  95:170 */       iter.advance();
/*  96:171 */       res.setEntry(iter.key() + this.virtualSize, iter.value());
/*  97:    */     }
/*  98:173 */     return res;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public FieldVector<T> append(FieldVector<T> v)
/* 102:    */   {
/* 103:178 */     if ((v instanceof SparseFieldVector)) {
/* 104:179 */       return append((SparseFieldVector)v);
/* 105:    */     }
/* 106:181 */     int n = v.getDimension();
/* 107:182 */     FieldVector<T> res = new SparseFieldVector(this, n);
/* 108:183 */     for (int i = 0; i < n; i++) {
/* 109:184 */       res.setEntry(i + this.virtualSize, v.getEntry(i));
/* 110:    */     }
/* 111:186 */     return res;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public FieldVector<T> append(T d)
/* 115:    */   {
/* 116:192 */     FieldVector<T> res = new SparseFieldVector(this, 1);
/* 117:193 */     res.setEntry(this.virtualSize, d);
/* 118:194 */     return res;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public FieldVector<T> copy()
/* 122:    */   {
/* 123:199 */     return new SparseFieldVector(this);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public T dotProduct(FieldVector<T> v)
/* 127:    */   {
/* 128:204 */     checkVectorDimensions(v.getDimension());
/* 129:205 */     T res = (T)this.field.getZero();
/* 130:206 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 131:207 */     while (iter.hasNext())
/* 132:    */     {
/* 133:208 */       iter.advance();
/* 134:209 */       res = (T)res.add(v.getEntry(iter.key()).multiply(iter.value()));
/* 135:    */     }
/* 136:211 */     return res;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public FieldVector<T> ebeDivide(FieldVector<T> v)
/* 140:    */   {
/* 141:216 */     checkVectorDimensions(v.getDimension());
/* 142:217 */     SparseFieldVector<T> res = new SparseFieldVector(this);
/* 143:218 */     OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
/* 144:219 */     while (iter.hasNext())
/* 145:    */     {
/* 146:220 */       iter.advance();
/* 147:221 */       res.setEntry(iter.key(), (FieldElement)iter.value().divide(v.getEntry(iter.key())));
/* 148:    */     }
/* 149:223 */     return res;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public FieldVector<T> ebeMultiply(FieldVector<T> v)
/* 153:    */   {
/* 154:228 */     checkVectorDimensions(v.getDimension());
/* 155:229 */     SparseFieldVector<T> res = new SparseFieldVector(this);
/* 156:230 */     OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
/* 157:231 */     while (iter.hasNext())
/* 158:    */     {
/* 159:232 */       iter.advance();
/* 160:233 */       res.setEntry(iter.key(), (FieldElement)iter.value().multiply(v.getEntry(iter.key())));
/* 161:    */     }
/* 162:235 */     return res;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public T[] getData()
/* 166:    */   {
/* 167:240 */     T[] res = buildArray(this.virtualSize);
/* 168:241 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 169:242 */     while (iter.hasNext())
/* 170:    */     {
/* 171:243 */       iter.advance();
/* 172:244 */       res[iter.key()] = iter.value();
/* 173:    */     }
/* 174:246 */     return res;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public int getDimension()
/* 178:    */   {
/* 179:251 */     return this.virtualSize;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public T getEntry(int index)
/* 183:    */   {
/* 184:256 */     checkIndex(index);
/* 185:257 */     return this.entries.get(index);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public Field<T> getField()
/* 189:    */   {
/* 190:262 */     return this.field;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public FieldVector<T> getSubVector(int index, int n)
/* 194:    */   {
/* 195:267 */     checkIndex(index);
/* 196:268 */     checkIndex(index + n - 1);
/* 197:269 */     SparseFieldVector<T> res = new SparseFieldVector(this.field, n);
/* 198:270 */     int end = index + n;
/* 199:271 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 200:272 */     while (iter.hasNext())
/* 201:    */     {
/* 202:273 */       iter.advance();
/* 203:274 */       int key = iter.key();
/* 204:275 */       if ((key >= index) && (key < end)) {
/* 205:276 */         res.setEntry(key - index, iter.value());
/* 206:    */       }
/* 207:    */     }
/* 208:279 */     return res;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public FieldVector<T> mapAdd(T d)
/* 212:    */   {
/* 213:284 */     return copy().mapAddToSelf(d);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public FieldVector<T> mapAddToSelf(T d)
/* 217:    */   {
/* 218:289 */     for (int i = 0; i < this.virtualSize; i++) {
/* 219:290 */       setEntry(i, (FieldElement)getEntry(i).add(d));
/* 220:    */     }
/* 221:292 */     return this;
/* 222:    */   }
/* 223:    */   
/* 224:    */   public FieldVector<T> mapDivide(T d)
/* 225:    */   {
/* 226:297 */     return copy().mapDivideToSelf(d);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public FieldVector<T> mapDivideToSelf(T d)
/* 230:    */   {
/* 231:302 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 232:303 */     while (iter.hasNext())
/* 233:    */     {
/* 234:304 */       iter.advance();
/* 235:305 */       this.entries.put(iter.key(), (T)iter.value().divide(d));
/* 236:    */     }
/* 237:307 */     return this;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public FieldVector<T> mapInv()
/* 241:    */   {
/* 242:312 */     return copy().mapInvToSelf();
/* 243:    */   }
/* 244:    */   
/* 245:    */   public FieldVector<T> mapInvToSelf()
/* 246:    */   {
/* 247:317 */     for (int i = 0; i < this.virtualSize; i++) {
/* 248:318 */       setEntry(i, (FieldElement)((FieldElement)this.field.getOne()).divide(getEntry(i)));
/* 249:    */     }
/* 250:320 */     return this;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public FieldVector<T> mapMultiply(T d)
/* 254:    */   {
/* 255:325 */     return copy().mapMultiplyToSelf(d);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public FieldVector<T> mapMultiplyToSelf(T d)
/* 259:    */   {
/* 260:330 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 261:331 */     while (iter.hasNext())
/* 262:    */     {
/* 263:332 */       iter.advance();
/* 264:333 */       this.entries.put(iter.key(), (T)iter.value().multiply(d));
/* 265:    */     }
/* 266:335 */     return this;
/* 267:    */   }
/* 268:    */   
/* 269:    */   public FieldVector<T> mapSubtract(T d)
/* 270:    */   {
/* 271:340 */     return copy().mapSubtractToSelf(d);
/* 272:    */   }
/* 273:    */   
/* 274:    */   public FieldVector<T> mapSubtractToSelf(T d)
/* 275:    */   {
/* 276:345 */     return mapAddToSelf((T)((FieldElement)this.field.getZero()).subtract(d));
/* 277:    */   }
/* 278:    */   
/* 279:    */   public FieldMatrix<T> outerProduct(SparseFieldVector<T> v)
/* 280:    */   {
/* 281:356 */     int n = v.getDimension();
/* 282:357 */     SparseFieldMatrix<T> res = new SparseFieldMatrix(this.field, this.virtualSize, n);
/* 283:358 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 284:359 */     while (iter.hasNext())
/* 285:    */     {
/* 286:360 */       iter.advance();
/* 287:361 */       OpenIntToFieldHashMap<T>.Iterator iter2 = v.entries.iterator();
/* 288:362 */       while (iter2.hasNext())
/* 289:    */       {
/* 290:363 */         iter2.advance();
/* 291:364 */         res.setEntry(iter.key(), iter2.key(), (T)iter.value().multiply(iter2.value()));
/* 292:    */       }
/* 293:    */     }
/* 294:367 */     return res;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public FieldMatrix<T> outerProduct(FieldVector<T> v)
/* 298:    */   {
/* 299:372 */     if ((v instanceof SparseFieldVector)) {
/* 300:373 */       return outerProduct((SparseFieldVector)v);
/* 301:    */     }
/* 302:375 */     int n = v.getDimension();
/* 303:376 */     FieldMatrix<T> res = new SparseFieldMatrix(this.field, this.virtualSize, n);
/* 304:377 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 305:378 */     while (iter.hasNext())
/* 306:    */     {
/* 307:379 */       iter.advance();
/* 308:380 */       int row = iter.key();
/* 309:381 */       FieldElement<T> value = iter.value();
/* 310:382 */       for (int col = 0; col < n; col++) {
/* 311:383 */         res.setEntry(row, col, (T)value.multiply(v.getEntry(col)));
/* 312:    */       }
/* 313:    */     }
/* 314:386 */     return res;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public FieldVector<T> projection(FieldVector<T> v)
/* 318:    */   {
/* 319:392 */     checkVectorDimensions(v.getDimension());
/* 320:393 */     return v.mapMultiply((T)dotProduct(v).divide(v.dotProduct(v)));
/* 321:    */   }
/* 322:    */   
/* 323:    */   public void set(T value)
/* 324:    */   {
/* 325:398 */     for (int i = 0; i < this.virtualSize; i++) {
/* 326:399 */       setEntry(i, value);
/* 327:    */     }
/* 328:    */   }
/* 329:    */   
/* 330:    */   public void setEntry(int index, FieldElement fieldElement)
/* 331:    */   {
/* 332:405 */     checkIndex(index);
/* 333:406 */     this.entries.put(index, (T) fieldElement);
/* 334:    */   }
/* 335:    */   
/* 336:    */   public void setSubVector(int index, FieldVector<T> v)
/* 337:    */   {
/* 338:411 */     checkIndex(index);
/* 339:412 */     checkIndex(index + v.getDimension() - 1);
/* 340:413 */     int n = v.getDimension();
/* 341:414 */     for (int i = 0; i < n; i++) {
/* 342:415 */       setEntry(i + index, v.getEntry(i));
/* 343:    */     }
/* 344:    */   }
/* 345:    */   
/* 346:    */   public SparseFieldVector<T> subtract(SparseFieldVector<T> v)
/* 347:    */   {
/* 348:428 */     checkVectorDimensions(v.getDimension());
/* 349:429 */     SparseFieldVector<T> res = (SparseFieldVector)copy();
/* 350:430 */     OpenIntToFieldHashMap<T>.Iterator iter = v.getEntries().iterator();
/* 351:431 */     while (iter.hasNext())
/* 352:    */     {
/* 353:432 */       iter.advance();
/* 354:433 */       int key = iter.key();
/* 355:434 */       if (this.entries.containsKey(key)) {
/* 356:435 */         res.setEntry(key, (FieldElement)this.entries.get(key).subtract(iter.value()));
/* 357:    */       } else {
/* 358:437 */         res.setEntry(key, (FieldElement)((FieldElement)this.field.getZero()).subtract(iter.value()));
/* 359:    */       }
/* 360:    */     }
/* 361:440 */     return res;
/* 362:    */   }
/* 363:    */   
/* 364:    */   public FieldVector<T> subtract(FieldVector<T> v)
/* 365:    */   {
/* 366:445 */     if ((v instanceof SparseFieldVector)) {
/* 367:446 */       return subtract((SparseFieldVector)v);
/* 368:    */     }
/* 369:448 */     int n = v.getDimension();
/* 370:449 */     checkVectorDimensions(n);
/* 371:450 */     SparseFieldVector<T> res = new SparseFieldVector(this);
/* 372:451 */     for (int i = 0; i < n; i++) {
/* 373:452 */       if (this.entries.containsKey(i)) {
/* 374:453 */         res.setEntry(i, (FieldElement)this.entries.get(i).subtract(v.getEntry(i)));
/* 375:    */       } else {
/* 376:455 */         res.setEntry(i, (FieldElement)((FieldElement)this.field.getZero()).subtract(v.getEntry(i)));
/* 377:    */       }
/* 378:    */     }
/* 379:458 */     return res;
/* 380:    */   }
/* 381:    */   
/* 382:    */   public T[] toArray()
/* 383:    */   {
/* 384:464 */     return getData();
/* 385:    */   }
/* 386:    */   
/* 387:    */   private void checkIndex(int index)
/* 388:    */   {
/* 389:474 */     if ((index < 0) || (index >= getDimension())) {
/* 390:475 */       throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(getDimension() - 1));
/* 391:    */     }
/* 392:    */   }
/* 393:    */   
/* 394:    */   protected void checkVectorDimensions(int n)
/* 395:    */   {
/* 396:486 */     if (getDimension() != n) {
/* 397:487 */       throw new DimensionMismatchException(getDimension(), n);
/* 398:    */     }
/* 399:    */   }
/* 400:    */   
/* 401:    */   public FieldVector<T> add(FieldVector<T> v)
/* 402:    */   {
/* 403:493 */     if ((v instanceof SparseFieldVector)) {
/* 404:494 */       return add((SparseFieldVector)v);
/* 405:    */     }
/* 406:496 */     int n = v.getDimension();
/* 407:497 */     checkVectorDimensions(n);
/* 408:498 */     SparseFieldVector<T> res = new SparseFieldVector(this.field, getDimension());
/* 409:500 */     for (int i = 0; i < n; i++) {
/* 410:501 */       res.setEntry(i, (FieldElement)v.getEntry(i).add(getEntry(i)));
/* 411:    */     }
/* 412:503 */     return res;
/* 413:    */   }
/* 414:    */   
/* 415:    */   private T[] buildArray(int length)
/* 416:    */   {
/* 417:515 */     return (T[])Array.newInstance(this.field.getRuntimeClass(), length);
/* 418:    */   }
/* 419:    */   
/* 420:    */   public int hashCode()
/* 421:    */   {
/* 422:522 */     int prime = 31;
/* 423:523 */     int result = 1;
/* 424:524 */     result = 31 * result + (this.field == null ? 0 : this.field.hashCode());
/* 425:525 */     result = 31 * result + this.virtualSize;
/* 426:526 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 427:527 */     while (iter.hasNext())
/* 428:    */     {
/* 429:528 */       iter.advance();
/* 430:529 */       int temp = iter.value().hashCode();
/* 431:530 */       result = 31 * result + temp;
/* 432:    */     }
/* 433:532 */     return result;
/* 434:    */   }
/* 435:    */   
/* 436:    */   public boolean equals(Object obj)
/* 437:    */   {
/* 438:540 */     if (this == obj) {
/* 439:541 */       return true;
/* 440:    */     }
/* 441:544 */     if (!(obj instanceof SparseFieldVector)) {
/* 442:545 */       return false;
/* 443:    */     }
/* 444:550 */     SparseFieldVector<T> other = (SparseFieldVector)obj;
/* 445:551 */     if (this.field == null)
/* 446:    */     {
/* 447:552 */       if (other.field != null) {
/* 448:553 */         return false;
/* 449:    */       }
/* 450:    */     }
/* 451:555 */     else if (!this.field.equals(other.field)) {
/* 452:556 */       return false;
/* 453:    */     }
/* 454:558 */     if (this.virtualSize != other.virtualSize) {
/* 455:559 */       return false;
/* 456:    */     }
/* 457:562 */     OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
/* 458:563 */     while (iter.hasNext())
/* 459:    */     {
/* 460:564 */       iter.advance();
/* 461:565 */       T test = other.getEntry(iter.key());
/* 462:566 */       if (!test.equals(iter.value())) {
/* 463:567 */         return false;
/* 464:    */       }
/* 465:    */     }
/* 466:570 */     iter = other.getEntries().iterator();
/* 467:571 */     while (iter.hasNext())
/* 468:    */     {
/* 469:572 */       iter.advance();
/* 470:573 */       T test = iter.value();
/* 471:574 */       if (!test.equals(getEntry(iter.key()))) {
/* 472:575 */         return false;
/* 473:    */       }
/* 474:    */     }
/* 475:578 */     return true;
/* 476:    */   }
/* 477:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.SparseFieldVector
 * JD-Core Version:    0.7.0.1
 */
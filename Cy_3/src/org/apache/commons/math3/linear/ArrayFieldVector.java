/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.Array;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import org.apache.commons.math3.Field;
/*   7:    */ import org.apache.commons.math3.FieldElement;
/*   8:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   9:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*  11:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  12:    */ import org.apache.commons.math3.exception.ZeroException;
/*  13:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  14:    */ 
/*  15:    */ public class ArrayFieldVector<T extends FieldElement<T>>
/*  16:    */   implements FieldVector<T>, Serializable
/*  17:    */ {
/*  18:    */   private static final long serialVersionUID = 7648186910365927050L;
/*  19:    */   private T[] data;
/*  20:    */   private final Field<T> field;
/*  21:    */   
/*  22:    */   public ArrayFieldVector(Field<T> field)
/*  23:    */   {
/*  24: 59 */     this(field, 0);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public ArrayFieldVector(Field<T> field, int size)
/*  28:    */   {
/*  29: 69 */     this.field = field;
/*  30: 70 */     this.data = buildArray(size);
/*  31: 71 */     Arrays.fill(this.data, field.getZero());
/*  32:    */   }
/*  33:    */   
/*  34:    */   public ArrayFieldVector(int size, T preset)
/*  35:    */   {
/*  36: 81 */     this(preset.getField(), size);
/*  37: 82 */     Arrays.fill(this.data, preset);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ArrayFieldVector(T[] d)
/*  41:    */   {
/*  42: 98 */     if (d == null) {
/*  43: 99 */       throw new NullArgumentException();
/*  44:    */     }
/*  45:    */     try
/*  46:    */     {
/*  47:102 */       this.field = d[0].getField();
/*  48:103 */       this.data = (T[]) ((FieldElement[])d.clone());
/*  49:    */     }
/*  50:    */     catch (ArrayIndexOutOfBoundsException e)
/*  51:    */     {
/*  52:105 */       throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public ArrayFieldVector(Field<T> field, T[] d)
/*  57:    */   {
/*  58:118 */     if (d == null) {
/*  59:119 */       throw new NullArgumentException();
/*  60:    */     }
/*  61:121 */     this.field = field;
/*  62:122 */     this.data = (T[]) ((FieldElement[])d.clone());
/*  63:    */   }
/*  64:    */   
/*  65:    */   public ArrayFieldVector(T[] d, boolean copyArray)
/*  66:    */   {
/*  67:147 */     if (d == null) {
/*  68:148 */       throw new NullArgumentException();
/*  69:    */     }
/*  70:150 */     if (d.length == 0) {
/*  71:151 */       throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
/*  72:    */     }
/*  73:153 */     this.field = d[0].getField();
/*  74:154 */     this.data = (T[]) (copyArray ? (FieldElement[])d.clone() : d);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public ArrayFieldVector(Field<T> field, T[] d, boolean copyArray)
/*  78:    */   {
/*  79:173 */     if (d == null) {
/*  80:174 */       throw new NullArgumentException();
/*  81:    */     }
/*  82:176 */     this.field = field;
/*  83:177 */     this.data = (T[]) (copyArray ? (FieldElement[])d.clone() : d);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public ArrayFieldVector(T[] d, int pos, int size)
/*  87:    */   {
/*  88:191 */     if (d == null) {
/*  89:192 */       throw new NullArgumentException();
/*  90:    */     }
/*  91:194 */     if (d.length < pos + size) {
/*  92:195 */       throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d.length), true);
/*  93:    */     }
/*  94:197 */     this.field = d[0].getField();
/*  95:198 */     this.data = buildArray(size);
/*  96:199 */     System.arraycopy(d, pos, this.data, 0, size);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public ArrayFieldVector(Field<T> field, T[] d, int pos, int size)
/* 100:    */   {
/* 101:214 */     if (d == null) {
/* 102:215 */       throw new NullArgumentException();
/* 103:    */     }
/* 104:217 */     if (d.length < pos + size) {
/* 105:218 */       throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d.length), true);
/* 106:    */     }
/* 107:220 */     this.field = field;
/* 108:221 */     this.data = buildArray(size);
/* 109:222 */     System.arraycopy(d, pos, this.data, 0, size);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public ArrayFieldVector(FieldVector<T> v)
/* 113:    */   {
/* 114:232 */     if (v == null) {
/* 115:233 */       throw new NullArgumentException();
/* 116:    */     }
/* 117:235 */     this.field = v.getField();
/* 118:236 */     this.data = buildArray(v.getDimension());
/* 119:237 */     for (int i = 0; i < this.data.length; i++) {
/* 120:238 */       this.data[i] = v.getEntry(i);
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public ArrayFieldVector(ArrayFieldVector<T> v)
/* 125:    */   {
/* 126:249 */     if (v == null) {
/* 127:250 */       throw new NullArgumentException();
/* 128:    */     }
/* 129:252 */     this.field = v.getField();
/* 130:253 */     this.data = (T[]) ((FieldElement[])v.data.clone());
/* 131:    */   }
/* 132:    */   
/* 133:    */   public ArrayFieldVector(ArrayFieldVector<T> v, boolean deep)
/* 134:    */   {
/* 135:265 */     if (v == null) {
/* 136:266 */       throw new NullArgumentException();
/* 137:    */     }
/* 138:268 */     this.field = v.getField();
/* 139:269 */     this.data = (T[]) (deep ? (FieldElement[])v.data.clone() : v.data);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public ArrayFieldVector(ArrayFieldVector<T> v1, ArrayFieldVector<T> v2)
/* 143:    */   {
/* 144:281 */     if ((v1 == null) || (v2 == null)) {
/* 145:283 */       throw new NullArgumentException();
/* 146:    */     }
/* 147:285 */     this.field = v1.getField();
/* 148:286 */     this.data = buildArray(v1.data.length + v2.data.length);
/* 149:287 */     System.arraycopy(v1.data, 0, this.data, 0, v1.data.length);
/* 150:288 */     System.arraycopy(v2.data, 0, this.data, v1.data.length, v2.data.length);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public ArrayFieldVector(ArrayFieldVector<T> v1, T[] v2)
/* 154:    */   {
/* 155:300 */     if ((v1 == null) || (v2 == null)) {
/* 156:302 */       throw new NullArgumentException();
/* 157:    */     }
/* 158:304 */     this.field = v1.getField();
/* 159:305 */     this.data = buildArray(v1.data.length + v2.length);
/* 160:306 */     System.arraycopy(v1.data, 0, this.data, 0, v1.data.length);
/* 161:307 */     System.arraycopy(v2, 0, this.data, v1.data.length, v2.length);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public ArrayFieldVector(T[] v1, ArrayFieldVector<T> v2)
/* 165:    */   {
/* 166:319 */     if ((v1 == null) || (v2 == null)) {
/* 167:321 */       throw new NullArgumentException();
/* 168:    */     }
/* 169:323 */     this.field = v2.getField();
/* 170:324 */     this.data = buildArray(v1.length + v2.data.length);
/* 171:325 */     System.arraycopy(v1, 0, this.data, 0, v1.length);
/* 172:326 */     System.arraycopy(v2.data, 0, this.data, v1.length, v2.data.length);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public ArrayFieldVector(T[] v1, T[] v2)
/* 176:    */   {
/* 177:345 */     if ((v1 == null) || (v2 == null)) {
/* 178:347 */       throw new NullArgumentException();
/* 179:    */     }
/* 180:349 */     if (v1.length + v2.length == 0) {
/* 181:350 */       throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
/* 182:    */     }
/* 183:352 */     this.data = buildArray(v1.length + v2.length);
/* 184:353 */     System.arraycopy(v1, 0, this.data, 0, v1.length);
/* 185:354 */     System.arraycopy(v2, 0, this.data, v1.length, v2.length);
/* 186:355 */     this.field = this.data[0].getField();
/* 187:    */   }
/* 188:    */   
/* 189:    */   public ArrayFieldVector(Field<T> field, T[] v1, T[] v2)
/* 190:    */   {
/* 191:370 */     if (v1.length + v2.length == 0) {
/* 192:371 */       throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
/* 193:    */     }
/* 194:373 */     this.data = buildArray(v1.length + v2.length);
/* 195:374 */     System.arraycopy(v1, 0, this.data, 0, v1.length);
/* 196:375 */     System.arraycopy(v2, 0, this.data, v1.length, v2.length);
/* 197:376 */     this.field = field;
/* 198:    */   }
/* 199:    */   
/* 200:    */   private T[] buildArray(int length)
/* 201:    */   {
/* 202:387 */     return (T[])Array.newInstance(this.field.getRuntimeClass(), length);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public Field<T> getField()
/* 206:    */   {
/* 207:392 */     return this.field;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public FieldVector<T> copy()
/* 211:    */   {
/* 212:397 */     return new ArrayFieldVector(this, true);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public FieldVector<T> add(FieldVector<T> v)
/* 216:    */   {
/* 217:    */     try
/* 218:    */     {
/* 219:403 */       return add((ArrayFieldVector)v);
/* 220:    */     }
/* 221:    */     catch (ClassCastException cce)
/* 222:    */     {
/* 223:405 */       checkVectorDimensions(v);
/* 224:406 */       T[] out = buildArray(this.data.length);
/* 225:407 */       for (int i = 0; i < this.data.length; i++) {
/* 226:408 */         out[i] = (T) ((FieldElement)this.data[i].add(v.getEntry(i)));
/* 227:    */       }
/* 228:410 */       return new ArrayFieldVector(this.field, out, false);
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   public ArrayFieldVector<T> add(ArrayFieldVector<T> v)
/* 233:    */   {
/* 234:421 */     checkVectorDimensions(v.data.length);
/* 235:422 */     T[] out = buildArray(this.data.length);
/* 236:423 */     for (int i = 0; i < this.data.length; i++) {
/* 237:424 */       out[i] = (T) ((FieldElement)this.data[i].add(v.data[i]));
/* 238:    */     }
/* 239:426 */     return new ArrayFieldVector(this.field, out, false);
/* 240:    */   }
/* 241:    */   
/* 242:    */   public FieldVector<T> subtract(FieldVector<T> v)
/* 243:    */   {
/* 244:    */     try
/* 245:    */     {
/* 246:432 */       return subtract((ArrayFieldVector)v);
/* 247:    */     }
/* 248:    */     catch (ClassCastException cce)
/* 249:    */     {
/* 250:434 */       checkVectorDimensions(v);
/* 251:435 */       T[] out = buildArray(this.data.length);
/* 252:436 */       for (int i = 0; i < this.data.length; i++) {
/* 253:437 */         out[i] = (T) ((FieldElement)this.data[i].subtract(v.getEntry(i)));
/* 254:    */       }
/* 255:439 */       return new ArrayFieldVector(this.field, out, false);
/* 256:    */     }
/* 257:    */   }
/* 258:    */   
/* 259:    */   public ArrayFieldVector<T> subtract(ArrayFieldVector<T> v)
/* 260:    */   {
/* 261:450 */     checkVectorDimensions(v.data.length);
/* 262:451 */     T[] out = buildArray(this.data.length);
/* 263:452 */     for (int i = 0; i < this.data.length; i++) {
/* 264:453 */       out[i] = (T) ((FieldElement)this.data[i].subtract(v.data[i]));
/* 265:    */     }
/* 266:455 */     return new ArrayFieldVector(this.field, out, false);
/* 267:    */   }
/* 268:    */   
/* 269:    */   public FieldVector<T> mapAdd(T d)
/* 270:    */   {
/* 271:460 */     T[] out = buildArray(this.data.length);
/* 272:461 */     for (int i = 0; i < this.data.length; i++) {
/* 273:462 */       out[i] = (T) ((FieldElement)this.data[i].add(d));
/* 274:    */     }
/* 275:464 */     return new ArrayFieldVector(this.field, out, false);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public FieldVector<T> mapAddToSelf(T d)
/* 279:    */   {
/* 280:469 */     for (int i = 0; i < this.data.length; i++) {
/* 281:470 */       this.data[i] = (T) ((FieldElement)this.data[i].add(d));
/* 282:    */     }
/* 283:472 */     return this;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public FieldVector<T> mapSubtract(T d)
/* 287:    */   {
/* 288:477 */     T[] out = buildArray(this.data.length);
/* 289:478 */     for (int i = 0; i < this.data.length; i++) {
/* 290:479 */       out[i] = (T) ((FieldElement)this.data[i].subtract(d));
/* 291:    */     }
/* 292:481 */     return new ArrayFieldVector(this.field, out, false);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public FieldVector<T> mapSubtractToSelf(T d)
/* 296:    */   {
/* 297:486 */     for (int i = 0; i < this.data.length; i++) {
/* 298:487 */       this.data[i] = (T) ((FieldElement)this.data[i].subtract(d));
/* 299:    */     }
/* 300:489 */     return this;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public FieldVector<T> mapMultiply(T d)
/* 304:    */   {
/* 305:494 */     T[] out = buildArray(this.data.length);
/* 306:495 */     for (int i = 0; i < this.data.length; i++) {
/* 307:496 */       out[i] = (T) ((FieldElement)this.data[i].multiply(d));
/* 308:    */     }
/* 309:498 */     return new ArrayFieldVector(this.field, out, false);
/* 310:    */   }
/* 311:    */   
/* 312:    */   public FieldVector<T> mapMultiplyToSelf(T d)
/* 313:    */   {
/* 314:503 */     for (int i = 0; i < this.data.length; i++) {
/* 315:504 */       this.data[i] = (T) ((FieldElement)this.data[i].multiply(d));
/* 316:    */     }
/* 317:506 */     return this;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public FieldVector<T> mapDivide(T d)
/* 321:    */   {
/* 322:511 */     T[] out = buildArray(this.data.length);
/* 323:512 */     for (int i = 0; i < this.data.length; i++) {
/* 324:513 */       out[i] = (T) ((FieldElement)this.data[i].divide(d));
/* 325:    */     }
/* 326:515 */     return new ArrayFieldVector(this.field, out, false);
/* 327:    */   }
/* 328:    */   
/* 329:    */   public FieldVector<T> mapDivideToSelf(T d)
/* 330:    */   {
/* 331:520 */     for (int i = 0; i < this.data.length; i++) {
/* 332:521 */       this.data[i] = (T) ((FieldElement)this.data[i].divide(d));
/* 333:    */     }
/* 334:523 */     return this;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public FieldVector<T> mapInv()
/* 338:    */   {
/* 339:528 */     T[] out = buildArray(this.data.length);
/* 340:529 */     T one = (T)this.field.getOne();
/* 341:530 */     for (int i = 0; i < this.data.length; i++) {
/* 342:531 */       out[i] = (T) ((FieldElement)one.divide(this.data[i]));
/* 343:    */     }
/* 344:533 */     return new ArrayFieldVector(this.field, out, false);
/* 345:    */   }
/* 346:    */   
/* 347:    */   public FieldVector<T> mapInvToSelf()
/* 348:    */   {
/* 349:538 */     T one = (T)this.field.getOne();
/* 350:539 */     for (int i = 0; i < this.data.length; i++) {
/* 351:540 */       this.data[i] = (T) ((FieldElement)one.divide(this.data[i]));
/* 352:    */     }
/* 353:542 */     return this;
/* 354:    */   }
/* 355:    */   
/* 356:    */   public FieldVector<T> ebeMultiply(FieldVector<T> v)
/* 357:    */   {
/* 358:    */     try
/* 359:    */     {
/* 360:548 */       return ebeMultiply((ArrayFieldVector)v);
/* 361:    */     }
/* 362:    */     catch (ClassCastException cce)
/* 363:    */     {
/* 364:550 */       checkVectorDimensions(v);
/* 365:551 */       T[] out = buildArray(this.data.length);
/* 366:552 */       for (int i = 0; i < this.data.length; i++) {
/* 367:553 */         out[i] = (T) ((FieldElement)this.data[i].multiply(v.getEntry(i)));
/* 368:    */       }
/* 369:555 */       return new ArrayFieldVector(this.field, out, false);
/* 370:    */     }
/* 371:    */   }
/* 372:    */   
/* 373:    */   public ArrayFieldVector<T> ebeMultiply(ArrayFieldVector<T> v)
/* 374:    */   {
/* 375:566 */     checkVectorDimensions(v.data.length);
/* 376:567 */     T[] out = buildArray(this.data.length);
/* 377:568 */     for (int i = 0; i < this.data.length; i++) {
/* 378:569 */       out[i] = (T) ((FieldElement)this.data[i].multiply(v.data[i]));
/* 379:    */     }
/* 380:571 */     return new ArrayFieldVector(this.field, out, false);
/* 381:    */   }
/* 382:    */   
/* 383:    */   public FieldVector<T> ebeDivide(FieldVector<T> v)
/* 384:    */   {
/* 385:    */     try
/* 386:    */     {
/* 387:577 */       return ebeDivide((ArrayFieldVector)v);
/* 388:    */     }
/* 389:    */     catch (ClassCastException cce)
/* 390:    */     {
/* 391:579 */       checkVectorDimensions(v);
/* 392:580 */       T[] out = buildArray(this.data.length);
/* 393:581 */       for (int i = 0; i < this.data.length; i++) {
/* 394:582 */         out[i] = (T) ((FieldElement)this.data[i].divide(v.getEntry(i)));
/* 395:    */       }
/* 396:584 */       return new ArrayFieldVector(this.field, out, false);
/* 397:    */     }
/* 398:    */   }
/* 399:    */   
/* 400:    */   public ArrayFieldVector<T> ebeDivide(ArrayFieldVector<T> v)
/* 401:    */   {
/* 402:595 */     checkVectorDimensions(v.data.length);
/* 403:596 */     T[] out = buildArray(this.data.length);
/* 404:597 */     for (int i = 0; i < this.data.length; i++) {
/* 405:598 */       out[i] = (T) ((FieldElement)this.data[i].divide(v.data[i]));
/* 406:    */     }
/* 407:600 */     return new ArrayFieldVector(this.field, out, false);
/* 408:    */   }
/* 409:    */   
/* 410:    */   public T[] getData()
/* 411:    */   {
/* 412:605 */     return (T[])this.data.clone();
/* 413:    */   }
/* 414:    */   
/* 415:    */   public T[] getDataRef()
/* 416:    */   {
/* 417:614 */     return this.data;
/* 418:    */   }
/* 419:    */   
/* 420:    */   public T dotProduct(FieldVector<T> v)
/* 421:    */   {
/* 422:    */     try
/* 423:    */     {
/* 424:620 */       return dotProduct((ArrayFieldVector)v);
/* 425:    */     }
/* 426:    */     catch (ClassCastException cce)
/* 427:    */     {
/* 428:622 */       checkVectorDimensions(v);
/* 429:623 */       T dot = (T)this.field.getZero();
/* 430:624 */       for (int i = 0; i < this.data.length; i++) {
/* 431:625 */         dot = (T)dot.add(this.data[i].multiply(v.getEntry(i)));
/* 432:    */       }
/* 433:627 */       return dot;
/* 434:    */     }
/* 435:    */   }
/* 436:    */   
/* 437:    */   public T dotProduct(ArrayFieldVector<T> v)
/* 438:    */   {
/* 439:638 */     checkVectorDimensions(v.data.length);
/* 440:639 */     T dot = (T)this.field.getZero();
/* 441:640 */     for (int i = 0; i < this.data.length; i++) {
/* 442:641 */       dot = (T)dot.add(this.data[i].multiply(v.data[i]));
/* 443:    */     }
/* 444:643 */     return dot;
/* 445:    */   }
/* 446:    */   
/* 447:    */   public FieldVector<T> projection(FieldVector<T> v)
/* 448:    */   {
/* 449:648 */     return v.mapMultiply((T)dotProduct(v).divide(v.dotProduct(v)));
/* 450:    */   }
/* 451:    */   
/* 452:    */   public ArrayFieldVector<T> projection(ArrayFieldVector<T> v)
/* 453:    */   {
/* 454:657 */     return (ArrayFieldVector)v.mapMultiply((T)dotProduct(v).divide(v.dotProduct(v)));
/* 455:    */   }
/* 456:    */   
/* 457:    */   public FieldMatrix<T> outerProduct(FieldVector<T> v)
/* 458:    */   {
/* 459:    */     try
/* 460:    */     {
/* 461:663 */       return outerProduct((ArrayFieldVector)v);
/* 462:    */     }
/* 463:    */     catch (ClassCastException cce)
/* 464:    */     {
/* 465:665 */       int m = this.data.length;
/* 466:666 */       int n = v.getDimension();
/* 467:667 */       FieldMatrix<T> out = new Array2DRowFieldMatrix(this.field, m, n);
/* 468:668 */       for (int i = 0; i < m; i++) {
/* 469:669 */         for (int j = 0; j < n; j++) {
/* 470:670 */           out.setEntry(i, j, (T)this.data[i].multiply(v.getEntry(j)));
/* 471:    */         }
/* 472:    */       }
/* 473:673 */       return out;
/* 474:    */     }
/* 475:    */   }
/* 476:    */   
/* 477:    */   public FieldMatrix<T> outerProduct(ArrayFieldVector<T> v)
/* 478:    */   {
/* 479:684 */     int m = this.data.length;
/* 480:685 */     int n = v.data.length;
/* 481:686 */     FieldMatrix<T> out = new Array2DRowFieldMatrix(this.field, m, n);
/* 482:687 */     for (int i = 0; i < m; i++) {
/* 483:688 */       for (int j = 0; j < n; j++) {
/* 484:689 */         out.setEntry(i, j, (T)this.data[i].multiply(v.data[j]));
/* 485:    */       }
/* 486:    */     }
/* 487:692 */     return out;
/* 488:    */   }
/* 489:    */   
/* 490:    */   public T getEntry(int index)
/* 491:    */   {
/* 492:697 */     return this.data[index];
/* 493:    */   }
/* 494:    */   
/* 495:    */   public int getDimension()
/* 496:    */   {
/* 497:702 */     return this.data.length;
/* 498:    */   }
/* 499:    */   
/* 500:    */   public FieldVector<T> append(FieldVector<T> v)
/* 501:    */   {
/* 502:    */     try
/* 503:    */     {
/* 504:708 */       return append((ArrayFieldVector)v);
/* 505:    */     }
/* 506:    */     catch (ClassCastException cce) {}
/* 507:710 */     return new ArrayFieldVector(this, new ArrayFieldVector(v));
/* 508:    */   }
/* 509:    */   
/* 510:    */   public ArrayFieldVector<T> append(ArrayFieldVector<T> v)
/* 511:    */   {
/* 512:720 */     return new ArrayFieldVector(this, v);
/* 513:    */   }
/* 514:    */   
/* 515:    */   public FieldVector<T> append(T in)
/* 516:    */   {
/* 517:725 */     T[] out = buildArray(this.data.length + 1);
/* 518:726 */     System.arraycopy(this.data, 0, out, 0, this.data.length);
/* 519:727 */     out[this.data.length] = in;
/* 520:728 */     return new ArrayFieldVector(this.field, out, false);
/* 521:    */   }
/* 522:    */   
/* 523:    */   public FieldVector<T> getSubVector(int index, int n)
/* 524:    */   {
/* 525:733 */     ArrayFieldVector<T> out = new ArrayFieldVector(this.field, n);
/* 526:    */     try
/* 527:    */     {
/* 528:735 */       System.arraycopy(this.data, index, out.data, 0, n);
/* 529:    */     }
/* 530:    */     catch (IndexOutOfBoundsException e)
/* 531:    */     {
/* 532:737 */       checkIndex(index);
/* 533:738 */       checkIndex(index + n - 1);
/* 534:    */     }
/* 535:740 */     return out;
/* 536:    */   }
/* 537:    */   
/* 538:    */   public void setEntry(int index, T value)
/* 539:    */   {
/* 540:    */     try
/* 541:    */     {
/* 542:746 */       this.data[index] = value;
/* 543:    */     }
/* 544:    */     catch (IndexOutOfBoundsException e)
/* 545:    */     {
/* 546:748 */       checkIndex(index);
/* 547:    */     }
/* 548:    */   }
/* 549:    */   
/* 550:    */   public void setSubVector(int index, FieldVector<T> v)
/* 551:    */   {
/* 552:    */     try
/* 553:    */     {
/* 554:    */       try
/* 555:    */       {
/* 556:756 */         set(index, (ArrayFieldVector)v);
/* 557:    */       }
/* 558:    */       catch (ClassCastException cce)
/* 559:    */       {
/* 560:758 */         for (int i = index; i < index + v.getDimension(); i++) {
/* 561:759 */           this.data[i] = v.getEntry(i - index);
/* 562:    */         }
/* 563:    */       }
/* 564:    */     }
/* 565:    */     catch (IndexOutOfBoundsException e)
/* 566:    */     {
/* 567:763 */       checkIndex(index);
/* 568:764 */       checkIndex(index + v.getDimension() - 1);
/* 569:    */     }
/* 570:    */   }
/* 571:    */   
/* 572:    */   public void set(int index, ArrayFieldVector<T> v)
/* 573:    */   {
/* 574:    */     try
/* 575:    */     {
/* 576:778 */       System.arraycopy(v.data, 0, this.data, index, v.data.length);
/* 577:    */     }
/* 578:    */     catch (IndexOutOfBoundsException e)
/* 579:    */     {
/* 580:780 */       checkIndex(index);
/* 581:781 */       checkIndex(index + v.data.length - 1);
/* 582:    */     }
/* 583:    */   }
/* 584:    */   
/* 585:    */   public void set(T value)
/* 586:    */   {
/* 587:787 */     Arrays.fill(this.data, value);
/* 588:    */   }
/* 589:    */   
/* 590:    */   public T[] toArray()
/* 591:    */   {
/* 592:792 */     return (T[])this.data.clone();
/* 593:    */   }
/* 594:    */   
/* 595:    */   protected void checkVectorDimensions(FieldVector<T> v)
/* 596:    */   {
/* 597:802 */     checkVectorDimensions(v.getDimension());
/* 598:    */   }
/* 599:    */   
/* 600:    */   protected void checkVectorDimensions(int n)
/* 601:    */   {
/* 602:813 */     if (this.data.length != n) {
/* 603:814 */       throw new DimensionMismatchException(this.data.length, n);
/* 604:    */     }
/* 605:    */   }
/* 606:    */   
/* 607:    */   public boolean equals(Object other)
/* 608:    */   {
/* 609:827 */     if (this == other) {
/* 610:828 */       return true;
/* 611:    */     }
/* 612:830 */     if (other == null) {
/* 613:831 */       return false;
/* 614:    */     }
/* 615:    */     try
/* 616:    */     {
/* 617:836 */       FieldVector<T> rhs = (FieldVector)other;
/* 618:837 */       if (this.data.length != rhs.getDimension()) {
/* 619:838 */         return false;
/* 620:    */       }
/* 621:841 */       for (int i = 0; i < this.data.length; i++) {
/* 622:842 */         if (!this.data[i].equals(rhs.getEntry(i))) {
/* 623:843 */           return false;
/* 624:    */         }
/* 625:    */       }
/* 626:846 */       return true;
/* 627:    */     }
/* 628:    */     catch (ClassCastException ex) {}
/* 629:849 */     return false;
/* 630:    */   }
/* 631:    */   
/* 632:    */   public int hashCode()
/* 633:    */   {
/* 634:860 */     int h = 3542;
/* 635:861 */     for (T a : this.data) {
/* 636:862 */       h ^= a.hashCode();
/* 637:    */     }
/* 638:864 */     return h;
/* 639:    */   }
/* 640:    */   
/* 641:    */   private void checkIndex(int index)
/* 642:    */   {
/* 643:874 */     if ((index < 0) || (index >= getDimension())) {
/* 644:875 */       throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(getDimension() - 1));
/* 645:    */     }
/* 646:    */   }
/* 647:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.ArrayFieldVector
 * JD-Core Version:    0.7.0.1
 */
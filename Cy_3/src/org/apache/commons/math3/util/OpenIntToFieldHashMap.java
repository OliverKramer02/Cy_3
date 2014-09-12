/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import java.lang.reflect.Array;
/*   7:    */ import java.util.ConcurrentModificationException;
/*   8:    */ import java.util.NoSuchElementException;
/*   9:    */ import org.apache.commons.math3.Field;
/*  10:    */ import org.apache.commons.math3.FieldElement;
/*  11:    */ 
/*  12:    */ public class OpenIntToFieldHashMap<T extends FieldElement<T>>
/*  13:    */   implements Serializable
/*  14:    */ {
/*  15:    */   protected static final byte FREE = 0;
/*  16:    */   protected static final byte FULL = 1;
/*  17:    */   protected static final byte REMOVED = 2;
/*  18:    */   private static final long serialVersionUID = -9179080286849120720L;
/*  19:    */   private static final float LOAD_FACTOR = 0.5F;
/*  20:    */   private static final int DEFAULT_EXPECTED_SIZE = 16;
/*  21:    */   private static final int RESIZE_MULTIPLIER = 2;
/*  22:    */   private static final int PERTURB_SHIFT = 5;
/*  23:    */   private final Field<T> field;
/*  24:    */   private int[] keys;
/*  25:    */   private T[] values;
/*  26:    */   private byte[] states;
/*  27:    */   private final T missingEntries;
/*  28:    */   private int size;
/*  29:    */   private int mask;
/*  30:    */   private transient int count;
/*  31:    */   
/*  32:    */   public OpenIntToFieldHashMap(Field<T> field)
/*  33:    */   {
/*  34:100 */     this(field, 16, (T)field.getZero());
/*  35:    */   }
/*  36:    */   
/*  37:    */   public OpenIntToFieldHashMap(Field<T> field, T missingEntries)
/*  38:    */   {
/*  39:109 */     this(field, 16, missingEntries);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public OpenIntToFieldHashMap(Field<T> field, int expectedSize)
/*  43:    */   {
/*  44:118 */     this(field, expectedSize, (T)field.getZero());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public OpenIntToFieldHashMap(Field<T> field, int expectedSize, T missingEntries)
/*  48:    */   {
/*  49:129 */     this.field = field;
/*  50:130 */     int capacity = computeCapacity(expectedSize);
/*  51:131 */     this.keys = new int[capacity];
/*  52:132 */     this.values = buildArray(capacity);
/*  53:133 */     this.states = new byte[capacity];
/*  54:134 */     this.missingEntries = missingEntries;
/*  55:135 */     this.mask = (capacity - 1);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public OpenIntToFieldHashMap(OpenIntToFieldHashMap<T> source)
/*  59:    */   {
/*  60:143 */     this.field = source.field;
/*  61:144 */     int length = source.keys.length;
/*  62:145 */     this.keys = new int[length];
/*  63:146 */     System.arraycopy(source.keys, 0, this.keys, 0, length);
/*  64:147 */     this.values = buildArray(length);
/*  65:148 */     System.arraycopy(source.values, 0, this.values, 0, length);
/*  66:149 */     this.states = new byte[length];
/*  67:150 */     System.arraycopy(source.states, 0, this.states, 0, length);
/*  68:151 */     this.missingEntries = source.missingEntries;
/*  69:152 */     this.size = source.size;
/*  70:153 */     this.mask = source.mask;
/*  71:154 */     this.count = source.count;
/*  72:    */   }
/*  73:    */   
/*  74:    */   private static int computeCapacity(int expectedSize)
/*  75:    */   {
/*  76:163 */     if (expectedSize == 0) {
/*  77:164 */       return 1;
/*  78:    */     }
/*  79:166 */     int capacity = (int)FastMath.ceil(expectedSize / 0.5F);
/*  80:167 */     int powerOfTwo = Integer.highestOneBit(capacity);
/*  81:168 */     if (powerOfTwo == capacity) {
/*  82:169 */       return capacity;
/*  83:    */     }
/*  84:171 */     return nextPowerOfTwo(capacity);
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static int nextPowerOfTwo(int i)
/*  88:    */   {
/*  89:180 */     return Integer.highestOneBit(i) << 1;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public T get(int key)
/*  93:    */   {
/*  94:190 */     int hash = hashOf(key);
/*  95:191 */     int index = hash & this.mask;
/*  96:192 */     if (containsKey(key, index)) {
/*  97:193 */       return this.values[index];
/*  98:    */     }
/*  99:196 */     if (this.states[index] == 0) {
/* 100:197 */       return this.missingEntries;
/* 101:    */     }
/* 102:200 */     int j = index;
/* 103:201 */     for (int perturb = perturb(hash); this.states[index] != 0; perturb >>= 5)
/* 104:    */     {
/* 105:202 */       j = probe(perturb, j);
/* 106:203 */       index = j & this.mask;
/* 107:204 */       if (containsKey(key, index)) {
/* 108:205 */         return this.values[index];
/* 109:    */       }
/* 110:    */     }
/* 111:209 */     return this.missingEntries;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean containsKey(int key)
/* 115:    */   {
/* 116:220 */     int hash = hashOf(key);
/* 117:221 */     int index = hash & this.mask;
/* 118:222 */     if (containsKey(key, index)) {
/* 119:223 */       return true;
/* 120:    */     }
/* 121:226 */     if (this.states[index] == 0) {
/* 122:227 */       return false;
/* 123:    */     }
/* 124:230 */     int j = index;
/* 125:231 */     for (int perturb = perturb(hash); this.states[index] != 0; perturb >>= 5)
/* 126:    */     {
/* 127:232 */       j = probe(perturb, j);
/* 128:233 */       index = j & this.mask;
/* 129:234 */       if (containsKey(key, index)) {
/* 130:235 */         return true;
/* 131:    */       }
/* 132:    */     }
/* 133:239 */     return false;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public OpenIntToFieldHashMap<T>.Iterator iterator()
/* 137:    */   {
/* 138:251 */     return new Iterator();
/* 139:    */   }
/* 140:    */   
/* 141:    */   private static int perturb(int hash)
/* 142:    */   {
/* 143:260 */     return hash & 0x7FFFFFFF;
/* 144:    */   }
/* 145:    */   
/* 146:    */   private int findInsertionIndex(int key)
/* 147:    */   {
/* 148:269 */     return findInsertionIndex(this.keys, this.states, key, this.mask);
/* 149:    */   }
/* 150:    */   
/* 151:    */   private static int findInsertionIndex(int[] keys, byte[] states, int key, int mask)
/* 152:    */   {
/* 153:282 */     int hash = hashOf(key);
/* 154:283 */     int index = hash & mask;
/* 155:284 */     if (states[index] == 0) {
/* 156:285 */       return index;
/* 157:    */     }
/* 158:286 */     if ((states[index] == 1) && (keys[index] == key)) {
/* 159:287 */       return changeIndexSign(index);
/* 160:    */     }
/* 161:290 */     int perturb = perturb(hash);
/* 162:291 */     int j = index;
/* 163:292 */     if (states[index] == 1) {
/* 164:    */       for (;;)
/* 165:    */       {
/* 166:294 */         j = probe(perturb, j);
/* 167:295 */         index = j & mask;
/* 168:296 */         perturb >>= 5;
/* 169:298 */         if (states[index] == 1) {
/* 170:298 */           if (keys[index] == key) {
/* 171:    */             break;
/* 172:    */           }
/* 173:    */         }
/* 174:    */       }
/* 175:    */     }
/* 176:304 */     if (states[index] == 0) {
/* 177:305 */       return index;
/* 178:    */     }
/* 179:306 */     if (states[index] == 1) {
/* 180:309 */       return changeIndexSign(index);
/* 181:    */     }
/* 182:312 */     int firstRemoved = index;
/* 183:    */     for (;;)
/* 184:    */     {
/* 185:314 */       j = probe(perturb, j);
/* 186:315 */       index = j & mask;
/* 187:317 */       if (states[index] == 0) {
/* 188:318 */         return firstRemoved;
/* 189:    */       }
/* 190:319 */       if ((states[index] == 1) && (keys[index] == key)) {
/* 191:320 */         return changeIndexSign(index);
/* 192:    */       }
/* 193:323 */       perturb >>= 5;
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   private static int probe(int perturb, int j)
/* 198:    */   {
/* 199:336 */     return (j << 2) + j + perturb + 1;
/* 200:    */   }
/* 201:    */   
/* 202:    */   private static int changeIndexSign(int index)
/* 203:    */   {
/* 204:345 */     return -index - 1;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public int size()
/* 208:    */   {
/* 209:353 */     return this.size;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public T remove(int key)
/* 213:    */   {
/* 214:364 */     int hash = hashOf(key);
/* 215:365 */     int index = hash & this.mask;
/* 216:366 */     if (containsKey(key, index)) {
/* 217:367 */       return doRemove(index);
/* 218:    */     }
/* 219:370 */     if (this.states[index] == 0) {
/* 220:371 */       return this.missingEntries;
/* 221:    */     }
/* 222:374 */     int j = index;
/* 223:375 */     for (int perturb = perturb(hash); this.states[index] != 0; perturb >>= 5)
/* 224:    */     {
/* 225:376 */       j = probe(perturb, j);
/* 226:377 */       index = j & this.mask;
/* 227:378 */       if (containsKey(key, index)) {
/* 228:379 */         return doRemove(index);
/* 229:    */       }
/* 230:    */     }
/* 231:383 */     return this.missingEntries;
/* 232:    */   }
/* 233:    */   
/* 234:    */   private boolean containsKey(int key, int index)
/* 235:    */   {
/* 236:395 */     return ((key != 0) || (this.states[index] == 1)) && (this.keys[index] == key);
/* 237:    */   }
/* 238:    */   
/* 239:    */   private T doRemove(int index)
/* 240:    */   {
/* 241:404 */     this.keys[index] = 0;
/* 242:405 */     this.states[index] = 2;
/* 243:406 */     T previous = this.values[index];
/* 244:407 */     this.values[index] = this.missingEntries;
/* 245:408 */     this.size -= 1;
/* 246:409 */     this.count += 1;
/* 247:410 */     return previous;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public T put(int key, T value)
/* 251:    */   {
/* 252:420 */     int index = findInsertionIndex(key);
/* 253:421 */     T previous = this.missingEntries;
/* 254:422 */     boolean newMapping = true;
/* 255:423 */     if (index < 0)
/* 256:    */     {
/* 257:424 */       index = changeIndexSign(index);
/* 258:425 */       previous = this.values[index];
/* 259:426 */       newMapping = false;
/* 260:    */     }
/* 261:428 */     this.keys[index] = key;
/* 262:429 */     this.states[index] = 1;
/* 263:430 */     this.values[index] = value;
/* 264:431 */     if (newMapping)
/* 265:    */     {
/* 266:432 */       this.size += 1;
/* 267:433 */       if (shouldGrowTable()) {
/* 268:434 */         growTable();
/* 269:    */       }
/* 270:436 */       this.count += 1;
/* 271:    */     }
/* 272:438 */     return previous;
/* 273:    */   }
/* 274:    */   
/* 275:    */   private void growTable()
/* 276:    */   {
/* 277:447 */     int oldLength = this.states.length;
/* 278:448 */     int[] oldKeys = this.keys;
/* 279:449 */     T[] oldValues = this.values;
/* 280:450 */     byte[] oldStates = this.states;
/* 281:    */     
/* 282:452 */     int newLength = 2 * oldLength;
/* 283:453 */     int[] newKeys = new int[newLength];
/* 284:454 */     T[] newValues = buildArray(newLength);
/* 285:455 */     byte[] newStates = new byte[newLength];
/* 286:456 */     int newMask = newLength - 1;
/* 287:457 */     for (int i = 0; i < oldLength; i++) {
/* 288:458 */       if (oldStates[i] == 1)
/* 289:    */       {
/* 290:459 */         int key = oldKeys[i];
/* 291:460 */         int index = findInsertionIndex(newKeys, newStates, key, newMask);
/* 292:461 */         newKeys[index] = key;
/* 293:462 */         newValues[index] = oldValues[i];
/* 294:463 */         newStates[index] = 1;
/* 295:    */       }
/* 296:    */     }
/* 297:467 */     this.mask = newMask;
/* 298:468 */     this.keys = newKeys;
/* 299:469 */     this.values = newValues;
/* 300:470 */     this.states = newStates;
/* 301:    */   }
/* 302:    */   
/* 303:    */   private boolean shouldGrowTable()
/* 304:    */   {
/* 305:479 */     return this.size > (this.mask + 1) * 0.5F;
/* 306:    */   }
/* 307:    */   
/* 308:    */   private static int hashOf(int key)
/* 309:    */   {
/* 310:488 */     int h = key ^ key >>> 20 ^ key >>> 12;
/* 311:489 */     return h ^ h >>> 7 ^ h >>> 4;
/* 312:    */   }
/* 313:    */   
/* 314:    */   public class Iterator
/* 315:    */   {
/* 316:    */     private final int referenceCount;
/* 317:    */     private int current;
/* 318:    */     private int next;
/* 319:    */     
/* 320:    */     private Iterator()
/* 321:    */     {
/* 322:511 */       this.referenceCount = OpenIntToFieldHashMap.this.count;
/* 323:    */       
/* 324:    */ 
/* 325:514 */       this.next = -1;
/* 326:    */       try
/* 327:    */       {
/* 328:516 */         advance();
/* 329:    */       }
/* 330:    */       catch (NoSuchElementException localNoSuchElementException) {}
/* 331:    */      
/* 332:    */     }
/* 333:    */     
/* 334:    */     public boolean hasNext()
/* 335:    */     {
/* 336:528 */       return this.next >= 0;
/* 337:    */     }
/* 338:    */     
/* 339:    */     public int key()
/* 340:    */       throws ConcurrentModificationException, NoSuchElementException
/* 341:    */     {
/* 342:539 */       if (this.referenceCount != OpenIntToFieldHashMap.this.count) {
/* 343:540 */         throw new ConcurrentModificationException();
/* 344:    */       }
/* 345:542 */       if (this.current < 0) {
/* 346:543 */         throw new NoSuchElementException();
/* 347:    */       }
/* 348:545 */       return OpenIntToFieldHashMap.this.keys[this.current];
/* 349:    */     }
/* 350:    */     
/* 351:    */     public T value()
/* 352:    */       throws ConcurrentModificationException, NoSuchElementException
/* 353:    */     {
/* 354:556 */       if (this.referenceCount != OpenIntToFieldHashMap.this.count) {
/* 355:557 */         throw new ConcurrentModificationException();
/* 356:    */       }
/* 357:559 */       if (this.current < 0) {
/* 358:560 */         throw new NoSuchElementException();
/* 359:    */       }
/* 360:562 */       return OpenIntToFieldHashMap.this.values[this.current];
/* 361:    */     }
/* 362:    */     
/* 363:    */     public void advance()
/* 364:    */       throws ConcurrentModificationException, NoSuchElementException
/* 365:    */     {
/* 366:573 */       if (this.referenceCount != OpenIntToFieldHashMap.this.count) {
/* 367:574 */         throw new ConcurrentModificationException();
/* 368:    */       }
/* 369:578 */       this.current = this.next;
/* 370:    */       try
/* 371:    */       {
/* 372:582 */         while (OpenIntToFieldHashMap.this.states[(++this.next)] != 1) {}
/* 373:    */       }
/* 374:    */       catch (ArrayIndexOutOfBoundsException e)
/* 375:    */       {
/* 376:586 */         this.next = -2;
/* 377:587 */         if (this.current < 0) {
/* 378:588 */           throw new NoSuchElementException();
/* 379:    */         }
/* 380:    */       }
/* 381:    */     }
/* 382:    */   }
/* 383:    */   
/* 384:    */   private void readObject(ObjectInputStream stream)
/* 385:    */     throws IOException, ClassNotFoundException
/* 386:    */   {
/* 387:605 */     stream.defaultReadObject();
/* 388:606 */     this.count = 0;
/* 389:    */   }
/* 390:    */   
/* 391:    */   private T[] buildArray(int length)
/* 392:    */   {
/* 393:615 */     return (T[])Array.newInstance(this.field.getRuntimeClass(), length);
/* 394:    */   }
/* 395:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.OpenIntToFieldHashMap
 * JD-Core Version:    0.7.0.1
 */
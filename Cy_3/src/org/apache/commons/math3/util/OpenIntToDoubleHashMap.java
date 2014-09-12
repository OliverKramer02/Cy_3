/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import java.util.ConcurrentModificationException;
/*   7:    */ import java.util.NoSuchElementException;
/*   8:    */ 
/*   9:    */ public class OpenIntToDoubleHashMap
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   protected static final byte FREE = 0;
/*  13:    */   protected static final byte FULL = 1;
/*  14:    */   protected static final byte REMOVED = 2;
/*  15:    */   private static final long serialVersionUID = -3646337053166149105L;
/*  16:    */   private static final float LOAD_FACTOR = 0.5F;
/*  17:    */   private static final int DEFAULT_EXPECTED_SIZE = 16;
/*  18:    */   private static final int RESIZE_MULTIPLIER = 2;
/*  19:    */   private static final int PERTURB_SHIFT = 5;
/*  20:    */   private int[] keys;
/*  21:    */   private double[] values;
/*  22:    */   private byte[] states;
/*  23:    */   private final double missingEntries;
/*  24:    */   private int size;
/*  25:    */   private int mask;
/*  26:    */   private transient int count;
/*  27:    */   
/*  28:    */   public OpenIntToDoubleHashMap()
/*  29:    */   {
/*  30: 92 */     this(16, (0.0D / 0.0D));
/*  31:    */   }
/*  32:    */   
/*  33:    */   public OpenIntToDoubleHashMap(double missingEntries)
/*  34:    */   {
/*  35:100 */     this(16, missingEntries);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public OpenIntToDoubleHashMap(int expectedSize)
/*  39:    */   {
/*  40:108 */     this(expectedSize, (0.0D / 0.0D));
/*  41:    */   }
/*  42:    */   
/*  43:    */   public OpenIntToDoubleHashMap(int expectedSize, double missingEntries)
/*  44:    */   {
/*  45:118 */     int capacity = computeCapacity(expectedSize);
/*  46:119 */     this.keys = new int[capacity];
/*  47:120 */     this.values = new double[capacity];
/*  48:121 */     this.states = new byte[capacity];
/*  49:122 */     this.missingEntries = missingEntries;
/*  50:123 */     this.mask = (capacity - 1);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public OpenIntToDoubleHashMap(OpenIntToDoubleHashMap source)
/*  54:    */   {
/*  55:131 */     int length = source.keys.length;
/*  56:132 */     this.keys = new int[length];
/*  57:133 */     System.arraycopy(source.keys, 0, this.keys, 0, length);
/*  58:134 */     this.values = new double[length];
/*  59:135 */     System.arraycopy(source.values, 0, this.values, 0, length);
/*  60:136 */     this.states = new byte[length];
/*  61:137 */     System.arraycopy(source.states, 0, this.states, 0, length);
/*  62:138 */     this.missingEntries = source.missingEntries;
/*  63:139 */     this.size = source.size;
/*  64:140 */     this.mask = source.mask;
/*  65:141 */     this.count = source.count;
/*  66:    */   }
/*  67:    */   
/*  68:    */   private static int computeCapacity(int expectedSize)
/*  69:    */   {
/*  70:150 */     if (expectedSize == 0) {
/*  71:151 */       return 1;
/*  72:    */     }
/*  73:153 */     int capacity = (int)FastMath.ceil(expectedSize / 0.5F);
/*  74:154 */     int powerOfTwo = Integer.highestOneBit(capacity);
/*  75:155 */     if (powerOfTwo == capacity) {
/*  76:156 */       return capacity;
/*  77:    */     }
/*  78:158 */     return nextPowerOfTwo(capacity);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private static int nextPowerOfTwo(int i)
/*  82:    */   {
/*  83:167 */     return Integer.highestOneBit(i) << 1;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public double get(int key)
/*  87:    */   {
/*  88:177 */     int hash = hashOf(key);
/*  89:178 */     int index = hash & this.mask;
/*  90:179 */     if (containsKey(key, index)) {
/*  91:180 */       return this.values[index];
/*  92:    */     }
/*  93:183 */     if (this.states[index] == 0) {
/*  94:184 */       return this.missingEntries;
/*  95:    */     }
/*  96:187 */     int j = index;
/*  97:188 */     for (int perturb = perturb(hash); this.states[index] != 0; perturb >>= 5)
/*  98:    */     {
/*  99:189 */       j = probe(perturb, j);
/* 100:190 */       index = j & this.mask;
/* 101:191 */       if (containsKey(key, index)) {
/* 102:192 */         return this.values[index];
/* 103:    */       }
/* 104:    */     }
/* 105:196 */     return this.missingEntries;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean containsKey(int key)
/* 109:    */   {
/* 110:207 */     int hash = hashOf(key);
/* 111:208 */     int index = hash & this.mask;
/* 112:209 */     if (containsKey(key, index)) {
/* 113:210 */       return true;
/* 114:    */     }
/* 115:213 */     if (this.states[index] == 0) {
/* 116:214 */       return false;
/* 117:    */     }
/* 118:217 */     int j = index;
/* 119:218 */     for (int perturb = perturb(hash); this.states[index] != 0; perturb >>= 5)
/* 120:    */     {
/* 121:219 */       j = probe(perturb, j);
/* 122:220 */       index = j & this.mask;
/* 123:221 */       if (containsKey(key, index)) {
/* 124:222 */         return true;
/* 125:    */       }
/* 126:    */     }
/* 127:226 */     return false;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public Iterator iterator()
/* 131:    */   {
/* 132:238 */     return new Iterator();
/* 133:    */   }
/* 134:    */   
/* 135:    */   private static int perturb(int hash)
/* 136:    */   {
/* 137:247 */     return hash & 0x7FFFFFFF;
/* 138:    */   }
/* 139:    */   
/* 140:    */   private int findInsertionIndex(int key)
/* 141:    */   {
/* 142:256 */     return findInsertionIndex(this.keys, this.states, key, this.mask);
/* 143:    */   }
/* 144:    */   
/* 145:    */   private static int findInsertionIndex(int[] keys, byte[] states, int key, int mask)
/* 146:    */   {
/* 147:269 */     int hash = hashOf(key);
/* 148:270 */     int index = hash & mask;
/* 149:271 */     if (states[index] == 0) {
/* 150:272 */       return index;
/* 151:    */     }
/* 152:273 */     if ((states[index] == 1) && (keys[index] == key)) {
/* 153:274 */       return changeIndexSign(index);
/* 154:    */     }
/* 155:277 */     int perturb = perturb(hash);
/* 156:278 */     int j = index;
/* 157:279 */     if (states[index] == 1) {
/* 158:    */       for (;;)
/* 159:    */       {
/* 160:281 */         j = probe(perturb, j);
/* 161:282 */         index = j & mask;
/* 162:283 */         perturb >>= 5;
/* 163:285 */         if (states[index] == 1) {
/* 164:285 */           if (keys[index] == key) {
/* 165:    */             break;
/* 166:    */           }
/* 167:    */         }
/* 168:    */       }
/* 169:    */     }
/* 170:291 */     if (states[index] == 0) {
/* 171:292 */       return index;
/* 172:    */     }
/* 173:293 */     if (states[index] == 1) {
/* 174:296 */       return changeIndexSign(index);
/* 175:    */     }
/* 176:299 */     int firstRemoved = index;
/* 177:    */     for (;;)
/* 178:    */     {
/* 179:301 */       j = probe(perturb, j);
/* 180:302 */       index = j & mask;
/* 181:304 */       if (states[index] == 0) {
/* 182:305 */         return firstRemoved;
/* 183:    */       }
/* 184:306 */       if ((states[index] == 1) && (keys[index] == key)) {
/* 185:307 */         return changeIndexSign(index);
/* 186:    */       }
/* 187:310 */       perturb >>= 5;
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   private static int probe(int perturb, int j)
/* 192:    */   {
/* 193:323 */     return (j << 2) + j + perturb + 1;
/* 194:    */   }
/* 195:    */   
/* 196:    */   private static int changeIndexSign(int index)
/* 197:    */   {
/* 198:332 */     return -index - 1;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public int size()
/* 202:    */   {
/* 203:340 */     return this.size;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public double remove(int key)
/* 207:    */   {
/* 208:351 */     int hash = hashOf(key);
/* 209:352 */     int index = hash & this.mask;
/* 210:353 */     if (containsKey(key, index)) {
/* 211:354 */       return doRemove(index);
/* 212:    */     }
/* 213:357 */     if (this.states[index] == 0) {
/* 214:358 */       return this.missingEntries;
/* 215:    */     }
/* 216:361 */     int j = index;
/* 217:362 */     for (int perturb = perturb(hash); this.states[index] != 0; perturb >>= 5)
/* 218:    */     {
/* 219:363 */       j = probe(perturb, j);
/* 220:364 */       index = j & this.mask;
/* 221:365 */       if (containsKey(key, index)) {
/* 222:366 */         return doRemove(index);
/* 223:    */       }
/* 224:    */     }
/* 225:370 */     return this.missingEntries;
/* 226:    */   }
/* 227:    */   
/* 228:    */   private boolean containsKey(int key, int index)
/* 229:    */   {
/* 230:382 */     return ((key != 0) || (this.states[index] == 1)) && (this.keys[index] == key);
/* 231:    */   }
/* 232:    */   
/* 233:    */   private double doRemove(int index)
/* 234:    */   {
/* 235:391 */     this.keys[index] = 0;
/* 236:392 */     this.states[index] = 2;
/* 237:393 */     double previous = this.values[index];
/* 238:394 */     this.values[index] = this.missingEntries;
/* 239:395 */     this.size -= 1;
/* 240:396 */     this.count += 1;
/* 241:397 */     return previous;
/* 242:    */   }
/* 243:    */   
/* 244:    */   public double put(int key, double value)
/* 245:    */   {
/* 246:407 */     int index = findInsertionIndex(key);
/* 247:408 */     double previous = this.missingEntries;
/* 248:409 */     boolean newMapping = true;
/* 249:410 */     if (index < 0)
/* 250:    */     {
/* 251:411 */       index = changeIndexSign(index);
/* 252:412 */       previous = this.values[index];
/* 253:413 */       newMapping = false;
/* 254:    */     }
/* 255:415 */     this.keys[index] = key;
/* 256:416 */     this.states[index] = 1;
/* 257:417 */     this.values[index] = value;
/* 258:418 */     if (newMapping)
/* 259:    */     {
/* 260:419 */       this.size += 1;
/* 261:420 */       if (shouldGrowTable()) {
/* 262:421 */         growTable();
/* 263:    */       }
/* 264:423 */       this.count += 1;
/* 265:    */     }
/* 266:425 */     return previous;
/* 267:    */   }
/* 268:    */   
/* 269:    */   private void growTable()
/* 270:    */   {
/* 271:434 */     int oldLength = this.states.length;
/* 272:435 */     int[] oldKeys = this.keys;
/* 273:436 */     double[] oldValues = this.values;
/* 274:437 */     byte[] oldStates = this.states;
/* 275:    */     
/* 276:439 */     int newLength = 2 * oldLength;
/* 277:440 */     int[] newKeys = new int[newLength];
/* 278:441 */     double[] newValues = new double[newLength];
/* 279:442 */     byte[] newStates = new byte[newLength];
/* 280:443 */     int newMask = newLength - 1;
/* 281:444 */     for (int i = 0; i < oldLength; i++) {
/* 282:445 */       if (oldStates[i] == 1)
/* 283:    */       {
/* 284:446 */         int key = oldKeys[i];
/* 285:447 */         int index = findInsertionIndex(newKeys, newStates, key, newMask);
/* 286:448 */         newKeys[index] = key;
/* 287:449 */         newValues[index] = oldValues[i];
/* 288:450 */         newStates[index] = 1;
/* 289:    */       }
/* 290:    */     }
/* 291:454 */     this.mask = newMask;
/* 292:455 */     this.keys = newKeys;
/* 293:456 */     this.values = newValues;
/* 294:457 */     this.states = newStates;
/* 295:    */   }
/* 296:    */   
/* 297:    */   private boolean shouldGrowTable()
/* 298:    */   {
/* 299:466 */     return this.size > (this.mask + 1) * 0.5F;
/* 300:    */   }
/* 301:    */   
/* 302:    */   private static int hashOf(int key)
/* 303:    */   {
/* 304:475 */     int h = key ^ key >>> 20 ^ key >>> 12;
/* 305:476 */     return h ^ h >>> 7 ^ h >>> 4;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public class Iterator
/* 309:    */   {
/* 310:    */     private final int referenceCount;
/* 311:    */     private int current;
/* 312:    */     private int next;
/* 313:    */     
/* 314:    */     private Iterator()
/* 315:    */     {
/* 316:498 */       this.referenceCount = OpenIntToDoubleHashMap.this.count;
/* 317:    */       
/* 318:    */ 
/* 319:501 */       this.next = -1;
/* 320:    */       try
/* 321:    */       {
/* 322:503 */         advance();
/* 323:    */       }
/* 324:    */       catch (NoSuchElementException localNoSuchElementException) {}
/* 325:    */       
/* 326:    */     }
/* 327:    */     
/* 328:    */     public boolean hasNext()
/* 329:    */     {
/* 330:515 */       return this.next >= 0;
/* 331:    */     }
/* 332:    */     
/* 333:    */     public int key()
/* 334:    */       throws ConcurrentModificationException, NoSuchElementException
/* 335:    */     {
/* 336:526 */       if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
/* 337:527 */         throw new ConcurrentModificationException();
/* 338:    */       }
/* 339:529 */       if (this.current < 0) {
/* 340:530 */         throw new NoSuchElementException();
/* 341:    */       }
/* 342:532 */       return OpenIntToDoubleHashMap.this.keys[this.current];
/* 343:    */     }
/* 344:    */     
/* 345:    */     public double value()
/* 346:    */       throws ConcurrentModificationException, NoSuchElementException
/* 347:    */     {
/* 348:543 */       if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
/* 349:544 */         throw new ConcurrentModificationException();
/* 350:    */       }
/* 351:546 */       if (this.current < 0) {
/* 352:547 */         throw new NoSuchElementException();
/* 353:    */       }
/* 354:549 */       return OpenIntToDoubleHashMap.this.values[this.current];
/* 355:    */     }
/* 356:    */     
/* 357:    */     public void advance()
/* 358:    */       throws ConcurrentModificationException, NoSuchElementException
/* 359:    */     {
/* 360:560 */       if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
/* 361:561 */         throw new ConcurrentModificationException();
/* 362:    */       }
/* 363:565 */       this.current = this.next;
/* 364:    */       try
/* 365:    */       {
/* 366:569 */         while (OpenIntToDoubleHashMap.this.states[(++this.next)] != 1) {}
/* 367:    */       }
/* 368:    */       catch (ArrayIndexOutOfBoundsException e)
/* 369:    */       {
/* 370:573 */         this.next = -2;
/* 371:574 */         if (this.current < 0) {
/* 372:575 */           throw new NoSuchElementException();
/* 373:    */         }
/* 374:    */       }
/* 375:    */     }
/* 376:    */   }
/* 377:    */   
/* 378:    */   private void readObject(ObjectInputStream stream)
/* 379:    */     throws IOException, ClassNotFoundException
/* 380:    */   {
/* 381:592 */     stream.defaultReadObject();
/* 382:593 */     this.count = 0;
/* 383:    */   }
/* 384:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.OpenIntToDoubleHashMap
 * JD-Core Version:    0.7.0.1
 */
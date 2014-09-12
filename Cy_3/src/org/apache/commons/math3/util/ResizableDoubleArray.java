/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ 
/*  10:    */ public class ResizableDoubleArray
/*  11:    */   implements DoubleArray, Serializable
/*  12:    */ {
/*  13:    */   public static final int ADDITIVE_MODE = 1;
/*  14:    */   public static final int MULTIPLICATIVE_MODE = 0;
/*  15:    */   private static final long serialVersionUID = -3485529955529426875L;
/*  16: 93 */   private float contractionCriteria = 2.5F;
/*  17:103 */   private float expansionFactor = 2.0F;
/*  18:109 */   private int expansionMode = 0;
/*  19:115 */   private int initialCapacity = 16;
/*  20:    */   private double[] internalArray;
/*  21:126 */   private int numElements = 0;
/*  22:134 */   private int startIndex = 0;
/*  23:    */   
/*  24:    */   public ResizableDoubleArray()
/*  25:    */   {
/*  26:146 */     this.internalArray = new double[this.initialCapacity];
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ResizableDoubleArray(int initialCapacity)
/*  30:    */   {
/*  31:161 */     setInitialCapacity(initialCapacity);
/*  32:162 */     this.internalArray = new double[this.initialCapacity];
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ResizableDoubleArray(double[] initialArray)
/*  36:    */   {
/*  37:183 */     if (initialArray == null)
/*  38:    */     {
/*  39:184 */       this.internalArray = new double[this.initialCapacity];
/*  40:    */     }
/*  41:    */     else
/*  42:    */     {
/*  43:186 */       this.internalArray = new double[initialArray.length];
/*  44:187 */       System.arraycopy(initialArray, 0, this.internalArray, 0, initialArray.length);
/*  45:188 */       this.initialCapacity = initialArray.length;
/*  46:189 */       this.numElements = initialArray.length;
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public ResizableDoubleArray(int initialCapacity, float expansionFactor)
/*  51:    */   {
/*  52:216 */     this.expansionFactor = expansionFactor;
/*  53:217 */     setInitialCapacity(initialCapacity);
/*  54:218 */     this.internalArray = new double[initialCapacity];
/*  55:219 */     setContractionCriteria(expansionFactor + 0.5F);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public ResizableDoubleArray(int initialCapacity, float expansionFactor, float contractionCriteria)
/*  59:    */   {
/*  60:243 */     this.expansionFactor = expansionFactor;
/*  61:244 */     setContractionCriteria(contractionCriteria);
/*  62:245 */     setInitialCapacity(initialCapacity);
/*  63:246 */     this.internalArray = new double[initialCapacity];
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ResizableDoubleArray(int initialCapacity, float expansionFactor, float contractionCriteria, int expansionMode)
/*  67:    */   {
/*  68:272 */     this.expansionFactor = expansionFactor;
/*  69:273 */     setContractionCriteria(contractionCriteria);
/*  70:274 */     setInitialCapacity(initialCapacity);
/*  71:275 */     setExpansionMode(expansionMode);
/*  72:276 */     this.internalArray = new double[initialCapacity];
/*  73:    */   }
/*  74:    */   
/*  75:    */   public ResizableDoubleArray(ResizableDoubleArray original)
/*  76:    */     throws NullArgumentException
/*  77:    */   {
/*  78:291 */     MathUtils.checkNotNull(original);
/*  79:292 */     copy(original, this);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public synchronized void addElement(double value)
/*  83:    */   {
/*  84:301 */     this.numElements += 1;
/*  85:302 */     if (this.startIndex + this.numElements > this.internalArray.length) {
/*  86:303 */       expand();
/*  87:    */     }
/*  88:305 */     this.internalArray[(this.startIndex + (this.numElements - 1))] = value;
/*  89:306 */     if (shouldContract()) {
/*  90:307 */       contract();
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public synchronized void addElements(double[] values)
/*  95:    */   {
/*  96:318 */     double[] tempArray = new double[this.numElements + values.length + 1];
/*  97:319 */     System.arraycopy(this.internalArray, this.startIndex, tempArray, 0, this.numElements);
/*  98:320 */     System.arraycopy(values, 0, tempArray, this.numElements, values.length);
/*  99:321 */     this.internalArray = tempArray;
/* 100:322 */     this.startIndex = 0;
/* 101:323 */     this.numElements += values.length;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public synchronized double addElementRolling(double value)
/* 105:    */   {
/* 106:343 */     double discarded = this.internalArray[this.startIndex];
/* 107:345 */     if (this.startIndex + (this.numElements + 1) > this.internalArray.length) {
/* 108:346 */       expand();
/* 109:    */     }
/* 110:349 */     this.startIndex += 1;
/* 111:    */     
/* 112:    */ 
/* 113:352 */     this.internalArray[(this.startIndex + (this.numElements - 1))] = value;
/* 114:355 */     if (shouldContract()) {
/* 115:356 */       contract();
/* 116:    */     }
/* 117:358 */     return discarded;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public synchronized double substituteMostRecentElement(double value)
/* 121:    */   {
/* 122:372 */     if (this.numElements < 1) {
/* 123:373 */       throw new MathIllegalStateException(LocalizedFormats.CANNOT_SUBSTITUTE_ELEMENT_FROM_EMPTY_ARRAY, new Object[0]);
/* 124:    */     }
/* 125:377 */     double discarded = this.internalArray[(this.startIndex + (this.numElements - 1))];
/* 126:    */     
/* 127:379 */     this.internalArray[(this.startIndex + (this.numElements - 1))] = value;
/* 128:    */     
/* 129:381 */     return discarded;
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected void checkContractExpand(float contraction, float expansion)
/* 133:    */   {
/* 134:397 */     if (contraction < expansion) {
/* 135:398 */       throw new MathIllegalArgumentException(LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_EXPANSION_FACTOR, new Object[] { Float.valueOf(contraction), Float.valueOf(expansion) });
/* 136:    */     }
/* 137:403 */     if (contraction <= 1.0D) {
/* 138:404 */       throw new MathIllegalArgumentException(LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_ONE, new Object[] { Float.valueOf(contraction) });
/* 139:    */     }
/* 140:409 */     if (expansion <= 1.0D) {
/* 141:410 */       throw new MathIllegalArgumentException(LocalizedFormats.EXPANSION_FACTOR_SMALLER_THAN_ONE, new Object[] { Float.valueOf(expansion) });
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   public synchronized void clear()
/* 146:    */   {
/* 147:421 */     this.numElements = 0;
/* 148:422 */     this.startIndex = 0;
/* 149:423 */     this.internalArray = new double[this.initialCapacity];
/* 150:    */   }
/* 151:    */   
/* 152:    */   public synchronized void contract()
/* 153:    */   {
/* 154:432 */     double[] tempArray = new double[this.numElements + 1];
/* 155:    */     
/* 156:    */ 
/* 157:435 */     System.arraycopy(this.internalArray, this.startIndex, tempArray, 0, this.numElements);
/* 158:436 */     this.internalArray = tempArray;
/* 159:    */     
/* 160:    */ 
/* 161:439 */     this.startIndex = 0;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public synchronized void discardFrontElements(int i)
/* 165:    */   {
/* 166:455 */     discardExtremeElements(i, true);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public synchronized void discardMostRecentElements(int i)
/* 170:    */   {
/* 171:472 */     discardExtremeElements(i, false);
/* 172:    */   }
/* 173:    */   
/* 174:    */   private synchronized void discardExtremeElements(int i, boolean front)
/* 175:    */   {
/* 176:496 */     if (i > this.numElements) {
/* 177:497 */       throw new MathIllegalArgumentException(LocalizedFormats.TOO_MANY_ELEMENTS_TO_DISCARD_FROM_ARRAY, new Object[] { Integer.valueOf(i), Integer.valueOf(this.numElements) });
/* 178:    */     }
/* 179:500 */     if (i < 0) {
/* 180:501 */       throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_DISCARD_NEGATIVE_NUMBER_OF_ELEMENTS, new Object[] { Integer.valueOf(i) });
/* 181:    */     }
/* 182:506 */     this.numElements -= i;
/* 183:507 */     if (front) {
/* 184:508 */       this.startIndex += i;
/* 185:    */     }
/* 186:511 */     if (shouldContract()) {
/* 187:512 */       contract();
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   protected synchronized void expand()
/* 192:    */   {
/* 193:532 */     int newSize = 0;
/* 194:533 */     if (this.expansionMode == 0) {
/* 195:534 */       newSize = (int)FastMath.ceil(this.internalArray.length * this.expansionFactor);
/* 196:    */     } else {
/* 197:536 */       newSize = this.internalArray.length + FastMath.round(this.expansionFactor);
/* 198:    */     }
/* 199:538 */     double[] tempArray = new double[newSize];
/* 200:    */     
/* 201:    */ 
/* 202:541 */     System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
/* 203:542 */     this.internalArray = tempArray;
/* 204:    */   }
/* 205:    */   
/* 206:    */   private synchronized void expandTo(int size)
/* 207:    */   {
/* 208:551 */     double[] tempArray = new double[size];
/* 209:    */     
/* 210:553 */     System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
/* 211:554 */     this.internalArray = tempArray;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public float getContractionCriteria()
/* 215:    */   {
/* 216:570 */     return this.contractionCriteria;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public synchronized double getElement(int index)
/* 220:    */   {
/* 221:582 */     if (index >= this.numElements) {
/* 222:583 */       throw new ArrayIndexOutOfBoundsException(index);
/* 223:    */     }
/* 224:584 */     if (index >= 0) {
/* 225:585 */       return this.internalArray[(this.startIndex + index)];
/* 226:    */     }
/* 227:587 */     throw new ArrayIndexOutOfBoundsException(index);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public synchronized double[] getElements()
/* 231:    */   {
/* 232:599 */     double[] elementArray = new double[this.numElements];
/* 233:600 */     System.arraycopy(this.internalArray, this.startIndex, elementArray, 0, this.numElements);
/* 234:    */     
/* 235:602 */     return elementArray;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public float getExpansionFactor()
/* 239:    */   {
/* 240:618 */     return this.expansionFactor;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public int getExpansionMode()
/* 244:    */   {
/* 245:629 */     return this.expansionMode;
/* 246:    */   }
/* 247:    */   
/* 248:    */   synchronized int getInternalLength()
/* 249:    */   {
/* 250:641 */     return this.internalArray.length;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public synchronized int getNumElements()
/* 254:    */   {
/* 255:651 */     return this.numElements;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public synchronized double[] getInternalValues()
/* 259:    */   {
/* 260:667 */     return this.internalArray;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public void setContractionCriteria(float contractionCriteria)
/* 264:    */   {
/* 265:676 */     checkContractExpand(contractionCriteria, getExpansionFactor());
/* 266:677 */     synchronized (this)
/* 267:    */     {
/* 268:678 */       this.contractionCriteria = contractionCriteria;
/* 269:    */     }
/* 270:    */   }
/* 271:    */   
/* 272:    */   public synchronized void setElement(int index, double value)
/* 273:    */   {
/* 274:696 */     if (index < 0) {
/* 275:697 */       throw new ArrayIndexOutOfBoundsException(index);
/* 276:    */     }
/* 277:699 */     if (index + 1 > this.numElements) {
/* 278:700 */       this.numElements = (index + 1);
/* 279:    */     }
/* 280:702 */     if (this.startIndex + index >= this.internalArray.length) {
/* 281:703 */       expandTo(this.startIndex + (index + 1));
/* 282:    */     }
/* 283:705 */     this.internalArray[(this.startIndex + index)] = value;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void setExpansionFactor(float expansionFactor)
/* 287:    */   {
/* 288:720 */     checkContractExpand(getContractionCriteria(), expansionFactor);
/* 289:722 */     synchronized (this)
/* 290:    */     {
/* 291:723 */       this.expansionFactor = expansionFactor;
/* 292:    */     }
/* 293:    */   }
/* 294:    */   
/* 295:    */   public void setExpansionMode(int expansionMode)
/* 296:    */   {
/* 297:735 */     if ((expansionMode != 0) && (expansionMode != 1)) {
/* 298:737 */       throw new MathIllegalArgumentException(LocalizedFormats.UNSUPPORTED_EXPANSION_MODE, new Object[] { Integer.valueOf(expansionMode), Integer.valueOf(0), "MULTIPLICATIVE_MODE", Integer.valueOf(1), "ADDITIVE_MODE" });
/* 299:    */     }
/* 300:742 */     synchronized (this)
/* 301:    */     {
/* 302:743 */       this.expansionMode = expansionMode;
/* 303:    */     }
/* 304:    */   }
/* 305:    */   
/* 306:    */   protected void setInitialCapacity(int initialCapacity)
/* 307:    */   {
/* 308:755 */     if (initialCapacity > 0) {
/* 309:756 */       synchronized (this)
/* 310:    */       {
/* 311:757 */         this.initialCapacity = initialCapacity;
/* 312:    */       }
/* 313:    */     } else {
/* 314:760 */       throw new MathIllegalArgumentException(LocalizedFormats.INITIAL_CAPACITY_NOT_POSITIVE, new Object[] { Integer.valueOf(initialCapacity) });
/* 315:    */     }
/* 316:    */   }
/* 317:    */   
/* 318:    */   public synchronized void setNumElements(int i)
/* 319:    */   {
/* 320:777 */     if (i < 0) {
/* 321:778 */       throw new MathIllegalArgumentException(LocalizedFormats.INDEX_NOT_POSITIVE, new Object[] { Integer.valueOf(i) });
/* 322:    */     }
/* 323:785 */     if (this.startIndex + i > this.internalArray.length) {
/* 324:786 */       expandTo(this.startIndex + i);
/* 325:    */     }
/* 326:790 */     this.numElements = i;
/* 327:    */   }
/* 328:    */   
/* 329:    */   private synchronized boolean shouldContract()
/* 330:    */   {
/* 331:800 */     if (this.expansionMode == 0) {
/* 332:801 */       return this.internalArray.length / this.numElements > this.contractionCriteria;
/* 333:    */     }
/* 334:803 */     return this.internalArray.length - this.numElements > this.contractionCriteria;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public synchronized int start()
/* 338:    */   {
/* 339:817 */     return this.startIndex;
/* 340:    */   }
/* 341:    */   
/* 342:    */   public static void copy(ResizableDoubleArray source, ResizableDoubleArray dest)
/* 343:    */     throws NullArgumentException
/* 344:    */   {
/* 345:839 */     MathUtils.checkNotNull(source);
/* 346:840 */     MathUtils.checkNotNull(dest);
/* 347:841 */     synchronized (source)
/* 348:    */     {
/* 349:842 */       synchronized (dest)
/* 350:    */       {
/* 351:843 */         dest.initialCapacity = source.initialCapacity;
/* 352:844 */         dest.contractionCriteria = source.contractionCriteria;
/* 353:845 */         dest.expansionFactor = source.expansionFactor;
/* 354:846 */         dest.expansionMode = source.expansionMode;
/* 355:847 */         dest.internalArray = new double[source.internalArray.length];
/* 356:848 */         System.arraycopy(source.internalArray, 0, dest.internalArray, 0, dest.internalArray.length);
/* 357:    */         
/* 358:850 */         dest.numElements = source.numElements;
/* 359:851 */         dest.startIndex = source.startIndex;
/* 360:    */       }
/* 361:    */     }
/* 362:    */   }
/* 363:    */   
/* 364:    */   public synchronized ResizableDoubleArray copy()
/* 365:    */   {
/* 366:865 */     ResizableDoubleArray result = new ResizableDoubleArray();
/* 367:866 */     copy(this, result);
/* 368:867 */     return result;
/* 369:    */   }
/* 370:    */   
/* 371:    */   public boolean equals(Object object)
/* 372:    */   {
/* 373:881 */     if (object == this) {
/* 374:882 */       return true;
/* 375:    */     }
/* 376:884 */     if (!(object instanceof ResizableDoubleArray)) {
/* 377:885 */       return false;
/* 378:    */     }
/* 379:887 */     synchronized (this)
/* 380:    */     {
/* 381:888 */       synchronized (object)
/* 382:    */       {
/* 383:889 */         boolean result = true;
/* 384:890 */         ResizableDoubleArray other = (ResizableDoubleArray)object;
/* 385:891 */         result = (result) && (other.initialCapacity == this.initialCapacity);
/* 386:892 */         result = (result) && (other.contractionCriteria == this.contractionCriteria);
/* 387:893 */         result = (result) && (other.expansionFactor == this.expansionFactor);
/* 388:894 */         result = (result) && (other.expansionMode == this.expansionMode);
/* 389:895 */         result = (result) && (other.numElements == this.numElements);
/* 390:896 */         result = (result) && (other.startIndex == this.startIndex);
/* 391:897 */         if (!result) {
/* 392:898 */           return false;
/* 393:    */         }
/* 394:900 */         return Arrays.equals(this.internalArray, other.internalArray);
/* 395:    */       }
/* 396:    */     }
/* 397:    */   }
/* 398:    */   
/* 399:    */   public synchronized int hashCode()
/* 400:    */   {
/* 401:914 */     int[] hashData = new int[7];
/* 402:915 */     hashData[0] = new Float(this.expansionFactor).hashCode();
/* 403:916 */     hashData[1] = new Float(this.contractionCriteria).hashCode();
/* 404:917 */     hashData[2] = this.expansionMode;
/* 405:918 */     hashData[3] = Arrays.hashCode(this.internalArray);
/* 406:919 */     hashData[4] = this.initialCapacity;
/* 407:920 */     hashData[5] = this.numElements;
/* 408:921 */     hashData[6] = this.startIndex;
/* 409:922 */     return Arrays.hashCode(hashData);
/* 410:    */   }
/* 411:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.ResizableDoubleArray
 * JD-Core Version:    0.7.0.1
 */
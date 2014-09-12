/*   1:    */ package org.apache.commons.math3.dfp;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.Field;
/*   4:    */ import org.apache.commons.math3.FieldElement;
/*   5:    */ 
/*   6:    */ public class DfpField
/*   7:    */   implements Field<Dfp>
/*   8:    */ {
/*   9:    */   public static final int FLAG_INVALID = 1;
/*  10:    */   public static final int FLAG_DIV_ZERO = 2;
/*  11:    */   public static final int FLAG_OVERFLOW = 4;
/*  12:    */   public static final int FLAG_UNDERFLOW = 8;
/*  13:    */   public static final int FLAG_INEXACT = 16;
/*  14:    */   private static String sqr2String;
/*  15:    */   private static String sqr2ReciprocalString;
/*  16:    */   private static String sqr3String;
/*  17:    */   private static String sqr3ReciprocalString;
/*  18:    */   private static String piString;
/*  19:    */   private static String eString;
/*  20:    */   private static String ln2String;
/*  21:    */   private static String ln5String;
/*  22:    */   private static String ln10String;
/*  23:    */   private final int radixDigits;
/*  24:    */   private final Dfp zero;
/*  25:    */   private final Dfp one;
/*  26:    */   private final Dfp two;
/*  27:    */   private final Dfp sqr2;
/*  28:    */   private final Dfp[] sqr2Split;
/*  29:    */   private final Dfp sqr2Reciprocal;
/*  30:    */   private final Dfp sqr3;
/*  31:    */   private final Dfp sqr3Reciprocal;
/*  32:    */   private final Dfp pi;
/*  33:    */   private final Dfp[] piSplit;
/*  34:    */   private final Dfp e;
/*  35:    */   private final Dfp[] eSplit;
/*  36:    */   private final Dfp ln2;
/*  37:    */   private final Dfp[] ln2Split;
/*  38:    */   private final Dfp ln5;
/*  39:    */   private final Dfp[] ln5Split;
/*  40:    */   private final Dfp ln10;
/*  41:    */   private RoundingMode rMode;
/*  42:    */   private int ieeeFlags;
/*  43:    */   
/*  44:    */   public static enum RoundingMode
/*  45:    */   {
/*  46: 33 */     ROUND_DOWN,  ROUND_UP,  ROUND_HALF_UP,  ROUND_HALF_DOWN,  ROUND_HALF_EVEN,  ROUND_HALF_ODD,  ROUND_CEIL,  ROUND_FLOOR;
/*  47:    */     
/*  48:    */     private RoundingMode() {}
/*  49:    */   }
/*  50:    */   
/*  51:    */   public DfpField(int decimalDigits)
/*  52:    */   {
/*  53:177 */     this(decimalDigits, true);
/*  54:    */   }
/*  55:    */   
/*  56:    */   private DfpField(int decimalDigits, boolean computeConstants)
/*  57:    */   {
/*  58:193 */     this.radixDigits = (decimalDigits < 13 ? 4 : (decimalDigits + 3) / 4);
/*  59:194 */     this.rMode = RoundingMode.ROUND_HALF_EVEN;
/*  60:195 */     this.ieeeFlags = 0;
/*  61:196 */     this.zero = new Dfp(this, 0);
/*  62:197 */     this.one = new Dfp(this, 1);
/*  63:198 */     this.two = new Dfp(this, 2);
/*  64:200 */     if (computeConstants)
/*  65:    */     {
/*  66:202 */       synchronized (DfpField.class)
/*  67:    */       {
/*  68:208 */         computeStringConstants(decimalDigits < 67 ? 200 : 3 * decimalDigits);
/*  69:    */         
/*  70:    */ 
/*  71:211 */         this.sqr2 = new Dfp(this, sqr2String);
/*  72:212 */         this.sqr2Split = split(sqr2String);
/*  73:213 */         this.sqr2Reciprocal = new Dfp(this, sqr2ReciprocalString);
/*  74:214 */         this.sqr3 = new Dfp(this, sqr3String);
/*  75:215 */         this.sqr3Reciprocal = new Dfp(this, sqr3ReciprocalString);
/*  76:216 */         this.pi = new Dfp(this, piString);
/*  77:217 */         this.piSplit = split(piString);
/*  78:218 */         this.e = new Dfp(this, eString);
/*  79:219 */         this.eSplit = split(eString);
/*  80:220 */         this.ln2 = new Dfp(this, ln2String);
/*  81:221 */         this.ln2Split = split(ln2String);
/*  82:222 */         this.ln5 = new Dfp(this, ln5String);
/*  83:223 */         this.ln5Split = split(ln5String);
/*  84:224 */         this.ln10 = new Dfp(this, ln10String);
/*  85:    */       }
/*  86:    */     }
/*  87:    */     else
/*  88:    */     {
/*  89:229 */       this.sqr2 = null;
/*  90:230 */       this.sqr2Split = null;
/*  91:231 */       this.sqr2Reciprocal = null;
/*  92:232 */       this.sqr3 = null;
/*  93:233 */       this.sqr3Reciprocal = null;
/*  94:234 */       this.pi = null;
/*  95:235 */       this.piSplit = null;
/*  96:236 */       this.e = null;
/*  97:237 */       this.eSplit = null;
/*  98:238 */       this.ln2 = null;
/*  99:239 */       this.ln2Split = null;
/* 100:240 */       this.ln5 = null;
/* 101:241 */       this.ln5Split = null;
/* 102:242 */       this.ln10 = null;
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public int getRadixDigits()
/* 107:    */   {
/* 108:251 */     return this.radixDigits;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setRoundingMode(RoundingMode mode)
/* 112:    */   {
/* 113:262 */     this.rMode = mode;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public RoundingMode getRoundingMode()
/* 117:    */   {
/* 118:269 */     return this.rMode;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public int getIEEEFlags()
/* 122:    */   {
/* 123:284 */     return this.ieeeFlags;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void clearIEEEFlags()
/* 127:    */   {
/* 128:298 */     this.ieeeFlags = 0;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setIEEEFlags(int flags)
/* 132:    */   {
/* 133:313 */     this.ieeeFlags = (flags & 0x1F);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setIEEEFlagsBits(int bits)
/* 137:    */   {
/* 138:331 */     this.ieeeFlags |= bits & 0x1F;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public Dfp newDfp()
/* 142:    */   {
/* 143:338 */     return new Dfp(this);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Dfp newDfp(byte x)
/* 147:    */   {
/* 148:346 */     return new Dfp(this, x);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public Dfp newDfp(int x)
/* 152:    */   {
/* 153:354 */     return new Dfp(this, x);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public Dfp newDfp(long x)
/* 157:    */   {
/* 158:362 */     return new Dfp(this, x);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public Dfp newDfp(double x)
/* 162:    */   {
/* 163:370 */     return new Dfp(this, x);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public Dfp newDfp(Dfp d)
/* 167:    */   {
/* 168:378 */     return new Dfp(d);
/* 169:    */   }
/* 170:    */   
/* 171:    */   public Dfp newDfp(String s)
/* 172:    */   {
/* 173:386 */     return new Dfp(this, s);
/* 174:    */   }
/* 175:    */   
/* 176:    */   public Dfp newDfp(byte sign, byte nans)
/* 177:    */   {
/* 178:396 */     return new Dfp(this, sign, nans);
/* 179:    */   }
/* 180:    */   
/* 181:    */   public Dfp getZero()
/* 182:    */   {
/* 183:403 */     return this.zero;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public Dfp getOne()
/* 187:    */   {
/* 188:410 */     return this.one;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public Class<? extends FieldElement<Dfp>> getRuntimeClass()
/* 192:    */   {
/* 193:415 */     return Dfp.class;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public Dfp getTwo()
/* 197:    */   {
/* 198:422 */     return this.two;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public Dfp getSqr2()
/* 202:    */   {
/* 203:429 */     return this.sqr2;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public Dfp[] getSqr2Split()
/* 207:    */   {
/* 208:436 */     return (Dfp[])this.sqr2Split.clone();
/* 209:    */   }
/* 210:    */   
/* 211:    */   public Dfp getSqr2Reciprocal()
/* 212:    */   {
/* 213:443 */     return this.sqr2Reciprocal;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public Dfp getSqr3()
/* 217:    */   {
/* 218:450 */     return this.sqr3;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public Dfp getSqr3Reciprocal()
/* 222:    */   {
/* 223:457 */     return this.sqr3Reciprocal;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public Dfp getPi()
/* 227:    */   {
/* 228:464 */     return this.pi;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public Dfp[] getPiSplit()
/* 232:    */   {
/* 233:471 */     return (Dfp[])this.piSplit.clone();
/* 234:    */   }
/* 235:    */   
/* 236:    */   public Dfp getE()
/* 237:    */   {
/* 238:478 */     return this.e;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public Dfp[] getESplit()
/* 242:    */   {
/* 243:485 */     return (Dfp[])this.eSplit.clone();
/* 244:    */   }
/* 245:    */   
/* 246:    */   public Dfp getLn2()
/* 247:    */   {
/* 248:492 */     return this.ln2;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public Dfp[] getLn2Split()
/* 252:    */   {
/* 253:499 */     return (Dfp[])this.ln2Split.clone();
/* 254:    */   }
/* 255:    */   
/* 256:    */   public Dfp getLn5()
/* 257:    */   {
/* 258:506 */     return this.ln5;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public Dfp[] getLn5Split()
/* 262:    */   {
/* 263:513 */     return (Dfp[])this.ln5Split.clone();
/* 264:    */   }
/* 265:    */   
/* 266:    */   public Dfp getLn10()
/* 267:    */   {
/* 268:520 */     return this.ln10;
/* 269:    */   }
/* 270:    */   
/* 271:    */   private Dfp[] split(String a)
/* 272:    */   {
/* 273:530 */     Dfp[] result = new Dfp[2];
/* 274:531 */     boolean leading = true;
/* 275:532 */     int sp = 0;
/* 276:533 */     int sig = 0;
/* 277:    */     
/* 278:535 */     char[] buf = new char[a.length()];
/* 279:537 */     for (int i = 0; i < buf.length; i++)
/* 280:    */     {
/* 281:538 */       buf[i] = a.charAt(i);
/* 282:540 */       if ((buf[i] >= '1') && (buf[i] <= '9')) {
/* 283:541 */         leading = false;
/* 284:    */       }
/* 285:544 */       if (buf[i] == '.')
/* 286:    */       {
/* 287:545 */         sig += (400 - sig) % 4;
/* 288:546 */         leading = false;
/* 289:    */       }
/* 290:549 */       if (sig == this.radixDigits / 2 * 4)
/* 291:    */       {
/* 292:550 */         sp = i;
/* 293:551 */         break;
/* 294:    */       }
/* 295:554 */       if ((buf[i] >= '0') && (buf[i] <= '9') && (!leading)) {
/* 296:555 */         sig++;
/* 297:    */       }
/* 298:    */     }
/* 299:559 */     result[0] = new Dfp(this, new String(buf, 0, sp));
/* 300:561 */     for (int i = 0; i < buf.length; i++)
/* 301:    */     {
/* 302:562 */       buf[i] = a.charAt(i);
/* 303:563 */       if ((buf[i] >= '0') && (buf[i] <= '9') && (i < sp)) {
/* 304:564 */         buf[i] = '0';
/* 305:    */       }
/* 306:    */     }
/* 307:568 */     result[1] = new Dfp(this, new String(buf));
/* 308:    */     
/* 309:570 */     return result;
/* 310:    */   }
/* 311:    */   
/* 312:    */   private static void computeStringConstants(int highPrecisionDecimalDigits)
/* 313:    */   {
/* 314:578 */     if ((sqr2String == null) || (sqr2String.length() < highPrecisionDecimalDigits - 3))
/* 315:    */     {
/* 316:581 */       DfpField highPrecisionField = new DfpField(highPrecisionDecimalDigits, false);
/* 317:582 */       Dfp highPrecisionOne = new Dfp(highPrecisionField, 1);
/* 318:583 */       Dfp highPrecisionTwo = new Dfp(highPrecisionField, 2);
/* 319:584 */       Dfp highPrecisionThree = new Dfp(highPrecisionField, 3);
/* 320:    */       
/* 321:586 */       Dfp highPrecisionSqr2 = highPrecisionTwo.sqrt();
/* 322:587 */       sqr2String = highPrecisionSqr2.toString();
/* 323:588 */       sqr2ReciprocalString = highPrecisionOne.divide(highPrecisionSqr2).toString();
/* 324:    */       
/* 325:590 */       Dfp highPrecisionSqr3 = highPrecisionThree.sqrt();
/* 326:591 */       sqr3String = highPrecisionSqr3.toString();
/* 327:592 */       sqr3ReciprocalString = highPrecisionOne.divide(highPrecisionSqr3).toString();
/* 328:    */       
/* 329:594 */       piString = computePi(highPrecisionOne, highPrecisionTwo, highPrecisionThree).toString();
/* 330:595 */       eString = computeExp(highPrecisionOne, highPrecisionOne).toString();
/* 331:596 */       ln2String = computeLn(highPrecisionTwo, highPrecisionOne, highPrecisionTwo).toString();
/* 332:597 */       ln5String = computeLn(new Dfp(highPrecisionField, 5), highPrecisionOne, highPrecisionTwo).toString();
/* 333:598 */       ln10String = computeLn(new Dfp(highPrecisionField, 10), highPrecisionOne, highPrecisionTwo).toString();
/* 334:    */     }
/* 335:    */   }
/* 336:    */   
/* 337:    */   private static Dfp computePi(Dfp one, Dfp two, Dfp three)
/* 338:    */   {
/* 339:611 */     Dfp sqrt2 = two.sqrt();
/* 340:612 */     Dfp yk = sqrt2.subtract(one);
/* 341:613 */     Dfp four = two.add(two);
/* 342:614 */     Dfp two2kp3 = two;
/* 343:615 */     Dfp ak = two.multiply(three.subtract(two.multiply(sqrt2)));
/* 344:623 */     for (int i = 1; i < 20; i++)
/* 345:    */     {
/* 346:624 */       Dfp ykM1 = yk;
/* 347:    */       
/* 348:626 */       Dfp y2 = yk.multiply(yk);
/* 349:627 */       Dfp oneMinusY4 = one.subtract(y2.multiply(y2));
/* 350:628 */       Dfp s = oneMinusY4.sqrt().sqrt();
/* 351:629 */       yk = one.subtract(s).divide(one.add(s));
/* 352:    */       
/* 353:631 */       two2kp3 = two2kp3.multiply(four);
/* 354:    */       
/* 355:633 */       Dfp p = one.add(yk);
/* 356:634 */       Dfp p2 = p.multiply(p);
/* 357:635 */       ak = ak.multiply(p2.multiply(p2)).subtract(two2kp3.multiply(yk).multiply(one.add(yk).add(yk.multiply(yk))));
/* 358:637 */       if (yk.equals(ykM1)) {
/* 359:    */         break;
/* 360:    */       }
/* 361:    */     }
/* 362:642 */     return one.divide(ak);
/* 363:    */   }
/* 364:    */   
/* 365:    */   public static Dfp computeExp(Dfp a, Dfp one)
/* 366:    */   {
/* 367:653 */     Dfp y = new Dfp(one);
/* 368:654 */     Dfp py = new Dfp(one);
/* 369:655 */     Dfp f = new Dfp(one);
/* 370:656 */     Dfp fi = new Dfp(one);
/* 371:657 */     Dfp x = new Dfp(one);
/* 372:659 */     for (int i = 0; i < 10000; i++)
/* 373:    */     {
/* 374:660 */       x = x.multiply(a);
/* 375:661 */       y = y.add(x.divide(f));
/* 376:662 */       fi = fi.add(one);
/* 377:663 */       f = f.multiply(fi);
/* 378:664 */       if (y.equals(py)) {
/* 379:    */         break;
/* 380:    */       }
/* 381:667 */       py = new Dfp(y);
/* 382:    */     }
/* 383:670 */     return y;
/* 384:    */   }
/* 385:    */   
/* 386:    */   public static Dfp computeLn(Dfp a, Dfp one, Dfp two)
/* 387:    */   {
/* 388:736 */     int den = 1;
/* 389:737 */     Dfp x = a.add(new Dfp(a.getField(), -1)).divide(a.add(one));
/* 390:    */     
/* 391:739 */     Dfp y = new Dfp(x);
/* 392:740 */     Dfp num = new Dfp(x);
/* 393:741 */     Dfp py = new Dfp(y);
/* 394:742 */     for (int i = 0; i < 10000; i++)
/* 395:    */     {
/* 396:743 */       num = num.multiply(x);
/* 397:744 */       num = num.multiply(x);
/* 398:745 */       den += 2;
/* 399:746 */       Dfp t = num.divide(den);
/* 400:747 */       y = y.add(t);
/* 401:748 */       if (y.equals(py)) {
/* 402:    */         break;
/* 403:    */       }
/* 404:751 */       py = new Dfp(y);
/* 405:    */     }
/* 406:754 */     return y.multiply(two);
/* 407:    */   }
/* 408:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.dfp.DfpField
 * JD-Core Version:    0.7.0.1
 */
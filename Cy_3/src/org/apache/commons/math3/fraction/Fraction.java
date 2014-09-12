/*   1:    */ package org.apache.commons.math3.fraction;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.math.BigInteger;
/*   5:    */ import org.apache.commons.math3.FieldElement;
/*   6:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.ArithmeticUtils;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*  11:    */ 
/*  12:    */ public class Fraction
/*  13:    */   extends Number
/*  14:    */   implements FieldElement<Fraction>, Comparable<Fraction>, Serializable
/*  15:    */ {
/*  16: 42 */   public static final Fraction TWO = new Fraction(2, 1);
/*  17: 45 */   public static final Fraction ONE = new Fraction(1, 1);
/*  18: 48 */   public static final Fraction ZERO = new Fraction(0, 1);
/*  19: 51 */   public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
/*  20: 54 */   public static final Fraction ONE_FIFTH = new Fraction(1, 5);
/*  21: 57 */   public static final Fraction ONE_HALF = new Fraction(1, 2);
/*  22: 60 */   public static final Fraction ONE_QUARTER = new Fraction(1, 4);
/*  23: 63 */   public static final Fraction ONE_THIRD = new Fraction(1, 3);
/*  24: 66 */   public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
/*  25: 69 */   public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
/*  26: 72 */   public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
/*  27: 75 */   public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
/*  28: 78 */   public static final Fraction TWO_THIRDS = new Fraction(2, 3);
/*  29: 81 */   public static final Fraction MINUS_ONE = new Fraction(-1, 1);
/*  30:    */   private static final long serialVersionUID = 3698073679419233275L;
/*  31:    */   private final int denominator;
/*  32:    */   private final int numerator;
/*  33:    */   
/*  34:    */   public Fraction(double value)
/*  35:    */     throws FractionConversionException
/*  36:    */   {
/*  37: 99 */     this(value, 1.E-005D, 100);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Fraction(double value, double epsilon, int maxIterations)
/*  41:    */     throws FractionConversionException
/*  42:    */   {
/*  43:121 */     this(value, epsilon, 2147483647, maxIterations);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public Fraction(double value, int maxDenominator)
/*  47:    */     throws FractionConversionException
/*  48:    */   {
/*  49:141 */     this(value, 0.0D, maxDenominator, 100);
/*  50:    */   }
/*  51:    */   
/*  52:    */   private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
/*  53:    */     throws FractionConversionException
/*  54:    */   {
/*  55:178 */     long overflow = 2147483647L;
/*  56:179 */     double r0 = value;
/*  57:180 */     long a0 = (long) FastMath.floor(r0);
/*  58:181 */     if (a0 > overflow) {
/*  59:182 */       throw new FractionConversionException(value, a0, 1L);
/*  60:    */     }
/*  61:187 */     if (FastMath.abs(a0 - value) < epsilon)
/*  62:    */     {
/*  63:188 */       this.numerator = ((int)a0);
/*  64:189 */       this.denominator = 1;
/*  65:190 */       return;
/*  66:    */     }
/*  67:193 */     long p0 = 1L;
/*  68:194 */     long q0 = 0L;
/*  69:195 */     long p1 = a0;
/*  70:196 */     long q1 = 1L;
/*  71:    */     
/*  72:198 */     long p2 = 0L;
/*  73:199 */     long q2 = 1L;
/*  74:    */     
/*  75:201 */     int n = 0;
/*  76:202 */     boolean stop = false;
/*  77:    */     do
/*  78:    */     {
/*  79:204 */       n++;
/*  80:205 */       double r1 = 1.0D / (r0 - a0);
/*  81:206 */       long a1 = (long) FastMath.floor(r1);
/*  82:207 */       p2 = a1 * p1 + p0;
/*  83:208 */       q2 = a1 * q1 + q0;
/*  84:209 */       if ((p2 > overflow) || (q2 > overflow)) {
/*  85:210 */         throw new FractionConversionException(value, p2, q2);
/*  86:    */       }
/*  87:213 */       double convergent = p2 / q2;
/*  88:214 */       if ((n < maxIterations) && (FastMath.abs(convergent - value) > epsilon) && (q2 < maxDenominator))
/*  89:    */       {
/*  90:215 */         p0 = p1;
/*  91:216 */         p1 = p2;
/*  92:217 */         q0 = q1;
/*  93:218 */         q1 = q2;
/*  94:219 */         a0 = a1;
/*  95:220 */         r0 = r1;
/*  96:    */       }
/*  97:    */       else
/*  98:    */       {
/*  99:222 */         stop = true;
/* 100:    */       }
/* 101:224 */     } while (!stop);
/* 102:226 */     if (n >= maxIterations) {
/* 103:227 */       throw new FractionConversionException(value, maxIterations);
/* 104:    */     }
/* 105:230 */     if (q2 < maxDenominator)
/* 106:    */     {
/* 107:231 */       this.numerator = ((int)p2);
/* 108:232 */       this.denominator = ((int)q2);
/* 109:    */     }
/* 110:    */     else
/* 111:    */     {
/* 112:234 */       this.numerator = ((int)p1);
/* 113:235 */       this.denominator = ((int)q1);
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Fraction(int num)
/* 118:    */   {
/* 119:246 */     this(num, 1);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public Fraction(int num, int den)
/* 123:    */   {
/* 124:257 */     if (den == 0) {
/* 125:258 */       throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, new Object[] { Integer.valueOf(num), Integer.valueOf(den) });
/* 126:    */     }
/* 127:261 */     if (den < 0)
/* 128:    */     {
/* 129:262 */       if ((num == -2147483648) || (den == -2147483648)) {
/* 130:264 */         throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, new Object[] { Integer.valueOf(num), Integer.valueOf(den) });
/* 131:    */       }
/* 132:267 */       num = -num;
/* 133:268 */       den = -den;
/* 134:    */     }
/* 135:271 */     int d = ArithmeticUtils.gcd(num, den);
/* 136:272 */     if (d > 1)
/* 137:    */     {
/* 138:273 */       num /= d;
/* 139:274 */       den /= d;
/* 140:    */     }
/* 141:278 */     if (den < 0)
/* 142:    */     {
/* 143:279 */       num = -num;
/* 144:280 */       den = -den;
/* 145:    */     }
/* 146:282 */     this.numerator = num;
/* 147:283 */     this.denominator = den;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public Fraction abs()
/* 151:    */   {
/* 152:    */     Fraction ret;
/* 153:    */    
/* 154:292 */     if (this.numerator >= 0) {
/* 155:293 */       ret = this;
/* 156:    */     } else {
/* 157:295 */       ret = negate();
/* 158:    */     }
/* 159:297 */     return ret;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int compareTo(Fraction object)
/* 163:    */   {
/* 164:307 */     long nOd = this.numerator * object.denominator;
/* 165:308 */     long dOn = this.denominator * object.numerator;
/* 166:309 */     return nOd > dOn ? 1 : nOd < dOn ? -1 : 0;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public double doubleValue()
/* 170:    */   {
/* 171:319 */     return this.numerator / this.denominator;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean equals(Object other)
/* 175:    */   {
/* 176:333 */     if (this == other) {
/* 177:334 */       return true;
/* 178:    */     }
/* 179:336 */     if ((other instanceof Fraction))
/* 180:    */     {
/* 181:339 */       Fraction rhs = (Fraction)other;
/* 182:340 */       return (this.numerator == rhs.numerator) && (this.denominator == rhs.denominator);
/* 183:    */     }
/* 184:343 */     return false;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public float floatValue()
/* 188:    */   {
/* 189:353 */     return (float)doubleValue();
/* 190:    */   }
/* 191:    */   
/* 192:    */   public int getDenominator()
/* 193:    */   {
/* 194:361 */     return this.denominator;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public int getNumerator()
/* 198:    */   {
/* 199:369 */     return this.numerator;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public int hashCode()
/* 203:    */   {
/* 204:378 */     return 37 * (629 + this.numerator) + this.denominator;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public int intValue()
/* 208:    */   {
/* 209:388 */     return (int)doubleValue();
/* 210:    */   }
/* 211:    */   
/* 212:    */   public long longValue()
/* 213:    */   {
/* 214:398 */     return (long) doubleValue();
/* 215:    */   }
/* 216:    */   
/* 217:    */   public Fraction negate()
/* 218:    */   {
/* 219:406 */     if (this.numerator == -2147483648) {
/* 220:407 */       throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, new Object[] { Integer.valueOf(this.numerator), Integer.valueOf(this.denominator) });
/* 221:    */     }
/* 222:409 */     return new Fraction(-this.numerator, this.denominator);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public Fraction reciprocal()
/* 226:    */   {
/* 227:417 */     return new Fraction(this.denominator, this.numerator);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public Fraction add(Fraction fraction)
/* 231:    */   {
/* 232:431 */     return addSub(fraction, true);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public Fraction add(int i)
/* 236:    */   {
/* 237:440 */     return new Fraction(this.numerator + i * this.denominator, this.denominator);
/* 238:    */   }
/* 239:    */   
/* 240:    */   public Fraction subtract(Fraction fraction)
/* 241:    */   {
/* 242:454 */     return addSub(fraction, false);
/* 243:    */   }
/* 244:    */   
/* 245:    */   public Fraction subtract(int i)
/* 246:    */   {
/* 247:463 */     return new Fraction(this.numerator - i * this.denominator, this.denominator);
/* 248:    */   }
/* 249:    */   
/* 250:    */   private Fraction addSub(Fraction fraction, boolean isAdd)
/* 251:    */   {
/* 252:477 */     if (fraction == null) {
/* 253:478 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/* 254:    */     }
/* 255:481 */     if (this.numerator == 0) {
/* 256:482 */       return isAdd ? fraction : fraction.negate();
/* 257:    */     }
/* 258:484 */     if (fraction.numerator == 0) {
/* 259:485 */       return this;
/* 260:    */     }
/* 261:489 */     int d1 = ArithmeticUtils.gcd(this.denominator, fraction.denominator);
/* 262:490 */     if (d1 == 1)
/* 263:    */     {
/* 264:492 */       int uvp = ArithmeticUtils.mulAndCheck(this.numerator, fraction.denominator);
/* 265:493 */       int upv = ArithmeticUtils.mulAndCheck(fraction.numerator, this.denominator);
/* 266:494 */       return new Fraction(isAdd ? ArithmeticUtils.addAndCheck(uvp, upv) : ArithmeticUtils.subAndCheck(uvp, upv), ArithmeticUtils.mulAndCheck(this.denominator, fraction.denominator));
/* 267:    */     }
/* 268:502 */     BigInteger uvp = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf(fraction.denominator / d1));
/* 269:    */     
/* 270:504 */     BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf(this.denominator / d1));
/* 271:    */     
/* 272:506 */     BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
/* 273:    */     
/* 274:    */ 
/* 275:509 */     int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
/* 276:510 */     int d2 = tmodd1 == 0 ? d1 : ArithmeticUtils.gcd(tmodd1, d1);
/* 277:    */     
/* 278:    */ 
/* 279:513 */     BigInteger w = t.divide(BigInteger.valueOf(d2));
/* 280:514 */     if (w.bitLength() > 31) {
/* 281:515 */       throw new MathArithmeticException(LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY, new Object[] { w });
/* 282:    */     }
/* 283:518 */     return new Fraction(w.intValue(), ArithmeticUtils.mulAndCheck(this.denominator / d1, fraction.denominator / d2));
/* 284:    */   }
/* 285:    */   
/* 286:    */   public Fraction multiply(Fraction fraction)
/* 287:    */   {
/* 288:534 */     if (fraction == null) {
/* 289:535 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/* 290:    */     }
/* 291:537 */     if ((this.numerator == 0) || (fraction.numerator == 0)) {
/* 292:538 */       return ZERO;
/* 293:    */     }
/* 294:542 */     int d1 = ArithmeticUtils.gcd(this.numerator, fraction.denominator);
/* 295:543 */     int d2 = ArithmeticUtils.gcd(fraction.numerator, this.denominator);
/* 296:544 */     return getReducedFraction(ArithmeticUtils.mulAndCheck(this.numerator / d1, fraction.numerator / d2), ArithmeticUtils.mulAndCheck(this.denominator / d2, fraction.denominator / d1));
/* 297:    */   }
/* 298:    */   
/* 299:    */   public Fraction multiply(int i)
/* 300:    */   {
/* 301:555 */     return new Fraction(this.numerator * i, this.denominator);
/* 302:    */   }
/* 303:    */   
/* 304:    */   public Fraction divide(Fraction fraction)
/* 305:    */   {
/* 306:569 */     if (fraction == null) {
/* 307:570 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/* 308:    */     }
/* 309:572 */     if (fraction.numerator == 0) {
/* 310:573 */       throw new MathArithmeticException(LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY, new Object[] { Integer.valueOf(fraction.numerator), Integer.valueOf(fraction.denominator) });
/* 311:    */     }
/* 312:576 */     return multiply(fraction.reciprocal());
/* 313:    */   }
/* 314:    */   
/* 315:    */   public Fraction divide(int i)
/* 316:    */   {
/* 317:585 */     return new Fraction(this.numerator, this.denominator * i);
/* 318:    */   }
/* 319:    */   
/* 320:    */   public double percentageValue()
/* 321:    */   {
/* 322:597 */     return multiply(100).doubleValue();
/* 323:    */   }
/* 324:    */   
/* 325:    */   public static Fraction getReducedFraction(int numerator, int denominator)
/* 326:    */   {
/* 327:612 */     if (denominator == 0) {
/* 328:613 */       throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, new Object[] { Integer.valueOf(numerator), Integer.valueOf(denominator) });
/* 329:    */     }
/* 330:616 */     if (numerator == 0) {
/* 331:617 */       return ZERO;
/* 332:    */     }
/* 333:620 */     if ((denominator == -2147483648) && ((numerator & 0x1) == 0))
/* 334:    */     {
/* 335:621 */       numerator /= 2;denominator /= 2;
/* 336:    */     }
/* 337:623 */     if (denominator < 0)
/* 338:    */     {
/* 339:624 */       if ((numerator == -2147483648) || (denominator == -2147483648)) {
/* 340:626 */         throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, new Object[] { Integer.valueOf(numerator), Integer.valueOf(denominator) });
/* 341:    */       }
/* 342:629 */       numerator = -numerator;
/* 343:630 */       denominator = -denominator;
/* 344:    */     }
/* 345:633 */     int gcd = ArithmeticUtils.gcd(numerator, denominator);
/* 346:634 */     numerator /= gcd;
/* 347:635 */     denominator /= gcd;
/* 348:636 */     return new Fraction(numerator, denominator);
/* 349:    */   }
/* 350:    */   
/* 351:    */   public String toString()
/* 352:    */   {
/* 353:650 */     String str = null;
/* 354:651 */     if (this.denominator == 1) {
/* 355:652 */       str = Integer.toString(this.numerator);
/* 356:653 */     } else if (this.numerator == 0) {
/* 357:654 */       str = "0";
/* 358:    */     } else {
/* 359:656 */       str = this.numerator + " / " + this.denominator;
/* 360:    */     }
/* 361:658 */     return str;
/* 362:    */   }
/* 363:    */   
/* 364:    */   public FractionField getField()
/* 365:    */   {
/* 366:663 */     return FractionField.getInstance();
/* 367:    */   }
/* 368:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.Fraction
 * JD-Core Version:    0.7.0.1
 */
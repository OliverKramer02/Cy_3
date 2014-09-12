/*    1:     */ package org.apache.commons.math3.fraction;
/*    2:     */ 
/*    3:     */ import java.io.Serializable;
/*    4:     */ import java.math.BigDecimal;
/*    5:     */ import java.math.BigInteger;
/*    6:     */ import org.apache.commons.math3.FieldElement;
/*    7:     */ import org.apache.commons.math3.exception.MathArithmeticException;
/*    8:     */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*    9:     */ import org.apache.commons.math3.exception.NullArgumentException;
/*   10:     */ import org.apache.commons.math3.exception.ZeroException;
/*   11:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   12:     */ import org.apache.commons.math3.util.ArithmeticUtils;
/*   13:     */ import org.apache.commons.math3.util.FastMath;
/*   14:     */ import org.apache.commons.math3.util.MathUtils;
/*   15:     */ 
/*   16:     */ public class BigFraction
/*   17:     */   extends Number
/*   18:     */   implements FieldElement<BigFraction>, Comparable<BigFraction>, Serializable
/*   19:     */ {
/*   20:  45 */   public static final BigFraction TWO = new BigFraction(2);
/*   21:  48 */   public static final BigFraction ONE = new BigFraction(1);
/*   22:  51 */   public static final BigFraction ZERO = new BigFraction(0);
/*   23:  54 */   public static final BigFraction MINUS_ONE = new BigFraction(-1);
/*   24:  57 */   public static final BigFraction FOUR_FIFTHS = new BigFraction(4, 5);
/*   25:  60 */   public static final BigFraction ONE_FIFTH = new BigFraction(1, 5);
/*   26:  63 */   public static final BigFraction ONE_HALF = new BigFraction(1, 2);
/*   27:  66 */   public static final BigFraction ONE_QUARTER = new BigFraction(1, 4);
/*   28:  69 */   public static final BigFraction ONE_THIRD = new BigFraction(1, 3);
/*   29:  72 */   public static final BigFraction THREE_FIFTHS = new BigFraction(3, 5);
/*   30:  75 */   public static final BigFraction THREE_QUARTERS = new BigFraction(3, 4);
/*   31:  78 */   public static final BigFraction TWO_FIFTHS = new BigFraction(2, 5);
/*   32:  81 */   public static final BigFraction TWO_QUARTERS = new BigFraction(2, 4);
/*   33:  84 */   public static final BigFraction TWO_THIRDS = new BigFraction(2, 3);
/*   34:     */   private static final long serialVersionUID = -5630213147331578515L;
/*   35:  90 */   private static final BigInteger ONE_HUNDRED = BigInteger.valueOf(100L);
/*   36:     */   private final BigInteger numerator;
/*   37:     */   private final BigInteger denominator;
/*   38:     */   
/*   39:     */   public BigFraction(BigInteger num)
/*   40:     */   {
/*   41: 108 */     this(num, BigInteger.ONE);
/*   42:     */   }
/*   43:     */   
/*   44:     */   public BigFraction(BigInteger num, BigInteger den)
/*   45:     */   {
/*   46: 121 */     MathUtils.checkNotNull(num, LocalizedFormats.NUMERATOR, new Object[0]);
/*   47: 122 */     MathUtils.checkNotNull(den, LocalizedFormats.DENOMINATOR, new Object[0]);
/*   48: 123 */     if (BigInteger.ZERO.equals(den)) {
/*   49: 124 */       throw new ZeroException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
/*   50:     */     }
/*   51: 126 */     if (BigInteger.ZERO.equals(num))
/*   52:     */     {
/*   53: 127 */       this.numerator = BigInteger.ZERO;
/*   54: 128 */       this.denominator = BigInteger.ONE;
/*   55:     */     }
/*   56:     */     else
/*   57:     */     {
/*   58: 132 */       BigInteger gcd = num.gcd(den);
/*   59: 133 */       if (BigInteger.ONE.compareTo(gcd) < 0)
/*   60:     */       {
/*   61: 134 */         num = num.divide(gcd);
/*   62: 135 */         den = den.divide(gcd);
/*   63:     */       }
/*   64: 139 */       if (BigInteger.ZERO.compareTo(den) > 0)
/*   65:     */       {
/*   66: 140 */         num = num.negate();
/*   67: 141 */         den = den.negate();
/*   68:     */       }
/*   69: 145 */       this.numerator = num;
/*   70: 146 */       this.denominator = den;
/*   71:     */     }
/*   72:     */   }
/*   73:     */   
/*   74:     */   public BigFraction(double value)
/*   75:     */     throws MathIllegalArgumentException
/*   76:     */   {
/*   77: 173 */     if (Double.isNaN(value)) {
/*   78: 174 */       throw new MathIllegalArgumentException(LocalizedFormats.NAN_VALUE_CONVERSION, new Object[0]);
/*   79:     */     }
/*   80: 176 */     if (Double.isInfinite(value)) {
/*   81: 177 */       throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_VALUE_CONVERSION, new Object[0]);
/*   82:     */     }
/*   83: 181 */     long bits = Double.doubleToLongBits(value);
/*   84: 182 */     long sign = bits & 0x0;
/*   85: 183 */     long exponent = bits & 0x0;
/*   86: 184 */     long m = bits & 0xFFFFFFFF;
/*   87: 185 */     if (exponent != 0L) {
/*   88: 187 */       m |= 0x0;
/*   89:     */     }
/*   90: 189 */     if (sign != 0L) {
/*   91: 190 */       m = -m;
/*   92:     */     }
/*   93: 192 */     int k = (int)(exponent >> 52) - 1075;
/*   94: 193 */     while (((m & 0xFFFFFFFE) != 0L) && ((m & 1L) == 0L))
/*   95:     */     {
/*   96: 194 */       m >>= 1;
/*   97: 195 */       k++;
/*   98:     */     }
/*   99: 198 */     if (k < 0)
/*  100:     */     {
/*  101: 199 */       this.numerator = BigInteger.valueOf(m);
/*  102: 200 */       this.denominator = BigInteger.ZERO.flipBit(-k);
/*  103:     */     }
/*  104:     */     else
/*  105:     */     {
/*  106: 202 */       this.numerator = BigInteger.valueOf(m).multiply(BigInteger.ZERO.flipBit(k));
/*  107: 203 */       this.denominator = BigInteger.ONE;
/*  108:     */     }
/*  109:     */   }
/*  110:     */   
/*  111:     */   public BigFraction(double value, double epsilon, int maxIterations)
/*  112:     */     throws FractionConversionException
/*  113:     */   {
/*  114: 232 */     this(value, epsilon, 2147483647, maxIterations);
/*  115:     */   }
/*  116:     */   
/*  117:     */   private BigFraction(double value, double epsilon, int maxDenominator, int maxIterations)
/*  118:     */     throws FractionConversionException
/*  119:     */   {
/*  120: 272 */     long overflow = 2147483647L;
/*  121: 273 */     double r0 = value;
/*  122: 274 */     long a0 = (long) FastMath.floor(r0);
/*  123: 275 */     if (a0 > overflow) {
/*  124: 276 */       throw new FractionConversionException(value, a0, 1L);
/*  125:     */     }
/*  126: 281 */     if (FastMath.abs(a0 - value) < epsilon)
/*  127:     */     {
/*  128: 282 */       this.numerator = BigInteger.valueOf(a0);
/*  129: 283 */       this.denominator = BigInteger.ONE;
/*  130: 284 */       return;
/*  131:     */     }
/*  132: 287 */     long p0 = 1L;
/*  133: 288 */     long q0 = 0L;
/*  134: 289 */     long p1 = a0;
/*  135: 290 */     long q1 = 1L;
/*  136:     */     
/*  137: 292 */     long p2 = 0L;
/*  138: 293 */     long q2 = 1L;
/*  139:     */     
/*  140: 295 */     int n = 0;
/*  141: 296 */     boolean stop = false;
/*  142:     */     do
/*  143:     */     {
/*  144: 298 */       n++;
/*  145: 299 */       double r1 = 1.0D / (r0 - a0);
/*  146: 300 */       long a1 = (long) FastMath.floor(r1);
/*  147: 301 */       p2 = a1 * p1 + p0;
/*  148: 302 */       q2 = a1 * q1 + q0;
/*  149: 303 */       if ((p2 > overflow) || (q2 > overflow)) {
/*  150: 304 */         throw new FractionConversionException(value, p2, q2);
/*  151:     */       }
/*  152: 307 */       double convergent = p2 / q2;
/*  153: 308 */       if ((n < maxIterations) && (FastMath.abs(convergent - value) > epsilon) && (q2 < maxDenominator))
/*  154:     */       {
/*  155: 311 */         p0 = p1;
/*  156: 312 */         p1 = p2;
/*  157: 313 */         q0 = q1;
/*  158: 314 */         q1 = q2;
/*  159: 315 */         a0 = a1;
/*  160: 316 */         r0 = r1;
/*  161:     */       }
/*  162:     */       else
/*  163:     */       {
/*  164: 318 */         stop = true;
/*  165:     */       }
/*  166: 320 */     } while (!stop);
/*  167: 322 */     if (n >= maxIterations) {
/*  168: 323 */       throw new FractionConversionException(value, maxIterations);
/*  169:     */     }
/*  170: 326 */     if (q2 < maxDenominator)
/*  171:     */     {
/*  172: 327 */       this.numerator = BigInteger.valueOf(p2);
/*  173: 328 */       this.denominator = BigInteger.valueOf(q2);
/*  174:     */     }
/*  175:     */     else
/*  176:     */     {
/*  177: 330 */       this.numerator = BigInteger.valueOf(p1);
/*  178: 331 */       this.denominator = BigInteger.valueOf(q1);
/*  179:     */     }
/*  180:     */   }
/*  181:     */   
/*  182:     */   public BigFraction(double value, int maxDenominator)
/*  183:     */     throws FractionConversionException
/*  184:     */   {
/*  185: 354 */     this(value, 0.0D, maxDenominator, 100);
/*  186:     */   }
/*  187:     */   
/*  188:     */   public BigFraction(int num)
/*  189:     */   {
/*  190: 367 */     this(BigInteger.valueOf(num), BigInteger.ONE);
/*  191:     */   }
/*  192:     */   
/*  193:     */   public BigFraction(int num, int den)
/*  194:     */   {
/*  195: 382 */     this(BigInteger.valueOf(num), BigInteger.valueOf(den));
/*  196:     */   }
/*  197:     */   
/*  198:     */   public BigFraction(long num)
/*  199:     */   {
/*  200: 394 */     this(BigInteger.valueOf(num), BigInteger.ONE);
/*  201:     */   }
/*  202:     */   
/*  203:     */   public BigFraction(long num, long den)
/*  204:     */   {
/*  205: 409 */     this(BigInteger.valueOf(num), BigInteger.valueOf(den));
/*  206:     */   }
/*  207:     */   
/*  208:     */   public static BigFraction getReducedFraction(int numerator, int denominator)
/*  209:     */   {
/*  210: 433 */     if (numerator == 0) {
/*  211: 434 */       return ZERO;
/*  212:     */     }
/*  213: 437 */     return new BigFraction(numerator, denominator);
/*  214:     */   }
/*  215:     */   
/*  216:     */   public BigFraction abs()
/*  217:     */   {
/*  218: 448 */     return BigInteger.ZERO.compareTo(this.numerator) <= 0 ? this : negate();
/*  219:     */   }
/*  220:     */   
/*  221:     */   public BigFraction add(BigInteger bg)
/*  222:     */     throws NullArgumentException
/*  223:     */   {
/*  224: 464 */     MathUtils.checkNotNull(bg);
/*  225: 465 */     return new BigFraction(this.numerator.add(this.denominator.multiply(bg)), this.denominator);
/*  226:     */   }
/*  227:     */   
/*  228:     */   public BigFraction add(int i)
/*  229:     */   {
/*  230: 479 */     return add(BigInteger.valueOf(i));
/*  231:     */   }
/*  232:     */   
/*  233:     */   public BigFraction add(long l)
/*  234:     */   {
/*  235: 493 */     return add(BigInteger.valueOf(l));
/*  236:     */   }
/*  237:     */   
/*  238:     */   public BigFraction add(BigFraction fraction)
/*  239:     */   {
/*  240: 508 */     if (fraction == null) {
/*  241: 509 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/*  242:     */     }
/*  243: 511 */     if (ZERO.equals(fraction)) {
/*  244: 512 */       return this;
/*  245:     */     }
/*  246: 515 */     BigInteger num = null;
/*  247: 516 */     BigInteger den = null;
/*  248: 518 */     if (this.denominator.equals(fraction.denominator))
/*  249:     */     {
/*  250: 519 */       num = this.numerator.add(fraction.numerator);
/*  251: 520 */       den = this.denominator;
/*  252:     */     }
/*  253:     */     else
/*  254:     */     {
/*  255: 522 */       num = this.numerator.multiply(fraction.denominator).add(fraction.numerator.multiply(this.denominator));
/*  256: 523 */       den = this.denominator.multiply(fraction.denominator);
/*  257:     */     }
/*  258: 525 */     return new BigFraction(num, den);
/*  259:     */   }
/*  260:     */   
/*  261:     */   public BigDecimal bigDecimalValue()
/*  262:     */   {
/*  263: 542 */     return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator));
/*  264:     */   }
/*  265:     */   
/*  266:     */   public BigDecimal bigDecimalValue(int roundingMode)
/*  267:     */   {
/*  268: 561 */     return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), roundingMode);
/*  269:     */   }
/*  270:     */   
/*  271:     */   public BigDecimal bigDecimalValue(int scale, int roundingMode)
/*  272:     */   {
/*  273: 580 */     return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), scale, roundingMode);
/*  274:     */   }
/*  275:     */   
/*  276:     */   public int compareTo(BigFraction object)
/*  277:     */   {
/*  278: 595 */     BigInteger nOd = this.numerator.multiply(object.denominator);
/*  279: 596 */     BigInteger dOn = this.denominator.multiply(object.numerator);
/*  280: 597 */     return nOd.compareTo(dOn);
/*  281:     */   }
/*  282:     */   
/*  283:     */   public BigFraction divide(BigInteger bg)
/*  284:     */   {
/*  285: 612 */     if (bg == null) {
/*  286: 613 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/*  287:     */     }
/*  288: 615 */     if (BigInteger.ZERO.equals(bg)) {
/*  289: 616 */       throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
/*  290:     */     }
/*  291: 618 */     return new BigFraction(this.numerator, this.denominator.multiply(bg));
/*  292:     */   }
/*  293:     */   
/*  294:     */   public BigFraction divide(int i)
/*  295:     */   {
/*  296: 632 */     return divide(BigInteger.valueOf(i));
/*  297:     */   }
/*  298:     */   
/*  299:     */   public BigFraction divide(long l)
/*  300:     */   {
/*  301: 646 */     return divide(BigInteger.valueOf(l));
/*  302:     */   }
/*  303:     */   
/*  304:     */   public BigFraction divide(BigFraction fraction)
/*  305:     */   {
/*  306: 661 */     if (fraction == null) {
/*  307: 662 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/*  308:     */     }
/*  309: 664 */     if (BigInteger.ZERO.equals(fraction.numerator)) {
/*  310: 665 */       throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
/*  311:     */     }
/*  312: 668 */     return multiply(fraction.reciprocal());
/*  313:     */   }
/*  314:     */   
/*  315:     */   public double doubleValue()
/*  316:     */   {
/*  317: 682 */     double result = this.numerator.doubleValue() / this.denominator.doubleValue();
/*  318: 683 */     if (Double.isNaN(result))
/*  319:     */     {
/*  320: 686 */       int shift = Math.max(this.numerator.bitLength(), this.denominator.bitLength()) - FastMath.getExponent(1.7976931348623157E+308D);
/*  321:     */       
/*  322: 688 */       result = this.numerator.shiftRight(shift).doubleValue() / this.denominator.shiftRight(shift).doubleValue();
/*  323:     */     }
/*  324: 691 */     return result;
/*  325:     */   }
/*  326:     */   
/*  327:     */   public boolean equals(Object other)
/*  328:     */   {
/*  329: 711 */     boolean ret = false;
/*  330: 713 */     if (this == other)
/*  331:     */     {
/*  332: 714 */       ret = true;
/*  333:     */     }
/*  334: 715 */     else if ((other instanceof BigFraction))
/*  335:     */     {
/*  336: 716 */       BigFraction rhs = ((BigFraction)other).reduce();
/*  337: 717 */       BigFraction thisOne = reduce();
/*  338: 718 */       ret = (thisOne.numerator.equals(rhs.numerator)) && (thisOne.denominator.equals(rhs.denominator));
/*  339:     */     }
/*  340: 721 */     return ret;
/*  341:     */   }
/*  342:     */   
/*  343:     */   public float floatValue()
/*  344:     */   {
/*  345: 735 */     float result = this.numerator.floatValue() / this.denominator.floatValue();
/*  346: 736 */     if (Double.isNaN(result))
/*  347:     */     {
/*  348: 739 */       int shift = Math.max(this.numerator.bitLength(), this.denominator.bitLength()) - FastMath.getExponent(3.4028235E+38F);
/*  349:     */       
/*  350: 741 */       result = this.numerator.shiftRight(shift).floatValue() / this.denominator.shiftRight(shift).floatValue();
/*  351:     */     }
/*  352: 744 */     return result;
/*  353:     */   }
/*  354:     */   
/*  355:     */   public BigInteger getDenominator()
/*  356:     */   {
/*  357: 755 */     return this.denominator;
/*  358:     */   }
/*  359:     */   
/*  360:     */   public int getDenominatorAsInt()
/*  361:     */   {
/*  362: 766 */     return this.denominator.intValue();
/*  363:     */   }
/*  364:     */   
/*  365:     */   public long getDenominatorAsLong()
/*  366:     */   {
/*  367: 777 */     return this.denominator.longValue();
/*  368:     */   }
/*  369:     */   
/*  370:     */   public BigInteger getNumerator()
/*  371:     */   {
/*  372: 788 */     return this.numerator;
/*  373:     */   }
/*  374:     */   
/*  375:     */   public int getNumeratorAsInt()
/*  376:     */   {
/*  377: 799 */     return this.numerator.intValue();
/*  378:     */   }
/*  379:     */   
/*  380:     */   public long getNumeratorAsLong()
/*  381:     */   {
/*  382: 810 */     return this.numerator.longValue();
/*  383:     */   }
/*  384:     */   
/*  385:     */   public int hashCode()
/*  386:     */   {
/*  387: 823 */     return 37 * (629 + this.numerator.hashCode()) + this.denominator.hashCode();
/*  388:     */   }
/*  389:     */   
/*  390:     */   public int intValue()
/*  391:     */   {
/*  392: 837 */     return this.numerator.divide(this.denominator).intValue();
/*  393:     */   }
/*  394:     */   
/*  395:     */   public long longValue()
/*  396:     */   {
/*  397: 851 */     return this.numerator.divide(this.denominator).longValue();
/*  398:     */   }
/*  399:     */   
/*  400:     */   public BigFraction multiply(BigInteger bg)
/*  401:     */   {
/*  402: 865 */     if (bg == null) {
/*  403: 866 */       throw new NullArgumentException();
/*  404:     */     }
/*  405: 868 */     return new BigFraction(bg.multiply(this.numerator), this.denominator);
/*  406:     */   }
/*  407:     */   
/*  408:     */   public BigFraction multiply(int i)
/*  409:     */   {
/*  410: 882 */     return multiply(BigInteger.valueOf(i));
/*  411:     */   }
/*  412:     */   
/*  413:     */   public BigFraction multiply(long l)
/*  414:     */   {
/*  415: 896 */     return multiply(BigInteger.valueOf(l));
/*  416:     */   }
/*  417:     */   
/*  418:     */   public BigFraction multiply(BigFraction fraction)
/*  419:     */   {
/*  420: 910 */     if (fraction == null) {
/*  421: 911 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/*  422:     */     }
/*  423: 913 */     if ((this.numerator.equals(BigInteger.ZERO)) || (fraction.numerator.equals(BigInteger.ZERO))) {
/*  424: 915 */       return ZERO;
/*  425:     */     }
/*  426: 917 */     return new BigFraction(this.numerator.multiply(fraction.numerator), this.denominator.multiply(fraction.denominator));
/*  427:     */   }
/*  428:     */   
/*  429:     */   public BigFraction negate()
/*  430:     */   {
/*  431: 930 */     return new BigFraction(this.numerator.negate(), this.denominator);
/*  432:     */   }
/*  433:     */   
/*  434:     */   public double percentageValue()
/*  435:     */   {
/*  436: 942 */     return multiply(ONE_HUNDRED).doubleValue();
/*  437:     */   }
/*  438:     */   
/*  439:     */   public BigFraction pow(int exponent)
/*  440:     */   {
/*  441: 957 */     if (exponent < 0) {
/*  442: 958 */       return new BigFraction(this.denominator.pow(-exponent), this.numerator.pow(-exponent));
/*  443:     */     }
/*  444: 960 */     return new BigFraction(this.numerator.pow(exponent), this.denominator.pow(exponent));
/*  445:     */   }
/*  446:     */   
/*  447:     */   public BigFraction pow(long exponent)
/*  448:     */   {
/*  449: 974 */     if (exponent < 0L) {
/*  450: 975 */       return new BigFraction(ArithmeticUtils.pow(this.denominator, -exponent), ArithmeticUtils.pow(this.numerator, -exponent));
/*  451:     */     }
/*  452: 978 */     return new BigFraction(ArithmeticUtils.pow(this.numerator, exponent), ArithmeticUtils.pow(this.denominator, exponent));
/*  453:     */   }
/*  454:     */   
/*  455:     */   public BigFraction pow(BigInteger exponent)
/*  456:     */   {
/*  457: 993 */     if (exponent.compareTo(BigInteger.ZERO) < 0)
/*  458:     */     {
/*  459: 994 */       BigInteger eNeg = exponent.negate();
/*  460: 995 */       return new BigFraction(ArithmeticUtils.pow(this.denominator, eNeg), ArithmeticUtils.pow(this.numerator, eNeg));
/*  461:     */     }
/*  462: 998 */     return new BigFraction(ArithmeticUtils.pow(this.numerator, exponent), ArithmeticUtils.pow(this.denominator, exponent));
/*  463:     */   }
/*  464:     */   
/*  465:     */   public double pow(double exponent)
/*  466:     */   {
/*  467:1013 */     return FastMath.pow(this.numerator.doubleValue(), exponent) / FastMath.pow(this.denominator.doubleValue(), exponent);
/*  468:     */   }
/*  469:     */   
/*  470:     */   public BigFraction reciprocal()
/*  471:     */   {
/*  472:1025 */     return new BigFraction(this.denominator, this.numerator);
/*  473:     */   }
/*  474:     */   
/*  475:     */   public BigFraction reduce()
/*  476:     */   {
/*  477:1037 */     BigInteger gcd = this.numerator.gcd(this.denominator);
/*  478:1038 */     return new BigFraction(this.numerator.divide(gcd), this.denominator.divide(gcd));
/*  479:     */   }
/*  480:     */   
/*  481:     */   public BigFraction subtract(BigInteger bg)
/*  482:     */   {
/*  483:1052 */     if (bg == null) {
/*  484:1053 */       throw new NullArgumentException();
/*  485:     */     }
/*  486:1055 */     return new BigFraction(this.numerator.subtract(this.denominator.multiply(bg)), this.denominator);
/*  487:     */   }
/*  488:     */   
/*  489:     */   public BigFraction subtract(int i)
/*  490:     */   {
/*  491:1068 */     return subtract(BigInteger.valueOf(i));
/*  492:     */   }
/*  493:     */   
/*  494:     */   public BigFraction subtract(long l)
/*  495:     */   {
/*  496:1081 */     return subtract(BigInteger.valueOf(l));
/*  497:     */   }
/*  498:     */   
/*  499:     */   public BigFraction subtract(BigFraction fraction)
/*  500:     */   {
/*  501:1095 */     if (fraction == null) {
/*  502:1096 */       throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
/*  503:     */     }
/*  504:1098 */     if (ZERO.equals(fraction)) {
/*  505:1099 */       return this;
/*  506:     */     }
/*  507:1102 */     BigInteger num = null;
/*  508:1103 */     BigInteger den = null;
/*  509:1104 */     if (this.denominator.equals(fraction.denominator))
/*  510:     */     {
/*  511:1105 */       num = this.numerator.subtract(fraction.numerator);
/*  512:1106 */       den = this.denominator;
/*  513:     */     }
/*  514:     */     else
/*  515:     */     {
/*  516:1108 */       num = this.numerator.multiply(fraction.denominator).subtract(fraction.numerator.multiply(this.denominator));
/*  517:1109 */       den = this.denominator.multiply(fraction.denominator);
/*  518:     */     }
/*  519:1111 */     return new BigFraction(num, den);
/*  520:     */   }
/*  521:     */   
/*  522:     */   public String toString()
/*  523:     */   {
/*  524:1126 */     String str = null;
/*  525:1127 */     if (BigInteger.ONE.equals(this.denominator)) {
/*  526:1128 */       str = this.numerator.toString();
/*  527:1129 */     } else if (BigInteger.ZERO.equals(this.numerator)) {
/*  528:1130 */       str = "0";
/*  529:     */     } else {
/*  530:1132 */       str = this.numerator + " / " + this.denominator;
/*  531:     */     }
/*  532:1134 */     return str;
/*  533:     */   }
/*  534:     */   
/*  535:     */   public BigFractionField getField()
/*  536:     */   {
/*  537:1139 */     return BigFractionField.getInstance();
/*  538:     */   }
/*  539:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.BigFraction
 * JD-Core Version:    0.7.0.1
 */
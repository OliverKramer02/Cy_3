/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   5:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   7:    */ import org.apache.commons.math3.exception.util.Localizable;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ 
/*  10:    */ public final class ArithmeticUtils
/*  11:    */ {
/*  12: 35 */   static final long[] FACTORIALS = { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
/*  13:    */   
/*  14:    */   public static int addAndCheck(int x, int y)
/*  15:    */   {
/*  16: 60 */     long s = x + y;
/*  17: 61 */     if ((s < -2147483648L) || (s > 2147483647L)) {
/*  18: 62 */       throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, new Object[] { Integer.valueOf(x), Integer.valueOf(y) });
/*  19:    */     }
/*  20: 64 */     return (int)s;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static long addAndCheck(long a, long b)
/*  24:    */   {
/*  25: 78 */     return addAndCheck(a, b, LocalizedFormats.OVERFLOW_IN_ADDITION);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static long binomialCoefficient(int n, int k)
/*  29:    */   {
/*  30:108 */     checkBinomial(n, k);
/*  31:109 */     if ((n == k) || (k == 0)) {
/*  32:110 */       return 1L;
/*  33:    */     }
/*  34:112 */     if ((k == 1) || (k == n - 1)) {
/*  35:113 */       return n;
/*  36:    */     }
/*  37:116 */     if (k > n / 2) {
/*  38:117 */       return binomialCoefficient(n, n - k);
/*  39:    */     }
/*  40:125 */     long result = 1L;
/*  41:126 */     if (n <= 61)
/*  42:    */     {
/*  43:128 */       int i = n - k + 1;
/*  44:129 */       for (int j = 1; j <= k; j++)
/*  45:    */       {
/*  46:130 */         result = result * i / j;
/*  47:131 */         i++;
/*  48:    */       }
/*  49:    */     }
/*  50:133 */     else if (n <= 66)
/*  51:    */     {
/*  52:136 */       int i = n - k + 1;
/*  53:137 */       for (int j = 1; j <= k; j++)
/*  54:    */       {
/*  55:144 */         long d = gcd(i, j);
/*  56:145 */         result = result / (j / d) * (i / d);
/*  57:146 */         i++;
/*  58:    */       }
/*  59:    */     }
/*  60:    */     else
/*  61:    */     {
/*  62:152 */       int i = n - k + 1;
/*  63:153 */       for (int j = 1; j <= k; j++)
/*  64:    */       {
/*  65:154 */         long d = gcd(i, j);
/*  66:155 */         result = mulAndCheck(result / (j / d), i / d);
/*  67:156 */         i++;
/*  68:    */       }
/*  69:    */     }
/*  70:159 */     return result;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static double binomialCoefficientDouble(int n, int k)
/*  74:    */   {
/*  75:186 */     checkBinomial(n, k);
/*  76:187 */     if ((n == k) || (k == 0)) {
/*  77:188 */       return 1.0D;
/*  78:    */     }
/*  79:190 */     if ((k == 1) || (k == n - 1)) {
/*  80:191 */       return n;
/*  81:    */     }
/*  82:193 */     if (k > n / 2) {
/*  83:194 */       return binomialCoefficientDouble(n, n - k);
/*  84:    */     }
/*  85:196 */     if (n < 67) {
/*  86:197 */       return binomialCoefficient(n, k);
/*  87:    */     }
/*  88:200 */     double result = 1.0D;
/*  89:201 */     for (int i = 1; i <= k; i++) {
/*  90:202 */       result *= (n - k + i) / i;
/*  91:    */     }
/*  92:205 */     return FastMath.floor(result + 0.5D);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static double binomialCoefficientLog(int n, int k)
/*  96:    */   {
/*  97:228 */     checkBinomial(n, k);
/*  98:229 */     if ((n == k) || (k == 0)) {
/*  99:230 */       return 0.0D;
/* 100:    */     }
/* 101:232 */     if ((k == 1) || (k == n - 1)) {
/* 102:233 */       return FastMath.log(n);
/* 103:    */     }
/* 104:240 */     if (n < 67) {
/* 105:241 */       return FastMath.log(binomialCoefficient(n, k));
/* 106:    */     }
/* 107:248 */     if (n < 1030) {
/* 108:249 */       return FastMath.log(binomialCoefficientDouble(n, k));
/* 109:    */     }
/* 110:252 */     if (k > n / 2) {
/* 111:253 */       return binomialCoefficientLog(n, n - k);
/* 112:    */     }
/* 113:259 */     double logSum = 0.0D;
/* 114:262 */     for (int i = n - k + 1; i <= n; i++) {
/* 115:263 */       logSum += FastMath.log(i);
/* 116:    */     }
/* 117:267 */     for (int i = 2; i <= k; i++) {
/* 118:268 */       logSum -= FastMath.log(i);
/* 119:    */     }
/* 120:271 */     return logSum;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static long factorial(int n)
/* 124:    */   {
/* 125:299 */     if (n < 0) {
/* 126:300 */       throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
/* 127:    */     }
/* 128:303 */     if (n > 20) {
/* 129:304 */       throw new MathArithmeticException();
/* 130:    */     }
/* 131:306 */     return FACTORIALS[n];
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static double factorialDouble(int n)
/* 135:    */   {
/* 136:323 */     if (n < 0) {
/* 137:324 */       throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
/* 138:    */     }
/* 139:327 */     if (n < 21) {
/* 140:328 */       return factorial(n);
/* 141:    */     }
/* 142:330 */     return FastMath.floor(FastMath.exp(factorialLog(n)) + 0.5D);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static double factorialLog(int n)
/* 146:    */   {
/* 147:341 */     if (n < 0) {
/* 148:342 */       throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
/* 149:    */     }
/* 150:345 */     if (n < 21) {
/* 151:346 */       return FastMath.log(factorial(n));
/* 152:    */     }
/* 153:348 */     double logSum = 0.0D;
/* 154:349 */     for (int i = 2; i <= n; i++) {
/* 155:350 */       logSum += FastMath.log(i);
/* 156:    */     }
/* 157:352 */     return logSum;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public static int gcd(int p, int q)
/* 161:    */   {
/* 162:385 */     int u = p;
/* 163:386 */     int v = q;
/* 164:387 */     if ((u == 0) || (v == 0))
/* 165:    */     {
/* 166:388 */       if ((u == -2147483648) || (v == -2147483648)) {
/* 167:389 */         throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, new Object[] { Integer.valueOf(p), Integer.valueOf(q) });
/* 168:    */       }
/* 169:392 */       return FastMath.abs(u) + FastMath.abs(v);
/* 170:    */     }
/* 171:399 */     if (u > 0) {
/* 172:400 */       u = -u;
/* 173:    */     }
/* 174:402 */     if (v > 0) {
/* 175:403 */       v = -v;
/* 176:    */     }
/* 177:406 */     int k = 0;
/* 178:407 */     while (((u & 0x1) == 0) && ((v & 0x1) == 0) && (k < 31))
/* 179:    */     {
/* 180:409 */       u /= 2;
/* 181:410 */       v /= 2;
/* 182:411 */       k++;
/* 183:    */     }
/* 184:413 */     if (k == 31) {
/* 185:414 */       throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, new Object[] { Integer.valueOf(p), Integer.valueOf(q) });
/* 186:    */     }
/* 187:419 */     int t = (u & 0x1) == 1 ? v : -(u / 2);
/* 188:    */     do
/* 189:    */     {
/* 190:425 */       while ((t & 0x1) == 0) {
/* 191:426 */         t /= 2;
/* 192:    */       }
/* 193:429 */       if (t > 0) {
/* 194:430 */         u = -t;
/* 195:    */       } else {
/* 196:432 */         v = t;
/* 197:    */       }
/* 198:435 */       t = (v - u) / 2;
/* 199:438 */     } while (t != 0);
/* 200:439 */     return -u * (1 << k);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public static long gcd(long p, long q)
/* 204:    */   {
/* 205:472 */     long u = p;
/* 206:473 */     long v = q;
/* 207:474 */     if ((u == 0L) || (v == 0L))
/* 208:    */     {
/* 209:475 */       if ((u == -9223372036854775808L) || (v == -9223372036854775808L)) {
/* 210:476 */         throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_64_BITS, new Object[] { Long.valueOf(p), Long.valueOf(q) });
/* 211:    */       }
/* 212:479 */       return FastMath.abs(u) + FastMath.abs(v);
/* 213:    */     }
/* 214:486 */     if (u > 0L) {
/* 215:487 */       u = -u;
/* 216:    */     }
/* 217:489 */     if (v > 0L) {
/* 218:490 */       v = -v;
/* 219:    */     }
/* 220:493 */     int k = 0;
/* 221:494 */     while (((u & 1L) == 0L) && ((v & 1L) == 0L) && (k < 63))
/* 222:    */     {
/* 223:496 */       u /= 2L;
/* 224:497 */       v /= 2L;
/* 225:498 */       k++;
/* 226:    */     }
/* 227:500 */     if (k == 63) {
/* 228:501 */       throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_64_BITS, new Object[] { Long.valueOf(p), Long.valueOf(q) });
/* 229:    */     }
/* 230:506 */     long t = (u & 1L) == 1L ? v : -(u / 2L);
/* 231:    */     do
/* 232:    */     {
/* 233:512 */       while ((t & 1L) == 0L) {
/* 234:513 */         t /= 2L;
/* 235:    */       }
/* 236:516 */       if (t > 0L) {
/* 237:517 */         u = -t;
/* 238:    */       } else {
/* 239:519 */         v = t;
/* 240:    */       }
/* 241:522 */       t = (v - u) / 2L;
/* 242:525 */     } while (t != 0L);
/* 243:526 */     return -u * (1L << k);
/* 244:    */   }
/* 245:    */   
/* 246:    */   public static int lcm(int a, int b)
/* 247:    */   {
/* 248:552 */     if ((a == 0) || (b == 0)) {
/* 249:553 */       return 0;
/* 250:    */     }
/* 251:555 */     int lcm = FastMath.abs(mulAndCheck(a / gcd(a, b), b));
/* 252:556 */     if (lcm == -2147483648) {
/* 253:557 */       throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_32_BITS, new Object[] { Integer.valueOf(a), Integer.valueOf(b) });
/* 254:    */     }
/* 255:560 */     return lcm;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public static long lcm(long a, long b)
/* 259:    */   {
/* 260:586 */     if ((a == 0L) || (b == 0L)) {
/* 261:587 */       return 0L;
/* 262:    */     }
/* 263:589 */     long lcm = FastMath.abs(mulAndCheck(a / gcd(a, b), b));
/* 264:590 */     if (lcm == -9223372036854775808L) {
/* 265:591 */       throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_64_BITS, new Object[] { Long.valueOf(a), Long.valueOf(b) });
/* 266:    */     }
/* 267:594 */     return lcm;
/* 268:    */   }
/* 269:    */   
/* 270:    */   public static int mulAndCheck(int x, int y)
/* 271:    */   {
/* 272:608 */     long m = x * y;
/* 273:609 */     if ((m < -2147483648L) || (m > 2147483647L)) {
/* 274:610 */       throw new MathArithmeticException();
/* 275:    */     }
/* 276:612 */     return (int)m;
/* 277:    */   }
/* 278:    */   
/* 279:    */   public static long mulAndCheck(long a, long b)
/* 280:    */   {
/* 281:    */     long ret;
/* 282:    */     
/* 283:627 */     if (a > b)
/* 284:    */     {
/* 285:629 */       ret = mulAndCheck(b, a);
/* 286:    */     }
/* 287:    */     else
/* 288:    */     {
/* 289:    */     
/* 290:631 */       if (a < 0L)
/* 291:    */       {
/* 292:632 */         if (b < 0L)
/* 293:    */         {
/* 294:    */          
/* 295:634 */           if (a >= 9223372036854775807L / b) {
/* 296:635 */             ret = a * b;
/* 297:    */           } else {
/* 298:637 */             throw new MathArithmeticException();
/* 299:    */           }
/* 300:    */         }
/* 301:639 */         else if (b > 0L)
/* 302:    */         {
/* 303:    */        
/* 304:641 */           if (-9223372036854775808L / b <= a) {
/* 305:642 */             ret = a * b;
/* 306:    */           } else {
/* 307:644 */             throw new MathArithmeticException();
/* 308:    */           }
/* 309:    */         }
/* 310:    */         else
/* 311:    */         {
/* 312:649 */           ret = 0L;
/* 313:    */         }
/* 314:    */       }
/* 315:651 */       else if (a > 0L)
/* 316:    */       {
/* 317:    */        
/* 318:656 */         if (a <= 9223372036854775807L / b) {
/* 319:657 */           ret = a * b;
/* 320:    */         } else {
/* 321:659 */           throw new MathArithmeticException();
/* 322:    */         }
/* 323:    */       }
/* 324:    */       else
/* 325:    */       {
/* 326:663 */         ret = 0L;
/* 327:    */       }
/* 328:    */     }
/* 329:666 */     return ret;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public static int subAndCheck(int x, int y)
/* 333:    */   {
/* 334:680 */     long s = x - y;
/* 335:681 */     if ((s < -2147483648L) || (s > 2147483647L)) {
/* 336:682 */       throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, new Object[] { Integer.valueOf(x), Integer.valueOf(y) });
/* 337:    */     }
/* 338:684 */     return (int)s;
/* 339:    */   }
/* 340:    */   
/* 341:    */   public static long subAndCheck(long a, long b)
/* 342:    */   {
/* 343:    */     long ret;
/* 344:699 */     if (b == -9223372036854775808L)
/* 345:    */     {
/* 346:    */     
/* 347:700 */       if (a < 0L) {
/* 348:701 */         ret = a - b;
/* 349:    */       } else {
/* 350:703 */         throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, new Object[] { Long.valueOf(a), Long.valueOf(-b) });
/* 351:    */       }
/* 352:    */     }
/* 353:    */     else
/* 354:    */     {
/* 355:707 */       ret = addAndCheck(a, -b, LocalizedFormats.OVERFLOW_IN_ADDITION);
/* 356:    */     }
/* 357:709 */     return ret;
/* 358:    */   }
/* 359:    */   
/* 360:    */   public static int pow(int k, int e)
/* 361:    */   {
/* 362:721 */     if (e < 0) {
/* 363:722 */       throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e));
/* 364:    */     }
/* 365:725 */     int result = 1;
/* 366:726 */     int k2p = k;
/* 367:727 */     while (e != 0)
/* 368:    */     {
/* 369:728 */       if ((e & 0x1) != 0) {
/* 370:729 */         result *= k2p;
/* 371:    */       }
/* 372:731 */       k2p *= k2p;
/* 373:732 */       e >>= 1;
/* 374:    */     }
/* 375:735 */     return result;
/* 376:    */   }
/* 377:    */   
/* 378:    */   public static int pow(int k, long e)
/* 379:    */   {
/* 380:747 */     if (e < 0L) {
/* 381:748 */       throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e));
/* 382:    */     }
/* 383:751 */     int result = 1;
/* 384:752 */     int k2p = k;
/* 385:753 */     while (e != 0L)
/* 386:    */     {
/* 387:754 */       if ((e & 1L) != 0L) {
/* 388:755 */         result *= k2p;
/* 389:    */       }
/* 390:757 */       k2p *= k2p;
/* 391:758 */       e >>= 1;
/* 392:    */     }
/* 393:761 */     return result;
/* 394:    */   }
/* 395:    */   
/* 396:    */   public static long pow(long k, int e)
/* 397:    */   {
/* 398:773 */     if (e < 0) {
/* 399:774 */       throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e));
/* 400:    */     }
/* 401:777 */     long result = 1L;
/* 402:778 */     long k2p = k;
/* 403:779 */     while (e != 0)
/* 404:    */     {
/* 405:780 */       if ((e & 0x1) != 0) {
/* 406:781 */         result *= k2p;
/* 407:    */       }
/* 408:783 */       k2p *= k2p;
/* 409:784 */       e >>= 1;
/* 410:    */     }
/* 411:787 */     return result;
/* 412:    */   }
/* 413:    */   
/* 414:    */   public static long pow(long k, long e)
/* 415:    */   {
/* 416:799 */     if (e < 0L) {
/* 417:800 */       throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e));
/* 418:    */     }
/* 419:803 */     long result = 1L;
/* 420:804 */     long k2p = k;
/* 421:805 */     while (e != 0L)
/* 422:    */     {
/* 423:806 */       if ((e & 1L) != 0L) {
/* 424:807 */         result *= k2p;
/* 425:    */       }
/* 426:809 */       k2p *= k2p;
/* 427:810 */       e >>= 1;
/* 428:    */     }
/* 429:813 */     return result;
/* 430:    */   }
/* 431:    */   
/* 432:    */   public static BigInteger pow(BigInteger k, int e)
/* 433:    */   {
/* 434:825 */     if (e < 0) {
/* 435:826 */       throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e));
/* 436:    */     }
/* 437:829 */     return k.pow(e);
/* 438:    */   }
/* 439:    */   
/* 440:    */   public static BigInteger pow(BigInteger k, long e)
/* 441:    */   {
/* 442:841 */     if (e < 0L) {
/* 443:842 */       throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e));
/* 444:    */     }
/* 445:845 */     BigInteger result = BigInteger.ONE;
/* 446:846 */     BigInteger k2p = k;
/* 447:847 */     while (e != 0L)
/* 448:    */     {
/* 449:848 */       if ((e & 1L) != 0L) {
/* 450:849 */         result = result.multiply(k2p);
/* 451:    */       }
/* 452:851 */       k2p = k2p.multiply(k2p);
/* 453:852 */       e >>= 1;
/* 454:    */     }
/* 455:855 */     return result;
/* 456:    */   }
/* 457:    */   
/* 458:    */   public static BigInteger pow(BigInteger k, BigInteger e)
/* 459:    */   {
/* 460:868 */     if (e.compareTo(BigInteger.ZERO) < 0) {
/* 461:869 */       throw new NotPositiveException(LocalizedFormats.EXPONENT, e);
/* 462:    */     }
/* 463:872 */     BigInteger result = BigInteger.ONE;
/* 464:873 */     BigInteger k2p = k;
/* 465:874 */     while (!BigInteger.ZERO.equals(e))
/* 466:    */     {
/* 467:875 */       if (e.testBit(0)) {
/* 468:876 */         result = result.multiply(k2p);
/* 469:    */       }
/* 470:878 */       k2p = k2p.multiply(k2p);
/* 471:879 */       e = e.shiftRight(1);
/* 472:    */     }
/* 473:882 */     return result;
/* 474:    */   }
/* 475:    */   
/* 476:    */   private static long addAndCheck(long a, long b, Localizable pattern)
/* 477:    */   {
/* 478:    */     long ret;
/* 479:898 */     if (a > b)
/* 480:    */     {
/* 481:900 */       ret = addAndCheck(b, a, pattern);
/* 482:    */     }
/* 483:    */     else
/* 484:    */     {
/* 485:    */     
/* 486:904 */       if (a < 0L)
/* 487:    */       {
/* 488:905 */         if (b < 0L)
/* 489:    */         {
/* 490:    */          
/* 491:907 */           if (-9223372036854775808L - b <= a) {
/* 492:908 */             ret = a + b;
/* 493:    */           } else {
/* 494:910 */             throw new MathArithmeticException(pattern, new Object[] { Long.valueOf(a), Long.valueOf(b) });
/* 495:    */           }
/* 496:    */         }
/* 497:    */         else
/* 498:    */         {
/* 499:914 */           ret = a + b;
/* 500:    */         }
/* 501:    */       }
/* 502:    */       else
/* 503:    */       {
/* 504:    */        
/* 505:921 */         if (a <= 9223372036854775807L - b) {
/* 506:922 */           ret = a + b;
/* 507:    */         } else {
/* 508:924 */           throw new MathArithmeticException(pattern, new Object[] { Long.valueOf(a), Long.valueOf(b) });
/* 509:    */         }
/* 510:    */       }
/* 511:    */     }
/* 512:    */    
/* 513:928 */     return ret;
/* 514:    */   }
/* 515:    */   
/* 516:    */   private static void checkBinomial(int n, int k)
/* 517:    */   {
/* 518:940 */     if (n < k) {
/* 519:941 */       throw new NumberIsTooLargeException(LocalizedFormats.BINOMIAL_INVALID_PARAMETERS_ORDER, Integer.valueOf(k), Integer.valueOf(n), true);
/* 520:    */     }
/* 521:944 */     if (n < 0) {
/* 522:945 */       throw new NotPositiveException(LocalizedFormats.BINOMIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
/* 523:    */     }
/* 524:    */   }
/* 525:    */   
/* 526:    */   public static boolean isPowerOfTwo(long n)
/* 527:    */   {
/* 528:956 */     return (n > 0L) && ((n & n - 1L) == 0L);
/* 529:    */   }
/* 530:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.ArithmeticUtils
 * JD-Core Version:    0.7.0.1
 */
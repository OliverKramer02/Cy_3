/*    1:     */ package org.apache.commons.math3.random;
/*    2:     */ 
/*    3:     */ import java.io.Serializable;
/*    4:     */ import java.security.MessageDigest;
/*    5:     */ import java.security.NoSuchAlgorithmException;
/*    6:     */ import java.security.NoSuchProviderException;
/*    7:     */ import java.security.SecureRandom;
/*    8:     */ import java.util.Collection;
/*    9:     */ import org.apache.commons.math3.distribution.BetaDistribution;
/*   10:     */ import org.apache.commons.math3.distribution.BinomialDistribution;
/*   11:     */ import org.apache.commons.math3.distribution.CauchyDistribution;
/*   12:     */ import org.apache.commons.math3.distribution.ChiSquaredDistribution;
/*   13:     */ import org.apache.commons.math3.distribution.FDistribution;
/*   14:     */ import org.apache.commons.math3.distribution.HypergeometricDistribution;
/*   15:     */ import org.apache.commons.math3.distribution.IntegerDistribution;
/*   16:     */ import org.apache.commons.math3.distribution.PascalDistribution;
/*   17:     */ import org.apache.commons.math3.distribution.RealDistribution;
/*   18:     */ import org.apache.commons.math3.distribution.TDistribution;
/*   19:     */ import org.apache.commons.math3.distribution.WeibullDistribution;
/*   20:     */ import org.apache.commons.math3.distribution.ZipfDistribution;
/*   21:     */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   22:     */ import org.apache.commons.math3.exception.MathInternalError;
/*   23:     */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   24:     */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   25:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   26:     */ import org.apache.commons.math3.util.ArithmeticUtils;
/*   27:     */ import org.apache.commons.math3.util.FastMath;
/*   28:     */ import org.apache.commons.math3.util.ResizableDoubleArray;
/*   29:     */ 
/*   30:     */ public class RandomDataImpl
/*   31:     */   implements RandomData, Serializable
/*   32:     */ {
/*   33:     */   private static final long serialVersionUID = -626730818244969716L;
/*   34:     */   private static final double[] EXPONENTIAL_SA_QI;
/*   35: 126 */   private RandomGenerator rand = null;
/*   36: 129 */   private SecureRandom secRand = null;
/*   37:     */   
/*   38:     */   static
/*   39:     */   {
/*   40: 139 */     double LN2 = FastMath.log(2.0D);
/*   41: 140 */     double qi = 0.0D;
/*   42: 141 */     int i = 1;
/*   43:     */     
/*   44:     */ 
/*   45:     */ 
/*   46:     */ 
/*   47:     */ 
/*   48:     */ 
/*   49:     */ 
/*   50: 149 */     ResizableDoubleArray ra = new ResizableDoubleArray(20);
/*   51: 151 */     while (qi < 1.0D)
/*   52:     */     {
/*   53: 152 */       qi += FastMath.pow(LN2, i) / ArithmeticUtils.factorial(i);
/*   54: 153 */       ra.addElement(qi);
/*   55: 154 */       i++;
/*   56:     */     }
/*   57: 157 */     EXPONENTIAL_SA_QI = ra.getElements();
/*   58:     */   }
/*   59:     */   
/*   60:     */   public RandomDataImpl(RandomGenerator rand)
/*   61:     */   {
/*   62: 181 */     this.rand = rand;
/*   63:     */   }
/*   64:     */   
/*   65:     */   public String nextHexString(int len)
/*   66:     */   {
/*   67: 201 */     if (len <= 0) {
/*   68: 202 */       throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(len));
/*   69:     */     }
/*   70: 206 */     RandomGenerator ran = getRan();
/*   71:     */     
/*   72:     */ 
/*   73: 209 */     StringBuilder outBuffer = new StringBuilder();
/*   74:     */     
/*   75:     */ 
/*   76: 212 */     byte[] randomBytes = new byte[len / 2 + 1];
/*   77: 213 */     ran.nextBytes(randomBytes);
/*   78: 216 */     for (int i = 0; i < randomBytes.length; i++)
/*   79:     */     {
/*   80: 217 */       Integer c = Integer.valueOf(randomBytes[i]);
/*   81:     */       
/*   82:     */ 
/*   83:     */ 
/*   84:     */ 
/*   85:     */ 
/*   86:     */ 
/*   87: 224 */       String hex = Integer.toHexString(c.intValue() + 128);
/*   88: 227 */       if (hex.length() == 1) {
/*   89: 228 */         hex = "0" + hex;
/*   90:     */       }
/*   91: 230 */       outBuffer.append(hex);
/*   92:     */     }
/*   93: 232 */     return outBuffer.toString().substring(0, len);
/*   94:     */   }
/*   95:     */   
/*   96:     */   public int nextInt(int lower, int upper)
/*   97:     */   {
/*   98: 237 */     if (lower >= upper) {
/*   99: 238 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Integer.valueOf(lower), Integer.valueOf(upper), false);
/*  100:     */     }
/*  101: 241 */     double r = getRan().nextDouble();
/*  102: 242 */     double scaled = r * upper + (1.0D - r) * lower + r;
/*  103: 243 */     return (int)FastMath.floor(scaled);
/*  104:     */   }
/*  105:     */   
/*  106:     */   public long nextLong(long lower, long upper)
/*  107:     */   {
/*  108: 248 */     if (lower >= upper) {
/*  109: 249 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Long.valueOf(lower), Long.valueOf(upper), false);
/*  110:     */     }
/*  111: 252 */     double r = getRan().nextDouble();
/*  112: 253 */     double scaled = r * upper + (1.0D - r) * lower + r;
/*  113: 254 */     return (long) FastMath.floor(scaled);
/*  114:     */   }
/*  115:     */   
/*  116:     */   public String nextSecureHexString(int len)
/*  117:     */   {
/*  118: 274 */     if (len <= 0) {
/*  119: 275 */       throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(len));
/*  120:     */     }
/*  121: 279 */     SecureRandom secRan = getSecRan();
/*  122: 280 */     MessageDigest alg = null;
/*  123:     */     try
/*  124:     */     {
/*  125: 282 */       alg = MessageDigest.getInstance("SHA-1");
/*  126:     */     }
/*  127:     */     catch (NoSuchAlgorithmException ex)
/*  128:     */     {
/*  129: 285 */       throw new MathInternalError(ex);
/*  130:     */     }
/*  131: 287 */     alg.reset();
/*  132:     */     
/*  133:     */ 
/*  134: 290 */     int numIter = len / 40 + 1;
/*  135:     */     
/*  136: 292 */     StringBuilder outBuffer = new StringBuilder();
/*  137: 293 */     for (int iter = 1; iter < numIter + 1; iter++)
/*  138:     */     {
/*  139: 294 */       byte[] randomBytes = new byte[40];
/*  140: 295 */       secRan.nextBytes(randomBytes);
/*  141: 296 */       alg.update(randomBytes);
/*  142:     */       
/*  143:     */ 
/*  144: 299 */       byte[] hash = alg.digest();
/*  145: 302 */       for (int i = 0; i < hash.length; i++)
/*  146:     */       {
/*  147: 303 */         Integer c = Integer.valueOf(hash[i]);
/*  148:     */         
/*  149:     */ 
/*  150:     */ 
/*  151:     */ 
/*  152:     */ 
/*  153:     */ 
/*  154: 310 */         String hex = Integer.toHexString(c.intValue() + 128);
/*  155: 313 */         if (hex.length() == 1) {
/*  156: 314 */           hex = "0" + hex;
/*  157:     */         }
/*  158: 316 */         outBuffer.append(hex);
/*  159:     */       }
/*  160:     */     }
/*  161: 319 */     return outBuffer.toString().substring(0, len);
/*  162:     */   }
/*  163:     */   
/*  164:     */   public int nextSecureInt(int lower, int upper)
/*  165:     */   {
/*  166: 324 */     if (lower >= upper) {
/*  167: 325 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Integer.valueOf(lower), Integer.valueOf(upper), false);
/*  168:     */     }
/*  169: 328 */     SecureRandom sec = getSecRan();
/*  170: 329 */     double r = sec.nextDouble();
/*  171: 330 */     double scaled = r * upper + (1.0D - r) * lower + r;
/*  172: 331 */     return (int)FastMath.floor(scaled);
/*  173:     */   }
/*  174:     */   
/*  175:     */   public long nextSecureLong(long lower, long upper)
/*  176:     */   {
/*  177: 337 */     if (lower >= upper) {
/*  178: 338 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Long.valueOf(lower), Long.valueOf(upper), false);
/*  179:     */     }
/*  180: 341 */     SecureRandom sec = getSecRan();
/*  181: 342 */     double r = sec.nextDouble();
/*  182: 343 */     double scaled = r * upper + (1.0D - r) * lower + r;
/*  183: 344 */     return (long) FastMath.floor(scaled);
/*  184:     */   }
/*  185:     */   
/*  186:     */   public long nextPoisson(double mean)
/*  187:     */   {
/*  188: 361 */     if (mean <= 0.0D) {
/*  189: 362 */       throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, Double.valueOf(mean));
/*  190:     */     }
/*  191: 365 */     double pivot = 40.0D;
/*  192: 366 */     if (mean < 40.0D)
/*  193:     */     {
/*  194: 367 */       RandomGenerator generator = getRan();
/*  195: 368 */       double p = FastMath.exp(-mean);
/*  196: 369 */       long n = 0L;
/*  197: 370 */       double r = 1.0D;
/*  198: 371 */       double rnd = 1.0D;
/*  199: 373 */       while (n < 1000.0D * mean)
/*  200:     */       {
/*  201: 374 */         rnd = generator.nextDouble();
/*  202: 375 */         r *= rnd;
/*  203: 376 */         if (r >= p) {
/*  204: 377 */           n += 1L;
/*  205:     */         } else {
/*  206: 379 */           return n;
/*  207:     */         }
/*  208:     */       }
/*  209: 382 */       return n;
/*  210:     */     }
/*  211: 384 */     double lambda = FastMath.floor(mean);
/*  212: 385 */     double lambdaFractional = mean - lambda;
/*  213: 386 */     double logLambda = FastMath.log(lambda);
/*  214: 387 */     double logLambdaFactorial = ArithmeticUtils.factorialLog((int)lambda);
/*  215: 388 */     long y2 = lambdaFractional < 4.9E-324D ? 0L : nextPoisson(lambdaFractional);
/*  216: 389 */     double delta = FastMath.sqrt(lambda * FastMath.log(32.0D * lambda / 3.141592653589793D + 1.0D));
/*  217: 390 */     double halfDelta = delta / 2.0D;
/*  218: 391 */     double twolpd = 2.0D * lambda + delta;
/*  219: 392 */     double a1 = FastMath.sqrt(3.141592653589793D * twolpd) * FastMath.exp(0.0D * lambda);
/*  220: 393 */     double a2 = twolpd / delta * FastMath.exp(-delta * (1.0D + delta) / twolpd);
/*  221: 394 */     double aSum = a1 + a2 + 1.0D;
/*  222: 395 */     double p1 = a1 / aSum;
/*  223: 396 */     double p2 = a2 / aSum;
/*  224: 397 */     double c1 = 1.0D / (8.0D * lambda);
/*  225:     */     
/*  226: 399 */     double x = 0.0D;
/*  227: 400 */     double y = 0.0D;
/*  228: 401 */     double v = 0.0D;
/*  229: 402 */     int a = 0;
/*  230: 403 */     double t = 0.0D;
/*  231: 404 */     double qr = 0.0D;
/*  232: 405 */     double qa = 0.0D;
/*  233:     */     for (;;)
/*  234:     */     {
/*  235: 407 */       double u = nextUniform(0.0D, 1.0D);
/*  236: 408 */       if (u <= p1)
/*  237:     */       {
/*  238: 409 */         double n = nextGaussian(0.0D, 1.0D);
/*  239: 410 */         x = n * FastMath.sqrt(lambda + halfDelta) - 0.5D;
/*  240: 411 */         if ((x > delta) || (x < -lambda)) {
/*  241:     */           continue;
/*  242:     */         }
/*  243: 414 */         y = x < 0.0D ? FastMath.floor(x) : FastMath.ceil(x);
/*  244: 415 */         double e = nextExponential(1.0D);
/*  245: 416 */         v = -e - n * n / 2.0D + c1;
/*  246:     */       }
/*  247:     */       else
/*  248:     */       {
/*  249: 418 */         if (u > p1 + p2)
/*  250:     */         {
/*  251: 419 */           y = lambda;
/*  252: 420 */           break;
/*  253:     */         }
/*  254: 422 */         x = delta + twolpd / delta * nextExponential(1.0D);
/*  255: 423 */         y = FastMath.ceil(x);
/*  256: 424 */         v = -nextExponential(1.0D) - delta * (x + 1.0D) / twolpd;
/*  257:     */       }
/*  258: 427 */       a = x < 0.0D ? 1 : 0;
/*  259: 428 */       t = y * (y + 1.0D) / (2.0D * lambda);
/*  260: 429 */       if ((v < -t) && (a == 0))
/*  261:     */       {
/*  262: 430 */         y = lambda + y;
/*  263: 431 */         break;
/*  264:     */       }
/*  265: 433 */       qr = t * ((2.0D * y + 1.0D) / (6.0D * lambda) - 1.0D);
/*  266: 434 */       qa = qr - t * t / (3.0D * (lambda + a * (y + 1.0D)));
/*  267: 435 */       if (v < qa)
/*  268:     */       {
/*  269: 436 */         y = lambda + y;
/*  270: 437 */         break;
/*  271:     */       }
/*  272: 439 */       if (v <= qr) {
/*  273: 442 */         if (v < y * logLambda - ArithmeticUtils.factorialLog((int)(y + lambda)) + logLambdaFactorial)
/*  274:     */         {
/*  275: 443 */           y = lambda + y;
/*  276: 444 */           break;
/*  277:     */         }
/*  278:     */       }
/*  279:     */     }
/*  280: 447 */     return (long) (y2 + y);
/*  281:     */   }
/*  282:     */   
/*  283:     */   public double nextGaussian(double mu, double sigma)
/*  284:     */   {
/*  285: 454 */     if (sigma <= 0.0D) {
/*  286: 455 */       throw new NotStrictlyPositiveException(LocalizedFormats.STANDARD_DEVIATION, Double.valueOf(sigma));
/*  287:     */     }
/*  288: 457 */     return sigma * getRan().nextGaussian() + mu;
/*  289:     */   }
/*  290:     */   
/*  291:     */   public double nextExponential(double mean)
/*  292:     */   {
/*  293: 473 */     if (mean <= 0.0D) {
/*  294: 474 */       throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, Double.valueOf(mean));
/*  295:     */     }
/*  296: 478 */     double a = 0.0D;
/*  297: 479 */     double u = nextUniform(0.0D, 1.0D);
/*  298: 482 */     while (u < 0.5D)
/*  299:     */     {
/*  300: 483 */       a += EXPONENTIAL_SA_QI[0];
/*  301: 484 */       u *= 2.0D;
/*  302:     */     }
/*  303: 488 */     u += u - 1.0D;
/*  304: 491 */     if (u <= EXPONENTIAL_SA_QI[0]) {
/*  305: 492 */       return mean * (a + u);
/*  306:     */     }
/*  307: 496 */     int i = 0;
/*  308: 497 */     double u2 = nextUniform(0.0D, 1.0D);
/*  309: 498 */     double umin = u2;
/*  310:     */     do
/*  311:     */     {
/*  312: 502 */       i++;
/*  313: 503 */       u2 = nextUniform(0.0D, 1.0D);
/*  314: 505 */       if (u2 < umin) {
/*  315: 506 */         umin = u2;
/*  316:     */       }
/*  317: 510 */     } while (u > EXPONENTIAL_SA_QI[i]);
/*  318: 512 */     return mean * (a + umin * EXPONENTIAL_SA_QI[0]);
/*  319:     */   }
/*  320:     */   
/*  321:     */   public double nextUniform(double lower, double upper)
/*  322:     */   {
/*  323: 530 */     return nextUniform(lower, upper, false);
/*  324:     */   }
/*  325:     */   
/*  326:     */   public double nextUniform(double lower, double upper, boolean lowerInclusive)
/*  327:     */   {
/*  328: 551 */     if (lower >= upper) {
/*  329: 552 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(lower), Double.valueOf(upper), false);
/*  330:     */     }
/*  331: 556 */     if ((Double.isInfinite(lower)) || (Double.isInfinite(upper))) {
/*  332: 557 */       throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_BOUND, new Object[0]);
/*  333:     */     }
/*  334: 560 */     if ((Double.isNaN(lower)) || (Double.isNaN(upper))) {
/*  335: 561 */       throw new MathIllegalArgumentException(LocalizedFormats.NAN_NOT_ALLOWED, new Object[0]);
/*  336:     */     }
/*  337: 564 */     RandomGenerator generator = getRan();
/*  338:     */     
/*  339:     */ 
/*  340: 567 */     double u = generator.nextDouble();
/*  341: 568 */     while ((!lowerInclusive) && (u <= 0.0D)) {
/*  342: 569 */       u = generator.nextDouble();
/*  343:     */     }
/*  344: 572 */     return u * upper + (1.0D - u) * lower;
/*  345:     */   }
/*  346:     */   
/*  347:     */   public double nextBeta(double alpha, double beta)
/*  348:     */   {
/*  349: 586 */     return nextInversionDeviate(new BetaDistribution(alpha, beta));
/*  350:     */   }
/*  351:     */   
/*  352:     */   public int nextBinomial(int numberOfTrials, double probabilityOfSuccess)
/*  353:     */   {
/*  354: 600 */     return nextInversionDeviate(new BinomialDistribution(numberOfTrials, probabilityOfSuccess));
/*  355:     */   }
/*  356:     */   
/*  357:     */   public double nextCauchy(double median, double scale)
/*  358:     */   {
/*  359: 614 */     return nextInversionDeviate(new CauchyDistribution(median, scale));
/*  360:     */   }
/*  361:     */   
/*  362:     */   public double nextChiSquare(double df)
/*  363:     */   {
/*  364: 627 */     return nextInversionDeviate(new ChiSquaredDistribution(df));
/*  365:     */   }
/*  366:     */   
/*  367:     */   public double nextF(double numeratorDf, double denominatorDf)
/*  368:     */   {
/*  369: 641 */     return nextInversionDeviate(new FDistribution(numeratorDf, denominatorDf));
/*  370:     */   }
/*  371:     */   
/*  372:     */   public double nextGamma(double shape, double scale)
/*  373:     */   {
/*  374: 666 */     if (shape < 1.0D)
/*  375:     */     {
/*  376:     */       double x = 0;
/*  377:     */       double u2;
/*  378:     */       do
/*  379:     */       {
/*  380:     */         double bGS;
/*  381:     */         double p;
/*  382:     */         
/*  383:     */      
/*  384:     */         do
/*  385:     */         {
/*  386: 671 */           double u = nextUniform(0.0D, 1.0D);
/*  387: 672 */           bGS = 1.0D + shape / 2.718281828459045D;
/*  388: 673 */           p = bGS * u;
/*  389: 675 */           if (p > 1.0D) {
/*  390:     */             break;
/*  391:     */           }
/*  392: 678 */           x = FastMath.pow(p, 1.0D / shape);
/*  393: 679 */           u2 = nextUniform(0.0D, 1.0D);
/*  394: 681 */         } while (u2 > FastMath.exp(-x));
/*  395: 685 */         return scale * x;       } while (u2 > FastMath.pow(x, shape - 1.0D));     }
/*  405: 705 */     RandomGenerator generator = getRan();
/*  406: 706 */     double d = shape - 0.3333333333333333D;
/*  407: 707 */     double c = 1.0D / (3.0D * FastMath.sqrt(d));
/*  408:     */     for (;;)
/*  409:     */     {
/*  410: 710 */       double x = generator.nextGaussian();
/*  411: 711 */       double v = (1.0D + c * x) * (1.0D + c * x) * (1.0D + c * x);
/*  412: 713 */       if (v > 0.0D)
/*  413:     */       {
/*  414: 717 */         double xx = x * x;
/*  415: 718 */         double u = nextUniform(0.0D, 1.0D);
/*  416: 721 */         if (u < 1.0D - 0.0331D * xx * xx) {
/*  417: 722 */           return scale * d * v;
/*  418:     */         }
/*  419: 725 */         if (FastMath.log(u) < 0.5D * xx + d * (1.0D - v + FastMath.log(v))) {
/*  420: 726 */           return scale * d * v;
/*  421:     */         }
/*  422:     */       }
/*  423:     */     }
/*  424:     */   }
/*  425:     */   
/*  426:     */   public int nextHypergeometric(int populationSize, int numberOfSuccesses, int sampleSize)
/*  427:     */   {
/*  428: 743 */     return nextInversionDeviate(new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
/*  429:     */   }
/*  430:     */   
/*  431:     */   public int nextPascal(int r, double p)
/*  432:     */   {
/*  433: 757 */     return nextInversionDeviate(new PascalDistribution(r, p));
/*  434:     */   }
/*  435:     */   
/*  436:     */   public double nextT(double df)
/*  437:     */   {
/*  438: 770 */     return nextInversionDeviate(new TDistribution(df));
/*  439:     */   }
/*  440:     */   
/*  441:     */   public double nextWeibull(double shape, double scale)
/*  442:     */   {
/*  443: 784 */     return nextInversionDeviate(new WeibullDistribution(shape, scale));
/*  444:     */   }
/*  445:     */   
/*  446:     */   public int nextZipf(int numberOfElements, double exponent)
/*  447:     */   {
/*  448: 798 */     return nextInversionDeviate(new ZipfDistribution(numberOfElements, exponent));
/*  449:     */   }
/*  450:     */   
/*  451:     */   private RandomGenerator getRan()
/*  452:     */   {
/*  453: 812 */     if (this.rand == null) {
/*  454: 813 */       initRan();
/*  455:     */     }
/*  456: 815 */     return this.rand;
/*  457:     */   }
/*  458:     */   
/*  459:     */   private void initRan()
/*  460:     */   {
/*  461: 823 */     this.rand = new Well19937c(System.currentTimeMillis() + System.identityHashCode(this));
/*  462:     */   }
/*  463:     */   
/*  464:     */   private SecureRandom getSecRan()
/*  465:     */   {
/*  466: 836 */     if (this.secRand == null)
/*  467:     */     {
/*  468: 837 */       this.secRand = new SecureRandom();
/*  469: 838 */       this.secRand.setSeed(System.currentTimeMillis() + System.identityHashCode(this));
/*  470:     */     }
/*  471: 840 */     return this.secRand;
/*  472:     */   }
/*  473:     */   
/*  474:     */   public void reSeed(long seed)
/*  475:     */   {
/*  476: 853 */     if (this.rand == null) {
/*  477: 854 */       initRan();
/*  478:     */     }
/*  479: 856 */     this.rand.setSeed(seed);
/*  480:     */   }
/*  481:     */   
/*  482:     */   public void reSeedSecure()
/*  483:     */   {
/*  484: 867 */     if (this.secRand == null) {
/*  485: 868 */       this.secRand = new SecureRandom();
/*  486:     */     }
/*  487: 870 */     this.secRand.setSeed(System.currentTimeMillis());
/*  488:     */   }
/*  489:     */   
/*  490:     */   public void reSeedSecure(long seed)
/*  491:     */   {
/*  492: 883 */     if (this.secRand == null) {
/*  493: 884 */       this.secRand = new SecureRandom();
/*  494:     */     }
/*  495: 886 */     this.secRand.setSeed(seed);
/*  496:     */   }
/*  497:     */   
/*  498:     */   public void reSeed()
/*  499:     */   {
/*  500: 894 */     if (this.rand == null) {
/*  501: 895 */       initRan();
/*  502:     */     }
/*  503: 897 */     this.rand.setSeed(System.currentTimeMillis() + System.identityHashCode(this));
/*  504:     */   }
/*  505:     */   
/*  506:     */   public void setSecureAlgorithm(String algorithm, String provider)
/*  507:     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*  508:     */   {
/*  509: 922 */     this.secRand = SecureRandom.getInstance(algorithm, provider);
/*  510:     */   }
/*  511:     */   
/*  512:     */   public int[] nextPermutation(int n, int k)
/*  513:     */   {
/*  514: 935 */     if (k > n) {
/*  515: 936 */       throw new NumberIsTooLargeException(LocalizedFormats.PERMUTATION_EXCEEDS_N, Integer.valueOf(k), Integer.valueOf(n), true);
/*  516:     */     }
/*  517: 939 */     if (k <= 0) {
/*  518: 940 */       throw new NotStrictlyPositiveException(LocalizedFormats.PERMUTATION_SIZE, Integer.valueOf(k));
/*  519:     */     }
/*  520: 944 */     int[] index = getNatural(n);
/*  521: 945 */     shuffle(index, n - k);
/*  522: 946 */     int[] result = new int[k];
/*  523: 947 */     for (int i = 0; i < k; i++) {
/*  524: 948 */       result[i] = index[(n - i - 1)];
/*  525:     */     }
/*  526: 951 */     return result;
/*  527:     */   }
/*  528:     */   
/*  529:     */   public Object[] nextSample(Collection<?> c, int k)
/*  530:     */   {
/*  531: 969 */     int len = c.size();
/*  532: 970 */     if (k > len) {
/*  533: 971 */       throw new NumberIsTooLargeException(LocalizedFormats.SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE, Integer.valueOf(k), Integer.valueOf(len), true);
/*  534:     */     }
/*  535: 974 */     if (k <= 0) {
/*  536: 975 */       throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(k));
/*  537:     */     }
/*  538: 978 */     Object[] objects = c.toArray();
/*  539: 979 */     int[] index = nextPermutation(len, k);
/*  540: 980 */     Object[] result = new Object[k];
/*  541: 981 */     for (int i = 0; i < k; i++) {
/*  542: 982 */       result[i] = objects[index[i]];
/*  543:     */     }
/*  544: 984 */     return result;
/*  545:     */   }
/*  546:     */   
/*  547:     */   public double nextInversionDeviate(RealDistribution distribution)
/*  548:     */   {
/*  549: 996 */     return distribution.inverseCumulativeProbability(nextUniform(0.0D, 1.0D));
/*  550:     */   }
/*  551:     */   
/*  552:     */   public int nextInversionDeviate(IntegerDistribution distribution)
/*  553:     */   {
/*  554:1009 */     return distribution.inverseCumulativeProbability(nextUniform(0.0D, 1.0D));
/*  555:     */   }
/*  556:     */   
/*  557:     */   private void shuffle(int[] list, int end)
/*  558:     */   {
/*  559:1024 */     int target = 0;
/*  560:1025 */     for (int i = list.length - 1; i >= end; i--)
/*  561:     */     {
/*  562:1026 */       if (i == 0) {
/*  563:1027 */         target = 0;
/*  564:     */       } else {
/*  565:1029 */         target = nextInt(0, i);
/*  566:     */       }
/*  567:1031 */       int temp = list[target];
/*  568:1032 */       list[target] = list[i];
/*  569:1033 */       list[i] = temp;
/*  570:     */     }
/*  571:     */   }
/*  572:     */   
/*  573:     */   private int[] getNatural(int n)
/*  574:     */   {
/*  575:1045 */     int[] natural = new int[n];
/*  576:1046 */     for (int i = 0; i < n; i++) {
/*  577:1047 */       natural[i] = i;
/*  578:     */     }
/*  579:1049 */     return natural;
/*  580:     */   }
/*  581:     */   
/*  582:     */   public RandomDataImpl() {}
/*  583:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.RandomDataImpl
 * JD-Core Version:    0.7.0.1
 */
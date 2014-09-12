/*    1:     */ package org.apache.commons.math3.complex;
/*    2:     */ 
/*    3:     */ import java.io.Serializable;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.List;
/*    6:     */ import org.apache.commons.math3.FieldElement;
/*    7:     */ import org.apache.commons.math3.exception.NotPositiveException;
/*    8:     */ import org.apache.commons.math3.exception.NullArgumentException;
/*    9:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   10:     */ import org.apache.commons.math3.util.FastMath;
/*   11:     */ import org.apache.commons.math3.util.MathUtils;
/*   12:     */ 
/*   13:     */ public class Complex
/*   14:     */   implements FieldElement<Complex>, Serializable
/*   15:     */ {
/*   16:  59 */   public static final Complex I = new Complex(0.0D, 1.0D);
/*   17:  62 */   public static final Complex NaN = new Complex((0.0D / 0.0D), (0.0D / 0.0D));
/*   18:  65 */   public static final Complex INF = new Complex((1.0D / 0.0D), (1.0D / 0.0D));
/*   19:  67 */   public static final Complex ONE = new Complex(1.0D, 0.0D);
/*   20:  69 */   public static final Complex ZERO = new Complex(0.0D, 0.0D);
/*   21:     */   private static final long serialVersionUID = -6195664516687396620L;
/*   22:     */   private final double imaginary;
/*   23:     */   private final double real;
/*   24:     */   private final transient boolean isNaN;
/*   25:     */   private final transient boolean isInfinite;
/*   26:     */   
/*   27:     */   public Complex(double real)
/*   28:     */   {
/*   29:  89 */     this(real, 0.0D);
/*   30:     */   }
/*   31:     */   
/*   32:     */   public Complex(double real, double imaginary)
/*   33:     */   {
/*   34:  99 */     this.real = real;
/*   35: 100 */     this.imaginary = imaginary;
/*   36:     */     
/*   37: 102 */     this.isNaN = ((Double.isNaN(real)) || (Double.isNaN(imaginary)));
/*   38: 103 */     this.isInfinite = ((!this.isNaN) && ((Double.isInfinite(real)) || (Double.isInfinite(imaginary))));
/*   39:     */   }
/*   40:     */   
/*   41:     */   public double abs()
/*   42:     */   {
/*   43: 116 */     if (this.isNaN) {
/*   44: 117 */       return (0.0D / 0.0D);
/*   45:     */     }
/*   46: 119 */     if (isInfinite()) {
/*   47: 120 */       return (1.0D / 0.0D);
/*   48:     */     }
/*   49: 122 */     if (FastMath.abs(this.real) < FastMath.abs(this.imaginary))
/*   50:     */     {
/*   51: 123 */       if (this.imaginary == 0.0D) {
/*   52: 124 */         return FastMath.abs(this.real);
/*   53:     */       }
/*   54: 126 */       double q = this.real / this.imaginary;
/*   55: 127 */       return FastMath.abs(this.imaginary) * FastMath.sqrt(1.0D + q * q);
/*   56:     */     }
/*   57: 129 */     if (this.real == 0.0D) {
/*   58: 130 */       return FastMath.abs(this.imaginary);
/*   59:     */     }
/*   60: 132 */     double q = this.imaginary / this.real;
/*   61: 133 */     return FastMath.abs(this.real) * FastMath.sqrt(1.0D + q * q);
/*   62:     */   }
/*   63:     */   
/*   64:     */   public Complex add(Complex addend)
/*   65:     */     throws NullArgumentException
/*   66:     */   {
/*   67: 157 */     MathUtils.checkNotNull(addend);
/*   68: 158 */     if ((this.isNaN) || (addend.isNaN)) {
/*   69: 159 */       return NaN;
/*   70:     */     }
/*   71: 162 */     return createComplex(this.real + addend.getReal(), this.imaginary + addend.getImaginary());
/*   72:     */   }
/*   73:     */   
/*   74:     */   public Complex add(double addend)
/*   75:     */   {
/*   76: 175 */     if ((this.isNaN) || (Double.isNaN(addend))) {
/*   77: 176 */       return NaN;
/*   78:     */     }
/*   79: 179 */     return createComplex(this.real + addend, this.imaginary);
/*   80:     */   }
/*   81:     */   
/*   82:     */   public Complex conjugate()
/*   83:     */   {
/*   84: 197 */     if (this.isNaN) {
/*   85: 198 */       return NaN;
/*   86:     */     }
/*   87: 201 */     return createComplex(this.real, -this.imaginary);
/*   88:     */   }
/*   89:     */   
/*   90:     */   public Complex divide(Complex divisor)
/*   91:     */     throws NullArgumentException
/*   92:     */   {
/*   93: 248 */     MathUtils.checkNotNull(divisor);
/*   94: 249 */     if ((this.isNaN) || (divisor.isNaN)) {
/*   95: 250 */       return NaN;
/*   96:     */     }
/*   97: 253 */     double c = divisor.getReal();
/*   98: 254 */     double d = divisor.getImaginary();
/*   99: 255 */     if ((c == 0.0D) && (d == 0.0D)) {
/*  100: 256 */       return NaN;
/*  101:     */     }
/*  102: 259 */     if ((divisor.isInfinite()) && (!isInfinite())) {
/*  103: 260 */       return ZERO;
/*  104:     */     }
/*  105: 263 */     if (FastMath.abs(c) < FastMath.abs(d))
/*  106:     */     {
/*  107: 264 */       double q = c / d;
/*  108: 265 */       double denominator = c * q + d;
/*  109: 266 */       return createComplex((this.real * q + this.imaginary) / denominator, (this.imaginary * q - this.real) / denominator);
/*  110:     */     }
/*  111: 269 */     double q = d / c;
/*  112: 270 */     double denominator = d * q + c;
/*  113: 271 */     return createComplex((this.imaginary * q + this.real) / denominator, (this.imaginary - this.real * q) / denominator);
/*  114:     */   }
/*  115:     */   
/*  116:     */   public Complex divide(double divisor)
/*  117:     */   {
/*  118: 285 */     if ((this.isNaN) || (Double.isNaN(divisor))) {
/*  119: 286 */       return NaN;
/*  120:     */     }
/*  121: 288 */     if (divisor == 0.0D) {
/*  122: 289 */       return NaN;
/*  123:     */     }
/*  124: 291 */     if (Double.isInfinite(divisor)) {
/*  125: 292 */       return !isInfinite() ? ZERO : NaN;
/*  126:     */     }
/*  127: 294 */     return createComplex(this.real / divisor, this.imaginary / divisor);
/*  128:     */   }
/*  129:     */   
/*  130:     */   public Complex reciprocal()
/*  131:     */   {
/*  132: 300 */     if (this.isNaN) {
/*  133: 301 */       return NaN;
/*  134:     */     }
/*  135: 304 */     if ((this.real == 0.0D) && (this.imaginary == 0.0D)) {
/*  136: 305 */       return NaN;
/*  137:     */     }
/*  138: 308 */     if (this.isInfinite) {
/*  139: 309 */       return ZERO;
/*  140:     */     }
/*  141: 312 */     if (FastMath.abs(this.real) < FastMath.abs(this.imaginary))
/*  142:     */     {
/*  143: 313 */       double q = this.real / this.imaginary;
/*  144: 314 */       double scale = 1.0D / (this.real * q + this.imaginary);
/*  145: 315 */       return createComplex(scale * q, -scale);
/*  146:     */     }
/*  147: 317 */     double q = this.imaginary / this.real;
/*  148: 318 */     double scale = 1.0D / (this.imaginary * q + this.real);
/*  149: 319 */     return createComplex(scale, -scale * q);
/*  150:     */   }
/*  151:     */   
/*  152:     */   public boolean equals(Object other)
/*  153:     */   {
/*  154: 340 */     if (this == other) {
/*  155: 341 */       return true;
/*  156:     */     }
/*  157: 343 */     if ((other instanceof Complex))
/*  158:     */     {
/*  159: 344 */       Complex c = (Complex)other;
/*  160: 345 */       if (c.isNaN) {
/*  161: 346 */         return this.isNaN;
/*  162:     */       }
/*  163: 348 */       return (this.real == c.real) && (this.imaginary == c.imaginary);
/*  164:     */     }
/*  165: 351 */     return false;
/*  166:     */   }
/*  167:     */   
/*  168:     */   public int hashCode()
/*  169:     */   {
/*  170: 363 */     if (this.isNaN) {
/*  171: 364 */       return 7;
/*  172:     */     }
/*  173: 366 */     return 37 * (17 * MathUtils.hash(this.imaginary) + MathUtils.hash(this.real));
/*  174:     */   }
/*  175:     */   
/*  176:     */   public double getImaginary()
/*  177:     */   {
/*  178: 376 */     return this.imaginary;
/*  179:     */   }
/*  180:     */   
/*  181:     */   public double getReal()
/*  182:     */   {
/*  183: 385 */     return this.real;
/*  184:     */   }
/*  185:     */   
/*  186:     */   public boolean isNaN()
/*  187:     */   {
/*  188: 396 */     return this.isNaN;
/*  189:     */   }
/*  190:     */   
/*  191:     */   public boolean isInfinite()
/*  192:     */   {
/*  193: 409 */     return this.isInfinite;
/*  194:     */   }
/*  195:     */   
/*  196:     */   public Complex multiply(Complex factor)
/*  197:     */     throws NullArgumentException
/*  198:     */   {
/*  199: 438 */     MathUtils.checkNotNull(factor);
/*  200: 439 */     if ((this.isNaN) || (factor.isNaN)) {
/*  201: 440 */       return NaN;
/*  202:     */     }
/*  203: 442 */     if ((Double.isInfinite(this.real)) || (Double.isInfinite(this.imaginary)) || (Double.isInfinite(factor.real)) || (Double.isInfinite(factor.imaginary))) {
/*  204: 447 */       return INF;
/*  205:     */     }
/*  206: 449 */     return createComplex(this.real * factor.real - this.imaginary * factor.imaginary, this.real * factor.imaginary + this.imaginary * factor.real);
/*  207:     */   }
/*  208:     */   
/*  209:     */   public Complex multiply(int factor)
/*  210:     */   {
/*  211: 462 */     if (this.isNaN) {
/*  212: 463 */       return NaN;
/*  213:     */     }
/*  214: 465 */     if ((Double.isInfinite(this.real)) || (Double.isInfinite(this.imaginary))) {
/*  215: 467 */       return INF;
/*  216:     */     }
/*  217: 469 */     return createComplex(this.real * factor, this.imaginary * factor);
/*  218:     */   }
/*  219:     */   
/*  220:     */   public Complex multiply(double factor)
/*  221:     */   {
/*  222: 481 */     if ((this.isNaN) || (Double.isNaN(factor))) {
/*  223: 482 */       return NaN;
/*  224:     */     }
/*  225: 484 */     if ((Double.isInfinite(this.real)) || (Double.isInfinite(this.imaginary)) || (Double.isInfinite(factor))) {
/*  226: 488 */       return INF;
/*  227:     */     }
/*  228: 490 */     return createComplex(this.real * factor, this.imaginary * factor);
/*  229:     */   }
/*  230:     */   
/*  231:     */   public Complex negate()
/*  232:     */   {
/*  233: 501 */     if (this.isNaN) {
/*  234: 502 */       return NaN;
/*  235:     */     }
/*  236: 505 */     return createComplex(-this.real, -this.imaginary);
/*  237:     */   }
/*  238:     */   
/*  239:     */   public Complex subtract(Complex subtrahend)
/*  240:     */     throws NullArgumentException
/*  241:     */   {
/*  242: 528 */     MathUtils.checkNotNull(subtrahend);
/*  243: 529 */     if ((this.isNaN) || (subtrahend.isNaN)) {
/*  244: 530 */       return NaN;
/*  245:     */     }
/*  246: 533 */     return createComplex(this.real - subtrahend.getReal(), this.imaginary - subtrahend.getImaginary());
/*  247:     */   }
/*  248:     */   
/*  249:     */   public Complex subtract(double subtrahend)
/*  250:     */   {
/*  251: 546 */     if ((this.isNaN) || (Double.isNaN(subtrahend))) {
/*  252: 547 */       return NaN;
/*  253:     */     }
/*  254: 549 */     return createComplex(this.real - subtrahend, this.imaginary);
/*  255:     */   }
/*  256:     */   
/*  257:     */   public Complex acos()
/*  258:     */   {
/*  259: 569 */     if (this.isNaN) {
/*  260: 570 */       return NaN;
/*  261:     */     }
/*  262: 573 */     return add(sqrt1z().multiply(I)).log().multiply(I.negate());
/*  263:     */   }
/*  264:     */   
/*  265:     */   public Complex asin()
/*  266:     */   {
/*  267: 594 */     if (this.isNaN) {
/*  268: 595 */       return NaN;
/*  269:     */     }
/*  270: 598 */     return sqrt1z().add(multiply(I)).log().multiply(I.negate());
/*  271:     */   }
/*  272:     */   
/*  273:     */   public Complex atan()
/*  274:     */   {
/*  275: 619 */     if (this.isNaN) {
/*  276: 620 */       return NaN;
/*  277:     */     }
/*  278: 623 */     return add(I).divide(I.subtract(this)).log().multiply(I.divide(createComplex(2.0D, 0.0D)));
/*  279:     */   }
/*  280:     */   
/*  281:     */   public Complex cos()
/*  282:     */   {
/*  283: 660 */     if (this.isNaN) {
/*  284: 661 */       return NaN;
/*  285:     */     }
/*  286: 664 */     return createComplex(FastMath.cos(this.real) * FastMath.cosh(this.imaginary), -FastMath.sin(this.real) * FastMath.sinh(this.imaginary));
/*  287:     */   }
/*  288:     */   
/*  289:     */   public Complex cosh()
/*  290:     */   {
/*  291: 700 */     if (this.isNaN) {
/*  292: 701 */       return NaN;
/*  293:     */     }
/*  294: 704 */     return createComplex(FastMath.cosh(this.real) * FastMath.cos(this.imaginary), FastMath.sinh(this.real) * FastMath.sin(this.imaginary));
/*  295:     */   }
/*  296:     */   
/*  297:     */   public Complex exp()
/*  298:     */   {
/*  299: 741 */     if (this.isNaN) {
/*  300: 742 */       return NaN;
/*  301:     */     }
/*  302: 745 */     double expReal = FastMath.exp(this.real);
/*  303: 746 */     return createComplex(expReal * FastMath.cos(this.imaginary), expReal * FastMath.sin(this.imaginary));
/*  304:     */   }
/*  305:     */   
/*  306:     */   public Complex log()
/*  307:     */   {
/*  308: 786 */     if (this.isNaN) {
/*  309: 787 */       return NaN;
/*  310:     */     }
/*  311: 790 */     return createComplex(FastMath.log(abs()), FastMath.atan2(this.imaginary, this.real));
/*  312:     */   }
/*  313:     */   
/*  314:     */   public Complex pow(Complex x)
/*  315:     */     throws NullArgumentException
/*  316:     */   {
/*  317: 816 */     MathUtils.checkNotNull(x);
/*  318: 817 */     return log().multiply(x).exp();
/*  319:     */   }
/*  320:     */   
/*  321:     */   public Complex pow(double x)
/*  322:     */   {
/*  323: 828 */     return log().multiply(x).exp();
/*  324:     */   }
/*  325:     */   
/*  326:     */   public Complex sin()
/*  327:     */   {
/*  328: 864 */     if (this.isNaN) {
/*  329: 865 */       return NaN;
/*  330:     */     }
/*  331: 868 */     return createComplex(FastMath.sin(this.real) * FastMath.cosh(this.imaginary), FastMath.cos(this.real) * FastMath.sinh(this.imaginary));
/*  332:     */   }
/*  333:     */   
/*  334:     */   public Complex sinh()
/*  335:     */   {
/*  336: 904 */     if (this.isNaN) {
/*  337: 905 */       return NaN;
/*  338:     */     }
/*  339: 908 */     return createComplex(FastMath.sinh(this.real) * FastMath.cos(this.imaginary), FastMath.cosh(this.real) * FastMath.sin(this.imaginary));
/*  340:     */   }
/*  341:     */   
/*  342:     */   public Complex sqrt()
/*  343:     */   {
/*  344: 947 */     if (this.isNaN) {
/*  345: 948 */       return NaN;
/*  346:     */     }
/*  347: 951 */     if ((this.real == 0.0D) && (this.imaginary == 0.0D)) {
/*  348: 952 */       return createComplex(0.0D, 0.0D);
/*  349:     */     }
/*  350: 955 */     double t = FastMath.sqrt((FastMath.abs(this.real) + abs()) / 2.0D);
/*  351: 956 */     if (this.real >= 0.0D) {
/*  352: 957 */       return createComplex(t, this.imaginary / (2.0D * t));
/*  353:     */     }
/*  354: 959 */     return createComplex(FastMath.abs(this.imaginary) / (2.0D * t), FastMath.copySign(1.0D, this.imaginary) * t);
/*  355:     */   }
/*  356:     */   
/*  357:     */   public Complex sqrt1z()
/*  358:     */   {
/*  359: 982 */     return createComplex(1.0D, 0.0D).subtract(multiply(this)).sqrt();
/*  360:     */   }
/*  361:     */   
/*  362:     */   public Complex tan()
/*  363:     */   {
/*  364:1018 */     if ((this.isNaN) || (Double.isInfinite(this.real))) {
/*  365:1019 */       return NaN;
/*  366:     */     }
/*  367:1021 */     if (this.imaginary > 20.0D) {
/*  368:1022 */       return createComplex(0.0D, 1.0D);
/*  369:     */     }
/*  370:1024 */     if (this.imaginary < -20.0D) {
/*  371:1025 */       return createComplex(0.0D, -1.0D);
/*  372:     */     }
/*  373:1028 */     double real2 = 2.0D * this.real;
/*  374:1029 */     double imaginary2 = 2.0D * this.imaginary;
/*  375:1030 */     double d = FastMath.cos(real2) + FastMath.cosh(imaginary2);
/*  376:     */     
/*  377:1032 */     return createComplex(FastMath.sin(real2) / d, FastMath.sinh(imaginary2) / d);
/*  378:     */   }
/*  379:     */   
/*  380:     */   public Complex tanh()
/*  381:     */   {
/*  382:1069 */     if ((this.isNaN) || (Double.isInfinite(this.imaginary))) {
/*  383:1070 */       return NaN;
/*  384:     */     }
/*  385:1072 */     if (this.real > 20.0D) {
/*  386:1073 */       return createComplex(1.0D, 0.0D);
/*  387:     */     }
/*  388:1075 */     if (this.real < -20.0D) {
/*  389:1076 */       return createComplex(-1.0D, 0.0D);
/*  390:     */     }
/*  391:1078 */     double real2 = 2.0D * this.real;
/*  392:1079 */     double imaginary2 = 2.0D * this.imaginary;
/*  393:1080 */     double d = FastMath.cosh(real2) + FastMath.cos(imaginary2);
/*  394:     */     
/*  395:1082 */     return createComplex(FastMath.sinh(real2) / d, FastMath.sin(imaginary2) / d);
/*  396:     */   }
/*  397:     */   
/*  398:     */   public double getArgument()
/*  399:     */   {
/*  400:1106 */     return FastMath.atan2(getImaginary(), getReal());
/*  401:     */   }
/*  402:     */   
/*  403:     */   public List<Complex> nthRoot(int n)
/*  404:     */   {
/*  405:1133 */     if (n <= 0) {
/*  406:1134 */       throw new NotPositiveException(LocalizedFormats.CANNOT_COMPUTE_NTH_ROOT_FOR_NEGATIVE_N, Integer.valueOf(n));
/*  407:     */     }
/*  408:1138 */     List<Complex> result = new ArrayList();
/*  409:1140 */     if (this.isNaN)
/*  410:     */     {
/*  411:1141 */       result.add(NaN);
/*  412:1142 */       return result;
/*  413:     */     }
/*  414:1144 */     if (isInfinite())
/*  415:     */     {
/*  416:1145 */       result.add(INF);
/*  417:1146 */       return result;
/*  418:     */     }
/*  419:1150 */     double nthRootOfAbs = FastMath.pow(abs(), 1.0D / n);
/*  420:     */     
/*  421:     */ 
/*  422:1153 */     double nthPhi = getArgument() / n;
/*  423:1154 */     double slice = 6.283185307179586D / n;
/*  424:1155 */     double innerPart = nthPhi;
/*  425:1156 */     for (int k = 0; k < n; k++)
/*  426:     */     {
/*  427:1158 */       double realPart = nthRootOfAbs * FastMath.cos(innerPart);
/*  428:1159 */       double imaginaryPart = nthRootOfAbs * FastMath.sin(innerPart);
/*  429:1160 */       result.add(createComplex(realPart, imaginaryPart));
/*  430:1161 */       innerPart += slice;
/*  431:     */     }
/*  432:1164 */     return result;
/*  433:     */   }
/*  434:     */   
/*  435:     */   protected Complex createComplex(double realPart, double imaginaryPart)
/*  436:     */   {
/*  437:1178 */     return new Complex(realPart, imaginaryPart);
/*  438:     */   }
/*  439:     */   
/*  440:     */   public static Complex valueOf(double realPart, double imaginaryPart)
/*  441:     */   {
/*  442:1190 */     if ((Double.isNaN(realPart)) || (Double.isNaN(imaginaryPart))) {
/*  443:1192 */       return NaN;
/*  444:     */     }
/*  445:1194 */     return new Complex(realPart, imaginaryPart);
/*  446:     */   }
/*  447:     */   
/*  448:     */   public static Complex valueOf(double realPart)
/*  449:     */   {
/*  450:1204 */     if (Double.isNaN(realPart)) {
/*  451:1205 */       return NaN;
/*  452:     */     }
/*  453:1207 */     return new Complex(realPart);
/*  454:     */   }
/*  455:     */   
/*  456:     */   protected final Object readResolve()
/*  457:     */   {
/*  458:1219 */     return createComplex(this.real, this.imaginary);
/*  459:     */   }
/*  460:     */   
/*  461:     */   public ComplexField getField()
/*  462:     */   {
/*  463:1224 */     return ComplexField.getInstance();
/*  464:     */   }
/*  465:     */   
/*  466:     */   public String toString()
/*  467:     */   {
/*  468:1230 */     return "(" + this.real + ", " + this.imaginary + ")";
/*  469:     */   }
/*  470:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.complex.Complex
 * JD-Core Version:    0.7.0.1
 */
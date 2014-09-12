/*    1:     */ package org.apache.commons.math3.dfp;
/*    2:     */ 
/*    3:     */ import java.util.Arrays;
/*    4:     */ import org.apache.commons.math3.FieldElement;
/*    5:     */ 
/*    6:     */ public class Dfp
/*    7:     */   implements FieldElement<Dfp>
/*    8:     */ {
/*    9:     */   public static final int RADIX = 10000;
/*   10:     */   public static final int MIN_EXP = -32767;
/*   11:     */   public static final int MAX_EXP = 32768;
/*   12:     */   public static final int ERR_SCALE = 32760;
/*   13:     */   public static final byte FINITE = 0;
/*   14:     */   public static final byte INFINITE = 1;
/*   15:     */   public static final byte SNAN = 2;
/*   16:     */   public static final byte QNAN = 3;
/*   17:     */   private static final String NAN_STRING = "NaN";
/*   18:     */   private static final String POS_INFINITY_STRING = "Infinity";
/*   19:     */   private static final String NEG_INFINITY_STRING = "-Infinity";
/*   20:     */   private static final String ADD_TRAP = "add";
/*   21:     */   private static final String MULTIPLY_TRAP = "multiply";
/*   22:     */   private static final String DIVIDE_TRAP = "divide";
/*   23:     */   private static final String SQRT_TRAP = "sqrt";
/*   24:     */   private static final String ALIGN_TRAP = "align";
/*   25:     */   private static final String TRUNC_TRAP = "trunc";
/*   26:     */   private static final String NEXT_AFTER_TRAP = "nextAfter";
/*   27:     */   private static final String LESS_THAN_TRAP = "lessThan";
/*   28:     */   private static final String GREATER_THAN_TRAP = "greaterThan";
/*   29:     */   private static final String NEW_INSTANCE_TRAP = "newInstance";
/*   30:     */   protected int[] mant;
/*   31:     */   protected byte sign;
/*   32:     */   protected static int exp;
/*   33:     */   protected byte nans;
/*   34:     */   private final DfpField field;
/*   35:     */   
/*   36:     */   protected Dfp(DfpField field)
/*   37:     */   {
/*   38: 182 */     this.mant = new int[field.getRadixDigits()];
/*   39: 183 */     this.sign = 1;
/*   40: 184 */     this.exp = 0;
/*   41: 185 */     this.nans = 0;
/*   42: 186 */     this.field = field;
/*   43:     */   }
/*   44:     */   
/*   45:     */   protected Dfp(DfpField field, byte x)
/*   46:     */   {
/*   47: 194 */     this(field, (double)x);
/*   48:     */   }
/*   49:     */   
/*   50:     */   protected Dfp(DfpField field, int x)
/*   51:     */   {
/*   52: 202 */     this(field, (double)x);
/*   53:     */   }
/*   54:     */   
/*   55:     */   protected Dfp(DfpField field, long x)
/*   56:     */   {
/*   57: 212 */     this.mant = new int[field.getRadixDigits()];
/*   58: 213 */     this.nans = 0;
/*   59: 214 */     this.field = field;
/*   60:     */     
/*   61: 216 */     boolean isLongMin = false;
/*   62: 217 */     if (x == -9223372036854775808L)
/*   63:     */     {
/*   64: 220 */       isLongMin = true;
/*   65: 221 */       x += 1L;
/*   66:     */     }
/*   67: 225 */     if (x < 0L)
/*   68:     */     {
/*   69: 226 */       this.sign = -1;
/*   70: 227 */       x = -x;
/*   71:     */     }
/*   72:     */     else
/*   73:     */     {
/*   74: 229 */       this.sign = 1;
/*   75:     */     }
/*   76: 232 */     this.exp = 0;
/*   77: 233 */     while (x != 0L)
/*   78:     */     {
/*   79: 234 */       System.arraycopy(this.mant, this.mant.length - this.exp, this.mant, this.mant.length - 1 - this.exp, this.exp);
/*   80: 235 */       this.mant[(this.mant.length - 1)] = ((int)(x % 10000L));
/*   81: 236 */       x /= 10000L;
/*   82: 237 */       this.exp += 1;
/*   83:     */     }
/*   84: 240 */     if (isLongMin) {
/*   85: 243 */       for (int i = 0; i < this.mant.length - 1; i++) {
/*   86: 244 */         if (this.mant[i] != 0)
/*   87:     */         {
/*   88: 245 */           this.mant[i] += 1;
/*   89: 246 */           break;
/*   90:     */         }
/*   91:     */       }
/*   92:     */     }
/*   93:     */   }
/*   94:     */   
/*   95:     */   protected Dfp(DfpField field, double x)
/*   96:     */   {
/*   97: 259 */     this.mant = new int[field.getRadixDigits()];
/*   98: 260 */     this.sign = 1;
/*   99: 261 */     this.exp = 0;
/*  100: 262 */     this.nans = 0;
/*  101: 263 */     this.field = field;
/*  102:     */     
/*  103: 265 */     long bits = Double.doubleToLongBits(x);
/*  104: 266 */     long mantissa = bits & 0xFFFFFFFF;
/*  105: 267 */     int exponent = (int)((bits & 0x0) >> 52) - 1023;
/*  106: 269 */     if (exponent == -1023)
/*  107:     */     {
/*  108: 271 */       if (x == 0.0D)
/*  109:     */       {
/*  110: 273 */         if ((bits & 0x0) != 0L) {
/*  111: 274 */           this.sign = -1;
/*  112:     */         }
/*  113: 276 */         return;
/*  114:     */       }
/*  115: 279 */       exponent++;
/*  116: 282 */       while ((mantissa & 0x0) == 0L)
/*  117:     */       {
/*  118: 283 */         exponent--;
/*  119: 284 */         mantissa <<= 1;
/*  120:     */       }
/*  121: 286 */       mantissa &= 0xFFFFFFFF;
/*  122:     */     }
/*  123: 289 */     if (exponent == 1024)
/*  124:     */     {
/*  125: 291 */       if (x != x)
/*  126:     */       {
/*  127: 292 */         this.sign = 1;
/*  128: 293 */         this.nans = 3;
/*  129:     */       }
/*  130: 294 */       else if (x < 0.0D)
/*  131:     */       {
/*  132: 295 */         this.sign = -1;
/*  133: 296 */         this.nans = 1;
/*  134:     */       }
/*  135:     */       else
/*  136:     */       {
/*  137: 298 */         this.sign = 1;
/*  138: 299 */         this.nans = 1;
/*  139:     */       }
/*  140: 301 */       return;
/*  141:     */     }
/*  142: 304 */     Dfp xdfp = new Dfp(field, mantissa);
/*  143: 305 */     xdfp = xdfp.divide(new Dfp(field, 4503599627370496L)).add(field.getOne());
/*  144: 306 */     xdfp = xdfp.multiply(DfpMath.pow(field.getTwo(), exponent));
/*  145: 308 */     if ((bits & 0x0) != 0L) {
/*  146: 309 */       xdfp = xdfp.negate();
/*  147:     */     }
/*  148: 312 */     System.arraycopy(xdfp.mant, 0, this.mant, 0, this.mant.length);
/*  149: 313 */     this.sign = xdfp.sign;
/*  150: 314 */     this.exp = xdfp.exp;
/*  151: 315 */     this.nans = xdfp.nans;
/*  152:     */   }
/*  153:     */   
/*  154:     */   public Dfp(Dfp d)
/*  155:     */   {
/*  156: 323 */     this.mant = ((int[])d.mant.clone());
/*  157: 324 */     this.sign = d.sign;
/*  158: 325 */     this.exp = d.exp;
/*  159: 326 */     this.nans = d.nans;
/*  160: 327 */     this.field = d.field;
/*  161:     */   }
/*  162:     */   
/*  163:     */   protected Dfp(DfpField field, String s)
/*  164:     */   {
/*  165: 337 */     this.mant = new int[field.getRadixDigits()];
/*  166: 338 */     this.sign = 1;
/*  167: 339 */     this.exp = 0;
/*  168: 340 */     this.nans = 0;
/*  169: 341 */     this.field = field;
/*  170:     */     
/*  171: 343 */     boolean decimalFound = false;
/*  172: 344 */     int rsize = 4;
/*  173: 345 */     int offset = 4;
/*  174: 346 */     char[] striped = new char[getRadixDigits() * 4 + 8];
/*  175: 349 */     if (s.equals("Infinity"))
/*  176:     */     {
/*  177: 350 */       this.sign = 1;
/*  178: 351 */       this.nans = 1;
/*  179: 352 */       return;
/*  180:     */     }
/*  181: 355 */     if (s.equals("-Infinity"))
/*  182:     */     {
/*  183: 356 */       this.sign = -1;
/*  184: 357 */       this.nans = 1;
/*  185: 358 */       return;
/*  186:     */     }
/*  187: 361 */     if (s.equals("NaN"))
/*  188:     */     {
/*  189: 362 */       this.sign = 1;
/*  190: 363 */       this.nans = 3;
/*  191: 364 */       return;
/*  192:     */     }
/*  193: 368 */     int p = s.indexOf("e");
/*  194: 369 */     if (p == -1) {
/*  195: 370 */       p = s.indexOf("E");
/*  196:     */     }
/*  197: 374 */     int sciexp = 0;
/*  198:     */     String fpdecimal = null;
/*  199: 375 */     if (p != -1)
/*  200:     */     {
/*  201: 377 */       String fpdecimal1 = s.substring(0, p);
/*  202: 378 */       String fpexp = s.substring(p + 1);
/*  203: 379 */       boolean negative = false;
/*  204: 381 */       for (int i = 0; i < fpexp.length(); i++) {
/*  205: 383 */         if (fpexp.charAt(i) == '-') {
/*  206: 385 */           negative = true;
/*  207: 388 */         } else if ((fpexp.charAt(i) >= '0') && (fpexp.charAt(i) <= '9')) {
/*  208: 389 */           sciexp = sciexp * 10 + fpexp.charAt(i) - 48;
/*  209:     */         }
/*  210:     */       }
/*  211: 393 */       if (negative) {
/*  212: 394 */         sciexp = -sciexp;
/*  213:     */       }
/*  214:     */     }
/*  215:     */     else
/*  216:     */     {
/*  217: 398 */       fpdecimal = s;
/*  218:     */     }
/*  219: 402 */     if (fpdecimal.indexOf("-") != -1) {
/*  220: 403 */       this.sign = -1;
/*  221:     */     }
/*  222: 407 */     p = 0;
/*  223:     */     
/*  224:     */ 
/*  225: 410 */     int decimalPos = 0;
/*  226: 412 */     while ((fpdecimal.charAt(p) < '1') || (fpdecimal.charAt(p) > '9'))
/*  227:     */     {
/*  228: 416 */       if ((decimalFound) && (fpdecimal.charAt(p) == '0')) {
/*  229: 417 */         decimalPos--;
/*  230:     */       }
/*  231: 420 */       if (fpdecimal.charAt(p) == '.') {
/*  232: 421 */         decimalFound = true;
/*  233:     */       }
/*  234: 424 */       p++;
/*  235: 426 */       if (p == fpdecimal.length()) {
/*  236:     */         break;
/*  237:     */       }
/*  238:     */     }
/*  239: 432 */     int q = 4;
/*  240: 433 */     striped[0] = '0';
/*  241: 434 */     striped[1] = '0';
/*  242: 435 */     striped[2] = '0';
/*  243: 436 */     striped[3] = '0';
/*  244: 437 */     int significantDigits = 0;
/*  245: 439 */     while (p != fpdecimal.length())
/*  246:     */     {
/*  247: 444 */       if (q == this.mant.length * 4 + 4 + 1) {
/*  248:     */         break;
/*  249:     */       }
/*  250: 448 */       if (fpdecimal.charAt(p) == '.')
/*  251:     */       {
/*  252: 449 */         decimalFound = true;
/*  253: 450 */         decimalPos = significantDigits;
/*  254: 451 */         p++;
/*  255:     */       }
/*  256: 455 */       else if ((fpdecimal.charAt(p) < '0') || (fpdecimal.charAt(p) > '9'))
/*  257:     */       {
/*  258: 456 */         p++;
/*  259:     */       }
/*  260:     */       else
/*  261:     */       {
/*  262: 460 */         striped[q] = fpdecimal.charAt(p);
/*  263: 461 */         q++;
/*  264: 462 */         p++;
/*  265: 463 */         significantDigits++;
/*  266:     */       }
/*  267:     */     }
/*  268: 468 */     if ((decimalFound) && (q != 4)) {
/*  269:     */       for (;;)
/*  270:     */       {
/*  271: 470 */         q--;
/*  272: 471 */         if (q == 4) {
/*  273:     */           break;
/*  274:     */         }
/*  275: 474 */         if (striped[q] != '0') {
/*  276:     */           break;
/*  277:     */         }
/*  278: 475 */         significantDigits--;
/*  279:     */       }
/*  280:     */     }
/*  281: 483 */     if ((decimalFound) && (significantDigits == 0)) {
/*  282: 484 */       decimalPos = 0;
/*  283:     */     }
/*  284: 488 */     if (!decimalFound) {
/*  285: 489 */       decimalPos = q - 4;
/*  286:     */     }
/*  287: 493 */     q = 4;
/*  288: 494 */     p = significantDigits - 1 + 4;
/*  289: 496 */     while ((p > q) && 
/*  290: 497 */       (striped[p] == '0')) {
/*  291: 500 */       p--;
/*  292:     */     }
/*  293: 504 */     int i = (400 - decimalPos - sciexp % 4) % 4;
/*  294: 505 */     q -= i;
/*  295: 506 */     decimalPos += i;
/*  296: 509 */     while (p - q < this.mant.length * 4) {
/*  297: 510 */       for (i = 0; i < 4; i++) {
/*  298: 511 */         striped[(++p)] = '0';
/*  299:     */       }
/*  300:     */     }
/*  301: 517 */     for (i = this.mant.length - 1; i >= 0; i--)
/*  302:     */     {
/*  303: 518 */       this.mant[i] = ((striped[q] - '0') * 1000 + (striped[(q + 1)] - '0') * 100 + (striped[(q + 2)] - '0') * 10 + (striped[(q + 3)] - '0'));
/*  304:     */       
/*  305:     */ 
/*  306:     */ 
/*  307: 522 */       q += 4;
/*  308:     */     }
/*  309: 526 */     this.exp = ((decimalPos + sciexp) / 4);
/*  310: 528 */     if (q < striped.length) {
/*  311: 530 */       round((striped[q] - '0') * 1000);
/*  312:     */     }
/*  313:     */   }
/*  314:     */   
/*  315:     */   protected Dfp(DfpField field, byte sign, byte nans)
/*  316:     */   {
/*  317: 542 */     this.field = field;
/*  318: 543 */     this.mant = new int[field.getRadixDigits()];
/*  319: 544 */     this.sign = sign;
/*  320: 545 */     this.exp = 0;
/*  321: 546 */     this.nans = nans;
/*  322:     */   }
/*  323:     */   
/*  324:     */   public Dfp newInstance()
/*  325:     */   {
/*  326: 554 */     return new Dfp(getField());
/*  327:     */   }
/*  328:     */   
/*  329:     */   public Dfp newInstance(byte x)
/*  330:     */   {
/*  331: 562 */     return new Dfp(getField(), x);
/*  332:     */   }
/*  333:     */   
/*  334:     */   public Dfp newInstance(int x)
/*  335:     */   {
/*  336: 570 */     return new Dfp(getField(), x);
/*  337:     */   }
/*  338:     */   
/*  339:     */   public Dfp newInstance(long x)
/*  340:     */   {
/*  341: 578 */     return new Dfp(getField(), x);
/*  342:     */   }
/*  343:     */   
/*  344:     */   public Dfp newInstance(double x)
/*  345:     */   {
/*  346: 586 */     return new Dfp(getField(), x);
/*  347:     */   }
/*  348:     */   
/*  349:     */   public Dfp newInstance(Dfp d)
/*  350:     */   {
/*  351: 597 */     if (this.field.getRadixDigits() != d.field.getRadixDigits())
/*  352:     */     {
/*  353: 598 */       this.field.setIEEEFlagsBits(1);
/*  354: 599 */       Dfp result = newInstance(getZero());
/*  355: 600 */       result.nans = 3;
/*  356: 601 */       return dotrap(1, "newInstance", d, result);
/*  357:     */     }
/*  358: 604 */     return new Dfp(d);
/*  359:     */   }
/*  360:     */   
/*  361:     */   public Dfp newInstance(String s)
/*  362:     */   {
/*  363: 614 */     return new Dfp(this.field, s);
/*  364:     */   }
/*  365:     */   
/*  366:     */   public Dfp newInstance(byte sig, byte code)
/*  367:     */   {
/*  368: 624 */     return this.field.newDfp(sig, code);
/*  369:     */   }
/*  370:     */   
/*  371:     */   public DfpField getField()
/*  372:     */   {
/*  373: 635 */     return this.field;
/*  374:     */   }
/*  375:     */   
/*  376:     */   public int getRadixDigits()
/*  377:     */   {
/*  378: 642 */     return this.field.getRadixDigits();
/*  379:     */   }
/*  380:     */   
/*  381:     */   public Dfp getZero()
/*  382:     */   {
/*  383: 649 */     return this.field.getZero();
/*  384:     */   }
/*  385:     */   
/*  386:     */   public Dfp getOne()
/*  387:     */   {
/*  388: 656 */     return this.field.getOne();
/*  389:     */   }
/*  390:     */   
/*  391:     */   public Dfp getTwo()
/*  392:     */   {
/*  393: 663 */     return this.field.getTwo();
/*  394:     */   }
/*  395:     */   
/*  396:     */   protected void shiftLeft()
/*  397:     */   {
/*  398: 669 */     for (int i = this.mant.length - 1; i > 0; i--) {
/*  399: 670 */       this.mant[i] = this.mant[(i - 1)];
/*  400:     */     }
/*  401: 672 */     this.mant[0] = 0;
/*  402: 673 */     this.exp -= 1;
/*  403:     */   }
/*  404:     */   
/*  405:     */   protected void shiftRight()
/*  406:     */   {
/*  407: 681 */     for (int i = 0; i < this.mant.length - 1; i++) {
/*  408: 682 */       this.mant[i] = this.mant[(i + 1)];
/*  409:     */     }
/*  410: 684 */     this.mant[(this.mant.length - 1)] = 0;
/*  411: 685 */     this.exp += 1;
/*  412:     */   }
/*  413:     */   
/*  414:     */   protected int align(int e)
/*  415:     */   {
/*  416: 697 */     int lostdigit = 0;
/*  417: 698 */     boolean inexact = false;
/*  418:     */     
/*  419: 700 */     int diff = this.exp - e;
/*  420:     */     
/*  421: 702 */     int adiff = diff;
/*  422: 703 */     if (adiff < 0) {
/*  423: 704 */       adiff = -adiff;
/*  424:     */     }
/*  425: 707 */     if (diff == 0) {
/*  426: 708 */       return 0;
/*  427:     */     }
/*  428: 711 */     if (adiff > this.mant.length + 1)
/*  429:     */     {
/*  430: 713 */       Arrays.fill(this.mant, 0);
/*  431: 714 */       this.exp = e;
/*  432:     */       
/*  433: 716 */       this.field.setIEEEFlagsBits(16);
/*  434: 717 */       dotrap(16, "align", this, this);
/*  435:     */       
/*  436: 719 */       return 0;
/*  437:     */     }
/*  438: 722 */     for (int i = 0; i < adiff; i++) {
/*  439: 723 */       if (diff < 0)
/*  440:     */       {
/*  441: 728 */         if (lostdigit != 0) {
/*  442: 729 */           inexact = true;
/*  443:     */         }
/*  444: 732 */         lostdigit = this.mant[0];
/*  445:     */         
/*  446: 734 */         shiftRight();
/*  447:     */       }
/*  448:     */       else
/*  449:     */       {
/*  450: 736 */         shiftLeft();
/*  451:     */       }
/*  452:     */     }
/*  453: 740 */     if (inexact)
/*  454:     */     {
/*  455: 741 */       this.field.setIEEEFlagsBits(16);
/*  456: 742 */       dotrap(16, "align", this, this);
/*  457:     */     }
/*  458: 745 */     return lostdigit;
/*  459:     */   }
/*  460:     */   
/*  461:     */   public boolean lessThan(Dfp x)
/*  462:     */   {
/*  463: 756 */     if (this.field.getRadixDigits() != x.field.getRadixDigits())
/*  464:     */     {
/*  465: 757 */       this.field.setIEEEFlagsBits(1);
/*  466: 758 */       Dfp result = newInstance(getZero());
/*  467: 759 */       result.nans = 3;
/*  468: 760 */       dotrap(1, "lessThan", x, result);
/*  469: 761 */       return false;
/*  470:     */     }
/*  471: 765 */     if ((isNaN()) || (x.isNaN()))
/*  472:     */     {
/*  473: 766 */       this.field.setIEEEFlagsBits(1);
/*  474: 767 */       dotrap(1, "lessThan", x, newInstance(getZero()));
/*  475: 768 */       return false;
/*  476:     */     }
/*  477: 771 */     return compare(this, x) < 0;
/*  478:     */   }
/*  479:     */   
/*  480:     */   public boolean greaterThan(Dfp x)
/*  481:     */   {
/*  482: 781 */     if (this.field.getRadixDigits() != x.field.getRadixDigits())
/*  483:     */     {
/*  484: 782 */       this.field.setIEEEFlagsBits(1);
/*  485: 783 */       Dfp result = newInstance(getZero());
/*  486: 784 */       result.nans = 3;
/*  487: 785 */       dotrap(1, "greaterThan", x, result);
/*  488: 786 */       return false;
/*  489:     */     }
/*  490: 790 */     if ((isNaN()) || (x.isNaN()))
/*  491:     */     {
/*  492: 791 */       this.field.setIEEEFlagsBits(1);
/*  493: 792 */       dotrap(1, "greaterThan", x, newInstance(getZero()));
/*  494: 793 */       return false;
/*  495:     */     }
/*  496: 796 */     return compare(this, x) > 0;
/*  497:     */   }
/*  498:     */   
/*  499:     */   public boolean negativeOrNull()
/*  500:     */   {
/*  501: 804 */     if (isNaN())
/*  502:     */     {
/*  503: 805 */       this.field.setIEEEFlagsBits(1);
/*  504: 806 */       dotrap(1, "lessThan", this, newInstance(getZero()));
/*  505: 807 */       return false;
/*  506:     */     }
/*  507: 810 */     return (this.sign < 0) || ((this.mant[(this.mant.length - 1)] == 0) && (!isInfinite()));
/*  508:     */   }
/*  509:     */   
/*  510:     */   public boolean strictlyNegative()
/*  511:     */   {
/*  512: 819 */     if (isNaN())
/*  513:     */     {
/*  514: 820 */       this.field.setIEEEFlagsBits(1);
/*  515: 821 */       dotrap(1, "lessThan", this, newInstance(getZero()));
/*  516: 822 */       return false;
/*  517:     */     }
/*  518: 825 */     return (this.sign < 0) && ((this.mant[(this.mant.length - 1)] != 0) || (isInfinite()));
/*  519:     */   }
/*  520:     */   
/*  521:     */   public boolean positiveOrNull()
/*  522:     */   {
/*  523: 834 */     if (isNaN())
/*  524:     */     {
/*  525: 835 */       this.field.setIEEEFlagsBits(1);
/*  526: 836 */       dotrap(1, "lessThan", this, newInstance(getZero()));
/*  527: 837 */       return false;
/*  528:     */     }
/*  529: 840 */     return (this.sign > 0) || ((this.mant[(this.mant.length - 1)] == 0) && (!isInfinite()));
/*  530:     */   }
/*  531:     */   
/*  532:     */   public boolean strictlyPositive()
/*  533:     */   {
/*  534: 849 */     if (isNaN())
/*  535:     */     {
/*  536: 850 */       this.field.setIEEEFlagsBits(1);
/*  537: 851 */       dotrap(1, "lessThan", this, newInstance(getZero()));
/*  538: 852 */       return false;
/*  539:     */     }
/*  540: 855 */     return (this.sign > 0) && ((this.mant[(this.mant.length - 1)] != 0) || (isInfinite()));
/*  541:     */   }
/*  542:     */   
/*  543:     */   public Dfp abs()
/*  544:     */   {
/*  545: 863 */     Dfp result = newInstance(this);
/*  546: 864 */     result.sign = 1;
/*  547: 865 */     return result;
/*  548:     */   }
/*  549:     */   
/*  550:     */   public boolean isInfinite()
/*  551:     */   {
/*  552: 872 */     return this.nans == 1;
/*  553:     */   }
/*  554:     */   
/*  555:     */   public boolean isNaN()
/*  556:     */   {
/*  557: 879 */     return (this.nans == 3) || (this.nans == 2);
/*  558:     */   }
/*  559:     */   
/*  560:     */   public boolean isZero()
/*  561:     */   {
/*  562: 887 */     if (isNaN())
/*  563:     */     {
/*  564: 888 */       this.field.setIEEEFlagsBits(1);
/*  565: 889 */       dotrap(1, "lessThan", this, newInstance(getZero()));
/*  566: 890 */       return false;
/*  567:     */     }
/*  568: 893 */     return (this.mant[(this.mant.length - 1)] == 0) && (!isInfinite());
/*  569:     */   }
/*  570:     */   
/*  571:     */   public boolean equals(Object other)
/*  572:     */   {
/*  573: 904 */     if ((other instanceof Dfp))
/*  574:     */     {
/*  575: 905 */       Dfp x = (Dfp)other;
/*  576: 906 */       if ((isNaN()) || (x.isNaN()) || (this.field.getRadixDigits() != x.field.getRadixDigits())) {
/*  577: 907 */         return false;
/*  578:     */       }
/*  579: 910 */       return compare(this, x) == 0;
/*  580:     */     }
/*  581: 913 */     return false;
/*  582:     */   }
/*  583:     */   
/*  584:     */   public int hashCode()
/*  585:     */   {
/*  586: 923 */     return 17 + (this.sign << 8) + (this.nans << 16) + this.exp + Arrays.hashCode(this.mant);
/*  587:     */   }
/*  588:     */   
/*  589:     */   public boolean unequal(Dfp x)
/*  590:     */   {
/*  591: 931 */     if ((isNaN()) || (x.isNaN()) || (this.field.getRadixDigits() != x.field.getRadixDigits())) {
/*  592: 932 */       return false;
/*  593:     */     }
/*  594: 935 */     return (greaterThan(x)) || (lessThan(x));
/*  595:     */   }
/*  596:     */   
/*  597:     */   private static int compare(Dfp a, Dfp b)
/*  598:     */   {
/*  599: 946 */     if ((a.mant[(a.mant.length - 1)] == 0) && (b.mant[(b.mant.length - 1)] == 0) && (a.nans == 0) && (b.nans == 0)) {
/*  600: 948 */       return 0;
/*  601:     */     }
/*  602: 951 */     if (a.sign != b.sign)
/*  603:     */     {
/*  604: 952 */       if (a.sign == -1) {
/*  605: 953 */         return -1;
/*  606:     */       }
/*  607: 955 */       return 1;
/*  608:     */     }
/*  609: 960 */     if ((a.nans == 1) && (b.nans == 0)) {
/*  610: 961 */       return a.sign;
/*  611:     */     }
/*  612: 964 */     if ((a.nans == 0) && (b.nans == 1)) {
/*  613: 965 */       return -b.sign;
/*  614:     */     }
/*  615: 968 */     if ((a.nans == 1) && (b.nans == 1)) {
/*  616: 969 */       return 0;
/*  617:     */     }
/*  618: 973 */     if ((b.mant[(b.mant.length - 1)] != 0) && (a.mant[(b.mant.length - 1)] != 0))
/*  619:     */     {
/*  620: 974 */       if (a.exp < b.exp) {
/*  621: 975 */         return -a.sign;
/*  622:     */       }
/*  623: 978 */       if (a.exp > b.exp) {
/*  624: 979 */         return a.sign;
/*  625:     */       }
/*  626:     */     }
/*  627: 984 */     for (int i = a.mant.length - 1; i >= 0; i--)
/*  628:     */     {
/*  629: 985 */       if (a.mant[i] > b.mant[i]) {
/*  630: 986 */         return a.sign;
/*  631:     */       }
/*  632: 989 */       if (a.mant[i] < b.mant[i]) {
/*  633: 990 */         return -a.sign;
/*  634:     */       }
/*  635:     */     }
/*  636: 994 */     return 0;
/*  637:     */   }
/*  638:     */   
/*  639:     */   public Dfp rint()
/*  640:     */   {
/*  641:1004 */     return trunc(DfpField.RoundingMode.ROUND_HALF_EVEN);
/*  642:     */   }
/*  643:     */   
/*  644:     */   public Dfp floor()
/*  645:     */   {
/*  646:1012 */     return trunc(DfpField.RoundingMode.ROUND_FLOOR);
/*  647:     */   }
/*  648:     */   
/*  649:     */   public Dfp ceil()
/*  650:     */   {
/*  651:1020 */     return trunc(DfpField.RoundingMode.ROUND_CEIL);
/*  652:     */   }
/*  653:     */   
/*  654:     */   public Dfp remainder(Dfp d)
/*  655:     */   {
/*  656:1029 */     Dfp result = subtract(divide(d).rint().multiply(d));
/*  657:1032 */     if (result.mant[(this.mant.length - 1)] == 0) {
/*  658:1033 */       result.sign = this.sign;
/*  659:     */     }
/*  660:1036 */     return result;
/*  661:     */   }
/*  662:     */   
/*  663:     */   protected Dfp trunc(DfpField.RoundingMode rmode)
/*  664:     */   {
/*  665:1045 */     boolean changed = false;
/*  666:1047 */     if (isNaN()) {
/*  667:1048 */       return newInstance(this);
/*  668:     */     }
/*  669:1051 */     if (this.nans == 1) {
/*  670:1052 */       return newInstance(this);
/*  671:     */     }
/*  672:1055 */     if (this.mant[(this.mant.length - 1)] == 0) {
/*  673:1057 */       return newInstance(this);
/*  674:     */     }
/*  675:1062 */     if (this.exp < 0)
/*  676:     */     {
/*  677:1063 */       this.field.setIEEEFlagsBits(16);
/*  678:1064 */       Dfp result = newInstance(getZero());
/*  679:1065 */       result = dotrap(16, "trunc", this, result);
/*  680:1066 */       return result;
/*  681:     */     }
/*  682:1073 */     if (this.exp >= this.mant.length) {
/*  683:1074 */       return newInstance(this);
/*  684:     */     }
/*  685:1080 */     Dfp result = newInstance(this);
/*  686:1081 */     for (int i = 0; i < this.mant.length - result.exp; i++)
/*  687:     */     {
/*  688:1082 */       changed |= result.mant[i] != 0;
/*  689:1083 */       result.mant[i] = 0;
/*  690:     */     }
/*  691:1086 */     if (changed)
/*  692:     */     {
/*  693:1087 */       switch (rmode.ordinal())
/*  694:     */       {
/*  695:     */       case 1: 
/*  696:1089 */         if (result.sign == -1) {
/*  697:1091 */           result = result.add(newInstance(-1));
/*  698:     */         }
/*  699:     */         break;
/*  700:     */       case 2: 
/*  701:1096 */         if (result.sign == 1) {
/*  702:1098 */           result = result.add(getOne());
/*  703:     */         }
/*  704:     */         break;
/*  705:     */       case 3: 
/*  706:     */       default: 
/*  707:1104 */         Dfp half = newInstance("0.5");
/*  708:1105 */         Dfp a = subtract(result);
/*  709:1106 */         a.sign = 1;
/*  710:1107 */         if (a.greaterThan(half))
/*  711:     */         {
/*  712:1108 */           a = newInstance(getOne());
/*  713:1109 */           a.sign = this.sign;
/*  714:1110 */           result = result.add(a);
/*  715:     */         }
/*  716:1114 */         if ((a.equals(half)) && (result.exp > 0) && ((result.mant[(this.mant.length - result.exp)] & 0x1) != 0))
/*  717:     */         {
/*  718:1115 */           a = newInstance(getOne());
/*  719:1116 */           a.sign = this.sign;
/*  720:1117 */           result = result.add(a);
/*  721:     */         }
/*  722:     */         break;
/*  723:     */       }
/*  724:1122 */       this.field.setIEEEFlagsBits(16);
/*  725:1123 */       result = dotrap(16, "trunc", this, result);
/*  726:1124 */       return result;
/*  727:     */     }
/*  728:1127 */     return result;
/*  729:     */   }
/*  730:     */   
/*  731:     */   public int intValue()
/*  732:     */   {
/*  733:1136 */     int result = 0;
/*  734:     */     
/*  735:1138 */     Dfp rounded = rint();
/*  736:1140 */     if (rounded.greaterThan(newInstance(2147483647))) {
/*  737:1141 */       return 2147483647;
/*  738:     */     }
/*  739:1144 */     if (rounded.lessThan(newInstance(-2147483648))) {
/*  740:1145 */       return -2147483648;
/*  741:     */     }
/*  742:1148 */     for (int i = this.mant.length - 1; i >= this.mant.length - rounded.exp; i--) {
/*  743:1149 */       result = result * 10000 + rounded.mant[i];
/*  744:     */     }
/*  745:1152 */     if (rounded.sign == -1) {
/*  746:1153 */       result = -result;
/*  747:     */     }
/*  748:1156 */     return result;
/*  749:     */   }
/*  750:     */   
/*  751:     */   public int log10K()
/*  752:     */   {
/*  753:1165 */     return this.exp - 1;
/*  754:     */   }
/*  755:     */   
/*  756:     */   public Dfp power10K(int e)
/*  757:     */   {
/*  758:1173 */     Dfp d = newInstance(getOne());
/*  759:1174 */     d.exp = (e + 1);
/*  760:1175 */     return d;
/*  761:     */   }
/*  762:     */   
/*  763:     */   public int log10()
/*  764:     */   {
/*  765:1182 */     if (this.mant[(this.mant.length - 1)] > 1000) {
/*  766:1183 */       return this.exp * 4 - 1;
/*  767:     */     }
/*  768:1185 */     if (this.mant[(this.mant.length - 1)] > 100) {
/*  769:1186 */       return this.exp * 4 - 2;
/*  770:     */     }
/*  771:1188 */     if (this.mant[(this.mant.length - 1)] > 10) {
/*  772:1189 */       return this.exp * 4 - 3;
/*  773:     */     }
/*  774:1191 */     return this.exp * 4 - 4;
/*  775:     */   }
/*  776:     */   
/*  777:     */   public Dfp power10(int e)
/*  778:     */   {
/*  779:1199 */     Dfp d = newInstance(getOne());
/*  780:1201 */     if (e >= 0) {
/*  781:1202 */       d.exp = (e / 4 + 1);
/*  782:     */     } else {
/*  783:1204 */       d.exp = ((e + 1) / 4);
/*  784:     */     }
/*  785:1207 */     switch ((e % 4 + 4) % 4)
/*  786:     */     {
/*  787:     */     case 0: 
/*  788:     */       break;
/*  789:     */     case 1: 
/*  790:1211 */       d = d.multiply(10);
/*  791:1212 */       break;
/*  792:     */     case 2: 
/*  793:1214 */       d = d.multiply(100);
/*  794:1215 */       break;
/*  795:     */     default: 
/*  796:1217 */       d = d.multiply(1000);
/*  797:     */     }
/*  798:1220 */     return d;
/*  799:     */   }
/*  800:     */   
/*  801:     */   protected int complement(int extra)
/*  802:     */   {
/*  803:1231 */     extra = 10000 - extra;
/*  804:1232 */     for (int i = 0; i < this.mant.length; i++) {
/*  805:1233 */       this.mant[i] = (10000 - this.mant[i] - 1);
/*  806:     */     }
/*  807:1236 */     int rh = extra / 10000;
/*  808:1237 */     extra -= rh * 10000;
/*  809:1238 */     for (int i = 0; i < this.mant.length; i++)
/*  810:     */     {
/*  811:1239 */       int r = this.mant[i] + rh;
/*  812:1240 */       rh = r / 10000;
/*  813:1241 */       this.mant[i] = (r - rh * 10000);
/*  814:     */     }
/*  815:1244 */     return extra;
/*  816:     */   }
/*  817:     */   
/*  818:     */   public Dfp add(Dfp x)
/*  819:     */   {
/*  820:1254 */     if (this.field.getRadixDigits() != x.field.getRadixDigits())
/*  821:     */     {
/*  822:1255 */       this.field.setIEEEFlagsBits(1);
/*  823:1256 */       Dfp result = newInstance(getZero());
/*  824:1257 */       result.nans = 3;
/*  825:1258 */       return dotrap(1, "add", x, result);
/*  826:     */     }
/*  827:1262 */     if ((this.nans != 0) || (x.nans != 0))
/*  828:     */     {
/*  829:1263 */       if (isNaN()) {
/*  830:1264 */         return this;
/*  831:     */       }
/*  832:1267 */       if (x.isNaN()) {
/*  833:1268 */         return x;
/*  834:     */       }
/*  835:1271 */       if ((this.nans == 1) && (x.nans == 0)) {
/*  836:1272 */         return this;
/*  837:     */       }
/*  838:1275 */       if ((x.nans == 1) && (this.nans == 0)) {
/*  839:1276 */         return x;
/*  840:     */       }
/*  841:1279 */       if ((x.nans == 1) && (this.nans == 1) && (this.sign == x.sign)) {
/*  842:1280 */         return x;
/*  843:     */       }
/*  844:1283 */       if ((x.nans == 1) && (this.nans == 1) && (this.sign != x.sign))
/*  845:     */       {
/*  846:1284 */         this.field.setIEEEFlagsBits(1);
/*  847:1285 */         Dfp result = newInstance(getZero());
/*  848:1286 */         result.nans = 3;
/*  849:1287 */         result = dotrap(1, "add", x, result);
/*  850:1288 */         return result;
/*  851:     */       }
/*  852:     */     }
/*  853:1293 */     Dfp a = newInstance(this);
/*  854:1294 */     Dfp b = newInstance(x);
/*  855:     */     
/*  856:     */ 
/*  857:1297 */     Dfp result = newInstance(getZero());
/*  858:     */     
/*  859:     */ 
/*  860:1300 */     byte asign = a.sign;
/*  861:1301 */     byte bsign = b.sign;
/*  862:     */     
/*  863:1303 */     a.sign = 1;
/*  864:1304 */     b.sign = 1;
/*  865:     */     
/*  866:     */ 
/*  867:1307 */     byte rsign = bsign;
/*  868:1308 */     if (compare(a, b) > 0) {
/*  869:1309 */       rsign = asign;
/*  870:     */     }
/*  871:1315 */     if (b.mant[(this.mant.length - 1)] == 0) {
/*  872:1316 */       b.exp = a.exp;
/*  873:     */     }
/*  874:1319 */     if (a.mant[(this.mant.length - 1)] == 0) {
/*  875:1320 */       a.exp = b.exp;
/*  876:     */     }
/*  877:1324 */     int aextradigit = 0;
/*  878:1325 */     int bextradigit = 0;
/*  879:1326 */     if (a.exp < b.exp) {
/*  880:1327 */       aextradigit = a.align(b.exp);
/*  881:     */     } else {
/*  882:1329 */       bextradigit = b.align(a.exp);
/*  883:     */     }
/*  884:1333 */     if (asign != bsign) {
/*  885:1334 */       if (asign == rsign) {
/*  886:1335 */         bextradigit = b.complement(bextradigit);
/*  887:     */       } else {
/*  888:1337 */         aextradigit = a.complement(aextradigit);
/*  889:     */       }
/*  890:     */     }
/*  891:1342 */     int rh = 0;
/*  892:1343 */     for (int i = 0; i < this.mant.length; i++)
/*  893:     */     {
/*  894:1344 */       int r = a.mant[i] + b.mant[i] + rh;
/*  895:1345 */       rh = r / 10000;
/*  896:1346 */       result.mant[i] = (r - rh * 10000);
/*  897:     */     }
/*  898:1348 */     result.exp = a.exp;
/*  899:1349 */     result.sign = rsign;
/*  900:1354 */     if ((rh != 0) && (asign == bsign))
/*  901:     */     {
/*  902:1355 */       int lostdigit = result.mant[0];
/*  903:1356 */       result.shiftRight();
/*  904:1357 */       result.mant[(this.mant.length - 1)] = rh;
/*  905:1358 */       int excp = result.round(lostdigit);
/*  906:1359 */       if (excp != 0) {
/*  907:1360 */         result = dotrap(excp, "add", x, result);
/*  908:     */       }
/*  909:     */     }
/*  910:1365 */     for (int i = 0; i < this.mant.length; i++)
/*  911:     */     {
/*  912:1366 */       if (result.mant[(this.mant.length - 1)] != 0) {
/*  913:     */         break;
/*  914:     */       }
/*  915:1369 */       result.shiftLeft();
/*  916:1370 */       if (i == 0)
/*  917:     */       {
/*  918:1371 */         result.mant[0] = (aextradigit + bextradigit);
/*  919:1372 */         aextradigit = 0;
/*  920:1373 */         bextradigit = 0;
/*  921:     */       }
/*  922:     */     }
/*  923:1378 */     if (result.mant[(this.mant.length - 1)] == 0)
/*  924:     */     {
/*  925:1379 */       result.exp = 0;
/*  926:1381 */       if (asign != bsign) {
/*  927:1383 */         result.sign = 1;
/*  928:     */       }
/*  929:     */     }
/*  930:1388 */     int excp = result.round(aextradigit + bextradigit);
/*  931:1389 */     if (excp != 0) {
/*  932:1390 */       result = dotrap(excp, "add", x, result);
/*  933:     */     }
/*  934:1393 */     return result;
/*  935:     */   }
/*  936:     */   
/*  937:     */   public Dfp negate()
/*  938:     */   {
/*  939:1400 */     Dfp result = newInstance(this);
/*  940:1401 */     result.sign = ((byte)-result.sign);
/*  941:1402 */     return result;
/*  942:     */   }
/*  943:     */   
/*  944:     */   public Dfp subtract(Dfp x)
/*  945:     */   {
/*  946:1410 */     return add(x.negate());
/*  947:     */   }
/*  948:     */   
/*  949:     */   protected int round(int n)
/*  950:     */   {
/*  951:1418 */     boolean inc = false;
/*  952:1419 */     switch (this.field.getRoundingMode().ordinal())
/*  953:     */     {
/*  954:     */     case 4: 
/*  955:1421 */       inc = false;
/*  956:1422 */       break;
/*  957:     */     case 5: 
/*  958:1425 */       inc = n != 0;
/*  959:1426 */       break;
/*  960:     */     case 6: 
/*  961:1429 */       inc = n >= 5000;
/*  962:1430 */       break;
/*  963:     */     case 7: 
/*  964:1433 */       inc = n > 5000;
/*  965:1434 */       break;
/*  966:     */     case 3: 
/*  967:1437 */       inc = (n > 5000) || ((n == 5000) && ((this.mant[0] & 0x1) == 1));
/*  968:1438 */       break;
/*  969:     */     case 8: 
/*  970:1441 */       inc = (n > 5000) || ((n == 5000) && ((this.mant[0] & 0x1) == 0));
/*  971:1442 */       break;
/*  972:     */     case 2: 
/*  973:1445 */       inc = (this.sign == 1) && (n != 0);
/*  974:1446 */       break;
/*  975:     */     case 1: 
/*  976:     */     default: 
/*  977:1450 */       inc = (this.sign == -1) && (n != 0);
/*  978:     */     }
/*  979:1454 */     if (inc)
/*  980:     */     {
/*  981:1456 */       int rh = 1;
/*  982:1457 */       for (int i = 0; i < this.mant.length; i++)
/*  983:     */       {
/*  984:1458 */         int r = this.mant[i] + rh;
/*  985:1459 */         rh = r / 10000;
/*  986:1460 */         this.mant[i] = (r - rh * 10000);
/*  987:     */       }
/*  988:1463 */       if (rh != 0)
/*  989:     */       {
/*  990:1464 */         shiftRight();
/*  991:1465 */         this.mant[(this.mant.length - 1)] = rh;
/*  992:     */       }
/*  993:     */     }
/*  994:1470 */     if (this.exp < -32767)
/*  995:     */     {
/*  996:1472 */       this.field.setIEEEFlagsBits(8);
/*  997:1473 */       return 8;
/*  998:     */     }
/*  999:1476 */     if (this.exp > 32768)
/* 1000:     */     {
/* 1001:1478 */       this.field.setIEEEFlagsBits(4);
/* 1002:1479 */       return 4;
/* 1003:     */     }
/* 1004:1482 */     if (n != 0)
/* 1005:     */     {
/* 1006:1484 */       this.field.setIEEEFlagsBits(16);
/* 1007:1485 */       return 16;
/* 1008:     */     }
/* 1009:1488 */     return 0;
/* 1010:     */   }
/* 1011:     */   
/* 1012:     */   public Dfp multiply(Dfp x)
/* 1013:     */   {
/* 1014:1499 */     if (this.field.getRadixDigits() != x.field.getRadixDigits())
/* 1015:     */     {
/* 1016:1500 */       this.field.setIEEEFlagsBits(1);
/* 1017:1501 */       Dfp result = newInstance(getZero());
/* 1018:1502 */       result.nans = 3;
/* 1019:1503 */       return dotrap(1, "multiply", x, result);
/* 1020:     */     }
/* 1021:1506 */     Dfp result = newInstance(getZero());
/* 1022:1509 */     if ((this.nans != 0) || (x.nans != 0))
/* 1023:     */     {
/* 1024:1510 */       if (isNaN()) {
/* 1025:1511 */         return this;
/* 1026:     */       }
/* 1027:1514 */       if (x.isNaN()) {
/* 1028:1515 */         return x;
/* 1029:     */       }
/* 1030:1518 */       if ((this.nans == 1) && (x.nans == 0) && (x.mant[(this.mant.length - 1)] != 0))
/* 1031:     */       {
/* 1032:1519 */         result = newInstance(this);
/* 1033:1520 */         result.sign = ((byte)(this.sign * x.sign));
/* 1034:1521 */         return result;
/* 1035:     */       }
/* 1036:1524 */       if ((x.nans == 1) && (this.nans == 0) && (this.mant[(this.mant.length - 1)] != 0))
/* 1037:     */       {
/* 1038:1525 */         result = newInstance(x);
/* 1039:1526 */         result.sign = ((byte)(this.sign * x.sign));
/* 1040:1527 */         return result;
/* 1041:     */       }
/* 1042:1530 */       if ((x.nans == 1) && (this.nans == 1))
/* 1043:     */       {
/* 1044:1531 */         result = newInstance(this);
/* 1045:1532 */         result.sign = ((byte)(this.sign * x.sign));
/* 1046:1533 */         return result;
/* 1047:     */       }
/* 1048:1536 */       if (((x.nans == 1) && (this.nans == 0) && (this.mant[(this.mant.length - 1)] == 0)) || ((this.nans == 1) && (x.nans == 0) && (x.mant[(this.mant.length - 1)] == 0)))
/* 1049:     */       {
/* 1050:1538 */         this.field.setIEEEFlagsBits(1);
/* 1051:1539 */         result = newInstance(getZero());
/* 1052:1540 */         result.nans = 3;
/* 1053:1541 */         result = dotrap(1, "multiply", x, result);
/* 1054:1542 */         return result;
/* 1055:     */       }
/* 1056:     */     }
/* 1057:1546 */     int[] product = new int[this.mant.length * 2];
/* 1058:1548 */     for (int i = 0; i < this.mant.length; i++)
/* 1059:     */     {
/* 1060:1549 */       int rh = 0;
/* 1061:1550 */       for (int j = 0; j < this.mant.length; j++)
/* 1062:     */       {
/* 1063:1551 */         int r = this.mant[i] * x.mant[j];
/* 1064:1552 */         r = r + product[(i + j)] + rh;
/* 1065:     */         
/* 1066:1554 */         rh = r / 10000;
/* 1067:1555 */         product[(i + j)] = (r - rh * 10000);
/* 1068:     */       }
/* 1069:1557 */       product[(i + this.mant.length)] = rh;
/* 1070:     */     }
/* 1071:1561 */     int md = this.mant.length * 2 - 1;
/* 1072:1562 */     for (int i = this.mant.length * 2 - 1; i >= 0; i--) {
/* 1073:1563 */       if (product[i] != 0)
/* 1074:     */       {
/* 1075:1564 */         md = i;
/* 1076:1565 */         break;
/* 1077:     */       }
/* 1078:     */     }
/* 1079:1570 */     for (int i = 0; i < this.mant.length; i++) {
/* 1080:1571 */       result.mant[(this.mant.length - i - 1)] = product[(md - i)];
/* 1081:     */     }
/* 1082:1575 */     result.exp = (this.exp + x.exp + md - 2 * this.mant.length + 1);
/* 1083:1576 */     result.sign = ((byte)(this.sign == x.sign ? 1 : -1));
/* 1084:1578 */     if (result.mant[(this.mant.length - 1)] == 0) {
/* 1085:1580 */       result.exp = 0;
/* 1086:     */     }
/* 1087:     */     int excp;
/* 1088:     */     
/* 1089:1584 */     if (md > this.mant.length - 1) {
/* 1090:1585 */       excp = result.round(product[(md - this.mant.length)]);
/* 1091:     */     } else {
/* 1092:1587 */       excp = result.round(0);
/* 1093:     */     }
/* 1094:1590 */     if (excp != 0) {
/* 1095:1591 */       result = dotrap(excp, "multiply", x, result);
/* 1096:     */     }
/* 1097:1594 */     return result;
/* 1098:     */   }
/* 1099:     */   
/* 1100:     */   public Dfp multiply(int x)
/* 1101:     */   {
/* 1102:1604 */     Dfp result = newInstance(this);
/* 1103:1607 */     if (this.nans != 0)
/* 1104:     */     {
/* 1105:1608 */       if (isNaN()) {
/* 1106:1609 */         return this;
/* 1107:     */       }
/* 1108:1612 */       if ((this.nans == 1) && (x != 0))
/* 1109:     */       {
/* 1110:1613 */         result = newInstance(this);
/* 1111:1614 */         return result;
/* 1112:     */       }
/* 1113:1617 */       if ((this.nans == 1) && (x == 0))
/* 1114:     */       {
/* 1115:1618 */         this.field.setIEEEFlagsBits(1);
/* 1116:1619 */         result = newInstance(getZero());
/* 1117:1620 */         result.nans = 3;
/* 1118:1621 */         result = dotrap(1, "multiply", newInstance(getZero()), result);
/* 1119:1622 */         return result;
/* 1120:     */       }
/* 1121:     */     }
/* 1122:1627 */     if ((x < 0) || (x >= 10000))
/* 1123:     */     {
/* 1124:1628 */       this.field.setIEEEFlagsBits(1);
/* 1125:1629 */       result = newInstance(getZero());
/* 1126:1630 */       result.nans = 3;
/* 1127:1631 */       result = dotrap(1, "multiply", result, result);
/* 1128:1632 */       return result;
/* 1129:     */     }
/* 1130:1635 */     int rh = 0;
/* 1131:1636 */     for (int i = 0; i < this.mant.length; i++)
/* 1132:     */     {
/* 1133:1637 */       int r = this.mant[i] * x + rh;
/* 1134:1638 */       rh = r / 10000;
/* 1135:1639 */       result.mant[i] = (r - rh * 10000);
/* 1136:     */     }
/* 1137:1642 */     int lostdigit = 0;
/* 1138:1643 */     if (rh != 0)
/* 1139:     */     {
/* 1140:1644 */       lostdigit = result.mant[0];
/* 1141:1645 */       result.shiftRight();
/* 1142:1646 */       result.mant[(this.mant.length - 1)] = rh;
/* 1143:     */     }
/* 1144:1649 */     if (result.mant[(this.mant.length - 1)] == 0) {
/* 1145:1650 */       result.exp = 0;
/* 1146:     */     }
/* 1147:1653 */     int excp = result.round(lostdigit);
/* 1148:1654 */     if (excp != 0) {
/* 1149:1655 */       result = dotrap(excp, "multiply", result, result);
/* 1150:     */     }
/* 1151:1658 */     return result;
/* 1152:     */   }
/* 1153:     */   
/* 1154:     */   public Dfp divide(Dfp divisor)
/* 1155:     */   {
/* 1156:1671 */     int trial = 0;
/* 1157:     */     
/* 1158:     */ 
/* 1159:1674 */     int md = 0;
/* 1160:1678 */     if (this.field.getRadixDigits() != divisor.field.getRadixDigits())
/* 1161:     */     {
/* 1162:1679 */       this.field.setIEEEFlagsBits(1);
/* 1163:1680 */       Dfp result = newInstance(getZero());
/* 1164:1681 */       result.nans = 3;
/* 1165:1682 */       return dotrap(1, "divide", divisor, result);
/* 1166:     */     }
/* 1167:1685 */     Dfp result = newInstance(getZero());
/* 1168:1688 */     if ((this.nans != 0) || (divisor.nans != 0))
/* 1169:     */     {
/* 1170:1689 */       if (isNaN()) {
/* 1171:1690 */         return this;
/* 1172:     */       }
/* 1173:1693 */       if (divisor.isNaN()) {
/* 1174:1694 */         return divisor;
/* 1175:     */       }
/* 1176:1697 */       if ((this.nans == 1) && (divisor.nans == 0))
/* 1177:     */       {
/* 1178:1698 */         result = newInstance(this);
/* 1179:1699 */         result.sign = ((byte)(this.sign * divisor.sign));
/* 1180:1700 */         return result;
/* 1181:     */       }
/* 1182:1703 */       if ((divisor.nans == 1) && (this.nans == 0))
/* 1183:     */       {
/* 1184:1704 */         result = newInstance(getZero());
/* 1185:1705 */         result.sign = ((byte)(this.sign * divisor.sign));
/* 1186:1706 */         return result;
/* 1187:     */       }
/* 1188:1709 */       if ((divisor.nans == 1) && (this.nans == 1))
/* 1189:     */       {
/* 1190:1710 */         this.field.setIEEEFlagsBits(1);
/* 1191:1711 */         result = newInstance(getZero());
/* 1192:1712 */         result.nans = 3;
/* 1193:1713 */         result = dotrap(1, "divide", divisor, result);
/* 1194:1714 */         return result;
/* 1195:     */       }
/* 1196:     */     }
/* 1197:1719 */     if (divisor.mant[(this.mant.length - 1)] == 0)
/* 1198:     */     {
/* 1199:1720 */       this.field.setIEEEFlagsBits(2);
/* 1200:1721 */       result = newInstance(getZero());
/* 1201:1722 */       result.sign = ((byte)(this.sign * divisor.sign));
/* 1202:1723 */       result.nans = 1;
/* 1203:1724 */       result = dotrap(2, "divide", divisor, result);
/* 1204:1725 */       return result;
/* 1205:     */     }
/* 1206:1728 */     int[] dividend = new int[this.mant.length + 1];
/* 1207:1729 */     int[] quotient = new int[this.mant.length + 2];
/* 1208:1730 */     int[] remainder = new int[this.mant.length + 1];
/* 1209:     */     
/* 1210:     */ 
/* 1211:     */ 
/* 1212:1734 */     dividend[this.mant.length] = 0;
/* 1213:1735 */     quotient[this.mant.length] = 0;
/* 1214:1736 */     quotient[(this.mant.length + 1)] = 0;
/* 1215:1737 */     remainder[this.mant.length] = 0;
/* 1216:1742 */     for (int i = 0; i < this.mant.length; i++)
/* 1217:     */     {
/* 1218:1743 */       dividend[i] = this.mant[i];
/* 1219:1744 */       quotient[i] = 0;
/* 1220:1745 */       remainder[i] = 0;
/* 1221:     */     }
/* 1222:1749 */     int nsqd = 0;
/* 1223:1750 */     for (int qd = this.mant.length + 1; qd >= 0; qd--)
/* 1224:     */     {
/* 1225:1754 */       int divMsb = dividend[this.mant.length] * 10000 + dividend[(this.mant.length - 1)];
/* 1226:1755 */       int min = divMsb / (divisor.mant[(this.mant.length - 1)] + 1);
/* 1227:1756 */       int max = (divMsb + 1) / divisor.mant[(this.mant.length - 1)];
/* 1228:     */       
/* 1229:1758 */       boolean trialgood = false;
/* 1230:1759 */       while (!trialgood)
/* 1231:     */       {
/* 1232:1761 */         trial = (min + max) / 2;
/* 1233:     */         
/* 1234:     */ 
/* 1235:1764 */         int rh = 0;
/* 1236:1765 */         for (int i = 0; i < this.mant.length + 1; i++)
/* 1237:     */         {
/* 1238:1766 */           int dm = i < this.mant.length ? divisor.mant[i] : 0;
/* 1239:1767 */           int r = dm * trial + rh;
/* 1240:1768 */           rh = r / 10000;
/* 1241:1769 */           remainder[i] = (r - rh * 10000);
/* 1242:     */         }
/* 1243:1773 */         rh = 1;
/* 1244:1774 */         for (int i = 0; i < this.mant.length + 1; i++)
/* 1245:     */         {
/* 1246:1775 */           int r = 9999 - remainder[i] + dividend[i] + rh;
/* 1247:1776 */           rh = r / 10000;
/* 1248:1777 */           remainder[i] = (r - rh * 10000);
/* 1249:     */         }
/* 1250:1781 */         if (rh == 0)
/* 1251:     */         {
/* 1252:1783 */           max = trial - 1;
/* 1253:     */         }
/* 1254:     */         else
/* 1255:     */         {
/* 1256:1788 */           int minadj = remainder[this.mant.length] * 10000 + remainder[(this.mant.length - 1)];
/* 1257:1789 */           minadj /= (divisor.mant[(this.mant.length - 1)] + 1);
/* 1258:1791 */           if (minadj >= 2)
/* 1259:     */           {
/* 1260:1792 */             min = trial + minadj;
/* 1261:     */           }
/* 1262:     */           else
/* 1263:     */           {
/* 1264:1798 */             trialgood = false;
/* 1265:1799 */             for (int i = this.mant.length - 1; i >= 0; i--)
/* 1266:     */             {
/* 1267:1800 */               if (divisor.mant[i] > remainder[i]) {
/* 1268:1801 */                 trialgood = true;
/* 1269:     */               }
/* 1270:1803 */               if (divisor.mant[i] < remainder[i]) {
/* 1271:     */                 break;
/* 1272:     */               }
/* 1273:     */             }
/* 1274:1808 */             if (remainder[this.mant.length] != 0) {
/* 1275:1809 */               trialgood = false;
/* 1276:     */             }
/* 1277:1812 */             if (!trialgood) {
/* 1278:1813 */               min = trial + 1;
/* 1279:     */             }
/* 1280:     */           }
/* 1281:     */         }
/* 1282:     */       }
/* 1283:1818 */       quotient[qd] = trial;
/* 1284:1819 */       if ((trial != 0) || (nsqd != 0)) {
/* 1285:1820 */         nsqd++;
/* 1286:     */       }
/* 1287:1823 */       if ((this.field.getRoundingMode() == DfpField.RoundingMode.ROUND_DOWN) && (nsqd == this.mant.length)) {
/* 1288:     */         break;
/* 1289:     */       }
/* 1290:1828 */       if (nsqd > this.mant.length) {
/* 1291:     */         break;
/* 1292:     */       }
/* 1293:1834 */       dividend[0] = 0;
/* 1294:1835 */       for (int i = 0; i < this.mant.length; i++) {
/* 1295:1836 */         dividend[(i + 1)] = remainder[i];
/* 1296:     */       }
/* 1297:     */     }
/* 1298:1841 */     md = this.mant.length;
/* 1299:1842 */     for (int i = this.mant.length + 1; i >= 0; i--) {
/* 1300:1843 */       if (quotient[i] != 0)
/* 1301:     */       {
/* 1302:1844 */         md = i;
/* 1303:1845 */         break;
/* 1304:     */       }
/* 1305:     */     }
/* 1306:1850 */     for (int i = 0; i < this.mant.length; i++) {
/* 1307:1851 */       result.mant[(this.mant.length - i - 1)] = quotient[(md - i)];
/* 1308:     */     }
/* 1309:1855 */     result.exp = (this.exp - divisor.exp + md - this.mant.length);
/* 1310:1856 */     result.sign = ((byte)(this.sign == divisor.sign ? 1 : -1));
/* 1311:1858 */     if (result.mant[(this.mant.length - 1)] == 0) {
/* 1312:1859 */       result.exp = 0;
/* 1313:     */     }
/* 1314:     */     int excp;
/* 1315:     */    
/* 1316:1862 */     if (md > this.mant.length - 1) {
/* 1317:1863 */       excp = result.round(quotient[(md - this.mant.length)]);
/* 1318:     */     } else {
/* 1319:1865 */       excp = result.round(0);
/* 1320:     */     }
/* 1321:1868 */     if (excp != 0) {
/* 1322:1869 */       result = dotrap(excp, "divide", divisor, result);
/* 1323:     */     }
/* 1324:1872 */     return result;
/* 1325:     */   }
/* 1326:     */   
/* 1327:     */   public Dfp divide(int divisor)
/* 1328:     */   {
/* 1329:1883 */     if (this.nans != 0)
/* 1330:     */     {
/* 1331:1884 */       if (isNaN()) {
/* 1332:1885 */         return this;
/* 1333:     */       }
/* 1334:1888 */       if (this.nans == 1) {
/* 1335:1889 */         return newInstance(this);
/* 1336:     */       }
/* 1337:     */     }
/* 1338:1894 */     if (divisor == 0)
/* 1339:     */     {
/* 1340:1895 */       this.field.setIEEEFlagsBits(2);
/* 1341:1896 */       Dfp result = newInstance(getZero());
/* 1342:1897 */       result.sign = this.sign;
/* 1343:1898 */       result.nans = 1;
/* 1344:1899 */       result = dotrap(2, "divide", getZero(), result);
/* 1345:1900 */       return result;
/* 1346:     */     }
/* 1347:1904 */     if ((divisor < 0) || (divisor >= 10000))
/* 1348:     */     {
/* 1349:1905 */       this.field.setIEEEFlagsBits(1);
/* 1350:1906 */       Dfp result = newInstance(getZero());
/* 1351:1907 */       result.nans = 3;
/* 1352:1908 */       result = dotrap(1, "divide", result, result);
/* 1353:1909 */       return result;
/* 1354:     */     }
/* 1355:1912 */     Dfp result = newInstance(this);
/* 1356:     */     
/* 1357:1914 */     int rl = 0;
/* 1358:1915 */     for (int i = this.mant.length - 1; i >= 0; i--)
/* 1359:     */     {
/* 1360:1916 */       int r = rl * 10000 + result.mant[i];
/* 1361:1917 */       int rh = r / divisor;
/* 1362:1918 */       rl = r - rh * divisor;
/* 1363:1919 */       result.mant[i] = rh;
/* 1364:     */     }
/* 1365:1922 */     if (result.mant[(this.mant.length - 1)] == 0)
/* 1366:     */     {
/* 1367:1924 */       result.shiftLeft();
/* 1368:1925 */       int r = rl * 10000;
/* 1369:1926 */       int rh = r / divisor;
/* 1370:1927 */       rl = r - rh * divisor;
/* 1371:1928 */       result.mant[0] = rh;
/* 1372:     */     }
/* 1373:1931 */     int excp = result.round(rl * 10000 / divisor);
/* 1374:1932 */     if (excp != 0) {
/* 1375:1933 */       result = dotrap(excp, "divide", result, result);
/* 1376:     */     }
/* 1377:1936 */     return result;
/* 1378:     */   }
/* 1379:     */   
/* 1380:     */   public Dfp reciprocal()
/* 1381:     */   {
/* 1382:1942 */     return this.field.getOne().divide(this);
/* 1383:     */   }
/* 1384:     */   
/* 1385:     */   public Dfp sqrt()
/* 1386:     */   {
/* 1387:1951 */     if ((this.nans == 0) && (this.mant[(this.mant.length - 1)] == 0)) {
/* 1388:1953 */       return newInstance(this);
/* 1389:     */     }
/* 1390:1956 */     if (this.nans != 0)
/* 1391:     */     {
/* 1392:1957 */       if ((this.nans == 1) && (this.sign == 1)) {
/* 1393:1959 */         return newInstance(this);
/* 1394:     */       }
/* 1395:1962 */       if (this.nans == 3) {
/* 1396:1963 */         return newInstance(this);
/* 1397:     */       }
/* 1398:1966 */       if (this.nans == 2)
/* 1399:     */       {
/* 1400:1969 */         this.field.setIEEEFlagsBits(1);
/* 1401:1970 */         Dfp result = newInstance(this);
/* 1402:1971 */         result = dotrap(1, "sqrt", null, result);
/* 1403:1972 */         return result;
/* 1404:     */       }
/* 1405:     */     }
/* 1406:1976 */     if (this.sign == -1)
/* 1407:     */     {
/* 1408:1980 */       this.field.setIEEEFlagsBits(1);
/* 1409:1981 */       Dfp result = newInstance(this);
/* 1410:1982 */       result.nans = 3;
/* 1411:1983 */       result = dotrap(1, "sqrt", null, result);
/* 1412:1984 */       return result;
/* 1413:     */     }
/* 1414:1987 */     Dfp x = newInstance(this);
/* 1415:1990 */     if ((x.exp < -1) || (x.exp > 1)) {
/* 1416:1991 */       this.exp /= 2;
/* 1417:     */     }
/* 1418:1995 */     switch (x.mant[(this.mant.length - 1)] / 2000)
/* 1419:     */     {
/* 1420:     */     case 0: 
/* 1421:1997 */       x.mant[(this.mant.length - 1)] = (x.mant[(this.mant.length - 1)] / 2 + 1);
/* 1422:1998 */       break;
/* 1423:     */     case 2: 
/* 1424:2000 */       x.mant[(this.mant.length - 1)] = 1500;
/* 1425:2001 */       break;
/* 1426:     */     case 3: 
/* 1427:2003 */       x.mant[(this.mant.length - 1)] = 2200;
/* 1428:2004 */       break;
/* 1429:     */     case 1: 
/* 1430:     */     default: 
/* 1431:2006 */       x.mant[(this.mant.length - 1)] = 3000;
/* 1432:     */     }
/* 1433:2009 */     Dfp dx = newInstance(x);
/* 1434:     */     
/* 1435:     */ 
/* 1436:     */ 
/* 1437:     */ 
/* 1438:2014 */     Dfp px = getZero();
/* 1439:2015 */     Dfp ppx = getZero();
/* 1440:2016 */     while (x.unequal(px))
/* 1441:     */     {
/* 1442:2017 */       dx = newInstance(x);
/* 1443:2018 */       dx.sign = -1;
/* 1444:2019 */       dx = dx.add(divide(x));
/* 1445:2020 */       dx = dx.divide(2);
/* 1446:2021 */       ppx = px;
/* 1447:2022 */       px = x;
/* 1448:2023 */       x = x.add(dx);
/* 1449:2025 */       if (!x.equals(ppx)) {
/* 1450:2032 */         if (dx.mant[(this.mant.length - 1)] == 0) {
/* 1451:     */           break;
/* 1452:     */         }
/* 1453:     */       }
/* 1454:     */     }
/* 1455:2037 */     return x;
/* 1456:     */   }
/* 1457:     */   
/* 1458:     */   public String toString()
/* 1459:     */   {
/* 1460:2046 */     if (this.nans != 0)
/* 1461:     */     {
/* 1462:2048 */       if (this.nans == 1) {
/* 1463:2049 */         return this.sign < 0 ? "-Infinity" : "Infinity";
/* 1464:     */       }
/* 1465:2051 */       return "NaN";
/* 1466:     */     }
/* 1467:2055 */     if ((this.exp > this.mant.length) || (this.exp < -1)) {
/* 1468:2056 */       return dfp2sci();
/* 1469:     */     }
/* 1470:2059 */     return dfp2string();
/* 1471:     */   }
/* 1472:     */   
/* 1473:     */   protected String dfp2sci()
/* 1474:     */   {
/* 1475:2067 */     char[] rawdigits = new char[this.mant.length * 4];
/* 1476:2068 */     char[] outputbuffer = new char[this.mant.length * 4 + 20];
/* 1477:     */     
/* 1478:     */ 
/* 1479:     */ 
/* 1480:     */ 
/* 1481:     */ 
/* 1482:     */ 
/* 1483:     */ 
/* 1484:2076 */     int p = 0;
/* 1485:2077 */     for (int i = this.mant.length - 1; i >= 0; i--)
/* 1486:     */     {
/* 1487:2078 */       rawdigits[(p++)] = ((char)(this.mant[i] / 1000 + 48));
/* 1488:2079 */       rawdigits[(p++)] = ((char)(this.mant[i] / 100 % 10 + 48));
/* 1489:2080 */       rawdigits[(p++)] = ((char)(this.mant[i] / 10 % 10 + 48));
/* 1490:2081 */       rawdigits[(p++)] = ((char)(this.mant[i] % 10 + 48));
/* 1491:     */     }
/* 1492:2085 */     for (p = 0; p < rawdigits.length; p++) {
/* 1493:2086 */       if (rawdigits[p] != '0') {
/* 1494:     */         break;
/* 1495:     */       }
/* 1496:     */     }
/* 1497:2090 */     int shf = p;
/* 1498:     */     
/* 1499:     */ 
/* 1500:2093 */     int q = 0;
/* 1501:2094 */     if (this.sign == -1) {
/* 1502:2095 */       outputbuffer[(q++)] = '-';
/* 1503:     */     }
/* 1504:2098 */     if (p != rawdigits.length)
/* 1505:     */     {
/* 1506:2100 */       outputbuffer[(q++)] = rawdigits[(p++)];
/* 1507:2101 */       outputbuffer[(q++)] = '.';
/* 1508:2103 */       while (p < rawdigits.length) {
/* 1509:2104 */         outputbuffer[(q++)] = rawdigits[(p++)];
/* 1510:     */       }
/* 1511:     */     }
/* 1512:2107 */     outputbuffer[(q++)] = '0';
/* 1513:2108 */     outputbuffer[(q++)] = '.';
/* 1514:2109 */     outputbuffer[(q++)] = '0';
/* 1515:2110 */     outputbuffer[(q++)] = 'e';
/* 1516:2111 */     outputbuffer[(q++)] = '0';
/* 1517:2112 */     return new String(outputbuffer, 0, 5);   }
/* 1541:     */   
/* 1542:     */   protected String dfp2string()
/* 1543:     */   {
/* 1544:2148 */     char[] buffer = new char[this.mant.length * 4 + 20];
/* 1545:2149 */     int p = 1;
/* 1546:     */     
/* 1547:2151 */     int e = this.exp;
/* 1548:2152 */     boolean pointInserted = false;
/* 1549:     */     
/* 1550:2154 */     buffer[0] = ' ';
/* 1551:2156 */     if (e <= 0)
/* 1552:     */     {
/* 1553:2157 */       buffer[(p++)] = '0';
/* 1554:2158 */       buffer[(p++)] = '.';
/* 1555:2159 */       pointInserted = true;
/* 1556:     */     }
/* 1557:2162 */     while (e < 0)
/* 1558:     */     {
/* 1559:2163 */       buffer[(p++)] = '0';
/* 1560:2164 */       buffer[(p++)] = '0';
/* 1561:2165 */       buffer[(p++)] = '0';
/* 1562:2166 */       buffer[(p++)] = '0';
/* 1563:2167 */       e++;
/* 1564:     */     }
/* 1565:2170 */     for (int i = this.mant.length - 1; i >= 0; i--)
/* 1566:     */     {
/* 1567:2171 */       buffer[(p++)] = ((char)(this.mant[i] / 1000 + 48));
/* 1568:2172 */       buffer[(p++)] = ((char)(this.mant[i] / 100 % 10 + 48));
/* 1569:2173 */       buffer[(p++)] = ((char)(this.mant[i] / 10 % 10 + 48));
/* 1570:2174 */       buffer[(p++)] = ((char)(this.mant[i] % 10 + 48));
/* 1571:2175 */       e--;
/* 1572:2175 */       if (e == 0)
/* 1573:     */       {
/* 1574:2176 */         buffer[(p++)] = '.';
/* 1575:2177 */         pointInserted = true;
/* 1576:     */       }
/* 1577:     */     }
/* 1578:2181 */     while (e > 0)
/* 1579:     */     {
/* 1580:2182 */       buffer[(p++)] = '0';
/* 1581:2183 */       buffer[(p++)] = '0';
/* 1582:2184 */       buffer[(p++)] = '0';
/* 1583:2185 */       buffer[(p++)] = '0';
/* 1584:2186 */       e--;
/* 1585:     */     }
/* 1586:2189 */     if (!pointInserted) {
/* 1587:2191 */       buffer[(p++)] = '.';
/* 1588:     */     }
/* 1589:2195 */     int q = 1;
/* 1590:2196 */     while (buffer[q] == '0') {
/* 1591:2197 */       q++;
/* 1592:     */     }
/* 1593:2199 */     if (buffer[q] == '.') {
/* 1594:2200 */       q--;
/* 1595:     */     }
/* 1596:2204 */     while (buffer[(p - 1)] == '0') {
/* 1597:2205 */       p--;
/* 1598:     */     }
/* 1599:2209 */     if (this.sign < 0) {
/* 1600:2210 */       buffer[(--q)] = '-';
/* 1601:     */     }
/* 1602:2213 */     return new String(buffer, q, p - q);
/* 1603:     */   }
/* 1604:     */   
/* 1605:     */   public Dfp dotrap(int type, String what, Dfp oper, Dfp result)
/* 1606:     */   {
/* 1607:2225 */     Dfp def = result;
/* 1608:2227 */     switch (type)
/* 1609:     */     {
/* 1610:     */     case 1: 
/* 1611:2229 */       def = newInstance(getZero());
/* 1612:2230 */       def.sign = result.sign;
/* 1613:2231 */       def.nans = 3;
/* 1614:2232 */       break;
/* 1615:     */     case 2: 
/* 1616:2235 */       if ((this.nans == 0) && (this.mant[(this.mant.length - 1)] != 0))
/* 1617:     */       {
/* 1618:2237 */         def = newInstance(getZero());
/* 1619:2238 */         def.sign = ((byte)(this.sign * oper.sign));
/* 1620:2239 */         def.nans = 1;
/* 1621:     */       }
/* 1622:2242 */       if ((this.nans == 0) && (this.mant[(this.mant.length - 1)] == 0))
/* 1623:     */       {
/* 1624:2244 */         def = newInstance(getZero());
/* 1625:2245 */         def.nans = 3;
/* 1626:     */       }
/* 1627:2248 */       if ((this.nans == 1) || (this.nans == 3))
/* 1628:     */       {
/* 1629:2249 */         def = newInstance(getZero());
/* 1630:2250 */         def.nans = 3;
/* 1631:     */       }
/* 1632:2253 */       if ((this.nans == 1) || (this.nans == 2))
/* 1633:     */       {
/* 1634:2254 */         def = newInstance(getZero());
/* 1635:2255 */         def.nans = 3;
/* 1636:     */       }
/* 1637:     */       break;
/* 1638:     */     case 8: 
/* 1639:2260 */       if (result.exp + this.mant.length < -32767)
/* 1640:     */       {
/* 1641:2261 */         def = newInstance(getZero());
/* 1642:2262 */         def.sign = result.sign;
/* 1643:     */       }
/* 1644:     */       else
/* 1645:     */       {
/* 1646:2264 */         def = newInstance(result);
/* 1647:     */       }
/* 1648:2266 */       result.exp += 32760;
/* 1649:2267 */       break;
/* 1650:     */     case 4: 
/* 1651:2270 */       result.exp -= 32760;
/* 1652:2271 */       def = newInstance(getZero());
/* 1653:2272 */       def.sign = result.sign;
/* 1654:2273 */       def.nans = 1;
/* 1655:2274 */       break;
/* 1656:     */     case 3: 
/* 1657:     */     case 5: 
/* 1658:     */     case 6: 
/* 1659:     */     case 7: 
/* 1660:     */     default: 
/* 1661:2276 */       def = result;
/* 1662:     */     }
/* 1663:2279 */     return trap(type, what, oper, def, result);
/* 1664:     */   }
/* 1665:     */   
/* 1666:     */   protected Dfp trap(int type, String what, Dfp oper, Dfp def, Dfp result)
/* 1667:     */   {
/* 1668:2295 */     return def;
/* 1669:     */   }
/* 1670:     */   
/* 1671:     */   public int classify()
/* 1672:     */   {
/* 1673:2302 */     return this.nans;
/* 1674:     */   }
/* 1675:     */   
/* 1676:     */   public static Dfp copysign(Dfp x, Dfp y)
/* 1677:     */   {
/* 1678:2312 */     Dfp result = x.newInstance(x);
/* 1679:2313 */     result.sign = y.sign;
/* 1680:2314 */     return result;
/* 1681:     */   }
/* 1682:     */   
/* 1683:     */   public Dfp nextAfter(Dfp x)
/* 1684:     */   {
/* 1685:2325 */     if (this.field.getRadixDigits() != x.field.getRadixDigits())
/* 1686:     */     {
/* 1687:2326 */       this.field.setIEEEFlagsBits(1);
/* 1688:2327 */       Dfp result = newInstance(getZero());
/* 1689:2328 */       result.nans = 3;
/* 1690:2329 */       return dotrap(1, "nextAfter", x, result);
/* 1691:     */     }
/* 1692:2333 */     boolean up = false;
/* 1693:2334 */     if (lessThan(x)) {
/* 1694:2335 */       up = true;
/* 1695:     */     }
/* 1696:2338 */     if (compare(this, x) == 0) {
/* 1697:2339 */       return newInstance(x);
/* 1698:     */     }
/* 1699:2342 */     if (lessThan(getZero())) {
/* 1700:2343 */       up = !up;
/* 1701:     */     }
/* 1702:     */     Dfp result;
/* 1703:     */     
/* 1704:2348 */     if (up)
/* 1705:     */     {
/* 1706:2349 */       Dfp inc = newInstance(getOne());
/* 1707:2350 */       inc.exp = (this.exp - this.mant.length + 1);
/* 1708:2351 */       inc.sign = this.sign;
/* 1709:2353 */       if (equals(getZero())) {
/* 1710:2354 */         inc.exp = (-32767 - this.mant.length);
/* 1711:     */       }
/* 1712:2357 */       result = add(inc);
/* 1713:     */     }
/* 1714:     */     else
/* 1715:     */     {
/* 1716:2359 */       Dfp inc = newInstance(getOne());
/* 1717:2360 */       inc.exp = this.exp;
/* 1718:2361 */       inc.sign = this.sign;
/* 1719:2363 */       if (equals(inc)) {
/* 1720:2364 */         this.exp -= this.mant.length;
/* 1721:     */       } else {
/* 1722:2366 */         inc.exp = (this.exp - this.mant.length + 1);
/* 1723:     */       }
/* 1724:2369 */       if (equals(getZero())) {
/* 1725:2370 */         inc.exp = (-32767 - this.mant.length);
/* 1726:     */       }
/* 1727:2373 */       result = subtract(inc);
/* 1728:     */     }
/* 1729:2376 */     if ((result.classify() == 1) && (classify() != 1))
/* 1730:     */     {
/* 1731:2377 */       this.field.setIEEEFlagsBits(16);
/* 1732:2378 */       result = dotrap(16, "nextAfter", x, result);
/* 1733:     */     }
/* 1734:2381 */     if ((result.equals(getZero())) && (!equals(getZero())))
/* 1735:     */     {
/* 1736:2382 */       this.field.setIEEEFlagsBits(16);
/* 1737:2383 */       result = dotrap(16, "nextAfter", x, result);
/* 1738:     */     }
/* 1739:2386 */     return result;
/* 1740:     */   }
/* 1741:     */   
/* 1742:     */   public double toDouble()
/* 1743:     */   {
/* 1744:2396 */     if (isInfinite())
/* 1745:     */     {
/* 1746:2397 */       if (lessThan(getZero())) {
/* 1747:2398 */         return (-1.0D / 0.0D);
/* 1748:     */       }
/* 1749:2400 */       return (1.0D / 0.0D);
/* 1750:     */     }
/* 1751:2404 */     if (isNaN()) {
/* 1752:2405 */       return (0.0D / 0.0D);
/* 1753:     */     }
/* 1754:2408 */     Dfp y = this;
/* 1755:2409 */     boolean negate = false;
/* 1756:2410 */     int cmp0 = compare(this, getZero());
/* 1757:2411 */     if (cmp0 == 0) {
/* 1758:2412 */       return this.sign < 0 ? -0.0D : 0.0D;
/* 1759:     */     }
/* 1760:2413 */     if (cmp0 < 0)
/* 1761:     */     {
/* 1762:2414 */       y = negate();
/* 1763:2415 */       negate = true;
/* 1764:     */     }
/* 1765:2420 */     int exponent = (int)(y.log10() * 3.32D);
/* 1766:2421 */     if (exponent < 0) {
/* 1767:2422 */       exponent--;
/* 1768:     */     }
/* 1769:2425 */     Dfp tempDfp = DfpMath.pow(getTwo(), exponent);
/* 1770:2426 */     while ((tempDfp.lessThan(y)) || (tempDfp.equals(y)))
/* 1771:     */     {
/* 1772:2427 */       tempDfp = tempDfp.multiply(2);
/* 1773:2428 */       exponent++;
/* 1774:     */     }
/* 1775:2430 */     exponent--;
/* 1776:     */     
/* 1777:     */ 
/* 1778:     */ 
/* 1779:2434 */     y = y.divide(DfpMath.pow(getTwo(), exponent));
/* 1780:2435 */     if (exponent > -1023) {
/* 1781:2436 */       y = y.subtract(getOne());
/* 1782:     */     }
/* 1783:2439 */     if (exponent < -1074) {
/* 1784:2440 */       return 0.0D;
/* 1785:     */     }
/* 1786:2443 */     if (exponent > 1023) {
/* 1787:2444 */       return negate ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/* 1788:     */     }
/* 1789:2448 */     y = y.multiply(newInstance(4503599627370496L)).rint();
/* 1790:2449 */     String str = y.toString();
/* 1791:2450 */     str = str.substring(0, str.length() - 1);
/* 1792:2451 */     long mantissa = Long.parseLong(str);
/* 1793:2453 */     if (mantissa == 4503599627370496L)
/* 1794:     */     {
/* 1795:2455 */       mantissa = 0L;
/* 1796:2456 */       exponent++;
/* 1797:     */     }
/* 1798:2460 */     if (exponent <= -1023) {
/* 1799:2461 */       exponent--;
/* 1800:     */     }
/* 1801:2464 */     while (exponent < -1023)
/* 1802:     */     {
/* 1803:2465 */       exponent++;
/* 1804:2466 */       mantissa >>>= 1;
/* 1805:     */     }
/* 1806:2469 */     long bits = mantissa | exponent + 1023L << 52;
/* 1807:2470 */     double x = Double.longBitsToDouble(bits);
/* 1808:2472 */     if (negate) {
/* 1809:2473 */       x = -x;
/* 1810:     */     }
/* 1811:2476 */     return x;
/* 1812:     */   }
/* 1813:     */   
/* 1814:     */   public double[] toSplitDouble()
/* 1815:     */   {
/* 1816:2485 */     double[] split = new double[2];
/* 1817:2486 */     long mask = -1073741824L;
/* 1818:     */     
/* 1819:2488 */     split[0] = Double.longBitsToDouble(Double.doubleToLongBits(toDouble()) & mask);
/* 1820:2489 */     split[1] = subtract(newInstance(split[0])).toDouble();
/* 1821:     */     
/* 1822:2491 */     return split;
/* 1823:     */   }
/* 1824:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.dfp.Dfp
 * JD-Core Version:    0.7.0.1
 */
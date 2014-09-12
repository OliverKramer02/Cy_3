/*    1:     */ package org.apache.commons.math3.util;
/*    2:     */ 
/*    3:     */ import java.io.PrintStream;
/*    4:     */ 
/*    5:     */ public class FastMath
/*    6:     */ {
/*    7:     */   public static final double PI = 3.141592653589793D;
/*    8:     */   public static final double E = 2.718281828459045D;
/*    9:     */   static final int EXP_INT_TABLE_MAX_INDEX = 750;
/*   10:     */   static final int EXP_INT_TABLE_LEN = 1500;
/*   11:     */   static final int LN_MANT_LEN = 1024;
/*   12:     */   static final int EXP_FRAC_TABLE_LEN = 1025;
/*   13:     */   private static final boolean RECOMPUTE_TABLES_AT_RUNTIME = false;
/*   14:     */   private static final double LN_2_A = 0.6931470632553101D;
/*   15:     */   private static final double LN_2_B = 1.173046352508235E-007D;
/*   16: 113 */   private static final double[][] LN_QUICK_COEF = { { 1.0D, 5.669184079525E-024D }, { -0.25D, -0.25D }, { 0.3333333134651184D, 1.986821492305628E-008D }, { -0.25D, -6.663542893624021E-014D }, { 0.199999988079071D, 1.192105680146323E-008D }, { -0.166666656732559D, -7.800414592973399E-009D }, { 0.142857134342194D, 5.650007086920087E-009D }, { -0.1250253021717072D, -7.44321345601866E-011D }, { 0.1111380755901337D, 9.219544613762692E-009D } };
/*   17: 126 */   private static final double[][] LN_HI_PREC_COEF = { { 1.0D, -6.032174644509064E-023D }, { -0.25D, -0.25D }, { 0.3333333134651184D, 1.986816177772435E-008D }, { -0.2499999701976776D, -2.957007209750105E-008D }, { 0.1999995410442352D, 1.583099333206127E-010D }, { -0.1662487983703613D, -2.603382435519167E-008D } };
/*   18:     */   private static final int SINE_TABLE_LEN = 14;
/*   19: 139 */   private static final double[] SINE_TABLE_A = { 0.0D, 0.1246747374534607D, 0.2474039494991303D, 0.366272509098053D, 0.4794255495071411D, 0.5850973129272461D, 0.6816387176513672D, 0.7675435543060303D, 0.8414709568023682D, 0.9022675752639771D, 0.9489846229553223D, 0.9808930158615112D, 0.9974949359893799D, 0.9985313415527344D };
/*   20: 158 */   private static final double[] SINE_TABLE_B = { 0.0D, -4.068233003401932E-009D, 9.755392680573412E-009D, 1.998799458285729E-008D, -1.090293811300796E-008D, -3.99867839389446E-008D, 4.23719669792332E-008D, -5.207000323380292E-008D, 2.800552834259E-008D, 1.883511811213715E-008D, -3.599736051276557E-009D, 4.116164446561962E-008D, 5.061467454812738E-008D, -1.012902791249686E-009D };
/*   21: 177 */   private static final double[] COSINE_TABLE_A = { 1.0D, 0.9921976327896118D, 0.9689123630523682D, 0.9305076599121094D, 0.8775825500488281D, 0.8109631538391113D, 0.7316888570785523D, 0.6409968137741089D, 0.5403022766113281D, 0.4311765432357788D, 0.3153223395347595D, 0.194547712802887D, 0.07073719799518585D, -0.05417713522911072D };
/*   22: 196 */   private static final double[] COSINE_TABLE_B = { 0.0D, 3.443971723674285E-008D, 5.865827662008209E-008D, -3.799979508385053E-008D, 1.184154459111628E-008D, -3.43338934259355E-008D, 1.179526864021679E-008D, 4.438921624363781E-008D, 2.925681159240093E-008D, -2.643711263204181E-008D, 2.286050914396312E-008D, -4.813899778443457E-009D, 3.672517058035558E-009D, 2.021743975633808E-010D };
/*   23: 216 */   private static final double[] TANGENT_TABLE_A = { 0.0D, 0.1256551444530487D, 0.2553419470787048D, 0.3936265707015991D, 0.546302437782288D, 0.7214844226837158D, 0.9315965175628662D, 1.197421550750732D, 1.55740761756897D, 2.092571258544922D, 3.009569644927979D, 5.041914939880371D, 14.101419448852539D, -18.430862426757813D };
/*   24: 235 */   private static final double[] TANGENT_TABLE_B = { 0.0D, -7.877917738262007E-009D, -2.585766856747989E-008D, 5.224033637135667E-009D, 5.206150291559893E-008D, 1.830718859967703E-008D, -5.761879374977071E-008D, 7.848361555046424E-008D, 1.070859325039445E-007D, 1.782725712942381E-008D, 2.893485277253286E-008D, 3.166009922273796E-007D, 4.983191803254889E-007D, -3.356118100840571E-007D };
/*   25: 254 */   private static final long[] RECIP_2PI = { 2935890503282001226L, 9154082963658192752L, 3952090531849364496L, 9193070505571053912L, 7910884519577875640L, 113236205062349959L, 4577762542105553359L, -5034868814120038111L, 4208363204685324176L, 5648769086999809661L, 2819561105158720014L, -4035746434778044925L, -302932621132653753L, -2644281811660520851L, -3183605296591799669L, 6722166367014452318L, -3512299194304650054L, -7278142539171889152L };
/*   26: 275 */   private static final long[] PI_O_4_BITS = { -3958705157555305932L, -4267615245585081135L };
/*   27: 283 */   private static final double[] EIGHTHS = { 0.0D, 0.125D, 0.25D, 0.375D, 0.5D, 0.625D, 0.75D, 0.875D, 1.0D, 1.125D, 1.25D, 1.375D, 1.5D, 1.625D };
/*   28: 286 */   private static final double[] CBRTTWO = { 0.629960524947437D, 0.7937005259840998D, 1.0D, 1.259921049894873D, 1.587401051968199D };
/*   29:     */   private static final long HEX_40000000 = 1073741824L;
/*   30:     */   private static final long MASK_30BITS = -1073741824L;
/*   31:     */   private static final double TWO_POWER_52 = 4503599627370496.0D;
/*   32:     */   private static final double F_1_3 = 0.3333333333333333D;
/*   33:     */   private static final double F_1_5 = 0.2D;
/*   34:     */   private static final double F_1_7 = 0.1428571428571429D;
/*   35:     */   private static final double F_1_9 = 0.111111111111111D;
/*   36:     */   private static final double F_1_11 = 0.09090909090909091D;
/*   37:     */   private static final double F_1_13 = 0.07692307692307693D;
/*   38:     */   private static final double F_1_15 = 0.06666666666666667D;
/*   39:     */   private static final double F_1_17 = 0.05882352941176471D;
/*   40:     */   private static final double F_3_4 = 0.75D;
/*   41:     */   private static final double F_15_16 = 0.9375D;
/*   42:     */   private static final double F_13_14 = 0.928571428571429D;
/*   43:     */   private static final double F_11_12 = 0.9166666666666666D;
/*   44:     */   private static final double F_9_10 = 0.9D;
/*   45:     */   private static final double F_7_8 = 0.875D;
/*   46:     */   private static final double F_5_6 = 0.8333333333333334D;
/*   47:     */   private static final double F_1_2 = 0.5D;
/*   48:     */   private static final double F_1_4 = 0.25D;
/*   49:     */   
/*   50:     */   private static double doubleHighPart(double d)
/*   51:     */   {
/*   52: 361 */     if ((d > -2.225073858507201E-308D) && (d < 2.225073858507201E-308D)) {
/*   53: 362 */       return d;
/*   54:     */     }
/*   55: 364 */     long xl = Double.doubleToLongBits(d);
/*   56: 365 */     xl &= 0xC0000000;
/*   57: 366 */     return Double.longBitsToDouble(xl);
/*   58:     */   }
/*   59:     */   
/*   60:     */   public static double sqrt(double a)
/*   61:     */   {
/*   62: 375 */     return Math.sqrt(a);
/*   63:     */   }
/*   64:     */   
/*   65:     */   public static double cosh(double x)
/*   66:     */   {
/*   67: 383 */     if (x != x) {
/*   68: 384 */       return x;
/*   69:     */     }
/*   70: 392 */     if (x > 20.0D) {
/*   71: 393 */       return exp(x) / 2.0D;
/*   72:     */     }
/*   73: 396 */     if (x < -20.0D) {
/*   74: 397 */       return exp(-x) / 2.0D;
/*   75:     */     }
/*   76: 400 */     double[] hiPrec = new double[2];
/*   77: 401 */     if (x < 0.0D) {
/*   78: 402 */       x = -x;
/*   79:     */     }
/*   80: 404 */     exp(x, 0.0D, hiPrec);
/*   81:     */     
/*   82: 406 */     double ya = hiPrec[0] + hiPrec[1];
/*   83: 407 */     double yb = -(ya - hiPrec[0] - hiPrec[1]);
/*   84:     */     
/*   85: 409 */     double temp = ya * 1073741824.0D;
/*   86: 410 */     double yaa = ya + temp - temp;
/*   87: 411 */     double yab = ya - yaa;
/*   88:     */     
/*   89:     */ 
/*   90: 414 */     double recip = 1.0D / ya;
/*   91: 415 */     temp = recip * 1073741824.0D;
/*   92: 416 */     double recipa = recip + temp - temp;
/*   93: 417 */     double recipb = recip - recipa;
/*   94:     */     
/*   95:     */ 
/*   96: 420 */     recipb += (1.0D - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
/*   97:     */     
/*   98: 422 */     recipb += -yb * recip * recip;
/*   99:     */     
/*  100:     */ 
/*  101: 425 */     temp = ya + recipa;
/*  102: 426 */     yb += -(temp - ya - recipa);
/*  103: 427 */     ya = temp;
/*  104: 428 */     temp = ya + recipb;
/*  105: 429 */     yb += -(temp - ya - recipb);
/*  106: 430 */     ya = temp;
/*  107:     */     
/*  108: 432 */     double result = ya + yb;
/*  109: 433 */     result *= 0.5D;
/*  110: 434 */     return result;
/*  111:     */   }
/*  112:     */   
/*  113:     */   public static double sinh(double x)
/*  114:     */   {
/*  115: 442 */     boolean negate = false;
/*  116: 443 */     if (x != x) {
/*  117: 444 */       return x;
/*  118:     */     }
/*  119: 452 */     if (x > 20.0D) {
/*  120: 453 */       return exp(x) / 2.0D;
/*  121:     */     }
/*  122: 456 */     if (x < -20.0D) {
/*  123: 457 */       return -exp(-x) / 2.0D;
/*  124:     */     }
/*  125: 460 */     if (x == 0.0D) {
/*  126: 461 */       return x;
/*  127:     */     }
/*  128: 464 */     if (x < 0.0D)
/*  129:     */     {
/*  130: 465 */       x = -x;
/*  131: 466 */       negate = true;
/*  132:     */     }
/*  133:     */     double result = 0;
/*  134: 471 */     if (x > 0.25D)
/*  135:     */     {
/*  136: 472 */       double[] hiPrec = new double[2];
/*  137: 473 */       exp(x, 0.0D, hiPrec);
/*  138:     */       
/*  139: 475 */       double ya = hiPrec[0] + hiPrec[1];
/*  140: 476 */       double yb = -(ya - hiPrec[0] - hiPrec[1]);
/*  141:     */       
/*  142: 478 */       double temp = ya * 1073741824.0D;
/*  143: 479 */       double yaa = ya + temp - temp;
/*  144: 480 */       double yab = ya - yaa;
/*  145:     */       
/*  146:     */ 
/*  147: 483 */       double recip = 1.0D / ya;
/*  148: 484 */       temp = recip * 1073741824.0D;
/*  149: 485 */       double recipa = recip + temp - temp;
/*  150: 486 */       double recipb = recip - recipa;
/*  151:     */       
/*  152:     */ 
/*  153: 489 */       recipb += (1.0D - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
/*  154:     */       
/*  155: 491 */       recipb += -yb * recip * recip;
/*  156:     */       
/*  157: 493 */       recipa = -recipa;
/*  158: 494 */       recipb = -recipb;
/*  159:     */       
/*  160:     */ 
/*  161: 497 */       temp = ya + recipa;
/*  162: 498 */       yb += -(temp - ya - recipa);
/*  163: 499 */       ya = temp;
/*  164: 500 */       temp = ya + recipb;
/*  165: 501 */       yb += -(temp - ya - recipb);
/*  166: 502 */       ya = temp;
/*  167:     */       
/*  168: 504 */       
/*  169: 505 */       result *= 0.5D;
/*  170:     */     }
/*  171:     */     else
/*  172:     */     {
/*  173: 508 */       double[] hiPrec = new double[2];
/*  174: 509 */       expm1(x, hiPrec);
/*  175:     */       
/*  176: 511 */       double ya = hiPrec[0] + hiPrec[1];
/*  177: 512 */       double yb = -(ya - hiPrec[0] - hiPrec[1]);
/*  178:     */       
/*  179:     */ 
/*  180: 515 */       double denom = 1.0D + ya;
/*  181: 516 */       double denomr = 1.0D / denom;
/*  182: 517 */       double denomb = -(denom - 1.0D - ya) + yb;
/*  183: 518 */       double ratio = ya * denomr;
/*  184: 519 */       double temp = ratio * 1073741824.0D;
/*  185: 520 */       double ra = ratio + temp - temp;
/*  186: 521 */       double rb = ratio - ra;
/*  187:     */       
/*  188: 523 */       temp = denom * 1073741824.0D;
/*  189: 524 */       double za = denom + temp - temp;
/*  190: 525 */       double zb = denom - za;
/*  191:     */       
/*  192: 527 */       rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;
/*  193:     */       
/*  194:     */ 
/*  195: 530 */       rb += yb * denomr;
/*  196: 531 */       rb += -ya * denomb * denomr * denomr;
/*  197:     */       
/*  198:     */ 
/*  199: 534 */       temp = ya + ra;
/*  200: 535 */       yb += -(temp - ya - ra);
/*  201: 536 */       ya = temp;
/*  202: 537 */       temp = ya + rb;
/*  203: 538 */       yb += -(temp - ya - rb);
/*  204: 539 */       ya = temp;
/*  205:     */       
/*  206: 541 */       result = ya + yb;
/*  207: 542 */       result *= 0.5D;
/*  208:     */     }
/*  209: 545 */     if (negate) {
/*  210: 546 */       result = -result;
/*  211:     */     }
/*  212: 549 */     return result;
/*  213:     */   }
/*  214:     */   
/*  215:     */   public static double tanh(double x)
/*  216:     */   {
/*  217: 557 */     boolean negate = false;
/*  218: 559 */     if (x != x) {
/*  219: 560 */       return x;
/*  220:     */     }
/*  221: 569 */     if (x > 20.0D) {
/*  222: 570 */       return 1.0D;
/*  223:     */     }
/*  224: 573 */     if (x < -20.0D) {
/*  225: 574 */       return -1.0D;
/*  226:     */     }
/*  227: 577 */     if (x == 0.0D) {
/*  228: 578 */       return x;
/*  229:     */     }
/*  230: 581 */     if (x < 0.0D)
/*  231:     */     {
/*  232: 582 */       x = -x;
/*  233: 583 */       negate = true;
/*  234:     */     }
/*  235:     */     double result;
/*  236:     */    
/*  237: 587 */     if (x >= 0.5D)
/*  238:     */     {
/*  239: 588 */       double[] hiPrec = new double[2];
/*  240:     */       
/*  241: 590 */       exp(x * 2.0D, 0.0D, hiPrec);
/*  242:     */       
/*  243: 592 */       double ya = hiPrec[0] + hiPrec[1];
/*  244: 593 */       double yb = -(ya - hiPrec[0] - hiPrec[1]);
/*  245:     */       
/*  246:     */ 
/*  247: 596 */       double na = -1.0D + ya;
/*  248: 597 */       double nb = -(na + 1.0D - ya);
/*  249: 598 */       double temp = na + yb;
/*  250: 599 */       nb += -(temp - na - yb);
/*  251: 600 */       na = temp;
/*  252:     */       
/*  253:     */ 
/*  254: 603 */       double da = 1.0D + ya;
/*  255: 604 */       double db = -(da - 1.0D - ya);
/*  256: 605 */       temp = da + yb;
/*  257: 606 */       db += -(temp - da - yb);
/*  258: 607 */       da = temp;
/*  259:     */       
/*  260: 609 */       temp = da * 1073741824.0D;
/*  261: 610 */       double daa = da + temp - temp;
/*  262: 611 */       double dab = da - daa;
/*  263:     */       
/*  264:     */ 
/*  265: 614 */       double ratio = na / da;
/*  266: 615 */       temp = ratio * 1073741824.0D;
/*  267: 616 */       double ratioa = ratio + temp - temp;
/*  268: 617 */       double ratiob = ratio - ratioa;
/*  269:     */       
/*  270:     */ 
/*  271: 620 */       ratiob += (na - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / da;
/*  272:     */       
/*  273:     */ 
/*  274: 623 */       ratiob += nb / da;
/*  275:     */       
/*  276: 625 */       ratiob += -db * na / da / da;
/*  277:     */       
/*  278: 627 */       result = ratioa + ratiob;
/*  279:     */     }
/*  280:     */     else
/*  281:     */     {
/*  282: 630 */       double[] hiPrec = new double[2];
/*  283:     */       
/*  284: 632 */       expm1(x * 2.0D, hiPrec);
/*  285:     */       
/*  286: 634 */       double ya = hiPrec[0] + hiPrec[1];
/*  287: 635 */       double yb = -(ya - hiPrec[0] - hiPrec[1]);
/*  288:     */       
/*  289:     */ 
/*  290: 638 */       double na = ya;
/*  291: 639 */       double nb = yb;
/*  292:     */       
/*  293:     */ 
/*  294: 642 */       double da = 2.0D + ya;
/*  295: 643 */       double db = -(da - 2.0D - ya);
/*  296: 644 */       double temp = da + yb;
/*  297: 645 */       db += -(temp - da - yb);
/*  298: 646 */       da = temp;
/*  299:     */       
/*  300: 648 */       temp = da * 1073741824.0D;
/*  301: 649 */       double daa = da + temp - temp;
/*  302: 650 */       double dab = da - daa;
/*  303:     */       
/*  304:     */ 
/*  305: 653 */       double ratio = na / da;
/*  306: 654 */       temp = ratio * 1073741824.0D;
/*  307: 655 */       double ratioa = ratio + temp - temp;
/*  308: 656 */       double ratiob = ratio - ratioa;
/*  309:     */       
/*  310:     */ 
/*  311: 659 */       ratiob += (na - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / da;
/*  312:     */       
/*  313:     */ 
/*  314: 662 */       ratiob += nb / da;
/*  315:     */       
/*  316: 664 */       ratiob += -db * na / da / da;
/*  317:     */       
/*  318: 666 */       result = ratioa + ratiob;
/*  319:     */     }
/*  320: 669 */     if (negate) {
/*  321: 670 */       result = -result;
/*  322:     */     }
/*  323: 673 */     return result;
/*  324:     */   }
/*  325:     */   
/*  326:     */   public static double acosh(double a)
/*  327:     */   {
/*  328: 681 */     return log(a + sqrt(a * a - 1.0D));
/*  329:     */   }
/*  330:     */   
/*  331:     */   public static double asinh(double a)
/*  332:     */   {
/*  333: 689 */     boolean negative = false;
/*  334: 690 */     if (a < 0.0D)
/*  335:     */     {
/*  336: 691 */       negative = true;
/*  337: 692 */       a = -a;
/*  338:     */     }
/*  339:     */     double absAsinh;
/*  340:     */    
/*  341: 696 */     if (a > 0.167D)
/*  342:     */     {
/*  343: 697 */       absAsinh = log(sqrt(a * a + 1.0D) + a);
/*  344:     */     }
/*  345:     */     else
/*  346:     */     {
/*  347: 699 */       double a2 = a * a;
/*  348:     */      
/*  349: 700 */       if (a > 0.097D)
/*  350:     */       {
/*  351: 701 */         absAsinh = a * (1.0D - a2 * (0.3333333333333333D - a2 * (0.2D - a2 * (0.1428571428571429D - a2 * (0.111111111111111D - a2 * (0.09090909090909091D - a2 * (0.07692307692307693D - a2 * (0.06666666666666667D - a2 * 0.05882352941176471D * 0.9375D) * 0.928571428571429D) * 0.9166666666666666D) * 0.9D) * 0.875D) * 0.8333333333333334D) * 0.75D) * 0.5D);
/*  352:     */       }
/*  353:     */       else
/*  354:     */       {
/*  355:     */         
/*  356: 702 */         if (a > 0.036D)
/*  357:     */         {
/*  358: 703 */           absAsinh = a * (1.0D - a2 * (0.3333333333333333D - a2 * (0.2D - a2 * (0.1428571428571429D - a2 * (0.111111111111111D - a2 * (0.09090909090909091D - a2 * 0.07692307692307693D * 0.9166666666666666D) * 0.9D) * 0.875D) * 0.8333333333333334D) * 0.75D) * 0.5D);
/*  359:     */         }
/*  360:     */         else
/*  361:     */         {
/*  362:     */          
/*  363: 704 */           if (a > 0.0036D) {
/*  364: 705 */             absAsinh = a * (1.0D - a2 * (0.3333333333333333D - a2 * (0.2D - a2 * (0.1428571428571429D - a2 * 0.111111111111111D * 0.875D) * 0.8333333333333334D) * 0.75D) * 0.5D);
/*  365:     */           } else {
/*  366: 707 */             absAsinh = a * (1.0D - a2 * (0.3333333333333333D - a2 * 0.2D * 0.75D) * 0.5D);
/*  367:     */           }
/*  368:     */         }
/*  369:     */       }
/*  370:     */     }
/*  371: 711 */     return negative ? -absAsinh : absAsinh;
/*  372:     */   }
/*  373:     */   
/*  374:     */   public static double atanh(double a)
/*  375:     */   {
/*  376: 719 */     boolean negative = false;
/*  377: 720 */     if (a < 0.0D)
/*  378:     */     {
/*  379: 721 */       negative = true;
/*  380: 722 */       a = -a;
/*  381:     */     }
/*  382:     */     double absAtanh;
/*  383:     */   
/*  384: 726 */     if (a > 0.15D)
/*  385:     */     {
/*  386: 727 */       absAtanh = 0.5D * log((1.0D + a) / (1.0D - a));
/*  387:     */     }
/*  388:     */     else
/*  389:     */     {
/*  390: 729 */       double a2 = a * a;
/*  391:     */      
/*  392: 730 */       if (a > 0.08699999999999999D)
/*  393:     */       {
/*  394: 731 */         absAtanh = a * (1.0D + a2 * (0.3333333333333333D + a2 * (0.2D + a2 * (0.1428571428571429D + a2 * (0.111111111111111D + a2 * (0.09090909090909091D + a2 * (0.07692307692307693D + a2 * (0.06666666666666667D + a2 * 0.05882352941176471D))))))));
/*  395:     */       }
/*  396:     */       else
/*  397:     */       {
/*  398:     */        
/*  399: 732 */         if (a > 0.031D)
/*  400:     */         {
/*  401: 733 */           absAtanh = a * (1.0D + a2 * (0.3333333333333333D + a2 * (0.2D + a2 * (0.1428571428571429D + a2 * (0.111111111111111D + a2 * (0.09090909090909091D + a2 * 0.07692307692307693D))))));
/*  402:     */         }
/*  403:     */         else
/*  404:     */         {
/*  405:     */        
/*  406: 734 */           if (a > 0.003D) {
/*  407: 735 */             absAtanh = a * (1.0D + a2 * (0.3333333333333333D + a2 * (0.2D + a2 * (0.1428571428571429D + a2 * 0.111111111111111D))));
/*  408:     */           } else {
/*  409: 737 */             absAtanh = a * (1.0D + a2 * (0.3333333333333333D + a2 * 0.2D));
/*  410:     */           }
/*  411:     */         }
/*  412:     */       }
/*  413:     */     }
/*  414: 741 */     return negative ? -absAtanh : absAtanh;
/*  415:     */   }
/*  416:     */   
/*  417:     */   public static double signum(double a)
/*  418:     */   {
/*  419: 750 */     return a > 0.0D ? 1.0D : a < 0.0D ? -1.0D : a;
/*  420:     */   }
/*  421:     */   
/*  422:     */   public static float signum(float a)
/*  423:     */   {
/*  424: 759 */     return a > 0.0F ? 1.0F : a < 0.0F ? -1.0F : a;
/*  425:     */   }
/*  426:     */   
/*  427:     */   public static double nextUp(double a)
/*  428:     */   {
/*  429: 767 */     return nextAfter(a, (1.0D / 0.0D));
/*  430:     */   }
/*  431:     */   
/*  432:     */   public static float nextUp(float a)
/*  433:     */   {
/*  434: 775 */     return nextAfter(a, (1.0D / 0.0D));
/*  435:     */   }
/*  436:     */   
/*  437:     */   public static double random()
/*  438:     */   {
/*  439: 783 */     return Math.random();
/*  440:     */   }
/*  441:     */   
/*  442:     */   public static double exp(double x)
/*  443:     */   {
/*  444: 807 */     return exp(x, 0.0D, null);
/*  445:     */   }
/*  446:     */   
/*  447:     */   private static double exp(double x, double extra, double[] hiPrec)
/*  448:     */   {
/*  449:     */     int intVal;
/*  450:     */     double intPartA;
/*  451:     */     double intPartB;
/*  452: 826 */     if (x < 0.0D)
/*  453:     */     {
/*  454: 827 */       intVal = (int)-x;
/*  455: 829 */       if (intVal > 746)
/*  456:     */       {
/*  457: 830 */         if (hiPrec != null)
/*  458:     */         {
/*  459: 831 */           hiPrec[0] = 0.0D;
/*  460: 832 */           hiPrec[1] = 0.0D;
/*  461:     */         }
/*  462: 834 */         return 0.0D;
/*  463:     */       }
/*  464: 837 */       if (intVal > 709)
/*  465:     */       {
/*  466: 839 */         double result = exp(x + 40.19140625D, extra, hiPrec) / 2.850400951440118E+017D;
/*  467: 840 */         if (hiPrec != null)
/*  468:     */         {
/*  469: 841 */           hiPrec[0] /= 2.850400951440118E+017D;
/*  470: 842 */           hiPrec[1] /= 2.850400951440118E+017D;
/*  471:     */         }
/*  472: 844 */         return result;
/*  473:     */       }
/*  474: 847 */       if (intVal == 709)
/*  475:     */       {
/*  476: 849 */         double result = exp(x + 1.494140625D, extra, hiPrec) / 4.455505956692757D;
/*  477: 850 */         if (hiPrec != null)
/*  478:     */         {
/*  479: 851 */           hiPrec[0] /= 4.455505956692757D;
/*  480: 852 */           hiPrec[1] /= 4.455505956692757D;
/*  481:     */         }
/*  482: 854 */         return result;
/*  483:     */       }
/*  484: 857 */       intVal++;
/*  485:     */       
/*  486: 859 */       intPartA = ExpIntTable.EXP_INT_TABLE_A[(750 - intVal)];
/*  487: 860 */       intPartB = ExpIntTable.EXP_INT_TABLE_B[(750 - intVal)];
/*  488:     */       
/*  489: 862 */       intVal = -intVal;
/*  490:     */     }
/*  491:     */     else
/*  492:     */     {
/*  493: 864 */       intVal = (int)x;
/*  494: 866 */       if (intVal > 709)
/*  495:     */       {
/*  496: 867 */         if (hiPrec != null)
/*  497:     */         {
/*  498: 868 */           hiPrec[0] = (1.0D / 0.0D);
/*  499: 869 */           hiPrec[1] = 0.0D;
/*  500:     */         }
/*  501: 871 */         return (1.0D / 0.0D);
/*  502:     */       }
/*  503: 874 */       intPartA = ExpIntTable.EXP_INT_TABLE_A[(750 + intVal)];
/*  504: 875 */       intPartB = ExpIntTable.EXP_INT_TABLE_B[(750 + intVal)];
/*  505:     */     }
/*  506: 882 */     int intFrac = (int)((x - intVal) * 1024.0D);
/*  507: 883 */     double fracPartA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac];
/*  508: 884 */     double fracPartB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
/*  509:     */     
/*  510:     */ 
/*  511:     */ 
/*  512:     */ 
/*  513:     */ 
/*  514: 890 */     double epsilon = x - (intVal + intFrac / 1024.0D);
/*  515:     */     
/*  516:     */ 
/*  517:     */ 
/*  518:     */ 
/*  519:     */ 
/*  520:     */ 
/*  521:     */ 
/*  522:     */ 
/*  523: 899 */     double z = 0.04168701738764507D;
/*  524: 900 */     z = z * epsilon + 0.1666666505023083D;
/*  525: 901 */     z = z * epsilon + 0.500000000004269D;
/*  526: 902 */     z = z * epsilon + 1.0D;
/*  527: 903 */     z = z * epsilon + -3.940510424527919E-020D;
/*  528:     */     
/*  529:     */ 
/*  530:     */ 
/*  531:     */ 
/*  532:     */ 
/*  533:     */ 
/*  534: 910 */     double tempA = intPartA * fracPartA;
/*  535: 911 */     double tempB = intPartA * fracPartB + intPartB * fracPartA + intPartB * fracPartB;
/*  536:     */     
/*  537:     */ 
/*  538:     */ 
/*  539:     */ 
/*  540:     */ 
/*  541: 917 */     double tempC = tempB + tempA;
/*  542:     */     double result;
/*  543:     */  
/*  544: 919 */     if (extra != 0.0D) {
/*  545: 920 */       result = tempC * extra * z + tempC * extra + tempC * z + tempB + tempA;
/*  546:     */     } else {
/*  547: 922 */       result = tempC * z + tempB + tempA;
/*  548:     */     }
/*  549: 925 */     if (hiPrec != null)
/*  550:     */     {
/*  551: 927 */       hiPrec[0] = tempA;
/*  552: 928 */       hiPrec[1] = (tempC * extra * z + tempC * extra + tempC * z + tempB);
/*  553:     */     }
/*  554: 931 */     return result;
/*  555:     */   }
/*  556:     */   
/*  557:     */   public static double expm1(double x)
/*  558:     */   {
/*  559: 939 */     return expm1(x, null);
/*  560:     */   }
/*  561:     */   
/*  562:     */   private static double expm1(double x, double[] hiPrecOut)
/*  563:     */   {
/*  564: 948 */     if ((x != x) || (x == 0.0D)) {
/*  565: 949 */       return x;
/*  566:     */     }
/*  567: 952 */     if ((x <= -1.0D) || (x >= 1.0D))
/*  568:     */     {
/*  569: 955 */       double[] hiPrec = new double[2];
/*  570: 956 */       exp(x, 0.0D, hiPrec);
/*  571: 957 */       if (x > 0.0D) {
/*  572: 958 */         return -1.0D + hiPrec[0] + hiPrec[1];
/*  573:     */       }
/*  574: 960 */       double ra = -1.0D + hiPrec[0];
/*  575: 961 */       double rb = -(ra + 1.0D - hiPrec[0]);
/*  576: 962 */       rb += hiPrec[1];
/*  577: 963 */       return ra + rb;
/*  578:     */     }
/*  579: 970 */     boolean negative = false;
/*  580: 972 */     if (x < 0.0D)
/*  581:     */     {
/*  582: 973 */       x = -x;
/*  583: 974 */       negative = true;
/*  584:     */     }
/*  585: 978 */     int intFrac = (int)(x * 1024.0D);
/*  586: 979 */     double tempA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac] - 1.0D;
/*  587: 980 */     double tempB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
/*  588:     */     
/*  589: 982 */     double temp = tempA + tempB;
/*  590: 983 */     tempB = -(temp - tempA - tempB);
/*  591: 984 */     tempA = temp;
/*  592:     */     
/*  593: 986 */     temp = tempA * 1073741824.0D;
/*  594: 987 */     double baseA = tempA + temp - temp;
/*  595: 988 */     double baseB = tempB + (tempA - baseA);
/*  596:     */     
/*  597: 990 */     double epsilon = x - intFrac / 1024.0D;
/*  598:     */     
/*  599:     */ 
/*  600:     */ 
/*  601:     */ 
/*  602: 995 */     double zb = 0.008336750013465571D;
/*  603: 996 */     zb = zb * epsilon + 0.04166666387918665D;
/*  604: 997 */     zb = zb * epsilon + 0.1666666666674539D;
/*  605: 998 */     zb = zb * epsilon + 0.4999999999999999D;
/*  606: 999 */     zb *= epsilon;
/*  607:1000 */     zb *= epsilon;
/*  608:     */     
/*  609:1002 */     double za = epsilon;
/*  610:1003 */     temp = za + zb;
/*  611:1004 */     zb = -(temp - za - zb);
/*  612:1005 */     za = temp;
/*  613:     */     
/*  614:1007 */     temp = za * 1073741824.0D;
/*  615:1008 */     temp = za + temp - temp;
/*  616:1009 */     zb += za - temp;
/*  617:1010 */     za = temp;
/*  618:     */     
/*  619:     */ 
/*  620:1013 */     double ya = za * baseA;
/*  621:     */     
/*  622:1015 */     temp = ya + za * baseB;
/*  623:1016 */     double yb = -(temp - ya - za * baseB);
/*  624:1017 */     ya = temp;
/*  625:     */     
/*  626:1019 */     temp = ya + zb * baseA;
/*  627:1020 */     yb += -(temp - ya - zb * baseA);
/*  628:1021 */     ya = temp;
/*  629:     */     
/*  630:1023 */     temp = ya + zb * baseB;
/*  631:1024 */     yb += -(temp - ya - zb * baseB);
/*  632:1025 */     ya = temp;
/*  633:     */     
/*  634:     */ 
/*  635:     */ 
/*  636:1029 */     temp = ya + baseA;
/*  637:1030 */     yb += -(temp - baseA - ya);
/*  638:1031 */     ya = temp;
/*  639:     */     
/*  640:1033 */     temp = ya + za;
/*  641:     */     
/*  642:1035 */     yb += -(temp - ya - za);
/*  643:1036 */     ya = temp;
/*  644:     */     
/*  645:1038 */     temp = ya + baseB;
/*  646:     */     
/*  647:1040 */     yb += -(temp - ya - baseB);
/*  648:1041 */     ya = temp;
/*  649:     */     
/*  650:1043 */     temp = ya + zb;
/*  651:     */     
/*  652:1045 */     yb += -(temp - ya - zb);
/*  653:1046 */     ya = temp;
/*  654:1048 */     if (negative)
/*  655:     */     {
/*  656:1050 */       double denom = 1.0D + ya;
/*  657:1051 */       double denomr = 1.0D / denom;
/*  658:1052 */       double denomb = -(denom - 1.0D - ya) + yb;
/*  659:1053 */       double ratio = ya * denomr;
/*  660:1054 */       temp = ratio * 1073741824.0D;
/*  661:1055 */       double ra = ratio + temp - temp;
/*  662:1056 */       double rb = ratio - ra;
/*  663:     */       
/*  664:1058 */       temp = denom * 1073741824.0D;
/*  665:1059 */       za = denom + temp - temp;
/*  666:1060 */       zb = denom - za;
/*  667:     */       
/*  668:1062 */       rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;
/*  669:     */       
/*  670:     */ 
/*  671:     */ 
/*  672:     */ 
/*  673:     */ 
/*  674:     */ 
/*  675:     */ 
/*  676:     */ 
/*  677:     */ 
/*  678:     */ 
/*  679:1073 */       rb += yb * denomr;
/*  680:1074 */       rb += -ya * denomb * denomr * denomr;
/*  681:     */       
/*  682:     */ 
/*  683:1077 */       ya = -ra;
/*  684:1078 */       yb = -rb;
/*  685:     */     }
/*  686:1081 */     if (hiPrecOut != null)
/*  687:     */     {
/*  688:1082 */       hiPrecOut[0] = ya;
/*  689:1083 */       hiPrecOut[1] = yb;
/*  690:     */     }
/*  691:1086 */     return ya + yb;
/*  692:     */   }
/*  693:     */   
/*  694:     */   public static double log(double x)
/*  695:     */   {
/*  696:1096 */     return log(x, null);
/*  697:     */   }
/*  698:     */   
/*  699:     */   private static double log(double x, double[] hiPrec)
/*  700:     */   {
/*  701:1106 */     if (x == 0.0D) {
/*  702:1107 */       return (-1.0D / 0.0D);
/*  703:     */     }
/*  704:1109 */     long bits = Double.doubleToLongBits(x);
/*  705:1112 */     if ((((bits & 0x0) != 0L) || (x != x)) && 
/*  706:1113 */       (x != 0.0D))
/*  707:     */     {
/*  708:1114 */       if (hiPrec != null) {
/*  709:1115 */         hiPrec[0] = (0.0D / 0.0D);
/*  710:     */       }
/*  711:1118 */       return (0.0D / 0.0D);
/*  712:     */     }
/*  713:1123 */     if (x == (1.0D / 0.0D))
/*  714:     */     {
/*  715:1124 */       if (hiPrec != null) {
/*  716:1125 */         hiPrec[0] = (1.0D / 0.0D);
/*  717:     */       }
/*  718:1128 */       return (1.0D / 0.0D);
/*  719:     */     }
/*  720:1132 */     int exp = (int)(bits >> 52) - 1023;
/*  721:1134 */     if ((bits & 0x0) == 0L)
/*  722:     */     {
/*  723:1136 */       if (x == 0.0D)
/*  724:     */       {
/*  725:1138 */         if (hiPrec != null) {
/*  726:1139 */           hiPrec[0] = (-1.0D / 0.0D);
/*  727:     */         }
/*  728:1142 */         return (-1.0D / 0.0D);
/*  729:     */       }
/*  730:1146 */       bits <<= 1;
/*  731:1147 */       while ((bits & 0x0) == 0L)
/*  732:     */       {
/*  733:1148 */         exp--;
/*  734:1149 */         bits <<= 1;
/*  735:     */       }
/*  736:     */     }
/*  737:1154 */     if (((exp == -1) || (exp == 0)) && 
/*  738:1155 */       (x < 1.01D) && (x > 0.99D) && (hiPrec == null))
/*  739:     */     {
/*  740:1160 */       double xa = x - 1.0D;
/*  741:1161 */       double xb = xa - x + 1.0D;
/*  742:1162 */       double tmp = xa * 1073741824.0D;
/*  743:1163 */       double aa = xa + tmp - tmp;
/*  744:1164 */       double ab = xa - aa;
/*  745:1165 */       xa = aa;
/*  746:1166 */       xb = ab;
/*  747:     */       
/*  748:1168 */       double ya = LN_QUICK_COEF[(LN_QUICK_COEF.length - 1)][0];
/*  749:1169 */       double yb = LN_QUICK_COEF[(LN_QUICK_COEF.length - 1)][1];
/*  750:1171 */       for (int i = LN_QUICK_COEF.length - 2; i >= 0; i--)
/*  751:     */       {
/*  752:1173 */         aa = ya * xa;
/*  753:1174 */         ab = ya * xb + yb * xa + yb * xb;
/*  754:     */         
/*  755:1176 */         tmp = aa * 1073741824.0D;
/*  756:1177 */         ya = aa + tmp - tmp;
/*  757:1178 */         yb = aa - ya + ab;
/*  758:     */         
/*  759:     */ 
/*  760:1181 */         aa = ya + LN_QUICK_COEF[i][0];
/*  761:1182 */         ab = yb + LN_QUICK_COEF[i][1];
/*  762:     */         
/*  763:1184 */         tmp = aa * 1073741824.0D;
/*  764:1185 */         ya = aa + tmp - tmp;
/*  765:1186 */         yb = aa - ya + ab;
/*  766:     */       }
/*  767:1190 */       aa = ya * xa;
/*  768:1191 */       ab = ya * xb + yb * xa + yb * xb;
/*  769:     */       
/*  770:1193 */       tmp = aa * 1073741824.0D;
/*  771:1194 */       ya = aa + tmp - tmp;
/*  772:1195 */       yb = aa - ya + ab;
/*  773:     */       
/*  774:1197 */       return ya + yb;
/*  775:     */     }
/*  776:1202 */     double[] lnm = lnMant.LN_MANT[((int)((bits & 0x0) >> 42))];
/*  777:     */     
/*  778:     */ 
/*  779:     */ 
/*  780:     */ 
/*  781:     */ 
/*  782:     */ 
/*  783:     */ 
/*  784:     */ 
/*  785:     */ 
/*  786:     */ 
/*  787:1213 */     double epsilon = (bits & 0xFFFFFFFF) / (4503599627370496.0D + (bits & 0x0));
/*  788:     */     
/*  789:1215 */     double lnza = 0.0D;
/*  790:1216 */     double lnzb = 0.0D;
/*  791:1218 */     if (hiPrec != null)
/*  792:     */     {
/*  793:1220 */       double tmp = epsilon * 1073741824.0D;
/*  794:1221 */       double aa = epsilon + tmp - tmp;
/*  795:1222 */       double ab = epsilon - aa;
/*  796:1223 */       double xa = aa;
/*  797:1224 */       double xb = ab;
/*  798:     */       
/*  799:     */ 
/*  800:1227 */       double numer = bits & 0xFFFFFFFF;
/*  801:1228 */       double denom = 4503599627370496.0D + (bits & 0x0);
/*  802:1229 */       aa = numer - xa * denom - xb * denom;
/*  803:1230 */       xb += aa / denom;
/*  804:     */       
/*  805:     */ 
/*  806:1233 */       double ya = LN_HI_PREC_COEF[(LN_HI_PREC_COEF.length - 1)][0];
/*  807:1234 */       double yb = LN_HI_PREC_COEF[(LN_HI_PREC_COEF.length - 1)][1];
/*  808:1236 */       for (int i = LN_HI_PREC_COEF.length - 2; i >= 0; i--)
/*  809:     */       {
/*  810:1238 */         aa = ya * xa;
/*  811:1239 */         ab = ya * xb + yb * xa + yb * xb;
/*  812:     */         
/*  813:1241 */         tmp = aa * 1073741824.0D;
/*  814:1242 */         ya = aa + tmp - tmp;
/*  815:1243 */         yb = aa - ya + ab;
/*  816:     */         
/*  817:     */ 
/*  818:1246 */         aa = ya + LN_HI_PREC_COEF[i][0];
/*  819:1247 */         ab = yb + LN_HI_PREC_COEF[i][1];
/*  820:     */         
/*  821:1249 */         tmp = aa * 1073741824.0D;
/*  822:1250 */         ya = aa + tmp - tmp;
/*  823:1251 */         yb = aa - ya + ab;
/*  824:     */       }
/*  825:1255 */       aa = ya * xa;
/*  826:1256 */       ab = ya * xb + yb * xa + yb * xb;
/*  827:     */       
/*  828:     */ 
/*  829:     */ 
/*  830:     */ 
/*  831:     */ 
/*  832:     */ 
/*  833:     */ 
/*  834:1264 */       lnza = aa + ab;
/*  835:1265 */       lnzb = -(lnza - aa - ab);
/*  836:     */     }
/*  837:     */     else
/*  838:     */     {
/*  839:1269 */       lnza = -0.1662488244041857D;
/*  840:1270 */       lnza = lnza * epsilon + 0.1999995412025452D;
/*  841:1271 */       lnza = lnza * epsilon + -0.24999999976775D;
/*  842:1272 */       lnza = lnza * epsilon + 0.3333333333332802D;
/*  843:1273 */       lnza = lnza * epsilon + -0.5D;
/*  844:1274 */       lnza = lnza * epsilon + 1.0D;
/*  845:1275 */       lnza *= epsilon;
/*  846:     */     }
/*  847:1292 */     double a = 0.6931470632553101D * exp;
/*  848:1293 */     double b = 0.0D;
/*  849:1294 */     double c = a + lnm[0];
/*  850:1295 */     double d = -(c - a - lnm[0]);
/*  851:1296 */     a = c;
/*  852:1297 */     b += d;
/*  853:     */     
/*  854:1299 */     c = a + lnza;
/*  855:1300 */     d = -(c - a - lnza);
/*  856:1301 */     a = c;
/*  857:1302 */     b += d;
/*  858:     */     
/*  859:1304 */     c = a + 1.173046352508235E-007D * exp;
/*  860:1305 */     d = -(c - a - 1.173046352508235E-007D * exp);
/*  861:1306 */     a = c;
/*  862:1307 */     b += d;
/*  863:     */     
/*  864:1309 */     c = a + lnm[1];
/*  865:1310 */     d = -(c - a - lnm[1]);
/*  866:1311 */     a = c;
/*  867:1312 */     b += d;
/*  868:     */     
/*  869:1314 */     c = a + lnzb;
/*  870:1315 */     d = -(c - a - lnzb);
/*  871:1316 */     a = c;
/*  872:1317 */     b += d;
/*  873:1319 */     if (hiPrec != null)
/*  874:     */     {
/*  875:1320 */       hiPrec[0] = a;
/*  876:1321 */       hiPrec[1] = b;
/*  877:     */     }
/*  878:1324 */     return a + b;
/*  879:     */   }
/*  880:     */   
/*  881:     */   public static double log1p(double x)
/*  882:     */   {
/*  883:1333 */     if (x == -1.0D) {
/*  884:1334 */       return x / 0.0D;
/*  885:     */     }
/*  886:1337 */     if ((x > 0.0D) && (1.0D / x == 0.0D)) {
/*  887:1338 */       return x;
/*  888:     */     }
/*  889:1341 */     if ((x > 1.0E-006D) || (x < -1.0E-006D))
/*  890:     */     {
/*  891:1342 */       double xpa = 1.0D + x;
/*  892:1343 */       double xpb = -(xpa - 1.0D - x);
/*  893:     */       
/*  894:1345 */       double[] hiPrec = new double[2];
/*  895:     */       
/*  896:1347 */       double lores = log(xpa, hiPrec);
/*  897:1348 */       if (Double.isInfinite(lores)) {
/*  898:1349 */         return lores;
/*  899:     */       }
/*  900:1354 */       double fx1 = xpb / xpa;
/*  901:     */       
/*  902:1356 */       double epsilon = 0.5D * fx1 + 1.0D;
/*  903:1357 */       epsilon *= fx1;
/*  904:     */       
/*  905:1359 */       return epsilon + hiPrec[1] + hiPrec[0];
/*  906:     */     }
/*  907:1363 */     double y = x * 0.3333333333333333D - 0.5D;
/*  908:1364 */     y = y * x + 1.0D;
/*  909:1365 */     y *= x;
/*  910:     */     
/*  911:1367 */     return y;
/*  912:     */   }
/*  913:     */   
/*  914:     */   public static double log10(double x)
/*  915:     */   {
/*  916:1375 */     double[] hiPrec = new double[2];
/*  917:     */     
/*  918:1377 */     double lores = log(x, hiPrec);
/*  919:1378 */     if (Double.isInfinite(lores)) {
/*  920:1379 */       return lores;
/*  921:     */     }
/*  922:1382 */     double tmp = hiPrec[0] * 1073741824.0D;
/*  923:1383 */     double lna = hiPrec[0] + tmp - tmp;
/*  924:1384 */     double lnb = hiPrec[0] - lna + hiPrec[1];
/*  925:     */     
/*  926:1386 */     double rln10a = 0.4342944622039795D;
/*  927:1387 */     double rln10b = 1.969927233546363E-008D;
/*  928:     */     
/*  929:1389 */     return 1.969927233546363E-008D * lnb + 1.969927233546363E-008D * lna + 0.4342944622039795D * lnb + 0.4342944622039795D * lna;
/*  930:     */   }
/*  931:     */   
/*  932:     */   public static double log(double base, double x)
/*  933:     */   {
/*  934:1409 */     return log(x) / log(base);
/*  935:     */   }
/*  936:     */   
/*  937:     */   public static double pow(double x, double y)
/*  938:     */   {
/*  939:1420 */     double[] lns = new double[2];
/*  940:1422 */     if (y == 0.0D) {
/*  941:1423 */       return 1.0D;
/*  942:     */     }
/*  943:1426 */     if (x != x) {
/*  944:1427 */       return x;
/*  945:     */     }
/*  946:1431 */     if (x == 0.0D)
/*  947:     */     {
/*  948:1432 */       long bits = Double.doubleToLongBits(x);
/*  949:1433 */       if ((bits & 0x0) != 0L)
/*  950:     */       {
/*  951:1435 */         long yi = (long) y;
/*  952:1437 */         if ((y < 0.0D) && (y == yi) && ((yi & 1L) == 1L)) {
/*  953:1438 */           return (-1.0D / 0.0D);
/*  954:     */         }
/*  955:1441 */         if ((y > 0.0D) && (y == yi) && ((yi & 1L) == 1L)) {
/*  956:1442 */           return -0.0D;
/*  957:     */         }
/*  958:     */       }
/*  959:1446 */       if (y < 0.0D) {
/*  960:1447 */         return (1.0D / 0.0D);
/*  961:     */       }
/*  962:1449 */       if (y > 0.0D) {
/*  963:1450 */         return 0.0D;
/*  964:     */       }
/*  965:1453 */       return (0.0D / 0.0D);
/*  966:     */     }
/*  967:1456 */     if (x == (1.0D / 0.0D))
/*  968:     */     {
/*  969:1457 */       if (y != y) {
/*  970:1458 */         return y;
/*  971:     */       }
/*  972:1460 */       if (y < 0.0D) {
/*  973:1461 */         return 0.0D;
/*  974:     */       }
/*  975:1463 */       return (1.0D / 0.0D);
/*  976:     */     }
/*  977:1467 */     if (y == (1.0D / 0.0D))
/*  978:     */     {
/*  979:1468 */       if (x * x == 1.0D) {
/*  980:1469 */         return (0.0D / 0.0D);
/*  981:     */       }
/*  982:1472 */       if (x * x > 1.0D) {
/*  983:1473 */         return (1.0D / 0.0D);
/*  984:     */       }
/*  985:1475 */       return 0.0D;
/*  986:     */     }
/*  987:1479 */     if (x == (-1.0D / 0.0D))
/*  988:     */     {
/*  989:1480 */       if (y != y) {
/*  990:1481 */         return y;
/*  991:     */       }
/*  992:1484 */       if (y < 0.0D)
/*  993:     */       {
/*  994:1485 */         long yi = (long) y;
/*  995:1486 */         if ((y == yi) && ((yi & 1L) == 1L)) {
/*  996:1487 */           return -0.0D;
/*  997:     */         }
/*  998:1490 */         return 0.0D;
/*  999:     */       }
/* 1000:1493 */       if (y > 0.0D)
/* 1001:     */       {
/* 1002:1494 */         long yi = (long) y;
/* 1003:1495 */         if ((y == yi) && ((yi & 1L) == 1L)) {
/* 1004:1496 */           return (-1.0D / 0.0D);
/* 1005:     */         }
/* 1006:1499 */         return (1.0D / 0.0D);
/* 1007:     */       }
/* 1008:     */     }
/* 1009:1503 */     if (y == (-1.0D / 0.0D))
/* 1010:     */     {
/* 1011:1505 */       if (x * x == 1.0D) {
/* 1012:1506 */         return (0.0D / 0.0D);
/* 1013:     */       }
/* 1014:1509 */       if (x * x < 1.0D) {
/* 1015:1510 */         return (1.0D / 0.0D);
/* 1016:     */       }
/* 1017:1512 */       return 0.0D;
/* 1018:     */     }
/* 1019:1517 */     if (x < 0.0D)
/* 1020:     */     {
/* 1021:1519 */       if ((y >= 4503599627370496.0D) || (y <= -4503599627370496.0D)) {
/* 1022:1520 */         return pow(-x, y);
/* 1023:     */       }
/* 1024:1523 */       //if (y == y) {
/* 1025:1525 */        // return null;//(y & 1) == 0L ? pow(-x, y) : -pow(-x, y);
/* 1026:     */       //else{
/* 1027:1527 */       return (0.0D / 0.0D);
						//}
/* 1028:     */     }
/* 1029:     */     double yb;
/* 1030:     */     double ya;
/* 1031:     */     
/* 1032:1534 */     if ((y < 8.0E+298D) && (y > -8.0E+298D))
/* 1033:     */     {
/* 1034:1535 */       double tmp1 = y * 1073741824.0D;
/* 1035:1536 */       ya = y + tmp1 - tmp1;
/* 1036:1537 */       yb = y - ya;
/* 1037:     */     }
/* 1038:     */     else
/* 1039:     */     {
/* 1040:1539 */       double tmp1 = y * 9.313225746154785E-010D;
/* 1041:1540 */       double tmp2 = tmp1 * 9.313225746154785E-010D;
/* 1042:1541 */       ya = (tmp1 + tmp2 - tmp1) * 1073741824.0D * 1073741824.0D;
/* 1043:1542 */       yb = y - ya;
/* 1044:     */     }
/* 1045:1546 */     double lores = log(x, lns);
/* 1046:1547 */     if (Double.isInfinite(lores)) {
/* 1047:1548 */       return lores;
/* 1048:     */     }
/* 1049:1551 */     double lna = lns[0];
/* 1050:1552 */     double lnb = lns[1];
/* 1051:     */     
/* 1052:     */ 
/* 1053:1555 */     double tmp1 = lna * 1073741824.0D;
/* 1054:1556 */     double tmp2 = lna + tmp1 - tmp1;
/* 1055:1557 */     lnb += lna - tmp2;
/* 1056:1558 */     lna = tmp2;
/* 1057:     */     
/* 1058:     */ 
/* 1059:1561 */     double aa = lna * ya;
/* 1060:1562 */     double ab = lna * yb + lnb * ya + lnb * yb;
/* 1061:     */     
/* 1062:1564 */     lna = aa + ab;
/* 1063:1565 */     lnb = -(lna - aa - ab);
/* 1064:     */     
/* 1065:1567 */     double z = 0.008333333333333333D;
/* 1066:1568 */     z = z * lnb + 0.04166666666666666D;
/* 1067:1569 */     z = z * lnb + 0.1666666666666667D;
/* 1068:1570 */     z = z * lnb + 0.5D;
/* 1069:1571 */     z = z * lnb + 1.0D;
/* 1070:1572 */     z *= lnb;
/* 1071:     */     
/* 1072:1574 */     double result = exp(lna, z, null);
/* 1073:     */     
/* 1074:1576 */     return result;
/* 1075:     */   }
/* 1076:     */   
/* 1077:     */   private static double polySine(double x)
/* 1078:     */   {
/* 1079:1588 */     double x2 = x * x;
/* 1080:     */     
/* 1081:1590 */     double p = 2.755381745227222E-006D;
/* 1082:1591 */     p = p * x2 + -0.0001984126965958651D;
/* 1083:1592 */     p = p * x2 + 0.008333333333329196D;
/* 1084:1593 */     p = p * x2 + -0.1666666666666667D;
/* 1085:     */     
/* 1086:     */ 
/* 1087:1596 */     p = p * x2 * x;
/* 1088:     */     
/* 1089:1598 */     return p;
/* 1090:     */   }
/* 1091:     */   
/* 1092:     */   private static double polyCosine(double x)
/* 1093:     */   {
/* 1094:1608 */     double x2 = x * x;
/* 1095:     */     
/* 1096:1610 */     double p = 2.479773539153719E-005D;
/* 1097:1611 */     p = p * x2 + -0.001388888868903988D;
/* 1098:1612 */     p = p * x2 + 0.04166666666662117D;
/* 1099:1613 */     p = p * x2 + -0.4999999999999999D;
/* 1100:1614 */     p *= x2;
/* 1101:     */     
/* 1102:1616 */     return p;
/* 1103:     */   }
/* 1104:     */   
/* 1105:     */   private static double sinQ(double xa, double xb)
/* 1106:     */   {
/* 1107:1627 */     int idx = (int)(xa * 8.0D + 0.5D);
/* 1108:1628 */     double epsilon = xa - EIGHTHS[idx];
/* 1109:     */     
/* 1110:     */ 
/* 1111:1631 */     double sintA = SINE_TABLE_A[idx];
/* 1112:1632 */     double sintB = SINE_TABLE_B[idx];
/* 1113:1633 */     double costA = COSINE_TABLE_A[idx];
/* 1114:1634 */     double costB = COSINE_TABLE_B[idx];
/* 1115:     */     
/* 1116:     */ 
/* 1117:1637 */     double sinEpsA = epsilon;
/* 1118:1638 */     double sinEpsB = polySine(epsilon);
/* 1119:1639 */     double cosEpsA = 1.0D;
/* 1120:1640 */     double cosEpsB = polyCosine(epsilon);
/* 1121:     */     
/* 1122:     */ 
/* 1123:1643 */     double temp = sinEpsA * 1073741824.0D;
/* 1124:1644 */     double temp2 = sinEpsA + temp - temp;
/* 1125:1645 */     sinEpsB += sinEpsA - temp2;
/* 1126:1646 */     sinEpsA = temp2;
/* 1127:     */     
/* 1128:     */ 
/* 1129:     */ 
/* 1130:     */ 
/* 1131:     */ 
/* 1132:     */ 
/* 1133:     */ 
/* 1134:     */ 
/* 1135:     */ 
/* 1136:     */ 
/* 1137:     */ 
/* 1138:     */ 
/* 1139:     */ 
/* 1140:     */ 
/* 1141:     */ 
/* 1142:     */ 
/* 1143:     */ 
/* 1144:     */ 
/* 1145:     */ 
/* 1146:     */ 
/* 1147:     */ 
/* 1148:     */ 
/* 1149:     */ 
/* 1150:     */ 
/* 1151:     */ 
/* 1152:1672 */     double a = 0.0D;
/* 1153:1673 */     double b = 0.0D;
/* 1154:     */     
/* 1155:1675 */     double t = sintA;
/* 1156:1676 */     double c = a + t;
/* 1157:1677 */     double d = -(c - a - t);
/* 1158:1678 */     a = c;
/* 1159:1679 */     b += d;
/* 1160:     */     
/* 1161:1681 */     t = costA * sinEpsA;
/* 1162:1682 */     c = a + t;
/* 1163:1683 */     d = -(c - a - t);
/* 1164:1684 */     a = c;
/* 1165:1685 */     b += d;
/* 1166:     */     
/* 1167:1687 */     b = b + sintA * cosEpsB + costA * sinEpsB;
/* 1168:     */     
/* 1169:     */ 
/* 1170:     */ 
/* 1171:     */ 
/* 1172:     */ 
/* 1173:     */ 
/* 1174:     */ 
/* 1175:     */ 
/* 1176:     */ 
/* 1177:     */ 
/* 1178:     */ 
/* 1179:     */ 
/* 1180:     */ 
/* 1181:     */ 
/* 1182:1702 */     b = b + sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB;
/* 1183:1729 */     if (xb != 0.0D)
/* 1184:     */     {
/* 1185:1730 */       t = ((costA + costB) * (1.0D + cosEpsB) - (sintA + sintB) * (sinEpsA + sinEpsB)) * xb;
/* 1186:     */       
/* 1187:1732 */       c = a + t;
/* 1188:1733 */       d = -(c - a - t);
/* 1189:1734 */       a = c;
/* 1190:1735 */       b += d;
/* 1191:     */     }
/* 1192:1738 */     double result = a + b;
/* 1193:     */     
/* 1194:1740 */     return result;
/* 1195:     */   }
/* 1196:     */   
/* 1197:     */   private static double cosQ(double xa, double xb)
/* 1198:     */   {
/* 1199:1751 */     double pi2a = 1.570796326794897D;
/* 1200:1752 */     double pi2b = 6.123233995736766E-017D;
/* 1201:     */     
/* 1202:1754 */     double a = 1.570796326794897D - xa;
/* 1203:1755 */     double b = -(a - 1.570796326794897D + xa);
/* 1204:1756 */     b += 6.123233995736766E-017D - xb;
/* 1205:     */     
/* 1206:1758 */     return sinQ(a, b);
/* 1207:     */   }
/* 1208:     */   
/* 1209:     */   private static double tanQ(double xa, double xb, boolean cotanFlag)
/* 1210:     */   {
/* 1211:1771 */     int idx = (int)(xa * 8.0D + 0.5D);
/* 1212:1772 */     double epsilon = xa - EIGHTHS[idx];
/* 1213:     */     
/* 1214:     */ 
/* 1215:1775 */     double sintA = SINE_TABLE_A[idx];
/* 1216:1776 */     double sintB = SINE_TABLE_B[idx];
/* 1217:1777 */     double costA = COSINE_TABLE_A[idx];
/* 1218:1778 */     double costB = COSINE_TABLE_B[idx];
/* 1219:     */     
/* 1220:     */ 
/* 1221:1781 */     double sinEpsA = epsilon;
/* 1222:1782 */     double sinEpsB = polySine(epsilon);
/* 1223:1783 */     double cosEpsA = 1.0D;
/* 1224:1784 */     double cosEpsB = polyCosine(epsilon);
/* 1225:     */     
/* 1226:     */ 
/* 1227:1787 */     double temp = sinEpsA * 1073741824.0D;
/* 1228:1788 */     double temp2 = sinEpsA + temp - temp;
/* 1229:1789 */     sinEpsB += sinEpsA - temp2;
/* 1230:1790 */     sinEpsA = temp2;
/* 1231:     */     
/* 1232:     */ 
/* 1233:     */ 
/* 1234:     */ 
/* 1235:     */ 
/* 1236:     */ 
/* 1237:     */ 
/* 1238:     */ 
/* 1239:     */ 
/* 1240:     */ 
/* 1241:     */ 
/* 1242:     */ 
/* 1243:     */ 
/* 1244:     */ 
/* 1245:     */ 
/* 1246:     */ 
/* 1247:     */ 
/* 1248:     */ 
/* 1249:     */ 
/* 1250:     */ 
/* 1251:     */ 
/* 1252:     */ 
/* 1253:     */ 
/* 1254:     */ 
/* 1255:1815 */     double a = 0.0D;
/* 1256:1816 */     double b = 0.0D;
/* 1257:     */     
/* 1258:     */ 
/* 1259:1819 */     double t = sintA;
/* 1260:1820 */     double c = a + t;
/* 1261:1821 */     double d = -(c - a - t);
/* 1262:1822 */     a = c;
/* 1263:1823 */     b += d;
/* 1264:     */     
/* 1265:1825 */     t = costA * sinEpsA;
/* 1266:1826 */     c = a + t;
/* 1267:1827 */     d = -(c - a - t);
/* 1268:1828 */     a = c;
/* 1269:1829 */     b += d;
/* 1270:     */     
/* 1271:1831 */     b = b + sintA * cosEpsB + costA * sinEpsB;
/* 1272:1832 */     b = b + sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB;
/* 1273:     */     
/* 1274:1834 */     double sina = a + b;
/* 1275:1835 */     double sinb = -(sina - a - b);
/* 1276:     */     
/* 1277:     */ 
/* 1278:     */ 
/* 1279:1839 */     a = b = c = d = 0.0D;
/* 1280:     */     
/* 1281:1841 */     t = costA * 1.0D;
/* 1282:1842 */     c = a + t;
/* 1283:1843 */     d = -(c - a - t);
/* 1284:1844 */     a = c;
/* 1285:1845 */     b += d;
/* 1286:     */     
/* 1287:1847 */     t = -sintA * sinEpsA;
/* 1288:1848 */     c = a + t;
/* 1289:1849 */     d = -(c - a - t);
/* 1290:1850 */     a = c;
/* 1291:1851 */     b += d;
/* 1292:     */     
/* 1293:1853 */     b = b + costB * 1.0D + costA * cosEpsB + costB * cosEpsB;
/* 1294:1854 */     b -= sintB * sinEpsA + sintA * sinEpsB + sintB * sinEpsB;
/* 1295:     */     
/* 1296:1856 */     double cosa = a + b;
/* 1297:1857 */     double cosb = -(cosa - a - b);
/* 1298:1859 */     if (cotanFlag)
/* 1299:     */     {
/* 1300:1861 */       double tmp = cosa;cosa = sina;sina = tmp;
/* 1301:1862 */       tmp = cosb;cosb = sinb;sinb = tmp;
/* 1302:     */     }
/* 1303:1876 */     double est = sina / cosa;
/* 1304:     */     
/* 1305:     */ 
/* 1306:1879 */     temp = est * 1073741824.0D;
/* 1307:1880 */     double esta = est + temp - temp;
/* 1308:1881 */     double estb = est - esta;
/* 1309:     */     
/* 1310:1883 */     temp = cosa * 1073741824.0D;
/* 1311:1884 */     double cosaa = cosa + temp - temp;
/* 1312:1885 */     double cosab = cosa - cosaa;
/* 1313:     */     
/* 1314:     */ 
/* 1315:1888 */     double err = (sina - esta * cosaa - esta * cosab - estb * cosaa - estb * cosab) / cosa;
/* 1316:1889 */     err += sinb / cosa;
/* 1317:1890 */     err += -sina * cosb / cosa / cosa;
/* 1318:1892 */     if (xb != 0.0D)
/* 1319:     */     {
/* 1320:1895 */       double xbadj = xb + est * est * xb;
/* 1321:1896 */       if (cotanFlag) {
/* 1322:1897 */         xbadj = -xbadj;
/* 1323:     */       }
/* 1324:1900 */       err += xbadj;
/* 1325:     */     }
/* 1326:1903 */     return est + err;
/* 1327:     */   }
/* 1328:     */   
/* 1329:     */   private static void reducePayneHanek(double x, double[] result)
/* 1330:     */   {
/* 1331:1920 */     long inbits = Double.doubleToLongBits(x);
/* 1332:1921 */     int exponent = (int)(inbits >> 52 & 0x7FF) - 1023;
/* 1333:     */     
/* 1334:     */ 
/* 1335:1924 */     inbits &= 0xFFFFFFFF;
/* 1336:1925 */     inbits |= 0x0;
/* 1337:     */     
/* 1338:     */ 
/* 1339:1928 */     exponent++;
/* 1340:1929 */     inbits <<= 11;
/* 1341:     */     
/* 1342:     */ 
/* 1343:     */ 
/* 1344:     */ 
/* 1345:     */ 
/* 1346:1935 */     int idx = exponent >> 6;
/* 1347:1936 */     int shift = exponent - (idx << 6);
/* 1348:     */     long shpiB;
/* 1349:     */     long shpi0;
/* 1350:     */     long shpiA;
/* 1351:     */     
/* 1352:1938 */     if (shift != 0)
/* 1353:     */     {
/* 1354:1939 */       shpi0 = idx == 0 ? 0L : RECIP_2PI[(idx - 1)] << shift;
/* 1355:1940 */       shpi0 |= RECIP_2PI[idx] >>> 64 - shift;
/* 1356:1941 */       shpiA = RECIP_2PI[idx] << shift | RECIP_2PI[(idx + 1)] >>> 64 - shift;
/* 1357:1942 */       shpiB = RECIP_2PI[(idx + 1)] << shift | RECIP_2PI[(idx + 2)] >>> 64 - shift;
/* 1358:     */     }
/* 1359:     */     else
/* 1360:     */     {
/* 1361:1944 */       shpi0 = idx == 0 ? 0L : RECIP_2PI[(idx - 1)];
/* 1362:1945 */       shpiA = RECIP_2PI[idx];
/* 1363:1946 */       shpiB = RECIP_2PI[(idx + 1)];
/* 1364:     */     }
/* 1365:1950 */     long a = inbits >>> 32;
/* 1366:1951 */     long b = inbits & 0xFFFFFFFF;
/* 1367:     */     
/* 1368:1953 */     long c = shpiA >>> 32;
/* 1369:1954 */     long d = shpiA & 0xFFFFFFFF;
/* 1370:     */     
/* 1371:1956 */     long ac = a * c;
/* 1372:1957 */     long bd = b * d;
/* 1373:1958 */     long bc = b * c;
/* 1374:1959 */     long ad = a * d;
/* 1375:     */     
/* 1376:1961 */     long prodB = bd + (ad << 32);
/* 1377:1962 */     long prodA = ac + (ad >>> 32);
/* 1378:     */     
/* 1379:1964 */     boolean bita = (bd & 0x0) != 0L;
/* 1380:1965 */     boolean bitb = (ad & 0x80000000) != 0L;
/* 1381:1966 */     boolean bitsum = (prodB & 0x0) != 0L;
/* 1382:1969 */     if (((bita) && (bitb)) || (((bita) || (bitb)) && (!bitsum))) {
/* 1383:1971 */       prodA += 1L;
/* 1384:     */     }
/* 1385:1974 */     bita = (prodB & 0x0) != 0L;
/* 1386:1975 */     bitb = (bc & 0x80000000) != 0L;
/* 1387:     */     
/* 1388:1977 */     prodB += (bc << 32);
/* 1389:1978 */     prodA += (bc >>> 32);
/* 1390:     */     
/* 1391:1980 */     bitsum = (prodB & 0x0) != 0L;
/* 1392:1983 */     if (((bita) && (bitb)) || (((bita) || (bitb)) && (!bitsum))) {
/* 1393:1985 */       prodA += 1L;
/* 1394:     */     }
/* 1395:1989 */     c = shpiB >>> 32;
/* 1396:1990 */     d = shpiB & 0xFFFFFFFF;
/* 1397:1991 */     ac = a * c;
/* 1398:1992 */     bc = b * c;
/* 1399:1993 */     ad = a * d;
/* 1400:     */     
/* 1401:     */ 
/* 1402:1996 */     ac += (bc + ad >>> 32);
/* 1403:     */     
/* 1404:1998 */     bita = (prodB & 0x0) != 0L;
/* 1405:1999 */     bitb = (ac & 0x0) != 0L;
/* 1406:2000 */     prodB += ac;
/* 1407:2001 */     bitsum = (prodB & 0x0) != 0L;
/* 1408:2003 */     if (((bita) && (bitb)) || (((bita) || (bitb)) && (!bitsum))) {
/* 1409:2005 */       prodA += 1L;
/* 1410:     */     }
/* 1411:2009 */     c = shpi0 >>> 32;
/* 1412:2010 */     d = shpi0 & 0xFFFFFFFF;
/* 1413:     */     
/* 1414:2012 */     bd = b * d;
/* 1415:2013 */     bc = b * c;
/* 1416:2014 */     ad = a * d;
/* 1417:     */     
/* 1418:2016 */     prodA += bd + (bc + ad << 32);
/* 1419:     */     
/* 1420:     */ 
/* 1421:     */ 
/* 1422:     */ 
/* 1423:     */ 
/* 1424:     */ 
/* 1425:     */ 
/* 1426:     */ 
/* 1427:     */ 
/* 1428:     */ 
/* 1429:     */ 
/* 1430:2028 */     int intPart = (int)(prodA >>> 62);
/* 1431:     */     
/* 1432:     */ 
/* 1433:2031 */     prodA <<= 2;
/* 1434:2032 */     prodA |= prodB >>> 62;
/* 1435:2033 */     prodB <<= 2;
/* 1436:     */     
/* 1437:     */ 
/* 1438:2036 */     a = prodA >>> 32;
/* 1439:2037 */     b = prodA & 0xFFFFFFFF;
/* 1440:     */     
/* 1441:2039 */     c = PI_O_4_BITS[0] >>> 32;
/* 1442:2040 */     d = PI_O_4_BITS[0] & 0xFFFFFFFF;
/* 1443:     */     
/* 1444:2042 */     ac = a * c;
/* 1445:2043 */     bd = b * d;
/* 1446:2044 */     bc = b * c;
/* 1447:2045 */     ad = a * d;
/* 1448:     */     
/* 1449:2047 */     long prod2B = bd + (ad << 32);
/* 1450:2048 */     long prod2A = ac + (ad >>> 32);
/* 1451:     */     
/* 1452:2050 */     bita = (bd & 0x0) != 0L;
/* 1453:2051 */     bitb = (ad & 0x80000000) != 0L;
/* 1454:2052 */     bitsum = (prod2B & 0x0) != 0L;
/* 1455:2055 */     if (((bita) && (bitb)) || (((bita) || (bitb)) && (!bitsum))) {
/* 1456:2057 */       prod2A += 1L;
/* 1457:     */     }
/* 1458:2060 */     bita = (prod2B & 0x0) != 0L;
/* 1459:2061 */     bitb = (bc & 0x80000000) != 0L;
/* 1460:     */     
/* 1461:2063 */     prod2B += (bc << 32);
/* 1462:2064 */     prod2A += (bc >>> 32);
/* 1463:     */     
/* 1464:2066 */     bitsum = (prod2B & 0x0) != 0L;
/* 1465:2069 */     if (((bita) && (bitb)) || (((bita) || (bitb)) && (!bitsum))) {
/* 1466:2071 */       prod2A += 1L;
/* 1467:     */     }
/* 1468:2075 */     c = PI_O_4_BITS[1] >>> 32;
/* 1469:2076 */     d = PI_O_4_BITS[1] & 0xFFFFFFFF;
/* 1470:2077 */     ac = a * c;
/* 1471:2078 */     bc = b * c;
/* 1472:2079 */     ad = a * d;
/* 1473:     */     
/* 1474:     */ 
/* 1475:2082 */     ac += (bc + ad >>> 32);
/* 1476:     */     
/* 1477:2084 */     bita = (prod2B & 0x0) != 0L;
/* 1478:2085 */     bitb = (ac & 0x0) != 0L;
/* 1479:2086 */     prod2B += ac;
/* 1480:2087 */     bitsum = (prod2B & 0x0) != 0L;
/* 1481:2089 */     if (((bita) && (bitb)) || (((bita) || (bitb)) && (!bitsum))) {
/* 1482:2091 */       prod2A += 1L;
/* 1483:     */     }
/* 1484:2095 */     a = prodB >>> 32;
/* 1485:2096 */     b = prodB & 0xFFFFFFFF;
/* 1486:2097 */     c = PI_O_4_BITS[0] >>> 32;
/* 1487:2098 */     d = PI_O_4_BITS[0] & 0xFFFFFFFF;
/* 1488:2099 */     ac = a * c;
/* 1489:2100 */     bc = b * c;
/* 1490:2101 */     ad = a * d;
/* 1491:     */     
/* 1492:     */ 
/* 1493:2104 */     ac += (bc + ad >>> 32);
/* 1494:     */     
/* 1495:2106 */     bita = (prod2B & 0x0) != 0L;
/* 1496:2107 */     bitb = (ac & 0x0) != 0L;
/* 1497:2108 */     prod2B += ac;
/* 1498:2109 */     bitsum = (prod2B & 0x0) != 0L;
/* 1499:2111 */     if (((bita) && (bitb)) || (((bita) || (bitb)) && (!bitsum))) {
/* 1500:2113 */       prod2A += 1L;
/* 1501:     */     }
/* 1502:2117 */     double tmpA = (prod2A >>> 12) / 4503599627370496.0D;
/* 1503:2118 */     double tmpB = (((prod2A & 0xFFF) << 40) + (prod2B >>> 24)) / 4503599627370496.0D / 4503599627370496.0D;
/* 1504:     */     
/* 1505:2120 */     double sumA = tmpA + tmpB;
/* 1506:2121 */     double sumB = -(sumA - tmpA - tmpB);
/* 1507:     */     
/* 1508:     */ 
/* 1509:2124 */     result[0] = intPart;
/* 1510:2125 */     result[1] = (sumA * 2.0D);
/* 1511:2126 */     result[2] = (sumB * 2.0D);
/* 1512:     */   }
/* 1513:     */   
/* 1514:     */   public static double sin(double x)
/* 1515:     */   {
/* 1516:2135 */     boolean negative = false;
/* 1517:2136 */     int quadrant = 0;
/* 1518:     */     
/* 1519:2138 */     double xb = 0.0D;
/* 1520:     */     
/* 1521:     */ 
/* 1522:2141 */     double xa = x;
/* 1523:2142 */     if (x < 0.0D)
/* 1524:     */     {
/* 1525:2143 */       negative = true;
/* 1526:2144 */       xa = -xa;
/* 1527:     */     }
/* 1528:2148 */     if (xa == 0.0D)
/* 1529:     */     {
/* 1530:2149 */       long bits = Double.doubleToLongBits(x);
/* 1531:2150 */       if (bits < 0L) {
/* 1532:2151 */         return -0.0D;
/* 1533:     */       }
/* 1534:2153 */       return 0.0D;
/* 1535:     */     }
/* 1536:2156 */     if ((xa != xa) || (xa == (1.0D / 0.0D))) {
/* 1537:2157 */       return (0.0D / 0.0D);
/* 1538:     */     }
/* 1539:2161 */     if (xa > 3294198.0D)
/* 1540:     */     {
/* 1541:2165 */       double[] reduceResults = new double[3];
/* 1542:2166 */       reducePayneHanek(xa, reduceResults);
/* 1543:2167 */       quadrant = (int)reduceResults[0] & 0x3;
/* 1544:2168 */       xa = reduceResults[1];
/* 1545:2169 */       xb = reduceResults[2];
/* 1546:     */     }
/* 1547:2170 */     else if (xa > 1.570796326794897D)
/* 1548:     */     {
/* 1549:2175 */       int k = (int)(xa * 0.6366197723675814D);
/* 1550:     */       double remA;
/* 1551:     */       double remB;
/* 1552:     */       for (;;)
/* 1553:     */       {
/* 1554:2181 */         double a = -k * 1.570796251296997D;
/* 1555:2182 */         remA = xa + a;
/* 1556:2183 */         remB = -(remA - xa - a);
/* 1557:     */         
/* 1558:2185 */         a = -k * 7.549789948768648E-008D;
/* 1559:2186 */         double b = remA;
/* 1560:2187 */         remA = a + b;
/* 1561:2188 */         remB += -(remA - b - a);
/* 1562:     */         
/* 1563:2190 */         a = -k * 6.123233995736766E-017D;
/* 1564:2191 */         b = remA;
/* 1565:2192 */         remA = a + b;
/* 1566:2193 */         remB += -(remA - b - a);
/* 1567:2195 */         if (remA > 0.0D) {
/* 1568:     */           break;
/* 1569:     */         }
/* 1570:2202 */         k--;
/* 1571:     */       }
/* 1572:2204 */       quadrant = k & 0x3;
/* 1573:2205 */       xa = remA;
/* 1574:2206 */       xb = remB;
/* 1575:     */     }
/* 1576:2209 */     if (negative) {
/* 1577:2210 */       quadrant ^= 0x2;
/* 1578:     */     }
/* 1579:2213 */     switch (quadrant)
/* 1580:     */     {
/* 1581:     */     case 0: 
/* 1582:2215 */       return sinQ(xa, xb);
/* 1583:     */     case 1: 
/* 1584:2217 */       return cosQ(xa, xb);
/* 1585:     */     case 2: 
/* 1586:2219 */       return -sinQ(xa, xb);
/* 1587:     */     case 3: 
/* 1588:2221 */       return -cosQ(xa, xb);
/* 1589:     */     }
/* 1590:2223 */     return (0.0D / 0.0D);
/* 1591:     */   }
/* 1592:     */   
/* 1593:     */   public static double cos(double x)
/* 1594:     */   {
/* 1595:2233 */     int quadrant = 0;
/* 1596:     */     
/* 1597:     */ 
/* 1598:2236 */     double xa = x;
/* 1599:2237 */     if (x < 0.0D) {
/* 1600:2238 */       xa = -xa;
/* 1601:     */     }
/* 1602:2241 */     if ((xa != xa) || (xa == (1.0D / 0.0D))) {
/* 1603:2242 */       return (0.0D / 0.0D);
/* 1604:     */     }
/* 1605:2246 */     double xb = 0.0D;
/* 1606:2247 */     if (xa > 3294198.0D)
/* 1607:     */     {
/* 1608:2251 */       double[] reduceResults = new double[3];
/* 1609:2252 */       reducePayneHanek(xa, reduceResults);
/* 1610:2253 */       quadrant = (int)reduceResults[0] & 0x3;
/* 1611:2254 */       xa = reduceResults[1];
/* 1612:2255 */       xb = reduceResults[2];
/* 1613:     */     }
/* 1614:2256 */     else if (xa > 1.570796326794897D)
/* 1615:     */     {
/* 1616:2261 */       int k = (int)(xa * 0.6366197723675814D);
/* 1617:     */       double remA;
/* 1618:     */       double remB;
/* 1619:     */       for (;;)
/* 1620:     */       {
/* 1621:2267 */         double a = -k * 1.570796251296997D;
/* 1622:2268 */         remA = xa + a;
/* 1623:2269 */         remB = -(remA - xa - a);
/* 1624:     */         
/* 1625:2271 */         a = -k * 7.549789948768648E-008D;
/* 1626:2272 */         double b = remA;
/* 1627:2273 */         remA = a + b;
/* 1628:2274 */         remB += -(remA - b - a);
/* 1629:     */         
/* 1630:2276 */         a = -k * 6.123233995736766E-017D;
/* 1631:2277 */         b = remA;
/* 1632:2278 */         remA = a + b;
/* 1633:2279 */         remB += -(remA - b - a);
/* 1634:2281 */         if (remA > 0.0D) {
/* 1635:     */           break;
/* 1636:     */         }
/* 1637:2288 */         k--;
/* 1638:     */       }
/* 1639:2290 */       quadrant = k & 0x3;
/* 1640:2291 */       xa = remA;
/* 1641:2292 */       xb = remB;
/* 1642:     */     }
/* 1643:2298 */     switch (quadrant)
/* 1644:     */     {
/* 1645:     */     case 0: 
/* 1646:2300 */       return cosQ(xa, xb);
/* 1647:     */     case 1: 
/* 1648:2302 */       return -sinQ(xa, xb);
/* 1649:     */     case 2: 
/* 1650:2304 */       return -cosQ(xa, xb);
/* 1651:     */     case 3: 
/* 1652:2306 */       return sinQ(xa, xb);
/* 1653:     */     }
/* 1654:2308 */     return (0.0D / 0.0D);
/* 1655:     */   }
/* 1656:     */   
/* 1657:     */   public static double tan(double x)
/* 1658:     */   {
/* 1659:2318 */     boolean negative = false;
/* 1660:2319 */     int quadrant = 0;
/* 1661:     */     
/* 1662:     */ 
/* 1663:2322 */     double xa = x;
/* 1664:2323 */     if (x < 0.0D)
/* 1665:     */     {
/* 1666:2324 */       negative = true;
/* 1667:2325 */       xa = -xa;
/* 1668:     */     }
/* 1669:2329 */     if (xa == 0.0D)
/* 1670:     */     {
/* 1671:2330 */       long bits = Double.doubleToLongBits(x);
/* 1672:2331 */       if (bits < 0L) {
/* 1673:2332 */         return -0.0D;
/* 1674:     */       }
/* 1675:2334 */       return 0.0D;
/* 1676:     */     }
/* 1677:2337 */     if ((xa != xa) || (xa == (1.0D / 0.0D))) {
/* 1678:2338 */       return (0.0D / 0.0D);
/* 1679:     */     }
/* 1680:2342 */     double xb = 0.0D;
/* 1681:2343 */     if (xa > 3294198.0D)
/* 1682:     */     {
/* 1683:2347 */       double[] reduceResults = new double[3];
/* 1684:2348 */       reducePayneHanek(xa, reduceResults);
/* 1685:2349 */       quadrant = (int)reduceResults[0] & 0x3;
/* 1686:2350 */       xa = reduceResults[1];
/* 1687:2351 */       xb = reduceResults[2];
/* 1688:     */     }
/* 1689:2352 */     else if (xa > 1.570796326794897D)
/* 1690:     */     {
/* 1691:2357 */       int k = (int)(xa * 0.6366197723675814D);
/* 1692:     */       double remA;
/* 1693:     */       double remB;
/* 1694:     */       for (;;)
/* 1695:     */       {
/* 1696:2363 */         double a = -k * 1.570796251296997D;
/* 1697:2364 */         remA = xa + a;
/* 1698:2365 */         remB = -(remA - xa - a);
/* 1699:     */         
/* 1700:2367 */         a = -k * 7.549789948768648E-008D;
/* 1701:2368 */         double b = remA;
/* 1702:2369 */         remA = a + b;
/* 1703:2370 */         remB += -(remA - b - a);
/* 1704:     */         
/* 1705:2372 */         a = -k * 6.123233995736766E-017D;
/* 1706:2373 */         b = remA;
/* 1707:2374 */         remA = a + b;
/* 1708:2375 */         remB += -(remA - b - a);
/* 1709:2377 */         if (remA > 0.0D) {
/* 1710:     */           break;
/* 1711:     */         }
/* 1712:2384 */         k--;
/* 1713:     */       }
/* 1714:2386 */       quadrant = k & 0x3;
/* 1715:2387 */       xa = remA;
/* 1716:2388 */       xb = remB;
/* 1717:     */     }
/* 1718:2391 */     if (xa > 1.5D)
/* 1719:     */     {
/* 1720:2393 */       double pi2a = 1.570796326794897D;
/* 1721:2394 */       double pi2b = 6.123233995736766E-017D;
/* 1722:     */       
/* 1723:2396 */       double a = 1.570796326794897D - xa;
/* 1724:2397 */       double b = -(a - 1.570796326794897D + xa);
/* 1725:2398 */       b += 6.123233995736766E-017D - xb;
/* 1726:     */       
/* 1727:2400 */       xa = a + b;
/* 1728:2401 */       xb = -(xa - a - b);
/* 1729:2402 */       quadrant ^= 0x1;
/* 1730:2403 */       negative ^= true;
/* 1731:     */     }
/* 1732:     */     double result;
/* 1733:     */     
/* 1734:2407 */     if ((quadrant & 0x1) == 0) {
/* 1735:2408 */       result = tanQ(xa, xb, false);
/* 1736:     */     } else {
/* 1737:2410 */       result = -tanQ(xa, xb, true);
/* 1738:     */     }
/* 1739:2413 */     if (negative) {
/* 1740:2414 */       result = -result;
/* 1741:     */     }
/* 1742:2417 */     return result;
/* 1743:     */   }
/* 1744:     */   
/* 1745:     */   public static double atan(double x)
/* 1746:     */   {
/* 1747:2426 */     return atan(x, 0.0D, false);
/* 1748:     */   }
/* 1749:     */   
/* 1750:     */   private static double atan(double xa, double xb, boolean leftPlane)
/* 1751:     */   {
/* 1752:2436 */     boolean negate = false;
/* 1753:2439 */     if (xa == 0.0D) {
/* 1754:2440 */       return leftPlane ? copySign(3.141592653589793D, xa) : xa;
/* 1755:     */     }
/* 1756:2443 */     if (xa < 0.0D)
/* 1757:     */     {
/* 1758:2445 */       xa = -xa;
/* 1759:2446 */       xb = -xb;
/* 1760:2447 */       negate = true;
/* 1761:     */     }
/* 1762:2450 */     if (xa > 16331239353195370.0D) {
/* 1763:2451 */       return (negate ^ leftPlane) ? -1.570796326794897D : 1.570796326794897D;
/* 1764:     */     }
/* 1765:     */     int idx;
/* 1766:     */    
/* 1767:2455 */     if (xa < 1.0D)
/* 1768:     */     {
/* 1769:2456 */       idx = (int)((-1.716814692820414D * xa * xa + 8.0D) * xa + 0.5D);
/* 1770:     */     }
/* 1771:     */     else
/* 1772:     */     {
/* 1773:2458 */       double oneOverXa = 1.0D / xa;
/* 1774:2459 */       idx = (int)(-((-1.716814692820414D * oneOverXa * oneOverXa + 8.0D) * oneOverXa) + 13.07D);
/* 1775:     */     }
/* 1776:2461 */     double epsA = xa - TANGENT_TABLE_A[idx];
/* 1777:2462 */     double epsB = -(epsA - xa + TANGENT_TABLE_A[idx]);
/* 1778:2463 */     epsB += xb - TANGENT_TABLE_B[idx];
/* 1779:     */     
/* 1780:2465 */     double temp = epsA + epsB;
/* 1781:2466 */     epsB = -(temp - epsA - epsB);
/* 1782:2467 */     epsA = temp;
/* 1783:     */     
/* 1784:     */ 
/* 1785:2470 */     temp = xa * 1073741824.0D;
/* 1786:2471 */     double ya = xa + temp - temp;
/* 1787:2472 */     double yb = xb + xa - ya;
/* 1788:2473 */     xa = ya;
/* 1789:2474 */     xb += yb;
/* 1790:2477 */     if (idx == 0)
/* 1791:     */     {
/* 1792:2480 */       double denom = 1.0D / (1.0D + (xa + xb) * (TANGENT_TABLE_A[idx] + TANGENT_TABLE_B[idx]));
/* 1793:     */       
/* 1794:2482 */       ya = epsA * denom;
/* 1795:2483 */       yb = epsB * denom;
/* 1796:     */     }
/* 1797:     */     else
/* 1798:     */     {
/* 1799:2485 */       double temp2 = xa * TANGENT_TABLE_A[idx];
/* 1800:2486 */       double za = 1.0D + temp2;
/* 1801:2487 */       double zb = -(za - 1.0D - temp2);
/* 1802:2488 */       temp2 = xb * TANGENT_TABLE_A[idx] + xa * TANGENT_TABLE_B[idx];
/* 1803:2489 */       temp = za + temp2;
/* 1804:2490 */       zb += -(temp - za - temp2);
/* 1805:2491 */       za = temp;
/* 1806:     */       
/* 1807:2493 */       zb += xb * TANGENT_TABLE_B[idx];
/* 1808:2494 */       ya = epsA / za;
/* 1809:     */       
/* 1810:2496 */       temp = ya * 1073741824.0D;
/* 1811:2497 */       double yaa = ya + temp - temp;
/* 1812:2498 */       double yab = ya - yaa;
/* 1813:     */       
/* 1814:2500 */       temp = za * 1073741824.0D;
/* 1815:2501 */       double zaa = za + temp - temp;
/* 1816:2502 */       double zab = za - zaa;
/* 1817:     */       
/* 1818:     */ 
/* 1819:2505 */       yb = (epsA - yaa * zaa - yaa * zab - yab * zaa - yab * zab) / za;
/* 1820:     */       
/* 1821:2507 */       yb += -epsA * zb / za / za;
/* 1822:2508 */       yb += epsB / za;
/* 1823:     */     }
/* 1824:2512 */     epsA = ya;
/* 1825:2513 */     epsB = yb;
/* 1826:     */     
/* 1827:     */ 
/* 1828:2516 */     double epsA2 = epsA * epsA;
/* 1829:     */     
/* 1830:     */ 
/* 1831:     */ 
/* 1832:     */ 
/* 1833:     */ 
/* 1834:     */ 
/* 1835:     */ 
/* 1836:     */ 
/* 1837:     */ 
/* 1838:     */ 
/* 1839:2527 */     yb = 0.07490822288864472D;
/* 1840:2528 */     yb = yb * epsA2 + -0.09088450866185192D;
/* 1841:2529 */     yb = yb * epsA2 + 0.1111109594231331D;
/* 1842:2530 */     yb = yb * epsA2 + -0.1428571423679182D;
/* 1843:2531 */     yb = yb * epsA2 + 0.1999999999992358D;
/* 1844:2532 */     yb = yb * epsA2 + -0.3333333333333329D;
/* 1845:2533 */     yb = yb * epsA2 * epsA;
/* 1846:     */     
/* 1847:     */ 
/* 1848:2536 */     ya = epsA;
/* 1849:     */     
/* 1850:2538 */     temp = ya + yb;
/* 1851:2539 */     yb = -(temp - ya - yb);
/* 1852:2540 */     ya = temp;
/* 1853:     */     
/* 1854:     */ 
/* 1855:2543 */     yb += epsB / (1.0D + epsA * epsA);
/* 1856:     */     
/* 1857:     */ 
/* 1858:2546 */     double za = EIGHTHS[idx] + ya;
/* 1859:2547 */     double zb = -(za - EIGHTHS[idx] - ya);
/* 1860:2548 */     temp = za + yb;
/* 1861:2549 */     zb += -(temp - za - yb);
/* 1862:2550 */     za = temp;
/* 1863:     */     
/* 1864:2552 */     double result = za + zb;
/* 1865:2553 */     double resultb = -(result - za - zb);
/* 1866:2555 */     if (leftPlane)
/* 1867:     */     {
/* 1868:2557 */       double pia = 3.141592653589793D;
/* 1869:2558 */       double pib = 1.224646799147353E-016D;
/* 1870:     */       
/* 1871:2560 */       za = 3.141592653589793D - result;
/* 1872:2561 */       zb = -(za - 3.141592653589793D + result);
/* 1873:2562 */       zb += 1.224646799147353E-016D - resultb;
/* 1874:     */       
/* 1875:2564 */       result = za + zb;
/* 1876:2565 */       resultb = -(result - za - zb);
/* 1877:     */     }
/* 1878:2569 */     if ((negate ^ leftPlane)) {
/* 1879:2570 */       result = -result;
/* 1880:     */     }
/* 1881:2573 */     return result;
/* 1882:     */   }
/* 1883:     */   
/* 1884:     */   public static double atan2(double y, double x)
/* 1885:     */   {
/* 1886:2583 */     if ((x != x) || (y != y)) {
/* 1887:2584 */       return (0.0D / 0.0D);
/* 1888:     */     }
/* 1889:2587 */     if (y == 0.0D)
/* 1890:     */     {
/* 1891:2588 */       double result = x * y;
/* 1892:2589 */       double invx = 1.0D / x;
/* 1893:2590 */       double invy = 1.0D / y;
/* 1894:2592 */       if (invx == 0.0D)
/* 1895:     */       {
/* 1896:2593 */         if (x > 0.0D) {
/* 1897:2594 */           return y;
/* 1898:     */         }
/* 1899:2596 */         return copySign(3.141592653589793D, y);
/* 1900:     */       }
/* 1901:2600 */       if ((x < 0.0D) || (invx < 0.0D))
/* 1902:     */       {
/* 1903:2601 */         if ((y < 0.0D) || (invy < 0.0D)) {
/* 1904:2602 */           return -3.141592653589793D;
/* 1905:     */         }
/* 1906:2604 */         return 3.141592653589793D;
/* 1907:     */       }
/* 1908:2607 */       return result;
/* 1909:     */     }
/* 1910:2613 */     if (y == (1.0D / 0.0D))
/* 1911:     */     {
/* 1912:2614 */       if (x == (1.0D / 0.0D)) {
/* 1913:2615 */         return 0.7853981633974483D;
/* 1914:     */       }
/* 1915:2618 */       if (x == (-1.0D / 0.0D)) {
/* 1916:2619 */         return 2.356194490192345D;
/* 1917:     */       }
/* 1918:2622 */       return 1.570796326794897D;
/* 1919:     */     }
/* 1920:2625 */     if (y == (-1.0D / 0.0D))
/* 1921:     */     {
/* 1922:2626 */       if (x == (1.0D / 0.0D)) {
/* 1923:2627 */         return -0.7853981633974483D;
/* 1924:     */       }
/* 1925:2630 */       if (x == (-1.0D / 0.0D)) {
/* 1926:2631 */         return -2.356194490192345D;
/* 1927:     */       }
/* 1928:2634 */       return -1.570796326794897D;
/* 1929:     */     }
/* 1930:2637 */     if (x == (1.0D / 0.0D))
/* 1931:     */     {
/* 1932:2638 */       if ((y > 0.0D) || (1.0D / y > 0.0D)) {
/* 1933:2639 */         return 0.0D;
/* 1934:     */       }
/* 1935:2642 */       if ((y < 0.0D) || (1.0D / y < 0.0D)) {
/* 1936:2643 */         return -0.0D;
/* 1937:     */       }
/* 1938:     */     }
/* 1939:2647 */     if (x == (-1.0D / 0.0D))
/* 1940:     */     {
/* 1941:2649 */       if ((y > 0.0D) || (1.0D / y > 0.0D)) {
/* 1942:2650 */         return 3.141592653589793D;
/* 1943:     */       }
/* 1944:2653 */       if ((y < 0.0D) || (1.0D / y < 0.0D)) {
/* 1945:2654 */         return -3.141592653589793D;
/* 1946:     */       }
/* 1947:     */     }
/* 1948:2660 */     if (x == 0.0D)
/* 1949:     */     {
/* 1950:2661 */       if ((y > 0.0D) || (1.0D / y > 0.0D)) {
/* 1951:2662 */         return 1.570796326794897D;
/* 1952:     */       }
/* 1953:2665 */       if ((y < 0.0D) || (1.0D / y < 0.0D)) {
/* 1954:2666 */         return -1.570796326794897D;
/* 1955:     */       }
/* 1956:     */     }
/* 1957:2671 */     double r = y / x;
/* 1958:2672 */     if (Double.isInfinite(r)) {
/* 1959:2673 */       return atan(r, 0.0D, x < 0.0D);
/* 1960:     */     }
/* 1961:2676 */     double ra = doubleHighPart(r);
/* 1962:2677 */     double rb = r - ra;
/* 1963:     */     
/* 1964:     */ 
/* 1965:2680 */     double xa = doubleHighPart(x);
/* 1966:2681 */     double xb = x - xa;
/* 1967:     */     
/* 1968:2683 */     rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;
/* 1969:     */     
/* 1970:2685 */     double temp = ra + rb;
/* 1971:2686 */     rb = -(temp - ra - rb);
/* 1972:2687 */     ra = temp;
/* 1973:2689 */     if (ra == 0.0D) {
/* 1974:2690 */       ra = copySign(0.0D, y);
/* 1975:     */     }
/* 1976:2694 */     double result = atan(ra, rb, x < 0.0D);
/* 1977:     */     
/* 1978:2696 */     return result;
/* 1979:     */   }
/* 1980:     */   
/* 1981:     */   public static double asin(double x)
/* 1982:     */   {
/* 1983:2704 */     if (x != x) {
/* 1984:2705 */       return (0.0D / 0.0D);
/* 1985:     */     }
/* 1986:2708 */     if ((x > 1.0D) || (x < -1.0D)) {
/* 1987:2709 */       return (0.0D / 0.0D);
/* 1988:     */     }
/* 1989:2712 */     if (x == 1.0D) {
/* 1990:2713 */       return 1.570796326794897D;
/* 1991:     */     }
/* 1992:2716 */     if (x == -1.0D) {
/* 1993:2717 */       return -1.570796326794897D;
/* 1994:     */     }
/* 1995:2720 */     if (x == 0.0D) {
/* 1996:2721 */       return x;
/* 1997:     */     }
/* 1998:2727 */     double temp = x * 1073741824.0D;
/* 1999:2728 */     double xa = x + temp - temp;
/* 2000:2729 */     double xb = x - xa;
/* 2001:     */     
/* 2002:     */ 
/* 2003:2732 */     double ya = xa * xa;
/* 2004:2733 */     double yb = xa * xb * 2.0D + xb * xb;
/* 2005:     */     
/* 2006:     */ 
/* 2007:2736 */     ya = -ya;
/* 2008:2737 */     yb = -yb;
/* 2009:     */     
/* 2010:2739 */     double za = 1.0D + ya;
/* 2011:2740 */     double zb = -(za - 1.0D - ya);
/* 2012:     */     
/* 2013:2742 */     temp = za + yb;
/* 2014:2743 */     zb += -(temp - za - yb);
/* 2015:2744 */     za = temp;
/* 2016:     */     
/* 2017:     */ 
/* 2018:     */ 
/* 2019:2748 */     double y = sqrt(za);
/* 2020:2749 */     temp = y * 1073741824.0D;
/* 2021:2750 */     ya = y + temp - temp;
/* 2022:2751 */     yb = y - ya;
/* 2023:     */     
/* 2024:     */ 
/* 2025:2754 */     yb += (za - ya * ya - 2.0D * ya * yb - yb * yb) / (2.0D * y);
/* 2026:     */     
/* 2027:     */ 
/* 2028:2757 */     double dx = zb / (2.0D * y);
/* 2029:     */     
/* 2030:     */ 
/* 2031:2760 */     double r = x / y;
/* 2032:2761 */     temp = r * 1073741824.0D;
/* 2033:2762 */     double ra = r + temp - temp;
/* 2034:2763 */     double rb = r - ra;
/* 2035:     */     
/* 2036:2765 */     rb += (x - ra * ya - ra * yb - rb * ya - rb * yb) / y;
/* 2037:2766 */     rb += -x * dx / y / y;
/* 2038:     */     
/* 2039:2768 */     temp = ra + rb;
/* 2040:2769 */     rb = -(temp - ra - rb);
/* 2041:2770 */     ra = temp;
/* 2042:     */     
/* 2043:2772 */     return atan(ra, rb, false);
/* 2044:     */   }
/* 2045:     */   
/* 2046:     */   public static double acos(double x)
/* 2047:     */   {
/* 2048:2780 */     if (x != x) {
/* 2049:2781 */       return (0.0D / 0.0D);
/* 2050:     */     }
/* 2051:2784 */     if ((x > 1.0D) || (x < -1.0D)) {
/* 2052:2785 */       return (0.0D / 0.0D);
/* 2053:     */     }
/* 2054:2788 */     if (x == -1.0D) {
/* 2055:2789 */       return 3.141592653589793D;
/* 2056:     */     }
/* 2057:2792 */     if (x == 1.0D) {
/* 2058:2793 */       return 0.0D;
/* 2059:     */     }
/* 2060:2796 */     if (x == 0.0D) {
/* 2061:2797 */       return 1.570796326794897D;
/* 2062:     */     }
/* 2063:2803 */     double temp = x * 1073741824.0D;
/* 2064:2804 */     double xa = x + temp - temp;
/* 2065:2805 */     double xb = x - xa;
/* 2066:     */     
/* 2067:     */ 
/* 2068:2808 */     double ya = xa * xa;
/* 2069:2809 */     double yb = xa * xb * 2.0D + xb * xb;
/* 2070:     */     
/* 2071:     */ 
/* 2072:2812 */     ya = -ya;
/* 2073:2813 */     yb = -yb;
/* 2074:     */     
/* 2075:2815 */     double za = 1.0D + ya;
/* 2076:2816 */     double zb = -(za - 1.0D - ya);
/* 2077:     */     
/* 2078:2818 */     temp = za + yb;
/* 2079:2819 */     zb += -(temp - za - yb);
/* 2080:2820 */     za = temp;
/* 2081:     */     
/* 2082:     */ 
/* 2083:2823 */     double y = sqrt(za);
/* 2084:2824 */     temp = y * 1073741824.0D;
/* 2085:2825 */     ya = y + temp - temp;
/* 2086:2826 */     yb = y - ya;
/* 2087:     */     
/* 2088:     */ 
/* 2089:2829 */     yb += (za - ya * ya - 2.0D * ya * yb - yb * yb) / (2.0D * y);
/* 2090:     */     
/* 2091:     */ 
/* 2092:2832 */     yb += zb / (2.0D * y);
/* 2093:2833 */     y = ya + yb;
/* 2094:2834 */     yb = -(y - ya - yb);
/* 2095:     */     
/* 2096:     */ 
/* 2097:2837 */     double r = y / x;
/* 2098:2840 */     if (Double.isInfinite(r)) {
/* 2099:2841 */       return 1.570796326794897D;
/* 2100:     */     }
/* 2101:2844 */     double ra = doubleHighPart(r);
/* 2102:2845 */     double rb = r - ra;
/* 2103:     */     
/* 2104:2847 */     rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;
/* 2105:2848 */     rb += yb / x;
/* 2106:     */     
/* 2107:2850 */     temp = ra + rb;
/* 2108:2851 */     rb = -(temp - ra - rb);
/* 2109:2852 */     ra = temp;
/* 2110:     */     
/* 2111:2854 */     return atan(ra, rb, x < 0.0D);
/* 2112:     */   }
/* 2113:     */   
/* 2114:     */   public static double cbrt(double x)
/* 2115:     */   {
/* 2116:2863 */     long inbits = Double.doubleToLongBits(x);
/* 2117:2864 */     int exponent = (int)(inbits >> 52 & 0x7FF) - 1023;
/* 2118:2865 */     boolean subnormal = false;
/* 2119:2867 */     if (exponent == -1023)
/* 2120:     */     {
/* 2121:2868 */       if (x == 0.0D) {
/* 2122:2869 */         return x;
/* 2123:     */       }
/* 2124:2873 */       subnormal = true;
/* 2125:2874 */       x *= 18014398509481984.0D;
/* 2126:2875 */       inbits = Double.doubleToLongBits(x);
/* 2127:2876 */       exponent = (int)(inbits >> 52 & 0x7FF) - 1023;
/* 2128:     */     }
/* 2129:2879 */     if (exponent == 1024) {
/* 2130:2881 */       return x;
/* 2131:     */     }
/* 2132:2885 */     int exp3 = exponent / 3;
/* 2133:     */     
/* 2134:     */ 
/* 2135:2888 */     double p2 = Double.longBitsToDouble(inbits & 0x0 | (exp3 + 1023 & 0x7FF) << 52);
/* 2136:     */     
/* 2137:     */ 
/* 2138:     */ 
/* 2139:2892 */     double mant = Double.longBitsToDouble(inbits & 0xFFFFFFFF | 0x0);
/* 2140:     */     
/* 2141:     */ 
/* 2142:2895 */     double est = -0.01071469073319593D;
/* 2143:2896 */     est = est * mant + 0.0875862700108075D;
/* 2144:2897 */     est = est * mant + -0.3058015757857271D;
/* 2145:2898 */     est = est * mant + 0.7249995199969751D;
/* 2146:2899 */     est = est * mant + 0.5039018405998234D;
/* 2147:     */     
/* 2148:2901 */     est *= CBRTTWO[(exponent % 3 + 2)];
/* 2149:     */     
/* 2150:     */ 
/* 2151:     */ 
/* 2152:     */ 
/* 2153:2906 */     double xs = x / (p2 * p2 * p2);
/* 2154:2907 */     est += (xs - est * est * est) / (3.0D * est * est);
/* 2155:2908 */     est += (xs - est * est * est) / (3.0D * est * est);
/* 2156:     */     
/* 2157:     */ 
/* 2158:2911 */     double temp = est * 1073741824.0D;
/* 2159:2912 */     double ya = est + temp - temp;
/* 2160:2913 */     double yb = est - ya;
/* 2161:     */     
/* 2162:2915 */     double za = ya * ya;
/* 2163:2916 */     double zb = ya * yb * 2.0D + yb * yb;
/* 2164:2917 */     temp = za * 1073741824.0D;
/* 2165:2918 */     double temp2 = za + temp - temp;
/* 2166:2919 */     zb += za - temp2;
/* 2167:2920 */     za = temp2;
/* 2168:     */     
/* 2169:2922 */     zb = za * yb + ya * zb + zb * yb;
/* 2170:2923 */     za *= ya;
/* 2171:     */     
/* 2172:2925 */     double na = xs - za;
/* 2173:2926 */     double nb = -(na - xs + za);
/* 2174:2927 */     nb -= zb;
/* 2175:     */     
/* 2176:2929 */     est += (na + nb) / (3.0D * est * est);
/* 2177:     */     
/* 2178:     */ 
/* 2179:2932 */     est *= p2;
/* 2180:2934 */     if (subnormal) {
/* 2181:2935 */       est *= 3.814697265625E-006D;
/* 2182:     */     }
/* 2183:2938 */     return est;
/* 2184:     */   }
/* 2185:     */   
/* 2186:     */   public static double toRadians(double x)
/* 2187:     */   {
/* 2188:2948 */     if ((Double.isInfinite(x)) || (x == 0.0D)) {
/* 2189:2949 */       return x;
/* 2190:     */     }
/* 2191:2953 */     double facta = 0.01745329052209854D;
/* 2192:2954 */     double factb = 1.997844754509471E-009D;
/* 2193:     */     
/* 2194:2956 */     double xa = doubleHighPart(x);
/* 2195:2957 */     double xb = x - xa;
/* 2196:     */     
/* 2197:2959 */     double result = xb * 1.997844754509471E-009D + xb * 0.01745329052209854D + xa * 1.997844754509471E-009D + xa * 0.01745329052209854D;
/* 2198:2960 */     if (result == 0.0D) {
/* 2199:2961 */       result *= x;
/* 2200:     */     }
/* 2201:2963 */     return result;
/* 2202:     */   }
/* 2203:     */   
/* 2204:     */   public static double toDegrees(double x)
/* 2205:     */   {
/* 2206:2973 */     if ((Double.isInfinite(x)) || (x == 0.0D)) {
/* 2207:2974 */       return x;
/* 2208:     */     }
/* 2209:2978 */     double facta = 57.2957763671875D;
/* 2210:2979 */     double factb = 3.145894820876798E-006D;
/* 2211:     */     
/* 2212:2981 */     double xa = doubleHighPart(x);
/* 2213:2982 */     double xb = x - xa;
/* 2214:     */     
/* 2215:2984 */     return xb * 3.145894820876798E-006D + xb * 57.2957763671875D + xa * 3.145894820876798E-006D + xa * 57.2957763671875D;
/* 2216:     */   }
/* 2217:     */   
/* 2218:     */   public static int abs(int x)
/* 2219:     */   {
/* 2220:2993 */     return x < 0 ? -x : x;
/* 2221:     */   }
/* 2222:     */   
/* 2223:     */   public static long abs(long x)
/* 2224:     */   {
/* 2225:3002 */     return x < 0L ? -x : x;
/* 2226:     */   }
/* 2227:     */   
/* 2228:     */   public static float abs(float x)
/* 2229:     */   {
/* 2230:3011 */     return x == 0.0F ? 0.0F : x < 0.0F ? -x : x;
/* 2231:     */   }
/* 2232:     */   
/* 2233:     */   public static double abs(double x)
/* 2234:     */   {
/* 2235:3020 */     return x == 0.0D ? 0.0D : x < 0.0D ? -x : x;
/* 2236:     */   }
/* 2237:     */   
/* 2238:     */   public static double ulp(double x)
/* 2239:     */   {
/* 2240:3029 */     if (Double.isInfinite(x)) {
/* 2241:3030 */       return (1.0D / 0.0D);
/* 2242:     */     }
/* 2243:3032 */     return abs(x - Double.longBitsToDouble(Double.doubleToLongBits(x) ^ 1L));
/* 2244:     */   }
/* 2245:     */   
/* 2246:     */   public static float ulp(float x)
/* 2247:     */   {
/* 2248:3041 */     if (Float.isInfinite(x)) {
/* 2249:3042 */       return (1.0F / 1.0F);
/* 2250:     */     }
/* 2251:3044 */     return abs(x - Float.intBitsToFloat(Float.floatToIntBits(x) ^ 0x1));
/* 2252:     */   }
/* 2253:     */   
/* 2254:     */   public static double scalb(double d, int n)
/* 2255:     */   {
/* 2256:3056 */     if ((n > -1023) && (n < 1024)) {
/* 2257:3057 */       return d * Double.longBitsToDouble(n + 1023 << 52);
/* 2258:     */     }
/* 2259:3061 */     if ((Double.isNaN(d)) || (Double.isInfinite(d)) || (d == 0.0D)) {
/* 2260:3062 */       return d;
/* 2261:     */     }
/* 2262:3064 */     if (n < -2098) {
/* 2263:3065 */       return d > 0.0D ? 0.0D : -0.0D;
/* 2264:     */     }
/* 2265:3067 */     if (n > 2097) {
/* 2266:3068 */       return d > 0.0D ? (1.0D / 0.0D) : (-1.0D / 0.0D);
/* 2267:     */     }
/* 2268:3072 */     long bits = Double.doubleToLongBits(d);
/* 2269:3073 */     long sign = bits & 0x0;
/* 2270:3074 */     int exponent = (int)(bits >>> 52) & 0x7FF;
/* 2271:3075 */     long mantissa = bits & 0xFFFFFFFF;
/* 2272:     */     
/* 2273:     */ 
/* 2274:3078 */     int scaledExponent = exponent + n;
/* 2275:3080 */     if (n < 0)
/* 2276:     */     {
/* 2277:3082 */       if (scaledExponent > 0) {
/* 2278:3084 */         return Double.longBitsToDouble(sign | scaledExponent << 52 | mantissa);
/* 2279:     */       }
/* 2280:3085 */       if (scaledExponent > -53)
/* 2281:     */       {
/* 2282:3089 */         mantissa |= 0x0;
/* 2283:     */         
/* 2284:     */ 
/* 2285:3092 */         long mostSignificantLostBit = mantissa & 1L << -scaledExponent;
/* 2286:3093 */         mantissa >>>= 1 - scaledExponent;
/* 2287:3094 */         if (mostSignificantLostBit != 0L) {
/* 2288:3096 */           mantissa += 1L;
/* 2289:     */         }
/* 2290:3098 */         return Double.longBitsToDouble(sign | mantissa);
/* 2291:     */       }
/* 2292:3102 */       return sign == 0L ? 0.0D : -0.0D;
/* 2293:     */     }
/* 2294:3106 */     if (exponent == 0)
/* 2295:     */     {
/* 2296:3109 */       while (mantissa >>> 52 != 1L)
/* 2297:     */       {
/* 2298:3110 */         mantissa <<= 1;
/* 2299:3111 */         scaledExponent--;
/* 2300:     */       }
/* 2301:3113 */       scaledExponent++;
/* 2302:3114 */       mantissa &= 0xFFFFFFFF;
/* 2303:3116 */       if (scaledExponent < 2047) {
/* 2304:3117 */         return Double.longBitsToDouble(sign | scaledExponent << 52 | mantissa);
/* 2305:     */       }
/* 2306:3119 */       return sign == 0L ? (1.0D / 0.0D) : (-1.0D / 0.0D);
/* 2307:     */     }
/* 2308:3122 */     if (scaledExponent < 2047) {
/* 2309:3123 */       return Double.longBitsToDouble(sign | scaledExponent << 52 | mantissa);
/* 2310:     */     }
/* 2311:3125 */     return sign == 0L ? (1.0D / 0.0D) : (-1.0D / 0.0D);
/* 2312:     */   }
/* 2313:     */   
/* 2314:     */   public static float scalb(float f, int n)
/* 2315:     */   {
/* 2316:3140 */     if ((n > -127) && (n < 128)) {
/* 2317:3141 */       return f * Float.intBitsToFloat(n + 127 << 23);
/* 2318:     */     }
/* 2319:3145 */     if ((Float.isNaN(f)) || (Float.isInfinite(f)) || (f == 0.0F)) {
/* 2320:3146 */       return f;
/* 2321:     */     }
/* 2322:3148 */     if (n < -277) {
/* 2323:3149 */       return f > 0.0F ? 0.0F : -0.0F;
/* 2324:     */     }
/* 2325:3151 */     if (n > 276) {
/* 2326:3152 */       return f > 0.0F ? (1.0F / 1.0F) : (1.0F / -1.0F);
/* 2327:     */     }
/* 2328:3156 */     int bits = Float.floatToIntBits(f);
/* 2329:3157 */     int sign = bits & 0x80000000;
/* 2330:3158 */     int exponent = bits >>> 23 & 0xFF;
/* 2331:3159 */     int mantissa = bits & 0x7FFFFF;
/* 2332:     */     
/* 2333:     */ 
/* 2334:3162 */     int scaledExponent = exponent + n;
/* 2335:3164 */     if (n < 0)
/* 2336:     */     {
/* 2337:3166 */       if (scaledExponent > 0) {
/* 2338:3168 */         return Float.intBitsToFloat(sign | scaledExponent << 23 | mantissa);
/* 2339:     */       }
/* 2340:3169 */       if (scaledExponent > -24)
/* 2341:     */       {
/* 2342:3173 */         mantissa |= 0x800000;
/* 2343:     */         
/* 2344:     */ 
/* 2345:3176 */         int mostSignificantLostBit = mantissa & 1 << -scaledExponent;
/* 2346:3177 */         mantissa >>>= 1 - scaledExponent;
/* 2347:3178 */         if (mostSignificantLostBit != 0) {
/* 2348:3180 */           mantissa++;
/* 2349:     */         }
/* 2350:3182 */         return Float.intBitsToFloat(sign | mantissa);
/* 2351:     */       }
/* 2352:3186 */       return sign == 0 ? 0.0F : -0.0F;
/* 2353:     */     }
/* 2354:3190 */     if (exponent == 0)
/* 2355:     */     {
/* 2356:3193 */       while (mantissa >>> 23 != 1)
/* 2357:     */       {
/* 2358:3194 */         mantissa <<= 1;
/* 2359:3195 */         scaledExponent--;
/* 2360:     */       }
/* 2361:3197 */       scaledExponent++;
/* 2362:3198 */       mantissa &= 0x7FFFFF;
/* 2363:3200 */       if (scaledExponent < 255) {
/* 2364:3201 */         return Float.intBitsToFloat(sign | scaledExponent << 23 | mantissa);
/* 2365:     */       }
/* 2366:3203 */       return sign == 0 ? (1.0F / 1.0F) : (1.0F / -1.0F);
/* 2367:     */     }
/* 2368:3206 */     if (scaledExponent < 255) {
/* 2369:3207 */       return Float.intBitsToFloat(sign | scaledExponent << 23 | mantissa);
/* 2370:     */     }
/* 2371:3209 */     return sign == 0 ? (1.0F / 1.0F) : (1.0F / -1.0F);
/* 2372:     */   }
/* 2373:     */   
/* 2374:     */   public static double nextAfter(double d, double direction)
/* 2375:     */   {
/* 2376:3249 */     if ((Double.isNaN(d)) || (Double.isNaN(direction))) {
/* 2377:3250 */       return (0.0D / 0.0D);
/* 2378:     */     }
/* 2379:3251 */     if (d == direction) {
/* 2380:3252 */       return direction;
/* 2381:     */     }
/* 2382:3253 */     if (Double.isInfinite(d)) {
/* 2383:3254 */       return d < 0.0D ? -1.797693134862316E+307D : 1.7976931348623157E+308D;
/* 2384:     */     }
/* 2385:3255 */     if (d == 0.0D) {
/* 2386:3256 */       return direction < 0.0D ? -4.940656458412465E-324D : 4.9E-324D;
/* 2387:     */     }
/* 2388:3261 */     long bits = Double.doubleToLongBits(d);
/* 2389:3262 */     long sign = bits & 0x0;
/* 2390:3263 */     if (((direction < d ? 1 : 0) ^ (sign == 0L ? 1 : 0)) != 0) {
/* 2391:3264 */       return Double.longBitsToDouble(sign | (bits & 0xFFFFFFFF) + 1L);
/* 2392:     */     }
/* 2393:3266 */     return Double.longBitsToDouble(sign | (bits & 0xFFFFFFFF) - 1L);
/* 2394:     */   }
/* 2395:     */   
/* 2396:     */   public static float nextAfter(float f, double direction)
/* 2397:     */   {
/* 2398:3305 */     if ((Double.isNaN(f)) || (Double.isNaN(direction))) {
/* 2399:3306 */       return (0.0F / 0.0F);
/* 2400:     */     }
/* 2401:3307 */     if (f == direction) {
/* 2402:3308 */       return (float)direction;
/* 2403:     */     }
/* 2404:3309 */     if (Float.isInfinite(f)) {
/* 2405:3310 */       return f < 0.0F ? -3.402824E+037F : 3.4028235E+38F;
/* 2406:     */     }
/* 2407:3311 */     if (f == 0.0F) {
/* 2408:3312 */       return direction < 0.0D ? -1.401299E-045F : 1.4E-45F;
/* 2409:     */     }
/* 2410:3317 */     int bits = Float.floatToIntBits(f);
/* 2411:3318 */     int sign = bits & 0x80000000;
/* 2412:3319 */     if (((direction < f ? 1 : 0) ^ (sign == 0 ? 1 : 0)) != 0) {
/* 2413:3320 */       return Float.intBitsToFloat(sign | (bits & 0x7FFFFFFF) + 1);
/* 2414:     */     }
/* 2415:3322 */     return Float.intBitsToFloat(sign | (bits & 0x7FFFFFFF) - 1);
/* 2416:     */   }
/* 2417:     */   
/* 2418:     */   public static double floor(double x)
/* 2419:     */   {
/* 2420:3334 */     if (x != x) {
/* 2421:3335 */       return x;
/* 2422:     */     }
/* 2423:3338 */     if ((x >= 4503599627370496.0D) || (x <= -4503599627370496.0D)) {
/* 2424:3339 */       return x;
/* 2425:     */     }
/* 2426:3342 */     long y = (long) x;
/* 2427:3343 */     if ((x < 0.0D) && (y != x)) {
/* 2428:3344 */       y -= 1L;
/* 2429:     */     }
/* 2430:3347 */     if (y == 0L) {
/* 2431:3348 */       return x * y;
/* 2432:     */     }
/* 2433:3351 */     return y;
/* 2434:     */   }
/* 2435:     */   
/* 2436:     */   public static double ceil(double x)
/* 2437:     */   {
/* 2438:3361 */     if (x != x) {
/* 2439:3362 */       return x;
/* 2440:     */     }
/* 2441:3365 */     double y = floor(x);
/* 2442:3366 */     if (y == x) {
/* 2443:3367 */       return y;
/* 2444:     */     }
/* 2445:3370 */     y += 1.0D;
/* 2446:3372 */     if (y == 0.0D) {
/* 2447:3373 */       return x * y;
/* 2448:     */     }
/* 2449:3376 */     return y;
/* 2450:     */   }
/* 2451:     */   
/* 2452:     */   public static double rint(double x)
/* 2453:     */   {
/* 2454:3384 */     double y = floor(x);
/* 2455:3385 */     double d = x - y;
/* 2456:3387 */     if (d > 0.5D)
/* 2457:     */     {
/* 2458:3388 */       if (y == -1.0D) {
/* 2459:3389 */         return -0.0D;
/* 2460:     */       }
/* 2461:3391 */       return y + 1.0D;
/* 2462:     */     }
/* 2463:3393 */     if (d < 0.5D) {
/* 2464:3394 */       return y;
/* 2465:     */     }
/* 2466:3398 */     long z = (long) y;
/* 2467:3399 */     return (z & 1L) == 0L ? y : y + 1.0D;
/* 2468:     */   }
/* 2469:     */   
/* 2470:     */   public static long round(double x)
/* 2471:     */   {
/* 2472:3407 */     return (long) floor(x + 0.5D);
/* 2473:     */   }
/* 2474:     */   
/* 2475:     */   public static int round(float x)
/* 2476:     */   {
/* 2477:3415 */     return (int)floor(x + 0.5F);
/* 2478:     */   }
/* 2479:     */   
/* 2480:     */   public static int min(int a, int b)
/* 2481:     */   {
/* 2482:3424 */     return a <= b ? a : b;
/* 2483:     */   }
/* 2484:     */   
/* 2485:     */   public static long min(long a, long b)
/* 2486:     */   {
/* 2487:3433 */     return a <= b ? a : b;
/* 2488:     */   }
/* 2489:     */   
/* 2490:     */   public static float min(float a, float b)
/* 2491:     */   {
/* 2492:3442 */     if (a > b) {
/* 2493:3443 */       return b;
/* 2494:     */     }
/* 2495:3445 */     if (a < b) {
/* 2496:3446 */       return a;
/* 2497:     */     }
/* 2498:3449 */     if (a != b) {
/* 2499:3450 */       return (0.0F / 0.0F);
/* 2500:     */     }
/* 2501:3454 */     int bits = Float.floatToRawIntBits(a);
/* 2502:3455 */     if (bits == -2147483648) {
/* 2503:3456 */       return a;
/* 2504:     */     }
/* 2505:3458 */     return b;
/* 2506:     */   }
/* 2507:     */   
/* 2508:     */   public static double min(double a, double b)
/* 2509:     */   {
/* 2510:3467 */     if (a > b) {
/* 2511:3468 */       return b;
/* 2512:     */     }
/* 2513:3470 */     if (a < b) {
/* 2514:3471 */       return a;
/* 2515:     */     }
/* 2516:3474 */     if (a != b) {
/* 2517:3475 */       return (0.0D / 0.0D);
/* 2518:     */     }
/* 2519:3479 */     long bits = Double.doubleToRawLongBits(a);
/* 2520:3480 */     if (bits == -9223372036854775808L) {
/* 2521:3481 */       return a;
/* 2522:     */     }
/* 2523:3483 */     return b;
/* 2524:     */   }
/* 2525:     */   
/* 2526:     */   public static int max(int a, int b)
/* 2527:     */   {
/* 2528:3492 */     return a <= b ? b : a;
/* 2529:     */   }
/* 2530:     */   
/* 2531:     */   public static long max(long a, long b)
/* 2532:     */   {
/* 2533:3501 */     return a <= b ? b : a;
/* 2534:     */   }
/* 2535:     */   
/* 2536:     */   public static float max(float a, float b)
/* 2537:     */   {
/* 2538:3510 */     if (a > b) {
/* 2539:3511 */       return a;
/* 2540:     */     }
/* 2541:3513 */     if (a < b) {
/* 2542:3514 */       return b;
/* 2543:     */     }
/* 2544:3517 */     if (a != b) {
/* 2545:3518 */       return (0.0F / 0.0F);
/* 2546:     */     }
/* 2547:3522 */     int bits = Float.floatToRawIntBits(a);
/* 2548:3523 */     if (bits == -2147483648) {
/* 2549:3524 */       return b;
/* 2550:     */     }
/* 2551:3526 */     return a;
/* 2552:     */   }
/* 2553:     */   
/* 2554:     */   public static double max(double a, double b)
/* 2555:     */   {
/* 2556:3535 */     if (a > b) {
/* 2557:3536 */       return a;
/* 2558:     */     }
/* 2559:3538 */     if (a < b) {
/* 2560:3539 */       return b;
/* 2561:     */     }
/* 2562:3542 */     if (a != b) {
/* 2563:3543 */       return (0.0D / 0.0D);
/* 2564:     */     }
/* 2565:3547 */     long bits = Double.doubleToRawLongBits(a);
/* 2566:3548 */     if (bits == -9223372036854775808L) {
/* 2567:3549 */       return b;
/* 2568:     */     }
/* 2569:3551 */     return a;
/* 2570:     */   }
/* 2571:     */   
/* 2572:     */   public static double hypot(double x, double y)
/* 2573:     */   {
/* 2574:3569 */     if ((Double.isInfinite(x)) || (Double.isInfinite(y))) {
/* 2575:3570 */       return (1.0D / 0.0D);
/* 2576:     */     }
/* 2577:3571 */     if ((Double.isNaN(x)) || (Double.isNaN(y))) {
/* 2578:3572 */       return (0.0D / 0.0D);
/* 2579:     */     }
/* 2580:3575 */     int expX = getExponent(x);
/* 2581:3576 */     int expY = getExponent(y);
/* 2582:3577 */     if (expX > expY + 27) {
/* 2583:3579 */       return abs(x);
/* 2584:     */     }
/* 2585:3580 */     if (expY > expX + 27) {
/* 2586:3582 */       return abs(y);
/* 2587:     */     }
/* 2588:3586 */     int middleExp = (expX + expY) / 2;
/* 2589:     */     
/* 2590:     */ 
/* 2591:3589 */     double scaledX = scalb(x, -middleExp);
/* 2592:3590 */     double scaledY = scalb(y, -middleExp);
/* 2593:     */     
/* 2594:     */ 
/* 2595:3593 */     double scaledH = sqrt(scaledX * scaledX + scaledY * scaledY);
/* 2596:     */     
/* 2597:     */ 
/* 2598:3596 */     return scalb(scaledH, middleExp);
/* 2599:     */   }
/* 2600:     */   
/* 2601:     */   public static double IEEEremainder(double dividend, double divisor)
/* 2602:     */   {
/* 2603:3624 */     return StrictMath.IEEEremainder(dividend, divisor);
/* 2604:     */   }
/* 2605:     */   
/* 2606:     */   public static double copySign(double magnitude, double sign)
/* 2607:     */   {
/* 2608:3636 */     long m = Double.doubleToLongBits(magnitude);
/* 2609:3637 */     long s = Double.doubleToLongBits(sign);
/* 2610:3638 */     if (((m >= 0L) && (s >= 0L)) || ((m < 0L) && (s < 0L))) {
/* 2611:3639 */       return magnitude;
/* 2612:     */     }
/* 2613:3641 */     return -magnitude;
/* 2614:     */   }
/* 2615:     */   
/* 2616:     */   public static float copySign(float magnitude, float sign)
/* 2617:     */   {
/* 2618:3653 */     int m = Float.floatToIntBits(magnitude);
/* 2619:3654 */     int s = Float.floatToIntBits(sign);
/* 2620:3655 */     if (((m >= 0) && (s >= 0)) || ((m < 0) && (s < 0))) {
/* 2621:3656 */       return magnitude;
/* 2622:     */     }
/* 2623:3658 */     return -magnitude;
/* 2624:     */   }
/* 2625:     */   
/* 2626:     */   public static int getExponent(double d)
/* 2627:     */   {
/* 2628:3671 */     return (int)(Double.doubleToLongBits(d) >>> 52 & 0x7FF) - 1023;
/* 2629:     */   }
/* 2630:     */   
/* 2631:     */   public static int getExponent(float f)
/* 2632:     */   {
/* 2633:3684 */     return (Float.floatToIntBits(f) >>> 23 & 0xFF) - 127;
/* 2634:     */   }
/* 2635:     */   
/* 2636:     */   public static void main(String[] a)
/* 2637:     */   {
/* 2638:3693 */     PrintStream out = System.out;
/* 2639:3694 */     FastMathCalc.printarray(out, "EXP_INT_TABLE_A", 1500, ExpIntTable.EXP_INT_TABLE_A);
/* 2640:3695 */     FastMathCalc.printarray(out, "EXP_INT_TABLE_B", 1500, ExpIntTable.EXP_INT_TABLE_B);
/* 2641:3696 */     FastMathCalc.printarray(out, "EXP_FRAC_TABLE_A", 1025, ExpFracTable.EXP_FRAC_TABLE_A);
/* 2642:3697 */     FastMathCalc.printarray(out, "EXP_FRAC_TABLE_B", 1025, ExpFracTable.EXP_FRAC_TABLE_B);
/* 2643:3698 */     FastMathCalc.printarray(out, "LN_MANT", 1024, lnMant.LN_MANT);
/* 2644:3699 */     FastMathCalc.printarray(out, "SINE_TABLE_A", 14, SINE_TABLE_A);
/* 2645:3700 */     FastMathCalc.printarray(out, "SINE_TABLE_B", 14, SINE_TABLE_B);
/* 2646:3701 */     FastMathCalc.printarray(out, "COSINE_TABLE_A", 14, COSINE_TABLE_A);
/* 2647:3702 */     FastMathCalc.printarray(out, "COSINE_TABLE_B", 14, COSINE_TABLE_B);
/* 2648:3703 */     FastMathCalc.printarray(out, "TANGENT_TABLE_A", 14, TANGENT_TABLE_A);
/* 2649:3704 */     FastMathCalc.printarray(out, "TANGENT_TABLE_B", 14, TANGENT_TABLE_B);
/* 2650:     */   }
/* 2651:     */   
/* 2652:     */   private static class ExpIntTable
/* 2653:     */   {
/* 2654:3740 */     private static final double[] EXP_INT_TABLE_A  = null;
/* 2655:3741 */     private static final double[] EXP_INT_TABLE_B = FastMathLiteralArrays.loadExpIntB();
/* 2656:     */   }
/* 2657:     */   
/* 2658:     */   private static class ExpFracTable
/* 2659:     */   {
/* 2660:3773 */     private static final double[] EXP_FRAC_TABLE_A  = null;
/* 2661:3774 */     private static final double[] EXP_FRAC_TABLE_B = FastMathLiteralArrays.loadExpFracB();
/* 2662:     */   }
/* 2663:     */   
/* 2664:     */   private static class lnMant
/* 2665:     */   {
/* 2666:3794 */     private static final double[][] LN_MANT  =null ;
/* 2667:     */   }
/* 2668:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.FastMath
 * JD-Core Version:    0.7.0.1
 */
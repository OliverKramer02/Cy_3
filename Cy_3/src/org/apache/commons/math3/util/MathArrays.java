/*    1:     */ package org.apache.commons.math3.util;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Collections;
/*    5:     */ import java.util.Comparator;
/*    6:     */ import java.util.List;

/*    7:     */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*    8:     */ import org.apache.commons.math3.exception.MathArithmeticException;
/*    9:     */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   10:     */ import org.apache.commons.math3.exception.MathInternalError;
/*   11:     */ import org.apache.commons.math3.exception.NonMonotonicSequenceException;
/*   12:     */ import org.apache.commons.math3.exception.NullArgumentException;
/*   13:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   14:     */ 
/*   15:     */ public class MathArrays
/*   16:     */ {
/*   17:     */   private static final int SPLIT_FACTOR = 134217729;
/*   18:     */   
/*   19:     */   public static double distance1(double[] p1, double[] p2)
/*   20:     */   {
/*   21:  56 */     double sum = 0.0D;
/*   22:  57 */     for (int i = 0; i < p1.length; i++) {
/*   23:  58 */       sum += FastMath.abs(p1[i] - p2[i]);
/*   24:     */     }
/*   25:  60 */     return sum;
/*   26:     */   }
/*   27:     */   
/*   28:     */   public static int distance1(int[] p1, int[] p2)
/*   29:     */   {
/*   30:  71 */     int sum = 0;
/*   31:  72 */     for (int i = 0; i < p1.length; i++) {
/*   32:  73 */       sum += FastMath.abs(p1[i] - p2[i]);
/*   33:     */     }
/*   34:  75 */     return sum;
/*   35:     */   }
/*   36:     */   
/*   37:     */   public static double distance(double[] p1, double[] p2)
/*   38:     */   {
/*   39:  86 */     double sum = 0.0D;
/*   40:  87 */     for (int i = 0; i < p1.length; i++)
/*   41:     */     {
/*   42:  88 */       double dp = p1[i] - p2[i];
/*   43:  89 */       sum += dp * dp;
/*   44:     */     }
/*   45:  91 */     return FastMath.sqrt(sum);
/*   46:     */   }
/*   47:     */   
/*   48:     */   public static double distance(int[] p1, int[] p2)
/*   49:     */   {
/*   50: 102 */     double sum = 0.0D;
/*   51: 103 */     for (int i = 0; i < p1.length; i++)
/*   52:     */     {
/*   53: 104 */       double dp = p1[i] - p2[i];
/*   54: 105 */       sum += dp * dp;
/*   55:     */     }
/*   56: 107 */     return FastMath.sqrt(sum);
/*   57:     */   }
/*   58:     */   
/*   59:     */   public static double distanceInf(double[] p1, double[] p2)
/*   60:     */   {
/*   61: 118 */     double max = 0.0D;
/*   62: 119 */     for (int i = 0; i < p1.length; i++) {
/*   63: 120 */       max = FastMath.max(max, FastMath.abs(p1[i] - p2[i]));
/*   64:     */     }
/*   65: 122 */     return max;
/*   66:     */   }
/*   67:     */   
/*   68:     */   public static int distanceInf(int[] p1, int[] p2)
/*   69:     */   {
/*   70: 133 */     int max = 0;
/*   71: 134 */     for (int i = 0; i < p1.length; i++) {
/*   72: 135 */       max = FastMath.max(max, FastMath.abs(p1[i] - p2[i]));
/*   73:     */     }
/*   74: 137 */     return max;
/*   75:     */   }
/*   76:     */   
/*   77:     */   public static enum OrderDirection
/*   78:     */   {
/*   79: 145 */     INCREASING,  DECREASING;
/*   80:     */     
/*   81:     */     private OrderDirection() {}
/*   82:     */   }
/*   83:     */   
/*   84:     */   public static boolean isMonotonic(Comparable[] val, OrderDirection dir, boolean strict)
/*   85:     */   {
/*   86: 161 */     Comparable previous = val[0];
/*   87: 162 */     int max = val.length;
/*   88: 163 */     for (int i = 1; i < max; i++)
/*   89:     */     {
/*   90:     */       int comp;
/*   91: 165 */       switch (dir.ordinal())
/*   92:     */       {
/*   93:     */       case 1: 
/*   94: 167 */         comp = previous.compareTo(val[i]);
/*   95: 168 */         if (strict)
/*   96:     */         {
/*   97: 169 */           if (comp >= 0) {
/*   98: 170 */             return false;
/*   99:     */           }
/*  100:     */         }
/*  101: 173 */         else if (comp > 0) {
/*  102: 174 */           return false;
/*  103:     */         }
/*  104:     */         break;
/*  105:     */       case 2: 
/*  106: 179 */         comp = val[i].compareTo(previous);
/*  107: 180 */         if (strict)
/*  108:     */         {
/*  109: 181 */           if (comp >= 0) {
/*  110: 182 */             return false;
/*  111:     */           }
/*  112:     */         }
/*  113: 185 */         else if (comp > 0) {
/*  114: 186 */           return false;
/*  115:     */         }
/*  116:     */         break;
/*  117:     */       default: 
/*  118: 192 */         throw new MathInternalError();
/*  119:     */       }
/*  120: 195 */       previous = val[i];
/*  121:     */     }
/*  122: 197 */     return true;
/*  123:     */   }
/*  124:     */   
/*  125:     */   public static boolean isMonotonic(double[] val, OrderDirection dir, boolean strict)
/*  126:     */   {
/*  127: 211 */     return checkOrder(val, dir, strict, false);
/*  128:     */   }
/*  129:     */   
/*  130:     */   public static boolean checkOrder(double[] val, OrderDirection dir, boolean strict, boolean abort)
/*  131:     */   {
/*  132: 227 */     double previous = val[0];
/*  133: 228 */     int max = val.length;
/*  134: 232 */     for (int index = 1; index < max; index++)
/*  135:     */     {
/*  136: 233 */       switch (dir.ordinal())
/*  137:     */       {
/*  138:     */       case 1: 
/*  139: 235 */         if (strict)
/*  140:     */         {
/*  141: 236 */           if (val[index] <= previous) {
/*  142:     */             break;
/*  143:     */           }
/*  144:     */         }
/*  145:     */         else {
/*  146: 240 */           if (val[index] >= previous) {
/*  147:     */             break;
/*  148:     */           }
/*  149:     */         }
/*  150: 241 */         break;
/*  151:     */       case 2: 
/*  152: 246 */         if (strict)
/*  153:     */         {
/*  154: 247 */           if (val[index] >= previous) {
/*  155:     */             break;
/*  156:     */           }
/*  157:     */         }
/*  158:     */         else {
/*  159: 251 */           if (val[index] <= previous) {
/*  160:     */             break;
/*  161:     */           }
/*  162:     */         }
/*  163: 252 */         break;
/*  164:     */       default: 
/*  165: 258 */         throw new MathInternalError();
/*  166:     */       }
/*  167: 261 */       previous = val[index];
/*  168:     */     }
int index = 0;
/*  169:     */     label132:
/*  170: 264 */     if (index == max) {
/*  171: 266 */       return true;
/*  172:     */     }
/*  173: 270 */     if (abort) {
/*  174: 271 */       throw new NonMonotonicSequenceException(Double.valueOf(val[index]), Double.valueOf(previous), index, dir, strict);
/*  175:     */     }
/*  176: 273 */     return false;
/*  177:     */   }
/*  178:     */   
/*  179:     */   public static void checkOrder(double[] val, OrderDirection dir, boolean strict)
/*  180:     */   {
/*  181: 288 */     checkOrder(val, dir, strict, true);
/*  182:     */   }
/*  183:     */   
/*  184:     */   public static void checkOrder(double[] val)
/*  185:     */   {
/*  186: 299 */     checkOrder(val, OrderDirection.INCREASING, true);
/*  187:     */   }
/*  188:     */   
/*  189:     */   public static double safeNorm(double[] v)
/*  190:     */   {
/*  191: 363 */     double rdwarf = 3.834E-020D;
/*  192: 364 */     double rgiant = 1.304E+019D;
/*  193: 365 */     double s1 = 0.0D;
/*  194: 366 */     double s2 = 0.0D;
/*  195: 367 */     double s3 = 0.0D;
/*  196: 368 */     double x1max = 0.0D;
/*  197: 369 */     double x3max = 0.0D;
/*  198: 370 */     double floatn = v.length;
/*  199: 371 */     double agiant = rgiant / floatn;
/*  200: 372 */     for (int i = 0; i < v.length; i++)
/*  201:     */     {
/*  202: 373 */       double xabs = Math.abs(v[i]);
/*  203: 374 */       if ((xabs < rdwarf) || (xabs > agiant))
/*  204:     */       {
/*  205: 375 */         if (xabs > rdwarf)
/*  206:     */         {
/*  207: 376 */           if (xabs > x1max)
/*  208:     */           {
/*  209: 377 */             double r = x1max / xabs;
/*  210: 378 */             s1 = 1.0D + s1 * r * r;
/*  211: 379 */             x1max = xabs;
/*  212:     */           }
/*  213:     */           else
/*  214:     */           {
/*  215: 381 */             double r = xabs / x1max;
/*  216: 382 */             s1 += r * r;
/*  217:     */           }
/*  218:     */         }
/*  219: 385 */         else if (xabs > x3max)
/*  220:     */         {
/*  221: 386 */           double r = x3max / xabs;
/*  222: 387 */           s3 = 1.0D + s3 * r * r;
/*  223: 388 */           x3max = xabs;
/*  224:     */         }
/*  225: 390 */         else if (xabs != 0.0D)
/*  226:     */         {
/*  227: 391 */           double r = xabs / x3max;
/*  228: 392 */           s3 += r * r;
/*  229:     */         }
/*  230:     */       }
/*  231:     */       else {
/*  232: 397 */         s2 += xabs * xabs;
/*  233:     */       }
/*  234:     */     }
/*  235:     */     double norm;
/*  236:     */    
/*  237: 401 */     if (s1 != 0.0D)
/*  238:     */     {
/*  239: 402 */       norm = x1max * Math.sqrt(s1 + s2 / x1max / x1max);
/*  240:     */     }
/*  241:     */     else
/*  242:     */     {
/*  243:     */      
/*  244: 404 */       if (s2 == 0.0D)
/*  245:     */       {
/*  246: 405 */         norm = x3max * Math.sqrt(s3);
/*  247:     */       }
/*  248:     */       else
/*  249:     */       {
/*  250:     */        
/*  251: 407 */         if (s2 >= x3max) {
/*  252: 408 */           norm = Math.sqrt(s2 * (1.0D + x3max / s2 * (x3max * s3)));
/*  253:     */         } else {
/*  254: 410 */           norm = Math.sqrt(x3max * (s2 / x3max + x3max * s3));
/*  255:     */         }
/*  256:     */       }
/*  257:     */     }
/*  258: 414 */     return norm;
/*  259:     */   }
/*  260:     */   
/*  261:     */   public static void sortInPlace(double[] x, double[]... yList)
/*  262:     */   {
/*  263: 435 */     sortInPlace(x, OrderDirection.INCREASING, yList);
/*  264:     */   }
/*  265:     */   
/*  266:     */   public static void sortInPlace(double[] x, OrderDirection dir, double[]... yList)
/*  267:     */   {
/*  268: 458 */     if (x == null) {
/*  269: 459 */       throw new NullArgumentException();
/*  270:     */     }
/*  271: 462 */     int len = x.length;
/*  272: 463 */     List<Pair<Double, double[]>> list = new ArrayList(len);
/*  273:     */     
/*  274:     */ 
/*  275: 466 */     int yListLen = yList.length;
/*  276: 467 */     for (int i = 0; i < len; i++)
/*  277:     */     {
/*  278: 468 */       double[] yValues = new double[yListLen];
/*  279: 469 */       for (int j = 0; j < yListLen; j++)
/*  280:     */       {
/*  281: 470 */         double[] y = yList[j];
/*  282: 471 */         if (y == null) {
/*  283: 472 */           throw new NullArgumentException();
/*  284:     */         }
/*  285: 474 */         if (y.length != len) {
/*  286: 475 */           throw new DimensionMismatchException(y.length, len);
/*  287:     */         }
/*  288: 477 */         yValues[j] = y[i];
/*  289:     */       }
/*  290: 479 */       list.add(new Pair(Double.valueOf(x[i]), yValues));
/*  291:     */     }
/*  292: 482 */     Comparator<Pair<Double, double[]>> comp = new Comparator()
/*  293:     */     {
private Object val$dir;

/*  294:     */       public int compare(Pair<Double, double[]> o1, Pair<Double, double[]> o2)
/*  295:     */       {
/*  296:     */         int val;
/*  297: 487 */         switch (((Enum<LocalizedFormats>) this.val$dir).ordinal())
/*  298:     */         {
/*  299:     */         case 1: 
/*  300: 489 */           val = ((Double)o1.getKey()).compareTo((Double)o2.getKey());
/*  301: 490 */           break;
/*  302:     */         case 2: 
/*  303: 492 */           val = ((Double)o2.getKey()).compareTo((Double)o1.getKey());
/*  304: 493 */           break;
/*  305:     */         default: 
/*  306: 496 */           throw new MathInternalError();
/*  307:     */         }
/*  308: 498 */         return val;
/*  309:     */       }

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}
/*  310: 501 */     };
/*  311: 502 */     Collections.sort(list, comp);
/*  312: 504 */     for (int i = 0; i < len; i++)
/*  313:     */     {
/*  314: 505 */       Pair<Double, double[]> e = (Pair)list.get(i);
/*  315: 506 */       x[i] = ((Double)e.getKey()).doubleValue();
/*  316: 507 */       double[] yValues = (double[])e.getValue();
/*  317: 508 */       for (int j = 0; j < yListLen; j++) {
/*  318: 509 */         yList[j][i] = yValues[j];
/*  319:     */       }
/*  320:     */     }
/*  321:     */   }
/*  322:     */   
/*  323:     */   public static int[] copyOf(int[] source)
/*  324:     */   {
/*  325: 521 */     return copyOf(source, source.length);
/*  326:     */   }
/*  327:     */   
/*  328:     */   public static double[] copyOf(double[] source)
/*  329:     */   {
/*  330: 531 */     return copyOf(source, source.length);
/*  331:     */   }
/*  332:     */   
/*  333:     */   public static int[] copyOf(int[] source, int len)
/*  334:     */   {
/*  335: 544 */     int[] output = new int[len];
/*  336: 545 */     System.arraycopy(source, 0, output, 0, FastMath.min(len, source.length));
/*  337: 546 */     return output;
/*  338:     */   }
/*  339:     */   
/*  340:     */   public static double[] copyOf(double[] source, int len)
/*  341:     */   {
/*  342: 559 */     double[] output = new double[len];
/*  343: 560 */     System.arraycopy(source, 0, output, 0, FastMath.min(len, source.length));
/*  344: 561 */     return output;
/*  345:     */   }
/*  346:     */   
/*  347:     */   public static double linearCombination(double[] a, double[] b)
/*  348:     */   {
/*  349: 581 */     int len = a.length;
/*  350: 582 */     if (len != b.length) {
/*  351: 583 */       throw new DimensionMismatchException(len, b.length);
/*  352:     */     }
/*  353: 586 */     double[] prodHigh = new double[len];
/*  354: 587 */     double prodLowSum = 0.0D;
/*  355: 589 */     for (int i = 0; i < len; i++)
/*  356:     */     {
/*  357: 590 */       double ai = a[i];
/*  358: 591 */       double ca = 134217729.0D * ai;
/*  359: 592 */       double aHigh = ca - (ca - ai);
/*  360: 593 */       double aLow = ai - aHigh;
/*  361:     */       
/*  362: 595 */       double bi = b[i];
/*  363: 596 */       double cb = 134217729.0D * bi;
/*  364: 597 */       double bHigh = cb - (cb - bi);
/*  365: 598 */       double bLow = bi - bHigh;
/*  366: 599 */       prodHigh[i] = (ai * bi);
/*  367: 600 */       double prodLow = aLow * bLow - (prodHigh[i] - aHigh * bHigh - aLow * bHigh - aHigh * bLow);
/*  368:     */       
/*  369:     */ 
/*  370:     */ 
/*  371: 604 */       prodLowSum += prodLow;
/*  372:     */     }
/*  373: 608 */     double prodHighCur = prodHigh[0];
/*  374: 609 */     double prodHighNext = prodHigh[1];
/*  375: 610 */     double sHighPrev = prodHighCur + prodHighNext;
/*  376: 611 */     double sPrime = sHighPrev - prodHighNext;
/*  377: 612 */     double sLowSum = prodHighNext - (sHighPrev - sPrime) + (prodHighCur - sPrime);
/*  378:     */     
/*  379: 614 */     int lenMinusOne = len - 1;
/*  380: 615 */     for (int i = 1; i < lenMinusOne; i++)
/*  381:     */     {
/*  382: 616 */       prodHighNext = prodHigh[(i + 1)];
/*  383: 617 */       double sHighCur = sHighPrev + prodHighNext;
/*  384: 618 */       sPrime = sHighCur - prodHighNext;
/*  385: 619 */       sLowSum += prodHighNext - (sHighCur - sPrime) + (sHighPrev - sPrime);
/*  386: 620 */       sHighPrev = sHighCur;
/*  387:     */     }
/*  388: 623 */     double result = sHighPrev + (prodLowSum + sLowSum);
/*  389: 625 */     if (Double.isNaN(result))
/*  390:     */     {
/*  391: 628 */       result = 0.0D;
/*  392: 629 */       for (int i = 0; i < len; i++) {
/*  393: 630 */         result += a[i] * b[i];
/*  394:     */       }
/*  395:     */     }
/*  396: 634 */     return result;
/*  397:     */   }
/*  398:     */   
/*  399:     */   public static double linearCombination(double a1, double b1, double a2, double b2)
/*  400:     */   {
/*  401: 673 */     double ca1 = 134217729.0D * a1;
/*  402: 674 */     double a1High = ca1 - (ca1 - a1);
/*  403: 675 */     double a1Low = a1 - a1High;
/*  404: 676 */     double cb1 = 134217729.0D * b1;
/*  405: 677 */     double b1High = cb1 - (cb1 - b1);
/*  406: 678 */     double b1Low = b1 - b1High;
/*  407:     */     
/*  408:     */ 
/*  409: 681 */     double prod1High = a1 * b1;
/*  410: 682 */     double prod1Low = a1Low * b1Low - (prod1High - a1High * b1High - a1Low * b1High - a1High * b1Low);
/*  411:     */     
/*  412:     */ 
/*  413: 685 */     double ca2 = 134217729.0D * a2;
/*  414: 686 */     double a2High = ca2 - (ca2 - a2);
/*  415: 687 */     double a2Low = a2 - a2High;
/*  416: 688 */     double cb2 = 134217729.0D * b2;
/*  417: 689 */     double b2High = cb2 - (cb2 - b2);
/*  418: 690 */     double b2Low = b2 - b2High;
/*  419:     */     
/*  420:     */ 
/*  421: 693 */     double prod2High = a2 * b2;
/*  422: 694 */     double prod2Low = a2Low * b2Low - (prod2High - a2High * b2High - a2Low * b2High - a2High * b2Low);
/*  423:     */     
/*  424:     */ 
/*  425: 697 */     double s12High = prod1High + prod2High;
/*  426: 698 */     double s12Prime = s12High - prod2High;
/*  427: 699 */     double s12Low = prod2High - (s12High - s12Prime) + (prod1High - s12Prime);
/*  428:     */     
/*  429:     */ 
/*  430:     */ 
/*  431: 703 */     double result = s12High + (prod1Low + prod2Low + s12Low);
/*  432: 705 */     if (Double.isNaN(result)) {
/*  433: 708 */       result = a1 * b1 + a2 * b2;
/*  434:     */     }
/*  435: 711 */     return result;
/*  436:     */   }
/*  437:     */   
/*  438:     */   public static double linearCombination(double a1, double b1, double a2, double b2, double a3, double b3)
/*  439:     */   {
/*  440: 753 */     double ca1 = 134217729.0D * a1;
/*  441: 754 */     double a1High = ca1 - (ca1 - a1);
/*  442: 755 */     double a1Low = a1 - a1High;
/*  443: 756 */     double cb1 = 134217729.0D * b1;
/*  444: 757 */     double b1High = cb1 - (cb1 - b1);
/*  445: 758 */     double b1Low = b1 - b1High;
/*  446:     */     
/*  447:     */ 
/*  448: 761 */     double prod1High = a1 * b1;
/*  449: 762 */     double prod1Low = a1Low * b1Low - (prod1High - a1High * b1High - a1Low * b1High - a1High * b1Low);
/*  450:     */     
/*  451:     */ 
/*  452: 765 */     double ca2 = 134217729.0D * a2;
/*  453: 766 */     double a2High = ca2 - (ca2 - a2);
/*  454: 767 */     double a2Low = a2 - a2High;
/*  455: 768 */     double cb2 = 134217729.0D * b2;
/*  456: 769 */     double b2High = cb2 - (cb2 - b2);
/*  457: 770 */     double b2Low = b2 - b2High;
/*  458:     */     
/*  459:     */ 
/*  460: 773 */     double prod2High = a2 * b2;
/*  461: 774 */     double prod2Low = a2Low * b2Low - (prod2High - a2High * b2High - a2Low * b2High - a2High * b2Low);
/*  462:     */     
/*  463:     */ 
/*  464: 777 */     double ca3 = 134217729.0D * a3;
/*  465: 778 */     double a3High = ca3 - (ca3 - a3);
/*  466: 779 */     double a3Low = a3 - a3High;
/*  467: 780 */     double cb3 = 134217729.0D * b3;
/*  468: 781 */     double b3High = cb3 - (cb3 - b3);
/*  469: 782 */     double b3Low = b3 - b3High;
/*  470:     */     
/*  471:     */ 
/*  472: 785 */     double prod3High = a3 * b3;
/*  473: 786 */     double prod3Low = a3Low * b3Low - (prod3High - a3High * b3High - a3Low * b3High - a3High * b3Low);
/*  474:     */     
/*  475:     */ 
/*  476: 789 */     double s12High = prod1High + prod2High;
/*  477: 790 */     double s12Prime = s12High - prod2High;
/*  478: 791 */     double s12Low = prod2High - (s12High - s12Prime) + (prod1High - s12Prime);
/*  479:     */     
/*  480:     */ 
/*  481: 794 */     double s123High = s12High + prod3High;
/*  482: 795 */     double s123Prime = s123High - prod3High;
/*  483: 796 */     double s123Low = prod3High - (s123High - s123Prime) + (s12High - s123Prime);
/*  484:     */     
/*  485:     */ 
/*  486:     */ 
/*  487: 800 */     double result = s123High + (prod1Low + prod2Low + prod3Low + s12Low + s123Low);
/*  488: 802 */     if (Double.isNaN(result)) {
/*  489: 805 */       result = a1 * b1 + a2 * b2 + a3 * b3;
/*  490:     */     }
/*  491: 808 */     return result;
/*  492:     */   }
/*  493:     */   
/*  494:     */   public static double linearCombination(double a1, double b1, double a2, double b2, double a3, double b3, double a4, double b4)
/*  495:     */   {
/*  496: 855 */     double ca1 = 134217729.0D * a1;
/*  497: 856 */     double a1High = ca1 - (ca1 - a1);
/*  498: 857 */     double a1Low = a1 - a1High;
/*  499: 858 */     double cb1 = 134217729.0D * b1;
/*  500: 859 */     double b1High = cb1 - (cb1 - b1);
/*  501: 860 */     double b1Low = b1 - b1High;
/*  502:     */     
/*  503:     */ 
/*  504: 863 */     double prod1High = a1 * b1;
/*  505: 864 */     double prod1Low = a1Low * b1Low - (prod1High - a1High * b1High - a1Low * b1High - a1High * b1Low);
/*  506:     */     
/*  507:     */ 
/*  508: 867 */     double ca2 = 134217729.0D * a2;
/*  509: 868 */     double a2High = ca2 - (ca2 - a2);
/*  510: 869 */     double a2Low = a2 - a2High;
/*  511: 870 */     double cb2 = 134217729.0D * b2;
/*  512: 871 */     double b2High = cb2 - (cb2 - b2);
/*  513: 872 */     double b2Low = b2 - b2High;
/*  514:     */     
/*  515:     */ 
/*  516: 875 */     double prod2High = a2 * b2;
/*  517: 876 */     double prod2Low = a2Low * b2Low - (prod2High - a2High * b2High - a2Low * b2High - a2High * b2Low);
/*  518:     */     
/*  519:     */ 
/*  520: 879 */     double ca3 = 134217729.0D * a3;
/*  521: 880 */     double a3High = ca3 - (ca3 - a3);
/*  522: 881 */     double a3Low = a3 - a3High;
/*  523: 882 */     double cb3 = 134217729.0D * b3;
/*  524: 883 */     double b3High = cb3 - (cb3 - b3);
/*  525: 884 */     double b3Low = b3 - b3High;
/*  526:     */     
/*  527:     */ 
/*  528: 887 */     double prod3High = a3 * b3;
/*  529: 888 */     double prod3Low = a3Low * b3Low - (prod3High - a3High * b3High - a3Low * b3High - a3High * b3Low);
/*  530:     */     
/*  531:     */ 
/*  532: 891 */     double ca4 = 134217729.0D * a4;
/*  533: 892 */     double a4High = ca4 - (ca4 - a4);
/*  534: 893 */     double a4Low = a4 - a4High;
/*  535: 894 */     double cb4 = 134217729.0D * b4;
/*  536: 895 */     double b4High = cb4 - (cb4 - b4);
/*  537: 896 */     double b4Low = b4 - b4High;
/*  538:     */     
/*  539:     */ 
/*  540: 899 */     double prod4High = a4 * b4;
/*  541: 900 */     double prod4Low = a4Low * b4Low - (prod4High - a4High * b4High - a4Low * b4High - a4High * b4Low);
/*  542:     */     
/*  543:     */ 
/*  544: 903 */     double s12High = prod1High + prod2High;
/*  545: 904 */     double s12Prime = s12High - prod2High;
/*  546: 905 */     double s12Low = prod2High - (s12High - s12Prime) + (prod1High - s12Prime);
/*  547:     */     
/*  548:     */ 
/*  549: 908 */     double s123High = s12High + prod3High;
/*  550: 909 */     double s123Prime = s123High - prod3High;
/*  551: 910 */     double s123Low = prod3High - (s123High - s123Prime) + (s12High - s123Prime);
/*  552:     */     
/*  553:     */ 
/*  554: 913 */     double s1234High = s123High + prod4High;
/*  555: 914 */     double s1234Prime = s1234High - prod4High;
/*  556: 915 */     double s1234Low = prod4High - (s1234High - s1234Prime) + (s123High - s1234Prime);
/*  557:     */     
/*  558:     */ 
/*  559:     */ 
/*  560: 919 */     double result = s1234High + (prod1Low + prod2Low + prod3Low + prod4Low + s12Low + s123Low + s1234Low);
/*  561: 921 */     if (Double.isNaN(result)) {
/*  562: 924 */       result = a1 * b1 + a2 * b2 + a3 * b3 + a4 * b4;
/*  563:     */     }
/*  564: 927 */     return result;
/*  565:     */   }
/*  566:     */   
/*  567:     */   public static boolean equals(float[] x, float[] y)
/*  568:     */   {
/*  569: 941 */     if ((x == null) || (y == null)) {
/*  570: 942 */       return ((x == null ? 1 : 0) ^ (y == null ? 1 : 0)) == 0;
/*  571:     */     }
/*  572: 944 */     if (x.length != y.length) {
/*  573: 945 */       return false;
/*  574:     */     }
/*  575: 947 */     for (int i = 0; i < x.length; i++) {
/*  576: 948 */       if (!Precision.equals(x[i], y[i])) {
/*  577: 949 */         return false;
/*  578:     */       }
/*  579:     */     }
/*  580: 952 */     return true;
/*  581:     */   }
/*  582:     */   
/*  583:     */   public static boolean equalsIncludingNaN(float[] x, float[] y)
/*  584:     */   {
/*  585: 967 */     if ((x == null) || (y == null)) {
/*  586: 968 */       return ((x == null ? 1 : 0) ^ (y == null ? 1 : 0)) == 0;
/*  587:     */     }
/*  588: 970 */     if (x.length != y.length) {
/*  589: 971 */       return false;
/*  590:     */     }
/*  591: 973 */     for (int i = 0; i < x.length; i++) {
/*  592: 974 */       if (!Precision.equalsIncludingNaN(x[i], y[i])) {
/*  593: 975 */         return false;
/*  594:     */       }
/*  595:     */     }
/*  596: 978 */     return true;
/*  597:     */   }
/*  598:     */   
/*  599:     */   public static boolean equals(double[] x, double[] y)
/*  600:     */   {
/*  601: 992 */     if ((x == null) || (y == null)) {
/*  602: 993 */       return ((x == null ? 1 : 0) ^ (y == null ? 1 : 0)) == 0;
/*  603:     */     }
/*  604: 995 */     if (x.length != y.length) {
/*  605: 996 */       return false;
/*  606:     */     }
/*  607: 998 */     for (int i = 0; i < x.length; i++) {
/*  608: 999 */       if (!Precision.equals(x[i], y[i])) {
/*  609:1000 */         return false;
/*  610:     */       }
/*  611:     */     }
/*  612:1003 */     return true;
/*  613:     */   }
/*  614:     */   
/*  615:     */   public static boolean equalsIncludingNaN(double[] x, double[] y)
/*  616:     */   {
/*  617:1018 */     if ((x == null) || (y == null)) {
/*  618:1019 */       return ((x == null ? 1 : 0) ^ (y == null ? 1 : 0)) == 0;
/*  619:     */     }
/*  620:1021 */     if (x.length != y.length) {
/*  621:1022 */       return false;
/*  622:     */     }
/*  623:1024 */     for (int i = 0; i < x.length; i++) {
/*  624:1025 */       if (!Precision.equalsIncludingNaN(x[i], y[i])) {
/*  625:1026 */         return false;
/*  626:     */       }
/*  627:     */     }
/*  628:1029 */     return true;
/*  629:     */   }
/*  630:     */   
/*  631:     */   public static double[] normalizeArray(double[] values, double normalizedSum)
/*  632:     */   {
/*  633:1055 */     if (Double.isInfinite(normalizedSum)) {
/*  634:1056 */       throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_INFINITE, new Object[0]);
/*  635:     */     }
/*  636:1058 */     if (Double.isNaN(normalizedSum)) {
/*  637:1059 */       throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_NAN, new Object[0]);
/*  638:     */     }
/*  639:1061 */     double sum = 0.0D;
/*  640:1062 */     int len = values.length;
/*  641:1063 */     double[] out = new double[len];
/*  642:1064 */     for (int i = 0; i < len; i++)
/*  643:     */     {
/*  644:1065 */       if (Double.isInfinite(values[i])) {
/*  645:1066 */         throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, new Object[] { Double.valueOf(values[i]), Integer.valueOf(i) });
/*  646:     */       }
/*  647:1068 */       if (!Double.isNaN(values[i])) {
/*  648:1069 */         sum += values[i];
/*  649:     */       }
/*  650:     */     }
/*  651:1072 */     if (sum == 0.0D) {
/*  652:1073 */       throw new MathArithmeticException(LocalizedFormats.ARRAY_SUMS_TO_ZERO, new Object[0]);
/*  653:     */     }
/*  654:1075 */     for (int i = 0; i < len; i++) {
/*  655:1076 */       if (Double.isNaN(values[i])) {
/*  656:1077 */         out[i] = (0.0D / 0.0D);
/*  657:     */       } else {
/*  658:1079 */         out[i] = (values[i] * normalizedSum / sum);
/*  659:     */       }
/*  660:     */     }
/*  661:1082 */     return out;
/*  662:     */   }
/*  663:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.MathArrays
 * JD-Core Version:    0.7.0.1
 */
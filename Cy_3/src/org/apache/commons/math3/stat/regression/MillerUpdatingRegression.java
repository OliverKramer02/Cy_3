/*    1:     */ package org.apache.commons.math3.stat.regression;
/*    2:     */ 
/*    3:     */ import java.util.Arrays;
/*    4:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*    5:     */ import org.apache.commons.math3.util.FastMath;
/*    6:     */ import org.apache.commons.math3.util.MathArrays;
/*    7:     */ 
/*    8:     */ public class MillerUpdatingRegression
/*    9:     */   implements UpdatingMultipleLinearRegression
/*   10:     */ {
/*   11:     */   private final int nvars;
/*   12:     */   private final double[] d;
/*   13:     */   private final double[] rhs;
/*   14:     */   private final double[] r;
/*   15:     */   private final double[] tol;
/*   16:     */   private final double[] rss;
/*   17:     */   private final int[] vorder;
/*   18:     */   private final double[] work_tolset;
/*   19:  62 */   private long nobs = 0L;
/*   20:  64 */   private double sserr = 0.0D;
/*   21:  66 */   private boolean rss_set = false;
/*   22:  68 */   private boolean tol_set = false;
/*   23:     */   private final boolean[] lindep;
/*   24:     */   private final double[] x_sing;
/*   25:     */   private final double[] work_sing;
/*   26:  76 */   private double sumy = 0.0D;
/*   27:  78 */   private double sumsqy = 0.0D;
/*   28:     */   private boolean hasIntercept;
/*   29:     */   private final double epsilon;
private int[] lindep2;
/*   30:     */   
/*   31:     */   private MillerUpdatingRegression()
/*   32:     */   {
/*   33:  89 */     this.d = null;
/*   34:  90 */     this.hasIntercept = false;
/*   35:  91 */     this.lindep = null;
/*   36:  92 */     this.nobs = -1L;
/*   37:  93 */     this.nvars = -1;
/*   38:  94 */     this.r = null;
/*   39:  95 */     this.rhs = null;
/*   40:  96 */     this.rss = null;
/*   41:  97 */     this.rss_set = false;
/*   42:  98 */     this.sserr = (0.0D / 0.0D);
/*   43:  99 */     this.sumsqy = (0.0D / 0.0D);
/*   44: 100 */     this.sumy = (0.0D / 0.0D);
/*   45: 101 */     this.tol = null;
/*   46: 102 */     this.tol_set = false;
/*   47: 103 */     this.vorder = null;
/*   48: 104 */     this.work_sing = null;
/*   49: 105 */     this.work_tolset = null;
/*   50: 106 */     this.x_sing = null;
/*   51: 107 */     this.epsilon = (0.0D / 0.0D);
/*   52:     */   }
/*   53:     */   
/*   54:     */   public MillerUpdatingRegression(int numberOfVariables, boolean includeConstant, double errorTolerance)
/*   55:     */   {
/*   56: 118 */     if (numberOfVariables < 1) {
/*   57: 119 */       throw new ModelSpecificationException(LocalizedFormats.NO_REGRESSORS, new Object[0]);
/*   58:     */     }
/*   59: 121 */     if (includeConstant) {
/*   60: 122 */       this.nvars = (numberOfVariables + 1);
/*   61:     */     } else {
/*   62: 124 */       this.nvars = numberOfVariables;
/*   63:     */     }
/*   64: 126 */     this.hasIntercept = includeConstant;
/*   65: 127 */     this.nobs = 0L;
/*   66: 128 */     this.d = new double[this.nvars];
/*   67: 129 */     this.rhs = new double[this.nvars];
/*   68: 130 */     this.r = new double[this.nvars * (this.nvars - 1) / 2];
/*   69: 131 */     this.tol = new double[this.nvars];
/*   70: 132 */     this.rss = new double[this.nvars];
/*   71: 133 */     this.vorder = new int[this.nvars];
/*   72: 134 */     this.x_sing = new double[this.nvars];
/*   73: 135 */     this.work_sing = new double[this.nvars];
/*   74: 136 */     this.work_tolset = new double[this.nvars];
/*   75: 137 */     this.lindep = new boolean[this.nvars];
/*   76: 138 */     for (int i = 0; i < this.nvars; i++) {
/*   77: 139 */       this.vorder[i] = i;
/*   78:     */     }
/*   79: 141 */     if (errorTolerance > 0.0D) {
/*   80: 142 */       this.epsilon = errorTolerance;
/*   81:     */     } else {
/*   82: 144 */       this.epsilon = (-errorTolerance);
/*   83:     */     }
/*   84:     */   }
/*   85:     */   
/*   86:     */   public MillerUpdatingRegression(int numberOfVariables, boolean includeConstant)
/*   87:     */   {
/*   88: 156 */     this(numberOfVariables, includeConstant, 1.110223024625157E-016D);
/*   89:     */   }
/*   90:     */   
/*   91:     */   public boolean hasIntercept()
/*   92:     */   {
/*   93: 164 */     return this.hasIntercept;
/*   94:     */   }
/*   95:     */   
/*   96:     */   public long getN()
/*   97:     */   {
/*   98: 172 */     return this.nobs;
/*   99:     */   }
/*  100:     */   
/*  101:     */   public void addObservation(double[] x, double y)
/*  102:     */   {
/*  103: 184 */     if (((!this.hasIntercept) && (x.length != this.nvars)) || ((this.hasIntercept) && (x.length + 1 != this.nvars))) {
/*  104: 186 */       throw new ModelSpecificationException(LocalizedFormats.INVALID_REGRESSION_OBSERVATION, new Object[] { Integer.valueOf(x.length), Integer.valueOf(this.nvars) });
/*  105:     */     }
/*  106: 189 */     if (!this.hasIntercept)
/*  107:     */     {
/*  108: 190 */       include(MathArrays.copyOf(x, x.length), 1.0D, y);
/*  109:     */     }
/*  110:     */     else
/*  111:     */     {
/*  112: 192 */       double[] tmp = new double[x.length + 1];
/*  113: 193 */       System.arraycopy(x, 0, tmp, 1, x.length);
/*  114: 194 */       tmp[0] = 1.0D;
/*  115: 195 */       include(tmp, 1.0D, y);
/*  116:     */     }
/*  117: 197 */     this.nobs += 1L;
/*  118:     */   }
/*  119:     */   
/*  120:     */   public void addObservations(double[][] x, double[] y)
/*  121:     */   {
/*  122: 210 */     if ((x == null) || (y == null) || (x.length != y.length)) {
/*  123: 211 */       throw new ModelSpecificationException(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, new Object[] { Integer.valueOf(x == null ? 0 : x.length), Integer.valueOf(y == null ? 0 : y.length) });
/*  124:     */     }
/*  125: 216 */     if (x.length == 0) {
/*  126: 217 */       throw new ModelSpecificationException(LocalizedFormats.NO_DATA, new Object[0]);
/*  127:     */     }
/*  128: 220 */     if (x[0].length + 1 > x.length) {
/*  129: 221 */       throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, new Object[] { Integer.valueOf(x.length), Integer.valueOf(x[0].length) });
/*  130:     */     }
/*  131: 225 */     for (int i = 0; i < x.length; i++) {
/*  132: 226 */       addObservation(x[i], y[i]);
/*  133:     */     }
/*  134:     */   }
/*  135:     */   
/*  136:     */   private void include(double[] x, double wi, double yi)
/*  137:     */   {
/*  138: 246 */     int nextr = 0;
/*  139: 247 */     double w = wi;
/*  140: 248 */     double y = yi;
/*  141:     */     
/*  142:     */ 
/*  143:     */ 
/*  144:     */ 
/*  145:     */ 
/*  146:     */ 
/*  147: 255 */     this.rss_set = false;
/*  148: 256 */     this.sumy = smartAdd(yi, this.sumy);
/*  149: 257 */     this.sumsqy = smartAdd(this.sumsqy, yi * yi);
/*  150: 258 */     for (int i = 0; i < x.length; i++)
/*  151:     */     {
/*  152: 259 */       if (w == 0.0D) {
/*  153: 260 */         return;
/*  154:     */       }
/*  155: 262 */       double xi = x[i];
/*  156: 264 */       if (xi == 0.0D)
/*  157:     */       {
/*  158: 265 */         nextr += this.nvars - i - 1;
/*  159:     */       }
/*  160:     */       else
/*  161:     */       {
/*  162: 268 */         double di = this.d[i];
/*  163: 269 */         double wxi = w * xi;
/*  164: 270 */         double _w = w;
/*  165:     */         double dpi;
/*  166: 271 */         if (di != 0.0D)
/*  167:     */         {
/*  168: 272 */           dpi = smartAdd(di, wxi * xi);
/*  169: 273 */           double tmp = wxi * xi / di;
/*  170: 274 */           if (FastMath.abs(tmp) > 1.110223024625157E-016D) {
/*  171: 275 */             w = di * w / dpi;
/*  172:     */           }
/*  173:     */         }
/*  174:     */         else
/*  175:     */         {
/*  176: 278 */           dpi = wxi * xi;
/*  177: 279 */           w = 0.0D;
/*  178:     */         }
/*  179: 281 */         this.d[i] = dpi;
/*  180: 282 */         for (int k = i + 1; k < this.nvars; k++)
/*  181:     */         {
/*  182: 283 */           double xk = x[k];
/*  183: 284 */           x[k] = smartAdd(xk, -xi * this.r[nextr]);
/*  184: 285 */           if (di != 0.0D) {
/*  185: 286 */             this.r[nextr] = (smartAdd(di * this.r[nextr], _w * xi * xk) / dpi);
/*  186:     */           } else {
/*  187: 288 */             this.r[nextr] = (xk / xi);
/*  188:     */           }
/*  189: 290 */           nextr++;
/*  190:     */         }
/*  191: 292 */         double xk = y;
/*  192: 293 */         y = smartAdd(xk, -xi * this.rhs[i]);
/*  193: 294 */         if (di != 0.0D) {
/*  194: 295 */           this.rhs[i] = (smartAdd(di * this.rhs[i], wxi * xk) / dpi);
/*  195:     */         } else {
/*  196: 297 */           this.rhs[i] = (xk / xi);
/*  197:     */         }
/*  198:     */       }
/*  199:     */     }
/*  200: 300 */     this.sserr = smartAdd(this.sserr, w * y * y);
/*  201:     */   }
/*  202:     */   
/*  203:     */   private double smartAdd(double a, double b)
/*  204:     */   {
/*  205: 312 */     double _a = FastMath.abs(a);
/*  206: 313 */     double _b = FastMath.abs(b);
/*  207: 314 */     if (_a > _b)
/*  208:     */     {
/*  209: 315 */       double eps = _a * 1.110223024625157E-016D;
/*  210: 316 */       if (_b > eps) {
/*  211: 317 */         return a + b;
/*  212:     */       }
/*  213: 319 */       return a;
/*  214:     */     }
/*  215: 321 */     double eps = _b * 1.110223024625157E-016D;
/*  216: 322 */     if (_a > eps) {
/*  217: 323 */       return a + b;
/*  218:     */     }
/*  219: 325 */     return b;
/*  220:     */   }
/*  221:     */   
/*  222:     */   public void clear()
/*  223:     */   {
/*  224: 334 */     Arrays.fill(this.d, 0.0D);
/*  225: 335 */     Arrays.fill(this.rhs, 0.0D);
/*  226: 336 */     Arrays.fill(this.r, 0.0D);
/*  227: 337 */     Arrays.fill(this.tol, 0.0D);
/*  228: 338 */     Arrays.fill(this.rss, 0.0D);
/*  229: 339 */     Arrays.fill(this.work_tolset, 0.0D);
/*  230: 340 */     Arrays.fill(this.work_sing, 0.0D);
/*  231: 341 */     Arrays.fill(this.x_sing, 0.0D);
/*  232: 342 */     Arrays.fill(this.lindep, false);
/*  233: 343 */     for (int i = 0; i < this.nvars; i++) {
/*  234: 344 */       this.vorder[i] = i;
/*  235:     */     }
/*  236: 346 */     this.nobs = 0L;
/*  237: 347 */     this.sserr = 0.0D;
/*  238: 348 */     this.sumy = 0.0D;
/*  239: 349 */     this.sumsqy = 0.0D;
/*  240: 350 */     this.rss_set = false;
/*  241: 351 */     this.tol_set = false;
/*  242:     */   }
/*  243:     */   
/*  244:     */   private void tolset()
/*  245:     */   {
/*  246: 361 */     double eps = this.epsilon;
/*  247: 362 */     for (int i = 0; i < this.nvars; i++) {
/*  248: 363 */       this.work_tolset[i] = Math.sqrt(this.d[i]);
/*  249:     */     }
/*  250: 365 */     this.tol[0] = (eps * this.work_tolset[0]);
/*  251: 366 */     for (int col = 1; col < this.nvars; col++)
/*  252:     */     {
/*  253: 367 */       int pos = col - 1;
/*  254: 368 */       double total = this.work_tolset[col];
/*  255: 369 */       for (int row = 0; row < col; row++)
/*  256:     */       {
/*  257: 370 */         total += Math.abs(this.r[pos]) * this.work_tolset[row];
/*  258: 371 */         pos += this.nvars - row - 2;
/*  259:     */       }
/*  260: 373 */       this.tol[col] = (eps * total);
/*  261:     */     }
/*  262: 375 */     this.tol_set = true;
/*  263:     */   }
/*  264:     */   
/*  265:     */   private double[] regcf(int nreq)
/*  266:     */   {
/*  267: 390 */     if (nreq < 1) {
/*  268: 391 */       throw new ModelSpecificationException(LocalizedFormats.NO_REGRESSORS, new Object[0]);
/*  269:     */     }
/*  270: 393 */     if (nreq > this.nvars) {
/*  271: 394 */       throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, new Object[] { Integer.valueOf(nreq), Integer.valueOf(this.nvars) });
/*  272:     */     }
/*  273: 397 */     if (!this.tol_set) {
/*  274: 398 */       tolset();
/*  275:     */     }
/*  276: 400 */     double[] ret = new double[nreq];
/*  277: 401 */     boolean rankProblem = false;
/*  278: 402 */     for (int i = nreq - 1; i > -1; i--) {
/*  279: 403 */       if (Math.sqrt(this.d[i]) < this.tol[i])
/*  280:     */       {
/*  281: 404 */         ret[i] = 0.0D;
/*  282: 405 */         this.d[i] = 0.0D;
/*  283: 406 */         rankProblem = true;
/*  284:     */       }
/*  285:     */       else
/*  286:     */       {
/*  287: 408 */         ret[i] = this.rhs[i];
/*  288: 409 */         int nextr = i * (this.nvars + this.nvars - i - 1) / 2;
/*  289: 410 */         for (int j = i + 1; j < nreq; j++)
/*  290:     */         {
/*  291: 411 */           ret[i] = smartAdd(ret[i], -this.r[nextr] * ret[j]);
/*  292: 412 */           nextr++;
/*  293:     */         }
/*  294:     */       }
/*  295:     */     }
/*  296: 416 */     if (rankProblem) {
/*  297: 417 */       for (int i = 0; i < nreq; i++) {
/*  298: 418 */         if (this.lindep2[i] != 0) {
/*  299: 419 */           ret[i] = (0.0D / 0.0D);
/*  300:     */         }
/*  301:     */       }
/*  302:     */     }
/*  303: 423 */     return ret;
/*  304:     */   }
/*  305:     */   
/*  306:     */   private void singcheck()
/*  307:     */   {
/*  308: 435 */     for (int i = 0; i < this.nvars; i++) {
/*  309: 436 */       this.work_sing[i] = Math.sqrt(this.d[i]);
/*  310:     */     }
/*  311: 438 */     for (int col = 0; col < this.nvars; col++)
/*  312:     */     {
/*  313: 442 */       double temp = this.tol[col];
/*  314: 443 */       int pos = col - 1;
/*  315: 444 */       for (int row = 0; row < col - 1; row++)
/*  316:     */       {
/*  317: 445 */         if (Math.abs(this.r[pos]) * this.work_sing[row] < temp) {
/*  318: 446 */           this.r[pos] = 0.0D;
/*  319:     */         }
/*  320: 448 */         pos += this.nvars - row - 2;
/*  321:     */       }
/*  322: 453 */       this.lindep[col] = false;
/*  323: 454 */       if (this.work_sing[col] < temp)
/*  324:     */       {
/*  325: 455 */         this.lindep[col] = true;
/*  326: 456 */         if (col < this.nvars - 1)
/*  327:     */         {
/*  328: 457 */           Arrays.fill(this.x_sing, 0.0D);
/*  329: 458 */           int _pi = col * (this.nvars + this.nvars - col - 1) / 2;
/*  330: 459 */           for (int _xi = col + 1; _xi < this.nvars; _pi++)
/*  331:     */           {
/*  332: 460 */             this.x_sing[_xi] = this.r[_pi];
/*  333: 461 */             this.r[_pi] = 0.0D;_xi++;
/*  334:     */           }
/*  335: 463 */           double y = this.rhs[col];
/*  336: 464 */           double weight = this.d[col];
/*  337: 465 */           this.d[col] = 0.0D;
/*  338: 466 */           this.rhs[col] = 0.0D;
/*  339: 467 */           include(this.x_sing, weight, y);
/*  340:     */         }
/*  341:     */         else
/*  342:     */         {
/*  343: 469 */           this.sserr += this.d[col] * this.rhs[col] * this.rhs[col];
/*  344:     */         }
/*  345:     */       }
/*  346:     */     }
/*  347:     */   }
/*  348:     */   
/*  349:     */   private void ss()
/*  350:     */   {
/*  351: 486 */     double total = this.sserr;
/*  352: 487 */     this.rss[(this.nvars - 1)] = this.sserr;
/*  353: 488 */     for (int i = this.nvars - 1; i > 0; i--)
/*  354:     */     {
/*  355: 489 */       total += this.d[i] * this.rhs[i] * this.rhs[i];
/*  356: 490 */       this.rss[(i - 1)] = total;
/*  357:     */     }
/*  358: 492 */     this.rss_set = true;
/*  359:     */   }
/*  360:     */   
/*  361:     */   private double[] cov(int nreq)
/*  362:     */   {
/*  363: 515 */     if (this.nobs <= nreq) {
/*  364: 516 */       return null;
/*  365:     */     }
/*  366: 518 */     double rnk = 0.0D;
/*  367: 519 */     for (int i = 0; i < nreq; i++) {
/*  368: 520 */       if (this.lindep2[i] == 0) {
/*  369: 521 */         rnk += 1.0D;
/*  370:     */       }
/*  371:     */     }
/*  372: 524 */     double var = this.rss[(nreq - 1)] / (this.nobs - rnk);
/*  373: 525 */     double[] rinv = new double[nreq * (nreq - 1) / 2];
/*  374: 526 */     inverse(rinv, nreq);
/*  375: 527 */     double[] covmat = new double[nreq * (nreq + 1) / 2];
/*  376: 528 */     Arrays.fill(covmat, (0.0D / 0.0D));
/*  377:     */     
/*  378:     */ 
/*  379: 531 */     int start = 0;
/*  380: 532 */     double total = 0.0D;
/*  381: 533 */     for (int row = 0; row < nreq; row++)
/*  382:     */     {
/*  383: 534 */       int pos2 = start;
/*  384: 535 */       if (this.lindep2[row] == 0) {
/*  385: 536 */         for (int col = row; col < nreq; col++) {
/*  386: 537 */           if (this.lindep2[col] == 0)
/*  387:     */           {
/*  388: 538 */             int pos1 = start + col - row;
/*  389: 539 */             if (row == col) {
/*  390: 540 */               total = 1.0D / this.d[col];
/*  391:     */             } else {
/*  392: 542 */               total = rinv[(pos1 - 1)] / this.d[col];
/*  393:     */             }
/*  394: 544 */             for (int k = col + 1; k < nreq; k++)
/*  395:     */             {
/*  396: 545 */               if (this.lindep2[k] == 0) {
/*  397: 546 */                 total += rinv[pos1] * rinv[pos2] / this.d[k];
/*  398:     */               }
/*  399: 548 */               pos1++;
/*  400: 549 */               pos2++;
/*  401:     */             }
/*  402: 551 */             covmat[((col + 1) * col / 2 + row)] = (total * var);
/*  403:     */           }
/*  404:     */           else
/*  405:     */           {
/*  406: 553 */             pos2 += nreq - col - 1;
/*  407:     */           }
/*  408:     */         }
/*  409:     */       }
/*  410: 557 */       start += nreq - row - 1;
/*  411:     */     }
/*  412: 559 */     return covmat;
/*  413:     */   }
/*  414:     */   
/*  415:     */   private void inverse(double[] rinv, int nreq)
/*  416:     */   {
/*  417: 570 */     int pos = nreq * (nreq - 1) / 2 - 1;
/*  418: 571 */     int pos1 = -1;
/*  419: 572 */     int pos2 = -1;
/*  420: 573 */     double total = 0.0D;
/*  421:     */     
/*  422: 575 */     Arrays.fill(rinv, (0.0D / 0.0D));
/*  423: 576 */     for (int row = nreq - 1; row > 0; row--) {
/*  424: 577 */       if (this.lindep2[row] == 0)
/*  425:     */       {
/*  426: 578 */         int start = (row - 1) * (this.nvars + this.nvars - row) / 2;
/*  427: 579 */         for (int col = nreq; col > row; col--)
/*  428:     */         {
/*  429: 580 */           pos1 = start;
/*  430: 581 */           pos2 = pos;
/*  431: 582 */           total = 0.0D;
/*  432: 583 */           for (int k = row; k < col - 1; k++)
/*  433:     */           {
/*  434: 584 */             pos2 += nreq - k - 1;
/*  435: 585 */             if (this.lindep2[k] == 0) {
/*  436: 586 */               total += -this.r[pos1] * rinv[pos2];
/*  437:     */             }
/*  438: 588 */             pos1++;
/*  439:     */           }
/*  440: 590 */           rinv[pos] = (total - this.r[pos1]);
/*  441: 591 */           pos--;
/*  442:     */         }
/*  443:     */       }
/*  444:     */       else
/*  445:     */       {
/*  446: 594 */         pos -= nreq - row;
/*  447:     */       }
/*  448:     */     }
/*  449:     */   }
/*  450:     */   
/*  451:     */   public double[] getPartialCorrelations(int in)
/*  452:     */   {
/*  453: 634 */     double[] output = new double[(this.nvars - in + 1) * (this.nvars - in) / 2];
/*  454:     */     
/*  455:     */ 
/*  456:     */ 
/*  457:     */ 
/*  458: 639 */     int rms_off = -in;
/*  459: 640 */     int wrk_off = -(in + 1);
/*  460: 641 */     double[] rms = new double[this.nvars - in];
/*  461: 642 */     double[] work = new double[this.nvars - in - 1];
/*  462:     */     
/*  463:     */ 
/*  464:     */ 
/*  465: 646 */     int offXX = (this.nvars - in) * (this.nvars - in - 1) / 2;
/*  466: 647 */     if ((in < -1) || (in >= this.nvars)) {
/*  467: 648 */       return null;
/*  468:     */     }
/*  469: 650 */     int nvm = this.nvars - 1;
/*  470: 651 */     int base_pos = this.r.length - (nvm - in) * (nvm - in + 1) / 2;
/*  471: 652 */     if (this.d[in] > 0.0D) {
/*  472: 653 */       rms[(in + rms_off)] = (1.0D / Math.sqrt(this.d[in]));
/*  473:     */     }
/*  474: 655 */     for (int col = in + 1; col < this.nvars; col++)
/*  475:     */     {
/*  476: 656 */       int pos = base_pos + col - 1 - in;
/*  477: 657 */       double sumxx = this.d[col];
/*  478: 658 */       for (int row = in; row < col; row++)
/*  479:     */       {
/*  480: 659 */         sumxx += this.d[row] * this.r[pos] * this.r[pos];
/*  481: 660 */         pos += this.nvars - row - 2;
/*  482:     */       }
/*  483: 662 */       if (sumxx > 0.0D) {
/*  484: 663 */         rms[(col + rms_off)] = (1.0D / Math.sqrt(sumxx));
/*  485:     */       } else {
/*  486: 665 */         rms[(col + rms_off)] = 0.0D;
/*  487:     */       }
/*  488:     */     }
/*  489: 668 */     double sumyy = this.sserr;
/*  490: 669 */     for (int row = in; row < this.nvars; row++) {
/*  491: 670 */       sumyy += this.d[row] * this.rhs[row] * this.rhs[row];
/*  492:     */     }
/*  493: 672 */     if (sumyy > 0.0D) {
/*  494: 673 */       sumyy = 1.0D / Math.sqrt(sumyy);
/*  495:     */     }
/*  496: 675 */     int pos = 0;
/*  497: 676 */     for (int col1 = in; col1 < this.nvars; col1++)
/*  498:     */     {
/*  499: 677 */       double sumxy = 0.0D;
/*  500: 678 */       Arrays.fill(work, 0.0D);
/*  501: 679 */       int pos1 = base_pos + col1 - in - 1;
/*  502: 680 */       for (int row = in; row < col1; row++)
/*  503:     */       {
/*  504: 681 */         int pos2 = pos1 + 1;
/*  505: 682 */         for (int col2 = col1 + 1; col2 < this.nvars; col2++)
/*  506:     */         {
/*  507: 683 */           work[(col2 + wrk_off)] += this.d[row] * this.r[pos1] * this.r[pos2];
/*  508: 684 */           pos2++;
/*  509:     */         }
/*  510: 686 */         sumxy += this.d[row] * this.r[pos1] * this.rhs[row];
/*  511: 687 */         pos1 += this.nvars - row - 2;
/*  512:     */       }
/*  513: 689 */       int pos2 = pos1 + 1;
/*  514: 690 */       for (int col2 = col1 + 1; col2 < this.nvars; col2++)
/*  515:     */       {
/*  516: 691 */         work[(col2 + wrk_off)] += this.d[col1] * this.r[pos2];
/*  517: 692 */         pos2++;
/*  518: 693 */         output[((col2 - 1 - in) * (col2 - in) / 2 + col1 - in)] = (work[(col2 + wrk_off)] * rms[(col1 + rms_off)] * rms[(col2 + rms_off)]);
/*  519:     */         
/*  520: 695 */         pos++;
/*  521:     */       }
/*  522: 697 */       sumxy += this.d[col1] * this.rhs[col1];
/*  523: 698 */       output[(col1 + rms_off + offXX)] = (sumxy * rms[(col1 + rms_off)] * sumyy);
/*  524:     */     }
/*  525: 701 */     return output;
/*  526:     */   }
/*  527:     */   
/*  528:     */   private void vmove(int from, int to)
/*  529:     */   {
/*  530: 727 */     boolean bSkipTo40 = false;
/*  531: 728 */     if (from == to) {
/*  532: 729 */       return;
/*  533:     */     }
/*  534: 731 */     if (!this.rss_set) {
/*  535: 732 */       ss();
/*  536:     */     }
/*  537: 734 */     int count = 0;
/*  538:     */     int first;
/*  539:     */     int inc;
/*  540: 735 */     if (from < to)
/*  541:     */     {
/*  542: 736 */        first = from;
/*  543: 737 */        inc = 1;
/*  544: 738 */       count = to - from;
/*  545:     */     }
/*  546:     */     else
/*  547:     */     {
/*  548: 740 */       first = from - 1;
/*  549: 741 */       inc = -1;
/*  550: 742 */       count = from - to;
/*  551:     */     }
/*  552: 745 */     int m = first;
/*  553: 746 */     int idx = 0;
/*  554: 747 */     while (idx < count)
/*  555:     */     {
/*  556: 748 */       int m1 = m * (this.nvars + this.nvars - m - 1) / 2;
/*  557: 749 */       int m2 = m1 + this.nvars - m - 1;
/*  558: 750 */       int mp1 = m + 1;
/*  559:     */       
/*  560: 752 */       double d1 = this.d[m];
/*  561: 753 */       double d2 = this.d[mp1];
/*  562: 755 */       if ((d1 > this.epsilon) || (d2 > this.epsilon))
/*  563:     */       {
/*  564: 756 */         double X = this.r[m1];
/*  565: 757 */         if (Math.abs(X) * Math.sqrt(d1) < this.tol[mp1]) {
/*  566: 758 */           X = 0.0D;
/*  567:     */         }
/*  568: 760 */         if ((d1 < this.epsilon) || (Math.abs(X) < this.epsilon))
/*  569:     */         {
/*  570: 761 */           this.d[m] = d2;
/*  571: 762 */           this.d[mp1] = d1;
/*  572: 763 */           this.r[m1] = 0.0D;
/*  573: 764 */           for (int col = m + 2; col < this.nvars; col++)
/*  574:     */           {
/*  575: 765 */             m1++;
/*  576: 766 */             X = this.r[m1];
/*  577: 767 */             this.r[m1] = this.r[m2];
/*  578: 768 */             this.r[m2] = X;
/*  579: 769 */             m2++;
/*  580:     */           }
/*  581: 771 */           X = this.rhs[m];
/*  582: 772 */           this.rhs[m] = this.rhs[mp1];
/*  583: 773 */           this.rhs[mp1] = X;
/*  584: 774 */           bSkipTo40 = true;
/*  585:     */         }
/*  586: 776 */         else if (d2 < this.epsilon)
/*  587:     */         {
/*  588: 777 */           this.d[m] = (d1 * X * X);
/*  589: 778 */           this.r[m1] = (1.0D / X);
/*  590: 779 */           for (int _i = m1 + 1; _i < m1 + this.nvars - m - 1; _i++) {
/*  591: 780 */             this.r[_i] /= X;
/*  592:     */           }
/*  593: 782 */           this.rhs[m] /= X;
/*  594: 783 */           bSkipTo40 = true;
/*  595:     */         }
/*  596: 786 */         if (!bSkipTo40)
/*  597:     */         {
/*  598: 787 */           double d1new = d2 + d1 * X * X;
/*  599: 788 */           double cbar = d2 / d1new;
/*  600: 789 */           double sbar = X * d1 / d1new;
/*  601: 790 */           double d2new = d1 * cbar;
/*  602: 791 */           this.d[m] = d1new;
/*  603: 792 */           this.d[mp1] = d2new;
/*  604: 793 */           this.r[m1] = sbar;
/*  605: 794 */           for (int col = m + 2; col < this.nvars; col++)
/*  606:     */           {
/*  607: 795 */             m1++;
/*  608: 796 */             double Y = this.r[m1];
/*  609: 797 */             this.r[m1] = (cbar * this.r[m2] + sbar * Y);
/*  610: 798 */             this.r[m2] = (Y - X * this.r[m2]);
/*  611: 799 */             m2++;
/*  612:     */           }
/*  613: 801 */           double Y = this.rhs[m];
/*  614: 802 */           this.rhs[m] = (cbar * this.rhs[mp1] + sbar * Y);
/*  615: 803 */           this.rhs[mp1] = (Y - X * this.rhs[mp1]);
/*  616:     */         }
/*  617:     */       }
/*  618: 806 */       if (m > 0)
/*  619:     */       {
/*  620: 807 */         int pos = m;
/*  621: 808 */         for (int row = 0; row < m; row++)
/*  622:     */         {
/*  623: 809 */           double X = this.r[pos];
/*  624: 810 */           this.r[pos] = this.r[(pos - 1)];
/*  625: 811 */           this.r[(pos - 1)] = X;
/*  626: 812 */           pos += this.nvars - row - 2;
/*  627:     */         }
/*  628:     */       }
/*  629: 817 */       m1 = this.vorder[m];
/*  630: 818 */       this.vorder[m] = this.vorder[mp1];
/*  631: 819 */       this.vorder[mp1] = m1;
/*  632: 820 */       double X = this.tol[m];
/*  633: 821 */       this.tol[m] = this.tol[mp1];
/*  634: 822 */       this.tol[mp1] = X;
/*  635: 823 */       this.rss[mp1] += this.d[mp1] * this.rhs[mp1] * this.rhs[mp1];
/*  636:     */       
/*  637: 825 */       m += inc;
/*  638: 826 */       idx++;
/*  639:     */     }
/*  640:     */   }
/*  641:     */   
/*  642:     */   private int reorderRegressors(int[] list, int pos1)
/*  643:     */   {
/*  644: 849 */     if ((list.length < 1) || (list.length > this.nvars + 1 - pos1)) {
/*  645: 850 */       return -1;
/*  646:     */     }
/*  647: 852 */     int next = pos1;
/*  648: 853 */     int i = pos1;
/*  649: 854 */     while (i < this.nvars)
/*  650:     */     {
/*  651: 855 */       int l = this.vorder[i];
/*  652: 856 */       for (int j = 0; j < list.length; j++) {
/*  653: 857 */         if ((l == list[j]) && 
/*  654: 858 */           (i > next))
/*  655:     */         {
/*  656: 859 */           vmove(i, next);
/*  657: 860 */           next++;
/*  658: 861 */           if (next < list.length + pos1) {
/*  659:     */             break;
/*  660:     */           }
/*  661: 862 */           return 0;
/*  662:     */         }
/*  663:     */       }
/*  664: 869 */       i++;
/*  665:     */     }
/*  666: 871 */     return 0;
/*  667:     */   }
/*  668:     */   
/*  669:     */   public double getDiagonalOfHatMatrix(double[] row_data)
/*  670:     */   {
/*  671: 881 */     double[] wk = new double[this.nvars];
/*  672: 885 */     if (row_data.length > this.nvars) {
/*  673: 886 */       return (0.0D / 0.0D);
/*  674:     */     }
/*  675:     */     double[] xrow;
/*  676: 889 */     if (this.hasIntercept)
/*  677:     */     {
/*  678: 890 */        xrow = new double[row_data.length + 1];
/*  679: 891 */       xrow[0] = 1.0D;
/*  680: 892 */       System.arraycopy(row_data, 0, xrow, 1, row_data.length);
/*  681:     */     }
/*  682:     */     else
/*  683:     */     {
/*  684: 894 */       xrow = row_data;
/*  685:     */     }
/*  686: 896 */     double hii = 0.0D;
/*  687: 897 */     for (int col = 0; col < xrow.length; col++) {
/*  688: 898 */       if (Math.sqrt(this.d[col]) < this.tol[col])
/*  689:     */       {
/*  690: 899 */         wk[col] = 0.0D;
/*  691:     */       }
/*  692:     */       else
/*  693:     */       {
/*  694: 901 */         int pos = col - 1;
/*  695: 902 */         double total = xrow[col];
/*  696: 903 */         for (int row = 0; row < col; row++)
/*  697:     */         {
/*  698: 904 */           total = smartAdd(total, -wk[row] * this.r[pos]);
/*  699: 905 */           pos += this.nvars - row - 2;
/*  700:     */         }
/*  701: 907 */         wk[col] = total;
/*  702: 908 */         hii = smartAdd(hii, total * total / this.d[col]);
/*  703:     */       }
/*  704:     */     }
/*  705: 911 */     return hii;
/*  706:     */   }
/*  707:     */   
/*  708:     */   public int[] getOrderOfRegressors()
/*  709:     */   {
/*  710: 922 */     return MathArrays.copyOf(this.vorder);
/*  711:     */   }
/*  712:     */   
/*  713:     */   public RegressionResults regress()
/*  714:     */     throws ModelSpecificationException
/*  715:     */   {
/*  716: 933 */     return regress(this.nvars);
/*  717:     */   }
/*  718:     */   
/*  719:     */   public RegressionResults regress(int numberOfRegressors)
/*  720:     */     throws ModelSpecificationException
/*  721:     */   {
/*  722: 947 */     if (this.nobs <= numberOfRegressors) {
/*  723: 948 */       throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, new Object[] { Long.valueOf(this.nobs), Integer.valueOf(numberOfRegressors) });
/*  724:     */     }
/*  725: 952 */     if (numberOfRegressors > this.nvars) {
/*  726: 953 */       throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, new Object[] { Integer.valueOf(numberOfRegressors), Integer.valueOf(this.nvars) });
/*  727:     */     }
/*  728: 956 */     tolset();
/*  729:     */     
/*  730: 958 */     singcheck();
/*  731:     */     
/*  732: 960 */     double[] beta = regcf(numberOfRegressors);
/*  733:     */     
/*  734: 962 */     ss();
/*  735:     */     
/*  736: 964 */     double[] cov = cov(numberOfRegressors);
/*  737:     */     
/*  738: 966 */     int rnk = 0;
/*  739: 967 */     for (int i = 0; i < this.lindep.length; i++) {
/*  740: 968 */       if (this.lindep2[i] == 0) {
/*  741: 969 */         rnk++;
/*  742:     */       }
/*  743:     */     }
/*  744: 973 */     boolean needsReorder = false;
/*  745: 974 */     for (int i = 0; i < numberOfRegressors; i++) {
/*  746: 975 */       if (this.vorder[i] != i)
/*  747:     */       {
/*  748: 976 */         needsReorder = true;
/*  749: 977 */         break;
/*  750:     */       }
/*  751:     */     }
/*  752: 980 */     if (!needsReorder) {
/*  753: 981 */       return new RegressionResults(beta, new double[][] { cov }, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
/*  754:     */     }
/*  755: 985 */     double[] betaNew = new double[beta.length];
/*  756: 986 */     double[] covNew = new double[cov.length];
/*  757:     */     
/*  758: 988 */     int[] newIndices = new int[beta.length];
/*  759: 989 */     for (int i = 0; i < this.nvars; i++) {
/*  760: 990 */       for (int j = 0; j < numberOfRegressors; j++) {
/*  761: 991 */         if (this.vorder[j] == i)
/*  762:     */         {
/*  763: 992 */           betaNew[i] = beta[j];
/*  764: 993 */           newIndices[i] = j;
/*  765:     */         }
/*  766:     */       }
/*  767:     */     }
/*  768: 998 */     int idx1 = 0;
/*  769:1002 */     for (int i = 0; i < beta.length; i++)
/*  770:     */     {
/*  771:1003 */       int _i = newIndices[i];
/*  772:1004 */       for (int j = 0; j <= i; idx1++)
/*  773:     */       {
/*  774:1005 */         int _j = newIndices[j];
/*  775:     */         int idx2;
/*  776:     */         
/*  777:1006 */         if (_i > _j) {
/*  778:1007 */           idx2 = _i * (_i + 1) / 2 + _j;
/*  779:     */         } else {
/*  780:1009 */           idx2 = _j * (_j + 1) / 2 + _i;
/*  781:     */         }
/*  782:1011 */         covNew[idx1] = cov[idx2];j++;
/*  783:     */       }
/*  784:     */     }
/*  785:1014 */     return new RegressionResults(betaNew, new double[][] { covNew }, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
/*  786:     */   }
/*  787:     */   
/*  788:     */   public RegressionResults regress(int[] variablesToInclude)
/*  789:     */     throws ModelSpecificationException
/*  790:     */   {
/*  791:1033 */     if (variablesToInclude.length > this.nvars) {
/*  792:1034 */       throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, new Object[] { Integer.valueOf(variablesToInclude.length), Integer.valueOf(this.nvars) });
/*  793:     */     }
/*  794:1037 */     if (this.nobs <= this.nvars) {
/*  795:1038 */       throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, new Object[] { Long.valueOf(this.nobs), Integer.valueOf(this.nvars) });
/*  796:     */     }
/*  797:1042 */     Arrays.sort(variablesToInclude);
/*  798:1043 */     int iExclude = 0;
/*  799:1044 */     for (int i = 0; i < variablesToInclude.length; i++)
/*  800:     */     {
/*  801:1045 */       if (i >= this.nvars) {
/*  802:1046 */         throw new ModelSpecificationException(LocalizedFormats.INDEX_LARGER_THAN_MAX, new Object[] { Integer.valueOf(i), Integer.valueOf(this.nvars) });
/*  803:     */       }
/*  804:1049 */       if ((i > 0) && (variablesToInclude[i] == variablesToInclude[(i - 1)]))
/*  805:     */       {
/*  806:1050 */         variablesToInclude[i] = -1;
/*  807:1051 */         iExclude++;
/*  808:     */       }
/*  809:     */     }
/*  810:     */     int[] series;
/*  811:1055 */     if (iExclude > 0)
/*  812:     */     {
/*  813:1056 */       int j = 0;
/*  814:1057 */       series = new int[variablesToInclude.length - iExclude];
/*  815:1058 */       for (int i = 0; i < variablesToInclude.length; i++) {
/*  816:1059 */         if (variablesToInclude[i] > -1)
/*  817:     */         {
/*  818:1060 */           series[j] = variablesToInclude[i];
/*  819:1061 */           j++;
/*  820:     */         }
/*  821:     */       }
/*  822:     */     }
/*  823:     */     else
/*  824:     */     {
/*  825:1065 */       series = variablesToInclude;
/*  826:     */     }
/*  827:1068 */     reorderRegressors(series, 0);
/*  828:     */     
/*  829:1070 */     tolset();
/*  830:     */     
/*  831:1072 */     singcheck();
/*  832:     */     
/*  833:1074 */     double[] beta = regcf(series.length);
/*  834:     */     
/*  835:1076 */     ss();
/*  836:     */     
/*  837:1078 */     double[] cov = cov(series.length);
/*  838:     */     
/*  839:1080 */     int rnk = 0;
/*  840:1081 */     for (int i = 0; i < this.lindep.length; i++) {
/*  841:1082 */       if (this.lindep2[i] == 0) {
/*  842:1083 */         rnk++;
/*  843:     */       }
/*  844:     */     }
/*  845:1087 */     boolean needsReorder = false;
/*  846:1088 */     for (int i = 0; i < this.nvars; i++) {
/*  847:1089 */       if (this.vorder[i] != series[i])
/*  848:     */       {
/*  849:1090 */         needsReorder = true;
/*  850:1091 */         break;
/*  851:     */       }
/*  852:     */     }
/*  853:1094 */     if (!needsReorder) {
/*  854:1095 */       return new RegressionResults(beta, new double[][] { cov }, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
/*  855:     */     }
/*  856:1099 */     double[] betaNew = new double[beta.length];
/*  857:1100 */     int[] newIndices = new int[beta.length];
/*  858:1101 */     for (int i = 0; i < series.length; i++) {
/*  859:1102 */       for (int j = 0; j < this.vorder.length; j++) {
/*  860:1103 */         if (this.vorder[j] == series[i])
/*  861:     */         {
/*  862:1104 */           betaNew[i] = beta[j];
/*  863:1105 */           newIndices[i] = j;
/*  864:     */         }
/*  865:     */       }
/*  866:     */     }
/*  867:1109 */     double[] covNew = new double[cov.length];
/*  868:1110 */     int idx1 = 0;
/*  869:1114 */     for (int i = 0; i < beta.length; i++)
/*  870:     */     {
/*  871:1115 */       int _i = newIndices[i];
/*  872:1116 */       for (int j = 0; j <= i; idx1++)
/*  873:     */       {
/*  874:1117 */         int _j = newIndices[j];
/*  875:     */         int idx2;
/*  876:     */        
/*  877:1118 */         if (_i > _j) {
/*  878:1119 */           idx2 = _i * (_i + 1) / 2 + _j;
/*  879:     */         } else {
/*  880:1121 */           idx2 = _j * (_j + 1) / 2 + _i;
/*  881:     */         }
/*  882:1123 */         covNew[idx1] = cov[idx2];j++;
/*  883:     */       }
/*  884:     */     }
/*  885:1126 */     return new RegressionResults(betaNew, new double[][] { covNew }, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
/*  886:     */   }
/*  887:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.MillerUpdatingRegression
 * JD-Core Version:    0.7.0.1
 */
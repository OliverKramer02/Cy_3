/*    1:     */ package org.apache.commons.math3.linear;
/*    2:     */ 
/*    3:     */ import java.io.Serializable;
/*    4:     */ import java.util.Arrays;
/*    5:     */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*    6:     */ import org.apache.commons.math3.exception.NoDataException;
/*    7:     */ import org.apache.commons.math3.exception.NullArgumentException;
/*    8:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*    9:     */ import org.apache.commons.math3.util.FastMath;
/*   10:     */ import org.apache.commons.math3.util.MathUtils;
/*   11:     */ 
/*   12:     */ public class BlockRealMatrix
/*   13:     */   extends AbstractRealMatrix
/*   14:     */   implements Serializable
/*   15:     */ {
/*   16:     */   public static final int BLOCK_SIZE = 52;
/*   17:     */   private static final long serialVersionUID = 4991895511313664478L;
/*   18:     */   private final double[][] blocks;
/*   19:     */   private final int rows;
/*   20:     */   private final int columns;
/*   21:     */   private final int blockRows;
/*   22:     */   private final int blockColumns;
/*   23:     */   
/*   24:     */   public BlockRealMatrix(int rows, int columns)
/*   25:     */   {
/*   26:  94 */     super(rows, columns);
/*   27:  95 */     this.rows = rows;
/*   28:  96 */     this.columns = columns;
/*   29:     */     
/*   30:     */ 
/*   31:  99 */     this.blockRows = ((rows + 52 - 1) / 52);
/*   32: 100 */     this.blockColumns = ((columns + 52 - 1) / 52);
/*   33:     */     
/*   34:     */ 
/*   35: 103 */     this.blocks = createBlocksLayout(rows, columns);
/*   36:     */   }
/*   37:     */   
/*   38:     */   public BlockRealMatrix(double[][] rawData)
/*   39:     */   {
/*   40: 120 */     this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
/*   41:     */   }
/*   42:     */   
/*   43:     */   public BlockRealMatrix(int rows, int columns, double[][] blockData, boolean copyArray)
/*   44:     */   {
/*   45: 139 */     super(rows, columns);
/*   46: 140 */     this.rows = rows;
/*   47: 141 */     this.columns = columns;
/*   48:     */     
/*   49:     */ 
/*   50: 144 */     this.blockRows = ((rows + 52 - 1) / 52);
/*   51: 145 */     this.blockColumns = ((columns + 52 - 1) / 52);
/*   52: 147 */     if (copyArray) {
/*   53: 149 */       this.blocks = new double[this.blockRows * this.blockColumns][];
/*   54:     */     } else {
/*   55: 152 */       this.blocks = blockData;
/*   56:     */     }
/*   57: 155 */     int index = 0;
/*   58: 156 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*   59:     */     {
/*   60: 157 */       int iHeight = blockHeight(iBlock);
/*   61: 158 */       for (int jBlock = 0; jBlock < this.blockColumns; index++)
/*   62:     */       {
/*   63: 159 */         if (blockData[index].length != iHeight * blockWidth(jBlock)) {
/*   64: 160 */           throw new DimensionMismatchException(blockData[index].length, iHeight * blockWidth(jBlock));
/*   65:     */         }
/*   66: 163 */         if (copyArray) {
/*   67: 164 */           this.blocks[index] = ((double[])blockData[index].clone());
/*   68:     */         }
/*   69: 158 */         jBlock++;
/*   70:     */       }
/*   71:     */     }
/*   72:     */   }
/*   73:     */   
/*   74:     */   public static double[][] toBlocksLayout(double[][] rawData)
/*   75:     */   {
/*   76: 192 */     int rows = rawData.length;
/*   77: 193 */     int columns = rawData[0].length;
/*   78: 194 */     int blockRows = (rows + 52 - 1) / 52;
/*   79: 195 */     int blockColumns = (columns + 52 - 1) / 52;
/*   80: 198 */     for (int i = 0; i < rawData.length; i++)
/*   81:     */     {
/*   82: 199 */       int length = rawData[i].length;
/*   83: 200 */       if (length != columns) {
/*   84: 201 */         throw new DimensionMismatchException(columns, length);
/*   85:     */       }
/*   86:     */     }
/*   87: 206 */     double[][] blocks = new double[blockRows * blockColumns][];
/*   88: 207 */     int blockIndex = 0;
/*   89: 208 */     for (int iBlock = 0; iBlock < blockRows; iBlock++)
/*   90:     */     {
/*   91: 209 */       int pStart = iBlock * 52;
/*   92: 210 */       int pEnd = FastMath.min(pStart + 52, rows);
/*   93: 211 */       int iHeight = pEnd - pStart;
/*   94: 212 */       for (int jBlock = 0; jBlock < blockColumns; jBlock++)
/*   95:     */       {
/*   96: 213 */         int qStart = jBlock * 52;
/*   97: 214 */         int qEnd = FastMath.min(qStart + 52, columns);
/*   98: 215 */         int jWidth = qEnd - qStart;
/*   99:     */         
/*  100:     */ 
/*  101: 218 */         double[] block = new double[iHeight * jWidth];
/*  102: 219 */         blocks[blockIndex] = block;
/*  103:     */         
/*  104:     */ 
/*  105: 222 */         int index = 0;
/*  106: 223 */         for (int p = pStart; p < pEnd; p++)
/*  107:     */         {
/*  108: 224 */           System.arraycopy(rawData[p], qStart, block, index, jWidth);
/*  109: 225 */           index += jWidth;
/*  110:     */         }
/*  111: 227 */         blockIndex++;
/*  112:     */       }
/*  113:     */     }
/*  114: 231 */     return blocks;
/*  115:     */   }
/*  116:     */   
/*  117:     */   public static double[][] createBlocksLayout(int rows, int columns)
/*  118:     */   {
/*  119: 247 */     int blockRows = (rows + 52 - 1) / 52;
/*  120: 248 */     int blockColumns = (columns + 52 - 1) / 52;
/*  121:     */     
/*  122: 250 */     double[][] blocks = new double[blockRows * blockColumns][];
/*  123: 251 */     int blockIndex = 0;
/*  124: 252 */     for (int iBlock = 0; iBlock < blockRows; iBlock++)
/*  125:     */     {
/*  126: 253 */       int pStart = iBlock * 52;
/*  127: 254 */       int pEnd = FastMath.min(pStart + 52, rows);
/*  128: 255 */       int iHeight = pEnd - pStart;
/*  129: 256 */       for (int jBlock = 0; jBlock < blockColumns; jBlock++)
/*  130:     */       {
/*  131: 257 */         int qStart = jBlock * 52;
/*  132: 258 */         int qEnd = FastMath.min(qStart + 52, columns);
/*  133: 259 */         int jWidth = qEnd - qStart;
/*  134: 260 */         blocks[blockIndex] = new double[iHeight * jWidth];
/*  135: 261 */         blockIndex++;
/*  136:     */       }
/*  137:     */     }
/*  138: 265 */     return blocks;
/*  139:     */   }
/*  140:     */   
/*  141:     */   public BlockRealMatrix createMatrix(int rowDimension, int columnDimension)
/*  142:     */   {
/*  143: 271 */     return new BlockRealMatrix(rowDimension, columnDimension);
/*  144:     */   }
/*  145:     */   
/*  146:     */   public BlockRealMatrix copy()
/*  147:     */   {
/*  148: 278 */     BlockRealMatrix copied = new BlockRealMatrix(this.rows, this.columns);
/*  149: 281 */     for (int i = 0; i < this.blocks.length; i++) {
/*  150: 282 */       System.arraycopy(this.blocks[i], 0, copied.blocks[i], 0, this.blocks[i].length);
/*  151:     */     }
/*  152: 285 */     return copied;
/*  153:     */   }
/*  154:     */   
/*  155:     */   public BlockRealMatrix add(RealMatrix m)
/*  156:     */   {
/*  157:     */     try
/*  158:     */     {
/*  159: 292 */       return add((BlockRealMatrix)m);
/*  160:     */     }
/*  161:     */     catch (ClassCastException cce)
/*  162:     */     {
/*  163: 295 */       MatrixUtils.checkAdditionCompatible(this, m);
/*  164:     */       
/*  165: 297 */       BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
/*  166:     */       
/*  167:     */ 
/*  168: 300 */       int blockIndex = 0;
/*  169: 301 */       for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
/*  170: 302 */         for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  171:     */         {
/*  172: 305 */           double[] outBlock = out.blocks[blockIndex];
/*  173: 306 */           double[] tBlock = this.blocks[blockIndex];
/*  174: 307 */           int pStart = iBlock * 52;
/*  175: 308 */           int pEnd = FastMath.min(pStart + 52, this.rows);
/*  176: 309 */           int qStart = jBlock * 52;
/*  177: 310 */           int qEnd = FastMath.min(qStart + 52, this.columns);
/*  178: 311 */           int k = 0;
/*  179: 312 */           for (int p = pStart; p < pEnd; p++) {
/*  180: 313 */             for (int q = qStart; q < qEnd; q++)
/*  181:     */             {
/*  182: 314 */               tBlock[k] += m.getEntry(p, q);
/*  183: 315 */               k++;
/*  184:     */             }
/*  185:     */           }
/*  186: 319 */           blockIndex++;
/*  187:     */         }
/*  188:     */       }
/*  189: 323 */       return out;
/*  190:     */     }
/*  191:     */   }
/*  192:     */   
/*  193:     */   public BlockRealMatrix add(BlockRealMatrix m)
/*  194:     */   {
/*  195: 337 */     MatrixUtils.checkAdditionCompatible(this, m);
/*  196:     */     
/*  197: 339 */     BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
/*  198: 342 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  199:     */     {
/*  200: 343 */       double[] outBlock = out.blocks[blockIndex];
/*  201: 344 */       double[] tBlock = this.blocks[blockIndex];
/*  202: 345 */       double[] mBlock = m.blocks[blockIndex];
/*  203: 346 */       for (int k = 0; k < outBlock.length; k++) {
/*  204: 347 */         tBlock[k] += mBlock[k];
/*  205:     */       }
/*  206:     */     }
/*  207: 351 */     return out;
/*  208:     */   }
/*  209:     */   
/*  210:     */   public BlockRealMatrix subtract(RealMatrix m)
/*  211:     */   {
/*  212:     */     try
/*  213:     */     {
/*  214: 358 */       return subtract((BlockRealMatrix)m);
/*  215:     */     }
/*  216:     */     catch (ClassCastException cce)
/*  217:     */     {
/*  218: 361 */       MatrixUtils.checkSubtractionCompatible(this, m);
/*  219:     */       
/*  220: 363 */       BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
/*  221:     */       
/*  222:     */ 
/*  223: 366 */       int blockIndex = 0;
/*  224: 367 */       for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
/*  225: 368 */         for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  226:     */         {
/*  227: 371 */           double[] outBlock = out.blocks[blockIndex];
/*  228: 372 */           double[] tBlock = this.blocks[blockIndex];
/*  229: 373 */           int pStart = iBlock * 52;
/*  230: 374 */           int pEnd = FastMath.min(pStart + 52, this.rows);
/*  231: 375 */           int qStart = jBlock * 52;
/*  232: 376 */           int qEnd = FastMath.min(qStart + 52, this.columns);
/*  233: 377 */           int k = 0;
/*  234: 378 */           for (int p = pStart; p < pEnd; p++) {
/*  235: 379 */             for (int q = qStart; q < qEnd; q++)
/*  236:     */             {
/*  237: 380 */               tBlock[k] -= m.getEntry(p, q);
/*  238: 381 */               k++;
/*  239:     */             }
/*  240:     */           }
/*  241: 385 */           blockIndex++;
/*  242:     */         }
/*  243:     */       }
/*  244: 389 */       return out;
/*  245:     */     }
/*  246:     */   }
/*  247:     */   
/*  248:     */   public BlockRealMatrix subtract(BlockRealMatrix m)
/*  249:     */   {
/*  250: 403 */     MatrixUtils.checkSubtractionCompatible(this, m);
/*  251:     */     
/*  252: 405 */     BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
/*  253: 408 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  254:     */     {
/*  255: 409 */       double[] outBlock = out.blocks[blockIndex];
/*  256: 410 */       double[] tBlock = this.blocks[blockIndex];
/*  257: 411 */       double[] mBlock = m.blocks[blockIndex];
/*  258: 412 */       for (int k = 0; k < outBlock.length; k++) {
/*  259: 413 */         tBlock[k] -= mBlock[k];
/*  260:     */       }
/*  261:     */     }
/*  262: 417 */     return out;
/*  263:     */   }
/*  264:     */   
/*  265:     */   public BlockRealMatrix scalarAdd(double d)
/*  266:     */   {
/*  267: 424 */     BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
/*  268: 427 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  269:     */     {
/*  270: 428 */       double[] outBlock = out.blocks[blockIndex];
/*  271: 429 */       double[] tBlock = this.blocks[blockIndex];
/*  272: 430 */       for (int k = 0; k < outBlock.length; k++) {
/*  273: 431 */         tBlock[k] += d;
/*  274:     */       }
/*  275:     */     }
/*  276: 435 */     return out;
/*  277:     */   }
/*  278:     */   
/*  279:     */   public RealMatrix scalarMultiply(double d)
/*  280:     */   {
/*  281: 441 */     BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
/*  282: 444 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  283:     */     {
/*  284: 445 */       double[] outBlock = out.blocks[blockIndex];
/*  285: 446 */       double[] tBlock = this.blocks[blockIndex];
/*  286: 447 */       for (int k = 0; k < outBlock.length; k++) {
/*  287: 448 */         tBlock[k] *= d;
/*  288:     */       }
/*  289:     */     }
/*  290: 452 */     return out;
/*  291:     */   }
/*  292:     */   
/*  293:     */   public BlockRealMatrix multiply(RealMatrix m)
/*  294:     */   {
/*  295:     */     try
/*  296:     */     {
/*  297: 459 */       return multiply((BlockRealMatrix)m);
/*  298:     */     }
/*  299:     */     catch (ClassCastException cce)
/*  300:     */     {
/*  301: 462 */       MatrixUtils.checkMultiplicationCompatible(this, m);
/*  302:     */       
/*  303: 464 */       BlockRealMatrix out = new BlockRealMatrix(this.rows, m.getColumnDimension());
/*  304:     */       
/*  305:     */ 
/*  306: 467 */       int blockIndex = 0;
/*  307: 468 */       for (int iBlock = 0; iBlock < out.blockRows; iBlock++)
/*  308:     */       {
/*  309: 469 */         int pStart = iBlock * 52;
/*  310: 470 */         int pEnd = FastMath.min(pStart + 52, this.rows);
/*  311: 472 */         for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  312:     */         {
/*  313: 473 */           int qStart = jBlock * 52;
/*  314: 474 */           int qEnd = FastMath.min(qStart + 52, m.getColumnDimension());
/*  315:     */           
/*  316:     */ 
/*  317: 477 */           double[] outBlock = out.blocks[blockIndex];
/*  318: 480 */           for (int kBlock = 0; kBlock < this.blockColumns; kBlock++)
/*  319:     */           {
/*  320: 481 */             int kWidth = blockWidth(kBlock);
/*  321: 482 */             double[] tBlock = this.blocks[(iBlock * this.blockColumns + kBlock)];
/*  322: 483 */             int rStart = kBlock * 52;
/*  323: 484 */             int k = 0;
/*  324: 485 */             for (int p = pStart; p < pEnd; p++)
/*  325:     */             {
/*  326: 486 */               int lStart = (p - pStart) * kWidth;
/*  327: 487 */               int lEnd = lStart + kWidth;
/*  328: 488 */               for (int q = qStart; q < qEnd; q++)
/*  329:     */               {
/*  330: 489 */                 double sum = 0.0D;
/*  331: 490 */                 int r = rStart;
/*  332: 491 */                 for (int l = lStart; l < lEnd; l++)
/*  333:     */                 {
/*  334: 492 */                   sum += tBlock[l] * m.getEntry(r, q);
/*  335: 493 */                   r++;
/*  336:     */                 }
/*  337: 495 */                 outBlock[k] += sum;
/*  338: 496 */                 k++;
/*  339:     */               }
/*  340:     */             }
/*  341:     */           }
/*  342: 501 */           blockIndex++;
/*  343:     */         }
/*  344:     */       }
/*  345: 505 */       return out;
/*  346:     */     }
/*  347:     */   }
/*  348:     */   
/*  349:     */   public BlockRealMatrix multiply(BlockRealMatrix m)
/*  350:     */   {
/*  351: 519 */     MatrixUtils.checkMultiplicationCompatible(this, m);
/*  352:     */     
/*  353: 521 */     BlockRealMatrix out = new BlockRealMatrix(this.rows, m.columns);
/*  354:     */     
/*  355:     */ 
/*  356: 524 */     int blockIndex = 0;
/*  357: 525 */     for (int iBlock = 0; iBlock < out.blockRows; iBlock++)
/*  358:     */     {
/*  359: 527 */       int pStart = iBlock * 52;
/*  360: 528 */       int pEnd = FastMath.min(pStart + 52, this.rows);
/*  361: 530 */       for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  362:     */       {
/*  363: 531 */         int jWidth = out.blockWidth(jBlock);
/*  364: 532 */         int jWidth2 = jWidth + jWidth;
/*  365: 533 */         int jWidth3 = jWidth2 + jWidth;
/*  366: 534 */         int jWidth4 = jWidth3 + jWidth;
/*  367:     */         
/*  368:     */ 
/*  369: 537 */         double[] outBlock = out.blocks[blockIndex];
/*  370: 540 */         for (int kBlock = 0; kBlock < this.blockColumns; kBlock++)
/*  371:     */         {
/*  372: 541 */           int kWidth = blockWidth(kBlock);
/*  373: 542 */           double[] tBlock = this.blocks[(iBlock * this.blockColumns + kBlock)];
/*  374: 543 */           double[] mBlock = m.blocks[(kBlock * m.blockColumns + jBlock)];
/*  375: 544 */           int k = 0;
/*  376: 545 */           for (int p = pStart; p < pEnd; p++)
/*  377:     */           {
/*  378: 546 */             int lStart = (p - pStart) * kWidth;
/*  379: 547 */             int lEnd = lStart + kWidth;
/*  380: 548 */             for (int nStart = 0; nStart < jWidth; nStart++)
/*  381:     */             {
/*  382: 549 */               double sum = 0.0D;
/*  383: 550 */               int l = lStart;
/*  384: 551 */               int n = nStart;
/*  385: 552 */               while (l < lEnd - 3)
/*  386:     */               {
/*  387: 553 */                 sum += tBlock[l] * mBlock[n] + tBlock[(l + 1)] * mBlock[(n + jWidth)] + tBlock[(l + 2)] * mBlock[(n + jWidth2)] + tBlock[(l + 3)] * mBlock[(n + jWidth3)];
/*  388:     */                 
/*  389:     */ 
/*  390:     */ 
/*  391: 557 */                 l += 4;
/*  392: 558 */                 n += jWidth4;
/*  393:     */               }
/*  394: 560 */               while (l < lEnd)
/*  395:     */               {
/*  396: 561 */                 sum += tBlock[(l++)] * mBlock[n];
/*  397: 562 */                 n += jWidth;
/*  398:     */               }
/*  399: 564 */               outBlock[k] += sum;
/*  400: 565 */               k++;
/*  401:     */             }
/*  402:     */           }
/*  403:     */         }
/*  404: 570 */         blockIndex++;
/*  405:     */       }
/*  406:     */     }
/*  407: 574 */     return out;
/*  408:     */   }
/*  409:     */   
/*  410:     */   public double[][] getData()
/*  411:     */   {
/*  412: 580 */     double[][] data = new double[getRowDimension()][getColumnDimension()];
/*  413: 581 */     int lastColumns = this.columns - (this.blockColumns - 1) * 52;
/*  414: 583 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  415:     */     {
/*  416: 584 */       int pStart = iBlock * 52;
/*  417: 585 */       int pEnd = FastMath.min(pStart + 52, this.rows);
/*  418: 586 */       int regularPos = 0;
/*  419: 587 */       int lastPos = 0;
/*  420: 588 */       for (int p = pStart; p < pEnd; p++)
/*  421:     */       {
/*  422: 589 */         double[] dataP = data[p];
/*  423: 590 */         int blockIndex = iBlock * this.blockColumns;
/*  424: 591 */         int dataPos = 0;
/*  425: 592 */         for (int jBlock = 0; jBlock < this.blockColumns - 1; jBlock++)
/*  426:     */         {
/*  427: 593 */           System.arraycopy(this.blocks[(blockIndex++)], regularPos, dataP, dataPos, 52);
/*  428: 594 */           dataPos += 52;
/*  429:     */         }
/*  430: 596 */         System.arraycopy(this.blocks[blockIndex], lastPos, dataP, dataPos, lastColumns);
/*  431: 597 */         regularPos += 52;
/*  432: 598 */         lastPos += lastColumns;
/*  433:     */       }
/*  434:     */     }
/*  435: 602 */     return data;
/*  436:     */   }
/*  437:     */   
/*  438:     */   public double getNorm()
/*  439:     */   {
/*  440: 608 */     double[] colSums = new double[52];
/*  441: 609 */     double maxColSum = 0.0D;
/*  442: 610 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  443:     */     {
/*  444: 611 */       int jWidth = blockWidth(jBlock);
/*  445: 612 */       Arrays.fill(colSums, 0, jWidth, 0.0D);
/*  446: 613 */       for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  447:     */       {
/*  448: 614 */         int iHeight = blockHeight(iBlock);
/*  449: 615 */         double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  450: 616 */         for (int j = 0; j < jWidth; j++)
/*  451:     */         {
/*  452: 617 */           double sum = 0.0D;
/*  453: 618 */           for (int i = 0; i < iHeight; i++) {
/*  454: 619 */             sum += FastMath.abs(block[(i * jWidth + j)]);
/*  455:     */           }
/*  456: 621 */           colSums[j] += sum;
/*  457:     */         }
/*  458:     */       }
/*  459: 624 */       for (int j = 0; j < jWidth; j++) {
/*  460: 625 */         maxColSum = FastMath.max(maxColSum, colSums[j]);
/*  461:     */       }
/*  462:     */     }
/*  463: 628 */     return maxColSum;
/*  464:     */   }
/*  465:     */   
/*  466:     */   public double getFrobeniusNorm()
/*  467:     */   {
/*  468: 634 */     double sum2 = 0.0D;
/*  469: 635 */     for (int blockIndex = 0; blockIndex < this.blocks.length; blockIndex++) {
/*  470: 636 */       for (double entry : this.blocks[blockIndex]) {
/*  471: 637 */         sum2 += entry * entry;
/*  472:     */       }
/*  473:     */     }
/*  474: 640 */     return FastMath.sqrt(sum2);
/*  475:     */   }
/*  476:     */   
/*  477:     */   public BlockRealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn)
/*  478:     */   {
/*  479: 648 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/*  480:     */     
/*  481:     */ 
/*  482: 651 */     BlockRealMatrix out = new BlockRealMatrix(endRow - startRow + 1, endColumn - startColumn + 1);
/*  483:     */     
/*  484:     */ 
/*  485:     */ 
/*  486: 655 */     int blockStartRow = startRow / 52;
/*  487: 656 */     int rowsShift = startRow % 52;
/*  488: 657 */     int blockStartColumn = startColumn / 52;
/*  489: 658 */     int columnsShift = startColumn % 52;
/*  490:     */     
/*  491:     */ 
/*  492: 661 */     int pBlock = blockStartRow;
/*  493: 662 */     for (int iBlock = 0; iBlock < out.blockRows; iBlock++)
/*  494:     */     {
/*  495: 663 */       int iHeight = out.blockHeight(iBlock);
/*  496: 664 */       int qBlock = blockStartColumn;
/*  497: 665 */       for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  498:     */       {
/*  499: 666 */         int jWidth = out.blockWidth(jBlock);
/*  500:     */         
/*  501:     */ 
/*  502: 669 */         int outIndex = iBlock * out.blockColumns + jBlock;
/*  503: 670 */         double[] outBlock = out.blocks[outIndex];
/*  504: 671 */         int index = pBlock * this.blockColumns + qBlock;
/*  505: 672 */         int width = blockWidth(qBlock);
/*  506:     */         
/*  507: 674 */         int heightExcess = iHeight + rowsShift - 52;
/*  508: 675 */         int widthExcess = jWidth + columnsShift - 52;
/*  509: 676 */         if (heightExcess > 0)
/*  510:     */         {
/*  511: 678 */           if (widthExcess > 0)
/*  512:     */           {
/*  513: 680 */             int width2 = blockWidth(qBlock + 1);
/*  514: 681 */             copyBlockPart(this.blocks[index], width, rowsShift, 52, columnsShift, 52, outBlock, jWidth, 0, 0);
/*  515:     */             
/*  516:     */ 
/*  517:     */ 
/*  518: 685 */             copyBlockPart(this.blocks[(index + 1)], width2, rowsShift, 52, 0, widthExcess, outBlock, jWidth, 0, jWidth - widthExcess);
/*  519:     */             
/*  520:     */ 
/*  521:     */ 
/*  522: 689 */             copyBlockPart(this.blocks[(index + this.blockColumns)], width, 0, heightExcess, columnsShift, 52, outBlock, jWidth, iHeight - heightExcess, 0);
/*  523:     */             
/*  524:     */ 
/*  525:     */ 
/*  526: 693 */             copyBlockPart(this.blocks[(index + this.blockColumns + 1)], width2, 0, heightExcess, 0, widthExcess, outBlock, jWidth, iHeight - heightExcess, jWidth - widthExcess);
/*  527:     */           }
/*  528:     */           else
/*  529:     */           {
/*  530: 699 */             copyBlockPart(this.blocks[index], width, rowsShift, 52, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
/*  531:     */             
/*  532:     */ 
/*  533:     */ 
/*  534: 703 */             copyBlockPart(this.blocks[(index + this.blockColumns)], width, 0, heightExcess, columnsShift, jWidth + columnsShift, outBlock, jWidth, iHeight - heightExcess, 0);
/*  535:     */           }
/*  536:     */         }
/*  537: 710 */         else if (widthExcess > 0)
/*  538:     */         {
/*  539: 712 */           int width2 = blockWidth(qBlock + 1);
/*  540: 713 */           copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, 52, outBlock, jWidth, 0, 0);
/*  541:     */           
/*  542:     */ 
/*  543:     */ 
/*  544: 717 */           copyBlockPart(this.blocks[(index + 1)], width2, rowsShift, iHeight + rowsShift, 0, widthExcess, outBlock, jWidth, 0, jWidth - widthExcess);
/*  545:     */         }
/*  546:     */         else
/*  547:     */         {
/*  548: 723 */           copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
/*  549:     */         }
/*  550: 729 */         qBlock++;
/*  551:     */       }
/*  552: 731 */       pBlock++;
/*  553:     */     }
/*  554: 734 */     return out;
/*  555:     */   }
/*  556:     */   
/*  557:     */   private void copyBlockPart(double[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, double[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn)
/*  558:     */   {
/*  559: 757 */     int length = srcEndColumn - srcStartColumn;
/*  560: 758 */     int srcPos = srcStartRow * srcWidth + srcStartColumn;
/*  561: 759 */     int dstPos = dstStartRow * dstWidth + dstStartColumn;
/*  562: 760 */     for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++)
/*  563:     */     {
/*  564: 761 */       System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
/*  565: 762 */       srcPos += srcWidth;
/*  566: 763 */       dstPos += dstWidth;
/*  567:     */     }
/*  568:     */   }
/*  569:     */   
/*  570:     */   public void setSubMatrix(double[][] subMatrix, int row, int column)
/*  571:     */     throws NoDataException, NullArgumentException
/*  572:     */   {
/*  573: 772 */     MathUtils.checkNotNull(subMatrix);
/*  574: 773 */     int refLength = subMatrix[0].length;
/*  575: 774 */     if (refLength == 0) {
/*  576: 775 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/*  577:     */     }
/*  578: 777 */     int endRow = row + subMatrix.length - 1;
/*  579: 778 */     int endColumn = column + refLength - 1;
/*  580: 779 */     MatrixUtils.checkSubMatrixIndex(this, row, endRow, column, endColumn);
/*  581: 780 */     for (double[] subRow : subMatrix) {
/*  582: 781 */       if (subRow.length != refLength) {
/*  583: 782 */         throw new DimensionMismatchException(refLength, subRow.length);
/*  584:     */       }
/*  585:     */     }
/*  586: 787 */     int blockStartRow = row / 52;
/*  587: 788 */     int blockEndRow = (endRow + 52) / 52;
/*  588: 789 */     int blockStartColumn = column / 52;
/*  589: 790 */     int blockEndColumn = (endColumn + 52) / 52;
/*  590: 793 */     for (int iBlock = blockStartRow; iBlock < blockEndRow; iBlock++)
/*  591:     */     {
/*  592: 794 */       int iHeight = blockHeight(iBlock);
/*  593: 795 */       int firstRow = iBlock * 52;
/*  594: 796 */       int iStart = FastMath.max(row, firstRow);
/*  595: 797 */       int iEnd = FastMath.min(endRow + 1, firstRow + iHeight);
/*  596: 799 */       for (int jBlock = blockStartColumn; jBlock < blockEndColumn; jBlock++)
/*  597:     */       {
/*  598: 800 */         int jWidth = blockWidth(jBlock);
/*  599: 801 */         int firstColumn = jBlock * 52;
/*  600: 802 */         int jStart = FastMath.max(column, firstColumn);
/*  601: 803 */         int jEnd = FastMath.min(endColumn + 1, firstColumn + jWidth);
/*  602: 804 */         int jLength = jEnd - jStart;
/*  603:     */         
/*  604:     */ 
/*  605: 807 */         double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  606: 808 */         for (int i = iStart; i < iEnd; i++) {
/*  607: 809 */           System.arraycopy(subMatrix[(i - row)], jStart - column, block, (i - firstRow) * jWidth + (jStart - firstColumn), jLength);
/*  608:     */         }
/*  609:     */       }
/*  610:     */     }
/*  611:     */   }
/*  612:     */   
/*  613:     */   public BlockRealMatrix getRowMatrix(int row)
/*  614:     */   {
/*  615: 821 */     MatrixUtils.checkRowIndex(this, row);
/*  616: 822 */     BlockRealMatrix out = new BlockRealMatrix(1, this.columns);
/*  617:     */     
/*  618:     */ 
/*  619: 825 */     int iBlock = row / 52;
/*  620: 826 */     int iRow = row - iBlock * 52;
/*  621: 827 */     int outBlockIndex = 0;
/*  622: 828 */     int outIndex = 0;
/*  623: 829 */     double[] outBlock = out.blocks[outBlockIndex];
/*  624: 830 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  625:     */     {
/*  626: 831 */       int jWidth = blockWidth(jBlock);
/*  627: 832 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  628: 833 */       int available = outBlock.length - outIndex;
/*  629: 834 */       if (jWidth > available)
/*  630:     */       {
/*  631: 835 */         System.arraycopy(block, iRow * jWidth, outBlock, outIndex, available);
/*  632: 836 */         outBlock = out.blocks[(++outBlockIndex)];
/*  633: 837 */         System.arraycopy(block, iRow * jWidth, outBlock, 0, jWidth - available);
/*  634: 838 */         outIndex = jWidth - available;
/*  635:     */       }
/*  636:     */       else
/*  637:     */       {
/*  638: 840 */         System.arraycopy(block, iRow * jWidth, outBlock, outIndex, jWidth);
/*  639: 841 */         outIndex += jWidth;
/*  640:     */       }
/*  641:     */     }
/*  642: 845 */     return out;
/*  643:     */   }
/*  644:     */   
/*  645:     */   public void setRowMatrix(int row, RealMatrix matrix)
/*  646:     */   {
/*  647:     */     try
/*  648:     */     {
/*  649: 852 */       setRowMatrix(row, (BlockRealMatrix)matrix);
/*  650:     */     }
/*  651:     */     catch (ClassCastException cce)
/*  652:     */     {
/*  653: 854 */       super.setRowMatrix(row, matrix);
/*  654:     */     }
/*  655:     */   }
/*  656:     */   
/*  657:     */   public void setRowMatrix(int row, BlockRealMatrix matrix)
/*  658:     */   {
/*  659: 871 */     MatrixUtils.checkRowIndex(this, row);
/*  660: 872 */     int nCols = getColumnDimension();
/*  661: 873 */     if ((matrix.getRowDimension() != 1) || (matrix.getColumnDimension() != nCols)) {
/*  662: 875 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
/*  663:     */     }
/*  664: 881 */     int iBlock = row / 52;
/*  665: 882 */     int iRow = row - iBlock * 52;
/*  666: 883 */     int mBlockIndex = 0;
/*  667: 884 */     int mIndex = 0;
/*  668: 885 */     double[] mBlock = matrix.blocks[mBlockIndex];
/*  669: 886 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  670:     */     {
/*  671: 887 */       int jWidth = blockWidth(jBlock);
/*  672: 888 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  673: 889 */       int available = mBlock.length - mIndex;
/*  674: 890 */       if (jWidth > available)
/*  675:     */       {
/*  676: 891 */         System.arraycopy(mBlock, mIndex, block, iRow * jWidth, available);
/*  677: 892 */         mBlock = matrix.blocks[(++mBlockIndex)];
/*  678: 893 */         System.arraycopy(mBlock, 0, block, iRow * jWidth, jWidth - available);
/*  679: 894 */         mIndex = jWidth - available;
/*  680:     */       }
/*  681:     */       else
/*  682:     */       {
/*  683: 896 */         System.arraycopy(mBlock, mIndex, block, iRow * jWidth, jWidth);
/*  684: 897 */         mIndex += jWidth;
/*  685:     */       }
/*  686:     */     }
/*  687:     */   }
/*  688:     */   
/*  689:     */   public BlockRealMatrix getColumnMatrix(int column)
/*  690:     */   {
/*  691: 905 */     MatrixUtils.checkColumnIndex(this, column);
/*  692: 906 */     BlockRealMatrix out = new BlockRealMatrix(this.rows, 1);
/*  693:     */     
/*  694:     */ 
/*  695: 909 */     int jBlock = column / 52;
/*  696: 910 */     int jColumn = column - jBlock * 52;
/*  697: 911 */     int jWidth = blockWidth(jBlock);
/*  698: 912 */     int outBlockIndex = 0;
/*  699: 913 */     int outIndex = 0;
/*  700: 914 */     double[] outBlock = out.blocks[outBlockIndex];
/*  701: 915 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  702:     */     {
/*  703: 916 */       int iHeight = blockHeight(iBlock);
/*  704: 917 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  705: 918 */       for (int i = 0; i < iHeight; i++)
/*  706:     */       {
/*  707: 919 */         if (outIndex >= outBlock.length)
/*  708:     */         {
/*  709: 920 */           outBlock = out.blocks[(++outBlockIndex)];
/*  710: 921 */           outIndex = 0;
/*  711:     */         }
/*  712: 923 */         outBlock[(outIndex++)] = block[(i * jWidth + jColumn)];
/*  713:     */       }
/*  714:     */     }
/*  715: 927 */     return out;
/*  716:     */   }
/*  717:     */   
/*  718:     */   public void setColumnMatrix(int column, RealMatrix matrix)
/*  719:     */   {
/*  720:     */     try
/*  721:     */     {
/*  722: 934 */       setColumnMatrix(column, (BlockRealMatrix)matrix);
/*  723:     */     }
/*  724:     */     catch (ClassCastException cce)
/*  725:     */     {
/*  726: 936 */       super.setColumnMatrix(column, matrix);
/*  727:     */     }
/*  728:     */   }
/*  729:     */   
/*  730:     */   void setColumnMatrix(int column, BlockRealMatrix matrix)
/*  731:     */   {
/*  732: 953 */     MatrixUtils.checkColumnIndex(this, column);
/*  733: 954 */     int nRows = getRowDimension();
/*  734: 955 */     if ((matrix.getRowDimension() != nRows) || (matrix.getColumnDimension() != 1)) {
/*  735: 957 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
/*  736:     */     }
/*  737: 963 */     int jBlock = column / 52;
/*  738: 964 */     int jColumn = column - jBlock * 52;
/*  739: 965 */     int jWidth = blockWidth(jBlock);
/*  740: 966 */     int mBlockIndex = 0;
/*  741: 967 */     int mIndex = 0;
/*  742: 968 */     double[] mBlock = matrix.blocks[mBlockIndex];
/*  743: 969 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  744:     */     {
/*  745: 970 */       int iHeight = blockHeight(iBlock);
/*  746: 971 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  747: 972 */       for (int i = 0; i < iHeight; i++)
/*  748:     */       {
/*  749: 973 */         if (mIndex >= mBlock.length)
/*  750:     */         {
/*  751: 974 */           mBlock = matrix.blocks[(++mBlockIndex)];
/*  752: 975 */           mIndex = 0;
/*  753:     */         }
/*  754: 977 */         block[(i * jWidth + jColumn)] = mBlock[(mIndex++)];
/*  755:     */       }
/*  756:     */     }
/*  757:     */   }
/*  758:     */   
/*  759:     */   public RealVector getRowVector(int row)
/*  760:     */   {
/*  761: 985 */     MatrixUtils.checkRowIndex(this, row);
/*  762: 986 */     double[] outData = new double[this.columns];
/*  763:     */     
/*  764:     */ 
/*  765: 989 */     int iBlock = row / 52;
/*  766: 990 */     int iRow = row - iBlock * 52;
/*  767: 991 */     int outIndex = 0;
/*  768: 992 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  769:     */     {
/*  770: 993 */       int jWidth = blockWidth(jBlock);
/*  771: 994 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  772: 995 */       System.arraycopy(block, iRow * jWidth, outData, outIndex, jWidth);
/*  773: 996 */       outIndex += jWidth;
/*  774:     */     }
/*  775: 999 */     return new ArrayRealVector(outData, false);
/*  776:     */   }
/*  777:     */   
/*  778:     */   public void setRowVector(int row, RealVector vector)
/*  779:     */   {
/*  780:     */     try
/*  781:     */     {
/*  782:1006 */       setRow(row, ((ArrayRealVector)vector).getDataRef());
/*  783:     */     }
/*  784:     */     catch (ClassCastException cce)
/*  785:     */     {
/*  786:1008 */       super.setRowVector(row, vector);
/*  787:     */     }
/*  788:     */   }
/*  789:     */   
/*  790:     */   public RealVector getColumnVector(int column)
/*  791:     */   {
/*  792:1015 */     MatrixUtils.checkColumnIndex(this, column);
/*  793:1016 */     double[] outData = new double[this.rows];
/*  794:     */     
/*  795:     */ 
/*  796:1019 */     int jBlock = column / 52;
/*  797:1020 */     int jColumn = column - jBlock * 52;
/*  798:1021 */     int jWidth = blockWidth(jBlock);
/*  799:1022 */     int outIndex = 0;
/*  800:1023 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  801:     */     {
/*  802:1024 */       int iHeight = blockHeight(iBlock);
/*  803:1025 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  804:1026 */       for (int i = 0; i < iHeight; i++) {
/*  805:1027 */         outData[(outIndex++)] = block[(i * jWidth + jColumn)];
/*  806:     */       }
/*  807:     */     }
/*  808:1031 */     return new ArrayRealVector(outData, false);
/*  809:     */   }
/*  810:     */   
/*  811:     */   public void setColumnVector(int column, RealVector vector)
/*  812:     */   {
/*  813:     */     try
/*  814:     */     {
/*  815:1038 */       setColumn(column, ((ArrayRealVector)vector).getDataRef());
/*  816:     */     }
/*  817:     */     catch (ClassCastException cce)
/*  818:     */     {
/*  819:1040 */       super.setColumnVector(column, vector);
/*  820:     */     }
/*  821:     */   }
/*  822:     */   
/*  823:     */   public double[] getRow(int row)
/*  824:     */   {
/*  825:1047 */     MatrixUtils.checkRowIndex(this, row);
/*  826:1048 */     double[] out = new double[this.columns];
/*  827:     */     
/*  828:     */ 
/*  829:1051 */     int iBlock = row / 52;
/*  830:1052 */     int iRow = row - iBlock * 52;
/*  831:1053 */     int outIndex = 0;
/*  832:1054 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  833:     */     {
/*  834:1055 */       int jWidth = blockWidth(jBlock);
/*  835:1056 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  836:1057 */       System.arraycopy(block, iRow * jWidth, out, outIndex, jWidth);
/*  837:1058 */       outIndex += jWidth;
/*  838:     */     }
/*  839:1061 */     return out;
/*  840:     */   }
/*  841:     */   
/*  842:     */   public void setRow(int row, double[] array)
/*  843:     */   {
/*  844:1067 */     MatrixUtils.checkRowIndex(this, row);
/*  845:1068 */     int nCols = getColumnDimension();
/*  846:1069 */     if (array.length != nCols) {
/*  847:1070 */       throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
/*  848:     */     }
/*  849:1074 */     int iBlock = row / 52;
/*  850:1075 */     int iRow = row - iBlock * 52;
/*  851:1076 */     int outIndex = 0;
/*  852:1077 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  853:     */     {
/*  854:1078 */       int jWidth = blockWidth(jBlock);
/*  855:1079 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  856:1080 */       System.arraycopy(array, outIndex, block, iRow * jWidth, jWidth);
/*  857:1081 */       outIndex += jWidth;
/*  858:     */     }
/*  859:     */   }
/*  860:     */   
/*  861:     */   public double[] getColumn(int column)
/*  862:     */   {
/*  863:1088 */     MatrixUtils.checkColumnIndex(this, column);
/*  864:1089 */     double[] out = new double[this.rows];
/*  865:     */     
/*  866:     */ 
/*  867:1092 */     int jBlock = column / 52;
/*  868:1093 */     int jColumn = column - jBlock * 52;
/*  869:1094 */     int jWidth = blockWidth(jBlock);
/*  870:1095 */     int outIndex = 0;
/*  871:1096 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  872:     */     {
/*  873:1097 */       int iHeight = blockHeight(iBlock);
/*  874:1098 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  875:1099 */       for (int i = 0; i < iHeight; i++) {
/*  876:1100 */         out[(outIndex++)] = block[(i * jWidth + jColumn)];
/*  877:     */       }
/*  878:     */     }
/*  879:1104 */     return out;
/*  880:     */   }
/*  881:     */   
/*  882:     */   public void setColumn(int column, double[] array)
/*  883:     */   {
/*  884:1110 */     MatrixUtils.checkColumnIndex(this, column);
/*  885:1111 */     int nRows = getRowDimension();
/*  886:1112 */     if (array.length != nRows) {
/*  887:1113 */       throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
/*  888:     */     }
/*  889:1117 */     int jBlock = column / 52;
/*  890:1118 */     int jColumn = column - jBlock * 52;
/*  891:1119 */     int jWidth = blockWidth(jBlock);
/*  892:1120 */     int outIndex = 0;
/*  893:1121 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  894:     */     {
/*  895:1122 */       int iHeight = blockHeight(iBlock);
/*  896:1123 */       double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  897:1124 */       for (int i = 0; i < iHeight; i++) {
/*  898:1125 */         block[(i * jWidth + jColumn)] = array[(outIndex++)];
/*  899:     */       }
/*  900:     */     }
/*  901:     */   }
/*  902:     */   
/*  903:     */   public double getEntry(int row, int column)
/*  904:     */   {
/*  905:1133 */     MatrixUtils.checkMatrixIndex(this, row, column);
/*  906:1134 */     int iBlock = row / 52;
/*  907:1135 */     int jBlock = column / 52;
/*  908:1136 */     int k = (row - iBlock * 52) * blockWidth(jBlock) + (column - jBlock * 52);
/*  909:     */     
/*  910:1138 */     return this.blocks[(iBlock * this.blockColumns + jBlock)][k];
/*  911:     */   }
/*  912:     */   
/*  913:     */   public void setEntry(int row, int column, double value)
/*  914:     */   {
/*  915:1144 */     MatrixUtils.checkMatrixIndex(this, row, column);
/*  916:1145 */     int iBlock = row / 52;
/*  917:1146 */     int jBlock = column / 52;
/*  918:1147 */     int k = (row - iBlock * 52) * blockWidth(jBlock) + (column - jBlock * 52);
/*  919:     */     
/*  920:1149 */     this.blocks[(iBlock * this.blockColumns + jBlock)][k] = value;
/*  921:     */   }
/*  922:     */   
/*  923:     */   public void addToEntry(int row, int column, double increment)
/*  924:     */   {
/*  925:1155 */     MatrixUtils.checkMatrixIndex(this, row, column);
/*  926:1156 */     int iBlock = row / 52;
/*  927:1157 */     int jBlock = column / 52;
/*  928:1158 */     int k = (row - iBlock * 52) * blockWidth(jBlock) + (column - jBlock * 52);
/*  929:     */     
/*  930:1160 */     this.blocks[(iBlock * this.blockColumns + jBlock)][k] += increment;
/*  931:     */   }
/*  932:     */   
/*  933:     */   public void multiplyEntry(int row, int column, double factor)
/*  934:     */   {
/*  935:1166 */     MatrixUtils.checkMatrixIndex(this, row, column);
/*  936:1167 */     int iBlock = row / 52;
/*  937:1168 */     int jBlock = column / 52;
/*  938:1169 */     int k = (row - iBlock * 52) * blockWidth(jBlock) + (column - jBlock * 52);
/*  939:     */     
/*  940:1171 */     this.blocks[(iBlock * this.blockColumns + jBlock)][k] *= factor;
/*  941:     */   }
/*  942:     */   
/*  943:     */   public BlockRealMatrix transpose()
/*  944:     */   {
/*  945:1177 */     int nRows = getRowDimension();
/*  946:1178 */     int nCols = getColumnDimension();
/*  947:1179 */     BlockRealMatrix out = new BlockRealMatrix(nCols, nRows);
/*  948:     */     
/*  949:     */ 
/*  950:1182 */     int blockIndex = 0;
/*  951:1183 */     for (int iBlock = 0; iBlock < this.blockColumns; iBlock++) {
/*  952:1184 */       for (int jBlock = 0; jBlock < this.blockRows; jBlock++)
/*  953:     */       {
/*  954:1186 */         double[] outBlock = out.blocks[blockIndex];
/*  955:1187 */         double[] tBlock = this.blocks[(jBlock * this.blockColumns + iBlock)];
/*  956:1188 */         int pStart = iBlock * 52;
/*  957:1189 */         int pEnd = FastMath.min(pStart + 52, this.columns);
/*  958:1190 */         int qStart = jBlock * 52;
/*  959:1191 */         int qEnd = FastMath.min(qStart + 52, this.rows);
/*  960:1192 */         int k = 0;
/*  961:1193 */         for (int p = pStart; p < pEnd; p++)
/*  962:     */         {
/*  963:1194 */           int lInc = pEnd - pStart;
/*  964:1195 */           int l = p - pStart;
/*  965:1196 */           for (int q = qStart; q < qEnd; q++)
/*  966:     */           {
/*  967:1197 */             outBlock[k] = tBlock[l];
/*  968:1198 */             k++;
/*  969:1199 */             l += lInc;
/*  970:     */           }
/*  971:     */         }
/*  972:1203 */         blockIndex++;
/*  973:     */       }
/*  974:     */     }
/*  975:1207 */     return out;
/*  976:     */   }
/*  977:     */   
/*  978:     */   public int getRowDimension()
/*  979:     */   {
/*  980:1213 */     return this.rows;
/*  981:     */   }
/*  982:     */   
/*  983:     */   public int getColumnDimension()
/*  984:     */   {
/*  985:1219 */     return this.columns;
/*  986:     */   }
/*  987:     */   
/*  988:     */   public double[] operate(double[] v)
/*  989:     */   {
/*  990:1225 */     if (v.length != this.columns) {
/*  991:1226 */       throw new DimensionMismatchException(v.length, this.columns);
/*  992:     */     }
/*  993:1228 */     double[] out = new double[this.rows];
/*  994:1231 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  995:     */     {
/*  996:1232 */       int pStart = iBlock * 52;
/*  997:1233 */       int pEnd = FastMath.min(pStart + 52, this.rows);
/*  998:1234 */       for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  999:     */       {
/* 1000:1235 */         double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1001:1236 */         int qStart = jBlock * 52;
/* 1002:1237 */         int qEnd = FastMath.min(qStart + 52, this.columns);
/* 1003:1238 */         int k = 0;
/* 1004:1239 */         for (int p = pStart; p < pEnd; p++)
/* 1005:     */         {
/* 1006:1240 */           double sum = 0.0D;
/* 1007:1241 */           int q = qStart;
/* 1008:1242 */           while (q < qEnd - 3)
/* 1009:     */           {
/* 1010:1243 */             sum += block[k] * v[q] + block[(k + 1)] * v[(q + 1)] + block[(k + 2)] * v[(q + 2)] + block[(k + 3)] * v[(q + 3)];
/* 1011:     */             
/* 1012:     */ 
/* 1013:     */ 
/* 1014:1247 */             k += 4;
/* 1015:1248 */             q += 4;
/* 1016:     */           }
/* 1017:1250 */           while (q < qEnd) {
/* 1018:1251 */             sum += block[(k++)] * v[(q++)];
/* 1019:     */           }
/* 1020:1253 */           out[p] += sum;
/* 1021:     */         }
/* 1022:     */       }
/* 1023:     */     }
/* 1024:1258 */     return out;
/* 1025:     */   }
/* 1026:     */   
/* 1027:     */   public double[] preMultiply(double[] v)
/* 1028:     */   {
/* 1029:1264 */     if (v.length != this.rows) {
/* 1030:1265 */       throw new DimensionMismatchException(v.length, this.rows);
/* 1031:     */     }
/* 1032:1267 */     double[] out = new double[this.columns];
/* 1033:1270 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1034:     */     {
/* 1035:1271 */       int jWidth = blockWidth(jBlock);
/* 1036:1272 */       int jWidth2 = jWidth + jWidth;
/* 1037:1273 */       int jWidth3 = jWidth2 + jWidth;
/* 1038:1274 */       int jWidth4 = jWidth3 + jWidth;
/* 1039:1275 */       int qStart = jBlock * 52;
/* 1040:1276 */       int qEnd = FastMath.min(qStart + 52, this.columns);
/* 1041:1277 */       for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1042:     */       {
/* 1043:1278 */         double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1044:1279 */         int pStart = iBlock * 52;
/* 1045:1280 */         int pEnd = FastMath.min(pStart + 52, this.rows);
/* 1046:1281 */         for (int q = qStart; q < qEnd; q++)
/* 1047:     */         {
/* 1048:1282 */           int k = q - qStart;
/* 1049:1283 */           double sum = 0.0D;
/* 1050:1284 */           int p = pStart;
/* 1051:1285 */           while (p < pEnd - 3)
/* 1052:     */           {
/* 1053:1286 */             sum += block[k] * v[p] + block[(k + jWidth)] * v[(p + 1)] + block[(k + jWidth2)] * v[(p + 2)] + block[(k + jWidth3)] * v[(p + 3)];
/* 1054:     */             
/* 1055:     */ 
/* 1056:     */ 
/* 1057:1290 */             k += jWidth4;
/* 1058:1291 */             p += 4;
/* 1059:     */           }
/* 1060:1293 */           while (p < pEnd)
/* 1061:     */           {
/* 1062:1294 */             sum += block[k] * v[(p++)];
/* 1063:1295 */             k += jWidth;
/* 1064:     */           }
/* 1065:1297 */           out[q] += sum;
/* 1066:     */         }
/* 1067:     */       }
/* 1068:     */     }
/* 1069:1302 */     return out;
/* 1070:     */   }
/* 1071:     */   
/* 1072:     */   public double walkInRowOrder(RealMatrixChangingVisitor visitor)
/* 1073:     */   {
/* 1074:1308 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1075:1309 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1076:     */     {
/* 1077:1310 */       int pStart = iBlock * 52;
/* 1078:1311 */       int pEnd = FastMath.min(pStart + 52, this.rows);
/* 1079:1312 */       for (int p = pStart; p < pEnd; p++) {
/* 1080:1313 */         for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1081:     */         {
/* 1082:1314 */           int jWidth = blockWidth(jBlock);
/* 1083:1315 */           int qStart = jBlock * 52;
/* 1084:1316 */           int qEnd = FastMath.min(qStart + 52, this.columns);
/* 1085:1317 */           double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1086:1318 */           int k = (p - pStart) * jWidth;
/* 1087:1319 */           for (int q = qStart; q < qEnd; q++)
/* 1088:     */           {
/* 1089:1320 */             block[k] = visitor.visit(p, q, block[k]);
/* 1090:1321 */             k++;
/* 1091:     */           }
/* 1092:     */         }
/* 1093:     */       }
/* 1094:     */     }
/* 1095:1326 */     return visitor.end();
/* 1096:     */   }
/* 1097:     */   
/* 1098:     */   public double walkInRowOrder(RealMatrixPreservingVisitor visitor)
/* 1099:     */   {
/* 1100:1332 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1101:1333 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1102:     */     {
/* 1103:1334 */       int pStart = iBlock * 52;
/* 1104:1335 */       int pEnd = FastMath.min(pStart + 52, this.rows);
/* 1105:1336 */       for (int p = pStart; p < pEnd; p++) {
/* 1106:1337 */         for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1107:     */         {
/* 1108:1338 */           int jWidth = blockWidth(jBlock);
/* 1109:1339 */           int qStart = jBlock * 52;
/* 1110:1340 */           int qEnd = FastMath.min(qStart + 52, this.columns);
/* 1111:1341 */           double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1112:1342 */           int k = (p - pStart) * jWidth;
/* 1113:1343 */           for (int q = qStart; q < qEnd; q++)
/* 1114:     */           {
/* 1115:1344 */             visitor.visit(p, q, block[k]);
/* 1116:1345 */             k++;
/* 1117:     */           }
/* 1118:     */         }
/* 1119:     */       }
/* 1120:     */     }
/* 1121:1350 */     return visitor.end();
/* 1122:     */   }
/* 1123:     */   
/* 1124:     */   public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1125:     */   {
/* 1126:1358 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 1127:1359 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1128:1360 */     for (int iBlock = startRow / 52; iBlock < 1 + endRow / 52; iBlock++)
/* 1129:     */     {
/* 1130:1361 */       int p0 = iBlock * 52;
/* 1131:1362 */       int pStart = FastMath.max(startRow, p0);
/* 1132:1363 */       int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
/* 1133:1364 */       for (int p = pStart; p < pEnd; p++) {
/* 1134:1365 */         for (int jBlock = startColumn / 52; jBlock < 1 + endColumn / 52; jBlock++)
/* 1135:     */         {
/* 1136:1366 */           int jWidth = blockWidth(jBlock);
/* 1137:1367 */           int q0 = jBlock * 52;
/* 1138:1368 */           int qStart = FastMath.max(startColumn, q0);
/* 1139:1369 */           int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
/* 1140:1370 */           double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1141:1371 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1142:1372 */           for (int q = qStart; q < qEnd; q++)
/* 1143:     */           {
/* 1144:1373 */             block[k] = visitor.visit(p, q, block[k]);
/* 1145:1374 */             k++;
/* 1146:     */           }
/* 1147:     */         }
/* 1148:     */       }
/* 1149:     */     }
/* 1150:1379 */     return visitor.end();
/* 1151:     */   }
/* 1152:     */   
/* 1153:     */   public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1154:     */   {
/* 1155:1387 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 1156:1388 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1157:1389 */     for (int iBlock = startRow / 52; iBlock < 1 + endRow / 52; iBlock++)
/* 1158:     */     {
/* 1159:1390 */       int p0 = iBlock * 52;
/* 1160:1391 */       int pStart = FastMath.max(startRow, p0);
/* 1161:1392 */       int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
/* 1162:1393 */       for (int p = pStart; p < pEnd; p++) {
/* 1163:1394 */         for (int jBlock = startColumn / 52; jBlock < 1 + endColumn / 52; jBlock++)
/* 1164:     */         {
/* 1165:1395 */           int jWidth = blockWidth(jBlock);
/* 1166:1396 */           int q0 = jBlock * 52;
/* 1167:1397 */           int qStart = FastMath.max(startColumn, q0);
/* 1168:1398 */           int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
/* 1169:1399 */           double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1170:1400 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1171:1401 */           for (int q = qStart; q < qEnd; q++)
/* 1172:     */           {
/* 1173:1402 */             visitor.visit(p, q, block[k]);
/* 1174:1403 */             k++;
/* 1175:     */           }
/* 1176:     */         }
/* 1177:     */       }
/* 1178:     */     }
/* 1179:1408 */     return visitor.end();
/* 1180:     */   }
/* 1181:     */   
/* 1182:     */   public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor)
/* 1183:     */   {
/* 1184:1414 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1185:1415 */     int blockIndex = 0;
/* 1186:1416 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1187:     */     {
/* 1188:1417 */       int pStart = iBlock * 52;
/* 1189:1418 */       int pEnd = FastMath.min(pStart + 52, this.rows);
/* 1190:1419 */       for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1191:     */       {
/* 1192:1420 */         int qStart = jBlock * 52;
/* 1193:1421 */         int qEnd = FastMath.min(qStart + 52, this.columns);
/* 1194:1422 */         double[] block = this.blocks[blockIndex];
/* 1195:1423 */         int k = 0;
/* 1196:1424 */         for (int p = pStart; p < pEnd; p++) {
/* 1197:1425 */           for (int q = qStart; q < qEnd; q++)
/* 1198:     */           {
/* 1199:1426 */             block[k] = visitor.visit(p, q, block[k]);
/* 1200:1427 */             k++;
/* 1201:     */           }
/* 1202:     */         }
/* 1203:1430 */         blockIndex++;
/* 1204:     */       }
/* 1205:     */     }
/* 1206:1433 */     return visitor.end();
/* 1207:     */   }
/* 1208:     */   
/* 1209:     */   public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor)
/* 1210:     */   {
/* 1211:1439 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1212:1440 */     int blockIndex = 0;
/* 1213:1441 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1214:     */     {
/* 1215:1442 */       int pStart = iBlock * 52;
/* 1216:1443 */       int pEnd = FastMath.min(pStart + 52, this.rows);
/* 1217:1444 */       for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1218:     */       {
/* 1219:1445 */         int qStart = jBlock * 52;
/* 1220:1446 */         int qEnd = FastMath.min(qStart + 52, this.columns);
/* 1221:1447 */         double[] block = this.blocks[blockIndex];
/* 1222:1448 */         int k = 0;
/* 1223:1449 */         for (int p = pStart; p < pEnd; p++) {
/* 1224:1450 */           for (int q = qStart; q < qEnd; q++)
/* 1225:     */           {
/* 1226:1451 */             visitor.visit(p, q, block[k]);
/* 1227:1452 */             k++;
/* 1228:     */           }
/* 1229:     */         }
/* 1230:1455 */         blockIndex++;
/* 1231:     */       }
/* 1232:     */     }
/* 1233:1458 */     return visitor.end();
/* 1234:     */   }
/* 1235:     */   
/* 1236:     */   public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1237:     */   {
/* 1238:1466 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 1239:1467 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1240:1468 */     for (int iBlock = startRow / 52; iBlock < 1 + endRow / 52; iBlock++)
/* 1241:     */     {
/* 1242:1469 */       int p0 = iBlock * 52;
/* 1243:1470 */       int pStart = FastMath.max(startRow, p0);
/* 1244:1471 */       int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
/* 1245:1472 */       for (int jBlock = startColumn / 52; jBlock < 1 + endColumn / 52; jBlock++)
/* 1246:     */       {
/* 1247:1473 */         int jWidth = blockWidth(jBlock);
/* 1248:1474 */         int q0 = jBlock * 52;
/* 1249:1475 */         int qStart = FastMath.max(startColumn, q0);
/* 1250:1476 */         int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
/* 1251:1477 */         double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1252:1478 */         for (int p = pStart; p < pEnd; p++)
/* 1253:     */         {
/* 1254:1479 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1255:1480 */           for (int q = qStart; q < qEnd; q++)
/* 1256:     */           {
/* 1257:1481 */             block[k] = visitor.visit(p, q, block[k]);
/* 1258:1482 */             k++;
/* 1259:     */           }
/* 1260:     */         }
/* 1261:     */       }
/* 1262:     */     }
/* 1263:1487 */     return visitor.end();
/* 1264:     */   }
/* 1265:     */   
/* 1266:     */   public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1267:     */   {
/* 1268:1495 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 1269:1496 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1270:1497 */     for (int iBlock = startRow / 52; iBlock < 1 + endRow / 52; iBlock++)
/* 1271:     */     {
/* 1272:1498 */       int p0 = iBlock * 52;
/* 1273:1499 */       int pStart = FastMath.max(startRow, p0);
/* 1274:1500 */       int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
/* 1275:1501 */       for (int jBlock = startColumn / 52; jBlock < 1 + endColumn / 52; jBlock++)
/* 1276:     */       {
/* 1277:1502 */         int jWidth = blockWidth(jBlock);
/* 1278:1503 */         int q0 = jBlock * 52;
/* 1279:1504 */         int qStart = FastMath.max(startColumn, q0);
/* 1280:1505 */         int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
/* 1281:1506 */         double[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1282:1507 */         for (int p = pStart; p < pEnd; p++)
/* 1283:     */         {
/* 1284:1508 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1285:1509 */           for (int q = qStart; q < qEnd; q++)
/* 1286:     */           {
/* 1287:1510 */             visitor.visit(p, q, block[k]);
/* 1288:1511 */             k++;
/* 1289:     */           }
/* 1290:     */         }
/* 1291:     */       }
/* 1292:     */     }
/* 1293:1516 */     return visitor.end();
/* 1294:     */   }
/* 1295:     */   
/* 1296:     */   private int blockHeight(int blockRow)
/* 1297:     */   {
/* 1298:1525 */     return blockRow == this.blockRows - 1 ? this.rows - blockRow * 52 : 52;
/* 1299:     */   }
/* 1300:     */   
/* 1301:     */   private int blockWidth(int blockColumn)
/* 1302:     */   {
/* 1303:1534 */     return blockColumn == this.blockColumns - 1 ? this.columns - blockColumn * 52 : 52;
/* 1304:     */   }
/* 1305:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.BlockRealMatrix
 * JD-Core Version:    0.7.0.1
 */
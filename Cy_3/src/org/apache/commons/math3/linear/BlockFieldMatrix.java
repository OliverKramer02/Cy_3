/*    1:     */ package org.apache.commons.math3.linear;
/*    2:     */ 
/*    3:     */ import java.io.Serializable;
/*    4:     */ import org.apache.commons.math3.Field;
/*    5:     */ import org.apache.commons.math3.FieldElement;
/*    6:     */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*    7:     */ import org.apache.commons.math3.exception.NoDataException;
/*    8:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*    9:     */ import org.apache.commons.math3.util.FastMath;
/*   10:     */ import org.apache.commons.math3.util.MathUtils;
/*   11:     */ 
/*   12:     */ public class BlockFieldMatrix<T extends FieldElement<T>>
/*   13:     */   extends AbstractFieldMatrix<T>
/*   14:     */   implements Serializable
/*   15:     */ {
/*   16:     */   public static final int BLOCK_SIZE = 36;
/*   17:     */   private static final long serialVersionUID = -4602336630143123183L;
/*   18:     */   private final T[][] blocks;
/*   19:     */   private final int rows;
/*   20:     */   private final int columns;
/*   21:     */   private final int blockRows;
/*   22:     */   private final int blockColumns;
/*   23:     */   
/*   24:     */   public BlockFieldMatrix(Field<T> field, int rows, int columns)
/*   25:     */   {
/*   26:  96 */     super(field, rows, columns);
/*   27:  97 */     this.rows = rows;
/*   28:  98 */     this.columns = columns;
/*   29:     */     
/*   30:     */ 
/*   31: 101 */     this.blockRows = ((rows + 36 - 1) / 36);
/*   32: 102 */     this.blockColumns = ((columns + 36 - 1) / 36);
/*   33:     */     
/*   34:     */ 
/*   35: 105 */     this.blocks = createBlocksLayout(field, rows, columns);
/*   36:     */   }
/*   37:     */   
/*   38:     */   public BlockFieldMatrix(T[][] rawData)
/*   39:     */   {
/*   40: 122 */     this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
/*   41:     */   }
/*   42:     */   
/*   43:     */   public BlockFieldMatrix(int rows, int columns, T[][] blockData, boolean copyArray)
/*   44:     */   {
/*   45: 142 */     super(extractField(blockData), rows, columns);
/*   46: 143 */     this.rows = rows;
/*   47: 144 */     this.columns = columns;
/*   48:     */     
/*   49:     */ 
/*   50: 147 */     this.blockRows = ((rows + 36 - 1) / 36);
/*   51: 148 */     this.blockColumns = ((columns + 36 - 1) / 36);
/*   52: 150 */     if (copyArray) {
/*   53: 152 */       this.blocks = buildArray(getField(), this.blockRows * this.blockColumns, -1);
/*   54:     */     } else {
/*   55: 155 */       this.blocks = blockData;
/*   56:     */     }
/*   57: 158 */     int index = 0;
/*   58: 159 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*   59:     */     {
/*   60: 160 */       int iHeight = blockHeight(iBlock);
/*   61: 161 */       for (int jBlock = 0; jBlock < this.blockColumns; index++)
/*   62:     */       {
/*   63: 162 */         if (blockData[index].length != iHeight * blockWidth(jBlock)) {
/*   64: 163 */           throw new DimensionMismatchException(blockData[index].length, iHeight * blockWidth(jBlock));
/*   65:     */         }
/*   66: 166 */         if (copyArray) {
/*   67: 167 */           this.blocks[index] = (T[]) ((FieldElement[])blockData[index].clone());
/*   68:     */         }
/*   69: 161 */         jBlock++;
/*   70:     */       }
/*   71:     */     }
/*   72:     */   }
/*   73:     */   
/*   74:     */   public static <T extends FieldElement<T>> T[][] toBlocksLayout(T[][] rawData)
/*   75:     */   {
/*   76: 199 */     int rows = rawData.length;
/*   77: 200 */     int columns = rawData[0].length;
/*   78: 201 */     int blockRows = (rows + 36 - 1) / 36;
/*   79: 202 */     int blockColumns = (columns + 36 - 1) / 36;
/*   80: 205 */     for (int i = 0; i < rawData.length; i++)
/*   81:     */     {
/*   82: 206 */       int length = rawData[i].length;
/*   83: 207 */       if (length != columns) {
/*   84: 208 */         throw new DimensionMismatchException(columns, length);
/*   85:     */       }
/*   86:     */     }
/*   87: 213 */     Field<T> field = extractField(rawData);
/*   88: 214 */     T[][] blocks = buildArray(field, blockRows * blockColumns, -1);
/*   89: 215 */     int blockIndex = 0;
/*   90: 216 */     for (int iBlock = 0; iBlock < blockRows; iBlock++)
/*   91:     */     {
/*   92: 217 */       int pStart = iBlock * 36;
/*   93: 218 */       int pEnd = FastMath.min(pStart + 36, rows);
/*   94: 219 */       int iHeight = pEnd - pStart;
/*   95: 220 */       for (int jBlock = 0; jBlock < blockColumns; jBlock++)
/*   96:     */       {
/*   97: 221 */         int qStart = jBlock * 36;
/*   98: 222 */         int qEnd = FastMath.min(qStart + 36, columns);
/*   99: 223 */         int jWidth = qEnd - qStart;
/*  100:     */         
/*  101:     */ 
/*  102: 226 */         T[] block = buildArray(field, iHeight * jWidth);
/*  103: 227 */         blocks[blockIndex] = block;
/*  104:     */         
/*  105:     */ 
/*  106: 230 */         int index = 0;
/*  107: 231 */         for (int p = pStart; p < pEnd; p++)
/*  108:     */         {
/*  109: 232 */           System.arraycopy(rawData[p], qStart, block, index, jWidth);
/*  110: 233 */           index += jWidth;
/*  111:     */         }
/*  112: 236 */         blockIndex++;
/*  113:     */       }
/*  114:     */     }
/*  115: 240 */     return blocks;
/*  116:     */   }
/*  117:     */   
/*  118:     */   public static <T extends FieldElement<T>> T[][] createBlocksLayout(Field<T> field, int rows, int columns)
/*  119:     */   {
/*  120: 260 */     int blockRows = (rows + 36 - 1) / 36;
/*  121: 261 */     int blockColumns = (columns + 36 - 1) / 36;
/*  122:     */     
/*  123: 263 */     T[][] blocks = buildArray(field, blockRows * blockColumns, -1);
/*  124: 264 */     int blockIndex = 0;
/*  125: 265 */     for (int iBlock = 0; iBlock < blockRows; iBlock++)
/*  126:     */     {
/*  127: 266 */       int pStart = iBlock * 36;
/*  128: 267 */       int pEnd = FastMath.min(pStart + 36, rows);
/*  129: 268 */       int iHeight = pEnd - pStart;
/*  130: 269 */       for (int jBlock = 0; jBlock < blockColumns; jBlock++)
/*  131:     */       {
/*  132: 270 */         int qStart = jBlock * 36;
/*  133: 271 */         int qEnd = FastMath.min(qStart + 36, columns);
/*  134: 272 */         int jWidth = qEnd - qStart;
/*  135: 273 */         blocks[blockIndex] = buildArray(field, iHeight * jWidth);
/*  136: 274 */         blockIndex++;
/*  137:     */       }
/*  138:     */     }
/*  139: 278 */     return blocks;
/*  140:     */   }
/*  141:     */   
/*  142:     */   public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension)
/*  143:     */   {
/*  144: 284 */     return new BlockFieldMatrix(getField(), rowDimension, columnDimension);
/*  145:     */   }
/*  146:     */   
/*  147:     */   public FieldMatrix<T> copy()
/*  148:     */   {
/*  149: 292 */     BlockFieldMatrix<T> copied = new BlockFieldMatrix(getField(), this.rows, this.columns);
/*  150: 295 */     for (int i = 0; i < this.blocks.length; i++) {
/*  151: 296 */       System.arraycopy(this.blocks[i], 0, copied.blocks[i], 0, this.blocks[i].length);
/*  152:     */     }
/*  153: 299 */     return copied;
/*  154:     */   }
/*  155:     */   
/*  156:     */   public FieldMatrix<T> add(FieldMatrix<T> m)
/*  157:     */   {
/*  158:     */     try
/*  159:     */     {
/*  160: 306 */       return add((BlockFieldMatrix)m);
/*  161:     */     }
/*  162:     */     catch (ClassCastException cce)
/*  163:     */     {
/*  164: 310 */       checkAdditionCompatible(m);
/*  165:     */       
/*  166: 312 */       BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, this.columns);
/*  167:     */       
/*  168:     */ 
/*  169: 315 */       int blockIndex = 0;
/*  170: 316 */       for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
/*  171: 317 */         for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  172:     */         {
/*  173: 320 */           T[] outBlock = out.blocks[blockIndex];
/*  174: 321 */           T[] tBlock = this.blocks[blockIndex];
/*  175: 322 */           int pStart = iBlock * 36;
/*  176: 323 */           int pEnd = FastMath.min(pStart + 36, this.rows);
/*  177: 324 */           int qStart = jBlock * 36;
/*  178: 325 */           int qEnd = FastMath.min(qStart + 36, this.columns);
/*  179: 326 */           int k = 0;
/*  180: 327 */           for (int p = pStart; p < pEnd; p++) {
/*  181: 328 */             for (int q = qStart; q < qEnd; q++)
/*  182:     */             {
/*  183: 329 */               outBlock[k] = (T) ((FieldElement)tBlock[k].add(m.getEntry(p, q)));
/*  184: 330 */               k++;
/*  185:     */             }
/*  186:     */           }
/*  187: 335 */           blockIndex++;
/*  188:     */         }
/*  189:     */       }
/*  190: 340 */       return out;
/*  191:     */     }
/*  192:     */   }
/*  193:     */   
/*  194:     */   public BlockFieldMatrix<T> add(BlockFieldMatrix<T> m)
/*  195:     */   {
/*  196: 354 */     checkAdditionCompatible(m);
/*  197:     */     
/*  198: 356 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, this.columns);
/*  199: 359 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  200:     */     {
/*  201: 360 */       T[] outBlock = out.blocks[blockIndex];
/*  202: 361 */       T[] tBlock = this.blocks[blockIndex];
/*  203: 362 */       T[] mBlock = m.blocks[blockIndex];
/*  204: 363 */       for (int k = 0; k < outBlock.length; k++) {
/*  205: 364 */         outBlock[k] = (T) ((FieldElement)tBlock[k].add(mBlock[k]));
/*  206:     */       }
/*  207:     */     }
/*  208: 368 */     return out;
/*  209:     */   }
/*  210:     */   
/*  211:     */   public FieldMatrix<T> subtract(FieldMatrix<T> m)
/*  212:     */   {
/*  213:     */     try
/*  214:     */     {
/*  215: 375 */       return subtract((BlockFieldMatrix)m);
/*  216:     */     }
/*  217:     */     catch (ClassCastException cce)
/*  218:     */     {
/*  219: 379 */       checkSubtractionCompatible(m);
/*  220:     */       
/*  221: 381 */       BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, this.columns);
/*  222:     */       
/*  223:     */ 
/*  224: 384 */       int blockIndex = 0;
/*  225: 385 */       for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
/*  226: 386 */         for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  227:     */         {
/*  228: 389 */           T[] outBlock = out.blocks[blockIndex];
/*  229: 390 */           T[] tBlock = this.blocks[blockIndex];
/*  230: 391 */           int pStart = iBlock * 36;
/*  231: 392 */           int pEnd = FastMath.min(pStart + 36, this.rows);
/*  232: 393 */           int qStart = jBlock * 36;
/*  233: 394 */           int qEnd = FastMath.min(qStart + 36, this.columns);
/*  234: 395 */           int k = 0;
/*  235: 396 */           for (int p = pStart; p < pEnd; p++) {
/*  236: 397 */             for (int q = qStart; q < qEnd; q++)
/*  237:     */             {
/*  238: 398 */               outBlock[k] = (T) ((FieldElement)tBlock[k].subtract(m.getEntry(p, q)));
/*  239: 399 */               k++;
/*  240:     */             }
/*  241:     */           }
/*  242: 404 */           blockIndex++;
/*  243:     */         }
/*  244:     */       }
/*  245: 409 */       return out;
/*  246:     */     }
/*  247:     */   }
/*  248:     */   
/*  249:     */   public BlockFieldMatrix<T> subtract(BlockFieldMatrix<T> m)
/*  250:     */   {
/*  251: 422 */     checkSubtractionCompatible(m);
/*  252:     */     
/*  253: 424 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, this.columns);
/*  254: 427 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  255:     */     {
/*  256: 428 */       T[] outBlock = out.blocks[blockIndex];
/*  257: 429 */       T[] tBlock = this.blocks[blockIndex];
/*  258: 430 */       T[] mBlock = m.blocks[blockIndex];
/*  259: 431 */       for (int k = 0; k < outBlock.length; k++) {
/*  260: 432 */         outBlock[k] = (T) ((FieldElement)tBlock[k].subtract(mBlock[k]));
/*  261:     */       }
/*  262:     */     }
/*  263: 436 */     return out;
/*  264:     */   }
/*  265:     */   
/*  266:     */   public FieldMatrix<T> scalarAdd(T d)
/*  267:     */   {
/*  268: 442 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, this.columns);
/*  269: 445 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  270:     */     {
/*  271: 446 */       T[] outBlock = out.blocks[blockIndex];
/*  272: 447 */       T[] tBlock = this.blocks[blockIndex];
/*  273: 448 */       for (int k = 0; k < outBlock.length; k++) {
/*  274: 449 */         outBlock[k] = (T) ((FieldElement)tBlock[k].add(d));
/*  275:     */       }
/*  276:     */     }
/*  277: 453 */     return out;
/*  278:     */   }
/*  279:     */   
/*  280:     */   public FieldMatrix<T> scalarMultiply(T d)
/*  281:     */   {
/*  282: 460 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, this.columns);
/*  283: 463 */     for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++)
/*  284:     */     {
/*  285: 464 */       T[] outBlock = out.blocks[blockIndex];
/*  286: 465 */       T[] tBlock = this.blocks[blockIndex];
/*  287: 466 */       for (int k = 0; k < outBlock.length; k++) {
/*  288: 467 */         outBlock[k] = (T) ((FieldElement)tBlock[k].multiply(d));
/*  289:     */       }
/*  290:     */     }
/*  291: 471 */     return out;
/*  292:     */   }
/*  293:     */   
/*  294:     */   public FieldMatrix<T> multiply(FieldMatrix<T> m)
/*  295:     */   {
/*  296:     */     try
/*  297:     */     {
/*  298: 478 */       return multiply((BlockFieldMatrix)m);
/*  299:     */     }
/*  300:     */     catch (ClassCastException cce)
/*  301:     */     {
/*  302: 482 */       checkMultiplicationCompatible(m);
/*  303:     */       
/*  304: 484 */       BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, m.getColumnDimension());
/*  305: 485 */       T zero = (T)getField().getZero();
/*  306:     */       
/*  307:     */ 
/*  308: 488 */       int blockIndex = 0;
/*  309: 489 */       for (int iBlock = 0; iBlock < out.blockRows; iBlock++)
/*  310:     */       {
/*  311: 491 */         int pStart = iBlock * 36;
/*  312: 492 */         int pEnd = FastMath.min(pStart + 36, this.rows);
/*  313: 494 */         for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  314:     */         {
/*  315: 496 */           int qStart = jBlock * 36;
/*  316: 497 */           int qEnd = FastMath.min(qStart + 36, m.getColumnDimension());
/*  317:     */           
/*  318:     */ 
/*  319: 500 */           T[] outBlock = out.blocks[blockIndex];
/*  320: 503 */           for (int kBlock = 0; kBlock < this.blockColumns; kBlock++)
/*  321:     */           {
/*  322: 504 */             int kWidth = blockWidth(kBlock);
/*  323: 505 */             T[] tBlock = this.blocks[(iBlock * this.blockColumns + kBlock)];
/*  324: 506 */             int rStart = kBlock * 36;
/*  325: 507 */             int k = 0;
/*  326: 508 */             for (int p = pStart; p < pEnd; p++)
/*  327:     */             {
/*  328: 509 */               int lStart = (p - pStart) * kWidth;
/*  329: 510 */               int lEnd = lStart + kWidth;
/*  330: 511 */               for (int q = qStart; q < qEnd; q++)
/*  331:     */               {
/*  332: 512 */                 T sum = zero;
/*  333: 513 */                 int r = rStart;
/*  334: 514 */                 for (int l = lStart; l < lEnd; l++)
/*  335:     */                 {
/*  336: 515 */                   sum = (T)sum.add(tBlock[l].multiply(m.getEntry(r, q)));
/*  337: 516 */                   r++;
/*  338:     */                 }
/*  339: 518 */                 outBlock[k] = (T) ((FieldElement)outBlock[k].add(sum));
/*  340: 519 */                 k++;
/*  341:     */               }
/*  342:     */             }
/*  343:     */           }
/*  344: 525 */           blockIndex++;
/*  345:     */         }
/*  346:     */       }
/*  347: 530 */       return out;
/*  348:     */     }
/*  349:     */   }
/*  350:     */   
/*  351:     */   public BlockFieldMatrix<T> multiply(BlockFieldMatrix<T> m)
/*  352:     */   {
/*  353: 545 */     checkMultiplicationCompatible(m);
/*  354:     */     
/*  355: 547 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, m.columns);
/*  356: 548 */     T zero = (T)getField().getZero();
/*  357:     */     
/*  358:     */ 
/*  359: 551 */     int blockIndex = 0;
/*  360: 552 */     for (int iBlock = 0; iBlock < out.blockRows; iBlock++)
/*  361:     */     {
/*  362: 554 */       int pStart = iBlock * 36;
/*  363: 555 */       int pEnd = FastMath.min(pStart + 36, this.rows);
/*  364: 557 */       for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  365:     */       {
/*  366: 558 */         int jWidth = out.blockWidth(jBlock);
/*  367: 559 */         int jWidth2 = jWidth + jWidth;
/*  368: 560 */         int jWidth3 = jWidth2 + jWidth;
/*  369: 561 */         int jWidth4 = jWidth3 + jWidth;
/*  370:     */         
/*  371:     */ 
/*  372: 564 */         T[] outBlock = out.blocks[blockIndex];
/*  373: 567 */         for (int kBlock = 0; kBlock < this.blockColumns; kBlock++)
/*  374:     */         {
/*  375: 568 */           int kWidth = blockWidth(kBlock);
/*  376: 569 */           T[] tBlock = this.blocks[(iBlock * this.blockColumns + kBlock)];
/*  377: 570 */           T[] mBlock = m.blocks[(kBlock * m.blockColumns + jBlock)];
/*  378: 571 */           int k = 0;
/*  379: 572 */           for (int p = pStart; p < pEnd; p++)
/*  380:     */           {
/*  381: 573 */             int lStart = (p - pStart) * kWidth;
/*  382: 574 */             int lEnd = lStart + kWidth;
/*  383: 575 */             for (int nStart = 0; nStart < jWidth; nStart++)
/*  384:     */             {
/*  385: 576 */               T sum = zero;
/*  386: 577 */               int l = lStart;
/*  387: 578 */               int n = nStart;
/*  388: 579 */               while (l < lEnd - 3)
/*  389:     */               {
/*  390: 580 */                 sum = (T)((FieldElement)((FieldElement)((FieldElement)sum.add(tBlock[l].multiply(mBlock[n]))).add(tBlock[(l + 1)].multiply(mBlock[(n + jWidth)]))).add(tBlock[(l + 2)].multiply(mBlock[(n + jWidth2)]))).add(tBlock[(l + 3)].multiply(mBlock[(n + jWidth3)]));
/*  391:     */                 
/*  392:     */ 
/*  393:     */ 
/*  394:     */ 
/*  395: 585 */                 l += 4;
/*  396: 586 */                 n += jWidth4;
/*  397:     */               }
/*  398: 588 */               while (l < lEnd)
/*  399:     */               {
/*  400: 589 */                 sum = (T)sum.add(tBlock[(l++)].multiply(mBlock[n]));
/*  401: 590 */                 n += jWidth;
/*  402:     */               }
/*  403: 592 */               outBlock[k] = (T) ((FieldElement)outBlock[k].add(sum));
/*  404: 593 */               k++;
/*  405:     */             }
/*  406:     */           }
/*  407:     */         }
/*  408: 599 */         blockIndex++;
/*  409:     */       }
/*  410:     */     }
/*  411: 603 */     return out;
/*  412:     */   }
/*  413:     */   
/*  414:     */   public T[][] getData()
/*  415:     */   {
/*  416: 610 */     T[][] data = buildArray(getField(), getRowDimension(), getColumnDimension());
/*  417: 611 */     int lastColumns = this.columns - (this.blockColumns - 1) * 36;
/*  418: 613 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  419:     */     {
/*  420: 614 */       int pStart = iBlock * 36;
/*  421: 615 */       int pEnd = FastMath.min(pStart + 36, this.rows);
/*  422: 616 */       int regularPos = 0;
/*  423: 617 */       int lastPos = 0;
/*  424: 618 */       for (int p = pStart; p < pEnd; p++)
/*  425:     */       {
/*  426: 619 */         T[] dataP = data[p];
/*  427: 620 */         int blockIndex = iBlock * this.blockColumns;
/*  428: 621 */         int dataPos = 0;
/*  429: 622 */         for (int jBlock = 0; jBlock < this.blockColumns - 1; jBlock++)
/*  430:     */         {
/*  431: 623 */           System.arraycopy(this.blocks[(blockIndex++)], regularPos, dataP, dataPos, 36);
/*  432: 624 */           dataPos += 36;
/*  433:     */         }
/*  434: 626 */         System.arraycopy(this.blocks[blockIndex], lastPos, dataP, dataPos, lastColumns);
/*  435: 627 */         regularPos += 36;
/*  436: 628 */         lastPos += lastColumns;
/*  437:     */       }
/*  438:     */     }
/*  439: 632 */     return data;
/*  440:     */   }
/*  441:     */   
/*  442:     */   public FieldMatrix<T> getSubMatrix(int startRow, int endRow, int startColumn, int endColumn)
/*  443:     */   {
/*  444: 640 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/*  445:     */     
/*  446:     */ 
/*  447: 643 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), endRow - startRow + 1, endColumn - startColumn + 1);
/*  448:     */     
/*  449:     */ 
/*  450:     */ 
/*  451: 647 */     int blockStartRow = startRow / 36;
/*  452: 648 */     int rowsShift = startRow % 36;
/*  453: 649 */     int blockStartColumn = startColumn / 36;
/*  454: 650 */     int columnsShift = startColumn % 36;
/*  455:     */     
/*  456:     */ 
/*  457: 653 */     int pBlock = blockStartRow;
/*  458: 654 */     for (int iBlock = 0; iBlock < out.blockRows; iBlock++)
/*  459:     */     {
/*  460: 655 */       int iHeight = out.blockHeight(iBlock);
/*  461: 656 */       int qBlock = blockStartColumn;
/*  462: 657 */       for (int jBlock = 0; jBlock < out.blockColumns; jBlock++)
/*  463:     */       {
/*  464: 658 */         int jWidth = out.blockWidth(jBlock);
/*  465:     */         
/*  466:     */ 
/*  467: 661 */         int outIndex = iBlock * out.blockColumns + jBlock;
/*  468: 662 */         T[] outBlock = out.blocks[outIndex];
/*  469: 663 */         int index = pBlock * this.blockColumns + qBlock;
/*  470: 664 */         int width = blockWidth(qBlock);
/*  471:     */         
/*  472: 666 */         int heightExcess = iHeight + rowsShift - 36;
/*  473: 667 */         int widthExcess = jWidth + columnsShift - 36;
/*  474: 668 */         if (heightExcess > 0)
/*  475:     */         {
/*  476: 670 */           if (widthExcess > 0)
/*  477:     */           {
/*  478: 672 */             int width2 = blockWidth(qBlock + 1);
/*  479: 673 */             copyBlockPart(this.blocks[index], width, rowsShift, 36, columnsShift, 36, outBlock, jWidth, 0, 0);
/*  480:     */             
/*  481:     */ 
/*  482:     */ 
/*  483: 677 */             copyBlockPart(this.blocks[(index + 1)], width2, rowsShift, 36, 0, widthExcess, outBlock, jWidth, 0, jWidth - widthExcess);
/*  484:     */             
/*  485:     */ 
/*  486:     */ 
/*  487: 681 */             copyBlockPart(this.blocks[(index + this.blockColumns)], width, 0, heightExcess, columnsShift, 36, outBlock, jWidth, iHeight - heightExcess, 0);
/*  488:     */             
/*  489:     */ 
/*  490:     */ 
/*  491: 685 */             copyBlockPart(this.blocks[(index + this.blockColumns + 1)], width2, 0, heightExcess, 0, widthExcess, outBlock, jWidth, iHeight - heightExcess, jWidth - widthExcess);
/*  492:     */           }
/*  493:     */           else
/*  494:     */           {
/*  495: 691 */             copyBlockPart(this.blocks[index], width, rowsShift, 36, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
/*  496:     */             
/*  497:     */ 
/*  498:     */ 
/*  499: 695 */             copyBlockPart(this.blocks[(index + this.blockColumns)], width, 0, heightExcess, columnsShift, jWidth + columnsShift, outBlock, jWidth, iHeight - heightExcess, 0);
/*  500:     */           }
/*  501:     */         }
/*  502: 702 */         else if (widthExcess > 0)
/*  503:     */         {
/*  504: 704 */           int width2 = blockWidth(qBlock + 1);
/*  505: 705 */           copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, 36, outBlock, jWidth, 0, 0);
/*  506:     */           
/*  507:     */ 
/*  508:     */ 
/*  509: 709 */           copyBlockPart(this.blocks[(index + 1)], width2, rowsShift, iHeight + rowsShift, 0, widthExcess, outBlock, jWidth, 0, jWidth - widthExcess);
/*  510:     */         }
/*  511:     */         else
/*  512:     */         {
/*  513: 715 */           copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
/*  514:     */         }
/*  515: 721 */         qBlock++;
/*  516:     */       }
/*  517: 723 */       pBlock++;
/*  518:     */     }
/*  519: 726 */     return out;
/*  520:     */   }
/*  521:     */   
/*  522:     */   private void copyBlockPart(T[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, T[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn)
/*  523:     */   {
/*  524: 749 */     int length = srcEndColumn - srcStartColumn;
/*  525: 750 */     int srcPos = srcStartRow * srcWidth + srcStartColumn;
/*  526: 751 */     int dstPos = dstStartRow * dstWidth + dstStartColumn;
/*  527: 752 */     for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++)
/*  528:     */     {
/*  529: 753 */       System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
/*  530: 754 */       srcPos += srcWidth;
/*  531: 755 */       dstPos += dstWidth;
/*  532:     */     }
/*  533:     */   }
/*  534:     */   
/*  535:     */   public void setSubMatrix(T[][] subMatrix, int row, int column)
/*  536:     */   {
/*  537: 763 */     MathUtils.checkNotNull(subMatrix);
/*  538: 764 */     int refLength = subMatrix[0].length;
/*  539: 765 */     if (refLength == 0) {
/*  540: 766 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/*  541:     */     }
/*  542: 768 */     int endRow = row + subMatrix.length - 1;
/*  543: 769 */     int endColumn = column + refLength - 1;
/*  544: 770 */     checkSubMatrixIndex(row, endRow, column, endColumn);
/*  545: 771 */     for (T[] subRow : subMatrix) {
/*  546: 772 */       if (subRow.length != refLength) {
/*  547: 773 */         throw new DimensionMismatchException(refLength, subRow.length);
/*  548:     */       }
/*  549:     */     }
/*  550: 778 */     int blockStartRow = row / 36;
/*  551: 779 */     int blockEndRow = (endRow + 36) / 36;
/*  552: 780 */     int blockStartColumn = column / 36;
/*  553: 781 */     int blockEndColumn = (endColumn + 36) / 36;
/*  554: 784 */     for (int iBlock = blockStartRow; iBlock < blockEndRow; iBlock++)
/*  555:     */     {
/*  556: 785 */       int iHeight = blockHeight(iBlock);
/*  557: 786 */       int firstRow = iBlock * 36;
/*  558: 787 */       int iStart = FastMath.max(row, firstRow);
/*  559: 788 */       int iEnd = FastMath.min(endRow + 1, firstRow + iHeight);
/*  560: 790 */       for (int jBlock = blockStartColumn; jBlock < blockEndColumn; jBlock++)
/*  561:     */       {
/*  562: 791 */         int jWidth = blockWidth(jBlock);
/*  563: 792 */         int firstColumn = jBlock * 36;
/*  564: 793 */         int jStart = FastMath.max(column, firstColumn);
/*  565: 794 */         int jEnd = FastMath.min(endColumn + 1, firstColumn + jWidth);
/*  566: 795 */         int jLength = jEnd - jStart;
/*  567:     */         
/*  568:     */ 
/*  569: 798 */         T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  570: 799 */         for (int i = iStart; i < iEnd; i++) {
/*  571: 800 */           System.arraycopy(subMatrix[(i - row)], jStart - column, block, (i - firstRow) * jWidth + (jStart - firstColumn), jLength);
/*  572:     */         }
/*  573:     */       }
/*  574:     */     }
/*  575:     */   }
/*  576:     */   
/*  577:     */   public FieldMatrix<T> getRowMatrix(int row)
/*  578:     */   {
/*  579: 812 */     checkRowIndex(row);
/*  580: 813 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), 1, this.columns);
/*  581:     */     
/*  582:     */ 
/*  583: 816 */     int iBlock = row / 36;
/*  584: 817 */     int iRow = row - iBlock * 36;
/*  585: 818 */     int outBlockIndex = 0;
/*  586: 819 */     int outIndex = 0;
/*  587: 820 */     T[] outBlock = out.blocks[outBlockIndex];
/*  588: 821 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  589:     */     {
/*  590: 822 */       int jWidth = blockWidth(jBlock);
/*  591: 823 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  592: 824 */       int available = outBlock.length - outIndex;
/*  593: 825 */       if (jWidth > available)
/*  594:     */       {
/*  595: 826 */         System.arraycopy(block, iRow * jWidth, outBlock, outIndex, available);
/*  596: 827 */         outBlock = out.blocks[(++outBlockIndex)];
/*  597: 828 */         System.arraycopy(block, iRow * jWidth, outBlock, 0, jWidth - available);
/*  598: 829 */         outIndex = jWidth - available;
/*  599:     */       }
/*  600:     */       else
/*  601:     */       {
/*  602: 831 */         System.arraycopy(block, iRow * jWidth, outBlock, outIndex, jWidth);
/*  603: 832 */         outIndex += jWidth;
/*  604:     */       }
/*  605:     */     }
/*  606: 836 */     return out;
/*  607:     */   }
/*  608:     */   
/*  609:     */   public void setRowMatrix(int row, FieldMatrix<T> matrix)
/*  610:     */   {
/*  611:     */     try
/*  612:     */     {
/*  613: 843 */       setRowMatrix(row, (BlockFieldMatrix)matrix);
/*  614:     */     }
/*  615:     */     catch (ClassCastException cce)
/*  616:     */     {
/*  617: 845 */       super.setRowMatrix(row, matrix);
/*  618:     */     }
/*  619:     */   }
/*  620:     */   
/*  621:     */   public void setRowMatrix(int row, BlockFieldMatrix<T> matrix)
/*  622:     */   {
/*  623: 862 */     checkRowIndex(row);
/*  624: 863 */     int nCols = getColumnDimension();
/*  625: 864 */     if ((matrix.getRowDimension() != 1) || (matrix.getColumnDimension() != nCols)) {
/*  626: 866 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
/*  627:     */     }
/*  628: 872 */     int iBlock = row / 36;
/*  629: 873 */     int iRow = row - iBlock * 36;
/*  630: 874 */     int mBlockIndex = 0;
/*  631: 875 */     int mIndex = 0;
/*  632: 876 */     T[] mBlock = matrix.blocks[mBlockIndex];
/*  633: 877 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  634:     */     {
/*  635: 878 */       int jWidth = blockWidth(jBlock);
/*  636: 879 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  637: 880 */       int available = mBlock.length - mIndex;
/*  638: 881 */       if (jWidth > available)
/*  639:     */       {
/*  640: 882 */         System.arraycopy(mBlock, mIndex, block, iRow * jWidth, available);
/*  641: 883 */         mBlock = matrix.blocks[(++mBlockIndex)];
/*  642: 884 */         System.arraycopy(mBlock, 0, block, iRow * jWidth, jWidth - available);
/*  643: 885 */         mIndex = jWidth - available;
/*  644:     */       }
/*  645:     */       else
/*  646:     */       {
/*  647: 887 */         System.arraycopy(mBlock, mIndex, block, iRow * jWidth, jWidth);
/*  648: 888 */         mIndex += jWidth;
/*  649:     */       }
/*  650:     */     }
/*  651:     */   }
/*  652:     */   
/*  653:     */   public FieldMatrix<T> getColumnMatrix(int column)
/*  654:     */   {
/*  655: 896 */     checkColumnIndex(column);
/*  656: 897 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), this.rows, 1);
/*  657:     */     
/*  658:     */ 
/*  659: 900 */     int jBlock = column / 36;
/*  660: 901 */     int jColumn = column - jBlock * 36;
/*  661: 902 */     int jWidth = blockWidth(jBlock);
/*  662: 903 */     int outBlockIndex = 0;
/*  663: 904 */     int outIndex = 0;
/*  664: 905 */     T[] outBlock = out.blocks[outBlockIndex];
/*  665: 906 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  666:     */     {
/*  667: 907 */       int iHeight = blockHeight(iBlock);
/*  668: 908 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  669: 909 */       for (int i = 0; i < iHeight; i++)
/*  670:     */       {
/*  671: 910 */         if (outIndex >= outBlock.length)
/*  672:     */         {
/*  673: 911 */           outBlock = out.blocks[(++outBlockIndex)];
/*  674: 912 */           outIndex = 0;
/*  675:     */         }
/*  676: 914 */         outBlock[(outIndex++)] = block[(i * jWidth + jColumn)];
/*  677:     */       }
/*  678:     */     }
/*  679: 918 */     return out;
/*  680:     */   }
/*  681:     */   
/*  682:     */   public void setColumnMatrix(int column, FieldMatrix<T> matrix)
/*  683:     */   {
/*  684:     */     try
/*  685:     */     {
/*  686: 925 */       setColumnMatrix(column, (BlockFieldMatrix)matrix);
/*  687:     */     }
/*  688:     */     catch (ClassCastException cce)
/*  689:     */     {
/*  690: 927 */       super.setColumnMatrix(column, matrix);
/*  691:     */     }
/*  692:     */   }
/*  693:     */   
/*  694:     */   void setColumnMatrix(int column, BlockFieldMatrix<T> matrix)
/*  695:     */   {
/*  696: 944 */     checkColumnIndex(column);
/*  697: 945 */     int nRows = getRowDimension();
/*  698: 946 */     if ((matrix.getRowDimension() != nRows) || (matrix.getColumnDimension() != 1)) {
/*  699: 948 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
/*  700:     */     }
/*  701: 954 */     int jBlock = column / 36;
/*  702: 955 */     int jColumn = column - jBlock * 36;
/*  703: 956 */     int jWidth = blockWidth(jBlock);
/*  704: 957 */     int mBlockIndex = 0;
/*  705: 958 */     int mIndex = 0;
/*  706: 959 */     T[] mBlock = matrix.blocks[mBlockIndex];
/*  707: 960 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  708:     */     {
/*  709: 961 */       int iHeight = blockHeight(iBlock);
/*  710: 962 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  711: 963 */       for (int i = 0; i < iHeight; i++)
/*  712:     */       {
/*  713: 964 */         if (mIndex >= mBlock.length)
/*  714:     */         {
/*  715: 965 */           mBlock = matrix.blocks[(++mBlockIndex)];
/*  716: 966 */           mIndex = 0;
/*  717:     */         }
/*  718: 968 */         block[(i * jWidth + jColumn)] = mBlock[(mIndex++)];
/*  719:     */       }
/*  720:     */     }
/*  721:     */   }
/*  722:     */   
/*  723:     */   public FieldVector<T> getRowVector(int row)
/*  724:     */   {
/*  725: 976 */     checkRowIndex(row);
/*  726: 977 */     T[] outData = buildArray(getField(), this.columns);
/*  727:     */     
/*  728:     */ 
/*  729: 980 */     int iBlock = row / 36;
/*  730: 981 */     int iRow = row - iBlock * 36;
/*  731: 982 */     int outIndex = 0;
/*  732: 983 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  733:     */     {
/*  734: 984 */       int jWidth = blockWidth(jBlock);
/*  735: 985 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  736: 986 */       System.arraycopy(block, iRow * jWidth, outData, outIndex, jWidth);
/*  737: 987 */       outIndex += jWidth;
/*  738:     */     }
/*  739: 990 */     return new ArrayFieldVector(getField(), outData, false);
/*  740:     */   }
/*  741:     */   
/*  742:     */   public void setRowVector(int row, FieldVector<T> vector)
/*  743:     */   {
/*  744:     */     try
/*  745:     */     {
/*  746: 997 */       setRow(row, (T[]) ((ArrayFieldVector)vector).getDataRef());
/*  747:     */     }
/*  748:     */     catch (ClassCastException cce)
/*  749:     */     {
/*  750: 999 */       super.setRowVector(row, vector);
/*  751:     */     }
/*  752:     */   }
/*  753:     */   
/*  754:     */   public FieldVector<T> getColumnVector(int column)
/*  755:     */   {
/*  756:1006 */     checkColumnIndex(column);
/*  757:1007 */     T[] outData = buildArray(getField(), this.rows);
/*  758:     */     
/*  759:     */ 
/*  760:1010 */     int jBlock = column / 36;
/*  761:1011 */     int jColumn = column - jBlock * 36;
/*  762:1012 */     int jWidth = blockWidth(jBlock);
/*  763:1013 */     int outIndex = 0;
/*  764:1014 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  765:     */     {
/*  766:1015 */       int iHeight = blockHeight(iBlock);
/*  767:1016 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  768:1017 */       for (int i = 0; i < iHeight; i++) {
/*  769:1018 */         outData[(outIndex++)] = block[(i * jWidth + jColumn)];
/*  770:     */       }
/*  771:     */     }
/*  772:1022 */     return new ArrayFieldVector(getField(), outData, false);
/*  773:     */   }
/*  774:     */   
/*  775:     */   public void setColumnVector(int column, FieldVector<T> vector)
/*  776:     */   {
/*  777:     */     try
/*  778:     */     {
/*  779:1029 */       setColumn(column, (T[]) ((ArrayFieldVector)vector).getDataRef());
/*  780:     */     }
/*  781:     */     catch (ClassCastException cce)
/*  782:     */     {
/*  783:1031 */       super.setColumnVector(column, vector);
/*  784:     */     }
/*  785:     */   }
/*  786:     */   
/*  787:     */   public T[] getRow(int row)
/*  788:     */   {
/*  789:1038 */     checkRowIndex(row);
/*  790:1039 */     T[] out = buildArray(getField(), this.columns);
/*  791:     */     
/*  792:     */ 
/*  793:1042 */     int iBlock = row / 36;
/*  794:1043 */     int iRow = row - iBlock * 36;
/*  795:1044 */     int outIndex = 0;
/*  796:1045 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  797:     */     {
/*  798:1046 */       int jWidth = blockWidth(jBlock);
/*  799:1047 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  800:1048 */       System.arraycopy(block, iRow * jWidth, out, outIndex, jWidth);
/*  801:1049 */       outIndex += jWidth;
/*  802:     */     }
/*  803:1052 */     return out;
/*  804:     */   }
/*  805:     */   
/*  806:     */   public void setRow(int row, T[] array)
/*  807:     */   {
/*  808:1058 */     checkRowIndex(row);
/*  809:1059 */     int nCols = getColumnDimension();
/*  810:1060 */     if (array.length != nCols) {
/*  811:1061 */       throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
/*  812:     */     }
/*  813:1065 */     int iBlock = row / 36;
/*  814:1066 */     int iRow = row - iBlock * 36;
/*  815:1067 */     int outIndex = 0;
/*  816:1068 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  817:     */     {
/*  818:1069 */       int jWidth = blockWidth(jBlock);
/*  819:1070 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  820:1071 */       System.arraycopy(array, outIndex, block, iRow * jWidth, jWidth);
/*  821:1072 */       outIndex += jWidth;
/*  822:     */     }
/*  823:     */   }
/*  824:     */   
/*  825:     */   public T[] getColumn(int column)
/*  826:     */   {
/*  827:1079 */     checkColumnIndex(column);
/*  828:1080 */     T[] out = buildArray(getField(), this.rows);
/*  829:     */     
/*  830:     */ 
/*  831:1083 */     int jBlock = column / 36;
/*  832:1084 */     int jColumn = column - jBlock * 36;
/*  833:1085 */     int jWidth = blockWidth(jBlock);
/*  834:1086 */     int outIndex = 0;
/*  835:1087 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  836:     */     {
/*  837:1088 */       int iHeight = blockHeight(iBlock);
/*  838:1089 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  839:1090 */       for (int i = 0; i < iHeight; i++) {
/*  840:1091 */         out[(outIndex++)] = block[(i * jWidth + jColumn)];
/*  841:     */       }
/*  842:     */     }
/*  843:1095 */     return out;
/*  844:     */   }
/*  845:     */   
/*  846:     */   public void setColumn(int column, T[] array)
/*  847:     */   {
/*  848:1101 */     checkColumnIndex(column);
/*  849:1102 */     int nRows = getRowDimension();
/*  850:1103 */     if (array.length != nRows) {
/*  851:1104 */       throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
/*  852:     */     }
/*  853:1108 */     int jBlock = column / 36;
/*  854:1109 */     int jColumn = column - jBlock * 36;
/*  855:1110 */     int jWidth = blockWidth(jBlock);
/*  856:1111 */     int outIndex = 0;
/*  857:1112 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  858:     */     {
/*  859:1113 */       int iHeight = blockHeight(iBlock);
/*  860:1114 */       T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  861:1115 */       for (int i = 0; i < iHeight; i++) {
/*  862:1116 */         block[(i * jWidth + jColumn)] = array[(outIndex++)];
/*  863:     */       }
/*  864:     */     }
/*  865:     */   }
/*  866:     */   
/*  867:     */   public T getEntry(int row, int column)
/*  868:     */   {
/*  869:1124 */     checkRowIndex(row);
/*  870:1125 */     checkColumnIndex(column);
/*  871:     */     
/*  872:1127 */     int iBlock = row / 36;
/*  873:1128 */     int jBlock = column / 36;
/*  874:1129 */     int k = (row - iBlock * 36) * blockWidth(jBlock) + (column - jBlock * 36);
/*  875:     */     
/*  876:     */ 
/*  877:1132 */     return this.blocks[(iBlock * this.blockColumns + jBlock)][k];
/*  878:     */   }
/*  879:     */   
/*  880:     */   public void setEntry(int row, int column, T value)
/*  881:     */   {
/*  882:1138 */     checkRowIndex(row);
/*  883:1139 */     checkColumnIndex(column);
/*  884:     */     
/*  885:1141 */     int iBlock = row / 36;
/*  886:1142 */     int jBlock = column / 36;
/*  887:1143 */     int k = (row - iBlock * 36) * blockWidth(jBlock) + (column - jBlock * 36);
/*  888:     */     
/*  889:     */ 
/*  890:1146 */     this.blocks[(iBlock * this.blockColumns + jBlock)][k] = value;
/*  891:     */   }
/*  892:     */   
/*  893:     */   public void addToEntry(int row, int column, T increment)
/*  894:     */   {
/*  895:1152 */     checkRowIndex(row);
/*  896:1153 */     checkColumnIndex(column);
/*  897:     */     
/*  898:1155 */     int iBlock = row / 36;
/*  899:1156 */     int jBlock = column / 36;
/*  900:1157 */     int k = (row - iBlock * 36) * blockWidth(jBlock) + (column - jBlock * 36);
/*  901:     */     
/*  902:1159 */     T[] blockIJ = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  903:     */     
/*  904:1161 */     blockIJ[k] = (T) ((FieldElement)blockIJ[k].add(increment));
/*  905:     */   }
/*  906:     */   
/*  907:     */   public void multiplyEntry(int row, int column, T factor)
/*  908:     */   {
/*  909:1167 */     checkRowIndex(row);
/*  910:1168 */     checkColumnIndex(column);
/*  911:     */     
/*  912:1170 */     int iBlock = row / 36;
/*  913:1171 */     int jBlock = column / 36;
/*  914:1172 */     int k = (row - iBlock * 36) * blockWidth(jBlock) + (column - jBlock * 36);
/*  915:     */     
/*  916:1174 */     T[] blockIJ = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  917:     */     
/*  918:1176 */     blockIJ[k] = (T) ((FieldElement)blockIJ[k].multiply(factor));
/*  919:     */   }
/*  920:     */   
/*  921:     */   public FieldMatrix<T> transpose()
/*  922:     */   {
/*  923:1182 */     int nRows = getRowDimension();
/*  924:1183 */     int nCols = getColumnDimension();
/*  925:1184 */     BlockFieldMatrix<T> out = new BlockFieldMatrix(getField(), nCols, nRows);
/*  926:     */     
/*  927:     */ 
/*  928:1187 */     int blockIndex = 0;
/*  929:1188 */     for (int iBlock = 0; iBlock < this.blockColumns; iBlock++) {
/*  930:1189 */       for (int jBlock = 0; jBlock < this.blockRows; jBlock++)
/*  931:     */       {
/*  932:1192 */         T[] outBlock = out.blocks[blockIndex];
/*  933:1193 */         T[] tBlock = this.blocks[(jBlock * this.blockColumns + iBlock)];
/*  934:1194 */         int pStart = iBlock * 36;
/*  935:1195 */         int pEnd = FastMath.min(pStart + 36, this.columns);
/*  936:1196 */         int qStart = jBlock * 36;
/*  937:1197 */         int qEnd = FastMath.min(qStart + 36, this.rows);
/*  938:1198 */         int k = 0;
/*  939:1199 */         for (int p = pStart; p < pEnd; p++)
/*  940:     */         {
/*  941:1200 */           int lInc = pEnd - pStart;
/*  942:1201 */           int l = p - pStart;
/*  943:1202 */           for (int q = qStart; q < qEnd; q++)
/*  944:     */           {
/*  945:1203 */             outBlock[k] = tBlock[l];
/*  946:1204 */             k++;
/*  947:1205 */             l += lInc;
/*  948:     */           }
/*  949:     */         }
/*  950:1210 */         blockIndex++;
/*  951:     */       }
/*  952:     */     }
/*  953:1215 */     return out;
/*  954:     */   }
/*  955:     */   
/*  956:     */   public int getRowDimension()
/*  957:     */   {
/*  958:1221 */     return this.rows;
/*  959:     */   }
/*  960:     */   
/*  961:     */   public int getColumnDimension()
/*  962:     */   {
/*  963:1227 */     return this.columns;
/*  964:     */   }
/*  965:     */   
/*  966:     */   public T[] operate(T[] v)
/*  967:     */   {
/*  968:1233 */     if (v.length != this.columns) {
/*  969:1234 */       throw new DimensionMismatchException(v.length, this.columns);
/*  970:     */     }
/*  971:1236 */     T[] out = buildArray(getField(), this.rows);
/*  972:1237 */     T zero = (T)getField().getZero();
/*  973:1240 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/*  974:     */     {
/*  975:1241 */       int pStart = iBlock * 36;
/*  976:1242 */       int pEnd = FastMath.min(pStart + 36, this.rows);
/*  977:1243 */       for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/*  978:     */       {
/*  979:1244 */         T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/*  980:1245 */         int qStart = jBlock * 36;
/*  981:1246 */         int qEnd = FastMath.min(qStart + 36, this.columns);
/*  982:1247 */         int k = 0;
/*  983:1248 */         for (int p = pStart; p < pEnd; p++)
/*  984:     */         {
/*  985:1249 */           T sum = zero;
/*  986:1250 */           int q = qStart;
/*  987:1251 */           while (q < qEnd - 3)
/*  988:     */           {
/*  989:1252 */             sum = (T)((FieldElement)((FieldElement)((FieldElement)sum.add(block[k].multiply(v[q]))).add(block[(k + 1)].multiply(v[(q + 1)]))).add(block[(k + 2)].multiply(v[(q + 2)]))).add(block[(k + 3)].multiply(v[(q + 3)]));
/*  990:     */             
/*  991:     */ 
/*  992:     */ 
/*  993:     */ 
/*  994:1257 */             k += 4;
/*  995:1258 */             q += 4;
/*  996:     */           }
/*  997:1260 */           while (q < qEnd) {
/*  998:1261 */             sum = (T)sum.add(block[(k++)].multiply(v[(q++)]));
/*  999:     */           }
/* 1000:1263 */           out[p] = (T) ((FieldElement)out[p].add(sum));
/* 1001:     */         }
/* 1002:     */       }
/* 1003:     */     }
/* 1004:1268 */     return out;
/* 1005:     */   }
/* 1006:     */   
/* 1007:     */   public T[] preMultiply(T[] v)
/* 1008:     */   {
/* 1009:1275 */     if (v.length != this.rows) {
/* 1010:1276 */       throw new DimensionMismatchException(v.length, this.rows);
/* 1011:     */     }
/* 1012:1278 */     T[] out = buildArray(getField(), this.columns);
/* 1013:1279 */     T zero = (T)getField().getZero();
/* 1014:1282 */     for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1015:     */     {
/* 1016:1283 */       int jWidth = blockWidth(jBlock);
/* 1017:1284 */       int jWidth2 = jWidth + jWidth;
/* 1018:1285 */       int jWidth3 = jWidth2 + jWidth;
/* 1019:1286 */       int jWidth4 = jWidth3 + jWidth;
/* 1020:1287 */       int qStart = jBlock * 36;
/* 1021:1288 */       int qEnd = FastMath.min(qStart + 36, this.columns);
/* 1022:1289 */       for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1023:     */       {
/* 1024:1290 */         T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1025:1291 */         int pStart = iBlock * 36;
/* 1026:1292 */         int pEnd = FastMath.min(pStart + 36, this.rows);
/* 1027:1293 */         for (int q = qStart; q < qEnd; q++)
/* 1028:     */         {
/* 1029:1294 */           int k = q - qStart;
/* 1030:1295 */           T sum = zero;
/* 1031:1296 */           int p = pStart;
/* 1032:1297 */           while (p < pEnd - 3)
/* 1033:     */           {
/* 1034:1298 */             sum = (T)((FieldElement)((FieldElement)((FieldElement)sum.add(block[k].multiply(v[p]))).add(block[(k + jWidth)].multiply(v[(p + 1)]))).add(block[(k + jWidth2)].multiply(v[(p + 2)]))).add(block[(k + jWidth3)].multiply(v[(p + 3)]));
/* 1035:     */             
/* 1036:     */ 
/* 1037:     */ 
/* 1038:     */ 
/* 1039:1303 */             k += jWidth4;
/* 1040:1304 */             p += 4;
/* 1041:     */           }
/* 1042:1306 */           while (p < pEnd)
/* 1043:     */           {
/* 1044:1307 */             sum = (T)sum.add(block[k].multiply(v[(p++)]));
/* 1045:1308 */             k += jWidth;
/* 1046:     */           }
/* 1047:1310 */           out[q] = (T) ((FieldElement)out[q].add(sum));
/* 1048:     */         }
/* 1049:     */       }
/* 1050:     */     }
/* 1051:1315 */     return out;
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor)
/* 1055:     */   {
/* 1056:1321 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1057:1322 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1058:     */     {
/* 1059:1323 */       int pStart = iBlock * 36;
/* 1060:1324 */       int pEnd = FastMath.min(pStart + 36, this.rows);
/* 1061:1325 */       for (int p = pStart; p < pEnd; p++) {
/* 1062:1326 */         for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1063:     */         {
/* 1064:1327 */           int jWidth = blockWidth(jBlock);
/* 1065:1328 */           int qStart = jBlock * 36;
/* 1066:1329 */           int qEnd = FastMath.min(qStart + 36, this.columns);
/* 1067:1330 */           T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1068:1331 */           int k = (p - pStart) * jWidth;
/* 1069:1332 */           for (int q = qStart; q < qEnd; q++)
/* 1070:     */           {
/* 1071:1333 */             block[k] = visitor.visit(p, q, block[k]);
/* 1072:1334 */             k++;
/* 1073:     */           }
/* 1074:     */         }
/* 1075:     */       }
/* 1076:     */     }
/* 1077:1339 */     return visitor.end();
/* 1078:     */   }
/* 1079:     */   
/* 1080:     */   public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor)
/* 1081:     */   {
/* 1082:1345 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1083:1346 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1084:     */     {
/* 1085:1347 */       int pStart = iBlock * 36;
/* 1086:1348 */       int pEnd = FastMath.min(pStart + 36, this.rows);
/* 1087:1349 */       for (int p = pStart; p < pEnd; p++) {
/* 1088:1350 */         for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1089:     */         {
/* 1090:1351 */           int jWidth = blockWidth(jBlock);
/* 1091:1352 */           int qStart = jBlock * 36;
/* 1092:1353 */           int qEnd = FastMath.min(qStart + 36, this.columns);
/* 1093:1354 */           T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1094:1355 */           int k = (p - pStart) * jWidth;
/* 1095:1356 */           for (int q = qStart; q < qEnd; q++)
/* 1096:     */           {
/* 1097:1357 */             visitor.visit(p, q, block[k]);
/* 1098:1358 */             k++;
/* 1099:     */           }
/* 1100:     */         }
/* 1101:     */       }
/* 1102:     */     }
/* 1103:1363 */     return visitor.end();
/* 1104:     */   }
/* 1105:     */   
/* 1106:     */   public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1107:     */   {
/* 1108:1371 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 1109:1372 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1110:1373 */     for (int iBlock = startRow / 36; iBlock < 1 + endRow / 36; iBlock++)
/* 1111:     */     {
/* 1112:1374 */       int p0 = iBlock * 36;
/* 1113:1375 */       int pStart = FastMath.max(startRow, p0);
/* 1114:1376 */       int pEnd = FastMath.min((iBlock + 1) * 36, 1 + endRow);
/* 1115:1377 */       for (int p = pStart; p < pEnd; p++) {
/* 1116:1378 */         for (int jBlock = startColumn / 36; jBlock < 1 + endColumn / 36; jBlock++)
/* 1117:     */         {
/* 1118:1379 */           int jWidth = blockWidth(jBlock);
/* 1119:1380 */           int q0 = jBlock * 36;
/* 1120:1381 */           int qStart = FastMath.max(startColumn, q0);
/* 1121:1382 */           int qEnd = FastMath.min((jBlock + 1) * 36, 1 + endColumn);
/* 1122:1383 */           T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1123:1384 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1124:1385 */           for (int q = qStart; q < qEnd; q++)
/* 1125:     */           {
/* 1126:1386 */             block[k] = visitor.visit(p, q, block[k]);
/* 1127:1387 */             k++;
/* 1128:     */           }
/* 1129:     */         }
/* 1130:     */       }
/* 1131:     */     }
/* 1132:1392 */     return visitor.end();
/* 1133:     */   }
/* 1134:     */   
/* 1135:     */   public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1136:     */   {
/* 1137:1400 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 1138:1401 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1139:1402 */     for (int iBlock = startRow / 36; iBlock < 1 + endRow / 36; iBlock++)
/* 1140:     */     {
/* 1141:1403 */       int p0 = iBlock * 36;
/* 1142:1404 */       int pStart = FastMath.max(startRow, p0);
/* 1143:1405 */       int pEnd = FastMath.min((iBlock + 1) * 36, 1 + endRow);
/* 1144:1406 */       for (int p = pStart; p < pEnd; p++) {
/* 1145:1407 */         for (int jBlock = startColumn / 36; jBlock < 1 + endColumn / 36; jBlock++)
/* 1146:     */         {
/* 1147:1408 */           int jWidth = blockWidth(jBlock);
/* 1148:1409 */           int q0 = jBlock * 36;
/* 1149:1410 */           int qStart = FastMath.max(startColumn, q0);
/* 1150:1411 */           int qEnd = FastMath.min((jBlock + 1) * 36, 1 + endColumn);
/* 1151:1412 */           T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1152:1413 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1153:1414 */           for (int q = qStart; q < qEnd; q++)
/* 1154:     */           {
/* 1155:1415 */             visitor.visit(p, q, block[k]);
/* 1156:1416 */             k++;
/* 1157:     */           }
/* 1158:     */         }
/* 1159:     */       }
/* 1160:     */     }
/* 1161:1421 */     return visitor.end();
/* 1162:     */   }
/* 1163:     */   
/* 1164:     */   public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor)
/* 1165:     */   {
/* 1166:1427 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1167:1428 */     int blockIndex = 0;
/* 1168:1429 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1169:     */     {
/* 1170:1430 */       int pStart = iBlock * 36;
/* 1171:1431 */       int pEnd = FastMath.min(pStart + 36, this.rows);
/* 1172:1432 */       for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1173:     */       {
/* 1174:1433 */         int qStart = jBlock * 36;
/* 1175:1434 */         int qEnd = FastMath.min(qStart + 36, this.columns);
/* 1176:1435 */         T[] block = this.blocks[blockIndex];
/* 1177:1436 */         int k = 0;
/* 1178:1437 */         for (int p = pStart; p < pEnd; p++) {
/* 1179:1438 */           for (int q = qStart; q < qEnd; q++)
/* 1180:     */           {
/* 1181:1439 */             block[k] = visitor.visit(p, q, block[k]);
/* 1182:1440 */             k++;
/* 1183:     */           }
/* 1184:     */         }
/* 1185:1443 */         blockIndex++;
/* 1186:     */       }
/* 1187:     */     }
/* 1188:1446 */     return visitor.end();
/* 1189:     */   }
/* 1190:     */   
/* 1191:     */   public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor)
/* 1192:     */   {
/* 1193:1452 */     visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
/* 1194:1453 */     int blockIndex = 0;
/* 1195:1454 */     for (int iBlock = 0; iBlock < this.blockRows; iBlock++)
/* 1196:     */     {
/* 1197:1455 */       int pStart = iBlock * 36;
/* 1198:1456 */       int pEnd = FastMath.min(pStart + 36, this.rows);
/* 1199:1457 */       for (int jBlock = 0; jBlock < this.blockColumns; jBlock++)
/* 1200:     */       {
/* 1201:1458 */         int qStart = jBlock * 36;
/* 1202:1459 */         int qEnd = FastMath.min(qStart + 36, this.columns);
/* 1203:1460 */         T[] block = this.blocks[blockIndex];
/* 1204:1461 */         int k = 0;
/* 1205:1462 */         for (int p = pStart; p < pEnd; p++) {
/* 1206:1463 */           for (int q = qStart; q < qEnd; q++)
/* 1207:     */           {
/* 1208:1464 */             visitor.visit(p, q, block[k]);
/* 1209:1465 */             k++;
/* 1210:     */           }
/* 1211:     */         }
/* 1212:1468 */         blockIndex++;
/* 1213:     */       }
/* 1214:     */     }
/* 1215:1471 */     return visitor.end();
/* 1216:     */   }
/* 1217:     */   
/* 1218:     */   public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1219:     */   {
/* 1220:1479 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 1221:1480 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1222:1481 */     for (int iBlock = startRow / 36; iBlock < 1 + endRow / 36; iBlock++)
/* 1223:     */     {
/* 1224:1482 */       int p0 = iBlock * 36;
/* 1225:1483 */       int pStart = FastMath.max(startRow, p0);
/* 1226:1484 */       int pEnd = FastMath.min((iBlock + 1) * 36, 1 + endRow);
/* 1227:1485 */       for (int jBlock = startColumn / 36; jBlock < 1 + endColumn / 36; jBlock++)
/* 1228:     */       {
/* 1229:1486 */         int jWidth = blockWidth(jBlock);
/* 1230:1487 */         int q0 = jBlock * 36;
/* 1231:1488 */         int qStart = FastMath.max(startColumn, q0);
/* 1232:1489 */         int qEnd = FastMath.min((jBlock + 1) * 36, 1 + endColumn);
/* 1233:1490 */         T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1234:1491 */         for (int p = pStart; p < pEnd; p++)
/* 1235:     */         {
/* 1236:1492 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1237:1493 */           for (int q = qStart; q < qEnd; q++)
/* 1238:     */           {
/* 1239:1494 */             block[k] = visitor.visit(p, q, block[k]);
/* 1240:1495 */             k++;
/* 1241:     */           }
/* 1242:     */         }
/* 1243:     */       }
/* 1244:     */     }
/* 1245:1500 */     return visitor.end();
/* 1246:     */   }
/* 1247:     */   
/* 1248:     */   public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 1249:     */   {
/* 1250:1508 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 1251:1509 */     visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
/* 1252:1510 */     for (int iBlock = startRow / 36; iBlock < 1 + endRow / 36; iBlock++)
/* 1253:     */     {
/* 1254:1511 */       int p0 = iBlock * 36;
/* 1255:1512 */       int pStart = FastMath.max(startRow, p0);
/* 1256:1513 */       int pEnd = FastMath.min((iBlock + 1) * 36, 1 + endRow);
/* 1257:1514 */       for (int jBlock = startColumn / 36; jBlock < 1 + endColumn / 36; jBlock++)
/* 1258:     */       {
/* 1259:1515 */         int jWidth = blockWidth(jBlock);
/* 1260:1516 */         int q0 = jBlock * 36;
/* 1261:1517 */         int qStart = FastMath.max(startColumn, q0);
/* 1262:1518 */         int qEnd = FastMath.min((jBlock + 1) * 36, 1 + endColumn);
/* 1263:1519 */         T[] block = this.blocks[(iBlock * this.blockColumns + jBlock)];
/* 1264:1520 */         for (int p = pStart; p < pEnd; p++)
/* 1265:     */         {
/* 1266:1521 */           int k = (p - p0) * jWidth + qStart - q0;
/* 1267:1522 */           for (int q = qStart; q < qEnd; q++)
/* 1268:     */           {
/* 1269:1523 */             visitor.visit(p, q, block[k]);
/* 1270:1524 */             k++;
/* 1271:     */           }
/* 1272:     */         }
/* 1273:     */       }
/* 1274:     */     }
/* 1275:1529 */     return visitor.end();
/* 1276:     */   }
/* 1277:     */   
/* 1278:     */   private int blockHeight(int blockRow)
/* 1279:     */   {
/* 1280:1538 */     return blockRow == this.blockRows - 1 ? this.rows - blockRow * 36 : 36;
/* 1281:     */   }
/* 1282:     */   
/* 1283:     */   private int blockWidth(int blockColumn)
/* 1284:     */   {
/* 1285:1547 */     return blockColumn == this.blockColumns - 1 ? this.columns - blockColumn * 36 : 36;
/* 1286:     */   }
/* 1287:     */
@Override
public FieldMatrix<T> preMultiply(FieldMatrix<T> paramFieldMatrix) {
	// TODO Auto-generated method stub
	return null;
} }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.BlockFieldMatrix
 * JD-Core Version:    0.7.0.1
 */
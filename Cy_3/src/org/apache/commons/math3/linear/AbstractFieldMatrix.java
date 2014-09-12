/*    1:     */ package org.apache.commons.math3.linear;
/*    2:     */ 
/*    3:     */ import java.lang.reflect.Array;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.Arrays;

/*    6:     */ import org.apache.commons.math3.Field;
/*    7:     */ import org.apache.commons.math3.FieldElement;
/*    8:     */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*    9:     */ import org.apache.commons.math3.exception.NoDataException;
/*   10:     */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   11:     */ import org.apache.commons.math3.exception.NullArgumentException;
/*   12:     */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   13:     */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   14:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   15:     */ 
/*   16:     */ public abstract class AbstractFieldMatrix<T extends FieldElement<T>>
/*   17:     */   implements FieldMatrix<T>
/*   18:     */ {
/*   19:     */   private final Field<T> field;
/*   20:     */   
/*   21:     */   protected AbstractFieldMatrix()
/*   22:     */   {
/*   23:  53 */     this.field = null;
/*   24:     */   }
/*   25:     */   
/*   26:     */   protected AbstractFieldMatrix(Field<T> field)
/*   27:     */   {
/*   28:  61 */     this.field = field;
/*   29:     */   }
/*   30:     */   
/*   31:     */   protected AbstractFieldMatrix(Field<T> field, int rowDimension, int columnDimension)
/*   32:     */   {
/*   33:  76 */     if (rowDimension <= 0) {
/*   34:  77 */       throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(rowDimension));
/*   35:     */     }
/*   36:  80 */     if (columnDimension <= 0) {
/*   37:  81 */       throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(columnDimension));
/*   38:     */     }
/*   39:  84 */     this.field = field;
/*   40:     */   }
/*   41:     */   
/*   42:     */   protected static <T extends FieldElement<T>> Field<T> extractField(T[][] d)
/*   43:     */   {
/*   44:  97 */     if (d == null) {
/*   45:  98 */       throw new NullArgumentException();
/*   46:     */     }
/*   47: 100 */     if (d.length == 0) {
/*   48: 101 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/*   49:     */     }
/*   50: 103 */     if (d[0].length == 0) {
/*   51: 104 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/*   52:     */     }
/*   53: 106 */     return d[0][0].getField();
/*   54:     */   }
/*   55:     */   
/*   56:     */   protected static <T extends FieldElement<T>> Field<T> extractField(T[] d)
/*   57:     */   {
/*   58: 118 */     if (d.length == 0) {
/*   59: 119 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/*   60:     */     }
/*   61: 121 */     return d[0].getField();
/*   62:     */   }
/*   63:     */   
/*   64:     */   protected static <T extends FieldElement<T>> T[][] buildArray(Field<T> field, int rows, int columns)
/*   65:     */   {
/*   66: 139 */     if (columns < 0)
/*   67:     */     {
/*   68: 140 */       T[] dummyRow = (T[])Array.newInstance(field.getRuntimeClass(), 0);
/*   69: 141 */       return (T[][])Array.newInstance(dummyRow.getClass(), rows);
/*   70:     */     }
/*   71: 143 */     T[][] array = (T[][])Array.newInstance(field.getRuntimeClass(), new int[] { rows, columns });
/*   72: 145 */     for (int i = 0; i < array.length; i++) {
/*   73: 146 */       Arrays.fill(array[i], field.getZero());
/*   74:     */     }
/*   75: 148 */     return array;
/*   76:     */   }
/*   77:     */   
/*   78:     */   protected static <T extends FieldElement<T>> T[] buildArray(Field<T> field, int length)
/*   79:     */   {
/*   80: 163 */     T[] array = (T[])Array.newInstance(field.getRuntimeClass(), length);
/*   81: 164 */     Arrays.fill(array, field.getZero());
/*   82: 165 */     return array;
/*   83:     */   }
/*   84:     */   
/*   85:     */   public Field<T> getField()
/*   86:     */   {
/*   87: 170 */     return this.field;
/*   88:     */   }
/*   89:     */   
/*   90:     */   public abstract FieldMatrix<T> createMatrix(int paramInt1, int paramInt2);
/*   91:     */   
/*   92:     */   public abstract FieldMatrix<T> copy();
/*   93:     */   
/*   94:     */   public FieldMatrix<T> add(FieldMatrix<T> m)
/*   95:     */   {
/*   96: 182 */     checkAdditionCompatible(m);
/*   97:     */     
/*   98: 184 */     int rowCount = getRowDimension();
/*   99: 185 */     int columnCount = getColumnDimension();
/*  100: 186 */     FieldMatrix<T> out = createMatrix(rowCount, columnCount);
/*  101: 187 */     for (int row = 0; row < rowCount; row++) {
/*  102: 188 */       for (int col = 0; col < columnCount; col++) {
/*  103: 189 */         out.setEntry(row, col, (T)getEntry(row, col).add(m.getEntry(row, col)));
/*  104:     */       }
/*  105:     */     }
/*  106: 193 */     return out;
/*  107:     */   }
/*  108:     */   
/*  109:     */   public FieldMatrix<T> subtract(FieldMatrix<T> m)
/*  110:     */   {
/*  111: 199 */     checkSubtractionCompatible(m);
/*  112:     */     
/*  113: 201 */     int rowCount = getRowDimension();
/*  114: 202 */     int columnCount = getColumnDimension();
/*  115: 203 */     FieldMatrix<T> out = createMatrix(rowCount, columnCount);
/*  116: 204 */     for (int row = 0; row < rowCount; row++) {
/*  117: 205 */       for (int col = 0; col < columnCount; col++) {
/*  118: 206 */         out.setEntry(row, col, (T)getEntry(row, col).subtract(m.getEntry(row, col)));
/*  119:     */       }
/*  120:     */     }
/*  121: 210 */     return out;
/*  122:     */   }
/*  123:     */   
/*  124:     */   public FieldMatrix<T> scalarAdd(T d)
/*  125:     */   {
/*  126: 216 */     int rowCount = getRowDimension();
/*  127: 217 */     int columnCount = getColumnDimension();
/*  128: 218 */     FieldMatrix<T> out = createMatrix(rowCount, columnCount);
/*  129: 219 */     for (int row = 0; row < rowCount; row++) {
/*  130: 220 */       for (int col = 0; col < columnCount; col++) {
/*  131: 221 */         out.setEntry(row, col, (T)getEntry(row, col).add(d));
/*  132:     */       }
/*  133:     */     }
/*  134: 225 */     return out;
/*  135:     */   }
/*  136:     */   
/*  137:     */   public FieldMatrix<T> scalarMultiply(T d)
/*  138:     */   {
/*  139: 230 */     int rowCount = getRowDimension();
/*  140: 231 */     int columnCount = getColumnDimension();
/*  141: 232 */     FieldMatrix<T> out = createMatrix(rowCount, columnCount);
/*  142: 233 */     for (int row = 0; row < rowCount; row++) {
/*  143: 234 */       for (int col = 0; col < columnCount; col++) {
/*  144: 235 */         out.setEntry(row, col, (T)getEntry(row, col).multiply(d));
/*  145:     */       }
/*  146:     */     }
/*  147: 239 */     return out;
/*  148:     */   }
/*  149:     */   
/*  150:     */   public FieldMatrix<T> multiply(FieldMatrix<T> m)
/*  151:     */   {
/*  152: 245 */     checkMultiplicationCompatible(m);
/*  153:     */     
/*  154: 247 */     int nRows = getRowDimension();
/*  155: 248 */     int nCols = m.getColumnDimension();
/*  156: 249 */     int nSum = getColumnDimension();
/*  157: 250 */     FieldMatrix<T> out = createMatrix(nRows, nCols);
/*  158: 251 */     for (int row = 0; row < nRows; row++) {
/*  159: 252 */       for (int col = 0; col < nCols; col++)
/*  160:     */       {
/*  161: 253 */         T sum = (T)this.field.getZero();
/*  162: 254 */         for (int i = 0; i < nSum; i++) {
/*  163: 255 */           sum = (T)sum.add(getEntry(row, i).multiply(m.getEntry(i, col)));
/*  164:     */         }
/*  165: 257 */         out.setEntry(row, col, sum);
/*  166:     */       }
/*  167:     */     }
/*  168: 261 */     return out;
/*  169:     */   }
/*  170:     */   
/*  171:     */   public FieldMatrix<T> preMultiply(Object fieldElements)
/*  172:     */   {
/*  173: 266 */     return ((FieldMatrix<T>) fieldElements).multiply(this);
/*  174:     */   }
/*  175:     */   
/*  176:     */   public FieldMatrix<T> power(int p)
/*  177:     */   {
/*  178: 271 */     if (p < 0) {
/*  179: 272 */       throw new IllegalArgumentException("p must be >= 0");
/*  180:     */     }
/*  181: 275 */     if (!isSquare()) {
/*  182: 276 */       throw new NonSquareMatrixException(getRowDimension(), getColumnDimension());
/*  183:     */     }
/*  184: 279 */     if (p == 0) {
/*  185: 280 */       return MatrixUtils.createFieldIdentityMatrix(getField(), getRowDimension());
/*  186:     */     }
/*  187: 283 */     if (p == 1) {
/*  188: 284 */       return copy();
/*  189:     */     }
/*  190: 287 */     int power = p - 1;
/*  191:     */     
/*  192:     */ 
/*  193:     */ 
/*  194:     */ 
/*  195:     */ 
/*  196:     */ 
/*  197:     */ 
/*  198:     */ 
/*  199: 296 */     char[] binaryRepresentation = Integer.toBinaryString(power).toCharArray();
/*  200:     */     
/*  201: 298 */     ArrayList<Integer> nonZeroPositions = new ArrayList();
/*  202: 300 */     for (int i = 0; i < binaryRepresentation.length; i++) {
/*  203: 301 */       if (binaryRepresentation[i] == '1')
/*  204:     */       {
/*  205: 302 */         int pos = binaryRepresentation.length - i - 1;
/*  206: 303 */         nonZeroPositions.add(Integer.valueOf(pos));
/*  207:     */       }
/*  208:     */     }
/*  209: 307 */     ArrayList<FieldMatrix<T>> results = new ArrayList(binaryRepresentation.length);
/*  210:     */     
/*  211:     */ 
/*  212: 310 */     results.add(0, copy());
/*  213: 312 */     for (int i = 1; i < binaryRepresentation.length; i++)
/*  214:     */     {
/*  215: 313 */       FieldMatrix<T> s = (FieldMatrix)results.get(i - 1);
/*  216: 314 */       FieldMatrix<T> r = s.multiply(s);
/*  217: 315 */       results.add(i, r);
/*  218:     */     }
/*  219: 318 */     FieldMatrix<T> result = copy();
/*  220: 320 */     for (Integer i : nonZeroPositions) {
/*  221: 321 */       result = result.multiply((FieldMatrix)results.get(i.intValue()));
/*  222:     */     }
/*  223: 324 */     return result;
/*  224:     */   }
/*  225:     */   
/*  226:     */   public T[][] getData()
/*  227:     */   {
/*  228: 329 */     T[][] data = buildArray(this.field, getRowDimension(), getColumnDimension());
/*  229: 331 */     for (int i = 0; i < data.length; i++)
/*  230:     */     {
/*  231: 332 */       T[] dataI = data[i];
/*  232: 333 */       for (int j = 0; j < dataI.length; j++) {
/*  233: 334 */         dataI[j] = getEntry(i, j);
/*  234:     */       }
/*  235:     */     }
/*  236: 338 */     return data;
/*  237:     */   }
/*  238:     */   
/*  239:     */   public FieldMatrix<T> getSubMatrix(int startRow, int endRow, int startColumn, int endColumn)
/*  240:     */   {
/*  241: 344 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/*  242:     */     
/*  243: 346 */     FieldMatrix<T> subMatrix = createMatrix(endRow - startRow + 1, endColumn - startColumn + 1);
/*  244: 348 */     for (int i = startRow; i <= endRow; i++) {
/*  245: 349 */       for (int j = startColumn; j <= endColumn; j++) {
/*  246: 350 */         subMatrix.setEntry(i - startRow, j - startColumn, getEntry(i, j));
/*  247:     */       }
/*  248:     */     }
/*  249: 354 */     return subMatrix;
/*  250:     */   }
/*  251:     */   
/*  252:     */   public FieldMatrix<T> getSubMatrix(final int[] selectedRows, final int[] selectedColumns)
/*  253:     */   {
/*  254: 363 */     checkSubMatrixIndex(selectedRows, selectedColumns);
/*  255:     */     
/*  256:     */ 
/*  257: 366 */     FieldMatrix<T> subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
/*  258:     */     
/*  259: 368 */     subMatrix.walkInOptimizedOrder(new DefaultFieldMatrixChangingVisitor((FieldElement)this.field.getZero())
/*  260:     */     {
/*  261:     */       public T visit2(int row, int column, T value)
/*  262:     */       {
/*  263: 373 */         return AbstractFieldMatrix.this.getEntry(selectedRows[row], selectedColumns[column]);
/*  264:     */       }
/*  265: 377 */     });
/*  266: 378 */     return subMatrix;
/*  267:     */   }
/*  268:     */   
/*  269:     */   public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, final T[][] destination)
/*  270:     */   {
/*  271: 387 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/*  272: 388 */     int rowsCount = endRow + 1 - startRow;
/*  273: 389 */     int columnsCount = endColumn + 1 - startColumn;
/*  274: 390 */     if ((destination.length < rowsCount) || (destination[0].length < columnsCount)) {
/*  275: 391 */       throw new MatrixDimensionMismatchException(destination.length, destination[0].length, rowsCount, columnsCount);
/*  276:     */     }
/*  277: 398 */     walkInOptimizedOrder(new DefaultFieldMatrixPreservingVisitor((FieldElement)this.field.getZero())
/*  278:     */     {
/*  279:     */       private int startRow;
/*  280:     */       private int startColumn;
/*  281:     */       
/*  282:     */       public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn)
/*  283:     */       {
/*  284: 411 */         this.startRow = startRow;
/*  285: 412 */         this.startColumn = startColumn;
/*  286:     */       }
/*  287:     */       
/*  288:     */       public void visit1(int row, int column, T value)
/*  289:     */       {
/*  290: 418 */         destination[(row - this.startRow)][(column - this.startColumn)] = value;
/*  291:     */       }
/*  292: 418 */     }, startRow, endRow, startColumn, endColumn);
/*  293:     */   }
/*  294:     */   
/*  295:     */   public void copySubMatrix(int[] selectedRows, int[] selectedColumns, T[][] destination)
/*  296:     */   {
/*  297: 428 */     checkSubMatrixIndex(selectedRows, selectedColumns);
/*  298: 429 */     if ((destination.length < selectedRows.length) || (destination[0].length < selectedColumns.length)) {
/*  299: 431 */       throw new MatrixDimensionMismatchException(destination.length, destination[0].length, selectedRows.length, selectedColumns.length);
/*  300:     */     }
/*  301: 438 */     for (int i = 0; i < selectedRows.length; i++)
/*  302:     */     {
/*  303: 439 */       T[] destinationI = destination[i];
/*  304: 440 */       for (int j = 0; j < selectedColumns.length; j++) {
/*  305: 441 */         destinationI[j] = getEntry(selectedRows[i], selectedColumns[j]);
/*  306:     */       }
/*  307:     */     }
/*  308:     */   }
/*  309:     */   
/*  310:     */   public void setSubMatrix(T[][] subMatrix, int row, int column)
/*  311:     */   {
/*  312: 449 */     if (subMatrix == null) {
/*  313: 450 */       throw new NullArgumentException();
/*  314:     */     }
/*  315: 452 */     int nRows = subMatrix.length;
/*  316: 453 */     if (nRows == 0) {
/*  317: 454 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/*  318:     */     }
/*  319: 457 */     int nCols = subMatrix[0].length;
/*  320: 458 */     if (nCols == 0) {
/*  321: 459 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/*  322:     */     }
/*  323: 462 */     for (int r = 1; r < nRows; r++) {
/*  324: 463 */       if (subMatrix[r].length != nCols) {
/*  325: 464 */         throw new DimensionMismatchException(nCols, subMatrix[r].length);
/*  326:     */       }
/*  327:     */     }
/*  328: 468 */     checkRowIndex(row);
/*  329: 469 */     checkColumnIndex(column);
/*  330: 470 */     checkRowIndex(nRows + row - 1);
/*  331: 471 */     checkColumnIndex(nCols + column - 1);
/*  332: 473 */     for (int i = 0; i < nRows; i++) {
/*  333: 474 */       for (int j = 0; j < nCols; j++) {
/*  334: 475 */         setEntry(row + i, column + j, subMatrix[i][j]);
/*  335:     */       }
/*  336:     */     }
/*  337:     */   }
/*  338:     */   
/*  339:     */   public FieldMatrix<T> getRowMatrix(int row)
/*  340:     */   {
/*  341: 482 */     checkRowIndex(row);
/*  342: 483 */     int nCols = getColumnDimension();
/*  343: 484 */     FieldMatrix<T> out = createMatrix(1, nCols);
/*  344: 485 */     for (int i = 0; i < nCols; i++) {
/*  345: 486 */       out.setEntry(0, i, getEntry(row, i));
/*  346:     */     }
/*  347: 489 */     return out;
/*  348:     */   }
/*  349:     */   
/*  350:     */   public void setRowMatrix(int row, FieldMatrix<T> matrix)
/*  351:     */   {
/*  352: 495 */     checkRowIndex(row);
/*  353: 496 */     int nCols = getColumnDimension();
/*  354: 497 */     if ((matrix.getRowDimension() != 1) || (matrix.getColumnDimension() != nCols)) {
/*  355: 499 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
/*  356:     */     }
/*  357: 503 */     for (int i = 0; i < nCols; i++) {
/*  358: 504 */       setEntry(row, i, matrix.getEntry(0, i));
/*  359:     */     }
/*  360:     */   }
/*  361:     */   
/*  362:     */   public FieldMatrix<T> getColumnMatrix(int column)
/*  363:     */   {
/*  364: 512 */     checkColumnIndex(column);
/*  365: 513 */     int nRows = getRowDimension();
/*  366: 514 */     FieldMatrix<T> out = createMatrix(nRows, 1);
/*  367: 515 */     for (int i = 0; i < nRows; i++) {
/*  368: 516 */       out.setEntry(i, 0, getEntry(i, column));
/*  369:     */     }
/*  370: 519 */     return out;
/*  371:     */   }
/*  372:     */   
/*  373:     */   public void setColumnMatrix(int column, FieldMatrix<T> matrix)
/*  374:     */   {
/*  375: 525 */     checkColumnIndex(column);
/*  376: 526 */     int nRows = getRowDimension();
/*  377: 527 */     if ((matrix.getRowDimension() != nRows) || (matrix.getColumnDimension() != 1)) {
/*  378: 529 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
/*  379:     */     }
/*  380: 533 */     for (int i = 0; i < nRows; i++) {
/*  381: 534 */       setEntry(i, column, matrix.getEntry(i, 0));
/*  382:     */     }
/*  383:     */   }
/*  384:     */   
/*  385:     */   public FieldVector<T> getRowVector(int row)
/*  386:     */   {
/*  387: 541 */     return new ArrayFieldVector(this.field, getRow(row), false);
/*  388:     */   }
/*  389:     */   
/*  390:     */   public void setRowVector(int row, FieldVector<T> vector)
/*  391:     */   {
/*  392: 546 */     checkRowIndex(row);
/*  393: 547 */     int nCols = getColumnDimension();
/*  394: 548 */     if (vector.getDimension() != nCols) {
/*  395: 549 */       throw new MatrixDimensionMismatchException(1, vector.getDimension(), 1, nCols);
/*  396:     */     }
/*  397: 552 */     for (int i = 0; i < nCols; i++) {
/*  398: 553 */       setEntry(row, i, vector.getEntry(i));
/*  399:     */     }
/*  400:     */   }
/*  401:     */   
/*  402:     */   public FieldVector<T> getColumnVector(int column)
/*  403:     */   {
/*  404: 560 */     return new ArrayFieldVector(this.field, getColumn(column), false);
/*  405:     */   }
/*  406:     */   
/*  407:     */   public void setColumnVector(int column, FieldVector<T> vector)
/*  408:     */   {
/*  409: 565 */     checkColumnIndex(column);
/*  410: 566 */     int nRows = getRowDimension();
/*  411: 567 */     if (vector.getDimension() != nRows) {
/*  412: 568 */       throw new MatrixDimensionMismatchException(vector.getDimension(), 1, nRows, 1);
/*  413:     */     }
/*  414: 571 */     for (int i = 0; i < nRows; i++) {
/*  415: 572 */       setEntry(i, column, vector.getEntry(i));
/*  416:     */     }
/*  417:     */   }
/*  418:     */   
/*  419:     */   public T[] getRow(int row)
/*  420:     */   {
/*  421: 579 */     checkRowIndex(row);
/*  422: 580 */     int nCols = getColumnDimension();
/*  423: 581 */     T[] out = buildArray(this.field, nCols);
/*  424: 582 */     for (int i = 0; i < nCols; i++) {
/*  425: 583 */       out[i] = getEntry(row, i);
/*  426:     */     }
/*  427: 586 */     return out;
/*  428:     */   }
/*  429:     */   
/*  430:     */   public void setRow(int row, T[] array)
/*  431:     */   {
/*  432: 592 */     checkRowIndex(row);
/*  433: 593 */     int nCols = getColumnDimension();
/*  434: 594 */     if (array.length != nCols) {
/*  435: 595 */       throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
/*  436:     */     }
/*  437: 597 */     for (int i = 0; i < nCols; i++) {
/*  438: 598 */       setEntry(row, i, array[i]);
/*  439:     */     }
/*  440:     */   }
/*  441:     */   
/*  442:     */   public T[] getColumn(int column)
/*  443:     */   {
/*  444: 605 */     checkColumnIndex(column);
/*  445: 606 */     int nRows = getRowDimension();
/*  446: 607 */     T[] out = buildArray(this.field, nRows);
/*  447: 608 */     for (int i = 0; i < nRows; i++) {
/*  448: 609 */       out[i] = getEntry(i, column);
/*  449:     */     }
/*  450: 612 */     return out;
/*  451:     */   }
/*  452:     */   
/*  453:     */   public void setColumn(int column, T[] array)
/*  454:     */   {
/*  455: 618 */     checkColumnIndex(column);
/*  456: 619 */     int nRows = getRowDimension();
/*  457: 620 */     if (array.length != nRows) {
/*  458: 621 */       throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
/*  459:     */     }
/*  460: 623 */     for (int i = 0; i < nRows; i++) {
/*  461: 624 */       setEntry(i, column, array[i]);
/*  462:     */     }
/*  463:     */   }
/*  464:     */   
/*  465:     */   public abstract T getEntry(int paramInt1, int paramInt2);
/*  466:     */   
/*  467:     */   public abstract void setEntry(int paramInt1, int paramInt2, T paramT);
/*  468:     */   
/*  469:     */   public abstract void addToEntry(int paramInt1, int paramInt2, T paramT);
/*  470:     */   
/*  471:     */   public abstract void multiplyEntry(int paramInt1, int paramInt2, T paramT);
/*  472:     */   
/*  473:     */   public FieldMatrix<T> transpose()
/*  474:     */   {
/*  475: 642 */     int nRows = getRowDimension();
/*  476: 643 */     int nCols = getColumnDimension();
/*  477: 644 */     final FieldMatrix<T> out = createMatrix(nCols, nRows);
/*  478: 645 */     walkInOptimizedOrder(new DefaultFieldMatrixPreservingVisitor((FieldElement)this.field.getZero())
/*  479:     */     {
/*  480:     */       public void visit3(int row, int column, T value)
/*  481:     */       {
/*  482: 649 */         out.setEntry(column, row, value);
/*  483:     */       }
/*  484: 652 */     });
/*  485: 653 */     return out;
/*  486:     */   }
/*  487:     */   
/*  488:     */   public boolean isSquare()
/*  489:     */   {
/*  490: 658 */     return getColumnDimension() == getRowDimension();
/*  491:     */   }
/*  492:     */   
/*  493:     */   public abstract int getRowDimension();
/*  494:     */   
/*  495:     */   public abstract int getColumnDimension();
/*  496:     */   
/*  497:     */   public T getTrace()
/*  498:     */   {
/*  499: 669 */     int nRows = getRowDimension();
/*  500: 670 */     int nCols = getColumnDimension();
/*  501: 671 */     if (nRows != nCols) {
/*  502: 672 */       throw new NonSquareMatrixException(nRows, nCols);
/*  503:     */     }
/*  504: 674 */     T trace = (T)this.field.getZero();
/*  505: 675 */     for (int i = 0; i < nRows; i++) {
/*  506: 676 */       trace = (T)trace.add(getEntry(i, i));
/*  507:     */     }
/*  508: 678 */     return trace;
/*  509:     */   }
/*  510:     */   
/*  511:     */   public T[] operate(T[] v)
/*  512:     */   {
/*  513: 684 */     int nRows = getRowDimension();
/*  514: 685 */     int nCols = getColumnDimension();
/*  515: 686 */     if (v.length != nCols) {
/*  516: 687 */       throw new DimensionMismatchException(v.length, nCols);
/*  517:     */     }
/*  518: 690 */     T[] out = buildArray(this.field, nRows);
/*  519: 691 */     for (int row = 0; row < nRows; row++)
/*  520:     */     {
/*  521: 692 */       T sum = (T)this.field.getZero();
/*  522: 693 */       for (int i = 0; i < nCols; i++) {
/*  523: 694 */         sum = (T)sum.add(getEntry(row, i).multiply(v[i]));
/*  524:     */       }
/*  525: 696 */       out[row] = sum;
/*  526:     */     }
/*  527: 699 */     return out;
/*  528:     */   }
/*  529:     */   
/*  530:     */   public FieldVector<T> operate(FieldVector<T> v)
/*  531:     */   {
/*  532:     */     try
/*  533:     */     {
/*  534: 705 */       return new ArrayFieldVector(this.field, operate((T[]) ((ArrayFieldVector)v).getDataRef()), false);
/*  535:     */     }
/*  536:     */     catch (ClassCastException cce)
/*  537:     */     {
/*  538: 707 */       int nRows = getRowDimension();
/*  539: 708 */       int nCols = getColumnDimension();
/*  540: 709 */       if (v.getDimension() != nCols) {
/*  541: 710 */         throw new DimensionMismatchException(v.getDimension(), nCols);
/*  542:     */       }
/*  543: 713 */       T[] out = buildArray(this.field, nRows);
/*  544: 714 */       for (int row = 0; row < nRows; row++)
/*  545:     */       {
/*  546: 715 */         T sum = (T)this.field.getZero();
/*  547: 716 */         for (int i = 0; i < nCols; i++) {
/*  548: 717 */           sum = (T)sum.add(getEntry(row, i).multiply(v.getEntry(i)));
/*  549:     */         }
/*  550: 719 */         out[row] = sum;
/*  551:     */       }
/*  552: 722 */       return new ArrayFieldVector(this.field, out, false);
/*  553:     */     }
/*  554:     */   }
/*  555:     */   
/*  556:     */   public T[] preMultiply2(T[] v)
/*  557:     */   {
/*  558: 729 */     int nRows = getRowDimension();
/*  559: 730 */     int nCols = getColumnDimension();
/*  560: 731 */     if (v.length != nRows) {
/*  561: 732 */       throw new DimensionMismatchException(v.length, nRows);
/*  562:     */     }
/*  563: 735 */     T[] out = buildArray(this.field, nCols);
/*  564: 736 */     for (int col = 0; col < nCols; col++)
/*  565:     */     {
/*  566: 737 */       T sum = (T)this.field.getZero();
/*  567: 738 */       for (int i = 0; i < nRows; i++) {
/*  568: 739 */         sum = (T)sum.add(getEntry(i, col).multiply(v[i]));
/*  569:     */       }
/*  570: 741 */       out[col] = sum;
/*  571:     */     }
/*  572: 744 */     return out;
/*  573:     */   }
/*  574:     */   
/*  575:     */   public FieldVector<T> preMultiply(FieldVector<T> v)
/*  576:     */   {
/*  577:     */     try
/*  578:     */     {
/*  579: 750 */       return new ArrayFieldVector(this.field);
/*  580:     */     }
/*  581:     */     catch (ClassCastException cce)
/*  582:     */     {
/*  583: 752 */       int nRows = getRowDimension();
/*  584: 753 */       int nCols = getColumnDimension();
/*  585: 754 */       if (v.getDimension() != nRows) {
/*  586: 755 */         throw new DimensionMismatchException(v.getDimension(), nRows);
/*  587:     */       }
/*  588: 758 */       T[] out = buildArray(this.field, nCols);
/*  589: 759 */       for (int col = 0; col < nCols; col++)
/*  590:     */       {
/*  591: 760 */         T sum = (T)this.field.getZero();
/*  592: 761 */         for (int i = 0; i < nRows; i++) {
/*  593: 762 */           sum = (T)sum.add(getEntry(i, col).multiply(v.getEntry(i)));
/*  594:     */         }
/*  595: 764 */         out[col] = sum;
/*  596:     */       }
/*  597: 767 */       return new ArrayFieldVector(this.field, out, false);
/*  598:     */     }
/*  599:     */   }
@SuppressWarnings("rawtypes")
private FieldElement[] preMultiply1(FieldElement[] dataRef) {
	// TODO Auto-generated method stub
	return null;
}

/*  600:     */   
/*  601:     */   public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor)
/*  602:     */   {
/*  603: 773 */     int rows = getRowDimension();
/*  604: 774 */     int columns = getColumnDimension();
/*  605: 775 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/*  606: 776 */     for (int row = 0; row < rows; row++) {
/*  607: 777 */       for (int column = 0; column < columns; column++)
/*  608:     */       {
/*  609: 778 */         T oldValue = getEntry(row, column);
/*  610: 779 */         T newValue = visitor.visit(row, column, oldValue);
/*  611: 780 */         setEntry(row, column, newValue);
/*  612:     */       }
/*  613:     */     }
/*  614: 783 */     return visitor.end();
/*  615:     */   }
/*  616:     */   
/*  617:     */   public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor)
/*  618:     */   {
/*  619: 788 */     int rows = getRowDimension();
/*  620: 789 */     int columns = getColumnDimension();
/*  621: 790 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/*  622: 791 */     for (int row = 0; row < rows; row++) {
/*  623: 792 */       for (int column = 0; column < columns; column++) {
/*  624: 793 */         visitor.visit(row, column, getEntry(row, column));
/*  625:     */       }
/*  626:     */     }
/*  627: 796 */     return visitor.end();
/*  628:     */   }
/*  629:     */   
/*  630:     */   public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/*  631:     */   {
/*  632: 803 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/*  633: 804 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/*  634: 806 */     for (int row = startRow; row <= endRow; row++) {
/*  635: 807 */       for (int column = startColumn; column <= endColumn; column++)
/*  636:     */       {
/*  637: 808 */         T oldValue = getEntry(row, column);
/*  638: 809 */         T newValue = visitor.visit(row, column, oldValue);
/*  639: 810 */         setEntry(row, column, newValue);
/*  640:     */       }
/*  641:     */     }
/*  642: 813 */     return visitor.end();
/*  643:     */   }
/*  644:     */   
/*  645:     */   public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/*  646:     */   {
/*  647: 820 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/*  648: 821 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/*  649: 823 */     for (int row = startRow; row <= endRow; row++) {
/*  650: 824 */       for (int column = startColumn; column <= endColumn; column++) {
/*  651: 825 */         visitor.visit(row, column, getEntry(row, column));
/*  652:     */       }
/*  653:     */     }
/*  654: 828 */     return visitor.end();
/*  655:     */   }
/*  656:     */   
/*  657:     */   public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor)
/*  658:     */   {
/*  659: 833 */     int rows = getRowDimension();
/*  660: 834 */     int columns = getColumnDimension();
/*  661: 835 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/*  662: 836 */     for (int column = 0; column < columns; column++) {
/*  663: 837 */       for (int row = 0; row < rows; row++)
/*  664:     */       {
/*  665: 838 */         T oldValue = getEntry(row, column);
/*  666: 839 */         T newValue = visitor.visit(row, column, oldValue);
/*  667: 840 */         setEntry(row, column, newValue);
/*  668:     */       }
/*  669:     */     }
/*  670: 843 */     return visitor.end();
/*  671:     */   }
/*  672:     */   
/*  673:     */   public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor)
/*  674:     */   {
/*  675: 848 */     int rows = getRowDimension();
/*  676: 849 */     int columns = getColumnDimension();
/*  677: 850 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/*  678: 851 */     for (int column = 0; column < columns; column++) {
/*  679: 852 */       for (int row = 0; row < rows; row++) {
/*  680: 853 */         visitor.visit(row, column, getEntry(row, column));
/*  681:     */       }
/*  682:     */     }
/*  683: 856 */     return visitor.end();
/*  684:     */   }
/*  685:     */   
/*  686:     */   public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/*  687:     */   {
/*  688: 863 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/*  689: 864 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/*  690: 866 */     for (int column = startColumn; column <= endColumn; column++) {
/*  691: 867 */       for (int row = startRow; row <= endRow; row++)
/*  692:     */       {
/*  693: 868 */         T oldValue = getEntry(row, column);
/*  694: 869 */         T newValue = visitor.visit(row, column, oldValue);
/*  695: 870 */         setEntry(row, column, newValue);
/*  696:     */       }
/*  697:     */     }
/*  698: 873 */     return visitor.end();
/*  699:     */   }
/*  700:     */   
/*  701:     */   public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/*  702:     */   {
/*  703: 880 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/*  704: 881 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/*  705: 883 */     for (int column = startColumn; column <= endColumn; column++) {
/*  706: 884 */       for (int row = startRow; row <= endRow; row++) {
/*  707: 885 */         visitor.visit(row, column, getEntry(row, column));
/*  708:     */       }
/*  709:     */     }
/*  710: 888 */     return visitor.end();
/*  711:     */   }
/*  712:     */   
/*  713:     */   public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor)
/*  714:     */   {
/*  715: 893 */     return walkInRowOrder(visitor);
/*  716:     */   }
/*  717:     */   
/*  718:     */   public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor)
/*  719:     */   {
/*  720: 898 */     return walkInRowOrder(visitor);
/*  721:     */   }
/*  722:     */   
/*  723:     */   public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/*  724:     */   {
/*  725: 905 */     return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
/*  726:     */   }
/*  727:     */   
/*  728:     */   public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/*  729:     */   {
/*  730: 912 */     return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
/*  731:     */   }
/*  732:     */   
/*  733:     */   public String toString()
/*  734:     */   {
/*  735: 921 */     int nRows = getRowDimension();
/*  736: 922 */     int nCols = getColumnDimension();
/*  737: 923 */     StringBuffer res = new StringBuffer();
/*  738: 924 */     String fullClassName = getClass().getName();
/*  739: 925 */     String shortClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
/*  740: 926 */     res.append(shortClassName).append("{");
/*  741: 928 */     for (int i = 0; i < nRows; i++)
/*  742:     */     {
/*  743: 929 */       if (i > 0) {
/*  744: 930 */         res.append(",");
/*  745:     */       }
/*  746: 932 */       res.append("{");
/*  747: 933 */       for (int j = 0; j < nCols; j++)
/*  748:     */       {
/*  749: 934 */         if (j > 0) {
/*  750: 935 */           res.append(",");
/*  751:     */         }
/*  752: 937 */         res.append(getEntry(i, j));
/*  753:     */       }
/*  754: 939 */       res.append("}");
/*  755:     */     }
/*  756: 942 */     res.append("}");
/*  757: 943 */     return res.toString();
/*  758:     */   }
/*  759:     */   
/*  760:     */   public boolean equals(Object object)
/*  761:     */   {
/*  762: 956 */     if (object == this) {
/*  763: 957 */       return true;
/*  764:     */     }
/*  765: 959 */     if (!(object instanceof FieldMatrix)) {
/*  766: 960 */       return false;
/*  767:     */     }
/*  768: 962 */     FieldMatrix<?> m = (FieldMatrix)object;
/*  769: 963 */     int nRows = getRowDimension();
/*  770: 964 */     int nCols = getColumnDimension();
/*  771: 965 */     if ((m.getColumnDimension() != nCols) || (m.getRowDimension() != nRows)) {
/*  772: 966 */       return false;
/*  773:     */     }
/*  774: 968 */     for (int row = 0; row < nRows; row++) {
/*  775: 969 */       for (int col = 0; col < nCols; col++) {
/*  776: 970 */         if (!getEntry(row, col).equals(m.getEntry(row, col))) {
/*  777: 971 */           return false;
/*  778:     */         }
/*  779:     */       }
/*  780:     */     }
/*  781: 975 */     return true;
/*  782:     */   }
/*  783:     */   
/*  784:     */   public int hashCode()
/*  785:     */   {
/*  786: 985 */     int ret = 322562;
/*  787: 986 */     int nRows = getRowDimension();
/*  788: 987 */     int nCols = getColumnDimension();
/*  789: 988 */     ret = ret * 31 + nRows;
/*  790: 989 */     ret = ret * 31 + nCols;
/*  791: 990 */     for (int row = 0; row < nRows; row++) {
/*  792: 991 */       for (int col = 0; col < nCols; col++) {
/*  793: 992 */         ret = ret * 31 + (11 * (row + 1) + 17 * (col + 1)) * getEntry(row, col).hashCode();
/*  794:     */       }
/*  795:     */     }
/*  796: 995 */     return ret;
/*  797:     */   }
/*  798:     */   
/*  799:     */   protected void checkRowIndex(int row)
/*  800:     */   {
/*  801:1005 */     if ((row < 0) || (row >= getRowDimension())) {
/*  802:1006 */       throw new OutOfRangeException(LocalizedFormats.ROW_INDEX, Integer.valueOf(row), Integer.valueOf(0), Integer.valueOf(getRowDimension() - 1));
/*  803:     */     }
/*  804:     */   }
/*  805:     */   
/*  806:     */   protected void checkColumnIndex(int column)
/*  807:     */   {
/*  808:1018 */     if ((column < 0) || (column >= getColumnDimension())) {
/*  809:1019 */       throw new OutOfRangeException(LocalizedFormats.COLUMN_INDEX, Integer.valueOf(column), Integer.valueOf(0), Integer.valueOf(getColumnDimension() - 1));
/*  810:     */     }
/*  811:     */   }
/*  812:     */   
/*  813:     */   protected void checkSubMatrixIndex(int startRow, int endRow, int startColumn, int endColumn)
/*  814:     */   {
/*  815:1038 */     checkRowIndex(startRow);
/*  816:1039 */     checkRowIndex(endRow);
/*  817:1040 */     if (endRow < startRow) {
/*  818:1041 */       throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(endRow), Integer.valueOf(startRow), true);
/*  819:     */     }
/*  820:1045 */     checkColumnIndex(startColumn);
/*  821:1046 */     checkColumnIndex(endColumn);
/*  822:1047 */     if (endColumn < startColumn) {
/*  823:1048 */       throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, Integer.valueOf(endColumn), Integer.valueOf(startColumn), true);
/*  824:     */     }
/*  825:     */   }
/*  826:     */   
/*  827:     */   protected void checkSubMatrixIndex(int[] selectedRows, int[] selectedColumns)
/*  828:     */   {
/*  829:1064 */     if ((selectedRows == null) || (selectedColumns == null)) {
/*  830:1066 */       throw new NullArgumentException();
/*  831:     */     }
/*  832:1068 */     if ((selectedRows.length == 0) || (selectedColumns.length == 0)) {
/*  833:1070 */       throw new NoDataException();
/*  834:     */     }
/*  835:1073 */     for (int row : selectedRows) {
/*  836:1074 */       checkRowIndex(row);
/*  837:     */     }
/*  838:1076 */     for (int column : selectedColumns) {
/*  839:1077 */       checkColumnIndex(column);
/*  840:     */     }
/*  841:     */   }
/*  842:     */   
/*  843:     */   protected void checkAdditionCompatible(FieldMatrix<T> m)
/*  844:     */   {
/*  845:1089 */     if ((getRowDimension() != m.getRowDimension()) || (getColumnDimension() != m.getColumnDimension())) {
/*  846:1091 */       throw new MatrixDimensionMismatchException(m.getRowDimension(), m.getColumnDimension(), getRowDimension(), getColumnDimension());
/*  847:     */     }
/*  848:     */   }
/*  849:     */   
/*  850:     */   protected void checkSubtractionCompatible(FieldMatrix<T> m)
/*  851:     */   {
/*  852:1104 */     if ((getRowDimension() != m.getRowDimension()) || (getColumnDimension() != m.getColumnDimension())) {
/*  853:1106 */       throw new MatrixDimensionMismatchException(m.getRowDimension(), m.getColumnDimension(), getRowDimension(), getColumnDimension());
/*  854:     */     }
/*  855:     */   }
/*  856:     */   
/*  857:     */   protected void checkMultiplicationCompatible(FieldMatrix<T> m)
/*  858:     */   {
/*  859:1119 */     if (getColumnDimension() != m.getRowDimension()) {
/*  860:1120 */       throw new DimensionMismatchException(m.getRowDimension(), getColumnDimension());
/*  861:     */     }
/*  862:     */   }
/*  863:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.AbstractFieldMatrix
 * JD-Core Version:    0.7.0.1
 */
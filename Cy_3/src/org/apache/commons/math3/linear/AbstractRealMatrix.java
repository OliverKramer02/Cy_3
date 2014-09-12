/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NoDataException;
/*   6:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ import org.apache.commons.math3.util.MathUtils;
/*  11:    */ 
/*  12:    */ public abstract class AbstractRealMatrix
/*  13:    */   extends RealLinearOperator
/*  14:    */   implements RealMatrix
/*  15:    */ {
/*  16:    */   protected AbstractRealMatrix() {}
/*  17:    */   
/*  18:    */   protected AbstractRealMatrix(int rowDimension, int columnDimension)
/*  19:    */   {
/*  20: 54 */     if (rowDimension < 1) {
/*  21: 55 */       throw new NotStrictlyPositiveException(Integer.valueOf(rowDimension));
/*  22:    */     }
/*  23: 57 */     if (columnDimension < 1) {
/*  24: 58 */       throw new NotStrictlyPositiveException(Integer.valueOf(columnDimension));
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public abstract RealMatrix createMatrix(int paramInt1, int paramInt2);
/*  29:    */   
/*  30:    */   public abstract RealMatrix copy();
/*  31:    */   
/*  32:    */   public RealMatrix add(RealMatrix m)
/*  33:    */   {
/*  34: 71 */     MatrixUtils.checkAdditionCompatible(this, m);
/*  35:    */     
/*  36: 73 */     int rowCount = getRowDimension();
/*  37: 74 */     int columnCount = getColumnDimension();
/*  38: 75 */     RealMatrix out = createMatrix(rowCount, columnCount);
/*  39: 76 */     for (int row = 0; row < rowCount; row++) {
/*  40: 77 */       for (int col = 0; col < columnCount; col++) {
/*  41: 78 */         out.setEntry(row, col, getEntry(row, col) + m.getEntry(row, col));
/*  42:    */       }
/*  43:    */     }
/*  44: 82 */     return out;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public RealMatrix subtract(RealMatrix m)
/*  48:    */   {
/*  49: 88 */     MatrixUtils.checkSubtractionCompatible(this, m);
/*  50:    */     
/*  51: 90 */     int rowCount = getRowDimension();
/*  52: 91 */     int columnCount = getColumnDimension();
/*  53: 92 */     RealMatrix out = createMatrix(rowCount, columnCount);
/*  54: 93 */     for (int row = 0; row < rowCount; row++) {
/*  55: 94 */       for (int col = 0; col < columnCount; col++) {
/*  56: 95 */         out.setEntry(row, col, getEntry(row, col) - m.getEntry(row, col));
/*  57:    */       }
/*  58:    */     }
/*  59: 99 */     return out;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public RealMatrix scalarAdd(double d)
/*  63:    */   {
/*  64:104 */     int rowCount = getRowDimension();
/*  65:105 */     int columnCount = getColumnDimension();
/*  66:106 */     RealMatrix out = createMatrix(rowCount, columnCount);
/*  67:107 */     for (int row = 0; row < rowCount; row++) {
/*  68:108 */       for (int col = 0; col < columnCount; col++) {
/*  69:109 */         out.setEntry(row, col, getEntry(row, col) + d);
/*  70:    */       }
/*  71:    */     }
/*  72:113 */     return out;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public RealMatrix scalarMultiply(double d)
/*  76:    */   {
/*  77:118 */     int rowCount = getRowDimension();
/*  78:119 */     int columnCount = getColumnDimension();
/*  79:120 */     RealMatrix out = createMatrix(rowCount, columnCount);
/*  80:121 */     for (int row = 0; row < rowCount; row++) {
/*  81:122 */       for (int col = 0; col < columnCount; col++) {
/*  82:123 */         out.setEntry(row, col, getEntry(row, col) * d);
/*  83:    */       }
/*  84:    */     }
/*  85:127 */     return out;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public RealMatrix multiply(RealMatrix m)
/*  89:    */   {
/*  90:133 */     MatrixUtils.checkMultiplicationCompatible(this, m);
/*  91:    */     
/*  92:135 */     int nRows = getRowDimension();
/*  93:136 */     int nCols = m.getColumnDimension();
/*  94:137 */     int nSum = getColumnDimension();
/*  95:138 */     RealMatrix out = createMatrix(nRows, nCols);
/*  96:139 */     for (int row = 0; row < nRows; row++) {
/*  97:140 */       for (int col = 0; col < nCols; col++)
/*  98:    */       {
/*  99:141 */         double sum = 0.0D;
/* 100:142 */         for (int i = 0; i < nSum; i++) {
/* 101:143 */           sum += getEntry(row, i) * m.getEntry(i, col);
/* 102:    */         }
/* 103:145 */         out.setEntry(row, col, sum);
/* 104:    */       }
/* 105:    */     }
/* 106:149 */     return out;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public RealMatrix preMultiply(RealMatrix m)
/* 110:    */   {
/* 111:154 */     return m.multiply(this);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public RealMatrix power(int p)
/* 115:    */   {
/* 116:159 */     if (p < 0) {
/* 117:160 */       throw new IllegalArgumentException("p must be >= 0");
/* 118:    */     }
/* 119:163 */     if (!isSquare()) {
/* 120:164 */       throw new NonSquareMatrixException(getRowDimension(), getColumnDimension());
/* 121:    */     }
/* 122:167 */     if (p == 0) {
/* 123:168 */       return MatrixUtils.createRealIdentityMatrix(getRowDimension());
/* 124:    */     }
/* 125:171 */     if (p == 1) {
/* 126:172 */       return copy();
/* 127:    */     }
/* 128:175 */     int power = p - 1;
/* 129:    */     
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:184 */     char[] binaryRepresentation = Integer.toBinaryString(power).toCharArray();
/* 138:185 */     ArrayList<Integer> nonZeroPositions = new ArrayList();
/* 139:186 */     int maxI = -1;
/* 140:188 */     for (int i = 0; i < binaryRepresentation.length; i++) {
/* 141:189 */       if (binaryRepresentation[i] == '1')
/* 142:    */       {
/* 143:190 */         int pos = binaryRepresentation.length - i - 1;
/* 144:191 */         nonZeroPositions.add(Integer.valueOf(pos));
/* 145:194 */         if (maxI == -1) {
/* 146:195 */           maxI = pos;
/* 147:    */         }
/* 148:    */       }
/* 149:    */     }
/* 150:200 */     RealMatrix[] results = new RealMatrix[maxI + 1];
/* 151:201 */     results[0] = copy();
/* 152:203 */     for (int i = 1; i <= maxI; i++) {
/* 153:204 */       results[i] = results[(i - 1)].multiply(results[(i - 1)]);
/* 154:    */     }
/* 155:207 */     RealMatrix result = copy();
/* 156:209 */     for (Integer i : nonZeroPositions) {
/* 157:210 */       result = result.multiply(results[i.intValue()]);
/* 158:    */     }
/* 159:213 */     return result;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public double[][] getData()
/* 163:    */   {
/* 164:218 */     double[][] data = new double[getRowDimension()][getColumnDimension()];
/* 165:220 */     for (int i = 0; i < data.length; i++)
/* 166:    */     {
/* 167:221 */       double[] dataI = data[i];
/* 168:222 */       for (int j = 0; j < dataI.length; j++) {
/* 169:223 */         dataI[j] = getEntry(i, j);
/* 170:    */       }
/* 171:    */     }
/* 172:227 */     return data;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public double getNorm()
/* 176:    */   {
return walkInColumnOrder(new RealMatrixPreservingVisitor()
/* 178:    */     {
/* 179:    */       private double endRow;
/* 180:    */       private double columnSum;
/* 181:    */       private double maxColSum;
/* 182:    */       
/* 183:    */       public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn)
/* 184:    */       {
/* 185:247 */         this.endRow = endRow;
/* 186:248 */         this.columnSum = 0.0D;
/* 187:249 */         this.maxColSum = 0.0D;
/* 188:    */       }
/* 189:    */       
/* 190:    */       public void visit(int row, int column, double value)
/* 191:    */       {
/* 192:254 */         this.columnSum += FastMath.abs(value);
/* 193:255 */         if (row == this.endRow)
/* 194:    */         {
/* 195:256 */           this.maxColSum = FastMath.max(this.maxColSum, this.columnSum);
/* 196:257 */           this.columnSum = 0.0D;
/* 197:    */         }
/* 198:    */       }
/* 199:    */       
/* 200:    */       public double end()
/* 201:    */       {
/* 202:263 */         return this.maxColSum;
/* 203:    */       }
/* 204:    */     });   }
/* 206:    */   
/* 207:    */   public double getFrobeniusNorm()
/* 208:    */   {
return walkInOptimizedOrder(new RealMatrixPreservingVisitor()
/* 210:    */     {
/* 211:    */       private double sum;
/* 212:    */       
/* 213:    */       public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn)
/* 214:    */       {
/* 215:279 */         this.sum = 0.0D;
/* 216:    */       }
/* 217:    */       
/* 218:    */       public void visit(int row, int column, double value)
/* 219:    */       {
/* 220:284 */         this.sum += value * value;
/* 221:    */       }
/* 222:    */       
/* 223:    */       public double end()
/* 224:    */       {
/* 225:289 */         return FastMath.sqrt(this.sum);
/* 226:    */       }
/* 227:    */     });   }
/* 229:    */   
/* 230:    */   public RealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn)
/* 231:    */   {
/* 232:297 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 233:    */     
/* 234:299 */     RealMatrix subMatrix = createMatrix(endRow - startRow + 1, endColumn - startColumn + 1);
/* 235:301 */     for (int i = startRow; i <= endRow; i++) {
/* 236:302 */       for (int j = startColumn; j <= endColumn; j++) {
/* 237:303 */         subMatrix.setEntry(i - startRow, j - startColumn, getEntry(i, j));
/* 238:    */       }
/* 239:    */     }
/* 240:307 */     return subMatrix;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public RealMatrix getSubMatrix(final int[] selectedRows, final int[] selectedColumns)
/* 244:    */   {
/* 245:313 */     MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
/* 246:    */     
/* 247:    */ 
/* 248:316 */     RealMatrix subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
/* 249:    */     
/* 250:318 */     subMatrix.walkInOptimizedOrder(new DefaultRealMatrixChangingVisitor()
/* 251:    */     {
/* 252:    */       public double visit(int row, int column, double value)
/* 253:    */       {
/* 254:323 */         return AbstractRealMatrix.this.getEntry(selectedRows[row], selectedColumns[column]);
/* 255:    */       }
/* 256:327 */     });
/* 257:328 */     return subMatrix;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, final double[][] destination)
/* 261:    */   {
/* 262:336 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 263:337 */     int rowsCount = endRow + 1 - startRow;
/* 264:338 */     int columnsCount = endColumn + 1 - startColumn;
/* 265:339 */     if ((destination.length < rowsCount) || (destination[0].length < columnsCount)) {
/* 266:340 */       throw new MatrixDimensionMismatchException(destination.length, destination[0].length, rowsCount, columnsCount);
/* 267:    */     }
/* 268:345 */     walkInOptimizedOrder(new DefaultRealMatrixPreservingVisitor()
/* 269:    */     {
/* 270:    */       private int startRow;
/* 271:    */       private int startColumn;
/* 272:    */       
/* 273:    */       public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn)
/* 274:    */       {
/* 275:358 */         this.startRow = startRow;
/* 276:359 */         this.startColumn = startColumn;
/* 277:    */       }
/* 278:    */       
/* 279:    */       public void visit(int row, int column, double value)
/* 280:    */       {
/* 281:365 */         destination[(row - this.startRow)][(column - this.startColumn)] = value;
/* 282:    */       }
/* 283:365 */     }, startRow, endRow, startColumn, endColumn);
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void copySubMatrix(int[] selectedRows, int[] selectedColumns, double[][] destination)
/* 287:    */   {
/* 288:374 */     MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
/* 289:375 */     if ((destination.length < selectedRows.length) || (destination[0].length < selectedColumns.length)) {
/* 290:377 */       throw new MatrixDimensionMismatchException(destination.length, destination[0].length, selectedRows.length, selectedColumns.length);
/* 291:    */     }
/* 292:382 */     for (int i = 0; i < selectedRows.length; i++)
/* 293:    */     {
/* 294:383 */       double[] destinationI = destination[i];
/* 295:384 */       for (int j = 0; j < selectedColumns.length; j++) {
/* 296:385 */         destinationI[j] = getEntry(selectedRows[i], selectedColumns[j]);
/* 297:    */       }
/* 298:    */     }
/* 299:    */   }
/* 300:    */   
/* 301:    */   public void setSubMatrix(double[][] subMatrix, int row, int column)
/* 302:    */     throws NoDataException, DimensionMismatchException, NullArgumentException
/* 303:    */   {
/* 304:393 */     MathUtils.checkNotNull(subMatrix);
/* 305:394 */     int nRows = subMatrix.length;
/* 306:395 */     if (nRows == 0) {
/* 307:396 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/* 308:    */     }
/* 309:399 */     int nCols = subMatrix[0].length;
/* 310:400 */     if (nCols == 0) {
/* 311:401 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/* 312:    */     }
/* 313:404 */     for (int r = 1; r < nRows; r++) {
/* 314:405 */       if (subMatrix[r].length != nCols) {
/* 315:406 */         throw new DimensionMismatchException(nCols, subMatrix[r].length);
/* 316:    */       }
/* 317:    */     }
/* 318:410 */     MatrixUtils.checkRowIndex(this, row);
/* 319:411 */     MatrixUtils.checkColumnIndex(this, column);
/* 320:412 */     MatrixUtils.checkRowIndex(this, nRows + row - 1);
/* 321:413 */     MatrixUtils.checkColumnIndex(this, nCols + column - 1);
/* 322:415 */     for (int i = 0; i < nRows; i++) {
/* 323:416 */       for (int j = 0; j < nCols; j++) {
/* 324:417 */         setEntry(row + i, column + j, subMatrix[i][j]);
/* 325:    */       }
/* 326:    */     }
/* 327:    */   }
/* 328:    */   
/* 329:    */   public RealMatrix getRowMatrix(int row)
/* 330:    */   {
/* 331:424 */     MatrixUtils.checkRowIndex(this, row);
/* 332:425 */     int nCols = getColumnDimension();
/* 333:426 */     RealMatrix out = createMatrix(1, nCols);
/* 334:427 */     for (int i = 0; i < nCols; i++) {
/* 335:428 */       out.setEntry(0, i, getEntry(row, i));
/* 336:    */     }
/* 337:431 */     return out;
/* 338:    */   }
/* 339:    */   
/* 340:    */   public void setRowMatrix(int row, RealMatrix matrix)
/* 341:    */   {
/* 342:436 */     MatrixUtils.checkRowIndex(this, row);
/* 343:437 */     int nCols = getColumnDimension();
/* 344:438 */     if ((matrix.getRowDimension() != 1) || (matrix.getColumnDimension() != nCols)) {
/* 345:440 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
/* 346:    */     }
/* 347:444 */     for (int i = 0; i < nCols; i++) {
/* 348:445 */       setEntry(row, i, matrix.getEntry(0, i));
/* 349:    */     }
/* 350:    */   }
/* 351:    */   
/* 352:    */   public RealMatrix getColumnMatrix(int column)
/* 353:    */   {
/* 354:451 */     MatrixUtils.checkColumnIndex(this, column);
/* 355:452 */     int nRows = getRowDimension();
/* 356:453 */     RealMatrix out = createMatrix(nRows, 1);
/* 357:454 */     for (int i = 0; i < nRows; i++) {
/* 358:455 */       out.setEntry(i, 0, getEntry(i, column));
/* 359:    */     }
/* 360:458 */     return out;
/* 361:    */   }
/* 362:    */   
/* 363:    */   public void setColumnMatrix(int column, RealMatrix matrix)
/* 364:    */   {
/* 365:463 */     MatrixUtils.checkColumnIndex(this, column);
/* 366:464 */     int nRows = getRowDimension();
/* 367:465 */     if ((matrix.getRowDimension() != nRows) || (matrix.getColumnDimension() != 1)) {
/* 368:467 */       throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
/* 369:    */     }
/* 370:471 */     for (int i = 0; i < nRows; i++) {
/* 371:472 */       setEntry(i, column, matrix.getEntry(i, 0));
/* 372:    */     }
/* 373:    */   }
/* 374:    */   
/* 375:    */   public RealVector getRowVector(int row)
/* 376:    */   {
/* 377:478 */     return new ArrayRealVector(getRow(row), false);
/* 378:    */   }
/* 379:    */   
/* 380:    */   public void setRowVector(int row, RealVector vector)
/* 381:    */   {
/* 382:483 */     MatrixUtils.checkRowIndex(this, row);
/* 383:484 */     int nCols = getColumnDimension();
/* 384:485 */     if (vector.getDimension() != nCols) {
/* 385:486 */       throw new MatrixDimensionMismatchException(1, vector.getDimension(), 1, nCols);
/* 386:    */     }
/* 387:489 */     for (int i = 0; i < nCols; i++) {
/* 388:490 */       setEntry(row, i, vector.getEntry(i));
/* 389:    */     }
/* 390:    */   }
/* 391:    */   
/* 392:    */   public RealVector getColumnVector(int column)
/* 393:    */   {
/* 394:496 */     return new ArrayRealVector(getColumn(column), false);
/* 395:    */   }
/* 396:    */   
/* 397:    */   public void setColumnVector(int column, RealVector vector)
/* 398:    */   {
/* 399:501 */     MatrixUtils.checkColumnIndex(this, column);
/* 400:502 */     int nRows = getRowDimension();
/* 401:503 */     if (vector.getDimension() != nRows) {
/* 402:504 */       throw new MatrixDimensionMismatchException(vector.getDimension(), 1, nRows, 1);
/* 403:    */     }
/* 404:507 */     for (int i = 0; i < nRows; i++) {
/* 405:508 */       setEntry(i, column, vector.getEntry(i));
/* 406:    */     }
/* 407:    */   }
/* 408:    */   
/* 409:    */   public double[] getRow(int row)
/* 410:    */   {
/* 411:514 */     MatrixUtils.checkRowIndex(this, row);
/* 412:515 */     int nCols = getColumnDimension();
/* 413:516 */     double[] out = new double[nCols];
/* 414:517 */     for (int i = 0; i < nCols; i++) {
/* 415:518 */       out[i] = getEntry(row, i);
/* 416:    */     }
/* 417:521 */     return out;
/* 418:    */   }
/* 419:    */   
/* 420:    */   public void setRow(int row, double[] array)
/* 421:    */   {
/* 422:526 */     MatrixUtils.checkRowIndex(this, row);
/* 423:527 */     int nCols = getColumnDimension();
/* 424:528 */     if (array.length != nCols) {
/* 425:529 */       throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
/* 426:    */     }
/* 427:531 */     for (int i = 0; i < nCols; i++) {
/* 428:532 */       setEntry(row, i, array[i]);
/* 429:    */     }
/* 430:    */   }
/* 431:    */   
/* 432:    */   public double[] getColumn(int column)
/* 433:    */   {
/* 434:538 */     MatrixUtils.checkColumnIndex(this, column);
/* 435:539 */     int nRows = getRowDimension();
/* 436:540 */     double[] out = new double[nRows];
/* 437:541 */     for (int i = 0; i < nRows; i++) {
/* 438:542 */       out[i] = getEntry(i, column);
/* 439:    */     }
/* 440:545 */     return out;
/* 441:    */   }
/* 442:    */   
/* 443:    */   public void setColumn(int column, double[] array)
/* 444:    */   {
/* 445:550 */     MatrixUtils.checkColumnIndex(this, column);
/* 446:551 */     int nRows = getRowDimension();
/* 447:552 */     if (array.length != nRows) {
/* 448:553 */       throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
/* 449:    */     }
/* 450:555 */     for (int i = 0; i < nRows; i++) {
/* 451:556 */       setEntry(i, column, array[i]);
/* 452:    */     }
/* 453:    */   }
/* 454:    */   
/* 455:    */   public abstract double getEntry(int paramInt1, int paramInt2);
/* 456:    */   
/* 457:    */   public abstract void setEntry(int paramInt1, int paramInt2, double paramDouble);
/* 458:    */   
/* 459:    */   public void addToEntry(int row, int column, double increment)
/* 460:    */   {
/* 461:568 */     MatrixUtils.checkMatrixIndex(this, row, column);
/* 462:569 */     setEntry(row, column, getEntry(row, column) + increment);
/* 463:    */   }
/* 464:    */   
/* 465:    */   public void multiplyEntry(int row, int column, double factor)
/* 466:    */   {
/* 467:574 */     MatrixUtils.checkMatrixIndex(this, row, column);
/* 468:575 */     setEntry(row, column, getEntry(row, column) * factor);
/* 469:    */   }
/* 470:    */   
/* 471:    */   public RealMatrix transpose()
/* 472:    */   {
/* 473:580 */     int nRows = getRowDimension();
/* 474:581 */     int nCols = getColumnDimension();
/* 475:582 */     final RealMatrix out = createMatrix(nCols, nRows);
/* 476:583 */     walkInOptimizedOrder(new DefaultRealMatrixPreservingVisitor()
/* 477:    */     {
/* 478:    */       public void visit(int row, int column, double value)
/* 479:    */       {
/* 480:588 */         out.setEntry(column, row, value);
/* 481:    */       }
/* 482:592 */     });
/* 483:593 */     return out;
/* 484:    */   }
/* 485:    */   
/* 486:    */   public boolean isSquare()
/* 487:    */   {
/* 488:598 */     return getColumnDimension() == getRowDimension();
/* 489:    */   }
/* 490:    */   
/* 491:    */   public abstract int getRowDimension();
/* 492:    */   
/* 493:    */   public abstract int getColumnDimension();
/* 494:    */   
/* 495:    */   public double getTrace()
/* 496:    */   {
/* 497:621 */     int nRows = getRowDimension();
/* 498:622 */     int nCols = getColumnDimension();
/* 499:623 */     if (nRows != nCols) {
/* 500:624 */       throw new NonSquareMatrixException(nRows, nCols);
/* 501:    */     }
/* 502:626 */     double trace = 0.0D;
/* 503:627 */     for (int i = 0; i < nRows; i++) {
/* 504:628 */       trace += getEntry(i, i);
/* 505:    */     }
/* 506:630 */     return trace;
/* 507:    */   }
/* 508:    */   
/* 509:    */   public double[] operate(double[] v)
/* 510:    */   {
/* 511:635 */     int nRows = getRowDimension();
/* 512:636 */     int nCols = getColumnDimension();
/* 513:637 */     if (v.length != nCols) {
/* 514:638 */       throw new DimensionMismatchException(v.length, nCols);
/* 515:    */     }
/* 516:641 */     double[] out = new double[nRows];
/* 517:642 */     for (int row = 0; row < nRows; row++)
/* 518:    */     {
/* 519:643 */       double sum = 0.0D;
/* 520:644 */       for (int i = 0; i < nCols; i++) {
/* 521:645 */         sum += getEntry(row, i) * v[i];
/* 522:    */       }
/* 523:647 */       out[row] = sum;
/* 524:    */     }
/* 525:650 */     return out;
/* 526:    */   }
/* 527:    */   
/* 528:    */   public RealVector operate(RealVector v)
/* 529:    */   {
/* 530:    */     try
/* 531:    */     {
/* 532:657 */       return new ArrayRealVector(operate(((ArrayRealVector)v).getDataRef()), false);
/* 533:    */     }
/* 534:    */     catch (ClassCastException cce)
/* 535:    */     {
/* 536:659 */       int nRows = getRowDimension();
/* 537:660 */       int nCols = getColumnDimension();
/* 538:661 */       if (v.getDimension() != nCols) {
/* 539:662 */         throw new DimensionMismatchException(v.getDimension(), nCols);
/* 540:    */       }
/* 541:665 */       double[] out = new double[nRows];
/* 542:666 */       for (int row = 0; row < nRows; row++)
/* 543:    */       {
/* 544:667 */         double sum = 0.0D;
/* 545:668 */         for (int i = 0; i < nCols; i++) {
/* 546:669 */           sum += getEntry(row, i) * v.getEntry(i);
/* 547:    */         }
/* 548:671 */         out[row] = sum;
/* 549:    */       }
/* 550:674 */       return new ArrayRealVector(out, false);
/* 551:    */     }
/* 552:    */   }
/* 553:    */   
/* 554:    */   public double[] preMultiply(double[] v)
/* 555:    */   {
/* 556:681 */     int nRows = getRowDimension();
/* 557:682 */     int nCols = getColumnDimension();
/* 558:683 */     if (v.length != nRows) {
/* 559:684 */       throw new DimensionMismatchException(v.length, nRows);
/* 560:    */     }
/* 561:687 */     double[] out = new double[nCols];
/* 562:688 */     for (int col = 0; col < nCols; col++)
/* 563:    */     {
/* 564:689 */       double sum = 0.0D;
/* 565:690 */       for (int i = 0; i < nRows; i++) {
/* 566:691 */         sum += getEntry(i, col) * v[i];
/* 567:    */       }
/* 568:693 */       out[col] = sum;
/* 569:    */     }
/* 570:696 */     return out;
/* 571:    */   }
/* 572:    */   
/* 573:    */   public RealVector preMultiply(RealVector v)
/* 574:    */   {
/* 575:    */     try
/* 576:    */     {
/* 577:702 */       return new ArrayRealVector(preMultiply(((ArrayRealVector)v).getDataRef()), false);
/* 578:    */     }
/* 579:    */     catch (ClassCastException cce)
/* 580:    */     {
/* 581:705 */       int nRows = getRowDimension();
/* 582:706 */       int nCols = getColumnDimension();
/* 583:707 */       if (v.getDimension() != nRows) {
/* 584:708 */         throw new DimensionMismatchException(v.getDimension(), nRows);
/* 585:    */       }
/* 586:711 */       double[] out = new double[nCols];
/* 587:712 */       for (int col = 0; col < nCols; col++)
/* 588:    */       {
/* 589:713 */         double sum = 0.0D;
/* 590:714 */         for (int i = 0; i < nRows; i++) {
/* 591:715 */           sum += getEntry(i, col) * v.getEntry(i);
/* 592:    */         }
/* 593:717 */         out[col] = sum;
/* 594:    */       }
/* 595:720 */       return new ArrayRealVector(out, false);
/* 596:    */     }
/* 597:    */   }
/* 598:    */   
/* 599:    */   public double walkInRowOrder(RealMatrixChangingVisitor visitor)
/* 600:    */   {
/* 601:726 */     int rows = getRowDimension();
/* 602:727 */     int columns = getColumnDimension();
/* 603:728 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 604:729 */     for (int row = 0; row < rows; row++) {
/* 605:730 */       for (int column = 0; column < columns; column++)
/* 606:    */       {
/* 607:731 */         double oldValue = getEntry(row, column);
/* 608:732 */         double newValue = visitor.visit(row, column, oldValue);
/* 609:733 */         setEntry(row, column, newValue);
/* 610:    */       }
/* 611:    */     }
/* 612:736 */     return visitor.end();
/* 613:    */   }
/* 614:    */   
/* 615:    */   public double walkInRowOrder(RealMatrixPreservingVisitor visitor)
/* 616:    */   {
/* 617:741 */     int rows = getRowDimension();
/* 618:742 */     int columns = getColumnDimension();
/* 619:743 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 620:744 */     for (int row = 0; row < rows; row++) {
/* 621:745 */       for (int column = 0; column < columns; column++) {
/* 622:746 */         visitor.visit(row, column, getEntry(row, column));
/* 623:    */       }
/* 624:    */     }
/* 625:749 */     return visitor.end();
/* 626:    */   }
/* 627:    */   
/* 628:    */   public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 629:    */   {
/* 630:756 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 631:757 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 632:759 */     for (int row = startRow; row <= endRow; row++) {
/* 633:760 */       for (int column = startColumn; column <= endColumn; column++)
/* 634:    */       {
/* 635:761 */         double oldValue = getEntry(row, column);
/* 636:762 */         double newValue = visitor.visit(row, column, oldValue);
/* 637:763 */         setEntry(row, column, newValue);
/* 638:    */       }
/* 639:    */     }
/* 640:766 */     return visitor.end();
/* 641:    */   }
/* 642:    */   
/* 643:    */   public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 644:    */   {
/* 645:773 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 646:774 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 647:776 */     for (int row = startRow; row <= endRow; row++) {
/* 648:777 */       for (int column = startColumn; column <= endColumn; column++) {
/* 649:778 */         visitor.visit(row, column, getEntry(row, column));
/* 650:    */       }
/* 651:    */     }
/* 652:781 */     return visitor.end();
/* 653:    */   }
/* 654:    */   
/* 655:    */   public double walkInColumnOrder(RealMatrixChangingVisitor visitor)
/* 656:    */   {
/* 657:786 */     int rows = getRowDimension();
/* 658:787 */     int columns = getColumnDimension();
/* 659:788 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 660:789 */     for (int column = 0; column < columns; column++) {
/* 661:790 */       for (int row = 0; row < rows; row++)
/* 662:    */       {
/* 663:791 */         double oldValue = getEntry(row, column);
/* 664:792 */         double newValue = visitor.visit(row, column, oldValue);
/* 665:793 */         setEntry(row, column, newValue);
/* 666:    */       }
/* 667:    */     }
/* 668:796 */     return visitor.end();
/* 669:    */   }
/* 670:    */   
/* 671:    */   public double walkInColumnOrder(RealMatrixPreservingVisitor visitor)
/* 672:    */   {
/* 673:801 */     int rows = getRowDimension();
/* 674:802 */     int columns = getColumnDimension();
/* 675:803 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 676:804 */     for (int column = 0; column < columns; column++) {
/* 677:805 */       for (int row = 0; row < rows; row++) {
/* 678:806 */         visitor.visit(row, column, getEntry(row, column));
/* 679:    */       }
/* 680:    */     }
/* 681:809 */     return visitor.end();
/* 682:    */   }
/* 683:    */   
/* 684:    */   public double walkInColumnOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 685:    */   {
/* 686:816 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 687:817 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 688:819 */     for (int column = startColumn; column <= endColumn; column++) {
/* 689:820 */       for (int row = startRow; row <= endRow; row++)
/* 690:    */       {
/* 691:821 */         double oldValue = getEntry(row, column);
/* 692:822 */         double newValue = visitor.visit(row, column, oldValue);
/* 693:823 */         setEntry(row, column, newValue);
/* 694:    */       }
/* 695:    */     }
/* 696:826 */     return visitor.end();
/* 697:    */   }
/* 698:    */   
/* 699:    */   public double walkInColumnOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 700:    */   {
/* 701:833 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 702:834 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 703:836 */     for (int column = startColumn; column <= endColumn; column++) {
/* 704:837 */       for (int row = startRow; row <= endRow; row++) {
/* 705:838 */         visitor.visit(row, column, getEntry(row, column));
/* 706:    */       }
/* 707:    */     }
/* 708:841 */     return visitor.end();
/* 709:    */   }
/* 710:    */   
/* 711:    */   public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor)
/* 712:    */   {
/* 713:846 */     return walkInRowOrder(visitor);
/* 714:    */   }
/* 715:    */   
/* 716:    */   public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor)
/* 717:    */   {
/* 718:851 */     return walkInRowOrder(visitor);
/* 719:    */   }
/* 720:    */   
/* 721:    */   public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 722:    */   {
/* 723:858 */     return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
/* 724:    */   }
/* 725:    */   
/* 726:    */   public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 727:    */   {
/* 728:865 */     return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
/* 729:    */   }
/* 730:    */   
/* 731:    */   public String toString()
/* 732:    */   {
/* 733:874 */     int nRows = getRowDimension();
/* 734:875 */     int nCols = getColumnDimension();
/* 735:876 */     StringBuffer res = new StringBuffer();
/* 736:877 */     String fullClassName = getClass().getName();
/* 737:878 */     String shortClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
/* 738:879 */     res.append(shortClassName).append("{");
/* 739:881 */     for (int i = 0; i < nRows; i++)
/* 740:    */     {
/* 741:882 */       if (i > 0) {
/* 742:883 */         res.append(",");
/* 743:    */       }
/* 744:885 */       res.append("{");
/* 745:886 */       for (int j = 0; j < nCols; j++)
/* 746:    */       {
/* 747:887 */         if (j > 0) {
/* 748:888 */           res.append(",");
/* 749:    */         }
/* 750:890 */         res.append(getEntry(i, j));
/* 751:    */       }
/* 752:892 */       res.append("}");
/* 753:    */     }
/* 754:895 */     res.append("}");
/* 755:896 */     return res.toString();
/* 756:    */   }
/* 757:    */   
/* 758:    */   public boolean equals(Object object)
/* 759:    */   {
/* 760:909 */     if (object == this) {
/* 761:910 */       return true;
/* 762:    */     }
/* 763:912 */     if (!(object instanceof RealMatrix)) {
/* 764:913 */       return false;
/* 765:    */     }
/* 766:915 */     RealMatrix m = (RealMatrix)object;
/* 767:916 */     int nRows = getRowDimension();
/* 768:917 */     int nCols = getColumnDimension();
/* 769:918 */     if ((m.getColumnDimension() != nCols) || (m.getRowDimension() != nRows)) {
/* 770:919 */       return false;
/* 771:    */     }
/* 772:921 */     for (int row = 0; row < nRows; row++) {
/* 773:922 */       for (int col = 0; col < nCols; col++) {
/* 774:923 */         if (getEntry(row, col) != m.getEntry(row, col)) {
/* 775:924 */           return false;
/* 776:    */         }
/* 777:    */       }
/* 778:    */     }
/* 779:928 */     return true;
/* 780:    */   }
/* 781:    */   
/* 782:    */   public int hashCode()
/* 783:    */   {
/* 784:938 */     int ret = 7;
/* 785:939 */     int nRows = getRowDimension();
/* 786:940 */     int nCols = getColumnDimension();
/* 787:941 */     ret = ret * 31 + nRows;
/* 788:942 */     ret = ret * 31 + nCols;
/* 789:943 */     for (int row = 0; row < nRows; row++) {
/* 790:944 */       for (int col = 0; col < nCols; col++) {
/* 791:945 */         ret = ret * 31 + (11 * (row + 1) + 17 * (col + 1)) * MathUtils.hash(getEntry(row, col));
/* 792:    */       }
/* 793:    */     }
/* 794:949 */     return ret;
/* 795:    */   }
/* 796:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.AbstractRealMatrix
 * JD-Core Version:    0.7.0.1
 */
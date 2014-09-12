/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.ObjectOutputStream;
/*   6:    */ import java.lang.reflect.Array;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import org.apache.commons.math3.FieldElement;
/*   9:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  10:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*  11:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  12:    */ import org.apache.commons.math3.exception.NoDataException;
/*  13:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  14:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  15:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  16:    */ import org.apache.commons.math3.exception.ZeroException;
/*  17:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  18:    */ import org.apache.commons.math3.fraction.BigFraction;
/*  19:    */ import org.apache.commons.math3.fraction.Fraction;
/*  20:    */ import org.apache.commons.math3.util.FastMath;
/*  21:    */ 
/*  22:    */ public class MatrixUtils
/*  23:    */ {
/*  24:    */   public static RealMatrix createRealMatrix(int rows, int columns)
/*  25:    */   {
/*  26: 70 */     return rows * columns <= 4096 ? new Array2DRowRealMatrix(rows, columns) : new BlockRealMatrix(rows, columns);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static <T extends FieldElement<T>> FieldMatrix<T> createFieldMatrix(org.apache.commons.math3.Field<T> field, int rows, int columns)
/*  30:    */   {
/*  31: 92 */     return rows * columns <= 4096 ? new Array2DRowFieldMatrix(field, rows, columns) : new BlockFieldMatrix(field, rows, columns);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static RealMatrix createRealMatrix(double[][] data)
/*  35:    */   {
/*  36:116 */     if ((data == null) || (data[0] == null)) {
/*  37:118 */       throw new NullArgumentException();
/*  38:    */     }
/*  39:120 */     return data.length * data[0].length <= 4096 ? new Array2DRowRealMatrix(data) : new BlockRealMatrix(data);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static <T extends FieldElement<T>> FieldMatrix<T> createFieldMatrix(T[][] data)
/*  43:    */   {
/*  44:144 */     if ((data == null) || (data[0] == null)) {
/*  45:146 */       throw new NullArgumentException();
/*  46:    */     }
/*  47:148 */     return data.length * data[0].length <= 4096 ? new Array2DRowFieldMatrix(data) : new BlockFieldMatrix(data);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static RealMatrix createRealIdentityMatrix(int dimension)
/*  51:    */   {
/*  52:161 */     RealMatrix m = createRealMatrix(dimension, dimension);
/*  53:162 */     for (int i = 0; i < dimension; i++) {
/*  54:163 */       m.setEntry(i, i, 1.0D);
/*  55:    */     }
/*  56:165 */     return m;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static <T extends FieldElement<T>> FieldMatrix<T> createFieldIdentityMatrix(org.apache.commons.math3.Field<T> field, int dimension)
/*  60:    */   {
/*  61:180 */     T zero = (T)field.getZero();
/*  62:181 */     T one = (T)field.getOne();
/*  63:    */     
/*  64:183 */     T[][] d = (T[][])Array.newInstance(field.getRuntimeClass(), new int[] { dimension, dimension });
/*  65:184 */     for (int row = 0; row < dimension; row++)
/*  66:    */     {
/*  67:185 */       T[] dRow = d[row];
/*  68:186 */       Arrays.fill(dRow, zero);
/*  69:187 */       dRow[row] = one;
/*  70:    */     }
/*  71:189 */     return new Array2DRowFieldMatrix(field, d, false);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public static RealMatrix createRealDiagonalMatrix(double[] diagonal)
/*  75:    */   {
/*  76:201 */     RealMatrix m = createRealMatrix(diagonal.length, diagonal.length);
/*  77:202 */     for (int i = 0; i < diagonal.length; i++) {
/*  78:203 */       m.setEntry(i, i, diagonal[i]);
/*  79:    */     }
/*  80:205 */     return m;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public static <T extends FieldElement<T>> FieldMatrix<T> createFieldDiagonalMatrix(T[] diagonal)
/*  84:    */   {
/*  85:219 */     FieldMatrix<T> m = createFieldMatrix(diagonal[0].getField(), diagonal.length, diagonal.length);
/*  86:221 */     for (int i = 0; i < diagonal.length; i++) {
/*  87:222 */       m.setEntry(i, i, diagonal[i]);
/*  88:    */     }
/*  89:224 */     return m;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static RealVector createRealVector(double[] data)
/*  93:    */   {
/*  94:236 */     if (data == null) {
/*  95:237 */       throw new NullArgumentException();
/*  96:    */     }
/*  97:239 */     return new ArrayRealVector(data, true);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static <T extends FieldElement<T>> FieldVector<T> createFieldVector(T[] data)
/* 101:    */   {
/* 102:253 */     if (data == null) {
/* 103:254 */       throw new NullArgumentException();
/* 104:    */     }
/* 105:256 */     if (data.length == 0) {
/* 106:257 */       throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
/* 107:    */     }
/* 108:259 */     return new ArrayFieldVector(data[0].getField(), data, true);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static RealMatrix createRowRealMatrix(double[] rowData)
/* 112:    */   {
/* 113:272 */     if (rowData == null) {
/* 114:273 */       throw new NullArgumentException();
/* 115:    */     }
/* 116:275 */     int nCols = rowData.length;
/* 117:276 */     RealMatrix m = createRealMatrix(1, nCols);
/* 118:277 */     for (int i = 0; i < nCols; i++) {
/* 119:278 */       m.setEntry(0, i, rowData[i]);
/* 120:    */     }
/* 121:280 */     return m;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static <T extends FieldElement<T>> FieldMatrix<T> createRowFieldMatrix(T[] rowData)
/* 125:    */   {
/* 126:295 */     if (rowData == null) {
/* 127:296 */       throw new NullArgumentException();
/* 128:    */     }
/* 129:298 */     int nCols = rowData.length;
/* 130:299 */     if (nCols == 0) {
/* 131:300 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/* 132:    */     }
/* 133:302 */     FieldMatrix<T> m = createFieldMatrix(rowData[0].getField(), 1, nCols);
/* 134:303 */     for (int i = 0; i < nCols; i++) {
/* 135:304 */       m.setEntry(0, i, rowData[i]);
/* 136:    */     }
/* 137:306 */     return m;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static RealMatrix createColumnRealMatrix(double[] columnData)
/* 141:    */   {
/* 142:319 */     if (columnData == null) {
/* 143:320 */       throw new NullArgumentException();
/* 144:    */     }
/* 145:322 */     int nRows = columnData.length;
/* 146:323 */     RealMatrix m = createRealMatrix(nRows, 1);
/* 147:324 */     for (int i = 0; i < nRows; i++) {
/* 148:325 */       m.setEntry(i, 0, columnData[i]);
/* 149:    */     }
/* 150:327 */     return m;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public static <T extends FieldElement<T>> FieldMatrix<T> createColumnFieldMatrix(T[] columnData)
/* 154:    */   {
/* 155:342 */     if (columnData == null) {
/* 156:343 */       throw new NullArgumentException();
/* 157:    */     }
/* 158:345 */     int nRows = columnData.length;
/* 159:346 */     if (nRows == 0) {
/* 160:347 */       throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/* 161:    */     }
/* 162:349 */     FieldMatrix<T> m = createFieldMatrix(columnData[0].getField(), nRows, 1);
/* 163:350 */     for (int i = 0; i < nRows; i++) {
/* 164:351 */       m.setEntry(i, 0, columnData[i]);
/* 165:    */     }
/* 166:353 */     return m;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static void checkMatrixIndex(AnyMatrix m, int row, int column)
/* 170:    */   {
/* 171:367 */     checkRowIndex(m, row);
/* 172:368 */     checkColumnIndex(m, column);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public static void checkRowIndex(AnyMatrix m, int row)
/* 176:    */   {
/* 177:379 */     if ((row < 0) || (row >= m.getRowDimension())) {
/* 178:381 */       throw new OutOfRangeException(LocalizedFormats.ROW_INDEX, Integer.valueOf(row), Integer.valueOf(0), Integer.valueOf(m.getRowDimension() - 1));
/* 179:    */     }
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static void checkColumnIndex(AnyMatrix m, int column)
/* 183:    */   {
/* 184:394 */     if ((column < 0) || (column >= m.getColumnDimension())) {
/* 185:395 */       throw new OutOfRangeException(LocalizedFormats.COLUMN_INDEX, Integer.valueOf(column), Integer.valueOf(0), Integer.valueOf(m.getColumnDimension() - 1));
/* 186:    */     }
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static void checkSubMatrixIndex(AnyMatrix m, int startRow, int endRow, int startColumn, int endColumn)
/* 190:    */   {
/* 191:416 */     checkRowIndex(m, startRow);
/* 192:417 */     checkRowIndex(m, endRow);
/* 193:418 */     if (endRow < startRow) {
/* 194:419 */       throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(endRow), Integer.valueOf(startRow), false);
/* 195:    */     }
/* 196:423 */     checkColumnIndex(m, startColumn);
/* 197:424 */     checkColumnIndex(m, endColumn);
/* 198:425 */     if (endColumn < startColumn) {
/* 199:426 */       throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, Integer.valueOf(endColumn), Integer.valueOf(startColumn), false);
/* 200:    */     }
/* 201:    */   }
/* 202:    */   
/* 203:    */   public static void checkSubMatrixIndex(AnyMatrix m, int[] selectedRows, int[] selectedColumns)
/* 204:    */   {
/* 205:449 */     if (selectedRows == null) {
/* 206:450 */       throw new NullArgumentException();
/* 207:    */     }
/* 208:452 */     if (selectedColumns == null) {
/* 209:453 */       throw new NullArgumentException();
/* 210:    */     }
/* 211:455 */     if (selectedRows.length == 0) {
/* 212:456 */       throw new NoDataException(LocalizedFormats.EMPTY_SELECTED_ROW_INDEX_ARRAY);
/* 213:    */     }
/* 214:458 */     if (selectedColumns.length == 0) {
/* 215:459 */       throw new NoDataException(LocalizedFormats.EMPTY_SELECTED_COLUMN_INDEX_ARRAY);
/* 216:    */     }
/* 217:462 */     for (int row : selectedRows) {
/* 218:463 */       checkRowIndex(m, row);
/* 219:    */     }
/* 220:465 */     for (int column : selectedColumns) {
/* 221:466 */       checkColumnIndex(m, column);
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   public static void checkAdditionCompatible(AnyMatrix left, AnyMatrix right)
/* 226:    */   {
/* 227:478 */     if ((left.getRowDimension() != right.getRowDimension()) || (left.getColumnDimension() != right.getColumnDimension())) {
/* 228:480 */       throw new MatrixDimensionMismatchException(left.getRowDimension(), left.getColumnDimension(), right.getRowDimension(), right.getColumnDimension());
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   public static void checkSubtractionCompatible(AnyMatrix left, AnyMatrix right)
/* 233:    */   {
/* 234:493 */     if ((left.getRowDimension() != right.getRowDimension()) || (left.getColumnDimension() != right.getColumnDimension())) {
/* 235:495 */       throw new MatrixDimensionMismatchException(left.getRowDimension(), left.getColumnDimension(), right.getRowDimension(), right.getColumnDimension());
/* 236:    */     }
/* 237:    */   }
/* 238:    */   
/* 239:    */   public static void checkMultiplicationCompatible(AnyMatrix left, AnyMatrix right)
/* 240:    */   {
/* 241:508 */     if (left.getColumnDimension() != right.getRowDimension()) {
/* 242:509 */       throw new DimensionMismatchException(left.getColumnDimension(), right.getRowDimension());
/* 243:    */     }
/* 244:    */   }
/* 245:    */   
/* 246:    */   public static Array2DRowRealMatrix fractionMatrixToRealMatrix(FieldMatrix<Fraction> m)
/* 247:    */   {
/* 248:520 */     FractionMatrixConverter converter = new FractionMatrixConverter();
/* 249:521 */     m.walkInOptimizedOrder(converter);
/* 250:522 */     return converter.getConvertedMatrix();
/* 251:    */   }
/* 252:    */   
/* 253:    */   private static class FractionMatrixConverter
/* 254:    */     extends DefaultFieldMatrixPreservingVisitor<Fraction>
/* 255:    */   {
/* 256:    */     private double[][] data;
/* 257:    */     
/* 258:    */     public FractionMatrixConverter()
/* 259:    */     {
/* 260:531 */       super(null);
/* 261:    */     }
/* 262:    */     
/* 263:    */     public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn)
/* 264:    */     {
/* 265:538 */       this.data = new double[rows][columns];
/* 266:    */     }
/* 267:    */     
/* 268:    */     public void visit(int row, int column, Fraction value)
/* 269:    */     {
/* 270:544 */       this.data[row][column] = value.doubleValue();
/* 271:    */     }
/* 272:    */     
/* 273:    */     Array2DRowRealMatrix getConvertedMatrix()
/* 274:    */     {
/* 275:553 */       return new Array2DRowRealMatrix(this.data, false);
/* 276:    */     }
/* 277:    */   }
/* 278:    */   
/* 279:    */   public static Array2DRowRealMatrix bigFractionMatrixToRealMatrix(FieldMatrix<BigFraction> m)
/* 280:    */   {
/* 281:565 */     BigFractionMatrixConverter converter = new BigFractionMatrixConverter();
/* 282:566 */     m.walkInOptimizedOrder(converter);
/* 283:567 */     return converter.getConvertedMatrix();
/* 284:    */   }
/* 285:    */   
/* 286:    */   private static class BigFractionMatrixConverter
/* 287:    */     extends DefaultFieldMatrixPreservingVisitor<BigFraction>
/* 288:    */   {
/* 289:    */     private double[][] data;
/* 290:    */     
/* 291:    */     public BigFractionMatrixConverter()
/* 292:    */     {
/* 293:576 */       super(null);
/* 294:    */     }
/* 295:    */     
/* 296:    */     public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn)
/* 297:    */     {
/* 298:583 */       this.data = new double[rows][columns];
/* 299:    */     }
/* 300:    */     
/* 301:    */     public void visit(int row, int column, BigFraction value)
/* 302:    */     {
/* 303:589 */       this.data[row][column] = value.doubleValue();
/* 304:    */     }
/* 305:    */     
/* 306:    */     Array2DRowRealMatrix getConvertedMatrix()
/* 307:    */     {
/* 308:598 */       return new Array2DRowRealMatrix(this.data, false);
/* 309:    */     }
/* 310:    */   }
/* 311:    */   
/* 312:    */   public static void serializeRealVector(RealVector vector, ObjectOutputStream oos)
/* 313:    */     throws IOException
/* 314:    */   {
/* 315:645 */     int n = vector.getDimension();
/* 316:646 */     oos.writeInt(n);
/* 317:647 */     for (int i = 0; i < n; i++) {
/* 318:648 */       oos.writeDouble(vector.getEntry(i));
/* 319:    */     }
/* 320:    */   }
/* 321:    */   
/* 322:    */   public static void deserializeRealVector(Object instance, String fieldName, ObjectInputStream ois)
/* 323:    */     throws ClassNotFoundException, IOException
/* 324:    */   {
/* 325:    */     try
/* 326:    */     {
/* 327:676 */       int n = ois.readInt();
/* 328:677 */       double[] data = new double[n];
/* 329:678 */       for (int i = 0; i < n; i++) {
/* 330:679 */         data[i] = ois.readDouble();
/* 331:    */       }
/* 332:683 */       RealVector vector = new ArrayRealVector(data, false);
/* 333:    */       
/* 334:    */ 
/* 335:686 */       java.lang.reflect.Field f = instance.getClass().getDeclaredField(fieldName);
/* 336:    */       
/* 337:688 */       f.setAccessible(true);
/* 338:689 */       f.set(instance, vector);
/* 339:    */     }
/* 340:    */     catch (NoSuchFieldException nsfe)
/* 341:    */     {
/* 342:692 */       IOException ioe = new IOException();
/* 343:693 */       ioe.initCause(nsfe);
/* 344:694 */       throw ioe;
/* 345:    */     }
/* 346:    */     catch (IllegalAccessException iae)
/* 347:    */     {
/* 348:696 */       IOException ioe = new IOException();
/* 349:697 */       ioe.initCause(iae);
/* 350:698 */       throw ioe;
/* 351:    */     }
/* 352:    */   }
/* 353:    */   
/* 354:    */   public static void serializeRealMatrix(RealMatrix matrix, ObjectOutputStream oos)
/* 355:    */     throws IOException
/* 356:    */   {
/* 357:746 */     int n = matrix.getRowDimension();
/* 358:747 */     int m = matrix.getColumnDimension();
/* 359:748 */     oos.writeInt(n);
/* 360:749 */     oos.writeInt(m);
/* 361:750 */     for (int i = 0; i < n; i++) {
/* 362:751 */       for (int j = 0; j < m; j++) {
/* 363:752 */         oos.writeDouble(matrix.getEntry(i, j));
/* 364:    */       }
/* 365:    */     }
/* 366:    */   }
/* 367:    */   
/* 368:    */   public static void deserializeRealMatrix(Object instance, String fieldName, ObjectInputStream ois)
/* 369:    */     throws ClassNotFoundException, IOException
/* 370:    */   {
/* 371:    */     try
/* 372:    */     {
/* 373:781 */       int n = ois.readInt();
/* 374:782 */       int m = ois.readInt();
/* 375:783 */       double[][] data = new double[n][m];
/* 376:784 */       for (int i = 0; i < n; i++)
/* 377:    */       {
/* 378:785 */         double[] dataI = data[i];
/* 379:786 */         for (int j = 0; j < m; j++) {
/* 380:787 */           dataI[j] = ois.readDouble();
/* 381:    */         }
/* 382:    */       }
/* 383:792 */       RealMatrix matrix = new Array2DRowRealMatrix(data, false);
/* 384:    */       
/* 385:    */ 
/* 386:795 */       java.lang.reflect.Field f = instance.getClass().getDeclaredField(fieldName);
/* 387:    */       
/* 388:797 */       f.setAccessible(true);
/* 389:798 */       f.set(instance, matrix);
/* 390:    */     }
/* 391:    */     catch (NoSuchFieldException nsfe)
/* 392:    */     {
/* 393:801 */       IOException ioe = new IOException();
/* 394:802 */       ioe.initCause(nsfe);
/* 395:803 */       throw ioe;
/* 396:    */     }
/* 397:    */     catch (IllegalAccessException iae)
/* 398:    */     {
/* 399:805 */       IOException ioe = new IOException();
/* 400:806 */       ioe.initCause(iae);
/* 401:807 */       throw ioe;
/* 402:    */     }
/* 403:    */   }
/* 404:    */   
/* 405:    */   public static void solveLowerTriangularSystem(RealMatrix rm, RealVector b)
/* 406:    */   {
/* 407:827 */     if ((rm == null) || (b == null) || (rm.getRowDimension() != b.getDimension())) {
/* 408:828 */       throw new MathIllegalArgumentException(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, new Object[] { Integer.valueOf(rm == null ? 0 : rm.getRowDimension()), Integer.valueOf(b == null ? 0 : b.getDimension()) });
/* 409:    */     }
/* 410:832 */     if (rm.getColumnDimension() != rm.getRowDimension()) {
/* 411:833 */       throw new MathIllegalArgumentException(LocalizedFormats.DIMENSIONS_MISMATCH_2x2, new Object[] { Integer.valueOf(rm.getRowDimension()), Integer.valueOf(rm.getRowDimension()), Integer.valueOf(rm.getRowDimension()), Integer.valueOf(rm.getColumnDimension()) });
/* 412:    */     }
/* 413:837 */     int rows = rm.getRowDimension();
/* 414:838 */     for (int i = 0; i < rows; i++)
/* 415:    */     {
/* 416:839 */       double diag = rm.getEntry(i, i);
/* 417:840 */       if (FastMath.abs(diag) < 2.225073858507201E-308D) {
/* 418:841 */         throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
/* 419:    */       }
/* 420:843 */       double bi = b.getEntry(i) / diag;
/* 421:844 */       b.setEntry(i, bi);
/* 422:845 */       for (int j = i + 1; j < rows; j++) {
/* 423:846 */         b.setEntry(j, b.getEntry(j) - bi * rm.getEntry(j, i));
/* 424:    */       }
/* 425:    */     }
/* 426:    */   }
/* 427:    */   
/* 428:    */   public static void solveUpperTriangularSystem(RealMatrix rm, RealVector b)
/* 429:    */   {
/* 430:867 */     if ((rm == null) || (b == null) || (rm.getRowDimension() != b.getDimension())) {
/* 431:868 */       throw new MathIllegalArgumentException(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, new Object[] { Integer.valueOf(rm == null ? 0 : rm.getRowDimension()), Integer.valueOf(b == null ? 0 : b.getDimension()) });
/* 432:    */     }
/* 433:872 */     if (rm.getColumnDimension() != rm.getRowDimension()) {
/* 434:873 */       throw new MathIllegalArgumentException(LocalizedFormats.DIMENSIONS_MISMATCH_2x2, new Object[] { Integer.valueOf(rm.getRowDimension()), Integer.valueOf(rm.getRowDimension()), Integer.valueOf(rm.getRowDimension()), Integer.valueOf(rm.getColumnDimension()) });
/* 435:    */     }
/* 436:877 */     int rows = rm.getRowDimension();
/* 437:878 */     for (int i = rows - 1; i > -1; i--)
/* 438:    */     {
/* 439:879 */       double diag = rm.getEntry(i, i);
/* 440:880 */       if (FastMath.abs(diag) < 2.225073858507201E-308D) {
/* 441:881 */         throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
/* 442:    */       }
/* 443:883 */       double bi = b.getEntry(i) / diag;
/* 444:884 */       b.setEntry(i, bi);
/* 445:885 */       for (int j = i - 1; j > -1; j--) {
/* 446:886 */         b.setEntry(j, b.getEntry(j) - bi * rm.getEntry(j, i));
/* 447:    */       }
/* 448:    */     }
/* 449:    */   }
/* 450:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.MatrixUtils
 * JD-Core Version:    0.7.0.1
 */
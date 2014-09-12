/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.Field;
/*   5:    */ import org.apache.commons.math3.FieldElement;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   8:    */ import org.apache.commons.math3.exception.NoDataException;
/*   9:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  11:    */ import org.apache.commons.math3.util.MathUtils;
/*  12:    */ 
/*  13:    */ public class Array2DRowFieldMatrix<T extends FieldElement<T>>
/*  14:    */   extends AbstractFieldMatrix<T>
/*  15:    */   implements Serializable
/*  16:    */ {
/*  17:    */   private static final long serialVersionUID = 7260756672015356458L;
/*  18:    */   private T[][] data;
/*  19:    */   
/*  20:    */   public Array2DRowFieldMatrix(Field<T> field)
/*  21:    */   {
/*  22: 55 */     super(field);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Array2DRowFieldMatrix(Field<T> field, int rowDimension, int columnDimension)
/*  26:    */   {
/*  27: 70 */     super(field, rowDimension, columnDimension);
/*  28: 71 */     this.data = buildArray(field, rowDimension, columnDimension);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Array2DRowFieldMatrix(T[][] d)
/*  32:    */   {
/*  33: 89 */     this(extractField(d), d);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Array2DRowFieldMatrix(Field<T> field, T[][] d)
/*  37:    */   {
/*  38:108 */     super(field);
/*  39:109 */     copyIn(d);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Array2DRowFieldMatrix(T[][] d, boolean copyArray)
/*  43:    */   {
/*  44:129 */     this(extractField(d), d, copyArray);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Array2DRowFieldMatrix(Field<T> field, T[][] d, boolean copyArray)
/*  48:    */     throws DimensionMismatchException, NoDataException, NullArgumentException
/*  49:    */   {
/*  50:150 */     super(field);
/*  51:151 */     if (copyArray)
/*  52:    */     {
/*  53:152 */       copyIn(d);
/*  54:    */     }
/*  55:    */     else
/*  56:    */     {
/*  57:154 */       MathUtils.checkNotNull(d);
/*  58:155 */       int nRows = d.length;
/*  59:156 */       if (nRows == 0) {
/*  60:157 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/*  61:    */       }
/*  62:159 */       int nCols = d[0].length;
/*  63:160 */       if (nCols == 0) {
/*  64:161 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/*  65:    */       }
/*  66:163 */       for (int r = 1; r < nRows; r++) {
/*  67:164 */         if (d[r].length != nCols) {
/*  68:165 */           throw new DimensionMismatchException(nCols, d[r].length);
/*  69:    */         }
/*  70:    */       }
/*  71:168 */       this.data = d;
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Array2DRowFieldMatrix(T[] v)
/*  76:    */   {
/*  77:180 */     this(extractField(v), v);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Array2DRowFieldMatrix(Field<T> field, T[] v)
/*  81:    */   {
/*  82:192 */     super(field);
/*  83:193 */     int nRows = v.length;
/*  84:194 */     this.data = buildArray(getField(), nRows, 1);
/*  85:195 */     for (int row = 0; row < nRows; row++) {
/*  86:196 */       this.data[row][0] = v[row];
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension)
/*  91:    */   {
/*  92:203 */     return new Array2DRowFieldMatrix(getField(), rowDimension, columnDimension);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public FieldMatrix<T> copy()
/*  96:    */   {
/*  97:209 */     return new Array2DRowFieldMatrix(getField(), copyOut(), false);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Array2DRowFieldMatrix<T> add(Array2DRowFieldMatrix<T> m)
/* 101:    */   {
/* 102:222 */     checkAdditionCompatible(m);
/* 103:    */     
/* 104:224 */     int rowCount = getRowDimension();
/* 105:225 */     int columnCount = getColumnDimension();
/* 106:226 */     T[][] outData = buildArray(getField(), rowCount, columnCount);
/* 107:227 */     for (int row = 0; row < rowCount; row++)
/* 108:    */     {
/* 109:228 */       T[] dataRow = this.data[row];
/* 110:229 */       T[] mRow = m.data[row];
/* 111:230 */       T[] outDataRow = outData[row];
/* 112:231 */       for (int col = 0; col < columnCount; col++) {
/* 113:232 */         outDataRow[col] = (T) ((FieldElement)dataRow[col].add(mRow[col]));
/* 114:    */       }
/* 115:    */     }
/* 116:236 */     return new Array2DRowFieldMatrix(getField(), outData, false);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public Array2DRowFieldMatrix<T> subtract(Array2DRowFieldMatrix<T> m)
/* 120:    */   {
/* 121:249 */     checkSubtractionCompatible(m);
/* 122:    */     
/* 123:251 */     int rowCount = getRowDimension();
/* 124:252 */     int columnCount = getColumnDimension();
/* 125:253 */     T[][] outData = buildArray(getField(), rowCount, columnCount);
/* 126:254 */     for (int row = 0; row < rowCount; row++)
/* 127:    */     {
/* 128:255 */       T[] dataRow = this.data[row];
/* 129:256 */       T[] mRow = m.data[row];
/* 130:257 */       T[] outDataRow = outData[row];
/* 131:258 */       for (int col = 0; col < columnCount; col++) {
/* 132:259 */         outDataRow[col] = (T) ((FieldElement)dataRow[col].subtract(mRow[col]));
/* 133:    */       }
/* 134:    */     }
/* 135:263 */     return new Array2DRowFieldMatrix(getField(), outData, false);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public Array2DRowFieldMatrix<T> multiply(Array2DRowFieldMatrix<T> m)
/* 139:    */   {
/* 140:277 */     checkMultiplicationCompatible(m);
/* 141:    */     
/* 142:279 */     int nRows = getRowDimension();
/* 143:280 */     int nCols = m.getColumnDimension();
/* 144:281 */     int nSum = getColumnDimension();
/* 145:282 */     T[][] outData = buildArray(getField(), nRows, nCols);
/* 146:283 */     for (int row = 0; row < nRows; row++)
/* 147:    */     {
/* 148:284 */       T[] dataRow = this.data[row];
/* 149:285 */       T[] outDataRow = outData[row];
/* 150:286 */       for (int col = 0; col < nCols; col++)
/* 151:    */       {
/* 152:287 */         T sum = (T)getField().getZero();
/* 153:288 */         for (int i = 0; i < nSum; i++) {
/* 154:289 */           sum = (T)sum.add(dataRow[i].multiply(m.data[i][col]));
/* 155:    */         }
/* 156:291 */         outDataRow[col] = sum;
/* 157:    */       }
/* 158:    */     }
/* 159:295 */     return new Array2DRowFieldMatrix(getField(), outData, false);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public T[][] getData()
/* 163:    */   {
/* 164:302 */     return copyOut();
/* 165:    */   }
/* 166:    */   
/* 167:    */   public T[][] getDataRef()
/* 168:    */   {
/* 169:312 */     return this.data;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setSubMatrix(T[][] subMatrix, int row, int column)
/* 173:    */   {
/* 174:318 */     if (this.data == null)
/* 175:    */     {
/* 176:319 */       if (row > 0) {
/* 177:320 */         throw new MathIllegalStateException(LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, new Object[] { Integer.valueOf(row) });
/* 178:    */       }
/* 179:322 */       if (column > 0) {
/* 180:323 */         throw new MathIllegalStateException(LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, new Object[] { Integer.valueOf(column) });
/* 181:    */       }
/* 182:325 */       int nRows = subMatrix.length;
/* 183:326 */       if (nRows == 0) {
/* 184:327 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/* 185:    */       }
/* 186:330 */       int nCols = subMatrix[0].length;
/* 187:331 */       if (nCols == 0) {
/* 188:332 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/* 189:    */       }
/* 190:334 */       this.data = buildArray(getField(), subMatrix.length, nCols);
/* 191:335 */       for (int i = 0; i < this.data.length; i++)
/* 192:    */       {
/* 193:336 */         if (subMatrix[i].length != nCols) {
/* 194:337 */           throw new DimensionMismatchException(nCols, subMatrix[i].length);
/* 195:    */         }
/* 196:339 */         System.arraycopy(subMatrix[i], 0, this.data[(i + row)], column, nCols);
/* 197:    */       }
/* 198:    */     }
/* 199:    */     else
/* 200:    */     {
/* 201:342 */       super.setSubMatrix(subMatrix, row, column);
/* 202:    */     }
/* 203:    */   }
/* 204:    */   
/* 205:    */   public T getEntry(int row, int column)
/* 206:    */   {
/* 207:350 */     checkRowIndex(row);
/* 208:351 */     checkColumnIndex(column);
/* 209:    */     
/* 210:353 */     return this.data[row][column];
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setEntry(int row, int column, T value)
/* 214:    */   {
/* 215:359 */     checkRowIndex(row);
/* 216:360 */     checkColumnIndex(column);
/* 217:    */     
/* 218:362 */     this.data[row][column] = value;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void addToEntry(int row, int column, T increment)
/* 222:    */   {
/* 223:368 */     checkRowIndex(row);
/* 224:369 */     checkColumnIndex(column);
/* 225:    */     
/* 226:371 */     this.data[row][column] = (T) ((FieldElement)this.data[row][column].add(increment));
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void multiplyEntry(int row, int column, T factor)
/* 230:    */   {
/* 231:377 */     checkRowIndex(row);
/* 232:378 */     checkColumnIndex(column);
/* 233:    */     
/* 234:380 */     this.data[row][column] = (T) ((FieldElement)this.data[row][column].multiply(factor));
/* 235:    */   }
/* 236:    */   
/* 237:    */   public int getRowDimension()
/* 238:    */   {
/* 239:386 */     return this.data == null ? 0 : this.data.length;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public int getColumnDimension()
/* 243:    */   {
/* 244:392 */     return (this.data == null) || (this.data[0] == null) ? 0 : this.data[0].length;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public T[] operate(T[] v)
/* 248:    */   {
/* 249:398 */     int nRows = getRowDimension();
/* 250:399 */     int nCols = getColumnDimension();
/* 251:400 */     if (v.length != nCols) {
/* 252:401 */       throw new DimensionMismatchException(v.length, nCols);
/* 253:    */     }
/* 254:403 */     T[] out = buildArray(getField(), nRows);
/* 255:404 */     for (int row = 0; row < nRows; row++)
/* 256:    */     {
/* 257:405 */       T[] dataRow = this.data[row];
/* 258:406 */       T sum = (T)getField().getZero();
/* 259:407 */       for (int i = 0; i < nCols; i++) {
/* 260:408 */         sum = (T)sum.add(dataRow[i].multiply(v[i]));
/* 261:    */       }
/* 262:410 */       out[row] = sum;
/* 263:    */     }
/* 264:412 */     return out;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public T[] preMultiply(T[] v)
/* 268:    */   {
/* 269:418 */     int nRows = getRowDimension();
/* 270:419 */     int nCols = getColumnDimension();
/* 271:420 */     if (v.length != nRows) {
/* 272:421 */       throw new DimensionMismatchException(v.length, nRows);
/* 273:    */     }
/* 274:424 */     T[] out = buildArray(getField(), nCols);
/* 275:425 */     for (int col = 0; col < nCols; col++)
/* 276:    */     {
/* 277:426 */       T sum = (T)getField().getZero();
/* 278:427 */       for (int i = 0; i < nRows; i++) {
/* 279:428 */         sum = (T)sum.add(this.data[i][col].multiply(v[i]));
/* 280:    */       }
/* 281:430 */       out[col] = sum;
/* 282:    */     }
/* 283:433 */     return out;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor)
/* 287:    */   {
/* 288:439 */     int rows = getRowDimension();
/* 289:440 */     int columns = getColumnDimension();
/* 290:441 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 291:442 */     for (int i = 0; i < rows; i++)
/* 292:    */     {
/* 293:443 */       T[] rowI = this.data[i];
/* 294:444 */       for (int j = 0; j < columns; j++) {
/* 295:445 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 296:    */       }
/* 297:    */     }
/* 298:448 */     return visitor.end();
/* 299:    */   }
/* 300:    */   
/* 301:    */   public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor)
/* 302:    */   {
/* 303:454 */     int rows = getRowDimension();
/* 304:455 */     int columns = getColumnDimension();
/* 305:456 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 306:457 */     for (int i = 0; i < rows; i++)
/* 307:    */     {
/* 308:458 */       T[] rowI = this.data[i];
/* 309:459 */       for (int j = 0; j < columns; j++) {
/* 310:460 */         visitor.visit(i, j, rowI[j]);
/* 311:    */       }
/* 312:    */     }
/* 313:463 */     return visitor.end();
/* 314:    */   }
/* 315:    */   
/* 316:    */   public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 317:    */   {
/* 318:471 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 319:472 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 320:474 */     for (int i = startRow; i <= endRow; i++)
/* 321:    */     {
/* 322:475 */       T[] rowI = this.data[i];
/* 323:476 */       for (int j = startColumn; j <= endColumn; j++) {
/* 324:477 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 325:    */       }
/* 326:    */     }
/* 327:480 */     return visitor.end();
/* 328:    */   }
/* 329:    */   
/* 330:    */   public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 331:    */   {
/* 332:488 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 333:489 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 334:491 */     for (int i = startRow; i <= endRow; i++)
/* 335:    */     {
/* 336:492 */       T[] rowI = this.data[i];
/* 337:493 */       for (int j = startColumn; j <= endColumn; j++) {
/* 338:494 */         visitor.visit(i, j, rowI[j]);
/* 339:    */       }
/* 340:    */     }
/* 341:497 */     return visitor.end();
/* 342:    */   }
/* 343:    */   
/* 344:    */   public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor)
/* 345:    */   {
/* 346:503 */     int rows = getRowDimension();
/* 347:504 */     int columns = getColumnDimension();
/* 348:505 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 349:506 */     for (int j = 0; j < columns; j++) {
/* 350:507 */       for (int i = 0; i < rows; i++)
/* 351:    */       {
/* 352:508 */         T[] rowI = this.data[i];
/* 353:509 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 354:    */       }
/* 355:    */     }
/* 356:512 */     return visitor.end();
/* 357:    */   }
/* 358:    */   
/* 359:    */   public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor)
/* 360:    */   {
/* 361:518 */     int rows = getRowDimension();
/* 362:519 */     int columns = getColumnDimension();
/* 363:520 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 364:521 */     for (int j = 0; j < columns; j++) {
/* 365:522 */       for (int i = 0; i < rows; i++) {
/* 366:523 */         visitor.visit(i, j, this.data[i][j]);
/* 367:    */       }
/* 368:    */     }
/* 369:526 */     return visitor.end();
/* 370:    */   }
/* 371:    */   
/* 372:    */   public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 373:    */   {
/* 374:534 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 375:535 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 376:537 */     for (int j = startColumn; j <= endColumn; j++) {
/* 377:538 */       for (int i = startRow; i <= endRow; i++)
/* 378:    */       {
/* 379:539 */         T[] rowI = this.data[i];
/* 380:540 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 381:    */       }
/* 382:    */     }
/* 383:543 */     return visitor.end();
/* 384:    */   }
/* 385:    */   
/* 386:    */   public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 387:    */   {
/* 388:551 */     checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
/* 389:552 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 390:554 */     for (int j = startColumn; j <= endColumn; j++) {
/* 391:555 */       for (int i = startRow; i <= endRow; i++) {
/* 392:556 */         visitor.visit(i, j, this.data[i][j]);
/* 393:    */       }
/* 394:    */     }
/* 395:559 */     return visitor.end();
/* 396:    */   }
/* 397:    */   
/* 398:    */   private T[][] copyOut()
/* 399:    */   {
/* 400:568 */     int nRows = getRowDimension();
/* 401:569 */     T[][] out = buildArray(getField(), nRows, getColumnDimension());
/* 402:571 */     for (int i = 0; i < nRows; i++) {
/* 403:572 */       System.arraycopy(this.data[i], 0, out[i], 0, this.data[i].length);
/* 404:    */     }
/* 405:574 */     return out;
/* 406:    */   }
/* 407:    */   
/* 408:    */   private void copyIn(T[][] in)
/* 409:    */   {
/* 410:587 */     setSubMatrix(in, 0, 0);
/* 411:    */   }
/* 412:    */
@Override
public FieldMatrix<T> preMultiply(FieldMatrix<T> paramFieldMatrix) {
	// TODO Auto-generated method stub
	return null;
} }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.Array2DRowFieldMatrix
 * JD-Core Version:    0.7.0.1
 */
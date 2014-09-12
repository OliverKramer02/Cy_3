/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   6:    */ import org.apache.commons.math3.exception.NoDataException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.MathUtils;
/*  10:    */ 
/*  11:    */ public class Array2DRowRealMatrix
/*  12:    */   extends AbstractRealMatrix
/*  13:    */   implements Serializable
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = -1067294169172445528L;
/*  16:    */   private double[][] data;
/*  17:    */   
/*  18:    */   public Array2DRowRealMatrix() {}
/*  19:    */   
/*  20:    */   public Array2DRowRealMatrix(int rowDimension, int columnDimension)
/*  21:    */   {
/*  22: 56 */     super(rowDimension, columnDimension);
/*  23: 57 */     this.data = new double[rowDimension][columnDimension];
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Array2DRowRealMatrix(double[][] d)
/*  27:    */     throws DimensionMismatchException, NoDataException, NullArgumentException
/*  28:    */   {
/*  29: 75 */     copyIn(d);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Array2DRowRealMatrix(double[][] d, boolean copyArray)
/*  33:    */   {
/*  34: 96 */     if (copyArray)
/*  35:    */     {
/*  36: 97 */       copyIn(d);
/*  37:    */     }
/*  38:    */     else
/*  39:    */     {
/*  40: 99 */       if (d == null) {
/*  41:100 */         throw new NullArgumentException();
/*  42:    */       }
/*  43:102 */       int nRows = d.length;
/*  44:103 */       if (nRows == 0) {
/*  45:104 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/*  46:    */       }
/*  47:106 */       int nCols = d[0].length;
/*  48:107 */       if (nCols == 0) {
/*  49:108 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/*  50:    */       }
/*  51:110 */       for (int r = 1; r < nRows; r++) {
/*  52:111 */         if (d[r].length != nCols) {
/*  53:112 */           throw new DimensionMismatchException(d[r].length, nCols);
/*  54:    */         }
/*  55:    */       }
/*  56:115 */       this.data = d;
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Array2DRowRealMatrix(double[] v)
/*  61:    */   {
/*  62:127 */     int nRows = v.length;
/*  63:128 */     this.data = new double[nRows][1];
/*  64:129 */     for (int row = 0; row < nRows; row++) {
/*  65:130 */       this.data[row][0] = v[row];
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public RealMatrix createMatrix(int rowDimension, int columnDimension)
/*  70:    */   {
/*  71:138 */     return new Array2DRowRealMatrix(rowDimension, columnDimension);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public RealMatrix copy()
/*  75:    */   {
/*  76:144 */     return new Array2DRowRealMatrix(copyOut(), false);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Array2DRowRealMatrix add(Array2DRowRealMatrix m)
/*  80:    */   {
/*  81:157 */     MatrixUtils.checkAdditionCompatible(this, m);
/*  82:    */     
/*  83:159 */     int rowCount = getRowDimension();
/*  84:160 */     int columnCount = getColumnDimension();
/*  85:161 */     double[][] outData = new double[rowCount][columnCount];
/*  86:162 */     for (int row = 0; row < rowCount; row++)
/*  87:    */     {
/*  88:163 */       double[] dataRow = this.data[row];
/*  89:164 */       double[] mRow = m.data[row];
/*  90:165 */       double[] outDataRow = outData[row];
/*  91:166 */       for (int col = 0; col < columnCount; col++) {
/*  92:167 */         dataRow[col] += mRow[col];
/*  93:    */       }
/*  94:    */     }
/*  95:171 */     return new Array2DRowRealMatrix(outData, false);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Array2DRowRealMatrix subtract(Array2DRowRealMatrix m)
/*  99:    */   {
/* 100:184 */     MatrixUtils.checkSubtractionCompatible(this, m);
/* 101:    */     
/* 102:186 */     int rowCount = getRowDimension();
/* 103:187 */     int columnCount = getColumnDimension();
/* 104:188 */     double[][] outData = new double[rowCount][columnCount];
/* 105:189 */     for (int row = 0; row < rowCount; row++)
/* 106:    */     {
/* 107:190 */       double[] dataRow = this.data[row];
/* 108:191 */       double[] mRow = m.data[row];
/* 109:192 */       double[] outDataRow = outData[row];
/* 110:193 */       for (int col = 0; col < columnCount; col++) {
/* 111:194 */         dataRow[col] -= mRow[col];
/* 112:    */       }
/* 113:    */     }
/* 114:198 */     return new Array2DRowRealMatrix(outData, false);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Array2DRowRealMatrix multiply(Array2DRowRealMatrix m)
/* 118:    */   {
/* 119:211 */     MatrixUtils.checkMultiplicationCompatible(this, m);
/* 120:    */     
/* 121:213 */     int nRows = getRowDimension();
/* 122:214 */     int nCols = m.getColumnDimension();
/* 123:215 */     int nSum = getColumnDimension();
/* 124:    */     
/* 125:217 */     double[][] outData = new double[nRows][nCols];
/* 126:    */     
/* 127:219 */     double[] mCol = new double[nSum];
/* 128:220 */     double[][] mData = m.data;
/* 129:223 */     for (int col = 0; col < nCols; col++)
/* 130:    */     {
/* 131:226 */       for (int mRow = 0; mRow < nSum; mRow++) {
/* 132:227 */         mCol[mRow] = mData[mRow][col];
/* 133:    */       }
/* 134:230 */       for (int row = 0; row < nRows; row++)
/* 135:    */       {
/* 136:231 */         double[] dataRow = this.data[row];
/* 137:232 */         double sum = 0.0D;
/* 138:233 */         for (int i = 0; i < nSum; i++) {
/* 139:234 */           sum += dataRow[i] * mCol[i];
/* 140:    */         }
/* 141:236 */         outData[row][col] = sum;
/* 142:    */       }
/* 143:    */     }
/* 144:240 */     return new Array2DRowRealMatrix(outData, false);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public double[][] getData()
/* 148:    */   {
/* 149:246 */     return copyOut();
/* 150:    */   }
/* 151:    */   
/* 152:    */   public double[][] getDataRef()
/* 153:    */   {
/* 154:255 */     return this.data;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void setSubMatrix(double[][] subMatrix, int row, int column)
/* 158:    */   {
/* 159:262 */     if (this.data == null)
/* 160:    */     {
/* 161:263 */       if (row > 0) {
/* 162:264 */         throw new MathIllegalStateException(LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, new Object[] { Integer.valueOf(row) });
/* 163:    */       }
/* 164:266 */       if (column > 0) {
/* 165:267 */         throw new MathIllegalStateException(LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, new Object[] { Integer.valueOf(column) });
/* 166:    */       }
/* 167:269 */       MathUtils.checkNotNull(subMatrix);
/* 168:270 */       int nRows = subMatrix.length;
/* 169:271 */       if (nRows == 0) {
/* 170:272 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
/* 171:    */       }
/* 172:275 */       int nCols = subMatrix[0].length;
/* 173:276 */       if (nCols == 0) {
/* 174:277 */         throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
/* 175:    */       }
/* 176:279 */       this.data = new double[subMatrix.length][nCols];
/* 177:280 */       for (int i = 0; i < this.data.length; i++)
/* 178:    */       {
/* 179:281 */         if (subMatrix[i].length != nCols) {
/* 180:282 */           throw new DimensionMismatchException(subMatrix[i].length, nCols);
/* 181:    */         }
/* 182:284 */         System.arraycopy(subMatrix[i], 0, this.data[(i + row)], column, nCols);
/* 183:    */       }
/* 184:    */     }
/* 185:    */     else
/* 186:    */     {
/* 187:287 */       super.setSubMatrix(subMatrix, row, column);
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   public double getEntry(int row, int column)
/* 192:    */   {
/* 193:295 */     MatrixUtils.checkMatrixIndex(this, row, column);
/* 194:296 */     return this.data[row][column];
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setEntry(int row, int column, double value)
/* 198:    */   {
/* 199:302 */     MatrixUtils.checkMatrixIndex(this, row, column);
/* 200:303 */     this.data[row][column] = value;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void addToEntry(int row, int column, double increment)
/* 204:    */   {
/* 205:309 */     MatrixUtils.checkMatrixIndex(this, row, column);
/* 206:310 */     this.data[row][column] += increment;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public void multiplyEntry(int row, int column, double factor)
/* 210:    */   {
/* 211:316 */     MatrixUtils.checkMatrixIndex(this, row, column);
/* 212:317 */     this.data[row][column] *= factor;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public int getRowDimension()
/* 216:    */   {
/* 217:323 */     return this.data == null ? 0 : this.data.length;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public int getColumnDimension()
/* 221:    */   {
/* 222:329 */     return (this.data == null) || (this.data[0] == null) ? 0 : this.data[0].length;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public double[] operate(double[] v)
/* 226:    */   {
/* 227:335 */     int nRows = getRowDimension();
/* 228:336 */     int nCols = getColumnDimension();
/* 229:337 */     if (v.length != nCols) {
/* 230:338 */       throw new DimensionMismatchException(v.length, nCols);
/* 231:    */     }
/* 232:340 */     double[] out = new double[nRows];
/* 233:341 */     for (int row = 0; row < nRows; row++)
/* 234:    */     {
/* 235:342 */       double[] dataRow = this.data[row];
/* 236:343 */       double sum = 0.0D;
/* 237:344 */       for (int i = 0; i < nCols; i++) {
/* 238:345 */         sum += dataRow[i] * v[i];
/* 239:    */       }
/* 240:347 */       out[row] = sum;
/* 241:    */     }
/* 242:349 */     return out;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public double[] preMultiply(double[] v)
/* 246:    */   {
/* 247:355 */     int nRows = getRowDimension();
/* 248:356 */     int nCols = getColumnDimension();
/* 249:357 */     if (v.length != nRows) {
/* 250:358 */       throw new DimensionMismatchException(v.length, nRows);
/* 251:    */     }
/* 252:361 */     double[] out = new double[nCols];
/* 253:362 */     for (int col = 0; col < nCols; col++)
/* 254:    */     {
/* 255:363 */       double sum = 0.0D;
/* 256:364 */       for (int i = 0; i < nRows; i++) {
/* 257:365 */         sum += this.data[i][col] * v[i];
/* 258:    */       }
/* 259:367 */       out[col] = sum;
/* 260:    */     }
/* 261:370 */     return out;
/* 262:    */   }
/* 263:    */   
/* 264:    */   public double walkInRowOrder(RealMatrixChangingVisitor visitor)
/* 265:    */   {
/* 266:377 */     int rows = getRowDimension();
/* 267:378 */     int columns = getColumnDimension();
/* 268:379 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 269:380 */     for (int i = 0; i < rows; i++)
/* 270:    */     {
/* 271:381 */       double[] rowI = this.data[i];
/* 272:382 */       for (int j = 0; j < columns; j++) {
/* 273:383 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 274:    */       }
/* 275:    */     }
/* 276:386 */     return visitor.end();
/* 277:    */   }
/* 278:    */   
/* 279:    */   public double walkInRowOrder(RealMatrixPreservingVisitor visitor)
/* 280:    */   {
/* 281:392 */     int rows = getRowDimension();
/* 282:393 */     int columns = getColumnDimension();
/* 283:394 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 284:395 */     for (int i = 0; i < rows; i++)
/* 285:    */     {
/* 286:396 */       double[] rowI = this.data[i];
/* 287:397 */       for (int j = 0; j < columns; j++) {
/* 288:398 */         visitor.visit(i, j, rowI[j]);
/* 289:    */       }
/* 290:    */     }
/* 291:401 */     return visitor.end();
/* 292:    */   }
/* 293:    */   
/* 294:    */   public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 295:    */   {
/* 296:409 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 297:410 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 298:412 */     for (int i = startRow; i <= endRow; i++)
/* 299:    */     {
/* 300:413 */       double[] rowI = this.data[i];
/* 301:414 */       for (int j = startColumn; j <= endColumn; j++) {
/* 302:415 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 303:    */       }
/* 304:    */     }
/* 305:418 */     return visitor.end();
/* 306:    */   }
/* 307:    */   
/* 308:    */   public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 309:    */   {
/* 310:426 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 311:427 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 312:429 */     for (int i = startRow; i <= endRow; i++)
/* 313:    */     {
/* 314:430 */       double[] rowI = this.data[i];
/* 315:431 */       for (int j = startColumn; j <= endColumn; j++) {
/* 316:432 */         visitor.visit(i, j, rowI[j]);
/* 317:    */       }
/* 318:    */     }
/* 319:435 */     return visitor.end();
/* 320:    */   }
/* 321:    */   
/* 322:    */   public double walkInColumnOrder(RealMatrixChangingVisitor visitor)
/* 323:    */   {
/* 324:441 */     int rows = getRowDimension();
/* 325:442 */     int columns = getColumnDimension();
/* 326:443 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 327:444 */     for (int j = 0; j < columns; j++) {
/* 328:445 */       for (int i = 0; i < rows; i++)
/* 329:    */       {
/* 330:446 */         double[] rowI = this.data[i];
/* 331:447 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 332:    */       }
/* 333:    */     }
/* 334:450 */     return visitor.end();
/* 335:    */   }
/* 336:    */   
/* 337:    */   public double walkInColumnOrder(RealMatrixPreservingVisitor visitor)
/* 338:    */   {
/* 339:456 */     int rows = getRowDimension();
/* 340:457 */     int columns = getColumnDimension();
/* 341:458 */     visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
/* 342:459 */     for (int j = 0; j < columns; j++) {
/* 343:460 */       for (int i = 0; i < rows; i++) {
/* 344:461 */         visitor.visit(i, j, this.data[i][j]);
/* 345:    */       }
/* 346:    */     }
/* 347:464 */     return visitor.end();
/* 348:    */   }
/* 349:    */   
/* 350:    */   public double walkInColumnOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 351:    */   {
/* 352:472 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 353:473 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 354:475 */     for (int j = startColumn; j <= endColumn; j++) {
/* 355:476 */       for (int i = startRow; i <= endRow; i++)
/* 356:    */       {
/* 357:477 */         double[] rowI = this.data[i];
/* 358:478 */         rowI[j] = visitor.visit(i, j, rowI[j]);
/* 359:    */       }
/* 360:    */     }
/* 361:481 */     return visitor.end();
/* 362:    */   }
/* 363:    */   
/* 364:    */   public double walkInColumnOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn)
/* 365:    */   {
/* 366:489 */     MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
/* 367:490 */     visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
/* 368:492 */     for (int j = startColumn; j <= endColumn; j++) {
/* 369:493 */       for (int i = startRow; i <= endRow; i++) {
/* 370:494 */         visitor.visit(i, j, this.data[i][j]);
/* 371:    */       }
/* 372:    */     }
/* 373:497 */     return visitor.end();
/* 374:    */   }
/* 375:    */   
/* 376:    */   private double[][] copyOut()
/* 377:    */   {
/* 378:506 */     int nRows = getRowDimension();
/* 379:507 */     double[][] out = new double[nRows][getColumnDimension()];
/* 380:509 */     for (int i = 0; i < nRows; i++) {
/* 381:510 */       System.arraycopy(this.data[i], 0, out[i], 0, this.data[i].length);
/* 382:    */     }
/* 383:512 */     return out;
/* 384:    */   }
/* 385:    */   
/* 386:    */   private void copyIn(double[][] in)
/* 387:    */     throws DimensionMismatchException, NoDataException, NullArgumentException
/* 388:    */   {
/* 389:526 */     setSubMatrix(in, 0, 0);
/* 390:    */   }
/* 391:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.Array2DRowRealMatrix
 * JD-Core Version:    0.7.0.1
 */
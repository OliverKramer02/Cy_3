/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ 
/*   8:    */ public class EigenDecomposition
/*   9:    */ {
/*  10: 69 */   private byte maxIter = 30;
/*  11:    */   private double[] main;
/*  12:    */   private double[] secondary;
/*  13:    */   private TriDiagonalTransformer transformer;
/*  14:    */   private double[] realEigenvalues;
/*  15:    */   private double[] imagEigenvalues;
/*  16:    */   private ArrayRealVector[] eigenvectors;
/*  17:    */   private RealMatrix cachedV;
/*  18:    */   private RealMatrix cachedD;
/*  19:    */   private RealMatrix cachedVt;
/*  20:    */   
/*  21:    */   public EigenDecomposition(RealMatrix matrix, double splitTolerance)
/*  22:    */   {
/*  23:103 */     if (isSymmetric(matrix, true))
/*  24:    */     {
/*  25:104 */       transformToTridiagonal(matrix);
/*  26:105 */       findEigenVectors(this.transformer.getQ().getData());
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   public EigenDecomposition(double[] main, double[] secondary, double splitTolerance)
/*  31:    */   {
/*  32:121 */     this.main = ((double[])main.clone());
/*  33:122 */     this.secondary = ((double[])secondary.clone());
/*  34:123 */     this.transformer = null;
/*  35:124 */     int size = main.length;
/*  36:125 */     double[][] z = new double[size][size];
/*  37:126 */     for (int i = 0; i < size; i++) {
/*  38:127 */       z[i][i] = 1.0D;
/*  39:    */     }
/*  40:129 */     findEigenVectors(z);
/*  41:    */   }
/*  42:    */   
/*  43:    */   private boolean isSymmetric(RealMatrix matrix, boolean raiseException)
/*  44:    */   {
/*  45:144 */     int rows = matrix.getRowDimension();
/*  46:145 */     int columns = matrix.getColumnDimension();
/*  47:146 */     double eps = 10 * rows * columns * 1.110223024625157E-016D;
/*  48:147 */     for (int i = 0; i < rows; i++) {
/*  49:148 */       for (int j = i + 1; j < columns; j++)
/*  50:    */       {
/*  51:149 */         double mij = matrix.getEntry(i, j);
/*  52:150 */         double mji = matrix.getEntry(j, i);
/*  53:151 */         if (FastMath.abs(mij - mji) > FastMath.max(FastMath.abs(mij), FastMath.abs(mji)) * eps)
/*  54:    */         {
/*  55:153 */           if (raiseException) {
/*  56:154 */             throw new NonSymmetricMatrixException(i, j, eps);
/*  57:    */           }
/*  58:156 */           return false;
/*  59:    */         }
/*  60:    */       }
/*  61:    */     }
/*  62:160 */     return true;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public RealMatrix getV()
/*  66:    */   {
/*  67:175 */     if (this.cachedV == null)
/*  68:    */     {
/*  69:176 */       int m = this.eigenvectors.length;
/*  70:177 */       this.cachedV = MatrixUtils.createRealMatrix(m, m);
/*  71:178 */       for (int k = 0; k < m; k++) {
/*  72:179 */         this.cachedV.setColumnVector(k, this.eigenvectors[k]);
/*  73:    */       }
/*  74:    */     }
/*  75:183 */     return this.cachedV;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public RealMatrix getD()
/*  79:    */   {
/*  80:199 */     if (this.cachedD == null) {
/*  81:201 */       this.cachedD = MatrixUtils.createRealDiagonalMatrix(this.realEigenvalues);
/*  82:    */     }
/*  83:203 */     return this.cachedD;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public RealMatrix getVT()
/*  87:    */   {
/*  88:218 */     if (this.cachedVt == null)
/*  89:    */     {
/*  90:219 */       int m = this.eigenvectors.length;
/*  91:220 */       this.cachedVt = MatrixUtils.createRealMatrix(m, m);
/*  92:221 */       for (int k = 0; k < m; k++) {
/*  93:222 */         this.cachedVt.setRowVector(k, this.eigenvectors[k]);
/*  94:    */       }
/*  95:    */     }
/*  96:228 */     return this.cachedVt;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public double[] getRealEigenvalues()
/* 100:    */   {
/* 101:241 */     return (double[])this.realEigenvalues.clone();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public double getRealEigenvalue(int i)
/* 105:    */   {
/* 106:257 */     return this.realEigenvalues[i];
/* 107:    */   }
/* 108:    */   
/* 109:    */   public double[] getImagEigenvalues()
/* 110:    */   {
/* 111:272 */     return (double[])this.imagEigenvalues.clone();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public double getImagEigenvalue(int i)
/* 115:    */   {
/* 116:288 */     return this.imagEigenvalues[i];
/* 117:    */   }
/* 118:    */   
/* 119:    */   public RealVector getEigenvector(int i)
/* 120:    */   {
/* 121:299 */     return this.eigenvectors[i].copy();
/* 122:    */   }
/* 123:    */   
/* 124:    */   public double getDeterminant()
/* 125:    */   {
/* 126:308 */     double determinant = 1.0D;
/* 127:309 */     for (double lambda : this.realEigenvalues) {
/* 128:310 */       determinant *= lambda;
/* 129:    */     }
/* 130:312 */     return determinant;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public DecompositionSolver getSolver()
/* 134:    */   {
/* 135:322 */     return new Solver(this.realEigenvalues, this.imagEigenvalues, this.eigenvectors);
/* 136:    */   }
/* 137:    */   
/* 138:    */   private static class Solver
/* 139:    */     implements DecompositionSolver
/* 140:    */   {
/* 141:    */     private double[] realEigenvalues;
/* 142:    */     private double[] imagEigenvalues;
/* 143:    */     private final ArrayRealVector[] eigenvectors;
/* 144:    */     
/* 145:    */     private Solver(double[] realEigenvalues, double[] imagEigenvalues, ArrayRealVector[] eigenvectors)
/* 146:    */     {
/* 147:344 */       this.realEigenvalues = realEigenvalues;
/* 148:345 */       this.imagEigenvalues = imagEigenvalues;
/* 149:346 */       this.eigenvectors = eigenvectors;
/* 150:    */     }
/* 151:    */     
/* 152:    */     public RealVector solve(RealVector b)
/* 153:    */     {
/* 154:363 */       if (!isNonSingular()) {
/* 155:364 */         throw new SingularMatrixException();
/* 156:    */       }
/* 157:367 */       int m = this.realEigenvalues.length;
/* 158:368 */       if (b.getDimension() != m) {
/* 159:369 */         throw new DimensionMismatchException(b.getDimension(), m);
/* 160:    */       }
/* 161:372 */       double[] bp = new double[m];
/* 162:373 */       for (int i = 0; i < m; i++)
/* 163:    */       {
/* 164:374 */         ArrayRealVector v = this.eigenvectors[i];
/* 165:375 */         double[] vData = v.getDataRef();
/* 166:376 */         double s = v.dotProduct(b) / this.realEigenvalues[i];
/* 167:377 */         for (int j = 0; j < m; j++) {
/* 168:378 */           bp[j] += s * vData[j];
/* 169:    */         }
/* 170:    */       }
/* 171:382 */       return new ArrayRealVector(bp, false);
/* 172:    */     }
/* 173:    */     
/* 174:    */     public RealMatrix solve(RealMatrix b)
/* 175:    */     {
/* 176:388 */       if (!isNonSingular()) {
/* 177:389 */         throw new SingularMatrixException();
/* 178:    */       }
/* 179:392 */       int m = this.realEigenvalues.length;
/* 180:393 */       if (b.getRowDimension() != m) {
/* 181:394 */         throw new DimensionMismatchException(b.getRowDimension(), m);
/* 182:    */       }
/* 183:397 */       int nColB = b.getColumnDimension();
/* 184:398 */       double[][] bp = new double[m][nColB];
/* 185:399 */       double[] tmpCol = new double[m];
/* 186:400 */       for (int k = 0; k < nColB; k++)
/* 187:    */       {
/* 188:401 */         for (int i = 0; i < m; i++)
/* 189:    */         {
/* 190:402 */           tmpCol[i] = b.getEntry(i, k);
/* 191:403 */           bp[i][k] = 0.0D;
/* 192:    */         }
/* 193:405 */         for (int i = 0; i < m; i++)
/* 194:    */         {
/* 195:406 */           ArrayRealVector v = this.eigenvectors[i];
/* 196:407 */           double[] vData = v.getDataRef();
/* 197:408 */           double s = 0.0D;
/* 198:409 */           for (int j = 0; j < m; j++) {
/* 199:410 */             s += v.getEntry(j) * tmpCol[j];
/* 200:    */           }
/* 201:412 */           s /= this.realEigenvalues[i];
/* 202:413 */           for (int j = 0; j < m; j++) {
/* 203:414 */             bp[j][k] += s * vData[j];
/* 204:    */           }
/* 205:    */         }
/* 206:    */       }
/* 207:419 */       return new Array2DRowRealMatrix(bp, false);
/* 208:    */     }
/* 209:    */     
/* 210:    */     public boolean isNonSingular()
/* 211:    */     {
/* 212:429 */       for (int i = 0; i < this.realEigenvalues.length; i++) {
/* 213:430 */         if ((this.realEigenvalues[i] == 0.0D) && (this.imagEigenvalues[i] == 0.0D)) {
/* 214:432 */           return false;
/* 215:    */         }
/* 216:    */       }
/* 217:435 */       return true;
/* 218:    */     }
/* 219:    */     
/* 220:    */     public RealMatrix getInverse()
/* 221:    */     {
/* 222:445 */       if (!isNonSingular()) {
/* 223:446 */         throw new SingularMatrixException();
/* 224:    */       }
/* 225:449 */       int m = this.realEigenvalues.length;
/* 226:450 */       double[][] invData = new double[m][m];
/* 227:452 */       for (int i = 0; i < m; i++)
/* 228:    */       {
/* 229:453 */         double[] invI = invData[i];
/* 230:454 */         for (int j = 0; j < m; j++)
/* 231:    */         {
/* 232:455 */           double invIJ = 0.0D;
/* 233:456 */           for (int k = 0; k < m; k++)
/* 234:    */           {
/* 235:457 */             double[] vK = this.eigenvectors[k].getDataRef();
/* 236:458 */             invIJ += vK[i] * vK[j] / this.realEigenvalues[k];
/* 237:    */           }
/* 238:460 */           invI[j] = invIJ;
/* 239:    */         }
/* 240:    */       }
/* 241:463 */       return MatrixUtils.createRealMatrix(invData);
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   private void transformToTridiagonal(RealMatrix matrix)
/* 246:    */   {
/* 247:474 */     this.transformer = new TriDiagonalTransformer(matrix);
/* 248:475 */     this.main = this.transformer.getMainDiagonalRef();
/* 249:476 */     this.secondary = this.transformer.getSecondaryDiagonalRef();
/* 250:    */   }
/* 251:    */   
/* 252:    */   private void findEigenVectors(double[][] householderMatrix)
/* 253:    */   {
/* 254:486 */     double[][] z = (double[][])householderMatrix.clone();
/* 255:487 */     int n = this.main.length;
/* 256:488 */     this.realEigenvalues = new double[n];
/* 257:489 */     this.imagEigenvalues = new double[n];
/* 258:490 */     double[] e = new double[n];
/* 259:491 */     for (int i = 0; i < n - 1; i++)
/* 260:    */     {
/* 261:492 */       this.realEigenvalues[i] = this.main[i];
/* 262:493 */       e[i] = this.secondary[i];
/* 263:    */     }
/* 264:495 */     this.realEigenvalues[(n - 1)] = this.main[(n - 1)];
/* 265:496 */     e[(n - 1)] = 0.0D;
/* 266:    */     
/* 267:    */ 
/* 268:499 */     double maxAbsoluteValue = 0.0D;
/* 269:500 */     for (int i = 0; i < n; i++)
/* 270:    */     {
/* 271:501 */       if (FastMath.abs(this.realEigenvalues[i]) > maxAbsoluteValue) {
/* 272:502 */         maxAbsoluteValue = FastMath.abs(this.realEigenvalues[i]);
/* 273:    */       }
/* 274:504 */       if (FastMath.abs(e[i]) > maxAbsoluteValue) {
/* 275:505 */         maxAbsoluteValue = FastMath.abs(e[i]);
/* 276:    */       }
/* 277:    */     }
/* 278:509 */     if (maxAbsoluteValue != 0.0D) {
/* 279:510 */       for (int i = 0; i < n; i++)
/* 280:    */       {
/* 281:511 */         if (FastMath.abs(this.realEigenvalues[i]) <= 1.110223024625157E-016D * maxAbsoluteValue) {
/* 282:512 */           this.realEigenvalues[i] = 0.0D;
/* 283:    */         }
/* 284:514 */         if (FastMath.abs(e[i]) <= 1.110223024625157E-016D * maxAbsoluteValue) {
/* 285:515 */           e[i] = 0.0D;
/* 286:    */         }
/* 287:    */       }
/* 288:    */     }
/* 289:520 */     for (int j = 0; j < n; j++)
/* 290:    */     {
/* 291:521 */       int its = 0;
/* 292:    */       int m;
/* 293:    */       do
/* 294:    */       {
/* 295:524 */         for (m = j; m < n - 1; m++)
/* 296:    */         {
/* 297:525 */           double delta = FastMath.abs(this.realEigenvalues[m]) + FastMath.abs(this.realEigenvalues[(m + 1)]);
/* 298:527 */           if (FastMath.abs(e[m]) + delta == delta) {
/* 299:    */             break;
/* 300:    */           }
/* 301:    */         }
/* 302:531 */         if (m != j)
/* 303:    */         {
/* 304:532 */           if (its == this.maxIter) {
/* 305:533 */             throw new MaxCountExceededException(LocalizedFormats.CONVERGENCE_FAILED, Byte.valueOf(this.maxIter), new Object[0]);
/* 306:    */           }
/* 307:536 */           its++;
/* 308:537 */           double q = (this.realEigenvalues[(j + 1)] - this.realEigenvalues[j]) / (2.0D * e[j]);
/* 309:538 */           double t = FastMath.sqrt(1.0D + q * q);
/* 310:539 */           if (q < 0.0D) {
/* 311:540 */             q = this.realEigenvalues[m] - this.realEigenvalues[j] + e[j] / (q - t);
/* 312:    */           } else {
/* 313:542 */             q = this.realEigenvalues[m] - this.realEigenvalues[j] + e[j] / (q + t);
/* 314:    */           }
/* 315:544 */           double u = 0.0D;
/* 316:545 */           double s = 1.0D;
/* 317:546 */           double c = 1.0D;
/* 318:548 */           for (int i = m - 1; i >= j; i--)
/* 319:    */           {
/* 320:549 */             double p = s * e[i];
/* 321:550 */             double h = c * e[i];
/* 322:551 */             if (FastMath.abs(p) >= FastMath.abs(q))
/* 323:    */             {
/* 324:552 */               c = q / p;
/* 325:553 */               t = FastMath.sqrt(c * c + 1.0D);
/* 326:554 */               e[(i + 1)] = (p * t);
/* 327:555 */               s = 1.0D / t;
/* 328:556 */               c *= s;
/* 329:    */             }
/* 330:    */             else
/* 331:    */             {
/* 332:558 */               s = p / q;
/* 333:559 */               t = FastMath.sqrt(s * s + 1.0D);
/* 334:560 */               e[(i + 1)] = (q * t);
/* 335:561 */               c = 1.0D / t;
/* 336:562 */               s *= c;
/* 337:    */             }
/* 338:564 */             if (e[(i + 1)] == 0.0D)
/* 339:    */             {
/* 340:565 */               this.realEigenvalues[(i + 1)] -= u;
/* 341:566 */               e[m] = 0.0D;
/* 342:567 */               break;
/* 343:    */             }
/* 344:569 */             q = this.realEigenvalues[(i + 1)] - u;
/* 345:570 */             t = (this.realEigenvalues[i] - q) * s + 2.0D * c * h;
/* 346:571 */             u = s * t;
/* 347:572 */             this.realEigenvalues[(i + 1)] = (q + u);
/* 348:573 */             q = c * t - h;
/* 349:574 */             for (int ia = 0; ia < n; ia++)
/* 350:    */             {
/* 351:575 */               p = z[ia][(i + 1)];
/* 352:576 */               z[ia][(i + 1)] = (s * z[ia][i] + c * p);
/* 353:577 */               z[ia][i] = (c * z[ia][i] - s * p);
/* 354:    */             }
/* 355:    */           }
int i = 0;
/* 356:580 */           if ((t != 0.0D) || (i < j))
/* 357:    */           {
/* 358:583 */             this.realEigenvalues[j] -= u;
/* 359:584 */             e[j] = q;
/* 360:585 */             e[m] = 0.0D;
/* 361:    */           }
/* 362:    */         }
/* 363:587 */       } while (m != j);
/* 364:    */     }
/* 365:591 */     for (int i = 0; i < n; i++)
/* 366:    */     {
/* 367:592 */       int k = i;
/* 368:593 */       double p = this.realEigenvalues[i];
/* 369:594 */       for (int j = i + 1; j < n; j++) {
/* 370:595 */         if (this.realEigenvalues[j] > p)
/* 371:    */         {
/* 372:596 */           k = j;
/* 373:597 */           p = this.realEigenvalues[j];
/* 374:    */         }
/* 375:    */       }
/* 376:600 */       if (k != i)
/* 377:    */       {
/* 378:601 */         this.realEigenvalues[k] = this.realEigenvalues[i];
/* 379:602 */         this.realEigenvalues[i] = p;
/* 380:603 */         for (int j = 0; j < n; j++)
/* 381:    */         {
/* 382:604 */           p = z[j][i];
/* 383:605 */           z[j][i] = z[j][k];
/* 384:606 */           z[j][k] = p;
/* 385:    */         }
/* 386:    */       }
/* 387:    */     }
/* 388:612 */     maxAbsoluteValue = 0.0D;
/* 389:613 */     for (int i = 0; i < n; i++) {
/* 390:614 */       if (FastMath.abs(this.realEigenvalues[i]) > maxAbsoluteValue) {
/* 391:615 */         maxAbsoluteValue = FastMath.abs(this.realEigenvalues[i]);
/* 392:    */       }
/* 393:    */     }
/* 394:619 */     if (maxAbsoluteValue != 0.0D) {
/* 395:620 */       for (int i = 0; i < n; i++) {
/* 396:621 */         if (FastMath.abs(this.realEigenvalues[i]) < 1.110223024625157E-016D * maxAbsoluteValue) {
/* 397:622 */           this.realEigenvalues[i] = 0.0D;
/* 398:    */         }
/* 399:    */       }
/* 400:    */     }
/* 401:626 */     this.eigenvectors = new ArrayRealVector[n];
/* 402:627 */     double[] tmp = new double[n];
/* 403:628 */     for (int i = 0; i < n; i++)
/* 404:    */     {
/* 405:629 */       for (int j = 0; j < n; j++) {
/* 406:630 */         tmp[j] = z[j][i];
/* 407:    */       }
/* 408:632 */       this.eigenvectors[i] = new ArrayRealVector(tmp);
/* 409:    */     }
/* 410:    */   }
/* 411:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.EigenDecomposition
 * JD-Core Version:    0.7.0.1
 */
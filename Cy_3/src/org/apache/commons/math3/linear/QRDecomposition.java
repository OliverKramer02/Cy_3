/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.util.FastMath;
/*   6:    */ 
/*   7:    */ public class QRDecomposition
/*   8:    */ {
/*   9:    */   private double[][] qrt;
/*  10:    */   private double[] rDiag;
/*  11:    */   private RealMatrix cachedQ;
/*  12:    */   private RealMatrix cachedQT;
/*  13:    */   private RealMatrix cachedR;
/*  14:    */   private RealMatrix cachedH;
/*  15:    */   private final double threshold;
/*  16:    */   
/*  17:    */   public QRDecomposition(RealMatrix matrix)
/*  18:    */   {
/*  19: 81 */     this(matrix, 0.0D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public QRDecomposition(RealMatrix matrix, double threshold)
/*  23:    */   {
/*  24: 92 */     this.threshold = threshold;
/*  25:    */     
/*  26: 94 */     int m = matrix.getRowDimension();
/*  27: 95 */     int n = matrix.getColumnDimension();
/*  28: 96 */     this.qrt = matrix.transpose().getData();
/*  29: 97 */     this.rDiag = new double[FastMath.min(m, n)];
/*  30: 98 */     this.cachedQ = null;
/*  31: 99 */     this.cachedQT = null;
/*  32:100 */     this.cachedR = null;
/*  33:101 */     this.cachedH = null;
/*  34:108 */     for (int minor = 0; minor < FastMath.min(m, n); minor++)
/*  35:    */     {
/*  36:110 */       double[] qrtMinor = this.qrt[minor];
/*  37:    */       
/*  38:    */ 
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45:119 */       double xNormSqr = 0.0D;
/*  46:120 */       for (int row = minor; row < m; row++)
/*  47:    */       {
/*  48:121 */         double c = qrtMinor[row];
/*  49:122 */         xNormSqr += c * c;
/*  50:    */       }
/*  51:124 */       double a = qrtMinor[minor] > 0.0D ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
/*  52:125 */       this.rDiag[minor] = a;
/*  53:127 */       if (a != 0.0D)
/*  54:    */       {
/*  55:137 */         qrtMinor[minor] -= a;
/*  56:151 */         for (int col = minor + 1; col < n; col++)
/*  57:    */         {
/*  58:152 */           double[] qrtCol = this.qrt[col];
/*  59:153 */           double alpha = 0.0D;
/*  60:154 */           for (int row = minor; row < m; row++) {
/*  61:155 */             alpha -= qrtCol[row] * qrtMinor[row];
/*  62:    */           }
/*  63:157 */           alpha /= a * qrtMinor[minor];
/*  64:160 */           for (int row = minor; row < m; row++) {
/*  65:161 */             qrtCol[row] -= alpha * qrtMinor[row];
/*  66:    */           }
/*  67:    */         }
/*  68:    */       }
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public RealMatrix getR()
/*  73:    */   {
/*  74:175 */     if (this.cachedR == null)
/*  75:    */     {
/*  76:178 */       int n = this.qrt.length;
/*  77:179 */       int m = this.qrt[0].length;
/*  78:180 */       double[][] ra = new double[m][n];
/*  79:182 */       for (int row = FastMath.min(m, n) - 1; row >= 0; row--)
/*  80:    */       {
/*  81:183 */         ra[row][row] = this.rDiag[row];
/*  82:184 */         for (int col = row + 1; col < n; col++) {
/*  83:185 */           ra[row][col] = this.qrt[col][row];
/*  84:    */         }
/*  85:    */       }
/*  86:188 */       this.cachedR = MatrixUtils.createRealMatrix(ra);
/*  87:    */     }
/*  88:192 */     return this.cachedR;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public RealMatrix getQ()
/*  92:    */   {
/*  93:201 */     if (this.cachedQ == null) {
/*  94:202 */       this.cachedQ = getQT().transpose();
/*  95:    */     }
/*  96:204 */     return this.cachedQ;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public RealMatrix getQT()
/* 100:    */   {
/* 101:213 */     if (this.cachedQT == null)
/* 102:    */     {
/* 103:216 */       int n = this.qrt.length;
/* 104:217 */       int m = this.qrt[0].length;
/* 105:218 */       double[][] qta = new double[m][m];
/* 106:225 */       for (int minor = m - 1; minor >= FastMath.min(m, n); minor--) {
/* 107:226 */         qta[minor][minor] = 1.0D;
/* 108:    */       }
/* 109:229 */       for (int minor = FastMath.min(m, n) - 1; minor >= 0; minor--)
/* 110:    */       {
/* 111:230 */         double[] qrtMinor = this.qrt[minor];
/* 112:231 */         qta[minor][minor] = 1.0D;
/* 113:232 */         if (qrtMinor[minor] != 0.0D) {
/* 114:233 */           for (int col = minor; col < m; col++)
/* 115:    */           {
/* 116:234 */             double alpha = 0.0D;
/* 117:235 */             for (int row = minor; row < m; row++) {
/* 118:236 */               alpha -= qta[col][row] * qrtMinor[row];
/* 119:    */             }
/* 120:238 */             alpha /= this.rDiag[minor] * qrtMinor[minor];
/* 121:240 */             for (int row = minor; row < m; row++) {
/* 122:241 */               qta[col][row] += -alpha * qrtMinor[row];
/* 123:    */             }
/* 124:    */           }
/* 125:    */         }
/* 126:    */       }
/* 127:246 */       this.cachedQT = MatrixUtils.createRealMatrix(qta);
/* 128:    */     }
/* 129:250 */     return this.cachedQT;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public RealMatrix getH()
/* 133:    */   {
/* 134:261 */     if (this.cachedH == null)
/* 135:    */     {
/* 136:263 */       int n = this.qrt.length;
/* 137:264 */       int m = this.qrt[0].length;
/* 138:265 */       double[][] ha = new double[m][n];
/* 139:266 */       for (int i = 0; i < m; i++) {
/* 140:267 */         for (int j = 0; j < FastMath.min(i + 1, n); j++) {
/* 141:268 */           ha[i][j] = (this.qrt[j][i] / -this.rDiag[j]);
/* 142:    */         }
/* 143:    */       }
/* 144:271 */       this.cachedH = MatrixUtils.createRealMatrix(ha);
/* 145:    */     }
/* 146:275 */     return this.cachedH;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public DecompositionSolver getSolver()
/* 150:    */   {
/* 151:283 */     return new Solver(this.qrt, this.rDiag, this.threshold);
/* 152:    */   }
/* 153:    */   
/* 154:    */   private static class Solver
/* 155:    */     implements DecompositionSolver
/* 156:    */   {
/* 157:    */     private final double[][] qrt;
/* 158:    */     private final double[] rDiag;
/* 159:    */     private final double threshold;
/* 160:    */     
/* 161:    */     private Solver(double[][] qrt, double[] rDiag, double threshold)
/* 162:    */     {
/* 163:310 */       this.qrt = qrt;
/* 164:311 */       this.rDiag = rDiag;
/* 165:312 */       this.threshold = threshold;
/* 166:    */     }
/* 167:    */     
/* 168:    */     public boolean isNonSingular()
/* 169:    */     {
/* 170:317 */       for (double diag : this.rDiag) {
/* 171:318 */         if (FastMath.abs(diag) <= this.threshold) {
/* 172:319 */           return false;
/* 173:    */         }
/* 174:    */       }
/* 175:322 */       return true;
/* 176:    */     }
/* 177:    */     
/* 178:    */     public RealVector solve(RealVector b)
/* 179:    */     {
/* 180:327 */       int n = this.qrt.length;
/* 181:328 */       int m = this.qrt[0].length;
/* 182:329 */       if (b.getDimension() != m) {
/* 183:330 */         throw new DimensionMismatchException(b.getDimension(), m);
/* 184:    */       }
/* 185:332 */       if (!isNonSingular()) {
/* 186:333 */         throw new SingularMatrixException();
/* 187:    */       }
/* 188:336 */       double[] x = new double[n];
/* 189:337 */       double[] y = b.toArray();
/* 190:340 */       for (int minor = 0; minor < FastMath.min(m, n); minor++)
/* 191:    */       {
/* 192:342 */         double[] qrtMinor = this.qrt[minor];
/* 193:343 */         double dotProduct = 0.0D;
/* 194:344 */         for (int row = minor; row < m; row++) {
/* 195:345 */           dotProduct += y[row] * qrtMinor[row];
/* 196:    */         }
/* 197:347 */         dotProduct /= this.rDiag[minor] * qrtMinor[minor];
/* 198:349 */         for (int row = minor; row < m; row++) {
/* 199:350 */           y[row] += dotProduct * qrtMinor[row];
/* 200:    */         }
/* 201:    */       }
/* 202:355 */       for (int row = this.rDiag.length - 1; row >= 0; row--)
/* 203:    */       {
/* 204:356 */         y[row] /= this.rDiag[row];
/* 205:357 */         double yRow = y[row];
/* 206:358 */         double[] qrtRow = this.qrt[row];
/* 207:359 */         x[row] = yRow;
/* 208:360 */         for (int i = 0; i < row; i++) {
/* 209:361 */           y[i] -= yRow * qrtRow[i];
/* 210:    */         }
/* 211:    */       }
/* 212:365 */       return new ArrayRealVector(x, false);
/* 213:    */     }
/* 214:    */     
/* 215:    */     public RealMatrix solve(RealMatrix b)
/* 216:    */     {
/* 217:370 */       int n = this.qrt.length;
/* 218:371 */       int m = this.qrt[0].length;
/* 219:372 */       if (b.getRowDimension() != m) {
/* 220:373 */         throw new DimensionMismatchException(b.getRowDimension(), m);
/* 221:    */       }
/* 222:375 */       if (!isNonSingular()) {
/* 223:376 */         throw new SingularMatrixException();
/* 224:    */       }
/* 225:379 */       int columns = b.getColumnDimension();
/* 226:380 */       int blockSize = 52;
/* 227:381 */       int cBlocks = (columns + 52 - 1) / 52;
/* 228:382 */       double[][] xBlocks = BlockRealMatrix.createBlocksLayout(n, columns);
/* 229:383 */       double[][] y = new double[b.getRowDimension()][52];
/* 230:384 */       double[] alpha = new double[52];
/* 231:386 */       for (int kBlock = 0; kBlock < cBlocks; kBlock++)
/* 232:    */       {
/* 233:387 */         int kStart = kBlock * 52;
/* 234:388 */         int kEnd = FastMath.min(kStart + 52, columns);
/* 235:389 */         int kWidth = kEnd - kStart;
/* 236:    */         
/* 237:    */ 
/* 238:392 */         b.copySubMatrix(0, m - 1, kStart, kEnd - 1, y);
/* 239:395 */         for (int minor = 0; minor < FastMath.min(m, n); minor++)
/* 240:    */         {
/* 241:396 */           double[] qrtMinor = this.qrt[minor];
/* 242:397 */           double factor = 1.0D / (this.rDiag[minor] * qrtMinor[minor]);
/* 243:    */           
/* 244:399 */           Arrays.fill(alpha, 0, kWidth, 0.0D);
/* 245:400 */           for (int row = minor; row < m; row++)
/* 246:    */           {
/* 247:401 */             double d = qrtMinor[row];
/* 248:402 */             double[] yRow = y[row];
/* 249:403 */             for (int k = 0; k < kWidth; k++) {
/* 250:404 */               alpha[k] += d * yRow[k];
/* 251:    */             }
/* 252:    */           }
/* 253:407 */           for (int k = 0; k < kWidth; k++) {
/* 254:408 */             alpha[k] *= factor;
/* 255:    */           }
/* 256:411 */           for (int row = minor; row < m; row++)
/* 257:    */           {
/* 258:412 */             double d = qrtMinor[row];
/* 259:413 */             double[] yRow = y[row];
/* 260:414 */             for (int k = 0; k < kWidth; k++) {
/* 261:415 */               yRow[k] += alpha[k] * d;
/* 262:    */             }
/* 263:    */           }
/* 264:    */         }
/* 265:421 */         for (int j = this.rDiag.length - 1; j >= 0; j--)
/* 266:    */         {
/* 267:422 */           int jBlock = j / 52;
/* 268:423 */           int jStart = jBlock * 52;
/* 269:424 */           double factor = 1.0D / this.rDiag[j];
/* 270:425 */           double[] yJ = y[j];
/* 271:426 */           double[] xBlock = xBlocks[(jBlock * cBlocks + kBlock)];
/* 272:427 */           int index = (j - jStart) * kWidth;
/* 273:428 */           for (int k = 0; k < kWidth; k++)
/* 274:    */           {
/* 275:429 */             yJ[k] *= factor;
/* 276:430 */             xBlock[(index++)] = yJ[k];
/* 277:    */           }
/* 278:433 */           double[] qrtJ = this.qrt[j];
/* 279:434 */           for (int i = 0; i < j; i++)
/* 280:    */           {
/* 281:435 */             double rIJ = qrtJ[i];
/* 282:436 */             double[] yI = y[i];
/* 283:437 */             for (int k = 0; k < kWidth; k++) {
/* 284:438 */               yI[k] -= yJ[k] * rIJ;
/* 285:    */             }
/* 286:    */           }
/* 287:    */         }
/* 288:    */       }
/* 289:444 */       return new BlockRealMatrix(n, columns, xBlocks, false);
/* 290:    */     }
/* 291:    */     
/* 292:    */     public RealMatrix getInverse()
/* 293:    */     {
/* 294:449 */       return solve(MatrixUtils.createRealIdentityMatrix(this.rDiag.length));
/* 295:    */     }
/* 296:    */   }
/* 297:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.QRDecomposition
 * JD-Core Version:    0.7.0.1
 */
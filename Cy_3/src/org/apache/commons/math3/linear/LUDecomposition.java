/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public class LUDecomposition
/*   7:    */ {
/*   8:    */   private static final double DEFAULT_TOO_SMALL = 9.999999999999999E-012D;
/*   9:    */   private final double[][] lu;
/*  10:    */   private final int[] pivot;
/*  11:    */   private boolean even;
/*  12:    */   private boolean singular;
/*  13:    */   private RealMatrix cachedL;
/*  14:    */   private RealMatrix cachedU;
/*  15:    */   private RealMatrix cachedP;
/*  16:    */   
/*  17:    */   public LUDecomposition(RealMatrix matrix)
/*  18:    */   {
/*  19: 76 */     this(matrix, 9.999999999999999E-012D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public LUDecomposition(RealMatrix matrix, double singularityThreshold)
/*  23:    */   {
/*  24: 87 */     if (!matrix.isSquare()) {
/*  25: 88 */       throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
/*  26:    */     }
/*  27: 92 */     int m = matrix.getColumnDimension();
/*  28: 93 */     this.lu = matrix.getData();
/*  29: 94 */     this.pivot = new int[m];
/*  30: 95 */     this.cachedL = null;
/*  31: 96 */     this.cachedU = null;
/*  32: 97 */     this.cachedP = null;
/*  33:100 */     for (int row = 0; row < m; row++) {
/*  34:101 */       this.pivot[row] = row;
/*  35:    */     }
/*  36:103 */     this.even = true;
/*  37:104 */     this.singular = false;
/*  38:107 */     for (int col = 0; col < m; col++)
/*  39:    */     {
/*  40:110 */       for (int row = 0; row < col; row++)
/*  41:    */       {
/*  42:111 */         double[] luRow = this.lu[row];
/*  43:112 */         double sum = luRow[col];
/*  44:113 */         for (int i = 0; i < row; i++) {
/*  45:114 */           sum -= luRow[i] * this.lu[i][col];
/*  46:    */         }
/*  47:116 */         luRow[col] = sum;
/*  48:    */       }
/*  49:120 */       int max = col;
/*  50:121 */       double largest = (-1.0D / 0.0D);
/*  51:122 */       for (int row = col; row < m; row++)
/*  52:    */       {
/*  53:123 */         double[] luRow = this.lu[row];
/*  54:124 */         double sum = luRow[col];
/*  55:125 */         for (int i = 0; i < col; i++) {
/*  56:126 */           sum -= luRow[i] * this.lu[i][col];
/*  57:    */         }
/*  58:128 */         luRow[col] = sum;
/*  59:131 */         if (FastMath.abs(sum) > largest)
/*  60:    */         {
/*  61:132 */           largest = FastMath.abs(sum);
/*  62:133 */           max = row;
/*  63:    */         }
/*  64:    */       }
/*  65:138 */       if (FastMath.abs(this.lu[max][col]) < singularityThreshold)
/*  66:    */       {
/*  67:139 */         this.singular = true;
/*  68:140 */         return;
/*  69:    */       }
/*  70:144 */       if (max != col)
/*  71:    */       {
/*  72:145 */         double tmp = 0.0D;
/*  73:146 */         double[] luMax = this.lu[max];
/*  74:147 */         double[] luCol = this.lu[col];
/*  75:148 */         for (int i = 0; i < m; i++)
/*  76:    */         {
/*  77:149 */           tmp = luMax[i];
/*  78:150 */           luMax[i] = luCol[i];
/*  79:151 */           luCol[i] = tmp;
/*  80:    */         }
/*  81:153 */         int temp = this.pivot[max];
/*  82:154 */         this.pivot[max] = this.pivot[col];
/*  83:155 */         this.pivot[col] = temp;
/*  84:156 */         this.even = (!this.even);
/*  85:    */       }
/*  86:160 */       double luDiag = this.lu[col][col];
/*  87:161 */       for (int row = col + 1; row < m; row++) {
/*  88:162 */         this.lu[row][col] /= luDiag;
/*  89:    */       }
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public RealMatrix getL()
/*  94:    */   {
/*  95:173 */     if ((this.cachedL == null) && (!this.singular))
/*  96:    */     {
/*  97:174 */       int m = this.pivot.length;
/*  98:175 */       this.cachedL = MatrixUtils.createRealMatrix(m, m);
/*  99:176 */       for (int i = 0; i < m; i++)
/* 100:    */       {
/* 101:177 */         double[] luI = this.lu[i];
/* 102:178 */         for (int j = 0; j < i; j++) {
/* 103:179 */           this.cachedL.setEntry(i, j, luI[j]);
/* 104:    */         }
/* 105:181 */         this.cachedL.setEntry(i, i, 1.0D);
/* 106:    */       }
/* 107:    */     }
/* 108:184 */     return this.cachedL;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public RealMatrix getU()
/* 112:    */   {
/* 113:193 */     if ((this.cachedU == null) && (!this.singular))
/* 114:    */     {
/* 115:194 */       int m = this.pivot.length;
/* 116:195 */       this.cachedU = MatrixUtils.createRealMatrix(m, m);
/* 117:196 */       for (int i = 0; i < m; i++)
/* 118:    */       {
/* 119:197 */         double[] luI = this.lu[i];
/* 120:198 */         for (int j = i; j < m; j++) {
/* 121:199 */           this.cachedU.setEntry(i, j, luI[j]);
/* 122:    */         }
/* 123:    */       }
/* 124:    */     }
/* 125:203 */     return this.cachedU;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public RealMatrix getP()
/* 129:    */   {
/* 130:216 */     if ((this.cachedP == null) && (!this.singular))
/* 131:    */     {
/* 132:217 */       int m = this.pivot.length;
/* 133:218 */       this.cachedP = MatrixUtils.createRealMatrix(m, m);
/* 134:219 */       for (int i = 0; i < m; i++) {
/* 135:220 */         this.cachedP.setEntry(i, this.pivot[i], 1.0D);
/* 136:    */       }
/* 137:    */     }
/* 138:223 */     return this.cachedP;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public int[] getPivot()
/* 142:    */   {
/* 143:232 */     return (int[])this.pivot.clone();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public double getDeterminant()
/* 147:    */   {
/* 148:240 */     if (this.singular) {
/* 149:241 */       return 0.0D;
/* 150:    */     }
/* 151:243 */     int m = this.pivot.length;
/* 152:244 */     double determinant = this.even ? 1.0D : -1.0D;
/* 153:245 */     for (int i = 0; i < m; i++) {
/* 154:246 */       determinant *= this.lu[i][i];
/* 155:    */     }
/* 156:248 */     return determinant;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public DecompositionSolver getSolver()
/* 160:    */   {
/* 161:258 */     return new Solver(this.lu, this.pivot, this.singular);
/* 162:    */   }
/* 163:    */   
/* 164:    */   private static class Solver
/* 165:    */     implements DecompositionSolver
/* 166:    */   {
/* 167:    */     private final double[][] lu;
/* 168:    */     private final int[] pivot;
/* 169:    */     private final boolean singular;
/* 170:    */     
/* 171:    */     private Solver(double[][] lu, int[] pivot, boolean singular)
/* 172:    */     {
/* 173:280 */       this.lu = lu;
/* 174:281 */       this.pivot = pivot;
/* 175:282 */       this.singular = singular;
/* 176:    */     }
/* 177:    */     
/* 178:    */     public boolean isNonSingular()
/* 179:    */     {
/* 180:287 */       return !this.singular;
/* 181:    */     }
/* 182:    */     
/* 183:    */     public RealVector solve(RealVector b)
/* 184:    */     {
/* 185:292 */       int m = this.pivot.length;
/* 186:293 */       if (b.getDimension() != m) {
/* 187:294 */         throw new DimensionMismatchException(b.getDimension(), m);
/* 188:    */       }
/* 189:296 */       if (this.singular) {
/* 190:297 */         throw new SingularMatrixException();
/* 191:    */       }
/* 192:300 */       double[] bp = new double[m];
/* 193:303 */       for (int row = 0; row < m; row++) {
/* 194:304 */         bp[row] = b.getEntry(this.pivot[row]);
/* 195:    */       }
/* 196:308 */       for (int col = 0; col < m; col++)
/* 197:    */       {
/* 198:309 */         double bpCol = bp[col];
/* 199:310 */         for (int i = col + 1; i < m; i++) {
/* 200:311 */           bp[i] -= bpCol * this.lu[i][col];
/* 201:    */         }
/* 202:    */       }
/* 203:316 */       for (int col = m - 1; col >= 0; col--)
/* 204:    */       {
/* 205:317 */         bp[col] /= this.lu[col][col];
/* 206:318 */         double bpCol = bp[col];
/* 207:319 */         for (int i = 0; i < col; i++) {
/* 208:320 */           bp[i] -= bpCol * this.lu[i][col];
/* 209:    */         }
/* 210:    */       }
/* 211:324 */       return new ArrayRealVector(bp, false);
/* 212:    */     }
/* 213:    */     
/* 214:    */     public RealMatrix solve(RealMatrix b)
/* 215:    */     {
/* 216:330 */       int m = this.pivot.length;
/* 217:331 */       if (b.getRowDimension() != m) {
/* 218:332 */         throw new DimensionMismatchException(b.getRowDimension(), m);
/* 219:    */       }
/* 220:334 */       if (this.singular) {
/* 221:335 */         throw new SingularMatrixException();
/* 222:    */       }
/* 223:338 */       int nColB = b.getColumnDimension();
/* 224:    */       
/* 225:    */ 
/* 226:341 */       double[][] bp = new double[m][nColB];
/* 227:342 */       for (int row = 0; row < m; row++)
/* 228:    */       {
/* 229:343 */         double[] bpRow = bp[row];
/* 230:344 */         int pRow = this.pivot[row];
/* 231:345 */         for (int col = 0; col < nColB; col++) {
/* 232:346 */           bpRow[col] = b.getEntry(pRow, col);
/* 233:    */         }
/* 234:    */       }
/* 235:351 */       for (int col = 0; col < m; col++)
/* 236:    */       {
/* 237:352 */         double[] bpCol = bp[col];
/* 238:353 */         for (int i = col + 1; i < m; i++)
/* 239:    */         {
/* 240:354 */           double[] bpI = bp[i];
/* 241:355 */           double luICol = this.lu[i][col];
/* 242:356 */           for (int j = 0; j < nColB; j++) {
/* 243:357 */             bpI[j] -= bpCol[j] * luICol;
/* 244:    */           }
/* 245:    */         }
/* 246:    */       }
/* 247:363 */       for (int col = m - 1; col >= 0; col--)
/* 248:    */       {
/* 249:364 */         double[] bpCol = bp[col];
/* 250:365 */         double luDiag = this.lu[col][col];
/* 251:366 */         for (int j = 0; j < nColB; j++) {
/* 252:367 */           bpCol[j] /= luDiag;
/* 253:    */         }
/* 254:369 */         for (int i = 0; i < col; i++)
/* 255:    */         {
/* 256:370 */           double[] bpI = bp[i];
/* 257:371 */           double luICol = this.lu[i][col];
/* 258:372 */           for (int j = 0; j < nColB; j++) {
/* 259:373 */             bpI[j] -= bpCol[j] * luICol;
/* 260:    */           }
/* 261:    */         }
/* 262:    */       }
/* 263:378 */       return new Array2DRowRealMatrix(bp, false);
/* 264:    */     }
/* 265:    */     
/* 266:    */     public RealMatrix getInverse()
/* 267:    */     {
/* 268:383 */       return solve(MatrixUtils.createRealIdentityMatrix(this.pivot.length));
/* 269:    */     }
/* 270:    */   }
/* 271:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.LUDecomposition
 * JD-Core Version:    0.7.0.1
 */
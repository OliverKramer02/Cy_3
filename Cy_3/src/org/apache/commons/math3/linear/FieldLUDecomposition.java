/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import org.apache.commons.math3.Field;
/*   5:    */ import org.apache.commons.math3.FieldElement;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ 
/*   8:    */ public class FieldLUDecomposition<T extends FieldElement<T>>
/*   9:    */ {
/*  10:    */   private final Field<T> field;
/*  11:    */   private T[][] lu;
/*  12:    */   private int[] pivot;
/*  13:    */   private boolean even;
/*  14:    */   private boolean singular;
/*  15:    */   private FieldMatrix<T> cachedL;
/*  16:    */   private FieldMatrix<T> cachedU;
/*  17:    */   private FieldMatrix<T> cachedP;
/*  18:    */   
/*  19:    */   public FieldLUDecomposition(FieldMatrix<T> matrix)
/*  20:    */   {
/*  21: 87 */     if (!matrix.isSquare()) {
/*  22: 88 */       throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
/*  23:    */     }
/*  24: 92 */     int m = matrix.getColumnDimension();
/*  25: 93 */     this.field = matrix.getField();
/*  26: 94 */     this.lu = matrix.getData();
/*  27: 95 */     this.pivot = new int[m];
/*  28: 96 */     this.cachedL = null;
/*  29: 97 */     this.cachedU = null;
/*  30: 98 */     this.cachedP = null;
/*  31:101 */     for (int row = 0; row < m; row++) {
/*  32:102 */       this.pivot[row] = row;
/*  33:    */     }
/*  34:104 */     this.even = true;
/*  35:105 */     this.singular = false;
/*  36:108 */     for (int col = 0; col < m; col++)
/*  37:    */     {
/*  38:110 */       T sum = (T)this.field.getZero();
/*  39:113 */       for (int row = 0; row < col; row++)
/*  40:    */       {
/*  41:114 */         T[] luRow = this.lu[row];
/*  42:115 */         sum = luRow[col];
/*  43:116 */         for (int i = 0; i < row; i++) {
/*  44:117 */           sum = (T)sum.subtract(luRow[i].multiply(this.lu[i][col]));
/*  45:    */         }
/*  46:119 */         luRow[col] = sum;
/*  47:    */       }
/*  48:123 */       int nonZero = col;
/*  49:124 */       for (int row = col; row < m; row++)
/*  50:    */       {
/*  51:125 */         T[] luRow = this.lu[row];
/*  52:126 */         sum = luRow[col];
/*  53:127 */         for (int i = 0; i < col; i++) {
/*  54:128 */           sum = (T)sum.subtract(luRow[i].multiply(this.lu[i][col]));
/*  55:    */         }
/*  56:130 */         luRow[col] = sum;
/*  57:132 */         if (this.lu[nonZero][col].equals(this.field.getZero())) {
/*  58:134 */           nonZero++;
/*  59:    */         }
/*  60:    */       }
/*  61:139 */       if (nonZero >= m)
/*  62:    */       {
/*  63:140 */         this.singular = true;
/*  64:141 */         return;
/*  65:    */       }
/*  66:145 */       if (nonZero != col)
/*  67:    */       {
/*  68:146 */         T tmp = (T)this.field.getZero();
/*  69:147 */         for (int i = 0; i < m; i++)
/*  70:    */         {
/*  71:148 */           tmp = this.lu[nonZero][i];
/*  72:149 */           this.lu[nonZero][i] = this.lu[col][i];
/*  73:150 */           this.lu[col][i] = tmp;
/*  74:    */         }
/*  75:152 */         int temp = this.pivot[nonZero];
/*  76:153 */         this.pivot[nonZero] = this.pivot[col];
/*  77:154 */         this.pivot[col] = temp;
/*  78:155 */         this.even = (!this.even);
/*  79:    */       }
/*  80:159 */       T luDiag = this.lu[col][col];
/*  81:160 */       for (int row = col + 1; row < m; row++)
/*  82:    */       {
/*  83:161 */         T[] luRow = this.lu[row];
/*  84:162 */         luRow[col] = (T) ((FieldElement)luRow[col].divide(luDiag));
/*  85:    */       }
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public FieldMatrix<T> getL()
/*  90:    */   {
/*  91:174 */     if ((this.cachedL == null) && (!this.singular))
/*  92:    */     {
/*  93:175 */       int m = this.pivot.length;
/*  94:176 */       this.cachedL = new Array2DRowFieldMatrix(this.field, m, m);
/*  95:177 */       for (int i = 0; i < m; i++)
/*  96:    */       {
/*  97:178 */         T[] luI = this.lu[i];
/*  98:179 */         for (int j = 0; j < i; j++) {
/*  99:180 */           this.cachedL.setEntry(i, j, luI[j]);
/* 100:    */         }
/* 101:182 */         this.cachedL.setEntry(i, i, (T)this.field.getOne());
/* 102:    */       }
/* 103:    */     }
/* 104:185 */     return this.cachedL;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public FieldMatrix<T> getU()
/* 108:    */   {
/* 109:194 */     if ((this.cachedU == null) && (!this.singular))
/* 110:    */     {
/* 111:195 */       int m = this.pivot.length;
/* 112:196 */       this.cachedU = new Array2DRowFieldMatrix(this.field, m, m);
/* 113:197 */       for (int i = 0; i < m; i++)
/* 114:    */       {
/* 115:198 */         T[] luI = this.lu[i];
/* 116:199 */         for (int j = i; j < m; j++) {
/* 117:200 */           this.cachedU.setEntry(i, j, luI[j]);
/* 118:    */         }
/* 119:    */       }
/* 120:    */     }
/* 121:204 */     return this.cachedU;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public FieldMatrix<T> getP()
/* 125:    */   {
/* 126:217 */     if ((this.cachedP == null) && (!this.singular))
/* 127:    */     {
/* 128:218 */       int m = this.pivot.length;
/* 129:219 */       this.cachedP = new Array2DRowFieldMatrix(this.field, m, m);
/* 130:220 */       for (int i = 0; i < m; i++) {
/* 131:221 */         this.cachedP.setEntry(i, this.pivot[i], (T)this.field.getOne());
/* 132:    */       }
/* 133:    */     }
/* 134:224 */     return this.cachedP;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public int[] getPivot()
/* 138:    */   {
/* 139:233 */     return (int[])this.pivot.clone();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public T getDeterminant()
/* 143:    */   {
/* 144:241 */     if (this.singular) {
/* 145:242 */       return (T)this.field.getZero();
/* 146:    */     }
/* 147:244 */     int m = this.pivot.length;
/* 148:245 */     T determinant = (T) (this.even ? (FieldElement)this.field.getOne() : (FieldElement)((FieldElement)this.field.getZero()).subtract(this.field.getOne()));
/* 149:246 */     for (int i = 0; i < m; i++) {
/* 150:247 */       determinant = (T)determinant.multiply(this.lu[i][i]);
/* 151:    */     }
/* 152:249 */     return determinant;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public FieldDecompositionSolver<T> getSolver()
/* 156:    */   {
/* 157:258 */     return new Solver(this.field, this.lu, this.pivot, this.singular);
/* 158:    */   }
/* 159:    */   
/* 160:    */   private static class Solver<T extends FieldElement<T>>
/* 161:    */     implements FieldDecompositionSolver<T>
/* 162:    */   {
/* 163:    */     private final Field<T> field;
/* 164:    */     private final T[][] lu;
/* 165:    */     private final int[] pivot;
/* 166:    */     private final boolean singular;
/* 167:    */     
/* 168:    */     private Solver(Field<T> field, T[][] lu, int[] pivot, boolean singular)
/* 169:    */     {
/* 170:285 */       this.field = field;
/* 171:286 */       this.lu = lu;
/* 172:287 */       this.pivot = pivot;
/* 173:288 */       this.singular = singular;
/* 174:    */     }
/* 175:    */     
/* 176:    */     public boolean isNonSingular()
/* 177:    */     {
/* 178:293 */       return !this.singular;
/* 179:    */     }
/* 180:    */     
/* 181:    */     public FieldVector<T> solve(FieldVector<T> b)
/* 182:    */     {
/* 183:    */       try
/* 184:    */       {
/* 185:299 */         return solve((ArrayFieldVector)b);
/* 186:    */       }
/* 187:    */       catch (ClassCastException cce)
/* 188:    */       {
/* 189:302 */         int m = this.pivot.length;
/* 190:303 */         if (b.getDimension() != m) {
/* 191:304 */           throw new DimensionMismatchException(b.getDimension(), m);
/* 192:    */         }
/* 193:306 */         if (this.singular) {
/* 194:307 */           throw new SingularMatrixException();
/* 195:    */         }
/* 196:311 */         T[] bp = (T[])Array.newInstance(this.field.getRuntimeClass(), m);
/* 197:314 */         for (int row = 0; row < m; row++) {
/* 198:315 */           bp[row] = b.getEntry(this.pivot[row]);
/* 199:    */         }
/* 200:319 */         for (int col = 0; col < m; col++)
/* 201:    */         {
/* 202:320 */           T bpCol = bp[col];
/* 203:321 */           for (int i = col + 1; i < m; i++) {
/* 204:322 */             bp[i] = (T) ((FieldElement)bp[i].subtract(bpCol.multiply(this.lu[i][col])));
/* 205:    */           }
/* 206:    */         }
/* 207:327 */         for (int col = m - 1; col >= 0; col--)
/* 208:    */         {
/* 209:328 */           bp[col] = (T) ((FieldElement)bp[col].divide(this.lu[col][col]));
/* 210:329 */           T bpCol = bp[col];
/* 211:330 */           for (int i = 0; i < col; i++) {
/* 212:331 */             bp[i] = (T) ((FieldElement)bp[i].subtract(bpCol.multiply(this.lu[i][col])));
/* 213:    */           }
/* 214:    */         }
/* 215:335 */         return new ArrayFieldVector(this.field, bp, false);
/* 216:    */       }
/* 217:    */     }
/* 218:    */     
/* 219:    */     public ArrayFieldVector<T> solve(ArrayFieldVector<T> b)
/* 220:    */     {
/* 221:348 */       int m = this.pivot.length;
/* 222:349 */       int length = b.getDimension();
/* 223:350 */       if (length != m) {
/* 224:351 */         throw new DimensionMismatchException(length, m);
/* 225:    */       }
/* 226:353 */       if (this.singular) {
/* 227:354 */         throw new SingularMatrixException();
/* 228:    */       }
/* 229:359 */       T[] bp = (T[])Array.newInstance(this.field.getRuntimeClass(), m);
/* 230:363 */       for (int row = 0; row < m; row++) {
/* 231:364 */         bp[row] = b.getEntry(this.pivot[row]);
/* 232:    */       }
/* 233:368 */       for (int col = 0; col < m; col++)
/* 234:    */       {
/* 235:369 */         T bpCol = bp[col];
/* 236:370 */         for (int i = col + 1; i < m; i++) {
/* 237:371 */           bp[i] = (T) ((FieldElement)bp[i].subtract(bpCol.multiply(this.lu[i][col])));
/* 238:    */         }
/* 239:    */       }
/* 240:376 */       for (int col = m - 1; col >= 0; col--)
/* 241:    */       {
/* 242:377 */         bp[col] = (T) ((FieldElement)bp[col].divide(this.lu[col][col]));
/* 243:378 */         T bpCol = bp[col];
/* 244:379 */         for (int i = 0; i < col; i++) {
/* 245:380 */           bp[i] = (T) ((FieldElement)bp[i].subtract(bpCol.multiply(this.lu[i][col])));
/* 246:    */         }
/* 247:    */       }
/* 248:384 */       return new ArrayFieldVector(bp, false);
/* 249:    */     }
/* 250:    */     
/* 251:    */     public FieldMatrix<T> solve(FieldMatrix<T> b)
/* 252:    */     {
/* 253:389 */       int m = this.pivot.length;
/* 254:390 */       if (b.getRowDimension() != m) {
/* 255:391 */         throw new DimensionMismatchException(b.getRowDimension(), m);
/* 256:    */       }
/* 257:393 */       if (this.singular) {
/* 258:394 */         throw new SingularMatrixException();
/* 259:    */       }
/* 260:397 */       int nColB = b.getColumnDimension();
/* 261:    */       
/* 262:    */ 
/* 263:    */ 
/* 264:401 */       T[][] bp = (T[][])Array.newInstance(this.field.getRuntimeClass(), new int[] { m, nColB });
/* 265:402 */       for (int row = 0; row < m; row++)
/* 266:    */       {
/* 267:403 */         T[] bpRow = bp[row];
/* 268:404 */         int pRow = this.pivot[row];
/* 269:405 */         for (int col = 0; col < nColB; col++) {
/* 270:406 */           bpRow[col] = b.getEntry(pRow, col);
/* 271:    */         }
/* 272:    */       }
/* 273:411 */       for (int col = 0; col < m; col++)
/* 274:    */       {
/* 275:412 */         T[] bpCol = bp[col];
/* 276:413 */         for (int i = col + 1; i < m; i++)
/* 277:    */         {
/* 278:414 */           T[] bpI = bp[i];
/* 279:415 */           T luICol = this.lu[i][col];
/* 280:416 */           for (int j = 0; j < nColB; j++) {
/* 281:417 */             bpI[j] = (T) ((FieldElement)bpI[j].subtract(bpCol[j].multiply(luICol)));
/* 282:    */           }
/* 283:    */         }
/* 284:    */       }
/* 285:423 */       for (int col = m - 1; col >= 0; col--)
/* 286:    */       {
/* 287:424 */         T[] bpCol = bp[col];
/* 288:425 */         T luDiag = this.lu[col][col];
/* 289:426 */         for (int j = 0; j < nColB; j++) {
/* 290:427 */           bpCol[j] = (T) ((FieldElement)bpCol[j].divide(luDiag));
/* 291:    */         }
/* 292:429 */         for (int i = 0; i < col; i++)
/* 293:    */         {
/* 294:430 */           T[] bpI = bp[i];
/* 295:431 */           T luICol = this.lu[i][col];
/* 296:432 */           for (int j = 0; j < nColB; j++) {
/* 297:433 */             bpI[j] = (T) ((FieldElement)bpI[j].subtract(bpCol[j].multiply(luICol)));
/* 298:    */           }
/* 299:    */         }
/* 300:    */       }
/* 301:438 */       return new Array2DRowFieldMatrix(this.field, bp, false);
/* 302:    */     }
/* 303:    */     
/* 304:    */     public FieldMatrix<T> getInverse()
/* 305:    */     {
/* 306:444 */       int m = this.pivot.length;
/* 307:445 */       T one = (T)this.field.getOne();
/* 308:446 */       FieldMatrix<T> identity = new Array2DRowFieldMatrix(this.field, m, m);
/* 309:447 */       for (int i = 0; i < m; i++) {
/* 310:448 */         identity.setEntry(i, i, one);
/* 311:    */       }
/* 312:450 */       return solve(identity);
/* 313:    */     }
/* 314:    */   }
/* 315:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.FieldLUDecomposition
 * JD-Core Version:    0.7.0.1
 */
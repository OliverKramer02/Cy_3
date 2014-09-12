/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ class BiDiagonalTransformer
/*   6:    */ {
/*   7:    */   private final double[][] householderVectors;
/*   8:    */   private final double[] main;
/*   9:    */   private final double[] secondary;
/*  10:    */   private RealMatrix cachedU;
/*  11:    */   private RealMatrix cachedB;
/*  12:    */   private RealMatrix cachedV;
/*  13:    */   
/*  14:    */   public BiDiagonalTransformer(RealMatrix matrix)
/*  15:    */   {
/*  16: 64 */     int m = matrix.getRowDimension();
/*  17: 65 */     int n = matrix.getColumnDimension();
/*  18: 66 */     int p = FastMath.min(m, n);
/*  19: 67 */     this.householderVectors = matrix.getData();
/*  20: 68 */     this.main = new double[p];
/*  21: 69 */     this.secondary = new double[p - 1];
/*  22: 70 */     this.cachedU = null;
/*  23: 71 */     this.cachedB = null;
/*  24: 72 */     this.cachedV = null;
/*  25: 75 */     if (m >= n) {
/*  26: 76 */       transformToUpperBiDiagonal();
/*  27:    */     } else {
/*  28: 78 */       transformToLowerBiDiagonal();
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public RealMatrix getU()
/*  33:    */   {
/*  34: 90 */     if (this.cachedU == null)
/*  35:    */     {
/*  36: 92 */       int m = this.householderVectors.length;
/*  37: 93 */       int n = this.householderVectors[0].length;
/*  38: 94 */       int p = this.main.length;
/*  39: 95 */       int diagOffset = m >= n ? 0 : 1;
/*  40: 96 */       double[] diagonal = m >= n ? this.main : this.secondary;
/*  41: 97 */       double[][] ua = new double[m][m];
/*  42:100 */       for (int k = m - 1; k >= p; k--) {
/*  43:101 */         ua[k][k] = 1.0D;
/*  44:    */       }
/*  45:105 */       for (int k = p - 1; k >= diagOffset; k--)
/*  46:    */       {
/*  47:106 */         double[] hK = this.householderVectors[k];
/*  48:107 */         ua[k][k] = 1.0D;
/*  49:108 */         if (hK[(k - diagOffset)] != 0.0D) {
/*  50:109 */           for (int j = k; j < m; j++)
/*  51:    */           {
/*  52:110 */             double alpha = 0.0D;
/*  53:111 */             for (int i = k; i < m; i++) {
/*  54:112 */               alpha -= ua[i][j] * this.householderVectors[i][(k - diagOffset)];
/*  55:    */             }
/*  56:114 */             alpha /= diagonal[(k - diagOffset)] * hK[(k - diagOffset)];
/*  57:116 */             for (int i = k; i < m; i++) {
/*  58:117 */               ua[i][j] += -alpha * this.householderVectors[i][(k - diagOffset)];
/*  59:    */             }
/*  60:    */           }
/*  61:    */         }
/*  62:    */       }
/*  63:122 */       if (diagOffset > 0) {
/*  64:123 */         ua[0][0] = 1.0D;
/*  65:    */       }
/*  66:125 */       this.cachedU = MatrixUtils.createRealMatrix(ua);
/*  67:    */     }
/*  68:129 */     return this.cachedU;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public RealMatrix getB()
/*  72:    */   {
/*  73:139 */     if (this.cachedB == null)
/*  74:    */     {
/*  75:141 */       int m = this.householderVectors.length;
/*  76:142 */       int n = this.householderVectors[0].length;
/*  77:143 */       double[][] ba = new double[m][n];
/*  78:144 */       for (int i = 0; i < this.main.length; i++)
/*  79:    */       {
/*  80:145 */         ba[i][i] = this.main[i];
/*  81:146 */         if (m < n)
/*  82:    */         {
/*  83:147 */           if (i > 0) {
/*  84:148 */             ba[i][(i - 1)] = this.secondary[(i - 1)];
/*  85:    */           }
/*  86:    */         }
/*  87:151 */         else if (i < this.main.length - 1) {
/*  88:152 */           ba[i][(i + 1)] = this.secondary[i];
/*  89:    */         }
/*  90:    */       }
/*  91:156 */       this.cachedB = MatrixUtils.createRealMatrix(ba);
/*  92:    */     }
/*  93:160 */     return this.cachedB;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public RealMatrix getV()
/*  97:    */   {
/*  98:171 */     if (this.cachedV == null)
/*  99:    */     {
/* 100:173 */       int m = this.householderVectors.length;
/* 101:174 */       int n = this.householderVectors[0].length;
/* 102:175 */       int p = this.main.length;
/* 103:176 */       int diagOffset = m >= n ? 1 : 0;
/* 104:177 */       double[] diagonal = m >= n ? this.secondary : this.main;
/* 105:178 */       double[][] va = new double[n][n];
/* 106:181 */       for (int k = n - 1; k >= p; k--) {
/* 107:182 */         va[k][k] = 1.0D;
/* 108:    */       }
/* 109:186 */       for (int k = p - 1; k >= diagOffset; k--)
/* 110:    */       {
/* 111:187 */         double[] hK = this.householderVectors[(k - diagOffset)];
/* 112:188 */         va[k][k] = 1.0D;
/* 113:189 */         if (hK[k] != 0.0D) {
/* 114:190 */           for (int j = k; j < n; j++)
/* 115:    */           {
/* 116:191 */             double beta = 0.0D;
/* 117:192 */             for (int i = k; i < n; i++) {
/* 118:193 */               beta -= va[i][j] * hK[i];
/* 119:    */             }
/* 120:195 */             beta /= diagonal[(k - diagOffset)] * hK[k];
/* 121:197 */             for (int i = k; i < n; i++) {
/* 122:198 */               va[i][j] += -beta * hK[i];
/* 123:    */             }
/* 124:    */           }
/* 125:    */         }
/* 126:    */       }
/* 127:203 */       if (diagOffset > 0) {
/* 128:204 */         va[0][0] = 1.0D;
/* 129:    */       }
/* 130:206 */       this.cachedV = MatrixUtils.createRealMatrix(va);
/* 131:    */     }
/* 132:210 */     return this.cachedV;
/* 133:    */   }
/* 134:    */   
/* 135:    */   double[][] getHouseholderVectorsRef()
/* 136:    */   {
/* 137:221 */     return this.householderVectors;
/* 138:    */   }
/* 139:    */   
/* 140:    */   double[] getMainDiagonalRef()
/* 141:    */   {
/* 142:231 */     return this.main;
/* 143:    */   }
/* 144:    */   
/* 145:    */   double[] getSecondaryDiagonalRef()
/* 146:    */   {
/* 147:241 */     return this.secondary;
/* 148:    */   }
/* 149:    */   
/* 150:    */   boolean isUpperBiDiagonal()
/* 151:    */   {
/* 152:249 */     return this.householderVectors.length >= this.householderVectors[0].length;
/* 153:    */   }
/* 154:    */   
/* 155:    */   private void transformToUpperBiDiagonal()
/* 156:    */   {
/* 157:259 */     int m = this.householderVectors.length;
/* 158:260 */     int n = this.householderVectors[0].length;
/* 159:261 */     for (int k = 0; k < n; k++)
/* 160:    */     {
/* 161:264 */       double xNormSqr = 0.0D;
/* 162:265 */       for (int i = k; i < m; i++)
/* 163:    */       {
/* 164:266 */         double c = this.householderVectors[i][k];
/* 165:267 */         xNormSqr += c * c;
/* 166:    */       }
/* 167:269 */       double[] hK = this.householderVectors[k];
/* 168:270 */       double a = hK[k] > 0.0D ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
/* 169:271 */       this.main[k] = a;
/* 170:272 */       if (a != 0.0D)
/* 171:    */       {
/* 172:273 */         hK[k] -= a;
/* 173:274 */         for (int j = k + 1; j < n; j++)
/* 174:    */         {
/* 175:275 */           double alpha = 0.0D;
/* 176:276 */           for (int i = k; i < m; i++)
/* 177:    */           {
/* 178:277 */             double[] hI = this.householderVectors[i];
/* 179:278 */             alpha -= hI[j] * hI[k];
/* 180:    */           }
/* 181:280 */           alpha /= a * this.householderVectors[k][k];
/* 182:281 */           for (int i = k; i < m; i++)
/* 183:    */           {
/* 184:282 */             double[] hI = this.householderVectors[i];
/* 185:283 */             hI[j] -= alpha * hI[k];
/* 186:    */           }
/* 187:    */         }
/* 188:    */       }
/* 189:288 */       if (k < n - 1)
/* 190:    */       {
/* 191:290 */         xNormSqr = 0.0D;
/* 192:291 */         for (int j = k + 1; j < n; j++)
/* 193:    */         {
/* 194:292 */           double c = hK[j];
/* 195:293 */           xNormSqr += c * c;
/* 196:    */         }
/* 197:295 */         double b = hK[(k + 1)] > 0.0D ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
/* 198:296 */         this.secondary[k] = b;
/* 199:297 */         if (b != 0.0D)
/* 200:    */         {
/* 201:298 */           hK[(k + 1)] -= b;
/* 202:299 */           for (int i = k + 1; i < m; i++)
/* 203:    */           {
/* 204:300 */             double[] hI = this.householderVectors[i];
/* 205:301 */             double beta = 0.0D;
/* 206:302 */             for (int j = k + 1; j < n; j++) {
/* 207:303 */               beta -= hI[j] * hK[j];
/* 208:    */             }
/* 209:305 */             beta /= b * hK[(k + 1)];
/* 210:306 */             for (int j = k + 1; j < n; j++) {
/* 211:307 */               hI[j] -= beta * hK[j];
/* 212:    */             }
/* 213:    */           }
/* 214:    */         }
/* 215:    */       }
/* 216:    */     }
/* 217:    */   }
/* 218:    */   
/* 219:    */   private void transformToLowerBiDiagonal()
/* 220:    */   {
/* 221:323 */     int m = this.householderVectors.length;
/* 222:324 */     int n = this.householderVectors[0].length;
/* 223:325 */     for (int k = 0; k < m; k++)
/* 224:    */     {
/* 225:328 */       double[] hK = this.householderVectors[k];
/* 226:329 */       double xNormSqr = 0.0D;
/* 227:330 */       for (int j = k; j < n; j++)
/* 228:    */       {
/* 229:331 */         double c = hK[j];
/* 230:332 */         xNormSqr += c * c;
/* 231:    */       }
/* 232:334 */       double a = hK[k] > 0.0D ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
/* 233:335 */       this.main[k] = a;
/* 234:336 */       if (a != 0.0D)
/* 235:    */       {
/* 236:337 */         hK[k] -= a;
/* 237:338 */         for (int i = k + 1; i < m; i++)
/* 238:    */         {
/* 239:339 */           double[] hI = this.householderVectors[i];
/* 240:340 */           double alpha = 0.0D;
/* 241:341 */           for (int j = k; j < n; j++) {
/* 242:342 */             alpha -= hI[j] * hK[j];
/* 243:    */           }
/* 244:344 */           alpha /= a * this.householderVectors[k][k];
/* 245:345 */           for (int j = k; j < n; j++) {
/* 246:346 */             hI[j] -= alpha * hK[j];
/* 247:    */           }
/* 248:    */         }
/* 249:    */       }
/* 250:351 */       if (k < m - 1)
/* 251:    */       {
/* 252:353 */         double[] hKp1 = this.householderVectors[(k + 1)];
/* 253:354 */         xNormSqr = 0.0D;
/* 254:355 */         for (int i = k + 1; i < m; i++)
/* 255:    */         {
/* 256:356 */           double c = this.householderVectors[i][k];
/* 257:357 */           xNormSqr += c * c;
/* 258:    */         }
/* 259:359 */         double b = hKp1[k] > 0.0D ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
/* 260:360 */         this.secondary[k] = b;
/* 261:361 */         if (b != 0.0D)
/* 262:    */         {
/* 263:362 */           hKp1[k] -= b;
/* 264:363 */           for (int j = k + 1; j < n; j++)
/* 265:    */           {
/* 266:364 */             double beta = 0.0D;
/* 267:365 */             for (int i = k + 1; i < m; i++)
/* 268:    */             {
/* 269:366 */               double[] hI = this.householderVectors[i];
/* 270:367 */               beta -= hI[j] * hI[k];
/* 271:    */             }
/* 272:369 */             beta /= b * hKp1[k];
/* 273:370 */             for (int i = k + 1; i < m; i++)
/* 274:    */             {
/* 275:371 */               double[] hI = this.householderVectors[i];
/* 276:372 */               hI[j] -= beta * hI[k];
/* 277:    */             }
/* 278:    */           }
/* 279:    */         }
/* 280:    */       }
/* 281:    */     }
/* 282:    */   }
/* 283:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.BiDiagonalTransformer
 * JD-Core Version:    0.7.0.1
 */
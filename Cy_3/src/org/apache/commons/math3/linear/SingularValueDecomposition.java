/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.util.FastMath;
/*   6:    */ 
/*   7:    */ public class SingularValueDecomposition
/*   8:    */ {
/*   9:    */   private static final double EPS = 2.220446049250313E-016D;
/*  10:    */   private static final double TINY = 1.603334688007178E-291D;
/*  11:    */   private final double[] singularValues;
/*  12:    */   private final int m;
/*  13:    */   private final int n;
/*  14:    */   private final boolean transposed;
/*  15:    */   private final RealMatrix cachedU;
/*  16:    */   private RealMatrix cachedUt;
/*  17:    */   private RealMatrix cachedS;
/*  18:    */   private final RealMatrix cachedV;
/*  19:    */   private RealMatrix cachedVt;
/*  20:    */   private final double tol;
/*  21:    */   
/*  22:    */   public SingularValueDecomposition(RealMatrix matrix)
/*  23:    */   {
/*  24:    */     double[][] A = null;
/*  25: 92 */     if (matrix.getRowDimension() < matrix.getColumnDimension())
/*  26:    */     {
/*  27: 93 */       this.transposed = true;
/*  28: 94 */       double[][] A1 = matrix.transpose().getData();
/*  29: 95 */       this.m = matrix.getColumnDimension();
/*  30: 96 */       this.n = matrix.getRowDimension();
/*  31:    */     }
/*  32:    */     else
/*  33:    */     {
/*  34: 98 */       this.transposed = false;
/*  35: 99 */       A = matrix.getData();
/*  36:100 */       this.m = matrix.getRowDimension();
/*  37:101 */       this.n = matrix.getColumnDimension();
/*  38:    */     }
/*  39:104 */     this.singularValues = new double[this.n];
/*  40:105 */     double[][] U = new double[this.m][this.n];
/*  41:106 */     double[][] V = new double[this.n][this.n];
/*  42:107 */     double[] e = new double[this.n];
/*  43:108 */     double[] work = new double[this.m];
/*  44:    */     
/*  45:    */ 
/*  46:111 */     int nct = FastMath.min(this.m - 1, this.n);
/*  47:112 */     int nrt = FastMath.max(0, this.n - 2);
/*  48:113 */     for (int k = 0; k < FastMath.max(nct, nrt); k++)
/*  49:    */     {
/*  50:114 */       if (k < nct)
/*  51:    */       {
/*  52:118 */         this.singularValues[k] = 0.0D;
/*  53:119 */         for (int i = k; i < this.m; i++) {
/*  54:120 */           this.singularValues[k] = FastMath.hypot(this.singularValues[k], A[i][k]);
/*  55:    */         }
/*  56:122 */         if (this.singularValues[k] != 0.0D)
/*  57:    */         {
/*  58:123 */           if (A[k][k] < 0.0D) {
/*  59:124 */             this.singularValues[k] = (-this.singularValues[k]);
/*  60:    */           }
/*  61:126 */           for (int i = k; i < this.m; i++) {
/*  62:127 */             A[i][k] /= this.singularValues[k];
/*  63:    */           }
/*  64:129 */           A[k][k] += 1.0D;
/*  65:    */         }
/*  66:131 */         this.singularValues[k] = (-this.singularValues[k]);
/*  67:    */       }
/*  68:133 */       for (int j = k + 1; j < this.n; j++)
/*  69:    */       {
/*  70:134 */         if ((k < nct) && (this.singularValues[k] != 0.0D))
/*  71:    */         {
/*  72:137 */           double t = 0.0D;
/*  73:138 */           for (int i = k; i < this.m; i++) {
/*  74:139 */             t += A[i][k] * A[i][j];
/*  75:    */           }
/*  76:141 */           t = -t / A[k][k];
/*  77:142 */           for (int i = k; i < this.m; i++) {
/*  78:143 */             A[i][j] += t * A[i][k];
/*  79:    */           }
/*  80:    */         }
/*  81:148 */         e[j] = A[k][j];
/*  82:    */       }
/*  83:150 */       if (k < nct) {
/*  84:153 */         for (int i = k; i < this.m; i++) {
/*  85:154 */           U[i][k] = A[i][k];
/*  86:    */         }
/*  87:    */       }
/*  88:157 */       if (k < nrt)
/*  89:    */       {
/*  90:161 */         e[k] = 0.0D;
/*  91:162 */         for (int i = k + 1; i < this.n; i++) {
/*  92:163 */           e[k] = FastMath.hypot(e[k], e[i]);
/*  93:    */         }
/*  94:165 */         if (e[k] != 0.0D)
/*  95:    */         {
/*  96:166 */           if (e[(k + 1)] < 0.0D) {
/*  97:167 */             e[k] = (-e[k]);
/*  98:    */           }
/*  99:169 */           for (int i = k + 1; i < this.n; i++) {
/* 100:170 */             e[i] /= e[k];
/* 101:    */           }
/* 102:172 */           e[(k + 1)] += 1.0D;
/* 103:    */         }
/* 104:174 */         e[k] = (-e[k]);
/* 105:175 */         if ((k + 1 < this.m) && (e[k] != 0.0D))
/* 106:    */         {
/* 107:178 */           for (int i = k + 1; i < this.m; i++) {
/* 108:179 */             work[i] = 0.0D;
/* 109:    */           }
/* 110:181 */           for (int j = k + 1; j < this.n; j++) {
/* 111:182 */             for (int i = k + 1; i < this.m; i++) {
/* 112:183 */               work[i] += e[j] * A[i][j];
/* 113:    */             }
/* 114:    */           }
/* 115:186 */           for (int j = k + 1; j < this.n; j++)
/* 116:    */           {
/* 117:187 */             double t = -e[j] / e[(k + 1)];
/* 118:188 */             for (int i = k + 1; i < this.m; i++) {
/* 119:189 */               A[i][j] += t * work[i];
/* 120:    */             }
/* 121:    */           }
/* 122:    */         }
/* 123:196 */         for (int i = k + 1; i < this.n; i++) {
/* 124:197 */           V[i][k] = e[i];
/* 125:    */         }
/* 126:    */       }
/* 127:    */     }
/* 128:202 */     int p = this.n;
/* 129:203 */     if (nct < this.n) {
/* 130:204 */       this.singularValues[nct] = A[nct][nct];
/* 131:    */     }
/* 132:206 */     if (this.m < p) {
/* 133:207 */       this.singularValues[(p - 1)] = 0.0D;
/* 134:    */     }
/* 135:209 */     if (nrt + 1 < p) {
/* 136:210 */       e[nrt] = A[nrt][(p - 1)];
/* 137:    */     }
/* 138:212 */     e[(p - 1)] = 0.0D;
/* 139:215 */     for (int j = nct; j < this.n; j++)
/* 140:    */     {
/* 141:216 */       for (int i = 0; i < this.m; i++) {
/* 142:217 */         U[i][j] = 0.0D;
/* 143:    */       }
/* 144:219 */       U[j][j] = 1.0D;
/* 145:    */     }
/* 146:221 */     for (int k = nct - 1; k >= 0; k--) {
/* 147:222 */       if (this.singularValues[k] != 0.0D)
/* 148:    */       {
/* 149:223 */         for (int j = k + 1; j < this.n; j++)
/* 150:    */         {
/* 151:224 */           double t = 0.0D;
/* 152:225 */           for (int i = k; i < this.m; i++) {
/* 153:226 */             t += U[i][k] * U[i][j];
/* 154:    */           }
/* 155:228 */           t = -t / U[k][k];
/* 156:229 */           for (int i = k; i < this.m; i++) {
/* 157:230 */             U[i][j] += t * U[i][k];
/* 158:    */           }
/* 159:    */         }
/* 160:233 */         for (int i = k; i < this.m; i++) {
/* 161:234 */           U[i][k] = (-U[i][k]);
/* 162:    */         }
/* 163:236 */         U[k][k] = (1.0D + U[k][k]);
/* 164:237 */         for (int i = 0; i < k - 1; i++) {
/* 165:238 */           U[i][k] = 0.0D;
/* 166:    */         }
/* 167:    */       }
/* 168:    */       else
/* 169:    */       {
/* 170:241 */         for (int i = 0; i < this.m; i++) {
/* 171:242 */           U[i][k] = 0.0D;
/* 172:    */         }
/* 173:244 */         U[k][k] = 1.0D;
/* 174:    */       }
/* 175:    */     }
/* 176:249 */     for (int k = this.n - 1; k >= 0; k--)
/* 177:    */     {
/* 178:250 */       if ((k < nrt) && (e[k] != 0.0D)) {
/* 179:252 */         for (int j = k + 1; j < this.n; j++)
/* 180:    */         {
/* 181:253 */           double t = 0.0D;
/* 182:254 */           for (int i = k + 1; i < this.n; i++) {
/* 183:255 */             t += V[i][k] * V[i][j];
/* 184:    */           }
/* 185:257 */           t = -t / V[(k + 1)][k];
/* 186:258 */           for (int i = k + 1; i < this.n; i++) {
/* 187:259 */             V[i][j] += t * V[i][k];
/* 188:    */           }
/* 189:    */         }
/* 190:    */       }
/* 191:263 */       for (int i = 0; i < this.n; i++) {
/* 192:264 */         V[i][k] = 0.0D;
/* 193:    */       }
/* 194:266 */       V[k][k] = 1.0D;
/* 195:    */     }
/* 196:270 */     int pp = p - 1;
/* 197:271 */     int iter = 0;
/* 198:272 */     while (p > 0)
/* 199:    */     {
/* 200:284 */       for (int k = p - 2; k >= 0; k--)
/* 201:    */       {
/* 202:285 */         double threshold = 1.603334688007178E-291D + 2.220446049250313E-016D * (FastMath.abs(this.singularValues[k]) + FastMath.abs(this.singularValues[(k + 1)]));
/* 203:288 */         if (FastMath.abs(e[k]) <= threshold)
/* 204:    */         {
/* 205:289 */           e[k] = 0.0D;
/* 206:290 */           break;
/* 207:    */         }
/* 208:    */       }
/* 209:    */       int kase;
int k = 0;
/* 210:    */       
/* 211:294 */       if (k == p - 2)
/* 212:    */       {
/* 213:295 */         kase = 4;
/* 214:    */       }
/* 215:    */       else
/* 216:    */       {
/* 217:298 */         for (int ks = p - 1; ks >= k; ks--)
/* 218:    */         {
/* 219:299 */           if (ks == k) {
/* 220:    */             break;
/* 221:    */           }
/* 222:302 */           double t = (ks != p ? FastMath.abs(e[ks]) : 0.0D) + (ks != k + 1 ? FastMath.abs(e[(ks - 1)]) : 0.0D);
/* 223:304 */           if (FastMath.abs(this.singularValues[ks]) <= 1.603334688007178E-291D + 2.220446049250313E-016D * t)
/* 224:    */           {
/* 225:305 */             this.singularValues[ks] = 0.0D;
/* 226:306 */             break;
/* 227:    */           }
/* 228:    */         }
int ks = 0;
/* 229:    */        
/* 230:309 */         if (ks == k)
/* 231:    */         {
/* 232:310 */           kase = 3;
/* 233:    */         }
/* 234:    */         else
/* 235:    */         {
/* 236:    */          
/* 237:311 */           if (ks == p - 1)
/* 238:    */           {
/* 239:312 */             kase = 1;
/* 240:    */           }
/* 241:    */           else
/* 242:    */           {
/* 243:314 */             kase = 2;
/* 244:315 */             k = ks;
/* 245:    */           }
/* 246:    */         }
/* 247:    */       }
/* 248:318 */       k++;
/* 249:320 */       switch (kase)
/* 250:    */       {
/* 251:    */       case 1: 
/* 252:323 */         double f = e[(p - 2)];
/* 253:324 */         e[(p - 2)] = 0.0D;
/* 254:325 */         for (int j = p - 2; j >= k; j--)
/* 255:    */         {
/* 256:326 */           double t = FastMath.hypot(this.singularValues[j], f);
/* 257:327 */           double cs = this.singularValues[j] / t;
/* 258:328 */           double sn = f / t;
/* 259:329 */           this.singularValues[j] = t;
/* 260:330 */           if (j != k)
/* 261:    */           {
/* 262:331 */             f = -sn * e[(j - 1)];
/* 263:332 */             e[(j - 1)] = (cs * e[(j - 1)]);
/* 264:    */           }
/* 265:335 */           for (int i = 0; i < this.n; i++)
/* 266:    */           {
/* 267:336 */             t = cs * V[i][j] + sn * V[i][(p - 1)];
/* 268:337 */             V[i][(p - 1)] = (-sn * V[i][j] + cs * V[i][(p - 1)]);
/* 269:338 */             V[i][j] = t;
/* 270:    */           }
/* 271:    */         }
/* 272:342 */         break;
/* 273:    */       case 2: 
/* 274:345 */         double f1 = e[(k - 1)];
/* 275:346 */         e[(k - 1)] = 0.0D;
/* 276:347 */         for (int j = k; j < p; j++)
/* 277:    */         {
/* 278:348 */           double t = FastMath.hypot(this.singularValues[j], f1);
/* 279:349 */           double cs = this.singularValues[j] / t;
/* 280:350 */           double sn = f1 / t;
/* 281:351 */           this.singularValues[j] = t;
/* 282:352 */           f1 = -sn * e[j];
/* 283:353 */           e[j] = (cs * e[j]);
/* 284:355 */           for (int i = 0; i < this.m; i++)
/* 285:    */           {
/* 286:356 */             t = cs * U[i][j] + sn * U[i][(k - 1)];
/* 287:357 */             U[i][(k - 1)] = (-sn * U[i][j] + cs * U[i][(k - 1)]);
/* 288:358 */             U[i][j] = t;
/* 289:    */           }
/* 290:    */         }
/* 291:362 */         break;
/* 292:    */       case 3: 
/* 293:366 */         double maxPm1Pm2 = FastMath.max(FastMath.abs(this.singularValues[(p - 1)]), FastMath.abs(this.singularValues[(p - 2)]));
/* 294:    */         
/* 295:368 */         double scale = FastMath.max(FastMath.max(FastMath.max(maxPm1Pm2, FastMath.abs(e[(p - 2)])), FastMath.abs(this.singularValues[k])), FastMath.abs(e[k]));
/* 296:    */         
/* 297:    */ 
/* 298:    */ 
/* 299:372 */         double sp = this.singularValues[(p - 1)] / scale;
/* 300:373 */         double spm1 = this.singularValues[(p - 2)] / scale;
/* 301:374 */         double epm1 = e[(p - 2)] / scale;
/* 302:375 */         double sk = this.singularValues[k] / scale;
/* 303:376 */         double ek = e[k] / scale;
/* 304:377 */         double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0D;
/* 305:378 */         double c = sp * epm1 * (sp * epm1);
/* 306:379 */         double shift = 0.0D;
/* 307:380 */         if ((b != 0.0D) || (c != 0.0D))
/* 308:    */         {
/* 309:382 */           shift = FastMath.sqrt(b * b + c);
/* 310:383 */           if (b < 0.0D) {
/* 311:384 */             shift = -shift;
/* 312:    */           }
/* 313:386 */           shift = c / (b + shift);
/* 314:    */         }
/* 315:388 */         double f11 = (sk + sp) * (sk - sp) + shift;
/* 316:389 */         double g = sk * ek;
/* 317:391 */         for (int j = k; j < p - 1; j++)
/* 318:    */         {
/* 319:392 */           double t = FastMath.hypot(f11, g);
/* 320:393 */           double cs = f11 / t;
/* 321:394 */           double sn = g / t;
/* 322:395 */           if (j != k) {
/* 323:396 */             e[(j - 1)] = t;
/* 324:    */           }
/* 325:398 */           f11 = cs * this.singularValues[j] + sn * e[j];
/* 326:399 */           e[j] = (cs * e[j] - sn * this.singularValues[j]);
/* 327:400 */           g = sn * this.singularValues[(j + 1)];
/* 328:401 */           this.singularValues[(j + 1)] = (cs * this.singularValues[(j + 1)]);
/* 329:403 */           for (int i = 0; i < this.n; i++)
/* 330:    */           {
/* 331:404 */             t = cs * V[i][j] + sn * V[i][(j + 1)];
/* 332:405 */             V[i][(j + 1)] = (-sn * V[i][j] + cs * V[i][(j + 1)]);
/* 333:406 */             V[i][j] = t;
/* 334:    */           }
/* 335:408 */           t = FastMath.hypot(f11, g);
/* 336:409 */           cs = f11 / t;
/* 337:410 */           sn = g / t;
/* 338:411 */           this.singularValues[j] = t;
/* 339:412 */           f11 = cs * e[j] + sn * this.singularValues[(j + 1)];
/* 340:413 */           this.singularValues[(j + 1)] = (-sn * e[j] + cs * this.singularValues[(j + 1)]);
/* 341:414 */           g = sn * e[(j + 1)];
/* 342:415 */           e[(j + 1)] = (cs * e[(j + 1)]);
/* 343:416 */           if (j < this.m - 1) {
/* 344:417 */             for (int i = 0; i < this.m; i++)
/* 345:    */             {
/* 346:418 */               t = cs * U[i][j] + sn * U[i][(j + 1)];
/* 347:419 */               U[i][(j + 1)] = (-sn * U[i][j] + cs * U[i][(j + 1)]);
/* 348:420 */               U[i][j] = t;
/* 349:    */             }
/* 350:    */           }
/* 351:    */         }
/* 352:424 */         e[(p - 2)] = f11;
/* 353:425 */         iter += 1;
/* 354:    */         
/* 355:427 */         break;
/* 356:    */       default: 
/* 357:431 */         if (this.singularValues[k] <= 0.0D)
/* 358:    */         {
/* 359:432 */           this.singularValues[k] = (this.singularValues[k] < 0.0D ? -this.singularValues[k] : 0.0D);
/* 360:434 */           for (int i = 0; i <= pp; i++) {
/* 361:435 */             V[i][k] = (-V[i][k]);
/* 362:    */           }
/* 363:    */         }
/* 364:439 */         while ((k < pp) && 
/* 365:440 */           (this.singularValues[k] < this.singularValues[(k + 1)]))
/* 366:    */         {
/* 367:443 */           double t = this.singularValues[k];
/* 368:444 */           this.singularValues[k] = this.singularValues[(k + 1)];
/* 369:445 */           this.singularValues[(k + 1)] = t;
/* 370:446 */           if (k < this.n - 1) {
/* 371:447 */             for (int i = 0; i < this.n; i++)
/* 372:    */             {
/* 373:448 */               t = V[i][(k + 1)];
/* 374:449 */               V[i][(k + 1)] = V[i][k];
/* 375:450 */               V[i][k] = t;
/* 376:    */             }
/* 377:    */           }
/* 378:453 */           if (k < this.m - 1) {
/* 379:454 */             for (int i = 0; i < this.m; i++)
/* 380:    */             {
/* 381:455 */               t = U[i][(k + 1)];
/* 382:456 */               U[i][(k + 1)] = U[i][k];
/* 383:457 */               U[i][k] = t;
/* 384:    */             }
/* 385:    */           }
/* 386:460 */           k++;
/* 387:    */         }
/* 388:462 */         iter = 0;
/* 389:463 */         p--;
/* 390:    */       }
/* 391:    */     }
/* 392:470 */     this.tol = FastMath.max(this.m * this.singularValues[0] * 2.220446049250313E-016D, FastMath.sqrt(2.225073858507201E-308D));
/* 393:473 */     if (!this.transposed)
/* 394:    */     {
/* 395:474 */       this.cachedU = MatrixUtils.createRealMatrix(U);
/* 396:475 */       this.cachedV = MatrixUtils.createRealMatrix(V);
/* 397:    */     }
/* 398:    */     else
/* 399:    */     {
/* 400:477 */       this.cachedU = MatrixUtils.createRealMatrix(V);
/* 401:478 */       this.cachedV = MatrixUtils.createRealMatrix(U);
/* 402:    */     }
/* 403:    */   }
/* 404:    */   
/* 405:    */   public RealMatrix getU()
/* 406:    */   {
/* 407:490 */     return this.cachedU;
/* 408:    */   }
/* 409:    */   
/* 410:    */   public RealMatrix getUT()
/* 411:    */   {
/* 412:501 */     if (this.cachedUt == null) {
/* 413:502 */       this.cachedUt = getU().transpose();
/* 414:    */     }
/* 415:505 */     return this.cachedUt;
/* 416:    */   }
/* 417:    */   
/* 418:    */   public RealMatrix getS()
/* 419:    */   {
/* 420:515 */     if (this.cachedS == null) {
/* 421:517 */       this.cachedS = MatrixUtils.createRealDiagonalMatrix(this.singularValues);
/* 422:    */     }
/* 423:519 */     return this.cachedS;
/* 424:    */   }
/* 425:    */   
/* 426:    */   public double[] getSingularValues()
/* 427:    */   {
/* 428:529 */     return (double[])this.singularValues.clone();
/* 429:    */   }
/* 430:    */   
/* 431:    */   public RealMatrix getV()
/* 432:    */   {
/* 433:540 */     return this.cachedV;
/* 434:    */   }
/* 435:    */   
/* 436:    */   public RealMatrix getVT()
/* 437:    */   {
/* 438:550 */     if (this.cachedVt == null) {
/* 439:551 */       this.cachedVt = getV().transpose();
/* 440:    */     }
/* 441:554 */     return this.cachedVt;
/* 442:    */   }
/* 443:    */   
/* 444:    */   public RealMatrix getCovariance(double minSingularValue)
/* 445:    */   {
/* 446:570 */     int p = this.singularValues.length;
/* 447:571 */     int dimension = 0;
/* 448:572 */     while ((dimension < p) && (this.singularValues[dimension] >= minSingularValue)) {
/* 449:574 */       dimension++;
/* 450:    */     }
/* 451:577 */     if (dimension == 0) {
/* 452:578 */       throw new NumberIsTooLargeException(LocalizedFormats.TOO_LARGE_CUTOFF_SINGULAR_VALUE, Double.valueOf(minSingularValue), Double.valueOf(this.singularValues[0]), true);
/* 453:    */     }
/* 454:582 */     final double[][] data = new double[dimension][p];
/* 455:583 */     getVT().walkInOptimizedOrder(new DefaultRealMatrixPreservingVisitor()
/* 456:    */     {
/* 457:    */       public void visit(int row, int column, double value)
/* 458:    */       {
/* 459:588 */         data[row][column] = (value / SingularValueDecomposition.this.singularValues[row]);
/* 460:    */       }
/* 461:588 */     }, 0, dimension - 1, 0, p - 1);
/* 462:    */     
/* 463:    */ 
/* 464:    */ 
/* 465:592 */     RealMatrix jv = new Array2DRowRealMatrix(data, false);
/* 466:593 */     return jv.transpose().multiply(jv);
/* 467:    */   }
/* 468:    */   
/* 469:    */   public double getNorm()
/* 470:    */   {
/* 471:604 */     return this.singularValues[0];
/* 472:    */   }
/* 473:    */   
/* 474:    */   public double getConditionNumber()
/* 475:    */   {
/* 476:612 */     return this.singularValues[0] / this.singularValues[(this.n - 1)];
/* 477:    */   }
/* 478:    */   
/* 479:    */   public double getInverseConditionNumber()
/* 480:    */   {
/* 481:623 */     return this.singularValues[(this.n - 1)] / this.singularValues[0];
/* 482:    */   }
/* 483:    */   
/* 484:    */   public int getRank()
/* 485:    */   {
/* 486:635 */     int r = 0;
/* 487:636 */     for (int i = 0; i < this.singularValues.length; i++) {
/* 488:637 */       if (this.singularValues[i] > this.tol) {
/* 489:638 */         r++;
/* 490:    */       }
/* 491:    */     }
/* 492:641 */     return r;
/* 493:    */   }
/* 494:    */   
/* 495:    */   public DecompositionSolver getSolver()
/* 496:    */   {
/* 497:649 */     return new Solver(this.singularValues, getUT(), getV(), getRank() == this.m, this.tol);
/* 498:    */   }
/* 499:    */   
/* 500:    */   private static class Solver
/* 501:    */     implements DecompositionSolver
/* 502:    */   {
/* 503:    */     private final RealMatrix pseudoInverse;
/* 504:    */     private boolean nonSingular;
/* 505:    */     
/* 506:    */     private Solver(double[] singularValues, RealMatrix uT, RealMatrix v, boolean nonSingular, double tol)
/* 507:    */     {
/* 508:670 */       double[][] suT = uT.getData();
/* 509:671 */       for (int i = 0; i < singularValues.length; i++)
/* 510:    */       {
/* 511:    */         double a;
/* 512:    */        
/* 513:673 */         if (singularValues[i] > tol) {
/* 514:674 */           a = 1.0D / singularValues[i];
/* 515:    */         } else {
/* 516:676 */           a = 0.0D;
/* 517:    */         }
/* 518:678 */         double[] suTi = suT[i];
/* 519:679 */         for (int j = 0; j < suTi.length; j++) {
/* 520:680 */           suTi[j] *= a;
/* 521:    */         }
/* 522:    */       }
/* 523:683 */       this.pseudoInverse = v.multiply(new Array2DRowRealMatrix(suT, false));
/* 524:684 */       this.nonSingular = nonSingular;
/* 525:    */     }
/* 526:    */     
/* 527:    */     public RealVector solve(RealVector b)
/* 528:    */     {
/* 529:699 */       return this.pseudoInverse.operate(b);
/* 530:    */     }
/* 531:    */     
/* 532:    */     public RealMatrix solve(RealMatrix b)
/* 533:    */     {
/* 534:715 */       return this.pseudoInverse.multiply(b);
/* 535:    */     }
/* 536:    */     
/* 537:    */     public boolean isNonSingular()
/* 538:    */     {
/* 539:724 */       return this.nonSingular;
/* 540:    */     }
/* 541:    */     
/* 542:    */     public RealMatrix getInverse()
/* 543:    */     {
/* 544:733 */       return this.pseudoInverse;
/* 545:    */     }
/* 546:    */   }
/* 547:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.SingularValueDecomposition
 * JD-Core Version:    0.7.0.1
 */
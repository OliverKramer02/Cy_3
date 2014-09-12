/*   1:    */ package org.apache.commons.math3.transform;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.Array;

/*   6:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   7:    */ import org.apache.commons.math3.complex.Complex;
/*   8:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   9:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ import org.apache.commons.math3.util.ArithmeticUtils;
/*  13:    */ import org.apache.commons.math3.util.FastMath;
/*  14:    */ import org.apache.commons.math3.util.MathArrays;
/*   5:    */ 
/*  15:    */ 
/*  16:    */ public class FastFourierTransformer
/*  17:    */   implements Serializable
/*  18:    */ {
/*  19:    */   static final long serialVersionUID = 20120210L;
/*  20: 68 */   private static final double[] W_SUB_N_R = { 1.0D, -1.0D, 6.123233995736766E-017D, 0.7071067811865476D, 0.9238795325112867D, 0.9807852804032304D, 0.9951847266721969D, 0.9987954562051724D, 0.9996988186962043D, 0.999924701839145D, 0.9999811752826011D, 0.9999952938095762D, 0.9999988234517019D, 0.9999997058628822D, 0.9999999264657179D, 0.9999999816164293D, 0.9999999954041073D, 0.9999999988510269D, 0.9999999997127567D, 0.9999999999281892D, 0.9999999999820473D, 0.9999999999955118D, 0.999999999998878D, 0.9999999999997195D, 0.9999999999999298D, 0.9999999999999825D, 0.9999999999999957D, 0.9999999999999989D, 0.9999999999999998D, 0.9999999999999999D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D };
/*  21: 91 */   private static final double[] W_SUB_N_I = { 2.449293598294706E-016D, -1.224646799147353E-016D, -1.0D, -0.7071067811865475D, -0.3826834323650898D, -0.1950903220161283D, -0.0980171403295606D, -0.04906767432741802D, -0.02454122852291229D, -0.01227153828571993D, -0.006135884649154475D, -0.003067956762965976D, -0.001533980186284766D, -0.0007669903187427045D, -0.0003834951875713956D, -0.0001917475973107033D, -9.587379909597735E-005D, -4.793689960306688E-005D, -2.396844980841822E-005D, -1.198422490506971E-005D, -5.992112452642428E-006D, -2.996056226334661E-006D, -1.498028113169011E-006D, -7.490140565847157E-007D, -3.745070282923841E-007D, -1.872535141461954E-007D, -9.362675707309808E-008D, -4.681337853654909E-008D, -2.340668926827455E-008D, -1.170334463413728E-008D, -5.851672317068639E-009D, -2.925836158534319E-009D, -1.46291807926716E-009D, -7.314590396335798E-010D, -3.657295198167899E-010D, -1.82864759908395E-010D, -9.143237995419748E-011D, -4.571618997709874E-011D, -2.285809498854937E-011D, -1.142904749427469E-011D, -5.714523747137342E-012D, -2.857261873568671E-012D, -1.428630936784336E-012D, -7.143154683921678E-013D, -3.571577341960839E-013D, -1.78578867098042E-013D, -8.928943354902097E-014D, -4.464471677451049E-014D, -2.232235838725524E-014D, -1.116117919362762E-014D, -5.580589596813811E-015D, -2.790294798406905E-015D, -1.395147399203453E-015D, -6.975736996017264E-016D, -3.487868498008632E-016D, -1.743934249004316E-016D, -8.719671245021579E-017D, -4.35983562251079E-017D, -2.179917811255395E-017D, -1.089958905627697E-017D, -5.449794528138487E-018D, -2.724897264069244E-018D, -1.362448632034622E-018D };
/*  22:    */   private final DftNormalization normalization;
/*  23:    */   
/*  24:    */   public FastFourierTransformer(DftNormalization normalization)
/*  25:    */   {
/*  26:120 */     this.normalization = normalization;
/*  27:    */   }
/*  28:    */   
/*  29:    */   private static void bitReversalShuffle2(double[] a, double[] b)
/*  30:    */   {
/*  31:134 */     int n = a.length;
/*  32:135 */     assert (b.length == n);
/*  33:136 */     int halfOfN = n >> 1;
/*  34:    */     
/*  35:138 */     int j = 0;
/*  36:139 */     for (int i = 0; i < n; i++)
/*  37:    */     {
/*  38:140 */       if (i < j)
/*  39:    */       {
/*  40:142 */         double temp = a[i];
/*  41:143 */         a[i] = a[j];
/*  42:144 */         a[j] = temp;
/*  43:    */         
/*  44:146 */         temp = b[i];
/*  45:147 */         b[i] = b[j];
/*  46:148 */         b[j] = temp;
/*  47:    */       }
/*  48:151 */       int k = halfOfN;
/*  49:152 */       while ((k <= j) && (k > 0))
/*  50:    */       {
/*  51:153 */         j -= k;
/*  52:154 */         k >>= 1;
/*  53:    */       }
/*  54:156 */       j += k;
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   private static void normalizeTransformedData(double[][] dataRI, DftNormalization normalization, TransformType type)
/*  59:    */   {
/*  60:171 */     double[] dataR = dataRI[0];
/*  61:172 */     double[] dataI = dataRI[1];
/*  62:173 */     int n = dataR.length;
/*  63:174 */     assert (dataI.length == n);
/*  64:176 */     switch (normalization.ordinal())
/*  65:    */     {
/*  66:    */     case 1: 
/*  67:178 */       if (type == TransformType.INVERSE)
/*  68:    */       {
/*  69:179 */         double scaleFactor = 1.0D / n;
/*  70:180 */         for (int i = 0; i < n; i++)
/*  71:    */         {
/*  72:181 */           dataR[i] *= scaleFactor;
/*  73:182 */           dataI[i] *= scaleFactor;
/*  74:    */         }
/*  75:    */       }
/*  76:184 */       break;
/*  77:    */     case 2: 
/*  78:187 */       double scaleFactor = 1.0D / FastMath.sqrt(n);
/*  79:188 */       for (int i = 0; i < n; i++)
/*  80:    */       {
/*  81:189 */         dataR[i] *= scaleFactor;
/*  82:190 */         dataI[i] *= scaleFactor;
/*  83:    */       }
/*  84:192 */       break;
/*  85:    */     default: 
/*  86:200 */       throw new MathIllegalStateException();
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static void transformInPlace(double[][] dataRI, DftNormalization normalization, TransformType type)
/*  91:    */   {
/*  92:227 */     if (dataRI.length != 2) {
/*  93:228 */       throw new DimensionMismatchException(dataRI.length, 2);
/*  94:    */     }
/*  95:230 */     double[] dataR = dataRI[0];
/*  96:231 */     double[] dataI = dataRI[1];
/*  97:232 */     if (dataR.length != dataI.length) {
/*  98:233 */       throw new DimensionMismatchException(dataI.length, dataR.length);
/*  99:    */     }
/* 100:236 */     int n = dataR.length;
/* 101:237 */     if (!ArithmeticUtils.isPowerOfTwo(n)) {
/* 102:238 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, new Object[] { Integer.valueOf(n) });
/* 103:    */     }
/* 104:243 */     if (n == 1) {
/* 105:244 */       return;
/* 106:    */     }
/* 107:245 */     if (n == 2)
/* 108:    */     {
/* 109:246 */       double srcR0 = dataR[0];
/* 110:247 */       double srcI0 = dataI[0];
/* 111:248 */       double srcR1 = dataR[1];
/* 112:249 */       double srcI1 = dataI[1];
/* 113:    */       
/* 114:    */ 
/* 115:252 */       dataR[0] = (srcR0 + srcR1);
/* 116:253 */       dataI[0] = (srcI0 + srcI1);
/* 117:    */       
/* 118:255 */       dataR[1] = (srcR0 - srcR1);
/* 119:256 */       dataI[1] = (srcI0 - srcI1);
/* 120:    */       
/* 121:258 */       normalizeTransformedData(dataRI, normalization, type);
/* 122:259 */       return;
/* 123:    */     }
/* 124:262 */     bitReversalShuffle2(dataR, dataI);
/* 125:265 */     if (type == TransformType.INVERSE) {
/* 126:266 */       for (int i0 = 0; i0 < n; i0 += 4)
/* 127:    */       {
/* 128:267 */         int i1 = i0 + 1;
/* 129:268 */         int i2 = i0 + 2;
/* 130:269 */         int i3 = i0 + 3;
/* 131:    */         
/* 132:271 */         double srcR0 = dataR[i0];
/* 133:272 */         double srcI0 = dataI[i0];
/* 134:273 */         double srcR1 = dataR[i2];
/* 135:274 */         double srcI1 = dataI[i2];
/* 136:275 */         double srcR2 = dataR[i1];
/* 137:276 */         double srcI2 = dataI[i1];
/* 138:277 */         double srcR3 = dataR[i3];
/* 139:278 */         double srcI3 = dataI[i3];
/* 140:    */         
/* 141:    */ 
/* 142:    */ 
/* 143:282 */         dataR[i0] = (srcR0 + srcR1 + srcR2 + srcR3);
/* 144:283 */         dataI[i0] = (srcI0 + srcI1 + srcI2 + srcI3);
/* 145:    */         
/* 146:285 */         dataR[i1] = (srcR0 - srcR2 + (srcI3 - srcI1));
/* 147:286 */         dataI[i1] = (srcI0 - srcI2 + (srcR1 - srcR3));
/* 148:    */         
/* 149:288 */         dataR[i2] = (srcR0 - srcR1 + srcR2 - srcR3);
/* 150:289 */         dataI[i2] = (srcI0 - srcI1 + srcI2 - srcI3);
/* 151:    */         
/* 152:291 */         dataR[i3] = (srcR0 - srcR2 + (srcI1 - srcI3));
/* 153:292 */         dataI[i3] = (srcI0 - srcI2 + (srcR3 - srcR1));
/* 154:    */       }
/* 155:    */     } else {
/* 156:295 */       for (int i0 = 0; i0 < n; i0 += 4)
/* 157:    */       {
/* 158:296 */         int i1 = i0 + 1;
/* 159:297 */         int i2 = i0 + 2;
/* 160:298 */         int i3 = i0 + 3;
/* 161:    */         
/* 162:300 */         double srcR0 = dataR[i0];
/* 163:301 */         double srcI0 = dataI[i0];
/* 164:302 */         double srcR1 = dataR[i2];
/* 165:303 */         double srcI1 = dataI[i2];
/* 166:304 */         double srcR2 = dataR[i1];
/* 167:305 */         double srcI2 = dataI[i1];
/* 168:306 */         double srcR3 = dataR[i3];
/* 169:307 */         double srcI3 = dataI[i3];
/* 170:    */         
/* 171:    */ 
/* 172:    */ 
/* 173:311 */         dataR[i0] = (srcR0 + srcR1 + srcR2 + srcR3);
/* 174:312 */         dataI[i0] = (srcI0 + srcI1 + srcI2 + srcI3);
/* 175:    */         
/* 176:314 */         dataR[i1] = (srcR0 - srcR2 + (srcI1 - srcI3));
/* 177:315 */         dataI[i1] = (srcI0 - srcI2 + (srcR3 - srcR1));
/* 178:    */         
/* 179:317 */         dataR[i2] = (srcR0 - srcR1 + srcR2 - srcR3);
/* 180:318 */         dataI[i2] = (srcI0 - srcI1 + srcI2 - srcI3);
/* 181:    */         
/* 182:320 */         dataR[i3] = (srcR0 - srcR2 + (srcI3 - srcI1));
/* 183:321 */         dataI[i3] = (srcI0 - srcI2 + (srcR1 - srcR3));
/* 184:    */       }
/* 185:    */     }
/* 186:325 */     int lastN0 = 4;
/* 187:326 */     int lastLogN0 = 2;
/* 188:327 */     while (lastN0 < n)
/* 189:    */     {
/* 190:328 */       int n0 = lastN0 << 1;
/* 191:329 */       int logN0 = lastLogN0 + 1;
/* 192:330 */       double wSubN0R = W_SUB_N_R[logN0];
/* 193:331 */       double wSubN0I = W_SUB_N_I[logN0];
/* 194:332 */       if (type == TransformType.INVERSE) {
/* 195:333 */         wSubN0I = -wSubN0I;
/* 196:    */       }
/* 197:337 */       for (int destEvenStartIndex = 0; destEvenStartIndex < n; destEvenStartIndex += n0)
/* 198:    */       {
/* 199:338 */         int destOddStartIndex = destEvenStartIndex + lastN0;
/* 200:    */         
/* 201:340 */         double wSubN0ToRR = 1.0D;
/* 202:341 */         double wSubN0ToRI = 0.0D;
/* 203:343 */         for (int r = 0; r < lastN0; r++)
/* 204:    */         {
/* 205:344 */           double grR = dataR[(destEvenStartIndex + r)];
/* 206:345 */           double grI = dataI[(destEvenStartIndex + r)];
/* 207:346 */           double hrR = dataR[(destOddStartIndex + r)];
/* 208:347 */           double hrI = dataI[(destOddStartIndex + r)];
/* 209:    */           
/* 210:    */ 
/* 211:350 */           dataR[(destEvenStartIndex + r)] = (grR + wSubN0ToRR * hrR - wSubN0ToRI * hrI);
/* 212:351 */           dataI[(destEvenStartIndex + r)] = (grI + wSubN0ToRR * hrI + wSubN0ToRI * hrR);
/* 213:    */           
/* 214:353 */           dataR[(destOddStartIndex + r)] = (grR - (wSubN0ToRR * hrR - wSubN0ToRI * hrI));
/* 215:354 */           dataI[(destOddStartIndex + r)] = (grI - (wSubN0ToRR * hrI + wSubN0ToRI * hrR));
/* 216:    */           
/* 217:    */ 
/* 218:357 */           double nextWsubN0ToRR = wSubN0ToRR * wSubN0R - wSubN0ToRI * wSubN0I;
/* 219:358 */           double nextWsubN0ToRI = wSubN0ToRR * wSubN0I + wSubN0ToRI * wSubN0R;
/* 220:359 */           wSubN0ToRR = nextWsubN0ToRR;
/* 221:360 */           wSubN0ToRI = nextWsubN0ToRI;
/* 222:    */         }
/* 223:    */       }
/* 224:364 */       lastN0 = n0;
/* 225:365 */       lastLogN0 = logN0;
/* 226:    */     }
/* 227:368 */     normalizeTransformedData(dataRI, normalization, type);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public Complex[] transform(double[] f, TransformType type)
/* 231:    */   {
/* 232:381 */     double[][] dataRI = { MathArrays.copyOf(f, f.length), new double[f.length] };
/* 233:    */     
/* 234:    */ 
/* 235:    */ 
/* 236:385 */     transformInPlace(dataRI, this.normalization, type);
/* 237:    */     
/* 238:387 */     return TransformUtils.createComplexArray(dataRI);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public Complex[] transform(UnivariateFunction f, double min, double max, int n, TransformType type)
/* 242:    */   {
double[] data = null;
/* 243:411 */    // double[] data = FunctionUtils.sample(f, min, max, n);
/* 244:412 */     return transform(data, type);
/* 245:    */   }
/* 246:    */   
/* 247:    */   public Complex[] transform(Complex[] f, TransformType type)
/* 248:    */   {
/* 249:426 */     double[][] dataRI = TransformUtils.createRealImaginaryArray(f);
/* 250:    */     
/* 251:428 */     transformInPlace(dataRI, this.normalization, type);
/* 252:    */     
/* 253:430 */     return TransformUtils.createComplexArray(dataRI);
/* 254:    */   }
/* 255:    */   
/* 256:    */   @Deprecated
/* 257:    */   public Object mdfft(Object mdca, TransformType type)
/* 258:    */   {
/* 259:452 */     MultiDimensionalComplexMatrix mdcm = (MultiDimensionalComplexMatrix)new MultiDimensionalComplexMatrix(mdca).clone();
/* 260:    */     
/* 261:454 */     int[] dimensionSize = mdcm.getDimensionSizes();
/* 262:456 */     for (int i = 0; i < dimensionSize.length; i++) {
/* 263:457 */       mdfft(mdcm, type, i, new int[0]);
/* 264:    */     }
/* 265:459 */     return mdcm.getArray();
/* 266:    */   }
/* 267:    */   
/* 268:    */   @Deprecated
/* 269:    */   private void mdfft(MultiDimensionalComplexMatrix mdcm, TransformType type, int d, int[] subVector)
/* 270:    */   {
/* 271:476 */     int[] dimensionSize = mdcm.getDimensionSizes();
/* 272:478 */     if (subVector.length == dimensionSize.length)
/* 273:    */     {
/* 274:479 */       Complex[] temp = new Complex[dimensionSize[d]];
/* 275:480 */       for (int i = 0; i < dimensionSize[d]; i++)
/* 276:    */       {
/* 277:482 */         subVector[d] = i;
/* 278:483 */         temp[i] = mdcm.get(subVector);
/* 279:    */       }
/* 280:486 */       temp = transform(temp, type);
/* 281:488 */       for (int i = 0; i < dimensionSize[d]; i++)
/* 282:    */       {
/* 283:489 */         subVector[d] = i;
/* 284:490 */         mdcm.set(temp[i], subVector);
/* 285:    */       }
/* 286:    */     }
/* 287:    */     else
/* 288:    */     {
/* 289:493 */       int[] vector = new int[subVector.length + 1];
/* 290:494 */       System.arraycopy(subVector, 0, vector, 0, subVector.length);
/* 291:495 */       if (subVector.length == d)
/* 292:    */       {
/* 293:498 */         vector[d] = 0;
/* 294:499 */         mdfft(mdcm, type, d, vector);
/* 295:    */       }
/* 296:    */       else
/* 297:    */       {
/* 298:501 */         for (int i = 0; i < dimensionSize[subVector.length]; i++)
/* 299:    */         {
/* 300:502 */           vector[subVector.length] = i;
/* 301:    */           
/* 302:504 */           mdfft(mdcm, type, d, vector);
/* 303:    */         }
/* 304:    */       }
/* 305:    */     }
/* 306:    */   }
/* 307:    */   
/* 308:    */   @Deprecated
/* 309:    */   private static class MultiDimensionalComplexMatrix
/* 310:    */     implements Cloneable
/* 311:    */   {
/* 312:    */     protected int[] dimensionSize;
/* 313:    */     protected Object multiDimensionalComplexArray;
/* 314:    */     
/* 315:    */     public MultiDimensionalComplexMatrix(Object multiDimensionalComplexArray)
/* 316:    */     {
/* 317:538 */       this.multiDimensionalComplexArray = multiDimensionalComplexArray;
/* 318:    */       
/* 319:    */ 
/* 320:541 */       int numOfDimensions = 0;
/* 321:542 */       Object lastDimension = multiDimensionalComplexArray;
/* 322:543 */       while ((lastDimension instanceof Object[]))
/* 323:    */       {
/* 324:544 */         Object[] array = (Object[])lastDimension;
/* 325:545 */         numOfDimensions++;
/* 326:546 */         lastDimension = array[0];
/* 327:    */       }
/* 328:550 */       this.dimensionSize = new int[numOfDimensions];
/* 329:    */       
/* 330:    */ 
/* 331:553 */       numOfDimensions = 0;
/* 332:554 */       lastDimension = multiDimensionalComplexArray;
/* 333:555 */       while ((lastDimension instanceof Object[]))
/* 334:    */       {
/* 335:556 */         Object[] array = (Object[])lastDimension;
/* 336:557 */         this.dimensionSize[(numOfDimensions++)] = array.length;
/* 337:558 */         lastDimension = array[0];
/* 338:    */       }
/* 339:    */     }
/* 340:    */     
/* 341:    */     public Complex get(int... vector)
/* 342:    */       throws DimensionMismatchException
/* 343:    */     {
/* 344:573 */       if (vector == null)
/* 345:    */       {
/* 346:574 */         if (this.dimensionSize.length > 0) {
/* 347:575 */           throw new DimensionMismatchException(0, this.dimensionSize.length);
/* 348:    */         }
/* 349:579 */         return null;
/* 350:    */       }
/* 351:581 */       if (vector.length != this.dimensionSize.length) {
/* 352:582 */         throw new DimensionMismatchException(vector.length, this.dimensionSize.length);
/* 353:    */       }
/* 354:587 */       Object lastDimension = this.multiDimensionalComplexArray;
/* 355:589 */       for (int i = 0; i < this.dimensionSize.length; i++) {
/* 356:590 */         lastDimension = ((Object[])(Object[])lastDimension)[vector[i]];
/* 357:    */       }
/* 358:592 */       return (Complex)lastDimension;
/* 359:    */     }
/* 360:    */     
/* 361:    */     public Complex set(Complex magnitude, int... vector)
/* 362:    */       throws DimensionMismatchException
/* 363:    */     {
/* 364:606 */       if (vector == null)
/* 365:    */       {
/* 366:607 */         if (this.dimensionSize.length > 0) {
/* 367:608 */           throw new DimensionMismatchException(0, this.dimensionSize.length);
/* 368:    */         }
/* 369:612 */         return null;
/* 370:    */       }
/* 371:614 */       if (vector.length != this.dimensionSize.length) {
/* 372:615 */         throw new DimensionMismatchException(vector.length, this.dimensionSize.length);
/* 373:    */       }
/* 374:620 */       Object[] lastDimension = (Object[])this.multiDimensionalComplexArray;
/* 375:621 */       for (int i = 0; i < this.dimensionSize.length - 1; i++) {
/* 376:622 */         lastDimension = (Object[])lastDimension[vector[i]];
/* 377:    */       }
/* 378:625 */       Complex lastValue = (Complex)lastDimension[vector[(this.dimensionSize.length - 1)]];
/* 379:626 */       lastDimension[vector[(this.dimensionSize.length - 1)]] = magnitude;
/* 380:    */       
/* 381:628 */       return lastValue;
/* 382:    */     }
/* 383:    */     
/* 384:    */     public int[] getDimensionSizes()
/* 385:    */     {
/* 386:637 */       return (int[])this.dimensionSize.clone();
/* 387:    */     }
/* 388:    */     
/* 389:    */     public Object getArray()
/* 390:    */     {
/* 391:646 */       return this.multiDimensionalComplexArray;
/* 392:    */     }
/* 393:    */     
/* 394:    */     public Object clone()
/* 395:    */     {
/* 396:652 */       MultiDimensionalComplexMatrix mdcm = new MultiDimensionalComplexMatrix(Array.newInstance(Complex.class, this.dimensionSize));
/* 397:    */       
/* 398:    */ 
/* 399:655 */       clone(mdcm);
/* 400:656 */       return mdcm;
/* 401:    */     }
/* 402:    */     
/* 403:    */     private void clone(MultiDimensionalComplexMatrix mdcm)
/* 404:    */     {
/* 405:666 */       int[] vector = new int[this.dimensionSize.length];
/* 406:667 */       int size = 1;
/* 407:668 */       for (int i = 0; i < this.dimensionSize.length; i++) {
/* 408:669 */         size *= this.dimensionSize[i];
/* 409:    */       }
/* 410:671 */       int[][] vectorList = new int[size][this.dimensionSize.length];
/* 411:672 */       for (int[] nextVector : vectorList)
/* 412:    */       {
/* 413:673 */         System.arraycopy(vector, 0, nextVector, 0, this.dimensionSize.length);
/* 414:675 */         for (int i = 0; i < this.dimensionSize.length; i++)
/* 415:    */         {
/* 416:676 */           vector[i] += 1;
/* 417:677 */           if (vector[i] < this.dimensionSize[i]) {
/* 418:    */             break;
/* 419:    */           }
/* 420:680 */           vector[i] = 0;
/* 421:    */         }
/* 422:    */       }
/* 423:685 */       for (int[] nextVector : vectorList) {
/* 424:686 */         mdcm.set(get(nextVector), nextVector);
/* 425:    */       }
/* 426:    */     }
/* 427:    */   }
/* 428:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.transform.FastFourierTransformer
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ 
/*   6:    */ class FastMathCalc
/*   7:    */ {
/*   8:    */   private static final long HEX_40000000 = 1073741824L;
/*   9: 36 */   private static final double[] FACT = { 1.0D, 1.0D, 2.0D, 6.0D, 24.0D, 120.0D, 720.0D, 5040.0D, 40320.0D, 362880.0D, 3628800.0D, 39916800.0D, 479001600.0D, 6227020800.0D, 87178291200.0D, 1307674368000.0D, 20922789888000.0D, 355687428096000.0D, 6402373705728000.0D, 1.21645100408832E+017D };
/*  10: 61 */   private static final double[][] LN_SPLIT_COEF = { { 2.0D, 0.0D }, { 0.6666666269302368D, 3.973642985026063E-008D }, { 0.3999999761581421D, 2.384185791001988E-008D }, { 0.2857142686843872D, 1.702989854350184E-008D }, { 0.2222222089767456D, 1.32454713117355E-008D }, { 0.181818157434464D, 2.438420304435491E-008D }, { 0.153846144676209D, 9.140260083262505E-009D }, { 0.1333333253860474D, 9.220590270857665E-009D }, { 0.1176470071077347D, 1.239334585501839E-008D }, { 0.1052640378475189D, 8.251545029714408E-009D }, { 0.0952233225107193D, 1.267593482375886E-008D }, { 0.08713622391223908D, 1.143025000890914E-008D }, { 0.07842259109020233D, 2.404307984052299E-009D }, { 0.08371849358081818D, 1.176342548272881E-008D }, { 0.03058958053588867D, 1.295864689901894E-009D }, { 0.1498230397701263D, 1.225743062930824E-008D } };
/*  11:    */   private static final String TABLE_START_DECL = "    {";
/*  12:    */   private static final String TABLE_END_DECL = "    };";
/*  13:    */   
/*  14:    */   private static void buildSinCosTables(double[] SINE_TABLE_A, double[] SINE_TABLE_B, double[] COSINE_TABLE_A, double[] COSINE_TABLE_B, int SINE_TABLE_LEN, double[] TANGENT_TABLE_A, double[] TANGENT_TABLE_B)
/*  15:    */   {
/*  16:105 */     double[] result = new double[2];
/*  17:108 */     for (int i = 0; i < 7; i++)
/*  18:    */     {
/*  19:109 */       double x = i / 8.0D;
/*  20:    */       
/*  21:111 */       slowSin(x, result);
/*  22:112 */       SINE_TABLE_A[i] = result[0];
/*  23:113 */       SINE_TABLE_B[i] = result[1];
/*  24:    */       
/*  25:115 */       slowCos(x, result);
/*  26:116 */       COSINE_TABLE_A[i] = result[0];
/*  27:117 */       COSINE_TABLE_B[i] = result[1];
/*  28:    */     }
/*  29:121 */     for (int i = 7; i < SINE_TABLE_LEN; i++)
/*  30:    */     {
/*  31:122 */       double[] xs = new double[2];
/*  32:123 */       double[] ys = new double[2];
/*  33:124 */       double[] as = new double[2];
/*  34:125 */       double[] bs = new double[2];
/*  35:126 */       double[] temps = new double[2];
/*  36:128 */       if ((i & 0x1) == 0)
/*  37:    */       {
/*  38:130 */         xs[0] = SINE_TABLE_A[(i / 2)];
/*  39:131 */         xs[1] = SINE_TABLE_B[(i / 2)];
/*  40:132 */         ys[0] = COSINE_TABLE_A[(i / 2)];
/*  41:133 */         ys[1] = COSINE_TABLE_B[(i / 2)];
/*  42:    */         
/*  43:    */ 
/*  44:136 */         splitMult(xs, ys, result);
/*  45:137 */         SINE_TABLE_A[i] = (result[0] * 2.0D);
/*  46:138 */         SINE_TABLE_B[i] = (result[1] * 2.0D);
/*  47:    */         
/*  48:    */ 
/*  49:141 */         splitMult(ys, ys, as);
/*  50:142 */         splitMult(xs, xs, temps);
/*  51:143 */         temps[0] = (-temps[0]);
/*  52:144 */         temps[1] = (-temps[1]);
/*  53:145 */         splitAdd(as, temps, result);
/*  54:146 */         COSINE_TABLE_A[i] = result[0];
/*  55:147 */         COSINE_TABLE_B[i] = result[1];
/*  56:    */       }
/*  57:    */       else
/*  58:    */       {
/*  59:149 */         xs[0] = SINE_TABLE_A[(i / 2)];
/*  60:150 */         xs[1] = SINE_TABLE_B[(i / 2)];
/*  61:151 */         ys[0] = COSINE_TABLE_A[(i / 2)];
/*  62:152 */         ys[1] = COSINE_TABLE_B[(i / 2)];
/*  63:153 */         as[0] = SINE_TABLE_A[(i / 2 + 1)];
/*  64:154 */         as[1] = SINE_TABLE_B[(i / 2 + 1)];
/*  65:155 */         bs[0] = COSINE_TABLE_A[(i / 2 + 1)];
/*  66:156 */         bs[1] = COSINE_TABLE_B[(i / 2 + 1)];
/*  67:    */         
/*  68:    */ 
/*  69:159 */         splitMult(xs, bs, temps);
/*  70:160 */         splitMult(ys, as, result);
/*  71:161 */         splitAdd(result, temps, result);
/*  72:162 */         SINE_TABLE_A[i] = result[0];
/*  73:163 */         SINE_TABLE_B[i] = result[1];
/*  74:    */         
/*  75:    */ 
/*  76:166 */         splitMult(ys, bs, result);
/*  77:167 */         splitMult(xs, as, temps);
/*  78:168 */         temps[0] = (-temps[0]);
/*  79:169 */         temps[1] = (-temps[1]);
/*  80:170 */         splitAdd(result, temps, result);
/*  81:171 */         COSINE_TABLE_A[i] = result[0];
/*  82:172 */         COSINE_TABLE_B[i] = result[1];
/*  83:    */       }
/*  84:    */     }
/*  85:177 */     for (int i = 0; i < SINE_TABLE_LEN; i++)
/*  86:    */     {
/*  87:178 */       double[] xs = new double[2];
/*  88:179 */       double[] ys = new double[2];
/*  89:180 */       double[] as = new double[2];
/*  90:    */       
/*  91:182 */       as[0] = COSINE_TABLE_A[i];
/*  92:183 */       as[1] = COSINE_TABLE_B[i];
/*  93:    */       
/*  94:185 */       splitReciprocal(as, ys);
/*  95:    */       
/*  96:187 */       xs[0] = SINE_TABLE_A[i];
/*  97:188 */       xs[1] = SINE_TABLE_B[i];
/*  98:    */       
/*  99:190 */       splitMult(xs, ys, as);
/* 100:    */       
/* 101:192 */       TANGENT_TABLE_A[i] = as[0];
/* 102:193 */       TANGENT_TABLE_B[i] = as[1];
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   static double slowCos(double x, double[] result)
/* 107:    */   {
/* 108:208 */     double[] xs = new double[2];
/* 109:209 */     double[] ys = new double[2];
/* 110:210 */     double[] facts = new double[2];
/* 111:211 */     double[] as = new double[2];
/* 112:212 */     split(x, xs); double 
/* 113:213 */       tmp31_30 = 0.0D;ys[1] = tmp31_30;ys[0] = tmp31_30;
/* 114:215 */     for (int i = FACT.length - 1; i >= 0; i--)
/* 115:    */     {
/* 116:216 */       splitMult(xs, ys, as);
/* 117:217 */       ys[0] = as[0];ys[1] = as[1];
/* 118:219 */       if ((i & 0x1) == 0)
/* 119:    */       {
/* 120:223 */         split(FACT[i], as);
/* 121:224 */         splitReciprocal(as, facts);
/* 122:226 */         if ((i & 0x2) != 0)
/* 123:    */         {
/* 124:227 */           facts[0] = (-facts[0]);
/* 125:228 */           facts[1] = (-facts[1]);
/* 126:    */         }
/* 127:231 */         splitAdd(ys, facts, as);
/* 128:232 */         ys[0] = as[0];ys[1] = as[1];
/* 129:    */       }
/* 130:    */     }
/* 131:235 */     if (result != null)
/* 132:    */     {
/* 133:236 */       result[0] = ys[0];
/* 134:237 */       result[1] = ys[1];
/* 135:    */     }
/* 136:240 */     return ys[0] + ys[1];
/* 137:    */   }
/* 138:    */   
/* 139:    */   static double slowSin(double x, double[] result)
/* 140:    */   {
/* 141:252 */     double[] xs = new double[2];
/* 142:253 */     double[] ys = new double[2];
/* 143:254 */     double[] facts = new double[2];
/* 144:255 */     double[] as = new double[2];
/* 145:256 */     split(x, xs); double 
/* 146:257 */       tmp31_30 = 0.0D;ys[1] = tmp31_30;ys[0] = tmp31_30;
/* 147:259 */     for (int i = FACT.length - 1; i >= 0; i--)
/* 148:    */     {
/* 149:260 */       splitMult(xs, ys, as);
/* 150:261 */       ys[0] = as[0];ys[1] = as[1];
/* 151:263 */       if ((i & 0x1) != 0)
/* 152:    */       {
/* 153:267 */         split(FACT[i], as);
/* 154:268 */         splitReciprocal(as, facts);
/* 155:270 */         if ((i & 0x2) != 0)
/* 156:    */         {
/* 157:271 */           facts[0] = (-facts[0]);
/* 158:272 */           facts[1] = (-facts[1]);
/* 159:    */         }
/* 160:275 */         splitAdd(ys, facts, as);
/* 161:276 */         ys[0] = as[0];ys[1] = as[1];
/* 162:    */       }
/* 163:    */     }
/* 164:279 */     if (result != null)
/* 165:    */     {
/* 166:280 */       result[0] = ys[0];
/* 167:281 */       result[1] = ys[1];
/* 168:    */     }
/* 169:284 */     return ys[0] + ys[1];
/* 170:    */   }
/* 171:    */   
/* 172:    */   static double slowexp(double x, double[] result)
/* 173:    */   {
/* 174:296 */     double[] xs = new double[2];
/* 175:297 */     double[] ys = new double[2];
/* 176:298 */     double[] facts = new double[2];
/* 177:299 */     double[] as = new double[2];
/* 178:300 */     split(x, xs); double 
/* 179:301 */       tmp31_30 = 0.0D;ys[1] = tmp31_30;ys[0] = tmp31_30;
/* 180:303 */     for (int i = FACT.length - 1; i >= 0; i--)
/* 181:    */     {
/* 182:304 */       splitMult(xs, ys, as);
/* 183:305 */       ys[0] = as[0];
/* 184:306 */       ys[1] = as[1];
/* 185:    */       
/* 186:308 */       split(FACT[i], as);
/* 187:309 */       splitReciprocal(as, facts);
/* 188:    */       
/* 189:311 */       splitAdd(ys, facts, as);
/* 190:312 */       ys[0] = as[0];
/* 191:313 */       ys[1] = as[1];
/* 192:    */     }
/* 193:316 */     if (result != null)
/* 194:    */     {
/* 195:317 */       result[0] = ys[0];
/* 196:318 */       result[1] = ys[1];
/* 197:    */     }
/* 198:321 */     return ys[0] + ys[1];
/* 199:    */   }
/* 200:    */   
/* 201:    */   private static void split(double d, double[] split)
/* 202:    */   {
/* 203:330 */     if ((d < 8.0E+298D) && (d > -8.0E+298D))
/* 204:    */     {
/* 205:331 */       double a = d * 1073741824.0D;
/* 206:332 */       split[0] = (d + a - a);
/* 207:333 */       split[1] = (d - split[0]);
/* 208:    */     }
/* 209:    */     else
/* 210:    */     {
/* 211:335 */       double a = d * 9.313225746154785E-010D;
/* 212:336 */       split[0] = ((d + a - d) * 1073741824.0D);
/* 213:337 */       split[1] = (d - split[0]);
/* 214:    */     }
/* 215:    */   }
/* 216:    */   
/* 217:    */   private static void resplit(double[] a)
/* 218:    */   {
/* 219:346 */     double c = a[0] + a[1];
/* 220:347 */     double d = -(c - a[0] - a[1]);
/* 221:349 */     if ((c < 8.0E+298D) && (c > -8.0E+298D))
/* 222:    */     {
/* 223:350 */       double z = c * 1073741824.0D;
/* 224:351 */       a[0] = (c + z - z);
/* 225:352 */       a[1] = (c - a[0] + d);
/* 226:    */     }
/* 227:    */     else
/* 228:    */     {
/* 229:354 */       double z = c * 9.313225746154785E-010D;
/* 230:355 */       a[0] = ((c + z - c) * 1073741824.0D);
/* 231:356 */       a[1] = (c - a[0] + d);
/* 232:    */     }
/* 233:    */   }
/* 234:    */   
/* 235:    */   private static void splitMult(double[] a, double[] b, double[] ans)
/* 236:    */   {
/* 237:366 */     a[0] *= b[0];
/* 238:367 */     ans[1] = (a[0] * b[1] + a[1] * b[0] + a[1] * b[1]);
/* 239:    */     
/* 240:    */ 
/* 241:370 */     resplit(ans);
/* 242:    */   }
/* 243:    */   
/* 244:    */   private static void splitAdd(double[] a, double[] b, double[] ans)
/* 245:    */   {
/* 246:379 */     a[0] += b[0];
/* 247:380 */     a[1] += b[1];
/* 248:    */     
/* 249:382 */     resplit(ans);
/* 250:    */   }
/* 251:    */   
/* 252:    */   static void splitReciprocal(double[] in, double[] result)
/* 253:    */   {
/* 254:404 */     double b = 2.384185791015625E-007D;
/* 255:405 */     double a = 0.999999761581421D;
/* 256:407 */     if (in[0] == 0.0D)
/* 257:    */     {
/* 258:408 */       in[0] = in[1];
/* 259:409 */       in[1] = 0.0D;
/* 260:    */     }
/* 261:412 */     result[0] = (0.999999761581421D / in[0]);
/* 262:413 */     result[1] = ((2.384185791015625E-007D * in[0] - 0.999999761581421D * in[1]) / (in[0] * in[0] + in[0] * in[1]));
/* 263:415 */     if (result[1] != result[1]) {
/* 264:416 */       result[1] = 0.0D;
/* 265:    */     }
/* 266:420 */     resplit(result);
/* 267:422 */     for (int i = 0; i < 2; i++)
/* 268:    */     {
/* 269:424 */       double err = 1.0D - result[0] * in[0] - result[0] * in[1] - result[1] * in[0] - result[1] * in[1];
/* 270:    */       
/* 271:    */ 
/* 272:427 */       err *= (result[0] + result[1]);
/* 273:    */       
/* 274:429 */       result[1] += err;
/* 275:    */     }
/* 276:    */   }
/* 277:    */   
/* 278:    */   private static void quadMult(double[] a, double[] b, double[] result)
/* 279:    */   {
/* 280:439 */     double[] xs = new double[2];
/* 281:440 */     double[] ys = new double[2];
/* 282:441 */     double[] zs = new double[2];
/* 283:    */     
/* 284:    */ 
/* 285:444 */     split(a[0], xs);
/* 286:445 */     split(b[0], ys);
/* 287:446 */     splitMult(xs, ys, zs);
/* 288:    */     
/* 289:448 */     result[0] = zs[0];
/* 290:449 */     result[1] = zs[1];
/* 291:    */     
/* 292:    */ 
/* 293:452 */     split(b[1], ys);
/* 294:453 */     splitMult(xs, ys, zs);
/* 295:    */     
/* 296:455 */     double tmp = result[0] + zs[0];
/* 297:456 */     result[1] -= tmp - result[0] - zs[0];
/* 298:457 */     result[0] = tmp;
/* 299:458 */     tmp = result[0] + zs[1];
/* 300:459 */     result[1] -= tmp - result[0] - zs[1];
/* 301:460 */     result[0] = tmp;
/* 302:    */     
/* 303:    */ 
/* 304:463 */     split(a[1], xs);
/* 305:464 */     split(b[0], ys);
/* 306:465 */     splitMult(xs, ys, zs);
/* 307:    */     
/* 308:467 */     tmp = result[0] + zs[0];
/* 309:468 */     result[1] -= tmp - result[0] - zs[0];
/* 310:469 */     result[0] = tmp;
/* 311:470 */     tmp = result[0] + zs[1];
/* 312:471 */     result[1] -= tmp - result[0] - zs[1];
/* 313:472 */     result[0] = tmp;
/* 314:    */     
/* 315:    */ 
/* 316:475 */     split(a[1], xs);
/* 317:476 */     split(b[1], ys);
/* 318:477 */     splitMult(xs, ys, zs);
/* 319:    */     
/* 320:479 */     tmp = result[0] + zs[0];
/* 321:480 */     result[1] -= tmp - result[0] - zs[0];
/* 322:481 */     result[0] = tmp;
/* 323:482 */     tmp = result[0] + zs[1];
/* 324:483 */     result[1] -= tmp - result[0] - zs[1];
/* 325:484 */     result[0] = tmp;
/* 326:    */   }
/* 327:    */   
/* 328:    */   static double expint(int p, double[] result)
/* 329:    */   {
/* 330:494 */     double[] xs = new double[2];
/* 331:495 */     double[] as = new double[2];
/* 332:496 */     double[] ys = new double[2];
/* 333:    */     
/* 334:    */ 
/* 335:    */ 
/* 336:    */ 
/* 337:    */ 
/* 338:    */ 
/* 339:    */ 
/* 340:    */ 
/* 341:505 */     xs[0] = 2.718281828459045D;
/* 342:506 */     xs[1] = 1.44564689172925E-016D;
/* 343:    */     
/* 344:508 */     split(1.0D, ys);
/* 345:510 */     while (p > 0)
/* 346:    */     {
/* 347:511 */       if ((p & 0x1) != 0)
/* 348:    */       {
/* 349:512 */         quadMult(ys, xs, as);
/* 350:513 */         ys[0] = as[0];ys[1] = as[1];
/* 351:    */       }
/* 352:516 */       quadMult(xs, xs, as);
/* 353:517 */       xs[0] = as[0];xs[1] = as[1];
/* 354:    */       
/* 355:519 */       p >>= 1;
/* 356:    */     }
/* 357:522 */     if (result != null)
/* 358:    */     {
/* 359:523 */       result[0] = ys[0];
/* 360:524 */       result[1] = ys[1];
/* 361:    */       
/* 362:526 */       resplit(result);
/* 363:    */     }
/* 364:529 */     return ys[0] + ys[1];
/* 365:    */   }
/* 366:    */   
/* 367:    */   static double[] slowLog(double xi)
/* 368:    */   {
/* 369:551 */     double[] x = new double[2];
/* 370:552 */     double[] x2 = new double[2];
/* 371:553 */     double[] y = new double[2];
/* 372:554 */     double[] a = new double[2];
/* 373:    */     
/* 374:556 */     split(xi, x);
/* 375:    */     
/* 376:    */ 
/* 377:559 */     x[0] += 1.0D;
/* 378:560 */     resplit(x);
/* 379:561 */     splitReciprocal(x, a);
/* 380:562 */     x[0] -= 2.0D;
/* 381:563 */     resplit(x);
/* 382:564 */     splitMult(x, a, y);
/* 383:565 */     x[0] = y[0];
/* 384:566 */     x[1] = y[1];
/* 385:    */     
/* 386:    */ 
/* 387:569 */     splitMult(x, x, x2);
/* 388:    */     
/* 389:    */ 
/* 390:    */ 
/* 391:    */ 
/* 392:    */ 
/* 393:575 */     y[0] = LN_SPLIT_COEF[(LN_SPLIT_COEF.length - 1)][0];
/* 394:576 */     y[1] = LN_SPLIT_COEF[(LN_SPLIT_COEF.length - 1)][1];
/* 395:578 */     for (int i = LN_SPLIT_COEF.length - 2; i >= 0; i--)
/* 396:    */     {
/* 397:579 */       splitMult(y, x2, a);
/* 398:580 */       y[0] = a[0];
/* 399:581 */       y[1] = a[1];
/* 400:582 */       splitAdd(y, LN_SPLIT_COEF[i], a);
/* 401:583 */       y[0] = a[0];
/* 402:584 */       y[1] = a[1];
/* 403:    */     }
/* 404:587 */     splitMult(y, x, a);
/* 405:588 */     y[0] = a[0];
/* 406:589 */     y[1] = a[1];
/* 407:    */     
/* 408:591 */     return y;
/* 409:    */   }
/* 410:    */   
/* 411:    */   static void printarray(PrintStream out, String name, int expectedLen, double[][] array2d)
/* 412:    */   {
/* 413:603 */     out.println(name);
/* 414:604 */     checkLen(expectedLen, array2d.length);
/* 415:605 */     out.println("    { ");
/* 416:606 */     int i = 0;
/* 417:607 */     for (double[] array : array2d)
/* 418:    */     {
/* 419:608 */       out.print("        {");
/* 420:609 */       for (double d : array) {
/* 421:610 */         out.printf("%-25.25s", new Object[] { format(d) });
/* 422:    */       }
/* 423:612 */       out.println("}, // " + i++);
/* 424:    */     }
/* 425:614 */     out.println("    };");
/* 426:    */   }
/* 427:    */   
/* 428:    */   static void printarray(PrintStream out, String name, int expectedLen, double[] array)
/* 429:    */   {
/* 430:625 */     out.println(name + "=");
/* 431:626 */     checkLen(expectedLen, array.length);
/* 432:627 */     out.println("    {");
/* 433:628 */     for (double d : array) {
/* 434:629 */       out.printf("        %s%n", new Object[] { format(d) });
/* 435:    */     }
/* 436:631 */     out.println("    };");
/* 437:    */   }
/* 438:    */   
/* 439:    */   static String format(double d)
/* 440:    */   {
/* 441:639 */     if (d != d) {
/* 442:640 */       return "Double.NaN,";
/* 443:    */     }
/* 444:642 */     return (d >= 0.0D ? "+" : "") + Double.toString(d) + "d,";
/* 445:    */   }
/* 446:    */   
/* 447:    */   private static void checkLen(int expectedLen, int actual)
/* 448:    */     throws DimensionMismatchException
/* 449:    */   {
/* 450:654 */     if (expectedLen != actual) {
/* 451:655 */       throw new DimensionMismatchException(actual, expectedLen);
/* 452:    */     }
/* 453:    */   }
/* 454:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.FastMathCalc
 * JD-Core Version:    0.7.0.1
 */
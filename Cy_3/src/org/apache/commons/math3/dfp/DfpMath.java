/*   1:    */ package org.apache.commons.math3.dfp;
/*   2:    */ 
/*   3:    */ public class DfpMath
/*   4:    */ {
/*   5:    */   private static final String POW_TRAP = "pow";
/*   6:    */   
/*   7:    */   protected static Dfp[] split(DfpField field, String a)
/*   8:    */   {
/*   9: 46 */     Dfp[] result = new Dfp[2];
/*  10:    */     
/*  11: 48 */     boolean leading = true;
/*  12: 49 */     int sp = 0;
/*  13: 50 */     int sig = 0;
/*  14:    */     
/*  15: 52 */     char[] buf = new char[a.length()];
/*  16: 54 */     for (int i = 0; i < buf.length; i++)
/*  17:    */     {
/*  18: 55 */       buf[i] = a.charAt(i);
/*  19: 57 */       if ((buf[i] >= '1') && (buf[i] <= '9')) {
/*  20: 58 */         leading = false;
/*  21:    */       }
/*  22: 61 */       if (buf[i] == '.')
/*  23:    */       {
/*  24: 62 */         sig += (400 - sig) % 4;
/*  25: 63 */         leading = false;
/*  26:    */       }
/*  27: 66 */       if (sig == field.getRadixDigits() / 2 * 4)
/*  28:    */       {
/*  29: 67 */         sp = i;
/*  30: 68 */         break;
/*  31:    */       }
/*  32: 71 */       if ((buf[i] >= '0') && (buf[i] <= '9') && (!leading)) {
/*  33: 72 */         sig++;
/*  34:    */       }
/*  35:    */     }
/*  36: 76 */     result[0] = field.newDfp(new String(buf, 0, sp));
/*  37: 78 */     for (int i = 0; i < buf.length; i++)
/*  38:    */     {
/*  39: 79 */       buf[i] = a.charAt(i);
/*  40: 80 */       if ((buf[i] >= '0') && (buf[i] <= '9') && (i < sp)) {
/*  41: 81 */         buf[i] = '0';
/*  42:    */       }
/*  43:    */     }
/*  44: 85 */     result[1] = field.newDfp(new String(buf));
/*  45:    */     
/*  46: 87 */     return result;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected static Dfp[] split(Dfp a)
/*  50:    */   {
/*  51: 95 */     Dfp[] result = new Dfp[2];
/*  52: 96 */     Dfp shift = a.multiply(a.power10K(a.getRadixDigits() / 2));
/*  53: 97 */     result[0] = a.add(shift).subtract(shift);
/*  54: 98 */     result[1] = a.subtract(result[0]);
/*  55: 99 */     return result;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected static Dfp[] splitMult(Dfp[] a, Dfp[] b)
/*  59:    */   {
/*  60:111 */     Dfp[] result = new Dfp[2];
/*  61:    */     
/*  62:113 */     result[1] = a[0].getZero();
/*  63:114 */     result[0] = a[0].multiply(b[0]);
/*  64:120 */     if ((result[0].classify() == 1) || (result[0].equals(result[1]))) {
/*  65:121 */       return result;
/*  66:    */     }
/*  67:124 */     result[1] = a[0].multiply(b[1]).add(a[1].multiply(b[0])).add(a[1].multiply(b[1]));
/*  68:    */     
/*  69:126 */     return result;
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected static Dfp[] splitDiv(Dfp[] a, Dfp[] b)
/*  73:    */   {
/*  74:139 */     Dfp[] result = new Dfp[2];
/*  75:    */     
/*  76:141 */     result[0] = a[0].divide(b[0]);
/*  77:142 */     result[1] = a[1].multiply(b[0]).subtract(a[0].multiply(b[1]));
/*  78:143 */     result[1] = result[1].divide(b[0].multiply(b[0]).add(b[0].multiply(b[1])));
/*  79:    */     
/*  80:145 */     return result;
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected static Dfp splitPow(Dfp[] base, int a)
/*  84:    */   {
/*  85:154 */     boolean invert = false;
/*  86:    */     
/*  87:156 */     Dfp[] r = new Dfp[2];
/*  88:    */     
/*  89:158 */     Dfp[] result = new Dfp[2];
/*  90:159 */     result[0] = base[0].getOne();
/*  91:160 */     result[1] = base[0].getZero();
/*  92:162 */     if (a == 0) {
/*  93:164 */       return result[0].add(result[1]);
/*  94:    */     }
/*  95:167 */     if (a < 0)
/*  96:    */     {
/*  97:169 */       invert = true;
/*  98:170 */       a = -a;
/*  99:    */     }
/* 100:    */     do
/* 101:    */     {
/* 102:175 */       r[0] = new Dfp(base[0]);
/* 103:176 */       r[1] = new Dfp(base[1]);
/* 104:177 */       int trial = 1;
/* 105:    */       int prevtrial;
/* 106:    */       for (;;)
/* 107:    */       {
/* 108:181 */         prevtrial = trial;
/* 109:182 */         trial *= 2;
/* 110:183 */         if (trial > a) {
/* 111:    */           break;
/* 112:    */         }
/* 113:186 */         r = splitMult(r, r);
/* 114:    */       }
/* 115:189 */       trial = prevtrial;
/* 116:    */       
/* 117:191 */       a -= trial;
/* 118:192 */       result = splitMult(result, r);
/* 119:194 */     } while (a >= 1);
/* 120:196 */     result[0] = result[0].add(result[1]);
/* 121:198 */     if (invert) {
/* 122:199 */       result[0] = base[0].getOne().divide(result[0]);
/* 123:    */     }
/* 124:202 */     return result[0];
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static Dfp pow(Dfp base, int a)
/* 128:    */   {
/* 129:213 */     boolean invert = false;
/* 130:    */     
/* 131:215 */     Dfp result = base.getOne();
/* 132:217 */     if (a == 0) {
/* 133:219 */       return result;
/* 134:    */     }
/* 135:222 */     if (a < 0)
/* 136:    */     {
/* 137:223 */       invert = true;
/* 138:224 */       a = -a;
/* 139:    */     }
/* 140:    */     do
/* 141:    */     {
/* 142:229 */       Dfp r = new Dfp(base);
/* 143:    */       
/* 144:231 */       int trial = 1;
/* 145:    */       Dfp prevr;
/* 146:    */       int prevtrial;
/* 147:    */       do
/* 148:    */       {
/* 149:235 */         prevr = new Dfp(r);
/* 150:236 */         prevtrial = trial;
/* 151:237 */         r = r.multiply(r);
/* 152:238 */         trial *= 2;
/* 153:239 */       } while (a > trial);
/* 154:241 */       r = prevr;
/* 155:242 */       trial = prevtrial;
/* 156:    */       
/* 157:244 */       a -= trial;
/* 158:245 */       result = result.multiply(r);
/* 159:247 */     } while (a >= 1);
/* 160:249 */     if (invert) {
/* 161:250 */       result = base.getOne().divide(result);
/* 162:    */     }
/* 163:253 */     return base.newInstance(result);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static Dfp exp(Dfp a)
/* 167:    */   {
/* 168:266 */     Dfp inta = a.rint();
/* 169:267 */     Dfp fraca = a.subtract(inta);
/* 170:    */     
/* 171:269 */     int ia = inta.intValue();
/* 172:270 */     if (ia > 2147483646) {
/* 173:272 */       return a.newInstance((byte)1, (byte)1);
/* 174:    */     }
/* 175:275 */     if (ia < -2147483646) {
/* 176:277 */       return a.newInstance();
/* 177:    */     }
/* 178:280 */     Dfp einta = splitPow(a.getField().getESplit(), ia);
/* 179:281 */     Dfp efraca = expInternal(fraca);
/* 180:    */     
/* 181:283 */     return einta.multiply(efraca);
/* 182:    */   }
/* 183:    */   
/* 184:    */   protected static Dfp expInternal(Dfp a)
/* 185:    */   {
/* 186:292 */     Dfp y = a.getOne();
/* 187:293 */     Dfp x = a.getOne();
/* 188:294 */     Dfp fact = a.getOne();
/* 189:295 */     Dfp py = new Dfp(y);
/* 190:297 */     for (int i = 1; i < 90; i++)
/* 191:    */     {
/* 192:298 */       x = x.multiply(a);
/* 193:299 */       fact = fact.divide(i);
/* 194:300 */       y = y.add(x.multiply(fact));
/* 195:301 */       if (y.equals(py)) {
/* 196:    */         break;
/* 197:    */       }
/* 198:304 */       py = new Dfp(y);
/* 199:    */     }
/* 200:307 */     return y;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public static Dfp log(Dfp a)
/* 204:    */   {
/* 205:321 */     int p2 = 0;
/* 206:324 */     if ((a.equals(a.getZero())) || (a.lessThan(a.getZero())) || (a.isNaN()))
/* 207:    */     {
/* 208:326 */       a.getField().setIEEEFlagsBits(1);
/* 209:327 */       return a.dotrap(1, "ln", a, a.newInstance((byte)1, (byte)3));
/* 210:    */     }
/* 211:330 */     if (a.classify() == 1) {
/* 212:331 */       return a;
/* 213:    */     }
/* 214:334 */     Dfp x = new Dfp(a);
/* 215:335 */     int lr = x.log10K();
/* 216:    */     
/* 217:337 */     x = x.divide(pow(a.newInstance(10000), lr));
/* 218:338 */     int ix = x.floor().intValue();
/* 219:340 */     while (ix > 2)
/* 220:    */     {
/* 221:341 */       ix >>= 1;
/* 222:342 */       p2++;
/* 223:    */     }
/* 224:346 */     Dfp[] spx = split(x);
/* 225:347 */     Dfp[] spy = new Dfp[2];
/* 226:348 */     spy[0] = pow(a.getTwo(), p2);
/* 227:349 */     spx[0] = spx[0].divide(spy[0]);
/* 228:350 */     spx[1] = spx[1].divide(spy[0]);
/* 229:    */     
/* 230:352 */     spy[0] = a.newInstance("1.33333");
/* 231:353 */     while (spx[0].add(spx[1]).greaterThan(spy[0]))
/* 232:    */     {
/* 233:354 */       spx[0] = spx[0].divide(2);
/* 234:355 */       spx[1] = spx[1].divide(2);
/* 235:356 */       p2++;
/* 236:    */     }
/* 237:360 */     Dfp[] spz = logInternal(spx);
/* 238:    */     
/* 239:362 */     spx[0] = a.newInstance(p2 + 4 * lr);
/* 240:363 */     spx[1] = a.getZero();
/* 241:364 */     spy = splitMult(a.getField().getLn2Split(), spx);
/* 242:    */     
/* 243:366 */     spz[0] = spz[0].add(spy[0]);
/* 244:367 */     spz[1] = spz[1].add(spy[1]);
/* 245:    */     
/* 246:369 */     spx[0] = a.newInstance(4 * lr);
/* 247:370 */     spx[1] = a.getZero();
/* 248:371 */     spy = splitMult(a.getField().getLn5Split(), spx);
/* 249:    */     
/* 250:373 */     spz[0] = spz[0].add(spy[0]);
/* 251:374 */     spz[1] = spz[1].add(spy[1]);
/* 252:    */     
/* 253:376 */     return a.newInstance(spz[0].add(spz[1]));
/* 254:    */   }
/* 255:    */   
/* 256:    */   protected static Dfp[] logInternal(Dfp[] a)
/* 257:    */   {
/* 258:440 */     Dfp t = a[0].divide(4).add(a[1].divide(4));
/* 259:441 */     Dfp x = t.add(a[0].newInstance("-0.25")).divide(t.add(a[0].newInstance("0.25")));
/* 260:    */     
/* 261:443 */     Dfp y = new Dfp(x);
/* 262:444 */     Dfp num = new Dfp(x);
/* 263:445 */     Dfp py = new Dfp(y);
/* 264:446 */     int den = 1;
/* 265:447 */     for (int i = 0; i < 10000; i++)
/* 266:    */     {
/* 267:448 */       num = num.multiply(x);
/* 268:449 */       num = num.multiply(x);
/* 269:450 */       den += 2;
/* 270:451 */       t = num.divide(den);
/* 271:452 */       y = y.add(t);
/* 272:453 */       if (y.equals(py)) {
/* 273:    */         break;
/* 274:    */       }
/* 275:456 */       py = new Dfp(y);
/* 276:    */     }
/* 277:459 */     y = y.multiply(a[0].getTwo());
/* 278:    */     
/* 279:461 */     return split(y);
/* 280:    */   }
/* 281:    */   
/* 282:    */   public static Dfp pow(Dfp x, Dfp y)
/* 283:    */   {
/* 284:508 */     if (x.getField().getRadixDigits() != y.getField().getRadixDigits())
/* 285:    */     {
/* 286:509 */       x.getField().setIEEEFlagsBits(1);
/* 287:510 */       Dfp result = x.newInstance(x.getZero());
/* 288:511 */       result.nans = 3;
/* 289:512 */       return x.dotrap(1, "pow", x, result);
/* 290:    */     }
/* 291:515 */     Dfp zero = x.getZero();
/* 292:516 */     Dfp one = x.getOne();
/* 293:517 */     Dfp two = x.getTwo();
/* 294:518 */     boolean invert = false;
/* 295:522 */     if (y.equals(zero)) {
/* 296:523 */       return x.newInstance(one);
/* 297:    */     }
/* 298:526 */     if (y.equals(one))
/* 299:    */     {
/* 300:527 */       if (x.isNaN())
/* 301:    */       {
/* 302:529 */         x.getField().setIEEEFlagsBits(1);
/* 303:530 */         return x.dotrap(1, "pow", x, x);
/* 304:    */       }
/* 305:532 */       return x;
/* 306:    */     }
/* 307:535 */     if ((x.isNaN()) || (y.isNaN()))
/* 308:    */     {
/* 309:537 */       x.getField().setIEEEFlagsBits(1);
/* 310:538 */       return x.dotrap(1, "pow", x, x.newInstance((byte)1, (byte)3));
/* 311:    */     }
/* 312:542 */     if (x.equals(zero))
/* 313:    */     {
/* 314:543 */       if (Dfp.copysign(one, x).greaterThan(zero))
/* 315:    */       {
/* 316:545 */         if (y.greaterThan(zero)) {
/* 317:546 */           return x.newInstance(zero);
/* 318:    */         }
/* 319:548 */         return x.newInstance(x.newInstance((byte)1, (byte)1));
/* 320:    */       }
/* 321:552 */       if ((y.classify() == 0) && (y.rint().equals(y)) && (!y.remainder(two).equals(zero)))
/* 322:    */       {
/* 323:554 */         if (y.greaterThan(zero)) {
/* 324:555 */           return x.newInstance(zero.negate());
/* 325:    */         }
/* 326:557 */         return x.newInstance(x.newInstance((byte)-1, (byte)1));
/* 327:    */       }
/* 328:561 */       if (y.greaterThan(zero)) {
/* 329:562 */         return x.newInstance(zero);
/* 330:    */       }
/* 331:564 */       return x.newInstance(x.newInstance((byte)1, (byte)1));
/* 332:    */     }
/* 333:570 */     if (x.lessThan(zero))
/* 334:    */     {
/* 335:572 */       x = x.negate();
/* 336:573 */       invert = true;
/* 337:    */     }
/* 338:576 */     if ((x.greaterThan(one)) && (y.classify() == 1))
/* 339:    */     {
/* 340:577 */       if (y.greaterThan(zero)) {
/* 341:578 */         return y;
/* 342:    */       }
/* 343:580 */       return x.newInstance(zero);
/* 344:    */     }
/* 345:584 */     if ((x.lessThan(one)) && (y.classify() == 1))
/* 346:    */     {
/* 347:585 */       if (y.greaterThan(zero)) {
/* 348:586 */         return x.newInstance(zero);
/* 349:    */       }
/* 350:588 */       return x.newInstance(Dfp.copysign(y, one));
/* 351:    */     }
/* 352:592 */     if ((x.equals(one)) && (y.classify() == 1))
/* 353:    */     {
/* 354:593 */       x.getField().setIEEEFlagsBits(1);
/* 355:594 */       return x.dotrap(1, "pow", x, x.newInstance((byte)1, (byte)3));
/* 356:    */     }
/* 357:597 */     if (x.classify() == 1)
/* 358:    */     {
/* 359:599 */       if (invert)
/* 360:    */       {
/* 361:601 */         if ((y.classify() == 0) && (y.rint().equals(y)) && (!y.remainder(two).equals(zero)))
/* 362:    */         {
/* 363:603 */           if (y.greaterThan(zero)) {
/* 364:604 */             return x.newInstance(x.newInstance((byte)-1, (byte)1));
/* 365:    */           }
/* 366:606 */           return x.newInstance(zero.negate());
/* 367:    */         }
/* 368:610 */         if (y.greaterThan(zero)) {
/* 369:611 */           return x.newInstance(x.newInstance((byte)1, (byte)1));
/* 370:    */         }
/* 371:613 */         return x.newInstance(zero);
/* 372:    */       }
/* 373:618 */       if (y.greaterThan(zero)) {
/* 374:619 */         return x;
/* 375:    */       }
/* 376:621 */       return x.newInstance(zero);
/* 377:    */     }
/* 378:626 */     if ((invert) && (!y.rint().equals(y)))
/* 379:    */     {
/* 380:627 */       x.getField().setIEEEFlagsBits(1);
/* 381:628 */       return x.dotrap(1, "pow", x, x.newInstance((byte)1, (byte)3));
/* 382:    */     }
/* 383:    */     Dfp r;
/* 384:    */    
/* 385:634 */     if ((y.lessThan(x.newInstance(100000000))) && (y.greaterThan(x.newInstance(-100000000))))
/* 386:    */     {
/* 387:635 */       Dfp u = y.rint();
/* 388:636 */       int ui = u.intValue();
/* 389:    */       
/* 390:638 */       Dfp v = y.subtract(u);
/* 391:640 */       if (v.unequal(zero))
/* 392:    */       {
/* 393:641 */         Dfp a = v.multiply(log(x));
/* 394:642 */         Dfp b = a.divide(x.getField().getLn2()).rint();
/* 395:    */         
/* 396:644 */         Dfp c = a.subtract(b.multiply(x.getField().getLn2()));
/* 397:645 */         r = splitPow(split(x), ui);
/* 398:646 */         r = r.multiply(pow(two, b.intValue()));
/* 399:647 */         r = r.multiply(exp(c));
/* 400:    */       }
/* 401:    */       else
/* 402:    */       {
/* 403:649 */         r = splitPow(split(x), ui);
/* 404:    */       }
/* 405:    */     }
/* 406:    */     else
/* 407:    */     {
/* 408:653 */       r = exp(log(x).multiply(y));
/* 409:    */     }
/* 410:656 */     if (invert) {
/* 411:658 */       if ((y.rint().equals(y)) && (!y.remainder(two).equals(zero))) {
/* 412:659 */         r = r.negate();
/* 413:    */       }
/* 414:    */     }
/* 415:663 */     return x.newInstance(r);
/* 416:    */   }
/* 417:    */   
/* 418:    */   protected static Dfp sinInternal(Dfp[] a)
/* 419:    */   {
/* 420:674 */     Dfp c = a[0].add(a[1]);
/* 421:675 */     Dfp y = c;
/* 422:676 */     c = c.multiply(c);
/* 423:677 */     Dfp x = y;
/* 424:678 */     Dfp fact = a[0].getOne();
/* 425:679 */     Dfp py = new Dfp(y);
/* 426:681 */     for (int i = 3; i < 90; i += 2)
/* 427:    */     {
/* 428:682 */       x = x.multiply(c);
/* 429:683 */       x = x.negate();
/* 430:    */       
/* 431:685 */       fact = fact.divide((i - 1) * i);
/* 432:686 */       y = y.add(x.multiply(fact));
/* 433:687 */       if (y.equals(py)) {
/* 434:    */         break;
/* 435:    */       }
/* 436:690 */       py = new Dfp(y);
/* 437:    */     }
/* 438:693 */     return y;
/* 439:    */   }
/* 440:    */   
/* 441:    */   protected static Dfp cosInternal(Dfp[] a)
/* 442:    */   {
/* 443:703 */     Dfp one = a[0].getOne();
/* 444:    */     
/* 445:    */ 
/* 446:706 */     Dfp x = one;
/* 447:707 */     Dfp y = one;
/* 448:708 */     Dfp c = a[0].add(a[1]);
/* 449:709 */     c = c.multiply(c);
/* 450:    */     
/* 451:711 */     Dfp fact = one;
/* 452:712 */     Dfp py = new Dfp(y);
/* 453:714 */     for (int i = 2; i < 90; i += 2)
/* 454:    */     {
/* 455:715 */       x = x.multiply(c);
/* 456:716 */       x = x.negate();
/* 457:    */       
/* 458:718 */       fact = fact.divide((i - 1) * i);
/* 459:    */       
/* 460:720 */       y = y.add(x.multiply(fact));
/* 461:721 */       if (y.equals(py)) {
/* 462:    */         break;
/* 463:    */       }
/* 464:724 */       py = new Dfp(y);
/* 465:    */     }
/* 466:727 */     return y;
/* 467:    */   }
/* 468:    */   
/* 469:    */   public static Dfp sin(Dfp a)
/* 470:    */   {
/* 471:736 */     Dfp pi = a.getField().getPi();
/* 472:737 */     Dfp zero = a.getField().getZero();
/* 473:738 */     boolean neg = false;
/* 474:    */     
/* 475:    */ 
/* 476:741 */     Dfp x = a.remainder(pi.multiply(2));
/* 477:745 */     if (x.lessThan(zero))
/* 478:    */     {
/* 479:746 */       x = x.negate();
/* 480:747 */       neg = true;
/* 481:    */     }
/* 482:754 */     if (x.greaterThan(pi.divide(2))) {
/* 483:755 */       x = pi.subtract(x);
/* 484:    */     }
/* 485:    */     Dfp y;
/* 486:    */     
/* 487:759 */     if (x.lessThan(pi.divide(4)))
/* 488:    */     {
/* 489:760 */       Dfp[] c = new Dfp[2];
/* 490:761 */       c[0] = x;
/* 491:762 */       c[1] = zero;
/* 492:    */       
/* 493:    */ 
/* 494:765 */       y = sinInternal(split(x));
/* 495:    */     }
/* 496:    */     else
/* 497:    */     {
/* 498:767 */       Dfp[] c = new Dfp[2];
/* 499:768 */       Dfp[] piSplit = a.getField().getPiSplit();
/* 500:769 */       c[0] = piSplit[0].divide(2).subtract(x);
/* 501:770 */       c[1] = piSplit[1].divide(2);
/* 502:771 */       y = cosInternal(c);
/* 503:    */     }
/* 504:774 */     if (neg) {
/* 505:775 */       y = y.negate();
/* 506:    */     }
/* 507:778 */     return a.newInstance(y);
/* 508:    */   }
/* 509:    */   
/* 510:    */   public static Dfp cos(Dfp a)
/* 511:    */   {
/* 512:787 */     Dfp pi = a.getField().getPi();
/* 513:788 */     Dfp zero = a.getField().getZero();
/* 514:789 */     boolean neg = false;
/* 515:    */     
/* 516:    */ 
/* 517:792 */     Dfp x = a.remainder(pi.multiply(2));
/* 518:796 */     if (x.lessThan(zero)) {
/* 519:797 */       x = x.negate();
/* 520:    */     }
/* 521:804 */     if (x.greaterThan(pi.divide(2)))
/* 522:    */     {
/* 523:805 */       x = pi.subtract(x);
/* 524:806 */       neg = true;
/* 525:    */     }
/* 526:    */     Dfp y;
/* 527:    */     
/* 528:810 */     if (x.lessThan(pi.divide(4)))
/* 529:    */     {
/* 530:811 */       Dfp[] c = new Dfp[2];
/* 531:812 */       c[0] = x;
/* 532:813 */       c[1] = zero;
/* 533:    */       
/* 534:815 */       y = cosInternal(c);
/* 535:    */     }
/* 536:    */     else
/* 537:    */     {
/* 538:817 */       Dfp[] c = new Dfp[2];
/* 539:818 */       Dfp[] piSplit = a.getField().getPiSplit();
/* 540:819 */       c[0] = piSplit[0].divide(2).subtract(x);
/* 541:820 */       c[1] = piSplit[1].divide(2);
/* 542:821 */       y = sinInternal(c);
/* 543:    */     }
/* 544:824 */     if (neg) {
/* 545:825 */       y = y.negate();
/* 546:    */     }
/* 547:828 */     return a.newInstance(y);
/* 548:    */   }
/* 549:    */   
/* 550:    */   public static Dfp tan(Dfp a)
/* 551:    */   {
/* 552:837 */     return sin(a).divide(cos(a));
/* 553:    */   }
/* 554:    */   
/* 555:    */   protected static Dfp atanInternal(Dfp a)
/* 556:    */   {
/* 557:846 */     Dfp y = new Dfp(a);
/* 558:847 */     Dfp x = new Dfp(y);
/* 559:848 */     Dfp py = new Dfp(y);
/* 560:850 */     for (int i = 3; i < 90; i += 2)
/* 561:    */     {
/* 562:851 */       x = x.multiply(a);
/* 563:852 */       x = x.multiply(a);
/* 564:853 */       x = x.negate();
/* 565:854 */       y = y.add(x.divide(i));
/* 566:855 */       if (y.equals(py)) {
/* 567:    */         break;
/* 568:    */       }
/* 569:858 */       py = new Dfp(y);
/* 570:    */     }
/* 571:861 */     return y;
/* 572:    */   }
/* 573:    */   
/* 574:    */   public static Dfp atan(Dfp a)
/* 575:    */   {
/* 576:879 */     Dfp zero = a.getField().getZero();
/* 577:880 */     Dfp one = a.getField().getOne();
/* 578:881 */     Dfp[] sqr2Split = a.getField().getSqr2Split();
/* 579:882 */     Dfp[] piSplit = a.getField().getPiSplit();
/* 580:883 */     boolean recp = false;
/* 581:884 */     boolean neg = false;
/* 582:885 */     boolean sub = false;
/* 583:    */     
/* 584:887 */     Dfp ty = sqr2Split[0].subtract(one).add(sqr2Split[1]);
/* 585:    */     
/* 586:889 */     Dfp x = new Dfp(a);
/* 587:890 */     if (x.lessThan(zero))
/* 588:    */     {
/* 589:891 */       neg = true;
/* 590:892 */       x = x.negate();
/* 591:    */     }
/* 592:895 */     if (x.greaterThan(one))
/* 593:    */     {
/* 594:896 */       recp = true;
/* 595:897 */       x = one.divide(x);
/* 596:    */     }
/* 597:900 */     if (x.greaterThan(ty))
/* 598:    */     {
/* 599:901 */       Dfp[] sty = new Dfp[2];
/* 600:902 */       sub = true;
/* 601:    */       
/* 602:904 */       sty[0] = sqr2Split[0].subtract(one);
/* 603:905 */       sty[1] = sqr2Split[1];
/* 604:    */       
/* 605:907 */       Dfp[] xs = split(x);
/* 606:    */       
/* 607:909 */       Dfp[] ds = splitMult(xs, sty);
/* 608:910 */       ds[0] = ds[0].add(one);
/* 609:    */       
/* 610:912 */       xs[0] = xs[0].subtract(sty[0]);
/* 611:913 */       xs[1] = xs[1].subtract(sty[1]);
/* 612:    */       
/* 613:915 */       xs = splitDiv(xs, ds);
/* 614:916 */       x = xs[0].add(xs[1]);
/* 615:    */     }
/* 616:921 */     Dfp y = atanInternal(x);
/* 617:923 */     if (sub) {
/* 618:924 */       y = y.add(piSplit[0].divide(8)).add(piSplit[1].divide(8));
/* 619:    */     }
/* 620:927 */     if (recp) {
/* 621:928 */       y = piSplit[0].divide(2).subtract(y).add(piSplit[1].divide(2));
/* 622:    */     }
/* 623:931 */     if (neg) {
/* 624:932 */       y = y.negate();
/* 625:    */     }
/* 626:935 */     return a.newInstance(y);
/* 627:    */   }
/* 628:    */   
/* 629:    */   public static Dfp asin(Dfp a)
/* 630:    */   {
/* 631:944 */     return atan(a.divide(a.getOne().subtract(a.multiply(a)).sqrt()));
/* 632:    */   }
/* 633:    */   
/* 634:    */   public static Dfp acos(Dfp a)
/* 635:    */   {
/* 636:953 */     boolean negative = false;
/* 637:955 */     if (a.lessThan(a.getZero())) {
/* 638:956 */       negative = true;
/* 639:    */     }
/* 640:959 */     a = Dfp.copysign(a, a.getOne());
/* 641:    */     
/* 642:961 */     Dfp result = atan(a.getOne().subtract(a.multiply(a)).sqrt().divide(a));
/* 643:963 */     if (negative) {
/* 644:964 */       result = a.getField().getPi().subtract(result);
/* 645:    */     }
/* 646:967 */     return a.newInstance(result);
/* 647:    */   }
/* 648:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.dfp.DfpMath
 * JD-Core Version:    0.7.0.1
 */
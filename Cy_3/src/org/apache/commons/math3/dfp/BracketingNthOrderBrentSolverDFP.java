/*   1:    */ package org.apache.commons.math3.dfp;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.solvers.AllowedSolution;
/*   4:    */ import org.apache.commons.math3.exception.MathInternalError;
/*   5:    */ import org.apache.commons.math3.exception.NoBracketingException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.util.Incrementor;
/*   8:    */ import org.apache.commons.math3.util.MathUtils;
/*   9:    */ 
/*  10:    */ public class BracketingNthOrderBrentSolverDFP
/*  11:    */ {
/*  12:    */   private static final int MAXIMAL_AGING = 2;
/*  13:    */   private final int maximalOrder;
/*  14:    */   private final Dfp functionValueAccuracy;
/*  15:    */   private final Dfp absoluteAccuracy;
/*  16:    */   private final Dfp relativeAccuracy;
/*  17: 61 */   private final Incrementor evaluations = new Incrementor();
/*  18:    */   
/*  19:    */   public BracketingNthOrderBrentSolverDFP(Dfp relativeAccuracy, Dfp absoluteAccuracy, Dfp functionValueAccuracy, int maximalOrder)
/*  20:    */     throws NumberIsTooSmallException
/*  21:    */   {
/*  22: 77 */     if (maximalOrder < 2) {
/*  23: 78 */       throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), Integer.valueOf(2), true);
/*  24:    */     }
/*  25: 80 */     this.maximalOrder = maximalOrder;
/*  26: 81 */     this.absoluteAccuracy = absoluteAccuracy;
/*  27: 82 */     this.relativeAccuracy = relativeAccuracy;
/*  28: 83 */     this.functionValueAccuracy = functionValueAccuracy;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getMaximalOrder()
/*  32:    */   {
/*  33: 90 */     return this.maximalOrder;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getMaxEvaluations()
/*  37:    */   {
/*  38: 99 */     return this.evaluations.getMaximalCount();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getEvaluations()
/*  42:    */   {
/*  43:111 */     return this.evaluations.getCount();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public Dfp getAbsoluteAccuracy()
/*  47:    */   {
/*  48:119 */     return this.absoluteAccuracy;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Dfp getRelativeAccuracy()
/*  52:    */   {
/*  53:127 */     return this.relativeAccuracy;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Dfp getFunctionValueAccuracy()
/*  57:    */   {
/*  58:135 */     return this.functionValueAccuracy;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Dfp solve(int maxEval, UnivariateDfpFunction f, Dfp min, Dfp max, AllowedSolution allowedSolution)
/*  62:    */   {
/*  63:158 */     return solve(maxEval, f, min, max, min.add(max).divide(2), allowedSolution);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Dfp solve(int maxEval, UnivariateDfpFunction f, Dfp min, Dfp max, Dfp startValue, AllowedSolution allowedSolution)
/*  67:    */   {
/*  68:185 */     MathUtils.checkNotNull(f);
/*  69:    */     
/*  70:    */ 
/*  71:188 */     this.evaluations.setMaximalCount(maxEval);
/*  72:189 */     this.evaluations.resetCount();
/*  73:190 */     Dfp zero = startValue.getZero();
/*  74:191 */     Dfp nan = zero.newInstance((byte)1, (byte)3);
/*  75:    */     
/*  76:    */ 
/*  77:194 */     Dfp[] x = new Dfp[this.maximalOrder + 1];
/*  78:195 */     Dfp[] y = new Dfp[this.maximalOrder + 1];
/*  79:196 */     x[0] = min;
/*  80:197 */     x[1] = startValue;
/*  81:198 */     x[2] = max;
/*  82:    */     
/*  83:    */ 
/*  84:201 */     this.evaluations.incrementCount();
/*  85:202 */     y[1] = f.value(x[1]);
/*  86:203 */     if (y[1].isZero()) {
/*  87:205 */       return x[1];
/*  88:    */     }
/*  89:209 */     this.evaluations.incrementCount();
/*  90:210 */     y[0] = f.value(x[0]);
/*  91:211 */     if (y[0].isZero()) {
/*  92:213 */       return x[0];
/*  93:    */     }
/*  94:    */     int signChangeIndex;
/*  95:218 */     if (y[0].multiply(y[1]).negativeOrNull())
/*  96:    */     {
/*  97:221 */       int nbPoints = 2;
/*  98:222 */       signChangeIndex = 1;
/*  99:    */     }
/* 100:    */     else
/* 101:    */     {
/* 102:227 */       this.evaluations.incrementCount();
/* 103:228 */       y[2] = f.value(x[2]);
/* 104:229 */       if (y[2].isZero()) {
/* 105:231 */         return x[2];
/* 106:    */       }
/* 107:    *
/* 108:234 */       if (y[1].multiply(y[2]).negativeOrNull())
/* 109:    */       {
/* 110:236 */         int nbPoints = 3;
/* 111:237 */         signChangeIndex = 2;
/* 112:    */       }
/* 113:    */       else
/* 114:    */       {
/* 115:239 */         throw new NoBracketingException(x[0].toDouble(), x[2].toDouble(), y[0].toDouble(), y[2].toDouble());
/* 116:    */       }
/* 117:    */     }
/* 118:    */   
/* 119:    */     int nbPoints = 0;
/* 120:246 */     Dfp[] tmpX = new Dfp[x.length];
/* 121:    */     
/* 122:    */ 
/* 123:249 */     Dfp xA = x[(signChangeIndex - 1)];
/* 124:250 */     Dfp yA = y[(signChangeIndex - 1)];
/* 125:251 */     Dfp absXA = xA.abs();
/* 126:252 */     Dfp absYA = yA.abs();
/* 127:253 */     int agingA = 0;
/* 128:254 */     Dfp xB = x[signChangeIndex];
/* 129:255 */     Dfp yB = y[signChangeIndex];
/* 130:256 */     Dfp absXB = xB.abs();
/* 131:257 */     Dfp absYB = yB.abs();
/* 132:258 */     int agingB = 0;
/* 133:    */     for (;;)
/* 134:    */     {
/* 135:264 */       Dfp maxX = absXA.lessThan(absXB) ? absXB : absXA;
/* 136:265 */       Dfp maxY = absYA.lessThan(absYB) ? absYB : absYA;
/* 137:266 */       Dfp xTol = this.absoluteAccuracy.add(this.relativeAccuracy.multiply(maxX));
/* 138:267 */       if ((xB.subtract(xA).subtract(xTol).negativeOrNull()) || (maxY.lessThan(this.functionValueAccuracy)))
/* 139:    */       {
/* 140:269 */         switch (allowedSolution.ordinal())
/* 141:    */         {
/* 142:    */         case 1: 
/* 143:271 */           return absYA.lessThan(absYB) ? xA : xB;
/* 144:    */         case 2: 
/* 145:273 */           return xA;
/* 146:    */         case 3: 
/* 147:275 */           return xB;
/* 148:    */         case 4: 
/* 149:277 */           return yA.lessThan(zero) ? xA : xB;
/* 150:    */         case 5: 
/* 151:279 */           return yA.lessThan(zero) ? xB : xA;
/* 152:    */         }
/* 153:282 */         throw new MathInternalError(null);
/* 154:    */       }
/* 155:    */       Dfp targetY;
/* 156:    */       
/* 157:288 */       if (agingA >= 2)
/* 158:    */       {
/* 159:290 */         targetY = yB.divide(16).negate();
/* 160:    */       }
/* 161:    */       else
/* 162:    */       {
/* 163:    */        
/* 164:291 */         if (agingB >= 2) {
/* 165:293 */           targetY = yA.divide(16).negate();
/* 166:    */         } else {
/* 167:296 */           targetY = zero;
/* 168:    */         }
/* 169:    */       }
/* 170:301 */       int start = 0;
/* 171:302 */       int end = nbPoints;
/* 172:    */       Dfp nextX;
/* 173:    */       do
/* 174:    */       {
/* 175:306 */         System.arraycopy(x, start, tmpX, start, end - start);
/* 176:307 */         nextX = guessX(targetY, tmpX, y, start, end);
/* 177:309 */         if ((!nextX.greaterThan(xA)) || (!nextX.lessThan(xB)))
/* 178:    */         {
/* 179:315 */           if (signChangeIndex - start >= end - signChangeIndex) {
/* 180:317 */             start++;
/* 181:    */           } else {
/* 182:320 */             end--;
/* 183:    */           }
/* 184:324 */           nextX = nan;
/* 185:    */         }
/* 186:328 */       } while ((nextX.isNaN()) && (end - start > 1));
/* 187:330 */       if (nextX.isNaN())
/* 188:    */       {
/* 189:332 */         nextX = xA.add(xB.subtract(xA).divide(2));
/* 190:333 */         start = signChangeIndex - 1;
/* 191:334 */         end = signChangeIndex;
/* 192:    */       }
/* 193:338 */       this.evaluations.incrementCount();
/* 194:339 */       Dfp nextY = f.value(nextX);
/* 195:340 */       if (nextY.isZero()) {
/* 196:343 */         return nextX;
/* 197:    */       }
/* 198:346 */       if ((nbPoints > 2) && (end - start != nbPoints))
/* 199:    */       {
/* 200:350 */         nbPoints = end - start;
/* 201:351 */         System.arraycopy(x, start, x, 0, nbPoints);
/* 202:352 */         System.arraycopy(y, start, y, 0, nbPoints);
/* 203:353 */         signChangeIndex -= start;
/* 204:    */       }
/* 205:355 */       else if (nbPoints == x.length)
/* 206:    */       {
/* 207:358 */         nbPoints--;
/* 208:361 */         if (signChangeIndex >= (x.length + 1) / 2)
/* 209:    */         {
/* 210:363 */           System.arraycopy(x, 1, x, 0, nbPoints);
/* 211:364 */           System.arraycopy(y, 1, y, 0, nbPoints);
/* 212:365 */           signChangeIndex--;
/* 213:    */         }
/* 214:    */       }
/* 215:372 */       System.arraycopy(x, signChangeIndex, x, signChangeIndex + 1, nbPoints - signChangeIndex);
/* 216:373 */       x[signChangeIndex] = nextX;
/* 217:374 */       System.arraycopy(y, signChangeIndex, y, signChangeIndex + 1, nbPoints - signChangeIndex);
/* 218:375 */       y[signChangeIndex] = nextY;
/* 219:376 */       nbPoints++;
/* 220:379 */       if (nextY.multiply(yA).negativeOrNull())
/* 221:    */       {
/* 222:381 */         xB = nextX;
/* 223:382 */         yB = nextY;
/* 224:383 */         absYB = yB.abs();
/* 225:384 */         agingA++;
/* 226:385 */         agingB = 0;
/* 227:    */       }
/* 228:    */       else
/* 229:    */       {
/* 230:388 */         xA = nextX;
/* 231:389 */         yA = nextY;
/* 232:390 */         absYA = yA.abs();
/* 233:391 */         agingA = 0;
/* 234:392 */         agingB++;
/* 235:    */         
/* 236:    */ 
/* 237:395 */         signChangeIndex++;
/* 238:    */       }
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   private Dfp guessX(Dfp targetY, Dfp[] x, Dfp[] y, int start, int end)
/* 243:    */   {
/* 244:421 */     for (int i = start; i < end - 1; i++)
/* 245:    */     {
/* 246:422 */       int delta = i + 1 - start;
/* 247:423 */       for (int j = end - 1; j > i; j--) {
/* 248:424 */         x[j] = x[j].subtract(x[(j - 1)]).divide(y[j].subtract(y[(j - delta)]));
/* 249:    */       }
/* 250:    */     }
/* 251:429 */     Dfp x0 = targetY.getZero();
/* 252:430 */     for (int j = end - 1; j >= start; j--) {
/* 253:431 */       x0 = x[j].add(x0.multiply(targetY.subtract(y[j])));
/* 254:    */     }
/* 255:434 */     return x0;
/* 256:    */   }
/* 257:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.dfp.BracketingNthOrderBrentSolverDFP
 * JD-Core Version:    0.7.0.1
 */
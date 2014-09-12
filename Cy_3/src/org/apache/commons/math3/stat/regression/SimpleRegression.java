/*   1:    */ package org.apache.commons.math3.stat.regression;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.distribution.TDistribution;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.NoDataException;
/*   7:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class SimpleRegression
/*  12:    */   implements Serializable, UpdatingMultipleLinearRegression
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = -3004689053607543335L;
/*  15: 70 */   private double sumX = 0.0D;
/*  16: 73 */   private double sumXX = 0.0D;
/*  17: 76 */   private double sumY = 0.0D;
/*  18: 79 */   private double sumYY = 0.0D;
/*  19: 82 */   private double sumXY = 0.0D;
/*  20: 85 */   private long n = 0L;
/*  21: 88 */   private double xbar = 0.0D;
/*  22: 91 */   private double ybar = 0.0D;
/*  23:    */   private final boolean hasIntercept;
/*  24:    */   
/*  25:    */   public SimpleRegression()
/*  26:    */   {
/*  27:101 */     this(true);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public SimpleRegression(boolean includeIntercept)
/*  31:    */   {
/*  32:116 */     this.hasIntercept = includeIntercept;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void addData(double x, double y)
/*  36:    */   {
/*  37:133 */     if (this.n == 0L)
/*  38:    */     {
/*  39:134 */       this.xbar = x;
/*  40:135 */       this.ybar = y;
/*  41:    */     }
/*  42:137 */     else if (this.hasIntercept)
/*  43:    */     {
/*  44:138 */       double fact1 = 1.0D + this.n;
/*  45:139 */       double fact2 = this.n / (1.0D + this.n);
/*  46:140 */       double dx = x - this.xbar;
/*  47:141 */       double dy = y - this.ybar;
/*  48:142 */       this.sumXX += dx * dx * fact2;
/*  49:143 */       this.sumYY += dy * dy * fact2;
/*  50:144 */       this.sumXY += dx * dy * fact2;
/*  51:145 */       this.xbar += dx / fact1;
/*  52:146 */       this.ybar += dy / fact1;
/*  53:    */     }
/*  54:149 */     if (!this.hasIntercept)
/*  55:    */     {
/*  56:150 */       this.sumXX += x * x;
/*  57:151 */       this.sumYY += y * y;
/*  58:152 */       this.sumXY += x * y;
/*  59:    */     }
/*  60:154 */     this.sumX += x;
/*  61:155 */     this.sumY += y;
/*  62:156 */     this.n += 1L;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void removeData(double x, double y)
/*  66:    */   {
/*  67:175 */     if (this.n > 0L)
/*  68:    */     {
/*  69:176 */       if (this.hasIntercept)
/*  70:    */       {
/*  71:177 */         double fact1 = this.n - 1.0D;
/*  72:178 */         double fact2 = this.n / (this.n - 1.0D);
/*  73:179 */         double dx = x - this.xbar;
/*  74:180 */         double dy = y - this.ybar;
/*  75:181 */         this.sumXX -= dx * dx * fact2;
/*  76:182 */         this.sumYY -= dy * dy * fact2;
/*  77:183 */         this.sumXY -= dx * dy * fact2;
/*  78:184 */         this.xbar -= dx / fact1;
/*  79:185 */         this.ybar -= dy / fact1;
/*  80:    */       }
/*  81:    */       else
/*  82:    */       {
/*  83:187 */         double fact1 = this.n - 1.0D;
/*  84:188 */         this.sumXX -= x * x;
/*  85:189 */         this.sumYY -= y * y;
/*  86:190 */         this.sumXY -= x * y;
/*  87:191 */         this.xbar -= x / fact1;
/*  88:192 */         this.ybar -= y / fact1;
/*  89:    */       }
/*  90:194 */       this.sumX -= x;
/*  91:195 */       this.sumY -= y;
/*  92:196 */       this.n -= 1L;
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void addData(double[][] data)
/*  97:    */   {
/*  98:220 */     for (int i = 0; i < data.length; i++)
/*  99:    */     {
/* 100:221 */       if (data[i].length < 2) {
/* 101:222 */         throw new ModelSpecificationException(LocalizedFormats.INVALID_REGRESSION_OBSERVATION, new Object[] { Integer.valueOf(data[i].length), Integer.valueOf(2) });
/* 102:    */       }
/* 103:225 */       addData(data[i][0], data[i][1]);
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void addObservation(double[] x, double y)
/* 108:    */     throws ModelSpecificationException
/* 109:    */   {
/* 110:239 */     if ((x == null) || (x.length == 0)) {
/* 111:240 */       throw new ModelSpecificationException(LocalizedFormats.INVALID_REGRESSION_OBSERVATION, new Object[] { Integer.valueOf(x != null ? x.length : 0), Integer.valueOf(1) });
/* 112:    */     }
/* 113:242 */     addData(x[0], y);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void addObservations(double[][] x, double[] y)
/* 117:    */   {
/* 118:257 */     if ((x == null) || (y == null) || (x.length != y.length)) {
/* 119:258 */       throw new ModelSpecificationException(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, new Object[] { Integer.valueOf(x == null ? 0 : x.length), Integer.valueOf(y == null ? 0 : y.length) });
/* 120:    */     }
/* 121:263 */     boolean obsOk = true;
/* 122:264 */     for (int i = 0; i < x.length; i++) {
/* 123:265 */       if ((x[i] == null) || (x[i].length == 0)) {
/* 124:266 */         obsOk = false;
/* 125:    */       }
/* 126:    */     }
/* 127:269 */     if (!obsOk) {
/* 128:270 */       throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, new Object[] { Integer.valueOf(0), Integer.valueOf(1) });
/* 129:    */     }
/* 130:274 */     for (int i = 0; i < x.length; i++) {
/* 131:275 */       addData(x[i][0], y[i]);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void removeData(double[][] data)
/* 136:    */   {
/* 137:294 */     for (int i = 0; (i < data.length) && (this.n > 0L); i++) {
/* 138:295 */       removeData(data[i][0], data[i][1]);
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void clear()
/* 143:    */   {
/* 144:303 */     this.sumX = 0.0D;
/* 145:304 */     this.sumXX = 0.0D;
/* 146:305 */     this.sumY = 0.0D;
/* 147:306 */     this.sumYY = 0.0D;
/* 148:307 */     this.sumXY = 0.0D;
/* 149:308 */     this.n = 0L;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public long getN()
/* 153:    */   {
/* 154:317 */     return this.n;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public double predict(double x)
/* 158:    */   {
/* 159:338 */     double b1 = getSlope();
/* 160:339 */     if (this.hasIntercept) {
/* 161:340 */       return getIntercept(b1) + b1 * x;
/* 162:    */     }
/* 163:342 */     return b1 * x;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public double getIntercept()
/* 167:    */   {
/* 168:365 */     return this.hasIntercept ? getIntercept(getSlope()) : 0.0D;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public boolean hasIntercept()
/* 172:    */   {
/* 173:375 */     return this.hasIntercept;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public double getSlope()
/* 177:    */   {
/* 178:395 */     if (this.n < 2L) {
/* 179:396 */       return (0.0D / 0.0D);
/* 180:    */     }
/* 181:398 */     if (FastMath.abs(this.sumXX) < 4.940656458412465E-323D) {
/* 182:399 */       return (0.0D / 0.0D);
/* 183:    */     }
/* 184:401 */     return this.sumXY / this.sumXX;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public double getSumSquaredErrors()
/* 188:    */   {
/* 189:434 */     return FastMath.max(0.0D, this.sumYY - this.sumXY * this.sumXY / this.sumXX);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public double getTotalSumSquares()
/* 193:    */   {
/* 194:448 */     if (this.n < 2L) {
/* 195:449 */       return (0.0D / 0.0D);
/* 196:    */     }
/* 197:451 */     return this.sumYY;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public double getXSumSquares()
/* 201:    */   {
/* 202:462 */     if (this.n < 2L) {
/* 203:463 */       return (0.0D / 0.0D);
/* 204:    */     }
/* 205:465 */     return this.sumXX;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public double getSumOfCrossProducts()
/* 209:    */   {
/* 210:474 */     return this.sumXY;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public double getRegressionSumSquares()
/* 214:    */   {
/* 215:494 */     return getRegressionSumSquares(getSlope());
/* 216:    */   }
/* 217:    */   
/* 218:    */   public double getMeanSquareError()
/* 219:    */   {
/* 220:508 */     if (this.n < 3L) {
/* 221:509 */       return (0.0D / 0.0D);
/* 222:    */     }
/* 223:511 */     return this.hasIntercept ? getSumSquaredErrors() / (this.n - 2L) : getSumSquaredErrors() / (this.n - 1L);
/* 224:    */   }
/* 225:    */   
/* 226:    */   public double getR()
/* 227:    */   {
/* 228:529 */     double b1 = getSlope();
/* 229:530 */     double result = FastMath.sqrt(getRSquare());
/* 230:531 */     if (b1 < 0.0D) {
/* 231:532 */       result = -result;
/* 232:    */     }
/* 233:534 */     return result;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public double getRSquare()
/* 237:    */   {
/* 238:552 */     double ssto = getTotalSumSquares();
/* 239:553 */     return (ssto - getSumSquaredErrors()) / ssto;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public double getInterceptStdErr()
/* 243:    */   {
/* 244:569 */     if (!this.hasIntercept) {
/* 245:570 */       return (0.0D / 0.0D);
/* 246:    */     }
/* 247:572 */     return FastMath.sqrt(getMeanSquareError() * (1.0D / this.n + this.xbar * this.xbar / this.sumXX));
/* 248:    */   }
/* 249:    */   
/* 250:    */   public double getSlopeStdErr()
/* 251:    */   {
/* 252:588 */     return FastMath.sqrt(getMeanSquareError() / this.sumXX);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public double getSlopeConfidenceInterval()
/* 256:    */   {
/* 257:614 */     return getSlopeConfidenceInterval(0.05D);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public double getSlopeConfidenceInterval(double alpha)
/* 261:    */   {
/* 262:649 */     if ((alpha >= 1.0D) || (alpha <= 0.0D)) {
/* 263:650 */       throw new OutOfRangeException(LocalizedFormats.SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Integer.valueOf(1));
/* 264:    */     }
/* 265:653 */     TDistribution distribution = new TDistribution(this.n - 2L);
/* 266:654 */     return getSlopeStdErr() * distribution.inverseCumulativeProbability(1.0D - alpha / 2.0D);
/* 267:    */   }
/* 268:    */   
/* 269:    */   public double getSignificance()
/* 270:    */   {
/* 271:681 */     TDistribution distribution = new TDistribution(this.n - 2L);
/* 272:682 */     return 2.0D * (1.0D - distribution.cumulativeProbability(FastMath.abs(getSlope()) / getSlopeStdErr()));
/* 273:    */   }
/* 274:    */   
/* 275:    */   private double getIntercept(double slope)
/* 276:    */   {
/* 277:697 */     if (this.hasIntercept) {
/* 278:698 */       return (this.sumY - slope * this.sumX) / this.n;
/* 279:    */     }
/* 280:700 */     return 0.0D;
/* 281:    */   }
/* 282:    */   
/* 283:    */   private double getRegressionSumSquares(double slope)
/* 284:    */   {
/* 285:710 */     return slope * slope * this.sumXX;
/* 286:    */   }
/* 287:    */   
/* 288:    */   public RegressionResults regress()
/* 289:    */     throws ModelSpecificationException
/* 290:    */   {
/* 291:719 */     if (this.hasIntercept)
/* 292:    */     {
/* 293:720 */       if (this.n < 3L) {
/* 294:721 */         throw new NoDataException(LocalizedFormats.NOT_ENOUGH_DATA_REGRESSION);
/* 295:    */       }
/* 296:723 */       if (FastMath.abs(this.sumXX) > 2.225073858507201E-308D)
/* 297:    */       {
/* 298:724 */         double[] params = { getIntercept(), getSlope() };
/* 299:725 */         double mse = getMeanSquareError();
/* 300:726 */         double _syy = this.sumYY + this.sumY * this.sumY / this.n;
/* 301:727 */         double[] vcv = { mse * (this.xbar * this.xbar / this.sumXX + 1.0D / this.n), -this.xbar * mse / this.sumXX, mse / this.sumXX };
/* 302:    */         
/* 303:    */ 
/* 304:    */ 
/* 305:731 */         return new RegressionResults(params, new double[][] { vcv }, true, this.n, 2, this.sumY, _syy, getSumSquaredErrors(), true, false);
/* 306:    */       }
/* 307:735 */       double[] params = { this.sumY / this.n, (0.0D / 0.0D) };
/* 308:    */       
/* 309:737 */       double[] vcv = { this.ybar / (this.n - 1.0D), (0.0D / 0.0D), (0.0D / 0.0D) };
/* 310:    */       
/* 311:    */ 
/* 312:    */ 
/* 313:741 */       return new RegressionResults(params, new double[][] { vcv }, true, this.n, 1, this.sumY, this.sumYY, getSumSquaredErrors(), true, false);
/* 314:    */     }
/* 315:746 */     if (this.n < 2L) {
/* 316:747 */       throw new NoDataException(LocalizedFormats.NOT_ENOUGH_DATA_REGRESSION);
/* 317:    */     }
/* 318:749 */     if (!Double.isNaN(this.sumXX))
/* 319:    */     {
/* 320:750 */       double[] vcv = { getMeanSquareError() / this.sumXX };
/* 321:751 */       double[] params = { this.sumXY / this.sumXX };
/* 322:752 */       return new RegressionResults(params, new double[][] { vcv }, true, this.n, 1, this.sumY, this.sumYY, getSumSquaredErrors(), false, false);
/* 323:    */     }
/* 324:756 */     double[] vcv = { (0.0D / 0.0D) };
/* 325:757 */     double[] params = { (0.0D / 0.0D) };
/* 326:758 */     return new RegressionResults(params, new double[][] { vcv }, true, this.n, 1, (0.0D / 0.0D), (0.0D / 0.0D), (0.0D / 0.0D), false, false);
/* 327:    */   }
/* 328:    */   
/* 329:    */   public RegressionResults regress(int[] variablesToInclude)
/* 330:    */     throws ModelSpecificationException
/* 331:    */   {
/* 332:775 */     if ((variablesToInclude == null) || (variablesToInclude.length == 0)) {
/* 333:776 */       throw new MathIllegalArgumentException(LocalizedFormats.ARRAY_ZERO_LENGTH_OR_NULL_NOT_ALLOWED, new Object[0]);
/* 334:    */     }
/* 335:778 */     if ((variablesToInclude.length > 2) || ((variablesToInclude.length > 1) && (!this.hasIntercept))) {
/* 336:779 */       throw new ModelSpecificationException(LocalizedFormats.ARRAY_SIZE_EXCEEDS_MAX_VARIABLES, new Object[] { Integer.valueOf((variablesToInclude.length > 1) && (!this.hasIntercept) ? 1 : 2) });
/* 337:    */     }
/* 338:784 */     if (this.hasIntercept)
/* 339:    */     {
/* 340:785 */       if (variablesToInclude.length == 2)
/* 341:    */       {
/* 342:786 */         if (variablesToInclude[0] == 1) {
/* 343:787 */           throw new ModelSpecificationException(LocalizedFormats.NOT_INCREASING_SEQUENCE, new Object[0]);
/* 344:    */         }
/* 345:788 */         if (variablesToInclude[0] != 0) {
/* 346:789 */           throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), Integer.valueOf(0), Integer.valueOf(1));
/* 347:    */         }
/* 348:791 */         if (variablesToInclude[1] != 1) {
/* 349:792 */           throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), Integer.valueOf(0), Integer.valueOf(1));
/* 350:    */         }
/* 351:794 */         return regress();
/* 352:    */       }
/* 353:796 */       if ((variablesToInclude[0] != 1) && (variablesToInclude[0] != 0)) {
/* 354:797 */         throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), Integer.valueOf(0), Integer.valueOf(1));
/* 355:    */       }
/* 356:799 */       double _mean = this.sumY * this.sumY / this.n;
/* 357:800 */       double _syy = this.sumYY + _mean;
/* 358:801 */       if (variablesToInclude[0] == 0)
/* 359:    */       {
/* 360:803 */         double[] vcv = { this.sumYY / ((this.n - 1L) * this.n) };
/* 361:804 */         double[] params = { this.ybar };
/* 362:805 */         return new RegressionResults(params, new double[][] { vcv }, true, this.n, 1, this.sumY, _syy + _mean, this.sumYY, true, false);
/* 363:    */       }
/* 364:809 */       if (variablesToInclude[0] == 1)
/* 365:    */       {
/* 366:811 */         double _sxx = this.sumXX + this.sumX * this.sumX / this.n;
/* 367:812 */         double _sxy = this.sumXY + this.sumX * this.sumY / this.n;
/* 368:813 */         double _sse = FastMath.max(0.0D, _syy - _sxy * _sxy / _sxx);
/* 369:814 */         double _mse = _sse / (this.n - 1L);
/* 370:815 */         if (!Double.isNaN(_sxx))
/* 371:    */         {
/* 372:816 */           double[] vcv = { _mse / _sxx };
/* 373:817 */           double[] params = { _sxy / _sxx };
/* 374:818 */           return new RegressionResults(params, new double[][] { vcv }, true, this.n, 1, this.sumY, _syy, _sse, false, false);
/* 375:    */         }
/* 376:822 */         double[] vcv = { (0.0D / 0.0D) };
/* 377:823 */         double[] params = { (0.0D / 0.0D) };
/* 378:824 */         return new RegressionResults(params, new double[][] { vcv }, true, this.n, 1, (0.0D / 0.0D), (0.0D / 0.0D), (0.0D / 0.0D), false, false);
/* 379:    */       }
/* 380:    */     }
/* 381:    */     else
/* 382:    */     {
/* 383:831 */       if (variablesToInclude[0] != 0) {
/* 384:832 */         throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), Integer.valueOf(0), Integer.valueOf(0));
/* 385:    */       }
/* 386:834 */       return regress();
/* 387:    */     }
/* 388:837 */     return null;
/* 389:    */   }
/* 390:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.SimpleRegression
 * JD-Core Version:    0.7.0.1
 */
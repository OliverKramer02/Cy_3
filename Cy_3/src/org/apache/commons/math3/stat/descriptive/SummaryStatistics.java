/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
/*   8:    */ import org.apache.commons.math3.stat.descriptive.moment.Mean;
/*   9:    */ import org.apache.commons.math3.stat.descriptive.moment.SecondMoment;
/*  10:    */ import org.apache.commons.math3.stat.descriptive.moment.Variance;
/*  11:    */ import org.apache.commons.math3.stat.descriptive.rank.Max;
/*  12:    */ import org.apache.commons.math3.stat.descriptive.rank.Min;
/*  13:    */ import org.apache.commons.math3.stat.descriptive.summary.Sum;
/*  14:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
/*  15:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
/*  16:    */ import org.apache.commons.math3.util.FastMath;
/*  17:    */ import org.apache.commons.math3.util.MathUtils;
/*  18:    */ import org.apache.commons.math3.util.Precision;
/*  19:    */ 
/*  20:    */ public class SummaryStatistics
/*  21:    */   implements StatisticalSummary, Serializable
/*  22:    */ {
/*  23:    */   private static final long serialVersionUID = -2021321786743555871L;
/*  24: 67 */   private long n = 0L;
/*  25: 70 */   private SecondMoment secondMoment = new SecondMoment();
/*  26: 73 */   private Sum sum = new Sum();
/*  27: 76 */   private SumOfSquares sumsq = new SumOfSquares();
/*  28: 79 */   private Min min = new Min();
/*  29: 82 */   private Max max = new Max();
/*  30: 85 */   private SumOfLogs sumLog = new SumOfLogs();
/*  31: 88 */   private GeometricMean geoMean = new GeometricMean(this.sumLog);
/*  32: 91 */   private Mean mean = new Mean(this.secondMoment);
/*  33: 94 */   private Variance variance = new Variance(this.secondMoment);
/*  34: 97 */   private StorelessUnivariateStatistic sumImpl = this.sum;
/*  35:100 */   private StorelessUnivariateStatistic sumsqImpl = this.sumsq;
/*  36:103 */   private StorelessUnivariateStatistic minImpl = this.min;
/*  37:106 */   private StorelessUnivariateStatistic maxImpl = this.max;
/*  38:109 */   private StorelessUnivariateStatistic sumLogImpl = this.sumLog;
/*  39:112 */   private StorelessUnivariateStatistic geoMeanImpl = this.geoMean;
/*  40:115 */   private StorelessUnivariateStatistic meanImpl = this.mean;
/*  41:118 */   private StorelessUnivariateStatistic varianceImpl = this.variance;
/*  42:    */   
/*  43:    */   public SummaryStatistics() {}
/*  44:    */   
/*  45:    */   public SummaryStatistics(SummaryStatistics original)
/*  46:    */   {
/*  47:132 */     copy(original, this);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public StatisticalSummary getSummary()
/*  51:    */   {
/*  52:141 */     return new StatisticalSummaryValues(getMean(), getVariance(), getN(), getMax(), getMin(), getSum());
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void addValue(double value)
/*  56:    */   {
/*  57:150 */     this.sumImpl.increment(value);
/*  58:151 */     this.sumsqImpl.increment(value);
/*  59:152 */     this.minImpl.increment(value);
/*  60:153 */     this.maxImpl.increment(value);
/*  61:154 */     this.sumLogImpl.increment(value);
/*  62:155 */     this.secondMoment.increment(value);
/*  63:158 */     if (this.meanImpl != this.mean) {
/*  64:159 */       this.meanImpl.increment(value);
/*  65:    */     }
/*  66:161 */     if (this.varianceImpl != this.variance) {
/*  67:162 */       this.varianceImpl.increment(value);
/*  68:    */     }
/*  69:164 */     if (this.geoMeanImpl != this.geoMean) {
/*  70:165 */       this.geoMeanImpl.increment(value);
/*  71:    */     }
/*  72:167 */     this.n += 1L;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public long getN()
/*  76:    */   {
/*  77:175 */     return this.n;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public double getSum()
/*  81:    */   {
/*  82:183 */     return this.sumImpl.getResult();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public double getSumsq()
/*  86:    */   {
/*  87:194 */     return this.sumsqImpl.getResult();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public double getMean()
/*  91:    */   {
/*  92:205 */     return this.meanImpl.getResult();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public double getStandardDeviation()
/*  96:    */   {
/*  97:216 */     double stdDev = (0.0D / 0.0D);
/*  98:217 */     if (getN() > 0L) {
/*  99:218 */       if (getN() > 1L) {
/* 100:219 */         stdDev = FastMath.sqrt(getVariance());
/* 101:    */       } else {
/* 102:221 */         stdDev = 0.0D;
/* 103:    */       }
/* 104:    */     }
/* 105:224 */     return stdDev;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public double getVariance()
/* 109:    */   {
/* 110:239 */     return this.varianceImpl.getResult();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public double getPopulationVariance()
/* 114:    */   {
/* 115:251 */     Variance populationVariance = new Variance(this.secondMoment);
/* 116:252 */     populationVariance.setBiasCorrected(false);
/* 117:253 */     return populationVariance.getResult();
/* 118:    */   }
/* 119:    */   
/* 120:    */   public double getMax()
/* 121:    */   {
/* 122:264 */     return this.maxImpl.getResult();
/* 123:    */   }
/* 124:    */   
/* 125:    */   public double getMin()
/* 126:    */   {
/* 127:275 */     return this.minImpl.getResult();
/* 128:    */   }
/* 129:    */   
/* 130:    */   public double getGeometricMean()
/* 131:    */   {
/* 132:286 */     return this.geoMeanImpl.getResult();
/* 133:    */   }
/* 134:    */   
/* 135:    */   public double getSumOfLogs()
/* 136:    */   {
/* 137:298 */     return this.sumLogImpl.getResult();
/* 138:    */   }
/* 139:    */   
/* 140:    */   public double getSecondMoment()
/* 141:    */   {
/* 142:313 */     return this.secondMoment.getResult();
/* 143:    */   }
/* 144:    */   
/* 145:    */   public String toString()
/* 146:    */   {
/* 147:324 */     StringBuilder outBuffer = new StringBuilder();
/* 148:325 */     String endl = "\n";
/* 149:326 */     outBuffer.append("SummaryStatistics:").append(endl);
/* 150:327 */     outBuffer.append("n: ").append(getN()).append(endl);
/* 151:328 */     outBuffer.append("min: ").append(getMin()).append(endl);
/* 152:329 */     outBuffer.append("max: ").append(getMax()).append(endl);
/* 153:330 */     outBuffer.append("mean: ").append(getMean()).append(endl);
/* 154:331 */     outBuffer.append("geometric mean: ").append(getGeometricMean()).append(endl);
/* 155:    */     
/* 156:333 */     outBuffer.append("variance: ").append(getVariance()).append(endl);
/* 157:334 */     outBuffer.append("sum of squares: ").append(getSumsq()).append(endl);
/* 158:335 */     outBuffer.append("standard deviation: ").append(getStandardDeviation()).append(endl);
/* 159:    */     
/* 160:337 */     outBuffer.append("sum of logs: ").append(getSumOfLogs()).append(endl);
/* 161:338 */     return outBuffer.toString();
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void clear()
/* 165:    */   {
/* 166:345 */     this.n = 0L;
/* 167:346 */     this.minImpl.clear();
/* 168:347 */     this.maxImpl.clear();
/* 169:348 */     this.sumImpl.clear();
/* 170:349 */     this.sumLogImpl.clear();
/* 171:350 */     this.sumsqImpl.clear();
/* 172:351 */     this.geoMeanImpl.clear();
/* 173:352 */     this.secondMoment.clear();
/* 174:353 */     if (this.meanImpl != this.mean) {
/* 175:354 */       this.meanImpl.clear();
/* 176:    */     }
/* 177:356 */     if (this.varianceImpl != this.variance) {
/* 178:357 */       this.varianceImpl.clear();
/* 179:    */     }
/* 180:    */   }
/* 181:    */   
/* 182:    */   public boolean equals(Object object)
/* 183:    */   {
/* 184:370 */     if (object == this) {
/* 185:371 */       return true;
/* 186:    */     }
/* 187:373 */     if (!(object instanceof SummaryStatistics)) {
/* 188:374 */       return false;
/* 189:    */     }
/* 190:376 */     SummaryStatistics stat = (SummaryStatistics)object;
/* 191:377 */     return (Precision.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean())) && (Precision.equalsIncludingNaN(stat.getMax(), getMax())) && (Precision.equalsIncludingNaN(stat.getMean(), getMean())) && (Precision.equalsIncludingNaN(stat.getMin(), getMin())) && (Precision.equalsIncludingNaN((float)stat.getN(), (float)getN())) && (Precision.equalsIncludingNaN(stat.getSum(), getSum())) && (Precision.equalsIncludingNaN(stat.getSumsq(), getSumsq())) && (Precision.equalsIncludingNaN(stat.getVariance(), getVariance()));
/* 192:    */   }
/* 193:    */   
/* 194:    */   public int hashCode()
/* 195:    */   {
/* 196:393 */     int result = 31 + MathUtils.hash(getGeometricMean());
/* 197:394 */     result = result * 31 + MathUtils.hash(getGeometricMean());
/* 198:395 */     result = result * 31 + MathUtils.hash(getMax());
/* 199:396 */     result = result * 31 + MathUtils.hash(getMean());
/* 200:397 */     result = result * 31 + MathUtils.hash(getMin());
/* 201:398 */     result = result * 31 + MathUtils.hash(getN());
/* 202:399 */     result = result * 31 + MathUtils.hash(getSum());
/* 203:400 */     result = result * 31 + MathUtils.hash(getSumsq());
/* 204:401 */     result = result * 31 + MathUtils.hash(getVariance());
/* 205:402 */     return result;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public StorelessUnivariateStatistic getSumImpl()
/* 209:    */   {
/* 210:412 */     return this.sumImpl;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setSumImpl(StorelessUnivariateStatistic sumImpl)
/* 214:    */   {
/* 215:431 */     checkEmpty();
/* 216:432 */     this.sumImpl = sumImpl;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public StorelessUnivariateStatistic getSumsqImpl()
/* 220:    */   {
/* 221:441 */     return this.sumsqImpl;
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void setSumsqImpl(StorelessUnivariateStatistic sumsqImpl)
/* 225:    */   {
/* 226:460 */     checkEmpty();
/* 227:461 */     this.sumsqImpl = sumsqImpl;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public StorelessUnivariateStatistic getMinImpl()
/* 231:    */   {
/* 232:470 */     return this.minImpl;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public void setMinImpl(StorelessUnivariateStatistic minImpl)
/* 236:    */   {
/* 237:489 */     checkEmpty();
/* 238:490 */     this.minImpl = minImpl;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public StorelessUnivariateStatistic getMaxImpl()
/* 242:    */   {
/* 243:499 */     return this.maxImpl;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setMaxImpl(StorelessUnivariateStatistic maxImpl)
/* 247:    */   {
/* 248:518 */     checkEmpty();
/* 249:519 */     this.maxImpl = maxImpl;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public StorelessUnivariateStatistic getSumLogImpl()
/* 253:    */   {
/* 254:528 */     return this.sumLogImpl;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public void setSumLogImpl(StorelessUnivariateStatistic sumLogImpl)
/* 258:    */   {
/* 259:547 */     checkEmpty();
/* 260:548 */     this.sumLogImpl = sumLogImpl;
/* 261:549 */     this.geoMean.setSumLogImpl(sumLogImpl);
/* 262:    */   }
/* 263:    */   
/* 264:    */   public StorelessUnivariateStatistic getGeoMeanImpl()
/* 265:    */   {
/* 266:558 */     return this.geoMeanImpl;
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void setGeoMeanImpl(StorelessUnivariateStatistic geoMeanImpl)
/* 270:    */   {
/* 271:577 */     checkEmpty();
/* 272:578 */     this.geoMeanImpl = geoMeanImpl;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public StorelessUnivariateStatistic getMeanImpl()
/* 276:    */   {
/* 277:587 */     return this.meanImpl;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void setMeanImpl(StorelessUnivariateStatistic meanImpl)
/* 281:    */   {
/* 282:606 */     checkEmpty();
/* 283:607 */     this.meanImpl = meanImpl;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public StorelessUnivariateStatistic getVarianceImpl()
/* 287:    */   {
/* 288:616 */     return this.varianceImpl;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public void setVarianceImpl(StorelessUnivariateStatistic varianceImpl)
/* 292:    */   {
/* 293:635 */     checkEmpty();
/* 294:636 */     this.varianceImpl = varianceImpl;
/* 295:    */   }
/* 296:    */   
/* 297:    */   private void checkEmpty()
/* 298:    */   {
/* 299:643 */     if (this.n > 0L) {
/* 300:644 */       throw new MathIllegalStateException(LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, new Object[] { Long.valueOf(this.n) });
/* 301:    */     }
/* 302:    */   }
/* 303:    */   
/* 304:    */   public SummaryStatistics copy()
/* 305:    */   {
/* 306:655 */     SummaryStatistics result = new SummaryStatistics();
/* 307:656 */     copy(this, result);
/* 308:657 */     return result;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public static void copy(SummaryStatistics source, SummaryStatistics dest)
/* 312:    */     throws NullArgumentException
/* 313:    */   {
/* 314:670 */     MathUtils.checkNotNull(source);
/* 315:671 */     MathUtils.checkNotNull(dest);
/* 316:672 */     dest.maxImpl = source.maxImpl.copy();
/* 317:673 */     dest.minImpl = source.minImpl.copy();
/* 318:674 */     dest.sumImpl = source.sumImpl.copy();
/* 319:675 */     dest.sumLogImpl = source.sumLogImpl.copy();
/* 320:676 */     dest.sumsqImpl = source.sumsqImpl.copy();
/* 321:677 */     dest.secondMoment = source.secondMoment.copy();
/* 322:678 */     dest.n = source.n;
/* 323:681 */     if ((source.getVarianceImpl() instanceof Variance)) {
/* 324:682 */       dest.varianceImpl = new Variance(dest.secondMoment);
/* 325:    */     } else {
/* 326:684 */       dest.varianceImpl = source.varianceImpl.copy();
/* 327:    */     }
/* 328:686 */     if ((source.meanImpl instanceof Mean)) {
/* 329:687 */       dest.meanImpl = new Mean(dest.secondMoment);
/* 330:    */     } else {
/* 331:689 */       dest.meanImpl = source.meanImpl.copy();
/* 332:    */     }
/* 333:691 */     if ((source.getGeoMeanImpl() instanceof GeometricMean)) {
/* 334:692 */       dest.geoMeanImpl = new GeometricMean((SumOfLogs)dest.sumLogImpl);
/* 335:    */     } else {
/* 336:694 */       dest.geoMeanImpl = source.geoMeanImpl.copy();
/* 337:    */     }
/* 338:699 */     if (source.geoMean == source.geoMeanImpl) {
/* 339:700 */       dest.geoMean = ((GeometricMean)dest.geoMeanImpl);
/* 340:    */     } else {
/* 341:702 */       GeometricMean.copy(source.geoMean, dest.geoMean);
/* 342:    */     }
/* 343:704 */     if (source.max == source.maxImpl) {
/* 344:705 */       dest.max = ((Max)dest.maxImpl);
/* 345:    */     } else {
/* 346:707 */       Max.copy(source.max, dest.max);
/* 347:    */     }
/* 348:709 */     if (source.mean == source.meanImpl) {
/* 349:710 */       dest.mean = ((Mean)dest.meanImpl);
/* 350:    */     } else {
/* 351:712 */       Mean.copy(source.mean, dest.mean);
/* 352:    */     }
/* 353:714 */     if (source.min == source.minImpl) {
/* 354:715 */       dest.min = ((Min)dest.minImpl);
/* 355:    */     } else {
/* 356:717 */       Min.copy(source.min, dest.min);
/* 357:    */     }
/* 358:719 */     if (source.sum == source.sumImpl) {
/* 359:720 */       dest.sum = ((Sum)dest.sumImpl);
/* 360:    */     } else {
/* 361:722 */       Sum.copy(source.sum, dest.sum);
/* 362:    */     }
/* 363:724 */     if (source.variance == source.varianceImpl) {
/* 364:725 */       dest.variance = ((Variance)dest.varianceImpl);
/* 365:    */     } else {
/* 366:727 */       Variance.copy(source.variance, dest.variance);
/* 367:    */     }
/* 368:729 */     if (source.sumLog == source.sumLogImpl) {
/* 369:730 */       dest.sumLog = ((SumOfLogs)dest.sumLogImpl);
/* 370:    */     } else {
/* 371:732 */       SumOfLogs.copy(source.sumLog, dest.sumLog);
/* 372:    */     }
/* 373:734 */     if (source.sumsq == source.sumsqImpl) {
/* 374:735 */       dest.sumsq = ((SumOfSquares)dest.sumsqImpl);
/* 375:    */     } else {
/* 376:737 */       SumOfSquares.copy(source.sumsq, dest.sumsq);
/* 377:    */     }
/* 378:    */   }
/* 379:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.SummaryStatistics
 * JD-Core Version:    0.7.0.1
 */
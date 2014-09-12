/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   9:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  11:    */ import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
/*  12:    */ import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
/*  13:    */ import org.apache.commons.math3.stat.descriptive.moment.Mean;
/*  14:    */ import org.apache.commons.math3.stat.descriptive.moment.Skewness;
/*  15:    */ import org.apache.commons.math3.stat.descriptive.moment.Variance;
/*  16:    */ import org.apache.commons.math3.stat.descriptive.rank.Max;
/*  17:    */ import org.apache.commons.math3.stat.descriptive.rank.Min;
/*  18:    */ import org.apache.commons.math3.stat.descriptive.rank.Percentile;
/*  19:    */ import org.apache.commons.math3.stat.descriptive.summary.Sum;
/*  20:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
/*  21:    */ import org.apache.commons.math3.util.FastMath;
/*  22:    */ import org.apache.commons.math3.util.MathUtils;
/*  23:    */ import org.apache.commons.math3.util.ResizableDoubleArray;
/*  24:    */ 
/*  25:    */ public class DescriptiveStatistics
/*  26:    */   implements StatisticalSummary, Serializable
/*  27:    */ {
/*  28:    */   public static final int INFINITE_WINDOW = -1;
/*  29:    */   private static final long serialVersionUID = 4133067267405273064L;
/*  30:    */   private static final String SET_QUANTILE_METHOD_NAME = "setQuantile";
/*  31: 77 */   protected int windowSize = -1;
/*  32: 82 */   private ResizableDoubleArray eDA = new ResizableDoubleArray();
/*  33: 85 */   private UnivariateStatistic meanImpl = new Mean();
/*  34: 88 */   private UnivariateStatistic geometricMeanImpl = new GeometricMean();
/*  35: 91 */   private UnivariateStatistic kurtosisImpl = new Kurtosis();
/*  36: 94 */   private UnivariateStatistic maxImpl = new Max();
/*  37: 97 */   private UnivariateStatistic minImpl = new Min();
/*  38:100 */   private UnivariateStatistic percentileImpl = new Percentile();
/*  39:103 */   private UnivariateStatistic skewnessImpl = new Skewness();
/*  40:106 */   private UnivariateStatistic varianceImpl = new Variance();
/*  41:109 */   private UnivariateStatistic sumsqImpl = new SumOfSquares();
/*  42:112 */   private UnivariateStatistic sumImpl = new Sum();
/*  43:    */   
/*  44:    */   public DescriptiveStatistics() {}
/*  45:    */   
/*  46:    */   public DescriptiveStatistics(int window)
/*  47:    */   {
/*  48:126 */     setWindowSize(window);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public DescriptiveStatistics(double[] initialDoubleArray)
/*  52:    */   {
/*  53:138 */     if (initialDoubleArray != null) {
/*  54:139 */       this.eDA = new ResizableDoubleArray(initialDoubleArray);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public DescriptiveStatistics(DescriptiveStatistics original)
/*  59:    */   {
/*  60:150 */     copy(original, this);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void addValue(double v)
/*  64:    */   {
/*  65:162 */     if (this.windowSize != -1)
/*  66:    */     {
/*  67:163 */       if (getN() == this.windowSize) {
/*  68:164 */         this.eDA.addElementRolling(v);
/*  69:165 */       } else if (getN() < this.windowSize) {
/*  70:166 */         this.eDA.addElement(v);
/*  71:    */       }
/*  72:    */     }
/*  73:    */     else {
/*  74:169 */       this.eDA.addElement(v);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void removeMostRecentValue()
/*  79:    */   {
/*  80:177 */     this.eDA.discardMostRecentElements(1);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public double replaceMostRecentValue(double v)
/*  84:    */   {
/*  85:188 */     return this.eDA.substituteMostRecentElement(v);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public double getMean()
/*  89:    */   {
/*  90:197 */     return apply(this.meanImpl);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public double getGeometricMean()
/*  94:    */   {
/*  95:207 */     return apply(this.geometricMeanImpl);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public double getVariance()
/*  99:    */   {
/* 100:221 */     return apply(this.varianceImpl);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double getPopulationVariance()
/* 104:    */   {
/* 105:232 */     return apply(new Variance(false));
/* 106:    */   }
/* 107:    */   
/* 108:    */   public double getStandardDeviation()
/* 109:    */   {
/* 110:241 */     double stdDev = (0.0D / 0.0D);
/* 111:242 */     if (getN() > 0L) {
/* 112:243 */       if (getN() > 1L) {
/* 113:244 */         stdDev = FastMath.sqrt(getVariance());
/* 114:    */       } else {
/* 115:246 */         stdDev = 0.0D;
/* 116:    */       }
/* 117:    */     }
/* 118:249 */     return stdDev;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public double getSkewness()
/* 122:    */   {
/* 123:259 */     return apply(this.skewnessImpl);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public double getKurtosis()
/* 127:    */   {
/* 128:269 */     return apply(this.kurtosisImpl);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public double getMax()
/* 132:    */   {
/* 133:277 */     return apply(this.maxImpl);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public double getMin()
/* 137:    */   {
/* 138:285 */     return apply(this.minImpl);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public long getN()
/* 142:    */   {
/* 143:293 */     return this.eDA.getNumElements();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public double getSum()
/* 147:    */   {
/* 148:301 */     return apply(this.sumImpl);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public double getSumsq()
/* 152:    */   {
/* 153:310 */     return apply(this.sumsqImpl);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void clear()
/* 157:    */   {
/* 158:317 */     this.eDA.clear();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public int getWindowSize()
/* 162:    */   {
/* 163:328 */     return this.windowSize;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setWindowSize(int windowSize)
/* 167:    */   {
/* 168:341 */     if ((windowSize < 1) && 
/* 169:342 */       (windowSize != -1)) {
/* 170:343 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_POSITIVE_WINDOW_SIZE, new Object[] { Integer.valueOf(windowSize) });
/* 171:    */     }
/* 172:348 */     this.windowSize = windowSize;
/* 173:353 */     if ((windowSize != -1) && (windowSize < this.eDA.getNumElements())) {
/* 174:354 */       this.eDA.discardFrontElements(this.eDA.getNumElements() - windowSize);
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   public double[] getValues()
/* 179:    */   {
/* 180:368 */     return this.eDA.getElements();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public double[] getSortedValues()
/* 184:    */   {
/* 185:380 */     double[] sort = getValues();
/* 186:381 */     Arrays.sort(sort);
/* 187:382 */     return sort;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public double getElement(int index)
/* 191:    */   {
/* 192:391 */     return this.eDA.getElement(index);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public double getPercentile(double p)
/* 196:    */   {
/* 197:413 */     if ((this.percentileImpl instanceof Percentile)) {
/* 198:414 */       ((Percentile)this.percentileImpl).setQuantile(p);
/* 199:    */     } else {
/* 200:    */       try
/* 201:    */       {
/* 202:417 */         this.percentileImpl.getClass().getMethod("setQuantile", new Class[] { Double.TYPE }).invoke(this.percentileImpl, new Object[] { Double.valueOf(p) });
/* 203:    */       }
/* 204:    */       catch (NoSuchMethodException e1)
/* 205:    */       {
/* 206:421 */         throw new MathIllegalStateException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_UNSUPPORTED_METHOD, new Object[] { this.percentileImpl.getClass().getName(), "setQuantile" });
/* 207:    */       }
/* 208:    */       catch (IllegalAccessException e2)
/* 209:    */       {
/* 210:425 */         throw new MathIllegalStateException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_CANNOT_ACCESS_METHOD, new Object[] { "setQuantile", this.percentileImpl.getClass().getName() });
/* 211:    */       }
/* 212:    */       catch (InvocationTargetException e3)
/* 213:    */       {
/* 214:429 */         throw new IllegalStateException(e3.getCause());
/* 215:    */       }
/* 216:    */     }
/* 217:432 */     return apply(this.percentileImpl);
/* 218:    */   }
/* 219:    */   
/* 220:    */   public String toString()
/* 221:    */   {
/* 222:444 */     StringBuilder outBuffer = new StringBuilder();
/* 223:445 */     String endl = "\n";
/* 224:446 */     outBuffer.append("DescriptiveStatistics:").append(endl);
/* 225:447 */     outBuffer.append("n: ").append(getN()).append(endl);
/* 226:448 */     outBuffer.append("min: ").append(getMin()).append(endl);
/* 227:449 */     outBuffer.append("max: ").append(getMax()).append(endl);
/* 228:450 */     outBuffer.append("mean: ").append(getMean()).append(endl);
/* 229:451 */     outBuffer.append("std dev: ").append(getStandardDeviation()).append(endl);
/* 230:    */     
/* 231:453 */     outBuffer.append("median: ").append(getPercentile(50.0D)).append(endl);
/* 232:454 */     outBuffer.append("skewness: ").append(getSkewness()).append(endl);
/* 233:455 */     outBuffer.append("kurtosis: ").append(getKurtosis()).append(endl);
/* 234:456 */     return outBuffer.toString();
/* 235:    */   }
/* 236:    */   
/* 237:    */   public double apply(UnivariateStatistic stat)
/* 238:    */   {
/* 239:465 */     return stat.evaluate(this.eDA.getInternalValues(), this.eDA.start(), this.eDA.getNumElements());
/* 240:    */   }
/* 241:    */   
/* 242:    */   public synchronized UnivariateStatistic getMeanImpl()
/* 243:    */   {
/* 244:477 */     return this.meanImpl;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public synchronized void setMeanImpl(UnivariateStatistic meanImpl)
/* 248:    */   {
/* 249:488 */     this.meanImpl = meanImpl;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public synchronized UnivariateStatistic getGeometricMeanImpl()
/* 253:    */   {
/* 254:498 */     return this.geometricMeanImpl;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public synchronized void setGeometricMeanImpl(UnivariateStatistic geometricMeanImpl)
/* 258:    */   {
/* 259:510 */     this.geometricMeanImpl = geometricMeanImpl;
/* 260:    */   }
/* 261:    */   
/* 262:    */   public synchronized UnivariateStatistic getKurtosisImpl()
/* 263:    */   {
/* 264:520 */     return this.kurtosisImpl;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public synchronized void setKurtosisImpl(UnivariateStatistic kurtosisImpl)
/* 268:    */   {
/* 269:531 */     this.kurtosisImpl = kurtosisImpl;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public synchronized UnivariateStatistic getMaxImpl()
/* 273:    */   {
/* 274:541 */     return this.maxImpl;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public synchronized void setMaxImpl(UnivariateStatistic maxImpl)
/* 278:    */   {
/* 279:552 */     this.maxImpl = maxImpl;
/* 280:    */   }
/* 281:    */   
/* 282:    */   public synchronized UnivariateStatistic getMinImpl()
/* 283:    */   {
/* 284:562 */     return this.minImpl;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public synchronized void setMinImpl(UnivariateStatistic minImpl)
/* 288:    */   {
/* 289:573 */     this.minImpl = minImpl;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public synchronized UnivariateStatistic getPercentileImpl()
/* 293:    */   {
/* 294:583 */     return this.percentileImpl;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public synchronized void setPercentileImpl(UnivariateStatistic percentileImpl)
/* 298:    */   {
/* 299:    */     try
/* 300:    */     {
/* 301:600 */       percentileImpl.getClass().getMethod("setQuantile", new Class[] { Double.TYPE }).invoke(percentileImpl, new Object[] { Double.valueOf(50.0D) });
/* 302:    */     }
/* 303:    */     catch (NoSuchMethodException e1)
/* 304:    */     {
/* 305:604 */       throw new MathIllegalArgumentException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_UNSUPPORTED_METHOD, new Object[] { percentileImpl.getClass().getName(), "setQuantile" });
/* 306:    */     }
/* 307:    */     catch (IllegalAccessException e2)
/* 308:    */     {
/* 309:608 */       throw new MathIllegalArgumentException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_CANNOT_ACCESS_METHOD, new Object[] { "setQuantile", percentileImpl.getClass().getName() });
/* 310:    */     }
/* 311:    */     catch (InvocationTargetException e3)
/* 312:    */     {
/* 313:612 */       throw new IllegalArgumentException(e3.getCause());
/* 314:    */     }
/* 315:614 */     this.percentileImpl = percentileImpl;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public synchronized UnivariateStatistic getSkewnessImpl()
/* 319:    */   {
/* 320:624 */     return this.skewnessImpl;
/* 321:    */   }
/* 322:    */   
/* 323:    */   public synchronized void setSkewnessImpl(UnivariateStatistic skewnessImpl)
/* 324:    */   {
/* 325:636 */     this.skewnessImpl = skewnessImpl;
/* 326:    */   }
/* 327:    */   
/* 328:    */   public synchronized UnivariateStatistic getVarianceImpl()
/* 329:    */   {
/* 330:646 */     return this.varianceImpl;
/* 331:    */   }
/* 332:    */   
/* 333:    */   public synchronized void setVarianceImpl(UnivariateStatistic varianceImpl)
/* 334:    */   {
/* 335:658 */     this.varianceImpl = varianceImpl;
/* 336:    */   }
/* 337:    */   
/* 338:    */   public synchronized UnivariateStatistic getSumsqImpl()
/* 339:    */   {
/* 340:668 */     return this.sumsqImpl;
/* 341:    */   }
/* 342:    */   
/* 343:    */   public synchronized void setSumsqImpl(UnivariateStatistic sumsqImpl)
/* 344:    */   {
/* 345:679 */     this.sumsqImpl = sumsqImpl;
/* 346:    */   }
/* 347:    */   
/* 348:    */   public synchronized UnivariateStatistic getSumImpl()
/* 349:    */   {
/* 350:689 */     return this.sumImpl;
/* 351:    */   }
/* 352:    */   
/* 353:    */   public synchronized void setSumImpl(UnivariateStatistic sumImpl)
/* 354:    */   {
/* 355:700 */     this.sumImpl = sumImpl;
/* 356:    */   }
/* 357:    */   
/* 358:    */   public DescriptiveStatistics copy()
/* 359:    */   {
/* 360:709 */     DescriptiveStatistics result = new DescriptiveStatistics();
/* 361:710 */     copy(this, result);
/* 362:711 */     return result;
/* 363:    */   }
/* 364:    */   
/* 365:    */   public static void copy(DescriptiveStatistics source, DescriptiveStatistics dest)
/* 366:    */     throws NullArgumentException
/* 367:    */   {
/* 368:724 */     MathUtils.checkNotNull(source);
/* 369:725 */     MathUtils.checkNotNull(dest);
/* 370:    */     
/* 371:727 */     dest.eDA = source.eDA.copy();
/* 372:728 */     dest.windowSize = source.windowSize;
/* 373:    */     
/* 374:    */ 
/* 375:731 */     dest.maxImpl = source.maxImpl.copy();
/* 376:732 */     dest.meanImpl = source.meanImpl.copy();
/* 377:733 */     dest.minImpl = source.minImpl.copy();
/* 378:734 */     dest.sumImpl = source.sumImpl.copy();
/* 379:735 */     dest.varianceImpl = source.varianceImpl.copy();
/* 380:736 */     dest.sumsqImpl = source.sumsqImpl.copy();
/* 381:737 */     dest.geometricMeanImpl = source.geometricMeanImpl.copy();
/* 382:738 */     dest.kurtosisImpl = source.kurtosisImpl;
/* 383:739 */     dest.skewnessImpl = source.skewnessImpl;
/* 384:740 */     dest.percentileImpl = source.percentileImpl;
/* 385:    */   }
/* 386:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
 * JD-Core Version:    0.7.0.1
 */
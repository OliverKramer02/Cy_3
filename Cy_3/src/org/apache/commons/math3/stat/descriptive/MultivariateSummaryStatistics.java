/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   9:    */ import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
/*  10:    */ import org.apache.commons.math3.stat.descriptive.moment.Mean;
/*  11:    */ import org.apache.commons.math3.stat.descriptive.moment.VectorialCovariance;
/*  12:    */ import org.apache.commons.math3.stat.descriptive.rank.Max;
/*  13:    */ import org.apache.commons.math3.stat.descriptive.rank.Min;
/*  14:    */ import org.apache.commons.math3.stat.descriptive.summary.Sum;
/*  15:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
/*  16:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
/*  17:    */ import org.apache.commons.math3.util.FastMath;
/*  18:    */ import org.apache.commons.math3.util.MathArrays;
/*  19:    */ import org.apache.commons.math3.util.MathUtils;
/*  20:    */ import org.apache.commons.math3.util.Precision;
/*  21:    */ 
/*  22:    */ public class MultivariateSummaryStatistics
/*  23:    */   implements StatisticalMultivariateSummary, Serializable
/*  24:    */ {
/*  25:    */   private static final long serialVersionUID = 2271900808994826718L;
/*  26:    */   private int k;
/*  27: 82 */   private long n = 0L;
/*  28:    */   private StorelessUnivariateStatistic[] sumImpl;
/*  29:    */   private StorelessUnivariateStatistic[] sumSqImpl;
/*  30:    */   private StorelessUnivariateStatistic[] minImpl;
/*  31:    */   private StorelessUnivariateStatistic[] maxImpl;
/*  32:    */   private StorelessUnivariateStatistic[] sumLogImpl;
/*  33:    */   private StorelessUnivariateStatistic[] geoMeanImpl;
/*  34:    */   private StorelessUnivariateStatistic[] meanImpl;
/*  35:    */   private VectorialCovariance covarianceImpl;
/*  36:    */   
/*  37:    */   public MultivariateSummaryStatistics(int k, boolean isCovarianceBiasCorrected)
/*  38:    */   {
/*  39:116 */     this.k = k;
/*  40:    */     
/*  41:118 */     this.sumImpl = new StorelessUnivariateStatistic[k];
/*  42:119 */     this.sumSqImpl = new StorelessUnivariateStatistic[k];
/*  43:120 */     this.minImpl = new StorelessUnivariateStatistic[k];
/*  44:121 */     this.maxImpl = new StorelessUnivariateStatistic[k];
/*  45:122 */     this.sumLogImpl = new StorelessUnivariateStatistic[k];
/*  46:123 */     this.geoMeanImpl = new StorelessUnivariateStatistic[k];
/*  47:124 */     this.meanImpl = new StorelessUnivariateStatistic[k];
/*  48:126 */     for (int i = 0; i < k; i++)
/*  49:    */     {
/*  50:127 */       this.sumImpl[i] = new Sum();
/*  51:128 */       this.sumSqImpl[i] = new SumOfSquares();
/*  52:129 */       this.minImpl[i] = new Min();
/*  53:130 */       this.maxImpl[i] = new Max();
/*  54:131 */       this.sumLogImpl[i] = new SumOfLogs();
/*  55:132 */       this.geoMeanImpl[i] = new GeometricMean();
/*  56:133 */       this.meanImpl[i] = new Mean();
/*  57:    */     }
/*  58:136 */     this.covarianceImpl = new VectorialCovariance(k, isCovarianceBiasCorrected);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void addValue(double[] value)
/*  62:    */   {
/*  63:149 */     checkDimension(value.length);
/*  64:150 */     for (int i = 0; i < this.k; i++)
/*  65:    */     {
/*  66:151 */       double v = value[i];
/*  67:152 */       this.sumImpl[i].increment(v);
/*  68:153 */       this.sumSqImpl[i].increment(v);
/*  69:154 */       this.minImpl[i].increment(v);
/*  70:155 */       this.maxImpl[i].increment(v);
/*  71:156 */       this.sumLogImpl[i].increment(v);
/*  72:157 */       this.geoMeanImpl[i].increment(v);
/*  73:158 */       this.meanImpl[i].increment(v);
/*  74:    */     }
/*  75:160 */     this.covarianceImpl.increment(value);
/*  76:161 */     this.n += 1L;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int getDimension()
/*  80:    */   {
/*  81:169 */     return this.k;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public long getN()
/*  85:    */   {
/*  86:177 */     return this.n;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private double[] getResults(StorelessUnivariateStatistic[] stats)
/*  90:    */   {
/*  91:186 */     double[] results = new double[stats.length];
/*  92:187 */     for (int i = 0; i < results.length; i++) {
/*  93:188 */       results[i] = stats[i].getResult();
/*  94:    */     }
/*  95:190 */     return results;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public double[] getSum()
/*  99:    */   {
/* 100:201 */     return getResults(this.sumImpl);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double[] getSumSq()
/* 104:    */   {
/* 105:212 */     return getResults(this.sumSqImpl);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public double[] getSumLog()
/* 109:    */   {
/* 110:223 */     return getResults(this.sumLogImpl);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public double[] getMean()
/* 114:    */   {
/* 115:234 */     return getResults(this.meanImpl);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public double[] getStandardDeviation()
/* 119:    */   {
/* 120:245 */     double[] stdDev = new double[this.k];
/* 121:246 */     if (getN() < 1L)
/* 122:    */     {
/* 123:247 */       Arrays.fill(stdDev, (0.0D / 0.0D));
/* 124:    */     }
/* 125:248 */     else if (getN() < 2L)
/* 126:    */     {
/* 127:249 */       Arrays.fill(stdDev, 0.0D);
/* 128:    */     }
/* 129:    */     else
/* 130:    */     {
/* 131:251 */       RealMatrix matrix = this.covarianceImpl.getResult();
/* 132:252 */       for (int i = 0; i < this.k; i++) {
/* 133:253 */         stdDev[i] = FastMath.sqrt(matrix.getEntry(i, i));
/* 134:    */       }
/* 135:    */     }
/* 136:256 */     return stdDev;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public RealMatrix getCovariance()
/* 140:    */   {
/* 141:265 */     return this.covarianceImpl.getResult();
/* 142:    */   }
/* 143:    */   
/* 144:    */   public double[] getMax()
/* 145:    */   {
/* 146:276 */     return getResults(this.maxImpl);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public double[] getMin()
/* 150:    */   {
/* 151:287 */     return getResults(this.minImpl);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public double[] getGeometricMean()
/* 155:    */   {
/* 156:298 */     return getResults(this.geoMeanImpl);
/* 157:    */   }
/* 158:    */   
/* 159:    */   public String toString()
/* 160:    */   {
/* 161:309 */     String separator = ", ";
/* 162:310 */     String suffix = System.getProperty("line.separator");
/* 163:311 */     StringBuilder outBuffer = new StringBuilder();
/* 164:312 */     outBuffer.append("MultivariateSummaryStatistics:" + suffix);
/* 165:313 */     outBuffer.append("n: " + getN() + suffix);
/* 166:314 */     append(outBuffer, getMin(), "min: ", ", ", suffix);
/* 167:315 */     append(outBuffer, getMax(), "max: ", ", ", suffix);
/* 168:316 */     append(outBuffer, getMean(), "mean: ", ", ", suffix);
/* 169:317 */     append(outBuffer, getGeometricMean(), "geometric mean: ", ", ", suffix);
/* 170:318 */     append(outBuffer, getSumSq(), "sum of squares: ", ", ", suffix);
/* 171:319 */     append(outBuffer, getSumLog(), "sum of logarithms: ", ", ", suffix);
/* 172:320 */     append(outBuffer, getStandardDeviation(), "standard deviation: ", ", ", suffix);
/* 173:321 */     outBuffer.append("covariance: " + getCovariance().toString() + suffix);
/* 174:322 */     return outBuffer.toString();
/* 175:    */   }
/* 176:    */   
/* 177:    */   private void append(StringBuilder buffer, double[] data, String prefix, String separator, String suffix)
/* 178:    */   {
/* 179:335 */     buffer.append(prefix);
/* 180:336 */     for (int i = 0; i < data.length; i++)
/* 181:    */     {
/* 182:337 */       if (i > 0) {
/* 183:338 */         buffer.append(separator);
/* 184:    */       }
/* 185:340 */       buffer.append(data[i]);
/* 186:    */     }
/* 187:342 */     buffer.append(suffix);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void clear()
/* 191:    */   {
/* 192:349 */     this.n = 0L;
/* 193:350 */     for (int i = 0; i < this.k; i++)
/* 194:    */     {
/* 195:351 */       this.minImpl[i].clear();
/* 196:352 */       this.maxImpl[i].clear();
/* 197:353 */       this.sumImpl[i].clear();
/* 198:354 */       this.sumLogImpl[i].clear();
/* 199:355 */       this.sumSqImpl[i].clear();
/* 200:356 */       this.geoMeanImpl[i].clear();
/* 201:357 */       this.meanImpl[i].clear();
/* 202:    */     }
/* 203:359 */     this.covarianceImpl.clear();
/* 204:    */   }
/* 205:    */   
/* 206:    */   public boolean equals(Object object)
/* 207:    */   {
/* 208:370 */     if (object == this) {
/* 209:371 */       return true;
/* 210:    */     }
/* 211:373 */     if (!(object instanceof MultivariateSummaryStatistics)) {
/* 212:374 */       return false;
/* 213:    */     }
/* 214:376 */     MultivariateSummaryStatistics stat = (MultivariateSummaryStatistics)object;
/* 215:377 */     return (MathArrays.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean())) && (MathArrays.equalsIncludingNaN(stat.getMax(), getMax())) && (MathArrays.equalsIncludingNaN(stat.getMean(), getMean())) && (MathArrays.equalsIncludingNaN(stat.getMin(), getMin())) && (Precision.equalsIncludingNaN((float)stat.getN(), (float)getN())) && (MathArrays.equalsIncludingNaN(stat.getSum(), getSum())) && (MathArrays.equalsIncludingNaN(stat.getSumSq(), getSumSq())) && (MathArrays.equalsIncludingNaN(stat.getSumLog(), getSumLog())) && (stat.getCovariance().equals(getCovariance()));
/* 216:    */   }
/* 217:    */   
/* 218:    */   public int hashCode()
/* 219:    */   {
/* 220:395 */     int result = 31 + MathUtils.hash(getGeometricMean());
/* 221:396 */     result = result * 31 + MathUtils.hash(getGeometricMean());
/* 222:397 */     result = result * 31 + MathUtils.hash(getMax());
/* 223:398 */     result = result * 31 + MathUtils.hash(getMean());
/* 224:399 */     result = result * 31 + MathUtils.hash(getMin());
/* 225:400 */     result = result * 31 + MathUtils.hash(getN());
/* 226:401 */     result = result * 31 + MathUtils.hash(getSum());
/* 227:402 */     result = result * 31 + MathUtils.hash(getSumSq());
/* 228:403 */     result = result * 31 + MathUtils.hash(getSumLog());
/* 229:404 */     result = result * 31 + getCovariance().hashCode();
/* 230:405 */     return result;
/* 231:    */   }
/* 232:    */   
/* 233:    */   private void setImpl(StorelessUnivariateStatistic[] newImpl, StorelessUnivariateStatistic[] oldImpl)
/* 234:    */   {
/* 235:420 */     checkEmpty();
/* 236:421 */     checkDimension(newImpl.length);
/* 237:422 */     System.arraycopy(newImpl, 0, oldImpl, 0, newImpl.length);
/* 238:    */   }
/* 239:    */   
/* 240:    */   public StorelessUnivariateStatistic[] getSumImpl()
/* 241:    */   {
/* 242:431 */     return (StorelessUnivariateStatistic[])this.sumImpl.clone();
/* 243:    */   }
/* 244:    */   
/* 245:    */   public void setSumImpl(StorelessUnivariateStatistic[] sumImpl)
/* 246:    */   {
/* 247:448 */     setImpl(sumImpl, this.sumImpl);
/* 248:    */   }
/* 249:    */   
/* 250:    */   public StorelessUnivariateStatistic[] getSumsqImpl()
/* 251:    */   {
/* 252:457 */     return (StorelessUnivariateStatistic[])this.sumSqImpl.clone();
/* 253:    */   }
/* 254:    */   
/* 255:    */   public void setSumsqImpl(StorelessUnivariateStatistic[] sumsqImpl)
/* 256:    */   {
/* 257:474 */     setImpl(sumsqImpl, this.sumSqImpl);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public StorelessUnivariateStatistic[] getMinImpl()
/* 261:    */   {
/* 262:483 */     return (StorelessUnivariateStatistic[])this.minImpl.clone();
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void setMinImpl(StorelessUnivariateStatistic[] minImpl)
/* 266:    */   {
/* 267:500 */     setImpl(minImpl, this.minImpl);
/* 268:    */   }
/* 269:    */   
/* 270:    */   public StorelessUnivariateStatistic[] getMaxImpl()
/* 271:    */   {
/* 272:509 */     return (StorelessUnivariateStatistic[])this.maxImpl.clone();
/* 273:    */   }
/* 274:    */   
/* 275:    */   public void setMaxImpl(StorelessUnivariateStatistic[] maxImpl)
/* 276:    */   {
/* 277:526 */     setImpl(maxImpl, this.maxImpl);
/* 278:    */   }
/* 279:    */   
/* 280:    */   public StorelessUnivariateStatistic[] getSumLogImpl()
/* 281:    */   {
/* 282:535 */     return (StorelessUnivariateStatistic[])this.sumLogImpl.clone();
/* 283:    */   }
/* 284:    */   
/* 285:    */   public void setSumLogImpl(StorelessUnivariateStatistic[] sumLogImpl)
/* 286:    */   {
/* 287:552 */     setImpl(sumLogImpl, this.sumLogImpl);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public StorelessUnivariateStatistic[] getGeoMeanImpl()
/* 291:    */   {
/* 292:561 */     return (StorelessUnivariateStatistic[])this.geoMeanImpl.clone();
/* 293:    */   }
/* 294:    */   
/* 295:    */   public void setGeoMeanImpl(StorelessUnivariateStatistic[] geoMeanImpl)
/* 296:    */   {
/* 297:578 */     setImpl(geoMeanImpl, this.geoMeanImpl);
/* 298:    */   }
/* 299:    */   
/* 300:    */   public StorelessUnivariateStatistic[] getMeanImpl()
/* 301:    */   {
/* 302:587 */     return (StorelessUnivariateStatistic[])this.meanImpl.clone();
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void setMeanImpl(StorelessUnivariateStatistic[] meanImpl)
/* 306:    */   {
/* 307:604 */     setImpl(meanImpl, this.meanImpl);
/* 308:    */   }
/* 309:    */   
/* 310:    */   private void checkEmpty()
/* 311:    */   {
/* 312:612 */     if (this.n > 0L) {
/* 313:613 */       throw new MathIllegalStateException(LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, new Object[] { Long.valueOf(this.n) });
/* 314:    */     }
/* 315:    */   }
/* 316:    */   
/* 317:    */   private void checkDimension(int dimension)
/* 318:    */   {
/* 319:624 */     if (dimension != this.k) {
/* 320:625 */       throw new DimensionMismatchException(dimension, this.k);
/* 321:    */     }
/* 322:    */   }
/* 323:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics
 * JD-Core Version:    0.7.0.1
 */
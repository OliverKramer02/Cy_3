/*   1:    */ package org.apache.commons.math3.stat;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.NoDataException;
/*   5:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
/*   8:    */ import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
/*   9:    */ import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
/*  10:    */ import org.apache.commons.math3.stat.descriptive.moment.Mean;
/*  11:    */ import org.apache.commons.math3.stat.descriptive.moment.Variance;
/*  12:    */ import org.apache.commons.math3.stat.descriptive.rank.Max;
/*  13:    */ import org.apache.commons.math3.stat.descriptive.rank.Min;
/*  14:    */ import org.apache.commons.math3.stat.descriptive.rank.Percentile;
/*  15:    */ import org.apache.commons.math3.stat.descriptive.summary.Product;
/*  16:    */ import org.apache.commons.math3.stat.descriptive.summary.Sum;
/*  17:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
/*  18:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
/*  19:    */ 
/*  20:    */ public final class StatUtils
/*  21:    */ {
/*  22: 45 */   private static final UnivariateStatistic SUM = new Sum();
/*  23: 48 */   private static final UnivariateStatistic SUM_OF_SQUARES = new SumOfSquares();
/*  24: 51 */   private static final UnivariateStatistic PRODUCT = new Product();
/*  25: 54 */   private static final UnivariateStatistic SUM_OF_LOGS = new SumOfLogs();
/*  26: 57 */   private static final UnivariateStatistic MIN = new Min();
/*  27: 60 */   private static final UnivariateStatistic MAX = new Max();
/*  28: 63 */   private static final UnivariateStatistic MEAN = new Mean();
/*  29: 66 */   private static final Variance VARIANCE = new Variance();
/*  30: 69 */   private static final Percentile PERCENTILE = new Percentile();
/*  31: 72 */   private static final GeometricMean GEOMETRIC_MEAN = new GeometricMean();
/*  32:    */   
/*  33:    */   public static double sum(double[] values)
/*  34:    */   {
/*  35: 93 */     return SUM.evaluate(values);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static double sum(double[] values, int begin, int length)
/*  39:    */   {
/*  40:112 */     return SUM.evaluate(values, begin, length);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static double sumSq(double[] values)
/*  44:    */   {
/*  45:127 */     return SUM_OF_SQUARES.evaluate(values);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static double sumSq(double[] values, int begin, int length)
/*  49:    */   {
/*  50:146 */     return SUM_OF_SQUARES.evaluate(values, begin, length);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static double product(double[] values)
/*  54:    */   {
/*  55:160 */     return PRODUCT.evaluate(values);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static double product(double[] values, int begin, int length)
/*  59:    */   {
/*  60:179 */     return PRODUCT.evaluate(values, begin, length);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static double sumLog(double[] values)
/*  64:    */   {
/*  65:197 */     return SUM_OF_LOGS.evaluate(values);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static double sumLog(double[] values, int begin, int length)
/*  69:    */   {
/*  70:220 */     return SUM_OF_LOGS.evaluate(values, begin, length);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static double mean(double[] values)
/*  74:    */   {
/*  75:237 */     return MEAN.evaluate(values);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static double mean(double[] values, int begin, int length)
/*  79:    */   {
/*  80:259 */     return MEAN.evaluate(values, begin, length);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public static double geometricMean(double[] values)
/*  84:    */   {
/*  85:276 */     return GEOMETRIC_MEAN.evaluate(values);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static double geometricMean(double[] values, int begin, int length)
/*  89:    */   {
/*  90:298 */     return GEOMETRIC_MEAN.evaluate(values, begin, length);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static double variance(double[] values)
/*  94:    */   {
/*  95:322 */     return VARIANCE.evaluate(values);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static double variance(double[] values, int begin, int length)
/*  99:    */   {
/* 100:351 */     return VARIANCE.evaluate(values, begin, length);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static double variance(double[] values, double mean, int begin, int length)
/* 104:    */   {
/* 105:386 */     return VARIANCE.evaluate(values, mean, begin, length);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static double variance(double[] values, double mean)
/* 109:    */   {
/* 110:416 */     return VARIANCE.evaluate(values, mean);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static double populationVariance(double[] values)
/* 114:    */   {
/* 115:436 */     return new Variance(false).evaluate(values);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public static double populationVariance(double[] values, int begin, int length)
/* 119:    */   {
/* 120:462 */     return new Variance(false).evaluate(values, begin, length);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static double populationVariance(double[] values, double mean, int begin, int length)
/* 124:    */   {
/* 125:494 */     return new Variance(false).evaluate(values, mean, begin, length);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public static double populationVariance(double[] values, double mean)
/* 129:    */   {
/* 130:521 */     return new Variance(false).evaluate(values, mean);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static double max(double[] values)
/* 134:    */   {
/* 135:542 */     return MAX.evaluate(values);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static double max(double[] values, int begin, int length)
/* 139:    */   {
/* 140:569 */     return MAX.evaluate(values, begin, length);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static double min(double[] values)
/* 144:    */   {
/* 145:590 */     return MIN.evaluate(values);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public static double min(double[] values, int begin, int length)
/* 149:    */   {
/* 150:617 */     return MIN.evaluate(values, begin, length);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public static double percentile(double[] values, double p)
/* 154:    */   {
/* 155:644 */     return PERCENTILE.evaluate(values, p);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public static double percentile(double[] values, int begin, int length, double p)
/* 159:    */   {
/* 160:676 */     return PERCENTILE.evaluate(values, begin, length, p);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static double sumDifference(double[] sample1, double[] sample2)
/* 164:    */   {
/* 165:691 */     int n = sample1.length;
/* 166:692 */     if (n != sample2.length) {
/* 167:693 */       throw new DimensionMismatchException(n, sample2.length);
/* 168:    */     }
/* 169:695 */     if (n <= 0) {
/* 170:696 */       throw new NoDataException(LocalizedFormats.INSUFFICIENT_DIMENSION);
/* 171:    */     }
/* 172:698 */     double result = 0.0D;
/* 173:699 */     for (int i = 0; i < n; i++) {
/* 174:700 */       result += sample1[i] - sample2[i];
/* 175:    */     }
/* 176:702 */     return result;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static double meanDifference(double[] sample1, double[] sample2)
/* 180:    */   {
/* 181:717 */     return sumDifference(sample1, sample2) / sample1.length;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static double varianceDifference(double[] sample1, double[] sample2, double meanDifference)
/* 185:    */   {
/* 186:736 */     double sum1 = 0.0D;
/* 187:737 */     double sum2 = 0.0D;
/* 188:738 */     double diff = 0.0D;
/* 189:739 */     int n = sample1.length;
/* 190:740 */     if (n != sample2.length) {
/* 191:741 */       throw new DimensionMismatchException(n, sample2.length);
/* 192:    */     }
/* 193:743 */     if (n < 2) {
/* 194:744 */       throw new NumberIsTooSmallException(Integer.valueOf(n), Integer.valueOf(2), true);
/* 195:    */     }
/* 196:746 */     for (int i = 0; i < n; i++)
/* 197:    */     {
/* 198:747 */       diff = sample1[i] - sample2[i];
/* 199:748 */       sum1 += (diff - meanDifference) * (diff - meanDifference);
/* 200:749 */       sum2 += diff - meanDifference;
/* 201:    */     }
/* 202:751 */     return (sum1 - sum2 * sum2 / n) / (n - 1);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public static double[] normalize(double[] sample)
/* 206:    */   {
/* 207:762 */     DescriptiveStatistics stats = new DescriptiveStatistics();
/* 208:765 */     for (int i = 0; i < sample.length; i++) {
/* 209:766 */       stats.addValue(sample[i]);
/* 210:    */     }
/* 211:770 */     double mean = stats.getMean();
/* 212:771 */     double standardDeviation = stats.getStandardDeviation();
/* 213:    */     
/* 214:    */ 
/* 215:774 */     double[] standardizedSample = new double[sample.length];
/* 216:776 */     for (int i = 0; i < sample.length; i++) {
/* 217:778 */       standardizedSample[i] = ((sample[i] - mean) / standardDeviation);
/* 218:    */     }
/* 219:780 */     return standardizedSample;
/* 220:    */   }
/* 221:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.StatUtils
 * JD-Core Version:    0.7.0.1
 */
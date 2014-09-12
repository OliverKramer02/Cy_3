/*    1:     */ package org.apache.commons.math3.stat.inference;
/*    2:     */ 
/*    3:     */ import org.apache.commons.math3.distribution.TDistribution;
/*    4:     */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*    5:     */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*    6:     */ import org.apache.commons.math3.exception.NoDataException;
/*    7:     */ import org.apache.commons.math3.exception.NullArgumentException;
/*    8:     */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*    9:     */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   10:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   11:     */ import org.apache.commons.math3.stat.StatUtils;
/*   12:     */ import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
/*   13:     */ import org.apache.commons.math3.util.FastMath;
/*   14:     */ 
/*   15:     */ public class TTest
/*   16:     */ {
/*   17:     */   public double pairedT(double[] sample1, double[] sample2)
/*   18:     */     throws NullArgumentException, NoDataException, DimensionMismatchException, NumberIsTooSmallException
/*   19:     */   {
/*   20:  83 */     checkSampleData(sample1);
/*   21:  84 */     checkSampleData(sample2);
/*   22:  85 */     double meanDifference = StatUtils.meanDifference(sample1, sample2);
/*   23:  86 */     return t(meanDifference, 0.0D, StatUtils.varianceDifference(sample1, sample2, meanDifference), sample1.length);
/*   24:     */   }
/*   25:     */   
/*   26:     */   public double pairedTTest(double[] sample1, double[] sample2)
/*   27:     */     throws NullArgumentException, NoDataException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException
/*   28:     */   {
/*   29: 132 */     double meanDifference = StatUtils.meanDifference(sample1, sample2);
/*   30: 133 */     return tTest(meanDifference, 0.0D, StatUtils.varianceDifference(sample1, sample2, meanDifference), sample1.length);
/*   31:     */   }
/*   32:     */   
/*   33:     */   public boolean pairedTTest(double[] sample1, double[] sample2, double alpha)
/*   34:     */     throws NullArgumentException, NoDataException, DimensionMismatchException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*   35:     */   {
/*   36: 180 */     checkSignificanceLevel(alpha);
/*   37: 181 */     return pairedTTest(sample1, sample2) < alpha;
/*   38:     */   }
/*   39:     */   
/*   40:     */   public double t(double mu, double[] observed)
/*   41:     */     throws NullArgumentException, NumberIsTooSmallException
/*   42:     */   {
/*   43: 204 */     checkSampleData(observed);
/*   44: 205 */     return t(StatUtils.mean(observed), mu, StatUtils.variance(observed), observed.length);
/*   45:     */   }
/*   46:     */   
/*   47:     */   public double t(double mu, StatisticalSummary sampleStats)
/*   48:     */     throws NullArgumentException, NumberIsTooSmallException
/*   49:     */   {
/*   50: 230 */     checkSampleData(sampleStats);
/*   51: 231 */     return t(sampleStats.getMean(), mu, sampleStats.getVariance(), sampleStats.getN());
/*   52:     */   }
/*   53:     */   
/*   54:     */   public double homoscedasticT(double[] sample1, double[] sample2)
/*   55:     */     throws NullArgumentException, NumberIsTooSmallException
/*   56:     */   {
/*   57: 273 */     checkSampleData(sample1);
/*   58: 274 */     checkSampleData(sample2);
/*   59: 275 */     return homoscedasticT(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
/*   60:     */   }
/*   61:     */   
/*   62:     */   public double t(double[] sample1, double[] sample2)
/*   63:     */     throws NullArgumentException, NumberIsTooSmallException
/*   64:     */   {
/*   65: 313 */     checkSampleData(sample1);
/*   66: 314 */     checkSampleData(sample2);
/*   67: 315 */     return t(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
/*   68:     */   }
/*   69:     */   
/*   70:     */   public double t(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/*   71:     */     throws NullArgumentException, NumberIsTooSmallException
/*   72:     */   {
/*   73: 357 */     checkSampleData(sampleStats1);
/*   74: 358 */     checkSampleData(sampleStats2);
/*   75: 359 */     return t(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
/*   76:     */   }
/*   77:     */   
/*   78:     */   public double homoscedasticT(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/*   79:     */     throws NullArgumentException, NumberIsTooSmallException
/*   80:     */   {
/*   81: 405 */     checkSampleData(sampleStats1);
/*   82: 406 */     checkSampleData(sampleStats2);
/*   83: 407 */     return homoscedasticT(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
/*   84:     */   }
/*   85:     */   
/*   86:     */   public double tTest(double mu, double[] sample)
/*   87:     */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*   88:     */   {
/*   89: 444 */     checkSampleData(sample);
/*   90: 445 */     return tTest(StatUtils.mean(sample), mu, StatUtils.variance(sample), sample.length);
/*   91:     */   }
/*   92:     */   
/*   93:     */   public boolean tTest(double mu, double[] sample, double alpha)
/*   94:     */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*   95:     */   {
/*   96: 491 */     checkSignificanceLevel(alpha);
/*   97: 492 */     return tTest(mu, sample) < alpha;
/*   98:     */   }
/*   99:     */   
/*  100:     */   public double tTest(double mu, StatisticalSummary sampleStats)
/*  101:     */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*  102:     */   {
/*  103: 529 */     checkSampleData(sampleStats);
/*  104: 530 */     return tTest(sampleStats.getMean(), mu, sampleStats.getVariance(), sampleStats.getN());
/*  105:     */   }
/*  106:     */   
/*  107:     */   public boolean tTest(double mu, StatisticalSummary sampleStats, double alpha)
/*  108:     */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*  109:     */   {
/*  110: 578 */     checkSignificanceLevel(alpha);
/*  111: 579 */     return tTest(mu, sampleStats) < alpha;
/*  112:     */   }
/*  113:     */   
/*  114:     */   public double tTest(double[] sample1, double[] sample2)
/*  115:     */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*  116:     */   {
/*  117: 624 */     checkSampleData(sample1);
/*  118: 625 */     checkSampleData(sample2);
/*  119: 626 */     return tTest(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
/*  120:     */   }
/*  121:     */   
/*  122:     */   public double homoscedasticTTest(double[] sample1, double[] sample2)
/*  123:     */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*  124:     */   {
/*  125: 670 */     checkSampleData(sample1);
/*  126: 671 */     checkSampleData(sample2);
/*  127: 672 */     return homoscedasticTTest(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
/*  128:     */   }
/*  129:     */   
/*  130:     */   public boolean tTest(double[] sample1, double[] sample2, double alpha)
/*  131:     */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*  132:     */   {
/*  133: 737 */     checkSignificanceLevel(alpha);
/*  134: 738 */     return tTest(sample1, sample2) < alpha;
/*  135:     */   }
/*  136:     */   
/*  137:     */   public boolean homoscedasticTTest(double[] sample1, double[] sample2, double alpha)
/*  138:     */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*  139:     */   {
/*  140: 800 */     checkSignificanceLevel(alpha);
/*  141: 801 */     return homoscedasticTTest(sample1, sample2) < alpha;
/*  142:     */   }
/*  143:     */   
/*  144:     */   public double tTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/*  145:     */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*  146:     */   {
/*  147: 845 */     checkSampleData(sampleStats1);
/*  148: 846 */     checkSampleData(sampleStats2);
/*  149: 847 */     return tTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
/*  150:     */   }
/*  151:     */   
/*  152:     */   public double homoscedasticTTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/*  153:     */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*  154:     */   {
/*  155: 892 */     checkSampleData(sampleStats1);
/*  156: 893 */     checkSampleData(sampleStats2);
/*  157: 894 */     return homoscedasticTTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
/*  158:     */   }
/*  159:     */   
/*  160:     */   public boolean tTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2, double alpha)
/*  161:     */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*  162:     */   {
/*  163: 963 */     checkSignificanceLevel(alpha);
/*  164: 964 */     return tTest(sampleStats1, sampleStats2) < alpha;
/*  165:     */   }
/*  166:     */   
/*  167:     */   protected double df(double v1, double v2, double n1, double n2)
/*  168:     */   {
/*  169: 980 */     return (v1 / n1 + v2 / n2) * (v1 / n1 + v2 / n2) / (v1 * v1 / (n1 * n1 * (n1 - 1.0D)) + v2 * v2 / (n2 * n2 * (n2 - 1.0D)));
/*  170:     */   }
/*  171:     */   
/*  172:     */   protected double t(double m, double mu, double v, double n)
/*  173:     */   {
/*  174: 996 */     return (m - mu) / FastMath.sqrt(v / n);
/*  175:     */   }
/*  176:     */   
/*  177:     */   protected double t(double m1, double m2, double v1, double v2, double n1, double n2)
/*  178:     */   {
/*  179:1015 */     return (m1 - m2) / FastMath.sqrt(v1 / n1 + v2 / n2);
/*  180:     */   }
/*  181:     */   
/*  182:     */   protected double homoscedasticT(double m1, double m2, double v1, double v2, double n1, double n2)
/*  183:     */   {
/*  184:1033 */     double pooledVariance = ((n1 - 1.0D) * v1 + (n2 - 1.0D) * v2) / (n1 + n2 - 2.0D);
/*  185:1034 */     return (m1 - m2) / FastMath.sqrt(pooledVariance * (1.0D / n1 + 1.0D / n2));
/*  186:     */   }
/*  187:     */   
/*  188:     */   protected double tTest(double m, double mu, double v, double n)
/*  189:     */     throws MaxCountExceededException
/*  190:     */   {
/*  191:1051 */     double t = FastMath.abs(t(m, mu, v, n));
/*  192:1052 */     TDistribution distribution = new TDistribution(n - 1.0D);
/*  193:1053 */     return 2.0D * distribution.cumulativeProbability(-t);
/*  194:     */   }
/*  195:     */   
/*  196:     */   protected double tTest(double m1, double m2, double v1, double v2, double n1, double n2)
/*  197:     */     throws MaxCountExceededException
/*  198:     */   {
/*  199:1077 */     double t = FastMath.abs(t(m1, m2, v1, v2, n1, n2));
/*  200:1078 */     double degreesOfFreedom = df(v1, v2, n1, n2);
/*  201:1079 */     TDistribution distribution = new TDistribution(degreesOfFreedom);
/*  202:1080 */     return 2.0D * distribution.cumulativeProbability(-t);
/*  203:     */   }
/*  204:     */   
/*  205:     */   protected double homoscedasticTTest(double m1, double m2, double v1, double v2, double n1, double n2)
/*  206:     */     throws MaxCountExceededException
/*  207:     */   {
/*  208:1104 */     double t = FastMath.abs(homoscedasticT(m1, m2, v1, v2, n1, n2));
/*  209:1105 */     double degreesOfFreedom = n1 + n2 - 2.0D;
/*  210:1106 */     TDistribution distribution = new TDistribution(degreesOfFreedom);
/*  211:1107 */     return 2.0D * distribution.cumulativeProbability(-t);
/*  212:     */   }
/*  213:     */   
/*  214:     */   private void checkSignificanceLevel(double alpha)
/*  215:     */     throws OutOfRangeException
/*  216:     */   {
/*  217:1120 */     if ((alpha <= 0.0D) || (alpha > 0.5D)) {
/*  218:1121 */       throw new OutOfRangeException(LocalizedFormats.SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Double.valueOf(0.0D), Double.valueOf(0.5D));
/*  219:     */     }
/*  220:     */   }
/*  221:     */   
/*  222:     */   private void checkSampleData(double[] data)
/*  223:     */     throws NullArgumentException, NumberIsTooSmallException
/*  224:     */   {
/*  225:1137 */     if (data == null) {
/*  226:1138 */       throw new NullArgumentException();
/*  227:     */     }
/*  228:1140 */     if (data.length < 2) {
/*  229:1141 */       throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DATA_FOR_T_STATISTIC, Integer.valueOf(data.length), Integer.valueOf(2), true);
/*  230:     */     }
/*  231:     */   }
/*  232:     */   
/*  233:     */   private void checkSampleData(StatisticalSummary stat)
/*  234:     */     throws NullArgumentException, NumberIsTooSmallException
/*  235:     */   {
/*  236:1158 */     if (stat == null) {
/*  237:1159 */       throw new NullArgumentException();
/*  238:     */     }
/*  239:1161 */     if (stat.getN() < 2L) {
/*  240:1162 */       throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DATA_FOR_T_STATISTIC, Long.valueOf(stat.getN()), Integer.valueOf(2), true);
/*  241:     */     }
/*  242:     */   }
/*  243:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.inference.TTest
 * JD-Core Version:    0.7.0.1
 */
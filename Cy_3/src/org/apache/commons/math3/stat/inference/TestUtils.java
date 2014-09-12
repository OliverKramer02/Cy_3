/*   1:    */ package org.apache.commons.math3.stat.inference;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   7:    */ import org.apache.commons.math3.exception.NoDataException;
/*   8:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   9:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*  10:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  11:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  12:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  13:    */ import org.apache.commons.math3.exception.ZeroException;
/*  14:    */ import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
/*  15:    */ 
/*  16:    */ public class TestUtils
/*  17:    */ {
/*  18: 42 */   private static final TTest T_TEST = new TTest();
/*  19: 45 */   private static final ChiSquareTest CHI_SQUARE_TEST = new ChiSquareTest();
/*  20: 48 */   private static final OneWayAnova ONE_WAY_ANANOVA = new OneWayAnova();
/*  21:    */   
/*  22:    */   public static double homoscedasticT(double[] sample1, double[] sample2)
/*  23:    */     throws NullArgumentException, NumberIsTooSmallException
/*  24:    */   {
/*  25: 64 */     return T_TEST.homoscedasticT(sample1, sample2);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static double homoscedasticT(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/*  29:    */     throws NullArgumentException, NumberIsTooSmallException
/*  30:    */   {
/*  31: 73 */     return T_TEST.homoscedasticT(sampleStats1, sampleStats2);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static boolean homoscedasticTTest(double[] sample1, double[] sample2, double alpha)
/*  35:    */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*  36:    */   {
/*  37: 83 */     return T_TEST.homoscedasticTTest(sample1, sample2, alpha);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static double homoscedasticTTest(double[] sample1, double[] sample2)
/*  41:    */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*  42:    */   {
/*  43: 91 */     return T_TEST.homoscedasticTTest(sample1, sample2);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static double homoscedasticTTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/*  47:    */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/*  48:    */   {
/*  49:100 */     return T_TEST.homoscedasticTTest(sampleStats1, sampleStats2);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static double pairedT(double[] sample1, double[] sample2)
/*  53:    */     throws NullArgumentException, NoDataException, DimensionMismatchException, NumberIsTooSmallException
/*  54:    */   {
/*  55:109 */     return T_TEST.pairedT(sample1, sample2);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static boolean pairedTTest(double[] sample1, double[] sample2, double alpha)
/*  59:    */     throws NullArgumentException, NoDataException, DimensionMismatchException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*  60:    */   {
/*  61:119 */     return T_TEST.pairedTTest(sample1, sample2, alpha);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static double pairedTTest(double[] sample1, double[] sample2)
/*  65:    */     throws NullArgumentException, NoDataException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException
/*  66:    */   {
/*  67:128 */     return T_TEST.pairedTTest(sample1, sample2);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static double t(double mu, double[] observed)
/*  71:    */     throws NullArgumentException, NumberIsTooSmallException
/*  72:    */   {
/*  73:136 */     return T_TEST.t(mu, observed);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static double t(double mu, StatisticalSummary sampleStats)
/*  77:    */     throws NullArgumentException, NumberIsTooSmallException
/*  78:    */   {
/*  79:144 */     return T_TEST.t(mu, sampleStats);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static double t(double[] sample1, double[] sample2)
/*  83:    */     throws NullArgumentException, NumberIsTooSmallException
/*  84:    */   {
/*  85:152 */     return T_TEST.t(sample1, sample2);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static double t(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/*  89:    */     throws NullArgumentException, NumberIsTooSmallException
/*  90:    */   {
/*  91:161 */     return T_TEST.t(sampleStats1, sampleStats2);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static boolean tTest(double mu, double[] sample, double alpha)
/*  95:    */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/*  96:    */   {
/*  97:170 */     return T_TEST.tTest(mu, sample, alpha);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static double tTest(double mu, double[] sample)
/* 101:    */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/* 102:    */   {
/* 103:179 */     return T_TEST.tTest(mu, sample);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static boolean tTest(double mu, StatisticalSummary sampleStats, double alpha)
/* 107:    */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/* 108:    */   {
/* 109:189 */     return T_TEST.tTest(mu, sampleStats, alpha);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static double tTest(double mu, StatisticalSummary sampleStats)
/* 113:    */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/* 114:    */   {
/* 115:198 */     return T_TEST.tTest(mu, sampleStats);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public static boolean tTest(double[] sample1, double[] sample2, double alpha)
/* 119:    */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/* 120:    */   {
/* 121:208 */     return T_TEST.tTest(sample1, sample2, alpha);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static double tTest(double[] sample1, double[] sample2)
/* 125:    */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/* 126:    */   {
/* 127:217 */     return T_TEST.tTest(sample1, sample2);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static boolean tTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2, double alpha)
/* 131:    */     throws NullArgumentException, NumberIsTooSmallException, OutOfRangeException, MaxCountExceededException
/* 132:    */   {
/* 133:228 */     return T_TEST.tTest(sampleStats1, sampleStats2, alpha);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public static double tTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
/* 137:    */     throws NullArgumentException, NumberIsTooSmallException, MaxCountExceededException
/* 138:    */   {
/* 139:238 */     return T_TEST.tTest(sampleStats1, sampleStats2);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public static double chiSquare(double[] expected, long[] observed)
/* 143:    */     throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException
/* 144:    */   {
/* 145:247 */     return CHI_SQUARE_TEST.chiSquare(expected, observed);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public static double chiSquare(long[][] counts)
/* 149:    */     throws NullArgumentException, NotPositiveException, DimensionMismatchException
/* 150:    */   {
/* 151:256 */     return CHI_SQUARE_TEST.chiSquare(counts);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static boolean chiSquareTest(double[] expected, long[] observed, double alpha)
/* 155:    */     throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, OutOfRangeException, MaxCountExceededException
/* 156:    */   {
/* 157:266 */     return CHI_SQUARE_TEST.chiSquareTest(expected, observed, alpha);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public static double chiSquareTest(double[] expected, long[] observed)
/* 161:    */     throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, MaxCountExceededException
/* 162:    */   {
/* 163:275 */     return CHI_SQUARE_TEST.chiSquareTest(expected, observed);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static boolean chiSquareTest(long[][] counts, double alpha)
/* 167:    */     throws NullArgumentException, DimensionMismatchException, NotPositiveException, OutOfRangeException, MaxCountExceededException
/* 168:    */   {
/* 169:284 */     return CHI_SQUARE_TEST.chiSquareTest(counts, alpha);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public static double chiSquareTest(long[][] counts)
/* 173:    */     throws NullArgumentException, DimensionMismatchException, NotPositiveException, MaxCountExceededException
/* 174:    */   {
/* 175:293 */     return CHI_SQUARE_TEST.chiSquareTest(counts);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public static double chiSquareDataSetsComparison(long[] observed1, long[] observed2)
/* 179:    */     throws DimensionMismatchException, NotPositiveException, ZeroException
/* 180:    */   {
/* 181:304 */     return CHI_SQUARE_TEST.chiSquareDataSetsComparison(observed1, observed2);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static double chiSquareTestDataSetsComparison(long[] observed1, long[] observed2)
/* 185:    */     throws DimensionMismatchException, NotPositiveException, ZeroException, MaxCountExceededException
/* 186:    */   {
/* 187:316 */     return CHI_SQUARE_TEST.chiSquareTestDataSetsComparison(observed1, observed2);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public static boolean chiSquareTestDataSetsComparison(long[] observed1, long[] observed2, double alpha)
/* 191:    */     throws DimensionMismatchException, NotPositiveException, ZeroException, OutOfRangeException, MaxCountExceededException
/* 192:    */   {
/* 193:329 */     return CHI_SQUARE_TEST.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public static double oneWayAnovaFValue(Collection<double[]> categoryData)
/* 197:    */     throws NullArgumentException, DimensionMismatchException
/* 198:    */   {
/* 199:339 */     return ONE_WAY_ANANOVA.anovaFValue(categoryData);
/* 200:    */   }
/* 201:    */   
/* 202:    */   public static double oneWayAnovaPValue(Collection<double[]> categoryData)
/* 203:    */     throws NullArgumentException, DimensionMismatchException, ConvergenceException, MaxCountExceededException
/* 204:    */   {
/* 205:350 */     return ONE_WAY_ANANOVA.anovaPValue(categoryData);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public static boolean oneWayAnovaTest(Collection<double[]> categoryData, double alpha)
/* 209:    */     throws NullArgumentException, DimensionMismatchException, OutOfRangeException, ConvergenceException, MaxCountExceededException
/* 210:    */   {
/* 211:362 */     return ONE_WAY_ANANOVA.anovaTest(categoryData, alpha);
/* 212:    */   }
/* 213:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.inference.TestUtils
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.stat.inference;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.distribution.ChiSquaredDistribution;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   6:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   7:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  10:    */ import org.apache.commons.math3.exception.ZeroException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ import org.apache.commons.math3.util.FastMath;
/*  13:    */ import org.apache.commons.math3.util.MathUtils;
/*  14:    */ 
/*  15:    */ public class ChiSquareTest
/*  16:    */ {
/*  17:    */   public double chiSquare(double[] expected, long[] observed)
/*  18:    */     throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException
/*  19:    */   {
/*  20: 83 */     if (expected.length < 2) {
/*  21: 84 */       throw new DimensionMismatchException(expected.length, 2);
/*  22:    */     }
/*  23: 86 */     if (expected.length != observed.length) {
/*  24: 87 */       throw new DimensionMismatchException(expected.length, observed.length);
/*  25:    */     }
/*  26: 89 */     checkPositive(expected);
/*  27: 90 */     checkNonNegative(observed);
/*  28:    */     
/*  29: 92 */     double sumExpected = 0.0D;
/*  30: 93 */     double sumObserved = 0.0D;
/*  31: 94 */     for (int i = 0; i < observed.length; i++)
/*  32:    */     {
/*  33: 95 */       sumExpected += expected[i];
/*  34: 96 */       sumObserved += observed[i];
/*  35:    */     }
/*  36: 98 */     double ratio = 1.0D;
/*  37: 99 */     boolean rescale = false;
/*  38:100 */     if (FastMath.abs(sumExpected - sumObserved) > 1.E-005D)
/*  39:    */     {
/*  40:101 */       ratio = sumObserved / sumExpected;
/*  41:102 */       rescale = true;
/*  42:    */     }
/*  43:104 */     double sumSq = 0.0D;
/*  44:105 */     for (int i = 0; i < observed.length; i++) {
/*  45:106 */       if (rescale)
/*  46:    */       {
/*  47:107 */         double dev = observed[i] - ratio * expected[i];
/*  48:108 */         sumSq += dev * dev / (ratio * expected[i]);
/*  49:    */       }
/*  50:    */       else
/*  51:    */       {
/*  52:110 */         double dev = observed[i] - expected[i];
/*  53:111 */         sumSq += dev * dev / expected[i];
/*  54:    */       }
/*  55:    */     }
/*  56:114 */     return sumSq;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double chiSquareTest(double[] expected, long[] observed)
/*  60:    */     throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, MaxCountExceededException
/*  61:    */   {
/*  62:158 */     ChiSquaredDistribution distribution = new ChiSquaredDistribution(expected.length - 1.0D);
/*  63:    */     
/*  64:160 */     return 1.0D - distribution.cumulativeProbability(chiSquare(expected, observed));
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean chiSquareTest(double[] expected, long[] observed, double alpha)
/*  68:    */     throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, OutOfRangeException, MaxCountExceededException
/*  69:    */   {
/*  70:209 */     if ((alpha <= 0.0D) || (alpha > 0.5D)) {
/*  71:210 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5D));
/*  72:    */     }
/*  73:213 */     return chiSquareTest(expected, observed) < alpha;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public double chiSquare(long[][] counts)
/*  77:    */     throws NullArgumentException, NotPositiveException, DimensionMismatchException
/*  78:    */   {
/*  79:249 */     checkArray(counts);
/*  80:250 */     int nRows = counts.length;
/*  81:251 */     int nCols = counts[0].length;
/*  82:    */     
/*  83:    */ 
/*  84:254 */     double[] rowSum = new double[nRows];
/*  85:255 */     double[] colSum = new double[nCols];
/*  86:256 */     double total = 0.0D;
/*  87:257 */     for (int row = 0; row < nRows; row++) {
/*  88:258 */       for (int col = 0; col < nCols; col++)
/*  89:    */       {
/*  90:259 */         rowSum[row] += counts[row][col];
/*  91:260 */         colSum[col] += counts[row][col];
/*  92:261 */         total += counts[row][col];
/*  93:    */       }
/*  94:    */     }
/*  95:266 */     double sumSq = 0.0D;
/*  96:267 */     double expected = 0.0D;
/*  97:268 */     for (int row = 0; row < nRows; row++) {
/*  98:269 */       for (int col = 0; col < nCols; col++)
/*  99:    */       {
/* 100:270 */         expected = rowSum[row] * colSum[col] / total;
/* 101:271 */         sumSq += (counts[row][col] - expected) * (counts[row][col] - expected) / expected;
/* 102:    */       }
/* 103:    */     }
/* 104:275 */     return sumSq;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public double chiSquareTest(long[][] counts)
/* 108:    */     throws NullArgumentException, DimensionMismatchException, NotPositiveException, MaxCountExceededException
/* 109:    */   {
/* 110:314 */     checkArray(counts);
/* 111:315 */     double df = (counts.length - 1.0D) * (counts[0].length - 1.0D);
/* 112:    */     
/* 113:317 */     ChiSquaredDistribution distribution = new ChiSquaredDistribution(df);
/* 114:318 */     return 1.0D - distribution.cumulativeProbability(chiSquare(counts));
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean chiSquareTest(long[][] counts, double alpha)
/* 118:    */     throws NullArgumentException, DimensionMismatchException, NotPositiveException, OutOfRangeException, MaxCountExceededException
/* 119:    */   {
/* 120:364 */     if ((alpha <= 0.0D) || (alpha > 0.5D)) {
/* 121:365 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5D));
/* 122:    */     }
/* 123:368 */     return chiSquareTest(counts) < alpha;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public double chiSquareDataSetsComparison(long[] observed1, long[] observed2)
/* 127:    */     throws DimensionMismatchException, NotPositiveException, ZeroException
/* 128:    */   {
/* 129:415 */     if (observed1.length < 2) {
/* 130:416 */       throw new DimensionMismatchException(observed1.length, 2);
/* 131:    */     }
/* 132:418 */     if (observed1.length != observed2.length) {
/* 133:419 */       throw new DimensionMismatchException(observed1.length, observed2.length);
/* 134:    */     }
/* 135:423 */     checkNonNegative(observed1);
/* 136:424 */     checkNonNegative(observed2);
/* 137:    */     
/* 138:    */ 
/* 139:427 */     long countSum1 = 0L;
/* 140:428 */     long countSum2 = 0L;
/* 141:429 */     boolean unequalCounts = false;
/* 142:430 */     double weight = 0.0D;
/* 143:431 */     for (int i = 0; i < observed1.length; i++)
/* 144:    */     {
/* 145:432 */       countSum1 += observed1[i];
/* 146:433 */       countSum2 += observed2[i];
/* 147:    */     }
/* 148:436 */     if ((countSum1 == 0L) || (countSum2 == 0L)) {
/* 149:437 */       throw new ZeroException();
/* 150:    */     }
/* 151:440 */     unequalCounts = countSum1 != countSum2;
/* 152:441 */     if (unequalCounts) {
/* 153:442 */       weight = FastMath.sqrt(countSum1 / countSum2);
/* 154:    */     }
/* 155:445 */     double sumSq = 0.0D;
/* 156:446 */     double dev = 0.0D;
/* 157:447 */     double obs1 = 0.0D;
/* 158:448 */     double obs2 = 0.0D;
/* 159:449 */     for (int i = 0; i < observed1.length; i++)
/* 160:    */     {
/* 161:450 */       if ((observed1[i] == 0L) && (observed2[i] == 0L)) {
/* 162:451 */         throw new ZeroException(LocalizedFormats.OBSERVED_COUNTS_BOTTH_ZERO_FOR_ENTRY, new Object[] { Integer.valueOf(i) });
/* 163:    */       }
/* 164:453 */       obs1 = observed1[i];
/* 165:454 */       obs2 = observed2[i];
/* 166:455 */       if (unequalCounts) {
/* 167:456 */         dev = obs1 / weight - obs2 * weight;
/* 168:    */       } else {
/* 169:458 */         dev = obs1 - obs2;
/* 170:    */       }
/* 171:460 */       sumSq += dev * dev / (obs1 + obs2);
/* 172:    */     }
/* 173:463 */     return sumSq;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public double chiSquareTestDataSetsComparison(long[] observed1, long[] observed2)
/* 177:    */     throws DimensionMismatchException, NotPositiveException, ZeroException, MaxCountExceededException
/* 178:    */   {
/* 179:513 */     ChiSquaredDistribution distribution = new ChiSquaredDistribution(observed1.length - 1.0D);
/* 180:514 */     return 1.0D - distribution.cumulativeProbability(chiSquareDataSetsComparison(observed1, observed2));
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean chiSquareTestDataSetsComparison(long[] observed1, long[] observed2, double alpha)
/* 184:    */     throws DimensionMismatchException, NotPositiveException, ZeroException, OutOfRangeException, MaxCountExceededException
/* 185:    */   {
/* 186:567 */     if ((alpha <= 0.0D) || (alpha > 0.5D)) {
/* 187:569 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5D));
/* 188:    */     }
/* 189:572 */     return chiSquareTestDataSetsComparison(observed1, observed2) < alpha;
/* 190:    */   }
/* 191:    */   
/* 192:    */   private void checkArray(long[][] in)
/* 193:    */     throws NullArgumentException, DimensionMismatchException, NotPositiveException
/* 194:    */   {
/* 195:589 */     if (in.length < 2) {
/* 196:590 */       throw new DimensionMismatchException(in.length, 2);
/* 197:    */     }
/* 198:593 */     if (in[0].length < 2) {
/* 199:594 */       throw new DimensionMismatchException(in[0].length, 2);
/* 200:    */     }
/* 201:597 */     checkRectangular(in);
/* 202:598 */     checkNonNegative(in);
/* 203:    */   }
/* 204:    */   
/* 205:    */   private void checkRectangular(long[][] in)
/* 206:    */     throws NullArgumentException, DimensionMismatchException
/* 207:    */   {
/* 208:614 */     MathUtils.checkNotNull(in);
/* 209:615 */     for (int i = 1; i < in.length; i++) {
/* 210:616 */       if (in[i].length != in[0].length) {
/* 211:617 */         throw new DimensionMismatchException(LocalizedFormats.DIFFERENT_ROWS_LENGTHS, in[i].length, in[0].length);
/* 212:    */       }
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   private void checkPositive(double[] in)
/* 217:    */     throws NotStrictlyPositiveException
/* 218:    */   {
/* 219:634 */     for (int i = 0; i < in.length; i++) {
/* 220:635 */       if (in[i] <= 0.0D) {
/* 221:636 */         throw new NotStrictlyPositiveException(Double.valueOf(in[i]));
/* 222:    */       }
/* 223:    */     }
/* 224:    */   }
/* 225:    */   
/* 226:    */   private void checkNonNegative(long[] in)
/* 227:    */     throws NotPositiveException
/* 228:    */   {
/* 229:651 */     for (int i = 0; i < in.length; i++) {
/* 230:652 */       if (in[i] < 0L) {
/* 231:653 */         throw new NotPositiveException(Long.valueOf(in[i]));
/* 232:    */       }
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   private void checkNonNegative(long[][] in)
/* 237:    */     throws NotPositiveException
/* 238:    */   {
/* 239:668 */     for (int i = 0; i < in.length; i++) {
/* 240:669 */       for (int j = 0; j < in[i].length; j++) {
/* 241:670 */         if (in[i][j] < 0L) {
/* 242:671 */           throw new NotPositiveException(Long.valueOf(in[i][j]));
/* 243:    */         }
/* 244:    */       }
/* 245:    */     }
/* 246:    */   }
/* 247:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.inference.ChiSquareTest
 * JD-Core Version:    0.7.0.1
 */
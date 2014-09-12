/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   5:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.util.ArithmeticUtils;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class HypergeometricDistribution
/*  11:    */   extends AbstractIntegerDistribution
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = -436928820673516179L;
/*  14:    */   private final int numberOfSuccesses;
/*  15:    */   private final int populationSize;
/*  16:    */   private final int sampleSize;
/*  17: 48 */   private double numericalVariance = (0.0D / 0.0D);
/*  18: 51 */   private boolean numericalVarianceIsCalculated = false;
/*  19:    */   
/*  20:    */   public HypergeometricDistribution(int populationSize, int numberOfSuccesses, int sampleSize)
/*  21:    */     throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException
/*  22:    */   {
/*  23: 67 */     if (populationSize <= 0) {
/*  24: 68 */       throw new NotStrictlyPositiveException(LocalizedFormats.POPULATION_SIZE, Integer.valueOf(populationSize));
/*  25:    */     }
/*  26: 71 */     if (numberOfSuccesses < 0) {
/*  27: 72 */       throw new NotPositiveException(LocalizedFormats.NUMBER_OF_SUCCESSES, Integer.valueOf(numberOfSuccesses));
/*  28:    */     }
/*  29: 75 */     if (sampleSize < 0) {
/*  30: 76 */       throw new NotPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
/*  31:    */     }
/*  32: 80 */     if (numberOfSuccesses > populationSize) {
/*  33: 81 */       throw new NumberIsTooLargeException(LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, Integer.valueOf(numberOfSuccesses), Integer.valueOf(populationSize), true);
/*  34:    */     }
/*  35: 84 */     if (sampleSize > populationSize) {
/*  36: 85 */       throw new NumberIsTooLargeException(LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, Integer.valueOf(sampleSize), Integer.valueOf(populationSize), true);
/*  37:    */     }
/*  38: 89 */     this.numberOfSuccesses = numberOfSuccesses;
/*  39: 90 */     this.populationSize = populationSize;
/*  40: 91 */     this.sampleSize = sampleSize;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double cumulativeProbability(int x)
/*  44:    */   {
/*  45: 98 */     int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
/*  46:    */     double ret;
/*  47:    */    
/*  48: 99 */     if (x < domain[0])
/*  49:    */     {
/*  50:100 */       ret = 0.0D;
/*  51:    */     }
/*  52:    */     else
/*  53:    */     {
/*  54:    */       
/*  55:101 */       if (x >= domain[1]) {
/*  56:102 */         ret = 1.0D;
/*  57:    */       } else {
/*  58:104 */         ret = innerCumulativeProbability(domain[0], x, 1, this.populationSize, this.numberOfSuccesses, this.sampleSize);
/*  59:    */       }
/*  60:    */     }
/*  61:108 */     return ret;
/*  62:    */   }
/*  63:    */   
/*  64:    */   private int[] getDomain(int n, int m, int k)
/*  65:    */   {
/*  66:121 */     return new int[] { getLowerDomain(n, m, k), getUpperDomain(m, k) };
/*  67:    */   }
/*  68:    */   
/*  69:    */   private int getLowerDomain(int n, int m, int k)
/*  70:    */   {
/*  71:134 */     return FastMath.max(0, m - (n - k));
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getNumberOfSuccesses()
/*  75:    */   {
/*  76:143 */     return this.numberOfSuccesses;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int getPopulationSize()
/*  80:    */   {
/*  81:152 */     return this.populationSize;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getSampleSize()
/*  85:    */   {
/*  86:161 */     return this.sampleSize;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private int getUpperDomain(int m, int k)
/*  90:    */   {
/*  91:173 */     return FastMath.min(k, m);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public double probability(int x)
/*  95:    */   {
/*  96:180 */     int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
/*  97:    */     double ret;
/*  98:    */    
/*  99:181 */     if ((x < domain[0]) || (x > domain[1]))
/* 100:    */     {
/* 101:182 */       ret = 0.0D;
/* 102:    */     }
/* 103:    */     else
/* 104:    */     {
/* 105:184 */       double p = this.sampleSize / this.populationSize;
/* 106:185 */       double q = (this.populationSize - this.sampleSize) / this.populationSize;
/* 107:186 */       double p1 = SaddlePointExpansion.logBinomialProbability(x, this.numberOfSuccesses, p, q);
/* 108:    */       
/* 109:188 */       double p2 = SaddlePointExpansion.logBinomialProbability(this.sampleSize - x, this.populationSize - this.numberOfSuccesses, p, q);
/* 110:    */       
/* 111:    */ 
/* 112:191 */       double p3 = SaddlePointExpansion.logBinomialProbability(this.sampleSize, this.populationSize, p, q);
/* 113:    */       
/* 114:193 */       ret = FastMath.exp(p1 + p2 - p3);
/* 115:    */     }
/* 116:196 */     return ret;
/* 117:    */   }
/* 118:    */   
/* 119:    */   private double probability(int n, int m, int k, int x)
/* 120:    */   {
/* 121:210 */     return FastMath.exp(ArithmeticUtils.binomialCoefficientLog(m, x) + ArithmeticUtils.binomialCoefficientLog(n - m, k - x) - ArithmeticUtils.binomialCoefficientLog(n, k));
/* 122:    */   }
/* 123:    */   
/* 124:    */   public double upperCumulativeProbability(int x)
/* 125:    */   {
/* 126:225 */     int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
/* 127:    */     double ret;
/* 128:    */ 
/* 129:226 */     if (x < domain[0])
/* 130:    */     {
/* 131:227 */       ret = 1.0D;
/* 132:    */     }
/* 133:    */     else
/* 134:    */     {
/* 135:    */       
/* 136:228 */       if (x > domain[1]) {
/* 137:229 */         ret = 0.0D;
/* 138:    */       } else {
/* 139:231 */         ret = innerCumulativeProbability(domain[1], x, -1, this.populationSize, this.numberOfSuccesses, this.sampleSize);
/* 140:    */       }
/* 141:    */     }
/* 142:235 */     return ret;
/* 143:    */   }
/* 144:    */   
/* 145:    */   private double innerCumulativeProbability(int x0, int x1, int dx, int n, int m, int k)
/* 146:    */   {
/* 147:256 */     double ret = probability(n, m, k, x0);
/* 148:257 */     while (x0 != x1)
/* 149:    */     {
/* 150:258 */       x0 += dx;
/* 151:259 */       ret += probability(n, m, k, x0);
/* 152:    */     }
/* 153:261 */     return ret;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public double getNumericalMean()
/* 157:    */   {
/* 158:271 */     return getSampleSize() * getNumberOfSuccesses() / getPopulationSize();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public double getNumericalVariance()
/* 162:    */   {
/* 163:282 */     if (!this.numericalVarianceIsCalculated)
/* 164:    */     {
/* 165:283 */       this.numericalVariance = calculateNumericalVariance();
/* 166:284 */       this.numericalVarianceIsCalculated = true;
/* 167:    */     }
/* 168:286 */     return this.numericalVariance;
/* 169:    */   }
/* 170:    */   
/* 171:    */   protected double calculateNumericalVariance()
/* 172:    */   {
/* 173:295 */     double N = getPopulationSize();
/* 174:296 */     double m = getNumberOfSuccesses();
/* 175:297 */     double n = getSampleSize();
/* 176:298 */     return n * m * (N - n) * (N - m) / (N * N * (N - 1.0D));
/* 177:    */   }
/* 178:    */   
/* 179:    */   public int getSupportLowerBound()
/* 180:    */   {
/* 181:311 */     return FastMath.max(0, getSampleSize() + getNumberOfSuccesses() - getPopulationSize());
/* 182:    */   }
/* 183:    */   
/* 184:    */   public int getSupportUpperBound()
/* 185:    */   {
/* 186:324 */     return FastMath.min(getNumberOfSuccesses(), getSampleSize());
/* 187:    */   }
/* 188:    */   
/* 189:    */   public boolean isSupportConnected()
/* 190:    */   {
/* 191:335 */     return true;
/* 192:    */   }
/* 193:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.HypergeometricDistribution
 * JD-Core Version:    0.7.0.1
 */
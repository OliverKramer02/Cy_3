/*   1:    */ package org.apache.commons.math3.stat.inference;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.distribution.NormalDistribution;
/*   4:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   7:    */ import org.apache.commons.math3.exception.NoDataException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*  10:    */ import org.apache.commons.math3.stat.ranking.NaNStrategy;
/*  11:    */ import org.apache.commons.math3.stat.ranking.NaturalRanking;
/*  12:    */ import org.apache.commons.math3.stat.ranking.TiesStrategy;
/*  13:    */ import org.apache.commons.math3.util.FastMath;
/*  14:    */ 
/*  15:    */ public class WilcoxonSignedRankTest
/*  16:    */ {
/*  17:    */   private NaturalRanking naturalRanking;
/*  18:    */   
/*  19:    */   public WilcoxonSignedRankTest()
/*  20:    */   {
/*  21: 47 */     this.naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public WilcoxonSignedRankTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy)
/*  25:    */   {
/*  26: 62 */     this.naturalRanking = new NaturalRanking(nanStrategy, tiesStrategy);
/*  27:    */   }
/*  28:    */   
/*  29:    */   private void ensureDataConformance(double[] x, double[] y)
/*  30:    */     throws NullArgumentException, NoDataException, DimensionMismatchException
/*  31:    */   {
/*  32: 78 */     if ((x == null) || (y == null)) {
/*  33: 80 */       throw new NullArgumentException();
/*  34:    */     }
/*  35: 82 */     if ((x.length == 0) || (y.length == 0)) {
/*  36: 84 */       throw new NoDataException();
/*  37:    */     }
/*  38: 86 */     if (y.length != x.length) {
/*  39: 87 */       throw new DimensionMismatchException(y.length, x.length);
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   private double[] calculateDifferences(double[] x, double[] y)
/*  44:    */   {
/*  45:100 */     double[] z = new double[x.length];
/*  46:102 */     for (int i = 0; i < x.length; i++) {
/*  47:103 */       y[i] -= x[i];
/*  48:    */     }
/*  49:106 */     return z;
/*  50:    */   }
/*  51:    */   
/*  52:    */   private double[] calculateAbsoluteDifferences(double[] z)
/*  53:    */     throws NullArgumentException, NoDataException
/*  54:    */   {
/*  55:120 */     if (z == null) {
/*  56:121 */       throw new NullArgumentException();
/*  57:    */     }
/*  58:124 */     if (z.length == 0) {
/*  59:125 */       throw new NoDataException();
/*  60:    */     }
/*  61:128 */     double[] zAbs = new double[z.length];
/*  62:130 */     for (int i = 0; i < z.length; i++) {
/*  63:131 */       zAbs[i] = FastMath.abs(z[i]);
/*  64:    */     }
/*  65:134 */     return zAbs;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public double wilcoxonSignedRank(double[] x, double[] y)
/*  69:    */     throws NullArgumentException, NoDataException, DimensionMismatchException
/*  70:    */   {
/*  71:175 */     ensureDataConformance(x, y);
/*  72:    */     
/*  73:    */ 
/*  74:    */ 
/*  75:179 */     double[] z = calculateDifferences(x, y);
/*  76:180 */     double[] zAbs = calculateAbsoluteDifferences(z);
/*  77:    */     
/*  78:182 */     double[] ranks = this.naturalRanking.rank(zAbs);
/*  79:    */     
/*  80:184 */     double Wplus = 0.0D;
/*  81:186 */     for (int i = 0; i < z.length; i++) {
/*  82:187 */       if (z[i] > 0.0D) {
/*  83:188 */         Wplus += ranks[i];
/*  84:    */       }
/*  85:    */     }
/*  86:192 */     int N = x.length;
/*  87:193 */     double Wminus = N * (N + 1) / 2.0D - Wplus;
/*  88:    */     
/*  89:195 */     return FastMath.max(Wplus, Wminus);
/*  90:    */   }
/*  91:    */   
/*  92:    */   private double calculateExactPValue(double Wmax, int N)
/*  93:    */   {
/*  94:211 */     int m = 1 << N;
/*  95:    */     
/*  96:213 */     int largerRankSums = 0;
/*  97:215 */     for (int i = 0; i < m; i++)
/*  98:    */     {
/*  99:216 */       int rankSum = 0;
/* 100:219 */       for (int j = 0; j < N; j++) {
/* 101:222 */         if ((i >> j & 0x1) == 1) {
/* 102:223 */           rankSum += j + 1;
/* 103:    */         }
/* 104:    */       }
/* 105:227 */       if (rankSum >= Wmax) {
/* 106:228 */         largerRankSums++;
/* 107:    */       }
/* 108:    */     }
/* 109:236 */     return 2.0D * largerRankSums / m;
/* 110:    */   }
/* 111:    */   
/* 112:    */   private double calculateAsymptoticPValue(double Wmin, int N)
/* 113:    */   {
/* 114:246 */     double ES = N * (N + 1) / 4.0D;
/* 115:    */     
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:251 */     double VarS = ES * ((2 * N + 1) / 6.0D);
/* 120:    */     
/* 121:    */ 
/* 122:254 */     double z = (Wmin - ES - 0.5D) / FastMath.sqrt(VarS);
/* 123:    */     
/* 124:256 */     NormalDistribution standardNormal = new NormalDistribution(0.0D, 1.0D);
/* 125:    */     
/* 126:258 */     return 2.0D * standardNormal.cumulativeProbability(z);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public double wilcoxonSignedRankTest(double[] x, double[] y, boolean exactPValue)
/* 130:    */     throws NullArgumentException, NoDataException, DimensionMismatchException, NumberIsTooLargeException, ConvergenceException, MaxCountExceededException
/* 131:    */   {
/* 132:308 */     ensureDataConformance(x, y);
/* 133:    */     
/* 134:310 */     int N = x.length;
/* 135:311 */     double Wmax = wilcoxonSignedRank(x, y);
/* 136:313 */     if ((exactPValue) && (N > 30)) {
/* 137:314 */       throw new NumberIsTooLargeException(Integer.valueOf(N), Integer.valueOf(30), true);
/* 138:    */     }
/* 139:317 */     if (exactPValue) {
/* 140:318 */       return calculateExactPValue(Wmax, N);
/* 141:    */     }
/* 142:320 */     double Wmin = N * (N + 1) / 2.0D - Wmax;
/* 143:321 */     return calculateAsymptoticPValue(Wmin, N);
/* 144:    */   }
/* 145:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest
 * JD-Core Version:    0.7.0.1
 */
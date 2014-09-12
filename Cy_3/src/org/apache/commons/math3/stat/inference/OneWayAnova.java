/*   1:    */ package org.apache.commons.math3.stat.inference;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import org.apache.commons.math3.distribution.FDistribution;
/*   5:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  10:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  11:    */ import org.apache.commons.math3.stat.descriptive.summary.Sum;
/*  12:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
/*  13:    */ 
/*  14:    */ public class OneWayAnova
/*  15:    */ {
/*  16:    */   public double anovaFValue(Collection<double[]> categoryData)
/*  17:    */     throws NullArgumentException, DimensionMismatchException
/*  18:    */   {
/*  19: 90 */     AnovaStats a = anovaStats(categoryData);
/*  20: 91 */     return a.F;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double anovaPValue(Collection<double[]> categoryData)
/*  24:    */     throws NullArgumentException, DimensionMismatchException, ConvergenceException, MaxCountExceededException
/*  25:    */   {
/*  26:127 */     AnovaStats a = anovaStats(categoryData);
/*  27:128 */     FDistribution fdist = new FDistribution(a.dfbg, a.dfwg);
/*  28:129 */     return 1.0D - fdist.cumulativeProbability(a.F);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean anovaTest(Collection<double[]> categoryData, double alpha)
/*  32:    */     throws NullArgumentException, DimensionMismatchException, OutOfRangeException, ConvergenceException, MaxCountExceededException
/*  33:    */   {
/*  34:172 */     if ((alpha <= 0.0D) || (alpha > 0.5D)) {
/*  35:173 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5D));
/*  36:    */     }
/*  37:177 */     return anovaPValue(categoryData) < alpha;
/*  38:    */   }
/*  39:    */   
/*  40:    */   private AnovaStats anovaStats(Collection<double[]> categoryData)
/*  41:    */     throws NullArgumentException, DimensionMismatchException
/*  42:    */   {
/*  43:195 */     if (categoryData == null) {
/*  44:196 */       throw new NullArgumentException();
/*  45:    */     }
/*  46:200 */     if (categoryData.size() < 2) {
/*  47:201 */       throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_CATEGORIES_REQUIRED, categoryData.size(), 2);
/*  48:    */     }
/*  49:207 */     for (double[] array : categoryData) {
/*  50:208 */       if (array.length <= 1) {
/*  51:209 */         throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_VALUES_IN_CATEGORY_REQUIRED, array.length, 2);
/*  52:    */       }
/*  53:    */     }
/*  54:215 */     int dfwg = 0;
/*  55:216 */     double sswg = 0.0D;
/*  56:217 */     Sum totsum = new Sum();
/*  57:218 */     SumOfSquares totsumsq = new SumOfSquares();
/*  58:219 */     int totnum = 0;
/*  59:221 */     for (double[] data : categoryData)
/*  60:    */     {
/*  61:223 */       Sum sum = new Sum();
/*  62:224 */       SumOfSquares sumsq = new SumOfSquares();
/*  63:225 */       int num = 0;
/*  64:227 */       for (int i = 0; i < data.length; i++)
/*  65:    */       {
/*  66:228 */         double val = data[i];
/*  67:    */         
/*  68:    */ 
/*  69:231 */         num++;
/*  70:232 */         sum.increment(val);
/*  71:233 */         sumsq.increment(val);
/*  72:    */         
/*  73:    */ 
/*  74:236 */         totnum++;
/*  75:237 */         totsum.increment(val);
/*  76:238 */         totsumsq.increment(val);
/*  77:    */       }
/*  78:240 */       dfwg += num - 1;
/*  79:241 */       double ss = sumsq.getResult() - sum.getResult() * sum.getResult() / num;
/*  80:242 */       sswg += ss;
/*  81:    */     }
/*  82:244 */     double sst = totsumsq.getResult() - totsum.getResult() * totsum.getResult() / totnum;
/*  83:    */     
/*  84:246 */     double ssbg = sst - sswg;
/*  85:247 */     int dfbg = categoryData.size() - 1;
/*  86:248 */     double msbg = ssbg / dfbg;
/*  87:249 */     double mswg = sswg / dfwg;
/*  88:250 */     double F = msbg / mswg;
/*  89:    */     
/*  90:252 */     return new AnovaStats(dfbg, dfwg, F);
/*  91:    */   }
/*  92:    */   
/*  93:    */   private static class AnovaStats
/*  94:    */   {
/*  95:    */     private final int dfbg;
/*  96:    */     private final int dfwg;
/*  97:    */     private final double F;
/*  98:    */     
/*  99:    */     private AnovaStats(int dfbg, int dfwg, double F)
/* 100:    */     {
/* 101:277 */       this.dfbg = dfbg;
/* 102:278 */       this.dfwg = dfwg;
/* 103:279 */       this.F = F;
/* 104:    */     }
/* 105:    */   }
/* 106:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.inference.OneWayAnova
 * JD-Core Version:    0.7.0.1
 */
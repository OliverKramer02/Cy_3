/*   1:    */ package org.apache.commons.math3.stat.inference;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.distribution.NormalDistribution;
/*   4:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   5:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   6:    */ import org.apache.commons.math3.exception.NoDataException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.stat.ranking.NaNStrategy;
/*   9:    */ import org.apache.commons.math3.stat.ranking.NaturalRanking;
/*  10:    */ import org.apache.commons.math3.stat.ranking.TiesStrategy;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public class MannWhitneyUTest
/*  14:    */ {
/*  15:    */   private NaturalRanking naturalRanking;
/*  16:    */   
/*  17:    */   public MannWhitneyUTest()
/*  18:    */   {
/*  19: 45 */     this.naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy)
/*  23:    */   {
/*  24: 60 */     this.naturalRanking = new NaturalRanking(nanStrategy, tiesStrategy);
/*  25:    */   }
/*  26:    */   
/*  27:    */   private void ensureDataConformance(double[] x, double[] y)
/*  28:    */     throws NullArgumentException, NoDataException
/*  29:    */   {
/*  30: 74 */     if ((x == null) || (y == null)) {
/*  31: 76 */       throw new NullArgumentException();
/*  32:    */     }
/*  33: 78 */     if ((x.length == 0) || (y.length == 0)) {
/*  34: 80 */       throw new NoDataException();
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   private double[] concatenateSamples(double[] x, double[] y)
/*  39:    */   {
/*  40: 90 */     double[] z = new double[x.length + y.length];
/*  41:    */     
/*  42: 92 */     System.arraycopy(x, 0, z, 0, x.length);
/*  43: 93 */     System.arraycopy(y, 0, z, x.length, y.length);
/*  44:    */     
/*  45: 95 */     return z;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public double mannWhitneyU(double[] x, double[] y)
/*  49:    */     throws NullArgumentException, NoDataException
/*  50:    */   {
/*  51:129 */     ensureDataConformance(x, y);
/*  52:    */     
/*  53:131 */     double[] z = concatenateSamples(x, y);
/*  54:132 */     double[] ranks = this.naturalRanking.rank(z);
/*  55:    */     
/*  56:134 */     double sumRankX = 0.0D;
/*  57:140 */     for (int i = 0; i < x.length; i++) {
/*  58:141 */       sumRankX += ranks[i];
/*  59:    */     }
/*  60:148 */     double U1 = sumRankX - x.length * (x.length + 1) / 2;
/*  61:    */     
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:153 */     double U2 = x.length * y.length - U1;
/*  66:    */     
/*  67:155 */     return FastMath.max(U1, U2);
/*  68:    */   }
/*  69:    */   
/*  70:    */   private double calculateAsymptoticPValue(double Umin, int n1, int n2)
/*  71:    */     throws ConvergenceException, MaxCountExceededException
/*  72:    */   {
/*  73:173 */     int n1n2prod = n1 * n2;
/*  74:    */     
/*  75:    */ 
/*  76:176 */     double EU = n1n2prod / 2.0D;
/*  77:177 */     double VarU = n1n2prod * (n1 + n2 + 1) / 12.0D;
/*  78:    */     
/*  79:179 */     double z = (Umin - EU) / FastMath.sqrt(VarU);
/*  80:    */     
/*  81:181 */     NormalDistribution standardNormal = new NormalDistribution(0.0D, 1.0D);
/*  82:    */     
/*  83:183 */     return 2.0D * standardNormal.cumulativeProbability(z);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public double mannWhitneyUTest(double[] x, double[] y)
/*  87:    */     throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException
/*  88:    */   {
/*  89:222 */     ensureDataConformance(x, y);
/*  90:    */     
/*  91:224 */     double Umax = mannWhitneyU(x, y);
/*  92:    */     
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:229 */     double Umin = x.length * y.length - Umax;
/*  97:    */     
/*  98:231 */     return calculateAsymptoticPValue(Umin, x.length, y.length);
/*  99:    */   }
/* 100:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.inference.MannWhitneyUTest
 * JD-Core Version:    0.7.0.1
 */
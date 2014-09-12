/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   4:    */ 
/*   5:    */ public class SynchronizedMultivariateSummaryStatistics
/*   6:    */   extends MultivariateSummaryStatistics
/*   7:    */ {
/*   8:    */   private static final long serialVersionUID = 7099834153347155363L;
/*   9:    */   
/*  10:    */   public SynchronizedMultivariateSummaryStatistics(int k, boolean isCovarianceBiasCorrected)
/*  11:    */   {
/*  12: 47 */     super(k, isCovarianceBiasCorrected);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public synchronized void addValue(double[] value)
/*  16:    */   {
/*  17: 55 */     super.addValue(value);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public synchronized int getDimension()
/*  21:    */   {
/*  22: 63 */     return super.getDimension();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public synchronized long getN()
/*  26:    */   {
/*  27: 71 */     return super.getN();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public synchronized double[] getSum()
/*  31:    */   {
/*  32: 79 */     return super.getSum();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public synchronized double[] getSumSq()
/*  36:    */   {
/*  37: 87 */     return super.getSumSq();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public synchronized double[] getSumLog()
/*  41:    */   {
/*  42: 95 */     return super.getSumLog();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public synchronized double[] getMean()
/*  46:    */   {
/*  47:103 */     return super.getMean();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public synchronized double[] getStandardDeviation()
/*  51:    */   {
/*  52:111 */     return super.getStandardDeviation();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public synchronized RealMatrix getCovariance()
/*  56:    */   {
/*  57:119 */     return super.getCovariance();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public synchronized double[] getMax()
/*  61:    */   {
/*  62:127 */     return super.getMax();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public synchronized double[] getMin()
/*  66:    */   {
/*  67:135 */     return super.getMin();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public synchronized double[] getGeometricMean()
/*  71:    */   {
/*  72:143 */     return super.getGeometricMean();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public synchronized String toString()
/*  76:    */   {
/*  77:151 */     return super.toString();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public synchronized void clear()
/*  81:    */   {
/*  82:159 */     super.clear();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public synchronized boolean equals(Object object)
/*  86:    */   {
/*  87:167 */     return super.equals(object);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public synchronized int hashCode()
/*  91:    */   {
/*  92:175 */     return super.hashCode();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public synchronized StorelessUnivariateStatistic[] getSumImpl()
/*  96:    */   {
/*  97:183 */     return super.getSumImpl();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public synchronized void setSumImpl(StorelessUnivariateStatistic[] sumImpl)
/* 101:    */   {
/* 102:191 */     super.setSumImpl(sumImpl);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public synchronized StorelessUnivariateStatistic[] getSumsqImpl()
/* 106:    */   {
/* 107:199 */     return super.getSumsqImpl();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public synchronized void setSumsqImpl(StorelessUnivariateStatistic[] sumsqImpl)
/* 111:    */   {
/* 112:207 */     super.setSumsqImpl(sumsqImpl);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public synchronized StorelessUnivariateStatistic[] getMinImpl()
/* 116:    */   {
/* 117:215 */     return super.getMinImpl();
/* 118:    */   }
/* 119:    */   
/* 120:    */   public synchronized void setMinImpl(StorelessUnivariateStatistic[] minImpl)
/* 121:    */   {
/* 122:223 */     super.setMinImpl(minImpl);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public synchronized StorelessUnivariateStatistic[] getMaxImpl()
/* 126:    */   {
/* 127:231 */     return super.getMaxImpl();
/* 128:    */   }
/* 129:    */   
/* 130:    */   public synchronized void setMaxImpl(StorelessUnivariateStatistic[] maxImpl)
/* 131:    */   {
/* 132:239 */     super.setMaxImpl(maxImpl);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public synchronized StorelessUnivariateStatistic[] getSumLogImpl()
/* 136:    */   {
/* 137:247 */     return super.getSumLogImpl();
/* 138:    */   }
/* 139:    */   
/* 140:    */   public synchronized void setSumLogImpl(StorelessUnivariateStatistic[] sumLogImpl)
/* 141:    */   {
/* 142:255 */     super.setSumLogImpl(sumLogImpl);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public synchronized StorelessUnivariateStatistic[] getGeoMeanImpl()
/* 146:    */   {
/* 147:263 */     return super.getGeoMeanImpl();
/* 148:    */   }
/* 149:    */   
/* 150:    */   public synchronized void setGeoMeanImpl(StorelessUnivariateStatistic[] geoMeanImpl)
/* 151:    */   {
/* 152:271 */     super.setGeoMeanImpl(geoMeanImpl);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public synchronized StorelessUnivariateStatistic[] getMeanImpl()
/* 156:    */   {
/* 157:279 */     return super.getMeanImpl();
/* 158:    */   }
/* 159:    */   
/* 160:    */   public synchronized void setMeanImpl(StorelessUnivariateStatistic[] meanImpl)
/* 161:    */   {
/* 162:287 */     super.setMeanImpl(meanImpl);
/* 163:    */   }
/* 164:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.SynchronizedMultivariateSummaryStatistics
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   4:    */ import org.apache.commons.math3.util.MathUtils;
/*   5:    */ 
/*   6:    */ public class SynchronizedSummaryStatistics
/*   7:    */   extends SummaryStatistics
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 1909861009042253704L;
/*  10:    */   
/*  11:    */   public SynchronizedSummaryStatistics() {}
/*  12:    */   
/*  13:    */   public SynchronizedSummaryStatistics(SynchronizedSummaryStatistics original)
/*  14:    */   {
/*  15: 53 */     copy(original, this);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public synchronized StatisticalSummary getSummary()
/*  19:    */   {
/*  20: 61 */     return super.getSummary();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public synchronized void addValue(double value)
/*  24:    */   {
/*  25: 69 */     super.addValue(value);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public synchronized long getN()
/*  29:    */   {
/*  30: 77 */     return super.getN();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public synchronized double getSum()
/*  34:    */   {
/*  35: 85 */     return super.getSum();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public synchronized double getSumsq()
/*  39:    */   {
/*  40: 93 */     return super.getSumsq();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public synchronized double getMean()
/*  44:    */   {
/*  45:101 */     return super.getMean();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public synchronized double getStandardDeviation()
/*  49:    */   {
/*  50:109 */     return super.getStandardDeviation();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public synchronized double getVariance()
/*  54:    */   {
/*  55:117 */     return super.getVariance();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public synchronized double getPopulationVariance()
/*  59:    */   {
/*  60:125 */     return super.getPopulationVariance();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public synchronized double getMax()
/*  64:    */   {
/*  65:133 */     return super.getMax();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public synchronized double getMin()
/*  69:    */   {
/*  70:141 */     return super.getMin();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public synchronized double getGeometricMean()
/*  74:    */   {
/*  75:149 */     return super.getGeometricMean();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public synchronized String toString()
/*  79:    */   {
/*  80:157 */     return super.toString();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public synchronized void clear()
/*  84:    */   {
/*  85:165 */     super.clear();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public synchronized boolean equals(Object object)
/*  89:    */   {
/*  90:173 */     return super.equals(object);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public synchronized int hashCode()
/*  94:    */   {
/*  95:181 */     return super.hashCode();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public synchronized StorelessUnivariateStatistic getSumImpl()
/*  99:    */   {
/* 100:189 */     return super.getSumImpl();
/* 101:    */   }
/* 102:    */   
/* 103:    */   public synchronized void setSumImpl(StorelessUnivariateStatistic sumImpl)
/* 104:    */   {
/* 105:197 */     super.setSumImpl(sumImpl);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public synchronized StorelessUnivariateStatistic getSumsqImpl()
/* 109:    */   {
/* 110:205 */     return super.getSumsqImpl();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public synchronized void setSumsqImpl(StorelessUnivariateStatistic sumsqImpl)
/* 114:    */   {
/* 115:213 */     super.setSumsqImpl(sumsqImpl);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public synchronized StorelessUnivariateStatistic getMinImpl()
/* 119:    */   {
/* 120:221 */     return super.getMinImpl();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public synchronized void setMinImpl(StorelessUnivariateStatistic minImpl)
/* 124:    */   {
/* 125:229 */     super.setMinImpl(minImpl);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public synchronized StorelessUnivariateStatistic getMaxImpl()
/* 129:    */   {
/* 130:237 */     return super.getMaxImpl();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public synchronized void setMaxImpl(StorelessUnivariateStatistic maxImpl)
/* 134:    */   {
/* 135:245 */     super.setMaxImpl(maxImpl);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public synchronized StorelessUnivariateStatistic getSumLogImpl()
/* 139:    */   {
/* 140:253 */     return super.getSumLogImpl();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public synchronized void setSumLogImpl(StorelessUnivariateStatistic sumLogImpl)
/* 144:    */   {
/* 145:261 */     super.setSumLogImpl(sumLogImpl);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public synchronized StorelessUnivariateStatistic getGeoMeanImpl()
/* 149:    */   {
/* 150:269 */     return super.getGeoMeanImpl();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public synchronized void setGeoMeanImpl(StorelessUnivariateStatistic geoMeanImpl)
/* 154:    */   {
/* 155:277 */     super.setGeoMeanImpl(geoMeanImpl);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public synchronized StorelessUnivariateStatistic getMeanImpl()
/* 159:    */   {
/* 160:285 */     return super.getMeanImpl();
/* 161:    */   }
/* 162:    */   
/* 163:    */   public synchronized void setMeanImpl(StorelessUnivariateStatistic meanImpl)
/* 164:    */   {
/* 165:293 */     super.setMeanImpl(meanImpl);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public synchronized StorelessUnivariateStatistic getVarianceImpl()
/* 169:    */   {
/* 170:301 */     return super.getVarianceImpl();
/* 171:    */   }
/* 172:    */   
/* 173:    */   public synchronized void setVarianceImpl(StorelessUnivariateStatistic varianceImpl)
/* 174:    */   {
/* 175:309 */     super.setVarianceImpl(varianceImpl);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public synchronized SynchronizedSummaryStatistics copy()
/* 179:    */   {
/* 180:320 */     SynchronizedSummaryStatistics result = new SynchronizedSummaryStatistics();
/* 181:    */     
/* 182:322 */     copy(this, result);
/* 183:323 */     return result;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public static void copy(SynchronizedSummaryStatistics source, SynchronizedSummaryStatistics dest)
/* 187:    */     throws NullArgumentException
/* 188:    */   {
/* 189:338 */     MathUtils.checkNotNull(source);
/* 190:339 */     MathUtils.checkNotNull(dest);
/* 191:340 */     synchronized (source)
/* 192:    */     {
/* 193:341 */       synchronized (dest)
/* 194:    */       {
/* 195:342 */         SummaryStatistics.copy(source, dest);
/* 196:    */       }
/* 197:    */     }
/* 198:    */   }
/* 199:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics
 * JD-Core Version:    0.7.0.1
 */
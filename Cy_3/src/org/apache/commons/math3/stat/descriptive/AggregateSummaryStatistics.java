/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Iterator;
/*   6:    */ 
/*   7:    */ public class AggregateSummaryStatistics
/*   8:    */   implements StatisticalSummary, Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = -8207112444016386906L;
/*  11:    */   private final SummaryStatistics statisticsPrototype;
/*  12:    */   private final SummaryStatistics statistics;
/*  13:    */   
/*  14:    */   public AggregateSummaryStatistics()
/*  15:    */   {
/*  16: 76 */     this(new SummaryStatistics());
/*  17:    */   }
/*  18:    */   
/*  19:    */   public AggregateSummaryStatistics(SummaryStatistics prototypeStatistics)
/*  20:    */   {
/*  21: 96 */     this(prototypeStatistics, prototypeStatistics == null ? null : new SummaryStatistics(prototypeStatistics));
/*  22:    */   }
/*  23:    */   
/*  24:    */   public AggregateSummaryStatistics(SummaryStatistics prototypeStatistics, SummaryStatistics initialStatistics)
/*  25:    */   {
/*  26:122 */     this.statisticsPrototype = (prototypeStatistics == null ? new SummaryStatistics() : prototypeStatistics);
/*  27:    */     
/*  28:124 */     this.statistics = (initialStatistics == null ? new SummaryStatistics() : initialStatistics);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public double getMax()
/*  32:    */   {
/*  33:135 */     synchronized (this.statistics)
/*  34:    */     {
/*  35:136 */       return this.statistics.getMax();
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public double getMean()
/*  40:    */   {
/*  41:146 */     synchronized (this.statistics)
/*  42:    */     {
/*  43:147 */       return this.statistics.getMean();
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getMin()
/*  48:    */   {
/*  49:158 */     synchronized (this.statistics)
/*  50:    */     {
/*  51:159 */       return this.statistics.getMin();
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public long getN()
/*  56:    */   {
/*  57:169 */     synchronized (this.statistics)
/*  58:    */     {
/*  59:170 */       return this.statistics.getN();
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public double getStandardDeviation()
/*  64:    */   {
/*  65:181 */     synchronized (this.statistics)
/*  66:    */     {
/*  67:182 */       return this.statistics.getStandardDeviation();
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public double getSum()
/*  72:    */   {
/*  73:192 */     synchronized (this.statistics)
/*  74:    */     {
/*  75:193 */       return this.statistics.getSum();
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public double getVariance()
/*  80:    */   {
/*  81:204 */     synchronized (this.statistics)
/*  82:    */     {
/*  83:205 */       return this.statistics.getVariance();
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public double getSumOfLogs()
/*  88:    */   {
/*  89:216 */     synchronized (this.statistics)
/*  90:    */     {
/*  91:217 */       return this.statistics.getSumOfLogs();
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public double getGeometricMean()
/*  96:    */   {
/*  97:228 */     synchronized (this.statistics)
/*  98:    */     {
/*  99:229 */       return this.statistics.getGeometricMean();
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double getSumsq()
/* 104:    */   {
/* 105:240 */     synchronized (this.statistics)
/* 106:    */     {
/* 107:241 */       return this.statistics.getSumsq();
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public double getSecondMoment()
/* 112:    */   {
/* 113:254 */     synchronized (this.statistics)
/* 114:    */     {
/* 115:255 */       return this.statistics.getSecondMoment();
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public StatisticalSummary getSummary()
/* 120:    */   {
/* 121:266 */     synchronized (this.statistics)
/* 122:    */     {
/* 123:267 */       return new StatisticalSummaryValues(getMean(), getVariance(), getN(), getMax(), getMin(), getSum());
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   public SummaryStatistics createContributingStatistics()
/* 128:    */   {
/* 129:281 */     SummaryStatistics contributingStatistics = new AggregatingSummaryStatistics(this.statistics);
/* 130:    */     
/* 131:    */ 
/* 132:284 */     SummaryStatistics.copy(this.statisticsPrototype, contributingStatistics);
/* 133:    */     
/* 134:286 */     return contributingStatistics;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public static StatisticalSummaryValues aggregate(Collection<SummaryStatistics> statistics)
/* 138:    */   {
/* 139:302 */     if (statistics == null) {
/* 140:303 */       return null;
/* 141:    */     }
/* 142:305 */     Iterator<SummaryStatistics> iterator = statistics.iterator();
/* 143:306 */     if (!iterator.hasNext()) {
/* 144:307 */       return null;
/* 145:    */     }
/* 146:309 */     SummaryStatistics current = (SummaryStatistics)iterator.next();
/* 147:310 */     long n = current.getN();
/* 148:311 */     double min = current.getMin();
/* 149:312 */     double sum = current.getSum();
/* 150:313 */     double max = current.getMax();
/* 151:314 */     double m2 = current.getSecondMoment();
/* 152:315 */     double mean = current.getMean();
/* 153:316 */     while (iterator.hasNext())
/* 154:    */     {
/* 155:317 */       current = (SummaryStatistics)iterator.next();
/* 156:318 */       if ((current.getMin() < min) || (Double.isNaN(min))) {
/* 157:319 */         min = current.getMin();
/* 158:    */       }
/* 159:321 */       if ((current.getMax() > max) || (Double.isNaN(max))) {
/* 160:322 */         max = current.getMax();
/* 161:    */       }
/* 162:324 */       sum += current.getSum();
/* 163:325 */       double oldN = n;
/* 164:326 */       double curN = current.getN();
/* 165:327 */       n = (long) (n + curN);
/* 166:328 */       double meanDiff = current.getMean() - mean;
/* 167:329 */       mean = sum / n;
/* 168:330 */       m2 = m2 + current.getSecondMoment() + meanDiff * meanDiff * oldN * curN / n;
/* 169:    */     }
/* 170:    */     double variance;
/* 171:    */     
/* 172:333 */     if (n == 0L)
/* 173:    */     {
/* 174:334 */       variance = (0.0D / 0.0D);
/* 175:    */     }
/* 176:    */     else
/* 177:    */     {
/* 178:    */      
/* 179:335 */       if (n == 1L) {
/* 180:336 */         variance = 0.0D;
/* 181:    */       } else {
/* 182:338 */         variance = m2 / (n - 1L);
/* 183:    */       }
/* 184:    */     }
/* 185:340 */     return new StatisticalSummaryValues(mean, variance, n, max, min, sum);
/* 186:    */   }
/* 187:    */   
/* 188:    */   private static class AggregatingSummaryStatistics
/* 189:    */     extends SummaryStatistics
/* 190:    */   {
/* 191:    */     private static final long serialVersionUID = 1L;
/* 192:    */     private final SummaryStatistics aggregateStatistics;
/* 193:    */     
/* 194:    */     public AggregatingSummaryStatistics(SummaryStatistics aggregateStatistics)
/* 195:    */     {
/* 196:370 */       this.aggregateStatistics = aggregateStatistics;
/* 197:    */     }
/* 198:    */     
/* 199:    */     public void addValue(double value)
/* 200:    */     {
/* 201:381 */       super.addValue(value);
/* 202:382 */       synchronized (this.aggregateStatistics)
/* 203:    */       {
/* 204:383 */         this.aggregateStatistics.addValue(value);
/* 205:    */       }
/* 206:    */     }
/* 207:    */     
/* 208:    */     public boolean equals(Object object)
/* 209:    */     {
/* 210:396 */       if (object == this) {
/* 211:397 */         return true;
/* 212:    */       }
/* 213:399 */       if (!(object instanceof AggregatingSummaryStatistics)) {
/* 214:400 */         return false;
/* 215:    */       }
/* 216:402 */       AggregatingSummaryStatistics stat = (AggregatingSummaryStatistics)object;
/* 217:403 */       return (super.equals(stat)) && (this.aggregateStatistics.equals(stat.aggregateStatistics));
/* 218:    */     }
/* 219:    */     
/* 220:    */     public int hashCode()
/* 221:    */     {
/* 222:413 */       return 123 + super.hashCode() + this.aggregateStatistics.hashCode();
/* 223:    */     }
/* 224:    */   }
/* 225:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics
 * JD-Core Version:    0.7.0.1
 */
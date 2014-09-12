/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ import org.apache.commons.math3.util.MathUtils;
/*   6:    */ import org.apache.commons.math3.util.Precision;
/*   7:    */ 
/*   8:    */ public class StatisticalSummaryValues
/*   9:    */   implements Serializable, StatisticalSummary
/*  10:    */ {
/*  11:    */   private static final long serialVersionUID = -5108854841843722536L;
/*  12:    */   private final double mean;
/*  13:    */   private final double variance;
/*  14:    */   private final long n;
/*  15:    */   private final double max;
/*  16:    */   private final double min;
/*  17:    */   private final double sum;
/*  18:    */   
/*  19:    */   public StatisticalSummaryValues(double mean, double variance, long n, double max, double min, double sum)
/*  20:    */   {
/*  21: 67 */     this.mean = mean;
/*  22: 68 */     this.variance = variance;
/*  23: 69 */     this.n = n;
/*  24: 70 */     this.max = max;
/*  25: 71 */     this.min = min;
/*  26: 72 */     this.sum = sum;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public double getMax()
/*  30:    */   {
/*  31: 79 */     return this.max;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getMean()
/*  35:    */   {
/*  36: 86 */     return this.mean;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public double getMin()
/*  40:    */   {
/*  41: 93 */     return this.min;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public long getN()
/*  45:    */   {
/*  46:100 */     return this.n;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double getSum()
/*  50:    */   {
/*  51:107 */     return this.sum;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public double getStandardDeviation()
/*  55:    */   {
/*  56:114 */     return FastMath.sqrt(this.variance);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double getVariance()
/*  60:    */   {
/*  61:121 */     return this.variance;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean equals(Object object)
/*  65:    */   {
/*  66:134 */     if (object == this) {
/*  67:135 */       return true;
/*  68:    */     }
/*  69:137 */     if (!(object instanceof StatisticalSummaryValues)) {
/*  70:138 */       return false;
/*  71:    */     }
/*  72:140 */     StatisticalSummaryValues stat = (StatisticalSummaryValues)object;
/*  73:141 */     return (Precision.equalsIncludingNaN(stat.getMax(), getMax())) && (Precision.equalsIncludingNaN(stat.getMean(), getMean())) && (Precision.equalsIncludingNaN(stat.getMin(), getMin())) && (Precision.equalsIncludingNaN((float)stat.getN(), (float)getN())) && (Precision.equalsIncludingNaN(stat.getSum(), getSum())) && (Precision.equalsIncludingNaN(stat.getVariance(), getVariance()));
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int hashCode()
/*  77:    */   {
/*  78:156 */     int result = 31 + MathUtils.hash(getMax());
/*  79:157 */     result = result * 31 + MathUtils.hash(getMean());
/*  80:158 */     result = result * 31 + MathUtils.hash(getMin());
/*  81:159 */     result = result * 31 + MathUtils.hash(getN());
/*  82:160 */     result = result * 31 + MathUtils.hash(getSum());
/*  83:161 */     result = result * 31 + MathUtils.hash(getVariance());
/*  84:162 */     return result;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String toString()
/*  88:    */   {
/*  89:173 */     StringBuffer outBuffer = new StringBuffer();
/*  90:174 */     String endl = "\n";
/*  91:175 */     outBuffer.append("StatisticalSummaryValues:").append(endl);
/*  92:176 */     outBuffer.append("n: ").append(getN()).append(endl);
/*  93:177 */     outBuffer.append("min: ").append(getMin()).append(endl);
/*  94:178 */     outBuffer.append("max: ").append(getMax()).append(endl);
/*  95:179 */     outBuffer.append("mean: ").append(getMean()).append(endl);
/*  96:180 */     outBuffer.append("std dev: ").append(getStandardDeviation()).append(endl);
/*  97:    */     
/*  98:182 */     outBuffer.append("variance: ").append(getVariance()).append(endl);
/*  99:183 */     outBuffer.append("sum: ").append(getSum()).append(endl);
/* 100:184 */     return outBuffer.toString();
/* 101:    */   }
/* 102:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues
 * JD-Core Version:    0.7.0.1
 */
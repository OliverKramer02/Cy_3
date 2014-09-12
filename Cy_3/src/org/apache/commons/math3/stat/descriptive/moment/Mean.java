/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
/*   7:    */ import org.apache.commons.math3.stat.descriptive.summary.Sum;
/*   8:    */ import org.apache.commons.math3.util.MathUtils;
/*   9:    */ 
/*  10:    */ public class Mean
/*  11:    */   extends AbstractStorelessUnivariateStatistic
/*  12:    */   implements Serializable, WeightedEvaluation
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = -1296043746617791564L;
/*  15:    */   protected FirstMoment moment;
/*  16:    */   protected boolean incMoment;
/*  17:    */   
/*  18:    */   public Mean()
/*  19:    */   {
/*  20: 80 */     this.incMoment = true;
/*  21: 81 */     this.moment = new FirstMoment();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Mean(FirstMoment m1)
/*  25:    */   {
/*  26: 90 */     this.moment = m1;
/*  27: 91 */     this.incMoment = false;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Mean(Mean original)
/*  31:    */   {
/*  32:101 */     copy(original, this);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void increment(double d)
/*  36:    */   {
/*  37:109 */     if (this.incMoment) {
/*  38:110 */       this.moment.increment(d);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void clear()
/*  43:    */   {
/*  44:119 */     if (this.incMoment) {
/*  45:120 */       this.moment.clear();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double getResult()
/*  50:    */   {
/*  51:129 */     return this.moment.m1;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public long getN()
/*  55:    */   {
/*  56:136 */     return this.moment.getN();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double evaluate(double[] values, int begin, int length)
/*  60:    */   {
/*  61:157 */     if (test(values, begin, length))
/*  62:    */     {
/*  63:158 */       Sum sum = new Sum();
/*  64:159 */       double sampleSize = length;
/*  65:    */       
/*  66:    */ 
/*  67:162 */       double xbar = sum.evaluate(values, begin, length) / sampleSize;
/*  68:    */       
/*  69:    */ 
/*  70:165 */       double correction = 0.0D;
/*  71:166 */       for (int i = begin; i < begin + length; i++) {
/*  72:167 */         correction += values[i] - xbar;
/*  73:    */       }
/*  74:169 */       return xbar + correction / sampleSize;
/*  75:    */     }
/*  76:171 */     return (0.0D / 0.0D);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public double evaluate(double[] values, double[] weights, int begin, int length)
/*  80:    */   {
/*  81:205 */     if (test(values, weights, begin, length))
/*  82:    */     {
/*  83:206 */       Sum sum = new Sum();
/*  84:    */       
/*  85:    */ 
/*  86:209 */       double sumw = sum.evaluate(weights, begin, length);
/*  87:210 */       double xbarw = sum.evaluate(values, weights, begin, length) / sumw;
/*  88:    */       
/*  89:    */ 
/*  90:213 */       double correction = 0.0D;
/*  91:214 */       for (int i = begin; i < begin + length; i++) {
/*  92:215 */         correction += weights[i] * (values[i] - xbarw);
/*  93:    */       }
/*  94:217 */       return xbarw + correction / sumw;
/*  95:    */     }
/*  96:219 */     return (0.0D / 0.0D);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public double evaluate(double[] values, double[] weights)
/* 100:    */   {
/* 101:247 */     return evaluate(values, weights, 0, values.length);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public Mean copy()
/* 105:    */   {
/* 106:255 */     Mean result = new Mean();
/* 107:256 */     copy(this, result);
/* 108:257 */     return result;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static void copy(Mean source, Mean dest)
/* 112:    */     throws NullArgumentException
/* 113:    */   {
/* 114:271 */     MathUtils.checkNotNull(source);
/* 115:272 */     MathUtils.checkNotNull(dest);
/* 116:273 */     dest.setData(source.getDataRef());
/* 117:274 */     dest.incMoment = source.incMoment;
/* 118:275 */     dest.moment = source.moment.copy();
/* 119:    */   }
/* 120:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.Mean
 * JD-Core Version:    0.7.0.1
 */
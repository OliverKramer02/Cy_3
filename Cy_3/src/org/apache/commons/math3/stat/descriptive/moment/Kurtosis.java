/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ import org.apache.commons.math3.util.MathUtils;
/*  10:    */ 
/*  11:    */ public class Kurtosis
/*  12:    */   extends AbstractStorelessUnivariateStatistic
/*  13:    */   implements Serializable
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = 2784465764798260919L;
/*  16:    */   protected FourthMoment moment;
/*  17:    */   protected boolean incMoment;
/*  18:    */   
/*  19:    */   public Kurtosis()
/*  20:    */   {
/*  21: 69 */     this.incMoment = true;
/*  22: 70 */     this.moment = new FourthMoment();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Kurtosis(FourthMoment m4)
/*  26:    */   {
/*  27: 79 */     this.incMoment = false;
/*  28: 80 */     this.moment = m4;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Kurtosis(Kurtosis original)
/*  32:    */   {
/*  33: 90 */     copy(original, this);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void increment(double d)
/*  37:    */   {
/*  38: 98 */     if (this.incMoment) {
/*  39: 99 */       this.moment.increment(d);
/*  40:    */     } else {
/*  41:101 */       throw new MathIllegalStateException(LocalizedFormats.CANNOT_INCREMENT_STATISTIC_CONSTRUCTED_FROM_EXTERNAL_MOMENTS, new Object[0]);
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double getResult()
/*  46:    */   {
/*  47:110 */     double kurtosis = (0.0D / 0.0D);
/*  48:111 */     if (this.moment.getN() > 3L)
/*  49:    */     {
/*  50:112 */       double variance = this.moment.m2 / (this.moment.n - 1L);
/*  51:113 */       if ((this.moment.n <= 3L) || (variance < 1.0E-019D))
/*  52:    */       {
/*  53:114 */         kurtosis = 0.0D;
/*  54:    */       }
/*  55:    */       else
/*  56:    */       {
/*  57:116 */         double n = this.moment.n;
/*  58:117 */         kurtosis = (n * (n + 1.0D) * this.moment.getResult() - 3.0D * this.moment.m2 * this.moment.m2 * (n - 1.0D)) / ((n - 1.0D) * (n - 2.0D) * (n - 3.0D) * variance * variance);
/*  59:    */       }
/*  60:    */     }
/*  61:123 */     return kurtosis;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void clear()
/*  65:    */   {
/*  66:131 */     if (this.incMoment) {
/*  67:132 */       this.moment.clear();
/*  68:    */     } else {
/*  69:134 */       throw new MathIllegalStateException(LocalizedFormats.CANNOT_CLEAR_STATISTIC_CONSTRUCTED_FROM_EXTERNAL_MOMENTS, new Object[0]);
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public long getN()
/*  74:    */   {
/*  75:142 */     return this.moment.getN();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public double evaluate(double[] values, int begin, int length)
/*  79:    */   {
/*  80:166 */     double kurt = (0.0D / 0.0D);
/*  81:168 */     if ((test(values, begin, length)) && (length > 3))
/*  82:    */     {
/*  83:171 */       Variance variance = new Variance();
/*  84:172 */       variance.incrementAll(values, begin, length);
/*  85:173 */       double mean = variance.moment.m1;
/*  86:174 */       double stdDev = FastMath.sqrt(variance.getResult());
/*  87:    */       
/*  88:    */ 
/*  89:    */ 
/*  90:178 */       double accum3 = 0.0D;
/*  91:179 */       for (int i = begin; i < begin + length; i++) {
/*  92:180 */         accum3 += FastMath.pow(values[i] - mean, 4.0D);
/*  93:    */       }
/*  94:182 */       accum3 /= FastMath.pow(stdDev, 4.0D);
/*  95:    */       
/*  96:    */ 
/*  97:185 */       double n0 = length;
/*  98:    */       
/*  99:187 */       double coefficientOne = n0 * (n0 + 1.0D) / ((n0 - 1.0D) * (n0 - 2.0D) * (n0 - 3.0D));
/* 100:    */       
/* 101:189 */       double termTwo = 3.0D * FastMath.pow(n0 - 1.0D, 2.0D) / ((n0 - 2.0D) * (n0 - 3.0D));
/* 102:    */       
/* 103:    */ 
/* 104:    */ 
/* 105:193 */       kurt = coefficientOne * accum3 - termTwo;
/* 106:    */     }
/* 107:195 */     return kurt;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public Kurtosis copy()
/* 111:    */   {
/* 112:203 */     Kurtosis result = new Kurtosis();
/* 113:204 */     copy(this, result);
/* 114:205 */     return result;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static void copy(Kurtosis source, Kurtosis dest)
/* 118:    */     throws NullArgumentException
/* 119:    */   {
/* 120:218 */     MathUtils.checkNotNull(source);
/* 121:219 */     MathUtils.checkNotNull(dest);
/* 122:220 */     dest.setData(source.getDataRef());
/* 123:221 */     dest.moment = source.moment.copy();
/* 124:222 */     dest.incMoment = source.incMoment;
/* 125:    */   }
/* 126:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.Kurtosis
 * JD-Core Version:    0.7.0.1
 */
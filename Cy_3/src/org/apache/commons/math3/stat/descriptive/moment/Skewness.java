/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ import org.apache.commons.math3.util.MathUtils;
/*   8:    */ 
/*   9:    */ public class Skewness
/*  10:    */   extends AbstractStorelessUnivariateStatistic
/*  11:    */   implements Serializable
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = 7101857578996691352L;
/*  14: 49 */   protected ThirdMoment moment = null;
/*  15:    */   protected boolean incMoment;
/*  16:    */   
/*  17:    */   public Skewness()
/*  18:    */   {
/*  19: 63 */     this.incMoment = true;
/*  20: 64 */     this.moment = new ThirdMoment();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Skewness(ThirdMoment m3)
/*  24:    */   {
/*  25: 72 */     this.incMoment = false;
/*  26: 73 */     this.moment = m3;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Skewness(Skewness original)
/*  30:    */   {
/*  31: 83 */     copy(original, this);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void increment(double d)
/*  35:    */   {
/*  36: 91 */     if (this.incMoment) {
/*  37: 92 */       this.moment.increment(d);
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double getResult()
/*  42:    */   {
/*  43:106 */     if (this.moment.n < 3L) {
/*  44:107 */       return (0.0D / 0.0D);
/*  45:    */     }
/*  46:109 */     double variance = this.moment.m2 / (this.moment.n - 1L);
/*  47:110 */     if (variance < 1.0E-019D) {
/*  48:111 */       return 0.0D;
/*  49:    */     }
/*  50:113 */     double n0 = this.moment.getN();
/*  51:114 */     return n0 * this.moment.m3 / ((n0 - 1.0D) * (n0 - 2.0D) * FastMath.sqrt(variance) * variance);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public long getN()
/*  55:    */   {
/*  56:123 */     return this.moment.getN();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void clear()
/*  60:    */   {
/*  61:131 */     if (this.incMoment) {
/*  62:132 */       this.moment.clear();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double evaluate(double[] values, int begin, int length)
/*  67:    */   {
/*  68:157 */     double skew = (0.0D / 0.0D);
/*  69:159 */     if ((test(values, begin, length)) && (length > 2))
/*  70:    */     {
/*  71:160 */       Mean mean = new Mean();
/*  72:    */       
/*  73:162 */       double m = mean.evaluate(values, begin, length);
/*  74:    */       
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:167 */       double accum = 0.0D;
/*  79:168 */       double accum2 = 0.0D;
/*  80:169 */       for (int i = begin; i < begin + length; i++)
/*  81:    */       {
/*  82:170 */         double d = values[i] - m;
/*  83:171 */         accum += d * d;
/*  84:172 */         accum2 += d;
/*  85:    */       }
/*  86:174 */       double variance = (accum - accum2 * accum2 / length) / (length - 1);
/*  87:    */       
/*  88:176 */       double accum3 = 0.0D;
/*  89:177 */       for (int i = begin; i < begin + length; i++)
/*  90:    */       {
/*  91:178 */         double d = values[i] - m;
/*  92:179 */         accum3 += d * d * d;
/*  93:    */       }
/*  94:181 */       accum3 /= variance * FastMath.sqrt(variance);
/*  95:    */       
/*  96:    */ 
/*  97:184 */       double n0 = length;
/*  98:    */       
/*  99:    */ 
/* 100:187 */       skew = n0 / ((n0 - 1.0D) * (n0 - 2.0D)) * accum3;
/* 101:    */     }
/* 102:189 */     return skew;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Skewness copy()
/* 106:    */   {
/* 107:197 */     Skewness result = new Skewness();
/* 108:198 */     copy(this, result);
/* 109:199 */     return result;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static void copy(Skewness source, Skewness dest)
/* 113:    */     throws NullArgumentException
/* 114:    */   {
/* 115:212 */     MathUtils.checkNotNull(source);
/* 116:213 */     MathUtils.checkNotNull(dest);
/* 117:214 */     dest.setData(source.getDataRef());
/* 118:215 */     dest.moment = new ThirdMoment(source.moment.copy());
/* 119:216 */     dest.incMoment = source.incMoment;
/* 120:    */   }
/* 121:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.Skewness
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ import org.apache.commons.math3.util.MathUtils;
/*   8:    */ 
/*   9:    */ public class StandardDeviation
/*  10:    */   extends AbstractStorelessUnivariateStatistic
/*  11:    */   implements Serializable
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = 5728716329662425188L;
/*  14: 50 */   private Variance variance = null;
/*  15:    */   
/*  16:    */   public StandardDeviation()
/*  17:    */   {
/*  18: 57 */     this.variance = new Variance();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public StandardDeviation(SecondMoment m2)
/*  22:    */   {
/*  23: 66 */     this.variance = new Variance(m2);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public StandardDeviation(StandardDeviation original)
/*  27:    */   {
/*  28: 76 */     copy(original, this);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public StandardDeviation(boolean isBiasCorrected)
/*  32:    */   {
/*  33: 90 */     this.variance = new Variance(isBiasCorrected);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public StandardDeviation(boolean isBiasCorrected, SecondMoment m2)
/*  37:    */   {
/*  38:105 */     this.variance = new Variance(isBiasCorrected, m2);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void increment(double d)
/*  42:    */   {
/*  43:113 */     this.variance.increment(d);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public long getN()
/*  47:    */   {
/*  48:120 */     return this.variance.getN();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getResult()
/*  52:    */   {
/*  53:128 */     return FastMath.sqrt(this.variance.getResult());
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void clear()
/*  57:    */   {
/*  58:136 */     this.variance.clear();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public double evaluate(double[] values)
/*  62:    */   {
/*  63:155 */     return FastMath.sqrt(this.variance.evaluate(values));
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double evaluate(double[] values, int begin, int length)
/*  67:    */   {
/*  68:178 */     return FastMath.sqrt(this.variance.evaluate(values, begin, length));
/*  69:    */   }
/*  70:    */   
/*  71:    */   public double evaluate(double[] values, double mean, int begin, int length)
/*  72:    */   {
/*  73:207 */     return FastMath.sqrt(this.variance.evaluate(values, mean, begin, length));
/*  74:    */   }
/*  75:    */   
/*  76:    */   public double evaluate(double[] values, double mean)
/*  77:    */   {
/*  78:232 */     return FastMath.sqrt(this.variance.evaluate(values, mean));
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean isBiasCorrected()
/*  82:    */   {
/*  83:239 */     return this.variance.isBiasCorrected();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setBiasCorrected(boolean isBiasCorrected)
/*  87:    */   {
/*  88:246 */     this.variance.setBiasCorrected(isBiasCorrected);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public StandardDeviation copy()
/*  92:    */   {
/*  93:254 */     StandardDeviation result = new StandardDeviation();
/*  94:255 */     copy(this, result);
/*  95:256 */     return result;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static void copy(StandardDeviation source, StandardDeviation dest)
/*  99:    */     throws NullArgumentException
/* 100:    */   {
/* 101:270 */     MathUtils.checkNotNull(source);
/* 102:271 */     MathUtils.checkNotNull(dest);
/* 103:272 */     dest.setData(source.getDataRef());
/* 104:273 */     dest.variance = source.variance.copy();
/* 105:    */   }
/* 106:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
 * JD-Core Version:    0.7.0.1
 */
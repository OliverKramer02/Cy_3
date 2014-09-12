/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   8:    */ import org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic;
/*   9:    */ import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*  11:    */ import org.apache.commons.math3.util.MathUtils;
/*  12:    */ 
/*  13:    */ public class GeometricMean
/*  14:    */   extends AbstractStorelessUnivariateStatistic
/*  15:    */   implements Serializable
/*  16:    */ {
/*  17:    */   private static final long serialVersionUID = -8178734905303459453L;
/*  18:    */   private StorelessUnivariateStatistic sumOfLogs;
/*  19:    */   
/*  20:    */   public GeometricMean()
/*  21:    */   {
/*  22: 66 */     this.sumOfLogs = new SumOfLogs();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public GeometricMean(GeometricMean original)
/*  26:    */   {
/*  27: 77 */     copy(original, this);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public GeometricMean(SumOfLogs sumOfLogs)
/*  31:    */   {
/*  32: 85 */     this.sumOfLogs = sumOfLogs;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public GeometricMean copy()
/*  36:    */   {
/*  37: 93 */     GeometricMean result = new GeometricMean();
/*  38: 94 */     copy(this, result);
/*  39: 95 */     return result;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void increment(double d)
/*  43:    */   {
/*  44:103 */     this.sumOfLogs.increment(d);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getResult()
/*  48:    */   {
/*  49:111 */     if (this.sumOfLogs.getN() > 0L) {
/*  50:112 */       return FastMath.exp(this.sumOfLogs.getResult() / this.sumOfLogs.getN());
/*  51:    */     }
/*  52:114 */     return (0.0D / 0.0D);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void clear()
/*  56:    */   {
/*  57:123 */     this.sumOfLogs.clear();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public double evaluate(double[] values, int begin, int length)
/*  61:    */   {
/*  62:145 */     return FastMath.exp(this.sumOfLogs.evaluate(values, begin, length) / length);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public long getN()
/*  66:    */   {
/*  67:153 */     return this.sumOfLogs.getN();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setSumLogImpl(StorelessUnivariateStatistic sumLogImpl)
/*  71:    */   {
/*  72:169 */     checkEmpty();
/*  73:170 */     this.sumOfLogs = sumLogImpl;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public StorelessUnivariateStatistic getSumLogImpl()
/*  77:    */   {
/*  78:179 */     return this.sumOfLogs;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static void copy(GeometricMean source, GeometricMean dest)
/*  82:    */     throws NullArgumentException
/*  83:    */   {
/*  84:192 */     MathUtils.checkNotNull(source);
/*  85:193 */     MathUtils.checkNotNull(dest);
/*  86:194 */     dest.setData(source.getDataRef());
/*  87:195 */     dest.sumOfLogs = source.sumOfLogs.copy();
/*  88:    */   }
/*  89:    */   
/*  90:    */   private void checkEmpty()
/*  91:    */   {
/*  92:203 */     if (getN() > 0L) {
/*  93:204 */       throw new MathIllegalStateException(LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, new Object[] { Long.valueOf(getN()) });
/*  94:    */     }
/*  95:    */   }
/*  96:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.GeometricMean
 * JD-Core Version:    0.7.0.1
 */
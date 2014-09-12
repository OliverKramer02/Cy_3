/*   1:    */ package org.apache.commons.math3.stat.descriptive.summary;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ import org.apache.commons.math3.util.MathUtils;
/*   8:    */ 
/*   9:    */ public class SumOfLogs
/*  10:    */   extends AbstractStorelessUnivariateStatistic
/*  11:    */   implements Serializable
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = -370076995648386763L;
/*  14:    */   private int n;
/*  15:    */   private double value;
/*  16:    */   
/*  17:    */   public SumOfLogs()
/*  18:    */   {
/*  19: 65 */     this.value = 0.0D;
/*  20: 66 */     this.n = 0;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public SumOfLogs(SumOfLogs original)
/*  24:    */   {
/*  25: 76 */     copy(original, this);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void increment(double d)
/*  29:    */   {
/*  30: 84 */     this.value += FastMath.log(d);
/*  31: 85 */     this.n += 1;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getResult()
/*  35:    */   {
/*  36: 93 */     return this.value;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public long getN()
/*  40:    */   {
/*  41:100 */     return this.n;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void clear()
/*  45:    */   {
/*  46:108 */     this.value = 0.0D;
/*  47:109 */     this.n = 0;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double evaluate(double[] values, int begin, int length)
/*  51:    */   {
/*  52:131 */     double sumLog = (0.0D / 0.0D);
/*  53:132 */     if (test(values, begin, length, true))
/*  54:    */     {
/*  55:133 */       sumLog = 0.0D;
/*  56:134 */       for (int i = begin; i < begin + length; i++) {
/*  57:135 */         sumLog += FastMath.log(values[i]);
/*  58:    */       }
/*  59:    */     }
/*  60:138 */     return sumLog;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public SumOfLogs copy()
/*  64:    */   {
/*  65:146 */     SumOfLogs result = new SumOfLogs();
/*  66:147 */     copy(this, result);
/*  67:148 */     return result;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void copy(SumOfLogs source, SumOfLogs dest)
/*  71:    */     throws NullArgumentException
/*  72:    */   {
/*  73:161 */     MathUtils.checkNotNull(source);
/*  74:162 */     MathUtils.checkNotNull(dest);
/*  75:163 */     dest.setData(source.getDataRef());
/*  76:164 */     dest.n = source.n;
/*  77:165 */     dest.value = source.value;
/*  78:    */   }
/*  79:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.summary.SumOfLogs
 * JD-Core Version:    0.7.0.1
 */
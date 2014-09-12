/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.util.MathUtils;
/*   6:    */ import org.apache.commons.math3.util.Precision;
/*   7:    */ 
/*   8:    */ public abstract class AbstractStorelessUnivariateStatistic
/*   9:    */   extends AbstractUnivariateStatistic
/*  10:    */   implements StorelessUnivariateStatistic
/*  11:    */ {
/*  12:    */   public double evaluate(double[] values)
/*  13:    */   {
/*  14: 59 */     if (values == null) {
/*  15: 60 */       throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
/*  16:    */     }
/*  17: 62 */     return evaluate(values, 0, values.length);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public double evaluate(double[] values, int begin, int length)
/*  21:    */   {
/*  22: 88 */     if (test(values, begin, length))
/*  23:    */     {
/*  24: 89 */       clear();
/*  25: 90 */       incrementAll(values, begin, length);
/*  26:    */     }
/*  27: 92 */     return getResult();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public abstract StorelessUnivariateStatistic copy();
/*  31:    */   
/*  32:    */   public abstract void clear();
/*  33:    */   
/*  34:    */   public abstract double getResult();
/*  35:    */   
/*  36:    */   public abstract void increment(double paramDouble);
/*  37:    */   
/*  38:    */   public void incrementAll(double[] values)
/*  39:    */   {
/*  40:127 */     if (values == null) {
/*  41:128 */       throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
/*  42:    */     }
/*  43:130 */     incrementAll(values, 0, values.length);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void incrementAll(double[] values, int begin, int length)
/*  47:    */   {
/*  48:146 */     if (test(values, begin, length))
/*  49:    */     {
/*  50:147 */       int k = begin + length;
/*  51:148 */       for (int i = begin; i < k; i++) {
/*  52:149 */         increment(values[i]);
/*  53:    */       }
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean equals(Object object)
/*  58:    */   {
/*  59:163 */     if (object == this) {
/*  60:164 */       return true;
/*  61:    */     }
/*  62:166 */     if (!(object instanceof AbstractStorelessUnivariateStatistic)) {
/*  63:167 */       return false;
/*  64:    */     }
/*  65:169 */     AbstractStorelessUnivariateStatistic stat = (AbstractStorelessUnivariateStatistic)object;
/*  66:170 */     return (Precision.equalsIncludingNaN(stat.getResult(), getResult())) && (Precision.equalsIncludingNaN((float)stat.getN(), (float)getN()));
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int hashCode()
/*  70:    */   {
/*  71:181 */     return 31 * (31 + MathUtils.hash(getResult())) + MathUtils.hash(getN());
/*  72:    */   }
/*  73:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic
 * JD-Core Version:    0.7.0.1
 */
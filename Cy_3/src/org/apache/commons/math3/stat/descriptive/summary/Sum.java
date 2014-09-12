/*   1:    */ package org.apache.commons.math3.stat.descriptive.summary;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.util.MathUtils;
/*   7:    */ 
/*   8:    */ public class Sum
/*   9:    */   extends AbstractStorelessUnivariateStatistic
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = -8231831954703408316L;
/*  13:    */   private long n;
/*  14:    */   private double value;
/*  15:    */   
/*  16:    */   public Sum()
/*  17:    */   {
/*  18: 57 */     this.n = 0L;
/*  19: 58 */     this.value = 0.0D;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Sum(Sum original)
/*  23:    */   {
/*  24: 68 */     copy(original, this);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void increment(double d)
/*  28:    */   {
/*  29: 76 */     this.value += d;
/*  30: 77 */     this.n += 1L;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public double getResult()
/*  34:    */   {
/*  35: 85 */     return this.value;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public long getN()
/*  39:    */   {
/*  40: 92 */     return this.n;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void clear()
/*  44:    */   {
/*  45:100 */     this.value = 0.0D;
/*  46:101 */     this.n = 0L;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double evaluate(double[] values, int begin, int length)
/*  50:    */   {
/*  51:120 */     double sum = (0.0D / 0.0D);
/*  52:121 */     if (test(values, begin, length, true))
/*  53:    */     {
/*  54:122 */       sum = 0.0D;
/*  55:123 */       for (int i = begin; i < begin + length; i++) {
/*  56:124 */         sum += values[i];
/*  57:    */       }
/*  58:    */     }
/*  59:127 */     return sum;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public double evaluate(double[] values, double[] weights, int begin, int length)
/*  63:    */   {
/*  64:159 */     double sum = (0.0D / 0.0D);
/*  65:160 */     if (test(values, weights, begin, length, true))
/*  66:    */     {
/*  67:161 */       sum = 0.0D;
/*  68:162 */       for (int i = begin; i < begin + length; i++) {
/*  69:163 */         sum += values[i] * weights[i];
/*  70:    */       }
/*  71:    */     }
/*  72:166 */     return sum;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public double evaluate(double[] values, double[] weights)
/*  76:    */   {
/*  77:192 */     return evaluate(values, weights, 0, values.length);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Sum copy()
/*  81:    */   {
/*  82:200 */     Sum result = new Sum();
/*  83:201 */     copy(this, result);
/*  84:202 */     return result;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static void copy(Sum source, Sum dest)
/*  88:    */     throws NullArgumentException
/*  89:    */   {
/*  90:215 */     MathUtils.checkNotNull(source);
/*  91:216 */     MathUtils.checkNotNull(dest);
/*  92:217 */     dest.setData(source.getDataRef());
/*  93:218 */     dest.n = source.n;
/*  94:219 */     dest.value = source.value;
/*  95:    */   }
/*  96:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.summary.Sum
 * JD-Core Version:    0.7.0.1
 */
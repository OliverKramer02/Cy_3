/*   1:    */ package org.apache.commons.math3.stat.descriptive.summary;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.util.MathUtils;
/*   7:    */ 
/*   8:    */ public class SumOfSquares
/*   9:    */   extends AbstractStorelessUnivariateStatistic
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = 1460986908574398008L;
/*  13:    */   private long n;
/*  14:    */   private double value;
/*  15:    */   
/*  16:    */   public SumOfSquares()
/*  17:    */   {
/*  18: 56 */     this.n = 0L;
/*  19: 57 */     this.value = 0.0D;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public SumOfSquares(SumOfSquares original)
/*  23:    */   {
/*  24: 67 */     copy(original, this);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void increment(double d)
/*  28:    */   {
/*  29: 75 */     this.value += d * d;
/*  30: 76 */     this.n += 1L;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public double getResult()
/*  34:    */   {
/*  35: 84 */     return this.value;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public long getN()
/*  39:    */   {
/*  40: 91 */     return this.n;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void clear()
/*  44:    */   {
/*  45: 99 */     this.value = 0.0D;
/*  46:100 */     this.n = 0L;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double evaluate(double[] values, int begin, int length)
/*  50:    */   {
/*  51:119 */     double sumSq = (0.0D / 0.0D);
/*  52:120 */     if (test(values, begin, length, true))
/*  53:    */     {
/*  54:121 */       sumSq = 0.0D;
/*  55:122 */       for (int i = begin; i < begin + length; i++) {
/*  56:123 */         sumSq += values[i] * values[i];
/*  57:    */       }
/*  58:    */     }
/*  59:126 */     return sumSq;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public SumOfSquares copy()
/*  63:    */   {
/*  64:134 */     SumOfSquares result = new SumOfSquares();
/*  65:135 */     copy(this, result);
/*  66:136 */     return result;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static void copy(SumOfSquares source, SumOfSquares dest)
/*  70:    */     throws NullArgumentException
/*  71:    */   {
/*  72:149 */     MathUtils.checkNotNull(source);
/*  73:150 */     MathUtils.checkNotNull(dest);
/*  74:151 */     dest.setData(source.getDataRef());
/*  75:152 */     dest.n = source.n;
/*  76:153 */     dest.value = source.value;
/*  77:    */   }
/*  78:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.summary.SumOfSquares
 * JD-Core Version:    0.7.0.1
 */
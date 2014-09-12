/*   1:    */ package org.apache.commons.math3.stat.descriptive.rank;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.util.MathUtils;
/*   7:    */ 
/*   8:    */ public class Min
/*   9:    */   extends AbstractStorelessUnivariateStatistic
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = -2941995784909003131L;
/*  13:    */   private long n;
/*  14:    */   private double value;
/*  15:    */   
/*  16:    */   public Min()
/*  17:    */   {
/*  18: 57 */     this.n = 0L;
/*  19: 58 */     this.value = (0.0D / 0.0D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Min(Min original)
/*  23:    */   {
/*  24: 68 */     copy(original, this);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void increment(double d)
/*  28:    */   {
/*  29: 76 */     if ((d < this.value) || (Double.isNaN(this.value))) {
/*  30: 77 */       this.value = d;
/*  31:    */     }
/*  32: 79 */     this.n += 1L;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void clear()
/*  36:    */   {
/*  37: 87 */     this.value = (0.0D / 0.0D);
/*  38: 88 */     this.n = 0L;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double getResult()
/*  42:    */   {
/*  43: 96 */     return this.value;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public long getN()
/*  47:    */   {
/*  48:103 */     return this.n;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double evaluate(double[] values, int begin, int length)
/*  52:    */   {
/*  53:130 */     double min = (0.0D / 0.0D);
/*  54:131 */     if (test(values, begin, length))
/*  55:    */     {
/*  56:132 */       min = values[begin];
/*  57:133 */       for (int i = begin; i < begin + length; i++) {
/*  58:134 */         if (!Double.isNaN(values[i])) {
/*  59:135 */           min = min < values[i] ? min : values[i];
/*  60:    */         }
/*  61:    */       }
/*  62:    */     }
/*  63:139 */     return min;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Min copy()
/*  67:    */   {
/*  68:147 */     Min result = new Min();
/*  69:148 */     copy(this, result);
/*  70:149 */     return result;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static void copy(Min source, Min dest)
/*  74:    */     throws NullArgumentException
/*  75:    */   {
/*  76:162 */     MathUtils.checkNotNull(source);
/*  77:163 */     MathUtils.checkNotNull(dest);
/*  78:164 */     dest.setData(source.getDataRef());
/*  79:165 */     dest.n = source.n;
/*  80:166 */     dest.value = source.value;
/*  81:    */   }
/*  82:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.rank.Min
 * JD-Core Version:    0.7.0.1
 */
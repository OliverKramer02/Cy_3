/*   1:    */ package org.apache.commons.math3.stat.descriptive.summary;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ import org.apache.commons.math3.util.MathUtils;
/*   9:    */ 
/*  10:    */ public class Product
/*  11:    */   extends AbstractStorelessUnivariateStatistic
/*  12:    */   implements Serializable, WeightedEvaluation
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = 2824226005990582538L;
/*  15:    */   private long n;
/*  16:    */   private double value;
/*  17:    */   
/*  18:    */   public Product()
/*  19:    */   {
/*  20: 58 */     this.n = 0L;
/*  21: 59 */     this.value = 1.0D;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Product(Product original)
/*  25:    */   {
/*  26: 69 */     copy(original, this);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void increment(double d)
/*  30:    */   {
/*  31: 77 */     this.value *= d;
/*  32: 78 */     this.n += 1L;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public double getResult()
/*  36:    */   {
/*  37: 86 */     return this.value;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public long getN()
/*  41:    */   {
/*  42: 93 */     return this.n;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void clear()
/*  46:    */   {
/*  47:101 */     this.value = 1.0D;
/*  48:102 */     this.n = 0L;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double evaluate(double[] values, int begin, int length)
/*  52:    */   {
/*  53:121 */     double product = (0.0D / 0.0D);
/*  54:122 */     if (test(values, begin, length, true))
/*  55:    */     {
/*  56:123 */       product = 1.0D;
/*  57:124 */       for (int i = begin; i < begin + length; i++) {
/*  58:125 */         product *= values[i];
/*  59:    */       }
/*  60:    */     }
/*  61:128 */     return product;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public double evaluate(double[] values, double[] weights, int begin, int length)
/*  65:    */   {
/*  66:161 */     double product = (0.0D / 0.0D);
/*  67:162 */     if (test(values, weights, begin, length, true))
/*  68:    */     {
/*  69:163 */       product = 1.0D;
/*  70:164 */       for (int i = begin; i < begin + length; i++) {
/*  71:165 */         product *= FastMath.pow(values[i], weights[i]);
/*  72:    */       }
/*  73:    */     }
/*  74:168 */     return product;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public double evaluate(double[] values, double[] weights)
/*  78:    */   {
/*  79:195 */     return evaluate(values, weights, 0, values.length);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Product copy()
/*  83:    */   {
/*  84:204 */     Product result = new Product();
/*  85:205 */     copy(this, result);
/*  86:206 */     return result;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static void copy(Product source, Product dest)
/*  90:    */     throws NullArgumentException
/*  91:    */   {
/*  92:219 */     MathUtils.checkNotNull(source);
/*  93:220 */     MathUtils.checkNotNull(dest);
/*  94:221 */     dest.setData(source.getDataRef());
/*  95:222 */     dest.n = source.n;
/*  96:223 */     dest.value = source.value;
/*  97:    */   }
/*  98:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.summary.Product
 * JD-Core Version:    0.7.0.1
 */
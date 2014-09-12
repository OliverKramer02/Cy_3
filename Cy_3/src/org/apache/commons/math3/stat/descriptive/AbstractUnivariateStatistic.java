/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ 
/*  10:    */ public abstract class AbstractUnivariateStatistic
/*  11:    */   implements UnivariateStatistic
/*  12:    */ {
/*  13:    */   private double[] storedData;
/*  14:    */   
/*  15:    */   public void setData(double[] values)
/*  16:    */   {
/*  17: 54 */     this.storedData = (values == null ? null : (double[])values.clone());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public double[] getData()
/*  21:    */   {
/*  22: 62 */     return this.storedData == null ? null : (double[])this.storedData.clone();
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected double[] getDataRef()
/*  26:    */   {
/*  27: 70 */     return this.storedData;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setData(double[] values, int begin, int length)
/*  31:    */   {
/*  32: 81 */     this.storedData = new double[length];
/*  33: 82 */     System.arraycopy(values, begin, this.storedData, 0, length);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public double evaluate()
/*  37:    */   {
/*  38: 93 */     return evaluate(this.storedData);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double evaluate(double[] values)
/*  42:    */   {
/*  43:100 */     test(values, 0, 0);
/*  44:101 */     return evaluate(values, 0, values.length);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public abstract double evaluate(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
/*  48:    */   
/*  49:    */   public abstract UnivariateStatistic copy();
/*  50:    */   
/*  51:    */   protected boolean test(double[] values, int begin, int length)
/*  52:    */   {
/*  53:137 */     return test(values, begin, length, false);
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected boolean test(double[] values, int begin, int length, boolean allowEmpty)
/*  57:    */   {
/*  58:163 */     if (values == null) {
/*  59:164 */       throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
/*  60:    */     }
/*  61:167 */     if (begin < 0) {
/*  62:168 */       throw new NotPositiveException(LocalizedFormats.START_POSITION, Integer.valueOf(begin));
/*  63:    */     }
/*  64:171 */     if (length < 0) {
/*  65:172 */       throw new NotPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(length));
/*  66:    */     }
/*  67:175 */     if (begin + length > values.length) {
/*  68:176 */       throw new NumberIsTooLargeException(LocalizedFormats.SUBARRAY_ENDS_AFTER_ARRAY_END, Integer.valueOf(begin + length), Integer.valueOf(values.length), true);
/*  69:    */     }
/*  70:180 */     if ((length == 0) && (!allowEmpty)) {
/*  71:181 */       return false;
/*  72:    */     }
/*  73:184 */     return true;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected boolean test(double[] values, double[] weights, int begin, int length)
/*  77:    */   {
/*  78:222 */     return test(values, weights, begin, length, false);
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected boolean test(double[] values, double[] weights, int begin, int length, boolean allowEmpty)
/*  82:    */   {
/*  83:258 */     if (weights == null) {
/*  84:259 */       throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
/*  85:    */     }
/*  86:262 */     if (weights.length != values.length) {
/*  87:263 */       throw new DimensionMismatchException(weights.length, values.length);
/*  88:    */     }
/*  89:266 */     boolean containsPositiveWeight = false;
/*  90:267 */     for (int i = begin; i < begin + length; i++)
/*  91:    */     {
/*  92:268 */       if (Double.isNaN(weights[i])) {
/*  93:269 */         throw new MathIllegalArgumentException(LocalizedFormats.NAN_ELEMENT_AT_INDEX, new Object[] { Integer.valueOf(i) });
/*  94:    */       }
/*  95:271 */       if (Double.isInfinite(weights[i])) {
/*  96:272 */         throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, new Object[] { Double.valueOf(weights[i]), Integer.valueOf(i) });
/*  97:    */       }
/*  98:274 */       if (weights[i] < 0.0D) {
/*  99:275 */         throw new MathIllegalArgumentException(LocalizedFormats.NEGATIVE_ELEMENT_AT_INDEX, new Object[] { Integer.valueOf(i), Double.valueOf(weights[i]) });
/* 100:    */       }
/* 101:277 */       if ((!containsPositiveWeight) && (weights[i] > 0.0D)) {
/* 102:278 */         containsPositiveWeight = true;
/* 103:    */       }
/* 104:    */     }
/* 105:282 */     if (!containsPositiveWeight) {
/* 106:283 */       throw new MathIllegalArgumentException(LocalizedFormats.WEIGHT_AT_LEAST_ONE_NON_ZERO, new Object[0]);
/* 107:    */     }
/* 108:286 */     return test(values, begin, length, allowEmpty);
/* 109:    */   }
/* 110:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic
 * JD-Core Version:    0.7.0.1
 */
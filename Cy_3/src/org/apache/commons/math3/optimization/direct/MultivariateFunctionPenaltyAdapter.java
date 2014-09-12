/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ import org.apache.commons.math3.util.MathUtils;
/*   8:    */ 
/*   9:    */ public class MultivariateFunctionPenaltyAdapter
/*  10:    */   implements MultivariateFunction
/*  11:    */ {
/*  12:    */   private final MultivariateFunction bounded;
/*  13:    */   private final double[] lower;
/*  14:    */   private final double[] upper;
/*  15:    */   private final double offset;
/*  16:    */   private final double[] scale;
/*  17:    */   
/*  18:    */   public MultivariateFunctionPenaltyAdapter(MultivariateFunction bounded, double[] lower, double[] upper, double offset, double[] scale)
/*  19:    */   {
/*  20:128 */     MathUtils.checkNotNull(lower);
/*  21:129 */     MathUtils.checkNotNull(upper);
/*  22:130 */     MathUtils.checkNotNull(scale);
/*  23:131 */     if (lower.length != upper.length) {
/*  24:132 */       throw new DimensionMismatchException(lower.length, upper.length);
/*  25:    */     }
/*  26:134 */     if (lower.length != scale.length) {
/*  27:135 */       throw new DimensionMismatchException(lower.length, scale.length);
/*  28:    */     }
/*  29:137 */     for (int i = 0; i < lower.length; i++) {
/*  30:139 */       if (upper[i] < lower[i]) {
/*  31:140 */         throw new NumberIsTooSmallException(Double.valueOf(upper[i]), Double.valueOf(lower[i]), true);
/*  32:    */       }
/*  33:    */     }
/*  34:144 */     this.bounded = bounded;
/*  35:145 */     this.lower = ((double[])lower.clone());
/*  36:146 */     this.upper = ((double[])upper.clone());
/*  37:147 */     this.offset = offset;
/*  38:148 */     this.scale = ((double[])scale.clone());
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double value(double[] point)
/*  42:    */   {
/*  43:164 */     for (int i = 0; i < this.scale.length; i++) {
/*  44:165 */       if ((point[i] < this.lower[i]) || (point[i] > this.upper[i]))
/*  45:    */       {
/*  46:167 */         double sum = 0.0D;
/*  47:168 */         for (int j = i; j < this.scale.length; j++)
/*  48:    */         {
/*  49:    */           double overshoot;
/*  50:    */           
/*  51:170 */           if (point[j] < this.lower[j])
/*  52:    */           {
/*  53:171 */             overshoot = this.scale[j] * (this.lower[j] - point[j]);
/*  54:    */           }
/*  55:    */           else
/*  56:    */           {
/*  57:    */            
/*  58:172 */             if (point[j] > this.upper[j]) {
/*  59:173 */               overshoot = this.scale[j] * (point[j] - this.upper[j]);
/*  60:    */             } else {
/*  61:175 */               overshoot = 0.0D;
/*  62:    */             }
/*  63:    */           }
/*  64:177 */           sum += FastMath.sqrt(overshoot);
/*  65:    */         }
/*  66:179 */         return this.offset + sum;
/*  67:    */       }
/*  68:    */     }
/*  69:185 */     return this.bounded.value(point);
/*  70:    */   }
/*  71:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.MultivariateFunctionPenaltyAdapter
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.function.Logit;
/*   6:    */ import org.apache.commons.math3.analysis.function.Sigmoid;
/*   7:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   8:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ import org.apache.commons.math3.util.MathUtils;
/*  11:    */ 
/*  12:    */ public class MultivariateFunctionMappingAdapter
/*  13:    */   implements MultivariateFunction
/*  14:    */ {
/*  15:    */   private final MultivariateFunction bounded;
/*  16:    */   private final Mapper[] mappers;
/*  17:    */   
/*  18:    */   public MultivariateFunctionMappingAdapter(MultivariateFunction bounded, double[] lower, double[] upper)
/*  19:    */   {
/*  20:101 */     MathUtils.checkNotNull(lower);
/*  21:102 */     MathUtils.checkNotNull(upper);
/*  22:103 */     if (lower.length != upper.length) {
/*  23:104 */       throw new DimensionMismatchException(lower.length, upper.length);
/*  24:    */     }
/*  25:106 */     for (int i = 0; i < lower.length; i++) {
/*  26:108 */       if (upper[i] < lower[i]) {
/*  27:109 */         throw new NumberIsTooSmallException(Double.valueOf(upper[i]), Double.valueOf(lower[i]), true);
/*  28:    */       }
/*  29:    */     }
/*  30:113 */     this.bounded = bounded;
/*  31:114 */     this.mappers = new Mapper[lower.length];
/*  32:115 */     for (int i = 0; i < this.mappers.length; i++) {
/*  33:116 */       if (Double.isInfinite(lower[i]))
/*  34:    */       {
/*  35:117 */         if (Double.isInfinite(upper[i])) {
/*  36:119 */           this.mappers[i] = new NoBoundsMapper();
/*  37:    */         } else {
/*  38:122 */           this.mappers[i] = new UpperBoundMapper(upper[i]);
/*  39:    */         }
/*  40:    */       }
/*  41:125 */       else if (Double.isInfinite(upper[i])) {
/*  42:127 */         this.mappers[i] = new LowerBoundMapper(lower[i]);
/*  43:    */       } else {
/*  44:130 */         this.mappers[i] = new LowerUpperBoundMapper(lower[i], upper[i]);
/*  45:    */       }
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double[] unboundedToBounded(double[] point)
/*  50:    */   {
/*  51:144 */     double[] mapped = new double[this.mappers.length];
/*  52:145 */     for (int i = 0; i < this.mappers.length; i++) {
/*  53:146 */       mapped[i] = this.mappers[i].unboundedToBounded(point[i]);
/*  54:    */     }
/*  55:149 */     return mapped;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public double[] boundedToUnbounded(double[] point)
/*  59:    */   {
/*  60:160 */     double[] mapped = new double[this.mappers.length];
/*  61:161 */     for (int i = 0; i < this.mappers.length; i++) {
/*  62:162 */       mapped[i] = this.mappers[i].boundedToUnbounded(point[i]);
/*  63:    */     }
/*  64:165 */     return mapped;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public double value(double[] point)
/*  68:    */   {
/*  69:180 */     return this.bounded.value(unboundedToBounded(point));
/*  70:    */   }
/*  71:    */   
/*  72:    */   private static abstract interface Mapper
/*  73:    */   {
/*  74:    */     public abstract double unboundedToBounded(double paramDouble);
/*  75:    */     
/*  76:    */     public abstract double boundedToUnbounded(double paramDouble);
/*  77:    */   }
/*  78:    */   
/*  79:    */   private static class NoBoundsMapper
/*  80:    */     implements MultivariateFunctionMappingAdapter.Mapper
/*  81:    */   {
/*  82:    */     public double unboundedToBounded(double y)
/*  83:    */     {
/*  84:210 */       return y;
/*  85:    */     }
/*  86:    */     
/*  87:    */     public double boundedToUnbounded(double x)
/*  88:    */     {
/*  89:215 */       return x;
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   private static class LowerBoundMapper
/*  94:    */     implements MultivariateFunctionMappingAdapter.Mapper
/*  95:    */   {
/*  96:    */     private final double lower;
/*  97:    */     
/*  98:    */     public LowerBoundMapper(double lower)
/*  99:    */     {
/* 100:230 */       this.lower = lower;
/* 101:    */     }
/* 102:    */     
/* 103:    */     public double unboundedToBounded(double y)
/* 104:    */     {
/* 105:235 */       return this.lower + FastMath.exp(y);
/* 106:    */     }
/* 107:    */     
/* 108:    */     public double boundedToUnbounded(double x)
/* 109:    */     {
/* 110:240 */       return FastMath.log(x - this.lower);
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   private static class UpperBoundMapper
/* 115:    */     implements MultivariateFunctionMappingAdapter.Mapper
/* 116:    */   {
/* 117:    */     private final double upper;
/* 118:    */     
/* 119:    */     public UpperBoundMapper(double upper)
/* 120:    */     {
/* 121:255 */       this.upper = upper;
/* 122:    */     }
/* 123:    */     
/* 124:    */     public double unboundedToBounded(double y)
/* 125:    */     {
/* 126:260 */       return this.upper - FastMath.exp(-y);
/* 127:    */     }
/* 128:    */     
/* 129:    */     public double boundedToUnbounded(double x)
/* 130:    */     {
/* 131:265 */       return -FastMath.log(this.upper - x);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   private static class LowerUpperBoundMapper
/* 136:    */     implements MultivariateFunctionMappingAdapter.Mapper
/* 137:    */   {
/* 138:    */     private final UnivariateFunction boundingFunction;
/* 139:    */     private final UnivariateFunction unboundingFunction;
/* 140:    */     
/* 141:    */     public LowerUpperBoundMapper(double lower, double upper)
/* 142:    */     {
/* 143:284 */       this.boundingFunction = new Sigmoid(lower, upper);
/* 144:285 */       this.unboundingFunction = new Logit(lower, upper);
/* 145:    */     }
/* 146:    */     
/* 147:    */     public double unboundedToBounded(double y)
/* 148:    */     {
/* 149:290 */       return this.boundingFunction.value(y);
/* 150:    */     }
/* 151:    */     
/* 152:    */     public double boundedToUnbounded(double x)
/* 153:    */     {
/* 154:295 */       return this.unboundingFunction.value(x);
/* 155:    */     }
/* 156:    */   }
/* 157:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter
 * JD-Core Version:    0.7.0.1
 */
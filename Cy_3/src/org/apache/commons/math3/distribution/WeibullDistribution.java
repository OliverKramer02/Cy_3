/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.special.Gamma;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class WeibullDistribution
/*  10:    */   extends AbstractRealDistribution
/*  11:    */ {
/*  12:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  13:    */   private static final long serialVersionUID = 8589540077390120676L;
/*  14:    */   private final double shape;
/*  15:    */   private final double scale;
/*  16:    */   private final double solverAbsoluteAccuracy;
/*  17: 57 */   private double numericalMean = (0.0D / 0.0D);
/*  18: 60 */   private boolean numericalMeanIsCalculated = false;
/*  19: 63 */   private double numericalVariance = (0.0D / 0.0D);
/*  20: 66 */   private boolean numericalVarianceIsCalculated = false;
/*  21:    */   
/*  22:    */   public WeibullDistribution(double alpha, double beta)
/*  23:    */     throws NotStrictlyPositiveException
/*  24:    */   {
/*  25: 79 */     this(alpha, beta, 1.E-009D);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public WeibullDistribution(double alpha, double beta, double inverseCumAccuracy)
/*  29:    */     throws NotStrictlyPositiveException
/*  30:    */   {
/*  31: 98 */     if (alpha <= 0.0D) {
/*  32: 99 */       throw new NotStrictlyPositiveException(LocalizedFormats.SHAPE, Double.valueOf(alpha));
/*  33:    */     }
/*  34:102 */     if (beta <= 0.0D) {
/*  35:103 */       throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(beta));
/*  36:    */     }
/*  37:106 */     this.scale = beta;
/*  38:107 */     this.shape = alpha;
/*  39:108 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double getShape()
/*  43:    */   {
/*  44:117 */     return this.shape;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getScale()
/*  48:    */   {
/*  49:126 */     return this.scale;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public double probability(double x)
/*  53:    */   {
/*  54:137 */     return 0.0D;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public double density(double x)
/*  58:    */   {
/*  59:142 */     if (x < 0.0D) {
/*  60:143 */       return 0.0D;
/*  61:    */     }
/*  62:146 */     double xscale = x / this.scale;
/*  63:147 */     double xscalepow = FastMath.pow(xscale, this.shape - 1.0D);
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:154 */     double xscalepowshape = xscalepow * xscale;
/*  71:    */     
/*  72:156 */     return this.shape / this.scale * xscalepow * FastMath.exp(-xscalepowshape);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public double cumulativeProbability(double x)
/*  76:    */   {
/*  77:    */     double ret;
/*  78:    */     
/*  79:162 */     if (x <= 0.0D) {
/*  80:163 */       ret = 0.0D;
/*  81:    */     } else {
/*  82:165 */       ret = 1.0D - FastMath.exp(-FastMath.pow(x / this.scale, this.shape));
/*  83:    */     }
/*  84:167 */     return ret;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public double inverseCumulativeProbability(double p)
/*  88:    */   {
/*  89:179 */     if ((p < 0.0D) || (p > 1.0D)) {
/*  90:180 */       throw new OutOfRangeException(Double.valueOf(p), Double.valueOf(0.0D), Double.valueOf(1.0D));
/*  91:    */     }
/*  92:    */     double ret;
/*  93:    */     
/*  94:181 */     if (p == 0.0D)
/*  95:    */     {
/*  96:182 */       ret = 0.0D;
/*  97:    */     }
/*  98:    */     else
/*  99:    */     {
/* 100:    */     
/* 101:183 */       if (p == 1.0D) {
/* 102:184 */         ret = (1.0D / 0.0D);
/* 103:    */       } else {
/* 104:186 */         ret = this.scale * FastMath.pow(-FastMath.log(1.0D - p), 1.0D / this.shape);
/* 105:    */       }
/* 106:    */     }
/* 107:188 */     return ret;
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected double getSolverAbsoluteAccuracy()
/* 111:    */   {
/* 112:200 */     return this.solverAbsoluteAccuracy;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public double getNumericalMean()
/* 116:    */   {
/* 117:210 */     if (!this.numericalMeanIsCalculated)
/* 118:    */     {
/* 119:211 */       this.numericalMean = calculateNumericalMean();
/* 120:212 */       this.numericalMeanIsCalculated = true;
/* 121:    */     }
/* 122:214 */     return this.numericalMean;
/* 123:    */   }
/* 124:    */   
/* 125:    */   protected double calculateNumericalMean()
/* 126:    */   {
/* 127:223 */     double sh = getShape();
/* 128:224 */     double sc = getScale();
/* 129:    */     
/* 130:226 */     return sc * FastMath.exp(Gamma.logGamma(1.0D + 1.0D / sh));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public double getNumericalVariance()
/* 134:    */   {
/* 135:236 */     if (!this.numericalVarianceIsCalculated)
/* 136:    */     {
/* 137:237 */       this.numericalVariance = calculateNumericalVariance();
/* 138:238 */       this.numericalVarianceIsCalculated = true;
/* 139:    */     }
/* 140:240 */     return this.numericalVariance;
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected double calculateNumericalVariance()
/* 144:    */   {
/* 145:249 */     double sh = getShape();
/* 146:250 */     double sc = getScale();
/* 147:251 */     double mn = getNumericalMean();
/* 148:    */     
/* 149:253 */     return sc * sc * FastMath.exp(Gamma.logGamma(1.0D + 2.0D / sh)) - mn * mn;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public double getSupportLowerBound()
/* 153:    */   {
/* 154:265 */     return 0.0D;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public double getSupportUpperBound()
/* 158:    */   {
/* 159:278 */     return (1.0D / 0.0D);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public boolean isSupportLowerBoundInclusive()
/* 163:    */   {
/* 164:283 */     return true;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public boolean isSupportUpperBoundInclusive()
/* 168:    */   {
/* 169:288 */     return false;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public boolean isSupportConnected()
/* 173:    */   {
/* 174:299 */     return true;
/* 175:    */   }
/* 176:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.WeibullDistribution
 * JD-Core Version:    0.7.0.1
 */
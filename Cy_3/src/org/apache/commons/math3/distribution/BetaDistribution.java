/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.special.Beta;
/*   6:    */ import org.apache.commons.math3.special.Gamma;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class BetaDistribution
/*  10:    */   extends AbstractRealDistribution
/*  11:    */ {
/*  12:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  13:    */   private static final long serialVersionUID = -1221965979403477668L;
/*  14:    */   private final double alpha;
/*  15:    */   private final double beta;
/*  16:    */   private double z;
/*  17:    */   private final double solverAbsoluteAccuracy;
/*  18:    */   
/*  19:    */   public BetaDistribution(double alpha, double beta, double inverseCumAccuracy)
/*  20:    */   {
/*  21: 62 */     this.alpha = alpha;
/*  22: 63 */     this.beta = beta;
/*  23: 64 */     this.z = (0.0D / 0.0D);
/*  24: 65 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public BetaDistribution(double alpha, double beta)
/*  28:    */   {
/*  29: 75 */     this(alpha, beta, 1.E-009D);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public double getAlpha()
/*  33:    */   {
/*  34: 84 */     return this.alpha;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double getBeta()
/*  38:    */   {
/*  39: 93 */     return this.beta;
/*  40:    */   }
/*  41:    */   
/*  42:    */   private void recomputeZ()
/*  43:    */   {
/*  44: 98 */     if (Double.isNaN(this.z)) {
/*  45: 99 */       this.z = (Gamma.logGamma(this.alpha) + Gamma.logGamma(this.beta) - Gamma.logGamma(this.alpha + this.beta));
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double probability(double x)
/*  50:    */   {
/*  51:111 */     return 0.0D;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public double density(double x)
/*  55:    */   {
/*  56:116 */     recomputeZ();
/*  57:117 */     if ((x < 0.0D) || (x > 1.0D)) {
/*  58:118 */       return 0.0D;
/*  59:    */     }
/*  60:119 */     if (x == 0.0D)
/*  61:    */     {
/*  62:120 */       if (this.alpha < 1.0D) {
/*  63:121 */         throw new NumberIsTooSmallException(LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_0_FOR_SOME_ALPHA, Double.valueOf(this.alpha), Integer.valueOf(1), false);
/*  64:    */       }
/*  65:123 */       return 0.0D;
/*  66:    */     }
/*  67:124 */     if (x == 1.0D)
/*  68:    */     {
/*  69:125 */       if (this.beta < 1.0D) {
/*  70:126 */         throw new NumberIsTooSmallException(LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_1_FOR_SOME_BETA, Double.valueOf(this.beta), Integer.valueOf(1), false);
/*  71:    */       }
/*  72:128 */       return 0.0D;
/*  73:    */     }
/*  74:130 */     double logX = FastMath.log(x);
/*  75:131 */     double log1mX = FastMath.log1p(-x);
/*  76:132 */     return FastMath.exp((this.alpha - 1.0D) * logX + (this.beta - 1.0D) * log1mX - this.z);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public double cumulativeProbability(double x)
/*  80:    */   {
/*  81:138 */     if (x <= 0.0D) {
/*  82:139 */       return 0.0D;
/*  83:    */     }
/*  84:140 */     if (x >= 1.0D) {
/*  85:141 */       return 1.0D;
/*  86:    */     }
/*  87:143 */     return Beta.regularizedBeta(x, this.alpha, this.beta);
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected double getSolverAbsoluteAccuracy()
/*  91:    */   {
/*  92:156 */     return this.solverAbsoluteAccuracy;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public double getNumericalMean()
/*  96:    */   {
/*  97:166 */     double a = getAlpha();
/*  98:167 */     return a / (a + getBeta());
/*  99:    */   }
/* 100:    */   
/* 101:    */   public double getNumericalVariance()
/* 102:    */   {
/* 103:178 */     double a = getAlpha();
/* 104:179 */     double b = getBeta();
/* 105:180 */     double alphabetasum = a + b;
/* 106:181 */     return a * b / (alphabetasum * alphabetasum * (alphabetasum + 1.0D));
/* 107:    */   }
/* 108:    */   
/* 109:    */   public double getSupportLowerBound()
/* 110:    */   {
/* 111:192 */     return 0.0D;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public double getSupportUpperBound()
/* 115:    */   {
/* 116:203 */     return 1.0D;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean isSupportLowerBoundInclusive()
/* 120:    */   {
/* 121:208 */     return false;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean isSupportUpperBoundInclusive()
/* 125:    */   {
/* 126:213 */     return false;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean isSupportConnected()
/* 130:    */   {
/* 131:224 */     return true;
/* 132:    */   }
/* 133:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.BetaDistribution
 * JD-Core Version:    0.7.0.1
 */
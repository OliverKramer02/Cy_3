/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.special.Gamma;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ 
/*   8:    */ public class GammaDistribution
/*   9:    */   extends AbstractRealDistribution
/*  10:    */ {
/*  11:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  12:    */   private static final long serialVersionUID = -3239549463135430361L;
/*  13:    */   private final double alpha;
/*  14:    */   private final double beta;
/*  15:    */   private final double solverAbsoluteAccuracy;
/*  16:    */   
/*  17:    */   public GammaDistribution(double alpha, double beta)
/*  18:    */   {
/*  19: 53 */     this(alpha, beta, 1.E-009D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public GammaDistribution(double alpha, double beta, double inverseCumAccuracy)
/*  23:    */     throws NotStrictlyPositiveException
/*  24:    */   {
/*  25: 71 */     if (alpha <= 0.0D) {
/*  26: 72 */       throw new NotStrictlyPositiveException(LocalizedFormats.ALPHA, Double.valueOf(alpha));
/*  27:    */     }
/*  28: 74 */     if (beta <= 0.0D) {
/*  29: 75 */       throw new NotStrictlyPositiveException(LocalizedFormats.BETA, Double.valueOf(beta));
/*  30:    */     }
/*  31: 78 */     this.alpha = alpha;
/*  32: 79 */     this.beta = beta;
/*  33: 80 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public double getAlpha()
/*  37:    */   {
/*  38: 89 */     return this.alpha;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double getBeta()
/*  42:    */   {
/*  43: 98 */     return this.beta;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public double probability(double x)
/*  47:    */   {
/*  48:109 */     return 0.0D;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double density(double x)
/*  52:    */   {
/*  53:114 */     if (x < 0.0D) {
/*  54:115 */       return 0.0D;
/*  55:    */     }
/*  56:117 */     return FastMath.pow(x / this.beta, this.alpha - 1.0D) / this.beta * FastMath.exp(-x / this.beta) / FastMath.exp(Gamma.logGamma(this.alpha));
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double cumulativeProbability(double x)
/*  60:    */   {
/*  61:    */     double ret;
/*  62:    */  
/*  63:138 */     if (x <= 0.0D) {
/*  64:139 */       ret = 0.0D;
/*  65:    */     } else {
/*  66:141 */       ret = Gamma.regularizedGammaP(this.alpha, x / this.beta);
/*  67:    */     }
/*  68:144 */     return ret;
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected double getSolverAbsoluteAccuracy()
/*  72:    */   {
/*  73:150 */     return this.solverAbsoluteAccuracy;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public double getNumericalMean()
/*  77:    */   {
/*  78:160 */     return getAlpha() * getBeta();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public double getNumericalVariance()
/*  82:    */   {
/*  83:172 */     double b = getBeta();
/*  84:173 */     return getAlpha() * b * b;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public double getSupportLowerBound()
/*  88:    */   {
/*  89:184 */     return 0.0D;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public double getSupportUpperBound()
/*  93:    */   {
/*  94:196 */     return (1.0D / 0.0D);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean isSupportLowerBoundInclusive()
/*  98:    */   {
/*  99:201 */     return true;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isSupportUpperBoundInclusive()
/* 103:    */   {
/* 104:206 */     return false;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean isSupportConnected()
/* 108:    */   {
/* 109:217 */     return true;
/* 110:    */   }
/* 111:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.GammaDistribution
 * JD-Core Version:    0.7.0.1
 */
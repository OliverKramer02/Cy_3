/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.special.Beta;
/*   6:    */ import org.apache.commons.math3.special.Gamma;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class TDistribution
/*  10:    */   extends AbstractRealDistribution
/*  11:    */ {
/*  12:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  13:    */   private static final long serialVersionUID = -5852615386664158222L;
/*  14:    */   private final double degreesOfFreedom;
/*  15:    */   private final double solverAbsoluteAccuracy;
/*  16:    */   
/*  17:    */   public TDistribution(double degreesOfFreedom, double inverseCumAccuracy)
/*  18:    */     throws NotStrictlyPositiveException
/*  19:    */   {
/*  20: 58 */     if (degreesOfFreedom <= 0.0D) {
/*  21: 59 */       throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, Double.valueOf(degreesOfFreedom));
/*  22:    */     }
/*  23: 62 */     this.degreesOfFreedom = degreesOfFreedom;
/*  24: 63 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public TDistribution(double degreesOfFreedom)
/*  28:    */     throws NotStrictlyPositiveException
/*  29:    */   {
/*  30: 74 */     this(degreesOfFreedom, 1.E-009D);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public double getDegreesOfFreedom()
/*  34:    */   {
/*  35: 83 */     return this.degreesOfFreedom;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public double probability(double x)
/*  39:    */   {
/*  40: 94 */     return 0.0D;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double density(double x)
/*  44:    */   {
/*  45: 99 */     double n = this.degreesOfFreedom;
/*  46:100 */     double nPlus1Over2 = (n + 1.0D) / 2.0D;
/*  47:101 */     return FastMath.exp(Gamma.logGamma(nPlus1Over2) - 0.5D * (FastMath.log(3.141592653589793D) + FastMath.log(n)) - Gamma.logGamma(n / 2.0D) - nPlus1Over2 * FastMath.log(1.0D + x * x / n));
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double cumulativeProbability(double x)
/*  51:    */   {
/*  52:    */     double ret;
/*  53:    */   
/*  54:111 */     if (x == 0.0D)
/*  55:    */     {
/*  56:112 */       ret = 0.5D;
/*  57:    */     }
/*  58:    */     else
/*  59:    */     {
/*  60:114 */       double t = Beta.regularizedBeta(this.degreesOfFreedom / (this.degreesOfFreedom + x * x), 0.5D * this.degreesOfFreedom, 0.5D);
/*  61:    */     
/*  62:119 */       if (x < 0.0D) {
/*  63:120 */         ret = 0.5D * t;
/*  64:    */       } else {
/*  65:122 */         ret = 1.0D - 0.5D * t;
/*  66:    */       }
/*  67:    */     }
/*  68:126 */     return ret;
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected double getSolverAbsoluteAccuracy()
/*  72:    */   {
/*  73:132 */     return this.solverAbsoluteAccuracy;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public double getNumericalMean()
/*  77:    */   {
/*  78:145 */     double df = getDegreesOfFreedom();
/*  79:147 */     if (df > 1.0D) {
/*  80:148 */       return 0.0D;
/*  81:    */     }
/*  82:151 */     return (0.0D / 0.0D);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public double getNumericalVariance()
/*  86:    */   {
/*  87:166 */     double df = getDegreesOfFreedom();
/*  88:168 */     if (df > 2.0D) {
/*  89:169 */       return df / (df - 2.0D);
/*  90:    */     }
/*  91:172 */     if ((df > 1.0D) && (df <= 2.0D)) {
/*  92:173 */       return (1.0D / 0.0D);
/*  93:    */     }
/*  94:176 */     return (0.0D / 0.0D);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public double getSupportLowerBound()
/*  98:    */   {
/*  99:189 */     return (-1.0D / 0.0D);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public double getSupportUpperBound()
/* 103:    */   {
/* 104:202 */     return (1.0D / 0.0D);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean isSupportLowerBoundInclusive()
/* 108:    */   {
/* 109:207 */     return false;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean isSupportUpperBoundInclusive()
/* 113:    */   {
/* 114:212 */     return false;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isSupportConnected()
/* 118:    */   {
/* 119:223 */     return true;
/* 120:    */   }
/* 121:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.TDistribution
 * JD-Core Version:    0.7.0.1
 */
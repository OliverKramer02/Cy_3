/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.special.Beta;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ 
/*   8:    */ public class FDistribution
/*   9:    */   extends AbstractRealDistribution
/*  10:    */ {
/*  11:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  12:    */   private static final long serialVersionUID = -8516354193418641566L;
/*  13:    */   private final double numeratorDegreesOfFreedom;
/*  14:    */   private final double denominatorDegreesOfFreedom;
/*  15:    */   private final double solverAbsoluteAccuracy;
/*  16: 52 */   private double numericalVariance = (0.0D / 0.0D);
/*  17: 55 */   private boolean numericalVarianceIsCalculated = false;
/*  18:    */   
/*  19:    */   public FDistribution(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom)
/*  20:    */     throws NotStrictlyPositiveException
/*  21:    */   {
/*  22: 68 */     this(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, 1.E-009D);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public FDistribution(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double inverseCumAccuracy)
/*  26:    */     throws NotStrictlyPositiveException
/*  27:    */   {
/*  28: 89 */     if (numeratorDegreesOfFreedom <= 0.0D) {
/*  29: 90 */       throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, Double.valueOf(numeratorDegreesOfFreedom));
/*  30:    */     }
/*  31: 93 */     if (denominatorDegreesOfFreedom <= 0.0D) {
/*  32: 94 */       throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, Double.valueOf(denominatorDegreesOfFreedom));
/*  33:    */     }
/*  34: 97 */     this.numeratorDegreesOfFreedom = numeratorDegreesOfFreedom;
/*  35: 98 */     this.denominatorDegreesOfFreedom = denominatorDegreesOfFreedom;
/*  36: 99 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public double probability(double x)
/*  40:    */   {
/*  41:110 */     return 0.0D;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public double density(double x)
/*  45:    */   {
/*  46:119 */     double nhalf = this.numeratorDegreesOfFreedom / 2.0D;
/*  47:120 */     double mhalf = this.denominatorDegreesOfFreedom / 2.0D;
/*  48:121 */     double logx = FastMath.log(x);
/*  49:122 */     double logn = FastMath.log(this.numeratorDegreesOfFreedom);
/*  50:123 */     double logm = FastMath.log(this.denominatorDegreesOfFreedom);
/*  51:124 */     double lognxm = FastMath.log(this.numeratorDegreesOfFreedom * x + this.denominatorDegreesOfFreedom);
/*  52:    */     
/*  53:126 */     return FastMath.exp(nhalf * logn + nhalf * logx - logx + mhalf * logm - nhalf * lognxm - mhalf * lognxm - Beta.logBeta(nhalf, mhalf));
/*  54:    */   }
/*  55:    */   
/*  56:    */   public double cumulativeProbability(double x)
/*  57:    */   {
/*  58:    */     double ret;
/*  59:    */     
/*  60:144 */     if (x <= 0.0D)
/*  61:    */     {
/*  62:145 */       ret = 0.0D;
/*  63:    */     }
/*  64:    */     else
/*  65:    */     {
/*  66:147 */       double n = this.numeratorDegreesOfFreedom;
/*  67:148 */       double m = this.denominatorDegreesOfFreedom;
/*  68:    */       
/*  69:150 */       ret = Beta.regularizedBeta(n * x / (m + n * x), 0.5D * n, 0.5D * m);
/*  70:    */     }
/*  71:154 */     return ret;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public double getNumeratorDegreesOfFreedom()
/*  75:    */   {
/*  76:163 */     return this.numeratorDegreesOfFreedom;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public double getDenominatorDegreesOfFreedom()
/*  80:    */   {
/*  81:172 */     return this.denominatorDegreesOfFreedom;
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected double getSolverAbsoluteAccuracy()
/*  85:    */   {
/*  86:178 */     return this.solverAbsoluteAccuracy;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public double getNumericalMean()
/*  90:    */   {
/*  91:191 */     double denominatorDF = getDenominatorDegreesOfFreedom();
/*  92:193 */     if (denominatorDF > 2.0D) {
/*  93:194 */       return denominatorDF / (denominatorDF - 2.0D);
/*  94:    */     }
/*  95:197 */     return (0.0D / 0.0D);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public double getNumericalVariance()
/*  99:    */   {
/* 100:214 */     if (!this.numericalVarianceIsCalculated)
/* 101:    */     {
/* 102:215 */       this.numericalVariance = calculateNumericalVariance();
/* 103:216 */       this.numericalVarianceIsCalculated = true;
/* 104:    */     }
/* 105:218 */     return this.numericalVariance;
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected double calculateNumericalVariance()
/* 109:    */   {
/* 110:227 */     double denominatorDF = getDenominatorDegreesOfFreedom();
/* 111:229 */     if (denominatorDF > 4.0D)
/* 112:    */     {
/* 113:230 */       double numeratorDF = getNumeratorDegreesOfFreedom();
/* 114:231 */       double denomDFMinusTwo = denominatorDF - 2.0D;
/* 115:    */       
/* 116:233 */       return 2.0D * (denominatorDF * denominatorDF) * (numeratorDF + denominatorDF - 2.0D) / (numeratorDF * (denomDFMinusTwo * denomDFMinusTwo) * (denominatorDF - 4.0D));
/* 117:    */     }
/* 118:237 */     return (0.0D / 0.0D);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public double getSupportLowerBound()
/* 122:    */   {
/* 123:248 */     return 0.0D;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public double getSupportUpperBound()
/* 127:    */   {
/* 128:260 */     return (1.0D / 0.0D);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public boolean isSupportLowerBoundInclusive()
/* 132:    */   {
/* 133:265 */     return true;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public boolean isSupportUpperBoundInclusive()
/* 137:    */   {
/* 138:270 */     return false;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean isSupportConnected()
/* 142:    */   {
/* 143:281 */     return true;
/* 144:    */   }
/* 145:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.FDistribution
 * JD-Core Version:    0.7.0.1
 */
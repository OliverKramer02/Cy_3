/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*   7:    */ import org.apache.commons.math3.special.Erf;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class NormalDistribution
/*  11:    */   extends AbstractRealDistribution
/*  12:    */ {
/*  13:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  14:    */   private static final long serialVersionUID = 8589540077390120676L;
/*  15: 42 */   private static final double SQRT2PI = FastMath.sqrt(6.283185307179586D);
/*  16: 44 */   private static final double SQRT2 = FastMath.sqrt(2.0D);
/*  17:    */   private final double mean;
/*  18:    */   private final double standardDeviation;
/*  19:    */   private final double solverAbsoluteAccuracy;
/*  20:    */   
/*  21:    */   public NormalDistribution(double mean, double sd)
/*  22:    */     throws NotStrictlyPositiveException
/*  23:    */   {
/*  24: 61 */     this(mean, sd, 1.E-009D);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public NormalDistribution(double mean, double sd, double inverseCumAccuracy)
/*  28:    */     throws NotStrictlyPositiveException
/*  29:    */   {
/*  30: 76 */     if (sd <= 0.0D) {
/*  31: 77 */       throw new NotStrictlyPositiveException(LocalizedFormats.STANDARD_DEVIATION, Double.valueOf(sd));
/*  32:    */     }
/*  33: 80 */     this.mean = mean;
/*  34: 81 */     this.standardDeviation = sd;
/*  35: 82 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public NormalDistribution()
/*  39:    */   {
/*  40: 90 */     this(0.0D, 1.0D);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double getMean()
/*  44:    */   {
/*  45: 99 */     return this.mean;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public double getStandardDeviation()
/*  49:    */   {
/*  50:108 */     return this.standardDeviation;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public double probability(double x)
/*  54:    */   {
/*  55:119 */     return 0.0D;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public double density(double x)
/*  59:    */   {
/*  60:124 */     double x0 = x - this.mean;
/*  61:125 */     double x1 = x0 / this.standardDeviation;
/*  62:126 */     return FastMath.exp(-0.5D * x1 * x1) / (this.standardDeviation * SQRT2PI);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public double cumulativeProbability(double x)
/*  66:    */   {
/*  67:137 */     double dev = x - this.mean;
/*  68:138 */     if (FastMath.abs(dev) > 40.0D * this.standardDeviation) {
/*  69:139 */       return dev < 0.0D ? 0.0D : 1.0D;
/*  70:    */     }
/*  71:141 */     return 0.5D * (1.0D + Erf.erf(dev / (this.standardDeviation * SQRT2)));
/*  72:    */   }
/*  73:    */   
/*  74:    */   public double cumulativeProbability(double x0, double x1)
/*  75:    */     throws NumberIsTooLargeException
/*  76:    */   {
/*  77:148 */     if (x0 > x1) {
/*  78:149 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, Double.valueOf(x0), Double.valueOf(x1), true);
/*  79:    */     }
/*  80:152 */     double denom = this.standardDeviation * SQRT2;
/*  81:153 */     double v0 = (x0 - this.mean) / denom;
/*  82:154 */     double v1 = (x1 - this.mean) / denom;
/*  83:155 */     return 0.5D * Erf.erf(v0, v1);
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected double getSolverAbsoluteAccuracy()
/*  87:    */   {
/*  88:161 */     return this.solverAbsoluteAccuracy;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public double getNumericalMean()
/*  92:    */   {
/*  93:170 */     return getMean();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public double getNumericalVariance()
/*  97:    */   {
/*  98:179 */     double s = getStandardDeviation();
/*  99:180 */     return s * s;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public double getSupportLowerBound()
/* 103:    */   {
/* 104:193 */     return (-1.0D / 0.0D);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public double getSupportUpperBound()
/* 108:    */   {
/* 109:206 */     return (1.0D / 0.0D);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean isSupportLowerBoundInclusive()
/* 113:    */   {
/* 114:211 */     return false;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isSupportUpperBoundInclusive()
/* 118:    */   {
/* 119:216 */     return false;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean isSupportConnected()
/* 123:    */   {
/* 124:227 */     return true;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public double sample()
/* 128:    */   {
/* 129:233 */     return this.randomData.nextGaussian(this.mean, this.standardDeviation);
/* 130:    */   }
/* 131:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.NormalDistribution
 * JD-Core Version:    0.7.0.1
 */
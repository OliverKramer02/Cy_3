/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class ExponentialDistribution
/*  10:    */   extends AbstractRealDistribution
/*  11:    */ {
/*  12:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  13:    */   private static final long serialVersionUID = 2401296428283614780L;
/*  14:    */   private final double mean;
/*  15:    */   private final double solverAbsoluteAccuracy;
/*  16:    */   
/*  17:    */   public ExponentialDistribution(double mean)
/*  18:    */   {
/*  19: 49 */     this(mean, 1.E-009D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public ExponentialDistribution(double mean, double inverseCumAccuracy)
/*  23:    */     throws NotStrictlyPositiveException
/*  24:    */   {
/*  25: 64 */     if (mean <= 0.0D) {
/*  26: 65 */       throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, Double.valueOf(mean));
/*  27:    */     }
/*  28: 67 */     this.mean = mean;
/*  29: 68 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public double getMean()
/*  33:    */   {
/*  34: 77 */     return this.mean;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double probability(double x)
/*  38:    */   {
/*  39: 88 */     return 0.0D;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double density(double x)
/*  43:    */   {
/*  44: 93 */     if (x < 0.0D) {
/*  45: 94 */       return 0.0D;
/*  46:    */     }
/*  47: 96 */     return FastMath.exp(-x / this.mean) / this.mean;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double cumulativeProbability(double x)
/*  51:    */   {
/*  52:    */     double ret;
/*  53:    */  
/*  54:111 */     if (x <= 0.0D) {
/*  55:112 */       ret = 0.0D;
/*  56:    */     } else {
/*  57:114 */       ret = 1.0D - FastMath.exp(-x / this.mean);
/*  58:    */     }
/*  59:116 */     return ret;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public double inverseCumulativeProbability(double p)
/*  63:    */     throws OutOfRangeException
/*  64:    */   {
/*  65:129 */     if ((p < 0.0D) || (p > 1.0D)) {
/*  66:130 */       throw new OutOfRangeException(Double.valueOf(p), Double.valueOf(0.0D), Double.valueOf(1.0D));
/*  67:    */     }
/*  68:    */     double ret;
/*  69:    */    
/*  70:131 */     if (p == 1.0D) {
/*  71:132 */       ret = (1.0D / 0.0D);
/*  72:    */     } else {
/*  73:134 */       ret = -this.mean * FastMath.log(1.0D - p);
/*  74:    */     }
/*  75:137 */     return ret;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public double sample()
/*  79:    */   {
/*  80:153 */     return this.randomData.nextExponential(this.mean);
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected double getSolverAbsoluteAccuracy()
/*  84:    */   {
/*  85:159 */     return this.solverAbsoluteAccuracy;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public double getNumericalMean()
/*  89:    */   {
/*  90:168 */     return getMean();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public double getNumericalVariance()
/*  94:    */   {
/*  95:177 */     double m = getMean();
/*  96:178 */     return m * m;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public double getSupportLowerBound()
/* 100:    */   {
/* 101:189 */     return 0.0D;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public double getSupportUpperBound()
/* 105:    */   {
/* 106:201 */     return (1.0D / 0.0D);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean isSupportLowerBoundInclusive()
/* 110:    */   {
/* 111:206 */     return true;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isSupportUpperBoundInclusive()
/* 115:    */   {
/* 116:211 */     return false;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean isSupportConnected()
/* 120:    */   {
/* 121:222 */     return true;
/* 122:    */   }
/* 123:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.ExponentialDistribution
 * JD-Core Version:    0.7.0.1
 */
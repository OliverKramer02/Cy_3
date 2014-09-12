/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
/*   6:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   7:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   8:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   9:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  10:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public abstract class AbstractRealDistribution
/*  14:    */   implements RealDistribution, Serializable
/*  15:    */ {
/*  16:    */   public static final double SOLVER_DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*  17:    */   private static final long serialVersionUID = -38038050983108802L;
/*  18: 47 */   protected final RandomDataImpl randomData = new RandomDataImpl();
/*  19: 50 */   private double solverAbsoluteAccuracy = 1.0E-006D;
/*  20:    */   
/*  21:    */   public double cumulativeProbability(double x0, double x1)
/*  22:    */     throws NumberIsTooLargeException
/*  23:    */   {
/*  24: 62 */     if (x0 > x1) {
/*  25: 63 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, Double.valueOf(x0), Double.valueOf(x1), true);
/*  26:    */     }
/*  27: 66 */     return cumulativeProbability(x1) - cumulativeProbability(x0);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public double inverseCumulativeProbability(final double p)
/*  31:    */     throws OutOfRangeException
/*  32:    */   {
/*  33:107 */     if ((p < 0.0D) || (p > 1.0D)) {
/*  34:108 */       throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
/*  35:    */     }
/*  36:111 */     double lowerBound = getSupportLowerBound();
/*  37:112 */     if (p == 0.0D) {
/*  38:113 */       return lowerBound;
/*  39:    */     }
/*  40:116 */     double upperBound = getSupportUpperBound();
/*  41:117 */     if (p == 1.0D) {
/*  42:118 */       return upperBound;
/*  43:    */     }
/*  44:121 */     double mu = getNumericalMean();
/*  45:122 */     double sig = FastMath.sqrt(getNumericalVariance());
/*  46:    */     
/*  47:124 */     boolean chebyshevApplies = (!Double.isInfinite(mu)) && (!Double.isNaN(mu)) && (!Double.isInfinite(sig)) && (!Double.isNaN(sig));
/*  48:127 */     if (lowerBound == (-1.0D / 0.0D)) {
/*  49:128 */       if (chebyshevApplies)
/*  50:    */       {
/*  51:129 */         lowerBound = mu - sig * FastMath.sqrt((1.0D - p) / p);
/*  52:    */       }
/*  53:    */       else
/*  54:    */       {
/*  55:131 */         lowerBound = -1.0D;
/*  56:132 */         while (cumulativeProbability(lowerBound) >= p) {
/*  57:133 */           lowerBound *= 2.0D;
/*  58:    */         }
/*  59:    */       }
/*  60:    */     }
/*  61:138 */     if (upperBound == (1.0D / 0.0D)) {
/*  62:139 */       if (chebyshevApplies)
/*  63:    */       {
/*  64:140 */         upperBound = mu + sig * FastMath.sqrt(p / (1.0D - p));
/*  65:    */       }
/*  66:    */       else
/*  67:    */       {
/*  68:142 */         upperBound = 1.0D;
/*  69:143 */         while (cumulativeProbability(upperBound) < p) {
/*  70:144 */           upperBound *= 2.0D;
/*  71:    */         }
/*  72:    */       }
/*  73:    */     }
/*  74:149 */     UnivariateFunction toSolve = new UnivariateFunction()
/*  75:    */     {
/*  76:    */       public double value(double x)
/*  77:    */       {
/*  78:152 */         return AbstractRealDistribution.this.cumulativeProbability(x) - p;
/*  79:    */       }
/*  80:155 */     };
/*  81:156 */     double x = UnivariateSolverUtils.solve(toSolve, lowerBound, upperBound, getSolverAbsoluteAccuracy());
/*  82:161 */     if (!isSupportConnected())
/*  83:    */     {
/*  84:163 */       double dx = getSolverAbsoluteAccuracy();
/*  85:164 */       if (x - dx >= getSupportLowerBound())
/*  86:    */       {
/*  87:165 */         double px = cumulativeProbability(x);
/*  88:166 */         if (cumulativeProbability(x - dx) == px)
/*  89:    */         {
/*  90:167 */           upperBound = x;
/*  91:168 */           while (upperBound - lowerBound > dx)
/*  92:    */           {
/*  93:169 */             double midPoint = 0.5D * (lowerBound + upperBound);
/*  94:170 */             if (cumulativeProbability(midPoint) < px) {
/*  95:171 */               lowerBound = midPoint;
/*  96:    */             } else {
/*  97:173 */               upperBound = midPoint;
/*  98:    */             }
/*  99:    */           }
/* 100:176 */           return upperBound;
/* 101:    */         }
/* 102:    */       }
/* 103:    */     }
/* 104:180 */     return x;
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected double getSolverAbsoluteAccuracy()
/* 108:    */   {
/* 109:191 */     return this.solverAbsoluteAccuracy;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void reseedRandomGenerator(long seed)
/* 113:    */   {
/* 114:196 */     this.randomData.reSeed(seed);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public double sample()
/* 118:    */   {
/* 119:208 */     return this.randomData.nextInversionDeviate(this);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public double[] sample(int sampleSize)
/* 123:    */   {
/* 124:218 */     if (sampleSize <= 0) {
/* 125:219 */       throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
/* 126:    */     }
/* 127:222 */     double[] out = new double[sampleSize];
/* 128:223 */     for (int i = 0; i < sampleSize; i++) {
/* 129:224 */       out[i] = sample();
/* 130:    */     }
/* 131:226 */     return out;
/* 132:    */   }
/* 133:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.AbstractRealDistribution
 * JD-Core Version:    0.7.0.1
 */
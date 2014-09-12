/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*   6:    */ import org.apache.commons.math3.special.Gamma;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class PoissonDistribution
/*  10:    */   extends AbstractIntegerDistribution
/*  11:    */ {
/*  12:    */   public static final int DEFAULT_MAX_ITERATIONS = 10000000;
/*  13:    */   public static final double DEFAULT_EPSILON = 1.0E-012D;
/*  14:    */   private static final long serialVersionUID = -3349935121172596109L;
/*  15:    */   private final NormalDistribution normal;
/*  16:    */   private final double mean;
/*  17:    */   private final int maxIterations;
/*  18:    */   private final double epsilon;
/*  19:    */   
/*  20:    */   public PoissonDistribution(double p)
/*  21:    */     throws NotStrictlyPositiveException
/*  22:    */   {
/*  23: 73 */     this(p, 1.0E-012D, 10000000);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public PoissonDistribution(double p, double epsilon, int maxIterations)
/*  27:    */     throws NotStrictlyPositiveException
/*  28:    */   {
/*  29: 89 */     if (p <= 0.0D) {
/*  30: 90 */       throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, Double.valueOf(p));
/*  31:    */     }
/*  32: 92 */     this.mean = p;
/*  33: 93 */     this.normal = new NormalDistribution(p, FastMath.sqrt(p));
/*  34: 94 */     this.epsilon = epsilon;
/*  35: 95 */     this.maxIterations = maxIterations;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public PoissonDistribution(double p, double epsilon)
/*  39:    */     throws NotStrictlyPositiveException
/*  40:    */   {
/*  41:109 */     this(p, epsilon, 10000000);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public PoissonDistribution(double p, int maxIterations)
/*  45:    */   {
/*  46:122 */     this(p, 1.0E-012D, maxIterations);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double getMean()
/*  50:    */   {
/*  51:131 */     return this.mean;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public double probability(int x)
/*  55:    */   {
/*  56:    */     double ret;
/*  57:    */    
/*  58:137 */     if ((x < 0) || (x == 2147483647))
/*  59:    */     {
/*  60:138 */       ret = 0.0D;
/*  61:    */     }
/*  62:    */     else
/*  63:    */     {
/*  64:    */    
/*  65:139 */       if (x == 0) {
/*  66:140 */         ret = FastMath.exp(-this.mean);
/*  67:    */       } else {
/*  68:142 */         ret = FastMath.exp(-SaddlePointExpansion.getStirlingError(x) - SaddlePointExpansion.getDeviancePart(x, this.mean)) / FastMath.sqrt(6.283185307179586D * x);
/*  69:    */       }
/*  70:    */     }
/*  71:146 */     return ret;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public double cumulativeProbability(int x)
/*  75:    */   {
/*  76:151 */     if (x < 0) {
/*  77:152 */       return 0.0D;
/*  78:    */     }
/*  79:154 */     if (x == 2147483647) {
/*  80:155 */       return 1.0D;
/*  81:    */     }
/*  82:157 */     return Gamma.regularizedGammaQ(x + 1.0D, this.mean, this.epsilon, this.maxIterations);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public double normalApproximateProbability(int x)
/*  86:    */   {
/*  87:174 */     return this.normal.cumulativeProbability(x + 0.5D);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public double getNumericalMean()
/*  91:    */   {
/*  92:183 */     return getMean();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public double getNumericalVariance()
/*  96:    */   {
/*  97:192 */     return getMean();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public int getSupportLowerBound()
/* 101:    */   {
/* 102:203 */     return 0;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public int getSupportUpperBound()
/* 106:    */   {
/* 107:217 */     return 2147483647;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isSupportConnected()
/* 111:    */   {
/* 112:228 */     return true;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public int sample()
/* 116:    */   {
/* 117:255 */     return (int)FastMath.min(this.randomData.nextPoisson(this.mean), 2147483647L);
/* 118:    */   }
/* 119:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.PoissonDistribution
 * JD-Core Version:    0.7.0.1
 */
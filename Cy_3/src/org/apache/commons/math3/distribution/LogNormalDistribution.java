/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*   7:    */ import org.apache.commons.math3.special.Erf;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class LogNormalDistribution
/*  11:    */   extends AbstractRealDistribution
/*  12:    */ {
/*  13:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  14:    */   private static final long serialVersionUID = 20120112L;
/*  15: 62 */   private static final double SQRT2PI = FastMath.sqrt(6.283185307179586D);
/*  16: 65 */   private static final double SQRT2 = FastMath.sqrt(2.0D);
/*  17:    */   private final double scale;
/*  18:    */   private final double shape;
/*  19:    */   private final double solverAbsoluteAccuracy;
/*  20:    */   
/*  21:    */   public LogNormalDistribution(double scale, double shape)
/*  22:    */     throws NotStrictlyPositiveException
/*  23:    */   {
/*  24: 85 */     this(scale, shape, 1.E-009D);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public LogNormalDistribution(double scale, double shape, double inverseCumAccuracy)
/*  28:    */     throws NotStrictlyPositiveException
/*  29:    */   {
/*  30: 99 */     if (shape <= 0.0D) {
/*  31:100 */       throw new NotStrictlyPositiveException(LocalizedFormats.SHAPE, Double.valueOf(shape));
/*  32:    */     }
/*  33:103 */     this.scale = scale;
/*  34:104 */     this.shape = shape;
/*  35:105 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public LogNormalDistribution()
/*  39:    */   {
/*  40:116 */     this(0.0D, 1.0D);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double getScale()
/*  44:    */   {
/*  45:125 */     return this.scale;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public double getShape()
/*  49:    */   {
/*  50:134 */     return this.shape;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public double probability(double x)
/*  54:    */   {
/*  55:145 */     return 0.0D;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public double density(double x)
/*  59:    */   {
/*  60:160 */     if (x <= 0.0D) {
/*  61:161 */       return 0.0D;
/*  62:    */     }
/*  63:163 */     double x0 = FastMath.log(x) - this.scale;
/*  64:164 */     double x1 = x0 / this.shape;
/*  65:165 */     return FastMath.exp(-0.5D * x1 * x1) / (this.shape * SQRT2PI * x);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public double cumulativeProbability(double x)
/*  69:    */   {
/*  70:184 */     if (x <= 0.0D) {
/*  71:185 */       return 0.0D;
/*  72:    */     }
/*  73:187 */     double dev = FastMath.log(x) - this.scale;
/*  74:188 */     if (FastMath.abs(dev) > 40.0D * this.shape) {
/*  75:189 */       return dev < 0.0D ? 0.0D : 1.0D;
/*  76:    */     }
/*  77:191 */     return 0.5D + 0.5D * Erf.erf(dev / (this.shape * SQRT2));
/*  78:    */   }
/*  79:    */   
/*  80:    */   public double cumulativeProbability(double x0, double x1)
/*  81:    */     throws NumberIsTooLargeException
/*  82:    */   {
/*  83:198 */     if (x0 > x1) {
/*  84:199 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, Double.valueOf(x0), Double.valueOf(x1), true);
/*  85:    */     }
/*  86:202 */     if ((x0 <= 0.0D) || (x1 <= 0.0D)) {
/*  87:203 */       return super.cumulativeProbability(x0, x1);
/*  88:    */     }
/*  89:205 */     double denom = this.shape * SQRT2;
/*  90:206 */     double v0 = (FastMath.log(x0) - this.scale) / denom;
/*  91:207 */     double v1 = (FastMath.log(x1) - this.scale) / denom;
/*  92:208 */     return 0.5D * Erf.erf(v0, v1);
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected double getSolverAbsoluteAccuracy()
/*  96:    */   {
/*  97:214 */     return this.solverAbsoluteAccuracy;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public double getNumericalMean()
/* 101:    */   {
/* 102:224 */     double s = this.shape;
/* 103:225 */     return FastMath.exp(this.scale + s * s / 2.0D);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public double getNumericalVariance()
/* 107:    */   {
/* 108:235 */     double s = this.shape;
/* 109:236 */     double ss = s * s;
/* 110:237 */     return (FastMath.exp(ss) - 1.0D) * FastMath.exp(2.0D * this.scale + ss);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public double getSupportLowerBound()
/* 114:    */   {
/* 115:248 */     return 0.0D;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public double getSupportUpperBound()
/* 119:    */   {
/* 120:261 */     return (1.0D / 0.0D);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public boolean isSupportLowerBoundInclusive()
/* 124:    */   {
/* 125:266 */     return true;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public boolean isSupportUpperBoundInclusive()
/* 129:    */   {
/* 130:271 */     return false;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean isSupportConnected()
/* 134:    */   {
/* 135:282 */     return true;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public double sample()
/* 139:    */   {
/* 140:288 */     double n = this.randomData.nextGaussian(0.0D, 1.0D);
/* 141:289 */     return FastMath.exp(this.scale + this.shape * n);
/* 142:    */   }
/* 143:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.LogNormalDistribution
 * JD-Core Version:    0.7.0.1
 */
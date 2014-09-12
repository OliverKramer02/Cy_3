/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ 
/*   8:    */ public class CauchyDistribution
/*   9:    */   extends AbstractRealDistribution
/*  10:    */ {
/*  11:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  12:    */   private static final long serialVersionUID = 8589540077390120676L;
/*  13:    */   private final double median;
/*  14:    */   private final double scale;
/*  15:    */   private final double solverAbsoluteAccuracy;
/*  16:    */   
/*  17:    */   public CauchyDistribution()
/*  18:    */   {
/*  19: 52 */     this(0.0D, 1.0D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public CauchyDistribution(double median, double scale)
/*  23:    */   {
/*  24: 62 */     this(median, scale, 1.E-009D);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public CauchyDistribution(double median, double scale, double inverseCumAccuracy)
/*  28:    */   {
/*  29: 78 */     if (scale <= 0.0D) {
/*  30: 79 */       throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(scale));
/*  31:    */     }
/*  32: 81 */     this.scale = scale;
/*  33: 82 */     this.median = median;
/*  34: 83 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double cumulativeProbability(double x)
/*  38:    */   {
/*  39: 88 */     return 0.5D + FastMath.atan((x - this.median) / this.scale) / 3.141592653589793D;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double getMedian()
/*  43:    */   {
/*  44: 97 */     return this.median;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getScale()
/*  48:    */   {
/*  49:106 */     return this.scale;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public double probability(double x)
/*  53:    */   {
/*  54:117 */     return 0.0D;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public double density(double x)
/*  58:    */   {
/*  59:122 */     double dev = x - this.median;
/*  60:123 */     return 0.3183098861837907D * (this.scale / (dev * dev + this.scale * this.scale));
/*  61:    */   }
/*  62:    */   
/*  63:    */   public double inverseCumulativeProbability(double p)
/*  64:    */     throws OutOfRangeException
/*  65:    */   {
/*  66:135 */     if ((p < 0.0D) || (p > 1.0D)) {
/*  67:136 */       throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
/*  68:    */     }
/*  69:    */     double ret;
/*  70:    */  
/*  71:137 */     if (p == 0.0D)
/*  72:    */     {
/*  73:138 */       ret = (-1.0D / 0.0D);
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77:    */      
/*  78:139 */       if (p == 1.0D) {
/*  79:140 */         ret = (1.0D / 0.0D);
/*  80:    */       } else {
/*  81:142 */         ret = this.median + this.scale * FastMath.tan(3.141592653589793D * (p - 0.5D));
/*  82:    */       }
/*  83:    */     }
/*  84:144 */     return ret;
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected double getSolverAbsoluteAccuracy()
/*  88:    */   {
/*  89:150 */     return this.solverAbsoluteAccuracy;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public double getNumericalMean()
/*  93:    */   {
/*  94:161 */     return (0.0D / 0.0D);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public double getNumericalVariance()
/*  98:    */   {
/*  99:172 */     return (0.0D / 0.0D);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public double getSupportLowerBound()
/* 103:    */   {
/* 104:184 */     return (-1.0D / 0.0D);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public double getSupportUpperBound()
/* 108:    */   {
/* 109:196 */     return (1.0D / 0.0D);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean isSupportLowerBoundInclusive()
/* 113:    */   {
/* 114:201 */     return false;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isSupportUpperBoundInclusive()
/* 118:    */   {
/* 119:206 */     return false;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean isSupportConnected()
/* 123:    */   {
/* 124:217 */     return true;
/* 125:    */   }
/* 126:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.CauchyDistribution
 * JD-Core Version:    0.7.0.1
 */
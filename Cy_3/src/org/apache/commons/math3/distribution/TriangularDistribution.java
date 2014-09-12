/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   4:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   5:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class TriangularDistribution
/*  10:    */   extends AbstractRealDistribution
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = 20120112L;
/*  13:    */   private final double a;
/*  14:    */   private final double b;
/*  15:    */   private final double c;
/*  16:    */   private final double solverAbsoluteAccuracy;
/*  17:    */   
/*  18:    */   public TriangularDistribution(double a, double c, double b)
/*  19:    */     throws NumberIsTooLargeException, NumberIsTooSmallException
/*  20:    */   {
/*  21: 63 */     if (a >= b) {
/*  22: 64 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(a), Double.valueOf(b), false);
/*  23:    */     }
/*  24: 68 */     if (c < a) {
/*  25: 69 */       throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_TOO_SMALL, Double.valueOf(c), Double.valueOf(a), true);
/*  26:    */     }
/*  27: 72 */     if (c > b) {
/*  28: 73 */       throw new NumberIsTooLargeException(LocalizedFormats.NUMBER_TOO_LARGE, Double.valueOf(c), Double.valueOf(b), true);
/*  29:    */     }
/*  30: 77 */     this.a = a;
/*  31: 78 */     this.c = c;
/*  32: 79 */     this.b = b;
/*  33: 80 */     this.solverAbsoluteAccuracy = FastMath.max(FastMath.ulp(a), FastMath.ulp(b));
/*  34:    */   }
/*  35:    */   
/*  36:    */   public double getMode()
/*  37:    */   {
/*  38: 89 */     return this.c;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected double getSolverAbsoluteAccuracy()
/*  42:    */   {
/*  43:107 */     return this.solverAbsoluteAccuracy;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public double probability(double x)
/*  47:    */   {
/*  48:118 */     return 0.0D;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double density(double x)
/*  52:    */   {
/*  53:134 */     if (x < this.a) {
/*  54:135 */       return 0.0D;
/*  55:    */     }
/*  56:137 */     if ((this.a <= x) && (x < this.c))
/*  57:    */     {
/*  58:138 */       double divident = 2.0D * (x - this.a);
/*  59:139 */       double divisor = (this.b - this.a) * (this.c - this.a);
/*  60:140 */       return divident / divisor;
/*  61:    */     }
/*  62:142 */     if (x == this.c) {
/*  63:143 */       return 2.0D / (this.b - this.a);
/*  64:    */     }
/*  65:145 */     if ((this.c < x) && (x <= this.b))
/*  66:    */     {
/*  67:146 */       double divident = 2.0D * (this.b - x);
/*  68:147 */       double divisor = (this.b - this.a) * (this.b - this.c);
/*  69:148 */       return divident / divisor;
/*  70:    */     }
/*  71:150 */     return 0.0D;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public double cumulativeProbability(double x)
/*  75:    */   {
/*  76:167 */     if (x < this.a) {
/*  77:168 */       return 0.0D;
/*  78:    */     }
/*  79:170 */     if ((this.a <= x) && (x < this.c))
/*  80:    */     {
/*  81:171 */       double divident = (x - this.a) * (x - this.a);
/*  82:172 */       double divisor = (this.b - this.a) * (this.c - this.a);
/*  83:173 */       return divident / divisor;
/*  84:    */     }
/*  85:175 */     if (x == this.c) {
/*  86:176 */       return (this.c - this.a) / (this.b - this.a);
/*  87:    */     }
/*  88:178 */     if ((this.c < x) && (x <= this.b))
/*  89:    */     {
/*  90:179 */       double divident = (this.b - x) * (this.b - x);
/*  91:180 */       double divisor = (this.b - this.a) * (this.b - this.c);
/*  92:181 */       return 1.0D - divident / divisor;
/*  93:    */     }
/*  94:183 */     return 1.0D;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public double getNumericalMean()
/*  98:    */   {
/*  99:193 */     return (this.a + this.b + this.c) / 3.0D;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public double getNumericalVariance()
/* 103:    */   {
/* 104:203 */     return (this.a * this.a + this.b * this.b + this.c * this.c - this.a * this.b - this.a * this.c - this.b * this.c) / 18.0D;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public double getSupportLowerBound()
/* 108:    */   {
/* 109:215 */     return this.a;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public double getSupportUpperBound()
/* 113:    */   {
/* 114:227 */     return this.b;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isSupportLowerBoundInclusive()
/* 118:    */   {
/* 119:232 */     return true;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean isSupportUpperBoundInclusive()
/* 123:    */   {
/* 124:237 */     return true;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean isSupportConnected()
/* 128:    */   {
/* 129:248 */     return true;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public double inverseCumulativeProbability(double p)
/* 133:    */     throws OutOfRangeException
/* 134:    */   {
/* 135:254 */     if ((p < 0.0D) || (p > 1.0D)) {
/* 136:255 */       throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
/* 137:    */     }
/* 138:257 */     if (p == 0.0D) {
/* 139:258 */       return this.a;
/* 140:    */     }
/* 141:260 */     if (p == 1.0D) {
/* 142:261 */       return this.b;
/* 143:    */     }
/* 144:263 */     if (p < (this.c - this.a) / (this.b - this.a)) {
/* 145:264 */       return this.a + FastMath.sqrt(p * (this.b - this.a) * (this.c - this.a));
/* 146:    */     }
/* 147:266 */     return this.b - FastMath.sqrt((1.0D - p) * (this.b - this.a) * (this.b - this.c));
/* 148:    */   }
/* 149:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.TriangularDistribution
 * JD-Core Version:    0.7.0.1
 */
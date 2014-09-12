/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*   6:    */ 
/*   7:    */ public class UniformRealDistribution
/*   8:    */   extends AbstractRealDistribution
/*   9:    */ {
/*  10:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*  11:    */   private static final long serialVersionUID = 20120109L;
/*  12:    */   private final double lower;
/*  13:    */   private final double upper;
/*  14:    */   private final double solverAbsoluteAccuracy;
/*  15:    */   
/*  16:    */   public UniformRealDistribution(double lower, double upper)
/*  17:    */     throws NumberIsTooLargeException
/*  18:    */   {
/*  19: 58 */     this(lower, upper, 1.E-009D);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public UniformRealDistribution(double lower, double upper, double inverseCumAccuracy)
/*  23:    */     throws NumberIsTooLargeException
/*  24:    */   {
/*  25: 72 */     if (lower >= upper) {
/*  26: 73 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(lower), Double.valueOf(upper), false);
/*  27:    */     }
/*  28: 78 */     this.lower = lower;
/*  29: 79 */     this.upper = upper;
/*  30: 80 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public UniformRealDistribution()
/*  34:    */   {
/*  35: 88 */     this(0.0D, 1.0D);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public double probability(double x)
/*  39:    */   {
/*  40: 99 */     return 0.0D;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double density(double x)
/*  44:    */   {
/*  45:104 */     if ((x < this.lower) || (x > this.upper)) {
/*  46:105 */       return 0.0D;
/*  47:    */     }
/*  48:107 */     return 1.0D / (this.upper - this.lower);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double cumulativeProbability(double x)
/*  52:    */   {
/*  53:112 */     if (x <= this.lower) {
/*  54:113 */       return 0.0D;
/*  55:    */     }
/*  56:115 */     if (x >= this.upper) {
/*  57:116 */       return 1.0D;
/*  58:    */     }
/*  59:118 */     return (x - this.lower) / (this.upper - this.lower);
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected double getSolverAbsoluteAccuracy()
/*  63:    */   {
/*  64:124 */     return this.solverAbsoluteAccuracy;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public double getNumericalMean()
/*  68:    */   {
/*  69:134 */     return 0.5D * (this.lower + this.upper);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public double getNumericalVariance()
/*  73:    */   {
/*  74:144 */     double ul = this.upper - this.lower;
/*  75:145 */     return ul * ul / 12.0D;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public double getSupportLowerBound()
/*  79:    */   {
/*  80:157 */     return this.lower;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public double getSupportUpperBound()
/*  84:    */   {
/*  85:169 */     return this.upper;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean isSupportLowerBoundInclusive()
/*  89:    */   {
/*  90:174 */     return true;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean isSupportUpperBoundInclusive()
/*  94:    */   {
/*  95:179 */     return false;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public boolean isSupportConnected()
/*  99:    */   {
/* 100:190 */     return true;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double sample()
/* 104:    */   {
/* 105:196 */     return this.randomData.nextUniform(this.lower, this.upper, true);
/* 106:    */   }
/* 107:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.UniformRealDistribution
 * JD-Core Version:    0.7.0.1
 */
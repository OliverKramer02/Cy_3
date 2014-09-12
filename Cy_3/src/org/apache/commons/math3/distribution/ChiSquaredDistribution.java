/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ public class ChiSquaredDistribution
/*   4:    */   extends AbstractRealDistribution
/*   5:    */ {
/*   6:    */   public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.E-009D;
/*   7:    */   private static final long serialVersionUID = -8352658048349159782L;
/*   8:    */   private final GammaDistribution gamma;
/*   9:    */   private final double solverAbsoluteAccuracy;
/*  10:    */   
/*  11:    */   public ChiSquaredDistribution(double degreesOfFreedom)
/*  12:    */   {
/*  13: 45 */     this(degreesOfFreedom, 1.E-009D);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public ChiSquaredDistribution(double degreesOfFreedom, double inverseCumAccuracy)
/*  17:    */   {
/*  18: 60 */     this.gamma = new GammaDistribution(degreesOfFreedom / 2.0D, 2.0D);
/*  19: 61 */     this.solverAbsoluteAccuracy = inverseCumAccuracy;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public double getDegreesOfFreedom()
/*  23:    */   {
/*  24: 70 */     return this.gamma.getAlpha() * 2.0D;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public double probability(double x)
/*  28:    */   {
/*  29: 81 */     return 0.0D;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public double density(double x)
/*  33:    */   {
/*  34: 86 */     return this.gamma.density(x);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double cumulativeProbability(double x)
/*  38:    */   {
/*  39: 91 */     return this.gamma.cumulativeProbability(x);
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected double getSolverAbsoluteAccuracy()
/*  43:    */   {
/*  44: 97 */     return this.solverAbsoluteAccuracy;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getNumericalMean()
/*  48:    */   {
/*  49:106 */     return getDegreesOfFreedom();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public double getNumericalVariance()
/*  53:    */   {
/*  54:117 */     return 2.0D * getDegreesOfFreedom();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public double getSupportLowerBound()
/*  58:    */   {
/*  59:129 */     return 0.0D;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public double getSupportUpperBound()
/*  63:    */   {
/*  64:141 */     return (1.0D / 0.0D);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean isSupportLowerBoundInclusive()
/*  68:    */   {
/*  69:146 */     return true;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean isSupportUpperBoundInclusive()
/*  73:    */   {
/*  74:151 */     return false;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public boolean isSupportConnected()
/*  78:    */   {
/*  79:162 */     return true;
/*  80:    */   }
/*  81:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.ChiSquaredDistribution
 * JD-Core Version:    0.7.0.1
 */
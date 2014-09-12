/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.special.Beta;
/*   7:    */ import org.apache.commons.math3.util.ArithmeticUtils;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class PascalDistribution
/*  11:    */   extends AbstractIntegerDistribution
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = 6751309484392813623L;
/*  14:    */   private final int numberOfSuccesses;
/*  15:    */   private final double probabilityOfSuccess;
/*  16:    */   
/*  17:    */   public PascalDistribution(int r, double p)
/*  18:    */     throws NotStrictlyPositiveException, OutOfRangeException
/*  19:    */   {
/*  20: 82 */     if (r <= 0) {
/*  21: 83 */       throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SUCCESSES, Integer.valueOf(r));
/*  22:    */     }
/*  23: 86 */     if ((p < 0.0D) || (p > 1.0D)) {
/*  24: 87 */       throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
/*  25:    */     }
/*  26: 90 */     this.numberOfSuccesses = r;
/*  27: 91 */     this.probabilityOfSuccess = p;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public int getNumberOfSuccesses()
/*  31:    */   {
/*  32:100 */     return this.numberOfSuccesses;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public double getProbabilityOfSuccess()
/*  36:    */   {
/*  37:109 */     return this.probabilityOfSuccess;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public double probability(int x)
/*  41:    */   {
/*  42:    */     double ret;
/*  43:    */    
/*  44:115 */     if (x < 0) {
/*  45:116 */       ret = 0.0D;
/*  46:    */     } else {
/*  47:118 */       ret = ArithmeticUtils.binomialCoefficientDouble(x + this.numberOfSuccesses - 1, this.numberOfSuccesses - 1) * FastMath.pow(this.probabilityOfSuccess, this.numberOfSuccesses) * FastMath.pow(1.0D - this.probabilityOfSuccess, x);
/*  48:    */     }
/*  49:123 */     return ret;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public double cumulativeProbability(int x)
/*  53:    */   {
/*  54:    */     double ret;
/*  55:    */  
/*  56:129 */     if (x < 0) {
/*  57:130 */       ret = 0.0D;
/*  58:    */     } else {
/*  59:132 */       ret = Beta.regularizedBeta(this.probabilityOfSuccess, this.numberOfSuccesses, x + 1);
/*  60:    */     }
/*  61:135 */     return ret;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public double getNumericalMean()
/*  65:    */   {
/*  66:145 */     double p = getProbabilityOfSuccess();
/*  67:146 */     double r = getNumberOfSuccesses();
/*  68:147 */     return r * (1.0D - p) / p;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public double getNumericalVariance()
/*  72:    */   {
/*  73:157 */     double p = getProbabilityOfSuccess();
/*  74:158 */     double r = getNumberOfSuccesses();
/*  75:159 */     return r * (1.0D - p) / (p * p);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int getSupportLowerBound()
/*  79:    */   {
/*  80:170 */     return 0;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public int getSupportUpperBound()
/*  84:    */   {
/*  85:183 */     return 2147483647;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean isSupportConnected()
/*  89:    */   {
/*  90:194 */     return true;
/*  91:    */   }
/*  92:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.PascalDistribution
 * JD-Core Version:    0.7.0.1
 */
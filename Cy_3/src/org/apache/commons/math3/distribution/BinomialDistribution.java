/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.special.Beta;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class BinomialDistribution
/*  10:    */   extends AbstractIntegerDistribution
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = 6751309484392813623L;
/*  13:    */   private final int numberOfTrials;
/*  14:    */   private final double probabilityOfSuccess;
/*  15:    */   
/*  16:    */   public BinomialDistribution(int trials, double p)
/*  17:    */   {
/*  18: 50 */     if (trials < 0) {
/*  19: 51 */       throw new NotPositiveException(LocalizedFormats.NUMBER_OF_TRIALS, Integer.valueOf(trials));
/*  20:    */     }
/*  21: 54 */     if ((p < 0.0D) || (p > 1.0D)) {
/*  22: 55 */       throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
/*  23:    */     }
/*  24: 58 */     this.probabilityOfSuccess = p;
/*  25: 59 */     this.numberOfTrials = trials;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public int getNumberOfTrials()
/*  29:    */   {
/*  30: 68 */     return this.numberOfTrials;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public double getProbabilityOfSuccess()
/*  34:    */   {
/*  35: 77 */     return this.probabilityOfSuccess;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public double probability(int x)
/*  39:    */   {
/*  40:    */     double ret;
/*  41:    */    
/*  42: 83 */     if ((x < 0) || (x > this.numberOfTrials)) {
/*  43: 84 */       ret = 0.0D;
/*  44:    */     } else {
/*  45: 86 */       ret = FastMath.exp(SaddlePointExpansion.logBinomialProbability(x, this.numberOfTrials, this.probabilityOfSuccess, 1.0D - this.probabilityOfSuccess));
/*  46:    */     }
/*  47: 90 */     return ret;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double cumulativeProbability(int x)
/*  51:    */   {
/*  52:    */     double ret;
/*  53:    */    
/*  54: 96 */     if (x < 0)
/*  55:    */     {
/*  56: 97 */       ret = 0.0D;
/*  57:    */     }
/*  58:    */     else
/*  59:    */     {
/*  60:    */      
/*  61: 98 */       if (x >= this.numberOfTrials) {
/*  62: 99 */         ret = 1.0D;
/*  63:    */       } else {
/*  64:101 */         ret = 1.0D - Beta.regularizedBeta(this.probabilityOfSuccess, x + 1.0D, this.numberOfTrials - x);
/*  65:    */       }
/*  66:    */     }
/*  67:104 */     return ret;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public double getNumericalMean()
/*  71:    */   {
/*  72:114 */     return this.numberOfTrials * this.probabilityOfSuccess;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public double getNumericalVariance()
/*  76:    */   {
/*  77:124 */     double p = this.probabilityOfSuccess;
/*  78:125 */     return this.numberOfTrials * p * (1.0D - p);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int getSupportLowerBound()
/*  82:    */   {
/*  83:137 */     return this.probabilityOfSuccess < 1.0D ? 0 : this.numberOfTrials;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int getSupportUpperBound()
/*  87:    */   {
/*  88:149 */     return this.probabilityOfSuccess > 0.0D ? this.numberOfTrials : 0;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean isSupportConnected()
/*  92:    */   {
/*  93:160 */     return true;
/*  94:    */   }
/*  95:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.BinomialDistribution
 * JD-Core Version:    0.7.0.1
 */
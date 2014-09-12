/*   1:    */ package org.apache.commons.math3.stat.correlation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ 
/*   6:    */ class StorelessBivariateCovariance
/*   7:    */ {
/*   8:    */   private double meanX;
/*   9:    */   private double meanY;
/*  10:    */   private double n;
/*  11:    */   private double covarianceNumerator;
/*  12:    */   private boolean biasCorrected;
/*  13:    */   
/*  14:    */   public StorelessBivariateCovariance()
/*  15:    */   {
/*  16: 61 */     this(true);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public StorelessBivariateCovariance(boolean biasCorrection)
/*  20:    */   {
/*  21: 72 */     this.meanX = (this.meanY = 0.0D);
/*  22: 73 */     this.n = 0.0D;
/*  23: 74 */     this.covarianceNumerator = 0.0D;
/*  24: 75 */     this.biasCorrected = biasCorrection;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void increment(double x, double y)
/*  28:    */   {
/*  29: 85 */     this.n += 1.0D;
/*  30: 86 */     double deltaX = x - this.meanX;
/*  31: 87 */     double deltaY = y - this.meanY;
/*  32: 88 */     this.meanX += deltaX / this.n;
/*  33: 89 */     this.meanY += deltaY / this.n;
/*  34: 90 */     this.covarianceNumerator += (this.n - 1.0D) / this.n * deltaX * deltaY;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double getN()
/*  38:    */   {
/*  39: 99 */     return this.n;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double getResult()
/*  43:    */     throws NumberIsTooSmallException
/*  44:    */   {
/*  45:110 */     if (this.n < 2.0D) {
/*  46:111 */       throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DIMENSION, Double.valueOf(this.n), Integer.valueOf(2), true);
/*  47:    */     }
/*  48:114 */     if (this.biasCorrected) {
/*  49:115 */       return this.covarianceNumerator / (this.n - 1.0D);
/*  50:    */     }
/*  51:117 */     return this.covarianceNumerator / this.n;
/*  52:    */   }
/*  53:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.correlation.StorelessBivariateCovariance
 * JD-Core Version:    0.7.0.1
 */
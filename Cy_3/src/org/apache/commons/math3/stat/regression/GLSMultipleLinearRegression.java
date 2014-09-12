/*   1:    */ package org.apache.commons.math3.stat.regression;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   4:    */ import org.apache.commons.math3.linear.DecompositionSolver;
/*   5:    */ import org.apache.commons.math3.linear.LUDecomposition;
/*   6:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   7:    */ import org.apache.commons.math3.linear.RealVector;
/*   8:    */ 
/*   9:    */ public class GLSMultipleLinearRegression
/*  10:    */   extends AbstractMultipleLinearRegression
/*  11:    */ {
/*  12:    */   private RealMatrix Omega;
/*  13:    */   private RealMatrix OmegaInverse;
/*  14:    */   
/*  15:    */   public void newSampleData(double[] y, double[][] x, double[][] covariance)
/*  16:    */   {
/*  17: 57 */     validateSampleData(x, y);
/*  18: 58 */     newYSampleData(y);
/*  19: 59 */     newXSampleData(x);
/*  20: 60 */     validateCovarianceData(x, covariance);
/*  21: 61 */     newCovarianceData(covariance);
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected void newCovarianceData(double[][] omega)
/*  25:    */   {
/*  26: 70 */     this.Omega = new Array2DRowRealMatrix(omega);
/*  27: 71 */     this.OmegaInverse = null;
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected RealMatrix getOmegaInverse()
/*  31:    */   {
/*  32: 80 */     if (this.OmegaInverse == null) {
/*  33: 81 */       this.OmegaInverse = new LUDecomposition(this.Omega).getSolver().getInverse();
/*  34:    */     }
/*  35: 83 */     return this.OmegaInverse;
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected RealVector calculateBeta()
/*  39:    */   {
/*  40: 95 */     RealMatrix OI = getOmegaInverse();
/*  41: 96 */     RealMatrix XT = getX().transpose();
/*  42: 97 */     RealMatrix XTOIX = XT.multiply(OI).multiply(getX());
/*  43: 98 */     RealMatrix inverse = new LUDecomposition(XTOIX).getSolver().getInverse();
/*  44: 99 */     return inverse.multiply(XT).multiply(OI).operate(getY());
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected RealMatrix calculateBetaVariance()
/*  48:    */   {
/*  49:111 */     RealMatrix OI = getOmegaInverse();
/*  50:112 */     RealMatrix XTOIX = getX().transpose().multiply(OI).multiply(getX());
/*  51:113 */     return new LUDecomposition(XTOIX).getSolver().getInverse();
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected double calculateErrorVariance()
/*  55:    */   {
/*  56:130 */     RealVector residuals = calculateResiduals();
/*  57:131 */     double t = residuals.dotProduct(getOmegaInverse().operate(residuals));
/*  58:132 */     return t / (getX().getRowDimension() - getX().getColumnDimension());
/*  59:    */   }
/*  60:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.GLSMultipleLinearRegression
 * JD-Core Version:    0.7.0.1
 */
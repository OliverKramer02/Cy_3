/*   1:    */ package org.apache.commons.math3.stat.regression;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   4:    */ import org.apache.commons.math3.linear.DecompositionSolver;
/*   5:    */ import org.apache.commons.math3.linear.LUDecomposition;
/*   6:    */ import org.apache.commons.math3.linear.QRDecomposition;
/*   7:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   8:    */ import org.apache.commons.math3.linear.RealVector;
/*   9:    */ import org.apache.commons.math3.stat.StatUtils;
/*  10:    */ import org.apache.commons.math3.stat.descriptive.moment.SecondMoment;
/*  11:    */ 
/*  12:    */ public class OLSMultipleLinearRegression
/*  13:    */   extends AbstractMultipleLinearRegression
/*  14:    */ {
/*  15: 57 */   private QRDecomposition qr = null;
/*  16:    */   
/*  17:    */   public void newSampleData(double[] y, double[][] x)
/*  18:    */   {
/*  19: 69 */     validateSampleData(x, y);
/*  20: 70 */     newYSampleData(y);
/*  21: 71 */     newXSampleData(x);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void newSampleData(double[] data, int nobs, int nvars)
/*  25:    */   {
/*  26: 80 */     super.newSampleData(data, nobs, nvars);
/*  27: 81 */     this.qr = new QRDecomposition(getX());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public RealMatrix calculateHat()
/*  31:    */   {
/*  32:101 */     RealMatrix Q = this.qr.getQ();
/*  33:102 */     int p = this.qr.getR().getColumnDimension();
/*  34:103 */     int n = Q.getColumnDimension();
/*  35:104 */     Array2DRowRealMatrix augI = new Array2DRowRealMatrix(n, n);
/*  36:105 */     double[][] augIData = augI.getDataRef();
/*  37:106 */     for (int i = 0; i < n; i++) {
/*  38:107 */       for (int j = 0; j < n; j++) {
/*  39:108 */         if ((i == j) && (i < p)) {
/*  40:109 */           augIData[i][j] = 1.0D;
/*  41:    */         } else {
/*  42:111 */           augIData[i][j] = 0.0D;
/*  43:    */         }
/*  44:    */       }
/*  45:    */     }
/*  46:117 */     return Q.multiply(augI).multiply(Q.transpose());
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double calculateTotalSumOfSquares()
/*  50:    */   {
/*  51:134 */     if (isNoIntercept()) {
/*  52:135 */       return StatUtils.sumSq(getY().toArray());
/*  53:    */     }
/*  54:137 */     return new SecondMoment().evaluate(getY().toArray());
/*  55:    */   }
/*  56:    */   
/*  57:    */   public double calculateResidualSumOfSquares()
/*  58:    */   {
/*  59:148 */     RealVector residuals = calculateResiduals();
/*  60:149 */     return residuals.dotProduct(residuals);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public double calculateRSquared()
/*  64:    */   {
/*  65:163 */     return 1.0D - calculateResidualSumOfSquares() / calculateTotalSumOfSquares();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public double calculateAdjustedRSquared()
/*  69:    */   {
/*  70:183 */     double n = getX().getRowDimension();
/*  71:184 */     if (isNoIntercept()) {
/*  72:185 */       return 1.0D - (1.0D - calculateRSquared()) * (n / (n - getX().getColumnDimension()));
/*  73:    */     }
/*  74:187 */     return 1.0D - calculateResidualSumOfSquares() * (n - 1.0D) / (calculateTotalSumOfSquares() * (n - getX().getColumnDimension()));
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected void newXSampleData(double[][] x)
/*  78:    */   {
/*  79:199 */     super.newXSampleData(x);
/*  80:200 */     this.qr = new QRDecomposition(getX());
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected RealVector calculateBeta()
/*  84:    */   {
/*  85:210 */     return this.qr.getSolver().solve(getY());
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected RealMatrix calculateBetaVariance()
/*  89:    */   {
/*  90:226 */     int p = getX().getColumnDimension();
/*  91:227 */     RealMatrix Raug = this.qr.getR().getSubMatrix(0, p - 1, 0, p - 1);
/*  92:228 */     RealMatrix Rinv = new LUDecomposition(Raug).getSolver().getInverse();
/*  93:229 */     return Rinv.multiply(Rinv.transpose());
/*  94:    */   }
/*  95:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression
 * JD-Core Version:    0.7.0.1
 */
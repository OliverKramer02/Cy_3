/*   1:    */ package org.apache.commons.math3.stat.correlation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MathUnsupportedOperationException;
/*   5:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   6:    */ import org.apache.commons.math3.linear.MatrixUtils;
/*   7:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   8:    */ 
/*   9:    */ public class StorelessCovariance
/*  10:    */   extends Covariance
/*  11:    */ {
/*  12:    */   private StorelessBivariateCovariance[] covMatrix;
/*  13:    */   private int dimension;
/*  14:    */   
/*  15:    */   public StorelessCovariance(int dim)
/*  16:    */   {
/*  17: 57 */     this(dim, true);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public StorelessCovariance(int dim, boolean biasCorrected)
/*  21:    */   {
/*  22: 70 */     this.dimension = dim;
/*  23: 71 */     this.covMatrix = new StorelessBivariateCovariance[this.dimension * (this.dimension + 1) / 2];
/*  24: 72 */     initializeMatrix(biasCorrected);
/*  25:    */   }
/*  26:    */   
/*  27:    */   private void initializeMatrix(boolean biasCorrected)
/*  28:    */   {
/*  29: 82 */     for (int i = 0; i < this.dimension; i++) {
/*  30: 83 */       for (int j = 0; j < this.dimension; j++) {
/*  31: 84 */         setElement(i, j, new StorelessBivariateCovariance(biasCorrected));
/*  32:    */       }
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   private int indexOf(int i, int j)
/*  37:    */   {
/*  38: 99 */     return j < i ? i * (i + 1) / 2 + j : j * (j + 1) / 2 + i;
/*  39:    */   }
/*  40:    */   
/*  41:    */   private StorelessBivariateCovariance getElement(int i, int j)
/*  42:    */   {
/*  43:109 */     return this.covMatrix[indexOf(i, j)];
/*  44:    */   }
/*  45:    */   
/*  46:    */   private void setElement(int i, int j, StorelessBivariateCovariance cov)
/*  47:    */   {
/*  48:120 */     this.covMatrix[indexOf(i, j)] = cov;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getCovariance(int xIndex, int yIndex)
/*  52:    */     throws NumberIsTooSmallException
/*  53:    */   {
/*  54:136 */     return getElement(xIndex, yIndex).getResult();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void increment(double[] data)
/*  58:    */     throws DimensionMismatchException
/*  59:    */   {
/*  60:150 */     int length = data.length;
/*  61:151 */     if (length != this.dimension) {
/*  62:152 */       throw new DimensionMismatchException(length, this.dimension);
/*  63:    */     }
/*  64:157 */     for (int i = 0; i < length; i++) {
/*  65:158 */       for (int j = i; j < length; j++) {
/*  66:159 */         getElement(i, j).increment(data[i], data[j]);
/*  67:    */       }
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public RealMatrix getCovarianceMatrix()
/*  72:    */     throws NumberIsTooSmallException
/*  73:    */   {
/*  74:172 */     return MatrixUtils.createRealMatrix(getData());
/*  75:    */   }
/*  76:    */   
/*  77:    */   public double[][] getData()
/*  78:    */     throws NumberIsTooSmallException
/*  79:    */   {
/*  80:183 */     double[][] data = new double[this.dimension][this.dimension];
/*  81:184 */     for (int i = 0; i < this.dimension; i++) {
/*  82:185 */       for (int j = 0; j < this.dimension; j++) {
/*  83:186 */         data[i][j] = getElement(i, j).getResult();
/*  84:    */       }
/*  85:    */     }
/*  86:189 */     return data;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getN()
/*  90:    */     throws MathUnsupportedOperationException
/*  91:    */   {
/*  92:204 */     throw new MathUnsupportedOperationException();
/*  93:    */   }
/*  94:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.correlation.StorelessCovariance
 * JD-Core Version:    0.7.0.1
 */
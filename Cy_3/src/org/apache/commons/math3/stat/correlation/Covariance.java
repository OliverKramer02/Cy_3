/*   1:    */ package org.apache.commons.math3.stat.correlation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.linear.BlockRealMatrix;
/*   6:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   7:    */ import org.apache.commons.math3.stat.descriptive.moment.Mean;
/*   8:    */ import org.apache.commons.math3.stat.descriptive.moment.Variance;
/*   9:    */ 
/*  10:    */ public class Covariance
/*  11:    */ {
/*  12:    */   private final RealMatrix covarianceMatrix;
/*  13:    */   private final int n;
/*  14:    */   
/*  15:    */   public Covariance()
/*  16:    */   {
/*  17: 62 */     this.covarianceMatrix = null;
/*  18: 63 */     this.n = 0;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Covariance(double[][] data, boolean biasCorrected)
/*  22:    */   {
/*  23: 82 */     this(new BlockRealMatrix(data), biasCorrected);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Covariance(double[][] data)
/*  27:    */   {
/*  28: 97 */     this(data, true);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Covariance(RealMatrix matrix, boolean biasCorrected)
/*  32:    */   {
/*  33:115 */     checkSufficientData(matrix);
/*  34:116 */     this.n = matrix.getRowDimension();
/*  35:117 */     this.covarianceMatrix = computeCovarianceMatrix(matrix, biasCorrected);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Covariance(RealMatrix matrix)
/*  39:    */   {
/*  40:131 */     this(matrix, true);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public RealMatrix getCovarianceMatrix()
/*  44:    */   {
/*  45:140 */     return this.covarianceMatrix;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int getN()
/*  49:    */   {
/*  50:149 */     return this.n;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected RealMatrix computeCovarianceMatrix(RealMatrix matrix, boolean biasCorrected)
/*  54:    */   {
/*  55:160 */     int dimension = matrix.getColumnDimension();
/*  56:161 */     Variance variance = new Variance(biasCorrected);
/*  57:162 */     RealMatrix outMatrix = new BlockRealMatrix(dimension, dimension);
/*  58:163 */     for (int i = 0; i < dimension; i++)
/*  59:    */     {
/*  60:164 */       for (int j = 0; j < i; j++)
/*  61:    */       {
/*  62:165 */         double cov = covariance(matrix.getColumn(i), matrix.getColumn(j), biasCorrected);
/*  63:166 */         outMatrix.setEntry(i, j, cov);
/*  64:167 */         outMatrix.setEntry(j, i, cov);
/*  65:    */       }
/*  66:169 */       outMatrix.setEntry(i, i, variance.evaluate(matrix.getColumn(i)));
/*  67:    */     }
/*  68:171 */     return outMatrix;
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected RealMatrix computeCovarianceMatrix(RealMatrix matrix)
/*  72:    */   {
/*  73:182 */     return computeCovarianceMatrix(matrix, true);
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected RealMatrix computeCovarianceMatrix(double[][] data, boolean biasCorrected)
/*  77:    */   {
/*  78:193 */     return computeCovarianceMatrix(new BlockRealMatrix(data), biasCorrected);
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected RealMatrix computeCovarianceMatrix(double[][] data)
/*  82:    */   {
/*  83:204 */     return computeCovarianceMatrix(data, true);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public double covariance(double[] xArray, double[] yArray, boolean biasCorrected)
/*  87:    */     throws IllegalArgumentException
/*  88:    */   {
/*  89:221 */     Mean mean = new Mean();
/*  90:222 */     double result = 0.0D;
/*  91:223 */     int length = xArray.length;
/*  92:224 */     if (length != yArray.length) {
/*  93:225 */       throw new MathIllegalArgumentException(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, new Object[] { Integer.valueOf(length), Integer.valueOf(yArray.length) });
/*  94:    */     }
/*  95:227 */     if (length < 2) {
/*  96:228 */       throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, new Object[] { Integer.valueOf(length), Integer.valueOf(2) });
/*  97:    */     }
/*  98:231 */     double xMean = mean.evaluate(xArray);
/*  99:232 */     double yMean = mean.evaluate(yArray);
/* 100:233 */     for (int i = 0; i < length; i++)
/* 101:    */     {
/* 102:234 */       double xDev = xArray[i] - xMean;
/* 103:235 */       double yDev = yArray[i] - yMean;
/* 104:236 */       result += (xDev * yDev - result) / (i + 1);
/* 105:    */     }
/* 106:239 */     return biasCorrected ? result * (length / (length - 1)) : result;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public double covariance(double[] xArray, double[] yArray)
/* 110:    */     throws IllegalArgumentException
/* 111:    */   {
/* 112:256 */     return covariance(xArray, yArray, true);
/* 113:    */   }
/* 114:    */   
/* 115:    */   private void checkSufficientData(RealMatrix matrix)
/* 116:    */   {
/* 117:265 */     int nRows = matrix.getRowDimension();
/* 118:266 */     int nCols = matrix.getColumnDimension();
/* 119:267 */     if ((nRows < 2) || (nCols < 2)) {
/* 120:268 */       throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_ROWS_AND_COLUMNS, new Object[] { Integer.valueOf(nRows), Integer.valueOf(nCols) });
/* 121:    */     }
/* 122:    */   }
/* 123:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.correlation.Covariance
 * JD-Core Version:    0.7.0.1
 */
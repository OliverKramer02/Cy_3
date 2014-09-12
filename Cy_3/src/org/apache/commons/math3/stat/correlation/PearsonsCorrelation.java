/*   1:    */ package org.apache.commons.math3.stat.correlation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.distribution.TDistribution;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.linear.BlockRealMatrix;
/*   9:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  10:    */ import org.apache.commons.math3.stat.regression.SimpleRegression;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public class PearsonsCorrelation
/*  14:    */ {
/*  15:    */   private final RealMatrix correlationMatrix;
/*  16:    */   private final int nObs;
/*  17:    */   
/*  18:    */   public PearsonsCorrelation()
/*  19:    */   {
/*  20: 57 */     this.correlationMatrix = null;
/*  21: 58 */     this.nObs = 0;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public PearsonsCorrelation(double[][] data)
/*  25:    */   {
/*  26: 70 */     this(new BlockRealMatrix(data));
/*  27:    */   }
/*  28:    */   
/*  29:    */   public PearsonsCorrelation(RealMatrix matrix)
/*  30:    */   {
/*  31: 80 */     checkSufficientData(matrix);
/*  32: 81 */     this.nObs = matrix.getRowDimension();
/*  33: 82 */     this.correlationMatrix = computeCorrelationMatrix(matrix);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public PearsonsCorrelation(Covariance covariance)
/*  37:    */   {
/*  38: 94 */     RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
/*  39: 95 */     if (covarianceMatrix == null) {
/*  40: 96 */       throw new NullArgumentException(LocalizedFormats.COVARIANCE_MATRIX, new Object[0]);
/*  41:    */     }
/*  42: 98 */     this.nObs = covariance.getN();
/*  43: 99 */     this.correlationMatrix = covarianceToCorrelation(covarianceMatrix);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public PearsonsCorrelation(RealMatrix covarianceMatrix, int numberOfObservations)
/*  47:    */   {
/*  48:111 */     this.nObs = numberOfObservations;
/*  49:112 */     this.correlationMatrix = covarianceToCorrelation(covarianceMatrix);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public RealMatrix getCorrelationMatrix()
/*  53:    */   {
/*  54:122 */     return this.correlationMatrix;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public RealMatrix getCorrelationStandardErrors()
/*  58:    */   {
/*  59:138 */     int nVars = this.correlationMatrix.getColumnDimension();
/*  60:139 */     double[][] out = new double[nVars][nVars];
/*  61:140 */     for (int i = 0; i < nVars; i++) {
/*  62:141 */       for (int j = 0; j < nVars; j++)
/*  63:    */       {
/*  64:142 */         double r = this.correlationMatrix.getEntry(i, j);
/*  65:143 */         out[i][j] = FastMath.sqrt((1.0D - r * r) / (this.nObs - 2));
/*  66:    */       }
/*  67:    */     }
/*  68:146 */     return new BlockRealMatrix(out);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public RealMatrix getCorrelationPValues()
/*  72:    */   {
/*  73:164 */     TDistribution tDistribution = new TDistribution(this.nObs - 2);
/*  74:165 */     int nVars = this.correlationMatrix.getColumnDimension();
/*  75:166 */     double[][] out = new double[nVars][nVars];
/*  76:167 */     for (int i = 0; i < nVars; i++) {
/*  77:168 */       for (int j = 0; j < nVars; j++) {
/*  78:169 */         if (i == j)
/*  79:    */         {
/*  80:170 */           out[i][j] = 0.0D;
/*  81:    */         }
/*  82:    */         else
/*  83:    */         {
/*  84:172 */           double r = this.correlationMatrix.getEntry(i, j);
/*  85:173 */           double t = FastMath.abs(r * FastMath.sqrt((this.nObs - 2) / (1.0D - r * r)));
/*  86:174 */           out[i][j] = (2.0D * tDistribution.cumulativeProbability(-t));
/*  87:    */         }
/*  88:    */       }
/*  89:    */     }
/*  90:178 */     return new BlockRealMatrix(out);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public RealMatrix computeCorrelationMatrix(RealMatrix matrix)
/*  94:    */   {
/*  95:190 */     int nVars = matrix.getColumnDimension();
/*  96:191 */     RealMatrix outMatrix = new BlockRealMatrix(nVars, nVars);
/*  97:192 */     for (int i = 0; i < nVars; i++)
/*  98:    */     {
/*  99:193 */       for (int j = 0; j < i; j++)
/* 100:    */       {
/* 101:194 */         double corr = correlation(matrix.getColumn(i), matrix.getColumn(j));
/* 102:195 */         outMatrix.setEntry(i, j, corr);
/* 103:196 */         outMatrix.setEntry(j, i, corr);
/* 104:    */       }
/* 105:198 */       outMatrix.setEntry(i, i, 1.0D);
/* 106:    */     }
/* 107:200 */     return outMatrix;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public RealMatrix computeCorrelationMatrix(double[][] data)
/* 111:    */   {
/* 112:212 */     return computeCorrelationMatrix(new BlockRealMatrix(data));
/* 113:    */   }
/* 114:    */   
/* 115:    */   public double correlation(double[] xArray, double[] yArray)
/* 116:    */   {
/* 117:228 */     SimpleRegression regression = new SimpleRegression();
/* 118:229 */     if (xArray.length != yArray.length) {
/* 119:230 */       throw new DimensionMismatchException(xArray.length, yArray.length);
/* 120:    */     }
/* 121:231 */     if (xArray.length < 2) {
/* 122:232 */       throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_DIMENSION, new Object[] { Integer.valueOf(xArray.length), Integer.valueOf(2) });
/* 123:    */     }
/* 124:235 */     for (int i = 0; i < xArray.length; i++) {
/* 125:236 */       regression.addData(xArray[i], yArray[i]);
/* 126:    */     }
/* 127:238 */     return regression.getR();
/* 128:    */   }
/* 129:    */   
/* 130:    */   public RealMatrix covarianceToCorrelation(RealMatrix covarianceMatrix)
/* 131:    */   {
/* 132:254 */     int nVars = covarianceMatrix.getColumnDimension();
/* 133:255 */     RealMatrix outMatrix = new BlockRealMatrix(nVars, nVars);
/* 134:256 */     for (int i = 0; i < nVars; i++)
/* 135:    */     {
/* 136:257 */       double sigma = FastMath.sqrt(covarianceMatrix.getEntry(i, i));
/* 137:258 */       outMatrix.setEntry(i, i, 1.0D);
/* 138:259 */       for (int j = 0; j < i; j++)
/* 139:    */       {
/* 140:260 */         double entry = covarianceMatrix.getEntry(i, j) / (sigma * FastMath.sqrt(covarianceMatrix.getEntry(j, j)));
/* 141:    */         
/* 142:262 */         outMatrix.setEntry(i, j, entry);
/* 143:263 */         outMatrix.setEntry(j, i, entry);
/* 144:    */       }
/* 145:    */     }
/* 146:266 */     return outMatrix;
/* 147:    */   }
/* 148:    */   
/* 149:    */   private void checkSufficientData(RealMatrix matrix)
/* 150:    */   {
/* 151:277 */     int nRows = matrix.getRowDimension();
/* 152:278 */     int nCols = matrix.getColumnDimension();
/* 153:279 */     if ((nRows < 2) || (nCols < 2)) {
/* 154:280 */       throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_ROWS_AND_COLUMNS, new Object[] { Integer.valueOf(nRows), Integer.valueOf(nCols) });
/* 155:    */     }
/* 156:    */   }
/* 157:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.correlation.PearsonsCorrelation
 * JD-Core Version:    0.7.0.1
 */
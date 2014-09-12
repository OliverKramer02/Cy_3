/*   1:    */ package org.apache.commons.math3.stat.regression;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.NoDataException;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*  10:    */ import org.apache.commons.math3.linear.ArrayRealVector;
/*  11:    */ import org.apache.commons.math3.linear.NonSquareMatrixException;
/*  12:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  13:    */ import org.apache.commons.math3.linear.RealVector;
/*  14:    */ import org.apache.commons.math3.stat.descriptive.moment.Variance;
/*  15:    */ import org.apache.commons.math3.util.FastMath;
/*  16:    */ 
/*  17:    */ public abstract class AbstractMultipleLinearRegression
/*  18:    */   implements MultipleLinearRegression
/*  19:    */ {
/*  20:    */   private RealMatrix xMatrix;
/*  21:    */   private RealVector yVector;
/*  22: 48 */   private boolean noIntercept = false;
/*  23:    */   
/*  24:    */   protected RealMatrix getX()
/*  25:    */   {
/*  26: 54 */     return this.xMatrix;
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected RealVector getY()
/*  30:    */   {
/*  31: 61 */     return this.yVector;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isNoIntercept()
/*  35:    */   {
/*  36: 69 */     return this.noIntercept;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setNoIntercept(boolean noIntercept)
/*  40:    */   {
/*  41: 77 */     this.noIntercept = noIntercept;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void newSampleData(double[] data, int nobs, int nvars)
/*  45:    */   {
/*  46:116 */     if (data == null) {
/*  47:117 */       throw new NullArgumentException();
/*  48:    */     }
/*  49:119 */     if (data.length != nobs * (nvars + 1)) {
/*  50:120 */       throw new DimensionMismatchException(data.length, nobs * (nvars + 1));
/*  51:    */     }
/*  52:122 */     if (nobs <= nvars) {
/*  53:123 */       throw new NumberIsTooSmallException(Integer.valueOf(nobs), Integer.valueOf(nvars), false);
/*  54:    */     }
/*  55:125 */     double[] y = new double[nobs];
/*  56:126 */     int cols = this.noIntercept ? nvars : nvars + 1;
/*  57:127 */     double[][] x = new double[nobs][cols];
/*  58:128 */     int pointer = 0;
/*  59:129 */     for (int i = 0; i < nobs; i++)
/*  60:    */     {
/*  61:130 */       y[i] = data[(pointer++)];
/*  62:131 */       if (!this.noIntercept) {
/*  63:132 */         x[i][0] = 1.0D;
/*  64:    */       }
/*  65:134 */       for (int j = this.noIntercept ? 0 : 1; j < cols; j++) {
/*  66:135 */         x[i][j] = data[(pointer++)];
/*  67:    */       }
/*  68:    */     }
/*  69:138 */     this.xMatrix = new Array2DRowRealMatrix(x);
/*  70:139 */     this.yVector = new ArrayRealVector(y);
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected void newYSampleData(double[] y)
/*  74:    */   {
/*  75:150 */     if (y == null) {
/*  76:151 */       throw new NullArgumentException();
/*  77:    */     }
/*  78:153 */     if (y.length == 0) {
/*  79:154 */       throw new NoDataException();
/*  80:    */     }
/*  81:156 */     this.yVector = new ArrayRealVector(y);
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected void newXSampleData(double[][] x)
/*  85:    */   {
/*  86:185 */     if (x == null) {
/*  87:186 */       throw new NullArgumentException();
/*  88:    */     }
/*  89:188 */     if (x.length == 0) {
/*  90:189 */       throw new NoDataException();
/*  91:    */     }
/*  92:191 */     if (this.noIntercept)
/*  93:    */     {
/*  94:192 */       this.xMatrix = new Array2DRowRealMatrix(x, true);
/*  95:    */     }
/*  96:    */     else
/*  97:    */     {
/*  98:194 */       int nVars = x[0].length;
/*  99:195 */       double[][] xAug = new double[x.length][nVars + 1];
/* 100:196 */       for (int i = 0; i < x.length; i++)
/* 101:    */       {
/* 102:197 */         if (x[i].length != nVars) {
/* 103:198 */           throw new DimensionMismatchException(x[i].length, nVars);
/* 104:    */         }
/* 105:200 */         xAug[i][0] = 1.0D;
/* 106:201 */         System.arraycopy(x[i], 0, xAug[i], 1, nVars);
/* 107:    */       }
/* 108:203 */       this.xMatrix = new Array2DRowRealMatrix(xAug, false);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected void validateSampleData(double[][] x, double[] y)
/* 113:    */   {
/* 114:226 */     if ((x == null) || (y == null)) {
/* 115:227 */       throw new NullArgumentException();
/* 116:    */     }
/* 117:229 */     if (x.length != y.length) {
/* 118:230 */       throw new DimensionMismatchException(y.length, x.length);
/* 119:    */     }
/* 120:232 */     if (x.length == 0) {
/* 121:233 */       throw new NoDataException();
/* 122:    */     }
/* 123:235 */     if (x[0].length + 1 > x.length) {
/* 124:236 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, new Object[] { Integer.valueOf(x.length), Integer.valueOf(x[0].length) });
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected void validateCovarianceData(double[][] x, double[][] covariance)
/* 129:    */   {
/* 130:253 */     if (x.length != covariance.length) {
/* 131:254 */       throw new DimensionMismatchException(x.length, covariance.length);
/* 132:    */     }
/* 133:256 */     if ((covariance.length > 0) && (covariance.length != covariance[0].length)) {
/* 134:257 */       throw new NonSquareMatrixException(covariance.length, covariance[0].length);
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   public double[] estimateRegressionParameters()
/* 139:    */   {
/* 140:265 */     RealVector b = calculateBeta();
/* 141:266 */     return b.toArray();
/* 142:    */   }
/* 143:    */   
/* 144:    */   public double[] estimateResiduals()
/* 145:    */   {
/* 146:273 */     RealVector b = calculateBeta();
/* 147:274 */     RealVector e = this.yVector.subtract(this.xMatrix.operate(b));
/* 148:275 */     return e.toArray();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public double[][] estimateRegressionParametersVariance()
/* 152:    */   {
/* 153:282 */     return calculateBetaVariance().getData();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public double[] estimateRegressionParametersStandardErrors()
/* 157:    */   {
/* 158:289 */     double[][] betaVariance = estimateRegressionParametersVariance();
/* 159:290 */     double sigma = calculateErrorVariance();
/* 160:291 */     int length = betaVariance[0].length;
/* 161:292 */     double[] result = new double[length];
/* 162:293 */     for (int i = 0; i < length; i++) {
/* 163:294 */       result[i] = FastMath.sqrt(sigma * betaVariance[i][i]);
/* 164:    */     }
/* 165:296 */     return result;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public double estimateRegressandVariance()
/* 169:    */   {
/* 170:303 */     return calculateYVariance();
/* 171:    */   }
/* 172:    */   
/* 173:    */   public double estimateErrorVariance()
/* 174:    */   {
/* 175:313 */     return calculateErrorVariance();
/* 176:    */   }
/* 177:    */   
/* 178:    */   public double estimateRegressionStandardError()
/* 179:    */   {
/* 180:324 */     return Math.sqrt(estimateErrorVariance());
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected abstract RealVector calculateBeta();
/* 184:    */   
/* 185:    */   protected abstract RealMatrix calculateBetaVariance();
/* 186:    */   
/* 187:    */   protected double calculateYVariance()
/* 188:    */   {
/* 189:349 */     return new Variance().evaluate(this.yVector.toArray());
/* 190:    */   }
/* 191:    */   
/* 192:    */   protected double calculateErrorVariance()
/* 193:    */   {
/* 194:364 */     RealVector residuals = calculateResiduals();
/* 195:365 */     return residuals.dotProduct(residuals) / (this.xMatrix.getRowDimension() - this.xMatrix.getColumnDimension());
/* 196:    */   }
/* 197:    */   
/* 198:    */   protected RealVector calculateResiduals()
/* 199:    */   {
/* 200:380 */     RealVector b = calculateBeta();
/* 201:381 */     return this.yVector.subtract(this.xMatrix.operate(b));
/* 202:    */   }
/* 203:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
 * JD-Core Version:    0.7.0.1
 */
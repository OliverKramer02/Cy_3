/*   1:    */ package org.apache.commons.math3.filter;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   5:    */ import org.apache.commons.math3.linear.ArrayRealVector;
/*   6:    */ import org.apache.commons.math3.linear.CholeskyDecomposition;
/*   7:    */ import org.apache.commons.math3.linear.DecompositionSolver;
/*   8:    */ import org.apache.commons.math3.linear.MatrixDimensionMismatchException;
/*   9:    */ import org.apache.commons.math3.linear.MatrixUtils;
/*  10:    */ import org.apache.commons.math3.linear.NonSquareMatrixException;
/*  11:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  12:    */ import org.apache.commons.math3.linear.RealVector;
/*  13:    */ import org.apache.commons.math3.util.MathUtils;
/*  14:    */ 
/*  15:    */ public class KalmanFilter
/*  16:    */ {
/*  17:    */   private final ProcessModel processModel;
/*  18:    */   private final MeasurementModel measurementModel;
/*  19:    */   private RealMatrix transitionMatrix;
/*  20:    */   private RealMatrix transitionMatrixT;
/*  21:    */   private RealMatrix controlMatrix;
/*  22:    */   private RealMatrix measurementMatrix;
/*  23:    */   private RealMatrix measurementMatrixT;
/*  24:    */   private RealVector stateEstimation;
/*  25:    */   private RealMatrix errorCovariance;
/*  26:    */   
/*  27:    */   public KalmanFilter(ProcessModel process, MeasurementModel measurement)
/*  28:    */   {
/*  29:123 */     MathUtils.checkNotNull(process);
/*  30:124 */     MathUtils.checkNotNull(measurement);
/*  31:    */     
/*  32:126 */     this.processModel = process;
/*  33:127 */     this.measurementModel = measurement;
/*  34:    */     
/*  35:129 */     this.transitionMatrix = this.processModel.getStateTransitionMatrix();
/*  36:130 */     MathUtils.checkNotNull(this.transitionMatrix);
/*  37:131 */     this.transitionMatrixT = this.transitionMatrix.transpose();
/*  38:134 */     if (this.processModel.getControlMatrix() == null) {
/*  39:135 */       this.controlMatrix = new Array2DRowRealMatrix();
/*  40:    */     } else {
/*  41:137 */       this.controlMatrix = this.processModel.getControlMatrix();
/*  42:    */     }
/*  43:140 */     this.measurementMatrix = this.measurementModel.getMeasurementMatrix();
/*  44:141 */     MathUtils.checkNotNull(this.measurementMatrix);
/*  45:142 */     this.measurementMatrixT = this.measurementMatrix.transpose();
/*  46:    */     
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50:147 */     RealMatrix processNoise = this.processModel.getProcessNoise();
/*  51:148 */     MathUtils.checkNotNull(processNoise);
/*  52:149 */     RealMatrix measNoise = this.measurementModel.getMeasurementNoise();
/*  53:150 */     MathUtils.checkNotNull(measNoise);
/*  54:154 */     if (this.processModel.getInitialStateEstimate() == null) {
/*  55:155 */       this.stateEstimation = new ArrayRealVector(this.transitionMatrix.getColumnDimension());
/*  56:    */     } else {
/*  57:158 */       this.stateEstimation = this.processModel.getInitialStateEstimate();
/*  58:    */     }
/*  59:161 */     if (this.transitionMatrix.getColumnDimension() != this.stateEstimation.getDimension()) {
/*  60:162 */       throw new DimensionMismatchException(this.transitionMatrix.getColumnDimension(), this.stateEstimation.getDimension());
/*  61:    */     }
/*  62:168 */     if (this.processModel.getInitialErrorCovariance() == null) {
/*  63:169 */       this.errorCovariance = processNoise.copy();
/*  64:    */     } else {
/*  65:171 */       this.errorCovariance = this.processModel.getInitialErrorCovariance();
/*  66:    */     }
/*  67:177 */     if (!this.transitionMatrix.isSquare()) {
/*  68:178 */       throw new NonSquareMatrixException(this.transitionMatrix.getRowDimension(), this.transitionMatrix.getColumnDimension());
/*  69:    */     }
/*  70:184 */     if ((this.controlMatrix != null) && (this.controlMatrix.getRowDimension() > 0) && (this.controlMatrix.getColumnDimension() > 0) && ((this.controlMatrix.getRowDimension() != this.transitionMatrix.getRowDimension()) || (this.controlMatrix.getColumnDimension() != 1))) {
/*  71:189 */       throw new MatrixDimensionMismatchException(this.controlMatrix.getRowDimension(), this.controlMatrix.getColumnDimension(), this.transitionMatrix.getRowDimension(), 1);
/*  72:    */     }
/*  73:195 */     MatrixUtils.checkAdditionCompatible(this.transitionMatrix, processNoise);
/*  74:198 */     if (this.measurementMatrix.getColumnDimension() != this.transitionMatrix.getRowDimension()) {
/*  75:199 */       throw new MatrixDimensionMismatchException(this.measurementMatrix.getRowDimension(), this.measurementMatrix.getColumnDimension(), this.measurementMatrix.getRowDimension(), this.transitionMatrix.getRowDimension());
/*  76:    */     }
/*  77:206 */     if ((measNoise.getRowDimension() != this.measurementMatrix.getRowDimension()) || (measNoise.getColumnDimension() != 1)) {
/*  78:208 */       throw new MatrixDimensionMismatchException(measNoise.getRowDimension(), measNoise.getColumnDimension(), this.measurementMatrix.getRowDimension(), 1);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int getStateDimension()
/*  83:    */   {
/*  84:220 */     return this.stateEstimation.getDimension();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public int getMeasurementDimension()
/*  88:    */   {
/*  89:229 */     return this.measurementMatrix.getRowDimension();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public double[] getStateEstimation()
/*  93:    */   {
/*  94:238 */     return this.stateEstimation.toArray();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public RealVector getStateEstimationVector()
/*  98:    */   {
/*  99:247 */     return this.stateEstimation.copy();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public double[][] getErrorCovariance()
/* 103:    */   {
/* 104:256 */     return this.errorCovariance.getData();
/* 105:    */   }
/* 106:    */   
/* 107:    */   public RealMatrix getErrorCovarianceMatrix()
/* 108:    */   {
/* 109:265 */     return this.errorCovariance.copy();
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void predict()
/* 113:    */   {
/* 114:272 */     predict((RealVector)null);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void predict(double[] u)
/* 118:    */   {
/* 119:284 */     predict(new ArrayRealVector(u));
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void predict(RealVector u)
/* 123:    */   {
/* 124:296 */     if ((u != null) && (u.getDimension() != this.controlMatrix.getColumnDimension())) {
/* 125:298 */       throw new DimensionMismatchException(u.getDimension(), this.controlMatrix.getColumnDimension());
/* 126:    */     }
/* 127:304 */     this.stateEstimation = this.transitionMatrix.operate(this.stateEstimation);
/* 128:307 */     if (u != null) {
/* 129:308 */       this.stateEstimation = this.stateEstimation.add(this.controlMatrix.operate(u));
/* 130:    */     }
/* 131:313 */     this.errorCovariance = this.transitionMatrix.multiply(this.errorCovariance).multiply(this.transitionMatrixT).add(this.processModel.getProcessNoise());
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void correct(double[] z)
/* 135:    */   {
/* 136:328 */     correct(new ArrayRealVector(z));
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void correct(RealVector z)
/* 140:    */   {
/* 141:342 */     MathUtils.checkNotNull(z);
/* 142:343 */     if (z.getDimension() != this.measurementMatrix.getRowDimension()) {
/* 143:344 */       throw new DimensionMismatchException(z.getDimension(), this.measurementMatrix.getRowDimension());
/* 144:    */     }
/* 145:349 */     RealMatrix s = this.measurementMatrix.multiply(this.errorCovariance).multiply(this.measurementMatrixT).add(this.measurementModel.getMeasurementNoise());
/* 146:    */     
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:356 */     DecompositionSolver solver = new CholeskyDecomposition(s).getSolver();
/* 153:357 */     RealMatrix invertedS = solver.getInverse();
/* 154:    */     
/* 155:    */ 
/* 156:360 */     RealVector innovation = z.subtract(this.measurementMatrix.operate(this.stateEstimation));
/* 157:    */     
/* 158:    */ 
/* 159:    */ 
/* 160:    */ 
/* 161:365 */     RealMatrix kalmanGain = this.errorCovariance.multiply(this.measurementMatrixT).multiply(invertedS);
/* 162:    */     
/* 163:    */ 
/* 164:    */ 
/* 165:369 */     this.stateEstimation = this.stateEstimation.add(kalmanGain.operate(innovation));
/* 166:    */     
/* 167:    */ 
/* 168:    */ 
/* 169:373 */     RealMatrix identity = MatrixUtils.createRealIdentityMatrix(kalmanGain.getRowDimension());
/* 170:374 */     this.errorCovariance = identity.subtract(kalmanGain.multiply(this.measurementMatrix)).multiply(this.errorCovariance);
/* 171:    */   }
/* 172:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.filter.KalmanFilter
 * JD-Core Version:    0.7.0.1
 */
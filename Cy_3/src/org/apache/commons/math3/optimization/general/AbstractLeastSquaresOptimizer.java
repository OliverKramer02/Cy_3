/*   1:    */ package org.apache.commons.math3.optimization.general;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
/*   4:    */ import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.linear.DecompositionSolver;
/*   9:    */ import org.apache.commons.math3.linear.MatrixUtils;
/*  10:    */ import org.apache.commons.math3.linear.QRDecomposition;
/*  11:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  12:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*  13:    */ import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
/*  14:    */ import org.apache.commons.math3.optimization.PointVectorValuePair;
/*  15:    */ import org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateVectorOptimizer;
/*  16:    */ import org.apache.commons.math3.util.FastMath;
/*  17:    */ 
/*  18:    */ public abstract class AbstractLeastSquaresOptimizer
/*  19:    */   extends BaseAbstractMultivariateVectorOptimizer<DifferentiableMultivariateVectorFunction>
/*  20:    */   implements DifferentiableMultivariateVectorOptimizer
/*  21:    */ {
/*  22:    */   private static final double DEFAULT_SINGULARITY_THRESHOLD = 1.0E-014D;
/*  23:    */   protected double[][] weightedResidualJacobian;
/*  24:    */   protected int cols;
/*  25:    */   protected int rows;
/*  26:    */   protected double[] point;
/*  27:    */   protected double[] objective;
/*  28:    */   protected double[] weightedResiduals;
/*  29:    */   protected double cost;
/*  30:    */   private MultivariateMatrixFunction jF;
/*  31:    */   private int jacobianEvaluations;
/*  32:    */   
/*  33:    */   protected AbstractLeastSquaresOptimizer() {}
/*  34:    */   
/*  35:    */   protected AbstractLeastSquaresOptimizer(ConvergenceChecker<PointVectorValuePair> checker)
/*  36:    */   {
/*  37: 91 */     super(checker);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int getJacobianEvaluations()
/*  41:    */   {
/*  42: 98 */     return this.jacobianEvaluations;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void updateJacobian()
/*  46:    */   {
/*  47:108 */     this.jacobianEvaluations += 1;
/*  48:109 */     this.weightedResidualJacobian = this.jF.value(this.point);
/*  49:110 */     if (this.weightedResidualJacobian.length != this.rows) {
/*  50:111 */       throw new DimensionMismatchException(this.weightedResidualJacobian.length, this.rows);
/*  51:    */     }
/*  52:114 */     double[] residualsWeights = getWeightRef();
/*  53:116 */     for (int i = 0; i < this.rows; i++)
/*  54:    */     {
/*  55:117 */       double[] ji = this.weightedResidualJacobian[i];
/*  56:118 */       double wi = FastMath.sqrt(residualsWeights[i]);
/*  57:119 */       for (int j = 0; j < this.cols; j++) {
/*  58:121 */         this.weightedResidualJacobian[i][j] = (-ji[j] * wi);
/*  59:    */       }
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected void updateResidualsAndCost()
/*  64:    */   {
/*  65:134 */     this.objective = computeObjectiveValue(this.point);
/*  66:135 */     if (this.objective.length != this.rows) {
/*  67:136 */       throw new DimensionMismatchException(this.objective.length, this.rows);
/*  68:    */     }
/*  69:139 */     double[] targetValues = getTargetRef();
/*  70:140 */     double[] residualsWeights = getWeightRef();
/*  71:    */     
/*  72:142 */     this.cost = 0.0D;
/*  73:143 */     for (int i = 0; i < this.rows; i++)
/*  74:    */     {
/*  75:144 */       double residual = targetValues[i] - this.objective[i];
/*  76:145 */       this.weightedResiduals[i] = (residual * FastMath.sqrt(residualsWeights[i]));
/*  77:146 */       this.cost += residualsWeights[i] * residual * residual;
/*  78:    */     }
/*  79:148 */     this.cost = FastMath.sqrt(this.cost);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public double getRMS()
/*  83:    */   {
/*  84:162 */     return FastMath.sqrt(getChiSquare() / this.rows);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public double getChiSquare()
/*  88:    */   {
/*  89:172 */     return this.cost * this.cost;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public double[][] getCovariances()
/*  93:    */   {
/*  94:185 */     return getCovariances(1.0E-014D);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public double[][] getCovariances(double threshold)
/*  98:    */   {
/*  99:205 */     updateJacobian();
/* 100:    */     
/* 101:    */ 
/* 102:208 */     double[][] jTj = new double[this.cols][this.cols];
/* 103:209 */     for (int i = 0; i < this.cols; i++) {
/* 104:210 */       for (int j = i; j < this.cols; j++)
/* 105:    */       {
/* 106:211 */         double sum = 0.0D;
/* 107:212 */         for (int k = 0; k < this.rows; k++) {
/* 108:213 */           sum += this.weightedResidualJacobian[k][i] * this.weightedResidualJacobian[k][j];
/* 109:    */         }
/* 110:215 */         jTj[i][j] = sum;
/* 111:216 */         jTj[j][i] = sum;
/* 112:    */       }
/* 113:    */     }
/* 114:221 */     DecompositionSolver solver = new QRDecomposition(MatrixUtils.createRealMatrix(jTj), threshold).getSolver();
/* 115:    */     
/* 116:223 */     return solver.getInverse().getData();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public double[] guessParametersErrors()
/* 120:    */   {
/* 121:238 */     if (this.rows <= this.cols) {
/* 122:239 */       throw new NumberIsTooSmallException(LocalizedFormats.NO_DEGREES_OF_FREEDOM, Integer.valueOf(this.rows), Integer.valueOf(this.cols), false);
/* 123:    */     }
/* 124:242 */     double[] errors = new double[this.cols];
/* 125:243 */     double c = FastMath.sqrt(getChiSquare() / (this.rows - this.cols));
/* 126:244 */     double[][] covar = getCovariances();
/* 127:245 */     for (int i = 0; i < errors.length; i++) {
/* 128:246 */       errors[i] = (FastMath.sqrt(covar[i][i]) * c);
/* 129:    */     }
/* 130:248 */     return errors;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public PointVectorValuePair optimize(int maxEval, DifferentiableMultivariateVectorFunction f, double[] target, double[] weights, double[] startPoint)
/* 134:    */   {
/* 135:258 */     this.jacobianEvaluations = 0;
/* 136:    */     
/* 137:    */ 
/* 138:261 */     this.jF = f.jacobian();
/* 139:    */     
/* 140:    */ 
/* 141:264 */     this.point = ((double[])startPoint.clone());
/* 142:265 */     this.rows = target.length;
/* 143:266 */     this.cols = this.point.length;
/* 144:    */     
/* 145:268 */     this.weightedResidualJacobian = new double[this.rows][this.cols];
/* 146:269 */     this.weightedResiduals = new double[this.rows];
/* 147:    */     
/* 148:271 */     this.cost = (1.0D / 0.0D);
/* 149:    */     
/* 150:273 */     return super.optimize(maxEval, f, target, weights, startPoint);
/* 151:    */   }
/* 152:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.general.AbstractLeastSquaresOptimizer
 * JD-Core Version:    0.7.0.1
 */
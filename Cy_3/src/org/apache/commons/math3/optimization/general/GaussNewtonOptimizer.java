/*   1:    */ package org.apache.commons.math3.optimization.general;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.linear.ArrayRealVector;
/*   6:    */ import org.apache.commons.math3.linear.BlockRealMatrix;
/*   7:    */ import org.apache.commons.math3.linear.DecompositionSolver;
/*   8:    */ import org.apache.commons.math3.linear.LUDecomposition;
/*   9:    */ import org.apache.commons.math3.linear.QRDecomposition;
/*  10:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  11:    */ import org.apache.commons.math3.linear.RealVector;
/*  12:    */ import org.apache.commons.math3.linear.SingularMatrixException;
/*  13:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*  14:    */ import org.apache.commons.math3.optimization.PointVectorValuePair;
/*  15:    */ import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
/*  16:    */ 
/*  17:    */ public class GaussNewtonOptimizer
/*  18:    */   extends AbstractLeastSquaresOptimizer
/*  19:    */ {
/*  20:    */   private final boolean useLU;
/*  21:    */   
/*  22:    */   public GaussNewtonOptimizer()
/*  23:    */   {
/*  24: 58 */     this(true);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public GaussNewtonOptimizer(ConvergenceChecker<PointVectorValuePair> checker)
/*  28:    */   {
/*  29: 68 */     this(true, checker);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public GaussNewtonOptimizer(boolean useLU)
/*  33:    */   {
/*  34: 81 */     this(useLU, new SimpleVectorValueChecker());
/*  35:    */   }
/*  36:    */   
/*  37:    */   public GaussNewtonOptimizer(boolean useLU, ConvergenceChecker<PointVectorValuePair> checker)
/*  38:    */   {
/*  39: 92 */     super(checker);
/*  40: 93 */     this.useLU = useLU;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public PointVectorValuePair doOptimize()
/*  44:    */   {
/*  45:100 */     ConvergenceChecker<PointVectorValuePair> checker = getConvergenceChecker();
/*  46:    */     
/*  47:    */ 
/*  48:    */ 
/*  49:104 */     PointVectorValuePair current = null;
/*  50:105 */     int iter = 0;
/*  51:106 */     for (boolean converged = false; !converged;)
/*  52:    */     {
/*  53:107 */       iter++;
/*  54:    */       
/*  55:    */ 
/*  56:110 */       PointVectorValuePair previous = current;
/*  57:111 */       updateResidualsAndCost();
/*  58:112 */       updateJacobian();
/*  59:113 */       current = new PointVectorValuePair(this.point, this.objective);
/*  60:    */       
/*  61:115 */       double[] targetValues = getTargetRef();
/*  62:116 */       double[] residualsWeights = getWeightRef();
/*  63:    */       
/*  64:    */ 
/*  65:119 */       double[] b = new double[this.cols];
/*  66:120 */       double[][] a = new double[this.cols][this.cols];
/*  67:121 */       for (int i = 0; i < this.rows; i++)
/*  68:    */       {
/*  69:123 */         double[] grad = this.weightedResidualJacobian[i];
/*  70:124 */         double weight = residualsWeights[i];
/*  71:125 */         double residual = this.objective[i] - targetValues[i];
/*  72:    */         
/*  73:    */ 
/*  74:128 */         double wr = weight * residual;
/*  75:129 */         for (int j = 0; j < this.cols; j++) {
/*  76:130 */           b[j] += wr * grad[j];
/*  77:    */         }
/*  78:134 */         for (int k = 0; k < this.cols; k++)
/*  79:    */         {
/*  80:135 */           double[] ak = a[k];
/*  81:136 */           double wgk = weight * grad[k];
/*  82:137 */           for (int l = 0; l < this.cols; l++) {
/*  83:138 */             ak[l] += wgk * grad[l];
/*  84:    */           }
/*  85:    */         }
/*  86:    */       }
/*  87:    */       try
/*  88:    */       {
/*  89:145 */         RealMatrix mA = new BlockRealMatrix(a);
/*  90:146 */         DecompositionSolver solver = this.useLU ? new LUDecomposition(mA).getSolver() : new QRDecomposition(mA).getSolver();
/*  91:    */         
/*  92:    */ 
/*  93:149 */         double[] dX = solver.solve(new ArrayRealVector(b, false)).toArray();
/*  94:151 */         for (int i = 0; i < this.cols; i++) {
/*  95:152 */           this.point[i] += dX[i];
/*  96:    */         }
/*  97:    */       }
/*  98:    */       catch (SingularMatrixException e)
/*  99:    */       {
/* 100:155 */         throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, new Object[0]);
/* 101:    */       }
/* 102:159 */       if ((checker != null) && 
/* 103:160 */         (previous != null)) {
/* 104:161 */         converged = checker.converged(iter, previous, current);
/* 105:    */       }
/* 106:    */     }
/* 107:166 */     return current;
/* 108:    */   }
/* 109:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.general.GaussNewtonOptimizer
 * JD-Core Version:    0.7.0.1
 */
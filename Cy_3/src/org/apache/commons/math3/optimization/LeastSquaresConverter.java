/*   1:    */ package org.apache.commons.math3.optimization;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.MultivariateVectorFunction;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   7:    */ 
/*   8:    */ public class LeastSquaresConverter
/*   9:    */   implements MultivariateFunction
/*  10:    */ {
/*  11:    */   private final MultivariateVectorFunction function;
/*  12:    */   private final double[] observations;
/*  13:    */   private final double[] weights;
/*  14:    */   private final RealMatrix scale;
/*  15:    */   
/*  16:    */   public LeastSquaresConverter(MultivariateVectorFunction function, double[] observations)
/*  17:    */   {
/*  18: 76 */     this.function = function;
/*  19: 77 */     this.observations = ((double[])observations.clone());
/*  20: 78 */     this.weights = null;
/*  21: 79 */     this.scale = null;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public LeastSquaresConverter(MultivariateVectorFunction function, double[] observations, double[] weights)
/*  25:    */   {
/*  26:112 */     if (observations.length != weights.length) {
/*  27:113 */       throw new DimensionMismatchException(observations.length, weights.length);
/*  28:    */     }
/*  29:115 */     this.function = function;
/*  30:116 */     this.observations = ((double[])observations.clone());
/*  31:117 */     this.weights = ((double[])weights.clone());
/*  32:118 */     this.scale = null;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public LeastSquaresConverter(MultivariateVectorFunction function, double[] observations, RealMatrix scale)
/*  36:    */   {
/*  37:142 */     if (observations.length != scale.getColumnDimension()) {
/*  38:143 */       throw new DimensionMismatchException(observations.length, scale.getColumnDimension());
/*  39:    */     }
/*  40:145 */     this.function = function;
/*  41:146 */     this.observations = ((double[])observations.clone());
/*  42:147 */     this.weights = null;
/*  43:148 */     this.scale = scale.copy();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public double value(double[] point)
/*  47:    */   {
/*  48:154 */     double[] residuals = this.function.value(point);
/*  49:155 */     if (residuals.length != this.observations.length) {
/*  50:156 */       throw new DimensionMismatchException(residuals.length, this.observations.length);
/*  51:    */     }
/*  52:158 */     for (int i = 0; i < residuals.length; i++) {
/*  53:159 */       residuals[i] -= this.observations[i];
/*  54:    */     }
/*  55:163 */     double sumSquares = 0.0D;
/*  56:164 */     if (this.weights != null) {
/*  57:165 */       for (int i = 0; i < residuals.length; i++)
/*  58:    */       {
/*  59:166 */         double ri = residuals[i];
/*  60:167 */         sumSquares += this.weights[i] * ri * ri;
/*  61:    */       }
/*  62:169 */     } else if (this.scale != null) {
/*  63:170 */       for (double yi : this.scale.operate(residuals)) {
/*  64:171 */         sumSquares += yi * yi;
/*  65:    */       }
/*  66:    */     } else {
/*  67:174 */       for (double ri : residuals) {
/*  68:175 */         sumSquares += ri * ri;
/*  69:    */       }
/*  70:    */     }
/*  71:179 */     return sumSquares;
/*  72:    */   }
/*  73:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.LeastSquaresConverter
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   5:    */ import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
/*   6:    */ 
/*   7:    */ public class CorrelatedRandomVectorGenerator
/*   8:    */   implements RandomVectorGenerator
/*   9:    */ {
/*  10:    */   private final double[] mean;
/*  11:    */   private final NormalizedRandomGenerator generator;
/*  12:    */   private final double[] normalized;
/*  13:    */   private final RealMatrix root;
/*  14:    */   
/*  15:    */   public CorrelatedRandomVectorGenerator(double[] mean, RealMatrix covariance, double small, NormalizedRandomGenerator generator)
/*  16:    */   {
/*  17: 90 */     int order = covariance.getRowDimension();
/*  18: 91 */     if (mean.length != order) {
/*  19: 92 */       throw new DimensionMismatchException(mean.length, order);
/*  20:    */     }
/*  21: 94 */     this.mean = ((double[])mean.clone());
/*  22:    */     
/*  23: 96 */     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(covariance, small);
/*  24:    */     
/*  25: 98 */     this.root = decomposition.getRootMatrix();
/*  26:    */     
/*  27:100 */     this.generator = generator;
/*  28:101 */     this.normalized = new double[decomposition.getRank()];
/*  29:    */   }
/*  30:    */   
/*  31:    */   public CorrelatedRandomVectorGenerator(RealMatrix covariance, double small, NormalizedRandomGenerator generator)
/*  32:    */   {
/*  33:119 */     int order = covariance.getRowDimension();
/*  34:120 */     this.mean = new double[order];
/*  35:121 */     for (int i = 0; i < order; i++) {
/*  36:122 */       this.mean[i] = 0.0D;
/*  37:    */     }
/*  38:125 */     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(covariance, small);
/*  39:    */     
/*  40:127 */     this.root = decomposition.getRootMatrix();
/*  41:    */     
/*  42:129 */     this.generator = generator;
/*  43:130 */     this.normalized = new double[decomposition.getRank()];
/*  44:    */   }
/*  45:    */   
/*  46:    */   public NormalizedRandomGenerator getGenerator()
/*  47:    */   {
/*  48:138 */     return this.generator;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getRank()
/*  52:    */   {
/*  53:148 */     return this.normalized.length;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public RealMatrix getRootMatrix()
/*  57:    */   {
/*  58:158 */     return this.root;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public double[] nextVector()
/*  62:    */   {
/*  63:168 */     for (int i = 0; i < this.normalized.length; i++) {
/*  64:169 */       this.normalized[i] = this.generator.nextNormalizedDouble();
/*  65:    */     }
/*  66:173 */     double[] correlated = new double[this.mean.length];
/*  67:174 */     for (int i = 0; i < correlated.length; i++)
/*  68:    */     {
/*  69:175 */       correlated[i] = this.mean[i];
/*  70:176 */       for (int j = 0; j < this.root.getColumnDimension(); j++) {
/*  71:177 */         correlated[i] += this.root.getEntry(i, j) * this.normalized[j];
/*  72:    */       }
/*  73:    */     }
/*  74:181 */     return correlated;
/*  75:    */   }
/*  76:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.CorrelatedRandomVectorGenerator
 * JD-Core Version:    0.7.0.1
 */
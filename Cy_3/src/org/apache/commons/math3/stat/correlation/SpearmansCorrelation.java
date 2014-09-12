/*   1:    */ package org.apache.commons.math3.stat.correlation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.linear.BlockRealMatrix;
/*   7:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   8:    */ import org.apache.commons.math3.stat.ranking.NaturalRanking;
/*   9:    */ import org.apache.commons.math3.stat.ranking.RankingAlgorithm;
/*  10:    */ 
/*  11:    */ public class SpearmansCorrelation
/*  12:    */ {
/*  13:    */   private final RealMatrix data;
/*  14:    */   private final RankingAlgorithm rankingAlgorithm;
/*  15:    */   private final PearsonsCorrelation rankCorrelation;
/*  16:    */   
/*  17:    */   public SpearmansCorrelation(RealMatrix dataMatrix, RankingAlgorithm rankingAlgorithm)
/*  18:    */   {
/*  19: 61 */     this.data = dataMatrix.copy();
/*  20: 62 */     this.rankingAlgorithm = rankingAlgorithm;
/*  21: 63 */     rankTransform(this.data);
/*  22: 64 */     this.rankCorrelation = new PearsonsCorrelation(this.data);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SpearmansCorrelation(RealMatrix dataMatrix)
/*  26:    */   {
/*  27: 74 */     this(dataMatrix, new NaturalRanking());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public SpearmansCorrelation()
/*  31:    */   {
/*  32: 81 */     this.data = null;
/*  33: 82 */     this.rankingAlgorithm = new NaturalRanking();
/*  34: 83 */     this.rankCorrelation = null;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public RealMatrix getCorrelationMatrix()
/*  38:    */   {
/*  39: 92 */     return this.rankCorrelation.getCorrelationMatrix();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public PearsonsCorrelation getRankCorrelation()
/*  43:    */   {
/*  44:108 */     return this.rankCorrelation;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public RealMatrix computeCorrelationMatrix(RealMatrix matrix)
/*  48:    */   {
/*  49:119 */     RealMatrix matrixCopy = matrix.copy();
/*  50:120 */     rankTransform(matrixCopy);
/*  51:121 */     return new PearsonsCorrelation().computeCorrelationMatrix(matrixCopy);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public RealMatrix computeCorrelationMatrix(double[][] matrix)
/*  55:    */   {
/*  56:133 */     return computeCorrelationMatrix(new BlockRealMatrix(matrix));
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double correlation(double[] xArray, double[] yArray)
/*  60:    */   {
/*  61:146 */     if (xArray.length != yArray.length) {
/*  62:147 */       throw new DimensionMismatchException(xArray.length, yArray.length);
/*  63:    */     }
/*  64:148 */     if (xArray.length < 2) {
/*  65:149 */       throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_DIMENSION, new Object[] { Integer.valueOf(xArray.length), Integer.valueOf(2) });
/*  66:    */     }
/*  67:152 */     return new PearsonsCorrelation().correlation(this.rankingAlgorithm.rank(xArray), this.rankingAlgorithm.rank(yArray));
/*  68:    */   }
/*  69:    */   
/*  70:    */   private void rankTransform(RealMatrix matrix)
/*  71:    */   {
/*  72:164 */     for (int i = 0; i < matrix.getColumnDimension(); i++) {
/*  73:165 */       matrix.setColumn(i, this.rankingAlgorithm.rank(matrix.getColumn(i)));
/*  74:    */     }
/*  75:    */   }
/*  76:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.correlation.SpearmansCorrelation
 * JD-Core Version:    0.7.0.1
 */
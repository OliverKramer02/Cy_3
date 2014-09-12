/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public class CholeskyDecomposition
/*   7:    */ {
/*   8:    */   public static final double DEFAULT_RELATIVE_SYMMETRY_THRESHOLD = 1.E-015D;
/*   9:    */   public static final double DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD = 1.0E-010D;
/*  10:    */   private double[][] lTData;
/*  11:    */   private RealMatrix cachedL;
/*  12:    */   private RealMatrix cachedLT;
/*  13:    */   
/*  14:    */   public CholeskyDecomposition(RealMatrix matrix)
/*  15:    */   {
/*  16: 85 */     this(matrix, 1.E-015D, 1.0E-010D);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public CholeskyDecomposition(RealMatrix matrix, double relativeSymmetryThreshold, double absolutePositivityThreshold)
/*  20:    */   {
/*  21:107 */     if (!matrix.isSquare()) {
/*  22:108 */       throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
/*  23:    */     }
/*  24:112 */     int order = matrix.getRowDimension();
/*  25:113 */     this.lTData = matrix.getData();
/*  26:114 */     this.cachedL = null;
/*  27:115 */     this.cachedLT = null;
/*  28:118 */     for (int i = 0; i < order; i++)
/*  29:    */     {
/*  30:119 */       double[] lI = this.lTData[i];
/*  31:122 */       for (int j = i + 1; j < order; j++)
/*  32:    */       {
/*  33:123 */         double[] lJ = this.lTData[j];
/*  34:124 */         double lIJ = lI[j];
/*  35:125 */         double lJI = lJ[i];
/*  36:126 */         double maxDelta = relativeSymmetryThreshold * FastMath.max(FastMath.abs(lIJ), FastMath.abs(lJI));
/*  37:128 */         if (FastMath.abs(lIJ - lJI) > maxDelta) {
/*  38:129 */           throw new NonSymmetricMatrixException(i, j, relativeSymmetryThreshold);
/*  39:    */         }
/*  40:131 */         lJ[i] = 0.0D;
/*  41:    */       }
/*  42:    */     }
/*  43:136 */     for (int i = 0; i < order; i++)
/*  44:    */     {
/*  45:138 */       double[] ltI = this.lTData[i];
/*  46:141 */       if (ltI[i] <= absolutePositivityThreshold) {
/*  47:142 */         throw new NonPositiveDefiniteMatrixException(ltI[i], i, absolutePositivityThreshold);
/*  48:    */       }
/*  49:145 */       ltI[i] = FastMath.sqrt(ltI[i]);
/*  50:146 */       double inverse = 1.0D / ltI[i];
/*  51:148 */       for (int q = order - 1; q > i; q--)
/*  52:    */       {
/*  53:149 */         ltI[q] *= inverse;
/*  54:150 */         double[] ltQ = this.lTData[q];
/*  55:151 */         for (int p = q; p < order; p++) {
/*  56:152 */           ltQ[p] -= ltI[q] * ltI[p];
/*  57:    */         }
/*  58:    */       }
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public RealMatrix getL()
/*  63:    */   {
/*  64:164 */     if (this.cachedL == null) {
/*  65:165 */       this.cachedL = getLT().transpose();
/*  66:    */     }
/*  67:167 */     return this.cachedL;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public RealMatrix getLT()
/*  71:    */   {
/*  72:177 */     if (this.cachedLT == null) {
/*  73:178 */       this.cachedLT = MatrixUtils.createRealMatrix(this.lTData);
/*  74:    */     }
/*  75:182 */     return this.cachedLT;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public double getDeterminant()
/*  79:    */   {
/*  80:190 */     double determinant = 1.0D;
/*  81:191 */     for (int i = 0; i < this.lTData.length; i++)
/*  82:    */     {
/*  83:192 */       double lTii = this.lTData[i][i];
/*  84:193 */       determinant *= lTii * lTii;
/*  85:    */     }
/*  86:195 */     return determinant;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public DecompositionSolver getSolver()
/*  90:    */   {
/*  91:203 */     return new Solver(this.lTData);
/*  92:    */   }
/*  93:    */   
/*  94:    */   private static class Solver
/*  95:    */     implements DecompositionSolver
/*  96:    */   {
/*  97:    */     private final double[][] lTData;
/*  98:    */     
/*  99:    */     private Solver(double[][] lTData)
/* 100:    */     {
/* 101:216 */       this.lTData = lTData;
/* 102:    */     }
/* 103:    */     
/* 104:    */     public boolean isNonSingular()
/* 105:    */     {
/* 106:222 */       return true;
/* 107:    */     }
/* 108:    */     
/* 109:    */     public RealVector solve(RealVector b)
/* 110:    */     {
/* 111:227 */       int m = this.lTData.length;
/* 112:228 */       if (b.getDimension() != m) {
/* 113:229 */         throw new DimensionMismatchException(b.getDimension(), m);
/* 114:    */       }
/* 115:232 */       double[] x = b.toArray();
/* 116:235 */       for (int j = 0; j < m; j++)
/* 117:    */       {
/* 118:236 */         double[] lJ = this.lTData[j];
/* 119:237 */         x[j] /= lJ[j];
/* 120:238 */         double xJ = x[j];
/* 121:239 */         for (int i = j + 1; i < m; i++) {
/* 122:240 */           x[i] -= xJ * lJ[i];
/* 123:    */         }
/* 124:    */       }
/* 125:245 */       for (int j = m - 1; j >= 0; j--)
/* 126:    */       {
/* 127:246 */         x[j] /= this.lTData[j][j];
/* 128:247 */         double xJ = x[j];
/* 129:248 */         for (int i = 0; i < j; i++) {
/* 130:249 */           x[i] -= xJ * this.lTData[i][j];
/* 131:    */         }
/* 132:    */       }
/* 133:253 */       return new ArrayRealVector(x, false);
/* 134:    */     }
/* 135:    */     
/* 136:    */     public RealMatrix solve(RealMatrix b)
/* 137:    */     {
/* 138:258 */       int m = this.lTData.length;
/* 139:259 */       if (b.getRowDimension() != m) {
/* 140:260 */         throw new DimensionMismatchException(b.getRowDimension(), m);
/* 141:    */       }
/* 142:263 */       int nColB = b.getColumnDimension();
/* 143:264 */       double[][] x = b.getData();
/* 144:267 */       for (int j = 0; j < m; j++)
/* 145:    */       {
/* 146:268 */         double[] lJ = this.lTData[j];
/* 147:269 */         double lJJ = lJ[j];
/* 148:270 */         double[] xJ = x[j];
/* 149:271 */         for (int k = 0; k < nColB; k++) {
/* 150:272 */           xJ[k] /= lJJ;
/* 151:    */         }
/* 152:274 */         for (int i = j + 1; i < m; i++)
/* 153:    */         {
/* 154:275 */           double[] xI = x[i];
/* 155:276 */           double lJI = lJ[i];
/* 156:277 */           for (int k = 0; k < nColB; k++) {
/* 157:278 */             xI[k] -= xJ[k] * lJI;
/* 158:    */           }
/* 159:    */         }
/* 160:    */       }
/* 161:284 */       for (int j = m - 1; j >= 0; j--)
/* 162:    */       {
/* 163:285 */         double lJJ = this.lTData[j][j];
/* 164:286 */         double[] xJ = x[j];
/* 165:287 */         for (int k = 0; k < nColB; k++) {
/* 166:288 */           xJ[k] /= lJJ;
/* 167:    */         }
/* 168:290 */         for (int i = 0; i < j; i++)
/* 169:    */         {
/* 170:291 */           double[] xI = x[i];
/* 171:292 */           double lIJ = this.lTData[i][j];
/* 172:293 */           for (int k = 0; k < nColB; k++) {
/* 173:294 */             xI[k] -= xJ[k] * lIJ;
/* 174:    */           }
/* 175:    */         }
/* 176:    */       }
/* 177:299 */       return new Array2DRowRealMatrix(x);
/* 178:    */     }
/* 179:    */     
/* 180:    */     public RealMatrix getInverse()
/* 181:    */     {
/* 182:304 */       return solve(MatrixUtils.createRealIdentityMatrix(this.lTData.length));
/* 183:    */     }
/* 184:    */   }
/* 185:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.CholeskyDecomposition
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ class TriDiagonalTransformer
/*   7:    */ {
/*   8:    */   private final double[][] householderVectors;
/*   9:    */   private final double[] main;
/*  10:    */   private final double[] secondary;
/*  11:    */   private RealMatrix cachedQ;
/*  12:    */   private RealMatrix cachedQt;
/*  13:    */   private RealMatrix cachedT;
/*  14:    */   
/*  15:    */   public TriDiagonalTransformer(RealMatrix matrix)
/*  16:    */   {
/*  17: 63 */     if (!matrix.isSquare()) {
/*  18: 64 */       throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
/*  19:    */     }
/*  20: 68 */     int m = matrix.getRowDimension();
/*  21: 69 */     this.householderVectors = matrix.getData();
/*  22: 70 */     this.main = new double[m];
/*  23: 71 */     this.secondary = new double[m - 1];
/*  24: 72 */     this.cachedQ = null;
/*  25: 73 */     this.cachedQt = null;
/*  26: 74 */     this.cachedT = null;
/*  27:    */     
/*  28:    */ 
/*  29: 77 */     transform();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public RealMatrix getQ()
/*  33:    */   {
/*  34: 86 */     if (this.cachedQ == null) {
/*  35: 87 */       this.cachedQ = getQT().transpose();
/*  36:    */     }
/*  37: 89 */     return this.cachedQ;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public RealMatrix getQT()
/*  41:    */   {
/*  42: 98 */     if (this.cachedQt == null)
/*  43:    */     {
/*  44: 99 */       int m = this.householderVectors.length;
/*  45:100 */       double[][] qta = new double[m][m];
/*  46:103 */       for (int k = m - 1; k >= 1; k--)
/*  47:    */       {
/*  48:104 */         double[] hK = this.householderVectors[(k - 1)];
/*  49:105 */         qta[k][k] = 1.0D;
/*  50:106 */         if (hK[k] != 0.0D)
/*  51:    */         {
/*  52:107 */           double inv = 1.0D / (this.secondary[(k - 1)] * hK[k]);
/*  53:108 */           double beta = 1.0D / this.secondary[(k - 1)];
/*  54:109 */           qta[k][k] = (1.0D + beta * hK[k]);
/*  55:110 */           for (int i = k + 1; i < m; i++) {
/*  56:111 */             qta[k][i] = (beta * hK[i]);
/*  57:    */           }
/*  58:113 */           for (int j = k + 1; j < m; j++)
/*  59:    */           {
/*  60:114 */             beta = 0.0D;
/*  61:115 */             for (int i = k + 1; i < m; i++) {
/*  62:116 */               beta += qta[j][i] * hK[i];
/*  63:    */             }
/*  64:118 */             beta *= inv;
/*  65:119 */             qta[j][k] = (beta * hK[k]);
/*  66:120 */             for (int i = k + 1; i < m; i++) {
/*  67:121 */               qta[j][i] += beta * hK[i];
/*  68:    */             }
/*  69:    */           }
/*  70:    */         }
/*  71:    */       }
/*  72:126 */       qta[0][0] = 1.0D;
/*  73:127 */       this.cachedQt = MatrixUtils.createRealMatrix(qta);
/*  74:    */     }
/*  75:131 */     return this.cachedQt;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public RealMatrix getT()
/*  79:    */   {
/*  80:139 */     if (this.cachedT == null)
/*  81:    */     {
/*  82:140 */       int m = this.main.length;
/*  83:141 */       double[][] ta = new double[m][m];
/*  84:142 */       for (int i = 0; i < m; i++)
/*  85:    */       {
/*  86:143 */         ta[i][i] = this.main[i];
/*  87:144 */         if (i > 0) {
/*  88:145 */           ta[i][(i - 1)] = this.secondary[(i - 1)];
/*  89:    */         }
/*  90:147 */         if (i < this.main.length - 1) {
/*  91:148 */           ta[i][(i + 1)] = this.secondary[i];
/*  92:    */         }
/*  93:    */       }
/*  94:151 */       this.cachedT = MatrixUtils.createRealMatrix(ta);
/*  95:    */     }
/*  96:155 */     return this.cachedT;
/*  97:    */   }
/*  98:    */   
/*  99:    */   double[][] getHouseholderVectorsRef()
/* 100:    */   {
/* 101:165 */     return this.householderVectors;
/* 102:    */   }
/* 103:    */   
/* 104:    */   double[] getMainDiagonalRef()
/* 105:    */   {
/* 106:175 */     return this.main;
/* 107:    */   }
/* 108:    */   
/* 109:    */   double[] getSecondaryDiagonalRef()
/* 110:    */   {
/* 111:185 */     return this.secondary;
/* 112:    */   }
/* 113:    */   
/* 114:    */   private void transform()
/* 115:    */   {
/* 116:193 */     int m = this.householderVectors.length;
/* 117:194 */     double[] z = new double[m];
/* 118:195 */     for (int k = 0; k < m - 1; k++)
/* 119:    */     {
/* 120:198 */       double[] hK = this.householderVectors[k];
/* 121:199 */       this.main[k] = hK[k];
/* 122:200 */       double xNormSqr = 0.0D;
/* 123:201 */       for (int j = k + 1; j < m; j++)
/* 124:    */       {
/* 125:202 */         double c = hK[j];
/* 126:203 */         xNormSqr += c * c;
/* 127:    */       }
/* 128:205 */       double a = hK[(k + 1)] > 0.0D ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
/* 129:206 */       this.secondary[k] = a;
/* 130:207 */       if (a != 0.0D)
/* 131:    */       {
/* 132:210 */         hK[(k + 1)] -= a;
/* 133:211 */         double beta = -1.0D / (a * hK[(k + 1)]);
/* 134:    */         
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:217 */         Arrays.fill(z, k + 1, m, 0.0D);
/* 140:218 */         for (int i = k + 1; i < m; i++)
/* 141:    */         {
/* 142:219 */           double[] hI = this.householderVectors[i];
/* 143:220 */           double hKI = hK[i];
/* 144:221 */           double zI = hI[i] * hKI;
/* 145:222 */           for (int j = i + 1; j < m; j++)
/* 146:    */           {
/* 147:223 */             double hIJ = hI[j];
/* 148:224 */             zI += hIJ * hK[j];
/* 149:225 */             z[j] += hIJ * hKI;
/* 150:    */           }
/* 151:227 */           z[i] = (beta * (z[i] + zI));
/* 152:    */         }
/* 153:231 */         double gamma = 0.0D;
/* 154:232 */         for (int i = k + 1; i < m; i++) {
/* 155:233 */           gamma += z[i] * hK[i];
/* 156:    */         }
/* 157:235 */         gamma *= beta / 2.0D;
/* 158:238 */         for (int i = k + 1; i < m; i++) {
/* 159:239 */           z[i] -= gamma * hK[i];
/* 160:    */         }
/* 161:244 */         for (int i = k + 1; i < m; i++)
/* 162:    */         {
/* 163:245 */           double[] hI = this.householderVectors[i];
/* 164:246 */           for (int j = i; j < m; j++) {
/* 165:247 */             hI[j] -= hK[i] * z[j] + z[i] * hK[j];
/* 166:    */           }
/* 167:    */         }
/* 168:    */       }
/* 169:    */     }
/* 170:252 */     this.main[(m - 1)] = this.householderVectors[(m - 1)][(m - 1)];
/* 171:    */   }
/* 172:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.TriDiagonalTransformer
 * JD-Core Version:    0.7.0.1
 */
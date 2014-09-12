/*   1:    */ package org.apache.commons.math3.stat.regression;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ import org.apache.commons.math3.util.MathArrays;
/*   8:    */ 
/*   9:    */ public class RegressionResults
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private static final int SSE_IDX = 0;
/*  13:    */   private static final int SST_IDX = 1;
/*  14:    */   private static final int RSQ_IDX = 2;
/*  15:    */   private static final int MSE_IDX = 3;
/*  16:    */   private static final int ADJRSQ_IDX = 4;
/*  17:    */   private static final long serialVersionUID = 1L;
/*  18:    */   private final double[] parameters;
/*  19:    */   private final double[][] varCovData;
/*  20:    */   private final boolean isSymmetricVCD;
/*  21:    */   private final int rank;
/*  22:    */   private final long nobs;
/*  23:    */   private final boolean containsConstant;
/*  24:    */   private final double[] globalFitInfo;
/*  25:    */   
/*  26:    */   private RegressionResults()
/*  27:    */   {
/*  28: 67 */     this.parameters = null;
/*  29: 68 */     this.varCovData = ((double[][])null);
/*  30: 69 */     this.rank = -1;
/*  31: 70 */     this.nobs = -1L;
/*  32: 71 */     this.containsConstant = false;
/*  33: 72 */     this.isSymmetricVCD = false;
/*  34: 73 */     this.globalFitInfo = null;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public RegressionResults(double[] parameters, double[][] varcov, boolean isSymmetricCompressed, long nobs, int rank, double sumy, double sumysq, double sse, boolean containsConstant, boolean copyData)
/*  38:    */   {
/*  39:100 */     if (copyData)
/*  40:    */     {
/*  41:101 */       this.parameters = MathArrays.copyOf(parameters);
/*  42:102 */       this.varCovData = new double[varcov.length][];
/*  43:103 */       for (int i = 0; i < varcov.length; i++) {
/*  44:104 */         this.varCovData[i] = MathArrays.copyOf(varcov[i]);
/*  45:    */       }
/*  46:    */     }
/*  47:    */     else
/*  48:    */     {
/*  49:107 */       this.parameters = parameters;
/*  50:108 */       this.varCovData = varcov;
/*  51:    */     }
/*  52:110 */     this.isSymmetricVCD = isSymmetricCompressed;
/*  53:111 */     this.nobs = nobs;
/*  54:112 */     this.rank = rank;
/*  55:113 */     this.containsConstant = containsConstant;
/*  56:114 */     this.globalFitInfo = new double[5];
/*  57:115 */     Arrays.fill(this.globalFitInfo, (0.0D / 0.0D));
/*  58:117 */     if (rank > 0) {
/*  59:118 */       this.globalFitInfo[1] = (containsConstant ? sumysq - sumy * sumy / nobs : sumysq);
/*  60:    */     }
/*  61:122 */     this.globalFitInfo[0] = sse;
/*  62:123 */     this.globalFitInfo[3] = (this.globalFitInfo[0] / (nobs - rank));
/*  63:    */     
/*  64:125 */     this.globalFitInfo[2] = (1.0D - this.globalFitInfo[0] / this.globalFitInfo[1]);
/*  65:129 */     if (!containsConstant) {
/*  66:130 */       this.globalFitInfo[4] = (1.0D - (1.0D - this.globalFitInfo[2]) * (nobs / (nobs - rank)));
/*  67:    */     } else {
/*  68:134 */       this.globalFitInfo[4] = (1.0D - sse * (nobs - 1.0D) / (this.globalFitInfo[1] * (nobs - rank)));
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public double getParameterEstimate(int index)
/*  73:    */   {
/*  74:151 */     if (this.parameters == null) {
/*  75:152 */       return (0.0D / 0.0D);
/*  76:    */     }
/*  77:154 */     if ((index < 0) || (index >= this.parameters.length)) {
/*  78:155 */       throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
/*  79:    */     }
/*  80:157 */     return this.parameters[index];
/*  81:    */   }
/*  82:    */   
/*  83:    */   public double[] getParameterEstimates()
/*  84:    */   {
/*  85:171 */     if (this.parameters == null) {
/*  86:172 */       return null;
/*  87:    */     }
/*  88:174 */     return MathArrays.copyOf(this.parameters);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public double getStdErrorOfEstimate(int index)
/*  92:    */   {
/*  93:188 */     if (this.parameters == null) {
/*  94:189 */       return (0.0D / 0.0D);
/*  95:    */     }
/*  96:191 */     if ((index < 0) || (index >= this.parameters.length)) {
/*  97:192 */       throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
/*  98:    */     }
/*  99:194 */     double var = getVcvElement(index, index);
/* 100:195 */     if ((!Double.isNaN(var)) && (var > 4.9E-324D)) {
/* 101:196 */       return FastMath.sqrt(var);
/* 102:    */     }
/* 103:198 */     return (0.0D / 0.0D);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public double[] getStdErrorOfEstimates()
/* 107:    */   {
/* 108:213 */     if (this.parameters == null) {
/* 109:214 */       return null;
/* 110:    */     }
/* 111:216 */     double[] se = new double[this.parameters.length];
/* 112:217 */     for (int i = 0; i < this.parameters.length; i++)
/* 113:    */     {
/* 114:218 */       double var = getVcvElement(i, i);
/* 115:219 */       if ((!Double.isNaN(var)) && (var > 4.9E-324D)) {
/* 116:220 */         se[i] = FastMath.sqrt(var);
/* 117:    */       } else {
/* 118:223 */         se[i] = (0.0D / 0.0D);
/* 119:    */       }
/* 120:    */     }
/* 121:225 */     return se;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public double getCovarianceOfParameters(int i, int j)
/* 125:    */   {
/* 126:241 */     if (this.parameters == null) {
/* 127:242 */       return (0.0D / 0.0D);
/* 128:    */     }
/* 129:244 */     if ((i < 0) || (i >= this.parameters.length)) {
/* 130:245 */       throw new OutOfRangeException(Integer.valueOf(i), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
/* 131:    */     }
/* 132:247 */     if ((j < 0) || (j >= this.parameters.length)) {
/* 133:248 */       throw new OutOfRangeException(Integer.valueOf(j), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
/* 134:    */     }
/* 135:250 */     return getVcvElement(i, j);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int getNumberOfParameters()
/* 139:    */   {
/* 140:262 */     if (this.parameters == null) {
/* 141:263 */       return -1;
/* 142:    */     }
/* 143:265 */     return this.parameters.length;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public long getN()
/* 147:    */   {
/* 148:274 */     return this.nobs;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public double getTotalSumSquares()
/* 152:    */   {
/* 153:288 */     return this.globalFitInfo[1];
/* 154:    */   }
/* 155:    */   
/* 156:    */   public double getRegressionSumSquares()
/* 157:    */   {
/* 158:308 */     return this.globalFitInfo[1] - this.globalFitInfo[0];
/* 159:    */   }
/* 160:    */   
/* 161:    */   public double getErrorSumSquares()
/* 162:    */   {
/* 163:330 */     return this.globalFitInfo[0];
/* 164:    */   }
/* 165:    */   
/* 166:    */   public double getMeanSquareError()
/* 167:    */   {
/* 168:344 */     return this.globalFitInfo[3];
/* 169:    */   }
/* 170:    */   
/* 171:    */   public double getRSquared()
/* 172:    */   {
/* 173:362 */     return this.globalFitInfo[2];
/* 174:    */   }
/* 175:    */   
/* 176:    */   public double getAdjustedRSquared()
/* 177:    */   {
/* 178:380 */     return this.globalFitInfo[4];
/* 179:    */   }
/* 180:    */   
/* 181:    */   public boolean hasIntercept()
/* 182:    */   {
/* 183:390 */     return this.containsConstant;
/* 184:    */   }
/* 185:    */   
/* 186:    */   private double getVcvElement(int i, int j)
/* 187:    */   {
/* 188:401 */     if (this.isSymmetricVCD)
/* 189:    */     {
/* 190:402 */       if (this.varCovData.length > 1)
/* 191:    */       {
/* 192:404 */         if (i == j) {
/* 193:405 */           return this.varCovData[i][i];
/* 194:    */         }
/* 195:406 */         if (i >= this.varCovData[j].length) {
/* 196:407 */           return this.varCovData[i][j];
/* 197:    */         }
/* 198:409 */         return this.varCovData[j][i];
/* 199:    */       }
/* 200:412 */       if (i > j) {
/* 201:413 */         return this.varCovData[0][((i + 1) * i / 2 + j)];
/* 202:    */       }
/* 203:415 */       return this.varCovData[0][((j + 1) * j / 2 + i)];
/* 204:    */     }
/* 205:419 */     return this.varCovData[i][j];
/* 206:    */   }
/* 207:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.RegressionResults
 * JD-Core Version:    0.7.0.1
 */
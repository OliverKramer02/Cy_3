/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.BivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NoDataException;
/*   6:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   7:    */ import org.apache.commons.math3.util.MathArrays;
/*   8:    */ 
/*   9:    */ public class BicubicSplineInterpolatingFunction
/*  10:    */   implements BivariateFunction
/*  11:    */ {
/*  12: 39 */   private static final double[][] AINV = { { 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D }, { -3.0D, 3.0D, 0.0D, 0.0D, -2.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D }, { 2.0D, -2.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, -3.0D, 3.0D, 0.0D, 0.0D, -2.0D, -1.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 2.0D, -2.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0D, 0.0D }, { -3.0D, 0.0D, 3.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, -2.0D, 0.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 0.0D, 0.0D, -3.0D, 0.0D, 3.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, -2.0D, 0.0D, -1.0D, 0.0D }, { 9.0D, -9.0D, -9.0D, 9.0D, 6.0D, 3.0D, -6.0D, -3.0D, 6.0D, -6.0D, 3.0D, -3.0D, 4.0D, 2.0D, 2.0D, 1.0D }, { -6.0D, 6.0D, 6.0D, -6.0D, -3.0D, -3.0D, 3.0D, 3.0D, -4.0D, 4.0D, -2.0D, 2.0D, -2.0D, -2.0D, -1.0D, -1.0D }, { 2.0D, 0.0D, -2.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 0.0D, 0.0D, 2.0D, 0.0D, -2.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D, 0.0D }, { -6.0D, 6.0D, 6.0D, -6.0D, -4.0D, -2.0D, 4.0D, 2.0D, -3.0D, 3.0D, -3.0D, 3.0D, -2.0D, -1.0D, -2.0D, -1.0D }, { 4.0D, -4.0D, -4.0D, 4.0D, 2.0D, 2.0D, -2.0D, -2.0D, 2.0D, -2.0D, 2.0D, -2.0D, 1.0D, 1.0D, 1.0D, 1.0D } };
/*  13:    */   private final double[] xval;
/*  14:    */   private final double[] yval;
/*  15:    */   private final BicubicSplineFunction[][] splines;
/*  16: 73 */   private BivariateFunction[][][] partialDerivatives = (BivariateFunction[][][])null;
/*  17:    */   
/*  18:    */   public BicubicSplineInterpolatingFunction(double[] x, double[] y, double[][] f, double[][] dFdX, double[][] dFdY, double[][] d2FdXdY)
/*  19:    */     throws DimensionMismatchException
/*  20:    */   {
/*  21: 98 */     int xLen = x.length;
/*  22: 99 */     int yLen = y.length;
/*  23:101 */     if ((xLen == 0) || (yLen == 0) || (f.length == 0) || (f[0].length == 0)) {
/*  24:102 */       throw new NoDataException();
/*  25:    */     }
/*  26:104 */     if (xLen != f.length) {
/*  27:105 */       throw new DimensionMismatchException(xLen, f.length);
/*  28:    */     }
/*  29:107 */     if (xLen != dFdX.length) {
/*  30:108 */       throw new DimensionMismatchException(xLen, dFdX.length);
/*  31:    */     }
/*  32:110 */     if (xLen != dFdY.length) {
/*  33:111 */       throw new DimensionMismatchException(xLen, dFdY.length);
/*  34:    */     }
/*  35:113 */     if (xLen != d2FdXdY.length) {
/*  36:114 */       throw new DimensionMismatchException(xLen, d2FdXdY.length);
/*  37:    */     }
/*  38:117 */     MathArrays.checkOrder(x);
/*  39:118 */     MathArrays.checkOrder(y);
/*  40:    */     
/*  41:120 */     this.xval = ((double[])x.clone());
/*  42:121 */     this.yval = ((double[])y.clone());
/*  43:    */     
/*  44:123 */     int lastI = xLen - 1;
/*  45:124 */     int lastJ = yLen - 1;
/*  46:125 */     this.splines = new BicubicSplineFunction[lastI][lastJ];
/*  47:127 */     for (int i = 0; i < lastI; i++)
/*  48:    */     {
/*  49:128 */       if (f[i].length != yLen) {
/*  50:129 */         throw new DimensionMismatchException(f[i].length, yLen);
/*  51:    */       }
/*  52:131 */       if (dFdX[i].length != yLen) {
/*  53:132 */         throw new DimensionMismatchException(dFdX[i].length, yLen);
/*  54:    */       }
/*  55:134 */       if (dFdY[i].length != yLen) {
/*  56:135 */         throw new DimensionMismatchException(dFdY[i].length, yLen);
/*  57:    */       }
/*  58:137 */       if (d2FdXdY[i].length != yLen) {
/*  59:138 */         throw new DimensionMismatchException(d2FdXdY[i].length, yLen);
/*  60:    */       }
/*  61:140 */       int ip1 = i + 1;
/*  62:141 */       for (int j = 0; j < lastJ; j++)
/*  63:    */       {
/*  64:142 */         int jp1 = j + 1;
/*  65:143 */         double[] beta = { f[i][j], f[ip1][j], f[i][jp1], f[ip1][jp1], dFdX[i][j], dFdX[ip1][j], dFdX[i][jp1], dFdX[ip1][jp1], dFdY[i][j], dFdY[ip1][j], dFdY[i][jp1], dFdY[ip1][jp1], d2FdXdY[i][j], d2FdXdY[ip1][j], d2FdXdY[i][jp1], d2FdXdY[ip1][jp1] };
/*  66:    */         
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:150 */         this.splines[i][j] = new BicubicSplineFunction(computeSplineCoefficients(beta));
/*  73:    */       }
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public double value(double x, double y)
/*  78:    */   {
/*  79:159 */     int i = searchIndex(x, this.xval);
/*  80:160 */     if (i == -1) {
/*  81:161 */       throw new OutOfRangeException(Double.valueOf(x), Double.valueOf(this.xval[0]), Double.valueOf(this.xval[(this.xval.length - 1)]));
/*  82:    */     }
/*  83:163 */     int j = searchIndex(y, this.yval);
/*  84:164 */     if (j == -1) {
/*  85:165 */       throw new OutOfRangeException(Double.valueOf(y), Double.valueOf(this.yval[0]), Double.valueOf(this.yval[(this.yval.length - 1)]));
/*  86:    */     }
/*  87:168 */     double xN = (x - this.xval[i]) / (this.xval[(i + 1)] - this.xval[i]);
/*  88:169 */     double yN = (y - this.yval[j]) / (this.yval[(j + 1)] - this.yval[j]);
/*  89:    */     
/*  90:171 */     return this.splines[i][j].value(xN, yN);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public double partialDerivativeX(double x, double y)
/*  94:    */   {
/*  95:181 */     return partialDerivative(0, x, y);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public double partialDerivativeY(double x, double y)
/*  99:    */   {
/* 100:190 */     return partialDerivative(1, x, y);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double partialDerivativeXX(double x, double y)
/* 104:    */   {
/* 105:199 */     return partialDerivative(2, x, y);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public double partialDerivativeYY(double x, double y)
/* 109:    */   {
/* 110:208 */     return partialDerivative(3, x, y);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public double partialDerivativeXY(double x, double y)
/* 114:    */   {
/* 115:216 */     return partialDerivative(4, x, y);
/* 116:    */   }
/* 117:    */   
/* 118:    */   private double partialDerivative(int which, double x, double y)
/* 119:    */   {
/* 120:226 */     if (this.partialDerivatives == null) {
/* 121:227 */       computePartialDerivatives();
/* 122:    */     }
/* 123:230 */     int i = searchIndex(x, this.xval);
/* 124:231 */     if (i == -1) {
/* 125:232 */       throw new OutOfRangeException(Double.valueOf(x), Double.valueOf(this.xval[0]), Double.valueOf(this.xval[(this.xval.length - 1)]));
/* 126:    */     }
/* 127:234 */     int j = searchIndex(y, this.yval);
/* 128:235 */     if (j == -1) {
/* 129:236 */       throw new OutOfRangeException(Double.valueOf(y), Double.valueOf(this.yval[0]), Double.valueOf(this.yval[(this.yval.length - 1)]));
/* 130:    */     }
/* 131:239 */     double xN = (x - this.xval[i]) / (this.xval[(i + 1)] - this.xval[i]);
/* 132:240 */     double yN = (y - this.yval[j]) / (this.yval[(j + 1)] - this.yval[j]);
/* 133:    */     
/* 134:242 */     return this.partialDerivatives[which][i][j].value(xN, yN);
/* 135:    */   }
/* 136:    */   
/* 137:    */   private void computePartialDerivatives()
/* 138:    */   {
/* 139:249 */     int lastI = this.xval.length - 1;
/* 140:250 */     int lastJ = this.yval.length - 1;
/* 141:251 */     this.partialDerivatives = new BivariateFunction[5][lastI][lastJ];
/* 142:253 */     for (int i = 0; i < lastI; i++) {
/* 143:254 */       for (int j = 0; j < lastJ; j++)
/* 144:    */       {
/* 145:255 */         BicubicSplineFunction f = this.splines[i][j];
/* 146:256 */         this.partialDerivatives[0][i][j] = f.partialDerivativeX();
/* 147:257 */         this.partialDerivatives[1][i][j] = f.partialDerivativeY();
/* 148:258 */         this.partialDerivatives[2][i][j] = f.partialDerivativeXX();
/* 149:259 */         this.partialDerivatives[3][i][j] = f.partialDerivativeYY();
/* 150:260 */         this.partialDerivatives[4][i][j] = f.partialDerivativeXY();
/* 151:    */       }
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   private int searchIndex(double c, double[] val)
/* 156:    */   {
/* 157:273 */     if (c < val[0]) {
/* 158:274 */       return -1;
/* 159:    */     }
/* 160:277 */     int max = val.length;
/* 161:278 */     for (int i = 1; i < max; i++) {
/* 162:279 */       if (c <= val[i]) {
/* 163:280 */         return i - 1;
/* 164:    */       }
/* 165:    */     }
/* 166:284 */     return -1;
/* 167:    */   }
/* 168:    */   
/* 169:    */   private double[] computeSplineCoefficients(double[] beta)
/* 170:    */   {
/* 171:317 */     double[] a = new double[16];
/* 172:319 */     for (int i = 0; i < 16; i++)
/* 173:    */     {
/* 174:320 */       double result = 0.0D;
/* 175:321 */       double[] row = AINV[i];
/* 176:322 */       for (int j = 0; j < 16; j++) {
/* 177:323 */         result += row[j] * beta[j];
/* 178:    */       }
/* 179:325 */       a[i] = result;
/* 180:    */     }
/* 181:328 */     return a;
/* 182:    */   }
/* 183:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolatingFunction
 * JD-Core Version:    0.7.0.1
 */
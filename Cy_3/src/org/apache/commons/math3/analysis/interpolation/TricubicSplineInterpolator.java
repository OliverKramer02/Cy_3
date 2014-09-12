/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.NoDataException;
/*   5:    */ import org.apache.commons.math3.util.MathArrays;
/*   6:    */ 
/*   7:    */ public class TricubicSplineInterpolator
/*   8:    */   implements TrivariateGridInterpolator
/*   9:    */ {
/*  10:    */   public TricubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[] zval, double[][][] fval)
/*  11:    */   {
/*  12: 38 */     if ((xval.length == 0) || (yval.length == 0) || (zval.length == 0) || (fval.length == 0)) {
/*  13: 39 */       throw new NoDataException();
/*  14:    */     }
/*  15: 41 */     if (xval.length != fval.length) {
/*  16: 42 */       throw new DimensionMismatchException(xval.length, fval.length);
/*  17:    */     }
/*  18: 45 */     MathArrays.checkOrder(xval);
/*  19: 46 */     MathArrays.checkOrder(yval);
/*  20: 47 */     MathArrays.checkOrder(zval);
/*  21:    */     
/*  22: 49 */     int xLen = xval.length;
/*  23: 50 */     int yLen = yval.length;
/*  24: 51 */     int zLen = zval.length;
/*  25:    */     
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29: 56 */     double[][][] fvalXY = new double[zLen][xLen][yLen];
/*  30: 57 */     double[][][] fvalZX = new double[yLen][zLen][xLen];
/*  31: 58 */     for (int i = 0; i < xLen; i++)
/*  32:    */     {
/*  33: 59 */       if (fval[i].length != yLen) {
/*  34: 60 */         throw new DimensionMismatchException(fval[i].length, yLen);
/*  35:    */       }
/*  36: 63 */       for (int j = 0; j < yLen; j++)
/*  37:    */       {
/*  38: 64 */         if (fval[i][j].length != zLen) {
/*  39: 65 */           throw new DimensionMismatchException(fval[i][j].length, zLen);
/*  40:    */         }
/*  41: 68 */         for (int k = 0; k < zLen; k++)
/*  42:    */         {
/*  43: 69 */           double v = fval[i][j][k];
/*  44: 70 */           fvalXY[k][i][j] = v;
/*  45: 71 */           fvalZX[j][k][i] = v;
/*  46:    */         }
/*  47:    */       }
/*  48:    */     }
/*  49: 76 */     BicubicSplineInterpolator bsi = new BicubicSplineInterpolator();
/*  50:    */     
/*  51:    */ 
/*  52: 79 */     BicubicSplineInterpolatingFunction[] xSplineYZ = new BicubicSplineInterpolatingFunction[xLen];
/*  53: 81 */     for (int i = 0; i < xLen; i++) {
/*  54: 82 */       xSplineYZ[i] = bsi.interpolate(yval, zval, fval[i]);
/*  55:    */     }
/*  56: 86 */     BicubicSplineInterpolatingFunction[] ySplineZX = new BicubicSplineInterpolatingFunction[yLen];
/*  57: 88 */     for (int j = 0; j < yLen; j++) {
/*  58: 89 */       ySplineZX[j] = bsi.interpolate(zval, xval, fvalZX[j]);
/*  59:    */     }
/*  60: 93 */     BicubicSplineInterpolatingFunction[] zSplineXY = new BicubicSplineInterpolatingFunction[zLen];
/*  61: 95 */     for (int k = 0; k < zLen; k++) {
/*  62: 96 */       zSplineXY[k] = bsi.interpolate(xval, yval, fvalXY[k]);
/*  63:    */     }
/*  64:100 */     double[][][] dFdX = new double[xLen][yLen][zLen];
/*  65:101 */     double[][][] dFdY = new double[xLen][yLen][zLen];
/*  66:102 */     double[][][] d2FdXdY = new double[xLen][yLen][zLen];
/*  67:103 */     for (int k = 0; k < zLen; k++)
/*  68:    */     {
/*  69:104 */       BicubicSplineInterpolatingFunction f = zSplineXY[k];
/*  70:105 */       for (int i = 0; i < xLen; i++)
/*  71:    */       {
/*  72:106 */         double x = xval[i];
/*  73:107 */         for (int j = 0; j < yLen; j++)
/*  74:    */         {
/*  75:108 */           double y = yval[j];
/*  76:109 */           dFdX[i][j][k] = f.partialDerivativeX(x, y);
/*  77:110 */           dFdY[i][j][k] = f.partialDerivativeY(x, y);
/*  78:111 */           d2FdXdY[i][j][k] = f.partialDerivativeXY(x, y);
/*  79:    */         }
/*  80:    */       }
/*  81:    */     }
/*  82:117 */     double[][][] dFdZ = new double[xLen][yLen][zLen];
/*  83:118 */     double[][][] d2FdYdZ = new double[xLen][yLen][zLen];
/*  84:119 */     for (int i = 0; i < xLen; i++)
/*  85:    */     {
/*  86:120 */       BicubicSplineInterpolatingFunction f = xSplineYZ[i];
/*  87:121 */       for (int j = 0; j < yLen; j++)
/*  88:    */       {
/*  89:122 */         double y = yval[j];
/*  90:123 */         for (int k = 0; k < zLen; k++)
/*  91:    */         {
/*  92:124 */           double z = zval[k];
/*  93:125 */           dFdZ[i][j][k] = f.partialDerivativeY(y, z);
/*  94:126 */           d2FdYdZ[i][j][k] = f.partialDerivativeXY(y, z);
/*  95:    */         }
/*  96:    */       }
/*  97:    */     }
/*  98:132 */     double[][][] d2FdZdX = new double[xLen][yLen][zLen];
/*  99:133 */     for (int j = 0; j < yLen; j++)
/* 100:    */     {
/* 101:134 */       BicubicSplineInterpolatingFunction f = ySplineZX[j];
/* 102:135 */       for (int k = 0; k < zLen; k++)
/* 103:    */       {
/* 104:136 */         double z = zval[k];
/* 105:137 */         for (int i = 0; i < xLen; i++)
/* 106:    */         {
/* 107:138 */           double x = xval[i];
/* 108:139 */           d2FdZdX[i][j][k] = f.partialDerivativeXY(z, x);
/* 109:    */         }
/* 110:    */       }
/* 111:    */     }
/* 112:145 */     double[][][] d3FdXdYdZ = new double[xLen][yLen][zLen];
/* 113:146 */     for (int i = 0; i < xLen; i++)
/* 114:    */     {
/* 115:147 */       int nI = nextIndex(i, xLen);
/* 116:148 */       int pI = previousIndex(i);
/* 117:149 */       for (int j = 0; j < yLen; j++)
/* 118:    */       {
/* 119:150 */         int nJ = nextIndex(j, yLen);
/* 120:151 */         int pJ = previousIndex(j);
/* 121:152 */         for (int k = 0; k < zLen; k++)
/* 122:    */         {
/* 123:153 */           int nK = nextIndex(k, zLen);
/* 124:154 */           int pK = previousIndex(k);
/* 125:    */           
/* 126:    */ 
/* 127:157 */           d3FdXdYdZ[i][j][k] = ((fval[nI][nJ][nK] - fval[nI][pJ][nK] - fval[pI][nJ][nK] + fval[pI][pJ][nK] - fval[nI][nJ][pK] + fval[nI][pJ][pK] + fval[pI][nJ][pK] - fval[pI][pJ][pK]) / ((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ]) * (zval[nK] - zval[pK])));
/* 128:    */         }
/* 129:    */       }
/* 130:    */     }
/* 131:167 */     return new TricubicSplineInterpolatingFunction(xval, yval, zval, fval, dFdX, dFdY, dFdZ, d2FdXdY, d2FdZdX, d2FdYdZ, d3FdXdYdZ);
/* 132:    */   }
/* 133:    */   
/* 134:    */   private int nextIndex(int i, int max)
/* 135:    */   {
/* 136:182 */     int index = i + 1;
/* 137:183 */     return index < max ? index : index - 1;
/* 138:    */   }
/* 139:    */   
/* 140:    */   private int previousIndex(int i)
/* 141:    */   {
/* 142:193 */     int index = i - 1;
/* 143:194 */     return index >= 0 ? index : 0;
/* 144:    */   }
/* 145:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.TricubicSplineInterpolator
 * JD-Core Version:    0.7.0.1
 */
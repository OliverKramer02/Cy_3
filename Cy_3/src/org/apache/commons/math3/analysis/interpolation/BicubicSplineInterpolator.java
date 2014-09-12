/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.NoDataException;
/*   7:    */ import org.apache.commons.math3.util.MathArrays;
/*   8:    */ 
/*   9:    */ public class BicubicSplineInterpolator
/*  10:    */   implements BivariateGridInterpolator
/*  11:    */ {
/*  12:    */   public BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval)
/*  13:    */   {
/*  14: 39 */     if ((xval.length == 0) || (yval.length == 0) || (fval.length == 0)) {
/*  15: 40 */       throw new NoDataException();
/*  16:    */     }
/*  17: 42 */     if (xval.length != fval.length) {
/*  18: 43 */       throw new DimensionMismatchException(xval.length, fval.length);
/*  19:    */     }
/*  20: 46 */     MathArrays.checkOrder(xval);
/*  21: 47 */     MathArrays.checkOrder(yval);
/*  22:    */     
/*  23: 49 */     int xLen = xval.length;
/*  24: 50 */     int yLen = yval.length;
/*  25:    */     
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30: 56 */     double[][] fX = new double[yLen][xLen];
/*  31: 57 */     for (int i = 0; i < xLen; i++)
/*  32:    */     {
/*  33: 58 */       if (fval[i].length != yLen) {
/*  34: 59 */         throw new DimensionMismatchException(fval[i].length, yLen);
/*  35:    */       }
/*  36: 62 */       for (int j = 0; j < yLen; j++) {
/*  37: 63 */         fX[j][i] = fval[i][j];
/*  38:    */       }
/*  39:    */     }
/*  40: 67 */     SplineInterpolator spInterpolator = new SplineInterpolator();
/*  41:    */     
/*  42:    */ 
/*  43:    */ 
/*  44: 71 */     PolynomialSplineFunction[] ySplineX = new PolynomialSplineFunction[yLen];
/*  45: 72 */     for (int j = 0; j < yLen; j++) {
/*  46: 73 */       ySplineX[j] = spInterpolator.interpolate(xval, fX[j]);
/*  47:    */     }
/*  48: 78 */     PolynomialSplineFunction[] xSplineY = new PolynomialSplineFunction[xLen];
/*  49: 79 */     for (int i = 0; i < xLen; i++) {
/*  50: 80 */       xSplineY[i] = spInterpolator.interpolate(yval, fval[i]);
/*  51:    */     }
/*  52: 84 */     double[][] dFdX = new double[xLen][yLen];
/*  53: 85 */     for (int j = 0; j < yLen; j++)
/*  54:    */     {
/*  55: 86 */       UnivariateFunction f = ySplineX[j].derivative();
/*  56: 87 */       for (int i = 0; i < xLen; i++) {
/*  57: 88 */         dFdX[i][j] = f.value(xval[i]);
/*  58:    */       }
/*  59:    */     }
/*  60: 93 */     double[][] dFdY = new double[xLen][yLen];
/*  61: 94 */     for (int i = 0; i < xLen; i++)
/*  62:    */     {
/*  63: 95 */       UnivariateFunction f = xSplineY[i].derivative();
/*  64: 96 */       for (int j = 0; j < yLen; j++) {
/*  65: 97 */         dFdY[i][j] = f.value(yval[j]);
/*  66:    */       }
/*  67:    */     }
/*  68:102 */     double[][] d2FdXdY = new double[xLen][yLen];
/*  69:103 */     for (int i = 0; i < xLen; i++)
/*  70:    */     {
/*  71:104 */       int nI = nextIndex(i, xLen);
/*  72:105 */       int pI = previousIndex(i);
/*  73:106 */       for (int j = 0; j < yLen; j++)
/*  74:    */       {
/*  75:107 */         int nJ = nextIndex(j, yLen);
/*  76:108 */         int pJ = previousIndex(j);
/*  77:109 */         d2FdXdY[i][j] = ((fval[nI][nJ] - fval[nI][pJ] - fval[pI][nJ] + fval[pI][pJ]) / ((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ])));
/*  78:    */       }
/*  79:    */     }
/*  80:116 */     return new BicubicSplineInterpolatingFunction(xval, yval, fval, dFdX, dFdY, d2FdXdY);
/*  81:    */   }
/*  82:    */   
/*  83:    */   private int nextIndex(int i, int max)
/*  84:    */   {
/*  85:129 */     int index = i + 1;
/*  86:130 */     return index < max ? index : index - 1;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private int previousIndex(int i)
/*  90:    */   {
/*  91:140 */     int index = i - 1;
/*  92:141 */     return index >= 0 ? index : 0;
/*  93:    */   }
/*  94:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolator
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NoDataException;
/*   6:    */ import org.apache.commons.math3.optimization.fitting.PolynomialFitter;
/*   7:    */ import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
/*   8:    */ import org.apache.commons.math3.util.MathArrays;
/*   9:    */ 
/*  10:    */ public class SmoothingPolynomialBicubicSplineInterpolator
/*  11:    */   extends BicubicSplineInterpolator
/*  12:    */ {
/*  13:    */   private final PolynomialFitter xFitter;
/*  14:    */   private final PolynomialFitter yFitter;
/*  15:    */   
/*  16:    */   public SmoothingPolynomialBicubicSplineInterpolator()
/*  17:    */   {
/*  18: 45 */     this(3);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public SmoothingPolynomialBicubicSplineInterpolator(int degree)
/*  22:    */   {
/*  23: 52 */     this(degree, degree);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SmoothingPolynomialBicubicSplineInterpolator(int xDegree, int yDegree)
/*  27:    */   {
/*  28: 63 */     this.xFitter = new PolynomialFitter(xDegree, new GaussNewtonOptimizer(false));
/*  29: 64 */     this.yFitter = new PolynomialFitter(yDegree, new GaussNewtonOptimizer(false));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval)
/*  33:    */   {
/*  34: 74 */     if ((xval.length == 0) || (yval.length == 0) || (fval.length == 0)) {
/*  35: 75 */       throw new NoDataException();
/*  36:    */     }
/*  37: 77 */     if (xval.length != fval.length) {
/*  38: 78 */       throw new DimensionMismatchException(xval.length, fval.length);
/*  39:    */     }
/*  40: 81 */     int xLen = xval.length;
/*  41: 82 */     int yLen = yval.length;
/*  42: 84 */     for (int i = 0; i < xLen; i++) {
/*  43: 85 */       if (fval[i].length != yLen) {
/*  44: 86 */         throw new DimensionMismatchException(fval[i].length, yLen);
/*  45:    */       }
/*  46:    */     }
/*  47: 90 */     MathArrays.checkOrder(xval);
/*  48: 91 */     MathArrays.checkOrder(yval);
/*  49:    */     
/*  50:    */ 
/*  51:    */ 
/*  52: 95 */     PolynomialFunction[] yPolyX = new PolynomialFunction[yLen];
/*  53: 96 */     for (int j = 0; j < yLen; j++)
/*  54:    */     {
/*  55: 97 */       this.xFitter.clearObservations();
/*  56: 98 */       for (int i = 0; i < xLen; i++) {
/*  57: 99 */         this.xFitter.addObservedPoint(1.0D, xval[i], fval[i][j]);
/*  58:    */       }
/*  59:102 */       yPolyX[j] = new PolynomialFunction(this.xFitter.fit());
/*  60:    */     }
/*  61:107 */     double[][] fval_1 = new double[xLen][yLen];
/*  62:108 */     for (int j = 0; j < yLen; j++)
/*  63:    */     {
/*  64:109 */       PolynomialFunction f = yPolyX[j];
/*  65:110 */       for (int i = 0; i < xLen; i++) {
/*  66:111 */         fval_1[i][j] = f.value(xval[i]);
/*  67:    */       }
/*  68:    */     }
/*  69:117 */     PolynomialFunction[] xPolyY = new PolynomialFunction[xLen];
/*  70:118 */     for (int i = 0; i < xLen; i++)
/*  71:    */     {
/*  72:119 */       this.yFitter.clearObservations();
/*  73:120 */       for (int j = 0; j < yLen; j++) {
/*  74:121 */         this.yFitter.addObservedPoint(1.0D, yval[j], fval_1[i][j]);
/*  75:    */       }
/*  76:124 */       xPolyY[i] = new PolynomialFunction(this.yFitter.fit());
/*  77:    */     }
/*  78:129 */     double[][] fval_2 = new double[xLen][yLen];
/*  79:130 */     for (int i = 0; i < xLen; i++)
/*  80:    */     {
/*  81:131 */       PolynomialFunction f = xPolyY[i];
/*  82:132 */       for (int j = 0; j < yLen; j++) {
/*  83:133 */         fval_2[i][j] = f.value(yval[j]);
/*  84:    */       }
/*  85:    */     }
/*  86:137 */     return super.interpolate(xval, yval, fval_2);
/*  87:    */   }
/*  88:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.SmoothingPolynomialBicubicSplineInterpolator
 * JD-Core Version:    0.7.0.1
 */
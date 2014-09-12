/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.TrivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ 
/*   6:    */ class TricubicSplineFunction
/*   7:    */   implements TrivariateFunction
/*   8:    */ {
/*   9:    */   private static final short N = 4;
/*  10:428 */   private final double[][][] a = new double[4][4][4];
/*  11:    */   
/*  12:    */   public TricubicSplineFunction(double[] aV)
/*  13:    */   {
/*  14:434 */     for (int i = 0; i < 4; i++) {
/*  15:435 */       for (int j = 0; j < 4; j++) {
/*  16:436 */         for (int k = 0; k < 4; k++) {
/*  17:437 */           this.a[i][j][k] = aV[(i + 4 * (j + 4 * k))];
/*  18:    */         }
/*  19:    */       }
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double value(double x, double y, double z)
/*  24:    */   {
/*  25:450 */     if ((x < 0.0D) || (x > 1.0D)) {
/*  26:451 */       throw new OutOfRangeException(Double.valueOf(x), Integer.valueOf(0), Integer.valueOf(1));
/*  27:    */     }
/*  28:453 */     if ((y < 0.0D) || (y > 1.0D)) {
/*  29:454 */       throw new OutOfRangeException(Double.valueOf(y), Integer.valueOf(0), Integer.valueOf(1));
/*  30:    */     }
/*  31:456 */     if ((z < 0.0D) || (z > 1.0D)) {
/*  32:457 */       throw new OutOfRangeException(Double.valueOf(z), Integer.valueOf(0), Integer.valueOf(1));
/*  33:    */     }
/*  34:460 */     double x2 = x * x;
/*  35:461 */     double x3 = x2 * x;
/*  36:462 */     double[] pX = { 1.0D, x, x2, x3 };
/*  37:    */     
/*  38:464 */     double y2 = y * y;
/*  39:465 */     double y3 = y2 * y;
/*  40:466 */     double[] pY = { 1.0D, y, y2, y3 };
/*  41:    */     
/*  42:468 */     double z2 = z * z;
/*  43:469 */     double z3 = z2 * z;
/*  44:470 */     double[] pZ = { 1.0D, z, z2, z3 };
/*  45:    */     
/*  46:472 */     double result = 0.0D;
/*  47:473 */     for (int i = 0; i < 4; i++) {
/*  48:474 */       for (int j = 0; j < 4; j++) {
/*  49:475 */         for (int k = 0; k < 4; k++) {
/*  50:476 */           result += this.a[i][j][k] * pX[i] * pY[j] * pZ[k];
/*  51:    */         }
/*  52:    */       }
/*  53:    */     }
/*  54:481 */     return result;
/*  55:    */   }
/*  56:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.TricubicSplineFunction
 * JD-Core Version:    0.7.0.1
 */
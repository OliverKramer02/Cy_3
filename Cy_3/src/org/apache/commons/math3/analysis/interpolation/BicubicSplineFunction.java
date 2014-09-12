/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.BivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ 
/*   6:    */ class BicubicSplineFunction
/*   7:    */   implements BivariateFunction
/*   8:    */ {
/*   9:    */   private static final short N = 4;
/*  10:    */   private final double[][] a;
/*  11:    */   private BivariateFunction partialDerivativeX;
/*  12:    */   private BivariateFunction partialDerivativeY;
/*  13:    */   private BivariateFunction partialDerivativeXX;
/*  14:    */   private BivariateFunction partialDerivativeYY;
/*  15:    */   private BivariateFunction partialDerivativeXY;
/*  16:    */   
/*  17:    */   public BicubicSplineFunction(double[] a)
/*  18:    */   {
/*  19:366 */     this.a = new double[4][4];
/*  20:367 */     for (int i = 0; i < 4; i++) {
/*  21:368 */       for (int j = 0; j < 4; j++) {
/*  22:369 */         this.a[i][j] = a[(i + 4 * j)];
/*  23:    */       }
/*  24:    */     }
/*  25:    */   }
/*  26:    */   
/*  27:    */   public double value(double x, double y)
/*  28:    */   {
/*  29:378 */     if ((x < 0.0D) || (x > 1.0D)) {
/*  30:379 */       throw new OutOfRangeException(Double.valueOf(x), Integer.valueOf(0), Integer.valueOf(1));
/*  31:    */     }
/*  32:381 */     if ((y < 0.0D) || (y > 1.0D)) {
/*  33:382 */       throw new OutOfRangeException(Double.valueOf(y), Integer.valueOf(0), Integer.valueOf(1));
/*  34:    */     }
/*  35:385 */     double x2 = x * x;
/*  36:386 */     double x3 = x2 * x;
/*  37:387 */     double[] pX = { 1.0D, x, x2, x3 };
/*  38:    */     
/*  39:389 */     double y2 = y * y;
/*  40:390 */     double y3 = y2 * y;
/*  41:391 */     double[] pY = { 1.0D, y, y2, y3 };
/*  42:    */     
/*  43:393 */     return apply(pX, pY, this.a);
/*  44:    */   }
/*  45:    */   
/*  46:    */   private double apply(double[] pX, double[] pY, double[][] coeff)
/*  47:    */   {
/*  48:405 */     double result = 0.0D;
/*  49:406 */     for (int i = 0; i < 4; i++) {
/*  50:407 */       for (int j = 0; j < 4; j++) {
/*  51:408 */         result += coeff[i][j] * pX[i] * pY[j];
/*  52:    */       }
/*  53:    */     }
/*  54:412 */     return result;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public BivariateFunction partialDerivativeX()
/*  58:    */   {
/*  59:419 */     if (this.partialDerivativeX == null) {
/*  60:420 */       computePartialDerivatives();
/*  61:    */     }
/*  62:423 */     return this.partialDerivativeX;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public BivariateFunction partialDerivativeY()
/*  66:    */   {
/*  67:429 */     if (this.partialDerivativeY == null) {
/*  68:430 */       computePartialDerivatives();
/*  69:    */     }
/*  70:433 */     return this.partialDerivativeY;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public BivariateFunction partialDerivativeXX()
/*  74:    */   {
/*  75:439 */     if (this.partialDerivativeXX == null) {
/*  76:440 */       computePartialDerivatives();
/*  77:    */     }
/*  78:443 */     return this.partialDerivativeXX;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public BivariateFunction partialDerivativeYY()
/*  82:    */   {
/*  83:449 */     if (this.partialDerivativeYY == null) {
/*  84:450 */       computePartialDerivatives();
/*  85:    */     }
/*  86:453 */     return this.partialDerivativeYY;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public BivariateFunction partialDerivativeXY()
/*  90:    */   {
/*  91:459 */     if (this.partialDerivativeXY == null) {
/*  92:460 */       computePartialDerivatives();
/*  93:    */     }
/*  94:463 */     return this.partialDerivativeXY;
/*  95:    */   }
/*  96:    */   
/*  97:    */   private void computePartialDerivatives()
/*  98:    */   {
/*  99:470 */     final double[][] aX = new double[4][4];
/* 100:471 */     final double[][] aY = new double[4][4];
/* 101:472 */     final double[][] aXX = new double[4][4];
/* 102:473 */     final double[][] aYY = new double[4][4];
/* 103:474 */     final double[][] aXY = new double[4][4];
/* 104:476 */     for (int i = 0; i < 4; i++) {
/* 105:477 */       for (int j = 0; j < 4; j++)
/* 106:    */       {
/* 107:478 */         double c = this.a[i][j];
/* 108:479 */         aX[i][j] = (i * c);
/* 109:480 */         aY[i][j] = (j * c);
/* 110:481 */         aXX[i][j] = ((i - 1) * aX[i][j]);
/* 111:482 */         aYY[i][j] = ((j - 1) * aY[i][j]);
/* 112:483 */         aXY[i][j] = (j * aX[i][j]);
/* 113:    */       }
/* 114:    */     }
/* 115:487 */     this.partialDerivativeX = new BivariateFunction()
/* 116:    */     {
/* 117:    */       public double value(double x, double y)
/* 118:    */       {
/* 119:489 */         double x2 = x * x;
/* 120:490 */         double[] pX = { 0.0D, 1.0D, x, x2 };
/* 121:    */         
/* 122:492 */         double y2 = y * y;
/* 123:493 */         double y3 = y2 * y;
/* 124:494 */         double[] pY = { 1.0D, y, y2, y3 };
/* 125:    */         
/* 126:496 */         return BicubicSplineFunction.this.apply(pX, pY, aX);
/* 127:    */       }
/* 128:498 */     };
/* 129:499 */     this.partialDerivativeY = new BivariateFunction()
/* 130:    */     {
/* 131:    */       public double value(double x, double y)
/* 132:    */       {
/* 133:501 */         double x2 = x * x;
/* 134:502 */         double x3 = x2 * x;
/* 135:503 */         double[] pX = { 1.0D, x, x2, x3 };
/* 136:    */         
/* 137:505 */         double y2 = y * y;
/* 138:506 */         double[] pY = { 0.0D, 1.0D, y, y2 };
/* 139:    */         
/* 140:508 */         return BicubicSplineFunction.this.apply(pX, pY, aY);
/* 141:    */       }
/* 142:510 */     };
/* 143:511 */     this.partialDerivativeXX = new BivariateFunction()
/* 144:    */     {
/* 145:    */       public double value(double x, double y)
/* 146:    */       {
/* 147:513 */         double[] pX = { 0.0D, 0.0D, 1.0D, x };
/* 148:    */         
/* 149:515 */         double y2 = y * y;
/* 150:516 */         double y3 = y2 * y;
/* 151:517 */         double[] pY = { 1.0D, y, y2, y3 };
/* 152:    */         
/* 153:519 */         return BicubicSplineFunction.this.apply(pX, pY, aXX);
/* 154:    */       }
/* 155:521 */     };
/* 156:522 */     this.partialDerivativeYY = new BivariateFunction()
/* 157:    */     {
/* 158:    */       public double value(double x, double y)
/* 159:    */       {
/* 160:524 */         double x2 = x * x;
/* 161:525 */         double x3 = x2 * x;
/* 162:526 */         double[] pX = { 1.0D, x, x2, x3 };
/* 163:    */         
/* 164:528 */         double[] pY = { 0.0D, 0.0D, 1.0D, y };
/* 165:    */         
/* 166:530 */         return BicubicSplineFunction.this.apply(pX, pY, aYY);
/* 167:    */       }
/* 168:532 */     };
/* 169:533 */     this.partialDerivativeXY = new BivariateFunction()
/* 170:    */     {
/* 171:    */       public double value(double x, double y)
/* 172:    */       {
/* 173:535 */         double x2 = x * x;
/* 174:536 */         double[] pX = { 0.0D, 1.0D, x, x2 };
/* 175:    */         
/* 176:538 */         double y2 = y * y;
/* 177:539 */         double[] pY = { 0.0D, 1.0D, y, y2 };
/* 178:    */         
/* 179:541 */         return BicubicSplineFunction.this.apply(pX, pY, aXY);
/* 180:    */       }
/* 181:    */     };
/* 182:    */   }
/* 183:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction
 * JD-Core Version:    0.7.0.1
 */
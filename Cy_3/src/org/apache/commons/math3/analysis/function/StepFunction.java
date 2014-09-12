/*  1:   */ package org.apache.commons.math3.analysis.function;
/*  2:   */ 
/*  3:   */ import java.util.Arrays;
/*  4:   */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*  5:   */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  6:   */ import org.apache.commons.math3.exception.NoDataException;
/*  7:   */ import org.apache.commons.math3.exception.NullArgumentException;
/*  8:   */ import org.apache.commons.math3.util.MathArrays;
/*  9:   */ 
/* 10:   */ public class StepFunction
/* 11:   */   implements UnivariateFunction
/* 12:   */ {
/* 13:   */   private final double[] abscissa;
/* 14:   */   private final double[] ordinate;
/* 15:   */   
/* 16:   */   public StepFunction(double[] x, double[] y)
/* 17:   */   {
/* 18:62 */     if ((x == null) || (y == null)) {
/* 19:64 */       throw new NullArgumentException();
/* 20:   */     }
/* 21:66 */     if ((x.length == 0) || (y.length == 0)) {
/* 22:68 */       throw new NoDataException();
/* 23:   */     }
/* 24:70 */     if (y.length != x.length) {
/* 25:71 */       throw new DimensionMismatchException(y.length, x.length);
/* 26:   */     }
/* 27:73 */     MathArrays.checkOrder(x);
/* 28:   */     
/* 29:75 */     this.abscissa = MathArrays.copyOf(x);
/* 30:76 */     this.ordinate = MathArrays.copyOf(y);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public double value(double x)
/* 34:   */   {
/* 35:81 */     int index = Arrays.binarySearch(this.abscissa, x);
/* 36:82 */     double fx = 0.0D;
/* 37:84 */     if (index < -1) {
/* 38:86 */       fx = this.ordinate[(-index - 2)];
/* 39:87 */     } else if (index >= 0) {
/* 40:89 */       fx = this.ordinate[index];
/* 41:   */     } else {
/* 42:93 */       fx = this.ordinate[0];
/* 43:   */     }
/* 44:96 */     return fx;
/* 45:   */   }
/* 46:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.StepFunction
 * JD-Core Version:    0.7.0.1
 */
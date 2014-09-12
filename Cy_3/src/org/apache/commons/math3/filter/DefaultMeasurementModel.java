/*  1:   */ package org.apache.commons.math3.filter;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*  4:   */ import org.apache.commons.math3.linear.RealMatrix;
/*  5:   */ 
/*  6:   */ public class DefaultMeasurementModel
/*  7:   */   implements MeasurementModel
/*  8:   */ {
/*  9:   */   private RealMatrix measurementMatrix;
/* 10:   */   private RealMatrix measurementNoise;
/* 11:   */   
/* 12:   */   public DefaultMeasurementModel(double[][] measMatrix, double[][] measNoise)
/* 13:   */   {
/* 14:51 */     this(new Array2DRowRealMatrix(measMatrix), new Array2DRowRealMatrix(measNoise));
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DefaultMeasurementModel(RealMatrix measMatrix, RealMatrix measNoise)
/* 18:   */   {
/* 19:64 */     this.measurementMatrix = measMatrix;
/* 20:65 */     this.measurementNoise = measNoise;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public RealMatrix getMeasurementMatrix()
/* 24:   */   {
/* 25:70 */     return this.measurementMatrix;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public RealMatrix getMeasurementNoise()
/* 29:   */   {
/* 30:75 */     return this.measurementNoise;
/* 31:   */   }
/* 32:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.filter.DefaultMeasurementModel
 * JD-Core Version:    0.7.0.1
 */
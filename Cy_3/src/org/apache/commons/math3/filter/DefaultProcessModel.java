/*   1:    */ package org.apache.commons.math3.filter;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   4:    */ import org.apache.commons.math3.linear.ArrayRealVector;
/*   5:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   6:    */ import org.apache.commons.math3.linear.RealVector;
/*   7:    */ 
/*   8:    */ public class DefaultProcessModel
/*   9:    */   implements ProcessModel
/*  10:    */ {
/*  11:    */   private RealMatrix stateTransitionMatrix;
/*  12:    */   private RealMatrix controlMatrix;
/*  13:    */   private RealMatrix processNoiseCovMatrix;
/*  14:    */   private RealVector initialStateEstimateVector;
/*  15:    */   private RealMatrix initialErrorCovMatrix;
/*  16:    */   
/*  17:    */   public DefaultProcessModel(double[][] stateTransition, double[][] control, double[][] processNoise, double[] initialStateEstimate, double[][] initialErrorCovariance)
/*  18:    */   {
/*  19: 68 */     this(new Array2DRowRealMatrix(stateTransition), new Array2DRowRealMatrix(control), new Array2DRowRealMatrix(processNoise), new ArrayRealVector(initialStateEstimate), new Array2DRowRealMatrix(initialErrorCovariance));
/*  20:    */   }
/*  21:    */   
/*  22:    */   public DefaultProcessModel(double[][] stateTransition, double[][] control, double[][] processNoise)
/*  23:    */   {
/*  24: 87 */     this(new Array2DRowRealMatrix(stateTransition), new Array2DRowRealMatrix(control), new Array2DRowRealMatrix(processNoise), null, null);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public DefaultProcessModel(RealMatrix stateTransition, RealMatrix control, RealMatrix processNoise, RealVector initialStateEstimate, RealMatrix initialErrorCovariance)
/*  28:    */   {
/*  29:107 */     this.stateTransitionMatrix = stateTransition;
/*  30:108 */     this.controlMatrix = control;
/*  31:109 */     this.processNoiseCovMatrix = processNoise;
/*  32:110 */     this.initialStateEstimateVector = initialStateEstimate;
/*  33:111 */     this.initialErrorCovMatrix = initialErrorCovariance;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public RealMatrix getStateTransitionMatrix()
/*  37:    */   {
/*  38:116 */     return this.stateTransitionMatrix;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public RealMatrix getControlMatrix()
/*  42:    */   {
/*  43:121 */     return this.controlMatrix;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public RealMatrix getProcessNoise()
/*  47:    */   {
/*  48:126 */     return this.processNoiseCovMatrix;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public RealVector getInitialStateEstimate()
/*  52:    */   {
/*  53:131 */     return this.initialStateEstimateVector;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public RealMatrix getInitialErrorCovariance()
/*  57:    */   {
/*  58:136 */     return this.initialErrorCovMatrix;
/*  59:    */   }
/*  60:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.filter.DefaultProcessModel
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.analysis.integration;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   4:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   5:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ import org.apache.commons.math3.util.Incrementor;
/*  10:    */ 
/*  11:    */ public class SimpsonIntegrator
/*  12:    */   extends BaseAbstractUnivariateIntegrator
/*  13:    */ {
/*  14:    */   public static final int SIMPSON_MAX_ITERATIONS_COUNT = 64;
/*  15:    */   
/*  16:    */   public SimpsonIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount)
/*  17:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException
/*  18:    */   {
/*  19: 62 */     super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
/*  20: 63 */     if (maximalIterationCount > 64) {
/*  21: 64 */       throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SimpsonIntegrator(int minimalIterationCount, int maximalIterationCount)
/*  26:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException
/*  27:    */   {
/*  28: 84 */     super(minimalIterationCount, maximalIterationCount);
/*  29: 85 */     if (maximalIterationCount > 64) {
/*  30: 86 */       throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SimpsonIntegrator()
/*  35:    */   {
/*  36: 96 */     super(3, 64);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected double doIntegrate()
/*  40:    */     throws TooManyEvaluationsException, MaxCountExceededException
/*  41:    */   {
/*  42:104 */     TrapezoidIntegrator qtrap = new TrapezoidIntegrator();
/*  43:105 */     if (getMinimalIterationCount() == 1) {
/*  44:106 */       return (4.0D * qtrap.stage(this, 1) - qtrap.stage(this, 0)) / 3.0D;
/*  45:    */     }
/*  46:110 */     double olds = 0.0D;
/*  47:111 */     double oldt = qtrap.stage(this, 0);
/*  48:    */     for (;;)
/*  49:    */     {
/*  50:113 */       double t = qtrap.stage(this, this.iterations.getCount());
/*  51:114 */       this.iterations.incrementCount();
/*  52:115 */       double s = (4.0D * t - oldt) / 3.0D;
/*  53:116 */       if (this.iterations.getCount() >= getMinimalIterationCount())
/*  54:    */       {
/*  55:117 */         double delta = FastMath.abs(s - olds);
/*  56:118 */         double rLimit = getRelativeAccuracy() * (FastMath.abs(olds) + FastMath.abs(s)) * 0.5D;
/*  57:120 */         if ((delta <= rLimit) || (delta <= getAbsoluteAccuracy())) {
/*  58:121 */           return s;
/*  59:    */         }
/*  60:    */       }
/*  61:124 */       olds = s;
/*  62:125 */       oldt = t;
/*  63:    */     }
/*  64:    */   }
/*  65:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.integration.SimpsonIntegrator
 * JD-Core Version:    0.7.0.1
 */
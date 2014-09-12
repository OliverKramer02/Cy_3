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
/*  11:    */ public class RombergIntegrator
/*  12:    */   extends BaseAbstractUnivariateIntegrator
/*  13:    */ {
/*  14:    */   public static final int ROMBERG_MAX_ITERATIONS_COUNT = 32;
/*  15:    */   
/*  16:    */   public RombergIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount)
/*  17:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException
/*  18:    */   {
/*  19: 63 */     super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
/*  20: 64 */     if (maximalIterationCount > 32) {
/*  21: 65 */       throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(32), false);
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   public RombergIntegrator(int minimalIterationCount, int maximalIterationCount)
/*  26:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException
/*  27:    */   {
/*  28: 85 */     super(minimalIterationCount, maximalIterationCount);
/*  29: 86 */     if (maximalIterationCount > 32) {
/*  30: 87 */       throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(32), false);
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public RombergIntegrator()
/*  35:    */   {
/*  36: 97 */     super(3, 32);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected double doIntegrate()
/*  40:    */     throws TooManyEvaluationsException, MaxCountExceededException
/*  41:    */   {
/*  42:105 */     int m = this.iterations.getMaximalCount() + 1;
/*  43:106 */     double[] previousRow = new double[m];
/*  44:107 */     double[] currentRow = new double[m];
/*  45:    */     
/*  46:109 */     TrapezoidIntegrator qtrap = new TrapezoidIntegrator();
/*  47:110 */     currentRow[0] = qtrap.stage(this, 0);
/*  48:111 */     this.iterations.incrementCount();
/*  49:112 */     double olds = currentRow[0];
/*  50:    */     for (;;)
/*  51:    */     {
/*  52:115 */       int i = this.iterations.getCount();
/*  53:    */       
/*  54:    */ 
/*  55:118 */       double[] tmpRow = previousRow;
/*  56:119 */       previousRow = currentRow;
/*  57:120 */       currentRow = tmpRow;
/*  58:    */       
/*  59:122 */       currentRow[0] = qtrap.stage(this, i);
/*  60:123 */       this.iterations.incrementCount();
/*  61:124 */       for (int j = 1; j <= i; j++)
/*  62:    */       {
/*  63:126 */         double r = (1L << 2 * j) - 1L;
/*  64:127 */         double tIJm1 = currentRow[(j - 1)];
/*  65:128 */         currentRow[j] = (tIJm1 + (tIJm1 - previousRow[(j - 1)]) / r);
/*  66:    */       }
/*  67:130 */       double s = currentRow[i];
/*  68:131 */       if (i >= getMinimalIterationCount())
/*  69:    */       {
/*  70:132 */         double delta = FastMath.abs(s - olds);
/*  71:133 */         double rLimit = getRelativeAccuracy() * (FastMath.abs(olds) + FastMath.abs(s)) * 0.5D;
/*  72:134 */         if ((delta <= rLimit) || (delta <= getAbsoluteAccuracy())) {
/*  73:135 */           return s;
/*  74:    */         }
/*  75:    */       }
/*  76:138 */       olds = s;
/*  77:    */     }
/*  78:    */   }
/*  79:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.integration.RombergIntegrator
 * JD-Core Version:    0.7.0.1
 */
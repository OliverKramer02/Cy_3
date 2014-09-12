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
/*  11:    */ public class TrapezoidIntegrator
/*  12:    */   extends BaseAbstractUnivariateIntegrator
/*  13:    */ {
/*  14:    */   public static final int TRAPEZOID_MAX_ITERATIONS_COUNT = 64;
/*  15:    */   private double s;
/*  16:    */   
/*  17:    */   public TrapezoidIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount)
/*  18:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException
/*  19:    */   {
/*  20: 64 */     super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
/*  21: 65 */     if (maximalIterationCount > 64) {
/*  22: 66 */       throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
/*  23:    */     }
/*  24:    */   }
/*  25:    */   
/*  26:    */   public TrapezoidIntegrator(int minimalIterationCount, int maximalIterationCount)
/*  27:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException
/*  28:    */   {
/*  29: 86 */     super(minimalIterationCount, maximalIterationCount);
/*  30: 87 */     if (maximalIterationCount > 64) {
/*  31: 88 */       throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public TrapezoidIntegrator()
/*  36:    */   {
/*  37: 98 */     super(3, 64);
/*  38:    */   }
/*  39:    */   
/*  40:    */   double stage(BaseAbstractUnivariateIntegrator baseIntegrator, int n)
/*  41:    */     throws TooManyEvaluationsException
/*  42:    */   {
/*  43:119 */     if (n == 0)
/*  44:    */     {
/*  45:120 */       double max = baseIntegrator.getMax();
/*  46:121 */       double min = baseIntegrator.getMin();
/*  47:122 */       this.s = (0.5D * (max - min) * (baseIntegrator.computeObjectiveValue(min) + baseIntegrator.computeObjectiveValue(max)));
/*  48:    */       
/*  49:    */ 
/*  50:125 */       return this.s;
/*  51:    */     }
/*  52:127 */     long np = 1L << n - 1;
/*  53:128 */     double sum = 0.0D;
/*  54:129 */     double max = baseIntegrator.getMax();
/*  55:130 */     double min = baseIntegrator.getMin();
/*  56:    */     
/*  57:132 */     double spacing = (max - min) / np;
/*  58:133 */     double x = min + 0.5D * spacing;
/*  59:134 */     for (long i = 0L; i < np; i += 1L)
/*  60:    */     {
/*  61:135 */       sum += baseIntegrator.computeObjectiveValue(x);
/*  62:136 */       x += spacing;
/*  63:    */     }
/*  64:139 */     this.s = (0.5D * (this.s + sum * spacing));
/*  65:140 */     return this.s;
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected double doIntegrate()
/*  69:    */     throws TooManyEvaluationsException, MaxCountExceededException
/*  70:    */   {
/*  71:149 */     double oldt = stage(this, 0);
/*  72:150 */     this.iterations.incrementCount();
/*  73:    */     for (;;)
/*  74:    */     {
/*  75:152 */       int i = this.iterations.getCount();
/*  76:153 */       double t = stage(this, i);
/*  77:154 */       if (i >= getMinimalIterationCount())
/*  78:    */       {
/*  79:155 */         double delta = FastMath.abs(t - oldt);
/*  80:156 */         double rLimit = getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t)) * 0.5D;
/*  81:158 */         if ((delta <= rLimit) || (delta <= getAbsoluteAccuracy())) {
/*  82:159 */           return t;
/*  83:    */         }
/*  84:    */       }
/*  85:162 */       oldt = t;
/*  86:163 */       this.iterations.incrementCount();
/*  87:    */     }
/*  88:    */   }
/*  89:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.integration.TrapezoidIntegrator
 * JD-Core Version:    0.7.0.1
 */
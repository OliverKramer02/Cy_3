/*   1:    */ package org.apache.commons.math3.analysis.integration;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ import org.apache.commons.math3.util.Incrementor;
/*  11:    */ 
/*  12:    */ public class LegendreGaussIntegrator
/*  13:    */   extends BaseAbstractUnivariateIntegrator
/*  14:    */ {
/*  15: 57 */   private static final double[] ABSCISSAS_2 = { -1.0D / FastMath.sqrt(3.0D), 1.0D / FastMath.sqrt(3.0D) };
/*  16: 63 */   private static final double[] WEIGHTS_2 = { 1.0D, 1.0D };
/*  17: 69 */   private static final double[] ABSCISSAS_3 = { -FastMath.sqrt(0.6D), 0.0D, FastMath.sqrt(0.6D) };
/*  18: 76 */   private static final double[] WEIGHTS_3 = { 0.5555555555555556D, 0.8888888888888888D, 0.5555555555555556D };
/*  19: 83 */   private static final double[] ABSCISSAS_4 = { -FastMath.sqrt((15.0D + 2.0D * FastMath.sqrt(30.0D)) / 35.0D), -FastMath.sqrt((15.0D - 2.0D * FastMath.sqrt(30.0D)) / 35.0D), FastMath.sqrt((15.0D - 2.0D * FastMath.sqrt(30.0D)) / 35.0D), FastMath.sqrt((15.0D + 2.0D * FastMath.sqrt(30.0D)) / 35.0D) };
/*  20: 91 */   private static final double[] WEIGHTS_4 = { (90.0D - 5.0D * FastMath.sqrt(30.0D)) / 180.0D, (90.0D + 5.0D * FastMath.sqrt(30.0D)) / 180.0D, (90.0D + 5.0D * FastMath.sqrt(30.0D)) / 180.0D, (90.0D - 5.0D * FastMath.sqrt(30.0D)) / 180.0D };
/*  21: 99 */   private static final double[] ABSCISSAS_5 = { -FastMath.sqrt((35.0D + 2.0D * FastMath.sqrt(70.0D)) / 63.0D), -FastMath.sqrt((35.0D - 2.0D * FastMath.sqrt(70.0D)) / 63.0D), 0.0D, FastMath.sqrt((35.0D - 2.0D * FastMath.sqrt(70.0D)) / 63.0D), FastMath.sqrt((35.0D + 2.0D * FastMath.sqrt(70.0D)) / 63.0D) };
/*  22:108 */   private static final double[] WEIGHTS_5 = { (322.0D - 13.0D * FastMath.sqrt(70.0D)) / 900.0D, (322.0D + 13.0D * FastMath.sqrt(70.0D)) / 900.0D, 0.5688888888888889D, (322.0D + 13.0D * FastMath.sqrt(70.0D)) / 900.0D, (322.0D - 13.0D * FastMath.sqrt(70.0D)) / 900.0D };
/*  23:    */   private final double[] abscissas;
/*  24:    */   private final double[] weights;
/*  25:    */   
/*  26:    */   public LegendreGaussIntegrator(int n, double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount)
/*  27:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException
/*  28:    */   {
/*  29:140 */     super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
/*  30:141 */     switch (n)
/*  31:    */     {
/*  32:    */     case 2: 
/*  33:143 */       this.abscissas = ABSCISSAS_2;
/*  34:144 */       this.weights = WEIGHTS_2;
/*  35:145 */       break;
/*  36:    */     case 3: 
/*  37:147 */       this.abscissas = ABSCISSAS_3;
/*  38:148 */       this.weights = WEIGHTS_3;
/*  39:149 */       break;
/*  40:    */     case 4: 
/*  41:151 */       this.abscissas = ABSCISSAS_4;
/*  42:152 */       this.weights = WEIGHTS_4;
/*  43:153 */       break;
/*  44:    */     case 5: 
/*  45:155 */       this.abscissas = ABSCISSAS_5;
/*  46:156 */       this.weights = WEIGHTS_5;
/*  47:157 */       break;
/*  48:    */     default: 
/*  49:159 */       throw new MathIllegalArgumentException(LocalizedFormats.N_POINTS_GAUSS_LEGENDRE_INTEGRATOR_NOT_SUPPORTED, new Object[] { Integer.valueOf(n), Integer.valueOf(2), Integer.valueOf(5) });
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public LegendreGaussIntegrator(int n, double relativeAccuracy, double absoluteAccuracy)
/*  54:    */   {
/*  55:175 */     this(n, relativeAccuracy, absoluteAccuracy, 3, 2147483647);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public LegendreGaussIntegrator(int n, int minimalIterationCount, int maximalIterationCount)
/*  59:    */   {
/*  60:192 */     this(n, 1.0E-006D, 1.E-015D, minimalIterationCount, maximalIterationCount);
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected double doIntegrate()
/*  64:    */     throws TooManyEvaluationsException, MaxCountExceededException
/*  65:    */   {
/*  66:202 */     double oldt = stage(1);
/*  67:    */     
/*  68:204 */     int n = 2;
/*  69:    */     for (;;)
/*  70:    */     {
/*  71:208 */       double t = stage(n);
/*  72:    */       
/*  73:    */ 
/*  74:211 */       double delta = FastMath.abs(t - oldt);
/*  75:212 */       double limit = FastMath.max(getAbsoluteAccuracy(), getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t)) * 0.5D);
/*  76:217 */       if ((this.iterations.getCount() + 1 >= getMinimalIterationCount()) && (delta <= limit)) {
/*  77:218 */         return t;
/*  78:    */       }
/*  79:222 */       double ratio = FastMath.min(4.0D, FastMath.pow(delta / limit, 0.5D / this.abscissas.length));
/*  80:223 */       n = FastMath.max((int)(ratio * n), n + 1);
/*  81:224 */       oldt = t;
/*  82:225 */       this.iterations.incrementCount();
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   private double stage(int n)
/*  87:    */     throws TooManyEvaluationsException
/*  88:    */   {
/*  89:242 */     double step = (getMax() - getMin()) / n;
/*  90:243 */     double halfStep = step / 2.0D;
/*  91:    */     
/*  92:    */ 
/*  93:246 */     double midPoint = getMin() + halfStep;
/*  94:247 */     double sum = 0.0D;
/*  95:248 */     for (int i = 0; i < n; i++)
/*  96:    */     {
/*  97:249 */       for (int j = 0; j < this.abscissas.length; j++) {
/*  98:250 */         sum += this.weights[j] * computeObjectiveValue(midPoint + halfStep * this.abscissas[j]);
/*  99:    */       }
/* 100:252 */       midPoint += step;
/* 101:    */     }
/* 102:255 */     return halfStep * sum;
/* 103:    */   }
/* 104:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.integration.LegendreGaussIntegrator
 * JD-Core Version:    0.7.0.1
 */
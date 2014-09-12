/*   1:    */ package org.apache.commons.math3.analysis.integration;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   7:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  10:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*  11:    */ import org.apache.commons.math3.util.Incrementor;
/*  12:    */ import org.apache.commons.math3.util.MathUtils;
/*  13:    */ 
/*  14:    */ public abstract class BaseAbstractUnivariateIntegrator
/*  15:    */   implements UnivariateIntegrator
/*  16:    */ {
/*  17:    */   public static final double DEFAULT_ABSOLUTE_ACCURACY = 1.E-015D;
/*  18:    */   public static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-006D;
/*  19:    */   public static final int DEFAULT_MIN_ITERATIONS_COUNT = 3;
/*  20:    */   public static final int DEFAULT_MAX_ITERATIONS_COUNT = 2147483647;
/*  21:    */   protected final Incrementor iterations;
/*  22:    */   private final double absoluteAccuracy;
/*  23:    */   private final double relativeAccuracy;
/*  24:    */   private final int minimalIterationCount;
/*  25:    */   private final Incrementor evaluations;
/*  26:    */   private UnivariateFunction function;
/*  27:    */   private double min;
/*  28:    */   private double max;
/*  29:    */   
/*  30:    */   protected BaseAbstractUnivariateIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount)
/*  31:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException
/*  32:    */   {
/*  33:116 */     this.relativeAccuracy = relativeAccuracy;
/*  34:117 */     this.absoluteAccuracy = absoluteAccuracy;
/*  35:120 */     if (minimalIterationCount <= 0) {
/*  36:121 */       throw new NotStrictlyPositiveException(Integer.valueOf(minimalIterationCount));
/*  37:    */     }
/*  38:123 */     if (maximalIterationCount <= minimalIterationCount) {
/*  39:124 */       throw new NumberIsTooSmallException(Integer.valueOf(maximalIterationCount), Integer.valueOf(minimalIterationCount), false);
/*  40:    */     }
/*  41:126 */     this.minimalIterationCount = minimalIterationCount;
/*  42:127 */     this.iterations = new Incrementor();
/*  43:128 */     this.iterations.setMaximalCount(maximalIterationCount);
/*  44:    */     
/*  45:    */ 
/*  46:131 */     this.evaluations = new Incrementor();
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected BaseAbstractUnivariateIntegrator(double relativeAccuracy, double absoluteAccuracy)
/*  50:    */   {
/*  51:142 */     this(relativeAccuracy, absoluteAccuracy, 3, 2147483647);
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected BaseAbstractUnivariateIntegrator(int minimalIterationCount, int maximalIterationCount)
/*  55:    */     throws NotStrictlyPositiveException, NumberIsTooSmallException
/*  56:    */   {
/*  57:158 */     this(1.0E-006D, 1.E-015D, minimalIterationCount, maximalIterationCount);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public double getRelativeAccuracy()
/*  61:    */   {
/*  62:164 */     return this.relativeAccuracy;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public double getAbsoluteAccuracy()
/*  66:    */   {
/*  67:169 */     return this.absoluteAccuracy;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getMinimalIterationCount()
/*  71:    */   {
/*  72:174 */     return this.minimalIterationCount;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public int getMaximalIterationCount()
/*  76:    */   {
/*  77:179 */     return this.iterations.getMaximalCount();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int getEvaluations()
/*  81:    */   {
/*  82:184 */     return this.evaluations.getCount();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getIterations()
/*  86:    */   {
/*  87:189 */     return this.iterations.getCount();
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected double getMin()
/*  91:    */   {
/*  92:196 */     return this.min;
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected double getMax()
/*  96:    */   {
/*  97:202 */     return this.max;
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected double computeObjectiveValue(double point)
/* 101:    */     throws TooManyEvaluationsException
/* 102:    */   {
/* 103:    */     try
/* 104:    */     {
/* 105:216 */       this.evaluations.incrementCount();
/* 106:    */     }
/* 107:    */     catch (MaxCountExceededException e)
/* 108:    */     {
/* 109:218 */       throw new TooManyEvaluationsException(e.getMax());
/* 110:    */     }
/* 111:220 */     return this.function.value(point);
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected void setup(int maxEval, UnivariateFunction f, double lower, double upper)
/* 115:    */     throws NullArgumentException, MathIllegalArgumentException
/* 116:    */   {
/* 117:241 */     MathUtils.checkNotNull(f);
/* 118:242 */     UnivariateSolverUtils.verifyInterval(lower, upper);
/* 119:    */     
/* 120:    */ 
/* 121:245 */     this.min = lower;
/* 122:246 */     this.max = upper;
/* 123:247 */     this.function = f;
/* 124:248 */     this.evaluations.setMaximalCount(maxEval);
/* 125:249 */     this.evaluations.resetCount();
/* 126:250 */     this.iterations.resetCount();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public double integrate(int maxEval, UnivariateFunction f, double lower, double upper)
/* 130:    */     throws TooManyEvaluationsException, MaxCountExceededException, MathIllegalArgumentException, NullArgumentException
/* 131:    */   {
/* 132:261 */     setup(maxEval, f, lower, upper);
/* 133:    */     
/* 134:    */ 
/* 135:264 */     return doIntegrate();
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected abstract double doIntegrate()
/* 139:    */     throws TooManyEvaluationsException, MaxCountExceededException;
/* 140:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator
 * JD-Core Version:    0.7.0.1
 */
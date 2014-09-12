/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.MultivariateVectorFunction;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   8:    */ import org.apache.commons.math3.optimization.BaseMultivariateVectorOptimizer;
/*   9:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*  10:    */ import org.apache.commons.math3.optimization.PointVectorValuePair;
/*  11:    */ import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
/*  12:    */ import org.apache.commons.math3.util.Incrementor;
/*  13:    */ 
/*  14:    */ public abstract class BaseAbstractMultivariateVectorOptimizer<FUNC extends MultivariateVectorFunction>
/*  15:    */   implements BaseMultivariateVectorOptimizer<FUNC>
/*  16:    */ {
/*  17: 44 */   protected final Incrementor evaluations = new Incrementor();
/*  18:    */   private ConvergenceChecker<PointVectorValuePair> checker;
/*  19:    */   private double[] target;
/*  20:    */   private double[] weight;
/*  21:    */   private double[] start;
/*  22:    */   private MultivariateVectorFunction function;
/*  23:    */   
/*  24:    */   protected BaseAbstractMultivariateVectorOptimizer()
/*  25:    */   {
/*  26: 62 */     this(new SimpleVectorValueChecker());
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected BaseAbstractMultivariateVectorOptimizer(ConvergenceChecker<PointVectorValuePair> checker)
/*  30:    */   {
/*  31: 68 */     this.checker = checker;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int getMaxEvaluations()
/*  35:    */   {
/*  36: 73 */     return this.evaluations.getMaximalCount();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public int getEvaluations()
/*  40:    */   {
/*  41: 78 */     return this.evaluations.getCount();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public ConvergenceChecker<PointVectorValuePair> getConvergenceChecker()
/*  45:    */   {
/*  46: 83 */     return this.checker;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected double[] computeObjectiveValue(double[] point)
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53: 96 */       this.evaluations.incrementCount();
/*  54:    */     }
/*  55:    */     catch (MaxCountExceededException e)
/*  56:    */     {
/*  57: 98 */       throw new TooManyEvaluationsException(e.getMax());
/*  58:    */     }
/*  59:100 */     return this.function.value(point);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public PointVectorValuePair optimize(int maxEval, FUNC f, double[] t, double[] w, double[] startPoint)
/*  63:    */   {
/*  64:107 */     if (f == null) {
/*  65:108 */       throw new NullArgumentException();
/*  66:    */     }
/*  67:110 */     if (t == null) {
/*  68:111 */       throw new NullArgumentException();
/*  69:    */     }
/*  70:113 */     if (w == null) {
/*  71:114 */       throw new NullArgumentException();
/*  72:    */     }
/*  73:116 */     if (startPoint == null) {
/*  74:117 */       throw new NullArgumentException();
/*  75:    */     }
/*  76:119 */     if (t.length != w.length) {
/*  77:120 */       throw new DimensionMismatchException(t.length, w.length);
/*  78:    */     }
/*  79:124 */     this.evaluations.setMaximalCount(maxEval);
/*  80:125 */     this.evaluations.resetCount();
/*  81:    */     
/*  82:    */ 
/*  83:128 */     this.function = f;
/*  84:129 */     this.target = ((double[])t.clone());
/*  85:130 */     this.weight = ((double[])w.clone());
/*  86:131 */     this.start = ((double[])startPoint.clone());
/*  87:    */     
/*  88:    */ 
/*  89:134 */     return doOptimize();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public double[] getStartPoint()
/*  93:    */   {
/*  94:141 */     return (double[])this.start.clone();
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected abstract PointVectorValuePair doOptimize();
/*  98:    */   
/*  99:    */   protected double[] getTargetRef()
/* 100:    */   {
/* 101:156 */     return this.target;
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected double[] getWeightRef()
/* 105:    */   {
/* 106:162 */     return this.weight;
/* 107:    */   }
/* 108:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateVectorOptimizer
 * JD-Core Version:    0.7.0.1
 */
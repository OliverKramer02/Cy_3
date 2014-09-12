/*   1:    */ package org.apache.commons.math3.optimization.univariate;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   7:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   8:    */ import org.apache.commons.math3.optimization.GoalType;
/*   9:    */ import org.apache.commons.math3.util.Incrementor;
/*  10:    */ 
/*  11:    */ public abstract class BaseAbstractUnivariateOptimizer
/*  12:    */   implements UnivariateOptimizer
/*  13:    */ {
/*  14:    */   private final ConvergenceChecker<UnivariatePointValuePair> checker;
/*  15: 40 */   private final Incrementor evaluations = new Incrementor();
/*  16:    */   private GoalType goal;
/*  17:    */   private double searchMin;
/*  18:    */   private double searchMax;
/*  19:    */   private double searchStart;
/*  20:    */   private UnivariateFunction function;
/*  21:    */   
/*  22:    */   protected BaseAbstractUnivariateOptimizer(ConvergenceChecker<UnivariatePointValuePair> checker)
/*  23:    */   {
/*  24: 56 */     this.checker = checker;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int getMaxEvaluations()
/*  28:    */   {
/*  29: 61 */     return this.evaluations.getMaximalCount();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getEvaluations()
/*  33:    */   {
/*  34: 66 */     return this.evaluations.getCount();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public GoalType getGoalType()
/*  38:    */   {
/*  39: 73 */     return this.goal;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double getMin()
/*  43:    */   {
/*  44: 79 */     return this.searchMin;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getMax()
/*  48:    */   {
/*  49: 85 */     return this.searchMax;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public double getStartValue()
/*  53:    */   {
/*  54: 91 */     return this.searchStart;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected double computeObjectiveValue(double point)
/*  58:    */   {
/*  59:    */     try
/*  60:    */     {
/*  61:104 */       this.evaluations.incrementCount();
/*  62:    */     }
/*  63:    */     catch (MaxCountExceededException e)
/*  64:    */     {
/*  65:106 */       throw new TooManyEvaluationsException(e.getMax());
/*  66:    */     }
/*  67:108 */     return this.function.value(point);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public UnivariatePointValuePair optimize(int maxEval, UnivariateFunction f, GoalType goalType, double min, double max, double startValue)
/*  71:    */   {
/*  72:117 */     if (f == null) {
/*  73:118 */       throw new NullArgumentException();
/*  74:    */     }
/*  75:120 */     if (goalType == null) {
/*  76:121 */       throw new NullArgumentException();
/*  77:    */     }
/*  78:125 */     this.searchMin = min;
/*  79:126 */     this.searchMax = max;
/*  80:127 */     this.searchStart = startValue;
/*  81:128 */     this.goal = goalType;
/*  82:129 */     this.function = f;
/*  83:130 */     this.evaluations.setMaximalCount(maxEval);
/*  84:131 */     this.evaluations.resetCount();
/*  85:    */     
/*  86:    */ 
/*  87:134 */     return doOptimize();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public UnivariatePointValuePair optimize(int maxEval, UnivariateFunction f, GoalType goalType, double min, double max)
/*  91:    */   {
/*  92:142 */     return optimize(maxEval, f, goalType, min, max, min + 0.5D * (max - min));
/*  93:    */   }
/*  94:    */   
/*  95:    */   public ConvergenceChecker<UnivariatePointValuePair> getConvergenceChecker()
/*  96:    */   {
/*  97:149 */     return this.checker;
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected abstract UnivariatePointValuePair doOptimize();
/* 101:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer
 * JD-Core Version:    0.7.0.1
 */
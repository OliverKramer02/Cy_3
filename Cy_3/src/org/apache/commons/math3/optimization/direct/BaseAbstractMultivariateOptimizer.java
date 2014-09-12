/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   7:    */ import org.apache.commons.math3.optimization.BaseMultivariateOptimizer;
/*   8:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   9:    */ import org.apache.commons.math3.optimization.GoalType;
/*  10:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*  11:    */ import org.apache.commons.math3.optimization.SimpleValueChecker;
/*  12:    */ import org.apache.commons.math3.util.Incrementor;
/*  13:    */ 
/*  14:    */ public abstract class BaseAbstractMultivariateOptimizer<FUNC extends MultivariateFunction>
/*  15:    */   implements BaseMultivariateOptimizer<FUNC>
/*  16:    */ {
/*  17: 44 */   protected final Incrementor evaluations = new Incrementor();
/*  18:    */   private ConvergenceChecker<PointValuePair> checker;
/*  19:    */   private GoalType goal;
/*  20:    */   private double[] start;
/*  21:    */   private MultivariateFunction function;
/*  22:    */   
/*  23:    */   protected BaseAbstractMultivariateOptimizer()
/*  24:    */   {
/*  25: 60 */     this(new SimpleValueChecker());
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected BaseAbstractMultivariateOptimizer(ConvergenceChecker<PointValuePair> checker)
/*  29:    */   {
/*  30: 66 */     this.checker = checker;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public int getMaxEvaluations()
/*  34:    */   {
/*  35: 71 */     return this.evaluations.getMaximalCount();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public int getEvaluations()
/*  39:    */   {
/*  40: 76 */     return this.evaluations.getCount();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public ConvergenceChecker<PointValuePair> getConvergenceChecker()
/*  44:    */   {
/*  45: 81 */     return this.checker;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected double computeObjectiveValue(double[] point)
/*  49:    */   {
/*  50:    */     try
/*  51:    */     {
/*  52: 94 */       this.evaluations.incrementCount();
/*  53:    */     }
/*  54:    */     catch (MaxCountExceededException e)
/*  55:    */     {
/*  56: 96 */       throw new TooManyEvaluationsException(e.getMax());
/*  57:    */     }
/*  58: 98 */     return this.function.value(point);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public PointValuePair optimize(int maxEval, FUNC f, GoalType goalType, double[] startPoint)
/*  62:    */   {
/*  63:105 */     if (f == null) {
/*  64:106 */       throw new NullArgumentException();
/*  65:    */     }
/*  66:108 */     if (goalType == null) {
/*  67:109 */       throw new NullArgumentException();
/*  68:    */     }
/*  69:111 */     if (startPoint == null) {
/*  70:112 */       throw new NullArgumentException();
/*  71:    */     }
/*  72:116 */     this.evaluations.setMaximalCount(maxEval);
/*  73:117 */     this.evaluations.resetCount();
/*  74:    */     
/*  75:    */ 
/*  76:120 */     this.function = f;
/*  77:121 */     this.goal = goalType;
/*  78:122 */     this.start = ((double[])startPoint.clone());
/*  79:    */     
/*  80:    */ 
/*  81:125 */     return doOptimize();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public GoalType getGoalType()
/*  85:    */   {
/*  86:132 */     return this.goal;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public double[] getStartPoint()
/*  90:    */   {
/*  91:139 */     return (double[])this.start.clone();
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected abstract PointValuePair doOptimize();
/*  95:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
 * JD-Core Version:    0.7.0.1
 */
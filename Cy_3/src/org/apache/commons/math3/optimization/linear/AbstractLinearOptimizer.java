/*   1:    */ package org.apache.commons.math3.optimization.linear;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   6:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   7:    */ import org.apache.commons.math3.optimization.GoalType;
/*   8:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*   9:    */ 
/*  10:    */ public abstract class AbstractLinearOptimizer
/*  11:    */   implements LinearOptimizer
/*  12:    */ {
/*  13:    */   public static final int DEFAULT_MAX_ITERATIONS = 100;
/*  14:    */   private LinearObjectiveFunction function;
/*  15:    */   private Collection<LinearConstraint> linearConstraints;
/*  16:    */   private GoalType goal;
/*  17:    */   private boolean nonNegative;
/*  18:    */   private int maxIterations;
/*  19:    */   private int iterations;
/*  20:    */   
/*  21:    */   protected AbstractLinearOptimizer()
/*  22:    */   {
/*  23: 75 */     setMaxIterations(100);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected boolean restrictToNonNegative()
/*  27:    */   {
/*  28: 82 */     return this.nonNegative;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected GoalType getGoalType()
/*  32:    */   {
/*  33: 89 */     return this.goal;
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected LinearObjectiveFunction getFunction()
/*  37:    */   {
/*  38: 96 */     return this.function;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected Collection<LinearConstraint> getConstraints()
/*  42:    */   {
/*  43:103 */     return Collections.unmodifiableCollection(this.linearConstraints);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setMaxIterations(int maxIterations)
/*  47:    */   {
/*  48:108 */     this.maxIterations = maxIterations;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getMaxIterations()
/*  52:    */   {
/*  53:113 */     return this.maxIterations;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getIterations()
/*  57:    */   {
/*  58:118 */     return this.iterations;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected void incrementIterationsCounter()
/*  62:    */     throws MaxCountExceededException
/*  63:    */   {
/*  64:127 */     if (++this.iterations > this.maxIterations) {
/*  65:128 */       throw new MaxCountExceededException(Integer.valueOf(this.maxIterations));
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public PointValuePair optimize(LinearObjectiveFunction f, Collection<LinearConstraint> constraints, GoalType goalType, boolean restrictToNonNegative)
/*  70:    */     throws MathIllegalStateException
/*  71:    */   {
/*  72:139 */     this.function = f;
/*  73:140 */     this.linearConstraints = constraints;
/*  74:141 */     this.goal = goalType;
/*  75:142 */     this.nonNegative = restrictToNonNegative;
/*  76:    */     
/*  77:144 */     this.iterations = 0;
/*  78:    */     
/*  79:    */ 
/*  80:147 */     return doOptimize();
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected abstract PointValuePair doOptimize()
/*  84:    */     throws MathIllegalStateException;
/*  85:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.AbstractLinearOptimizer
 * JD-Core Version:    0.7.0.1
 */
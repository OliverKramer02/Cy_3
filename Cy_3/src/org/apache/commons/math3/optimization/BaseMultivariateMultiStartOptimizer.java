/*   1:    */ package org.apache.commons.math3.optimization;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Comparator;

/*   5:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   7:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  10:    */ import org.apache.commons.math3.random.RandomVectorGenerator;
/*  11:    */ 
/*  12:    */ public class BaseMultivariateMultiStartOptimizer<FUNC extends MultivariateFunction>
/*  13:    */   implements BaseMultivariateOptimizer<FUNC>
/*  14:    */ {
/*  15:    */   private final BaseMultivariateOptimizer<FUNC> optimizer;
/*  16:    */   private int maxEvaluations;
/*  17:    */   private int totalEvaluations;
/*  18:    */   private int starts;
/*  19:    */   private RandomVectorGenerator generator;
/*  20:    */   private PointValuePair[] optima;
/*  21:    */   
/*  22:    */   protected BaseMultivariateMultiStartOptimizer(BaseMultivariateOptimizer<FUNC> optimizer, int starts, RandomVectorGenerator generator)
/*  23:    */   {
/*  24: 73 */     if ((optimizer == null) || (generator == null)) {
/*  25: 75 */       throw new NullArgumentException();
/*  26:    */     }
/*  27: 77 */     if (starts < 1) {
/*  28: 78 */       throw new NotStrictlyPositiveException(Integer.valueOf(starts));
/*  29:    */     }
/*  30: 81 */     this.optimizer = optimizer;
/*  31: 82 */     this.starts = starts;
/*  32: 83 */     this.generator = generator;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public PointValuePair[] getOptima()
/*  36:    */   {
/*  37:113 */     if (this.optima == null) {
/*  38:114 */       throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
/*  39:    */     }
/*  40:116 */     return (PointValuePair[])this.optima.clone();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public int getMaxEvaluations()
/*  44:    */   {
/*  45:121 */     return this.maxEvaluations;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int getEvaluations()
/*  49:    */   {
/*  50:126 */     return this.totalEvaluations;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public ConvergenceChecker<PointValuePair> getConvergenceChecker()
/*  54:    */   {
/*  55:131 */     return this.optimizer.getConvergenceChecker();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public PointValuePair optimize(int maxEval, FUNC f, GoalType goal, double[] startPoint)
/*  59:    */   {
/*  60:140 */     this.maxEvaluations = maxEval;
/*  61:141 */     RuntimeException lastException = null;
/*  62:142 */     this.optima = new PointValuePair[this.starts];
/*  63:143 */     this.totalEvaluations = 0;
/*  64:146 */     for (int i = 0; i < this.starts; i++)
/*  65:    */     {
/*  66:    */       try
/*  67:    */       {
/*  68:149 */         this.optima[i] = this.optimizer.optimize(maxEval - this.totalEvaluations, f, goal, i == 0 ? startPoint : this.generator.nextVector());
/*  69:    */       }
/*  70:    */       catch (RuntimeException mue)
/*  71:    */       {
/*  72:152 */         lastException = mue;
/*  73:153 */         this.optima[i] = null;
/*  74:    */       }
/*  75:157 */       this.totalEvaluations += this.optimizer.getEvaluations();
/*  76:    */     }
/*  77:160 */     sortPairs(goal);
/*  78:162 */     if (this.optima[0] == null) {
/*  79:163 */       throw lastException;
/*  80:    */     }
/*  81:167 */     return this.optima[0];
/*  82:    */   }
/*  83:    */   
/*  84:    */   private void sortPairs(final GoalType goal)
/*  85:    */   {
/*  86:176 */     Arrays.sort(this.optima, new Comparator()
/*  87:    */     {
/*  88:    */       public int compare(PointValuePair o1, PointValuePair o2)
/*  89:    */       {
/*  90:179 */         if (o1 == null) {
/*  91:180 */           return o2 == null ? 0 : 1;
/*  92:    */         }
/*  93:181 */         if (o2 == null) {
/*  94:182 */           return -1;
/*  95:    */         }
/*  96:184 */         double v1 = ((Double)o1.getValue()).doubleValue();
/*  97:185 */         double v2 = ((Double)o2.getValue()).doubleValue();
/*  98:186 */         return goal == GoalType.MINIMIZE ? Double.compare(v1, v2) : Double.compare(v2, v1);
/*  99:    */       }
/* 100:    */

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}     });
/* 101:    */   }
/* 102:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.BaseMultivariateMultiStartOptimizer
 * JD-Core Version:    0.7.0.1
 */
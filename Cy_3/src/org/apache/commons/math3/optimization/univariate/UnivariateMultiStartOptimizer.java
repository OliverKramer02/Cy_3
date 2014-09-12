/*   1:    */ package org.apache.commons.math3.optimization.univariate;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Comparator;

/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   7:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  10:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*  11:    */ import org.apache.commons.math3.optimization.GoalType;
/*  12:    */ import org.apache.commons.math3.random.RandomGenerator;
/*  13:    */ 
/*  14:    */ public class UnivariateMultiStartOptimizer<FUNC extends UnivariateFunction>
/*  15:    */   implements BaseUnivariateOptimizer<FUNC>
/*  16:    */ {
/*  17:    */   private final BaseUnivariateOptimizer<FUNC> optimizer;
/*  18:    */   private int maxEvaluations;
/*  19:    */   private int totalEvaluations;
/*  20:    */   private int starts;
/*  21:    */   private RandomGenerator generator;
/*  22:    */   private UnivariatePointValuePair[] optima;
/*  23:    */   
/*  24:    */   public UnivariateMultiStartOptimizer(BaseUnivariateOptimizer<FUNC> optimizer, int starts, RandomGenerator generator)
/*  25:    */   {
/*  26: 75 */     if ((optimizer == null) || (generator == null)) {
/*  27: 77 */       throw new NullArgumentException();
/*  28:    */     }
/*  29: 79 */     if (starts < 1) {
/*  30: 80 */       throw new NotStrictlyPositiveException(Integer.valueOf(starts));
/*  31:    */     }
/*  32: 83 */     this.optimizer = optimizer;
/*  33: 84 */     this.starts = starts;
/*  34: 85 */     this.generator = generator;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ConvergenceChecker<UnivariatePointValuePair> getConvergenceChecker()
/*  38:    */   {
/*  39: 92 */     return this.optimizer.getConvergenceChecker();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getMaxEvaluations()
/*  43:    */   {
/*  44: 97 */     return this.maxEvaluations;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int getEvaluations()
/*  48:    */   {
/*  49:102 */     return this.totalEvaluations;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public UnivariatePointValuePair[] getOptima()
/*  53:    */   {
/*  54:133 */     if (this.optima == null) {
/*  55:134 */       throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
/*  56:    */     }
/*  57:136 */     return (UnivariatePointValuePair[])this.optima.clone();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public UnivariatePointValuePair optimize(int maxEval, FUNC f, GoalType goal, double min, double max)
/*  61:    */   {
/*  62:143 */     return optimize(maxEval, f, goal, min, max, min + 0.5D * (max - min));
/*  63:    */   }
/*  64:    */   
/*  65:    */   public UnivariatePointValuePair optimize(int maxEval, FUNC f, GoalType goal, double min, double max, double startValue)
/*  66:    */   {
/*  67:151 */     RuntimeException lastException = null;
/*  68:152 */     this.optima = new UnivariatePointValuePair[this.starts];
/*  69:153 */     this.totalEvaluations = 0;
/*  70:156 */     for (int i = 0; i < this.starts; i++)
/*  71:    */     {
/*  72:    */       try
/*  73:    */       {
/*  74:159 */         double s = i == 0 ? startValue : min + this.generator.nextDouble() * (max - min);
/*  75:160 */         this.optima[i] = this.optimizer.optimize(maxEval - this.totalEvaluations, f, goal, min, max, s);
/*  76:    */       }
/*  77:    */       catch (RuntimeException mue)
/*  78:    */       {
/*  79:162 */         lastException = mue;
/*  80:163 */         this.optima[i] = null;
/*  81:    */       }
/*  82:167 */       this.totalEvaluations += this.optimizer.getEvaluations();
/*  83:    */     }
/*  84:170 */     sortPairs(goal);
/*  85:172 */     if (this.optima[0] == null) {
/*  86:173 */       throw lastException;
/*  87:    */     }
/*  88:177 */     return this.optima[0];
/*  89:    */   }
/*  90:    */   
/*  91:    */   private void sortPairs(final GoalType goal)
/*  92:    */   {
/*  93:186 */     Arrays.sort(this.optima, new Comparator()
/*  94:    */     {
/*  95:    */       public int compare(UnivariatePointValuePair o1, UnivariatePointValuePair o2)
/*  96:    */       {
/*  97:189 */         if (o1 == null) {
/*  98:190 */           return o2 == null ? 0 : 1;
/*  99:    */         }
/* 100:191 */         if (o2 == null) {
/* 101:192 */           return -1;
/* 102:    */         }
/* 103:194 */         double v1 = o1.getValue();
/* 104:195 */         double v2 = o2.getValue();
/* 105:196 */         return goal == GoalType.MINIMIZE ? Double.compare(v1, v2) : Double.compare(v2, v1);
/* 106:    */       }
/* 107:    */

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}     });
/* 108:    */   }
/* 109:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.univariate.UnivariateMultiStartOptimizer
 * JD-Core Version:    0.7.0.1
 */
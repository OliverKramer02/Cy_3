/*   1:    */ package org.apache.commons.math3.optimization;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Comparator;

/*   5:    */ import org.apache.commons.math3.analysis.MultivariateVectorFunction;
/*   6:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   8:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   9:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  11:    */ import org.apache.commons.math3.random.RandomVectorGenerator;
/*  12:    */ 
/*  13:    */ public class BaseMultivariateVectorMultiStartOptimizer<FUNC extends MultivariateVectorFunction>
/*  14:    */   implements BaseMultivariateVectorOptimizer<FUNC>
/*  15:    */ {
/*  16:    */   private final BaseMultivariateVectorOptimizer<FUNC> optimizer;
/*  17:    */   private int maxEvaluations;
/*  18:    */   private int totalEvaluations;
/*  19:    */   private int starts;
/*  20:    */   private RandomVectorGenerator generator;
/*  21:    */   private PointVectorValuePair[] optima;
/*  22:    */   
/*  23:    */   protected BaseMultivariateVectorMultiStartOptimizer(BaseMultivariateVectorOptimizer<FUNC> optimizer, int starts, RandomVectorGenerator generator)
/*  24:    */   {
/*  25: 73 */     if ((optimizer == null) || (generator == null)) {
/*  26: 75 */       throw new NullArgumentException();
/*  27:    */     }
/*  28: 77 */     if (starts < 1) {
/*  29: 78 */       throw new NotStrictlyPositiveException(Integer.valueOf(starts));
/*  30:    */     }
/*  31: 81 */     this.optimizer = optimizer;
/*  32: 82 */     this.starts = starts;
/*  33: 83 */     this.generator = generator;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public PointVectorValuePair[] getOptima()
/*  37:    */   {
/*  38:114 */     if (this.optima == null) {
/*  39:115 */       throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
/*  40:    */     }
/*  41:117 */     return (PointVectorValuePair[])this.optima.clone();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getMaxEvaluations()
/*  45:    */   {
/*  46:122 */     return this.maxEvaluations;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getEvaluations()
/*  50:    */   {
/*  51:127 */     return this.totalEvaluations;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public ConvergenceChecker<PointVectorValuePair> getConvergenceChecker()
/*  55:    */   {
/*  56:132 */     return this.optimizer.getConvergenceChecker();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public PointVectorValuePair optimize(int maxEval, FUNC f, double[] target, double[] weights, double[] startPoint)
/*  60:    */   {
/*  61:141 */     this.maxEvaluations = maxEval;
/*  62:142 */     RuntimeException lastException = null;
/*  63:143 */     this.optima = new PointVectorValuePair[this.starts];
/*  64:144 */     this.totalEvaluations = 0;
/*  65:147 */     for (int i = 0; i < this.starts; i++)
/*  66:    */     {
/*  67:    */       try
/*  68:    */       {
/*  69:151 */         this.optima[i] = this.optimizer.optimize(maxEval - this.totalEvaluations, f, target, weights, i == 0 ? startPoint : this.generator.nextVector());
/*  70:    */       }
/*  71:    */       catch (ConvergenceException oe)
/*  72:    */       {
/*  73:154 */         this.optima[i] = null;
/*  74:    */       }
/*  75:    */       catch (RuntimeException mue)
/*  76:    */       {
/*  77:156 */         lastException = mue;
/*  78:157 */         this.optima[i] = null;
/*  79:    */       }
/*  80:161 */       this.totalEvaluations += this.optimizer.getEvaluations();
/*  81:    */     }
/*  82:164 */     sortPairs(target, weights);
/*  83:166 */     if (this.optima[0] == null) {
/*  84:167 */       throw lastException;
/*  85:    */     }
/*  86:171 */     return this.optima[0];
/*  87:    */   }
/*  88:    */   
/*  89:    */   private void sortPairs(final double[] target, final double[] weights)
/*  90:    */   {
/*  91:182 */     Arrays.sort(this.optima, new Comparator()
/*  92:    */     {
/*  93:    */       public int compare(PointVectorValuePair o1, PointVectorValuePair o2)
/*  94:    */       {
/*  95:185 */         if (o1 == null) {
/*  96:186 */           return o2 == null ? 0 : 1;
/*  97:    */         }
/*  98:187 */         if (o2 == null) {
/*  99:188 */           return -1;
/* 100:    */         }
/* 101:190 */         return Double.compare(weightedResidual(o1), weightedResidual(o2));
/* 102:    */       }
/* 103:    */       
/* 104:    */       private double weightedResidual(PointVectorValuePair pv)
/* 105:    */       {
/* 106:193 */         double[] value = pv.getValueRef();
/* 107:194 */         double sum = 0.0D;
/* 108:195 */         for (int i = 0; i < value.length; i++)
/* 109:    */         {
/* 110:196 */           double ri = value[i] - target[i];
/* 111:197 */           sum += weights[i] * ri * ri;
/* 112:    */         }
/* 113:199 */         return sum;
/* 114:    */       }
/* 115:    */
@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}     });
/* 116:    */   }
/* 117:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.BaseMultivariateVectorMultiStartOptimizer
 * JD-Core Version:    0.7.0.1
 */
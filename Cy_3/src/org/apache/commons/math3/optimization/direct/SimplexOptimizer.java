/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import java.util.Comparator;

/*   4:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   7:    */ import org.apache.commons.math3.optimization.GoalType;
/*   8:    */ import org.apache.commons.math3.optimization.MultivariateOptimizer;
/*   9:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*  10:    */ import org.apache.commons.math3.optimization.SimpleValueChecker;
/*  11:    */ 
/*  12:    */ public class SimplexOptimizer
/*  13:    */   extends BaseAbstractMultivariateOptimizer<MultivariateFunction>
/*  14:    */   implements MultivariateOptimizer
/*  15:    */ {
/*  16:    */   private AbstractSimplex simplex;
/*  17:    */   
/*  18:    */   public SimplexOptimizer()
/*  19:    */   {
/*  20: 97 */     this(new SimpleValueChecker());
/*  21:    */   }
/*  22:    */   
/*  23:    */   public SimplexOptimizer(ConvergenceChecker<PointValuePair> checker)
/*  24:    */   {
/*  25:104 */     super(checker);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public SimplexOptimizer(double rel, double abs)
/*  29:    */   {
/*  30:112 */     this(new SimpleValueChecker(rel, abs));
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setSimplex(AbstractSimplex simplex)
/*  34:    */   {
/*  35:121 */     this.simplex = simplex;
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected PointValuePair doOptimize()
/*  39:    */   {
/*  40:127 */     if (this.simplex == null) {
/*  41:128 */       throw new NullArgumentException();
/*  42:    */     }
/*  43:133 */     MultivariateFunction evalFunc = new MultivariateFunction()
/*  44:    */     {
/*  45:    */       public double value(double[] point)
/*  46:    */       {
/*  47:136 */         return SimplexOptimizer.this.computeObjectiveValue(point);
/*  48:    */       }
/*  49:139 */     };
/*  50:140 */     final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
/*  51:141 */     Comparator<PointValuePair> comparator = new Comparator()
/*  52:    */     {
/*  53:    */       public int compare(PointValuePair o1, PointValuePair o2)
/*  54:    */       {
/*  55:145 */         double v1 = ((Double)o1.getValue()).doubleValue();
/*  56:146 */         double v2 = ((Double)o2.getValue()).doubleValue();
/*  57:147 */         return isMinim ? Double.compare(v1, v2) : Double.compare(v2, v1);
/*  58:    */       }
/*  59:151 */

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}     };
/*  60:152 */     this.simplex.build(getStartPoint());
/*  61:153 */     this.simplex.evaluate(evalFunc, comparator);
/*  62:    */     
/*  63:155 */     PointValuePair[] previous = null;
/*  64:156 */     int iteration = 0;
/*  65:157 */     ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
/*  66:    */     for (;;)
/*  67:    */     {
/*  68:159 */       if (iteration > 0)
/*  69:    */       {
/*  70:160 */         boolean converged = true;
/*  71:161 */         for (int i = 0; i < this.simplex.getSize(); i++)
/*  72:    */         {
/*  73:162 */           PointValuePair prev = previous[i];
/*  74:163 */           converged &= checker.converged(iteration, prev, this.simplex.getPoint(i));
/*  75:    */         }
/*  76:165 */         if (converged) {
/*  77:167 */           return this.simplex.getPoint(0);
/*  78:    */         }
/*  79:    */       }
/*  80:172 */       previous = this.simplex.getPoints();
/*  81:173 */       this.simplex.iterate(evalFunc, comparator);
/*  82:174 */       iteration++;
/*  83:    */     }
/*  84:    */   }
/*  85:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.SimplexOptimizer
 * JD-Core Version:    0.7.0.1
 */
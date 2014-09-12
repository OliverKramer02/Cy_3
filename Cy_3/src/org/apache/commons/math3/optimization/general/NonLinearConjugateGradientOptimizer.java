/*   1:    */ package org.apache.commons.math3.optimization.general;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.solvers.BrentSolver;
/*   5:    */ import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   9:    */ import org.apache.commons.math3.optimization.GoalType;
/*  10:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*  11:    */ import org.apache.commons.math3.optimization.SimpleValueChecker;
/*  12:    */ import org.apache.commons.math3.util.FastMath;
/*  13:    */ 
/*  14:    */ public class NonLinearConjugateGradientOptimizer
/*  15:    */   extends AbstractScalarDifferentiableOptimizer
/*  16:    */ {
/*  17:    */   private final ConjugateGradientFormula updateFormula;
/*  18:    */   private final Preconditioner preconditioner;
/*  19:    */   private final UnivariateSolver solver;
/*  20:    */   private double initialStep;
/*  21:    */   private double[] point;
/*  22:    */   
/*  23:    */   public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula)
/*  24:    */   {
/*  25: 66 */     this(updateFormula, new SimpleValueChecker());
/*  26:    */   }
/*  27:    */   
/*  28:    */   public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula, ConvergenceChecker<PointValuePair> checker)
/*  29:    */   {
/*  30: 81 */     this(updateFormula, checker, new BrentSolver(), new IdentityPreconditioner());
/*  31:    */   }
/*  32:    */   
/*  33:    */   public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver)
/*  34:    */   {
/*  35:100 */     this(updateFormula, checker, lineSearchSolver, new IdentityPreconditioner());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver, Preconditioner preconditioner)
/*  39:    */   {
/*  40:118 */     super(checker);
/*  41:    */     
/*  42:120 */     this.updateFormula = updateFormula;
/*  43:121 */     this.solver = lineSearchSolver;
/*  44:122 */     this.preconditioner = preconditioner;
/*  45:123 */     this.initialStep = 1.0D;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setInitialStep(double initialStep)
/*  49:    */   {
/*  50:137 */     if (initialStep <= 0.0D) {
/*  51:138 */       this.initialStep = 1.0D;
/*  52:    */     } else {
/*  53:140 */       this.initialStep = initialStep;
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected PointValuePair doOptimize()
/*  58:    */   {
/*  59:147 */     ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
/*  60:148 */     this.point = getStartPoint();
/*  61:149 */     GoalType goal = getGoalType();
/*  62:150 */     int n = this.point.length;
/*  63:151 */     double[] r = computeObjectiveGradient(this.point);
/*  64:152 */     if (goal == GoalType.MINIMIZE) {
/*  65:153 */       for (int i = 0; i < n; i++) {
/*  66:154 */         r[i] = (-r[i]);
/*  67:    */       }
/*  68:    */     }
/*  69:159 */     double[] steepestDescent = this.preconditioner.precondition(this.point, r);
/*  70:160 */     double[] searchDirection = (double[])steepestDescent.clone();
/*  71:    */     
/*  72:162 */     double delta = 0.0D;
/*  73:163 */     for (int i = 0; i < n; i++) {
/*  74:164 */       delta += r[i] * searchDirection[i];
/*  75:    */     }
/*  76:167 */     PointValuePair current = null;
/*  77:168 */     int iter = 0;
/*  78:169 */     int maxEval = getMaxEvaluations();
/*  79:    */     for (;;)
/*  80:    */     {
/*  81:171 */       iter++;
/*  82:    */       
/*  83:173 */       double objective = computeObjectiveValue(this.point);
/*  84:174 */       PointValuePair previous = current;
/*  85:175 */       current = new PointValuePair(this.point, objective);
/*  86:176 */       if ((previous != null) && 
/*  87:177 */         (checker.converged(iter, previous, current))) {
/*  88:179 */         return current;
/*  89:    */       }
/*  90:184 */       UnivariateFunction lsf = new LineSearchFunction(searchDirection);
/*  91:185 */       double uB = findUpperBound(lsf, 0.0D, this.initialStep);
/*  92:    */       
/*  93:    */ 
/*  94:    */ 
/*  95:189 */       double step = this.solver.solve(maxEval, lsf, 0.0D, uB, 1.E-015D);
/*  96:190 */       maxEval -= this.solver.getEvaluations();
/*  97:193 */       for (int i = 0; i < this.point.length; i++) {
/*  98:194 */         this.point[i] += step * searchDirection[i];
/*  99:    */       }
/* 100:197 */       r = computeObjectiveGradient(this.point);
/* 101:198 */       if (goal == GoalType.MINIMIZE) {
/* 102:199 */         for (int i = 0; i < n; i++) {
/* 103:200 */           r[i] = (-r[i]);
/* 104:    */         }
/* 105:    */       }
/* 106:205 */       double deltaOld = delta;
/* 107:206 */       double[] newSteepestDescent = this.preconditioner.precondition(this.point, r);
/* 108:207 */       delta = 0.0D;
/* 109:208 */       for (int i = 0; i < n; i++) {
/* 110:209 */         delta += r[i] * newSteepestDescent[i];
/* 111:    */       }
/* 112:    */       double beta;
/* 113:    */       
/* 114:213 */       if (this.updateFormula == ConjugateGradientFormula.FLETCHER_REEVES)
/* 115:    */       {
/* 116:214 */         beta = delta / deltaOld;
/* 117:    */       }
/* 118:    */       else
/* 119:    */       {
/* 120:216 */         double deltaMid = 0.0D;
/* 121:217 */         for (int i = 0; i < r.length; i++) {
/* 122:218 */           deltaMid += r[i] * steepestDescent[i];
/* 123:    */         }
/* 124:220 */         beta = (delta - deltaMid) / deltaOld;
/* 125:    */       }
/* 126:222 */       steepestDescent = newSteepestDescent;
/* 127:225 */       if ((iter % n == 0) || (beta < 0.0D)) {
/* 128:228 */         searchDirection = (double[])steepestDescent.clone();
/* 129:    */       } else {
/* 130:231 */         for (int i = 0; i < n; i++) {
/* 131:232 */           steepestDescent[i] += beta * searchDirection[i];
/* 132:    */         }
/* 133:    */       }
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   private double findUpperBound(UnivariateFunction f, double a, double h)
/* 138:    */   {
/* 139:249 */     double yA = f.value(a);
/* 140:250 */     double yB = yA;
/* 141:251 */     for (double step = h; step < 1.7976931348623157E+308D; step *= FastMath.max(2.0D, yA / yB))
/* 142:    */     {
/* 143:252 */       double b = a + step;
/* 144:253 */       yB = f.value(b);
/* 145:254 */       if (yA * yB <= 0.0D) {
/* 146:255 */         return b;
/* 147:    */       }
/* 148:    */     }
/* 149:258 */     throw new MathIllegalStateException(LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH, new Object[0]);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static class IdentityPreconditioner
/* 153:    */     implements Preconditioner
/* 154:    */   {
/* 155:    */     public double[] precondition(double[] variables, double[] r)
/* 156:    */     {
/* 157:266 */       return (double[])r.clone();
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   private class LineSearchFunction
/* 162:    */     implements UnivariateFunction
/* 163:    */   {
/* 164:    */     private final double[] searchDirection;
/* 165:    */     
/* 166:    */     public LineSearchFunction(double[] searchDirection)
/* 167:    */     {
/* 168:287 */       this.searchDirection = searchDirection;
/* 169:    */     }
/* 170:    */     
/* 171:    */     public double value(double x)
/* 172:    */     {
/* 173:293 */       double[] shiftedPoint = (double[])NonLinearConjugateGradientOptimizer.this.point.clone();
/* 174:294 */       for (int i = 0; i < shiftedPoint.length; i++) {
/* 175:295 */         shiftedPoint[i] += x * this.searchDirection[i];
/* 176:    */       }
/* 177:299 */       double[] gradient = NonLinearConjugateGradientOptimizer.this.computeObjectiveGradient(shiftedPoint);
/* 178:    */       
/* 179:    */ 
/* 180:302 */       double dotProduct = 0.0D;
/* 181:303 */       for (int i = 0; i < gradient.length; i++) {
/* 182:304 */         dotProduct += gradient[i] * this.searchDirection[i];
/* 183:    */       }
/* 184:307 */       return dotProduct;
/* 185:    */     }
/* 186:    */   }
/* 187:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer
 * JD-Core Version:    0.7.0.1
 */
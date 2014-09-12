/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   5:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   8:    */ import org.apache.commons.math3.optimization.GoalType;
/*   9:    */ import org.apache.commons.math3.optimization.MultivariateOptimizer;
/*  10:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*  11:    */ import org.apache.commons.math3.optimization.univariate.BracketFinder;
/*  12:    */ import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
/*  13:    */ import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
/*  14:    */ import org.apache.commons.math3.util.FastMath;
/*  15:    */ import org.apache.commons.math3.util.MathArrays;
/*  16:    */ 
/*  17:    */ public class PowellOptimizer
/*  18:    */   extends BaseAbstractMultivariateOptimizer<MultivariateFunction>
/*  19:    */   implements MultivariateOptimizer
/*  20:    */ {
/*  21: 54 */   private static final double MIN_RELATIVE_TOLERANCE = 2.0D * FastMath.ulp(1.0D);
/*  22:    */   private final double relativeThreshold;
/*  23:    */   private final double absoluteThreshold;
/*  24:    */   private final LineSearch line;
/*  25:    */   
/*  26:    */   public PowellOptimizer(double rel, double abs, ConvergenceChecker<PointValuePair> checker)
/*  27:    */   {
/*  28: 82 */     super(checker);
/*  29: 84 */     if (rel < MIN_RELATIVE_TOLERANCE) {
/*  30: 85 */       throw new NumberIsTooSmallException(Double.valueOf(rel), Double.valueOf(MIN_RELATIVE_TOLERANCE), true);
/*  31:    */     }
/*  32: 87 */     if (abs <= 0.0D) {
/*  33: 88 */       throw new NotStrictlyPositiveException(Double.valueOf(abs));
/*  34:    */     }
/*  35: 90 */     this.relativeThreshold = rel;
/*  36: 91 */     this.absoluteThreshold = abs;
/*  37:    */     
/*  38:    */ 
/*  39:    */ 
/*  40: 95 */     double minTol = 0.0001D;
/*  41: 96 */     double lsRel = Math.min(FastMath.sqrt(this.relativeThreshold), 0.0001D);
/*  42: 97 */     double lsAbs = Math.min(FastMath.sqrt(this.absoluteThreshold), 0.0001D);
/*  43: 98 */     this.line = new LineSearch(lsRel, lsAbs);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public PowellOptimizer(double rel, double abs)
/*  47:    */   {
/*  48:112 */     this(rel, abs, null);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected PointValuePair doOptimize()
/*  52:    */   {
/*  53:118 */     GoalType goal = getGoalType();
/*  54:119 */     double[] guess = getStartPoint();
/*  55:120 */     int n = guess.length;
/*  56:    */     
/*  57:122 */     double[][] direc = new double[n][n];
/*  58:123 */     for (int i = 0; i < n; i++) {
/*  59:124 */       direc[i][i] = 1.0D;
/*  60:    */     }
/*  61:127 */     ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
/*  62:    */     
/*  63:    */ 
/*  64:130 */     double[] x = guess;
/*  65:131 */     double fVal = computeObjectiveValue(x);
/*  66:132 */     double[] x1 = (double[])x.clone();
/*  67:133 */     int iter = 0;
/*  68:    */     for (;;)
/*  69:    */     {
/*  70:135 */       iter++;
/*  71:    */       
/*  72:137 */       double fX = fVal;
/*  73:138 */       double fX2 = 0.0D;
/*  74:139 */       double delta = 0.0D;
/*  75:140 */       int bigInd = 0;
/*  76:141 */       double alphaMin = 0.0D;
/*  77:143 */       for (int i = 0; i < n; i++)
/*  78:    */       {
/*  79:144 */         double[] d = MathArrays.copyOf(direc[i]);
/*  80:    */         
/*  81:146 */         fX2 = fVal;
/*  82:    */         
/*  83:148 */         UnivariatePointValuePair optimum = this.line.search(x, d);
/*  84:149 */         fVal = optimum.getValue();
/*  85:150 */         alphaMin = optimum.getPoint();
/*  86:151 */         double[][] result = newPointAndDirection(x, d, alphaMin);
/*  87:152 */         x = result[0];
/*  88:154 */         if (fX2 - fVal > delta)
/*  89:    */         {
/*  90:155 */           delta = fX2 - fVal;
/*  91:156 */           bigInd = i;
/*  92:    */         }
/*  93:    */       }
/*  94:161 */       boolean stop = 2.0D * (fX - fVal) <= this.relativeThreshold * (FastMath.abs(fX) + FastMath.abs(fVal)) + this.absoluteThreshold;
/*  95:    */       
/*  96:    */ 
/*  97:    */ 
/*  98:165 */       PointValuePair previous = new PointValuePair(x1, fX);
/*  99:166 */       PointValuePair current = new PointValuePair(x, fVal);
/* 100:167 */       if ((!stop) && 
/* 101:168 */         (checker != null)) {
/* 102:169 */         stop = checker.converged(iter, previous, current);
/* 103:    */       }
/* 104:172 */       if (stop)
/* 105:    */       {
/* 106:173 */         if (goal == GoalType.MINIMIZE) {
/* 107:174 */           return fVal < fX ? current : previous;
/* 108:    */         }
/* 109:176 */         return fVal > fX ? current : previous;
/* 110:    */       }
/* 111:180 */       double[] d = new double[n];
/* 112:181 */       double[] x2 = new double[n];
/* 113:182 */       for (int i = 0; i < n; i++)
/* 114:    */       {
/* 115:183 */         x[i] -= x1[i];
/* 116:184 */         x2[i] = (2.0D * x[i] - x1[i]);
/* 117:    */       }
/* 118:187 */       x1 = (double[])x.clone();
/* 119:188 */       fX2 = computeObjectiveValue(x2);
/* 120:190 */       if (fX > fX2)
/* 121:    */       {
/* 122:191 */         double t = 2.0D * (fX + fX2 - 2.0D * fVal);
/* 123:192 */         double temp = fX - fVal - delta;
/* 124:193 */         t *= temp * temp;
/* 125:194 */         temp = fX - fX2;
/* 126:195 */         t -= delta * temp * temp;
/* 127:197 */         if (t < 0.0D)
/* 128:    */         {
/* 129:198 */           UnivariatePointValuePair optimum = this.line.search(x, d);
/* 130:199 */           fVal = optimum.getValue();
/* 131:200 */           alphaMin = optimum.getPoint();
/* 132:201 */           double[][] result = newPointAndDirection(x, d, alphaMin);
/* 133:202 */           x = result[0];
/* 134:    */           
/* 135:204 */           int lastInd = n - 1;
/* 136:205 */           direc[bigInd] = direc[lastInd];
/* 137:206 */           direc[lastInd] = result[1];
/* 138:    */         }
/* 139:    */       }
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   private double[][] newPointAndDirection(double[] p, double[] d, double optimum)
/* 144:    */   {
/* 145:226 */     int n = p.length;
/* 146:227 */     double[][] result = new double[2][n];
/* 147:228 */     double[] nP = result[0];
/* 148:229 */     double[] nD = result[1];
/* 149:230 */     for (int i = 0; i < n; i++)
/* 150:    */     {
/* 151:231 */       d[i] *= optimum;
/* 152:232 */       p[i] += nD[i];
/* 153:    */     }
/* 154:234 */     return result;
/* 155:    */   }
/* 156:    */   
/* 157:    */   private class LineSearch
/* 158:    */     extends BrentOptimizer
/* 159:    */   {
/* 160:245 */     private final BracketFinder bracket = new BracketFinder();
/* 161:    */     
/* 162:    */     LineSearch(double rel, double abs)
/* 163:    */     {
/* 164:253 */       super(abs, 0);
/* 165:    */     }
/* 166:    */     
/* 167:    */     public UnivariatePointValuePair search(final double[] p, final double[] d)
/* 168:    */     {
/* 169:266 */       final int n = p.length;
/* 170:267 */       UnivariateFunction f = new UnivariateFunction()
/* 171:    */       {
/* 172:    */         public double value(double alpha)
/* 173:    */         {
/* 174:269 */           double[] x = new double[n];
/* 175:270 */           for (int i = 0; i < n; i++) {
/* 176:271 */             x[i] = (p[i] + alpha * d[i]);
/* 177:    */           }
/* 178:273 */           double obj = PowellOptimizer.this.computeObjectiveValue(x);
/* 179:274 */           return obj;
/* 180:    */         }
/* 181:277 */       };
/* 182:278 */       GoalType goal = PowellOptimizer.this.getGoalType();
/* 183:279 */       this.bracket.search(f, goal, 0.0D, 1.0D);
/* 184:    */       
/* 185:    */ 
/* 186:    */ 
/* 187:283 */       return optimize(2147483647, f, goal, this.bracket.getLo(), this.bracket.getHi(), this.bracket.getMid());
/* 188:    */     }
/* 189:    */   }
/* 190:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.PowellOptimizer
 * JD-Core Version:    0.7.0.1
 */
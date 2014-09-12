/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NoBracketingException;
/*   6:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   7:    */ import org.apache.commons.math3.util.Incrementor;
/*   8:    */ import org.apache.commons.math3.util.MathUtils;
/*   9:    */ 
/*  10:    */ public abstract class BaseAbstractUnivariateSolver<FUNC extends UnivariateFunction>
/*  11:    */   implements BaseUnivariateSolver<FUNC>
/*  12:    */ {
/*  13:    */   private static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-014D;
/*  14:    */   private static final double DEFAULT_FUNCTION_VALUE_ACCURACY = 1.E-015D;
/*  15:    */   private final double functionValueAccuracy;
/*  16:    */   private final double absoluteAccuracy;
/*  17:    */   private final double relativeAccuracy;
/*  18: 49 */   private final Incrementor evaluations = new Incrementor();
/*  19:    */   private double searchMin;
/*  20:    */   private double searchMax;
/*  21:    */   private double searchStart;
/*  22:    */   private FUNC function;
/*  23:    */   
/*  24:    */   protected BaseAbstractUnivariateSolver(double absoluteAccuracy)
/*  25:    */   {
/*  26: 65 */     this(1.0E-014D, absoluteAccuracy, 1.E-015D);
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected BaseAbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy)
/*  30:    */   {
/*  31: 78 */     this(relativeAccuracy, absoluteAccuracy, 1.E-015D);
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected BaseAbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/*  35:    */   {
/*  36: 93 */     this.absoluteAccuracy = absoluteAccuracy;
/*  37: 94 */     this.relativeAccuracy = relativeAccuracy;
/*  38: 95 */     this.functionValueAccuracy = functionValueAccuracy;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getMaxEvaluations()
/*  42:    */   {
/*  43:100 */     return this.evaluations.getMaximalCount();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getEvaluations()
/*  47:    */   {
/*  48:104 */     return this.evaluations.getCount();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getMin()
/*  52:    */   {
/*  53:110 */     return this.searchMin;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public double getMax()
/*  57:    */   {
/*  58:116 */     return this.searchMax;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public double getStartValue()
/*  62:    */   {
/*  63:122 */     return this.searchStart;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double getAbsoluteAccuracy()
/*  67:    */   {
/*  68:128 */     return this.absoluteAccuracy;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public double getRelativeAccuracy()
/*  72:    */   {
/*  73:134 */     return this.relativeAccuracy;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public double getFunctionValueAccuracy()
/*  77:    */   {
/*  78:140 */     return this.functionValueAccuracy;
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected double computeObjectiveValue(double point)
/*  82:    */     throws TooManyEvaluationsException
/*  83:    */   {
/*  84:153 */     incrementEvaluationCount();
/*  85:154 */     return this.function.value(point);
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected void setup(int maxEval, FUNC f, double min, double max, double startValue)
/*  89:    */   {
/*  90:173 */     MathUtils.checkNotNull(f);
/*  91:    */     
/*  92:    */ 
/*  93:176 */     this.searchMin = min;
/*  94:177 */     this.searchMax = max;
/*  95:178 */     this.searchStart = startValue;
/*  96:179 */     this.function = f;
/*  97:180 */     this.evaluations.setMaximalCount(maxEval);
/*  98:181 */     this.evaluations.resetCount();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public double solve(int maxEval, FUNC f, double min, double max, double startValue)
/* 102:    */   {
/* 103:187 */     setup(maxEval, f, min, max, startValue);
/* 104:    */     
/* 105:    */ 
/* 106:190 */     return doSolve();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public double solve(int maxEval, FUNC f, double min, double max)
/* 110:    */   {
/* 111:195 */     return solve(maxEval, f, min, max, min + 0.5D * (max - min));
/* 112:    */   }
/* 113:    */   
/* 114:    */   public double solve(int maxEval, FUNC f, double startValue)
/* 115:    */   {
/* 116:200 */     return solve(maxEval, f, (0.0D / 0.0D), (0.0D / 0.0D), startValue);
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected abstract double doSolve()
/* 120:    */     throws TooManyEvaluationsException, NoBracketingException;
/* 121:    */   
/* 122:    */   protected boolean isBracketing(double lower, double upper)
/* 123:    */   {
/* 124:226 */     return UnivariateSolverUtils.isBracketing(this.function, lower, upper);
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected boolean isSequence(double start, double mid, double end)
/* 128:    */   {
/* 129:240 */     return UnivariateSolverUtils.isSequence(start, mid, end);
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected void verifyInterval(double lower, double upper)
/* 133:    */   {
/* 134:253 */     UnivariateSolverUtils.verifyInterval(lower, upper);
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected void verifySequence(double lower, double initial, double upper)
/* 138:    */   {
/* 139:268 */     UnivariateSolverUtils.verifySequence(lower, initial, upper);
/* 140:    */   }
/* 141:    */   
/* 142:    */   protected void verifyBracketing(double lower, double upper)
/* 143:    */   {
/* 144:282 */     UnivariateSolverUtils.verifyBracketing(this.function, lower, upper);
/* 145:    */   }
/* 146:    */   
/* 147:    */   protected void incrementEvaluationCount()
/* 148:    */   {
/* 149:    */     try
/* 150:    */     {
/* 151:294 */       this.evaluations.incrementCount();
/* 152:    */     }
/* 153:    */     catch (MaxCountExceededException e)
/* 154:    */     {
/* 155:296 */       throw new TooManyEvaluationsException(e.getMax());
/* 156:    */     }
/* 157:    */   }
/* 158:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
 * JD-Core Version:    0.7.0.1
 */
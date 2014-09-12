/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   5:    */ import org.apache.commons.math3.exception.MathInternalError;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ 
/*   8:    */ public abstract class BaseSecantSolver
/*   9:    */   extends AbstractUnivariateSolver
/*  10:    */   implements BracketedUnivariateSolver<UnivariateFunction>
/*  11:    */ {
/*  12:    */   protected static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*  13:    */   private AllowedSolution allowed;
/*  14:    */   private final Method method;
/*  15:    */   
/*  16:    */   protected BaseSecantSolver(double absoluteAccuracy, Method method)
/*  17:    */   {
/*  18: 69 */     super(absoluteAccuracy);
/*  19: 70 */     this.allowed = AllowedSolution.ANY_SIDE;
/*  20: 71 */     this.method = method;
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected BaseSecantSolver(double relativeAccuracy, double absoluteAccuracy, Method method)
/*  24:    */   {
/*  25: 84 */     super(relativeAccuracy, absoluteAccuracy);
/*  26: 85 */     this.allowed = AllowedSolution.ANY_SIDE;
/*  27: 86 */     this.method = method;
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected BaseSecantSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, Method method)
/*  31:    */   {
/*  32:101 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
/*  33:102 */     this.allowed = AllowedSolution.ANY_SIDE;
/*  34:103 */     this.method = method;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double solve(int maxEval, UnivariateFunction f, double min, double max, AllowedSolution allowedSolution)
/*  38:    */   {
/*  39:110 */     return solve(maxEval, f, min, max, min + 0.5D * (max - min), allowedSolution);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue, AllowedSolution allowedSolution)
/*  43:    */   {
/*  44:117 */     this.allowed = allowedSolution;
/*  45:118 */     return super.solve(maxEval, f, min, max, startValue);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue)
/*  49:    */   {
/*  50:125 */     return solve(maxEval, f, min, max, startValue, AllowedSolution.ANY_SIDE);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected final double doSolve()
/*  54:    */   {
/*  55:132 */     double x0 = getMin();
/*  56:133 */     double x1 = getMax();
/*  57:134 */     double f0 = computeObjectiveValue(x0);
/*  58:135 */     double f1 = computeObjectiveValue(x1);
/*  59:140 */     if (f0 == 0.0D) {
/*  60:141 */       return x0;
/*  61:    */     }
/*  62:143 */     if (f1 == 0.0D) {
/*  63:144 */       return x1;
/*  64:    */     }
/*  65:148 */     verifyBracketing(x0, x1);
/*  66:    */     
/*  67:    */ 
/*  68:151 */     double ftol = getFunctionValueAccuracy();
/*  69:152 */     double atol = getAbsoluteAccuracy();
/*  70:153 */     double rtol = getRelativeAccuracy();
/*  71:    */     
/*  72:    */ 
/*  73:    */ 
/*  74:157 */     boolean inverted = false;
/*  75:    */     for (;;)
/*  76:    */     {
/*  77:162 */       double x = x1 - f1 * (x1 - x0) / (f1 - f0);
/*  78:163 */       double fx = computeObjectiveValue(x);
/*  79:168 */       if (fx == 0.0D) {
/*  80:169 */         return x;
/*  81:    */       }
/*  82:173 */       if (f1 * fx < 0.0D)
/*  83:    */       {
/*  84:176 */         x0 = x1;
/*  85:177 */         f0 = f1;
/*  86:178 */         inverted = !inverted;
/*  87:    */       }
/*  88:    */       else
/*  89:    */       {
/*  90:180 */         switch (this.method.ordinal())
/*  91:    */         {
/*  92:    */         case 1: 
/*  93:182 */           f0 *= 0.5D;
/*  94:183 */           break;
/*  95:    */         case 2: 
/*  96:185 */           f0 *= f1 / (f1 + fx);
/*  97:186 */           break;
/*  98:    */         case 3: 
/*  99:190 */           if (x == x1) {
/* 100:191 */             throw new ConvergenceException();
/* 101:    */           }
/* 102:    */           break;
/* 103:    */         default: 
/* 104:196 */           throw new MathInternalError();
/* 105:    */         }
/* 106:    */       }
/* 107:200 */       x1 = x;
/* 108:201 */       f1 = fx;
/* 109:206 */       if (FastMath.abs(f1) <= ftol) {
/* 110:207 */         switch (this.allowed.ordinal())
/* 111:    */         {
/* 112:    */         case 1: 
/* 113:209 */           return x1;
/* 114:    */         case 2: 
/* 115:211 */           if (inverted) {
/* 116:212 */             return x1;
/* 117:    */           }
/* 118:    */           break;
/* 119:    */         case 3: 
/* 120:216 */           if (!inverted) {
/* 121:217 */             return x1;
/* 122:    */           }
/* 123:    */           break;
/* 124:    */         case 4: 
/* 125:221 */           if (f1 <= 0.0D) {
/* 126:222 */             return x1;
/* 127:    */           }
/* 128:    */           break;
/* 129:    */         case 5: 
/* 130:226 */           if (f1 >= 0.0D) {
/* 131:227 */             return x1;
/* 132:    */           }
/* 133:    */           break;
/* 134:    */         default: 
/* 135:231 */           throw new MathInternalError();
/* 136:    */         }
/* 137:    */       }
/* 138:237 */       if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol))
/* 139:    */       {
/* 140:239 */         switch (this.allowed.ordinal())
/* 141:    */         {
/* 142:    */         case 1: 
/* 143:241 */           return x1;
/* 144:    */         case 2: 
/* 145:243 */           return inverted ? x1 : x0;
/* 146:    */         case 3: 
/* 147:245 */           return inverted ? x0 : x1;
/* 148:    */         case 4: 
/* 149:247 */           return f1 <= 0.0D ? x1 : x0;
/* 150:    */         case 5: 
/* 151:249 */           return f1 >= 0.0D ? x1 : x0;
/* 152:    */         }
/* 153:251 */         throw new MathInternalError();
/* 154:    */       }
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected static enum Method
/* 159:    */   {
/* 160:264 */     REGULA_FALSI,  ILLINOIS,  PEGASUS;
/* 161:    */     
/* 162:    */     private Method() {}
/* 163:    */   }
/* 164:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.BaseSecantSolver
 * JD-Core Version:    0.7.0.1
 */
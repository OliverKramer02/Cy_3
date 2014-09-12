/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.NoBracketingException;
/*   5:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class UnivariateSolverUtils
/*  12:    */ {
/*  13:    */   public static double solve(UnivariateFunction function, double x0, double x1)
/*  14:    */   {
/*  15: 50 */     if (function == null) {
/*  16: 51 */       throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
/*  17:    */     }
/*  18: 53 */     UnivariateSolver solver = new BrentSolver();
/*  19: 54 */     return solver.solve(2147483647, function, x0, x1);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static double solve(UnivariateFunction function, double x0, double x1, double absoluteAccuracy)
/*  23:    */   {
/*  24: 73 */     if (function == null) {
/*  25: 74 */       throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
/*  26:    */     }
/*  27: 76 */     UnivariateSolver solver = new BrentSolver(absoluteAccuracy);
/*  28: 77 */     return solver.solve(2147483647, function, x0, x1);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static double forceSide(int maxEval, UnivariateFunction f, BracketedUnivariateSolver<UnivariateFunction> bracketing, double baseRoot, double min, double max, AllowedSolution allowedSolution)
/*  32:    */   {
/*  33: 99 */     if (allowedSolution == AllowedSolution.ANY_SIDE) {
/*  34:101 */       return baseRoot;
/*  35:    */     }
/*  36:105 */     double step = FastMath.max(bracketing.getAbsoluteAccuracy(), FastMath.abs(baseRoot * bracketing.getRelativeAccuracy()));
/*  37:    */     
/*  38:107 */     double xLo = FastMath.max(min, baseRoot - step);
/*  39:108 */     double fLo = f.value(xLo);
/*  40:109 */     double xHi = FastMath.min(max, baseRoot + step);
/*  41:110 */     double fHi = f.value(xHi);
/*  42:111 */     int remainingEval = maxEval - 2;
/*  43:112 */     while (remainingEval > 0)
/*  44:    */     {
/*  45:114 */       if (((fLo >= 0.0D) && (fHi <= 0.0D)) || ((fLo <= 0.0D) && (fHi >= 0.0D))) {
/*  46:116 */         return bracketing.solve(remainingEval, f, xLo, xHi, baseRoot, allowedSolution);
/*  47:    */       }
/*  48:120 */       boolean changeLo = false;
/*  49:121 */       boolean changeHi = false;
/*  50:122 */       if (fLo < fHi)
/*  51:    */       {
/*  52:124 */         if (fLo >= 0.0D) {
/*  53:125 */           changeLo = true;
/*  54:    */         } else {
/*  55:127 */           changeHi = true;
/*  56:    */         }
/*  57:    */       }
/*  58:129 */       else if (fLo > fHi)
/*  59:    */       {
/*  60:131 */         if (fLo <= 0.0D) {
/*  61:132 */           changeLo = true;
/*  62:    */         } else {
/*  63:134 */           changeHi = true;
/*  64:    */         }
/*  65:    */       }
/*  66:    */       else
/*  67:    */       {
/*  68:138 */         changeLo = true;
/*  69:139 */         changeHi = true;
/*  70:    */       }
/*  71:143 */       if (changeLo)
/*  72:    */       {
/*  73:144 */         xLo = FastMath.max(min, xLo - step);
/*  74:145 */         fLo = f.value(xLo);
/*  75:146 */         remainingEval--;
/*  76:    */       }
/*  77:150 */       if (changeHi)
/*  78:    */       {
/*  79:151 */         xHi = FastMath.min(max, xHi + step);
/*  80:152 */         fHi = f.value(xHi);
/*  81:153 */         remainingEval--;
/*  82:    */       }
/*  83:    */     }
/*  84:158 */     throw new NoBracketingException(LocalizedFormats.FAILED_BRACKETING, xLo, xHi, fLo, fHi, new Object[] { Integer.valueOf(maxEval - remainingEval), Integer.valueOf(maxEval), Double.valueOf(baseRoot), Double.valueOf(min), Double.valueOf(max) });
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static double[] bracket(UnivariateFunction function, double initial, double lowerBound, double upperBound)
/*  88:    */   {
/*  89:207 */     return bracket(function, initial, lowerBound, upperBound, 2147483647);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static double[] bracket(UnivariateFunction function, double initial, double lowerBound, double upperBound, int maximumIterations)
/*  93:    */   {
/*  94:246 */     if (function == null) {
/*  95:247 */       throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
/*  96:    */     }
/*  97:249 */     if (maximumIterations <= 0) {
/*  98:250 */       throw new NotStrictlyPositiveException(LocalizedFormats.INVALID_MAX_ITERATIONS, Integer.valueOf(maximumIterations));
/*  99:    */     }
/* 100:252 */     verifySequence(lowerBound, initial, upperBound);
/* 101:    */     
/* 102:254 */     double a = initial;
/* 103:255 */     double b = initial;
/* 104:    */     
/* 105:    */ 
/* 106:258 */     int numIterations = 0;
/* 107:    */     double fa;
/* 108:    */     double fb;
/* 109:    */     do
/* 110:    */     {
/* 111:261 */       a = FastMath.max(a - 1.0D, lowerBound);
/* 112:262 */       b = FastMath.min(b + 1.0D, upperBound);
/* 113:263 */       fa = function.value(a);
/* 114:    */       
/* 115:265 */       fb = function.value(b);
/* 116:266 */       numIterations++;
/* 117:267 */     } while ((fa * fb > 0.0D) && (numIterations < maximumIterations) && ((a > lowerBound) || (b < upperBound)));
/* 118:270 */     if (fa * fb > 0.0D) {
/* 119:271 */       throw new NoBracketingException(LocalizedFormats.FAILED_BRACKETING, a, b, fa, fb, new Object[] { Integer.valueOf(numIterations), Integer.valueOf(maximumIterations), Double.valueOf(initial), Double.valueOf(lowerBound), Double.valueOf(upperBound) });
/* 120:    */     }
/* 121:277 */     return new double[] { a, b };
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static double midpoint(double a, double b)
/* 125:    */   {
/* 126:288 */     return (a + b) * 0.5D;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static boolean isBracketing(UnivariateFunction function, double lower, double upper)
/* 130:    */   {
/* 131:305 */     if (function == null) {
/* 132:306 */       throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
/* 133:    */     }
/* 134:308 */     double fLo = function.value(lower);
/* 135:309 */     double fHi = function.value(upper);
/* 136:310 */     return ((fLo >= 0.0D) && (fHi <= 0.0D)) || ((fLo <= 0.0D) && (fHi >= 0.0D));
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static boolean isSequence(double start, double mid, double end)
/* 140:    */   {
/* 141:324 */     return (start < mid) && (mid < end);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public static void verifyInterval(double lower, double upper)
/* 145:    */   {
/* 146:336 */     if (lower >= upper) {
/* 147:337 */       throw new NumberIsTooLargeException(LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, Double.valueOf(lower), Double.valueOf(upper), false);
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static void verifySequence(double lower, double initial, double upper)
/* 152:    */   {
/* 153:354 */     verifyInterval(lower, initial);
/* 154:355 */     verifyInterval(initial, upper);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public static void verifyBracketing(UnivariateFunction function, double lower, double upper)
/* 158:    */   {
/* 159:371 */     if (function == null) {
/* 160:372 */       throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
/* 161:    */     }
/* 162:374 */     verifyInterval(lower, upper);
/* 163:375 */     if (!isBracketing(function, lower, upper)) {
/* 164:376 */       throw new NoBracketingException(lower, upper, function.value(lower), function.value(upper));
/* 165:    */     }
/* 166:    */   }
/* 167:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils
 * JD-Core Version:    0.7.0.1
 */
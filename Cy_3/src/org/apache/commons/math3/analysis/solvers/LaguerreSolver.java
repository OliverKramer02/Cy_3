/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.complex.Complex;
/*   4:    */ import org.apache.commons.math3.exception.NoBracketingException;
/*   5:    */ import org.apache.commons.math3.exception.NoDataException;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class LaguerreSolver
/*  11:    */   extends AbstractPolynomialSolver
/*  12:    */ {
/*  13:    */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*  14: 45 */   private final ComplexSolver complexSolver = new ComplexSolver();
/*  15:    */   
/*  16:    */   public LaguerreSolver()
/*  17:    */   {
/*  18: 51 */     this(1.0E-006D);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public LaguerreSolver(double absoluteAccuracy)
/*  22:    */   {
/*  23: 59 */     super(absoluteAccuracy);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public LaguerreSolver(double relativeAccuracy, double absoluteAccuracy)
/*  27:    */   {
/*  28: 69 */     super(relativeAccuracy, absoluteAccuracy);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public LaguerreSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/*  32:    */   {
/*  33: 81 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public double doSolve()
/*  37:    */   {
/*  38: 89 */     double min = getMin();
/*  39: 90 */     double max = getMax();
/*  40: 91 */     double initial = getStartValue();
/*  41: 92 */     double functionValueAccuracy = getFunctionValueAccuracy();
/*  42:    */     
/*  43: 94 */     verifySequence(min, initial, max);
/*  44:    */     
/*  45:    */ 
/*  46: 97 */     double yInitial = computeObjectiveValue(initial);
/*  47: 98 */     if (FastMath.abs(yInitial) <= functionValueAccuracy) {
/*  48: 99 */       return initial;
/*  49:    */     }
/*  50:103 */     double yMin = computeObjectiveValue(min);
/*  51:104 */     if (FastMath.abs(yMin) <= functionValueAccuracy) {
/*  52:105 */       return min;
/*  53:    */     }
/*  54:109 */     if (yInitial * yMin < 0.0D) {
/*  55:110 */       return laguerre(min, initial, yMin, yInitial);
/*  56:    */     }
/*  57:114 */     double yMax = computeObjectiveValue(max);
/*  58:115 */     if (FastMath.abs(yMax) <= functionValueAccuracy) {
/*  59:116 */       return max;
/*  60:    */     }
/*  61:120 */     if (yInitial * yMax < 0.0D) {
/*  62:121 */       return laguerre(initial, max, yInitial, yMax);
/*  63:    */     }
/*  64:124 */     throw new NoBracketingException(min, max, yMin, yMax);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public double laguerre(double lo, double hi, double fLo, double fHi)
/*  68:    */   {
/*  69:147 */     double[] coefficients = getCoefficients();
/*  70:148 */     Complex[] c = new Complex[coefficients.length];
/*  71:149 */     for (int i = 0; i < coefficients.length; i++) {
/*  72:150 */       c[i] = new Complex(coefficients[i], 0.0D);
/*  73:    */     }
/*  74:152 */     Complex initial = new Complex(0.5D * (lo + hi), 0.0D);
/*  75:153 */     Complex z = this.complexSolver.solve(c, initial);
/*  76:154 */     if (this.complexSolver.isRoot(lo, hi, z)) {
/*  77:155 */       return z.getReal();
/*  78:    */     }
/*  79:157 */     double r = (0.0D / 0.0D);
/*  80:    */     
/*  81:159 */     Complex[] root = this.complexSolver.solveAll(c, initial);
/*  82:160 */     for (int i = 0; i < root.length; i++) {
/*  83:161 */       if (this.complexSolver.isRoot(lo, hi, root[i]))
/*  84:    */       {
/*  85:162 */         r = root[i].getReal();
/*  86:163 */         break;
/*  87:    */       }
/*  88:    */     }
/*  89:166 */     return r;
/*  90:    */   }
/*  91:    */   
/*  92:    */   private class ComplexSolver
/*  93:    */   {
/*  94:    */     private ComplexSolver() {}
/*  95:    */     
/*  96:    */     public boolean isRoot(double min, double max, Complex z)
/*  97:    */     {
/*  98:184 */       if (LaguerreSolver.this.isSequence(min, z.getReal(), max))
/*  99:    */       {
/* 100:185 */         double tolerance = FastMath.max(LaguerreSolver.this.getRelativeAccuracy() * z.abs(), LaguerreSolver.this.getAbsoluteAccuracy());
/* 101:186 */         return (FastMath.abs(z.getImaginary()) <= tolerance) || (z.abs() <= LaguerreSolver.this.getFunctionValueAccuracy());
/* 102:    */       }
/* 103:189 */       return false;
/* 104:    */     }
/* 105:    */     
/* 106:    */     public Complex[] solveAll(Complex[] coefficients, Complex initial)
/* 107:    */     {
/* 108:206 */       if (coefficients == null) {
/* 109:207 */         throw new NullArgumentException();
/* 110:    */       }
/* 111:209 */       int n = coefficients.length - 1;
/* 112:210 */       if (n == 0) {
/* 113:211 */         throw new NoDataException(LocalizedFormats.POLYNOMIAL);
/* 114:    */       }
/* 115:214 */       Complex[] c = new Complex[n + 1];
/* 116:215 */       for (int i = 0; i <= n; i++) {
/* 117:216 */         c[i] = coefficients[i];
/* 118:    */       }
/* 119:220 */       Complex[] root = new Complex[n];
/* 120:221 */       for (int i = 0; i < n; i++)
/* 121:    */       {
/* 122:222 */         Complex[] subarray = new Complex[n - i + 1];
/* 123:223 */         System.arraycopy(c, 0, subarray, 0, subarray.length);
/* 124:224 */         root[i] = solve(subarray, initial);
/* 125:    */         
/* 126:226 */         Complex newc = c[(n - i)];
/* 127:227 */         Complex oldc = null;
/* 128:228 */         for (int j = n - i - 1; j >= 0; j--)
/* 129:    */         {
/* 130:229 */           oldc = c[j];
/* 131:230 */           c[j] = newc;
/* 132:231 */           newc = oldc.add(newc.multiply(root[i]));
/* 133:    */         }
/* 134:    */       }
/* 135:235 */       return root;
/* 136:    */     }
/* 137:    */     
/* 138:    */     public Complex solve(Complex[] coefficients, Complex initial)
/* 139:    */     {
/* 140:252 */       if (coefficients == null) {
/* 141:253 */         throw new NullArgumentException();
/* 142:    */       }
/* 143:256 */       int n = coefficients.length - 1;
/* 144:257 */       if (n == 0) {
/* 145:258 */         throw new NoDataException(LocalizedFormats.POLYNOMIAL);
/* 146:    */       }
/* 147:261 */       double absoluteAccuracy = LaguerreSolver.this.getAbsoluteAccuracy();
/* 148:262 */       double relativeAccuracy = LaguerreSolver.this.getRelativeAccuracy();
/* 149:263 */       double functionValueAccuracy = LaguerreSolver.this.getFunctionValueAccuracy();
/* 150:    */       
/* 151:265 */       Complex N = new Complex(n, 0.0D);
/* 152:266 */       Complex N1 = new Complex(n - 1, 0.0D);
/* 153:    */       
/* 154:268 */       Complex pv = null;
/* 155:269 */       Complex dv = null;
/* 156:270 */       Complex d2v = null;
/* 157:271 */       Complex G = null;
/* 158:272 */       Complex G2 = null;
/* 159:273 */       Complex H = null;
/* 160:274 */       Complex delta = null;
/* 161:275 */       Complex denominator = null;
/* 162:276 */       Complex z = initial;
/* 163:277 */       Complex oldz = new Complex((1.0D / 0.0D), (1.0D / 0.0D));
/* 164:    */       for (;;)
/* 165:    */       {
/* 166:282 */         pv = coefficients[n];
/* 167:283 */         dv = Complex.ZERO;
/* 168:284 */         d2v = Complex.ZERO;
/* 169:285 */         for (int j = n - 1; j >= 0; j--)
/* 170:    */         {
/* 171:286 */           d2v = dv.add(z.multiply(d2v));
/* 172:287 */           dv = pv.add(z.multiply(dv));
/* 173:288 */           pv = coefficients[j].add(z.multiply(pv));
/* 174:    */         }
/* 175:290 */         d2v = d2v.multiply(new Complex(2.0D, 0.0D));
/* 176:    */         
/* 177:    */ 
/* 178:293 */         double tolerance = FastMath.max(relativeAccuracy * z.abs(), absoluteAccuracy);
/* 179:295 */         if (z.subtract(oldz).abs() <= tolerance) {
/* 180:296 */           return z;
/* 181:    */         }
/* 182:298 */         if (pv.abs() <= functionValueAccuracy) {
/* 183:299 */           return z;
/* 184:    */         }
/* 185:303 */         G = dv.divide(pv);
/* 186:304 */         G2 = G.multiply(G);
/* 187:305 */         H = G2.subtract(d2v.divide(pv));
/* 188:306 */         delta = N1.multiply(N.multiply(H).subtract(G2));
/* 189:    */         
/* 190:308 */         Complex deltaSqrt = delta.sqrt();
/* 191:309 */         Complex dplus = G.add(deltaSqrt);
/* 192:310 */         Complex dminus = G.subtract(deltaSqrt);
/* 193:311 */         denominator = dplus.abs() > dminus.abs() ? dplus : dminus;
/* 194:314 */         if (denominator.equals(new Complex(0.0D, 0.0D)))
/* 195:    */         {
/* 196:315 */           z = z.add(new Complex(absoluteAccuracy, absoluteAccuracy));
/* 197:316 */           oldz = new Complex((1.0D / 0.0D), (1.0D / 0.0D));
/* 198:    */         }
/* 199:    */         else
/* 200:    */         {
/* 201:319 */           oldz = z;
/* 202:320 */           z = z.subtract(N.divide(denominator));
/* 203:    */         }
/* 204:322 */         LaguerreSolver.this.incrementEvaluationCount();
/* 205:    */       }
/* 206:    */     }
/* 207:    */   }
/* 208:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.LaguerreSolver
 * JD-Core Version:    0.7.0.1
 */
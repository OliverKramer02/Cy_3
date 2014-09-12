/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.MathInternalError;
/*   5:    */ import org.apache.commons.math3.exception.NoBracketingException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ import org.apache.commons.math3.util.Precision;
/*   9:    */ 
/*  10:    */ public class BracketingNthOrderBrentSolver
/*  11:    */   extends AbstractUnivariateSolver
/*  12:    */   implements BracketedUnivariateSolver<UnivariateFunction>
/*  13:    */ {
/*  14:    */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*  15:    */   private static final int DEFAULT_MAXIMAL_ORDER = 5;
/*  16:    */   private static final int MAXIMAL_AGING = 2;
/*  17:    */   private static final double REDUCTION_FACTOR = 0.0625D;
/*  18:    */   private final int maximalOrder;
/*  19:    */   private AllowedSolution allowed;
/*  20:    */   
/*  21:    */   public BracketingNthOrderBrentSolver()
/*  22:    */   {
/*  23: 69 */     this(1.0E-006D, 5);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public BracketingNthOrderBrentSolver(double absoluteAccuracy, int maximalOrder)
/*  27:    */     throws NumberIsTooSmallException
/*  28:    */   {
/*  29: 82 */     super(absoluteAccuracy);
/*  30: 83 */     if (maximalOrder < 2) {
/*  31: 84 */       throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), Integer.valueOf(2), true);
/*  32:    */     }
/*  33: 86 */     this.maximalOrder = maximalOrder;
/*  34: 87 */     this.allowed = AllowedSolution.ANY_SIDE;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public BracketingNthOrderBrentSolver(double relativeAccuracy, double absoluteAccuracy, int maximalOrder)
/*  38:    */     throws NumberIsTooSmallException
/*  39:    */   {
/*  40:102 */     super(relativeAccuracy, absoluteAccuracy);
/*  41:103 */     if (maximalOrder < 2) {
/*  42:104 */       throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), Integer.valueOf(2), true);
/*  43:    */     }
/*  44:106 */     this.maximalOrder = maximalOrder;
/*  45:107 */     this.allowed = AllowedSolution.ANY_SIDE;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public BracketingNthOrderBrentSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, int maximalOrder)
/*  49:    */     throws NumberIsTooSmallException
/*  50:    */   {
/*  51:124 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
/*  52:125 */     if (maximalOrder < 2) {
/*  53:126 */       throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), Integer.valueOf(2), true);
/*  54:    */     }
/*  55:128 */     this.maximalOrder = maximalOrder;
/*  56:129 */     this.allowed = AllowedSolution.ANY_SIDE;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int getMaximalOrder()
/*  60:    */   {
/*  61:136 */     return this.maximalOrder;
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected double doSolve()
/*  65:    */   {
/*  66:146 */     double[] x = new double[this.maximalOrder + 1];
/*  67:147 */     double[] y = new double[this.maximalOrder + 1];
/*  68:148 */     x[0] = getMin();
/*  69:149 */     x[1] = getStartValue();
/*  70:150 */     x[2] = getMax();
/*  71:151 */     verifySequence(x[0], x[1], x[2]);
/*  72:    */     
/*  73:    */ 
/*  74:154 */     y[1] = computeObjectiveValue(x[1]);
/*  75:155 */     if (Precision.equals(y[1], 0.0D, 1)) {
/*  76:157 */       return x[1];
/*  77:    */     }
/*  78:161 */     y[0] = computeObjectiveValue(x[0]);
/*  79:162 */     if (Precision.equals(y[0], 0.0D, 1)) {
/*  80:164 */       return x[0];
/*  81:    */     }
/*  82:    */     int signChangeIndex;
/*  83:169 */     if (y[0] * y[1] < 0.0D)
/*  84:    */     {
/*  85:172 */       int nbPoints = 2;
/*  86:173 */       signChangeIndex = 1;
/*  87:    */     }
/*  88:    */     else
/*  89:    */     {
/*  90:178 */       y[2] = computeObjectiveValue(x[2]);
/*  91:179 */       if (Precision.equals(y[2], 0.0D, 1)) {
/*  92:181 */         return x[2];
/*  93:    */       }
/*  94:    */       
/*  95:184 */       if (y[1] * y[2] < 0.0D)
/*  96:    */       {
/*  97:186 */         int nbPoints = 3;
/*  98:187 */         signChangeIndex = 2;
/*  99:    */       }
/* 100:    */       else
/* 101:    */       {
/* 102:189 */         throw new NoBracketingException(x[0], x[2], y[0], y[2]);
/* 103:    */       }
/* 104:    */     }
/* 105:    */    
/* 106:    */     int nbPoints = 0;
/* 107:195 */     double[] tmpX = new double[x.length];
/* 108:    */     
/* 109:    */ 
/* 110:198 */     double xA = x[(signChangeIndex - 1)];
/* 111:199 */     double yA = y[(signChangeIndex - 1)];
/* 112:200 */     double absYA = FastMath.abs(yA);
/* 113:201 */     int agingA = 0;
/* 114:202 */     double xB = x[signChangeIndex];
/* 115:203 */     double yB = y[signChangeIndex];
/* 116:204 */     double absYB = FastMath.abs(yB);
/* 117:205 */     int agingB = 0;
/* 118:    */     for (;;)
/* 119:    */     {
/* 120:211 */       double xTol = getAbsoluteAccuracy() + getRelativeAccuracy() * FastMath.max(FastMath.abs(xA), FastMath.abs(xB));
/* 121:213 */       if ((xB - xA <= xTol) || (FastMath.max(absYA, absYB) < getFunctionValueAccuracy()))
/* 122:    */       {
/* 123:214 */         switch (this.allowed.ordinal())
/* 124:    */         {
/* 125:    */         case 1: 
/* 126:216 */           return absYA < absYB ? xA : xB;
/* 127:    */         case 2: 
/* 128:218 */           return xA;
/* 129:    */         case 3: 
/* 130:220 */           return xB;
/* 131:    */         case 4: 
/* 132:222 */           return yA <= 0.0D ? xA : xB;
/* 133:    */         case 5: 
/* 134:224 */           return yA < 0.0D ? xB : xA;
/* 135:    */         }
/* 136:227 */         throw new MathInternalError(null);
/* 137:    */       }
/* 138:    */       double targetY;
/* 139:    */      
/* 140:233 */       if (agingA >= 2)
/* 141:    */       {
/* 142:235 */         int p = agingA - 2;
/* 143:236 */         double weightA = (1 << p) - 1;
/* 144:237 */         double weightB = p + 1;
/* 145:238 */         targetY = (weightA * yA - weightB * 0.0625D * yB) / (weightA + weightB);
/* 146:    */       }
/* 147:    */       else
/* 148:    */       {
/* 149:    */         
/* 150:239 */         if (agingB >= 2)
/* 151:    */         {
/* 152:241 */           int p = agingB - 2;
/* 153:242 */           double weightA = p + 1;
/* 154:243 */           double weightB = (1 << p) - 1;
/* 155:244 */           targetY = (weightB * yB - weightA * 0.0625D * yA) / (weightA + weightB);
/* 156:    */         }
/* 157:    */         else
/* 158:    */         {
/* 159:247 */           targetY = 0.0D;
/* 160:    */         }
/* 161:    */       }
/* 162:252 */       int start = 0;
/* 163:253 */       int end = nbPoints;
/* 164:    */       double nextX;
/* 165:    */       do
/* 166:    */       {
/* 167:257 */         System.arraycopy(x, start, tmpX, start, end - start);
/* 168:258 */         nextX = guessX(targetY, tmpX, y, start, end);
/* 169:260 */         if ((nextX <= xA) || (nextX >= xB))
/* 170:    */         {
/* 171:266 */           if (signChangeIndex - start >= end - signChangeIndex) {
/* 172:268 */             start++;
/* 173:    */           } else {
/* 174:271 */             end--;
/* 175:    */           }
/* 176:275 */           nextX = (0.0D / 0.0D);
/* 177:    */         }
/* 178:279 */       } while ((Double.isNaN(nextX)) && (end - start > 1));
/* 179:281 */       if (Double.isNaN(nextX))
/* 180:    */       {
/* 181:283 */         nextX = xA + 0.5D * (xB - xA);
/* 182:284 */         start = signChangeIndex - 1;
/* 183:285 */         end = signChangeIndex;
/* 184:    */       }
/* 185:289 */       double nextY = computeObjectiveValue(nextX);
/* 186:290 */       if (Precision.equals(nextY, 0.0D, 1)) {
/* 187:293 */         return nextX;
/* 188:    */       }
/* 189:296 */       if ((nbPoints > 2) && (end - start != nbPoints))
/* 190:    */       {
/* 191:300 */         nbPoints = end - start;
/* 192:301 */         System.arraycopy(x, start, x, 0, nbPoints);
/* 193:302 */         System.arraycopy(y, start, y, 0, nbPoints);
/* 194:303 */         signChangeIndex -= start;
/* 195:    */       }
/* 196:305 */       else if (nbPoints == x.length)
/* 197:    */       {
/* 198:308 */         nbPoints--;
/* 199:311 */         if (signChangeIndex >= (x.length + 1) / 2)
/* 200:    */         {
/* 201:313 */           System.arraycopy(x, 1, x, 0, nbPoints);
/* 202:314 */           System.arraycopy(y, 1, y, 0, nbPoints);
/* 203:315 */           signChangeIndex--;
/* 204:    */         }
/* 205:    */       }
/* 206:322 */       System.arraycopy(x, signChangeIndex, x, signChangeIndex + 1, nbPoints - signChangeIndex);
/* 207:323 */       x[signChangeIndex] = nextX;
/* 208:324 */       System.arraycopy(y, signChangeIndex, y, signChangeIndex + 1, nbPoints - signChangeIndex);
/* 209:325 */       y[signChangeIndex] = nextY;
/* 210:326 */       nbPoints++;
/* 211:329 */       if (nextY * yA <= 0.0D)
/* 212:    */       {
/* 213:331 */         xB = nextX;
/* 214:332 */         yB = nextY;
/* 215:333 */         absYB = FastMath.abs(yB);
/* 216:334 */         agingA++;
/* 217:335 */         agingB = 0;
/* 218:    */       }
/* 219:    */       else
/* 220:    */       {
/* 221:338 */         xA = nextX;
/* 222:339 */         yA = nextY;
/* 223:340 */         absYA = FastMath.abs(yA);
/* 224:341 */         agingA = 0;
/* 225:342 */         agingB++;
/* 226:    */         
/* 227:    */ 
/* 228:345 */         signChangeIndex++;
/* 229:    */       }
/* 230:    */     }
/* 231:    */   }
/* 232:    */   
/* 233:    */   private double guessX(double targetY, double[] x, double[] y, int start, int end)
/* 234:    */   {
/* 235:371 */     for (int i = start; i < end - 1; i++)
/* 236:    */     {
/* 237:372 */       int delta = i + 1 - start;
/* 238:373 */       for (int j = end - 1; j > i; j--) {
/* 239:374 */         x[j] = ((x[j] - x[(j - 1)]) / (y[j] - y[(j - delta)]));
/* 240:    */       }
/* 241:    */     }
/* 242:379 */     double x0 = 0.0D;
/* 243:380 */     for (int j = end - 1; j >= start; j--) {
/* 244:381 */       x0 = x[j] + x0 * (targetY - y[j]);
/* 245:    */     }
/* 246:384 */     return x0;
/* 247:    */   }
/* 248:    */   
/* 249:    */   public double solve(int maxEval, UnivariateFunction f, double min, double max, AllowedSolution allowedSolution)
/* 250:    */   {
/* 251:391 */     this.allowed = allowedSolution;
/* 252:392 */     return super.solve(maxEval, f, min, max);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue, AllowedSolution allowedSolution)
/* 256:    */   {
/* 257:399 */     this.allowed = allowedSolution;
/* 258:400 */     return super.solve(maxEval, f, min, max, startValue);
/* 259:    */   }
/* 260:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver
 * JD-Core Version:    0.7.0.1
 */
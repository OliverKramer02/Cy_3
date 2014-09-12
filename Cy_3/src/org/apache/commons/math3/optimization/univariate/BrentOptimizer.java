/*   1:    */ package org.apache.commons.math3.optimization.univariate;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   5:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*   6:    */ import org.apache.commons.math3.optimization.GoalType;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ import org.apache.commons.math3.util.Precision;
/*   9:    */ 
/*  10:    */ public class BrentOptimizer
/*  11:    */   extends BaseAbstractUnivariateOptimizer
/*  12:    */ {
/*  13: 42 */   private static final double GOLDEN_SECTION = 0.5D * (3.0D - FastMath.sqrt(5.0D));
/*  14: 46 */   private static final double MIN_RELATIVE_TOLERANCE = 2.0D * FastMath.ulp(1.0D);
/*  15:    */   private final double relativeThreshold;
/*  16:    */   private final double absoluteThreshold;
/*  17:    */   
/*  18:    */   public BrentOptimizer(double rel, double abs, ConvergenceChecker<UnivariatePointValuePair> checker)
/*  19:    */   {
/*  20: 75 */     super(checker);
/*  21: 77 */     if (rel < MIN_RELATIVE_TOLERANCE) {
/*  22: 78 */       throw new NumberIsTooSmallException(Double.valueOf(rel), Double.valueOf(MIN_RELATIVE_TOLERANCE), true);
/*  23:    */     }
/*  24: 80 */     if (abs <= 0.0D) {
/*  25: 81 */       throw new NotStrictlyPositiveException(Double.valueOf(abs));
/*  26:    */     }
/*  27: 83 */     this.relativeThreshold = rel;
/*  28: 84 */     this.absoluteThreshold = abs;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public BrentOptimizer(double rel, double abs)
/*  32:    */   {
/*  33:103 */     this(rel, abs, null);
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected UnivariatePointValuePair doOptimize()
/*  37:    */   {
/*  38:109 */     boolean isMinim = getGoalType() == GoalType.MINIMIZE;
/*  39:110 */     double lo = getMin();
/*  40:111 */     double mid = getStartValue();
/*  41:112 */     double hi = getMax();
/*  42:    */     
/*  43:    */ 
/*  44:115 */     ConvergenceChecker<UnivariatePointValuePair> checker = getConvergenceChecker();
/*  45:    */     double b;
/*  46:    */     double a;
/*  47:    */     
/*  48:120 */     if (lo < hi)
/*  49:    */     {
/*  50:121 */       a = lo;
/*  51:122 */       b = hi;
/*  52:    */     }
/*  53:    */     else
/*  54:    */     {
/*  55:124 */       a = hi;
/*  56:125 */       b = lo;
/*  57:    */     }
/*  58:128 */     double x = mid;
/*  59:129 */     double v = x;
/*  60:130 */     double w = x;
/*  61:131 */     double d = 0.0D;
/*  62:132 */     double e = 0.0D;
/*  63:133 */     double fx = computeObjectiveValue(x);
/*  64:134 */     if (!isMinim) {
/*  65:135 */       fx = -fx;
/*  66:    */     }
/*  67:137 */     double fv = fx;
/*  68:138 */     double fw = fx;
/*  69:    */     
/*  70:140 */     UnivariatePointValuePair previous = null;
/*  71:141 */     UnivariatePointValuePair current = new UnivariatePointValuePair(x, isMinim ? fx : -fx);
/*  72:    */     
/*  73:    */ 
/*  74:144 */     int iter = 0;
/*  75:    */     for (;;)
/*  76:    */     {
/*  77:146 */       double m = 0.5D * (a + b);
/*  78:147 */       double tol1 = this.relativeThreshold * FastMath.abs(x) + this.absoluteThreshold;
/*  79:148 */       double tol2 = 2.0D * tol1;
/*  80:    */       
/*  81:    */ 
/*  82:151 */       boolean stop = FastMath.abs(x - m) <= tol2 - 0.5D * (b - a);
/*  83:152 */       if (!stop)
/*  84:    */       {
/*  85:153 */         double p = 0.0D;
/*  86:154 */         double q = 0.0D;
/*  87:155 */         double r = 0.0D;
/*  88:156 */         double u = 0.0D;
/*  89:158 */         if (FastMath.abs(e) > tol1)
/*  90:    */         {
/*  91:159 */           r = (x - w) * (fx - fv);
/*  92:160 */           q = (x - v) * (fx - fw);
/*  93:161 */           p = (x - v) * q - (x - w) * r;
/*  94:162 */           q = 2.0D * (q - r);
/*  95:164 */           if (q > 0.0D) {
/*  96:165 */             p = -p;
/*  97:    */           } else {
/*  98:167 */             q = -q;
/*  99:    */           }
/* 100:170 */           r = e;
/* 101:171 */           e = d;
/* 102:173 */           if ((p > q * (a - x)) && (p < q * (b - x)) && (FastMath.abs(p) < FastMath.abs(0.5D * q * r)))
/* 103:    */           {
/* 104:177 */             d = p / q;
/* 105:178 */             u = x + d;
/* 106:181 */             if ((u - a < tol2) || (b - u < tol2)) {
/* 107:182 */               if (x <= m) {
/* 108:183 */                 d = tol1;
/* 109:    */               } else {
/* 110:185 */                 d = -tol1;
/* 111:    */               }
/* 112:    */             }
/* 113:    */           }
/* 114:    */           else
/* 115:    */           {
/* 116:190 */             if (x < m) {
/* 117:191 */               e = b - x;
/* 118:    */             } else {
/* 119:193 */               e = a - x;
/* 120:    */             }
/* 121:195 */             d = GOLDEN_SECTION * e;
/* 122:    */           }
/* 123:    */         }
/* 124:    */         else
/* 125:    */         {
/* 126:199 */           if (x < m) {
/* 127:200 */             e = b - x;
/* 128:    */           } else {
/* 129:202 */             e = a - x;
/* 130:    */           }
/* 131:204 */           d = GOLDEN_SECTION * e;
/* 132:    */         }
/* 133:208 */         if (FastMath.abs(d) < tol1)
/* 134:    */         {
/* 135:209 */           if (d >= 0.0D) {
/* 136:210 */             u = x + tol1;
/* 137:    */           } else {
/* 138:212 */             u = x - tol1;
/* 139:    */           }
/* 140:    */         }
/* 141:    */         else {
/* 142:215 */           u = x + d;
/* 143:    */         }
/* 144:218 */         double fu = computeObjectiveValue(u);
/* 145:219 */         if (!isMinim) {
/* 146:220 */           fu = -fu;
/* 147:    */         }
/* 148:224 */         if (fu <= fx)
/* 149:    */         {
/* 150:225 */           if (u < x) {
/* 151:226 */             b = x;
/* 152:    */           } else {
/* 153:228 */             a = x;
/* 154:    */           }
/* 155:230 */           v = w;
/* 156:231 */           fv = fw;
/* 157:232 */           w = x;
/* 158:233 */           fw = fx;
/* 159:234 */           x = u;
/* 160:235 */           fx = fu;
/* 161:    */         }
/* 162:    */         else
/* 163:    */         {
/* 164:237 */           if (u < x) {
/* 165:238 */             a = u;
/* 166:    */           } else {
/* 167:240 */             b = u;
/* 168:    */           }
/* 169:242 */           if ((fu <= fw) || (Precision.equals(w, x)))
/* 170:    */           {
/* 171:244 */             v = w;
/* 172:245 */             fv = fw;
/* 173:246 */             w = u;
/* 174:247 */             fw = fu;
/* 175:    */           }
/* 176:248 */           else if ((fu <= fv) || (Precision.equals(v, x)) || (Precision.equals(v, w)))
/* 177:    */           {
/* 178:251 */             v = u;
/* 179:252 */             fv = fu;
/* 180:    */           }
/* 181:    */         }
/* 182:256 */         previous = current;
/* 183:257 */         current = new UnivariatePointValuePair(x, isMinim ? fx : -fx);
/* 184:260 */         if ((checker != null) && 
/* 185:261 */           (checker.converged(iter, previous, current))) {
/* 186:262 */           return current;
/* 187:    */         }
/* 188:    */       }
/* 189:    */       else
/* 190:    */       {
/* 191:266 */         return current;
/* 192:    */       }
/* 193:268 */       iter++;
/* 194:    */     }
/* 195:    */   }
/* 196:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.univariate.BrentOptimizer
 * JD-Core Version:    0.7.0.1
 */
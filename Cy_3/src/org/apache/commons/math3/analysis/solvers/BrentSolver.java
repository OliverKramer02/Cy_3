/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NoBracketingException;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ import org.apache.commons.math3.util.Precision;
/*   6:    */ 
/*   7:    */ public class BrentSolver
/*   8:    */   extends AbstractUnivariateSolver
/*   9:    */ {
/*  10:    */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*  11:    */   
/*  12:    */   public BrentSolver()
/*  13:    */   {
/*  14: 45 */     this(1.0E-006D);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public BrentSolver(double absoluteAccuracy)
/*  18:    */   {
/*  19: 53 */     super(absoluteAccuracy);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public BrentSolver(double relativeAccuracy, double absoluteAccuracy)
/*  23:    */   {
/*  24: 63 */     super(relativeAccuracy, absoluteAccuracy);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public BrentSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy)
/*  28:    */   {
/*  29: 75 */     super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected double doSolve()
/*  33:    */   {
/*  34: 83 */     double min = getMin();
/*  35: 84 */     double max = getMax();
/*  36: 85 */     double initial = getStartValue();
/*  37: 86 */     double functionValueAccuracy = getFunctionValueAccuracy();
/*  38:    */     
/*  39: 88 */     verifySequence(min, initial, max);
/*  40:    */     
/*  41:    */ 
/*  42: 91 */     double yInitial = computeObjectiveValue(initial);
/*  43: 92 */     if (FastMath.abs(yInitial) <= functionValueAccuracy) {
/*  44: 93 */       return initial;
/*  45:    */     }
/*  46: 97 */     double yMin = computeObjectiveValue(min);
/*  47: 98 */     if (FastMath.abs(yMin) <= functionValueAccuracy) {
/*  48: 99 */       return min;
/*  49:    */     }
/*  50:103 */     if (yInitial * yMin < 0.0D) {
/*  51:104 */       return brent(min, initial, yMin, yInitial);
/*  52:    */     }
/*  53:108 */     double yMax = computeObjectiveValue(max);
/*  54:109 */     if (FastMath.abs(yMax) <= functionValueAccuracy) {
/*  55:110 */       return max;
/*  56:    */     }
/*  57:114 */     if (yInitial * yMax < 0.0D) {
/*  58:115 */       return brent(initial, max, yInitial, yMax);
/*  59:    */     }
/*  60:118 */     throw new NoBracketingException(min, max, yMin, yMax);
/*  61:    */   }
/*  62:    */   
/*  63:    */   private double brent(double lo, double hi, double fLo, double fHi)
/*  64:    */   {
/*  65:139 */     double a = lo;
/*  66:140 */     double fa = fLo;
/*  67:141 */     double b = hi;
/*  68:142 */     double fb = fHi;
/*  69:143 */     double c = a;
/*  70:144 */     double fc = fa;
/*  71:145 */     double d = b - a;
/*  72:146 */     double e = d;
/*  73:    */     
/*  74:148 */     double t = getAbsoluteAccuracy();
/*  75:149 */     double eps = getRelativeAccuracy();
/*  76:    */     for (;;)
/*  77:    */     {
/*  78:152 */       if (FastMath.abs(fc) < FastMath.abs(fb))
/*  79:    */       {
/*  80:153 */         a = b;
/*  81:154 */         b = c;
/*  82:155 */         c = a;
/*  83:156 */         fa = fb;
/*  84:157 */         fb = fc;
/*  85:158 */         fc = fa;
/*  86:    */       }
/*  87:161 */       double tol = 2.0D * eps * FastMath.abs(b) + t;
/*  88:162 */       double m = 0.5D * (c - b);
/*  89:164 */       if ((FastMath.abs(m) <= tol) || (Precision.equals(fb, 0.0D))) {
/*  90:166 */         return b;
/*  91:    */       }
/*  92:168 */       if ((FastMath.abs(e) < tol) || (FastMath.abs(fa) <= FastMath.abs(fb)))
/*  93:    */       {
/*  94:171 */         d = m;
/*  95:172 */         e = d;
/*  96:    */       }
/*  97:    */       else
/*  98:    */       {
/*  99:174 */         double s = fb / fa;
/* 100:    */         double q;
/* 101:    */       
/* 102:    */         double p;
/* 103:180 */         if (a == c)
/* 104:    */         {
/* 105:182 */           p = 2.0D * m * s;
/* 106:183 */           q = 1.0D - s;
/* 107:    */         }
/* 108:    */         else
/* 109:    */         {
/* 110:186 */           q = fa / fc;
/* 111:187 */           double r = fb / fc;
/* 112:188 */           p = s * (2.0D * m * q * (q - r) - (b - a) * (r - 1.0D));
/* 113:189 */           q = (q - 1.0D) * (r - 1.0D) * (s - 1.0D);
/* 114:    */         }
/* 115:191 */         if (p > 0.0D) {
/* 116:192 */           q = -q;
/* 117:    */         } else {
/* 118:194 */           p = -p;
/* 119:    */         }
/* 120:196 */         s = e;
/* 121:197 */         e = d;
/* 122:198 */         if ((p >= 1.5D * m * q - FastMath.abs(tol * q)) || (p >= FastMath.abs(0.5D * s * q)))
/* 123:    */         {
/* 124:203 */           d = m;
/* 125:204 */           e = d;
/* 126:    */         }
/* 127:    */         else
/* 128:    */         {
/* 129:206 */           d = p / q;
/* 130:    */         }
/* 131:    */       }
/* 132:209 */       a = b;
/* 133:210 */       fa = fb;
/* 134:212 */       if (FastMath.abs(d) > tol) {
/* 135:213 */         b += d;
/* 136:214 */       } else if (m > 0.0D) {
/* 137:215 */         b += tol;
/* 138:    */       } else {
/* 139:217 */         b -= tol;
/* 140:    */       }
/* 141:219 */       fb = computeObjectiveValue(b);
/* 142:220 */       if (((fb > 0.0D) && (fc > 0.0D)) || ((fb <= 0.0D) && (fc <= 0.0D)))
/* 143:    */       {
/* 144:222 */         c = a;
/* 145:223 */         fc = fa;
/* 146:224 */         d = b - a;
/* 147:225 */         e = d;
/* 148:    */       }
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.BrentSolver
 * JD-Core Version:    0.7.0.1
 */
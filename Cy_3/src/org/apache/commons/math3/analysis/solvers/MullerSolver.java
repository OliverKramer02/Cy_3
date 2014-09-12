/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class MullerSolver
/*   6:    */   extends AbstractUnivariateSolver
/*   7:    */ {
/*   8:    */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*   9:    */   
/*  10:    */   public MullerSolver()
/*  11:    */   {
/*  12: 56 */     this(1.0E-006D);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public MullerSolver(double absoluteAccuracy)
/*  16:    */   {
/*  17: 64 */     super(absoluteAccuracy);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public MullerSolver(double relativeAccuracy, double absoluteAccuracy)
/*  21:    */   {
/*  22: 74 */     super(relativeAccuracy, absoluteAccuracy);
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected double doSolve()
/*  26:    */   {
/*  27: 82 */     double min = getMin();
/*  28: 83 */     double max = getMax();
/*  29: 84 */     double initial = getStartValue();
/*  30:    */     
/*  31: 86 */     double functionValueAccuracy = getFunctionValueAccuracy();
/*  32:    */     
/*  33: 88 */     verifySequence(min, initial, max);
/*  34:    */     
/*  35:    */ 
/*  36: 91 */     double fMin = computeObjectiveValue(min);
/*  37: 92 */     if (FastMath.abs(fMin) < functionValueAccuracy) {
/*  38: 93 */       return min;
/*  39:    */     }
/*  40: 95 */     double fMax = computeObjectiveValue(max);
/*  41: 96 */     if (FastMath.abs(fMax) < functionValueAccuracy) {
/*  42: 97 */       return max;
/*  43:    */     }
/*  44: 99 */     double fInitial = computeObjectiveValue(initial);
/*  45:100 */     if (FastMath.abs(fInitial) < functionValueAccuracy) {
/*  46:101 */       return initial;
/*  47:    */     }
/*  48:104 */     verifyBracketing(min, max);
/*  49:106 */     if (isBracketing(min, initial)) {
/*  50:107 */       return solve(min, initial, fMin, fInitial);
/*  51:    */     }
/*  52:109 */     return solve(initial, max, fInitial, fMax);
/*  53:    */   }
/*  54:    */   
/*  55:    */   private double solve(double min, double max, double fMin, double fMax)
/*  56:    */   {
/*  57:124 */     double relativeAccuracy = getRelativeAccuracy();
/*  58:125 */     double absoluteAccuracy = getAbsoluteAccuracy();
/*  59:126 */     double functionValueAccuracy = getFunctionValueAccuracy();
/*  60:    */     
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:133 */     double x0 = min;
/*  67:134 */     double y0 = fMin;
/*  68:135 */     double x2 = max;
/*  69:136 */     double y2 = fMax;
/*  70:137 */     double x1 = 0.5D * (x0 + x2);
/*  71:138 */     double y1 = computeObjectiveValue(x1);
/*  72:    */     
/*  73:140 */     double oldx = (1.0D / 0.0D);
/*  74:    */     for (;;)
/*  75:    */     {
/*  76:146 */       double d01 = (y1 - y0) / (x1 - x0);
/*  77:147 */       double d12 = (y2 - y1) / (x2 - x1);
/*  78:148 */       double d012 = (d12 - d01) / (x2 - x0);
/*  79:149 */       double c1 = d01 + (x1 - x0) * d012;
/*  80:150 */       double delta = c1 * c1 - 4.0D * y1 * d012;
/*  81:151 */       double xplus = x1 + -2.0D * y1 / (c1 + FastMath.sqrt(delta));
/*  82:152 */       double xminus = x1 + -2.0D * y1 / (c1 - FastMath.sqrt(delta));
/*  83:    */       
/*  84:    */ 
/*  85:155 */       double x = isSequence(x0, xplus, x2) ? xplus : xminus;
/*  86:156 */       double y = computeObjectiveValue(x);
/*  87:    */       
/*  88:    */ 
/*  89:159 */       double tolerance = FastMath.max(relativeAccuracy * FastMath.abs(x), absoluteAccuracy);
/*  90:160 */       if ((FastMath.abs(x - oldx) <= tolerance) || (FastMath.abs(y) <= functionValueAccuracy)) {
/*  91:162 */         return x;
/*  92:    */       }
/*  93:169 */       boolean bisect = ((x < x1) && (x1 - x0 > 0.95D * (x2 - x0))) || ((x > x1) && (x2 - x1 > 0.95D * (x2 - x0))) || (x == x1);
/*  94:173 */       if (!bisect)
/*  95:    */       {
/*  96:174 */         x0 = x < x1 ? x0 : x1;
/*  97:175 */         y0 = x < x1 ? y0 : y1;
/*  98:176 */         x2 = x > x1 ? x2 : x1;
/*  99:177 */         y2 = x > x1 ? y2 : y1;
/* 100:178 */         x1 = x;y1 = y;
/* 101:179 */         oldx = x;
/* 102:    */       }
/* 103:    */       else
/* 104:    */       {
/* 105:181 */         double xm = 0.5D * (x0 + x2);
/* 106:182 */         double ym = computeObjectiveValue(xm);
/* 107:183 */         if (FastMath.signum(y0) + FastMath.signum(ym) == 0.0D)
/* 108:    */         {
/* 109:184 */           x2 = xm;y2 = ym;
/* 110:    */         }
/* 111:    */         else
/* 112:    */         {
/* 113:186 */           x0 = xm;y0 = ym;
/* 114:    */         }
/* 115:188 */         x1 = 0.5D * (x0 + x2);
/* 116:189 */         y1 = computeObjectiveValue(x1);
/* 117:190 */         oldx = (1.0D / 0.0D);
/* 118:    */       }
/* 119:    */     }
/* 120:    */   }
/* 121:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.MullerSolver
 * JD-Core Version:    0.7.0.1
 */
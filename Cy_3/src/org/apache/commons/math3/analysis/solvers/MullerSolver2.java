/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NoBracketingException;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public class MullerSolver2
/*   7:    */   extends AbstractUnivariateSolver
/*   8:    */ {
/*   9:    */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*  10:    */   
/*  11:    */   public MullerSolver2()
/*  12:    */   {
/*  13: 57 */     this(1.0E-006D);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public MullerSolver2(double absoluteAccuracy)
/*  17:    */   {
/*  18: 65 */     super(absoluteAccuracy);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public MullerSolver2(double relativeAccuracy, double absoluteAccuracy)
/*  22:    */   {
/*  23: 75 */     super(relativeAccuracy, absoluteAccuracy);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected double doSolve()
/*  27:    */   {
/*  28: 83 */     double min = getMin();
/*  29: 84 */     double max = getMax();
/*  30:    */     
/*  31: 86 */     verifyInterval(min, max);
/*  32:    */     
/*  33: 88 */     double relativeAccuracy = getRelativeAccuracy();
/*  34: 89 */     double absoluteAccuracy = getAbsoluteAccuracy();
/*  35: 90 */     double functionValueAccuracy = getFunctionValueAccuracy();
/*  36:    */     
/*  37:    */ 
/*  38:    */ 
/*  39:    */ 
/*  40:    */ 
/*  41: 96 */     double x0 = min;
/*  42: 97 */     double y0 = computeObjectiveValue(x0);
/*  43: 98 */     if (FastMath.abs(y0) < functionValueAccuracy) {
/*  44: 99 */       return x0;
/*  45:    */     }
/*  46:101 */     double x1 = max;
/*  47:102 */     double y1 = computeObjectiveValue(x1);
/*  48:103 */     if (FastMath.abs(y1) < functionValueAccuracy) {
/*  49:104 */       return x1;
/*  50:    */     }
/*  51:107 */     if (y0 * y1 > 0.0D) {
/*  52:108 */       throw new NoBracketingException(x0, x1, y0, y1);
/*  53:    */     }
/*  54:111 */     double x2 = 0.5D * (x0 + x1);
/*  55:112 */     double y2 = computeObjectiveValue(x2);
/*  56:    */     
/*  57:114 */     double oldx = (1.0D / 0.0D);
/*  58:    */     for (;;)
/*  59:    */     {
/*  60:117 */       double q = (x2 - x1) / (x1 - x0);
/*  61:118 */       double a = q * (y2 - (1.0D + q) * y1 + q * y0);
/*  62:119 */       double b = (2.0D * q + 1.0D) * y2 - (1.0D + q) * (1.0D + q) * y1 + q * q * y0;
/*  63:120 */       double c = (1.0D + q) * y2;
/*  64:121 */       double delta = b * b - 4.0D * a * c;
/*  65:    */       double denominator;
/*  66:    */       
/*  67:124 */       if (delta >= 0.0D)
/*  68:    */       {
/*  69:126 */         double dplus = b + FastMath.sqrt(delta);
/*  70:127 */         double dminus = b - FastMath.sqrt(delta);
/*  71:128 */         denominator = FastMath.abs(dplus) > FastMath.abs(dminus) ? dplus : dminus;
/*  72:    */       }
/*  73:    */       else
/*  74:    */       {
/*  75:131 */         denominator = FastMath.sqrt(b * b - delta);
/*  76:    */       }
/*  77:133 */       if (denominator != 0.0D)
/*  78:    */       {
/*  79:134 */         double x = x2 - 2.0D * c * (x2 - x1) / denominator;
/*  80:137 */         while ((x == x1) || (x == x2)) {
/*  81:138 */           x += absoluteAccuracy;
/*  82:    */         }
/*  83:    */       }
/*  84:142 */       double x = min + FastMath.random() * (max - min);
/*  85:143 */       oldx = (1.0D / 0.0D);
/*  86:    */       
/*  87:145 */       double y = computeObjectiveValue(x);
/*  88:    */       
/*  89:    */ 
/*  90:148 */       double tolerance = FastMath.max(relativeAccuracy * FastMath.abs(x), absoluteAccuracy);
/*  91:149 */       if ((FastMath.abs(x - oldx) <= tolerance) || (FastMath.abs(y) <= functionValueAccuracy)) {
/*  92:151 */         return x;
/*  93:    */       }
/*  94:155 */       x0 = x1;
/*  95:156 */       y0 = y1;
/*  96:157 */       x1 = x2;
/*  97:158 */       y1 = y2;
/*  98:159 */       x2 = x;
/*  99:160 */       y2 = y;
/* 100:161 */       oldx = x;
/* 101:    */     }
/* 102:    */   }
/* 103:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.MullerSolver2
 * JD-Core Version:    0.7.0.1
 */
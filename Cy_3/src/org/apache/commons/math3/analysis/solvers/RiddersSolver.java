/*   1:    */ package org.apache.commons.math3.analysis.solvers;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class RiddersSolver
/*   6:    */   extends AbstractUnivariateSolver
/*   7:    */ {
/*   8:    */   private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-006D;
/*   9:    */   
/*  10:    */   public RiddersSolver()
/*  11:    */   {
/*  12: 41 */     this(1.0E-006D);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public RiddersSolver(double absoluteAccuracy)
/*  16:    */   {
/*  17: 49 */     super(absoluteAccuracy);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public RiddersSolver(double relativeAccuracy, double absoluteAccuracy)
/*  21:    */   {
/*  22: 59 */     super(relativeAccuracy, absoluteAccuracy);
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected double doSolve()
/*  26:    */   {
/*  27: 67 */     double min = getMin();
/*  28: 68 */     double max = getMax();
/*  29:    */     
/*  30:    */ 
/*  31:    */ 
/*  32: 72 */     double x1 = min;
/*  33: 73 */     double y1 = computeObjectiveValue(x1);
/*  34: 74 */     double x2 = max;
/*  35: 75 */     double y2 = computeObjectiveValue(x2);
/*  36: 78 */     if (y1 == 0.0D) {
/*  37: 79 */       return min;
/*  38:    */     }
/*  39: 81 */     if (y2 == 0.0D) {
/*  40: 82 */       return max;
/*  41:    */     }
/*  42: 84 */     verifyBracketing(min, max);
/*  43:    */     
/*  44: 86 */     double absoluteAccuracy = getAbsoluteAccuracy();
/*  45: 87 */     double functionValueAccuracy = getFunctionValueAccuracy();
/*  46: 88 */     double relativeAccuracy = getRelativeAccuracy();
/*  47:    */     
/*  48: 90 */     double oldx = (1.0D / 0.0D);
/*  49:    */     for (;;)
/*  50:    */     {
/*  51: 93 */       double x3 = 0.5D * (x1 + x2);
/*  52: 94 */       double y3 = computeObjectiveValue(x3);
/*  53: 95 */       if (FastMath.abs(y3) <= functionValueAccuracy) {
/*  54: 96 */         return x3;
/*  55:    */       }
/*  56: 98 */       double delta = 1.0D - y1 * y2 / (y3 * y3);
/*  57: 99 */       double correction = FastMath.signum(y2) * FastMath.signum(y3) * (x3 - x1) / FastMath.sqrt(delta);
/*  58:    */       
/*  59:101 */       double x = x3 - correction;
/*  60:102 */       double y = computeObjectiveValue(x);
/*  61:    */       
/*  62:    */ 
/*  63:105 */       double tolerance = FastMath.max(relativeAccuracy * FastMath.abs(x), absoluteAccuracy);
/*  64:106 */       if (FastMath.abs(x - oldx) <= tolerance) {
/*  65:107 */         return x;
/*  66:    */       }
/*  67:109 */       if (FastMath.abs(y) <= functionValueAccuracy) {
/*  68:110 */         return x;
/*  69:    */       }
/*  70:115 */       if (correction > 0.0D)
/*  71:    */       {
/*  72:116 */         if (FastMath.signum(y1) + FastMath.signum(y) == 0.0D)
/*  73:    */         {
/*  74:117 */           x2 = x;
/*  75:118 */           y2 = y;
/*  76:    */         }
/*  77:    */         else
/*  78:    */         {
/*  79:120 */           x1 = x;
/*  80:121 */           x2 = x3;
/*  81:122 */           y1 = y;
/*  82:123 */           y2 = y3;
/*  83:    */         }
/*  84:    */       }
/*  85:126 */       else if (FastMath.signum(y2) + FastMath.signum(y) == 0.0D)
/*  86:    */       {
/*  87:127 */         x1 = x;
/*  88:128 */         y1 = y;
/*  89:    */       }
/*  90:    */       else
/*  91:    */       {
/*  92:130 */         x1 = x3;
/*  93:131 */         x2 = x;
/*  94:132 */         y1 = y3;
/*  95:133 */         y2 = y;
/*  96:    */       }
/*  97:136 */       oldx = x;
/*  98:    */     }
/*  99:    */   }
/* 100:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.solvers.RiddersSolver
 * JD-Core Version:    0.7.0.1
 */
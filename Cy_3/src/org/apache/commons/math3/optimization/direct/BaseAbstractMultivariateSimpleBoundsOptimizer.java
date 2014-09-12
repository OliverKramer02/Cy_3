/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   7:    */ import org.apache.commons.math3.optimization.BaseMultivariateOptimizer;
/*   8:    */ import org.apache.commons.math3.optimization.BaseMultivariateSimpleBoundsOptimizer;
/*   9:    */ import org.apache.commons.math3.optimization.ConvergenceChecker;
/*  10:    */ import org.apache.commons.math3.optimization.GoalType;
/*  11:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*  12:    */ 
/*  13:    */ public abstract class BaseAbstractMultivariateSimpleBoundsOptimizer<FUNC extends MultivariateFunction>
/*  14:    */   extends BaseAbstractMultivariateOptimizer<FUNC>
/*  15:    */   implements BaseMultivariateOptimizer<FUNC>, BaseMultivariateSimpleBoundsOptimizer<FUNC>
/*  16:    */ {
/*  17:    */   private double[] lowerBound;
/*  18:    */   private double[] upperBound;
/*  19:    */   
/*  20:    */   protected BaseAbstractMultivariateSimpleBoundsOptimizer() {}
/*  21:    */   
/*  22:    */   protected BaseAbstractMultivariateSimpleBoundsOptimizer(ConvergenceChecker<PointValuePair> checker)
/*  23:    */   {
/*  24: 65 */     super(checker);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public double[] getLowerBound()
/*  28:    */   {
/*  29: 72 */     return (double[])this.lowerBound.clone();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public double[] getUpperBound()
/*  33:    */   {
/*  34: 79 */     return (double[])this.upperBound.clone();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public PointValuePair optimize(int maxEval, FUNC f, GoalType goalType, double[] startPoint)
/*  38:    */   {
/*  39: 85 */     return optimize(maxEval, f, goalType, startPoint, null, null);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public PointValuePair optimize(int maxEval, FUNC f, GoalType goalType, double[] startPoint, double[] lower, double[] upper)
/*  43:    */   {
/*  44: 93 */     int dim = startPoint.length;
/*  45: 94 */     if (lower != null)
/*  46:    */     {
/*  47: 95 */       if (lower.length != dim) {
/*  48: 96 */         throw new DimensionMismatchException(lower.length, dim);
/*  49:    */       }
/*  50: 98 */       for (int i = 0; i < dim; i++)
/*  51:    */       {
/*  52: 99 */         double v = startPoint[i];
/*  53:100 */         double lo = lower[i];
/*  54:101 */         if (v < lo) {
/*  55:102 */           throw new NumberIsTooSmallException(Double.valueOf(v), Double.valueOf(lo), true);
/*  56:    */         }
/*  57:    */       }
/*  58:    */     }
/*  59:106 */     if (upper != null)
/*  60:    */     {
/*  61:107 */       if (upper.length != dim) {
/*  62:108 */         throw new DimensionMismatchException(upper.length, dim);
/*  63:    */       }
/*  64:110 */       for (int i = 0; i < dim; i++)
/*  65:    */       {
/*  66:111 */         double v = startPoint[i];
/*  67:112 */         double hi = upper[i];
/*  68:113 */         if (v > hi) {
/*  69:114 */           throw new NumberIsTooLargeException(Double.valueOf(v), Double.valueOf(hi), true);
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73:120 */     if (lower == null)
/*  74:    */     {
/*  75:121 */       this.lowerBound = new double[dim];
/*  76:122 */       for (int i = 0; i < dim; i++) {
/*  77:123 */         this.lowerBound[i] = (-1.0D / 0.0D);
/*  78:    */       }
/*  79:    */     }
/*  80:    */     else
/*  81:    */     {
/*  82:126 */       this.lowerBound = ((double[])lower.clone());
/*  83:    */     }
/*  84:128 */     if (upper == null)
/*  85:    */     {
/*  86:129 */       this.upperBound = new double[dim];
/*  87:130 */       for (int i = 0; i < dim; i++) {
/*  88:131 */         this.upperBound[i] = (1.0D / 0.0D);
/*  89:    */       }
/*  90:    */     }
/*  91:    */     else
/*  92:    */     {
/*  93:134 */       this.upperBound = ((double[])upper.clone());
/*  94:    */     }
/*  95:138 */     return super.optimize(maxEval, f, goalType, startPoint);
/*  96:    */   }
/*  97:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateSimpleBoundsOptimizer
 * JD-Core Version:    0.7.0.1
 */
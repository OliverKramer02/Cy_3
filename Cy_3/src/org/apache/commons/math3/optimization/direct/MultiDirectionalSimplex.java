/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import java.util.Comparator;
/*   4:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   5:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*   6:    */ 
/*   7:    */ public class MultiDirectionalSimplex
/*   8:    */   extends AbstractSimplex
/*   9:    */ {
/*  10:    */   private static final double DEFAULT_KHI = 2.0D;
/*  11:    */   private static final double DEFAULT_GAMMA = 0.5D;
/*  12:    */   private final double khi;
/*  13:    */   private final double gamma;
/*  14:    */   
/*  15:    */   public MultiDirectionalSimplex(int n)
/*  16:    */   {
/*  17: 48 */     this(n, 1.0D);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public MultiDirectionalSimplex(int n, double sideLength)
/*  21:    */   {
/*  22: 60 */     this(n, sideLength, 2.0D, 0.5D);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public MultiDirectionalSimplex(int n, double khi, double gamma)
/*  26:    */   {
/*  27: 73 */     this(n, 1.0D, khi, gamma);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public MultiDirectionalSimplex(int n, double sideLength, double khi, double gamma)
/*  31:    */   {
/*  32: 88 */     super(n, sideLength);
/*  33:    */     
/*  34: 90 */     this.khi = khi;
/*  35: 91 */     this.gamma = gamma;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public MultiDirectionalSimplex(double[] steps)
/*  39:    */   {
/*  40:102 */     this(steps, 2.0D, 0.5D);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public MultiDirectionalSimplex(double[] steps, double khi, double gamma)
/*  44:    */   {
/*  45:116 */     super(steps);
/*  46:    */     
/*  47:118 */     this.khi = khi;
/*  48:119 */     this.gamma = gamma;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public MultiDirectionalSimplex(double[][] referenceSimplex)
/*  52:    */   {
/*  53:130 */     this(referenceSimplex, 2.0D, 0.5D);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public MultiDirectionalSimplex(double[][] referenceSimplex, double khi, double gamma)
/*  57:    */   {
/*  58:147 */     super(referenceSimplex);
/*  59:    */     
/*  60:149 */     this.khi = khi;
/*  61:150 */     this.gamma = gamma;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void iterate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator)
/*  65:    */   {
/*  66:158 */     PointValuePair[] original = getPoints();
/*  67:159 */     PointValuePair best = original[0];
/*  68:    */     
/*  69:    */ 
/*  70:162 */     PointValuePair reflected = evaluateNewSimplex(evaluationFunction, original, 1.0D, comparator);
/*  71:164 */     if (comparator.compare(reflected, best) < 0)
/*  72:    */     {
/*  73:166 */       PointValuePair[] reflectedSimplex = getPoints();
/*  74:167 */       PointValuePair expanded = evaluateNewSimplex(evaluationFunction, original, this.khi, comparator);
/*  75:169 */       if (comparator.compare(reflected, expanded) <= 0) {
/*  76:171 */         setPoints(reflectedSimplex);
/*  77:    */       }
/*  78:174 */       return;
/*  79:    */     }
/*  80:178 */     evaluateNewSimplex(evaluationFunction, original, this.gamma, comparator);
/*  81:    */   }
/*  82:    */   
/*  83:    */   private PointValuePair evaluateNewSimplex(MultivariateFunction evaluationFunction, PointValuePair[] original, double coeff, Comparator<PointValuePair> comparator)
/*  84:    */   {
/*  85:198 */     double[] xSmallest = original[0].getPointRef();
/*  86:    */     
/*  87:    */ 
/*  88:201 */     setPoint(0, original[0]);
/*  89:202 */     int dim = getDimension();
/*  90:203 */     for (int i = 1; i < getSize(); i++)
/*  91:    */     {
/*  92:204 */       double[] xOriginal = original[i].getPointRef();
/*  93:205 */       double[] xTransformed = new double[dim];
/*  94:206 */       for (int j = 0; j < dim; j++) {
/*  95:207 */         xSmallest[j] += coeff * (xSmallest[j] - xOriginal[j]);
/*  96:    */       }
/*  97:209 */       setPoint(i, new PointValuePair(xTransformed, (0.0D / 0.0D), false));
/*  98:    */     }
/*  99:213 */     evaluate(evaluationFunction, comparator);
/* 100:    */     
/* 101:215 */     return getPoint(0);
/* 102:    */   }
/* 103:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.MultiDirectionalSimplex
 * JD-Core Version:    0.7.0.1
 */
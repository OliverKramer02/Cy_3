/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import java.util.Comparator;
/*   4:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   5:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*   6:    */ 
/*   7:    */ public class NelderMeadSimplex
/*   8:    */   extends AbstractSimplex
/*   9:    */ {
/*  10:    */   private static final double DEFAULT_RHO = 1.0D;
/*  11:    */   private static final double DEFAULT_KHI = 2.0D;
/*  12:    */   private static final double DEFAULT_GAMMA = 0.5D;
/*  13:    */   private static final double DEFAULT_SIGMA = 0.5D;
/*  14:    */   private final double rho;
/*  15:    */   private final double khi;
/*  16:    */   private final double gamma;
/*  17:    */   private final double sigma;
/*  18:    */   
/*  19:    */   public NelderMeadSimplex(int n)
/*  20:    */   {
/*  21: 57 */     this(n, 1.0D);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public NelderMeadSimplex(int n, double sideLength)
/*  25:    */   {
/*  26: 70 */     this(n, sideLength, 1.0D, 2.0D, 0.5D, 0.5D);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public NelderMeadSimplex(int n, double sideLength, double rho, double khi, double gamma, double sigma)
/*  30:    */   {
/*  31: 89 */     super(n, sideLength);
/*  32:    */     
/*  33: 91 */     this.rho = rho;
/*  34: 92 */     this.khi = khi;
/*  35: 93 */     this.gamma = gamma;
/*  36: 94 */     this.sigma = sigma;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public NelderMeadSimplex(int n, double rho, double khi, double gamma, double sigma)
/*  40:    */   {
/*  41:110 */     this(n, 1.0D, rho, khi, gamma, sigma);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public NelderMeadSimplex(double[] steps)
/*  45:    */   {
/*  46:122 */     this(steps, 1.0D, 2.0D, 0.5D, 0.5D);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public NelderMeadSimplex(double[] steps, double rho, double khi, double gamma, double sigma)
/*  50:    */   {
/*  51:140 */     super(steps);
/*  52:    */     
/*  53:142 */     this.rho = rho;
/*  54:143 */     this.khi = khi;
/*  55:144 */     this.gamma = gamma;
/*  56:145 */     this.sigma = sigma;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public NelderMeadSimplex(double[][] referenceSimplex)
/*  60:    */   {
/*  61:157 */     this(referenceSimplex, 1.0D, 2.0D, 0.5D, 0.5D);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public NelderMeadSimplex(double[][] referenceSimplex, double rho, double khi, double gamma, double sigma)
/*  65:    */   {
/*  66:177 */     super(referenceSimplex);
/*  67:    */     
/*  68:179 */     this.rho = rho;
/*  69:180 */     this.khi = khi;
/*  70:181 */     this.gamma = gamma;
/*  71:182 */     this.sigma = sigma;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void iterate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator)
/*  75:    */   {
/*  76:190 */     int n = getDimension();
/*  77:    */     
/*  78:    */ 
/*  79:193 */     PointValuePair best = getPoint(0);
/*  80:194 */     PointValuePair secondBest = getPoint(n - 1);
/*  81:195 */     PointValuePair worst = getPoint(n);
/*  82:196 */     double[] xWorst = worst.getPointRef();
/*  83:    */     
/*  84:    */ 
/*  85:    */ 
/*  86:200 */     double[] centroid = new double[n];
/*  87:201 */     for (int i = 0; i < n; i++)
/*  88:    */     {
/*  89:202 */       double[] x = getPoint(i).getPointRef();
/*  90:203 */       for (int j = 0; j < n; j++) {
/*  91:204 */         centroid[j] += x[j];
/*  92:    */       }
/*  93:    */     }
/*  94:207 */     double scaling = 1.0D / n;
/*  95:208 */     for (int j = 0; j < n; j++) {
/*  96:209 */       centroid[j] *= scaling;
/*  97:    */     }
/*  98:213 */     double[] xR = new double[n];
/*  99:214 */     for (int j = 0; j < n; j++) {
/* 100:215 */       centroid[j] += this.rho * (centroid[j] - xWorst[j]);
/* 101:    */     }
/* 102:217 */     PointValuePair reflected = new PointValuePair(xR, evaluationFunction.value(xR), false);
/* 103:220 */     if ((comparator.compare(best, reflected) <= 0) && (comparator.compare(reflected, secondBest) < 0))
/* 104:    */     {
/* 105:223 */       replaceWorstPoint(reflected, comparator);
/* 106:    */     }
/* 107:224 */     else if (comparator.compare(reflected, best) < 0)
/* 108:    */     {
/* 109:226 */       double[] xE = new double[n];
/* 110:227 */       for (int j = 0; j < n; j++) {
/* 111:228 */         centroid[j] += this.khi * (xR[j] - centroid[j]);
/* 112:    */       }
/* 113:230 */       PointValuePair expanded = new PointValuePair(xE, evaluationFunction.value(xE), false);
/* 114:233 */       if (comparator.compare(expanded, reflected) < 0) {
/* 115:235 */         replaceWorstPoint(expanded, comparator);
/* 116:    */       } else {
/* 117:238 */         replaceWorstPoint(reflected, comparator);
/* 118:    */       }
/* 119:    */     }
/* 120:    */     else
/* 121:    */     {
/* 122:241 */       if (comparator.compare(reflected, worst) < 0)
/* 123:    */       {
/* 124:243 */         double[] xC = new double[n];
/* 125:244 */         for (int j = 0; j < n; j++) {
/* 126:245 */           centroid[j] += this.gamma * (xR[j] - centroid[j]);
/* 127:    */         }
/* 128:247 */         PointValuePair outContracted = new PointValuePair(xC, evaluationFunction.value(xC), false);
/* 129:249 */         if (comparator.compare(outContracted, reflected) <= 0)
/* 130:    */         {
/* 131:251 */           replaceWorstPoint(outContracted, comparator);
/* 132:252 */           return;
/* 133:    */         }
/* 134:    */       }
/* 135:    */       else
/* 136:    */       {
/* 137:256 */         double[] xC = new double[n];
/* 138:257 */         for (int j = 0; j < n; j++) {
/* 139:258 */           centroid[j] -= this.gamma * (centroid[j] - xWorst[j]);
/* 140:    */         }
/* 141:260 */         PointValuePair inContracted = new PointValuePair(xC, evaluationFunction.value(xC), false);
/* 142:263 */         if (comparator.compare(inContracted, worst) < 0)
/* 143:    */         {
/* 144:265 */           replaceWorstPoint(inContracted, comparator);
/* 145:266 */           return;
/* 146:    */         }
/* 147:    */       }
/* 148:271 */       double[] xSmallest = getPoint(0).getPointRef();
/* 149:272 */       for (int i = 1; i <= n; i++)
/* 150:    */       {
/* 151:273 */         double[] x = getPoint(i).getPoint();
/* 152:274 */         for (int j = 0; j < n; j++) {
/* 153:275 */           xSmallest[j] += this.sigma * (x[j] - xSmallest[j]);
/* 154:    */         }
/* 155:277 */         setPoint(i, new PointValuePair(x, (0.0D / 0.0D), false));
/* 156:    */       }
/* 157:279 */       evaluate(evaluationFunction, comparator);
/* 158:    */     }
/* 159:    */   }
/* 160:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.NelderMeadSimplex
 * JD-Core Version:    0.7.0.1
 */
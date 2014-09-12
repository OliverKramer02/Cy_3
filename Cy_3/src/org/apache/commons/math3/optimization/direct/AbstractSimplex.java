/*   1:    */ package org.apache.commons.math3.optimization.direct;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Comparator;
/*   5:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   9:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  11:    */ import org.apache.commons.math3.exception.ZeroException;
/*  12:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  13:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*  14:    */ 
/*  15:    */ public abstract class AbstractSimplex
/*  16:    */ {
/*  17:    */   private PointValuePair[] simplex;
/*  18:    */   private double[][] startConfiguration;
/*  19:    */   private final int dimension;
/*  20:    */   
/*  21:    */   protected AbstractSimplex(int n)
/*  22:    */   {
/*  23: 64 */     this(n, 1.0D);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected AbstractSimplex(int n, double sideLength)
/*  27:    */   {
/*  28: 75 */     this(createHypercubeSteps(n, sideLength));
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected AbstractSimplex(double[] steps)
/*  32:    */   {
/*  33: 97 */     if (steps == null) {
/*  34: 98 */       throw new NullArgumentException();
/*  35:    */     }
/*  36:100 */     if (steps.length == 0) {
/*  37:101 */       throw new ZeroException();
/*  38:    */     }
/*  39:103 */     this.dimension = steps.length;
/*  40:    */     
/*  41:    */ 
/*  42:    */ 
/*  43:107 */     this.startConfiguration = new double[this.dimension][this.dimension];
/*  44:108 */     for (int i = 0; i < this.dimension; i++)
/*  45:    */     {
/*  46:109 */       double[] vertexI = this.startConfiguration[i];
/*  47:110 */       for (int j = 0; j < i + 1; j++)
/*  48:    */       {
/*  49:111 */         if (steps[j] == 0.0D) {
/*  50:112 */           throw new ZeroException(LocalizedFormats.EQUAL_VERTICES_IN_SIMPLEX, new Object[0]);
/*  51:    */         }
/*  52:114 */         System.arraycopy(steps, 0, vertexI, 0, j + 1);
/*  53:    */       }
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected AbstractSimplex(double[][] referenceSimplex)
/*  58:    */   {
/*  59:132 */     if (referenceSimplex.length <= 0) {
/*  60:133 */       throw new NotStrictlyPositiveException(LocalizedFormats.SIMPLEX_NEED_ONE_POINT, Integer.valueOf(referenceSimplex.length));
/*  61:    */     }
/*  62:136 */     this.dimension = (referenceSimplex.length - 1);
/*  63:    */     
/*  64:    */ 
/*  65:    */ 
/*  66:140 */     this.startConfiguration = new double[this.dimension][this.dimension];
/*  67:141 */     double[] ref0 = referenceSimplex[0];
/*  68:144 */     for (int i = 0; i < referenceSimplex.length; i++)
/*  69:    */     {
/*  70:145 */       double[] refI = referenceSimplex[i];
/*  71:148 */       if (refI.length != this.dimension) {
/*  72:149 */         throw new DimensionMismatchException(refI.length, this.dimension);
/*  73:    */       }
/*  74:151 */       for (int j = 0; j < i; j++)
/*  75:    */       {
/*  76:152 */         double[] refJ = referenceSimplex[j];
/*  77:153 */         boolean allEquals = true;
/*  78:154 */         for (int k = 0; k < this.dimension; k++) {
/*  79:155 */           if (refI[k] != refJ[k])
/*  80:    */           {
/*  81:156 */             allEquals = false;
/*  82:157 */             break;
/*  83:    */           }
/*  84:    */         }
/*  85:160 */         if (allEquals) {
/*  86:161 */           throw new MathIllegalArgumentException(LocalizedFormats.EQUAL_VERTICES_IN_SIMPLEX, new Object[] { Integer.valueOf(i), Integer.valueOf(j) });
/*  87:    */         }
/*  88:    */       }
/*  89:167 */       if (i > 0)
/*  90:    */       {
/*  91:168 */         double[] confI = this.startConfiguration[(i - 1)];
/*  92:169 */         for (int k = 0; k < this.dimension; k++) {
/*  93:170 */           refI[k] -= ref0[k];
/*  94:    */         }
/*  95:    */       }
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getDimension()
/* 100:    */   {
/* 101:182 */     return this.dimension;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int getSize()
/* 105:    */   {
/* 106:193 */     return this.simplex.length;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public abstract void iterate(MultivariateFunction paramMultivariateFunction, Comparator<PointValuePair> paramComparator);
/* 110:    */   
/* 111:    */   public void build(double[] startPoint)
/* 112:    */   {
/* 113:216 */     if (this.dimension != startPoint.length) {
/* 114:217 */       throw new DimensionMismatchException(this.dimension, startPoint.length);
/* 115:    */     }
/* 116:221 */     this.simplex = new PointValuePair[this.dimension + 1];
/* 117:222 */     this.simplex[0] = new PointValuePair(startPoint, (0.0D / 0.0D));
/* 118:225 */     for (int i = 0; i < this.dimension; i++)
/* 119:    */     {
/* 120:226 */       double[] confI = this.startConfiguration[i];
/* 121:227 */       double[] vertexI = new double[this.dimension];
/* 122:228 */       for (int k = 0; k < this.dimension; k++) {
/* 123:229 */         startPoint[k] += confI[k];
/* 124:    */       }
/* 125:231 */       this.simplex[(i + 1)] = new PointValuePair(vertexI, (0.0D / 0.0D));
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void evaluate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator)
/* 130:    */   {
/* 131:246 */     for (int i = 0; i < this.simplex.length; i++)
/* 132:    */     {
/* 133:247 */       PointValuePair vertex = this.simplex[i];
/* 134:248 */       double[] point = vertex.getPointRef();
/* 135:249 */       if (Double.isNaN(((Double)vertex.getValue()).doubleValue())) {
/* 136:250 */         this.simplex[i] = new PointValuePair(point, evaluationFunction.value(point), false);
/* 137:    */       }
/* 138:    */     }
/* 139:255 */     Arrays.sort(this.simplex, comparator);
/* 140:    */   }
/* 141:    */   
/* 142:    */   protected void replaceWorstPoint(PointValuePair pointValuePair, Comparator<PointValuePair> comparator)
/* 143:    */   {
/* 144:267 */     for (int i = 0; i < this.dimension; i++) {
/* 145:268 */       if (comparator.compare(this.simplex[i], pointValuePair) > 0)
/* 146:    */       {
/* 147:269 */         PointValuePair tmp = this.simplex[i];
/* 148:270 */         this.simplex[i] = pointValuePair;
/* 149:271 */         pointValuePair = tmp;
/* 150:    */       }
/* 151:    */     }
/* 152:274 */     this.simplex[this.dimension] = pointValuePair;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public PointValuePair[] getPoints()
/* 156:    */   {
/* 157:283 */     PointValuePair[] copy = new PointValuePair[this.simplex.length];
/* 158:284 */     System.arraycopy(this.simplex, 0, copy, 0, this.simplex.length);
/* 159:285 */     return copy;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public PointValuePair getPoint(int index)
/* 163:    */   {
/* 164:295 */     if ((index < 0) || (index >= this.simplex.length)) {
/* 165:297 */       throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.simplex.length - 1));
/* 166:    */     }
/* 167:299 */     return this.simplex[index];
/* 168:    */   }
/* 169:    */   
/* 170:    */   protected void setPoint(int index, PointValuePair point)
/* 171:    */   {
/* 172:310 */     if ((index < 0) || (index >= this.simplex.length)) {
/* 173:312 */       throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.simplex.length - 1));
/* 174:    */     }
/* 175:314 */     this.simplex[index] = point;
/* 176:    */   }
/* 177:    */   
/* 178:    */   protected void setPoints(PointValuePair[] points)
/* 179:    */   {
/* 180:324 */     if (points.length != this.simplex.length) {
/* 181:325 */       throw new DimensionMismatchException(points.length, this.simplex.length);
/* 182:    */     }
/* 183:327 */     this.simplex = points;
/* 184:    */   }
/* 185:    */   
/* 186:    */   private static double[] createHypercubeSteps(int n, double sideLength)
/* 187:    */   {
/* 188:339 */     double[] steps = new double[n];
/* 189:340 */     for (int i = 0; i < n; i++) {
/* 190:341 */       steps[i] = sideLength;
/* 191:    */     }
/* 192:343 */     return steps;
/* 193:    */   }
/* 194:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.direct.AbstractSimplex
 * JD-Core Version:    0.7.0.1
 */
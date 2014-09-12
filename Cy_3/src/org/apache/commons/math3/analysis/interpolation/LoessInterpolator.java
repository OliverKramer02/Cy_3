/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.NoDataException;
/*   8:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   9:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  10:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ import org.apache.commons.math3.util.FastMath;
/*  13:    */ import org.apache.commons.math3.util.MathArrays;
/*  14:    */ import org.apache.commons.math3.util.MathUtils;
/*  15:    */ 
/*  16:    */ public class LoessInterpolator
/*  17:    */   implements UnivariateInterpolator, Serializable
/*  18:    */ {
/*  19:    */   public static final double DEFAULT_BANDWIDTH = 0.3D;
/*  20:    */   public static final int DEFAULT_ROBUSTNESS_ITERS = 2;
/*  21:    */   public static final double DEFAULT_ACCURACY = 1.0E-012D;
/*  22:    */   private static final long serialVersionUID = 5204927143605193821L;
/*  23:    */   private final double bandwidth;
/*  24:    */   private final int robustnessIters;
/*  25:    */   private final double accuracy;
/*  26:    */   
/*  27:    */   public LoessInterpolator()
/*  28:    */   {
/*  29: 94 */     this.bandwidth = 0.3D;
/*  30: 95 */     this.robustnessIters = 2;
/*  31: 96 */     this.accuracy = 1.0E-012D;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public LoessInterpolator(double bandwidth, int robustnessIters)
/*  35:    */   {
/*  36:122 */     this(bandwidth, robustnessIters, 1.0E-012D);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public LoessInterpolator(double bandwidth, int robustnessIters, double accuracy)
/*  40:    */   {
/*  41:147 */     if ((bandwidth < 0.0D) || (bandwidth > 1.0D)) {
/*  42:149 */       throw new OutOfRangeException(LocalizedFormats.BANDWIDTH, Double.valueOf(bandwidth), Integer.valueOf(0), Integer.valueOf(1));
/*  43:    */     }
/*  44:151 */     this.bandwidth = bandwidth;
/*  45:152 */     if (robustnessIters < 0) {
/*  46:153 */       throw new NotPositiveException(LocalizedFormats.ROBUSTNESS_ITERATIONS, Integer.valueOf(robustnessIters));
/*  47:    */     }
/*  48:155 */     this.robustnessIters = robustnessIters;
/*  49:156 */     this.accuracy = accuracy;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public final PolynomialSplineFunction interpolate(double[] xval, double[] yval)
/*  53:    */   {
/*  54:181 */     return new SplineInterpolator().interpolate(xval, smooth(xval, yval));
/*  55:    */   }
/*  56:    */   
/*  57:    */   public final double[] smooth(double[] xval, double[] yval, double[] weights)
/*  58:    */   {
/*  59:206 */     if (xval.length != yval.length) {
/*  60:207 */       throw new DimensionMismatchException(xval.length, yval.length);
/*  61:    */     }
/*  62:210 */     int n = xval.length;
/*  63:212 */     if (n == 0) {
/*  64:213 */       throw new NoDataException();
/*  65:    */     }
/*  66:216 */     checkAllFiniteReal(xval);
/*  67:217 */     checkAllFiniteReal(yval);
/*  68:218 */     checkAllFiniteReal(weights);
/*  69:    */     
/*  70:220 */     MathArrays.checkOrder(xval);
/*  71:222 */     if (n == 1) {
/*  72:223 */       return new double[] { yval[0] };
/*  73:    */     }
/*  74:226 */     if (n == 2) {
/*  75:227 */       return new double[] { yval[0], yval[1] };
/*  76:    */     }
/*  77:230 */     int bandwidthInPoints = (int)(this.bandwidth * n);
/*  78:232 */     if (bandwidthInPoints < 2) {
/*  79:233 */       throw new NumberIsTooSmallException(LocalizedFormats.BANDWIDTH, Integer.valueOf(bandwidthInPoints), Integer.valueOf(2), true);
/*  80:    */     }
/*  81:237 */     double[] res = new double[n];
/*  82:    */     
/*  83:239 */     double[] residuals = new double[n];
/*  84:240 */     double[] sortedResiduals = new double[n];
/*  85:    */     
/*  86:242 */     double[] robustnessWeights = new double[n];
/*  87:    */     
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:247 */     Arrays.fill(robustnessWeights, 1.0D);
/*  92:249 */     for (int iter = 0; iter <= this.robustnessIters; iter++)
/*  93:    */     {
/*  94:250 */       int[] bandwidthInterval = { 0, bandwidthInPoints - 1 };
/*  95:252 */       for (int i = 0; i < n; i++)
/*  96:    */       {
/*  97:253 */         double x = xval[i];
/*  98:257 */         if (i > 0) {
/*  99:258 */           updateBandwidthInterval(xval, weights, i, bandwidthInterval);
/* 100:    */         }
/* 101:261 */         int ileft = bandwidthInterval[0];
/* 102:262 */         int iright = bandwidthInterval[1];
/* 103:    */         int edge;
/* 104:    */       
/* 105:267 */         if (xval[i] - xval[ileft] > xval[iright] - xval[i]) {
/* 106:268 */           edge = ileft;
/* 107:    */         } else {
/* 108:270 */           edge = iright;
/* 109:    */         }
/* 110:280 */         double sumWeights = 0.0D;
/* 111:281 */         double sumX = 0.0D;
/* 112:282 */         double sumXSquared = 0.0D;
/* 113:283 */         double sumY = 0.0D;
/* 114:284 */         double sumXY = 0.0D;
/* 115:285 */         double denom = FastMath.abs(1.0D / (xval[edge] - x));
/* 116:286 */         for (int k = ileft; k <= iright; k++)
/* 117:    */         {
/* 118:287 */           double xk = xval[k];
/* 119:288 */           double yk = yval[k];
/* 120:289 */           double dist = k < i ? x - xk : xk - x;
/* 121:290 */           double w = tricube(dist * denom) * robustnessWeights[k] * weights[k];
/* 122:291 */           double xkw = xk * w;
/* 123:292 */           sumWeights += w;
/* 124:293 */           sumX += xkw;
/* 125:294 */           sumXSquared += xk * xkw;
/* 126:295 */           sumY += yk * w;
/* 127:296 */           sumXY += yk * xkw;
/* 128:    */         }
/* 129:299 */         double meanX = sumX / sumWeights;
/* 130:300 */         double meanY = sumY / sumWeights;
/* 131:301 */         double meanXY = sumXY / sumWeights;
/* 132:302 */         double meanXSquared = sumXSquared / sumWeights;
/* 133:    */         double beta;
/* 134:    */      
/* 135:305 */         if (FastMath.sqrt(FastMath.abs(meanXSquared - meanX * meanX)) < this.accuracy) {
/* 136:306 */           beta = 0.0D;
/* 137:    */         } else {
/* 138:308 */           beta = (meanXY - meanX * meanY) / (meanXSquared - meanX * meanX);
/* 139:    */         }
/* 140:311 */         double alpha = meanY - beta * meanX;
/* 141:    */         
/* 142:313 */         res[i] = (beta * x + alpha);
/* 143:314 */         residuals[i] = FastMath.abs(yval[i] - res[i]);
/* 144:    */       }
/* 145:319 */       if (iter == this.robustnessIters) {
/* 146:    */         break;
/* 147:    */       }
/* 148:328 */       System.arraycopy(residuals, 0, sortedResiduals, 0, n);
/* 149:329 */       Arrays.sort(sortedResiduals);
/* 150:330 */       double medianResidual = sortedResiduals[(n / 2)];
/* 151:332 */       if (FastMath.abs(medianResidual) < this.accuracy) {
/* 152:    */         break;
/* 153:    */       }
/* 154:336 */       for (int i = 0; i < n; i++)
/* 155:    */       {
/* 156:337 */         double arg = residuals[i] / (6.0D * medianResidual);
/* 157:338 */         if (arg >= 1.0D)
/* 158:    */         {
/* 159:339 */           robustnessWeights[i] = 0.0D;
/* 160:    */         }
/* 161:    */         else
/* 162:    */         {
/* 163:341 */           double w = 1.0D - arg * arg;
/* 164:342 */           robustnessWeights[i] = (w * w);
/* 165:    */         }
/* 166:    */       }
/* 167:    */     }
/* 168:347 */     return res;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public final double[] smooth(double[] xval, double[] yval)
/* 172:    */   {
/* 173:368 */     if (xval.length != yval.length) {
/* 174:369 */       throw new DimensionMismatchException(xval.length, yval.length);
/* 175:    */     }
/* 176:372 */     double[] unitWeights = new double[xval.length];
/* 177:373 */     Arrays.fill(unitWeights, 1.0D);
/* 178:    */     
/* 179:375 */     return smooth(xval, yval, unitWeights);
/* 180:    */   }
/* 181:    */   
/* 182:    */   private static void updateBandwidthInterval(double[] xval, double[] weights, int i, int[] bandwidthInterval)
/* 183:    */   {
/* 184:396 */     int left = bandwidthInterval[0];
/* 185:397 */     int right = bandwidthInterval[1];
/* 186:    */     
/* 187:    */ 
/* 188:    */ 
/* 189:401 */     int nextRight = nextNonzero(weights, right);
/* 190:402 */     if ((nextRight < xval.length) && (xval[nextRight] - xval[i] < xval[i] - xval[left]))
/* 191:    */     {
/* 192:403 */       int nextLeft = nextNonzero(weights, bandwidthInterval[0]);
/* 193:404 */       bandwidthInterval[0] = nextLeft;
/* 194:405 */       bandwidthInterval[1] = nextRight;
/* 195:    */     }
/* 196:    */   }
/* 197:    */   
/* 198:    */   private static int nextNonzero(double[] weights, int i)
/* 199:    */   {
/* 200:418 */     int j = i + 1;
/* 201:419 */     while ((j < weights.length) && (weights[j] == 0.0D)) {
/* 202:420 */       j++;
/* 203:    */     }
/* 204:422 */     return j;
/* 205:    */   }
/* 206:    */   
/* 207:    */   private static double tricube(double x)
/* 208:    */   {
/* 209:434 */     double absX = FastMath.abs(x);
/* 210:435 */     if (absX >= 1.0D) {
/* 211:436 */       return 0.0D;
/* 212:    */     }
/* 213:438 */     double tmp = 1.0D - absX * absX * absX;
/* 214:439 */     return tmp * tmp * tmp;
/* 215:    */   }
/* 216:    */   
/* 217:    */   private static void checkAllFiniteReal(double[] values)
/* 218:    */   {
/* 219:450 */     for (int i = 0; i < values.length; i++) {
/* 220:451 */       MathUtils.checkFinite(values[i]);
/* 221:    */     }
/* 222:    */   }
/* 223:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.LoessInterpolator
 * JD-Core Version:    0.7.0.1
 */
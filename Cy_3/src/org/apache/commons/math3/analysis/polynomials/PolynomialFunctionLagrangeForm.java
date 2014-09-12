/*   1:    */ package org.apache.commons.math3.analysis.polynomials;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ import org.apache.commons.math3.util.MathArrays;
/*   9:    */ import org.apache.commons.math3.util.MathArrays.OrderDirection;
/*  10:    */ 
/*  11:    */ public class PolynomialFunctionLagrangeForm
/*  12:    */   implements UnivariateFunction
/*  13:    */ {
/*  14:    */   private double[] coefficients;
/*  15:    */   private final double[] x;
/*  16:    */   private final double[] y;
/*  17:    */   private boolean coefficientsComputed;
/*  18:    */   
/*  19:    */   public PolynomialFunctionLagrangeForm(double[] x, double[] y)
/*  20:    */   {
/*  21: 72 */     this.x = new double[x.length];
/*  22: 73 */     this.y = new double[y.length];
/*  23: 74 */     System.arraycopy(x, 0, this.x, 0, x.length);
/*  24: 75 */     System.arraycopy(y, 0, this.y, 0, y.length);
/*  25: 76 */     this.coefficientsComputed = false;
/*  26: 78 */     if (!verifyInterpolationArray(x, y, false))
/*  27:    */     {
/*  28: 79 */       MathArrays.sortInPlace(this.x, new double[][] { this.y });
/*  29:    */       
/*  30: 81 */       verifyInterpolationArray(this.x, this.y, true);
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double value(double z)
/*  35:    */   {
/*  36: 98 */     return evaluateInternal(this.x, this.y, z);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public int degree()
/*  40:    */   {
/*  41:107 */     return this.x.length - 1;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public double[] getInterpolatingPoints()
/*  45:    */   {
/*  46:118 */     double[] out = new double[this.x.length];
/*  47:119 */     System.arraycopy(this.x, 0, out, 0, this.x.length);
/*  48:120 */     return out;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double[] getInterpolatingValues()
/*  52:    */   {
/*  53:131 */     double[] out = new double[this.y.length];
/*  54:132 */     System.arraycopy(this.y, 0, out, 0, this.y.length);
/*  55:133 */     return out;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public double[] getCoefficients()
/*  59:    */   {
/*  60:147 */     if (!this.coefficientsComputed) {
/*  61:148 */       computeCoefficients();
/*  62:    */     }
/*  63:150 */     double[] out = new double[this.coefficients.length];
/*  64:151 */     System.arraycopy(this.coefficients, 0, out, 0, this.coefficients.length);
/*  65:152 */     return out;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static double evaluate(double[] x, double[] y, double z)
/*  69:    */   {
/*  70:172 */     if (verifyInterpolationArray(x, y, false)) {
/*  71:173 */       return evaluateInternal(x, y, z);
/*  72:    */     }
/*  73:177 */     double[] xNew = new double[x.length];
/*  74:178 */     double[] yNew = new double[y.length];
/*  75:179 */     System.arraycopy(x, 0, xNew, 0, x.length);
/*  76:180 */     System.arraycopy(y, 0, yNew, 0, y.length);
/*  77:    */     
/*  78:182 */     MathArrays.sortInPlace(xNew, new double[][] { yNew });
/*  79:    */     
/*  80:184 */     verifyInterpolationArray(xNew, yNew, true);
/*  81:185 */     return evaluateInternal(xNew, yNew, z);
/*  82:    */   }
/*  83:    */   
/*  84:    */   private static double evaluateInternal(double[] x, double[] y, double z)
/*  85:    */   {
/*  86:205 */     int nearest = 0;
/*  87:206 */     int n = x.length;
/*  88:207 */     double[] c = new double[n];
/*  89:208 */     double[] d = new double[n];
/*  90:209 */     double min_dist = (1.0D / 0.0D);
/*  91:210 */     for (int i = 0; i < n; i++)
/*  92:    */     {
/*  93:212 */       c[i] = y[i];
/*  94:213 */       d[i] = y[i];
/*  95:    */       
/*  96:215 */       double dist = FastMath.abs(z - x[i]);
/*  97:216 */       if (dist < min_dist)
/*  98:    */       {
/*  99:217 */         nearest = i;
/* 100:218 */         min_dist = dist;
/* 101:    */       }
/* 102:    */     }
/* 103:223 */     double value = y[nearest];
/* 104:225 */     for (int i = 1; i < n; i++)
/* 105:    */     {
/* 106:226 */       for (int j = 0; j < n - i; j++)
/* 107:    */       {
/* 108:227 */         double tc = x[j] - z;
/* 109:228 */         double td = x[(i + j)] - z;
/* 110:229 */         double divider = x[j] - x[(i + j)];
/* 111:    */         
/* 112:231 */         double w = (c[(j + 1)] - d[j]) / divider;
/* 113:232 */         c[j] = (tc * w);
/* 114:233 */         d[j] = (td * w);
/* 115:    */       }
/* 116:236 */       if (nearest < 0.5D * (n - i + 1))
/* 117:    */       {
/* 118:237 */         value += c[nearest];
/* 119:    */       }
/* 120:    */       else
/* 121:    */       {
/* 122:239 */         nearest--;
/* 123:240 */         value += d[nearest];
/* 124:    */       }
/* 125:    */     }
/* 126:244 */     return value;
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected void computeCoefficients()
/* 130:    */   {
/* 131:254 */     int n = degree() + 1;
/* 132:255 */     this.coefficients = new double[n];
/* 133:256 */     for (int i = 0; i < n; i++) {
/* 134:257 */       this.coefficients[i] = 0.0D;
/* 135:    */     }
/* 136:261 */     double[] c = new double[n + 1];
/* 137:262 */     c[0] = 1.0D;
/* 138:263 */     for (int i = 0; i < n; i++)
/* 139:    */     {
/* 140:264 */       for (int j = i; j > 0; j--) {
/* 141:265 */         c[j] = (c[(j - 1)] - c[j] * this.x[i]);
/* 142:    */       }
/* 143:267 */       c[0] *= -this.x[i];
/* 144:268 */       c[(i + 1)] = 1.0D;
/* 145:    */     }
/* 146:271 */     double[] tc = new double[n];
/* 147:272 */     for (int i = 0; i < n; i++)
/* 148:    */     {
/* 149:274 */       double d = 1.0D;
/* 150:275 */       for (int j = 0; j < n; j++) {
/* 151:276 */         if (i != j) {
/* 152:277 */           d *= (this.x[i] - this.x[j]);
/* 153:    */         }
/* 154:    */       }
/* 155:280 */       double t = this.y[i] / d;
/* 156:    */       
/* 157:    */ 
/* 158:    */ 
/* 159:284 */       tc[(n - 1)] = c[n];
/* 160:285 */       this.coefficients[(n - 1)] += t * tc[(n - 1)];
/* 161:286 */       for (int j = n - 2; j >= 0; j--)
/* 162:    */       {
/* 163:287 */         tc[j] = (c[(j + 1)] + tc[(j + 1)] * this.x[i]);
/* 164:288 */         this.coefficients[j] += t * tc[j];
/* 165:    */       }
/* 166:    */     }
/* 167:292 */     this.coefficientsComputed = true;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public static boolean verifyInterpolationArray(double[] x, double[] y, boolean abort)
/* 171:    */   {
/* 172:314 */     if (x.length != y.length) {
/* 173:315 */       throw new DimensionMismatchException(x.length, y.length);
/* 174:    */     }
/* 175:317 */     if (x.length < 2) {
/* 176:318 */       throw new NumberIsTooSmallException(LocalizedFormats.WRONG_NUMBER_OF_POINTS, Integer.valueOf(2), Integer.valueOf(x.length), true);
/* 177:    */     }
/* 178:321 */     return MathArrays.checkOrder(x, MathArrays.OrderDirection.INCREASING, true, abort);
/* 179:    */   }
/* 180:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.analysis.polynomials;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   6:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
/*   7:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   8:    */ import org.apache.commons.math3.exception.NoDataException;
/*   9:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ import org.apache.commons.math3.util.MathUtils;
/*  13:    */ 
/*  14:    */ public class PolynomialFunction
/*  15:    */   implements DifferentiableUnivariateFunction, Serializable
/*  16:    */ {
/*  17:    */   private static final long serialVersionUID = -7726511984200295583L;
/*  18:    */   private final double[] coefficients;
/*  19:    */   
/*  20:    */   public PolynomialFunction(double[] c)
/*  21:    */     throws NullArgumentException, NoDataException
/*  22:    */   {
/*  23: 68 */     MathUtils.checkNotNull(c);
/*  24: 69 */     int n = c.length;
/*  25: 70 */     if (n == 0) {
/*  26: 71 */       throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
/*  27:    */     }
/*  28: 73 */     while ((n > 1) && (c[(n - 1)] == 0.0D)) {
/*  29: 74 */       n--;
/*  30:    */     }
/*  31: 76 */     this.coefficients = new double[n];
/*  32: 77 */     System.arraycopy(c, 0, this.coefficients, 0, n);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public double value(double x)
/*  36:    */   {
/*  37: 92 */     return evaluate(this.coefficients, x);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int degree()
/*  41:    */   {
/*  42:101 */     return this.coefficients.length - 1;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double[] getCoefficients()
/*  46:    */   {
/*  47:113 */     return (double[])this.coefficients.clone();
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected static double evaluate(double[] coefficients, double argument)
/*  51:    */     throws NullArgumentException, NoDataException
/*  52:    */   {
/*  53:128 */     MathUtils.checkNotNull(coefficients);
/*  54:129 */     int n = coefficients.length;
/*  55:130 */     if (n == 0) {
/*  56:131 */       throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
/*  57:    */     }
/*  58:133 */     double result = coefficients[(n - 1)];
/*  59:134 */     for (int j = n - 2; j >= 0; j--) {
/*  60:135 */       result = argument * result + coefficients[j];
/*  61:    */     }
/*  62:137 */     return result;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public PolynomialFunction add(PolynomialFunction p)
/*  66:    */   {
/*  67:148 */     int lowLength = FastMath.min(this.coefficients.length, p.coefficients.length);
/*  68:149 */     int highLength = FastMath.max(this.coefficients.length, p.coefficients.length);
/*  69:    */     
/*  70:    */ 
/*  71:152 */     double[] newCoefficients = new double[highLength];
/*  72:153 */     for (int i = 0; i < lowLength; i++) {
/*  73:154 */       newCoefficients[i] = (this.coefficients[i] + p.coefficients[i]);
/*  74:    */     }
/*  75:156 */     System.arraycopy(this.coefficients.length < p.coefficients.length ? p.coefficients : this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
/*  76:    */     
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:162 */     return new PolynomialFunction(newCoefficients);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public PolynomialFunction subtract(PolynomialFunction p)
/*  85:    */   {
/*  86:173 */     int lowLength = FastMath.min(this.coefficients.length, p.coefficients.length);
/*  87:174 */     int highLength = FastMath.max(this.coefficients.length, p.coefficients.length);
/*  88:    */     
/*  89:    */ 
/*  90:177 */     double[] newCoefficients = new double[highLength];
/*  91:178 */     for (int i = 0; i < lowLength; i++) {
/*  92:179 */       newCoefficients[i] = (this.coefficients[i] - p.coefficients[i]);
/*  93:    */     }
/*  94:181 */     if (this.coefficients.length < p.coefficients.length) {
/*  95:182 */       for (int i = lowLength; i < highLength; i++) {
/*  96:183 */         newCoefficients[i] = (-p.coefficients[i]);
/*  97:    */       }
/*  98:    */     } else {
/*  99:186 */       System.arraycopy(this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
/* 100:    */     }
/* 101:190 */     return new PolynomialFunction(newCoefficients);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public PolynomialFunction negate()
/* 105:    */   {
/* 106:199 */     double[] newCoefficients = new double[this.coefficients.length];
/* 107:200 */     for (int i = 0; i < this.coefficients.length; i++) {
/* 108:201 */       newCoefficients[i] = (-this.coefficients[i]);
/* 109:    */     }
/* 110:203 */     return new PolynomialFunction(newCoefficients);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public PolynomialFunction multiply(PolynomialFunction p)
/* 114:    */   {
/* 115:213 */     double[] newCoefficients = new double[this.coefficients.length + p.coefficients.length - 1];
/* 116:215 */     for (int i = 0; i < newCoefficients.length; i++)
/* 117:    */     {
/* 118:216 */       newCoefficients[i] = 0.0D;
/* 119:217 */       for (int j = FastMath.max(0, i + 1 - p.coefficients.length); j < FastMath.min(this.coefficients.length, i + 1); j++) {
/* 120:220 */         newCoefficients[i] += this.coefficients[j] * p.coefficients[(i - j)];
/* 121:    */       }
/* 122:    */     }
/* 123:224 */     return new PolynomialFunction(newCoefficients);
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected static double[] differentiate(double[] coefficients)
/* 127:    */     throws NullArgumentException, NoDataException
/* 128:    */   {
/* 129:237 */     MathUtils.checkNotNull(coefficients);
/* 130:238 */     int n = coefficients.length;
/* 131:239 */     if (n == 0) {
/* 132:240 */       throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
/* 133:    */     }
/* 134:242 */     if (n == 1) {
/* 135:243 */       return new double[] { 0.0D };
/* 136:    */     }
/* 137:245 */     double[] result = new double[n - 1];
/* 138:246 */     for (int i = n - 1; i > 0; i--) {
/* 139:247 */       result[(i - 1)] = (i * coefficients[i]);
/* 140:    */     }
/* 141:249 */     return result;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public PolynomialFunction polynomialDerivative()
/* 145:    */   {
/* 146:258 */     return new PolynomialFunction(differentiate(this.coefficients));
/* 147:    */   }
/* 148:    */   
/* 149:    */   public UnivariateFunction derivative()
/* 150:    */   {
/* 151:267 */     return polynomialDerivative();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public String toString()
/* 155:    */   {
/* 156:287 */     StringBuilder s = new StringBuilder();
/* 157:288 */     if (this.coefficients[0] == 0.0D)
/* 158:    */     {
/* 159:289 */       if (this.coefficients.length == 1) {
/* 160:290 */         return "0";
/* 161:    */       }
/* 162:    */     }
/* 163:    */     else {
/* 164:293 */       s.append(toString(this.coefficients[0]));
/* 165:    */     }
/* 166:296 */     for (int i = 1; i < this.coefficients.length; i++) {
/* 167:297 */       if (this.coefficients[i] != 0.0D)
/* 168:    */       {
/* 169:298 */         if (s.length() > 0)
/* 170:    */         {
/* 171:299 */           if (this.coefficients[i] < 0.0D) {
/* 172:300 */             s.append(" - ");
/* 173:    */           } else {
/* 174:302 */             s.append(" + ");
/* 175:    */           }
/* 176:    */         }
/* 177:305 */         else if (this.coefficients[i] < 0.0D) {
/* 178:306 */           s.append("-");
/* 179:    */         }
/* 180:310 */         double absAi = FastMath.abs(this.coefficients[i]);
/* 181:311 */         if (absAi - 1.0D != 0.0D)
/* 182:    */         {
/* 183:312 */           s.append(toString(absAi));
/* 184:313 */           s.append(' ');
/* 185:    */         }
/* 186:316 */         s.append("x");
/* 187:317 */         if (i > 1)
/* 188:    */         {
/* 189:318 */           s.append('^');
/* 190:319 */           s.append(Integer.toString(i));
/* 191:    */         }
/* 192:    */       }
/* 193:    */     }
/* 194:324 */     return s.toString();
/* 195:    */   }
/* 196:    */   
/* 197:    */   private static String toString(double coeff)
/* 198:    */   {
/* 199:334 */     String c = Double.toString(coeff);
/* 200:335 */     if (c.endsWith(".0")) {
/* 201:336 */       return c.substring(0, c.length() - 2);
/* 202:    */     }
/* 203:338 */     return c;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public int hashCode()
/* 207:    */   {
/* 208:345 */     int prime = 31;
/* 209:346 */     int result = 1;
/* 210:347 */     result = 31 * result + Arrays.hashCode(this.coefficients);
/* 211:348 */     return result;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public boolean equals(Object obj)
/* 215:    */   {
/* 216:354 */     if (this == obj) {
/* 217:355 */       return true;
/* 218:    */     }
/* 219:357 */     if (!(obj instanceof PolynomialFunction)) {
/* 220:358 */       return false;
/* 221:    */     }
/* 222:360 */     PolynomialFunction other = (PolynomialFunction)obj;
/* 223:361 */     if (!Arrays.equals(this.coefficients, other.coefficients)) {
/* 224:362 */       return false;
/* 225:    */     }
/* 226:364 */     return true;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static class Parametric
/* 230:    */     implements ParametricUnivariateFunction
/* 231:    */   {
/* 232:    */     public double[] gradient(double x, double... parameters)
/* 233:    */     {
/* 234:375 */       double[] gradient = new double[parameters.length];
/* 235:376 */       double xn = 1.0D;
/* 236:377 */       for (int i = 0; i < parameters.length; i++)
/* 237:    */       {
/* 238:378 */         gradient[i] = xn;
/* 239:379 */         xn *= x;
/* 240:    */       }
/* 241:381 */       return gradient;
/* 242:    */     }
/* 243:    */     
/* 244:    */     public double value(double x, double... parameters)
/* 245:    */     {
/* 246:386 */       return PolynomialFunction.evaluate(parameters, x);
/* 247:    */     }
/* 248:    */   }
/* 249:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.polynomials.PolynomialFunction
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.analysis.polynomials;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.commons.math3.fraction.BigFraction;
/*   8:    */ import org.apache.commons.math3.util.ArithmeticUtils;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class PolynomialsUtils
/*  12:    */ {
/*  13: 55 */   private static final List<BigFraction> CHEBYSHEV_COEFFICIENTS = new ArrayList();
/*  14:    */   private static final List<BigFraction> HERMITE_COEFFICIENTS;
/*  15:    */   private static final List<BigFraction> LAGUERRE_COEFFICIENTS;
/*  16:    */   private static final List<BigFraction> LEGENDRE_COEFFICIENTS;
/*  17:    */   
/*  18:    */   static
/*  19:    */   {
/*  20: 56 */     CHEBYSHEV_COEFFICIENTS.add(BigFraction.ONE);
/*  21: 57 */     CHEBYSHEV_COEFFICIENTS.add(BigFraction.ZERO);
/*  22: 58 */     CHEBYSHEV_COEFFICIENTS.add(BigFraction.ONE);
/*  23:    */     
/*  24:    */ 
/*  25:    */ 
/*  26: 62 */     HERMITE_COEFFICIENTS = new ArrayList();
/*  27: 63 */     HERMITE_COEFFICIENTS.add(BigFraction.ONE);
/*  28: 64 */     HERMITE_COEFFICIENTS.add(BigFraction.ZERO);
/*  29: 65 */     HERMITE_COEFFICIENTS.add(BigFraction.TWO);
/*  30:    */     
/*  31:    */ 
/*  32:    */ 
/*  33: 69 */     LAGUERRE_COEFFICIENTS = new ArrayList();
/*  34: 70 */     LAGUERRE_COEFFICIENTS.add(BigFraction.ONE);
/*  35: 71 */     LAGUERRE_COEFFICIENTS.add(BigFraction.ONE);
/*  36: 72 */     LAGUERRE_COEFFICIENTS.add(BigFraction.MINUS_ONE);
/*  37:    */     
/*  38:    */ 
/*  39:    */ 
/*  40: 76 */     LEGENDRE_COEFFICIENTS = new ArrayList();
/*  41: 77 */     LEGENDRE_COEFFICIENTS.add(BigFraction.ONE);
/*  42: 78 */     LEGENDRE_COEFFICIENTS.add(BigFraction.ZERO);
/*  43: 79 */     LEGENDRE_COEFFICIENTS.add(BigFraction.ONE);
/*  44:    */   }
/*  45:    */   
/*  46: 82 */   private static final Map<JacobiKey, List<BigFraction>> JACOBI_COEFFICIENTS = new HashMap();
/*  47:    */   
/*  48:    */   public static PolynomialFunction createChebyshevPolynomial(int degree)
/*  49:    */   {
return buildPolynomial(degree, CHEBYSHEV_COEFFICIENTS, new RecurrenceCoefficientsGenerator()
/*  51:    */     {
/*  52:108 */       private final BigFraction[] coeffs = { BigFraction.ZERO, BigFraction.TWO, BigFraction.ONE };
/*  53:    */       
/*  54:    */       public BigFraction[] generate(int k)
/*  55:    */       {
/*  56:111 */         return this.coeffs;
/*  57:    */       }
/*  58:    */     });   }
/*  60:    */   
/*  61:    */   public static PolynomialFunction createHermitePolynomial(int degree)
/*  62:    */   {
return buildPolynomial(degree, HERMITE_COEFFICIENTS, new RecurrenceCoefficientsGenerator()
/*  64:    */     {
/*  65:    */       public BigFraction[] generate(int k)
/*  66:    */       {
/*  67:135 */         return new BigFraction[] { BigFraction.ZERO, BigFraction.TWO, new BigFraction(2 * k) };
/*  68:    */       }
/*  69:    */     });   }
/*  71:    */   
/*  72:    */   public static PolynomialFunction createLaguerrePolynomial(int degree)
/*  73:    */   {
return buildPolynomial(degree, LAGUERRE_COEFFICIENTS, new RecurrenceCoefficientsGenerator()
/*  75:    */     {
/*  76:    */       public BigFraction[] generate(int k)
/*  77:    */       {
/*  78:161 */         int kP1 = k + 1;
/*  79:162 */         return new BigFraction[] { new BigFraction(2 * k + 1, kP1), new BigFraction(-1, kP1), new BigFraction(k, kP1) };
/*  80:    */       }
/*  81:    */     });   }
/*  83:    */   
/*  84:    */   public static PolynomialFunction createLegendrePolynomial(int degree)
/*  85:    */   {
return buildPolynomial(degree, LEGENDRE_COEFFICIENTS, new RecurrenceCoefficientsGenerator()
/*  87:    */     {
/*  88:    */       public BigFraction[] generate(int k)
/*  89:    */       {
/*  90:188 */         int kP1 = k + 1;
/*  91:189 */         return new BigFraction[] { BigFraction.ZERO, new BigFraction(k + kP1, kP1), new BigFraction(k, kP1) };
/*  92:    */       }
/*  93:    */     });   }
/*  95:    */   
/*  96:    */   public static PolynomialFunction createJacobiPolynomial(int degree, int v, final int w)
/*  97:    */   {
/*  98:217 */     JacobiKey key = new JacobiKey(v, w);
/*  99:219 */     if (!JACOBI_COEFFICIENTS.containsKey(key))
/* 100:    */     {
/* 101:222 */       List<BigFraction> list = new ArrayList();
/* 102:223 */       JACOBI_COEFFICIENTS.put(key, list);
/* 103:    */       
/* 104:    */ 
/* 105:226 */       list.add(BigFraction.ONE);
/* 106:    */       
/* 107:    */ 
/* 108:229 */       list.add(new BigFraction(v - w, 2));
/* 109:230 */       list.add(new BigFraction(2 + v + w, 2));
/* 110:    */     }
return buildPolynomial(degree, (List)JACOBI_COEFFICIENTS.get(key), new RecurrenceCoefficientsGenerator()
/* 112:    */     {
private int val$v;

/* 113:    */       public BigFraction[] generate(int k)
/* 114:    */       {
/* 115:238 */         k++;
/* 116:239 */         int kvw = k + this.val$v + w;
/* 117:240 */         int twoKvw = kvw + k;
/* 118:241 */         int twoKvwM1 = twoKvw - 1;
/* 119:242 */         int twoKvwM2 = twoKvw - 2;
/* 120:243 */         int den = 2 * k * kvw * twoKvwM2;
/* 121:    */         
/* 122:245 */         return new BigFraction[] { new BigFraction(twoKvwM1 * (this.val$v * this.val$v - w * w), den), new BigFraction(twoKvwM1 * twoKvw * twoKvwM2, den), new BigFraction(2 * (k + this.val$v - 1) * (k + w - 1) * twoKvw, den) };
/* 123:    */       }
/* 124:    */     });   }
/* 126:    */   
/* 127:    */   private static abstract interface RecurrenceCoefficientsGenerator
/* 128:    */   {
/* 129:    */     public abstract BigFraction[] generate(int paramInt);
/* 130:    */   }
/* 131:    */   
/* 132:    */   private static class JacobiKey
/* 133:    */   {
/* 134:    */     private final int v;
/* 135:    */     private final int w;
/* 136:    */     
/* 137:    */     public JacobiKey(int v, int w)
/* 138:    */     {
/* 139:269 */       this.v = v;
/* 140:270 */       this.w = w;
/* 141:    */     }
/* 142:    */     
/* 143:    */     public int hashCode()
/* 144:    */     {
/* 145:278 */       return this.v << 16 ^ this.w;
/* 146:    */     }
/* 147:    */     
/* 148:    */     public boolean equals(Object key)
/* 149:    */     {
/* 150:288 */       if ((key == null) || (!(key instanceof JacobiKey))) {
/* 151:289 */         return false;
/* 152:    */       }
/* 153:292 */       JacobiKey otherK = (JacobiKey)key;
/* 154:293 */       return (this.v == otherK.v) && (this.w == otherK.w);
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   public static double[] shift(double[] coefficients, double shift)
/* 159:    */   {
/* 160:324 */     int dp1 = coefficients.length;
/* 161:325 */     double[] newCoefficients = new double[dp1];
/* 162:    */     
/* 163:    */ 
/* 164:328 */     int[][] coeff = new int[dp1][dp1];
/* 165:329 */     for (int i = 0; i < dp1; i++) {
/* 166:330 */       for (int j = 0; j <= i; j++) {
/* 167:331 */         coeff[i][j] = ((int)ArithmeticUtils.binomialCoefficient(i, j));
/* 168:    */       }
/* 169:    */     }
/* 170:336 */     for (int i = 0; i < dp1; i++) {
/* 171:337 */       newCoefficients[0] += coefficients[i] * FastMath.pow(shift, i);
/* 172:    */     }
/* 173:341 */     int d = dp1 - 1;
/* 174:342 */     for (int i = 0; i < d; i++) {
/* 175:343 */       for (int j = i; j < d; j++) {
/* 176:344 */         newCoefficients[(i + 1)] += coeff[(j + 1)][(j - i)] * coefficients[(j + 1)] * FastMath.pow(shift, j - i);
/* 177:    */       }
/* 178:    */     }
/* 179:349 */     return newCoefficients;
/* 180:    */   }
/* 181:    */   
/* 182:    */   private static PolynomialFunction buildPolynomial(int degree, List<BigFraction> coefficients, RecurrenceCoefficientsGenerator generator)
/* 183:    */   {
/* 184:363 */     int maxDegree = (int)FastMath.floor(FastMath.sqrt(2 * coefficients.size())) - 1;
/* 185:364 */     synchronized (PolynomialsUtils.class)
/* 186:    */     {
/* 187:365 */       if (degree > maxDegree) {
/* 188:366 */         computeUpToDegree(degree, maxDegree, generator, coefficients);
/* 189:    */       }
/* 190:    */     }
/* 191:378 */     int start = degree * (degree + 1) / 2;
/* 192:    */     
/* 193:380 */     double[] a = new double[degree + 1];
/* 194:381 */     for (int i = 0; i <= degree; i++) {
/* 195:382 */       a[i] = ((BigFraction)coefficients.get(start + i)).doubleValue();
/* 196:    */     }
/* 197:386 */     return new PolynomialFunction(a);
/* 198:    */   }
/* 199:    */   
/* 200:    */   private static void computeUpToDegree(int degree, int maxDegree, RecurrenceCoefficientsGenerator generator, List<BigFraction> coefficients)
/* 201:    */   {
/* 202:400 */     int startK = (maxDegree - 1) * maxDegree / 2;
/* 203:401 */     for (int k = maxDegree; k < degree; k++)
/* 204:    */     {
/* 205:404 */       int startKm1 = startK;
/* 206:405 */       startK += k;
/* 207:    */       
/* 208:    */ 
/* 209:408 */       BigFraction[] ai = generator.generate(k);
/* 210:    */       
/* 211:410 */       BigFraction ck = (BigFraction)coefficients.get(startK);
/* 212:411 */       BigFraction ckm1 = (BigFraction)coefficients.get(startKm1);
/* 213:    */       
/* 214:    */ 
/* 215:414 */       coefficients.add(ck.multiply(ai[0]).subtract(ckm1.multiply(ai[2])));
/* 216:417 */       for (int i = 1; i < k; i++)
/* 217:    */       {
/* 218:418 */         BigFraction ckPrev = ck;
/* 219:419 */         ck = (BigFraction)coefficients.get(startK + i);
/* 220:420 */         ckm1 = (BigFraction)coefficients.get(startKm1 + i);
/* 221:421 */         coefficients.add(ck.multiply(ai[0]).add(ckPrev.multiply(ai[1])).subtract(ckm1.multiply(ai[2])));
/* 222:    */       }
/* 223:425 */       BigFraction ckPrev = ck;
/* 224:426 */       ck = (BigFraction)coefficients.get(startK + k);
/* 225:427 */       coefficients.add(ck.multiply(ai[0]).add(ckPrev.multiply(ai[1])));
/* 226:    */       
/* 227:    */ 
/* 228:430 */       coefficients.add(ck.multiply(ai[1]));
/* 229:    */     }
/* 230:    */   }
/* 231:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.polynomials.PolynomialsUtils
 * JD-Core Version:    0.7.0.1
 */
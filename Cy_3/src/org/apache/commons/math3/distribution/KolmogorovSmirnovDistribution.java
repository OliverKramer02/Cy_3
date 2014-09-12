/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.math.BigDecimal;
/*   5:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   6:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   7:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.fraction.BigFraction;
/*  10:    */ import org.apache.commons.math3.fraction.BigFractionField;
/*  11:    */ import org.apache.commons.math3.fraction.FractionConversionException;
/*  12:    */ import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
/*  13:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*  14:    */ import org.apache.commons.math3.linear.FieldMatrix;
/*  15:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  16:    */ 
/*  17:    */ public class KolmogorovSmirnovDistribution
/*  18:    */   implements Serializable
/*  19:    */ {
/*  20:    */   private static final long serialVersionUID = -4670676796862967187L;
/*  21:    */   private int n;
/*  22:    */   
/*  23:    */   public KolmogorovSmirnovDistribution(int n)
/*  24:    */     throws NotStrictlyPositiveException
/*  25:    */   {
/*  26: 86 */     if (n <= 0) {
/*  27: 87 */       throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_NUMBER_OF_SAMPLES, Integer.valueOf(n));
/*  28:    */     }
/*  29: 90 */     this.n = n;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public double cdf(double d)
/*  33:    */     throws MathArithmeticException
/*  34:    */   {
/*  35:109 */     return cdf(d, false);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public double cdfExact(double d)
/*  39:    */     throws MathArithmeticException
/*  40:    */   {
/*  41:129 */     return cdf(d, true);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public double cdf(double d, boolean exact)
/*  45:    */     throws MathArithmeticException
/*  46:    */   {
/*  47:151 */     double ninv = 1.0D / this.n;
/*  48:152 */     double ninvhalf = 0.5D * ninv;
/*  49:154 */     if (d <= ninvhalf) {
/*  50:156 */       return 0.0D;
/*  51:    */     }
/*  52:158 */     if ((ninvhalf < d) && (d <= ninv))
/*  53:    */     {
/*  54:160 */       double res = 1.0D;
/*  55:161 */       double f = 2.0D * d - ninv;
/*  56:164 */       for (int i = 1; i <= this.n; i++) {
/*  57:165 */         res *= i * f;
/*  58:    */       }
/*  59:168 */       return res;
/*  60:    */     }
/*  61:170 */     if ((1.0D - ninv <= d) && (d < 1.0D)) {
/*  62:172 */       return 1.0D - 2.0D * Math.pow(1.0D - d, this.n);
/*  63:    */     }
/*  64:174 */     if (1.0D <= d) {
/*  65:176 */       return 1.0D;
/*  66:    */     }
/*  67:179 */     return exact ? exactK(d) : roundedK(d);
/*  68:    */   }
/*  69:    */   
/*  70:    */   private double exactK(double d)
/*  71:    */     throws MathArithmeticException
/*  72:    */   {
/*  73:196 */     int k = (int)Math.ceil(this.n * d);
/*  74:    */     
/*  75:198 */     FieldMatrix<BigFraction> H = createH(d);
/*  76:199 */     FieldMatrix<BigFraction> Hpower = H.power(this.n);
/*  77:    */     
/*  78:201 */     BigFraction pFrac = (BigFraction)Hpower.getEntry(k - 1, k - 1);
/*  79:203 */     for (int i = 1; i <= this.n; i++) {
/*  80:204 */       pFrac = pFrac.multiply(i).divide(this.n);
/*  81:    */     }
/*  82:212 */     return pFrac.bigDecimalValue(20, 4).doubleValue();
/*  83:    */   }
/*  84:    */   
/*  85:    */   private double roundedK(double d)
/*  86:    */     throws MathArithmeticException
/*  87:    */   {
/*  88:228 */     int k = (int)Math.ceil(this.n * d);
/*  89:229 */     FieldMatrix<BigFraction> HBigFraction = createH(d);
/*  90:230 */     int m = HBigFraction.getRowDimension();
/*  91:    */     
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:236 */     RealMatrix H = new Array2DRowRealMatrix(m, m);
/*  97:238 */     for (int i = 0; i < m; i++) {
/*  98:239 */       for (int j = 0; j < m; j++) {
/*  99:240 */         H.setEntry(i, j, ((BigFraction)HBigFraction.getEntry(i, j)).doubleValue());
/* 100:    */       }
/* 101:    */     }
/* 102:244 */     RealMatrix Hpower = H.power(this.n);
/* 103:    */     
/* 104:246 */     double pFrac = Hpower.getEntry(k - 1, k - 1);
/* 105:248 */     for (int i = 1; i <= this.n; i++) {
/* 106:249 */       pFrac *= i / this.n;
/* 107:    */     }
/* 108:252 */     return pFrac;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private FieldMatrix<BigFraction> createH(double d)
/* 112:    */     throws NumberIsTooLargeException, FractionConversionException
/* 113:    */   {
/* 114:269 */     int k = (int)Math.ceil(this.n * d);
/* 115:    */     
/* 116:271 */     int m = 2 * k - 1;
/* 117:272 */     double hDouble = k - this.n * d;
/* 118:274 */     if (hDouble >= 1.0D) {
/* 119:275 */       throw new NumberIsTooLargeException(Double.valueOf(hDouble), Double.valueOf(1.0D), false);
/* 120:    */     }
/* 121:278 */     BigFraction h = null;
/* 122:    */     try
/* 123:    */     {
/* 124:281 */       h = new BigFraction(hDouble, 1.0E-020D, 10000);
/* 125:    */     }
/* 126:    */     catch (FractionConversionException e1)
/* 127:    */     {
/* 128:    */       try
/* 129:    */       {
/* 130:284 */         h = new BigFraction(hDouble, 1.0E-010D, 10000);
/* 131:    */       }
/* 132:    */       catch (FractionConversionException e2)
/* 133:    */       {
/* 134:286 */         h = new BigFraction(hDouble, 1.E-005D, 10000);
/* 135:    */       }
/* 136:    */     }
/* 137:290 */     BigFraction[][] Hdata = new BigFraction[m][m];
/* 138:295 */     for (int i = 0; i < m; i++) {
/* 139:296 */       for (int j = 0; j < m; j++) {
/* 140:297 */         if (i - j + 1 < 0) {
/* 141:298 */           Hdata[i][j] = BigFraction.ZERO;
/* 142:    */         } else {
/* 143:300 */           Hdata[i][j] = BigFraction.ONE;
/* 144:    */         }
/* 145:    */       }
/* 146:    */     }
/* 147:309 */     BigFraction[] hPowers = new BigFraction[m];
/* 148:310 */     hPowers[0] = h;
/* 149:311 */     for (int i = 1; i < m; i++) {
/* 150:312 */       hPowers[i] = h.multiply(hPowers[(i - 1)]);
/* 151:    */     }
/* 152:318 */     for (int i = 0; i < m; i++)
/* 153:    */     {
/* 154:319 */       Hdata[i][0] = Hdata[i][0].subtract(hPowers[i]);
/* 155:320 */       Hdata[(m - 1)][i] = Hdata[(m - 1)][i].subtract(hPowers[(m - i - 1)]);
/* 156:    */     }
/* 157:328 */     if (h.compareTo(BigFraction.ONE_HALF) == 1) {
/* 158:329 */       Hdata[(m - 1)][0] = Hdata[(m - 1)][0].add(h.multiply(2).subtract(1).pow(m));
/* 159:    */     }
/* 160:343 */     for (int i = 0; i < m; i++) {
/* 161:344 */       for (int j = 0; j < i + 1; j++) {
/* 162:345 */         if (i - j + 1 > 0) {
/* 163:346 */           for (int g = 2; g <= i - j + 1; g++) {
/* 164:347 */             Hdata[i][j] = Hdata[i][j].divide(g);
/* 165:    */           }
/* 166:    */         }
/* 167:    */       }
/* 168:    */     }
/* 169:353 */     return new Array2DRowFieldMatrix(BigFractionField.getInstance(), Hdata);
/* 170:    */   }
/* 171:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.KolmogorovSmirnovDistribution
 * JD-Core Version:    0.7.0.1
 */
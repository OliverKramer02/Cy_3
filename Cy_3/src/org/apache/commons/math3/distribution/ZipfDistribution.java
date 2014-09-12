/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.util.FastMath;
/*   6:    */ 
/*   7:    */ public class ZipfDistribution
/*   8:    */   extends AbstractIntegerDistribution
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = -140627372283420404L;
/*  11:    */   private final int numberOfElements;
/*  12:    */   private final double exponent;
/*  13: 41 */   private double numericalMean = (0.0D / 0.0D);
/*  14: 44 */   private boolean numericalMeanIsCalculated = false;
/*  15: 47 */   private double numericalVariance = (0.0D / 0.0D);
/*  16: 50 */   private boolean numericalVarianceIsCalculated = false;
/*  17:    */   
/*  18:    */   public ZipfDistribution(int numberOfElements, double exponent)
/*  19:    */     throws NotStrictlyPositiveException
/*  20:    */   {
/*  21: 63 */     if (numberOfElements <= 0) {
/*  22: 64 */       throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(numberOfElements));
/*  23:    */     }
/*  24: 67 */     if (exponent <= 0.0D) {
/*  25: 68 */       throw new NotStrictlyPositiveException(LocalizedFormats.EXPONENT, Double.valueOf(exponent));
/*  26:    */     }
/*  27: 72 */     this.numberOfElements = numberOfElements;
/*  28: 73 */     this.exponent = exponent;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getNumberOfElements()
/*  32:    */   {
/*  33: 82 */     return this.numberOfElements;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public double getExponent()
/*  37:    */   {
/*  38: 91 */     return this.exponent;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double probability(int x)
/*  42:    */   {
/*  43: 96 */     if ((x <= 0) || (x > this.numberOfElements)) {
/*  44: 97 */       return 0.0D;
/*  45:    */     }
/*  46:100 */     return 1.0D / FastMath.pow(x, this.exponent) / generalizedHarmonic(this.numberOfElements, this.exponent);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double cumulativeProbability(int x)
/*  50:    */   {
/*  51:105 */     if (x <= 0) {
/*  52:106 */       return 0.0D;
/*  53:    */     }
/*  54:107 */     if (x >= this.numberOfElements) {
/*  55:108 */       return 1.0D;
/*  56:    */     }
/*  57:111 */     return generalizedHarmonic(x, this.exponent) / generalizedHarmonic(this.numberOfElements, this.exponent);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public double getNumericalMean()
/*  61:    */   {
/*  62:125 */     if (!this.numericalMeanIsCalculated)
/*  63:    */     {
/*  64:126 */       this.numericalMean = calculateNumericalMean();
/*  65:127 */       this.numericalMeanIsCalculated = true;
/*  66:    */     }
/*  67:129 */     return this.numericalMean;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected double calculateNumericalMean()
/*  71:    */   {
/*  72:138 */     int N = getNumberOfElements();
/*  73:139 */     double s = getExponent();
/*  74:    */     
/*  75:141 */     double Hs1 = generalizedHarmonic(N, s - 1.0D);
/*  76:142 */     double Hs = generalizedHarmonic(N, s);
/*  77:    */     
/*  78:144 */     return Hs1 / Hs;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public double getNumericalVariance()
/*  82:    */   {
/*  83:159 */     if (!this.numericalVarianceIsCalculated)
/*  84:    */     {
/*  85:160 */       this.numericalVariance = calculateNumericalVariance();
/*  86:161 */       this.numericalVarianceIsCalculated = true;
/*  87:    */     }
/*  88:163 */     return this.numericalVariance;
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected double calculateNumericalVariance()
/*  92:    */   {
/*  93:172 */     int N = getNumberOfElements();
/*  94:173 */     double s = getExponent();
/*  95:    */     
/*  96:175 */     double Hs2 = generalizedHarmonic(N, s - 2.0D);
/*  97:176 */     double Hs1 = generalizedHarmonic(N, s - 1.0D);
/*  98:177 */     double Hs = generalizedHarmonic(N, s);
/*  99:    */     
/* 100:179 */     return Hs2 / Hs - Hs1 * Hs1 / (Hs * Hs);
/* 101:    */   }
/* 102:    */   
/* 103:    */   private double generalizedHarmonic(int n, double m)
/* 104:    */   {
/* 105:192 */     double value = 0.0D;
/* 106:193 */     for (int k = n; k > 0; k--) {
/* 107:194 */       value += 1.0D / FastMath.pow(k, m);
/* 108:    */     }
/* 109:196 */     return value;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public int getSupportLowerBound()
/* 113:    */   {
/* 114:207 */     return 1;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public int getSupportUpperBound()
/* 118:    */   {
/* 119:218 */     return getNumberOfElements();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean isSupportConnected()
/* 123:    */   {
/* 124:229 */     return true;
/* 125:    */   }
/* 126:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.ZipfDistribution
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.MathInternalError;
/*   5:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   7:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*  11:    */ 
/*  12:    */ public abstract class AbstractIntegerDistribution
/*  13:    */   implements IntegerDistribution, Serializable
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = -1146319659338487221L;
/*  16: 44 */   protected final RandomDataImpl randomData = new RandomDataImpl();
/*  17:    */   
/*  18:    */   public double cumulativeProbability(int x0, int x1)
/*  19:    */     throws NumberIsTooLargeException
/*  20:    */   {
/*  21: 56 */     if (x1 < x0) {
/*  22: 57 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, Integer.valueOf(x0), Integer.valueOf(x1), true);
/*  23:    */     }
/*  24: 60 */     return cumulativeProbability(x1) - cumulativeProbability(x0);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int inverseCumulativeProbability(double p)
/*  28:    */     throws OutOfRangeException
/*  29:    */   {
/*  30: 75 */     if ((p < 0.0D) || (p > 1.0D)) {
/*  31: 76 */       throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
/*  32:    */     }
/*  33: 79 */     int lower = getSupportLowerBound();
/*  34: 80 */     if (p == 0.0D) {
/*  35: 81 */       return lower;
/*  36:    */     }
/*  37: 83 */     if (lower == -2147483648)
/*  38:    */     {
/*  39: 84 */       if (checkedCumulativeProbability(lower) >= p) {
/*  40: 85 */         return lower;
/*  41:    */       }
/*  42:    */     }
/*  43:    */     else {
/*  44: 88 */       lower--;
/*  45:    */     }
/*  46: 92 */     int upper = getSupportUpperBound();
/*  47: 93 */     if (p == 1.0D) {
/*  48: 94 */       return upper;
/*  49:    */     }
/*  50: 99 */     double mu = getNumericalMean();
/*  51:100 */     double sigma = FastMath.sqrt(getNumericalVariance());
/*  52:101 */     boolean chebyshevApplies = (!Double.isInfinite(mu)) && (!Double.isNaN(mu)) && (!Double.isInfinite(sigma)) && (!Double.isNaN(sigma)) && (sigma != 0.0D);
/*  53:103 */     if (chebyshevApplies)
/*  54:    */     {
/*  55:104 */       double k = FastMath.sqrt((1.0D - p) / p);
/*  56:105 */       double tmp = mu - k * sigma;
/*  57:106 */       if (tmp > lower) {
/*  58:107 */         lower = (int)Math.ceil(tmp) - 1;
/*  59:    */       }
/*  60:109 */       k = 1.0D / k;
/*  61:110 */       tmp = mu + k * sigma;
/*  62:111 */       if (tmp < upper) {
/*  63:112 */         upper = (int)Math.ceil(tmp) - 1;
/*  64:    */       }
/*  65:    */     }
/*  66:116 */     return solveInverseCumulativeProbability(p, lower, upper);
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected int solveInverseCumulativeProbability(double p, int lower, int upper)
/*  70:    */   {
/*  71:132 */     while (lower + 1 < upper)
/*  72:    */     {
/*  73:133 */       int xm = (lower + upper) / 2;
/*  74:134 */       if ((xm < lower) || (xm > upper)) {
/*  75:140 */         xm = lower + (upper - lower) / 2;
/*  76:    */       }
/*  77:143 */       double pm = checkedCumulativeProbability(xm);
/*  78:144 */       if (pm >= p) {
/*  79:145 */         upper = xm;
/*  80:    */       } else {
/*  81:147 */         lower = xm;
/*  82:    */       }
/*  83:    */     }
/*  84:150 */     return upper;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void reseedRandomGenerator(long seed)
/*  88:    */   {
/*  89:155 */     this.randomData.reSeed(seed);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public int sample()
/*  93:    */   {
/*  94:166 */     return this.randomData.nextInversionDeviate(this);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public int[] sample(int sampleSize)
/*  98:    */   {
/*  99:176 */     if (sampleSize <= 0) {
/* 100:177 */       throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
/* 101:    */     }
/* 102:180 */     int[] out = new int[sampleSize];
/* 103:181 */     for (int i = 0; i < sampleSize; i++) {
/* 104:182 */       out[i] = sample();
/* 105:    */     }
/* 106:184 */     return out;
/* 107:    */   }
/* 108:    */   
/* 109:    */   private double checkedCumulativeProbability(int argument)
/* 110:    */     throws MathInternalError
/* 111:    */   {
/* 112:200 */     double result = (0.0D / 0.0D);
/* 113:201 */     result = cumulativeProbability(argument);
/* 114:202 */     if (Double.isNaN(result)) {
/* 115:203 */       throw new MathInternalError(LocalizedFormats.DISCRETE_CUMULATIVE_PROBABILITY_RETURNED_NAN, new Object[] { Integer.valueOf(argument) });
/* 116:    */     }
/* 117:206 */     return result;
/* 118:    */   }
/* 119:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.AbstractIntegerDistribution
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.analysis.function;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class Logistic
/*  12:    */   implements DifferentiableUnivariateFunction
/*  13:    */ {
/*  14:    */   private final double a;
/*  15:    */   private final double k;
/*  16:    */   private final double b;
/*  17:    */   private final double oneOverN;
/*  18:    */   private final double q;
/*  19:    */   private final double m;
/*  20:    */   
/*  21:    */   public Logistic(double k, double m, double b, double q, double a, double n)
/*  22:    */   {
/*  23: 68 */     if (n <= 0.0D) {
/*  24: 69 */       throw new NotStrictlyPositiveException(Double.valueOf(n));
/*  25:    */     }
/*  26: 72 */     this.k = k;
/*  27: 73 */     this.m = m;
/*  28: 74 */     this.b = b;
/*  29: 75 */     this.q = q;
/*  30: 76 */     this.a = a;
/*  31: 77 */     this.oneOverN = (1.0D / n);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double value(double x)
/*  35:    */   {
/*  36: 82 */     return value(this.m - x, this.k, this.b, this.q, this.a, this.oneOverN);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public UnivariateFunction derivative()
/*  40:    */   {
return new UnivariateFunction()
/*  42:    */     {
/*  43:    */       public double value(double x)
/*  44:    */       {
/*  45: 90 */         double exp = Logistic.this.q * FastMath.exp(Logistic.this.b * (Logistic.this.m - x));
/*  46: 91 */         if (Double.isInfinite(exp)) {
/*  47: 93 */           return 0.0D;
/*  48:    */         }
/*  49: 95 */         double exp1 = exp + 1.0D;
/*  50: 96 */         return Logistic.this.b * Logistic.this.oneOverN * exp / FastMath.pow(exp1, Logistic.this.oneOverN + 1.0D);
/*  51:    */       }
/*  52:    */     };   }
/*  54:    */   
/*  55:    */   public static class Parametric
/*  56:    */     implements ParametricUnivariateFunction
/*  57:    */   {
/*  58:    */     public double value(double x, double... param)
/*  59:    */     {
/*  60:122 */       validateParameters(param);
/*  61:123 */       return Logistic.value(param[1] - x, param[0], param[2], param[3], param[4], 1.0D / param[5]);
/*  62:    */     }
/*  63:    */     
/*  64:    */     public double[] gradient(double x, double... param)
/*  65:    */     {
/*  66:143 */       validateParameters(param);
/*  67:    */       
/*  68:145 */       double b = param[2];
/*  69:146 */       double q = param[3];
/*  70:    */       
/*  71:148 */       double mMinusX = param[1] - x;
/*  72:149 */       double oneOverN = 1.0D / param[5];
/*  73:150 */       double exp = FastMath.exp(b * mMinusX);
/*  74:151 */       double qExp = q * exp;
/*  75:152 */       double qExp1 = qExp + 1.0D;
/*  76:153 */       double factor1 = (param[0] - param[4]) * oneOverN / FastMath.pow(qExp1, oneOverN);
/*  77:154 */       double factor2 = -factor1 / qExp1;
/*  78:    */       
/*  79:    */ 
/*  80:157 */       double gk = Logistic.value(mMinusX, 1.0D, b, q, 0.0D, oneOverN);
/*  81:158 */       double gm = factor2 * b * qExp;
/*  82:159 */       double gb = factor2 * mMinusX * qExp;
/*  83:160 */       double gq = factor2 * exp;
/*  84:161 */       double ga = Logistic.value(mMinusX, 0.0D, b, q, 1.0D, oneOverN);
/*  85:162 */       double gn = factor1 * Math.log(qExp1) * oneOverN;
/*  86:    */       
/*  87:164 */       return new double[] { gk, gm, gb, gq, ga, gn };
/*  88:    */     }
/*  89:    */     
/*  90:    */     private void validateParameters(double[] param)
/*  91:    */     {
/*  92:179 */       if (param == null) {
/*  93:180 */         throw new NullArgumentException();
/*  94:    */       }
/*  95:182 */       if (param.length != 6) {
/*  96:183 */         throw new DimensionMismatchException(param.length, 6);
/*  97:    */       }
/*  98:185 */       if (param[5] <= 0.0D) {
/*  99:186 */         throw new NotStrictlyPositiveException(Double.valueOf(param[5]));
/* 100:    */       }
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   private static double value(double mMinusX, double k, double b, double q, double a, double oneOverN)
/* 105:    */   {
/* 106:206 */     return a + (k - a) / FastMath.pow(1.0D + q * FastMath.exp(b * mMinusX), oneOverN);
/* 107:    */   }
/* 108:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Logistic
 * JD-Core Version:    0.7.0.1
 */
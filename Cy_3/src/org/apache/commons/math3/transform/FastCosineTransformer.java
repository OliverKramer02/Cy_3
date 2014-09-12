/*   1:    */ package org.apache.commons.math3.transform;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;

/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.complex.Complex;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.ArithmeticUtils;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*  11:    */ 
/*  12:    */ public class FastCosineTransformer
/*  13:    */   implements RealTransformer, Serializable
/*  14:    */ {
/*  15:    */   static final long serialVersionUID = 20120212L;
/*  16:    */   private final DctNormalization normalization;
/*  17:    */   
/*  18:    */   public FastCosineTransformer(DctNormalization normalization)
/*  19:    */   {
/*  20: 88 */     this.normalization = normalization;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double[] transform(double[] f, TransformType type)
/*  24:    */   {
/*  25: 98 */     if (type == TransformType.FORWARD)
/*  26:    */     {
/*  27: 99 */       if (this.normalization == DctNormalization.ORTHOGONAL_DCT_I)
/*  28:    */       {
/*  29:100 */         double s = FastMath.sqrt(2.0D / (f.length - 1));
/*  30:101 */         return TransformUtils.scaleArray(fct(f), s);
/*  31:    */       }
/*  32:103 */       return fct(f);
/*  33:    */     }
/*  34:105 */     double s2 = 2.0D / (f.length - 1);
/*  35:    */     double s1;
/*  36:    */     
/*  37:107 */     if (this.normalization == DctNormalization.ORTHOGONAL_DCT_I) {
/*  38:108 */       s1 = FastMath.sqrt(s2);
/*  39:    */     } else {
/*  40:110 */       s1 = s2;
/*  41:    */     }
/*  42:112 */     return TransformUtils.scaleArray(fct(f), s1);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double[] transform(UnivariateFunction f, double min, double max, int n, TransformType type)
/*  46:    */   {
double[] data = null;
/*  47:129 */     //double[] data = FunctionUtils.sample(f, min, max, n);
/*  48:130 */     return transform(data, type);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected double[] fct(double[] f)
/*  52:    */     throws MathIllegalArgumentException
/*  53:    */   {
/*  54:144 */     double[] transformed = new double[f.length];
/*  55:    */     
/*  56:146 */     int n = f.length - 1;
/*  57:147 */     if (!ArithmeticUtils.isPowerOfTwo(n)) {
/*  58:148 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_PLUS_ONE, new Object[] { Integer.valueOf(f.length) });
/*  59:    */     }
/*  60:152 */     if (n == 1)
/*  61:    */     {
/*  62:153 */       transformed[0] = (0.5D * (f[0] + f[1]));
/*  63:154 */       transformed[1] = (0.5D * (f[0] - f[1]));
/*  64:155 */       return transformed;
/*  65:    */     }
/*  66:159 */     double[] x = new double[n];
/*  67:160 */     x[0] = (0.5D * (f[0] + f[n]));
/*  68:161 */     x[(n >> 1)] = f[(n >> 1)];
/*  69:    */     
/*  70:163 */     double t1 = 0.5D * (f[0] - f[n]);
/*  71:164 */     for (int i = 1; i < n >> 1; i++)
/*  72:    */     {
/*  73:165 */       double a = 0.5D * (f[i] + f[(n - i)]);
/*  74:166 */       double b = FastMath.sin(i * 3.141592653589793D / n) * (f[i] - f[(n - i)]);
/*  75:167 */       double c = FastMath.cos(i * 3.141592653589793D / n) * (f[i] - f[(n - i)]);
/*  76:168 */       x[i] = (a - b);
/*  77:169 */       x[(n - i)] = (a + b);
/*  78:170 */       t1 += c;
/*  79:    */     }
/*  80:173 */     FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
/*  81:174 */     Complex[] y = transformer.transform(x, TransformType.FORWARD);
/*  82:    */     
/*  83:    */ 
/*  84:177 */     transformed[0] = y[0].getReal();
/*  85:178 */     transformed[1] = t1;
/*  86:179 */     for (int i = 1; i < n >> 1; i++)
/*  87:    */     {
/*  88:180 */       transformed[(2 * i)] = y[i].getReal();
/*  89:181 */       transformed[(2 * i + 1)] = (transformed[(2 * i - 1)] - y[i].getImaginary());
/*  90:    */     }
/*  91:183 */     transformed[n] = y[(n >> 1)].getReal();
/*  92:    */     
/*  93:185 */     return transformed;
/*  94:    */   }
/*  95:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.transform.FastCosineTransformer
 * JD-Core Version:    0.7.0.1
 */
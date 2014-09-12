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
/*  12:    */ public class FastSineTransformer
/*  13:    */   implements RealTransformer, Serializable
/*  14:    */ {
/*  15:    */   static final long serialVersionUID = 20120211L;
/*  16:    */   private final DstNormalization normalization;
/*  17:    */   
/*  18:    */   public FastSineTransformer(DstNormalization normalization)
/*  19:    */   {
/*  20: 93 */     this.normalization = normalization;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double[] transform(double[] f, TransformType type)
/*  24:    */   {
/*  25:105 */     if (this.normalization == DstNormalization.ORTHOGONAL_DST_I)
/*  26:    */     {
/*  27:106 */       double s = FastMath.sqrt(2.0D / f.length);
/*  28:107 */       return TransformUtils.scaleArray(fst(f), s);
/*  29:    */     }
/*  30:109 */     if (type == TransformType.FORWARD) {
/*  31:110 */       return fst(f);
/*  32:    */     }
/*  33:112 */     double s = 2.0D / f.length;
/*  34:113 */     return TransformUtils.scaleArray(fst(f), s);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double[] transform(UnivariateFunction f, double min, double max, int n, TransformType type)
/*  38:    */   {
/*  39:132 */     double[] data = null;
/*  40:133 */     data[0] = 0.0D;
/*  41:134 */     return transform(data, type);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected double[] fst(double[] f)
/*  45:    */     throws MathIllegalArgumentException
/*  46:    */   {
/*  47:148 */     double[] transformed = new double[f.length];
/*  48:150 */     if (!ArithmeticUtils.isPowerOfTwo(f.length)) {
/*  49:151 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, new Object[] { Integer.valueOf(f.length) });
/*  50:    */     }
/*  51:155 */     if (f[0] != 0.0D) {
/*  52:156 */       throw new MathIllegalArgumentException(LocalizedFormats.FIRST_ELEMENT_NOT_ZERO, new Object[] { Double.valueOf(f[0]) });
/*  53:    */     }
/*  54:160 */     int n = f.length;
/*  55:161 */     if (n == 1)
/*  56:    */     {
/*  57:162 */       transformed[0] = 0.0D;
/*  58:163 */       return transformed;
/*  59:    */     }
/*  60:167 */     double[] x = new double[n];
/*  61:168 */     x[0] = 0.0D;
/*  62:169 */     x[(n >> 1)] = (2.0D * f[(n >> 1)]);
/*  63:170 */     for (int i = 1; i < n >> 1; i++)
/*  64:    */     {
/*  65:171 */       double a = FastMath.sin(i * 3.141592653589793D / n) * (f[i] + f[(n - i)]);
/*  66:172 */       double b = 0.5D * (f[i] - f[(n - i)]);
/*  67:173 */       x[i] = (a + b);
/*  68:174 */       x[(n - i)] = (a - b);
/*  69:    */     }
/*  70:177 */     FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
/*  71:178 */     Complex[] y = transformer.transform(x, TransformType.FORWARD);
/*  72:    */     
/*  73:    */ 
/*  74:181 */     transformed[0] = 0.0D;
/*  75:182 */     transformed[1] = (0.5D * y[0].getReal());
/*  76:183 */     for (int i = 1; i < n >> 1; i++)
/*  77:    */     {
/*  78:184 */       transformed[(2 * i)] = (-y[i].getImaginary());
/*  79:185 */       transformed[(2 * i + 1)] = (y[i].getReal() + transformed[(2 * i - 1)]);
/*  80:    */     }
/*  81:188 */     return transformed;
/*  82:    */   }
/*  83:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.transform.FastSineTransformer
 * JD-Core Version:    0.7.0.1
 */
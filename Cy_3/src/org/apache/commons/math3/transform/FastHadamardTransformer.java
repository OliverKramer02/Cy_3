/*   1:    */ package org.apache.commons.math3.transform;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;

/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.util.ArithmeticUtils;
/*   4:    */ 
/*   9:    */ 
/*  10:    */ public class FastHadamardTransformer
/*  11:    */   implements RealTransformer, Serializable
/*  12:    */ {
/*  13:    */   static final long serialVersionUID = 20120211L;
/*  14:    */   
/*  15:    */   public double[] transform(double[] f, TransformType type)
/*  16:    */   {
/*  17: 50 */     if (type == TransformType.FORWARD) {
/*  18: 51 */       return fht(f);
/*  19:    */     }
/*  20: 53 */     return TransformUtils.scaleArray(fht(f), 1.0D / f.length);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double[] transform(UnivariateFunction f, double min, double max, int n, TransformType type)
/*  24:    */   {
	return null;
/*  25: 70 */     //return transform(FunctionUtils.sample(f, min, max, n), type);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public int[] transform(int[] f)
/*  29:    */   {
/*  30: 84 */     return fht(f);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected double[] fht(double[] x)
/*  34:    */     throws MathIllegalArgumentException
/*  35:    */   {
/*  36:232 */     int n = x.length;
/*  37:233 */     int halfN = n / 2;
/*  38:235 */     if (!ArithmeticUtils.isPowerOfTwo(n)) {
/*  39:236 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO, new Object[] { Integer.valueOf(n) });
/*  40:    */     }
/*  41:245 */     double[] yPrevious = new double[n];
/*  42:246 */     double[] yCurrent = (double[])x.clone();
/*  43:249 */     for (int j = 1; j < n; j <<= 1)
/*  44:    */     {
/*  45:252 */       double[] yTmp = yCurrent;
/*  46:253 */       yCurrent = yPrevious;
/*  47:254 */       yPrevious = yTmp;
/*  48:257 */       for (int i = 0; i < halfN; i++)
/*  49:    */       {
/*  50:259 */         int twoI = 2 * i;
/*  51:260 */         yPrevious[twoI] += yPrevious[(twoI + 1)];
/*  52:    */       }
/*  53:262 */       for (int i = halfN; i < n; i++)
/*  54:    */       {
/*  55:264 */         int twoI = 2 * i;
/*  56:265 */         yCurrent[i] = (yPrevious[(twoI - n)] - yPrevious[(twoI - n + 1)]);
/*  57:    */       }
/*  58:    */     }
/*  59:269 */     return yCurrent;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected int[] fht(int[] x)
/*  63:    */     throws MathIllegalArgumentException
/*  64:    */   {
/*  65:284 */     int n = x.length;
/*  66:285 */     int halfN = n / 2;
/*  67:287 */     if (!ArithmeticUtils.isPowerOfTwo(n)) {
/*  68:288 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO, new Object[] { Integer.valueOf(n) });
/*  69:    */     }
/*  70:297 */     int[] yPrevious = new int[n];
/*  71:298 */     int[] yCurrent = (int[])x.clone();
/*  72:301 */     for (int j = 1; j < n; j <<= 1)
/*  73:    */     {
/*  74:304 */       int[] yTmp = yCurrent;
/*  75:305 */       yCurrent = yPrevious;
/*  76:306 */       yPrevious = yTmp;
/*  77:309 */       for (int i = 0; i < halfN; i++)
/*  78:    */       {
/*  79:311 */         int twoI = 2 * i;
/*  80:312 */         yPrevious[twoI] += yPrevious[(twoI + 1)];
/*  81:    */       }
/*  82:314 */       for (int i = halfN; i < n; i++)
/*  83:    */       {
/*  84:316 */         int twoI = 2 * i;
/*  85:317 */         yCurrent[i] = (yPrevious[(twoI - n)] - yPrevious[(twoI - n + 1)]);
/*  86:    */       }
/*  87:    */     }
/*  88:322 */     return yCurrent;
/*  89:    */   }
/*  90:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.transform.FastHadamardTransformer
 * JD-Core Version:    0.7.0.1
 */
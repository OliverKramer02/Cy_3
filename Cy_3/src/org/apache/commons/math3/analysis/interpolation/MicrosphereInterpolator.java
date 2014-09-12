/*  1:   */ package org.apache.commons.math3.analysis.interpolation;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*  4:   */ import org.apache.commons.math3.exception.NotPositiveException;
/*  5:   */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*  6:   */ import org.apache.commons.math3.random.UnitSphereRandomVectorGenerator;
/*  7:   */ 
/*  8:   */ public class MicrosphereInterpolator
/*  9:   */   implements MultivariateInterpolator
/* 10:   */ {
/* 11:   */   public static final int DEFAULT_MICROSPHERE_ELEMENTS = 2000;
/* 12:   */   public static final int DEFAULT_BRIGHTNESS_EXPONENT = 2;
/* 13:   */   private final int microsphereElements;
/* 14:   */   private final int brightnessExponent;
/* 15:   */   
/* 16:   */   public MicrosphereInterpolator()
/* 17:   */   {
/* 18:60 */     this(2000, 2);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public MicrosphereInterpolator(int elements, int exponent)
/* 22:   */   {
/* 23:72 */     if (exponent < 0) {
/* 24:73 */       throw new NotPositiveException(Integer.valueOf(exponent));
/* 25:   */     }
/* 26:75 */     if (elements <= 0) {
/* 27:76 */       throw new NotStrictlyPositiveException(Integer.valueOf(elements));
/* 28:   */     }
/* 29:79 */     this.microsphereElements = elements;
/* 30:80 */     this.brightnessExponent = exponent;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public MultivariateFunction interpolate(double[][] xval, double[] yval)
/* 34:   */   {
/* 35:88 */     UnitSphereRandomVectorGenerator rand = new UnitSphereRandomVectorGenerator(xval[0].length);
/* 36:   */     
/* 37:90 */     return new MicrosphereInterpolatingFunction(xval, yval, this.brightnessExponent, this.microsphereElements, rand);
/* 38:   */   }
/* 39:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.MicrosphereInterpolator
 * JD-Core Version:    0.7.0.1
 */
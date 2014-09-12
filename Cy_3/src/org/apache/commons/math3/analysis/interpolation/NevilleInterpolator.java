/*  1:   */ package org.apache.commons.math3.analysis.interpolation;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
/*  5:   */ 
/*  6:   */ public class NevilleInterpolator
/*  7:   */   implements UnivariateInterpolator, Serializable
/*  8:   */ {
/*  9:   */   static final long serialVersionUID = 3003707660147873733L;
/* 10:   */   
/* 11:   */   public PolynomialFunctionLagrangeForm interpolate(double[] x, double[] y)
/* 12:   */   {
/* 13:55 */     return new PolynomialFunctionLagrangeForm(x, y);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.NevilleInterpolator
 * JD-Core Version:    0.7.0.1
 */
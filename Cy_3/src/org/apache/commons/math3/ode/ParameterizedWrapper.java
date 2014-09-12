/*  1:   */ package org.apache.commons.math3.ode;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Collection;
/*  5:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ 
/*  8:   */ class ParameterizedWrapper
/*  9:   */   implements ParameterizedODE
/* 10:   */ {
/* 11:   */   private final FirstOrderDifferentialEquations fode;
/* 12:   */   
/* 13:   */   public ParameterizedWrapper(FirstOrderDifferentialEquations ode)
/* 14:   */   {
/* 15:40 */     this.fode = ode;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public int getDimension()
/* 19:   */   {
/* 20:47 */     return this.fode.getDimension();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void computeDerivatives(double t, double[] y, double[] yDot)
/* 24:   */   {
/* 25:56 */     this.fode.computeDerivatives(t, y, yDot);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Collection<String> getParametersNames()
/* 29:   */   {
/* 30:61 */     return new ArrayList();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public boolean isSupported(String name)
/* 34:   */   {
/* 35:66 */     return false;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public double getParameter(String name)
/* 39:   */     throws MathIllegalArgumentException
/* 40:   */   {
/* 41:72 */     if (!isSupported(name)) {
/* 42:73 */       throw new MathIllegalArgumentException(LocalizedFormats.UNKNOWN_PARAMETER, new Object[] { name });
/* 43:   */     }
/* 44:75 */     return (0.0D / 0.0D);
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void setParameter(String name, double value) {}
/* 48:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ParameterizedWrapper
 * JD-Core Version:    0.7.0.1
 */
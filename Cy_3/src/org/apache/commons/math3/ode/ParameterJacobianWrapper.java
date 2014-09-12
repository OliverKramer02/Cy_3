/*  1:   */ package org.apache.commons.math3.ode;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import java.util.Map;
/*  6:   */ 
/*  7:   */ class ParameterJacobianWrapper
/*  8:   */   implements ParameterJacobianProvider
/*  9:   */ {
/* 10:   */   private final FirstOrderDifferentialEquations fode;
/* 11:   */   private final ParameterizedODE pode;
/* 12:   */   private final Map<String, Double> hParam;
/* 13:   */   
/* 14:   */   public ParameterJacobianWrapper(FirstOrderDifferentialEquations fode, ParameterizedODE pode, ParameterConfiguration[] paramsAndSteps)
/* 15:   */   {
/* 16:49 */     this.fode = fode;
/* 17:50 */     this.pode = pode;
/* 18:51 */     this.hParam = new HashMap();
/* 19:54 */     for (ParameterConfiguration param : paramsAndSteps)
/* 20:   */     {
/* 21:55 */       String name = param.getParameterName();
/* 22:56 */       if (pode.isSupported(name)) {
/* 23:57 */         this.hParam.put(name, Double.valueOf(param.getHP()));
/* 24:   */       }
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Collection<String> getParametersNames()
/* 29:   */   {
/* 30:64 */     return this.pode.getParametersNames();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public boolean isSupported(String name)
/* 34:   */   {
/* 35:69 */     return this.pode.isSupported(name);
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void computeParameterJacobian(double t, double[] y, double[] yDot, String paramName, double[] dFdP)
/* 39:   */   {
/* 40:76 */     int n = this.fode.getDimension();
/* 41:77 */     double[] tmpDot = new double[n];
/* 42:   */     
/* 43:   */ 
/* 44:80 */     double p = this.pode.getParameter(paramName);
/* 45:81 */     double hP = ((Double)this.hParam.get(paramName)).doubleValue();
/* 46:82 */     this.pode.setParameter(paramName, p + hP);
/* 47:83 */     this.fode.computeDerivatives(t, y, tmpDot);
/* 48:84 */     for (int i = 0; i < n; i++) {
/* 49:85 */       dFdP[i] = ((tmpDot[i] - yDot[i]) / hP);
/* 50:   */     }
/* 51:87 */     this.pode.setParameter(paramName, p);
/* 52:   */   }
/* 53:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ParameterJacobianWrapper
 * JD-Core Version:    0.7.0.1
 */
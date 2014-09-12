/*  1:   */ package org.apache.commons.math3.ode;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ 
/*  5:   */ class ParameterConfiguration
/*  6:   */   implements Serializable
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 2247518849090889379L;
/*  9:   */   private String parameterName;
/* 10:   */   private double hP;
/* 11:   */   
/* 12:   */   public ParameterConfiguration(String parameterName, double hP)
/* 13:   */   {
/* 14:42 */     this.parameterName = parameterName;
/* 15:43 */     this.hP = hP;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String getParameterName()
/* 19:   */   {
/* 20:50 */     return this.parameterName;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public double getHP()
/* 24:   */   {
/* 25:57 */     return this.hP;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setHP(double hParam)
/* 29:   */   {
/* 30:64 */     this.hP = hParam;
/* 31:   */   }
/* 32:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ParameterConfiguration
 * JD-Core Version:    0.7.0.1
 */
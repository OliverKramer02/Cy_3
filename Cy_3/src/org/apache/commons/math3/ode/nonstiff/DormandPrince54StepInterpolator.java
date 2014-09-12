/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.ode.AbstractIntegrator;
/*   4:    */ import org.apache.commons.math3.ode.EquationsMapper;
/*   5:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   6:    */ 
/*   7:    */ class DormandPrince54StepInterpolator
/*   8:    */   extends RungeKuttaStepInterpolator
/*   9:    */ {
/*  10:    */   private static final double A70 = 0.09114583333333333D;
/*  11:    */   private static final double A72 = 0.4492362982929021D;
/*  12:    */   private static final double A73 = 0.6510416666666666D;
/*  13:    */   private static final double A74 = -0.322376179245283D;
/*  14:    */   private static final double A75 = 0.130952380952381D;
/*  15:    */   private static final double D0 = -1.127017565386284D;
/*  16:    */   private static final double D2 = 2.675424484351598D;
/*  17:    */   private static final double D3 = -5.685526961588504D;
/*  18:    */   private static final double D4 = 3.521932367920791D;
/*  19:    */   private static final double D5 = -1.767281257075746D;
/*  20:    */   private static final double D6 = 2.382468931778144D;
/*  21:    */   private static final long serialVersionUID = 20111120L;
/*  22:    */   private double[] v1;
/*  23:    */   private double[] v2;
/*  24:    */   private double[] v3;
/*  25:    */   private double[] v4;
/*  26:    */   private boolean vectorsInitialized;
/*  27:    */   
/*  28:    */   public DormandPrince54StepInterpolator()
/*  29:    */   {
/*  30:103 */     this.v1 = null;
/*  31:104 */     this.v2 = null;
/*  32:105 */     this.v3 = null;
/*  33:106 */     this.v4 = null;
/*  34:107 */     this.vectorsInitialized = false;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public DormandPrince54StepInterpolator(DormandPrince54StepInterpolator interpolator)
/*  38:    */   {
/*  39:117 */     super(interpolator);
/*  40:119 */     if (interpolator.v1 == null)
/*  41:    */     {
/*  42:121 */       this.v1 = null;
/*  43:122 */       this.v2 = null;
/*  44:123 */       this.v3 = null;
/*  45:124 */       this.v4 = null;
/*  46:125 */       this.vectorsInitialized = false;
/*  47:    */     }
/*  48:    */     else
/*  49:    */     {
/*  50:129 */       this.v1 = ((double[])interpolator.v1.clone());
/*  51:130 */       this.v2 = ((double[])interpolator.v2.clone());
/*  52:131 */       this.v3 = ((double[])interpolator.v3.clone());
/*  53:132 */       this.v4 = ((double[])interpolator.v4.clone());
/*  54:133 */       this.vectorsInitialized = interpolator.vectorsInitialized;
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected StepInterpolator doCopy()
/*  59:    */   {
/*  60:142 */     return new DormandPrince54StepInterpolator(this);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void reinitialize(AbstractIntegrator integrator, double[] y, double[][] yDotK, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers)
/*  64:    */   {
/*  65:152 */     super.reinitialize(integrator, y, yDotK, forward, primaryMapper, secondaryMappers);
/*  66:153 */     this.v1 = null;
/*  67:154 */     this.v2 = null;
/*  68:155 */     this.v3 = null;
/*  69:156 */     this.v4 = null;
/*  70:157 */     this.vectorsInitialized = false;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void storeTime(double t)
/*  74:    */   {
/*  75:163 */     super.storeTime(t);
/*  76:164 */     this.vectorsInitialized = false;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/*  80:    */   {
/*  81:172 */     if (!this.vectorsInitialized)
/*  82:    */     {
/*  83:174 */       if (this.v1 == null)
/*  84:    */       {
/*  85:175 */         this.v1 = new double[this.interpolatedState.length];
/*  86:176 */         this.v2 = new double[this.interpolatedState.length];
/*  87:177 */         this.v3 = new double[this.interpolatedState.length];
/*  88:178 */         this.v4 = new double[this.interpolatedState.length];
/*  89:    */       }
/*  90:184 */       for (int i = 0; i < this.interpolatedState.length; i++)
/*  91:    */       {
/*  92:185 */         double yDot0 = this.yDotK[0][i];
/*  93:186 */         double yDot2 = this.yDotK[2][i];
/*  94:187 */         double yDot3 = this.yDotK[3][i];
/*  95:188 */         double yDot4 = this.yDotK[4][i];
/*  96:189 */         double yDot5 = this.yDotK[5][i];
/*  97:190 */         double yDot6 = this.yDotK[6][i];
/*  98:191 */         this.v1[i] = (0.09114583333333333D * yDot0 + 0.4492362982929021D * yDot2 + 0.6510416666666666D * yDot3 + -0.322376179245283D * yDot4 + 0.130952380952381D * yDot5);
/*  99:192 */         this.v2[i] = (yDot0 - this.v1[i]);
/* 100:193 */         this.v3[i] = (this.v1[i] - this.v2[i] - yDot6);
/* 101:194 */         this.v4[i] = (-1.127017565386284D * yDot0 + 2.675424484351598D * yDot2 + -5.685526961588504D * yDot3 + 3.521932367920791D * yDot4 + -1.767281257075746D * yDot5 + 2.382468931778144D * yDot6);
/* 102:    */       }
/* 103:197 */       this.vectorsInitialized = true;
/* 104:    */     }
/* 105:202 */     double eta = 1.0D - theta;
/* 106:203 */     double twoTheta = 2.0D * theta;
/* 107:204 */     double dot2 = 1.0D - twoTheta;
/* 108:205 */     double dot3 = theta * (2.0D - 3.0D * theta);
/* 109:206 */     double dot4 = twoTheta * (1.0D + theta * (twoTheta - 3.0D));
/* 110:207 */     if ((this.previousState != null) && (theta <= 0.5D)) {
/* 111:208 */       for (int i = 0; i < this.interpolatedState.length; i++)
/* 112:    */       {
/* 113:209 */         this.interpolatedState[i] = (this.previousState[i] + theta * this.h * (this.v1[i] + eta * (this.v2[i] + theta * (this.v3[i] + eta * this.v4[i]))));
/* 114:    */         
/* 115:211 */         this.interpolatedDerivatives[i] = (this.v1[i] + dot2 * this.v2[i] + dot3 * this.v3[i] + dot4 * this.v4[i]);
/* 116:    */       }
/* 117:    */     } else {
/* 118:214 */       for (int i = 0; i < this.interpolatedState.length; i++)
/* 119:    */       {
/* 120:215 */         this.interpolatedState[i] = (this.currentState[i] - oneMinusThetaH * (this.v1[i] - theta * (this.v2[i] + theta * (this.v3[i] + eta * this.v4[i]))));
/* 121:    */         
/* 122:217 */         this.interpolatedDerivatives[i] = (this.v1[i] + dot2 * this.v2[i] + dot3 * this.v3[i] + dot4 * this.v4[i]);
/* 123:    */       }
/* 124:    */     }
/* 125:    */   }
/* 126:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.DormandPrince54StepInterpolator
 * JD-Core Version:    0.7.0.1
 */
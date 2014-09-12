/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   5:    */ import org.apache.commons.math3.ode.AbstractIntegrator;
/*   6:    */ import org.apache.commons.math3.ode.ExpandableStatefulODE;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public abstract class RungeKuttaIntegrator
/*  10:    */   extends AbstractIntegrator
/*  11:    */ {
/*  12:    */   private final double[] c;
/*  13:    */   private final double[][] a;
/*  14:    */   private final double[] b;
/*  15:    */   private final RungeKuttaStepInterpolator prototype;
/*  16:    */   private final double step;
/*  17:    */   
/*  18:    */   protected RungeKuttaIntegrator(String name, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double step)
/*  19:    */   {
/*  20: 83 */     super(name);
/*  21: 84 */     this.c = c;
/*  22: 85 */     this.a = a;
/*  23: 86 */     this.b = b;
/*  24: 87 */     this.prototype = prototype;
/*  25: 88 */     this.step = FastMath.abs(step);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void integrate(ExpandableStatefulODE equations, double t)
/*  29:    */     throws MathIllegalStateException, MathIllegalArgumentException
/*  30:    */   {
/*  31: 96 */     sanityChecks(equations, t);
/*  32: 97 */     setEquations(equations);
/*  33: 98 */     boolean forward = t > equations.getTime();
/*  34:    */     
/*  35:    */ 
/*  36:101 */     double[] y0 = equations.getCompleteState();
/*  37:102 */     double[] y = (double[])y0.clone();
/*  38:103 */     int stages = this.c.length + 1;
/*  39:104 */     double[][] yDotK = new double[stages][];
/*  40:105 */     for (int i = 0; i < stages; i++) {
/*  41:106 */       yDotK[i] = new double[y0.length];
/*  42:    */     }
/*  43:108 */     double[] yTmp = (double[])y0.clone();
/*  44:109 */     double[] yDotTmp = new double[y0.length];
/*  45:    */     
/*  46:    */ 
/*  47:112 */     RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator)this.prototype.copy();
/*  48:113 */     interpolator.reinitialize(this, yTmp, yDotK, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
/*  49:    */     
/*  50:115 */     interpolator.storeTime(equations.getTime());
/*  51:    */     
/*  52:    */ 
/*  53:118 */     this.stepStart = equations.getTime();
/*  54:119 */     this.stepSize = (forward ? this.step : -this.step);
/*  55:120 */     initIntegration(equations.getTime(), y0, t);
/*  56:    */     
/*  57:    */ 
/*  58:123 */     this.isLastStep = false;
/*  59:    */     do
/*  60:    */     {
/*  61:126 */       interpolator.shift();
/*  62:    */       
/*  63:    */ 
/*  64:129 */       computeDerivatives(this.stepStart, y, yDotK[0]);
/*  65:132 */       for (int k = 1; k < stages; k++)
/*  66:    */       {
/*  67:134 */         for (int j = 0; j < y0.length; j++)
/*  68:    */         {
/*  69:135 */           double sum = this.a[(k - 1)][0] * yDotK[0][j];
/*  70:136 */           for (int l = 1; l < k; l++) {
/*  71:137 */             sum += this.a[(k - 1)][l] * yDotK[l][j];
/*  72:    */           }
/*  73:139 */           y[j] += this.stepSize * sum;
/*  74:    */         }
/*  75:142 */         computeDerivatives(this.stepStart + this.c[(k - 1)] * this.stepSize, yTmp, yDotK[k]);
/*  76:    */       }
/*  77:147 */       for (int j = 0; j < y0.length; j++)
/*  78:    */       {
/*  79:148 */         double sum = this.b[0] * yDotK[0][j];
/*  80:149 */         for (int l = 1; l < stages; l++) {
/*  81:150 */           sum += this.b[l] * yDotK[l][j];
/*  82:    */         }
/*  83:152 */         y[j] += this.stepSize * sum;
/*  84:    */       }
/*  85:156 */       interpolator.storeTime(this.stepStart + this.stepSize);
/*  86:157 */       System.arraycopy(yTmp, 0, y, 0, y0.length);
/*  87:158 */       System.arraycopy(yDotK[(stages - 1)], 0, yDotTmp, 0, y0.length);
/*  88:159 */       this.stepStart = acceptStep(interpolator, y, yDotTmp, t);
/*  89:161 */       if (!this.isLastStep)
/*  90:    */       {
/*  91:164 */         interpolator.storeTime(this.stepStart);
/*  92:    */         
/*  93:    */ 
/*  94:167 */         double nextT = this.stepStart + this.stepSize;
/*  95:168 */         boolean nextIsLast = nextT >= t;
/*  96:169 */         if (nextIsLast) {
/*  97:170 */           this.stepSize = (t - this.stepStart);
/*  98:    */         }
/*  99:    */       }
/* 100:174 */     } while (!this.isLastStep);
/* 101:177 */     equations.setTime(this.stepStart);
/* 102:178 */     equations.setCompleteState(y);
/* 103:    */     
/* 104:180 */     this.stepStart = (0.0D / 0.0D);
/* 105:181 */     this.stepSize = (0.0D / 0.0D);
/* 106:    */   }
/* 107:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator
 * JD-Core Version:    0.7.0.1
 */
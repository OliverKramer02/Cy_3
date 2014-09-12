/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInput;
/*   5:    */ import java.io.ObjectOutput;
/*   6:    */ import org.apache.commons.math3.ode.AbstractIntegrator;
/*   7:    */ import org.apache.commons.math3.ode.EquationsMapper;
/*   8:    */ import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
/*   9:    */ 
/*  10:    */ abstract class RungeKuttaStepInterpolator
/*  11:    */   extends AbstractStepInterpolator
/*  12:    */ {
/*  13:    */   protected double[] previousState;
/*  14:    */   protected double[][] yDotK;
/*  15:    */   protected AbstractIntegrator integrator;
/*  16:    */   
/*  17:    */   protected RungeKuttaStepInterpolator()
/*  18:    */   {
/*  19: 61 */     this.previousState = null;
/*  20: 62 */     this.yDotK = ((double[][])null);
/*  21: 63 */     this.integrator = null;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public RungeKuttaStepInterpolator(RungeKuttaStepInterpolator interpolator)
/*  25:    */   {
/*  26: 85 */     super(interpolator);
/*  27: 87 */     if (interpolator.currentState != null)
/*  28:    */     {
/*  29: 89 */       this.previousState = ((double[])interpolator.previousState.clone());
/*  30:    */       
/*  31: 91 */       this.yDotK = new double[interpolator.yDotK.length][];
/*  32: 92 */       for (int k = 0; k < interpolator.yDotK.length; k++) {
/*  33: 93 */         this.yDotK[k] = ((double[])interpolator.yDotK[k].clone());
/*  34:    */       }
/*  35:    */     }
/*  36:    */     else
/*  37:    */     {
/*  38: 97 */       this.previousState = null;
/*  39: 98 */       this.yDotK = ((double[][])null);
/*  40:    */     }
/*  41:103 */     this.integrator = null;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void reinitialize(AbstractIntegrator rkIntegrator, double[] y, double[][] yDotArray, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers)
/*  45:    */   {
/*  46:134 */     reinitialize(y, forward, primaryMapper, secondaryMappers);
/*  47:135 */     this.previousState = null;
/*  48:136 */     this.yDotK = yDotArray;
/*  49:137 */     this.integrator = rkIntegrator;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void shift()
/*  53:    */   {
/*  54:143 */     this.previousState = ((double[])this.currentState.clone());
/*  55:144 */     super.shift();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void writeExternal(ObjectOutput out)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61:153 */     writeBaseExternal(out);
/*  62:    */     
/*  63:    */ 
/*  64:156 */     int n = this.currentState == null ? -1 : this.currentState.length;
/*  65:157 */     for (int i = 0; i < n; i++) {
/*  66:158 */       out.writeDouble(this.previousState[i]);
/*  67:    */     }
/*  68:161 */     int kMax = this.yDotK == null ? -1 : this.yDotK.length;
/*  69:162 */     out.writeInt(kMax);
/*  70:163 */     for (int k = 0; k < kMax; k++) {
/*  71:164 */       for (int i = 0; i < n; i++) {
/*  72:165 */         out.writeDouble(this.yDotK[k][i]);
/*  73:    */       }
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void readExternal(ObjectInput in)
/*  78:    */     throws IOException, ClassNotFoundException
/*  79:    */   {
/*  80:179 */     double t = readBaseExternal(in);
/*  81:    */     
/*  82:    */ 
/*  83:182 */     int n = this.currentState == null ? -1 : this.currentState.length;
/*  84:183 */     if (n < 0)
/*  85:    */     {
/*  86:184 */       this.previousState = null;
/*  87:    */     }
/*  88:    */     else
/*  89:    */     {
/*  90:186 */       this.previousState = new double[n];
/*  91:187 */       for (int i = 0; i < n; i++) {
/*  92:188 */         this.previousState[i] = in.readDouble();
/*  93:    */       }
/*  94:    */     }
/*  95:192 */     int kMax = in.readInt();
/*  96:193 */     this.yDotK = (kMax < 0 ? (double[][])null : new double[kMax][]);
/*  97:194 */     for (int k = 0; k < kMax; k++)
/*  98:    */     {
/*  99:195 */       this.yDotK[k] = (n < 0 ? null : new double[n]);
/* 100:196 */       for (int i = 0; i < n; i++) {
/* 101:197 */         this.yDotK[k][i] = in.readDouble();
/* 102:    */       }
/* 103:    */     }
/* 104:201 */     this.integrator = null;
/* 105:203 */     if (this.currentState != null) {
/* 106:205 */       setInterpolatedTime(t);
/* 107:    */     } else {
/* 108:207 */       this.interpolatedTime = t;
/* 109:    */     }
/* 110:    */   }
/* 111:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
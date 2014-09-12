/*   1:    */ package org.apache.commons.math3.ode.sampling;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInput;
/*   5:    */ import java.io.ObjectOutput;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   8:    */ import org.apache.commons.math3.ode.EquationsMapper;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class NordsieckStepInterpolator
/*  12:    */   extends AbstractStepInterpolator
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = -7179861704951334960L;
/*  15:    */   protected double[] stateVariation;
/*  16:    */   private double scalingH;
/*  17:    */   private double referenceTime;
/*  18:    */   private double[] scaled;
/*  19:    */   private Array2DRowRealMatrix nordsieck;
/*  20:    */   
/*  21:    */   public NordsieckStepInterpolator() {}
/*  22:    */   
/*  23:    */   public NordsieckStepInterpolator(NordsieckStepInterpolator interpolator)
/*  24:    */   {
/*  25: 82 */     super(interpolator);
/*  26: 83 */     this.scalingH = interpolator.scalingH;
/*  27: 84 */     this.referenceTime = interpolator.referenceTime;
/*  28: 85 */     if (interpolator.scaled != null) {
/*  29: 86 */       this.scaled = ((double[])interpolator.scaled.clone());
/*  30:    */     }
/*  31: 88 */     if (interpolator.nordsieck != null) {
/*  32: 89 */       this.nordsieck = new Array2DRowRealMatrix(interpolator.nordsieck.getDataRef(), true);
/*  33:    */     }
/*  34: 91 */     if (interpolator.stateVariation != null) {
/*  35: 92 */       this.stateVariation = ((double[])interpolator.stateVariation.clone());
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected StepInterpolator doCopy()
/*  40:    */   {
/*  41: 99 */     return new NordsieckStepInterpolator(this);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void reinitialize(double[] y, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers)
/*  45:    */   {
/*  46:115 */     super.reinitialize(y, forward, primaryMapper, secondaryMappers);
/*  47:116 */     this.stateVariation = new double[y.length];
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void reinitialize(double time, double stepSize, double[] scaledDerivative, Array2DRowRealMatrix nordsieckVector)
/*  51:    */   {
/*  52:132 */     this.referenceTime = time;
/*  53:133 */     this.scalingH = stepSize;
/*  54:134 */     this.scaled = scaledDerivative;
/*  55:135 */     this.nordsieck = nordsieckVector;
/*  56:    */     
/*  57:    */ 
/*  58:138 */     setInterpolatedTime(getInterpolatedTime());
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void rescale(double stepSize)
/*  62:    */   {
/*  63:149 */     double ratio = stepSize / this.scalingH;
/*  64:150 */     for (int i = 0; i < this.scaled.length; i++) {
/*  65:151 */       this.scaled[i] *= ratio;
/*  66:    */     }
/*  67:154 */     double[][] nData = this.nordsieck.getDataRef();
/*  68:155 */     double power = ratio;
/*  69:156 */     for (int i = 0; i < nData.length; i++)
/*  70:    */     {
/*  71:157 */       power *= ratio;
/*  72:158 */       double[] nDataI = nData[i];
/*  73:159 */       for (int j = 0; j < nDataI.length; j++) {
/*  74:160 */         nDataI[j] *= power;
/*  75:    */       }
/*  76:    */     }
/*  77:164 */     this.scalingH = stepSize;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public double[] getInterpolatedStateVariation()
/*  81:    */   {
/*  82:182 */     getInterpolatedState();
/*  83:183 */     return this.stateVariation;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/*  87:    */   {
/*  88:190 */     double x = this.interpolatedTime - this.referenceTime;
/*  89:191 */     double normalizedAbscissa = x / this.scalingH;
/*  90:    */     
/*  91:193 */     Arrays.fill(this.stateVariation, 0.0D);
/*  92:194 */     Arrays.fill(this.interpolatedDerivatives, 0.0D);
/*  93:    */     
/*  94:    */ 
/*  95:    */ 
/*  96:198 */     double[][] nData = this.nordsieck.getDataRef();
/*  97:199 */     for (int i = nData.length - 1; i >= 0; i--)
/*  98:    */     {
/*  99:200 */       int order = i + 2;
/* 100:201 */       double[] nDataI = nData[i];
/* 101:202 */       double power = FastMath.pow(normalizedAbscissa, order);
/* 102:203 */       for (int j = 0; j < nDataI.length; j++)
/* 103:    */       {
/* 104:204 */         double d = nDataI[j] * power;
/* 105:205 */         this.stateVariation[j] += d;
/* 106:206 */         this.interpolatedDerivatives[j] += order * d;
/* 107:    */       }
/* 108:    */     }
/* 109:210 */     for (int j = 0; j < this.currentState.length; j++)
/* 110:    */     {
/* 111:211 */       this.stateVariation[j] += this.scaled[j] * normalizedAbscissa;
/* 112:212 */       this.interpolatedState[j] = (this.currentState[j] + this.stateVariation[j]);
/* 113:213 */       this.interpolatedDerivatives[j] = ((this.interpolatedDerivatives[j] + this.scaled[j] * normalizedAbscissa) / x);
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void writeExternal(ObjectOutput out)
/* 118:    */     throws IOException
/* 119:    */   {
/* 120:225 */     writeBaseExternal(out);
/* 121:    */     
/* 122:    */ 
/* 123:228 */     out.writeDouble(this.scalingH);
/* 124:229 */     out.writeDouble(this.referenceTime);
/* 125:    */     
/* 126:231 */     int n = this.currentState == null ? -1 : this.currentState.length;
/* 127:232 */     if (this.scaled == null)
/* 128:    */     {
/* 129:233 */       out.writeBoolean(false);
/* 130:    */     }
/* 131:    */     else
/* 132:    */     {
/* 133:235 */       out.writeBoolean(true);
/* 134:236 */       for (int j = 0; j < n; j++) {
/* 135:237 */         out.writeDouble(this.scaled[j]);
/* 136:    */       }
/* 137:    */     }
/* 138:241 */     if (this.nordsieck == null)
/* 139:    */     {
/* 140:242 */       out.writeBoolean(false);
/* 141:    */     }
/* 142:    */     else
/* 143:    */     {
/* 144:244 */       out.writeBoolean(true);
/* 145:245 */       out.writeObject(this.nordsieck);
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void readExternal(ObjectInput in)
/* 150:    */     throws IOException, ClassNotFoundException
/* 151:    */   {
/* 152:258 */     double t = readBaseExternal(in);
/* 153:    */     
/* 154:    */ 
/* 155:261 */     this.scalingH = in.readDouble();
/* 156:262 */     this.referenceTime = in.readDouble();
/* 157:    */     
/* 158:264 */     int n = this.currentState == null ? -1 : this.currentState.length;
/* 159:265 */     boolean hasScaled = in.readBoolean();
/* 160:266 */     if (hasScaled)
/* 161:    */     {
/* 162:267 */       this.scaled = new double[n];
/* 163:268 */       for (int j = 0; j < n; j++) {
/* 164:269 */         this.scaled[j] = in.readDouble();
/* 165:    */       }
/* 166:    */     }
/* 167:    */     else
/* 168:    */     {
/* 169:272 */       this.scaled = null;
/* 170:    */     }
/* 171:275 */     boolean hasNordsieck = in.readBoolean();
/* 172:276 */     if (hasNordsieck) {
/* 173:277 */       this.nordsieck = ((Array2DRowRealMatrix)in.readObject());
/* 174:    */     } else {
/* 175:279 */       this.nordsieck = null;
/* 176:    */     }
/* 177:282 */     if ((hasScaled) && (hasNordsieck))
/* 178:    */     {
/* 179:284 */       this.stateVariation = new double[n];
/* 180:285 */       setInterpolatedTime(t);
/* 181:    */     }
/* 182:    */     else
/* 183:    */     {
/* 184:287 */       this.stateVariation = null;
/* 185:    */     }
/* 186:    */   }
/* 187:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
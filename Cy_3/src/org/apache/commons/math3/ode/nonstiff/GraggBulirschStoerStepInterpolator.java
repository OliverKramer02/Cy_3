/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInput;
/*   5:    */ import java.io.ObjectOutput;
/*   6:    */ import org.apache.commons.math3.ode.EquationsMapper;
/*   7:    */ import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
/*   8:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ class GraggBulirschStoerStepInterpolator
/*  12:    */   extends AbstractStepInterpolator
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = 20110928L;
/*  15:    */   private double[] y0Dot;
/*  16:    */   private double[] y1;
/*  17:    */   private double[] y1Dot;
/*  18:    */   private double[][] yMidDots;
/*  19:    */   private double[][] polynomials;
/*  20:    */   private double[] errfac;
/*  21:    */   private int currentDegree;
/*  22:    */   
/*  23:    */   public GraggBulirschStoerStepInterpolator()
/*  24:    */   {
/*  25:112 */     this.y0Dot = null;
/*  26:113 */     this.y1 = null;
/*  27:114 */     this.y1Dot = null;
/*  28:115 */     this.yMidDots = ((double[][])null);
/*  29:116 */     resetTables(-1);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public GraggBulirschStoerStepInterpolator(double[] y, double[] y0Dot, double[] y1, double[] y1Dot, double[][] yMidDots, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers)
/*  33:    */   {
/*  34:140 */     super(y, forward, primaryMapper, secondaryMappers);
/*  35:141 */     this.y0Dot = y0Dot;
/*  36:142 */     this.y1 = y1;
/*  37:143 */     this.y1Dot = y1Dot;
/*  38:144 */     this.yMidDots = yMidDots;
/*  39:    */     
/*  40:146 */     resetTables(yMidDots.length + 4);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public GraggBulirschStoerStepInterpolator(GraggBulirschStoerStepInterpolator interpolator)
/*  44:    */   {
/*  45:158 */     super(interpolator);
/*  46:    */     
/*  47:160 */     int dimension = this.currentState.length;
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51:164 */     this.y0Dot = null;
/*  52:165 */     this.y1 = null;
/*  53:166 */     this.y1Dot = null;
/*  54:167 */     this.yMidDots = ((double[][])null);
/*  55:170 */     if (interpolator.polynomials == null)
/*  56:    */     {
/*  57:171 */       this.polynomials = ((double[][])null);
/*  58:172 */       this.currentDegree = -1;
/*  59:    */     }
/*  60:    */     else
/*  61:    */     {
/*  62:174 */       resetTables(interpolator.currentDegree);
/*  63:175 */       for (int i = 0; i < this.polynomials.length; i++)
/*  64:    */       {
/*  65:176 */         this.polynomials[i] = new double[dimension];
/*  66:177 */         System.arraycopy(interpolator.polynomials[i], 0, this.polynomials[i], 0, dimension);
/*  67:    */       }
/*  68:180 */       this.currentDegree = interpolator.currentDegree;
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   private void resetTables(int maxDegree)
/*  73:    */   {
/*  74:192 */     if (maxDegree < 0)
/*  75:    */     {
/*  76:193 */       this.polynomials = ((double[][])null);
/*  77:194 */       this.errfac = null;
/*  78:195 */       this.currentDegree = -1;
/*  79:    */     }
/*  80:    */     else
/*  81:    */     {
/*  82:198 */       double[][] newPols = new double[maxDegree + 1][];
/*  83:199 */       if (this.polynomials != null)
/*  84:    */       {
/*  85:200 */         System.arraycopy(this.polynomials, 0, newPols, 0, this.polynomials.length);
/*  86:201 */         for (int i = this.polynomials.length; i < newPols.length; i++) {
/*  87:202 */           newPols[i] = new double[this.currentState.length];
/*  88:    */         }
/*  89:    */       }
/*  90:    */       else
/*  91:    */       {
/*  92:205 */         for (int i = 0; i < newPols.length; i++) {
/*  93:206 */           newPols[i] = new double[this.currentState.length];
/*  94:    */         }
/*  95:    */       }
/*  96:209 */       this.polynomials = newPols;
/*  97:212 */       if (maxDegree <= 4)
/*  98:    */       {
/*  99:213 */         this.errfac = null;
/* 100:    */       }
/* 101:    */       else
/* 102:    */       {
/* 103:215 */         this.errfac = new double[maxDegree - 4];
/* 104:216 */         for (int i = 0; i < this.errfac.length; i++)
/* 105:    */         {
/* 106:217 */           int ip5 = i + 5;
/* 107:218 */           this.errfac[i] = (1.0D / (ip5 * ip5));
/* 108:219 */           double e = 0.5D * FastMath.sqrt((i + 1) / ip5);
/* 109:220 */           for (int j = 0; j <= i; j++) {
/* 110:221 */             this.errfac[i] *= e / (j + 1);
/* 111:    */           }
/* 112:    */         }
/* 113:    */       }
/* 114:226 */       this.currentDegree = 0;
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected StepInterpolator doCopy()
/* 119:    */   {
/* 120:235 */     return new GraggBulirschStoerStepInterpolator(this);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void computeCoefficients(int mu, double h)
/* 124:    */   {
/* 125:245 */     if ((this.polynomials == null) || (this.polynomials.length <= mu + 4)) {
/* 126:246 */       resetTables(mu + 4);
/* 127:    */     }
/* 128:249 */     this.currentDegree = (mu + 4);
/* 129:251 */     for (int i = 0; i < this.currentState.length; i++)
/* 130:    */     {
/* 131:253 */       double yp0 = h * this.y0Dot[i];
/* 132:254 */       double yp1 = h * this.y1Dot[i];
/* 133:255 */       double ydiff = this.y1[i] - this.currentState[i];
/* 134:256 */       double aspl = ydiff - yp1;
/* 135:257 */       double bspl = yp0 - ydiff;
/* 136:    */       
/* 137:259 */       this.polynomials[0][i] = this.currentState[i];
/* 138:260 */       this.polynomials[1][i] = ydiff;
/* 139:261 */       this.polynomials[2][i] = aspl;
/* 140:262 */       this.polynomials[3][i] = bspl;
/* 141:264 */       if (mu < 0) {
/* 142:265 */         return;
/* 143:    */       }
/* 144:269 */       double ph0 = 0.5D * (this.currentState[i] + this.y1[i]) + 0.125D * (aspl + bspl);
/* 145:270 */       this.polynomials[4][i] = (16.0D * (this.yMidDots[0][i] - ph0));
/* 146:272 */       if (mu > 0)
/* 147:    */       {
/* 148:273 */         double ph1 = ydiff + 0.25D * (aspl - bspl);
/* 149:274 */         this.polynomials[5][i] = (16.0D * (this.yMidDots[1][i] - ph1));
/* 150:276 */         if (mu > 1)
/* 151:    */         {
/* 152:277 */           double ph2 = yp1 - yp0;
/* 153:278 */           this.polynomials[6][i] = (16.0D * (this.yMidDots[2][i] - ph2 + this.polynomials[4][i]));
/* 154:280 */           if (mu > 2)
/* 155:    */           {
/* 156:281 */             double ph3 = 6.0D * (bspl - aspl);
/* 157:282 */             this.polynomials[7][i] = (16.0D * (this.yMidDots[3][i] - ph3 + 3.0D * this.polynomials[5][i]));
/* 158:284 */             for (int j = 4; j <= mu; j++)
/* 159:    */             {
/* 160:285 */               double fac1 = 0.5D * j * (j - 1);
/* 161:286 */               double fac2 = 2.0D * fac1 * (j - 2) * (j - 3);
/* 162:287 */               this.polynomials[(j + 4)][i] = (16.0D * (this.yMidDots[j][i] + fac1 * this.polynomials[(j + 2)][i] - fac2 * this.polynomials[j][i]));
/* 163:    */             }
/* 164:    */           }
/* 165:    */         }
/* 166:    */       }
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   public double estimateError(double[] scale)
/* 171:    */   {
/* 172:303 */     double error = 0.0D;
/* 173:304 */     if (this.currentDegree >= 5)
/* 174:    */     {
/* 175:305 */       for (int i = 0; i < scale.length; i++)
/* 176:    */       {
/* 177:306 */         double e = this.polynomials[this.currentDegree][i] / scale[i];
/* 178:307 */         error += e * e;
/* 179:    */       }
/* 180:309 */       error = FastMath.sqrt(error / scale.length) * this.errfac[(this.currentDegree - 5)];
/* 181:    */     }
/* 182:311 */     return error;
/* 183:    */   }
/* 184:    */   
/* 185:    */   protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH)
/* 186:    */   {
/* 187:319 */     int dimension = this.currentState.length;
/* 188:    */     
/* 189:321 */     double oneMinusTheta = 1.0D - theta;
/* 190:322 */     double theta05 = theta - 0.5D;
/* 191:323 */     double tOmT = theta * oneMinusTheta;
/* 192:324 */     double t4 = tOmT * tOmT;
/* 193:325 */     double t4Dot = 2.0D * tOmT * (1.0D - 2.0D * theta);
/* 194:326 */     double dot1 = 1.0D / this.h;
/* 195:327 */     double dot2 = theta * (2.0D - 3.0D * theta) / this.h;
/* 196:328 */     double dot3 = ((3.0D * theta - 4.0D) * theta + 1.0D) / this.h;
/* 197:330 */     for (int i = 0; i < dimension; i++)
/* 198:    */     {
/* 199:332 */       double p0 = this.polynomials[0][i];
/* 200:333 */       double p1 = this.polynomials[1][i];
/* 201:334 */       double p2 = this.polynomials[2][i];
/* 202:335 */       double p3 = this.polynomials[3][i];
/* 203:336 */       this.interpolatedState[i] = (p0 + theta * (p1 + oneMinusTheta * (p2 * theta + p3 * oneMinusTheta)));
/* 204:337 */       this.interpolatedDerivatives[i] = (dot1 * p1 + dot2 * p2 + dot3 * p3);
/* 205:339 */       if (this.currentDegree > 3)
/* 206:    */       {
/* 207:340 */         double cDot = 0.0D;
/* 208:341 */         double c = this.polynomials[this.currentDegree][i];
/* 209:342 */         for (int j = this.currentDegree - 1; j > 3; j--)
/* 210:    */         {
/* 211:343 */           double d = 1.0D / (j - 3);
/* 212:344 */           cDot = d * (theta05 * cDot + c);
/* 213:345 */           c = this.polynomials[j][i] + c * d * theta05;
/* 214:    */         }
/* 215:347 */         this.interpolatedState[i] += t4 * c;
/* 216:348 */         this.interpolatedDerivatives[i] += (t4 * cDot + t4Dot * c) / this.h;
/* 217:    */       }
/* 218:    */     }
/* 219:353 */     if (this.h == 0.0D) {
/* 220:356 */       System.arraycopy(this.yMidDots[1], 0, this.interpolatedDerivatives, 0, dimension);
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void writeExternal(ObjectOutput out)
/* 225:    */     throws IOException
/* 226:    */   {
/* 227:366 */     int dimension = this.currentState == null ? -1 : this.currentState.length;
/* 228:    */     
/* 229:    */ 
/* 230:369 */     writeBaseExternal(out);
/* 231:    */     
/* 232:    */ 
/* 233:372 */     out.writeInt(this.currentDegree);
/* 234:373 */     for (int k = 0; k <= this.currentDegree; k++) {
/* 235:374 */       for (int l = 0; l < dimension; l++) {
/* 236:375 */         out.writeDouble(this.polynomials[k][l]);
/* 237:    */       }
/* 238:    */     }
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void readExternal(ObjectInput in)
/* 242:    */     throws IOException, ClassNotFoundException
/* 243:    */   {
/* 244:387 */     double t = readBaseExternal(in);
/* 245:388 */     int dimension = this.currentState == null ? -1 : this.currentState.length;
/* 246:    */     
/* 247:    */ 
/* 248:391 */     int degree = in.readInt();
/* 249:392 */     resetTables(degree);
/* 250:393 */     this.currentDegree = degree;
/* 251:395 */     for (int k = 0; k <= this.currentDegree; k++) {
/* 252:396 */       for (int l = 0; l < dimension; l++) {
/* 253:397 */         this.polynomials[k][l] = in.readDouble();
/* 254:    */       }
/* 255:    */     }
/* 256:402 */     setInterpolatedTime(t);
/* 257:    */   }
/* 258:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerStepInterpolator
 * JD-Core Version:    0.7.0.1
 */
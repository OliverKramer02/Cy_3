/*   1:    */ package org.apache.commons.math3.ode;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ 
/*  10:    */ public class JacobianMatrices
/*  11:    */ {
/*  12:    */   private ExpandableStatefulODE efode;
/*  13:    */   private int index;
/*  14:    */   private MainStateJacobianProvider jode;
/*  15:    */   private ParameterizedODE pode;
/*  16:    */   private int stateDim;
/*  17:    */   private ParameterConfiguration[] selectedParameters;
/*  18:    */   private List<ParameterJacobianProvider> jacobianProviders;
/*  19:    */   private int paramDim;
/*  20:    */   private boolean dirtyParameter;
/*  21:    */   private double[] matricesData;
/*  22:    */   
/*  23:    */   public JacobianMatrices(FirstOrderDifferentialEquations fode, double[] hY, String... parameters)
/*  24:    */     throws MathIllegalArgumentException
/*  25:    */   {
/*  26:105 */     this(new MainStateJacobianWrapper(fode, hY), parameters);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public JacobianMatrices(MainStateJacobianProvider jode, String... parameters)
/*  30:    */     throws MathIllegalArgumentException
/*  31:    */   {
/*  32:125 */     this.efode = null;
/*  33:126 */     this.index = -1;
/*  34:    */     
/*  35:128 */     this.jode = jode;
/*  36:129 */     this.pode = null;
/*  37:    */     
/*  38:131 */     this.stateDim = jode.getDimension();
/*  39:133 */     if (parameters == null)
/*  40:    */     {
/*  41:134 */       this.selectedParameters = null;
/*  42:135 */       this.paramDim = 0;
/*  43:    */     }
/*  44:    */     else
/*  45:    */     {
/*  46:137 */       this.selectedParameters = new ParameterConfiguration[parameters.length];
/*  47:138 */       for (int i = 0; i < parameters.length; i++) {
/*  48:139 */         this.selectedParameters[i] = new ParameterConfiguration(parameters[i], (0.0D / 0.0D));
/*  49:    */       }
/*  50:141 */       this.paramDim = parameters.length;
/*  51:    */     }
/*  52:143 */     this.dirtyParameter = false;
/*  53:    */     
/*  54:145 */     this.jacobianProviders = new ArrayList();
/*  55:    */     
/*  56:    */ 
/*  57:    */ 
/*  58:149 */     this.matricesData = new double[(this.stateDim + this.paramDim) * this.stateDim];
/*  59:150 */     for (int i = 0; i < this.stateDim; i++) {
/*  60:151 */       this.matricesData[(i * (this.stateDim + 1))] = 1.0D;
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void registerVariationalEquations(ExpandableStatefulODE expandable)
/*  65:    */     throws MathIllegalArgumentException
/*  66:    */   {
/*  67:166 */     FirstOrderDifferentialEquations ode = (this.jode instanceof MainStateJacobianWrapper) ? ((MainStateJacobianWrapper)this.jode).ode : this.jode;
/*  68:169 */     if (expandable.getPrimary() != ode) {
/*  69:170 */       throw new MathIllegalArgumentException(LocalizedFormats.UNMATCHED_ODE_IN_EXPANDED_SET, new Object[0]);
/*  70:    */     }
/*  71:173 */     this.efode = expandable;
/*  72:174 */     this.index = this.efode.addSecondaryEquations(new JacobiansSecondaryEquations());
/*  73:175 */     this.efode.setSecondaryState(this.index, this.matricesData);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void addParameterJacobianProvider(ParameterJacobianProvider provider)
/*  77:    */   {
/*  78:183 */     this.jacobianProviders.add(provider);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setParameterizedODE(ParameterizedODE parameterizedOde)
/*  82:    */   {
/*  83:190 */     this.pode = parameterizedOde;
/*  84:191 */     this.dirtyParameter = true;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setParameterStep(String parameter, double hP)
/*  88:    */   {
/*  89:213 */     for (ParameterConfiguration param : this.selectedParameters) {
/*  90:214 */       if (parameter.equals(param.getParameterName()))
/*  91:    */       {
/*  92:215 */         param.setHP(hP);
/*  93:216 */         this.dirtyParameter = true;
/*  94:217 */         return;
/*  95:    */       }
/*  96:    */     }
/*  97:221 */     throw new MathIllegalArgumentException(LocalizedFormats.UNKNOWN_PARAMETER, new Object[] { parameter });
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setInitialMainStateJacobian(double[][] dYdY0)
/* 101:    */     throws DimensionMismatchException
/* 102:    */   {
/* 103:237 */     checkDimension(this.stateDim, dYdY0);
/* 104:238 */     checkDimension(this.stateDim, dYdY0[0]);
/* 105:    */     
/* 106:    */ 
/* 107:241 */     int i = 0;
/* 108:242 */     for (double[] row : dYdY0)
/* 109:    */     {
/* 110:243 */       System.arraycopy(row, 0, this.matricesData, i, this.stateDim);
/* 111:244 */       i += this.stateDim;
/* 112:    */     }
/* 113:247 */     if (this.efode != null) {
/* 114:248 */       this.efode.setSecondaryState(this.index, this.matricesData);
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setInitialParameterJacobian(String pName, double[] dYdP)
/* 119:    */     throws MathIllegalArgumentException
/* 120:    */   {
/* 121:266 */     checkDimension(this.stateDim, dYdP);
/* 122:    */     
/* 123:    */ 
/* 124:269 */     int i = this.stateDim * this.stateDim;
/* 125:270 */     for (ParameterConfiguration param : this.selectedParameters)
/* 126:    */     {
/* 127:271 */       if (pName.equals(param.getParameterName()))
/* 128:    */       {
/* 129:272 */         System.arraycopy(dYdP, 0, this.matricesData, i, this.stateDim);
/* 130:273 */         if (this.efode != null) {
/* 131:274 */           this.efode.setSecondaryState(this.index, this.matricesData);
/* 132:    */         }
/* 133:276 */         return;
/* 134:    */       }
/* 135:278 */       i += this.stateDim;
/* 136:    */     }
/* 137:281 */     throw new MathIllegalArgumentException(LocalizedFormats.UNKNOWN_PARAMETER, new Object[] { pName });
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void getCurrentMainSetJacobian(double[][] dYdY0)
/* 141:    */   {
/* 142:291 */     double[] p = this.efode.getSecondaryState(this.index);
/* 143:    */     
/* 144:293 */     int j = 0;
/* 145:294 */     for (int i = 0; i < this.stateDim; i++)
/* 146:    */     {
/* 147:295 */       System.arraycopy(p, j, dYdY0[i], 0, this.stateDim);
/* 148:296 */       j += this.stateDim;
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void getCurrentParameterJacobian(String pName, double[] dYdP)
/* 153:    */   {
/* 154:308 */     double[] p = this.efode.getSecondaryState(this.index);
/* 155:    */     
/* 156:310 */     int i = this.stateDim * this.stateDim;
/* 157:311 */     for (ParameterConfiguration param : this.selectedParameters)
/* 158:    */     {
/* 159:312 */       if (param.getParameterName().equals(pName))
/* 160:    */       {
/* 161:313 */         System.arraycopy(p, i, dYdP, 0, this.stateDim);
/* 162:314 */         return;
/* 163:    */       }
/* 164:316 */       i += this.stateDim;
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   private void checkDimension(int expected, Object array)
/* 169:    */     throws DimensionMismatchException
/* 170:    */   {
/* 171:328 */     int arrayDimension = array == null ? 0 : Array.getLength(array);
/* 172:329 */     if (arrayDimension != expected) {
/* 173:330 */       throw new DimensionMismatchException(arrayDimension, expected);
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   private class JacobiansSecondaryEquations
/* 178:    */     implements SecondaryEquations
/* 179:    */   {
/* 180:    */     private JacobiansSecondaryEquations() {}
/* 181:    */     
/* 182:    */     public int getDimension()
/* 183:    */     {
/* 184:344 */       return JacobianMatrices.this.stateDim * (JacobianMatrices.this.stateDim + JacobianMatrices.this.paramDim);
/* 185:    */     }
/* 186:    */     
/* 187:    */     public void computeDerivatives(double t, double[] y, double[] yDot, double[] z, double[] zDot)
/* 188:    */     {
/* 189:352 */       if ((JacobianMatrices.this.dirtyParameter) && (JacobianMatrices.this.paramDim != 0))
/* 190:    */       {
/* 191:353 */         JacobianMatrices.this.jacobianProviders.add(new ParameterJacobianWrapper(JacobianMatrices.this.jode, JacobianMatrices.this.pode, JacobianMatrices.this.selectedParameters));
/* 192:354 */         JacobianMatrices.this.dirtyParameter = false;
/* 193:    */       }
/* 194:361 */       double[][] dFdY = new double[JacobianMatrices.this.stateDim][JacobianMatrices.this.stateDim];
/* 195:362 */       JacobianMatrices.this.jode.computeMainStateJacobian(t, y, yDot, dFdY);
/* 196:365 */       for (int i = 0; i < JacobianMatrices.this.stateDim; i++)
/* 197:    */       {
/* 198:366 */         double[] dFdYi = dFdY[i];
/* 199:367 */         for (int j = 0; j < JacobianMatrices.this.stateDim; j++)
/* 200:    */         {
/* 201:368 */           double s = 0.0D;
/* 202:369 */           int startIndex = j;
/* 203:370 */           int zIndex = startIndex;
/* 204:371 */           for (int l = 0; l < JacobianMatrices.this.stateDim; l++)
/* 205:    */           {
/* 206:372 */             s += dFdYi[l] * z[zIndex];
/* 207:373 */             zIndex += JacobianMatrices.this.stateDim;
/* 208:    */           }
/* 209:375 */           zDot[(startIndex + i * JacobianMatrices.this.stateDim)] = s;
/* 210:    */         }
/* 211:    */       }
/* 212:379 */       if (JacobianMatrices.this.paramDim != 0)
/* 213:    */       {
/* 214:381 */         double[] dFdP = new double[JacobianMatrices.this.stateDim];
/* 215:382 */         int startIndex = JacobianMatrices.this.stateDim * JacobianMatrices.this.stateDim;
/* 216:383 */         for (ParameterConfiguration param : JacobianMatrices.this.selectedParameters)
/* 217:    */         {
/* 218:384 */           boolean found = false;
/* 219:385 */           for (ParameterJacobianProvider provider : JacobianMatrices.this.jacobianProviders) {
/* 220:386 */             if (provider.isSupported(param.getParameterName())) {
/* 221:    */               try
/* 222:    */               {
/* 223:388 */                 provider.computeParameterJacobian(t, y, yDot, param.getParameterName(), dFdP);
/* 224:389 */                 for (int i = 0; i < JacobianMatrices.this.stateDim; i++)
/* 225:    */                 {
/* 226:390 */                   double[] dFdYi = dFdY[i];
/* 227:391 */                   int zIndex = startIndex;
/* 228:392 */                   double s = dFdP[i];
/* 229:393 */                   for (int l = 0; l < JacobianMatrices.this.stateDim; l++)
/* 230:    */                   {
/* 231:394 */                     s += dFdYi[l] * z[zIndex];
/* 232:395 */                     zIndex++;
/* 233:    */                   }
/* 234:397 */                   zDot[(startIndex + i)] = s;
/* 235:    */                 }
/* 236:399 */                 startIndex += JacobianMatrices.this.stateDim;
/* 237:400 */                 found = true;
/* 238:    */               }
/* 239:    */               catch (IllegalArgumentException iae) {}
/* 240:    */             }
/* 241:    */           }
/* 242:406 */           if (!found) {
/* 243:407 */             throw new MathIllegalArgumentException(LocalizedFormats.UNKNOWN_PARAMETER, new Object[] { param });
/* 244:    */           }
/* 245:    */         }
/* 246:    */       }
/* 247:    */     }
/* 248:    */   }
/* 249:    */   
/* 250:    */   private static class MainStateJacobianWrapper
/* 251:    */     implements MainStateJacobianProvider
/* 252:    */   {
/* 253:    */     private final FirstOrderDifferentialEquations ode;
/* 254:    */     private final double[] hY;
/* 255:    */     
/* 256:    */     public MainStateJacobianWrapper(FirstOrderDifferentialEquations ode, double[] hY)
/* 257:    */     {
/* 258:434 */       this.ode = ode;
/* 259:435 */       this.hY = ((double[])hY.clone());
/* 260:    */     }
/* 261:    */     
/* 262:    */     public int getDimension()
/* 263:    */     {
/* 264:440 */       return this.ode.getDimension();
/* 265:    */     }
/* 266:    */     
/* 267:    */     public void computeDerivatives(double t, double[] y, double[] yDot)
/* 268:    */     {
/* 269:445 */       this.ode.computeDerivatives(t, y, yDot);
/* 270:    */     }
/* 271:    */     
/* 272:    */     public void computeMainStateJacobian(double t, double[] y, double[] yDot, double[][] dFdY)
/* 273:    */     {
/* 274:452 */       int n = this.ode.getDimension();
/* 275:453 */       double[] tmpDot = new double[n];
/* 276:455 */       for (int j = 0; j < n; j++)
/* 277:    */       {
/* 278:456 */         double savedYj = y[j];
/* 279:457 */         y[j] += this.hY[j];
/* 280:458 */         this.ode.computeDerivatives(t, y, tmpDot);
/* 281:459 */         for (int i = 0; i < n; i++) {
/* 282:460 */           dFdY[i][j] = ((tmpDot[i] - yDot[i]) / this.hY[j]);
/* 283:    */         }
/* 284:462 */         y[j] = savedYj;
/* 285:    */       }
/* 286:    */     }
/* 287:    */   }
/* 288:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.JacobianMatrices
 * JD-Core Version:    0.7.0.1
 */
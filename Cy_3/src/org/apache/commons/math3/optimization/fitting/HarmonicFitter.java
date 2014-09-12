/*   1:    */ package org.apache.commons.math3.optimization.fitting;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.function.HarmonicOscillator;
import org.apache.commons.math3.analysis.function.HarmonicOscillator.Parametric;
/*   4:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*   5:    */ import org.apache.commons.math3.exception.ZeroException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class HarmonicFitter
/*  11:    */   extends CurveFitter
/*  12:    */ {
/*  13:    */   public HarmonicFitter(DifferentiableMultivariateVectorOptimizer optimizer)
/*  14:    */   {
/*  15: 45 */     super(optimizer);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public double[] fit(double[] initialGuess)
/*  19:    */   {
/*  20: 61 */     return fit(new HarmonicOscillator.Parametric(), initialGuess);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double[] fit()
/*  24:    */   {
/*  25: 76 */     return fit(new ParameterGuesser(getObservations()).guess());
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static class ParameterGuesser
/*  29:    */   {
/*  30:    */     private final WeightedObservedPoint[] observations;
/*  31:    */     private double a;
/*  32:    */     private double omega;
/*  33:    */     private double phi;
/*  34:    */     
/*  35:    */     public ParameterGuesser(WeightedObservedPoint[] observations)
/*  36:    */     {
/*  37:195 */       if (observations.length < 4) {
/*  38:196 */         throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(observations.length), Integer.valueOf(4), true);
/*  39:    */       }
/*  40:200 */       this.observations = ((WeightedObservedPoint[])observations.clone());
/*  41:    */     }
/*  42:    */     
/*  43:    */     public double[] guess()
/*  44:    */     {
/*  45:214 */       sortObservations();
/*  46:215 */       guessAOmega();
/*  47:216 */       guessPhi();
/*  48:217 */       return new double[] { this.a, this.omega, this.phi };
/*  49:    */     }
/*  50:    */     
/*  51:    */     private void sortObservations()
/*  52:    */     {
/*  53:227 */       WeightedObservedPoint curr = this.observations[0];
/*  54:228 */       for (int j = 1; j < this.observations.length; j++)
/*  55:    */       {
/*  56:229 */         WeightedObservedPoint prec = curr;
/*  57:230 */         curr = this.observations[j];
/*  58:231 */         if (curr.getX() < prec.getX())
/*  59:    */         {
/*  60:233 */           int i = j - 1;
/*  61:234 */           WeightedObservedPoint mI = this.observations[i];
/*  62:235 */           while ((i >= 0) && (curr.getX() < mI.getX()))
/*  63:    */           {
/*  64:236 */             this.observations[(i + 1)] = mI;
/*  65:237 */             if (i-- != 0) {
/*  66:238 */               mI = this.observations[i];
/*  67:    */             }
/*  68:    */           }
/*  69:241 */           this.observations[(i + 1)] = curr;
/*  70:242 */           curr = this.observations[j];
/*  71:    */         }
/*  72:    */       }
/*  73:    */     }
/*  74:    */     
/*  75:    */     private void guessAOmega()
/*  76:    */     {
/*  77:256 */       double sx2 = 0.0D;
/*  78:257 */       double sy2 = 0.0D;
/*  79:258 */       double sxy = 0.0D;
/*  80:259 */       double sxz = 0.0D;
/*  81:260 */       double syz = 0.0D;
/*  82:    */       
/*  83:262 */       double currentX = this.observations[0].getX();
/*  84:263 */       double currentY = this.observations[0].getY();
/*  85:264 */       double f2Integral = 0.0D;
/*  86:265 */       double fPrime2Integral = 0.0D;
/*  87:266 */       double startX = currentX;
/*  88:267 */       for (int i = 1; i < this.observations.length; i++)
/*  89:    */       {
/*  90:269 */         double previousX = currentX;
/*  91:270 */         double previousY = currentY;
/*  92:271 */         currentX = this.observations[i].getX();
/*  93:272 */         currentY = this.observations[i].getY();
/*  94:    */         
/*  95:    */ 
/*  96:    */ 
/*  97:276 */         double dx = currentX - previousX;
/*  98:277 */         double dy = currentY - previousY;
/*  99:278 */         double f2StepIntegral = dx * (previousY * previousY + previousY * currentY + currentY * currentY) / 3.0D;
/* 100:    */         
/* 101:280 */         double fPrime2StepIntegral = dy * dy / dx;
/* 102:    */         
/* 103:282 */         double x = currentX - startX;
/* 104:283 */         f2Integral += f2StepIntegral;
/* 105:284 */         fPrime2Integral += fPrime2StepIntegral;
/* 106:    */         
/* 107:286 */         sx2 += x * x;
/* 108:287 */         sy2 += f2Integral * f2Integral;
/* 109:288 */         sxy += x * f2Integral;
/* 110:289 */         sxz += x * fPrime2Integral;
/* 111:290 */         syz += f2Integral * fPrime2Integral;
/* 112:    */       }
/* 113:294 */       double c1 = sy2 * sxz - sxy * syz;
/* 114:295 */       double c2 = sxy * sxz - sx2 * syz;
/* 115:296 */       double c3 = sx2 * sy2 - sxy * sxy;
/* 116:297 */       if ((c1 / c2 < 0.0D) || (c2 / c3 < 0.0D))
/* 117:    */       {
/* 118:298 */         int last = this.observations.length - 1;
/* 119:    */         
/* 120:    */ 
/* 121:301 */         double xRange = this.observations[last].getX() - this.observations[0].getX();
/* 122:302 */         if (xRange == 0.0D) {
/* 123:303 */           throw new ZeroException();
/* 124:    */         }
/* 125:305 */         this.omega = (6.283185307179586D / xRange);
/* 126:    */         
/* 127:307 */         double yMin = (1.0D / 0.0D);
/* 128:308 */         double yMax = (-1.0D / 0.0D);
/* 129:309 */         for (int i = 1; i < this.observations.length; i++)
/* 130:    */         {
/* 131:310 */           double y = this.observations[i].getY();
/* 132:311 */           if (y < yMin) {
/* 133:312 */             yMin = y;
/* 134:    */           }
/* 135:314 */           if (y > yMax) {
/* 136:315 */             yMax = y;
/* 137:    */           }
/* 138:    */         }
/* 139:318 */         this.a = (0.5D * (yMax - yMin));
/* 140:    */       }
/* 141:    */       else
/* 142:    */       {
/* 143:320 */         this.a = FastMath.sqrt(c1 / c2);
/* 144:321 */         this.omega = FastMath.sqrt(c2 / c3);
/* 145:    */       }
/* 146:    */     }
/* 147:    */     
/* 148:    */     private void guessPhi()
/* 149:    */     {
/* 150:330 */       double fcMean = 0.0D;
/* 151:331 */       double fsMean = 0.0D;
/* 152:    */       
/* 153:333 */       double currentX = this.observations[0].getX();
/* 154:334 */       double currentY = this.observations[0].getY();
/* 155:335 */       for (int i = 1; i < this.observations.length; i++)
/* 156:    */       {
/* 157:337 */         double previousX = currentX;
/* 158:338 */         double previousY = currentY;
/* 159:339 */         currentX = this.observations[i].getX();
/* 160:340 */         currentY = this.observations[i].getY();
/* 161:341 */         double currentYPrime = (currentY - previousY) / (currentX - previousX);
/* 162:    */         
/* 163:343 */         double omegaX = this.omega * currentX;
/* 164:344 */         double cosine = FastMath.cos(omegaX);
/* 165:345 */         double sine = FastMath.sin(omegaX);
/* 166:346 */         fcMean += this.omega * currentY * cosine - currentYPrime * sine;
/* 167:347 */         fsMean += this.omega * currentY * sine + currentYPrime * cosine;
/* 168:    */       }
/* 169:350 */       this.phi = FastMath.atan2(-fsMean, fcMean);
/* 170:    */     }
/* 171:    */   }
/* 172:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.fitting.HarmonicFitter
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.optimization.fitting;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Comparator;

/*   5:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.function.Gaussian;
/*   6:    */ import org.apache.commons.math3.analysis.function.Gaussian.Parametric;
/*   7:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  10:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*  11:    */ import org.apache.commons.math3.exception.ZeroException;
/*  12:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  13:    */ import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
/*  14:    */ 
/*  15:    */ public class GaussianFitter
/*  16:    */   extends CurveFitter
/*  17:    */ {
/*  18:    */   public GaussianFitter(DifferentiableMultivariateVectorOptimizer optimizer)
/*  19:    */   {
/*  20: 68 */     super(optimizer);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double[] fit(double[] initialGuess)
/*  24:    */   {
/*  25: 85 */     ParametricUnivariateFunction f = new ParametricUnivariateFunction()
/*  26:    */     {
/*  27: 86 */       private final ParametricUnivariateFunction g = new Gaussian.Parametric();
/*  28:    */       
/*  29:    */       public double value(double x, double... p)
/*  30:    */       {
/*  31: 89 */         double v = (1.0D / 0.0D);
/*  32:    */         try
/*  33:    */         {
/*  34: 91 */           v = this.g.value(x, p);
/*  35:    */         }
/*  36:    */         catch (NotStrictlyPositiveException e) {}
/*  37: 95 */         return v;
/*  38:    */       }
/*  39:    */       
/*  40:    */       public double[] gradient(double x, double... p)
/*  41:    */       {
/*  42: 99 */         double[] v = { (1.0D / 0.0D), (1.0D / 0.0D), (1.0D / 0.0D) };
/*  43:    */         try
/*  44:    */         {
/*  45:103 */           v = this.g.gradient(x, p);
/*  46:    */         }
/*  47:    */         catch (NotStrictlyPositiveException e) {}
/*  48:107 */         return v;
/*  49:    */       }
/*  50:110 */     };
/*  51:111 */     return fit(f, initialGuess);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public double[] fit()
/*  55:    */   {
/*  56:121 */     double[] guess = new ParameterGuesser(getObservations()).guess();
/*  57:122 */     return fit(guess);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static class ParameterGuesser
/*  61:    */   {
/*  62:    */     private final WeightedObservedPoint[] observations;
/*  63:    */     private double[] parameters;
/*  64:    */     
/*  65:    */     public ParameterGuesser(WeightedObservedPoint[] observations)
/*  66:    */     {
/*  67:142 */       if (observations == null) {
/*  68:143 */         throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
/*  69:    */       }
/*  70:145 */       if (observations.length < 3) {
/*  71:146 */         throw new NumberIsTooSmallException(Integer.valueOf(observations.length), Integer.valueOf(3), true);
/*  72:    */       }
/*  73:148 */       this.observations = ((WeightedObservedPoint[])observations.clone());
/*  74:    */     }
/*  75:    */     
/*  76:    */     public double[] guess()
/*  77:    */     {
/*  78:157 */       if (this.parameters == null) {
/*  79:158 */         this.parameters = basicGuess(this.observations);
/*  80:    */       }
/*  81:160 */       return (double[])this.parameters.clone();
/*  82:    */     }
/*  83:    */     
/*  84:    */     private double[] basicGuess(WeightedObservedPoint[] points)
/*  85:    */     {
/*  86:170 */       Arrays.sort(points, createWeightedObservedPointComparator());
/*  87:171 */       double[] params = new double[3];
/*  88:    */       
/*  89:173 */       int maxYIdx = findMaxY(points);
/*  90:174 */       params[0] = points[maxYIdx].getY();
/*  91:175 */       params[1] = points[maxYIdx].getX();
/*  92:    */       double fwhmApprox;
/*  93:    */       try
/*  94:    */       {
/*  95:179 */         double halfY = params[0] + (params[1] - params[0]) / 2.0D;
/*  96:180 */         double fwhmX1 = interpolateXAtY(points, maxYIdx, -1, halfY);
/*  97:181 */         double fwhmX2 = interpolateXAtY(points, maxYIdx, 1, halfY);
/*  98:182 */         fwhmApprox = fwhmX2 - fwhmX1;
/*  99:    */       }
/* 100:    */       catch (OutOfRangeException e)
/* 101:    */       {
/* 102:184 */         fwhmApprox = points[(points.length - 1)].getX() - points[0].getX();
/* 103:    */       }
/* 104:186 */       params[2] = (fwhmApprox / (2.0D * Math.sqrt(2.0D * Math.log(2.0D))));
/* 105:    */       
/* 106:188 */       return params;
/* 107:    */     }
/* 108:    */     
/* 109:    */     private int findMaxY(WeightedObservedPoint[] points)
/* 110:    */     {
/* 111:198 */       int maxYIdx = 0;
/* 112:199 */       for (int i = 1; i < points.length; i++) {
/* 113:200 */         if (points[i].getY() > points[maxYIdx].getY()) {
/* 114:201 */           maxYIdx = i;
/* 115:    */         }
/* 116:    */       }
/* 117:204 */       return maxYIdx;
/* 118:    */     }
/* 119:    */     
/* 120:    */     private double interpolateXAtY(WeightedObservedPoint[] points, int startIdx, int idxStep, double y)
/* 121:    */       throws OutOfRangeException
/* 122:    */     {
/* 123:224 */       if (idxStep == 0) {
/* 124:225 */         throw new ZeroException();
/* 125:    */       }
/* 126:227 */       WeightedObservedPoint[] twoPoints = getInterpolationPointsForY(points, startIdx, idxStep, y);
/* 127:228 */       WeightedObservedPoint pointA = twoPoints[0];
/* 128:229 */       WeightedObservedPoint pointB = twoPoints[1];
/* 129:230 */       if (pointA.getY() == y) {
/* 130:231 */         return pointA.getX();
/* 131:    */       }
/* 132:233 */       if (pointB.getY() == y) {
/* 133:234 */         return pointB.getX();
/* 134:    */       }
/* 135:236 */       return pointA.getX() + (y - pointA.getY()) * (pointB.getX() - pointA.getX()) / (pointB.getY() - pointA.getY());
/* 136:    */     }
/* 137:    */     
/* 138:    */     private WeightedObservedPoint[] getInterpolationPointsForY(WeightedObservedPoint[] points, int startIdx, int idxStep, double y)
/* 139:    */       throws OutOfRangeException
/* 140:    */     {
/* 141:259 */       if (idxStep == 0) {
/* 142:260 */         throw new ZeroException();
/* 143:    */       }
/* 144:262 */       int i = startIdx;
/* 145:263 */       for (; idxStep < 0 ? i + idxStep >= 0 : i + idxStep < points.length; i += idxStep) {
/* 146:265 */         if (isBetween(y, points[i].getY(), points[(i + idxStep)].getY())) {
/* 147:266 */           return new WeightedObservedPoint[] { points[i], (WeightedObservedPoint) (idxStep < 0 ? new WeightedObservedPoint[] { points[(i + idxStep)], points[i] } : points[(i + idxStep)]) };
/* 148:    */         }
/* 149:    */       }
/* 150:272 */       double minY = (1.0D / 0.0D);
/* 151:273 */       double maxY = (-1.0D / 0.0D);
/* 152:274 */       for (WeightedObservedPoint point : points)
/* 153:    */       {
/* 154:275 */         minY = Math.min(minY, point.getY());
/* 155:276 */         maxY = Math.max(maxY, point.getY());
/* 156:    */       }
/* 157:278 */       throw new OutOfRangeException(Double.valueOf(y), Double.valueOf(minY), Double.valueOf(maxY));
/* 158:    */     }
/* 159:    */     
/* 160:    */     private boolean isBetween(double value, double boundary1, double boundary2)
/* 161:    */     {
/* 162:292 */       return ((value >= boundary1) && (value <= boundary2)) || ((value >= boundary2) && (value <= boundary1));
/* 163:    */     }
/* 164:    */     
/* 165:    */     private Comparator<WeightedObservedPoint> createWeightedObservedPointComparator()
/* 166:    */     {
return new Comparator()
/* 168:    */       {
/* 169:    */         public int compare(WeightedObservedPoint p1, WeightedObservedPoint p2)
/* 170:    */         {
/* 171:305 */           if ((p1 == null) && (p2 == null)) {
/* 172:306 */             return 0;
/* 173:    */           }
/* 174:308 */           if (p1 == null) {
/* 175:309 */             return -1;
/* 176:    */           }
/* 177:311 */           if (p2 == null) {
/* 178:312 */             return 1;
/* 179:    */           }
/* 180:314 */           if (p1.getX() < p2.getX()) {
/* 181:315 */             return -1;
/* 182:    */           }
/* 183:317 */           if (p1.getX() > p2.getX()) {
/* 184:318 */             return 1;
/* 185:    */           }
/* 186:320 */           if (p1.getY() < p2.getY()) {
/* 187:321 */             return -1;
/* 188:    */           }
/* 189:323 */           if (p1.getY() > p2.getY()) {
/* 190:324 */             return 1;
/* 191:    */           }
/* 192:326 */           if (p1.getWeight() < p2.getWeight()) {
/* 193:327 */             return -1;
/* 194:    */           }
/* 195:329 */           if (p1.getWeight() > p2.getWeight()) {
/* 196:330 */             return 1;
/* 197:    */           }
/* 198:332 */           return 0;
/* 199:    */         }
/* 200:    */

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}       };     }
/* 202:    */   }
/* 203:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.fitting.GaussianFitter
 * JD-Core Version:    0.7.0.1
 */
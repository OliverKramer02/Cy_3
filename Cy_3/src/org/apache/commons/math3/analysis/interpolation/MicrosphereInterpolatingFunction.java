/*   1:    */ package org.apache.commons.math3.analysis.interpolation;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.apache.commons.math3.analysis.MultivariateFunction;
/*  11:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  12:    */ import org.apache.commons.math3.exception.NoDataException;
/*  13:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  14:    */ import org.apache.commons.math3.linear.ArrayRealVector;
/*  15:    */ import org.apache.commons.math3.linear.RealVector;
/*  16:    */ import org.apache.commons.math3.random.UnitSphereRandomVectorGenerator;
/*  17:    */ import org.apache.commons.math3.util.FastMath;
/*  18:    */ 
/*  19:    */ public class MicrosphereInterpolatingFunction
/*  20:    */   implements MultivariateFunction
/*  21:    */ {
/*  22:    */   private final int dimension;
/*  23:    */   private final List<MicrosphereSurfaceElement> microsphere;
/*  24:    */   private final double brightnessExponent;
/*  25:    */   private final Map<RealVector, Double> samples;
/*  26:    */   
/*  27:    */   private static class MicrosphereSurfaceElement
/*  28:    */   {
/*  29:    */     private final RealVector normal;
/*  30:    */     private double brightestIllumination;
/*  31:    */     private Map.Entry<RealVector, Double> brightestSample;
/*  32:    */     
/*  33:    */     MicrosphereSurfaceElement(double[] n)
/*  34:    */     {
/*  35: 78 */       this.normal = new ArrayRealVector(n);
/*  36:    */     }
/*  37:    */     
/*  38:    */     RealVector normal()
/*  39:    */     {
/*  40: 86 */       return this.normal;
/*  41:    */     }
/*  42:    */     
/*  43:    */     void reset()
/*  44:    */     {
/*  45: 93 */       this.brightestIllumination = 0.0D;
/*  46: 94 */       this.brightestSample = null;
/*  47:    */     }
/*  48:    */     
/*  49:    */     void store(double illuminationFromSample, Map.Entry<RealVector, Double> sample)
/*  50:    */     {
/*  51:104 */       if (illuminationFromSample > this.brightestIllumination)
/*  52:    */       {
/*  53:105 */         this.brightestIllumination = illuminationFromSample;
/*  54:106 */         this.brightestSample = sample;
/*  55:    */       }
/*  56:    */     }
/*  57:    */     
/*  58:    */     double illumination()
/*  59:    */     {
/*  60:115 */       return this.brightestIllumination;
/*  61:    */     }
/*  62:    */     
/*  63:    */     Map.Entry<RealVector, Double> sample()
/*  64:    */     {
/*  65:123 */       return this.brightestSample;
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public MicrosphereInterpolatingFunction(double[][] xval, double[] yval, int brightnessExponent, int microsphereElements, UnitSphereRandomVectorGenerator rand)
/*  70:    */   {
/*  71:151 */     if ((xval == null) || (yval == null)) {
/*  72:153 */       throw new NullArgumentException();
/*  73:    */     }
/*  74:155 */     if (xval.length == 0) {
/*  75:156 */       throw new NoDataException();
/*  76:    */     }
/*  77:158 */     if (xval.length != yval.length) {
/*  78:159 */       throw new DimensionMismatchException(xval.length, yval.length);
/*  79:    */     }
/*  80:161 */     if (xval[0] == null) {
/*  81:162 */       throw new NullArgumentException();
/*  82:    */     }
/*  83:165 */     this.dimension = xval[0].length;
/*  84:166 */     this.brightnessExponent = brightnessExponent;
/*  85:    */     
/*  86:    */ 
/*  87:169 */     this.samples = new HashMap(yval.length);
/*  88:170 */     for (int i = 0; i < xval.length; i++)
/*  89:    */     {
/*  90:171 */       double[] xvalI = xval[i];
/*  91:172 */       if (xvalI == null) {
/*  92:173 */         throw new NullArgumentException();
/*  93:    */       }
/*  94:175 */       if (xvalI.length != this.dimension) {
/*  95:176 */         throw new DimensionMismatchException(xvalI.length, this.dimension);
/*  96:    */       }
/*  97:179 */       this.samples.put(new ArrayRealVector(xvalI), Double.valueOf(yval[i]));
/*  98:    */     }
/*  99:182 */     this.microsphere = new ArrayList(microsphereElements);
/* 100:185 */     for (int i = 0; i < microsphereElements; i++) {
/* 101:186 */       this.microsphere.add(new MicrosphereSurfaceElement(rand.nextVector()));
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public double value(double[] point)
/* 106:    */   {
/* 107:195 */     RealVector p = new ArrayRealVector(point);
/* 108:198 */     for (MicrosphereSurfaceElement md : this.microsphere) {
/* 109:199 */       md.reset();
/* 110:    */     }
/* 111:203 */     for (Iterator i$ = this.samples.entrySet().iterator(); i$.hasNext();)
/* 112:    */     {
/* 113:203 */       Entry sd = (Map.Entry)i$.next();
/* 114:    */       
/* 115:    */ 
/* 116:206 */       RealVector diff = ((RealVector)sd.getKey()).subtract(p);
/* 117:207 */       double diffNorm = diff.getNorm();
/* 118:209 */       if (FastMath.abs(diffNorm) < FastMath.ulp(1.0D)) {
/* 119:212 */         return ((Double)sd.getValue()).doubleValue();
/* 120:    */       }
/* 121:215 */       for (MicrosphereSurfaceElement md : this.microsphere)
/* 122:    */       {
/* 123:216 */         double w = FastMath.pow(diffNorm, -this.brightnessExponent);
/* 124:217 */         md.store(cosAngle(diff, md.normal()) * w, sd);
/* 125:    */       }
/* 126:    */     }
/* 127:    */     Map.Entry<RealVector, Double> sd;
/* 128:    */     RealVector diff;
/* 129:    */     double diffNorm;
/* 130:223 */     double value = 0.0D;
/* 131:224 */     double totalWeight = 0.0D;
/* 132:225 */     for (MicrosphereSurfaceElement md : this.microsphere)
/* 133:    */     {
/* 134:226 */       double iV = md.illumination();
/* 135:227 */       Map.Entry<RealVector, Double> sd1 = md.sample();
/* 136:228 */       if (sd1 != null)
/* 137:    */       {
/* 138:229 */         value += iV * ((Double)sd1.getValue()).doubleValue();
/* 139:230 */         totalWeight += iV;
/* 140:    */       }
/* 141:    */     }
/* 142:234 */     return value / totalWeight;
/* 143:    */   }
/* 144:    */   
/* 145:    */   private double cosAngle(RealVector v, RealVector w)
/* 146:    */   {
/* 147:245 */     return v.dotProduct(w) / (v.getNorm() * w.getNorm());
/* 148:    */   }
/* 149:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.interpolation.MicrosphereInterpolatingFunction
 * JD-Core Version:    0.7.0.1
 */
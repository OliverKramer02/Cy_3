/*   1:    */ package org.apache.commons.math3.analysis.function;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class HarmonicOscillator
/*  11:    */   implements DifferentiableUnivariateFunction
/*  12:    */ {
/*  13:    */   private final double amplitude;
/*  14:    */   private final double omega;
/*  15:    */   private final double phase;
/*  16:    */   
/*  17:    */   public HarmonicOscillator(double amplitude, double omega, double phase)
/*  18:    */   {
/*  19: 52 */     this.amplitude = amplitude;
/*  20: 53 */     this.omega = omega;
/*  21: 54 */     this.phase = phase;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public double value(double x)
/*  25:    */   {
/*  26: 59 */     return value(this.omega * x + this.phase, this.amplitude);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public UnivariateFunction derivative()
/*  30:    */   {
return new UnivariateFunction()
/*  32:    */     {
/*  33:    */       public double value(double x)
/*  34:    */       {
/*  35: 67 */         return -HarmonicOscillator.this.amplitude * HarmonicOscillator.this.omega * FastMath.sin(HarmonicOscillator.this.omega * x + HarmonicOscillator.this.phase);
/*  36:    */       }
/*  37:    */     };   }
/*  39:    */   
/*  40:    */   public static class Parametric
/*  41:    */     implements ParametricUnivariateFunction
/*  42:    */   {
/*  43:    */     public double value(double x, double... param)
/*  44:    */     {
/*  45: 93 */       validateParameters(param);
/*  46: 94 */       return HarmonicOscillator.value(x * param[1] + param[2], param[0]);
/*  47:    */     }
/*  48:    */     
/*  49:    */     public double[] gradient(double x, double... param)
/*  50:    */     {
/*  51:111 */       validateParameters(param);
/*  52:    */       
/*  53:113 */       double amplitude = param[0];
/*  54:114 */       double omega = param[1];
/*  55:115 */       double phase = param[2];
/*  56:    */       
/*  57:117 */       double xTimesOmegaPlusPhase = omega * x + phase;
/*  58:118 */       double a = HarmonicOscillator.value(xTimesOmegaPlusPhase, 1.0D);
/*  59:119 */       double p = -amplitude * FastMath.sin(xTimesOmegaPlusPhase);
/*  60:120 */       double w = p * x;
/*  61:    */       
/*  62:122 */       return new double[] { a, w, p };
/*  63:    */     }
/*  64:    */     
/*  65:    */     private void validateParameters(double[] param)
/*  66:    */     {
/*  67:136 */       if (param == null) {
/*  68:137 */         throw new NullArgumentException();
/*  69:    */       }
/*  70:139 */       if (param.length != 3) {
/*  71:140 */         throw new DimensionMismatchException(param.length, 3);
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   private static double value(double xTimesOmegaPlusPhase, double amplitude)
/*  77:    */   {
/*  78:152 */     return amplitude * FastMath.cos(xTimesOmegaPlusPhase);
/*  79:    */   }
/*  80:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.HarmonicOscillator
 * JD-Core Version:    0.7.0.1
 */
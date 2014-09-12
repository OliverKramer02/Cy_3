/*   1:    */ package org.apache.commons.math3.analysis.function;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
/*   5:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   6:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ 
/*  10:    */ public class Sigmoid
/*  11:    */   implements DifferentiableUnivariateFunction
/*  12:    */ {
/*  13:    */   private final double lo;
/*  14:    */   private final double hi;
/*  15:    */   
/*  16:    */   public Sigmoid()
/*  17:    */   {
/*  18: 48 */     this(0.0D, 1.0D);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Sigmoid(double lo, double hi)
/*  22:    */   {
/*  23: 59 */     this.lo = lo;
/*  24: 60 */     this.hi = hi;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public UnivariateFunction derivative()
/*  28:    */   {
return new UnivariateFunction()
/*  30:    */     {
/*  31:    */       public double value(double x)
/*  32:    */       {
/*  33: 68 */         double exp = FastMath.exp(-x);
/*  34: 69 */         if (Double.isInfinite(exp)) {
/*  35: 71 */           return 0.0D;
/*  36:    */         }
/*  37: 73 */         double exp1 = 1.0D + exp;
/*  38: 74 */         return (Sigmoid.this.hi - Sigmoid.this.lo) * exp / (exp1 * exp1);
/*  39:    */       }
/*  40:    */     };   }
/*  42:    */   
/*  43:    */   public double value(double x)
/*  44:    */   {
/*  45: 81 */     return value(x, this.lo, this.hi);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static class Parametric
/*  49:    */     implements ParametricUnivariateFunction
/*  50:    */   {
/*  51:    */     public double value(double x, double... param)
/*  52:    */     {
/*  53:104 */       validateParameters(param);
/*  54:105 */       return Sigmoid.value(x, param[0], param[1]);
/*  55:    */     }
/*  56:    */     
/*  57:    */     public double[] gradient(double x, double... param)
/*  58:    */     {
/*  59:122 */       validateParameters(param);
/*  60:    */       
/*  61:124 */       double invExp1 = 1.0D / (1.0D + FastMath.exp(-x));
/*  62:    */       
/*  63:126 */       return new double[] { 1.0D - invExp1, invExp1 };
/*  64:    */     }
/*  65:    */     
/*  66:    */     private void validateParameters(double[] param)
/*  67:    */     {
/*  68:140 */       if (param == null) {
/*  69:141 */         throw new NullArgumentException();
/*  70:    */       }
/*  71:143 */       if (param.length != 2) {
/*  72:144 */         throw new DimensionMismatchException(param.length, 2);
/*  73:    */       }
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   private static double value(double x, double lo, double hi)
/*  78:    */   {
/*  79:158 */     return lo + (hi - lo) / (1.0D + FastMath.exp(-x));
/*  80:    */   }
/*  81:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.analysis.function.Sigmoid
 * JD-Core Version:    0.7.0.1
 */
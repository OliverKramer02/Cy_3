/*   1:    */ package org.apache.commons.math3.ode;
/*   2:    */ 
/*   3:    */ public class FirstOrderConverter
/*   4:    */   implements FirstOrderDifferentialEquations
/*   5:    */ {
/*   6:    */   private final SecondOrderDifferentialEquations equations;
/*   7:    */   private final int dimension;
/*   8:    */   private final double[] z;
/*   9:    */   private final double[] zDot;
/*  10:    */   private final double[] zDDot;
/*  11:    */   
/*  12:    */   public FirstOrderConverter(SecondOrderDifferentialEquations equations)
/*  13:    */   {
/*  14: 79 */     this.equations = equations;
/*  15: 80 */     this.dimension = equations.getDimension();
/*  16: 81 */     this.z = new double[this.dimension];
/*  17: 82 */     this.zDot = new double[this.dimension];
/*  18: 83 */     this.zDDot = new double[this.dimension];
/*  19:    */   }
/*  20:    */   
/*  21:    */   public int getDimension()
/*  22:    */   {
/*  23: 92 */     return 2 * this.dimension;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void computeDerivatives(double t, double[] y, double[] yDot)
/*  27:    */   {
/*  28:103 */     System.arraycopy(y, 0, this.z, 0, this.dimension);
/*  29:104 */     System.arraycopy(y, this.dimension, this.zDot, 0, this.dimension);
/*  30:    */     
/*  31:    */ 
/*  32:107 */     this.equations.computeSecondDerivatives(t, this.z, this.zDot, this.zDDot);
/*  33:    */     
/*  34:    */ 
/*  35:110 */     System.arraycopy(this.zDot, 0, yDot, 0, this.dimension);
/*  36:111 */     System.arraycopy(this.zDDot, 0, yDot, this.dimension, this.dimension);
/*  37:    */   }
/*  38:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.FirstOrderConverter
 * JD-Core Version:    0.7.0.1
 */
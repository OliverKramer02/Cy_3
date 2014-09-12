/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathUnsupportedOperationException;
/*   4:    */ 
/*   5:    */ public class DefaultIterativeLinearSolverEvent
/*   6:    */   extends IterativeLinearSolverEvent
/*   7:    */ {
/*   8:    */   private static final long serialVersionUID = 20120129L;
/*   9:    */   private final RealVector b;
/*  10:    */   private final RealVector r;
/*  11:    */   private final double rnorm;
/*  12:    */   private final RealVector x;
/*  13:    */   
/*  14:    */   public DefaultIterativeLinearSolverEvent(Object source, int iterations, RealVector x, RealVector b, RealVector r, double rnorm)
/*  15:    */   {
/*  16: 66 */     super(source, iterations);
/*  17: 67 */     this.x = x;
/*  18: 68 */     this.b = b;
/*  19: 69 */     this.r = r;
/*  20: 70 */     this.rnorm = rnorm;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public DefaultIterativeLinearSolverEvent(Object source, int iterations, RealVector x, RealVector b, double rnorm)
/*  24:    */   {
/*  25: 93 */     super(source, iterations);
/*  26: 94 */     this.x = x;
/*  27: 95 */     this.b = b;
/*  28: 96 */     this.r = null;
/*  29: 97 */     this.rnorm = rnorm;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public double getNormOfResidual()
/*  33:    */   {
/*  34:103 */     return this.rnorm;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public RealVector getResidual()
/*  38:    */   {
/*  39:113 */     if (this.r != null) {
/*  40:114 */       return this.r;
/*  41:    */     }
/*  42:116 */     throw new MathUnsupportedOperationException();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public RealVector getRightHandSideVector()
/*  46:    */   {
/*  47:122 */     return this.b;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public RealVector getSolution()
/*  51:    */   {
/*  52:128 */     return this.x;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public boolean providesResidual()
/*  56:    */   {
/*  57:141 */     return this.r != null;
/*  58:    */   }
/*  59:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.DefaultIterativeLinearSolverEvent
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathUnsupportedOperationException;
/*   4:    */ import org.apache.commons.math3.util.IterationEvent;
/*   5:    */ 
/*   6:    */ public abstract class IterativeLinearSolverEvent
/*   7:    */   extends IterationEvent
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 20120129L;
/*  10:    */   
/*  11:    */   public IterativeLinearSolverEvent(Object source, int iterations)
/*  12:    */   {
/*  13: 43 */     super(source, iterations);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public abstract RealVector getRightHandSideVector();
/*  17:    */   
/*  18:    */   public abstract double getNormOfResidual();
/*  19:    */   
/*  20:    */   public RealVector getResidual()
/*  21:    */   {
/*  22: 93 */     throw new MathUnsupportedOperationException();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public abstract RealVector getSolution();
/*  26:    */   
/*  27:    */   public boolean providesResidual()
/*  28:    */   {
/*  29:114 */     return false;
/*  30:    */   }
/*  31:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.IterativeLinearSolverEvent
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ 
/*   5:    */ public abstract class RealLinearOperator
/*   6:    */ {
/*   7:    */   public abstract int getRowDimension();
/*   8:    */   
/*   9:    */   public abstract int getColumnDimension();
/*  10:    */   
/*  11:    */   public abstract RealVector operate(RealVector paramRealVector);
/*  12:    */   
/*  13:    */   public RealVector operateTranspose(RealVector x)
/*  14:    */     throws DimensionMismatchException, UnsupportedOperationException
/*  15:    */   {
/*  16: 94 */     throw new UnsupportedOperationException();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public boolean isTransposable()
/*  20:    */   {
/*  21:107 */     return false;
/*  22:    */   }
/*  23:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.RealLinearOperator
 * JD-Core Version:    0.7.0.1
 */
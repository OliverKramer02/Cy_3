/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.util.IterationManager;
/*   7:    */ import org.apache.commons.math3.util.MathUtils;
/*   8:    */ 
/*   9:    */ public abstract class IterativeLinearSolver
/*  10:    */ {
/*  11:    */   private final IterationManager manager;
/*  12:    */   
/*  13:    */   public IterativeLinearSolver(int maxIterations)
/*  14:    */   {
/*  15: 45 */     this.manager = new IterationManager(maxIterations);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public IterativeLinearSolver(IterationManager manager)
/*  19:    */     throws NullArgumentException
/*  20:    */   {
/*  21: 56 */     MathUtils.checkNotNull(manager);
/*  22: 57 */     this.manager = manager;
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected static void checkParameters(RealLinearOperator a, RealVector b, RealVector x0)
/*  26:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException
/*  27:    */   {
/*  28: 78 */     MathUtils.checkNotNull(a);
/*  29: 79 */     MathUtils.checkNotNull(b);
/*  30: 80 */     MathUtils.checkNotNull(x0);
/*  31: 81 */     if (a.getRowDimension() != a.getColumnDimension()) {
/*  32: 82 */       throw new NonSquareOperatorException(a.getRowDimension(), a.getColumnDimension());
/*  33:    */     }
/*  34: 85 */     if (b.getDimension() != a.getRowDimension()) {
/*  35: 86 */       throw new DimensionMismatchException(b.getDimension(), a.getRowDimension());
/*  36:    */     }
/*  37: 89 */     if (x0.getDimension() != a.getColumnDimension()) {
/*  38: 90 */       throw new DimensionMismatchException(x0.getDimension(), a.getColumnDimension());
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public IterationManager getIterationManager()
/*  43:    */   {
/*  44:101 */     return this.manager;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public RealVector solve(RealLinearOperator a, RealVector b)
/*  48:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  49:    */   {
/*  50:123 */     MathUtils.checkNotNull(a);
/*  51:124 */     RealVector x = new ArrayRealVector(a.getColumnDimension());
/*  52:125 */     x.set(0.0D);
/*  53:126 */     return solveInPlace(a, b, x);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public RealVector solve(RealLinearOperator a, RealVector b, RealVector x0)
/*  57:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  58:    */   {
/*  59:149 */     MathUtils.checkNotNull(x0);
/*  60:150 */     return solveInPlace(a, b, x0.copy());
/*  61:    */   }
/*  62:    */   
/*  63:    */   public abstract RealVector solveInPlace(RealLinearOperator paramRealLinearOperator, RealVector paramRealVector1, RealVector paramRealVector2)
/*  64:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException;
/*  65:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.IterativeLinearSolver
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.util.IterationManager;
/*   7:    */ import org.apache.commons.math3.util.MathUtils;
/*   8:    */ 
/*   9:    */ public abstract class PreconditionedIterativeLinearSolver
/*  10:    */   extends IterativeLinearSolver
/*  11:    */ {
/*  12:    */   public PreconditionedIterativeLinearSolver(int maxIterations)
/*  13:    */   {
/*  14: 52 */     super(maxIterations);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public PreconditionedIterativeLinearSolver(IterationManager manager)
/*  18:    */     throws NullArgumentException
/*  19:    */   {
/*  20: 63 */     super(manager);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public RealVector solve(RealLinearOperator a, RealLinearOperator minv, RealVector b, RealVector x0)
/*  24:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  25:    */   {
/*  26: 90 */     MathUtils.checkNotNull(x0);
/*  27: 91 */     return solveInPlace(a, minv, b, x0.copy());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public RealVector solve(RealLinearOperator a, RealVector b)
/*  31:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  32:    */   {
/*  33: 99 */     MathUtils.checkNotNull(a);
/*  34:100 */     RealVector x = new ArrayRealVector(a.getColumnDimension());
/*  35:101 */     x.set(0.0D);
/*  36:102 */     return solveInPlace(a, null, b, x);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public RealVector solve(RealLinearOperator a, RealVector b, RealVector x0)
/*  40:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  41:    */   {
/*  42:111 */     MathUtils.checkNotNull(x0);
/*  43:112 */     return solveInPlace(a, null, b, x0.copy());
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected static void checkParameters(RealLinearOperator a, RealLinearOperator minv, RealVector b, RealVector x0)
/*  47:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException
/*  48:    */   {
/*  49:137 */     checkParameters(a, b, x0);
/*  50:138 */     if (minv != null)
/*  51:    */     {
/*  52:139 */       if (minv.getColumnDimension() != minv.getRowDimension()) {
/*  53:140 */         throw new NonSquareOperatorException(minv.getColumnDimension(), minv.getRowDimension());
/*  54:    */       }
/*  55:143 */       if (minv.getRowDimension() != a.getRowDimension()) {
/*  56:144 */         throw new DimensionMismatchException(minv.getRowDimension(), a.getRowDimension());
/*  57:    */       }
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public RealVector solve(RealLinearOperator a, RealLinearOperator minv, RealVector b)
/*  62:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  63:    */   {
/*  64:172 */     MathUtils.checkNotNull(a);
/*  65:173 */     RealVector x = new ArrayRealVector(a.getColumnDimension());
/*  66:174 */     return solveInPlace(a, minv, b, x);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public abstract RealVector solveInPlace(RealLinearOperator paramRealLinearOperator1, RealLinearOperator paramRealLinearOperator2, RealVector paramRealVector1, RealVector paramRealVector2)
/*  70:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException;
/*  71:    */   
/*  72:    */   public RealVector solveInPlace(RealLinearOperator a, RealVector b, RealVector x0)
/*  73:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  74:    */   {
/*  75:209 */     return solveInPlace(a, null, b, x0);
/*  76:    */   }
/*  77:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver
 * JD-Core Version:    0.7.0.1
 */
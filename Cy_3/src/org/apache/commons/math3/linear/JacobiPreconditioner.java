/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ public class JacobiPreconditioner
/*  4:   */   extends RealLinearOperator
/*  5:   */ {
/*  6:   */   private final ArrayRealVector diag;
/*  7:   */   
/*  8:   */   public JacobiPreconditioner(double[] diag, boolean deep)
/*  9:   */   {
/* 10:44 */     this.diag = new ArrayRealVector(diag, deep);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public static JacobiPreconditioner create(RealLinearOperator a)
/* 14:   */     throws NonSquareOperatorException
/* 15:   */   {
/* 16:62 */     int n = a.getColumnDimension();
/* 17:63 */     if (a.getRowDimension() != n) {
/* 18:64 */       throw new NonSquareOperatorException(a.getRowDimension(), n);
/* 19:   */     }
/* 20:66 */     double[] diag = new double[n];
/* 21:67 */     if ((a instanceof AbstractRealMatrix))
/* 22:   */     {
/* 23:68 */       AbstractRealMatrix m = (AbstractRealMatrix)a;
/* 24:69 */       for (int i = 0; i < n; i++) {
/* 25:70 */         diag[i] = m.getEntry(i, i);
/* 26:   */       }
/* 27:   */     }
/* 28:   */     else
/* 29:   */     {
/* 30:73 */       ArrayRealVector x = new ArrayRealVector(n);
/* 31:74 */       for (int i = 0; i < n; i++)
/* 32:   */       {
/* 33:75 */         x.set(0.0D);
/* 34:76 */         x.setEntry(i, 1.0D);
/* 35:77 */         diag[i] = a.operate(x).getEntry(i);
/* 36:   */       }
/* 37:   */     }
/* 38:80 */     return new JacobiPreconditioner(diag, false);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public int getColumnDimension()
/* 42:   */   {
/* 43:86 */     return this.diag.getDimension();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public int getRowDimension()
/* 47:   */   {
/* 48:92 */     return this.diag.getDimension();
/* 49:   */   }
/* 50:   */   
/* 51:   */   public RealVector operate(RealVector x)
/* 52:   */   {
/* 53:99 */     return x.ebeDivide(this.diag);
/* 54:   */   }
/* 55:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.JacobiPreconditioner
 * JD-Core Version:    0.7.0.1
 */
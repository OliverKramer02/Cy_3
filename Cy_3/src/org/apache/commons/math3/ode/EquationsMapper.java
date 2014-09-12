/*  1:   */ package org.apache.commons.math3.ode;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  5:   */ 
/*  6:   */ public class EquationsMapper
/*  7:   */   implements Serializable
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 20110925L;
/* 10:   */   private final int firstIndex;
/* 11:   */   private final int dimension;
/* 12:   */   
/* 13:   */   public EquationsMapper(int firstIndex, int dimension)
/* 14:   */   {
/* 15:50 */     this.firstIndex = firstIndex;
/* 16:51 */     this.dimension = dimension;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public int getFirstIndex()
/* 20:   */   {
/* 21:58 */     return this.firstIndex;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public int getDimension()
/* 25:   */   {
/* 26:65 */     return this.dimension;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void extractEquationData(double[] complete, double[] equationData)
/* 30:   */     throws DimensionMismatchException
/* 31:   */   {
/* 32:77 */     if (equationData.length != this.dimension) {
/* 33:78 */       throw new DimensionMismatchException(equationData.length, this.dimension);
/* 34:   */     }
/* 35:80 */     System.arraycopy(complete, this.firstIndex, equationData, 0, this.dimension);
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void insertEquationData(double[] equationData, double[] complete)
/* 39:   */     throws DimensionMismatchException
/* 40:   */   {
/* 41:92 */     if (equationData.length != this.dimension) {
/* 42:93 */       throw new DimensionMismatchException(equationData.length, this.dimension);
/* 43:   */     }
/* 44:95 */     System.arraycopy(equationData, 0, complete, this.firstIndex, this.dimension);
/* 45:   */   }
/* 46:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.EquationsMapper
 * JD-Core Version:    0.7.0.1
 */
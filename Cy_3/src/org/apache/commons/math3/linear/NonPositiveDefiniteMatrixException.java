/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  4:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  5:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  6:   */ 
/*  7:   */ public class NonPositiveDefiniteMatrixException
/*  8:   */   extends NumberIsTooSmallException
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 1641613838113738061L;
/* 11:   */   private final int index;
/* 12:   */   private final double threshold;
/* 13:   */   
/* 14:   */   public NonPositiveDefiniteMatrixException(double wrong, int index, double threshold)
/* 15:   */   {
/* 16:47 */     super(Double.valueOf(wrong), Double.valueOf(threshold), false);
/* 17:48 */     this.index = index;
/* 18:49 */     this.threshold = threshold;
/* 19:   */     
/* 20:51 */     ExceptionContext context = getContext();
/* 21:52 */     context.addMessage(LocalizedFormats.NOT_POSITIVE_DEFINITE_MATRIX, new Object[0]);
/* 22:53 */     context.addMessage(LocalizedFormats.ARRAY_ELEMENT, new Object[] { Double.valueOf(wrong), Integer.valueOf(index) });
/* 23:   */   }
/* 24:   */   
/* 25:   */   public int getRow()
/* 26:   */   {
/* 27:60 */     return this.index;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public int getColumn()
/* 31:   */   {
/* 32:66 */     return this.index;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public double getThreshold()
/* 36:   */   {
/* 37:72 */     return this.threshold;
/* 38:   */   }
/* 39:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException
 * JD-Core Version:    0.7.0.1
 */
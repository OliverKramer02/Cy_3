/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ 
/*  5:   */ public class NotPositiveException
/*  6:   */   extends NumberIsTooSmallException
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = -2250556892093726375L;
/*  9:   */   
/* 10:   */   public NotPositiveException(Number value)
/* 11:   */   {
/* 12:37 */     super(value, Integer.valueOf(0), true);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public NotPositiveException(Localizable specific, Number value)
/* 16:   */   {
/* 17:47 */     super(specific, value, Integer.valueOf(0), true);
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NotPositiveException
 * JD-Core Version:    0.7.0.1
 */
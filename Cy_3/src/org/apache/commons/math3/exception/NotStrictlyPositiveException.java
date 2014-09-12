/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ 
/*  5:   */ public class NotStrictlyPositiveException
/*  6:   */   extends NumberIsTooSmallException
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = -7824848630829852237L;
/*  9:   */   
/* 10:   */   public NotStrictlyPositiveException(Number value)
/* 11:   */   {
/* 12:38 */     super(value, Integer.valueOf(0), false);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public NotStrictlyPositiveException(Localizable specific, Number value)
/* 16:   */   {
/* 17:48 */     super(specific, value, Integer.valueOf(0), false);
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NotStrictlyPositiveException
 * JD-Core Version:    0.7.0.1
 */
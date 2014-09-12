/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NumberIsTooLargeException
/*  7:   */   extends MathIllegalNumberException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 4330003017885151975L;
/* 10:   */   private final Number max;
/* 11:   */   private final boolean boundIsAllowed;
/* 12:   */   
/* 13:   */   public NumberIsTooLargeException(Number wrong, Number max, boolean boundIsAllowed)
/* 14:   */   {
/* 15:50 */     this(boundIsAllowed ? LocalizedFormats.NUMBER_TOO_LARGE : LocalizedFormats.NUMBER_TOO_LARGE_BOUND_EXCLUDED, wrong, max, boundIsAllowed);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public NumberIsTooLargeException(Localizable specific, Number wrong, Number max, boolean boundIsAllowed)
/* 19:   */   {
/* 20:67 */     super(specific, wrong, new Object[] { max });
/* 21:   */     
/* 22:69 */     this.max = max;
/* 23:70 */     this.boundIsAllowed = boundIsAllowed;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean getBoundIsAllowed()
/* 27:   */   {
/* 28:77 */     return this.boundIsAllowed;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Number getMax()
/* 32:   */   {
/* 33:84 */     return this.max;
/* 34:   */   }
/* 35:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NumberIsTooLargeException
 * JD-Core Version:    0.7.0.1
 */
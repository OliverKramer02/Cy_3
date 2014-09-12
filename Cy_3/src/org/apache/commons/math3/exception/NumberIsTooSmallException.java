/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NumberIsTooSmallException
/*  7:   */   extends MathIllegalNumberException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -6100997100383932834L;
/* 10:   */   private final Number min;
/* 11:   */   private final boolean boundIsAllowed;
/* 12:   */   
/* 13:   */   public NumberIsTooSmallException(Number wrong, Number min, boolean boundIsAllowed)
/* 14:   */   {
/* 15:50 */     this(boundIsAllowed ? LocalizedFormats.NUMBER_TOO_SMALL : LocalizedFormats.NUMBER_TOO_SMALL_BOUND_EXCLUDED, wrong, min, boundIsAllowed);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public NumberIsTooSmallException(Localizable specific, Number wrong, Number min, boolean boundIsAllowed)
/* 19:   */   {
/* 20:68 */     super(specific, wrong, new Object[] { min });
/* 21:   */     
/* 22:70 */     this.min = min;
/* 23:71 */     this.boundIsAllowed = boundIsAllowed;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean getBoundIsAllowed()
/* 27:   */   {
/* 28:78 */     return this.boundIsAllowed;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Number getMin()
/* 32:   */   {
/* 33:85 */     return this.min;
/* 34:   */   }
/* 35:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NumberIsTooSmallException
 * JD-Core Version:    0.7.0.1
 */
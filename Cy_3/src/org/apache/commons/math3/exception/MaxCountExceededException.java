/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  5:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  6:   */ 
/*  7:   */ public class MaxCountExceededException
/*  8:   */   extends MathIllegalStateException
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 4330003017885151975L;
/* 11:   */   private final Number max;
/* 12:   */   
/* 13:   */   public MaxCountExceededException(Number max)
/* 14:   */   {
/* 15:42 */     this(LocalizedFormats.MAX_COUNT_EXCEEDED, max, new Object[0]);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MaxCountExceededException(Localizable specific, Number max, Object... args)
/* 19:   */   {
/* 20:54 */     getContext().addMessage(specific, new Object[] { max, args });
/* 21:55 */     this.max = max;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Number getMax()
/* 25:   */   {
/* 26:62 */     return this.max;
/* 27:   */   }
/* 28:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MaxCountExceededException
 * JD-Core Version:    0.7.0.1
 */
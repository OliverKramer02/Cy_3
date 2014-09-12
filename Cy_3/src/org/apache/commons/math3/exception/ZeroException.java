/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class ZeroException
/*  7:   */   extends MathIllegalNumberException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -1960874856936000015L;
/* 10:   */   
/* 11:   */   public ZeroException()
/* 12:   */   {
/* 13:37 */     this(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ZeroException(Localizable specific, Object... arguments)
/* 17:   */   {
/* 18:47 */     super(specific, Integer.valueOf(0), arguments);
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.ZeroException
 * JD-Core Version:    0.7.0.1
 */
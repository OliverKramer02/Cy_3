/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NullArgumentException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -6024911025449780478L;
/* 10:   */   
/* 11:   */   public NullArgumentException()
/* 12:   */   {
/* 13:41 */     this(LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public NullArgumentException(Localizable pattern, Object... arguments)
/* 17:   */   {
/* 18:50 */     super(pattern, arguments);
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NullArgumentException
 * JD-Core Version:    0.7.0.1
 */
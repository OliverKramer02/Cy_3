/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  5:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  6:   */ 
/*  7:   */ public class ConvergenceException
/*  8:   */   extends MathIllegalStateException
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 4330003017885151975L;
/* 11:   */   
/* 12:   */   public ConvergenceException()
/* 13:   */   {
/* 14:37 */     this(LocalizedFormats.CONVERGENCE_FAILED, new Object[0]);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ConvergenceException(Localizable pattern, Object... args)
/* 18:   */   {
/* 19:49 */     getContext().addMessage(pattern, args);
/* 20:   */   }
/* 21:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.ConvergenceException
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  5:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  6:   */ 
/*  7:   */ public class MathInternalError
/*  8:   */   extends MathIllegalStateException
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = -6276776513966934846L;
/* 11:   */   private static final String REPORT_URL = "https://issues.apache.org/jira/browse/MATH";
/* 12:   */   
/* 13:   */   public MathInternalError()
/* 14:   */   {
/* 15:38 */     getContext().addMessage(LocalizedFormats.INTERNAL_ERROR, new Object[] { "https://issues.apache.org/jira/browse/MATH" });
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MathInternalError(Throwable cause)
/* 19:   */   {
/* 20:46 */     super(cause, LocalizedFormats.INTERNAL_ERROR, new Object[] { "https://issues.apache.org/jira/browse/MATH" });
/* 21:   */   }
/* 22:   */   
/* 23:   */   public MathInternalError(Localizable pattern, Object... args)
/* 24:   */   {
/* 25:56 */     super(pattern, args);
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MathInternalError
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.ExceptionContextProvider;
/*  5:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ 
/*  8:   */ public class MathUnsupportedOperationException
/*  9:   */   extends UnsupportedOperationException
/* 10:   */   implements ExceptionContextProvider
/* 11:   */ {
/* 12:   */   private static final long serialVersionUID = -6024911025449780478L;
/* 13:   */   private final ExceptionContext context;
/* 14:   */   
/* 15:   */   public MathUnsupportedOperationException()
/* 16:   */   {
/* 17:44 */     this(LocalizedFormats.UNSUPPORTED_OPERATION, new Object[0]);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public MathUnsupportedOperationException(Localizable pattern, Object... args)
/* 21:   */   {
/* 22:53 */     this.context = new ExceptionContext(this);
/* 23:54 */     this.context.addMessage(pattern, args);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public ExceptionContext getContext()
/* 27:   */   {
/* 28:59 */     return this.context;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String getMessage()
/* 32:   */   {
/* 33:65 */     return this.context.getMessage();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getLocalizedMessage()
/* 37:   */   {
/* 38:71 */     return this.context.getLocalizedMessage();
/* 39:   */   }
/* 40:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MathUnsupportedOperationException
 * JD-Core Version:    0.7.0.1
 */
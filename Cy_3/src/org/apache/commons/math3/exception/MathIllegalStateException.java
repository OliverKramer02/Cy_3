/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.ExceptionContextProvider;
/*  5:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ 
/*  8:   */ public class MathIllegalStateException
/*  9:   */   extends IllegalStateException
/* 10:   */   implements ExceptionContextProvider
/* 11:   */ {
/* 12:   */   private static final long serialVersionUID = -6024911025449780478L;
/* 13:   */   private final ExceptionContext context;
/* 14:   */   
/* 15:   */   public MathIllegalStateException(Localizable pattern, Object... args)
/* 16:   */   {
/* 17:46 */     this.context = new ExceptionContext(this);
/* 18:47 */     this.context.addMessage(pattern, args);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public MathIllegalStateException(Throwable cause, Localizable pattern, Object... args)
/* 22:   */   {
/* 23:60 */     super(cause);
/* 24:61 */     this.context = new ExceptionContext(this);
/* 25:62 */     this.context.addMessage(pattern, args);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public MathIllegalStateException()
/* 29:   */   {
/* 30:69 */     this(LocalizedFormats.ILLEGAL_STATE, new Object[0]);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public ExceptionContext getContext()
/* 34:   */   {
/* 35:74 */     return this.context;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getMessage()
/* 39:   */   {
/* 40:80 */     return this.context.getMessage();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getLocalizedMessage()
/* 44:   */   {
/* 45:86 */     return this.context.getLocalizedMessage();
/* 46:   */   }
/* 47:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MathIllegalStateException
 * JD-Core Version:    0.7.0.1
 */
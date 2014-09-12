/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.ExceptionContextProvider;
/*  5:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ 
/*  8:   */ public class MathArithmeticException
/*  9:   */   extends ArithmeticException
/* 10:   */   implements ExceptionContextProvider
/* 11:   */ {
/* 12:   */   private static final long serialVersionUID = -6024911025449780478L;
/* 13:   */   private final ExceptionContext context;
/* 14:   */   
/* 15:   */   public MathArithmeticException()
/* 16:   */   {
/* 17:44 */     this.context = new ExceptionContext(this);
/* 18:45 */     this.context.addMessage(LocalizedFormats.ARITHMETIC_EXCEPTION, new Object[0]);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public MathArithmeticException(Localizable pattern, Object... args)
/* 22:   */   {
/* 23:57 */     this.context = new ExceptionContext(this);
/* 24:58 */     this.context.addMessage(pattern, args);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public ExceptionContext getContext()
/* 28:   */   {
/* 29:63 */     return this.context;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getMessage()
/* 33:   */   {
/* 34:69 */     return this.context.getMessage();
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getLocalizedMessage()
/* 38:   */   {
/* 39:75 */     return this.context.getLocalizedMessage();
/* 40:   */   }
/* 41:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MathArithmeticException
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.ExceptionContextProvider;
/*  5:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  6:   */ 
/*  7:   */ public class MathIllegalArgumentException
/*  8:   */   extends IllegalArgumentException
/*  9:   */   implements ExceptionContextProvider
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = -6024911025449780478L;
/* 12:   */   private final ExceptionContext context;
/* 13:   */   
/* 14:   */   public MathIllegalArgumentException(Localizable pattern, Object... args)
/* 15:   */   {
/* 16:45 */     this.context = new ExceptionContext(this);
/* 17:46 */     this.context.addMessage(pattern, args);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ExceptionContext getContext()
/* 21:   */   {
/* 22:51 */     return this.context;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getMessage()
/* 26:   */   {
/* 27:57 */     return this.context.getMessage();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getLocalizedMessage()
/* 31:   */   {
/* 32:63 */     return this.context.getLocalizedMessage();
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MathIllegalArgumentException
 * JD-Core Version:    0.7.0.1
 */
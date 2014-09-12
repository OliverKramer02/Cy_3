/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.ExceptionContextProvider;
/*  5:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  6:   */ 
/*  7:   */ public class MathParseException
/*  8:   */   extends MathIllegalStateException
/*  9:   */   implements ExceptionContextProvider
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = -6024911025449780478L;
/* 12:   */   
/* 13:   */   public MathParseException(String wrong, int position, Class<?> type)
/* 14:   */   {
/* 15:43 */     getContext().addMessage(LocalizedFormats.CANNOT_PARSE_AS_TYPE, new Object[] { wrong, Integer.valueOf(position), type.getName() });
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MathParseException(String wrong, int position)
/* 19:   */   {
/* 20:54 */     getContext().addMessage(LocalizedFormats.CANNOT_PARSE, new Object[] { wrong, Integer.valueOf(position) });
/* 21:   */   }
/* 22:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MathParseException
 * JD-Core Version:    0.7.0.1
 */
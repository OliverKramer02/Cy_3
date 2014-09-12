/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class TooManyEvaluationsException
/*  7:   */   extends MaxCountExceededException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 4330003017885151975L;
/* 10:   */   
/* 11:   */   public TooManyEvaluationsException(Number max)
/* 12:   */   {
/* 13:37 */     super(max);
/* 14:38 */     getContext().addMessage(LocalizedFormats.EVALUATIONS, new Object[0]);
/* 15:   */   }
/* 16:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.TooManyEvaluationsException
 * JD-Core Version:    0.7.0.1
 */
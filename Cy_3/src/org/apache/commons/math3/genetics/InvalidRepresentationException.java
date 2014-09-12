/*  1:   */ package org.apache.commons.math3.genetics;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  5:   */ 
/*  6:   */ public class InvalidRepresentationException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 1L;
/* 10:   */   
/* 11:   */   public InvalidRepresentationException(Localizable pattern, Object... args)
/* 12:   */   {
/* 13:40 */     super(pattern, args);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.InvalidRepresentationException
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NotFiniteNumberException
/*  7:   */   extends MathIllegalNumberException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -6100997100383932834L;
/* 10:   */   
/* 11:   */   public NotFiniteNumberException(Number wrong, Object... args)
/* 12:   */   {
/* 13:40 */     this(LocalizedFormats.NOT_FINITE_NUMBER, wrong, args);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public NotFiniteNumberException(Localizable specific, Number wrong, Object... args)
/* 17:   */   {
/* 18:53 */     super(specific, wrong, args);
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NotFiniteNumberException
 * JD-Core Version:    0.7.0.1
 */
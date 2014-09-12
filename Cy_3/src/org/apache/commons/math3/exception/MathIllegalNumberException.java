/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ 
/*  5:   */ public class MathIllegalNumberException
/*  6:   */   extends MathIllegalArgumentException
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = -7447085893598031110L;
/*  9:   */   private final Number argument;
/* 10:   */   
/* 11:   */   protected MathIllegalNumberException(Localizable pattern, Number wrong, Object... arguments)
/* 12:   */   {
/* 13:46 */     super(pattern, new Object[] { wrong, arguments });
/* 14:47 */     this.argument = wrong;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Number getArgument()
/* 18:   */   {
/* 19:54 */     return this.argument;
/* 20:   */   }
/* 21:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.MathIllegalNumberException
 * JD-Core Version:    0.7.0.1
 */
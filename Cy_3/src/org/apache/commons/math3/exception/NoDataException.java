/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NoDataException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -3629324471511904459L;
/* 10:   */   
/* 11:   */   public NoDataException()
/* 12:   */   {
/* 13:37 */     this(LocalizedFormats.NO_DATA);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public NoDataException(Localizable specific)
/* 17:   */   {
/* 18:45 */     super(specific, new Object[0]);
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NoDataException
 * JD-Core Version:    0.7.0.1
 */
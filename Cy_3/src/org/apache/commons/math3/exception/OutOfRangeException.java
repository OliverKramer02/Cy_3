/*  1:   */ package org.apache.commons.math3.exception;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class OutOfRangeException
/*  7:   */   extends MathIllegalNumberException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 111601815794403609L;
/* 10:   */   private final Number lo;
/* 11:   */   private final Number hi;
/* 12:   */   
/* 13:   */   public OutOfRangeException(Number wrong, Number lo, Number hi)
/* 14:   */   {
/* 15:46 */     this(LocalizedFormats.OUT_OF_RANGE_SIMPLE, wrong, lo, hi);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public OutOfRangeException(Localizable specific, Number wrong, Number lo, Number hi)
/* 19:   */   {
/* 20:62 */     super(specific, wrong, new Object[] { lo, hi });
/* 21:63 */     this.lo = lo;
/* 22:64 */     this.hi = hi;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Number getLo()
/* 26:   */   {
/* 27:71 */     return this.lo;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Number getHi()
/* 31:   */   {
/* 32:77 */     return this.hi;
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.OutOfRangeException
 * JD-Core Version:    0.7.0.1
 */
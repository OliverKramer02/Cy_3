/*  1:   */ package org.apache.commons.math3.fraction;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.ConvergenceException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class FractionConversionException
/*  7:   */   extends ConvergenceException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -4661812640132576263L;
/* 10:   */   
/* 11:   */   public FractionConversionException(double value, int maxIterations)
/* 12:   */   {
/* 13:42 */     super(LocalizedFormats.FAILED_FRACTION_CONVERSION, new Object[] { Double.valueOf(value), Integer.valueOf(maxIterations) });
/* 14:   */   }
/* 15:   */   
/* 16:   */   public FractionConversionException(double value, long p, long q)
/* 17:   */   {
/* 18:53 */     super(LocalizedFormats.FRACTION_CONVERSION_OVERFLOW, new Object[] { Double.valueOf(value), Long.valueOf(p), Long.valueOf(q) });
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.FractionConversionException
 * JD-Core Version:    0.7.0.1
 */
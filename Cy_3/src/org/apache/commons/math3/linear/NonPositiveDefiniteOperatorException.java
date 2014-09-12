/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class NonPositiveDefiniteOperatorException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 917034489420549847L;
/* 10:   */   
/* 11:   */   public NonPositiveDefiniteOperatorException()
/* 12:   */   {
/* 13:42 */     super(LocalizedFormats.NON_POSITIVE_DEFINITE_OPERATOR, new Object[0]);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.NonPositiveDefiniteOperatorException
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class IllConditionedOperatorException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -7883263944530490135L;
/* 10:   */   
/* 11:   */   public IllConditionedOperatorException(double cond)
/* 12:   */   {
/* 13:41 */     super(LocalizedFormats.ILL_CONDITIONED_OPERATOR, new Object[] { Double.valueOf(cond) });
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.IllConditionedOperatorException
 * JD-Core Version:    0.7.0.1
 */
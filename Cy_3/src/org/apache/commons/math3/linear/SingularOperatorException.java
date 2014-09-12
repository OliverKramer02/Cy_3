/*  1:   */ package org.apache.commons.math3.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class SingularOperatorException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -476049978595245033L;
/* 10:   */   
/* 11:   */   public SingularOperatorException()
/* 12:   */   {
/* 13:37 */     super(LocalizedFormats.SINGULAR_OPERATOR, new Object[0]);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.SingularOperatorException
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.optimization.linear;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class UnboundedSolutionException
/*  7:   */   extends MathIllegalStateException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 940539497277290619L;
/* 10:   */   
/* 11:   */   public UnboundedSolutionException()
/* 12:   */   {
/* 13:38 */     super(LocalizedFormats.UNBOUNDED_SOLUTION, new Object[0]);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.UnboundedSolutionException
 * JD-Core Version:    0.7.0.1
 */
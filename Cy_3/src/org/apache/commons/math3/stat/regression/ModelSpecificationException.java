/*  1:   */ package org.apache.commons.math3.stat.regression;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  5:   */ 
/*  6:   */ public class ModelSpecificationException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 4206514456095401070L;
/* 10:   */   
/* 11:   */   public ModelSpecificationException(Localizable pattern, Object... args)
/* 12:   */   {
/* 13:39 */     super(pattern, args);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.regression.ModelSpecificationException
 * JD-Core Version:    0.7.0.1
 */
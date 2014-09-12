/*  1:   */ package org.apache.commons.math3.geometry.euclidean.threed;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  4:   */ import org.apache.commons.math3.exception.util.Localizable;
/*  5:   */ 
/*  6:   */ public class NotARotationMatrixException
/*  7:   */   extends MathIllegalArgumentException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 5647178478658937642L;
/* 10:   */   
/* 11:   */   public NotARotationMatrixException(Localizable specifier, Object... parts)
/* 12:   */   {
/* 13:45 */     super(specifier, parts);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException
 * JD-Core Version:    0.7.0.1
 */
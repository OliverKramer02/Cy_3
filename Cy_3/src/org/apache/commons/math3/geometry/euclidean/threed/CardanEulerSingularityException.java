/*  1:   */ package org.apache.commons.math3.geometry.euclidean.threed;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*  4:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  5:   */ 
/*  6:   */ public class CardanEulerSingularityException
/*  7:   */   extends MathIllegalStateException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -1360952845582206770L;
/* 10:   */   
/* 11:   */   public CardanEulerSingularityException(boolean isCardan)
/* 12:   */   {
/* 13:42 */     super(isCardan ? LocalizedFormats.CARDAN_ANGLES_SINGULARITY : LocalizedFormats.EULER_ANGLES_SINGULARITY, new Object[0]);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException
 * JD-Core Version:    0.7.0.1
 */
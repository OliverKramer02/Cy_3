/*  1:   */ package org.apache.commons.math3.geometry.partitioning;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.geometry.Space;
/*  4:   */ 
/*  5:   */ public class BoundaryAttribute<S extends Space>
/*  6:   */ {
/*  7:   */   private final SubHyperplane<S> plusOutside;
/*  8:   */   private final SubHyperplane<S> plusInside;
/*  9:   */   
/* 10:   */   public BoundaryAttribute(SubHyperplane<S> plusOutside, SubHyperplane<S> plusInside)
/* 11:   */   {
/* 12:58 */     this.plusOutside = plusOutside;
/* 13:59 */     this.plusInside = plusInside;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public SubHyperplane<S> getPlusOutside()
/* 17:   */   {
/* 18:70 */     return this.plusOutside;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public SubHyperplane<S> getPlusInside()
/* 22:   */   {
/* 23:81 */     return this.plusInside;
/* 24:   */   }
/* 25:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.BoundaryAttribute
 * JD-Core Version:    0.7.0.1
 */
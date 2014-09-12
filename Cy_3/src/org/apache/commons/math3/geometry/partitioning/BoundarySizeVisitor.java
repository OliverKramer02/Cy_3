/*  1:   */ package org.apache.commons.math3.geometry.partitioning;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.geometry.Space;
/*  4:   */ 
/*  5:   */ class BoundarySizeVisitor<S extends Space>
/*  6:   */   implements BSPTreeVisitor<S>
/*  7:   */ {
/*  8:   */   private double boundarySize;
/*  9:   */   
/* 10:   */   public BoundarySizeVisitor()
/* 11:   */   {
/* 12:34 */     this.boundarySize = 0.0D;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public BSPTreeVisitor.Order visitOrder(BSPTree<S> node)
/* 16:   */   {
/* 17:39 */     return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void visitInternalNode(BSPTree<S> node)
/* 21:   */   {
/* 22:45 */     BoundaryAttribute<S> attribute = (BoundaryAttribute)node.getAttribute();
/* 23:47 */     if (attribute.getPlusOutside() != null) {
/* 24:48 */       this.boundarySize += attribute.getPlusOutside().getSize();
/* 25:   */     }
/* 26:50 */     if (attribute.getPlusInside() != null) {
/* 27:51 */       this.boundarySize += attribute.getPlusInside().getSize();
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void visitLeafNode(BSPTree<S> node) {}
/* 32:   */   
/* 33:   */   public double getSize()
/* 34:   */   {
/* 35:63 */     return this.boundarySize;
/* 36:   */   }
/* 37:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.BoundarySizeVisitor
 * JD-Core Version:    0.7.0.1
 */
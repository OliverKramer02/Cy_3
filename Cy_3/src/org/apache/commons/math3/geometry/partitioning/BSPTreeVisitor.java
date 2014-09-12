/*  1:   */ package org.apache.commons.math3.geometry.partitioning;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.geometry.Space;
/*  4:   */ 
/*  5:   */ public abstract interface BSPTreeVisitor<S extends Space>
/*  6:   */ {
/*  7:   */   public abstract Order visitOrder(BSPTree<S> paramBSPTree);
/*  8:   */   
/*  9:   */   public abstract void visitInternalNode(BSPTree<S> paramBSPTree);
/* 10:   */   
/* 11:   */   public abstract void visitLeafNode(BSPTree<S> paramBSPTree);
/* 12:   */   
/* 13:   */   public static enum Order
/* 14:   */   {
/* 15:58 */     PLUS_MINUS_SUB,  PLUS_SUB_MINUS,  MINUS_PLUS_SUB,  MINUS_SUB_PLUS,  SUB_PLUS_MINUS,  SUB_MINUS_PLUS;
/* 16:   */     
/* 17:   */     private Order() {}
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
 * JD-Core Version:    0.7.0.1
 */
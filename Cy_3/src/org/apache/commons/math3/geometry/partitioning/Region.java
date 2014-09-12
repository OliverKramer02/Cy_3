/*  1:   */ package org.apache.commons.math3.geometry.partitioning;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.geometry.Space;
/*  4:   */ import org.apache.commons.math3.geometry.Vector;
/*  5:   */ 
/*  6:   */ public abstract interface Region<S extends Space>
/*  7:   */ {
/*  8:   */   public abstract Region<S> buildNew(BSPTree<S> paramBSPTree);
/*  9:   */   
/* 10:   */   public abstract Region<S> copySelf();
/* 11:   */   
/* 12:   */   public abstract boolean isEmpty();
/* 13:   */   
/* 14:   */   public abstract boolean isEmpty(BSPTree<S> paramBSPTree);
/* 15:   */   
/* 16:   */   public abstract boolean contains(Region<S> paramRegion);
/* 17:   */   
/* 18:   */   public abstract Location checkPoint(Vector<S> paramVector);
/* 19:   */   
/* 20:   */   public abstract BSPTree<S> getTree(boolean paramBoolean);
/* 21:   */   
/* 22:   */   public abstract double getBoundarySize();
/* 23:   */   
/* 24:   */   public abstract double getSize();
/* 25:   */   
/* 26:   */   public abstract Vector<S> getBarycenter();
/* 27:   */   
/* 28:   */   public abstract Side side(Hyperplane<S> paramHyperplane);
/* 29:   */   
/* 30:   */   public abstract SubHyperplane<S> intersection(SubHyperplane<S> paramSubHyperplane);
/* 31:   */   
/* 32:   */   public static enum Location
/* 33:   */   {
/* 34:52 */     INSIDE,  OUTSIDE,  BOUNDARY;
/* 35:   */     
/* 36:   */     private Location() {}
/* 37:   */   }
/* 38:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.Region
 * JD-Core Version:    0.7.0.1
 */
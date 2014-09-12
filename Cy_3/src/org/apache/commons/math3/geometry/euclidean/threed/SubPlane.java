/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*   4:    */ import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
/*   5:    */ import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
/*   6:    */ import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
/*   7:    */ import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
/*   8:    */ import org.apache.commons.math3.geometry.partitioning.BSPTree;
/*   9:    */ import org.apache.commons.math3.geometry.partitioning.Hyperplane;
/*  10:    */ import org.apache.commons.math3.geometry.partitioning.Region;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.Side;
/*  12:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  13:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;
/*  14:    */ 
/*  15:    */ public class SubPlane
/*  16:    */   extends AbstractSubHyperplane<Euclidean3D, Euclidean2D>
/*  17:    */ {
/*  18:    */   public SubPlane(Hyperplane<Euclidean3D> hyperplane, Region<Euclidean2D> remainingRegion)
/*  19:    */   {
/*  20: 42 */     super(hyperplane, remainingRegion);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected AbstractSubHyperplane<Euclidean3D, Euclidean2D> buildNew(Hyperplane<Euclidean3D> hyperplane, Region<Euclidean2D> remainingRegion)
/*  24:    */   {
/*  25: 49 */     return new SubPlane(hyperplane, remainingRegion);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Side side(Hyperplane<Euclidean3D> hyperplane)
/*  29:    */   {
/*  30: 56 */     Plane otherPlane = (Plane)hyperplane;
/*  31: 57 */     Plane thisPlane = (Plane)getHyperplane();
/*  32: 58 */     Line inter = otherPlane.intersection(thisPlane);
/*  33: 60 */     if (inter == null)
/*  34:    */     {
/*  35: 63 */       double global = otherPlane.getOffset(thisPlane);
/*  36: 64 */       return global > 1.0E-010D ? Side.PLUS : global < -1.0E-010D ? Side.MINUS : Side.HYPER;
/*  37:    */     }
/*  38: 74 */     Vector2D p = thisPlane.toSubSpace(inter.toSpace(Vector1D.ZERO));
/*  39: 75 */     Vector2D q = thisPlane.toSubSpace(inter.toSpace(Vector1D.ONE));
/*  40: 76 */     Vector3D crossP = Vector3D.crossProduct(inter.getDirection(), thisPlane.getNormal());
/*  41: 77 */     if (crossP.dotProduct(otherPlane.getNormal()) < 0.0D)
/*  42:    */     {
/*  43: 78 */       Vector2D tmp = p;
/*  44: 79 */       p = q;
/*  45: 80 */       q = tmp;
/*  46:    */     }
/*  47: 82 */     org.apache.commons.math3.geometry.euclidean.twod.Line line2D = new org.apache.commons.math3.geometry.euclidean.twod.Line(p, q);
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51: 86 */     return getRemainingRegion().side(line2D);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public SubHyperplane.SplitSubHyperplane<Euclidean3D> split(Hyperplane<Euclidean3D> hyperplane)
/*  55:    */   {
/*  56: 99 */     Plane otherPlane = (Plane)hyperplane;
/*  57:100 */     Plane thisPlane = (Plane)getHyperplane();
/*  58:101 */     Line inter = otherPlane.intersection(thisPlane);
/*  59:103 */     if (inter == null)
/*  60:    */     {
/*  61:105 */       double global = otherPlane.getOffset(thisPlane);
/*  62:106 */       return global < -1.0E-010D ? new SubHyperplane.SplitSubHyperplane(null, this) : new SubHyperplane.SplitSubHyperplane(this, null);
/*  63:    */     }
/*  64:112 */     Vector2D p = thisPlane.toSubSpace(inter.toSpace(Vector1D.ZERO));
/*  65:113 */     Vector2D q = thisPlane.toSubSpace(inter.toSpace(Vector1D.ONE));
/*  66:114 */     Vector3D crossP = Vector3D.crossProduct(inter.getDirection(), thisPlane.getNormal());
/*  67:115 */     if (crossP.dotProduct(otherPlane.getNormal()) < 0.0D)
/*  68:    */     {
/*  69:116 */       Vector2D tmp = p;
/*  70:117 */       p = q;
/*  71:118 */       q = tmp;
/*  72:    */     }
/*  73:120 */     SubHyperplane<Euclidean2D> l2DMinus = new org.apache.commons.math3.geometry.euclidean.twod.Line(p, q).wholeHyperplane();
/*  74:    */     
/*  75:122 */     SubHyperplane<Euclidean2D> l2DPlus = new org.apache.commons.math3.geometry.euclidean.twod.Line(q, p).wholeHyperplane();
/*  76:    */     
/*  77:    */ 
/*  78:125 */     BSPTree<Euclidean2D> splitTree = getRemainingRegion().getTree(false).split(l2DMinus);
/*  79:126 */     BSPTree<Euclidean2D> plusTree = getRemainingRegion().isEmpty(splitTree.getPlus()) ? new BSPTree(Boolean.FALSE) : new BSPTree(l2DPlus, new BSPTree(Boolean.FALSE), splitTree.getPlus(), null);
/*  80:    */     
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:131 */     BSPTree<Euclidean2D> minusTree = getRemainingRegion().isEmpty(splitTree.getMinus()) ? new BSPTree(Boolean.FALSE) : new BSPTree(l2DMinus, new BSPTree(Boolean.FALSE), splitTree.getMinus(), null);
/*  85:    */     
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:136 */     return new SubHyperplane.SplitSubHyperplane(new SubPlane(thisPlane.copySelf(), new PolygonsSet(plusTree)), new SubPlane(thisPlane.copySelf(), new PolygonsSet(minusTree)));
/*  90:    */   }
/*  91:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.SubPlane
 * JD-Core Version:    0.7.0.1
 */
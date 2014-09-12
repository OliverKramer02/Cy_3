/*  1:   */ package org.apache.commons.math3.geometry.euclidean.oned;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
/*  4:   */ import org.apache.commons.math3.geometry.partitioning.Hyperplane;
/*  5:   */ import org.apache.commons.math3.geometry.partitioning.Region;
/*  6:   */ import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  7:   */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;
/*  8:   */ 
/*  9:   */ public class SubOrientedPoint
/* 10:   */   extends AbstractSubHyperplane<Euclidean1D, Euclidean1D>
/* 11:   */ {
/* 12:   */   public SubOrientedPoint(Hyperplane<Euclidean1D> hyperplane, Region<Euclidean1D> remainingRegion)
/* 13:   */   {
/* 14:39 */     super(hyperplane, remainingRegion);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public double getSize()
/* 18:   */   {
/* 19:45 */     return 0.0D;
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected AbstractSubHyperplane<Euclidean1D, Euclidean1D> buildNew(Hyperplane<Euclidean1D> hyperplane, Region<Euclidean1D> remainingRegion)
/* 23:   */   {
/* 24:52 */     return new SubOrientedPoint(hyperplane, remainingRegion);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Side side(Hyperplane<Euclidean1D> hyperplane)
/* 28:   */   {
/* 29:58 */     double global = hyperplane.getOffset(((OrientedPoint)getHyperplane()).getLocation());
/* 30:59 */     return global > 1.0E-010D ? Side.PLUS : global < -1.0E-010D ? Side.MINUS : Side.HYPER;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public SubHyperplane.SplitSubHyperplane<Euclidean1D> split1(Hyperplane<Euclidean1D> hyperplane)
/* 34:   */   {
/* 35:65 */     double global = hyperplane.getOffset(((OrientedPoint)getHyperplane()).getLocation());
/* 36:66 */     return global < -1.0E-010D ? new SubHyperplane.SplitSubHyperplane(null, this) : new SubHyperplane.SplitSubHyperplane(this, null);
/* 37:   */   }
/* 38:   */
@Override
public org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane<Euclidean1D> split(
		Hyperplane<Euclidean1D> paramHyperplane) {
	// TODO Auto-generated method stub
	return null;
} }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.oned.SubOrientedPoint
 * JD-Core Version:    0.7.0.1
 */
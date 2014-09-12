/*   1:    */ package org.apache.commons.math3.geometry.euclidean.twod;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
/*   6:    */ import org.apache.commons.math3.geometry.euclidean.oned.Interval;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
/*   8:    */ import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
/*   9:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*  10:    */ import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.BSPTree;
/*  12:    */ import org.apache.commons.math3.geometry.partitioning.Hyperplane;
/*  13:    */ import org.apache.commons.math3.geometry.partitioning.Region;
/*  14:    */ import org.apache.commons.math3.geometry.partitioning.Region.Location;
/*  15:    */ import org.apache.commons.math3.geometry.partitioning.Side;
/*  16:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  17:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;
/*  18:    */ import org.apache.commons.math3.util.FastMath;
/*  19:    */ 
/*  20:    */ public class SubLine
/*  21:    */   extends AbstractSubHyperplane<Euclidean2D, Euclidean1D>
/*  22:    */ {
/*  23:    */   public SubLine(Hyperplane<Euclidean2D> hyperplane, Region<Euclidean1D> remainingRegion)
/*  24:    */   {
/*  25: 48 */     super(hyperplane, remainingRegion);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public SubLine(Vector2D start, Vector2D end)
/*  29:    */   {
/*  30: 56 */     super(new Line(start, end), buildIntervalSet(start, end));
/*  31:    */   }
/*  32:    */   
/*  33:    */   public SubLine(Segment segment)
/*  34:    */   {
/*  35: 63 */     super(segment.getLine(), buildIntervalSet(segment.getStart(), segment.getEnd()));
/*  36:    */   }
/*  37:    */   
/*  38:    */   public List<Segment> getSegments()
/*  39:    */   {
/*  40: 82 */     Line line = (Line)getHyperplane();
/*  41: 83 */     List<Interval> list = ((IntervalsSet)getRemainingRegion()).asList();
/*  42: 84 */     List<Segment> segments = new ArrayList();
/*  43: 86 */     for (Interval interval : list)
/*  44:    */     {
/*  45: 87 */       Vector2D start = line.toSpace(new Vector1D(interval.getLower()));
/*  46: 88 */       Vector2D end = line.toSpace(new Vector1D(interval.getUpper()));
/*  47: 89 */       segments.add(new Segment(start, end, line));
/*  48:    */     }
/*  49: 92 */     return segments;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Vector2D intersection(SubLine subLine, boolean includeEndPoints)
/*  53:    */   {
/*  54:113 */     Line line1 = (Line)getHyperplane();
/*  55:114 */     Line line2 = (Line)subLine.getHyperplane();
/*  56:    */     
/*  57:    */ 
/*  58:117 */     Vector2D v2D = line1.intersection(line2);
/*  59:    */     
/*  60:    */ 
/*  61:120 */     Region.Location loc1 = getRemainingRegion().checkPoint(line1.toSubSpace(v2D));
/*  62:    */     
/*  63:    */ 
/*  64:123 */     Region.Location loc2 = subLine.getRemainingRegion().checkPoint(line2.toSubSpace(v2D));
/*  65:125 */     if (includeEndPoints) {
/*  66:126 */       return (loc1 != Region.Location.OUTSIDE) && (loc2 != Region.Location.OUTSIDE) ? v2D : null;
/*  67:    */     }
/*  68:128 */     return (loc1 == Region.Location.INSIDE) && (loc2 == Region.Location.INSIDE) ? v2D : null;
/*  69:    */   }
/*  70:    */   
/*  71:    */   private static IntervalsSet buildIntervalSet(Vector2D start, Vector2D end)
/*  72:    */   {
/*  73:139 */     Line line = new Line(start, end);
/*  74:140 */     return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected AbstractSubHyperplane<Euclidean2D, Euclidean1D> buildNew(Hyperplane<Euclidean2D> hyperplane, Region<Euclidean1D> remainingRegion)
/*  78:    */   {
/*  79:148 */     return new SubLine(hyperplane, remainingRegion);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Side side(Hyperplane<Euclidean2D> hyperplane)
/*  83:    */   {
/*  84:155 */     Line thisLine = (Line)getHyperplane();
/*  85:156 */     Line otherLine = (Line)hyperplane;
/*  86:157 */     Vector2D crossing = thisLine.intersection(otherLine);
/*  87:159 */     if (crossing == null)
/*  88:    */     {
/*  89:161 */       double global = otherLine.getOffset(thisLine);
/*  90:162 */       return global > 1.0E-010D ? Side.PLUS : global < -1.0E-010D ? Side.MINUS : Side.HYPER;
/*  91:    */     }
/*  92:166 */     boolean direct = FastMath.sin(thisLine.getAngle() - otherLine.getAngle()) < 0.0D;
/*  93:167 */     Vector1D x = thisLine.toSubSpace(crossing);
/*  94:168 */     return getRemainingRegion().side(new OrientedPoint(x, direct));
/*  95:    */   }
/*  96:    */   
/*  97:    */   public SubHyperplane.SplitSubHyperplane<Euclidean2D> split(Hyperplane<Euclidean2D> hyperplane)
/*  98:    */   {
/*  99:176 */     Line thisLine = (Line)getHyperplane();
/* 100:177 */     Line otherLine = (Line)hyperplane;
/* 101:178 */     Vector2D crossing = thisLine.intersection(otherLine);
/* 102:180 */     if (crossing == null)
/* 103:    */     {
/* 104:182 */       double global = otherLine.getOffset(thisLine);
/* 105:183 */       return global < -1.0E-010D ? new SubHyperplane.SplitSubHyperplane(null, this) : new SubHyperplane.SplitSubHyperplane(this, null);
/* 106:    */     }
/* 107:189 */     boolean direct = FastMath.sin(thisLine.getAngle() - otherLine.getAngle()) < 0.0D;
/* 108:190 */     Vector1D x = thisLine.toSubSpace(crossing);
/* 109:191 */     SubHyperplane<Euclidean1D> subPlus = new OrientedPoint(x, !direct).wholeHyperplane();
/* 110:192 */     SubHyperplane<Euclidean1D> subMinus = new OrientedPoint(x, direct).wholeHyperplane();
/* 111:    */     
/* 112:194 */     BSPTree<Euclidean1D> splitTree = getRemainingRegion().getTree(false).split(subMinus);
/* 113:195 */     BSPTree<Euclidean1D> plusTree = getRemainingRegion().isEmpty(splitTree.getPlus()) ? new BSPTree(Boolean.FALSE) : new BSPTree(subPlus, new BSPTree(Boolean.FALSE), splitTree.getPlus(), null);
/* 114:    */     
/* 115:    */ 
/* 116:    */ 
/* 117:199 */     BSPTree<Euclidean1D> minusTree = getRemainingRegion().isEmpty(splitTree.getMinus()) ? new BSPTree(Boolean.FALSE) : new BSPTree(subMinus, new BSPTree(Boolean.FALSE), splitTree.getMinus(), null);
/* 118:    */     
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:204 */     return new SubHyperplane.SplitSubHyperplane(new SubLine(thisLine.copySelf(), new IntervalsSet(plusTree)), new SubLine(thisLine.copySelf(), new IntervalsSet(minusTree)));
/* 123:    */   }
/* 124:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.SubLine
 * JD-Core Version:    0.7.0.1
 */
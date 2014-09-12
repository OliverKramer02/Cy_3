/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;

import javax.swing.plaf.synth.Region;

/*   5:    */ import org.apache.commons.math3.geometry.euclidean.oned.Interval;
/*   6:    */ import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*   8:    */ import org.apache.commons.math3.geometry.partitioning.Region.Location;
/*   9:    */ 
/*  10:    */ public class SubLine
/*  11:    */ {
/*  12:    */   private final Line line;
/*  13:    */   private final IntervalsSet remainingRegion;
/*  14:    */   
/*  15:    */   public SubLine(Line line, IntervalsSet remainingRegion)
/*  16:    */   {
/*  17: 44 */     this.line = line;
/*  18: 45 */     this.remainingRegion = remainingRegion;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public SubLine(Vector3D start, Vector3D end)
/*  22:    */   {
/*  23: 53 */     this(new Line(start, end), buildIntervalSet(start, end));
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SubLine(Segment segment)
/*  27:    */   {
/*  28: 60 */     this(segment.getLine(), buildIntervalSet(segment.getStart(), segment.getEnd()));
/*  29:    */   }
/*  30:    */   
/*  31:    */   public List<Segment> getSegments()
/*  32:    */   {
/*  33: 79 */     List<Interval> list = this.remainingRegion.asList();
/*  34: 80 */     List<Segment> segments = new ArrayList();
/*  35: 82 */     for (Interval interval : list)
/*  36:    */     {
/*  37: 83 */       Vector3D start = this.line.toSpace(new Vector1D(interval.getLower()));
/*  38: 84 */       Vector3D end = this.line.toSpace(new Vector1D(interval.getUpper()));
/*  39: 85 */       segments.add(new Segment(start, end, this.line));
/*  40:    */     }
/*  41: 88 */     return segments;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Vector3D intersection(SubLine subLine, boolean includeEndPoints)
/*  45:    */   {
/*  46:109 */     Vector3D v1D = this.line.intersection(subLine.line);
/*  47:    */     
/*  48:    */ 
/*  49:112 */     Location loc1 = this.remainingRegion.checkPoint(this.line.toSubSpace(v1D));
/*  50:    */     
/*  51:    */ 
/*  52:115 */     Location loc2 = subLine.remainingRegion.checkPoint(subLine.line.toSubSpace(v1D));
/*  53:117 */     if (includeEndPoints) {
/*  54:118 */       return (loc1 != loc2.OUTSIDE) && (loc2 != loc1.OUTSIDE) ? v1D : null;
/*  55:    */     }
/*  56:120 */     return (loc1 == loc2.INSIDE) && (loc2 == loc1.INSIDE) ? v1D : null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   private static IntervalsSet buildIntervalSet(Vector3D start, Vector3D end)
/*  60:    */   {
/*  61:131 */     Line line = new Line(start, end);
/*  62:132 */     return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
/*  63:    */   }
/*  64:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.SubLine
 * JD-Core Version:    0.7.0.1
 */
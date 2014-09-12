/*   1:    */ package org.apache.commons.math3.geometry.euclidean.oned;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
/*   7:    */ import org.apache.commons.math3.geometry.partitioning.BSPTree;
/*   8:    */ import org.apache.commons.math3.geometry.partitioning.Region.Location;
/*   9:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  10:    */ 
/*  11:    */ public class IntervalsSet
/*  12:    */   extends AbstractRegion<Euclidean1D, Euclidean1D>
/*  13:    */ {
/*  14:    */   public IntervalsSet() {}
/*  15:    */   
/*  16:    */   public IntervalsSet(double lower, double upper)
/*  17:    */   {
/*  18: 46 */     super(buildTree(lower, upper));
/*  19:    */   }
/*  20:    */   
/*  21:    */   public IntervalsSet(BSPTree<Euclidean1D> tree)
/*  22:    */   {
/*  23: 59 */     super(tree);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public IntervalsSet(Collection<SubHyperplane<Euclidean1D>> boundary)
/*  27:    */   {
/*  28: 82 */     super(boundary);
/*  29:    */   }
/*  30:    */   
/*  31:    */   private static BSPTree<Euclidean1D> buildTree(double lower, double upper)
/*  32:    */   {
/*  33: 93 */     if ((Double.isInfinite(lower)) && (lower < 0.0D))
/*  34:    */     {
/*  35: 94 */       if ((Double.isInfinite(upper)) && (upper > 0.0D)) {
/*  36: 96 */         return new BSPTree(Boolean.TRUE);
/*  37:    */       }
/*  38: 99 */       SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
/*  39:    */       
/*  40:101 */       return new BSPTree(upperCut, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null);
/*  41:    */     }
/*  42:106 */     SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(lower), false).wholeHyperplane();
/*  43:108 */     if ((Double.isInfinite(upper)) && (upper > 0.0D)) {
/*  44:110 */       return new BSPTree(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null);
/*  45:    */     }
/*  46:117 */     SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
/*  47:    */     
/*  48:119 */     return new BSPTree(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(upperCut, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), null);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public IntervalsSet buildNew(BSPTree<Euclidean1D> tree)
/*  52:    */   {
/*  53:132 */     return new IntervalsSet(tree);
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void computeGeometricalProperties()
/*  57:    */   {
/*  58:138 */     if (getTree(false).getCut() == null)
/*  59:    */     {
/*  60:139 */       setBarycenter(Vector1D.NaN);
/*  61:140 */       setSize(((Boolean)getTree(false).getAttribute()).booleanValue() ? (1.0D / 0.0D) : 0.0D);
/*  62:    */     }
/*  63:    */     else
/*  64:    */     {
/*  65:142 */       double size = 0.0D;
/*  66:143 */       double sum = 0.0D;
/*  67:144 */       for (Interval interval : asList())
/*  68:    */       {
/*  69:145 */         size += interval.getLength();
/*  70:146 */         sum += interval.getLength() * interval.getMidPoint();
/*  71:    */       }
/*  72:148 */       setSize(size);
/*  73:149 */       setBarycenter(Double.isInfinite(size) ? Vector1D.NaN : new Vector1D(sum / size));
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public double getInf()
/*  78:    */   {
/*  79:160 */     BSPTree<Euclidean1D> node = getTree(false);
/*  80:161 */     double inf = (1.0D / 0.0D);
/*  81:162 */     while (node.getCut() != null)
/*  82:    */     {
/*  83:163 */       OrientedPoint op = (OrientedPoint)node.getCut().getHyperplane();
/*  84:164 */       inf = op.getLocation().getX();
/*  85:165 */       node = op.isDirect() ? node.getMinus() : node.getPlus();
/*  86:    */     }
/*  87:167 */     return ((Boolean)node.getAttribute()).booleanValue() ? (-1.0D / 0.0D) : inf;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public double getSup()
/*  91:    */   {
/*  92:177 */     BSPTree<Euclidean1D> node = getTree(false);
/*  93:178 */     double sup = (-1.0D / 0.0D);
/*  94:179 */     while (node.getCut() != null)
/*  95:    */     {
/*  96:180 */       OrientedPoint op = (OrientedPoint)node.getCut().getHyperplane();
/*  97:181 */       sup = op.getLocation().getX();
/*  98:182 */       node = op.isDirect() ? node.getPlus() : node.getMinus();
/*  99:    */     }
/* 100:184 */     return ((Boolean)node.getAttribute()).booleanValue() ? (1.0D / 0.0D) : sup;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public List<Interval> asList()
/* 104:    */   {
/* 105:201 */     List<Interval> list = new ArrayList();
/* 106:202 */     recurseList(getTree(false), list, (-1.0D / 0.0D), (1.0D / 0.0D));
/* 107:    */     
/* 108:204 */     return list;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private void recurseList(BSPTree<Euclidean1D> node, List<Interval> list, double lower, double upper)
/* 112:    */   {
/* 113:217 */     if (node.getCut() == null)
/* 114:    */     {
/* 115:218 */       if (((Boolean)node.getAttribute()).booleanValue()) {
/* 116:220 */         list.add(new Interval(lower, upper));
/* 117:    */       }
/* 118:    */     }
/* 119:    */     else
/* 120:    */     {
/* 121:223 */       OrientedPoint op = (OrientedPoint)node.getCut().getHyperplane();
/* 122:224 */       Vector1D loc = op.getLocation();
/* 123:225 */       double x = loc.getX();
/* 124:    */       
/* 125:    */ 
/* 126:228 */       BSPTree<Euclidean1D> low = op.isDirect() ? node.getMinus() : node.getPlus();
/* 127:    */       
/* 128:230 */       BSPTree<Euclidean1D> high = op.isDirect() ? node.getPlus() : node.getMinus();
/* 129:    */       
/* 130:    */ 
/* 131:233 */       recurseList(low, list, lower, x);
/* 132:234 */       if ((checkPoint(low, loc) == Location.INSIDE) && (checkPoint(high, loc) == Location.INSIDE)) {
/* 133:237 */         x = ((Interval)list.remove(list.size() - 1)).getLower();
/* 134:    */       }
/* 135:239 */       recurseList(high, list, x, upper);
/* 136:    */     }
/* 137:    */   }
/* 138:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet
 * JD-Core Version:    0.7.0.1
 */
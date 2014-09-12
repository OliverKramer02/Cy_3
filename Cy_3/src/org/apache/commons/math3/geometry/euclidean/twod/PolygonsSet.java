/*   1:    */ package org.apache.commons.math3.geometry.euclidean.twod;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.List;

/*   6:    */ import org.apache.commons.math3.exception.MathInternalError;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
/*   8:    */ import org.apache.commons.math3.geometry.euclidean.oned.Interval;
/*   9:    */ import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
/*  10:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
/*  12:    */ import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
/*  13:    */ import org.apache.commons.math3.geometry.partitioning.BSPTree;
/*  14:    */ import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
/*  16:    */ import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
/*  17:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  20:    */ import org.apache.commons.math3.geometry.partitioning.utilities.OrderedTuple;
/*  21:    */ import org.apache.commons.math3.util.FastMath;
/*  15:    */ 
/*  18:    */ 
/*  19:    */ 
/*  22:    */ 
/*  23:    */ public class PolygonsSet
/*  24:    */   extends AbstractRegion<Euclidean2D, Euclidean1D>
/*  25:    */ {
/*  26:    */   private Vector2D[][] vertices;
/*  27:    */   
/*  28:    */   public PolygonsSet() {}
/*  29:    */   
/*  30:    */   public PolygonsSet(BSPTree<Euclidean2D> tree)
/*  31:    */   {
/*  32: 63 */     super(tree);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public PolygonsSet(Collection<SubHyperplane<Euclidean2D>> boundary)
/*  36:    */   {
/*  37: 87 */     super(boundary);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public PolygonsSet(double xMin, double xMax, double yMin, double yMax)
/*  41:    */   {
/*  42: 98 */     super(boxBoundary(xMin, xMax, yMin, yMax));
/*  43:    */   }
/*  44:    */   
/*  45:    */   private static Line[] boxBoundary(double xMin, double xMax, double yMin, double yMax)
/*  46:    */   {
/*  47:110 */     Vector2D minMin = new Vector2D(xMin, yMin);
/*  48:111 */     Vector2D minMax = new Vector2D(xMin, yMax);
/*  49:112 */     Vector2D maxMin = new Vector2D(xMax, yMin);
/*  50:113 */     Vector2D maxMax = new Vector2D(xMax, yMax);
/*  51:114 */     return new Line[] { new Line(minMin, maxMin), new Line(maxMin, maxMax), new Line(maxMax, minMax), new Line(minMax, minMin) };
/*  52:    */   }
/*  53:    */   
/*  54:    */   public PolygonsSet buildNew(BSPTree<Euclidean2D> tree)
/*  55:    */   {
/*  56:125 */     return new PolygonsSet(tree);
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void computeGeometricalProperties()
/*  60:    */   {
/*  61:132 */     Vector2D[][] v = getVertices();
/*  62:134 */     if (v.length == 0)
/*  63:    */     {
/*  64:135 */       if (((Boolean)getTree(false).getAttribute()).booleanValue())
/*  65:    */       {
/*  66:136 */         setSize((1.0D / 0.0D));
/*  67:137 */         setBarycenter(Vector2D.NaN);
/*  68:    */       }
/*  69:    */       else
/*  70:    */       {
/*  71:139 */         setSize(0.0D);
/*  72:140 */         setBarycenter(new Vector2D(0.0D, 0.0D));
/*  73:    */       }
/*  74:    */     }
/*  75:142 */     else if (v[0][0] == null)
/*  76:    */     {
/*  77:144 */       setSize((1.0D / 0.0D));
/*  78:145 */       setBarycenter(Vector2D.NaN);
/*  79:    */     }
/*  80:    */     else
/*  81:    */     {
/*  82:149 */       double sum = 0.0D;
/*  83:150 */       double sumX = 0.0D;
/*  84:151 */       double sumY = 0.0D;
/*  85:153 */       for (Vector2D[] loop : v)
/*  86:    */       {
/*  87:154 */         double x1 = loop[(loop.length - 1)].getX();
/*  88:155 */         double y1 = loop[(loop.length - 1)].getY();
/*  89:156 */         for (Vector2D point : loop)
/*  90:    */         {
/*  91:157 */           double x0 = x1;
/*  92:158 */           double y0 = y1;
/*  93:159 */           x1 = point.getX();
/*  94:160 */           y1 = point.getY();
/*  95:161 */           double factor = x0 * y1 - y0 * x1;
/*  96:162 */           sum += factor;
/*  97:163 */           sumX += factor * (x0 + x1);
/*  98:164 */           sumY += factor * (y0 + y1);
/*  99:    */         }
/* 100:    */       }
/* 101:168 */       if (sum < 0.0D)
/* 102:    */       {
/* 103:170 */         setSize((1.0D / 0.0D));
/* 104:171 */         setBarycenter(Vector2D.NaN);
/* 105:    */       }
/* 106:    */       else
/* 107:    */       {
/* 108:173 */         setSize(sum / 2.0D);
/* 109:174 */         setBarycenter(new Vector2D(sumX / (3.0D * sum), sumY / (3.0D * sum)));
/* 110:    */       }
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public Vector2D[][] getVertices()
/* 115:    */   {
/* 116:    */     int i;
/* 117:205 */     if (this.vertices == null) {
/* 118:206 */       if (getTree(false).getCut() == null)
/* 119:    */       {
/* 120:207 */         this.vertices = new Vector2D[0][];
/* 121:    */       }
/* 122:    */       else
/* 123:    */       {
/* 124:211 */         SegmentsBuilder visitor = new SegmentsBuilder();
/* 125:212 */         getTree(true).visit(visitor);
/* 126:213 */       //  AVLTree<ComparableSegment> sorted = visitor.getSorted();
/* 127:    */         
/* 128:    */ 
/* 129:    */ 
/* 130:217 */         ArrayList<List<ComparableSegment>> loops = new ArrayList();
/* 131:218 */         //while (!sorted.isEmpty())
/* 132:    */         //{
/* 133:219 */         //  AVLTree<ComparableSegment>.Node node = sorted.getSmallest();
/* 134:220 */           //List<ComparableSegment> loop = followLoop(node, sorted);
/* 135:221 */           //if (loop != null) {
/* 136:222 */            // loops.add(loop);
/* 137:    */          // }
/* 138:    */         }
/* 139:227 */         //this.vertices = new Vector2D[loops.size()][];
/* 140:228 */         i = 0;
/* 141:230 */         //for (List<ComparableSegment> loop : loops) {
/* 142:231 */          // if (loop.size() < 2)
/* 143:    */          // {
/* 144:233 */           //  Line line = ((ComparableSegment)loop.get(0)).getLine();
/* 145:234 */             //this.vertices[(i++)] = {  line.toSpace(new Vector1D(-3.402823466385289E+038D)), line.toSpace(new Vector1D(3.402823466385289E+038D)) };
/* 146:    */           //}
/* 147:239 */           //else if (((ComparableSegment)loop.get(0)).getStart() == null)
/* 148:    */           {
/* 149:241 */             //Vector2D[] array = new Vector2D[loop.size() + 2];
/* 150:242 */             int j = 0;
/* 151:243 */             //for (Segment segment : loop)
/* 152:    */             //{
/* 153:245 */              // if (j == 0)
/* 154:    */               //{
/* 155:247 */                // double x = segment.getLine().toSubSpace(segment.getEnd()).getX();
/* 156:248 */                 //x -= FastMath.max(1.0D, FastMath.abs(x / 2.0D));
/* 157:249 */                 //array[(j++)] = null;
/* 158:250 */                 //array[(j++)] = segment.getLine().toSpace(new Vector1D(x));
/* 159:    */               }
/* 160:253 */               //if (j < array.length - 1) {
/* 161:255 */                // array[(j++)] = segment.getEnd();
/* 162:    */              // }
/* 163:258 */               //if (j == array.length - 1)
/* 164:    */               //{
/* 165:260 */                // double x = segment.getLine().toSubSpace(segment.getStart()).getX();
/* 166:261 */                 //x += FastMath.max(1.0D, FastMath.abs(x / 2.0D));
/* 167:262 */                 //array[(j++)] = segment.getLine().toSpace(new Vector1D(x));
/* 168:    */               //}
/* 169:    */             //}
/* 170:266 */             //this.vertices[(i++)] = array;
/* 171:    */           }
/* 172:    */           else
/* 173:    */           {
/* 174:268 */           //  Vector2D[] array = new Vector2D[loop.size()];
/* 175:269 */             int j = 0;
/* 176:270 */            // for (Segment segment : loop) {
/* 177:271 */             //  array[(j++)] = segment.getStart();
/* 178:    */             //}
/* 179:273 */             //this.vertices[(i++)] = array;
/* 180:    */           }
/* 181:    */       //  }
/* 182:    */       //}
/* 183:    */     //}
/* 184:280 */     //return (Vector2D[][])this.vertices.clone();
/* 185:    */   //}
/* 186:    */   
/* 187:    */   //private List<ComparableSegment> followLoop(AVLTree<ComparableSegment>.Node node, AVLTree<ComparableSegment> sorted)
/* 188:    */   //{
/* 189:294 */    // ArrayList<ComparableSegment> loop = new ArrayList();
/* 190:295 */     //ComparableSegment segment = (ComparableSegment)node.getElement();
/* 191:296 */     //loop.add(segment);
/* 192:297 */     //Vector2D globalStart = segment.getStart();
/* 193:298 */     //Vector2D end = segment.getEnd();
/* 194:299 */     //node.delete();
/* 195:    */     
/* 196:    */ 
/* 197:302 */    // boolean open = segment.getStart() == null;
/* 198:304 */     //while ((end != null) && ((open) || (globalStart.distance(end) > 1.0E-010D)))
/* 199:    */     //{
/* 200:307 */      // AVLTree<ComparableSegment>.Node selectedNode = null;
/* 201:308 */       //ComparableSegment selectedSegment = null;
/* 202:309 */       //double selectedDistance = (1.0D / 0.0D);
/* 203:310 */       //ComparableSegment lowerLeft = new ComparableSegment(end, -1.0E-010D, -1.0E-010D);
/* 204:311 */       //ComparableSegment upperRight = new ComparableSegment(end, 1.0E-010D, 1.0E-010D);
/* 205:312 */       //for (AVLTree<ComparableSegment>.Node n = sorted.getNotSmaller(lowerLeft); (n != null) && (((ComparableSegment)n.getElement()).compareTo(upperRight) <= 0); n = n.getNext())
/* 206:    */       //{
/* 207:315 */        // segment = (ComparableSegment)n.getElement();
/* 208:316 */         //double distance = end.distance(segment.getStart());
/* 209:317 */         //if (distance < selectedDistance)
/* 210:    */         //{
/* 211:318 */          // selectedNode = n;
/* 212:319 */           //selectedSegment = segment;
/* 213:320 */           //selectedDistance = distance;
/* 214:    */         //}
/* 215:    */       //}
/* 216:324 */       //if (selectedDistance > 1.0E-010D) {
/* 217:328 */     //    return null;
/* 218:    */      // }
/* 219:331 */       //end = selectedSegment.getEnd();
/* 220:332 */       //loop.add(selectedSegment);
/* 221:333 */       //selectedNode.delete();
/* 222:    */    // }
/* 223:337 */     //if ((loop.size() == 2) && (!open)) {
/* 224:339 */      // return null;
/* 225:    */     //}
/* 226:342 */     //if ((end == null) && (!open)) {
/* 227:343 */      // throw new MathInternalError();
/* 228:    */     //}
/* 229:346 */     //return loop;
/* 230:    */
return vertices;   }
/* 231:    */   
/* 232:    */   private static class ComparableSegment
/* 233:    */     extends Segment
/* 234:    */     implements Comparable<ComparableSegment>
/* 235:    */   {
/* 236:    */     private OrderedTuple sortingKey;
/* 237:    */     
/* 238:    */     public ComparableSegment(Vector2D start, Vector2D end, Line line)
/* 239:    */     {
/* 240:362 */       super(end, end, line);
/* 241:363 */       this.sortingKey = (start == null ? new OrderedTuple(new double[] { (-1.0D / 0.0D), (-1.0D / 0.0D) }) : new OrderedTuple(new double[] { start.getX(), start.getY() }));
/* 242:    */     }
/* 243:    */     
/* 244:    */     public ComparableSegment(Vector2D start, double dx, double dy)
/* 245:    */     {
/* 246:378 */       super(null, null, null);
/* 247:379 */       this.sortingKey = new OrderedTuple(new double[] { start.getX() + dx, start.getY() + dy });
/* 248:    */     }
/* 249:    */     
/* 250:    */     public int compareTo(ComparableSegment o)
/* 251:    */     {
/* 252:384 */       return this.sortingKey.compareTo(o.sortingKey);
/* 253:    */     }
/* 254:    */     
/* 255:    */     public boolean equals(Object other)
/* 256:    */     {
/* 257:390 */       if (this == other) {
/* 258:391 */         return true;
/* 259:    */       }
/* 260:392 */       if ((other instanceof ComparableSegment)) {
/* 261:393 */         return compareTo((ComparableSegment)other) == 0;
/* 262:    */       }
/* 263:395 */       return false;
/* 264:    */     }
/* 265:    */     
/* 266:    */     public int hashCode()
/* 267:    */     {
/* 268:402 */       return getStart().hashCode() ^ getEnd().hashCode() ^ getLine().hashCode() ^ this.sortingKey.hashCode();
/* 269:    */     }
/* 270:    */   }
/* 271:    */   
/* 272:    */   private static class SegmentsBuilder
/* 273:    */     implements BSPTreeVisitor<Euclidean2D>
/* 274:    */   {
/* 275:    */  //   private AVLTree<PolygonsSet.ComparableSegment> sorted;
/* 276:    */     
/* 277:    */     public SegmentsBuilder()
/* 278:    */     {
/* 279:416 */   //    this.sorted = new AVLTree();
/* 280:    */     }
/* 281:    */     
/* 282:    */     public BSPTreeVisitor.Order visitOrder(BSPTree<Euclidean2D> node)
/* 283:    */     {
/* 284:421 */       return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
/* 285:    */     }
/* 286:    */     
/* 287:    */     public void visitInternalNode(BSPTree<Euclidean2D> node)
/* 288:    */     {
/* 289:427 */       BoundaryAttribute<Euclidean2D> attribute = (BoundaryAttribute)node.getAttribute();
/* 290:428 */       if (attribute.getPlusOutside() != null) {
/* 291:429 */         addContribution(attribute.getPlusOutside(), false);
/* 292:    */       }
/* 293:431 */       if (attribute.getPlusInside() != null) {
/* 294:432 */         addContribution(attribute.getPlusInside(), true);
/* 295:    */       }
/* 296:    */     }
/* 297:    */     
/* 298:    */     public void visitLeafNode(BSPTree<Euclidean2D> node) {}
/* 299:    */     
/* 300:    */     private void addContribution(SubHyperplane<Euclidean2D> sub, boolean reversed)
/* 301:    */     {
/* 302:446 */       AbstractSubHyperplane<Euclidean2D, Euclidean1D> absSub = (AbstractSubHyperplane)sub;
/* 303:    */       
/* 304:448 */       Line line = (Line)sub.getHyperplane();
/* 305:449 */       List<Interval> intervals = ((IntervalsSet)absSub.getRemainingRegion()).asList();
/* 306:450 */       for (Interval i : intervals)
/* 307:    */       {
/* 308:451 */         Vector2D start = Double.isInfinite(i.getLower()) ? null : line.toSpace(new Vector1D(i.getLower()));
/* 309:    */         
/* 310:453 */         Vector2D end = Double.isInfinite(i.getUpper()) ? null : line.toSpace(new Vector1D(i.getUpper()));
/* 311:455 */         if (reversed) {
/* 312:456 */           //this.sorted.insert(new PolygonsSet.ComparableSegment(end, start, line.getReverse()));
/* 313:    */         } else {
/* 314:458 */         //  this.sorted.insert(new PolygonsSet.ComparableSegment(start, end, line));
/* 315:    */         }
/* 316:    */       }
/* 317:    */     }
/* 318:    */     
/* 319:    */     //public AVLTree<PolygonsSet.ComparableSegment> getSorted()
/* 320:    */     //{
/* 321:467 */     //  return this.sorted;
/* 322:    */     //}
/* 323:    */   }
/* 324:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet
 * JD-Core Version:    0.7.0.1
 */
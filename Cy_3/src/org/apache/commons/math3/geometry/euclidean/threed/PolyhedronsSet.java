/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ import java.awt.geom.AffineTransform;
/*   4:    */ import java.util.Collection;
/*   5:    */ import org.apache.commons.math3.geometry.Vector;
/*   6:    */ import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
/*   8:    */ import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
/*   9:    */ import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
/*  10:    */ import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.BSPTree;
/*  12:    */ import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
/*  13:    */ import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
/*  14:    */ import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
/*  15:    */ import org.apache.commons.math3.geometry.partitioning.Hyperplane;
/*  16:    */ import org.apache.commons.math3.geometry.partitioning.Region;
/*  17:    */ import org.apache.commons.math3.geometry.partitioning.Region.Location;
/*  18:    */ import org.apache.commons.math3.geometry.partitioning.RegionFactory;
/*  19:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  20:    */ import org.apache.commons.math3.geometry.partitioning.Transform;
/*  21:    */ import org.apache.commons.math3.util.FastMath;
/*  22:    */ 
/*  23:    */ public class PolyhedronsSet
/*  24:    */   extends AbstractRegion<Euclidean3D, Euclidean2D>
/*  25:    */ {
/*  26:    */   public PolyhedronsSet() {}
/*  27:    */   
/*  28:    */   public PolyhedronsSet(BSPTree<Euclidean3D> tree)
/*  29:    */   {
/*  30: 60 */     super(tree);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public PolyhedronsSet(Collection<SubHyperplane<Euclidean3D>> boundary)
/*  34:    */   {
/*  35: 83 */     super(boundary);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public PolyhedronsSet(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax)
/*  39:    */   {
/*  40: 98 */     this(new RegionFactory().buildConvex(new Hyperplane[] { new Plane(new Vector3D(xMin, 0.0D, 0.0D), Vector3D.MINUS_I), new Plane(new Vector3D(xMax, 0.0D, 0.0D), Vector3D.PLUS_I), new Plane(new Vector3D(0.0D, yMin, 0.0D), Vector3D.MINUS_J), new Plane(new Vector3D(0.0D, yMax, 0.0D), Vector3D.PLUS_J), new Plane(new Vector3D(0.0D, 0.0D, zMin), Vector3D.MINUS_K), new Plane(new Vector3D(0.0D, 0.0D, zMax), Vector3D.PLUS_K) }).getTree(false));
/*  41:    */   }
/*  42:    */   
/*  43:    */   public PolyhedronsSet buildNew(BSPTree<Euclidean3D> tree)
/*  44:    */   {
/*  45:110 */     return new PolyhedronsSet(tree);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected void computeGeometricalProperties()
/*  49:    */   {
/*  50:118 */     getTree(true).visit(new FacetsContributionVisitor());
/*  51:120 */     if (getSize() < 0.0D)
/*  52:    */     {
/*  53:123 */       setSize((1.0D / 0.0D));
/*  54:124 */       setBarycenter(Vector3D.NaN);
/*  55:    */     }
/*  56:    */     else
/*  57:    */     {
/*  58:127 */       setSize(getSize() / 3.0D);
/*  59:128 */       setBarycenter(new Vector3D(1.0D / (4.0D * getSize()), (Vector3D)getBarycenter()));
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   private class FacetsContributionVisitor
/*  64:    */     implements BSPTreeVisitor<Euclidean3D>
/*  65:    */   {
/*  66:    */     public FacetsContributionVisitor()
/*  67:    */     {
/*  68:138 */       PolyhedronsSet.this.setSize(0.0D);
/*  69:139 */       PolyhedronsSet.this.setBarycenter(new Vector3D(0.0D, 0.0D, 0.0D));
/*  70:    */     }
/*  71:    */     
/*  72:    */     public BSPTreeVisitor.Order visitOrder(BSPTree<Euclidean3D> node)
/*  73:    */     {
/*  74:144 */       return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
/*  75:    */     }
/*  76:    */     
/*  77:    */     public void visitInternalNode(BSPTree<Euclidean3D> node)
/*  78:    */     {
/*  79:150 */       BoundaryAttribute<Euclidean3D> attribute = (BoundaryAttribute)node.getAttribute();
/*  80:152 */       if (attribute.getPlusOutside() != null) {
/*  81:153 */         addContribution(attribute.getPlusOutside(), false);
/*  82:    */       }
/*  83:155 */       if (attribute.getPlusInside() != null) {
/*  84:156 */         addContribution(attribute.getPlusInside(), true);
/*  85:    */       }
/*  86:    */     }
/*  87:    */     
/*  88:    */     public void visitLeafNode(BSPTree<Euclidean3D> node) {}
/*  89:    */     
/*  90:    */     private void addContribution(SubHyperplane<Euclidean3D> facet, boolean reversed)
/*  91:    */     {
/*  92:170 */       Region<Euclidean2D> polygon = ((SubPlane)facet).getRemainingRegion();
/*  93:171 */       double area = polygon.getSize();
/*  94:173 */       if (Double.isInfinite(area))
/*  95:    */       {
/*  96:174 */         PolyhedronsSet.this.setSize((1.0D / 0.0D));
/*  97:175 */         PolyhedronsSet.this.setBarycenter(Vector3D.NaN);
/*  98:    */       }
/*  99:    */       else
/* 100:    */       {
/* 101:178 */         Plane plane = (Plane)facet.getHyperplane();
/* 102:179 */         Vector3D facetB = plane.toSpace(polygon.getBarycenter());
/* 103:180 */         double scaled = area * facetB.dotProduct(plane.getNormal());
/* 104:181 */         if (reversed) {
/* 105:182 */           scaled = -scaled;
/* 106:    */         }
/* 107:185 */         PolyhedronsSet.this.setSize(PolyhedronsSet.this.getSize() + scaled);
/* 108:186 */         PolyhedronsSet.this.setBarycenter(new Vector3D(1.0D, (Vector3D)PolyhedronsSet.this.getBarycenter(), scaled, facetB));
/* 109:    */       }
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public SubHyperplane<Euclidean3D> firstIntersection(Vector3D point, Line line)
/* 114:    */   {
/* 115:202 */     return recurseFirstIntersection(getTree(true), point, line);
/* 116:    */   }
/* 117:    */   
/* 118:    */   private SubHyperplane<Euclidean3D> recurseFirstIntersection(BSPTree<Euclidean3D> node, Vector3D point, Line line)
/* 119:    */   {
/* 120:217 */     SubHyperplane<Euclidean3D> cut = node.getCut();
/* 121:218 */     if (cut == null) {
/* 122:219 */       return null;
/* 123:    */     }
/* 124:221 */     BSPTree<Euclidean3D> minus = node.getMinus();
/* 125:222 */     BSPTree<Euclidean3D> plus = node.getPlus();
/* 126:223 */     Plane plane = (Plane)cut.getHyperplane();
/* 127:    */     
/* 128:    */ 
/* 129:226 */     double offset = plane.getOffset(point);
/* 130:227 */     boolean in = FastMath.abs(offset) < 1.0E-010D;
/* 131:    */     BSPTree<Euclidean3D> far;
/* 132:    */     BSPTree<Euclidean3D> near;
/* 133:    */     
/* 134:230 */     if (offset < 0.0D)
/* 135:    */     {
/* 136:231 */       near = minus;
/* 137:232 */       far = plus;
/* 138:    */     }
/* 139:    */     else
/* 140:    */     {
/* 141:234 */       near = plus;
/* 142:235 */       far = minus;
/* 143:    */     }
/* 144:238 */     if (in)
/* 145:    */     {
/* 146:240 */       SubHyperplane<Euclidean3D> facet = boundaryFacet(point, node);
/* 147:241 */       if (facet != null) {
/* 148:242 */         return facet;
/* 149:    */       }
/* 150:    */     }
/* 151:247 */     SubHyperplane<Euclidean3D> crossed = recurseFirstIntersection(near, point, line);
/* 152:248 */     if (crossed != null) {
/* 153:249 */       return crossed;
/* 154:    */     }
/* 155:252 */     if (!in)
/* 156:    */     {
/* 157:254 */       Vector3D hit3D = plane.intersection(line);
/* 158:255 */       if (hit3D != null)
/* 159:    */       {
/* 160:256 */         SubHyperplane<Euclidean3D> facet = boundaryFacet(hit3D, node);
/* 161:257 */         if (facet != null) {
/* 162:258 */           return facet;
/* 163:    */         }
/* 164:    */       }
/* 165:    */     }
/* 166:264 */     return recurseFirstIntersection(far, point, line);
/* 167:    */   }
/* 168:    */   
/* 169:    */   private SubHyperplane<Euclidean3D> boundaryFacet(Vector3D point, BSPTree<Euclidean3D> node)
/* 170:    */   {
/* 171:276 */     Vector2D point2D = ((Plane)node.getCut().getHyperplane()).toSubSpace(point);
/* 172:    */     
/* 173:278 */     BoundaryAttribute<Euclidean3D> attribute = (BoundaryAttribute)node.getAttribute();
/* 174:280 */     if ((attribute.getPlusOutside() != null) && (((SubPlane)attribute.getPlusOutside()).getRemainingRegion().checkPoint(point2D) == Region.Location.INSIDE)) {
/* 175:282 */       return attribute.getPlusOutside();
/* 176:    */     }
/* 177:284 */     if ((attribute.getPlusInside() != null) && (((SubPlane)attribute.getPlusInside()).getRemainingRegion().checkPoint(point2D) == Region.Location.INSIDE)) {
/* 178:286 */       return attribute.getPlusInside();
/* 179:    */     }
/* 180:288 */     return null;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public PolyhedronsSet rotate(Vector3D center, Rotation rotation)
/* 184:    */   {
/* 185:298 */     return (PolyhedronsSet)applyTransform(new RotationTransform(center, rotation));
/* 186:    */   }
/* 187:    */   
/* 188:    */   private static class RotationTransform
/* 189:    */     implements Transform<Euclidean3D, Euclidean2D>
/* 190:    */   {
/* 191:    */     private Vector3D center;
/* 192:    */     private Rotation rotation;
/* 193:    */     private Plane cachedOriginal;
/* 194:    */     private Transform<Euclidean2D, Euclidean1D> cachedTransform;
/* 195:    */     
/* 196:    */     public RotationTransform(Vector3D center, Rotation rotation)
/* 197:    */     {
/* 198:321 */       this.center = center;
/* 199:322 */       this.rotation = rotation;
/* 200:    */     }
/* 201:    */     
/* 202:    */     public Vector3D apply(Vector<Euclidean3D> point)
/* 203:    */     {
/* 204:327 */       Vector3D delta = ((Vector3D)point).subtract(this.center);
/* 205:328 */       return new Vector3D(1.0D, this.center, 1.0D, this.rotation.applyTo(delta));
/* 206:    */     }
/* 207:    */     
/* 208:    */     public Plane apply(Hyperplane<Euclidean3D> hyperplane)
/* 209:    */     {
/* 210:333 */       return ((Plane)hyperplane).rotate(this.center, this.rotation);
/* 211:    */     }
/* 212:    */     
/* 213:    */     public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed)
/* 214:    */     {
/* 215:340 */       if (original != this.cachedOriginal)
/* 216:    */       {
/* 217:343 */         Plane oPlane = (Plane)original;
/* 218:344 */         Plane tPlane = (Plane)transformed;
/* 219:345 */         Vector3D p00 = oPlane.getOrigin();
/* 220:346 */         Vector3D p10 = oPlane.toSpace(new Vector2D(1.0D, 0.0D));
/* 221:347 */         Vector3D p01 = oPlane.toSpace(new Vector2D(0.0D, 1.0D));
/* 222:348 */         Vector2D tP00 = tPlane.toSubSpace(apply(p00));
/* 223:349 */         Vector2D tP10 = tPlane.toSubSpace(apply(p10));
/* 224:350 */         Vector2D tP01 = tPlane.toSubSpace(apply(p01));
/* 225:351 */         AffineTransform at = new AffineTransform(tP10.getX() - tP00.getX(), tP10.getY() - tP00.getY(), tP01.getX() - tP00.getX(), tP01.getY() - tP00.getY(), tP00.getX(), tP00.getY());
/* 226:    */         
/* 227:    */ 
/* 228:    */ 
/* 229:    */ 
/* 230:356 */         this.cachedOriginal = ((Plane)original);
/* 231:357 */         this.cachedTransform = org.apache.commons.math3.geometry.euclidean.twod.Line.getTransform(at);
/* 232:    */       }
/* 233:360 */       return ((SubLine)sub).applyTransform(this.cachedTransform);
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   public PolyhedronsSet translate(Vector3D translation)
/* 238:    */   {
/* 239:371 */     return (PolyhedronsSet)applyTransform(new TranslationTransform(translation));
/* 240:    */   }
/* 241:    */   
/* 242:    */   private static class TranslationTransform
/* 243:    */     implements Transform<Euclidean3D, Euclidean2D>
/* 244:    */   {
/* 245:    */     private Vector3D translation;
/* 246:    */     private Plane cachedOriginal;
/* 247:    */     private Transform<Euclidean2D, Euclidean1D> cachedTransform;
/* 248:    */     
/* 249:    */     public TranslationTransform(Vector3D translation)
/* 250:    */     {
/* 251:390 */       this.translation = translation;
/* 252:    */     }
/* 253:    */     
/* 254:    */     public Vector3D apply(Vector<Euclidean3D> point)
/* 255:    */     {
/* 256:395 */       return new Vector3D(1.0D, (Vector3D)point, 1.0D, this.translation);
/* 257:    */     }
/* 258:    */     
/* 259:    */     public Plane apply(Hyperplane<Euclidean3D> hyperplane)
/* 260:    */     {
/* 261:400 */       return ((Plane)hyperplane).translate(this.translation);
/* 262:    */     }
/* 263:    */     
/* 264:    */     public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed)
/* 265:    */     {
/* 266:407 */       if (original != this.cachedOriginal)
/* 267:    */       {
/* 268:410 */         Plane oPlane = (Plane)original;
/* 269:411 */         Plane tPlane = (Plane)transformed;
/* 270:412 */         Vector2D shift = tPlane.toSubSpace(apply(oPlane.getOrigin()));
/* 271:413 */         AffineTransform at = AffineTransform.getTranslateInstance(shift.getX(), shift.getY());
/* 272:    */         
/* 273:    */ 
/* 274:416 */         this.cachedOriginal = ((Plane)original);
/* 275:417 */         this.cachedTransform = org.apache.commons.math3.geometry.euclidean.twod.Line.getTransform(at);
/* 276:    */       }
/* 277:422 */       return ((SubLine)sub).applyTransform(this.cachedTransform);
/* 278:    */     }
/* 279:    */   }
/* 280:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet
 * JD-Core Version:    0.7.0.1
 */
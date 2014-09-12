/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
/*   5:    */ import org.apache.commons.math3.geometry.euclidean.twod.Line;
/*   6:    */ import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
/*   8:    */ import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
/*   9:    */ import org.apache.commons.math3.geometry.partitioning.BSPTree;
/*  10:    */ import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
/*  12:    */ import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
/*  13:    */ import org.apache.commons.math3.geometry.partitioning.RegionFactory;
/*  14:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  15:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;
/*  16:    */ import org.apache.commons.math3.util.FastMath;
/*  17:    */ 
/*  18:    */ public class OutlineExtractor
/*  19:    */ {
/*  20:    */   private Vector3D u;
/*  21:    */   private Vector3D v;
/*  22:    */   private Vector3D w;
/*  23:    */   
/*  24:    */   public OutlineExtractor(Vector3D u, Vector3D v)
/*  25:    */   {
/*  26: 54 */     this.u = u;
/*  27: 55 */     this.v = v;
/*  28: 56 */     this.w = Vector3D.crossProduct(u, v);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Vector2D[][] getOutline(PolyhedronsSet polyhedronsSet)
/*  32:    */   {
/*  33: 66 */     BoundaryProjector projector = new BoundaryProjector();
/*  34: 67 */     polyhedronsSet.getTree(true).visit(projector);
/*  35: 68 */     PolygonsSet projected = projector.getProjected();
/*  36:    */     
/*  37:    */ 
/*  38: 71 */     Vector2D[][] outline = projected.getVertices();
/*  39: 72 */     for (int i = 0; i < outline.length; i++)
/*  40:    */     {
/*  41: 73 */       Vector2D[] rawLoop = outline[i];
/*  42: 74 */       int end = rawLoop.length;
/*  43: 75 */       int j = 0;
/*  44: 76 */       while (j < end) {
/*  45: 77 */         if (pointIsBetween(rawLoop, end, j))
/*  46:    */         {
/*  47: 79 */           for (int k = j; k < end - 1; k++) {
/*  48: 80 */             rawLoop[k] = rawLoop[(k + 1)];
/*  49:    */           }
/*  50: 82 */           end--;
/*  51:    */         }
/*  52:    */         else
/*  53:    */         {
/*  54: 85 */           j++;
/*  55:    */         }
/*  56:    */       }
/*  57: 88 */       if (end != rawLoop.length)
/*  58:    */       {
/*  59: 90 */         outline[i] = new Vector2D[end];
/*  60: 91 */         System.arraycopy(rawLoop, 0, outline[i], 0, end);
/*  61:    */       }
/*  62:    */     }
/*  63: 95 */     return outline;
/*  64:    */   }
/*  65:    */   
/*  66:    */   private boolean pointIsBetween(Vector2D[] loop, int n, int i)
/*  67:    */   {
/*  68:108 */     Vector2D previous = loop[((i + n - 1) % n)];
/*  69:109 */     Vector2D current = loop[i];
/*  70:110 */     Vector2D next = loop[((i + 1) % n)];
/*  71:111 */     double dx1 = current.getX() - previous.getX();
/*  72:112 */     double dy1 = current.getY() - previous.getY();
/*  73:113 */     double dx2 = next.getX() - current.getX();
/*  74:114 */     double dy2 = next.getY() - current.getY();
/*  75:115 */     double cross = dx1 * dy2 - dx2 * dy1;
/*  76:116 */     double dot = dx1 * dx2 + dy1 * dy2;
/*  77:117 */     double d1d2 = FastMath.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2));
/*  78:118 */     return (FastMath.abs(cross) <= 1.0E-006D * d1d2) && (dot >= 0.0D);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private class BoundaryProjector
/*  82:    */     implements BSPTreeVisitor<Euclidean3D>
/*  83:    */   {
/*  84:    */     private PolygonsSet projected;
/*  85:    */     
/*  86:    */     public BoundaryProjector()
/*  87:    */     {
/*  88:130 */       this.projected = new PolygonsSet(new BSPTree(Boolean.FALSE));
/*  89:    */     }
/*  90:    */     
/*  91:    */     public BSPTreeVisitor.Order visitOrder(BSPTree<Euclidean3D> node)
/*  92:    */     {
/*  93:135 */       return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
/*  94:    */     }
/*  95:    */     
/*  96:    */     public void visitInternalNode(BSPTree<Euclidean3D> node)
/*  97:    */     {
/*  98:141 */       BoundaryAttribute<Euclidean3D> attribute = (BoundaryAttribute)node.getAttribute();
/*  99:143 */       if (attribute.getPlusOutside() != null) {
/* 100:144 */         addContribution(attribute.getPlusOutside(), false);
/* 101:    */       }
/* 102:146 */       if (attribute.getPlusInside() != null) {
/* 103:147 */         addContribution(attribute.getPlusInside(), true);
/* 104:    */       }
/* 105:    */     }
/* 106:    */     
/* 107:    */     public void visitLeafNode(BSPTree<Euclidean3D> node) {}
/* 108:    */     
/* 109:    */     private void addContribution(SubHyperplane<Euclidean3D> facet, boolean reversed)
/* 110:    */     {
/* 111:163 */       AbstractSubHyperplane<Euclidean3D, Euclidean2D> absFacet = (AbstractSubHyperplane)facet;
/* 112:    */       
/* 113:165 */       Plane plane = (Plane)facet.getHyperplane();
/* 114:    */       
/* 115:167 */       double scal = plane.getNormal().dotProduct(OutlineExtractor.this.w);
/* 116:168 */       if (FastMath.abs(scal) > 0.001D)
/* 117:    */       {
/* 118:169 */         Vector2D[][] vertices = ((PolygonsSet)absFacet.getRemainingRegion()).getVertices();
/* 119:172 */         if ((scal < 0.0D ^ reversed))
/* 120:    */         {
/* 121:175 */           Vector2D[][] newVertices = new Vector2D[vertices.length][];
/* 122:176 */           for (int i = 0; i < vertices.length; i++)
/* 123:    */           {
/* 124:177 */             Vector2D[] loop = vertices[i];
/* 125:178 */             Vector2D[] newLoop = new Vector2D[loop.length];
/* 126:179 */             if (loop[0] == null)
/* 127:    */             {
/* 128:180 */               newLoop[0] = null;
/* 129:181 */               for (int j = 1; j < loop.length; j++) {
/* 130:182 */                 newLoop[j] = loop[(loop.length - j)];
/* 131:    */               }
/* 132:    */             }
/* 133:    */             else
/* 134:    */             {
/* 135:185 */               for (int j = 0; j < loop.length; j++) {
/* 136:186 */                 newLoop[j] = loop[(loop.length - (j + 1))];
/* 137:    */               }
/* 138:    */             }
/* 139:189 */             newVertices[i] = newLoop;
/* 140:    */           }
/* 141:193 */           vertices = newVertices;
/* 142:    */         }
/* 143:198 */         ArrayList<SubHyperplane<Euclidean2D>> edges = new ArrayList();
/* 144:199 */         for (Vector2D[] loop : vertices)
/* 145:    */         {
/* 146:200 */           boolean closed = loop[0] != null;
/* 147:201 */           int previous = closed ? loop.length - 1 : 1;
/* 148:202 */           Vector3D previous3D = plane.toSpace(loop[previous]);
/* 149:203 */           int current = (previous + 1) % loop.length;
/* 150:204 */           Vector2D pPoint = new Vector2D(previous3D.dotProduct(OutlineExtractor.this.u), previous3D.dotProduct(OutlineExtractor.this.v));
/* 151:206 */           while (current < loop.length)
/* 152:    */           {
/* 153:208 */             Vector3D current3D = plane.toSpace(loop[current]);
/* 154:209 */             Vector2D cPoint = new Vector2D(current3D.dotProduct(OutlineExtractor.this.u), current3D.dotProduct(OutlineExtractor.this.v));
/* 155:    */             
/* 156:211 */             Line line = new Line(pPoint, cPoint);
/* 157:    */             
/* 158:213 */             SubHyperplane<Euclidean2D> edge = line.wholeHyperplane();
/* 159:215 */             if ((closed) || (previous != 1))
/* 160:    */             {
/* 161:218 */               double angle = line.getAngle() + 1.570796326794897D;
/* 162:219 */               Line l = new Line(pPoint, angle);
/* 163:    */               
/* 164:221 */               edge = edge.split(l).getPlus();
/* 165:    */             }
/* 166:224 */             if ((closed) || (current != loop.length - 1))
/* 167:    */             {
/* 168:227 */               double angle = line.getAngle() + 1.570796326794897D;
/* 169:228 */               Line l = new Line(cPoint, angle);
/* 170:    */               
/* 171:230 */               edge = edge.split(l).getMinus();
/* 172:    */             }
/* 173:233 */             edges.add(edge);
/* 174:    */             
/* 175:235 */             previous = current++;
/* 176:236 */             previous3D = current3D;
/* 177:237 */             pPoint = cPoint;
/* 178:    */           }
/* 179:    */         }
/* 180:241 */         PolygonsSet projectedFacet = new PolygonsSet(edges);
/* 181:    */         
/* 182:    */ 
/* 183:244 */         this.projected = ((PolygonsSet)new RegionFactory().union(this.projected, projectedFacet));
/* 184:    */       }
/* 185:    */     }
/* 186:    */     
/* 187:    */     public PolygonsSet getProjected()
/* 188:    */     {
/* 189:253 */       return this.projected;
/* 190:    */     }
/* 191:    */   }
/* 192:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.OutlineExtractor
 * JD-Core Version:    0.7.0.1
 */
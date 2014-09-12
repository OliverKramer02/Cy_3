/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.geometry.Vector;
/*   6:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
/*   8:    */ import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
/*   9:    */ import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
/*  10:    */ import org.apache.commons.math3.geometry.partitioning.Embedding;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.Hyperplane;
/*  12:    */ import org.apache.commons.math3.util.FastMath;
/*  13:    */ 
/*  14:    */ public class Plane
/*  15:    */   implements Hyperplane<Euclidean3D>, Embedding<Euclidean3D, Euclidean2D>
/*  16:    */ {
/*  17:    */   private double originOffset;
/*  18:    */   private Vector3D origin;
/*  19:    */   private Vector3D u;
/*  20:    */   private Vector3D v;
/*  21:    */   private Vector3D w;
/*  22:    */   
/*  23:    */   public Plane(Vector3D normal)
/*  24:    */   {
/*  25: 56 */     setNormal(normal);
/*  26: 57 */     this.originOffset = 0.0D;
/*  27: 58 */     setFrame();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Plane(Vector3D p, Vector3D normal)
/*  31:    */   {
/*  32: 67 */     setNormal(normal);
/*  33: 68 */     this.originOffset = (-p.dotProduct(this.w));
/*  34: 69 */     setFrame();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Plane(Vector3D p1, Vector3D p2, Vector3D p3)
/*  38:    */   {
/*  39: 81 */     this(p1, p2.subtract(p1).crossProduct(p3.subtract(p1)));
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Plane(Plane plane)
/*  43:    */   {
/*  44: 91 */     this.originOffset = plane.originOffset;
/*  45: 92 */     this.origin = plane.origin;
/*  46: 93 */     this.u = plane.u;
/*  47: 94 */     this.v = plane.v;
/*  48: 95 */     this.w = plane.w;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Plane copySelf()
/*  52:    */   {
/*  53:105 */     return new Plane(this);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void reset(Vector3D p, Vector3D normal)
/*  57:    */   {
/*  58:113 */     setNormal(normal);
/*  59:114 */     this.originOffset = (-p.dotProduct(this.w));
/*  60:115 */     setFrame();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void reset(Plane original)
/*  64:    */   {
/*  65:125 */     this.originOffset = original.originOffset;
/*  66:126 */     this.origin = original.origin;
/*  67:127 */     this.u = original.u;
/*  68:128 */     this.v = original.v;
/*  69:129 */     this.w = original.w;
/*  70:    */   }
/*  71:    */   
/*  72:    */   private void setNormal(Vector3D normal)
/*  73:    */   {
/*  74:137 */     double norm = normal.getNorm();
/*  75:138 */     if (norm < 1.0E-010D) {
/*  76:139 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
/*  77:    */     }
/*  78:141 */     this.w = new Vector3D(1.0D / norm, normal);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void setFrame()
/*  82:    */   {
/*  83:147 */     this.origin = new Vector3D(-this.originOffset, this.w);
/*  84:148 */     this.u = this.w.orthogonal();
/*  85:149 */     this.v = Vector3D.crossProduct(this.w, this.u);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Vector3D getOrigin()
/*  89:    */   {
/*  90:159 */     return this.origin;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public Vector3D getNormal()
/*  94:    */   {
/*  95:171 */     return this.w;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Vector3D getU()
/*  99:    */   {
/* 100:183 */     return this.u;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public Vector3D getV()
/* 104:    */   {
/* 105:195 */     return this.v;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void revertSelf()
/* 109:    */   {
/* 110:210 */     Vector3D tmp = this.u;
/* 111:211 */     this.u = this.v;
/* 112:212 */     this.v = tmp;
/* 113:213 */     this.w = this.w.negate();
/* 114:214 */     this.originOffset = (-this.originOffset);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Vector2D toSubSpace(Vector<Euclidean3D> point)
/* 118:    */   {
/* 119:225 */     return new Vector2D(point.dotProduct(this.u), point.dotProduct(this.v));
/* 120:    */   }
/* 121:    */   
/* 122:    */   public Vector3D toSpace(Vector<Euclidean2D> point)
/* 123:    */   {
/* 124:235 */     Vector2D p2D = (Vector2D)point;
/* 125:236 */     return new Vector3D(p2D.getX(), this.u, p2D.getY(), this.v, -this.originOffset, this.w);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public Vector3D getPointAt(Vector2D inPlane, double offset)
/* 129:    */   {
/* 130:247 */     return new Vector3D(inPlane.getX(), this.u, inPlane.getY(), this.v, offset - this.originOffset, this.w);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean isSimilarTo(Plane plane)
/* 134:    */   {
/* 135:258 */     double angle = Vector3D.angle(this.w, plane.w);
/* 136:259 */     return ((angle < 1.0E-010D) && (FastMath.abs(this.originOffset - plane.originOffset) < 1.0E-010D)) || ((angle > 3.141592653489793D) && (FastMath.abs(this.originOffset + plane.originOffset) < 1.0E-010D));
/* 137:    */   }
/* 138:    */   
/* 139:    */   public Plane rotate(Vector3D center, Rotation rotation)
/* 140:    */   {
/* 141:271 */     Vector3D delta = this.origin.subtract(center);
/* 142:272 */     Plane plane = new Plane(center.add(rotation.applyTo(delta)), rotation.applyTo(this.w));
/* 143:    */     
/* 144:    */ 
/* 145:    */ 
/* 146:276 */     plane.u = rotation.applyTo(this.u);
/* 147:277 */     plane.v = rotation.applyTo(this.v);
/* 148:    */     
/* 149:279 */     return plane;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public Plane translate(Vector3D translation)
/* 153:    */   {
/* 154:290 */     Plane plane = new Plane(this.origin.add(translation), this.w);
/* 155:    */     
/* 156:    */ 
/* 157:293 */     plane.u = this.u;
/* 158:294 */     plane.v = this.v;
/* 159:    */     
/* 160:296 */     return plane;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public Vector3D intersection(Line line)
/* 164:    */   {
/* 165:306 */     Vector3D direction = line.getDirection();
/* 166:307 */     double dot = this.w.dotProduct(direction);
/* 167:308 */     if (FastMath.abs(dot) < 1.0E-010D) {
/* 168:309 */       return null;
/* 169:    */     }
/* 170:311 */     Vector3D point = line.toSpace(Vector1D.ZERO);
/* 171:312 */     double k = -(this.originOffset + this.w.dotProduct(point)) / dot;
/* 172:313 */     return new Vector3D(1.0D, point, k, direction);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public Line intersection(Plane other)
/* 176:    */   {
/* 177:322 */     Vector3D direction = Vector3D.crossProduct(this.w, other.w);
/* 178:323 */     if (direction.getNorm() < 1.0E-010D) {
/* 179:324 */       return null;
/* 180:    */     }
/* 181:326 */     Vector3D point = intersection(this, other, new Plane(direction));
/* 182:327 */     return new Line(point, point.add(direction));
/* 183:    */   }
/* 184:    */   
/* 185:    */   public static Vector3D intersection(Plane plane1, Plane plane2, Plane plane3)
/* 186:    */   {
/* 187:339 */     double a1 = plane1.w.getX();
/* 188:340 */     double b1 = plane1.w.getY();
/* 189:341 */     double c1 = plane1.w.getZ();
/* 190:342 */     double d1 = plane1.originOffset;
/* 191:    */     
/* 192:344 */     double a2 = plane2.w.getX();
/* 193:345 */     double b2 = plane2.w.getY();
/* 194:346 */     double c2 = plane2.w.getZ();
/* 195:347 */     double d2 = plane2.originOffset;
/* 196:    */     
/* 197:349 */     double a3 = plane3.w.getX();
/* 198:350 */     double b3 = plane3.w.getY();
/* 199:351 */     double c3 = plane3.w.getZ();
/* 200:352 */     double d3 = plane3.originOffset;
/* 201:    */     
/* 202:    */ 
/* 203:    */ 
/* 204:356 */     double a23 = b2 * c3 - b3 * c2;
/* 205:357 */     double b23 = c2 * a3 - c3 * a2;
/* 206:358 */     double c23 = a2 * b3 - a3 * b2;
/* 207:359 */     double determinant = a1 * a23 + b1 * b23 + c1 * c23;
/* 208:360 */     if (FastMath.abs(determinant) < 1.0E-010D) {
/* 209:361 */       return null;
/* 210:    */     }
/* 211:364 */     double r = 1.0D / determinant;
/* 212:365 */     return new Vector3D((-a23 * d1 - (c1 * b3 - c3 * b1) * d2 - (c2 * b1 - c1 * b2) * d3) * r, (-b23 * d1 - (c3 * a1 - c1 * a3) * d2 - (c1 * a2 - c2 * a1) * d3) * r, (-c23 * d1 - (b1 * a3 - b3 * a1) * d2 - (b2 * a1 - b1 * a2) * d3) * r);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public SubPlane wholeHyperplane()
/* 216:    */   {
/* 217:376 */     return new SubPlane(this, new PolygonsSet());
/* 218:    */   }
/* 219:    */   
/* 220:    */   public PolyhedronsSet wholeSpace()
/* 221:    */   {
/* 222:384 */     return new PolyhedronsSet();
/* 223:    */   }
/* 224:    */   
/* 225:    */   public boolean contains(Vector3D p)
/* 226:    */   {
/* 227:392 */     return FastMath.abs(getOffset(p)) < 1.0E-010D;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public double getOffset(Plane plane)
/* 231:    */   {
/* 232:406 */     return this.originOffset + (sameOrientationAs(plane) ? -plane.originOffset : plane.originOffset);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public double getOffset(Vector<Euclidean3D> point)
/* 236:    */   {
/* 237:418 */     return point.dotProduct(this.w) + this.originOffset;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public boolean sameOrientationAs(Hyperplane<Euclidean3D> other)
/* 241:    */   {
/* 242:427 */     return ((Plane)other).w.dotProduct(this.w) > 0.0D;
/* 243:    */   }
/* 244:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.Plane
 * JD-Core Version:    0.7.0.1
 */
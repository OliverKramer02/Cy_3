/*   1:    */ package org.apache.commons.math3.geometry.euclidean.twod;
/*   2:    */ 
/*   3:    */ import java.awt.geom.AffineTransform;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.geometry.Vector;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
/*   8:    */ import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
/*   9:    */ import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
/*  10:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.Embedding;
/*  12:    */ import org.apache.commons.math3.geometry.partitioning.Hyperplane;
/*  13:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  14:    */ import org.apache.commons.math3.geometry.partitioning.Transform;
/*  15:    */ import org.apache.commons.math3.util.FastMath;
/*  16:    */ import org.apache.commons.math3.util.MathUtils;
/*  17:    */ 
/*  18:    */ public class Line
/*  19:    */   implements Hyperplane<Euclidean2D>, Embedding<Euclidean2D, Euclidean1D>
/*  20:    */ {
/*  21:    */   private double angle;
/*  22:    */   private double cos;
/*  23:    */   private double sin;
/*  24:    */   private double originOffset;
/*  25:    */   
/*  26:    */   public Line(Vector2D p1, Vector2D p2)
/*  27:    */   {
/*  28: 82 */     reset(p1, p2);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Line(Vector2D p, double angle)
/*  32:    */   {
/*  33: 90 */     reset(p, angle);
/*  34:    */   }
/*  35:    */   
/*  36:    */   private Line(double angle, double cos, double sin, double originOffset)
/*  37:    */   {
/*  38:100 */     this.angle = angle;
/*  39:101 */     this.cos = cos;
/*  40:102 */     this.sin = sin;
/*  41:103 */     this.originOffset = originOffset;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Line(Line line)
/*  45:    */   {
/*  46:112 */     this.angle = MathUtils.normalizeAngle(line.angle, 3.141592653589793D);
/*  47:113 */     this.cos = FastMath.cos(this.angle);
/*  48:114 */     this.sin = FastMath.sin(this.angle);
/*  49:115 */     this.originOffset = line.originOffset;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Line copySelf()
/*  53:    */   {
/*  54:120 */     return new Line(this);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void reset(Vector2D p1, Vector2D p2)
/*  58:    */   {
/*  59:129 */     double dx = p2.getX() - p1.getX();
/*  60:130 */     double dy = p2.getY() - p1.getY();
/*  61:131 */     double d = FastMath.hypot(dx, dy);
/*  62:132 */     if (d == 0.0D)
/*  63:    */     {
/*  64:133 */       this.angle = 0.0D;
/*  65:134 */       this.cos = 1.0D;
/*  66:135 */       this.sin = 0.0D;
/*  67:136 */       this.originOffset = p1.getY();
/*  68:    */     }
/*  69:    */     else
/*  70:    */     {
/*  71:138 */       this.angle = (3.141592653589793D + FastMath.atan2(-dy, -dx));
/*  72:139 */       this.cos = FastMath.cos(this.angle);
/*  73:140 */       this.sin = FastMath.sin(this.angle);
/*  74:141 */       this.originOffset = ((p2.getX() * p1.getY() - p1.getX() * p2.getY()) / d);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void reset(Vector2D p, double alpha)
/*  79:    */   {
/*  80:150 */     this.angle = MathUtils.normalizeAngle(alpha, 3.141592653589793D);
/*  81:151 */     this.cos = FastMath.cos(this.angle);
/*  82:152 */     this.sin = FastMath.sin(this.angle);
/*  83:153 */     this.originOffset = (this.cos * p.getY() - this.sin * p.getX());
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void revertSelf()
/*  87:    */   {
/*  88:159 */     if (this.angle < 3.141592653589793D) {
/*  89:160 */       this.angle += 3.141592653589793D;
/*  90:    */     } else {
/*  91:162 */       this.angle -= 3.141592653589793D;
/*  92:    */     }
/*  93:164 */     this.cos = (-this.cos);
/*  94:165 */     this.sin = (-this.sin);
/*  95:166 */     this.originOffset = (-this.originOffset);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Line getReverse()
/*  99:    */   {
/* 100:175 */     return new Line(this.angle < 3.141592653589793D ? this.angle + 3.141592653589793D : this.angle - 3.141592653589793D, -this.cos, -this.sin, -this.originOffset);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public Vector1D toSubSpace(Vector<Euclidean2D> point)
/* 104:    */   {
/* 105:181 */     Vector2D p2 = (Vector2D)point;
/* 106:182 */     return new Vector1D(this.cos * p2.getX() + this.sin * p2.getY());
/* 107:    */   }
/* 108:    */   
/* 109:    */   public Vector2D toSpace(Vector<Euclidean1D> point)
/* 110:    */   {
/* 111:187 */     double abscissa = ((Vector1D)point).getX();
/* 112:188 */     return new Vector2D(abscissa * this.cos - this.originOffset * this.sin, abscissa * this.sin + this.originOffset * this.cos);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public Vector2D intersection(Line other)
/* 116:    */   {
/* 117:198 */     double d = this.sin * other.cos - other.sin * this.cos;
/* 118:199 */     if (FastMath.abs(d) < 1.0E-010D) {
/* 119:200 */       return null;
/* 120:    */     }
/* 121:202 */     return new Vector2D((this.cos * other.originOffset - other.cos * this.originOffset) / d, (this.sin * other.originOffset - other.sin * this.originOffset) / d);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public SubLine wholeHyperplane()
/* 125:    */   {
/* 126:208 */     return new SubLine(this, new IntervalsSet());
/* 127:    */   }
/* 128:    */   
/* 129:    */   public PolygonsSet wholeSpace()
/* 130:    */   {
/* 131:216 */     return new PolygonsSet();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public double getOffset(Line line)
/* 135:    */   {
/* 136:230 */     return this.originOffset + (this.cos * line.cos + this.sin * line.sin > 0.0D ? -line.originOffset : line.originOffset);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public double getOffset(Vector<Euclidean2D> point)
/* 140:    */   {
/* 141:236 */     Vector2D p2 = (Vector2D)point;
/* 142:237 */     return this.sin * p2.getX() - this.cos * p2.getY() + this.originOffset;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public boolean sameOrientationAs(Hyperplane<Euclidean2D> other)
/* 146:    */   {
/* 147:242 */     Line otherL = (Line)other;
/* 148:243 */     return this.sin * otherL.sin + this.cos * otherL.cos >= 0.0D;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public Vector2D getPointAt(Vector1D abscissa, double offset)
/* 152:    */   {
/* 153:253 */     double x = abscissa.getX();
/* 154:254 */     double dOffset = offset - this.originOffset;
/* 155:255 */     return new Vector2D(x * this.cos + dOffset * this.sin, x * this.sin - dOffset * this.cos);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public boolean contains(Vector2D p)
/* 159:    */   {
/* 160:263 */     return FastMath.abs(getOffset(p)) < 1.0E-010D;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public boolean isParallelTo(Line line)
/* 164:    */   {
/* 165:272 */     return FastMath.abs(this.sin * line.cos - this.cos * line.sin) < 1.0E-010D;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void translateToPoint(Vector2D p)
/* 169:    */   {
/* 170:279 */     this.originOffset = (this.cos * p.getY() - this.sin * p.getX());
/* 171:    */   }
/* 172:    */   
/* 173:    */   public double getAngle()
/* 174:    */   {
/* 175:286 */     return MathUtils.normalizeAngle(this.angle, 3.141592653589793D);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setAngle(double angle)
/* 179:    */   {
/* 180:293 */     this.angle = MathUtils.normalizeAngle(angle, 3.141592653589793D);
/* 181:294 */     this.cos = FastMath.cos(this.angle);
/* 182:295 */     this.sin = FastMath.sin(this.angle);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public double getOriginOffset()
/* 186:    */   {
/* 187:302 */     return this.originOffset;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void setOriginOffset(double offset)
/* 191:    */   {
/* 192:309 */     this.originOffset = offset;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public static Transform<Euclidean2D, Euclidean1D> getTransform(AffineTransform transform)
/* 196:    */     throws MathIllegalArgumentException
/* 197:    */   {
/* 198:327 */     return new LineTransform(transform);
/* 199:    */   }
/* 200:    */   
/* 201:    */   private static class LineTransform
/* 202:    */     implements Transform<Euclidean2D, Euclidean1D>
/* 203:    */   {
/* 204:    */     private double cXX;
/* 205:    */     private double cXY;
/* 206:    */     private double cX1;
/* 207:    */     private double cYX;
/* 208:    */     private double cYY;
/* 209:    */     private double cY1;
/* 210:    */     private double c1Y;
/* 211:    */     private double c1X;
/* 212:    */     private double c11;
/* 213:    */     
/* 214:    */     public LineTransform(AffineTransform transform)
/* 215:    */       throws MathIllegalArgumentException
/* 216:    */     {
/* 217:360 */       double[] m = new double[6];
/* 218:361 */       transform.getMatrix(m);
/* 219:362 */       this.cXX = m[0];
/* 220:363 */       this.cXY = m[2];
/* 221:364 */       this.cX1 = m[4];
/* 222:365 */       this.cYX = m[1];
/* 223:366 */       this.cYY = m[3];
/* 224:367 */       this.cY1 = m[5];
/* 225:    */       
/* 226:369 */       this.c1Y = (this.cXY * this.cY1 - this.cYY * this.cX1);
/* 227:370 */       this.c1X = (this.cXX * this.cY1 - this.cYX * this.cX1);
/* 228:371 */       this.c11 = (this.cXX * this.cYY - this.cYX * this.cXY);
/* 229:373 */       if (FastMath.abs(this.c11) < 1.0E-020D) {
/* 230:374 */         throw new MathIllegalArgumentException(LocalizedFormats.NON_INVERTIBLE_TRANSFORM, new Object[0]);
/* 231:    */       }
/* 232:    */     }
/* 233:    */     
/* 234:    */     public Vector2D apply(Vector<Euclidean2D> point)
/* 235:    */     {
/* 236:381 */       Vector2D p2D = (Vector2D)point;
/* 237:382 */       double x = p2D.getX();
/* 238:383 */       double y = p2D.getY();
/* 239:384 */       return new Vector2D(this.cXX * x + this.cXY * y + this.cX1, this.cYX * x + this.cYY * y + this.cY1);
/* 240:    */     }
/* 241:    */     
/* 242:    */     public Line apply(Hyperplane<Euclidean2D> hyperplane)
/* 243:    */     {
/* 244:390 */       Line line = (Line)hyperplane;
/* 245:391 */       double rOffset = this.c1X * line.cos + this.c1Y * line.sin + this.c11 * line.originOffset;
/* 246:392 */       double rCos = this.cXX * line.cos + this.cXY * line.sin;
/* 247:393 */       double rSin = this.cYX * line.cos + this.cYY * line.sin;
/* 248:394 */       double inv = 1.0D / FastMath.sqrt(rSin * rSin + rCos * rCos);
/* 249:395 */       return new Line(3.141592653589793D + FastMath.atan2(-rSin, -rCos), inv * rCos, inv * rSin, inv * rOffset);
/* 250:    */     }
/* 251:    */     
/* 252:    */     public SubHyperplane<Euclidean1D> apply(SubHyperplane<Euclidean1D> sub, Hyperplane<Euclidean2D> original, Hyperplane<Euclidean2D> transformed)
/* 253:    */     {
/* 254:404 */       OrientedPoint op = (OrientedPoint)sub.getHyperplane();
/* 255:405 */       Line originalLine = (Line)original;
/* 256:406 */       Line transformedLine = (Line)transformed;
/* 257:407 */       Vector1D newLoc = transformedLine.toSubSpace(apply(originalLine.toSpace(op.getLocation())));
/* 258:    */       
/* 259:409 */       return new OrientedPoint(newLoc, op.isDirect()).wholeHyperplane();
/* 260:    */     }
/* 261:    */   }
/* 262:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.Line
 * JD-Core Version:    0.7.0.1
 */
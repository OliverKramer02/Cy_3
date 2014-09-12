/*   1:    */ package org.apache.commons.math3.geometry.euclidean.twod;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.geometry.Space;
/*   8:    */ import org.apache.commons.math3.geometry.Vector;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ import org.apache.commons.math3.util.MathUtils;
/*  11:    */ 
/*  12:    */ public class Vector2D
/*  13:    */   implements Vector<Euclidean2D>
/*  14:    */ {
/*  15: 37 */   public static final Vector2D ZERO = new Vector2D(0.0D, 0.0D);
/*  16: 41 */   public static final Vector2D NaN = new Vector2D((0.0D / 0.0D), (0.0D / 0.0D));
/*  17: 45 */   public static final Vector2D POSITIVE_INFINITY = new Vector2D((1.0D / 0.0D), (1.0D / 0.0D));
/*  18: 49 */   public static final Vector2D NEGATIVE_INFINITY = new Vector2D((-1.0D / 0.0D), (-1.0D / 0.0D));
/*  19:    */   private static final long serialVersionUID = 266938651998679754L;
/*  20:    */   private final double x;
/*  21:    */   private final double y;
/*  22:    */   
/*  23:    */   public Vector2D(double x, double y)
/*  24:    */   {
/*  25: 69 */     this.x = x;
/*  26: 70 */     this.y = y;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Vector2D(double[] v)
/*  30:    */     throws DimensionMismatchException
/*  31:    */   {
/*  32: 80 */     if (v.length != 2) {
/*  33: 81 */       throw new DimensionMismatchException(v.length, 2);
/*  34:    */     }
/*  35: 83 */     this.x = v[0];
/*  36: 84 */     this.y = v[1];
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Vector2D(double a, Vector2D u)
/*  40:    */   {
/*  41: 94 */     this.x = (a * u.x);
/*  42: 95 */     this.y = (a * u.y);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2)
/*  46:    */   {
/*  47:107 */     this.x = (a1 * u1.x + a2 * u2.x);
/*  48:108 */     this.y = (a1 * u1.y + a2 * u2.y);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3)
/*  52:    */   {
/*  53:123 */     this.x = (a1 * u1.x + a2 * u2.x + a3 * u3.x);
/*  54:124 */     this.y = (a1 * u1.y + a2 * u2.y + a3 * u3.y);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3, double a4, Vector2D u4)
/*  58:    */   {
/*  59:141 */     this.x = (a1 * u1.x + a2 * u2.x + a3 * u3.x + a4 * u4.x);
/*  60:142 */     this.y = (a1 * u1.y + a2 * u2.y + a3 * u3.y + a4 * u4.y);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public double getX()
/*  64:    */   {
/*  65:150 */     return this.x;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public double getY()
/*  69:    */   {
/*  70:158 */     return this.y;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public double[] toArray()
/*  74:    */   {
/*  75:166 */     return new double[] { this.x, this.y };
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Space getSpace()
/*  79:    */   {
/*  80:171 */     return Euclidean2D.getInstance();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Vector2D getZero()
/*  84:    */   {
/*  85:176 */     return ZERO;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public double getNorm1()
/*  89:    */   {
/*  90:181 */     return FastMath.abs(this.x) + FastMath.abs(this.y);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public double getNorm()
/*  94:    */   {
/*  95:186 */     return FastMath.sqrt(this.x * this.x + this.y * this.y);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public double getNormSq()
/*  99:    */   {
/* 100:191 */     return this.x * this.x + this.y * this.y;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double getNormInf()
/* 104:    */   {
/* 105:196 */     return FastMath.max(FastMath.abs(this.x), FastMath.abs(this.y));
/* 106:    */   }
/* 107:    */   
/* 108:    */   public Vector2D add(Vector<Euclidean2D> v)
/* 109:    */   {
/* 110:201 */     Vector2D v2 = (Vector2D)v;
/* 111:202 */     return new Vector2D(this.x + v2.getX(), this.y + v2.getY());
/* 112:    */   }
/* 113:    */   
/* 114:    */   public Vector2D add(double factor, Vector<Euclidean2D> v)
/* 115:    */   {
/* 116:207 */     Vector2D v2 = (Vector2D)v;
/* 117:208 */     return new Vector2D(this.x + factor * v2.getX(), this.y + factor * v2.getY());
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Vector2D subtract(Vector<Euclidean2D> p)
/* 121:    */   {
/* 122:213 */     Vector2D p3 = (Vector2D)p;
/* 123:214 */     return new Vector2D(this.x - p3.x, this.y - p3.y);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public Vector2D subtract(double factor, Vector<Euclidean2D> v)
/* 127:    */   {
/* 128:219 */     Vector2D v2 = (Vector2D)v;
/* 129:220 */     return new Vector2D(this.x - factor * v2.getX(), this.y - factor * v2.getY());
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Vector2D normalize()
/* 133:    */   {
/* 134:225 */     double s = getNorm();
/* 135:226 */     if (s == 0.0D) {
/* 136:227 */       throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
/* 137:    */     }
/* 138:229 */     return scalarMultiply(1.0D / s);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public Vector2D negate()
/* 142:    */   {
/* 143:233 */     return new Vector2D(-this.x, -this.y);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Vector2D scalarMultiply(double a)
/* 147:    */   {
/* 148:238 */     return new Vector2D(a * this.x, a * this.y);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public boolean isNaN()
/* 152:    */   {
/* 153:243 */     return (Double.isNaN(this.x)) || (Double.isNaN(this.y));
/* 154:    */   }
/* 155:    */   
/* 156:    */   public boolean isInfinite()
/* 157:    */   {
/* 158:248 */     return (!isNaN()) && ((Double.isInfinite(this.x)) || (Double.isInfinite(this.y)));
/* 159:    */   }
/* 160:    */   
/* 161:    */   public double distance1(Vector<Euclidean2D> p)
/* 162:    */   {
/* 163:253 */     Vector2D p3 = (Vector2D)p;
/* 164:254 */     double dx = FastMath.abs(p3.x - this.x);
/* 165:255 */     double dy = FastMath.abs(p3.y - this.y);
/* 166:256 */     return dx + dy;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public double distance(Vector<Euclidean2D> p)
/* 170:    */   {
/* 171:261 */     Vector2D p3 = (Vector2D)p;
/* 172:262 */     double dx = p3.x - this.x;
/* 173:263 */     double dy = p3.y - this.y;
/* 174:264 */     return FastMath.sqrt(dx * dx + dy * dy);
/* 175:    */   }
/* 176:    */   
/* 177:    */   public double distanceInf(Vector<Euclidean2D> p)
/* 178:    */   {
/* 179:269 */     Vector2D p3 = (Vector2D)p;
/* 180:270 */     double dx = FastMath.abs(p3.x - this.x);
/* 181:271 */     double dy = FastMath.abs(p3.y - this.y);
/* 182:272 */     return FastMath.max(dx, dy);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public double distanceSq(Vector<Euclidean2D> p)
/* 186:    */   {
/* 187:277 */     Vector2D p3 = (Vector2D)p;
/* 188:278 */     double dx = p3.x - this.x;
/* 189:279 */     double dy = p3.y - this.y;
/* 190:280 */     return dx * dx + dy * dy;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public double dotProduct(Vector<Euclidean2D> v)
/* 194:    */   {
/* 195:285 */     Vector2D v2 = (Vector2D)v;
/* 196:286 */     return this.x * v2.x + this.y * v2.y;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public static double distance(Vector2D p1, Vector2D p2)
/* 200:    */   {
/* 201:298 */     return p1.distance(p2);
/* 202:    */   }
/* 203:    */   
/* 204:    */   public static double distanceInf(Vector2D p1, Vector2D p2)
/* 205:    */   {
/* 206:310 */     return p1.distanceInf(p2);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public static double distanceSq(Vector2D p1, Vector2D p2)
/* 210:    */   {
/* 211:322 */     return p1.distanceSq(p2);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public boolean equals(Object other)
/* 215:    */   {
/* 216:347 */     if (this == other) {
/* 217:348 */       return true;
/* 218:    */     }
/* 219:351 */     if ((other instanceof Vector2D))
/* 220:    */     {
/* 221:352 */       Vector2D rhs = (Vector2D)other;
/* 222:353 */       if (rhs.isNaN()) {
/* 223:354 */         return isNaN();
/* 224:    */       }
/* 225:357 */       return (this.x == rhs.x) && (this.y == rhs.y);
/* 226:    */     }
/* 227:359 */     return false;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public int hashCode()
/* 231:    */   {
/* 232:371 */     if (isNaN()) {
/* 233:372 */       return 542;
/* 234:    */     }
/* 235:374 */     return 122 * (76 * MathUtils.hash(this.x) + MathUtils.hash(this.y));
/* 236:    */   }
/* 237:    */   
/* 238:    */   public String toString()
/* 239:    */   {
/* 240:382 */     return Vector2DFormat.getInstance().format(this);
/* 241:    */   }
/* 242:    */   
/* 243:    */   public String toString(NumberFormat format)
/* 244:    */   {
/* 245:387 */     return new Vector2DFormat(format).format(this);
/* 246:    */   }
/* 247:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.Vector2D
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.geometry.Space;
/*   9:    */ import org.apache.commons.math3.geometry.Vector;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*  11:    */ import org.apache.commons.math3.util.MathArrays;
/*  12:    */ import org.apache.commons.math3.util.MathUtils;
/*  13:    */ 
/*  14:    */ public class Vector3D
/*  15:    */   implements Serializable, Vector<Euclidean3D>
/*  16:    */ {
/*  17: 41 */   public static final Vector3D ZERO = new Vector3D(0.0D, 0.0D, 0.0D);
/*  18: 44 */   public static final Vector3D PLUS_I = new Vector3D(1.0D, 0.0D, 0.0D);
/*  19: 47 */   public static final Vector3D MINUS_I = new Vector3D(-1.0D, 0.0D, 0.0D);
/*  20: 50 */   public static final Vector3D PLUS_J = new Vector3D(0.0D, 1.0D, 0.0D);
/*  21: 53 */   public static final Vector3D MINUS_J = new Vector3D(0.0D, -1.0D, 0.0D);
/*  22: 56 */   public static final Vector3D PLUS_K = new Vector3D(0.0D, 0.0D, 1.0D);
/*  23: 59 */   public static final Vector3D MINUS_K = new Vector3D(0.0D, 0.0D, -1.0D);
/*  24: 63 */   public static final Vector3D NaN = new Vector3D((0.0D / 0.0D), (0.0D / 0.0D), (0.0D / 0.0D));
/*  25: 67 */   public static final Vector3D POSITIVE_INFINITY = new Vector3D((1.0D / 0.0D), (1.0D / 0.0D), (1.0D / 0.0D));
/*  26: 71 */   public static final Vector3D NEGATIVE_INFINITY = new Vector3D((-1.0D / 0.0D), (-1.0D / 0.0D), (-1.0D / 0.0D));
/*  27:    */   private static final long serialVersionUID = 1313493323784566947L;
/*  28:    */   private final double x;
/*  29:    */   private final double y;
/*  30:    */   private final double z;
/*  31:    */   
/*  32:    */   public Vector3D(double x, double y, double z)
/*  33:    */   {
/*  34: 96 */     this.x = x;
/*  35: 97 */     this.y = y;
/*  36: 98 */     this.z = z;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Vector3D(double[] v)
/*  40:    */     throws DimensionMismatchException
/*  41:    */   {
/*  42:108 */     if (v.length != 3) {
/*  43:109 */       throw new DimensionMismatchException(v.length, 3);
/*  44:    */     }
/*  45:111 */     this.x = v[0];
/*  46:112 */     this.y = v[1];
/*  47:113 */     this.z = v[2];
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Vector3D(double alpha, double delta)
/*  51:    */   {
/*  52:125 */     double cosDelta = FastMath.cos(delta);
/*  53:126 */     this.x = (FastMath.cos(alpha) * cosDelta);
/*  54:127 */     this.y = (FastMath.sin(alpha) * cosDelta);
/*  55:128 */     this.z = FastMath.sin(delta);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Vector3D(double a, Vector3D u)
/*  59:    */   {
/*  60:138 */     this.x = (a * u.x);
/*  61:139 */     this.y = (a * u.y);
/*  62:140 */     this.z = (a * u.z);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2)
/*  66:    */   {
/*  67:152 */     this.x = MathArrays.linearCombination(a1, u1.x, a2, u2.x);
/*  68:153 */     this.y = MathArrays.linearCombination(a1, u1.y, a2, u2.y);
/*  69:154 */     this.z = MathArrays.linearCombination(a1, u1.z, a2, u2.z);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3)
/*  73:    */   {
/*  74:169 */     this.x = MathArrays.linearCombination(a1, u1.x, a2, u2.x, a3, u3.x);
/*  75:170 */     this.y = MathArrays.linearCombination(a1, u1.y, a2, u2.y, a3, u3.y);
/*  76:171 */     this.z = MathArrays.linearCombination(a1, u1.z, a2, u2.z, a3, u3.z);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3, double a4, Vector3D u4)
/*  80:    */   {
/*  81:188 */     this.x = MathArrays.linearCombination(a1, u1.x, a2, u2.x, a3, u3.x, a4, u4.x);
/*  82:189 */     this.y = MathArrays.linearCombination(a1, u1.y, a2, u2.y, a3, u3.y, a4, u4.y);
/*  83:190 */     this.z = MathArrays.linearCombination(a1, u1.z, a2, u2.z, a3, u3.z, a4, u4.z);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public double getX()
/*  87:    */   {
/*  88:198 */     return this.x;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public double getY()
/*  92:    */   {
/*  93:206 */     return this.y;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public double getZ()
/*  97:    */   {
/*  98:214 */     return this.z;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public double[] toArray()
/* 102:    */   {
/* 103:222 */     return new double[] { this.x, this.y, this.z };
/* 104:    */   }
/* 105:    */   
/* 106:    */   public Space getSpace()
/* 107:    */   {
/* 108:227 */     return Euclidean3D.getInstance();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public Vector3D getZero()
/* 112:    */   {
/* 113:232 */     return ZERO;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public double getNorm1()
/* 117:    */   {
/* 118:237 */     return FastMath.abs(this.x) + FastMath.abs(this.y) + FastMath.abs(this.z);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public double getNorm()
/* 122:    */   {
/* 123:243 */     return FastMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public double getNormSq()
/* 127:    */   {
/* 128:249 */     return this.x * this.x + this.y * this.y + this.z * this.z;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public double getNormInf()
/* 132:    */   {
/* 133:254 */     return FastMath.max(FastMath.max(FastMath.abs(this.x), FastMath.abs(this.y)), FastMath.abs(this.z));
/* 134:    */   }
/* 135:    */   
/* 136:    */   public double getAlpha()
/* 137:    */   {
/* 138:262 */     return FastMath.atan2(this.y, this.x);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public double getDelta()
/* 142:    */   {
/* 143:270 */     return FastMath.asin(this.z / getNorm());
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Vector3D add(Vector<Euclidean3D> v)
/* 147:    */   {
/* 148:275 */     Vector3D v3 = (Vector3D)v;
/* 149:276 */     return new Vector3D(this.x + v3.x, this.y + v3.y, this.z + v3.z);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public Vector3D add(double factor, Vector<Euclidean3D> v)
/* 153:    */   {
/* 154:281 */     return new Vector3D(1.0D, this, factor, (Vector3D)v);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public Vector3D subtract(Vector<Euclidean3D> v)
/* 158:    */   {
/* 159:286 */     Vector3D v3 = (Vector3D)v;
/* 160:287 */     return new Vector3D(this.x - v3.x, this.y - v3.y, this.z - v3.z);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public Vector3D subtract(double factor, Vector<Euclidean3D> v)
/* 164:    */   {
/* 165:292 */     return new Vector3D(1.0D, this, -factor, (Vector3D)v);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public Vector3D normalize()
/* 169:    */   {
/* 170:297 */     double s = getNorm();
/* 171:298 */     if (s == 0.0D) {
/* 172:299 */       throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
/* 173:    */     }
/* 174:301 */     return scalarMultiply(1.0D / s);
/* 175:    */   }
/* 176:    */   
/* 177:    */   public Vector3D orthogonal()
/* 178:    */   {
/* 179:321 */     double threshold = 0.6D * getNorm();
/* 180:322 */     if (threshold == 0.0D) {
/* 181:323 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
/* 182:    */     }
/* 183:326 */     if ((this.x >= -threshold) && (this.x <= threshold))
/* 184:    */     {
/* 185:327 */       double inverse = 1.0D / FastMath.sqrt(this.y * this.y + this.z * this.z);
/* 186:328 */       return new Vector3D(0.0D, inverse * this.z, -inverse * this.y);
/* 187:    */     }
/* 188:329 */     if ((this.y >= -threshold) && (this.y <= threshold))
/* 189:    */     {
/* 190:330 */       double inverse = 1.0D / FastMath.sqrt(this.x * this.x + this.z * this.z);
/* 191:331 */       return new Vector3D(-inverse * this.z, 0.0D, inverse * this.x);
/* 192:    */     }
/* 193:333 */     double inverse = 1.0D / FastMath.sqrt(this.x * this.x + this.y * this.y);
/* 194:334 */     return new Vector3D(inverse * this.y, -inverse * this.x, 0.0D);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public static double angle(Vector3D v1, Vector3D v2)
/* 198:    */   {
/* 199:351 */     double normProduct = v1.getNorm() * v2.getNorm();
/* 200:352 */     if (normProduct == 0.0D) {
/* 201:353 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
/* 202:    */     }
/* 203:356 */     double dot = v1.dotProduct(v2);
/* 204:357 */     double threshold = normProduct * 0.9999D;
/* 205:358 */     if ((dot < -threshold) || (dot > threshold))
/* 206:    */     {
/* 207:360 */       Vector3D v3 = crossProduct(v1, v2);
/* 208:361 */       if (dot >= 0.0D) {
/* 209:362 */         return FastMath.asin(v3.getNorm() / normProduct);
/* 210:    */       }
/* 211:364 */       return 3.141592653589793D - FastMath.asin(v3.getNorm() / normProduct);
/* 212:    */     }
/* 213:368 */     return FastMath.acos(dot / normProduct);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public Vector3D negate()
/* 217:    */   {
/* 218:374 */     return new Vector3D(-this.x, -this.y, -this.z);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public Vector3D scalarMultiply(double a)
/* 222:    */   {
/* 223:379 */     return new Vector3D(a * this.x, a * this.y, a * this.z);
/* 224:    */   }
/* 225:    */   
/* 226:    */   public boolean isNaN()
/* 227:    */   {
/* 228:384 */     return (Double.isNaN(this.x)) || (Double.isNaN(this.y)) || (Double.isNaN(this.z));
/* 229:    */   }
/* 230:    */   
/* 231:    */   public boolean isInfinite()
/* 232:    */   {
/* 233:389 */     return (!isNaN()) && ((Double.isInfinite(this.x)) || (Double.isInfinite(this.y)) || (Double.isInfinite(this.z)));
/* 234:    */   }
/* 235:    */   
/* 236:    */   public boolean equals(Object other)
/* 237:    */   {
/* 238:414 */     if (this == other) {
/* 239:415 */       return true;
/* 240:    */     }
/* 241:418 */     if ((other instanceof Vector3D))
/* 242:    */     {
/* 243:419 */       Vector3D rhs = (Vector3D)other;
/* 244:420 */       if (rhs.isNaN()) {
/* 245:421 */         return isNaN();
/* 246:    */       }
/* 247:424 */       return (this.x == rhs.x) && (this.y == rhs.y) && (this.z == rhs.z);
/* 248:    */     }
/* 249:426 */     return false;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public int hashCode()
/* 253:    */   {
/* 254:438 */     if (isNaN()) {
/* 255:439 */       return 642;
/* 256:    */     }
/* 257:441 */     return 643 * (164 * MathUtils.hash(this.x) + 3 * MathUtils.hash(this.y) + MathUtils.hash(this.z));
/* 258:    */   }
/* 259:    */   
/* 260:    */   public double dotProduct(Vector<Euclidean3D> v)
/* 261:    */   {
/* 262:453 */     Vector3D v3 = (Vector3D)v;
/* 263:454 */     return MathArrays.linearCombination(this.x, v3.x, this.y, v3.y, this.z, v3.z);
/* 264:    */   }
/* 265:    */   
/* 266:    */   public Vector3D crossProduct(Vector<Euclidean3D> v)
/* 267:    */   {
/* 268:462 */     Vector3D v3 = (Vector3D)v;
/* 269:463 */     return new Vector3D(MathArrays.linearCombination(this.y, v3.z, -this.z, v3.y), MathArrays.linearCombination(this.z, v3.x, -this.x, v3.z), MathArrays.linearCombination(this.x, v3.y, -this.y, v3.x));
/* 270:    */   }
/* 271:    */   
/* 272:    */   public double distance1(Vector<Euclidean3D> v)
/* 273:    */   {
/* 274:470 */     Vector3D v3 = (Vector3D)v;
/* 275:471 */     double dx = FastMath.abs(v3.x - this.x);
/* 276:472 */     double dy = FastMath.abs(v3.y - this.y);
/* 277:473 */     double dz = FastMath.abs(v3.z - this.z);
/* 278:474 */     return dx + dy + dz;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public double distance(Vector<Euclidean3D> v)
/* 282:    */   {
/* 283:479 */     Vector3D v3 = (Vector3D)v;
/* 284:480 */     double dx = v3.x - this.x;
/* 285:481 */     double dy = v3.y - this.y;
/* 286:482 */     double dz = v3.z - this.z;
/* 287:483 */     return FastMath.sqrt(dx * dx + dy * dy + dz * dz);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public double distanceInf(Vector<Euclidean3D> v)
/* 291:    */   {
/* 292:488 */     Vector3D v3 = (Vector3D)v;
/* 293:489 */     double dx = FastMath.abs(v3.x - this.x);
/* 294:490 */     double dy = FastMath.abs(v3.y - this.y);
/* 295:491 */     double dz = FastMath.abs(v3.z - this.z);
/* 296:492 */     return FastMath.max(FastMath.max(dx, dy), dz);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public double distanceSq(Vector<Euclidean3D> v)
/* 300:    */   {
/* 301:497 */     Vector3D v3 = (Vector3D)v;
/* 302:498 */     double dx = v3.x - this.x;
/* 303:499 */     double dy = v3.y - this.y;
/* 304:500 */     double dz = v3.z - this.z;
/* 305:501 */     return dx * dx + dy * dy + dz * dz;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public static double dotProduct(Vector3D v1, Vector3D v2)
/* 309:    */   {
/* 310:510 */     return v1.dotProduct(v2);
/* 311:    */   }
/* 312:    */   
/* 313:    */   public static Vector3D crossProduct(Vector3D v1, Vector3D v2)
/* 314:    */   {
/* 315:519 */     return v1.crossProduct(v2);
/* 316:    */   }
/* 317:    */   
/* 318:    */   public static double distance1(Vector3D v1, Vector3D v2)
/* 319:    */   {
/* 320:531 */     return v1.distance1(v2);
/* 321:    */   }
/* 322:    */   
/* 323:    */   public static double distance(Vector3D v1, Vector3D v2)
/* 324:    */   {
/* 325:543 */     return v1.distance(v2);
/* 326:    */   }
/* 327:    */   
/* 328:    */   public static double distanceInf(Vector3D v1, Vector3D v2)
/* 329:    */   {
/* 330:555 */     return v1.distanceInf(v2);
/* 331:    */   }
/* 332:    */   
/* 333:    */   public static double distanceSq(Vector3D v1, Vector3D v2)
/* 334:    */   {
/* 335:567 */     return v1.distanceSq(v2);
/* 336:    */   }
/* 337:    */   
/* 338:    */   public String toString()
/* 339:    */   {
/* 340:575 */     return Vector3DFormat.getInstance().format(this);
/* 341:    */   }
/* 342:    */   
/* 343:    */   public String toString(NumberFormat format)
/* 344:    */   {
/* 345:580 */     return new Vector3DFormat(format).format(this);
/* 346:    */   }
/* 347:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.Vector3D
 * JD-Core Version:    0.7.0.1
 */
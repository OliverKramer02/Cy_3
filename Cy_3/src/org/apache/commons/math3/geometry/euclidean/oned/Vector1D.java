/*   1:    */ package org.apache.commons.math3.geometry.euclidean.oned;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.geometry.Space;
/*   7:    */ import org.apache.commons.math3.geometry.Vector;
/*   8:    */ import org.apache.commons.math3.util.FastMath;
/*   9:    */ import org.apache.commons.math3.util.MathUtils;
/*  10:    */ 
/*  11:    */ public class Vector1D
/*  12:    */   implements Vector<Euclidean1D>
/*  13:    */ {
/*  14: 36 */   public static final Vector1D ZERO = new Vector1D(0.0D);
/*  15: 39 */   public static final Vector1D ONE = new Vector1D(1.0D);
/*  16: 43 */   public static final Vector1D NaN = new Vector1D((0.0D / 0.0D));
/*  17: 47 */   public static final Vector1D POSITIVE_INFINITY = new Vector1D((1.0D / 0.0D));
/*  18: 51 */   public static final Vector1D NEGATIVE_INFINITY = new Vector1D((-1.0D / 0.0D));
/*  19:    */   private static final long serialVersionUID = 7556674948671647925L;
/*  20:    */   private final double x;
/*  21:    */   
/*  22:    */   public Vector1D(double x)
/*  23:    */   {
/*  24: 66 */     this.x = x;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Vector1D(double a, Vector1D u)
/*  28:    */   {
/*  29: 76 */     this.x = (a * u.x);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2)
/*  33:    */   {
/*  34: 88 */     this.x = (a1 * u1.x + a2 * u2.x);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2, double a3, Vector1D u3)
/*  38:    */   {
/*  39:103 */     this.x = (a1 * u1.x + a2 * u2.x + a3 * u3.x);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2, double a3, Vector1D u3, double a4, Vector1D u4)
/*  43:    */   {
/*  44:120 */     this.x = (a1 * u1.x + a2 * u2.x + a3 * u3.x + a4 * u4.x);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getX()
/*  48:    */   {
/*  49:128 */     return this.x;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Space getSpace()
/*  53:    */   {
/*  54:133 */     return Euclidean1D.getInstance();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Vector1D getZero()
/*  58:    */   {
/*  59:138 */     return ZERO;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public double getNorm1()
/*  63:    */   {
/*  64:143 */     return FastMath.abs(this.x);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public double getNorm()
/*  68:    */   {
/*  69:148 */     return FastMath.abs(this.x);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public double getNormSq()
/*  73:    */   {
/*  74:153 */     return this.x * this.x;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public double getNormInf()
/*  78:    */   {
/*  79:158 */     return FastMath.abs(this.x);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Vector1D add(Vector<Euclidean1D> v)
/*  83:    */   {
/*  84:163 */     Vector1D v1 = (Vector1D)v;
/*  85:164 */     return new Vector1D(this.x + v1.getX());
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Vector1D add(double factor, Vector<Euclidean1D> v)
/*  89:    */   {
/*  90:169 */     Vector1D v1 = (Vector1D)v;
/*  91:170 */     return new Vector1D(this.x + factor * v1.getX());
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Vector1D subtract(Vector<Euclidean1D> p)
/*  95:    */   {
/*  96:175 */     Vector1D p3 = (Vector1D)p;
/*  97:176 */     return new Vector1D(this.x - p3.x);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Vector1D subtract(double factor, Vector<Euclidean1D> v)
/* 101:    */   {
/* 102:181 */     Vector1D v1 = (Vector1D)v;
/* 103:182 */     return new Vector1D(this.x - factor * v1.getX());
/* 104:    */   }
/* 105:    */   
/* 106:    */   public Vector1D normalize()
/* 107:    */   {
/* 108:187 */     double s = getNorm();
/* 109:188 */     if (s == 0.0D) {
/* 110:189 */       throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
/* 111:    */     }
/* 112:191 */     return scalarMultiply(1.0D / s);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public Vector1D negate()
/* 116:    */   {
/* 117:195 */     return new Vector1D(-this.x);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Vector1D scalarMultiply(double a)
/* 121:    */   {
/* 122:200 */     return new Vector1D(a * this.x);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public boolean isNaN()
/* 126:    */   {
/* 127:205 */     return Double.isNaN(this.x);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public boolean isInfinite()
/* 131:    */   {
/* 132:210 */     return (!isNaN()) && (Double.isInfinite(this.x));
/* 133:    */   }
/* 134:    */   
/* 135:    */   public double distance1(Vector<Euclidean1D> p)
/* 136:    */   {
/* 137:215 */     Vector1D p3 = (Vector1D)p;
/* 138:216 */     double dx = FastMath.abs(p3.x - this.x);
/* 139:217 */     return dx;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public double distance(Vector<Euclidean1D> p)
/* 143:    */   {
/* 144:222 */     Vector1D p3 = (Vector1D)p;
/* 145:223 */     double dx = p3.x - this.x;
/* 146:224 */     return FastMath.abs(dx);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public double distanceInf(Vector<Euclidean1D> p)
/* 150:    */   {
/* 151:229 */     Vector1D p3 = (Vector1D)p;
/* 152:230 */     double dx = FastMath.abs(p3.x - this.x);
/* 153:231 */     return dx;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public double distanceSq(Vector<Euclidean1D> p)
/* 157:    */   {
/* 158:236 */     Vector1D p3 = (Vector1D)p;
/* 159:237 */     double dx = p3.x - this.x;
/* 160:238 */     return dx * dx;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public double dotProduct(Vector<Euclidean1D> v)
/* 164:    */   {
/* 165:243 */     Vector1D v1 = (Vector1D)v;
/* 166:244 */     return this.x * v1.x;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static double distance(Vector1D p1, Vector1D p2)
/* 170:    */   {
/* 171:256 */     return p1.distance(p2);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static double distanceInf(Vector1D p1, Vector1D p2)
/* 175:    */   {
/* 176:268 */     return p1.distanceInf(p2);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static double distanceSq(Vector1D p1, Vector1D p2)
/* 180:    */   {
/* 181:280 */     return p1.distanceSq(p2);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public boolean equals(Object other)
/* 185:    */   {
/* 186:305 */     if (this == other) {
/* 187:306 */       return true;
/* 188:    */     }
/* 189:309 */     if ((other instanceof Vector1D))
/* 190:    */     {
/* 191:310 */       Vector1D rhs = (Vector1D)other;
/* 192:311 */       if (rhs.isNaN()) {
/* 193:312 */         return isNaN();
/* 194:    */       }
/* 195:315 */       return this.x == rhs.x;
/* 196:    */     }
/* 197:317 */     return false;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public int hashCode()
/* 201:    */   {
/* 202:329 */     if (isNaN()) {
/* 203:330 */       return 7785;
/* 204:    */     }
/* 205:332 */     return 997 * MathUtils.hash(this.x);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public String toString()
/* 209:    */   {
/* 210:340 */     return Vector1DFormat.getInstance().format(this);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public String toString(NumberFormat format)
/* 214:    */   {
/* 215:345 */     return new Vector1DFormat(format).format(this);
/* 216:    */   }
/* 217:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.oned.Vector1D
 * JD-Core Version:    0.7.0.1
 */
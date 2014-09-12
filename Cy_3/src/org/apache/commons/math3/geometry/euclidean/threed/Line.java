/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.geometry.Vector;
/*   6:    */ import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
/*   8:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*   9:    */ import org.apache.commons.math3.geometry.partitioning.Embedding;
/*  10:    */ import org.apache.commons.math3.util.FastMath;
/*  11:    */ 
/*  12:    */ public class Line
/*  13:    */   implements Embedding<Euclidean3D, Euclidean1D>
/*  14:    */ {
/*  15:    */   private Vector3D direction;
/*  16:    */   private Vector3D zero;
/*  17:    */   
/*  18:    */   public Line(Vector3D p1, Vector3D p2)
/*  19:    */   {
/*  20: 55 */     reset(p1, p2);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Line(Line line)
/*  24:    */   {
/*  25: 64 */     this.direction = line.direction;
/*  26: 65 */     this.zero = line.zero;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void reset(Vector3D p1, Vector3D p2)
/*  30:    */   {
/*  31: 74 */     Vector3D delta = p2.subtract(p1);
/*  32: 75 */     double norm2 = delta.getNormSq();
/*  33: 76 */     if (norm2 == 0.0D) {
/*  34: 77 */       throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM, new Object[0]);
/*  35:    */     }
/*  36: 79 */     this.direction = new Vector3D(1.0D / FastMath.sqrt(norm2), delta);
/*  37: 80 */     this.zero = new Vector3D(1.0D, p1, -p1.dotProduct(delta) / norm2, delta);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Line revert()
/*  41:    */   {
/*  42: 87 */     return new Line(this.zero, this.zero.subtract(this.direction));
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Vector3D getDirection()
/*  46:    */   {
/*  47: 94 */     return this.direction;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Vector3D getOrigin()
/*  51:    */   {
/*  52:101 */     return this.zero;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public double getAbscissa(Vector3D point)
/*  56:    */   {
/*  57:112 */     return point.subtract(this.zero).dotProduct(this.direction);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Vector3D pointAt(double abscissa)
/*  61:    */   {
/*  62:120 */     return new Vector3D(1.0D, this.zero, abscissa, this.direction);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Vector1D toSubSpace(Vector<Euclidean3D> point)
/*  66:    */   {
/*  67:127 */     return new Vector1D(getAbscissa((Vector3D)point));
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Vector3D toSpace(Vector<Euclidean1D> point)
/*  71:    */   {
/*  72:134 */     return pointAt(((Vector1D)point).getX());
/*  73:    */   }
/*  74:    */   
/*  75:    */   public boolean isSimilarTo(Line line)
/*  76:    */   {
/*  77:145 */     double angle = Vector3D.angle(this.direction, line.direction);
/*  78:146 */     return ((angle < 1.0E-010D) || (angle > 3.141592653489793D)) && (contains(line.zero));
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean contains(Vector3D p)
/*  82:    */   {
/*  83:154 */     return distance(p) < 1.0E-010D;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public double distance(Vector3D p)
/*  87:    */   {
/*  88:162 */     Vector3D d = p.subtract(this.zero);
/*  89:163 */     Vector3D n = new Vector3D(1.0D, d, -d.dotProduct(this.direction), this.direction);
/*  90:164 */     return n.getNorm();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public double distance(Line line)
/*  94:    */   {
/*  95:173 */     Vector3D normal = Vector3D.crossProduct(this.direction, line.direction);
/*  96:174 */     double n = normal.getNorm();
/*  97:175 */     if (n < 2.225073858507201E-308D) {
/*  98:177 */       return distance(line.zero);
/*  99:    */     }
/* 100:181 */     double offset = line.zero.subtract(this.zero).dotProduct(normal) / n;
/* 101:    */     
/* 102:183 */     return FastMath.abs(offset);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Vector3D closestPoint(Line line)
/* 106:    */   {
/* 107:193 */     double cos = this.direction.dotProduct(line.direction);
/* 108:194 */     double n = 1.0D - cos * cos;
/* 109:195 */     if (n < 1.110223024625157E-016D) {
/* 110:197 */       return this.zero;
/* 111:    */     }
/* 112:200 */     Vector3D delta0 = line.zero.subtract(this.zero);
/* 113:201 */     double a = delta0.dotProduct(this.direction);
/* 114:202 */     double b = delta0.dotProduct(line.direction);
/* 115:    */     
/* 116:204 */     return new Vector3D(1.0D, this.zero, (a - b * cos) / n, this.direction);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public Vector3D intersection(Line line)
/* 120:    */   {
/* 121:214 */     Vector3D closest = closestPoint(line);
/* 122:215 */     return line.contains(closest) ? closest : null;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public SubLine wholeLine()
/* 126:    */   {
/* 127:222 */     return new SubLine(this, new IntervalsSet());
/* 128:    */   }
/* 129:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.Line
 * JD-Core Version:    0.7.0.1
 */
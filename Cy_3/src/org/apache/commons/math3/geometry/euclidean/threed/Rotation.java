/*    1:     */ package org.apache.commons.math3.geometry.euclidean.threed;
/*    2:     */ 
/*    3:     */ import java.io.Serializable;
/*    4:     */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*    5:     */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*    6:     */ import org.apache.commons.math3.util.FastMath;
/*    7:     */ 
/*    8:     */ public class Rotation
/*    9:     */   implements Serializable
/*   10:     */ {
/*   11:  98 */   public static final Rotation IDENTITY = new Rotation(1.0D, 0.0D, 0.0D, 0.0D, false);
/*   12:     */   private static final long serialVersionUID = -2153622329907944313L;
/*   13:     */   private final double q0;
/*   14:     */   private final double q1;
/*   15:     */   private final double q2;
/*   16:     */   private final double q3;
/*   17:     */   
/*   18:     */   public Rotation(double q0, double q1, double q2, double q3, boolean needsNormalization)
/*   19:     */   {
/*   20: 136 */     if (needsNormalization)
/*   21:     */     {
/*   22: 138 */       double inv = 1.0D / FastMath.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
/*   23: 139 */       q0 *= inv;
/*   24: 140 */       q1 *= inv;
/*   25: 141 */       q2 *= inv;
/*   26: 142 */       q3 *= inv;
/*   27:     */     }
/*   28: 145 */     this.q0 = q0;
/*   29: 146 */     this.q1 = q1;
/*   30: 147 */     this.q2 = q2;
/*   31: 148 */     this.q3 = q3;
/*   32:     */   }
/*   33:     */   
/*   34:     */   public Rotation(Vector3D axis, double angle)
/*   35:     */   {
/*   36: 175 */     double norm = axis.getNorm();
/*   37: 176 */     if (norm == 0.0D) {
/*   38: 177 */       throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS, new Object[0]);
/*   39:     */     }
/*   40: 180 */     double halfAngle = -0.5D * angle;
/*   41: 181 */     double coeff = FastMath.sin(halfAngle) / norm;
/*   42:     */     
/*   43: 183 */     this.q0 = FastMath.cos(halfAngle);
/*   44: 184 */     this.q1 = (coeff * axis.getX());
/*   45: 185 */     this.q2 = (coeff * axis.getY());
/*   46: 186 */     this.q3 = (coeff * axis.getZ());
/*   47:     */   }
/*   48:     */   
/*   49:     */   public Rotation(double[][] m, double threshold)
/*   50:     */     throws NotARotationMatrixException
/*   51:     */   {
/*   52: 224 */     if ((m.length != 3) || (m[0].length != 3) || (m[1].length != 3) || (m[2].length != 3)) {
/*   53: 226 */       throw new NotARotationMatrixException(LocalizedFormats.ROTATION_MATRIX_DIMENSIONS, new Object[] { Integer.valueOf(m.length), Integer.valueOf(m[0].length) });
/*   54:     */     }
/*   55: 232 */     double[][] ort = orthogonalizeMatrix(m, threshold);
/*   56:     */     
/*   57:     */ 
/*   58: 235 */     double det = ort[0][0] * (ort[1][1] * ort[2][2] - ort[2][1] * ort[1][2]) - ort[1][0] * (ort[0][1] * ort[2][2] - ort[2][1] * ort[0][2]) + ort[2][0] * (ort[0][1] * ort[1][2] - ort[1][1] * ort[0][2]);
/*   59: 238 */     if (det < 0.0D) {
/*   60: 239 */       throw new NotARotationMatrixException(LocalizedFormats.CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT, new Object[] { Double.valueOf(det) });
/*   61:     */     }
/*   62: 255 */     double s = ort[0][0] + ort[1][1] + ort[2][2];
/*   63: 256 */     if (s > -0.19D)
/*   64:     */     {
/*   65: 258 */       this.q0 = (0.5D * FastMath.sqrt(s + 1.0D));
/*   66: 259 */       double inv = 0.25D / this.q0;
/*   67: 260 */       this.q1 = (inv * (ort[1][2] - ort[2][1]));
/*   68: 261 */       this.q2 = (inv * (ort[2][0] - ort[0][2]));
/*   69: 262 */       this.q3 = (inv * (ort[0][1] - ort[1][0]));
/*   70:     */     }
/*   71:     */     else
/*   72:     */     {
/*   73: 264 */       s = ort[0][0] - ort[1][1] - ort[2][2];
/*   74: 265 */       if (s > -0.19D)
/*   75:     */       {
/*   76: 267 */         this.q1 = (0.5D * FastMath.sqrt(s + 1.0D));
/*   77: 268 */         double inv = 0.25D / this.q1;
/*   78: 269 */         this.q0 = (inv * (ort[1][2] - ort[2][1]));
/*   79: 270 */         this.q2 = (inv * (ort[0][1] + ort[1][0]));
/*   80: 271 */         this.q3 = (inv * (ort[0][2] + ort[2][0]));
/*   81:     */       }
/*   82:     */       else
/*   83:     */       {
/*   84: 273 */         s = ort[1][1] - ort[0][0] - ort[2][2];
/*   85: 274 */         if (s > -0.19D)
/*   86:     */         {
/*   87: 276 */           this.q2 = (0.5D * FastMath.sqrt(s + 1.0D));
/*   88: 277 */           double inv = 0.25D / this.q2;
/*   89: 278 */           this.q0 = (inv * (ort[2][0] - ort[0][2]));
/*   90: 279 */           this.q1 = (inv * (ort[0][1] + ort[1][0]));
/*   91: 280 */           this.q3 = (inv * (ort[2][1] + ort[1][2]));
/*   92:     */         }
/*   93:     */         else
/*   94:     */         {
/*   95: 283 */           s = ort[2][2] - ort[0][0] - ort[1][1];
/*   96: 284 */           this.q3 = (0.5D * FastMath.sqrt(s + 1.0D));
/*   97: 285 */           double inv = 0.25D / this.q3;
/*   98: 286 */           this.q0 = (inv * (ort[0][1] - ort[1][0]));
/*   99: 287 */           this.q1 = (inv * (ort[0][2] + ort[2][0]));
/*  100: 288 */           this.q2 = (inv * (ort[2][1] + ort[1][2]));
/*  101:     */         }
/*  102:     */       }
/*  103:     */     }
/*  104:     */   }
/*  105:     */   
/*  106:     */   public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2)
/*  107:     */   {
/*  108: 316 */     double u1u1 = u1.getNormSq();
/*  109: 317 */     double u2u2 = u2.getNormSq();
/*  110: 318 */     double v1v1 = v1.getNormSq();
/*  111: 319 */     double v2v2 = v2.getNormSq();
/*  112: 320 */     if ((u1u1 == 0.0D) || (u2u2 == 0.0D) || (v1v1 == 0.0D) || (v2v2 == 0.0D)) {
/*  113: 321 */       throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new Object[0]);
/*  114:     */     }
/*  115: 325 */     v1 = new Vector3D(FastMath.sqrt(u1u1 / v1v1), v1);
/*  116:     */     
/*  117:     */ 
/*  118: 328 */     double u1u2 = u1.dotProduct(u2);
/*  119: 329 */     double v1v2 = v1.dotProduct(v2);
/*  120: 330 */     double coeffU = u1u2 / u1u1;
/*  121: 331 */     double coeffV = v1v2 / u1u1;
/*  122: 332 */     double beta = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
/*  123: 333 */     double alpha = coeffU - beta * coeffV;
/*  124: 334 */     v2 = new Vector3D(alpha, v1, beta, v2);
/*  125:     */     
/*  126:     */ 
/*  127: 337 */     Vector3D uRef = u1;
/*  128: 338 */     Vector3D vRef = v1;
/*  129: 339 */     Vector3D v1Su1 = v1.subtract(u1);
/*  130: 340 */     Vector3D v2Su2 = v2.subtract(u2);
/*  131: 341 */     Vector3D k = v1Su1.crossProduct(v2Su2);
/*  132: 342 */     Vector3D u3 = u1.crossProduct(u2);
/*  133: 343 */     double c = k.dotProduct(u3);
/*  134: 344 */     double inPlaneThreshold = 0.001D;
/*  135: 345 */     if (c <= 0.001D * k.getNorm() * u3.getNorm())
/*  136:     */     {
/*  137: 348 */       Vector3D v3 = Vector3D.crossProduct(v1, v2);
/*  138: 349 */       Vector3D v3Su3 = v3.subtract(u3);
/*  139: 350 */       k = v1Su1.crossProduct(v3Su3);
/*  140: 351 */       Vector3D u2Prime = u1.crossProduct(u3);
/*  141: 352 */       c = k.dotProduct(u2Prime);
/*  142: 354 */       if (c <= 0.001D * k.getNorm() * u2Prime.getNorm())
/*  143:     */       {
/*  144: 357 */         k = v2Su2.crossProduct(v3Su3);
/*  145: 358 */         c = k.dotProduct(u2.crossProduct(u3));
/*  146: 360 */         if (c <= 0.0D)
/*  147:     */         {
/*  148: 363 */           this.q0 = 1.0D;
/*  149: 364 */           this.q1 = 0.0D;
/*  150: 365 */           this.q2 = 0.0D;
/*  151: 366 */           this.q3 = 0.0D;
/*  152: 367 */           return;
/*  153:     */         }
/*  154: 371 */         uRef = u2;
/*  155: 372 */         vRef = v2;
/*  156:     */       }
/*  157:     */     }
/*  158: 379 */     c = FastMath.sqrt(c);
/*  159: 380 */     double inv = 1.0D / (c + c);
/*  160: 381 */     this.q1 = (inv * k.getX());
/*  161: 382 */     this.q2 = (inv * k.getY());
/*  162: 383 */     this.q3 = (inv * k.getZ());
/*  163:     */     
/*  164:     */ 
/*  165: 386 */     k = new Vector3D(uRef.getY() * this.q3 - uRef.getZ() * this.q2, uRef.getZ() * this.q1 - uRef.getX() * this.q3, uRef.getX() * this.q2 - uRef.getY() * this.q1);
/*  166:     */     
/*  167:     */ 
/*  168: 389 */     this.q0 = (vRef.dotProduct(k) / (2.0D * k.getNormSq()));
/*  169:     */   }
/*  170:     */   
/*  171:     */   public Rotation(Vector3D u, Vector3D v)
/*  172:     */   {
/*  173: 408 */     double normProduct = u.getNorm() * v.getNorm();
/*  174: 409 */     if (normProduct == 0.0D) {
/*  175: 410 */       throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new Object[0]);
/*  176:     */     }
/*  177: 413 */     double dot = u.dotProduct(v);
/*  178: 415 */     if (dot < -0.999999999999998D * normProduct)
/*  179:     */     {
/*  180: 418 */       Vector3D w = u.orthogonal();
/*  181: 419 */       this.q0 = 0.0D;
/*  182: 420 */       this.q1 = (-w.getX());
/*  183: 421 */       this.q2 = (-w.getY());
/*  184: 422 */       this.q3 = (-w.getZ());
/*  185:     */     }
/*  186:     */     else
/*  187:     */     {
/*  188: 426 */       this.q0 = FastMath.sqrt(0.5D * (1.0D + dot / normProduct));
/*  189: 427 */       double coeff = 1.0D / (2.0D * this.q0 * normProduct);
/*  190: 428 */       Vector3D q = v.crossProduct(u);
/*  191: 429 */       this.q1 = (coeff * q.getX());
/*  192: 430 */       this.q2 = (coeff * q.getY());
/*  193: 431 */       this.q3 = (coeff * q.getZ());
/*  194:     */     }
/*  195:     */   }
/*  196:     */   
/*  197:     */   public Rotation(RotationOrder order, double alpha1, double alpha2, double alpha3)
/*  198:     */   {
/*  199: 457 */     Rotation r1 = new Rotation(order.getA1(), alpha1);
/*  200: 458 */     Rotation r2 = new Rotation(order.getA2(), alpha2);
/*  201: 459 */     Rotation r3 = new Rotation(order.getA3(), alpha3);
/*  202: 460 */     Rotation composed = r1.applyTo(r2.applyTo(r3));
/*  203: 461 */     this.q0 = composed.q0;
/*  204: 462 */     this.q1 = composed.q1;
/*  205: 463 */     this.q2 = composed.q2;
/*  206: 464 */     this.q3 = composed.q3;
/*  207:     */   }
/*  208:     */   
/*  209:     */   public Rotation revert()
/*  210:     */   {
/*  211: 475 */     return new Rotation(-this.q0, this.q1, this.q2, this.q3, false);
/*  212:     */   }
/*  213:     */   
/*  214:     */   public double getQ0()
/*  215:     */   {
/*  216: 482 */     return this.q0;
/*  217:     */   }
/*  218:     */   
/*  219:     */   public double getQ1()
/*  220:     */   {
/*  221: 489 */     return this.q1;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public double getQ2()
/*  225:     */   {
/*  226: 496 */     return this.q2;
/*  227:     */   }
/*  228:     */   
/*  229:     */   public double getQ3()
/*  230:     */   {
/*  231: 503 */     return this.q3;
/*  232:     */   }
/*  233:     */   
/*  234:     */   public Vector3D getAxis()
/*  235:     */   {
/*  236: 511 */     double squaredSine = this.q1 * this.q1 + this.q2 * this.q2 + this.q3 * this.q3;
/*  237: 512 */     if (squaredSine == 0.0D) {
/*  238: 513 */       return new Vector3D(1.0D, 0.0D, 0.0D);
/*  239:     */     }
/*  240: 514 */     if (this.q0 < 0.0D)
/*  241:     */     {
/*  242: 515 */       double inverse = 1.0D / FastMath.sqrt(squaredSine);
/*  243: 516 */       return new Vector3D(this.q1 * inverse, this.q2 * inverse, this.q3 * inverse);
/*  244:     */     }
/*  245: 518 */     double inverse = -1.0D / FastMath.sqrt(squaredSine);
/*  246: 519 */     return new Vector3D(this.q1 * inverse, this.q2 * inverse, this.q3 * inverse);
/*  247:     */   }
/*  248:     */   
/*  249:     */   public double getAngle()
/*  250:     */   {
/*  251: 527 */     if ((this.q0 < -0.1D) || (this.q0 > 0.1D)) {
/*  252: 528 */       return 2.0D * FastMath.asin(FastMath.sqrt(this.q1 * this.q1 + this.q2 * this.q2 + this.q3 * this.q3));
/*  253:     */     }
/*  254: 529 */     if (this.q0 < 0.0D) {
/*  255: 530 */       return 2.0D * FastMath.acos(-this.q0);
/*  256:     */     }
/*  257: 532 */     return 2.0D * FastMath.acos(this.q0);
/*  258:     */   }
/*  259:     */   
/*  260:     */   public double[] getAngles(RotationOrder order)
/*  261:     */     throws CardanEulerSingularityException
/*  262:     */   {
/*  263: 573 */     if (order == RotationOrder.XYZ)
/*  264:     */     {
/*  265: 580 */       Vector3D v1 = applyTo(Vector3D.PLUS_K);
/*  266: 581 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_I);
/*  267: 582 */       if ((v2.getZ() < -0.9999999999D) || (v2.getZ() > 0.9999999999D)) {
/*  268: 583 */         throw new CardanEulerSingularityException(true);
/*  269:     */       }
/*  270: 585 */       return new double[] { FastMath.atan2(-v1.getY(), v1.getZ()), FastMath.asin(v2.getZ()), FastMath.atan2(-v2.getY(), v2.getX()) };
/*  271:     */     }
/*  272: 591 */     if (order == RotationOrder.XZY)
/*  273:     */     {
/*  274: 598 */       Vector3D v1 = applyTo(Vector3D.PLUS_J);
/*  275: 599 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_I);
/*  276: 600 */       if ((v2.getY() < -0.9999999999D) || (v2.getY() > 0.9999999999D)) {
/*  277: 601 */         throw new CardanEulerSingularityException(true);
/*  278:     */       }
/*  279: 603 */       return new double[] { FastMath.atan2(v1.getZ(), v1.getY()), -FastMath.asin(v2.getY()), FastMath.atan2(v2.getZ(), v2.getX()) };
/*  280:     */     }
/*  281: 609 */     if (order == RotationOrder.YXZ)
/*  282:     */     {
/*  283: 616 */       Vector3D v1 = applyTo(Vector3D.PLUS_K);
/*  284: 617 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_J);
/*  285: 618 */       if ((v2.getZ() < -0.9999999999D) || (v2.getZ() > 0.9999999999D)) {
/*  286: 619 */         throw new CardanEulerSingularityException(true);
/*  287:     */       }
/*  288: 621 */       return new double[] { FastMath.atan2(v1.getX(), v1.getZ()), -FastMath.asin(v2.getZ()), FastMath.atan2(v2.getX(), v2.getY()) };
/*  289:     */     }
/*  290: 627 */     if (order == RotationOrder.YZX)
/*  291:     */     {
/*  292: 634 */       Vector3D v1 = applyTo(Vector3D.PLUS_I);
/*  293: 635 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_J);
/*  294: 636 */       if ((v2.getX() < -0.9999999999D) || (v2.getX() > 0.9999999999D)) {
/*  295: 637 */         throw new CardanEulerSingularityException(true);
/*  296:     */       }
/*  297: 639 */       return new double[] { FastMath.atan2(-v1.getZ(), v1.getX()), FastMath.asin(v2.getX()), FastMath.atan2(-v2.getZ(), v2.getY()) };
/*  298:     */     }
/*  299: 645 */     if (order == RotationOrder.ZXY)
/*  300:     */     {
/*  301: 652 */       Vector3D v1 = applyTo(Vector3D.PLUS_J);
/*  302: 653 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_K);
/*  303: 654 */       if ((v2.getY() < -0.9999999999D) || (v2.getY() > 0.9999999999D)) {
/*  304: 655 */         throw new CardanEulerSingularityException(true);
/*  305:     */       }
/*  306: 657 */       return new double[] { FastMath.atan2(-v1.getX(), v1.getY()), FastMath.asin(v2.getY()), FastMath.atan2(-v2.getX(), v2.getZ()) };
/*  307:     */     }
/*  308: 663 */     if (order == RotationOrder.ZYX)
/*  309:     */     {
/*  310: 670 */       Vector3D v1 = applyTo(Vector3D.PLUS_I);
/*  311: 671 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_K);
/*  312: 672 */       if ((v2.getX() < -0.9999999999D) || (v2.getX() > 0.9999999999D)) {
/*  313: 673 */         throw new CardanEulerSingularityException(true);
/*  314:     */       }
/*  315: 675 */       return new double[] { FastMath.atan2(v1.getY(), v1.getX()), -FastMath.asin(v2.getX()), FastMath.atan2(v2.getY(), v2.getZ()) };
/*  316:     */     }
/*  317: 681 */     if (order == RotationOrder.XYX)
/*  318:     */     {
/*  319: 688 */       Vector3D v1 = applyTo(Vector3D.PLUS_I);
/*  320: 689 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_I);
/*  321: 690 */       if ((v2.getX() < -0.9999999999D) || (v2.getX() > 0.9999999999D)) {
/*  322: 691 */         throw new CardanEulerSingularityException(false);
/*  323:     */       }
/*  324: 693 */       return new double[] { FastMath.atan2(v1.getY(), -v1.getZ()), FastMath.acos(v2.getX()), FastMath.atan2(v2.getY(), v2.getZ()) };
/*  325:     */     }
/*  326: 699 */     if (order == RotationOrder.XZX)
/*  327:     */     {
/*  328: 706 */       Vector3D v1 = applyTo(Vector3D.PLUS_I);
/*  329: 707 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_I);
/*  330: 708 */       if ((v2.getX() < -0.9999999999D) || (v2.getX() > 0.9999999999D)) {
/*  331: 709 */         throw new CardanEulerSingularityException(false);
/*  332:     */       }
/*  333: 711 */       return new double[] { FastMath.atan2(v1.getZ(), v1.getY()), FastMath.acos(v2.getX()), FastMath.atan2(v2.getZ(), -v2.getY()) };
/*  334:     */     }
/*  335: 717 */     if (order == RotationOrder.YXY)
/*  336:     */     {
/*  337: 724 */       Vector3D v1 = applyTo(Vector3D.PLUS_J);
/*  338: 725 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_J);
/*  339: 726 */       if ((v2.getY() < -0.9999999999D) || (v2.getY() > 0.9999999999D)) {
/*  340: 727 */         throw new CardanEulerSingularityException(false);
/*  341:     */       }
/*  342: 729 */       return new double[] { FastMath.atan2(v1.getX(), v1.getZ()), FastMath.acos(v2.getY()), FastMath.atan2(v2.getX(), -v2.getZ()) };
/*  343:     */     }
/*  344: 735 */     if (order == RotationOrder.YZY)
/*  345:     */     {
/*  346: 742 */       Vector3D v1 = applyTo(Vector3D.PLUS_J);
/*  347: 743 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_J);
/*  348: 744 */       if ((v2.getY() < -0.9999999999D) || (v2.getY() > 0.9999999999D)) {
/*  349: 745 */         throw new CardanEulerSingularityException(false);
/*  350:     */       }
/*  351: 747 */       return new double[] { FastMath.atan2(v1.getZ(), -v1.getX()), FastMath.acos(v2.getY()), FastMath.atan2(v2.getZ(), v2.getX()) };
/*  352:     */     }
/*  353: 753 */     if (order == RotationOrder.ZXZ)
/*  354:     */     {
/*  355: 760 */       Vector3D v1 = applyTo(Vector3D.PLUS_K);
/*  356: 761 */       Vector3D v2 = applyInverseTo(Vector3D.PLUS_K);
/*  357: 762 */       if ((v2.getZ() < -0.9999999999D) || (v2.getZ() > 0.9999999999D)) {
/*  358: 763 */         throw new CardanEulerSingularityException(false);
/*  359:     */       }
/*  360: 765 */       return new double[] { FastMath.atan2(v1.getX(), -v1.getY()), FastMath.acos(v2.getZ()), FastMath.atan2(v2.getX(), v2.getY()) };
/*  361:     */     }
/*  362: 778 */     Vector3D v1 = applyTo(Vector3D.PLUS_K);
/*  363: 779 */     Vector3D v2 = applyInverseTo(Vector3D.PLUS_K);
/*  364: 780 */     if ((v2.getZ() < -0.9999999999D) || (v2.getZ() > 0.9999999999D)) {
/*  365: 781 */       throw new CardanEulerSingularityException(false);
/*  366:     */     }
/*  367: 783 */     return new double[] { FastMath.atan2(v1.getY(), v1.getX()), FastMath.acos(v2.getZ()), FastMath.atan2(v2.getY(), -v2.getX()) };
/*  368:     */   }
/*  369:     */   
/*  370:     */   public double[][] getMatrix()
/*  371:     */   {
/*  372: 799 */     double q0q0 = this.q0 * this.q0;
/*  373: 800 */     double q0q1 = this.q0 * this.q1;
/*  374: 801 */     double q0q2 = this.q0 * this.q2;
/*  375: 802 */     double q0q3 = this.q0 * this.q3;
/*  376: 803 */     double q1q1 = this.q1 * this.q1;
/*  377: 804 */     double q1q2 = this.q1 * this.q2;
/*  378: 805 */     double q1q3 = this.q1 * this.q3;
/*  379: 806 */     double q2q2 = this.q2 * this.q2;
/*  380: 807 */     double q2q3 = this.q2 * this.q3;
/*  381: 808 */     double q3q3 = this.q3 * this.q3;
/*  382:     */     
/*  383:     */ 
/*  384: 811 */     double[][] m = new double[3][];
/*  385: 812 */     m[0] = new double[3];
/*  386: 813 */     m[1] = new double[3];
/*  387: 814 */     m[2] = new double[3];
/*  388:     */     
/*  389: 816 */     m[0][0] = (2.0D * (q0q0 + q1q1) - 1.0D);
/*  390: 817 */     m[1][0] = (2.0D * (q1q2 - q0q3));
/*  391: 818 */     m[2][0] = (2.0D * (q1q3 + q0q2));
/*  392:     */     
/*  393: 820 */     m[0][1] = (2.0D * (q1q2 + q0q3));
/*  394: 821 */     m[1][1] = (2.0D * (q0q0 + q2q2) - 1.0D);
/*  395: 822 */     m[2][1] = (2.0D * (q2q3 - q0q1));
/*  396:     */     
/*  397: 824 */     m[0][2] = (2.0D * (q1q3 - q0q2));
/*  398: 825 */     m[1][2] = (2.0D * (q2q3 + q0q1));
/*  399: 826 */     m[2][2] = (2.0D * (q0q0 + q3q3) - 1.0D);
/*  400:     */     
/*  401: 828 */     return m;
/*  402:     */   }
/*  403:     */   
/*  404:     */   public Vector3D applyTo(Vector3D u)
/*  405:     */   {
/*  406: 838 */     double x = u.getX();
/*  407: 839 */     double y = u.getY();
/*  408: 840 */     double z = u.getZ();
/*  409:     */     
/*  410: 842 */     double s = this.q1 * x + this.q2 * y + this.q3 * z;
/*  411:     */     
/*  412: 844 */     return new Vector3D(2.0D * (this.q0 * (x * this.q0 - (this.q2 * z - this.q3 * y)) + s * this.q1) - x, 2.0D * (this.q0 * (y * this.q0 - (this.q3 * x - this.q1 * z)) + s * this.q2) - y, 2.0D * (this.q0 * (z * this.q0 - (this.q1 * y - this.q2 * x)) + s * this.q3) - z);
/*  413:     */   }
/*  414:     */   
/*  415:     */   public void applyTo(double[] in, double[] out)
/*  416:     */   {
/*  417: 857 */     double x = in[0];
/*  418: 858 */     double y = in[1];
/*  419: 859 */     double z = in[2];
/*  420:     */     
/*  421: 861 */     double s = this.q1 * x + this.q2 * y + this.q3 * z;
/*  422:     */     
/*  423: 863 */     out[0] = (2.0D * (this.q0 * (x * this.q0 - (this.q2 * z - this.q3 * y)) + s * this.q1) - x);
/*  424: 864 */     out[1] = (2.0D * (this.q0 * (y * this.q0 - (this.q3 * x - this.q1 * z)) + s * this.q2) - y);
/*  425: 865 */     out[2] = (2.0D * (this.q0 * (z * this.q0 - (this.q1 * y - this.q2 * x)) + s * this.q3) - z);
/*  426:     */   }
/*  427:     */   
/*  428:     */   public Vector3D applyInverseTo(Vector3D u)
/*  429:     */   {
/*  430: 875 */     double x = u.getX();
/*  431: 876 */     double y = u.getY();
/*  432: 877 */     double z = u.getZ();
/*  433:     */     
/*  434: 879 */     double s = this.q1 * x + this.q2 * y + this.q3 * z;
/*  435: 880 */     double m0 = -this.q0;
/*  436:     */     
/*  437: 882 */     return new Vector3D(2.0D * (m0 * (x * m0 - (this.q2 * z - this.q3 * y)) + s * this.q1) - x, 2.0D * (m0 * (y * m0 - (this.q3 * x - this.q1 * z)) + s * this.q2) - y, 2.0D * (m0 * (z * m0 - (this.q1 * y - this.q2 * x)) + s * this.q3) - z);
/*  438:     */   }
/*  439:     */   
/*  440:     */   public void applyInverseTo(double[] in, double[] out)
/*  441:     */   {
/*  442: 895 */     double x = in[0];
/*  443: 896 */     double y = in[1];
/*  444: 897 */     double z = in[2];
/*  445:     */     
/*  446: 899 */     double s = this.q1 * x + this.q2 * y + this.q3 * z;
/*  447: 900 */     double m0 = -this.q0;
/*  448:     */     
/*  449: 902 */     out[0] = (2.0D * (m0 * (x * m0 - (this.q2 * z - this.q3 * y)) + s * this.q1) - x);
/*  450: 903 */     out[1] = (2.0D * (m0 * (y * m0 - (this.q3 * x - this.q1 * z)) + s * this.q2) - y);
/*  451: 904 */     out[2] = (2.0D * (m0 * (z * m0 - (this.q1 * y - this.q2 * x)) + s * this.q3) - z);
/*  452:     */   }
/*  453:     */   
/*  454:     */   public Rotation applyTo(Rotation r)
/*  455:     */   {
/*  456: 918 */     return new Rotation(r.q0 * this.q0 - (r.q1 * this.q1 + r.q2 * this.q2 + r.q3 * this.q3), r.q1 * this.q0 + r.q0 * this.q1 + (r.q2 * this.q3 - r.q3 * this.q2), r.q2 * this.q0 + r.q0 * this.q2 + (r.q3 * this.q1 - r.q1 * this.q3), r.q3 * this.q0 + r.q0 * this.q3 + (r.q1 * this.q2 - r.q2 * this.q1), false);
/*  457:     */   }
/*  458:     */   
/*  459:     */   public Rotation applyInverseTo(Rotation r)
/*  460:     */   {
/*  461: 937 */     return new Rotation(-r.q0 * this.q0 - (r.q1 * this.q1 + r.q2 * this.q2 + r.q3 * this.q3), -r.q1 * this.q0 + r.q0 * this.q1 + (r.q2 * this.q3 - r.q3 * this.q2), -r.q2 * this.q0 + r.q0 * this.q2 + (r.q3 * this.q1 - r.q1 * this.q3), -r.q3 * this.q0 + r.q0 * this.q3 + (r.q1 * this.q2 - r.q2 * this.q1), false);
/*  462:     */   }
/*  463:     */   
/*  464:     */   private double[][] orthogonalizeMatrix(double[][] m, double threshold)
/*  465:     */     throws NotARotationMatrixException
/*  466:     */   {
/*  467: 956 */     double[] m0 = m[0];
/*  468: 957 */     double[] m1 = m[1];
/*  469: 958 */     double[] m2 = m[2];
/*  470: 959 */     double x00 = m0[0];
/*  471: 960 */     double x01 = m0[1];
/*  472: 961 */     double x02 = m0[2];
/*  473: 962 */     double x10 = m1[0];
/*  474: 963 */     double x11 = m1[1];
/*  475: 964 */     double x12 = m1[2];
/*  476: 965 */     double x20 = m2[0];
/*  477: 966 */     double x21 = m2[1];
/*  478: 967 */     double x22 = m2[2];
/*  479: 968 */     double fn = 0.0D;
/*  480:     */     
/*  481:     */ 
/*  482: 971 */     double[][] o = new double[3][3];
/*  483: 972 */     double[] o0 = o[0];
/*  484: 973 */     double[] o1 = o[1];
/*  485: 974 */     double[] o2 = o[2];
/*  486:     */     
/*  487:     */ 
/*  488: 977 */     int i = 0;
/*  489:     */     for (;;)
/*  490:     */     {
/*  491: 978 */       i++;
/*  492: 978 */       if (i >= 11) {
/*  493:     */         break;
/*  494:     */       }
/*  495: 981 */       double mx00 = m0[0] * x00 + m1[0] * x10 + m2[0] * x20;
/*  496: 982 */       double mx10 = m0[1] * x00 + m1[1] * x10 + m2[1] * x20;
/*  497: 983 */       double mx20 = m0[2] * x00 + m1[2] * x10 + m2[2] * x20;
/*  498: 984 */       double mx01 = m0[0] * x01 + m1[0] * x11 + m2[0] * x21;
/*  499: 985 */       double mx11 = m0[1] * x01 + m1[1] * x11 + m2[1] * x21;
/*  500: 986 */       double mx21 = m0[2] * x01 + m1[2] * x11 + m2[2] * x21;
/*  501: 987 */       double mx02 = m0[0] * x02 + m1[0] * x12 + m2[0] * x22;
/*  502: 988 */       double mx12 = m0[1] * x02 + m1[1] * x12 + m2[1] * x22;
/*  503: 989 */       double mx22 = m0[2] * x02 + m1[2] * x12 + m2[2] * x22;
/*  504:     */       
/*  505:     */ 
/*  506: 992 */       o0[0] = (x00 - 0.5D * (x00 * mx00 + x01 * mx10 + x02 * mx20 - m0[0]));
/*  507: 993 */       o0[1] = (x01 - 0.5D * (x00 * mx01 + x01 * mx11 + x02 * mx21 - m0[1]));
/*  508: 994 */       o0[2] = (x02 - 0.5D * (x00 * mx02 + x01 * mx12 + x02 * mx22 - m0[2]));
/*  509: 995 */       o1[0] = (x10 - 0.5D * (x10 * mx00 + x11 * mx10 + x12 * mx20 - m1[0]));
/*  510: 996 */       o1[1] = (x11 - 0.5D * (x10 * mx01 + x11 * mx11 + x12 * mx21 - m1[1]));
/*  511: 997 */       o1[2] = (x12 - 0.5D * (x10 * mx02 + x11 * mx12 + x12 * mx22 - m1[2]));
/*  512: 998 */       o2[0] = (x20 - 0.5D * (x20 * mx00 + x21 * mx10 + x22 * mx20 - m2[0]));
/*  513: 999 */       o2[1] = (x21 - 0.5D * (x20 * mx01 + x21 * mx11 + x22 * mx21 - m2[1]));
/*  514:1000 */       o2[2] = (x22 - 0.5D * (x20 * mx02 + x21 * mx12 + x22 * mx22 - m2[2]));
/*  515:     */       
/*  516:     */ 
/*  517:1003 */       double corr00 = o0[0] - m0[0];
/*  518:1004 */       double corr01 = o0[1] - m0[1];
/*  519:1005 */       double corr02 = o0[2] - m0[2];
/*  520:1006 */       double corr10 = o1[0] - m1[0];
/*  521:1007 */       double corr11 = o1[1] - m1[1];
/*  522:1008 */       double corr12 = o1[2] - m1[2];
/*  523:1009 */       double corr20 = o2[0] - m2[0];
/*  524:1010 */       double corr21 = o2[1] - m2[1];
/*  525:1011 */       double corr22 = o2[2] - m2[2];
/*  526:     */       
/*  527:     */ 
/*  528:1014 */       double fn1 = corr00 * corr00 + corr01 * corr01 + corr02 * corr02 + corr10 * corr10 + corr11 * corr11 + corr12 * corr12 + corr20 * corr20 + corr21 * corr21 + corr22 * corr22;
/*  529:1019 */       if (FastMath.abs(fn1 - fn) <= threshold) {
/*  530:1020 */         return o;
/*  531:     */       }
/*  532:1024 */       x00 = o0[0];
/*  533:1025 */       x01 = o0[1];
/*  534:1026 */       x02 = o0[2];
/*  535:1027 */       x10 = o1[0];
/*  536:1028 */       x11 = o1[1];
/*  537:1029 */       x12 = o1[2];
/*  538:1030 */       x20 = o2[0];
/*  539:1031 */       x21 = o2[1];
/*  540:1032 */       x22 = o2[2];
/*  541:1033 */       fn = fn1;
/*  542:     */     }
/*  543:1038 */     throw new NotARotationMatrixException(LocalizedFormats.UNABLE_TO_ORTHOGONOLIZE_MATRIX, new Object[] { Integer.valueOf(i - 1) });
/*  544:     */   }
/*  545:     */   
/*  546:     */   public static double distance(Rotation r1, Rotation r2)
/*  547:     */   {
/*  548:1068 */     return r1.applyInverseTo(r2).getAngle();
/*  549:     */   }
/*  550:     */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.Rotation
 * JD-Core Version:    0.7.0.1
 */
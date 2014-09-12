/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
/*   2:    */ 
/*   3:    */ public final class RotationOrder
/*   4:    */ {
/*   5: 38 */   public static final RotationOrder XYZ = new RotationOrder("XYZ", Vector3D.PLUS_I, Vector3D.PLUS_J, Vector3D.PLUS_K);
/*   6: 45 */   public static final RotationOrder XZY = new RotationOrder("XZY", Vector3D.PLUS_I, Vector3D.PLUS_K, Vector3D.PLUS_J);
/*   7: 52 */   public static final RotationOrder YXZ = new RotationOrder("YXZ", Vector3D.PLUS_J, Vector3D.PLUS_I, Vector3D.PLUS_K);
/*   8: 59 */   public static final RotationOrder YZX = new RotationOrder("YZX", Vector3D.PLUS_J, Vector3D.PLUS_K, Vector3D.PLUS_I);
/*   9: 66 */   public static final RotationOrder ZXY = new RotationOrder("ZXY", Vector3D.PLUS_K, Vector3D.PLUS_I, Vector3D.PLUS_J);
/*  10: 73 */   public static final RotationOrder ZYX = new RotationOrder("ZYX", Vector3D.PLUS_K, Vector3D.PLUS_J, Vector3D.PLUS_I);
/*  11: 80 */   public static final RotationOrder XYX = new RotationOrder("XYX", Vector3D.PLUS_I, Vector3D.PLUS_J, Vector3D.PLUS_I);
/*  12: 87 */   public static final RotationOrder XZX = new RotationOrder("XZX", Vector3D.PLUS_I, Vector3D.PLUS_K, Vector3D.PLUS_I);
/*  13: 94 */   public static final RotationOrder YXY = new RotationOrder("YXY", Vector3D.PLUS_J, Vector3D.PLUS_I, Vector3D.PLUS_J);
/*  14:101 */   public static final RotationOrder YZY = new RotationOrder("YZY", Vector3D.PLUS_J, Vector3D.PLUS_K, Vector3D.PLUS_J);
/*  15:108 */   public static final RotationOrder ZXZ = new RotationOrder("ZXZ", Vector3D.PLUS_K, Vector3D.PLUS_I, Vector3D.PLUS_K);
/*  16:115 */   public static final RotationOrder ZYZ = new RotationOrder("ZYZ", Vector3D.PLUS_K, Vector3D.PLUS_J, Vector3D.PLUS_K);
/*  17:    */   private final String name;
/*  18:    */   private final Vector3D a1;
/*  19:    */   private final Vector3D a2;
/*  20:    */   private final Vector3D a3;
/*  21:    */   
/*  22:    */   private RotationOrder(String name, Vector3D a1, Vector3D a2, Vector3D a3)
/*  23:    */   {
/*  24:140 */     this.name = name;
/*  25:141 */     this.a1 = a1;
/*  26:142 */     this.a2 = a2;
/*  27:143 */     this.a3 = a3;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public String toString()
/*  31:    */   {
/*  32:151 */     return this.name;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public Vector3D getA1()
/*  36:    */   {
/*  37:158 */     return this.a1;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Vector3D getA2()
/*  41:    */   {
/*  42:165 */     return this.a2;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Vector3D getA3()
/*  46:    */   {
/*  47:172 */     return this.a3;
/*  48:    */   }
/*  49:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.RotationOrder
 * JD-Core Version:    0.7.0.1
 */
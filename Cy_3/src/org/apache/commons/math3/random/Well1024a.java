/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ public class Well1024a
/*   4:    */   extends AbstractWell
/*   5:    */ {
/*   6:    */   private static final long serialVersionUID = 5680173464174485492L;
/*   7:    */   private static final int K = 1024;
/*   8:    */   private static final int M1 = 3;
/*   9:    */   private static final int M2 = 24;
/*  10:    */   private static final int M3 = 10;
/*  11:    */   
/*  12:    */   public Well1024a()
/*  13:    */   {
/*  14: 57 */     super(1024, 3, 24, 10);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public Well1024a(int seed)
/*  18:    */   {
/*  19: 64 */     super(1024, 3, 24, 10, seed);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Well1024a(int[] seed)
/*  23:    */   {
/*  24: 72 */     super(1024, 3, 24, 10, seed);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Well1024a(long seed)
/*  28:    */   {
/*  29: 79 */     super(1024, 3, 24, 10, seed);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected int next(int bits)
/*  33:    */   {
/*  34: 86 */     int indexRm1 = this.iRm1[this.index];
/*  35:    */     
/*  36: 88 */     int v0 = this.v[this.index];
/*  37: 89 */     int vM1 = this.v[this.i1[this.index]];
/*  38: 90 */     int vM2 = this.v[this.i2[this.index]];
/*  39: 91 */     int vM3 = this.v[this.i3[this.index]];
/*  40:    */     
/*  41: 93 */     int z0 = this.v[indexRm1];
/*  42: 94 */     int z1 = v0 ^ vM1 ^ vM1 >>> 8;
/*  43: 95 */     int z2 = vM2 ^ vM2 << 19 ^ vM3 ^ vM3 << 14;
/*  44: 96 */     int z3 = z1 ^ z2;
/*  45: 97 */     int z4 = z0 ^ z0 << 11 ^ z1 ^ z1 << 7 ^ z2 ^ z2 << 13;
/*  46:    */     
/*  47: 99 */     this.v[this.index] = z3;
/*  48:100 */     this.v[indexRm1] = z4;
/*  49:101 */     this.index = indexRm1;
/*  50:    */     
/*  51:103 */     return z4 >>> 32 - bits;
/*  52:    */   }
/*  53:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.Well1024a
 * JD-Core Version:    0.7.0.1
 */
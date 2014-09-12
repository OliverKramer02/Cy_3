/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ public class Well19937a
/*   4:    */   extends AbstractWell
/*   5:    */ {
/*   6:    */   private static final long serialVersionUID = -7462102162223815419L;
/*   7:    */   private static final int K = 19937;
/*   8:    */   private static final int M1 = 70;
/*   9:    */   private static final int M2 = 179;
/*  10:    */   private static final int M3 = 449;
/*  11:    */   
/*  12:    */   public Well19937a()
/*  13:    */   {
/*  14: 57 */     super(19937, 70, 179, 449);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public Well19937a(int seed)
/*  18:    */   {
/*  19: 64 */     super(19937, 70, 179, 449, seed);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Well19937a(int[] seed)
/*  23:    */   {
/*  24: 72 */     super(19937, 70, 179, 449, seed);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Well19937a(long seed)
/*  28:    */   {
/*  29: 79 */     super(19937, 70, 179, 449, seed);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected int next(int bits)
/*  33:    */   {
/*  34: 86 */     int indexRm1 = this.iRm1[this.index];
/*  35: 87 */     int indexRm2 = this.iRm2[this.index];
/*  36:    */     
/*  37: 89 */     int v0 = this.v[this.index];
/*  38: 90 */     int vM1 = this.v[this.i1[this.index]];
/*  39: 91 */     int vM2 = this.v[this.i2[this.index]];
/*  40: 92 */     int vM3 = this.v[this.i3[this.index]];
/*  41:    */     
/*  42: 94 */     int z0 = 0x80000000 & this.v[indexRm1] ^ 0x7FFFFFFF & this.v[indexRm2];
/*  43: 95 */     int z1 = v0 ^ v0 << 25 ^ vM1 ^ vM1 >>> 27;
/*  44: 96 */     int z2 = vM2 >>> 9 ^ vM3 ^ vM3 >>> 1;
/*  45: 97 */     int z3 = z1 ^ z2;
/*  46: 98 */     int z4 = z0 ^ z1 ^ z1 << 9 ^ z2 ^ z2 << 21 ^ z3 ^ z3 >>> 21;
/*  47:    */     
/*  48:100 */     this.v[this.index] = z3;
/*  49:101 */     this.v[indexRm1] = z4;
/*  50:102 */     this.v[indexRm2] &= 0x80000000;
/*  51:103 */     this.index = indexRm1;
/*  52:    */     
/*  53:105 */     return z4 >>> 32 - bits;
/*  54:    */   }
/*  55:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.Well19937a
 * JD-Core Version:    0.7.0.1
 */
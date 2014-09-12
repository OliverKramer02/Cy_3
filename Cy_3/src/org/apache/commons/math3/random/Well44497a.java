/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ public class Well44497a
/*   4:    */   extends AbstractWell
/*   5:    */ {
/*   6:    */   private static final long serialVersionUID = -3859207588353972099L;
/*   7:    */   private static final int K = 44497;
/*   8:    */   private static final int M1 = 23;
/*   9:    */   private static final int M2 = 481;
/*  10:    */   private static final int M3 = 229;
/*  11:    */   
/*  12:    */   public Well44497a()
/*  13:    */   {
/*  14: 57 */     super(44497, 23, 481, 229);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public Well44497a(int seed)
/*  18:    */   {
/*  19: 64 */     super(44497, 23, 481, 229, seed);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Well44497a(int[] seed)
/*  23:    */   {
/*  24: 72 */     super(44497, 23, 481, 229, seed);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Well44497a(long seed)
/*  28:    */   {
/*  29: 79 */     super(44497, 23, 481, 229, seed);
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
/*  42:    */ 
/*  43: 95 */     int z0 = 0xFFFF8000 & this.v[indexRm1] ^ 0x7FFF & this.v[indexRm2];
/*  44: 96 */     int z1 = v0 ^ v0 << 24 ^ vM1 ^ vM1 >>> 30;
/*  45: 97 */     int z2 = vM2 ^ vM2 << 10 ^ vM3 << 26;
/*  46: 98 */     int z3 = z1 ^ z2;
/*  47: 99 */     int z2Prime = (z2 << 9 ^ z2 >>> 23) & 0xFBFFFFFF;
/*  48:100 */     int z2Second = (z2 & 0x20000) != 0 ? z2Prime ^ 0xB729FCEC : z2Prime;
/*  49:101 */     int z4 = z0 ^ z1 ^ z1 >>> 20 ^ z2Second ^ z3;
/*  50:    */     
/*  51:103 */     this.v[this.index] = z3;
/*  52:104 */     this.v[indexRm1] = z4;
/*  53:105 */     this.v[indexRm2] &= 0xFFFF8000;
/*  54:106 */     this.index = indexRm1;
/*  55:    */     
/*  56:108 */     return z4 >>> 32 - bits;
/*  57:    */   }
/*  58:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.Well44497a
 * JD-Core Version:    0.7.0.1
 */
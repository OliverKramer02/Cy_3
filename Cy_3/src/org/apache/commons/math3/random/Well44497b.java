/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ public class Well44497b
/*   4:    */   extends AbstractWell
/*   5:    */ {
/*   6:    */   private static final long serialVersionUID = 4032007538246675492L;
/*   7:    */   private static final int K = 44497;
/*   8:    */   private static final int M1 = 23;
/*   9:    */   private static final int M2 = 481;
/*  10:    */   private static final int M3 = 229;
/*  11:    */   
/*  12:    */   public Well44497b()
/*  13:    */   {
/*  14: 57 */     super(44497, 23, 481, 229);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public Well44497b(int seed)
/*  18:    */   {
/*  19: 64 */     super(44497, 23, 481, 229, seed);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Well44497b(int[] seed)
/*  23:    */   {
/*  24: 72 */     super(44497, 23, 481, 229, seed);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Well44497b(long seed)
/*  28:    */   {
/*  29: 79 */     super(44497, 23, 481, 229, seed);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected int next(int bits)
/*  33:    */   {
/*  34: 88 */     int indexRm1 = this.iRm1[this.index];
/*  35: 89 */     int indexRm2 = this.iRm2[this.index];
/*  36:    */     
/*  37: 91 */     int v0 = this.v[this.index];
/*  38: 92 */     int vM1 = this.v[this.i1[this.index]];
/*  39: 93 */     int vM2 = this.v[this.i2[this.index]];
/*  40: 94 */     int vM3 = this.v[this.i3[this.index]];
/*  41:    */     
/*  42:    */ 
/*  43: 97 */     int z0 = 0xFFFF8000 & this.v[indexRm1] ^ 0x7FFF & this.v[indexRm2];
/*  44: 98 */     int z1 = v0 ^ v0 << 24 ^ vM1 ^ vM1 >>> 30;
/*  45: 99 */     int z2 = vM2 ^ vM2 << 10 ^ vM3 << 26;
/*  46:100 */     int z3 = z1 ^ z2;
/*  47:101 */     int z2Prime = (z2 << 9 ^ z2 >>> 23) & 0xFBFFFFFF;
/*  48:102 */     int z2Second = (z2 & 0x20000) != 0 ? z2Prime ^ 0xB729FCEC : z2Prime;
/*  49:103 */     int z4 = z0 ^ z1 ^ z1 >>> 20 ^ z2Second ^ z3;
/*  50:    */     
/*  51:105 */     this.v[this.index] = z3;
/*  52:106 */     this.v[indexRm1] = z4;
/*  53:107 */     this.v[indexRm2] &= 0xFFFF8000;
/*  54:108 */     this.index = indexRm1;
/*  55:    */     
/*  56:    */ 
/*  57:    */ 
/*  58:112 */     z4 ^= z4 << 7 & 0x93DD1400;
/*  59:113 */     z4 ^= z4 << 15 & 0xFA118000;
/*  60:    */     
/*  61:115 */     return z4 >>> 32 - bits;
/*  62:    */   }
/*  63:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.Well44497b
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ 
/*   5:    */ public abstract class AbstractWell
/*   6:    */   extends BitsStreamGenerator
/*   7:    */   implements Serializable
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = -817701723016583596L;
/*  10:    */   protected int index;
/*  11:    */   protected final int[] v;
/*  12:    */   protected final int[] iRm1;
/*  13:    */   protected final int[] iRm2;
/*  14:    */   protected final int[] i1;
/*  15:    */   protected final int[] i2;
/*  16:    */   protected final int[] i3;
/*  17:    */   
/*  18:    */   protected AbstractWell(int k, int m1, int m2, int m3)
/*  19:    */   {
/*  20: 72 */     this(k, m1, m2, m3, null);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected AbstractWell(int k, int m1, int m2, int m3, int seed)
/*  24:    */   {
/*  25: 83 */     this(k, m1, m2, m3, new int[] { seed });
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected AbstractWell(int k, int m1, int m2, int m3, int[] seed)
/*  29:    */   {
/*  30: 99 */     int w = 32;
/*  31:100 */     int r = (k + 32 - 1) / 32;
/*  32:101 */     this.v = new int[r];
/*  33:102 */     this.index = 0;
/*  34:    */     
/*  35:    */ 
/*  36:    */ 
/*  37:106 */     this.iRm1 = new int[r];
/*  38:107 */     this.iRm2 = new int[r];
/*  39:108 */     this.i1 = new int[r];
/*  40:109 */     this.i2 = new int[r];
/*  41:110 */     this.i3 = new int[r];
/*  42:111 */     for (int j = 0; j < r; j++)
/*  43:    */     {
/*  44:112 */       this.iRm1[j] = ((j + r - 1) % r);
/*  45:113 */       this.iRm2[j] = ((j + r - 2) % r);
/*  46:114 */       this.i1[j] = ((j + m1) % r);
/*  47:115 */       this.i2[j] = ((j + m2) % r);
/*  48:116 */       this.i3[j] = ((j + m3) % r);
/*  49:    */     }
/*  50:120 */     setSeed(seed);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected AbstractWell(int k, int m1, int m2, int m3, long seed)
/*  54:    */   {
/*  55:132 */     this(k, m1, m2, m3, new int[] { (int)(seed >>> 32), (int)(seed & 0xFFFFFFFF) });
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setSeed(int seed)
/*  59:    */   {
/*  60:142 */     setSeed(new int[] { seed });
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setSeed(int[] seed)
/*  64:    */   {
/*  65:154 */     if (seed == null)
/*  66:    */     {
/*  67:155 */       setSeed(System.currentTimeMillis() + System.identityHashCode(this));
/*  68:156 */       return;
/*  69:    */     }
/*  70:159 */     System.arraycopy(seed, 0, this.v, 0, Math.min(seed.length, this.v.length));
/*  71:161 */     if (seed.length < this.v.length) {
/*  72:162 */       for (int i = seed.length; i < this.v.length; i++)
/*  73:    */       {
/*  74:163 */         long l = this.v[(i - seed.length)];
/*  75:164 */         this.v[i] = ((int)(1812433253L * (l ^ l >> 30) + i & 0xFFFFFFFF));
/*  76:    */       }
/*  77:    */     }
/*  78:168 */     this.index = 0;
/*  79:169 */     clear();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setSeed(long seed)
/*  83:    */   {
/*  84:179 */     setSeed(new int[] { (int)(seed >>> 32), (int)(seed & 0xFFFFFFFF) });
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected abstract int next(int paramInt);
/*  88:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.AbstractWell
 * JD-Core Version:    0.7.0.1
 */
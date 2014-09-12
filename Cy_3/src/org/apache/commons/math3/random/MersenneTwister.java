/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public class MersenneTwister
/*   7:    */   extends BitsStreamGenerator
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 8661194735290153518L;
/*  11:    */   private static final int N = 624;
/*  12:    */   private static final int M = 397;
/*  13: 94 */   private static final int[] MAG01 = { 0, -1727483681 };
/*  14:    */   private int[] mt;
/*  15:    */   private int mti;
/*  16:    */   
/*  17:    */   public MersenneTwister()
/*  18:    */   {
/*  19:107 */     this.mt = new int[624];
/*  20:108 */     setSeed(System.currentTimeMillis() + System.identityHashCode(this));
/*  21:    */   }
/*  22:    */   
/*  23:    */   public MersenneTwister(int seed)
/*  24:    */   {
/*  25:115 */     this.mt = new int[624];
/*  26:116 */     setSeed(seed);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public MersenneTwister(int[] seed)
/*  30:    */   {
/*  31:124 */     this.mt = new int[624];
/*  32:125 */     setSeed(seed);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public MersenneTwister(long seed)
/*  36:    */   {
/*  37:132 */     this.mt = new int[624];
/*  38:133 */     setSeed(seed);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setSeed(int seed)
/*  42:    */   {
/*  43:144 */     long longMT = seed;
/*  44:    */     
/*  45:146 */     this.mt[0] = ((int)longMT);
/*  46:147 */     for (this.mti = 1; this.mti < 624; this.mti += 1)
/*  47:    */     {
/*  48:150 */       longMT = 1812433253L * (longMT ^ longMT >> 30) + this.mti & 0xFFFFFFFF;
/*  49:151 */       this.mt[this.mti] = ((int)longMT);
/*  50:    */     }
/*  51:154 */     clear();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setSeed(int[] seed)
/*  55:    */   {
/*  56:167 */     if (seed == null)
/*  57:    */     {
/*  58:168 */       setSeed(System.currentTimeMillis() + System.identityHashCode(this));
/*  59:169 */       return;
/*  60:    */     }
/*  61:172 */     setSeed(19650218);
/*  62:173 */     int i = 1;
/*  63:174 */     int j = 0;
/*  64:176 */     for (int k = FastMath.max(624, seed.length); k != 0; k--)
/*  65:    */     {
/*  66:177 */       long l0 = this.mt[i] & 0x7FFFFFFF | (this.mt[i] < 0 ? 2147483648L : 0L);
/*  67:178 */       long l1 = this.mt[(i - 1)] & 0x7FFFFFFF | (this.mt[(i - 1)] < 0 ? 2147483648L : 0L);
/*  68:179 */       long l = (l0 ^ (l1 ^ l1 >> 30) * 1664525L) + seed[j] + j;
/*  69:180 */       this.mt[i] = ((int)(l & 0xFFFFFFFF));
/*  70:181 */       i++;j++;
/*  71:182 */       if (i >= 624)
/*  72:    */       {
/*  73:183 */         this.mt[0] = this.mt[623];
/*  74:184 */         i = 1;
/*  75:    */       }
/*  76:186 */       if (j >= seed.length) {
/*  77:187 */         j = 0;
/*  78:    */       }
/*  79:    */     }
/*  80:191 */     for (int k = 623; k != 0; k--)
/*  81:    */     {
/*  82:192 */       long l0 = this.mt[i] & 0x7FFFFFFF | (this.mt[i] < 0 ? 2147483648L : 0L);
/*  83:193 */       long l1 = this.mt[(i - 1)] & 0x7FFFFFFF | (this.mt[(i - 1)] < 0 ? 2147483648L : 0L);
/*  84:194 */       long l = (l0 ^ (l1 ^ l1 >> 30) * 1566083941L) - i;
/*  85:195 */       this.mt[i] = ((int)(l & 0xFFFFFFFF));
/*  86:196 */       i++;
/*  87:197 */       if (i >= 624)
/*  88:    */       {
/*  89:198 */         this.mt[0] = this.mt[623];
/*  90:199 */         i = 1;
/*  91:    */       }
/*  92:    */     }
/*  93:203 */     this.mt[0] = -2147483648;
/*  94:    */     
/*  95:205 */     clear();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setSeed(long seed)
/*  99:    */   {
/* 100:216 */     setSeed(new int[] { (int)(seed >>> 32), (int)(seed & 0xFFFFFFFF) });
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected int next(int bits)
/* 104:    */   {
/* 105:233 */     if (this.mti >= 624)
/* 106:    */     {
/* 107:234 */       int mtNext = this.mt[0];
/* 108:235 */       for (int k = 0; k < 227; k++)
/* 109:    */       {
/* 110:236 */         int mtCurr = mtNext;
/* 111:237 */         mtNext = this.mt[(k + 1)];
/* 112:238 */         int y = mtCurr & 0x80000000 | mtNext & 0x7FFFFFFF;
/* 113:239 */         this.mt[k] = (this.mt[(k + 397)] ^ y >>> 1 ^ MAG01[(y & 0x1)]);
/* 114:    */       }
/* 115:241 */       for (int k = 227; k < 623; k++)
/* 116:    */       {
/* 117:242 */         int mtCurr = mtNext;
/* 118:243 */         mtNext = this.mt[(k + 1)];
/* 119:244 */         int y = mtCurr & 0x80000000 | mtNext & 0x7FFFFFFF;
/* 120:245 */         this.mt[k] = (this.mt[(k + -227)] ^ y >>> 1 ^ MAG01[(y & 0x1)]);
/* 121:    */       }
/* 122:247 */       int y = mtNext & 0x80000000 | this.mt[0] & 0x7FFFFFFF;
/* 123:248 */       this.mt[623] = (this.mt[396] ^ y >>> 1 ^ MAG01[(y & 0x1)]);
/* 124:    */       
/* 125:250 */       this.mti = 0;
/* 126:    */     }
/* 127:253 */     int y = this.mt[(this.mti++)];
/* 128:    */     
/* 129:    */ 
/* 130:256 */     y ^= y >>> 11;
/* 131:257 */     y ^= y << 7 & 0x9D2C5680;
/* 132:258 */     y ^= y << 15 & 0xEFC60000;
/* 133:259 */     y ^= y >>> 18;
/* 134:    */     
/* 135:261 */     return y >>> 32 - bits;
/* 136:    */   }
/* 137:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.MersenneTwister
 * JD-Core Version:    0.7.0.1
 */
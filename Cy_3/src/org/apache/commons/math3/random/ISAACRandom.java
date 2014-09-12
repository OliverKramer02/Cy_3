/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ 
/*   5:    */ public class ISAACRandom
/*   6:    */   extends BitsStreamGenerator
/*   7:    */   implements Serializable
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 7288197941165002400L;
/*  10:    */   private static final int SIZE_L = 8;
/*  11:    */   private static final int SIZE = 256;
/*  12:    */   private static final int H_SIZE = 128;
/*  13:    */   private static final int MASK = 1020;
/*  14:    */   private static final int GLD_RATIO = -1640531527;
/*  15: 56 */   private final int[] rsl = new int[256];
/*  16: 58 */   private final int[] mem = new int[256];
/*  17:    */   private int count;
/*  18:    */   private int isaacA;
/*  19:    */   private int isaacB;
/*  20:    */   private int isaacC;
/*  21: 68 */   private final int[] arr = new int[8];
/*  22:    */   private int isaacX;
/*  23:    */   private int isaacI;
/*  24:    */   private int isaacJ;
/*  25:    */   
/*  26:    */   public ISAACRandom()
/*  27:    */   {
/*  28: 84 */     setSeed(System.currentTimeMillis() + System.identityHashCode(this));
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ISAACRandom(long seed)
/*  32:    */   {
/*  33: 93 */     setSeed(seed);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public ISAACRandom(int[] seed)
/*  37:    */   {
/*  38:103 */     setSeed(seed);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setSeed(int seed)
/*  42:    */   {
/*  43:109 */     setSeed(new int[] { seed });
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setSeed(long seed)
/*  47:    */   {
/*  48:115 */     setSeed(new int[] { (int)(seed >>> 32), (int)(seed & 0xFFFFFFFF) });
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setSeed(int[] seed)
/*  52:    */   {
/*  53:121 */     if (seed == null)
/*  54:    */     {
/*  55:122 */       setSeed(System.currentTimeMillis() + System.identityHashCode(this));
/*  56:123 */       return;
/*  57:    */     }
/*  58:125 */     int seedLen = seed.length;
/*  59:126 */     int rslLen = this.rsl.length;
/*  60:127 */     System.arraycopy(seed, 0, this.rsl, 0, Math.min(seedLen, rslLen));
/*  61:128 */     if (seedLen < rslLen) {
/*  62:129 */       for (int j = seedLen; j < rslLen; j++)
/*  63:    */       {
/*  64:130 */         long k = this.rsl[(j - seedLen)];
/*  65:131 */         this.rsl[j] = ((int)(1812433253L * (k ^ k >> 30) + j & 0xFFFFFFFF));
/*  66:    */       }
/*  67:    */     }
/*  68:134 */     initState();
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected int next(int bits)
/*  72:    */   {
/*  73:140 */     if (this.count < 0)
/*  74:    */     {
/*  75:141 */       isaac();
/*  76:142 */       this.count = 255;
/*  77:    */     }
/*  78:144 */     return this.rsl[(this.count--)] >>> 32 - bits;
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void isaac()
/*  82:    */   {
/*  83:149 */     this.isaacI = 0;
/*  84:150 */     this.isaacJ = 128;
/*  85:151 */     this.isaacB += ++this.isaacC;
/*  86:152 */     while (this.isaacI < 128) {
/*  87:153 */       isaac2();
/*  88:    */     }
/*  89:155 */     this.isaacJ = 0;
/*  90:156 */     while (this.isaacJ < 128) {
/*  91:157 */       isaac2();
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   private void isaac2()
/*  96:    */   {
/*  97:163 */     this.isaacX = this.mem[this.isaacI];
/*  98:164 */     this.isaacA ^= this.isaacA << 13;
/*  99:165 */     this.isaacA += this.mem[(this.isaacJ++)];
/* 100:166 */     isaac3();
/* 101:167 */     this.isaacX = this.mem[this.isaacI];
/* 102:168 */     this.isaacA ^= this.isaacA >>> 6;
/* 103:169 */     this.isaacA += this.mem[(this.isaacJ++)];
/* 104:170 */     isaac3();
/* 105:171 */     this.isaacX = this.mem[this.isaacI];
/* 106:172 */     this.isaacA ^= this.isaacA << 2;
/* 107:173 */     this.isaacA += this.mem[(this.isaacJ++)];
/* 108:174 */     isaac3();
/* 109:175 */     this.isaacX = this.mem[this.isaacI];
/* 110:176 */     this.isaacA ^= this.isaacA >>> 16;
/* 111:177 */     this.isaacA += this.mem[(this.isaacJ++)];
/* 112:178 */     isaac3();
/* 113:    */   }
/* 114:    */   
/* 115:    */   private void isaac3()
/* 116:    */   {
/* 117:183 */     this.mem[this.isaacI] = (this.mem[((this.isaacX & 0x3FC) >> 2)] + this.isaacA + this.isaacB);
/* 118:184 */     this.isaacB = (this.mem[((this.mem[this.isaacI] >> 8 & 0x3FC) >> 2)] + this.isaacX);
/* 119:185 */     this.rsl[(this.isaacI++)] = this.isaacB;
/* 120:    */   }
/* 121:    */   
/* 122:    */   private void initState()
/* 123:    */   {
/* 124:190 */     this.isaacA = 0;
/* 125:191 */     this.isaacB = 0;
/* 126:192 */     this.isaacC = 0;
/* 127:193 */     for (int j = 0; j < this.arr.length; j++) {
/* 128:194 */       this.arr[j] = -1640531527;
/* 129:    */     }
/* 130:196 */     for (int j = 0; j < 4; j++) {
/* 131:197 */       shuffle();
/* 132:    */     }
/* 133:200 */     for (int j = 0; j < 256; j += 8)
/* 134:    */     {
/* 135:201 */       this.arr[0] += this.rsl[j];
/* 136:202 */       this.arr[1] += this.rsl[(j + 1)];
/* 137:203 */       this.arr[2] += this.rsl[(j + 2)];
/* 138:204 */       this.arr[3] += this.rsl[(j + 3)];
/* 139:205 */       this.arr[4] += this.rsl[(j + 4)];
/* 140:206 */       this.arr[5] += this.rsl[(j + 5)];
/* 141:207 */       this.arr[6] += this.rsl[(j + 6)];
/* 142:208 */       this.arr[7] += this.rsl[(j + 7)];
/* 143:209 */       shuffle();
/* 144:210 */       setState(j);
/* 145:    */     }
/* 146:213 */     for (int j = 0; j < 256; j += 8)
/* 147:    */     {
/* 148:214 */       this.arr[0] += this.mem[j];
/* 149:215 */       this.arr[1] += this.mem[(j + 1)];
/* 150:216 */       this.arr[2] += this.mem[(j + 2)];
/* 151:217 */       this.arr[3] += this.mem[(j + 3)];
/* 152:218 */       this.arr[4] += this.mem[(j + 4)];
/* 153:219 */       this.arr[5] += this.mem[(j + 5)];
/* 154:220 */       this.arr[6] += this.mem[(j + 6)];
/* 155:221 */       this.arr[7] += this.mem[(j + 7)];
/* 156:222 */       shuffle();
/* 157:223 */       setState(j);
/* 158:    */     }
/* 159:225 */     isaac();
/* 160:226 */     this.count = 255;
/* 161:227 */     clear();
/* 162:    */   }
/* 163:    */   
/* 164:    */   private void shuffle()
/* 165:    */   {
/* 166:232 */     this.arr[0] ^= this.arr[1] << 11;
/* 167:233 */     this.arr[3] += this.arr[0];
/* 168:234 */     this.arr[1] += this.arr[2];
/* 169:235 */     this.arr[1] ^= this.arr[2] >>> 2;
/* 170:236 */     this.arr[4] += this.arr[1];
/* 171:237 */     this.arr[2] += this.arr[3];
/* 172:238 */     this.arr[2] ^= this.arr[3] << 8;
/* 173:239 */     this.arr[5] += this.arr[2];
/* 174:240 */     this.arr[3] += this.arr[4];
/* 175:241 */     this.arr[3] ^= this.arr[4] >>> 16;
/* 176:242 */     this.arr[6] += this.arr[3];
/* 177:243 */     this.arr[4] += this.arr[5];
/* 178:244 */     this.arr[4] ^= this.arr[5] << 10;
/* 179:245 */     this.arr[7] += this.arr[4];
/* 180:246 */     this.arr[5] += this.arr[6];
/* 181:247 */     this.arr[5] ^= this.arr[6] >>> 4;
/* 182:248 */     this.arr[0] += this.arr[5];
/* 183:249 */     this.arr[6] += this.arr[7];
/* 184:250 */     this.arr[6] ^= this.arr[7] << 8;
/* 185:251 */     this.arr[1] += this.arr[6];
/* 186:252 */     this.arr[7] += this.arr[0];
/* 187:253 */     this.arr[7] ^= this.arr[0] >>> 9;
/* 188:254 */     this.arr[2] += this.arr[7];
/* 189:255 */     this.arr[0] += this.arr[1];
/* 190:    */   }
/* 191:    */   
/* 192:    */   private void setState(int start)
/* 193:    */   {
/* 194:263 */     this.mem[start] = this.arr[0];
/* 195:264 */     this.mem[(start + 1)] = this.arr[1];
/* 196:265 */     this.mem[(start + 2)] = this.arr[2];
/* 197:266 */     this.mem[(start + 3)] = this.arr[3];
/* 198:267 */     this.mem[(start + 4)] = this.arr[4];
/* 199:268 */     this.mem[(start + 5)] = this.arr[5];
/* 200:269 */     this.mem[(start + 6)] = this.arr[6];
/* 201:270 */     this.mem[(start + 7)] = this.arr[7];
/* 202:    */   }
/* 203:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.ISAACRandom
 * JD-Core Version:    0.7.0.1
 */
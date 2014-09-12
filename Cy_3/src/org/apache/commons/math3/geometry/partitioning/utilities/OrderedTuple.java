/*   1:    */ package org.apache.commons.math3.geometry.partitioning.utilities;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public class OrderedTuple
/*   7:    */   implements Comparable<OrderedTuple>
/*   8:    */ {
/*   9:    */   private static final long SIGN_MASK = -9223372036854775808L;
/*  10:    */   private static final long EXPONENT_MASK = 9218868437227405312L;
/*  11:    */   private static final long MANTISSA_MASK = 4503599627370495L;
/*  12:    */   private static final long IMPLICIT_ONE = 4503599627370496L;
/*  13:    */   private double[] components;
/*  14:    */   private int offset;
/*  15:    */   private int lsb;
/*  16:    */   private long[] encoding;
/*  17:    */   private boolean posInf;
/*  18:    */   private boolean negInf;
/*  19:    */   private boolean nan;
/*  20:    */   
/*  21:    */   public OrderedTuple(double... components)
/*  22:    */   {
/*  23:133 */     this.components = ((double[])components.clone());
/*  24:134 */     int msb = -2147483648;
/*  25:135 */     this.lsb = 2147483647;
/*  26:136 */     this.posInf = false;
/*  27:137 */     this.negInf = false;
/*  28:138 */     this.nan = false;
/*  29:139 */     for (int i = 0; i < components.length; i++) {
/*  30:140 */       if (Double.isInfinite(components[i]))
/*  31:    */       {
/*  32:141 */         if (components[i] < 0.0D) {
/*  33:142 */           this.negInf = true;
/*  34:    */         } else {
/*  35:144 */           this.posInf = true;
/*  36:    */         }
/*  37:    */       }
/*  38:146 */       else if (Double.isNaN(components[i]))
/*  39:    */       {
/*  40:147 */         this.nan = true;
/*  41:    */       }
/*  42:    */       else
/*  43:    */       {
/*  44:149 */         long b = Double.doubleToLongBits(components[i]);
/*  45:150 */         long m = mantissa(b);
/*  46:151 */         if (m != 0L)
/*  47:    */         {
/*  48:152 */           int e = exponent(b);
/*  49:153 */           msb = FastMath.max(msb, e + computeMSB(m));
/*  50:154 */           this.lsb = FastMath.min(this.lsb, e + computeLSB(m));
/*  51:    */         }
/*  52:    */       }
/*  53:    */     }
/*  54:159 */     if ((this.posInf) && (this.negInf))
/*  55:    */     {
/*  56:161 */       this.posInf = false;
/*  57:162 */       this.negInf = false;
/*  58:163 */       this.nan = true;
/*  59:    */     }
/*  60:166 */     if (this.lsb <= msb) {
/*  61:168 */       encode(msb + 16);
/*  62:    */     } else {
/*  63:170 */       this.encoding = new long[] { 0L };
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   private void encode(int minOffset)
/*  68:    */   {
/*  69:184 */     this.offset = (minOffset + 31);
/*  70:185 */     this.offset -= this.offset % 32;
/*  71:187 */     if ((this.encoding != null) && (this.encoding.length == 1) && (this.encoding[0] == 0L)) {
/*  72:189 */       return;
/*  73:    */     }
/*  74:194 */     int neededBits = this.offset + 1 - this.lsb;
/*  75:195 */     int neededLongs = (neededBits + 62) / 63;
/*  76:196 */     this.encoding = new long[this.components.length * neededLongs];
/*  77:    */     
/*  78:    */ 
/*  79:199 */     int eIndex = 0;
/*  80:200 */     int shift = 62;
/*  81:201 */     long word = 0L;
/*  82:202 */     for (int k = this.offset; eIndex < this.encoding.length; k--) {
/*  83:203 */       for (int vIndex = 0; vIndex < this.components.length; vIndex++)
/*  84:    */       {
/*  85:204 */         if (getBit(vIndex, k) != 0) {
/*  86:205 */           word |= 1L << shift;
/*  87:    */         }
/*  88:207 */         if (shift-- == 0)
/*  89:    */         {
/*  90:208 */           this.encoding[(eIndex++)] = word;
/*  91:209 */           word = 0L;
/*  92:210 */           shift = 62;
/*  93:    */         }
/*  94:    */       }
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int compareTo(OrderedTuple ot)
/*  99:    */   {
/* 100:248 */     if (this.components.length == ot.components.length)
/* 101:    */     {
/* 102:249 */       if (this.nan) {
/* 103:250 */         return 1;
/* 104:    */       }
/* 105:251 */       if (ot.nan) {
/* 106:252 */         return -1;
/* 107:    */       }
/* 108:253 */       if ((this.negInf) || (ot.posInf)) {
/* 109:254 */         return -1;
/* 110:    */       }
/* 111:255 */       if ((this.posInf) || (ot.negInf)) {
/* 112:256 */         return 1;
/* 113:    */       }
/* 114:259 */       if (this.offset < ot.offset) {
/* 115:260 */         encode(ot.offset);
/* 116:261 */       } else if (this.offset > ot.offset) {
/* 117:262 */         ot.encode(this.offset);
/* 118:    */       }
/* 119:265 */       int limit = FastMath.min(this.encoding.length, ot.encoding.length);
/* 120:266 */       for (int i = 0; i < limit; i++)
/* 121:    */       {
/* 122:267 */         if (this.encoding[i] < ot.encoding[i]) {
/* 123:268 */           return -1;
/* 124:    */         }
/* 125:269 */         if (this.encoding[i] > ot.encoding[i]) {
/* 126:270 */           return 1;
/* 127:    */         }
/* 128:    */       }
/* 129:274 */       if (this.encoding.length < ot.encoding.length) {
/* 130:275 */         return -1;
/* 131:    */       }
/* 132:276 */       if (this.encoding.length > ot.encoding.length) {
/* 133:277 */         return 1;
/* 134:    */       }
/* 135:279 */       return 0;
/* 136:    */     }
/* 137:285 */     return this.components.length - ot.components.length;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean equals(Object other)
/* 141:    */   {
/* 142:292 */     if (this == other) {
/* 143:293 */       return true;
/* 144:    */     }
/* 145:294 */     if ((other instanceof OrderedTuple)) {
/* 146:295 */       return compareTo((OrderedTuple)other) == 0;
/* 147:    */     }
/* 148:297 */     return false;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public int hashCode()
/* 152:    */   {
/* 153:304 */     return Arrays.hashCode(this.components) ^ Integer.valueOf(this.offset).hashCode() ^ Integer.valueOf(this.lsb).hashCode() ^ Boolean.valueOf(this.posInf).hashCode() ^ Boolean.valueOf(this.negInf).hashCode() ^ Boolean.valueOf(this.nan).hashCode();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public double[] getComponents()
/* 157:    */   {
/* 158:316 */     return (double[])this.components.clone();
/* 159:    */   }
/* 160:    */   
/* 161:    */   private static long sign(long bits)
/* 162:    */   {
/* 163:324 */     return bits & 0x0;
/* 164:    */   }
/* 165:    */   
/* 166:    */   private static int exponent(long bits)
/* 167:    */   {
/* 168:332 */     return (int)((bits & 0x0) >> 52) - 1075;
/* 169:    */   }
/* 170:    */   
/* 171:    */   private static long mantissa(long bits)
/* 172:    */   {
/* 173:340 */     return (bits & 0x0) == 0L ? (bits & 0xFFFFFFFF) << 1 : 0x0 | bits & 0xFFFFFFFF;
/* 174:    */   }
/* 175:    */   
/* 176:    */   private static int computeMSB(long l)
/* 177:    */   {
/* 178:353 */     long ll = l;
/* 179:354 */     long mask = 4294967295L;
/* 180:355 */     int scale = 32;
/* 181:356 */     int msb = 0;
/* 182:358 */     while (scale != 0)
/* 183:    */     {
/* 184:359 */       if ((ll & mask) != ll)
/* 185:    */       {
/* 186:360 */         msb |= scale;
/* 187:361 */         ll >>= scale;
/* 188:    */       }
/* 189:363 */       scale >>= 1;
/* 190:364 */       mask >>= scale;
/* 191:    */     }
/* 192:367 */     return msb;
/* 193:    */   }
/* 194:    */   
/* 195:    */   private static int computeLSB(long l)
/* 196:    */   {
/* 197:379 */     long ll = l;
/* 198:380 */     long mask = -4294967296L;
/* 199:381 */     int scale = 32;
/* 200:382 */     int lsb = 0;
/* 201:384 */     while (scale != 0)
/* 202:    */     {
/* 203:385 */       if ((ll & mask) == ll)
/* 204:    */       {
/* 205:386 */         lsb |= scale;
/* 206:387 */         ll >>= scale;
/* 207:    */       }
/* 208:389 */       scale >>= 1;
/* 209:390 */       mask >>= scale;
/* 210:    */     }
/* 211:393 */     return lsb;
/* 212:    */   }
/* 213:    */   
/* 214:    */   private int getBit(int i, int k)
/* 215:    */   {
/* 216:404 */     long bits = Double.doubleToLongBits(this.components[i]);
/* 217:405 */     int e = exponent(bits);
/* 218:406 */     if ((k < e) || (k > this.offset)) {
/* 219:407 */       return 0;
/* 220:    */     }
/* 221:408 */     if (k == this.offset) {
/* 222:409 */       return sign(bits) == 0L ? 1 : 0;
/* 223:    */     }
/* 224:410 */     if (k > e + 52) {
/* 225:411 */       return sign(bits) == 0L ? 0 : 1;
/* 226:    */     }
/* 227:413 */     long m = sign(bits) == 0L ? mantissa(bits) : -mantissa(bits);
/* 228:414 */     return (int)(m >> k - e & 1L);
/* 229:    */   }
/* 230:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.utilities.OrderedTuple
 * JD-Core Version:    0.7.0.1
 */
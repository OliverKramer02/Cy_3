/*   1:    */ package org.apache.commons.math3.dfp;
/*   2:    */ 
/*   3:    */ public class DfpDec
/*   4:    */   extends Dfp
/*   5:    */ {
/*   6:    */   protected DfpDec(DfpField factory)
/*   7:    */   {
/*   8: 33 */     super(factory);
/*   9:    */   }
/*  10:    */   
/*  11:    */   protected DfpDec(DfpField factory, byte x)
/*  12:    */   {
/*  13: 41 */     super(factory, x);
/*  14:    */   }
/*  15:    */   
/*  16:    */   protected DfpDec(DfpField factory, int x)
/*  17:    */   {
/*  18: 49 */     super(factory, x);
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected DfpDec(DfpField factory, long x)
/*  22:    */   {
/*  23: 57 */     super(factory, x);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected DfpDec(DfpField factory, double x)
/*  27:    */   {
/*  28: 65 */     super(factory, x);
/*  29: 66 */     round(0);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public DfpDec(Dfp d)
/*  33:    */   {
/*  34: 73 */     super(d);
/*  35: 74 */     round(0);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected DfpDec(DfpField factory, String s)
/*  39:    */   {
/*  40: 82 */     super(factory, s);
/*  41: 83 */     round(0);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected DfpDec(DfpField factory, byte sign, byte nans)
/*  45:    */   {
/*  46: 93 */     super(factory, sign, nans);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Dfp newInstance()
/*  50:    */   {
/*  51: 99 */     return new DfpDec(getField());
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Dfp newInstance(byte x)
/*  55:    */   {
/*  56:105 */     return new DfpDec(getField(), x);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Dfp newInstance(int x)
/*  60:    */   {
/*  61:111 */     return new DfpDec(getField(), x);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Dfp newInstance(long x)
/*  65:    */   {
/*  66:117 */     return new DfpDec(getField(), x);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Dfp newInstance(double x)
/*  70:    */   {
/*  71:123 */     return new DfpDec(getField(), x);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Dfp newInstance(Dfp d)
/*  75:    */   {
/*  76:131 */     if (getField().getRadixDigits() != d.getField().getRadixDigits())
/*  77:    */     {
/*  78:132 */       getField().setIEEEFlagsBits(1);
/*  79:133 */       Dfp result = newInstance(getZero());
/*  80:134 */       result.nans = 3;
/*  81:135 */       return dotrap(1, "newInstance", d, result);
/*  82:    */     }
/*  83:138 */     return new DfpDec(d);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Dfp newInstance(String s)
/*  87:    */   {
/*  88:145 */     return new DfpDec(getField(), s);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Dfp newInstance(byte sign, byte nans)
/*  92:    */   {
/*  93:151 */     return new DfpDec(getField(), sign, nans);
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected int getDecimalDigits()
/*  97:    */   {
/*  98:160 */     return getRadixDigits() * 4 - 3;
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected int round(int in)
/* 102:    */   {
/* 103:167 */     int msb = this.mant[(this.mant.length - 1)];
/* 104:168 */     if (msb == 0) {
/* 105:170 */       return 0;
/* 106:    */     }
/* 107:173 */     int cmaxdigits = this.mant.length * 4;
/* 108:174 */     int lsbthreshold = 1000;
/* 109:175 */     while (lsbthreshold > msb)
/* 110:    */     {
/* 111:176 */       lsbthreshold /= 10;
/* 112:177 */       cmaxdigits--;
/* 113:    */     }
/* 114:181 */     int digits = getDecimalDigits();
/* 115:182 */     int lsbshift = cmaxdigits - digits;
/* 116:183 */     int lsd = lsbshift / 4;
/* 117:    */     
/* 118:185 */     lsbthreshold = 1;
/* 119:186 */     for (int i = 0; i < lsbshift % 4; i++) {
/* 120:187 */       lsbthreshold *= 10;
/* 121:    */     }
/* 122:190 */     int lsb = this.mant[lsd];
/* 123:192 */     if ((lsbthreshold <= 1) && (digits == 4 * this.mant.length - 3)) {
/* 124:193 */       return super.round(in);
/* 125:    */     }
/* 126:196 */     int discarded = in;
/* 127:    */     int n;
/* 128:198 */     if (lsbthreshold == 1)
/* 129:    */     {
/* 130:200 */       n = this.mant[(lsd - 1)] / 1000 % 10;
/* 131:201 */       this.mant[(lsd - 1)] %= 1000;
/* 132:202 */       discarded |= this.mant[(lsd - 1)];
/* 133:    */     }
/* 134:    */     else
/* 135:    */     {
/* 136:204 */       n = lsb * 10 / lsbthreshold % 10;
/* 137:205 */       discarded |= lsb % (lsbthreshold / 10);
/* 138:    */     }
/* 139:208 */     for (int i = 0; i < lsd; i++)
/* 140:    */     {
/* 141:209 */       discarded |= this.mant[i];
/* 142:210 */       this.mant[i] = 0;
/* 143:    */     }
/* 144:213 */     this.mant[lsd] = (lsb / lsbthreshold * lsbthreshold);
/* 145:    */     boolean inc;
/* 146:216 */     switch (getField().getRoundingMode().ordinal())
/* 147:    */     {
/* 148:    */     case 1: 
/* 149:218 */       inc = false;
/* 150:219 */       break;
/* 151:    */     case 2: 
/* 152:222 */       inc = (n != 0) || (discarded != 0);
/* 153:223 */       break;
/* 154:    */     case 3: 
/* 155:226 */       inc = n >= 5;
/* 156:227 */       break;
/* 157:    */     case 4: 
/* 158:230 */       inc = n > 5;
/* 159:231 */       break;
/* 160:    */     case 5: 
/* 161:234 */       inc = (n > 5) || ((n == 5) && (discarded != 0)) || ((n == 5) && (discarded == 0) && ((lsb / lsbthreshold & 0x1) == 1));
/* 162:    */       
/* 163:    */ 
/* 164:237 */       break;
/* 165:    */     case 6: 
/* 166:240 */       inc = (n > 5) || ((n == 5) && (discarded != 0)) || ((n == 5) && (discarded == 0) && ((lsb / lsbthreshold & 0x1) == 0));
/* 167:    */       
/* 168:    */ 
/* 169:243 */       break;
/* 170:    */     case 7: 
/* 171:246 */       inc = (this.sign == 1) && ((n != 0) || (discarded != 0));
/* 172:247 */       break;
/* 173:    */     case 8: 
/* 174:    */     default: 
/* 175:251 */       inc = (this.sign == -1) && ((n != 0) || (discarded != 0));
/* 176:    */     }
/* 177:255 */     if (inc)
/* 178:    */     {
/* 179:257 */       int rh = lsbthreshold;
/* 180:258 */       for (int i = lsd; i < this.mant.length; i++)
/* 181:    */       {
/* 182:259 */         int r = this.mant[i] + rh;
/* 183:260 */         rh = r / 10000;
/* 184:261 */         this.mant[i] = (r % 10000);
/* 185:    */       }
/* 186:264 */       if (rh != 0)
/* 187:    */       {
/* 188:265 */         shiftRight();
/* 189:266 */         this.mant[(this.mant.length - 1)] = rh;
/* 190:    */       }
/* 191:    */     }
/* 192:271 */     if (this.exp < -32767)
/* 193:    */     {
/* 194:273 */       getField().setIEEEFlagsBits(8);
/* 195:274 */       return 8;
/* 196:    */     }
/* 197:277 */     if (this.exp > 32768)
/* 198:    */     {
/* 199:279 */       getField().setIEEEFlagsBits(4);
/* 200:280 */       return 4;
/* 201:    */     }
/* 202:283 */     if ((n != 0) || (discarded != 0))
/* 203:    */     {
/* 204:285 */       getField().setIEEEFlagsBits(16);
/* 205:286 */       return 16;
/* 206:    */     }
/* 207:288 */     return 0;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public Dfp nextAfter(Dfp x)
/* 211:    */   {
/* 212:295 */     String trapName = "nextAfter";
/* 213:298 */     if (getField().getRadixDigits() != x.getField().getRadixDigits())
/* 214:    */     {
/* 215:299 */       getField().setIEEEFlagsBits(1);
/* 216:300 */       Dfp result = newInstance(getZero());
/* 217:301 */       result.nans = 3;
/* 218:302 */       return dotrap(1, "nextAfter", x, result);
/* 219:    */     }
/* 220:305 */     boolean up = false;
/* 221:310 */     if (lessThan(x)) {
/* 222:311 */       up = true;
/* 223:    */     }
/* 224:314 */     if (equals(x)) {
/* 225:315 */       return newInstance(x);
/* 226:    */     }
/* 227:318 */     if (lessThan(getZero())) {
/* 228:319 */       up = !up;
/* 229:    */     }
/* 230:    */     Dfp result;
/* 231:    */     
/* 232:322 */     if (up)
/* 233:    */     {
/* 234:323 */       Dfp inc = power10(log10() - getDecimalDigits() + 1);
/* 235:324 */       inc = copysign(inc, this);
/* 236:326 */       if (equals(getZero())) {
/* 237:327 */         inc = power10K(-32767 - this.mant.length - 1);
/* 238:    */       }
/* 239:    */      
/* 240:330 */       if (inc.equals(getZero())) {
/* 241:331 */         result = copysign(newInstance(getZero()), this);
/* 242:    */       } else {
/* 243:333 */         result = add(inc);
/* 244:    */       }
/* 245:    */     }
/* 246:    */     else
/* 247:    */     {
/* 248:336 */       Dfp inc = power10(log10());
/* 249:337 */       inc = copysign(inc, this);
/* 250:339 */       if (equals(inc)) {
/* 251:340 */         inc = inc.divide(power10(getDecimalDigits()));
/* 252:    */       } else {
/* 253:342 */         inc = inc.divide(power10(getDecimalDigits() - 1));
/* 254:    */       }
/* 255:345 */       if (equals(getZero())) {
/* 256:346 */         inc = power10K(-32767 - this.mant.length - 1);
/* 257:    */       }
/* 258:    */     
/* 259:349 */       if (inc.equals(getZero())) {
/* 260:350 */         result = copysign(newInstance(getZero()), this);
/* 261:    */       } else {
/* 262:352 */         result = subtract(inc);
/* 263:    */       }
/* 264:    */     }
/* 265:356 */     if ((result.classify() == 1) && (classify() != 1))
/* 266:    */     {
/* 267:357 */       getField().setIEEEFlagsBits(16);
/* 268:358 */       result = dotrap(16, "nextAfter", x, result);
/* 269:    */     }
/* 270:361 */     if ((result.equals(getZero())) && (!equals(getZero())))
/* 271:    */     {
/* 272:362 */       getField().setIEEEFlagsBits(16);
/* 273:363 */       result = dotrap(16, "nextAfter", x, result);
/* 274:    */     }
/* 275:366 */     return result;
/* 276:    */   }
/* 277:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.dfp.DfpDec
 * JD-Core Version:    0.7.0.1
 */
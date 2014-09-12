/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ 
/*   8:    */ public class Precision
/*   9:    */ {
/*  10:    */   public static final double EPSILON = 1.110223024625157E-016D;
/*  11:    */   public static final double SAFE_MIN = 2.225073858507201E-308D;
/*  12:    */   private static final long SGN_MASK = -9223372036854775808L;
/*  13:    */   private static final int SGN_MASK_FLOAT = -2147483648;
/*  14:    */   
/*  15:    */   public static int compareTo(double x, double y, double eps)
/*  16:    */   {
/*  17: 65 */     if (equals(x, y, eps)) {
/*  18: 66 */       return 0;
/*  19:    */     }
/*  20: 67 */     if (x < y) {
/*  21: 68 */       return -1;
/*  22:    */     }
/*  23: 70 */     return 1;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static int compareTo(double x, double y, int maxUlps)
/*  27:    */   {
/*  28: 91 */     if (equals(x, y, maxUlps)) {
/*  29: 92 */       return 0;
/*  30:    */     }
/*  31: 93 */     if (x < y) {
/*  32: 94 */       return -1;
/*  33:    */     }
/*  34: 96 */     return 1;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static boolean equals(float x, float y)
/*  38:    */   {
/*  39:108 */     return equals(x, y, 1);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static boolean equalsIncludingNaN(float x, float y)
/*  43:    */   {
/*  44:121 */     return ((Float.isNaN(x)) && (Float.isNaN(y))) || (equals(x, y, 1));
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static boolean equals(float x, float y, float eps)
/*  48:    */   {
/*  49:135 */     return (equals(x, y, 1)) || (FastMath.abs(y - x) <= eps);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static boolean equalsIncludingNaN(float x, float y, float eps)
/*  53:    */   {
/*  54:150 */     return (equalsIncludingNaN(x, y)) || (FastMath.abs(y - x) <= eps);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static boolean equals(float x, float y, int maxUlps)
/*  58:    */   {
/*  59:172 */     int xInt = Float.floatToIntBits(x);
/*  60:173 */     int yInt = Float.floatToIntBits(y);
/*  61:176 */     if (xInt < 0) {
/*  62:177 */       xInt = -2147483648 - xInt;
/*  63:    */     }
/*  64:179 */     if (yInt < 0) {
/*  65:180 */       yInt = -2147483648 - yInt;
/*  66:    */     }
/*  67:183 */     boolean isEqual = FastMath.abs(xInt - yInt) <= maxUlps;
/*  68:    */     
/*  69:185 */     return (isEqual) && (!Float.isNaN(x)) && (!Float.isNaN(y));
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static boolean equalsIncludingNaN(float x, float y, int maxUlps)
/*  73:    */   {
/*  74:201 */     return ((Float.isNaN(x)) && (Float.isNaN(y))) || (equals(x, y, maxUlps));
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static boolean equals(double x, double y)
/*  78:    */   {
/*  79:213 */     return equals(x, y, 1);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static boolean equalsIncludingNaN(double x, double y)
/*  83:    */   {
/*  84:226 */     return ((Double.isNaN(x)) && (Double.isNaN(y))) || (equals(x, y, 1));
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static boolean equals(double x, double y, double eps)
/*  88:    */   {
/*  89:241 */     return (equals(x, y, 1)) || (FastMath.abs(y - x) <= eps);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static boolean equalsIncludingNaN(double x, double y, double eps)
/*  93:    */   {
/*  94:256 */     return (equalsIncludingNaN(x, y)) || (FastMath.abs(y - x) <= eps);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static boolean equals(double x, double y, int maxUlps)
/*  98:    */   {
/*  99:277 */     long xInt = Double.doubleToLongBits(x);
/* 100:278 */     long yInt = Double.doubleToLongBits(y);
/* 101:281 */     if (xInt < 0L) {
/* 102:282 */       xInt = -9223372036854775808L - xInt;
/* 103:    */     }
/* 104:284 */     if (yInt < 0L) {
/* 105:285 */       yInt = -9223372036854775808L - yInt;
/* 106:    */     }
/* 107:288 */     boolean isEqual = FastMath.abs(xInt - yInt) <= maxUlps;
/* 108:    */     
/* 109:290 */     return (isEqual) && (!Double.isNaN(x)) && (!Double.isNaN(y));
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static boolean equalsIncludingNaN(double x, double y, int maxUlps)
/* 113:    */   {
/* 114:306 */     return ((Double.isNaN(x)) && (Double.isNaN(y))) || (equals(x, y, maxUlps));
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static double round(double x, int scale)
/* 118:    */   {
/* 119:319 */     return round(x, scale, 4);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static double round(double x, int scale, int roundingMethod)
/* 123:    */   {
/* 124:    */     try
/* 125:    */     {
/* 126:341 */       return new BigDecimal(Double.toString(x)).setScale(scale, roundingMethod).doubleValue();
/* 127:    */     }
/* 128:    */     catch (NumberFormatException ex)
/* 129:    */     {
/* 130:346 */       if (Double.isInfinite(x)) {
/* 131:347 */         return x;
/* 132:    */       }
/* 133:    */     }
/* 134:349 */     return (0.0D / 0.0D);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public static float round(float x, int scale)
/* 138:    */   {
/* 139:364 */     return round(x, scale, 4);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public static float round(float x, int scale, int roundingMethod)
/* 143:    */   {
/* 144:379 */     float sign = FastMath.copySign(1.0F, x);
/* 145:380 */     float factor = (float)FastMath.pow(10.0D, scale) * sign;
/* 146:381 */     return (float)roundUnscaled(x * factor, sign, roundingMethod) / factor;
/* 147:    */   }
/* 148:    */   
/* 149:    */   private static double roundUnscaled(double unscaled, double sign, int roundingMethod)
/* 150:    */   {
/* 151:399 */     switch (roundingMethod)
/* 152:    */     {
/* 153:    */     case 2: 
/* 154:401 */       if (sign == -1.0D) {
/* 155:402 */         unscaled = FastMath.floor(FastMath.nextAfter(unscaled, (-1.0D / 0.0D)));
/* 156:    */       } else {
/* 157:404 */         unscaled = FastMath.ceil(FastMath.nextAfter(unscaled, (1.0D / 0.0D)));
/* 158:    */       }
/* 159:406 */       break;
/* 160:    */     case 1: 
/* 161:408 */       unscaled = FastMath.floor(FastMath.nextAfter(unscaled, (-1.0D / 0.0D)));
/* 162:409 */       break;
/* 163:    */     case 3: 
/* 164:411 */       if (sign == -1.0D) {
/* 165:412 */         unscaled = FastMath.ceil(FastMath.nextAfter(unscaled, (1.0D / 0.0D)));
/* 166:    */       } else {
/* 167:414 */         unscaled = FastMath.floor(FastMath.nextAfter(unscaled, (-1.0D / 0.0D)));
/* 168:    */       }
/* 169:416 */       break;
/* 170:    */     case 5: 
/* 171:418 */       unscaled = FastMath.nextAfter(unscaled, (-1.0D / 0.0D));
/* 172:419 */       double fraction = unscaled - FastMath.floor(unscaled);
/* 173:420 */       if (fraction > 0.5D) {
/* 174:421 */         unscaled = FastMath.ceil(unscaled);
/* 175:    */       } else {
/* 176:423 */         unscaled = FastMath.floor(unscaled);
/* 177:    */       }
/* 178:425 */       break;
/* 179:    */     case 6: 
/* 180:428 */       fraction = unscaled - FastMath.floor(unscaled);
/* 181:429 */       if (fraction > 0.5D) {
/* 182:430 */         unscaled = FastMath.ceil(unscaled);
/* 183:431 */       } else if (fraction < 0.5D) {
/* 184:432 */         unscaled = FastMath.floor(unscaled);
/* 185:435 */       } else if (FastMath.floor(unscaled) / 2.0D == FastMath.floor(Math.floor(unscaled) / 2.0D)) {
/* 186:437 */         unscaled = FastMath.floor(unscaled);
/* 187:    */       } else {
/* 188:439 */         unscaled = FastMath.ceil(unscaled);
/* 189:    */       }
/* 190:442 */       break;
/* 191:    */     case 4: 
/* 192:445 */       unscaled = FastMath.nextAfter(unscaled, (1.0D / 0.0D));
/* 193:446 */       fraction = unscaled - FastMath.floor(unscaled);
/* 194:447 */       if (fraction >= 0.5D) {
/* 195:448 */         unscaled = FastMath.ceil(unscaled);
/* 196:    */       } else {
/* 197:450 */         unscaled = FastMath.floor(unscaled);
/* 198:    */       }
/* 199:452 */       break;
/* 200:    */     case 7: 
/* 201:455 */       if (unscaled != FastMath.floor(unscaled)) {
/* 202:456 */         throw new MathArithmeticException();
/* 203:    */       }
/* 204:    */       break;
/* 205:    */     case 0: 
/* 206:460 */       unscaled = FastMath.ceil(FastMath.nextAfter(unscaled, (1.0D / 0.0D)));
/* 207:461 */       break;
/* 208:    */     default: 
/* 209:463 */       throw new MathIllegalArgumentException(LocalizedFormats.INVALID_ROUNDING_METHOD, new Object[] { Integer.valueOf(roundingMethod), "ROUND_CEILING", Integer.valueOf(2), "ROUND_DOWN", Integer.valueOf(1), "ROUND_FLOOR", Integer.valueOf(3), "ROUND_HALF_DOWN", Integer.valueOf(5), "ROUND_HALF_EVEN", Integer.valueOf(6), "ROUND_HALF_UP", Integer.valueOf(4), "ROUND_UNNECESSARY", Integer.valueOf(7), "ROUND_UP", Integer.valueOf(0) });
/* 210:    */     }
/* 211:474 */     return unscaled;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public static double representableDelta(double x, double originalDelta)
/* 215:    */   {
/* 216:494 */     return x + originalDelta - x;
/* 217:    */   }
/* 218:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.Precision
 * JD-Core Version:    0.7.0.1
 */
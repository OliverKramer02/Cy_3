/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.math.BigDecimal;
/*   5:    */ import java.math.BigInteger;
/*   6:    */ import java.math.MathContext;
/*   7:    */ import java.math.RoundingMode;
/*   8:    */ import org.apache.commons.math3.Field;
/*   9:    */ import org.apache.commons.math3.FieldElement;
/*  10:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ 
/*  13:    */ public class BigReal
/*  14:    */   implements FieldElement<BigReal>, Comparable<BigReal>, Serializable
/*  15:    */ {
/*  16: 43 */   public static final BigReal ZERO = new BigReal(BigDecimal.ZERO);
/*  17: 46 */   public static final BigReal ONE = new BigReal(BigDecimal.ONE);
/*  18:    */   private static final long serialVersionUID = 4984534880991310382L;
/*  19:    */   private final BigDecimal d;
/*  20: 55 */   private RoundingMode roundingMode = RoundingMode.HALF_UP;
/*  21: 58 */   private int scale = 64;
/*  22:    */   
/*  23:    */   public BigReal(BigDecimal val)
/*  24:    */   {
/*  25: 64 */     this.d = val;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public BigReal(BigInteger val)
/*  29:    */   {
/*  30: 71 */     this.d = new BigDecimal(val);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public BigReal(BigInteger unscaledVal, int scale)
/*  34:    */   {
/*  35: 79 */     this.d = new BigDecimal(unscaledVal, scale);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public BigReal(BigInteger unscaledVal, int scale, MathContext mc)
/*  39:    */   {
/*  40: 88 */     this.d = new BigDecimal(unscaledVal, scale, mc);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public BigReal(BigInteger val, MathContext mc)
/*  44:    */   {
/*  45: 96 */     this.d = new BigDecimal(val, mc);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public BigReal(char[] in)
/*  49:    */   {
/*  50:103 */     this.d = new BigDecimal(in);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public BigReal(char[] in, int offset, int len)
/*  54:    */   {
/*  55:112 */     this.d = new BigDecimal(in, offset, len);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public BigReal(char[] in, int offset, int len, MathContext mc)
/*  59:    */   {
/*  60:122 */     this.d = new BigDecimal(in, offset, len, mc);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public BigReal(char[] in, MathContext mc)
/*  64:    */   {
/*  65:130 */     this.d = new BigDecimal(in, mc);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public BigReal(double val)
/*  69:    */   {
/*  70:137 */     this.d = new BigDecimal(val);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public BigReal(double val, MathContext mc)
/*  74:    */   {
/*  75:145 */     this.d = new BigDecimal(val, mc);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public BigReal(int val)
/*  79:    */   {
/*  80:152 */     this.d = new BigDecimal(val);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public BigReal(int val, MathContext mc)
/*  84:    */   {
/*  85:160 */     this.d = new BigDecimal(val, mc);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public BigReal(long val)
/*  89:    */   {
/*  90:167 */     this.d = new BigDecimal(val);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public BigReal(long val, MathContext mc)
/*  94:    */   {
/*  95:175 */     this.d = new BigDecimal(val, mc);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public BigReal(String val)
/*  99:    */   {
/* 100:182 */     this.d = new BigDecimal(val);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public BigReal(String val, MathContext mc)
/* 104:    */   {
/* 105:190 */     this.d = new BigDecimal(val, mc);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public RoundingMode getRoundingMode()
/* 109:    */   {
/* 110:200 */     return this.roundingMode;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setRoundingMode(RoundingMode roundingMode)
/* 114:    */   {
/* 115:209 */     this.roundingMode = roundingMode;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public int getScale()
/* 119:    */   {
/* 120:219 */     return this.scale;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setScale(int scale)
/* 124:    */   {
/* 125:228 */     this.scale = scale;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public BigReal add(BigReal a)
/* 129:    */   {
/* 130:233 */     return new BigReal(this.d.add(a.d));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public BigReal subtract(BigReal a)
/* 134:    */   {
/* 135:238 */     return new BigReal(this.d.subtract(a.d));
/* 136:    */   }
/* 137:    */   
/* 138:    */   public BigReal negate()
/* 139:    */   {
/* 140:243 */     return new BigReal(this.d.negate());
/* 141:    */   }
/* 142:    */   
/* 143:    */   public BigReal divide(BigReal a)
/* 144:    */   {
/* 145:    */     try
/* 146:    */     {
/* 147:253 */       return new BigReal(this.d.divide(a.d, this.scale, this.roundingMode));
/* 148:    */     }
/* 149:    */     catch (ArithmeticException e)
/* 150:    */     {
/* 151:256 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public BigReal reciprocal()
/* 156:    */   {
/* 157:    */     try
/* 158:    */     {
/* 159:267 */       return new BigReal(BigDecimal.ONE.divide(this.d, this.scale, this.roundingMode));
/* 160:    */     }
/* 161:    */     catch (ArithmeticException e)
/* 162:    */     {
/* 163:270 */       throw new MathArithmeticException(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public BigReal multiply(BigReal a)
/* 168:    */   {
/* 169:276 */     return new BigReal(this.d.multiply(a.d));
/* 170:    */   }
/* 171:    */   
/* 172:    */   public BigReal multiply(int n)
/* 173:    */   {
/* 174:281 */     return new BigReal(this.d.multiply(new BigDecimal(n)));
/* 175:    */   }
/* 176:    */   
/* 177:    */   public int compareTo(BigReal a)
/* 178:    */   {
/* 179:286 */     return this.d.compareTo(a.d);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public double doubleValue()
/* 183:    */   {
/* 184:293 */     return this.d.doubleValue();
/* 185:    */   }
/* 186:    */   
/* 187:    */   public BigDecimal bigDecimalValue()
/* 188:    */   {
/* 189:300 */     return this.d;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public boolean equals(Object other)
/* 193:    */   {
/* 194:306 */     if (this == other) {
/* 195:307 */       return true;
/* 196:    */     }
/* 197:310 */     if ((other instanceof BigReal)) {
/* 198:311 */       return this.d.equals(((BigReal)other).d);
/* 199:    */     }
/* 200:313 */     return false;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public int hashCode()
/* 204:    */   {
/* 205:319 */     return this.d.hashCode();
/* 206:    */   }
/* 207:    */   
/* 208:    */   public Field<BigReal> getField()
/* 209:    */   {
/* 210:324 */     return BigRealField.getInstance();
/* 211:    */   }
/* 212:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.BigReal
 * JD-Core Version:    0.7.0.1
 */
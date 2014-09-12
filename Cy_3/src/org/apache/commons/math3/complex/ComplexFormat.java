/*   1:    */ package org.apache.commons.math3.complex;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.MathInternalError;
/*   9:    */ import org.apache.commons.math3.exception.MathParseException;
/*  10:    */ import org.apache.commons.math3.exception.NoDataException;
/*  11:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  12:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  13:    */ import org.apache.commons.math3.util.CompositeFormat;
/*  14:    */ 
/*  15:    */ public class ComplexFormat
/*  16:    */ {
/*  17:    */   private static final String DEFAULT_IMAGINARY_CHARACTER = "i";
/*  18:    */   private final String imaginaryCharacter;
/*  19:    */   private final NumberFormat imaginaryFormat;
/*  20:    */   private final NumberFormat realFormat;
/*  21:    */   
/*  22:    */   public ComplexFormat()
/*  23:    */   {
/*  24: 56 */     this("i", CompositeFormat.getDefaultNumberFormat());
/*  25:    */   }
/*  26:    */   
/*  27:    */   public ComplexFormat(NumberFormat format)
/*  28:    */   {
/*  29: 65 */     this("i", format);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ComplexFormat(NumberFormat realFormat, NumberFormat imaginaryFormat)
/*  33:    */   {
/*  34: 75 */     this("i", realFormat, imaginaryFormat);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ComplexFormat(String imaginaryCharacter)
/*  38:    */   {
/*  39: 84 */     this(imaginaryCharacter, CompositeFormat.getDefaultNumberFormat());
/*  40:    */   }
/*  41:    */   
/*  42:    */   public ComplexFormat(String imaginaryCharacter, NumberFormat format)
/*  43:    */   {
/*  44: 94 */     this(imaginaryCharacter, format, format);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public ComplexFormat(String imaginaryCharacter, NumberFormat realFormat, NumberFormat imaginaryFormat)
/*  48:    */   {
/*  49:115 */     if (imaginaryCharacter == null) {
/*  50:116 */       throw new NullArgumentException();
/*  51:    */     }
/*  52:118 */     if (imaginaryCharacter.length() == 0) {
/*  53:119 */       throw new NoDataException();
/*  54:    */     }
/*  55:121 */     if (imaginaryFormat == null) {
/*  56:122 */       throw new NullArgumentException(LocalizedFormats.IMAGINARY_FORMAT, new Object[0]);
/*  57:    */     }
/*  58:124 */     if (realFormat == null) {
/*  59:125 */       throw new NullArgumentException(LocalizedFormats.REAL_FORMAT, new Object[0]);
/*  60:    */     }
/*  61:128 */     this.imaginaryCharacter = imaginaryCharacter;
/*  62:129 */     this.imaginaryFormat = imaginaryFormat;
/*  63:130 */     this.realFormat = realFormat;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static Locale[] getAvailableLocales()
/*  67:    */   {
/*  68:139 */     return NumberFormat.getAvailableLocales();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String format(Complex c)
/*  72:    */   {
/*  73:149 */     return format(c, new StringBuffer(), new FieldPosition(0)).toString();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String format(Double c)
/*  77:    */   {
/*  78:159 */     return format(new Complex(c.doubleValue(), 0.0D), new StringBuffer(), new FieldPosition(0)).toString();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public StringBuffer format(Complex complex, StringBuffer toAppendTo, FieldPosition pos)
/*  82:    */   {
/*  83:173 */     pos.setBeginIndex(0);
/*  84:174 */     pos.setEndIndex(0);
/*  85:    */     
/*  86:    */ 
/*  87:177 */     double re = complex.getReal();
/*  88:178 */     CompositeFormat.formatDouble(re, getRealFormat(), toAppendTo, pos);
/*  89:    */     
/*  90:    */ 
/*  91:181 */     double im = complex.getImaginary();
/*  92:183 */     if (im < 0.0D)
/*  93:    */     {
/*  94:184 */       toAppendTo.append(" - ");
/*  95:185 */       StringBuffer imAppendTo = formatImaginary(-im, new StringBuffer(), pos);
/*  96:186 */       toAppendTo.append(imAppendTo);
/*  97:187 */       toAppendTo.append(getImaginaryCharacter());
/*  98:    */     }
/*  99:188 */     else if ((im > 0.0D) || (Double.isNaN(im)))
/* 100:    */     {
/* 101:189 */       toAppendTo.append(" + ");
/* 102:190 */       StringBuffer imAppendTo = formatImaginary(im, new StringBuffer(), pos);
/* 103:191 */       toAppendTo.append(imAppendTo);
/* 104:192 */       toAppendTo.append(getImaginaryCharacter());
/* 105:    */     }
/* 106:195 */     return toAppendTo;
/* 107:    */   }
/* 108:    */   
/* 109:    */   private StringBuffer formatImaginary(double absIm, StringBuffer toAppendTo, FieldPosition pos)
/* 110:    */   {
/* 111:211 */     if (absIm < 0.0D) {
/* 112:212 */       throw new MathInternalError();
/* 113:    */     }
/* 114:215 */     pos.setBeginIndex(0);
/* 115:216 */     pos.setEndIndex(0);
/* 116:    */     
/* 117:218 */     CompositeFormat.formatDouble(absIm, getImaginaryFormat(), toAppendTo, pos);
/* 118:219 */     if (toAppendTo.toString().equals("1")) {
/* 119:221 */       toAppendTo.setLength(0);
/* 120:    */     }
/* 121:224 */     return toAppendTo;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/* 125:    */   {
/* 126:243 */     StringBuffer ret = null;
/* 127:245 */     if ((obj instanceof Complex)) {
/* 128:246 */       ret = format((Complex)obj, toAppendTo, pos);
/* 129:247 */     } else if ((obj instanceof Number)) {
/* 130:248 */       ret = format(new Complex(((Number)obj).doubleValue(), 0.0D), toAppendTo, pos);
/* 131:    */     } else {
/* 132:251 */       throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_FORMAT_INSTANCE_AS_COMPLEX, new Object[] { obj.getClass().getName() });
/* 133:    */     }
/* 134:255 */     return ret;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String getImaginaryCharacter()
/* 138:    */   {
/* 139:263 */     return this.imaginaryCharacter;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public NumberFormat getImaginaryFormat()
/* 143:    */   {
/* 144:271 */     return this.imaginaryFormat;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public static ComplexFormat getInstance()
/* 148:    */   {
/* 149:279 */     return getInstance(Locale.getDefault());
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static ComplexFormat getInstance(Locale locale)
/* 153:    */   {
/* 154:288 */     NumberFormat f = CompositeFormat.getDefaultNumberFormat(locale);
/* 155:289 */     return new ComplexFormat(f);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public static ComplexFormat getInstance(String imaginaryCharacter, Locale locale)
/* 159:    */   {
/* 160:300 */     NumberFormat f = CompositeFormat.getDefaultNumberFormat(locale);
/* 161:301 */     return new ComplexFormat(imaginaryCharacter, f);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public NumberFormat getRealFormat()
/* 165:    */   {
/* 166:309 */     return this.realFormat;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public Complex parse(String source)
/* 170:    */   {
/* 171:321 */     ParsePosition parsePosition = new ParsePosition(0);
/* 172:322 */     Complex result = parse(source, parsePosition);
/* 173:323 */     if (parsePosition.getIndex() == 0) {
/* 174:324 */       throw new MathParseException(source, parsePosition.getErrorIndex(), Complex.class);
/* 175:    */     }
/* 176:328 */     return result;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public Complex parse(String source, ParsePosition pos)
/* 180:    */   {
/* 181:339 */     int initialIndex = pos.getIndex();
/* 182:    */     
/* 183:    */ 
/* 184:342 */     CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 185:    */     
/* 186:    */ 
/* 187:345 */     Number re = CompositeFormat.parseNumber(source, getRealFormat(), pos);
/* 188:346 */     if (re == null)
/* 189:    */     {
/* 190:349 */       pos.setIndex(initialIndex);
/* 191:350 */       return null;
/* 192:    */     }
/* 193:354 */     int startIndex = pos.getIndex();
/* 194:355 */     char c = CompositeFormat.parseNextCharacter(source, pos);
/* 195:356 */     int sign = 0;
/* 196:357 */     switch (c)
/* 197:    */     {
/* 198:    */     case '\000': 
/* 199:361 */       return new Complex(re.doubleValue(), 0.0D);
/* 200:    */     case '-': 
/* 201:363 */       sign = -1;
/* 202:364 */       break;
/* 203:    */     case '+': 
/* 204:366 */       sign = 1;
/* 205:367 */       break;
/* 206:    */     default: 
/* 207:372 */       pos.setIndex(initialIndex);
/* 208:373 */       pos.setErrorIndex(startIndex);
/* 209:374 */       return null;
/* 210:    */     }
/* 211:378 */     CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 212:    */     
/* 213:    */ 
/* 214:381 */     Number im = CompositeFormat.parseNumber(source, getRealFormat(), pos);
/* 215:382 */     if (im == null)
/* 216:    */     {
/* 217:385 */       pos.setIndex(initialIndex);
/* 218:386 */       return null;
/* 219:    */     }
/* 220:390 */     if (!CompositeFormat.parseFixedstring(source, getImaginaryCharacter(), pos)) {
/* 221:391 */       return null;
/* 222:    */     }
/* 223:394 */     return new Complex(re.doubleValue(), im.doubleValue() * sign);
/* 224:    */   }
/* 225:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.complex.ComplexFormat
 * JD-Core Version:    0.7.0.1
 */
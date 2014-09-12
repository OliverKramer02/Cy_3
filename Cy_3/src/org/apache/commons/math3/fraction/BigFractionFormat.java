/*   1:    */ package org.apache.commons.math3.fraction;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.math.BigInteger;
/*   5:    */ import java.text.FieldPosition;
/*   6:    */ import java.text.NumberFormat;
/*   7:    */ import java.text.ParsePosition;
/*   8:    */ import java.util.Locale;
/*   9:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.MathParseException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ 
/*  13:    */ public class BigFractionFormat
/*  14:    */   extends AbstractFormat
/*  15:    */   implements Serializable
/*  16:    */ {
/*  17:    */   private static final long serialVersionUID = -2932167925527338976L;
/*  18:    */   
/*  19:    */   public BigFractionFormat() {}
/*  20:    */   
/*  21:    */   public BigFractionFormat(NumberFormat format)
/*  22:    */   {
/*  23: 59 */     super(format);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public BigFractionFormat(NumberFormat numeratorFormat, NumberFormat denominatorFormat)
/*  27:    */   {
/*  28: 70 */     super(numeratorFormat, denominatorFormat);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static Locale[] getAvailableLocales()
/*  32:    */   {
/*  33: 79 */     return NumberFormat.getAvailableLocales();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static String formatBigFraction(BigFraction f)
/*  37:    */   {
/*  38: 90 */     return getImproperInstance().format(f);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static BigFractionFormat getImproperInstance()
/*  42:    */   {
/*  43: 98 */     return getImproperInstance(Locale.getDefault());
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static BigFractionFormat getImproperInstance(Locale locale)
/*  47:    */   {
/*  48:107 */     return new BigFractionFormat(getDefaultNumberFormat(locale));
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static BigFractionFormat getProperInstance()
/*  52:    */   {
/*  53:115 */     return getProperInstance(Locale.getDefault());
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static BigFractionFormat getProperInstance(Locale locale)
/*  57:    */   {
/*  58:124 */     return new ProperBigFractionFormat(getDefaultNumberFormat(locale));
/*  59:    */   }
/*  60:    */   
/*  61:    */   public StringBuffer format(BigFraction BigFraction, StringBuffer toAppendTo, FieldPosition pos)
/*  62:    */   {
/*  63:140 */     pos.setBeginIndex(0);
/*  64:141 */     pos.setEndIndex(0);
/*  65:    */     
/*  66:143 */     getNumeratorFormat().format(BigFraction.getNumerator(), toAppendTo, pos);
/*  67:144 */     toAppendTo.append(" / ");
/*  68:145 */     getDenominatorFormat().format(BigFraction.getDenominator(), toAppendTo, pos);
/*  69:    */     
/*  70:147 */     return toAppendTo;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  74:    */   {
/*  75:    */     StringBuffer ret;
/*  76:169 */     if ((obj instanceof BigFraction))
/*  77:    */     {
/*  78:170 */       ret = format((BigFraction)obj, toAppendTo, pos);
/*  79:    */     }
/*  80:    */     else
/*  81:    */     {
/*  82:    */      
/*  83:171 */       if ((obj instanceof BigInteger))
/*  84:    */       {
/*  85:172 */         ret = format(new BigFraction((BigInteger)obj), toAppendTo, pos);
/*  86:    */       }
/*  87:    */       else
/*  88:    */       {
/*  89:    */        
/*  90:173 */         if ((obj instanceof Number)) {
/*  91:174 */           ret = format(new BigFraction(((Number)obj).doubleValue()), toAppendTo, pos);
/*  92:    */         } else {
/*  93:177 */           throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION, new Object[0]);
/*  94:    */         }
/*  95:    */       }
/*  96:    */     }
/*  97:    */    
/*  98:180 */     return ret;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public BigFraction parse(String source)
/* 102:    */     throws MathParseException
/* 103:    */   {
/* 104:192 */     ParsePosition parsePosition = new ParsePosition(0);
/* 105:193 */     BigFraction result = parse(source, parsePosition);
/* 106:194 */     if (parsePosition.getIndex() == 0) {
/* 107:195 */       throw new MathParseException(source, parsePosition.getErrorIndex(), BigFraction.class);
/* 108:    */     }
/* 109:197 */     return result;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public BigFraction parse(String source, ParsePosition pos)
/* 113:    */   {
/* 114:209 */     int initialIndex = pos.getIndex();
/* 115:    */     
/* 116:    */ 
/* 117:212 */     parseAndIgnoreWhitespace(source, pos);
/* 118:    */     
/* 119:    */ 
/* 120:215 */     BigInteger num = parseNextBigInteger(source, pos);
/* 121:216 */     if (num == null)
/* 122:    */     {
/* 123:220 */       pos.setIndex(initialIndex);
/* 124:221 */       return null;
/* 125:    */     }
/* 126:225 */     int startIndex = pos.getIndex();
/* 127:226 */     char c = parseNextCharacter(source, pos);
/* 128:227 */     switch (c)
/* 129:    */     {
/* 130:    */     case '\000': 
/* 131:231 */       return new BigFraction(num);
/* 132:    */     case '/': 
/* 133:    */       break;
/* 134:    */     default: 
/* 135:239 */       pos.setIndex(initialIndex);
/* 136:240 */       pos.setErrorIndex(startIndex);
/* 137:241 */       return null;
/* 138:    */     }
/* 139:245 */     parseAndIgnoreWhitespace(source, pos);
/* 140:    */     
/* 141:    */ 
/* 142:248 */     BigInteger den = parseNextBigInteger(source, pos);
/* 143:249 */     if (den == null)
/* 144:    */     {
/* 145:253 */       pos.setIndex(initialIndex);
/* 146:254 */       return null;
/* 147:    */     }
/* 148:257 */     return new BigFraction(num, den);
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected BigInteger parseNextBigInteger(String source, ParsePosition pos)
/* 152:    */   {
/* 153:270 */     int start = pos.getIndex();
/* 154:271 */     int end = source.charAt(start) == '-' ? start + 1 : start;
/* 155:272 */     while ((end < source.length()) && (Character.isDigit(source.charAt(end)))) {
/* 156:274 */       end++;
/* 157:    */     }
/* 158:    */     try
/* 159:    */     {
/* 160:278 */       BigInteger n = new BigInteger(source.substring(start, end));
/* 161:279 */       pos.setIndex(end);
/* 162:280 */       return n;
/* 163:    */     }
/* 164:    */     catch (NumberFormatException nfe)
/* 165:    */     {
/* 166:282 */       pos.setErrorIndex(start);
/* 167:    */     }
/* 168:283 */     return null;
/* 169:    */   }
/* 170:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.BigFractionFormat
 * JD-Core Version:    0.7.0.1
 */
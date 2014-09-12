/*   1:    */ package org.apache.commons.math3.fraction;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ import java.text.FieldPosition;
/*   5:    */ import java.text.NumberFormat;
/*   6:    */ import java.text.ParsePosition;
/*   7:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ 
/*  10:    */ public class ProperBigFractionFormat
/*  11:    */   extends BigFractionFormat
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = -6337346779577272307L;
/*  14:    */   private NumberFormat wholeFormat;
/*  15:    */   
/*  16:    */   public ProperBigFractionFormat()
/*  17:    */   {
/*  18: 51 */     this(getDefaultNumberFormat());
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ProperBigFractionFormat(NumberFormat format)
/*  22:    */   {
/*  23: 61 */     this(format, (NumberFormat)format.clone(), (NumberFormat)format.clone());
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ProperBigFractionFormat(NumberFormat wholeFormat, NumberFormat numeratorFormat, NumberFormat denominatorFormat)
/*  27:    */   {
/*  28: 74 */     super(numeratorFormat, denominatorFormat);
/*  29: 75 */     setWholeFormat(wholeFormat);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public StringBuffer format(BigFraction fraction, StringBuffer toAppendTo, FieldPosition pos)
/*  33:    */   {
/*  34: 92 */     pos.setBeginIndex(0);
/*  35: 93 */     pos.setEndIndex(0);
/*  36:    */     
/*  37: 95 */     BigInteger num = fraction.getNumerator();
/*  38: 96 */     BigInteger den = fraction.getDenominator();
/*  39: 97 */     BigInteger whole = num.divide(den);
/*  40: 98 */     num = num.remainder(den);
/*  41:100 */     if (!BigInteger.ZERO.equals(whole))
/*  42:    */     {
/*  43:101 */       getWholeFormat().format(whole, toAppendTo, pos);
/*  44:102 */       toAppendTo.append(' ');
/*  45:103 */       if (num.compareTo(BigInteger.ZERO) < 0) {
/*  46:104 */         num = num.negate();
/*  47:    */       }
/*  48:    */     }
/*  49:107 */     getNumeratorFormat().format(num, toAppendTo, pos);
/*  50:108 */     toAppendTo.append(" / ");
/*  51:109 */     getDenominatorFormat().format(den, toAppendTo, pos);
/*  52:    */     
/*  53:111 */     return toAppendTo;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public NumberFormat getWholeFormat()
/*  57:    */   {
/*  58:119 */     return this.wholeFormat;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public BigFraction parse(String source, ParsePosition pos)
/*  62:    */   {
/*  63:137 */     BigFraction ret = super.parse(source, pos);
/*  64:138 */     if (ret != null) {
/*  65:139 */       return ret;
/*  66:    */     }
/*  67:142 */     int initialIndex = pos.getIndex();
/*  68:    */     
/*  69:    */ 
/*  70:145 */     parseAndIgnoreWhitespace(source, pos);
/*  71:    */     
/*  72:    */ 
/*  73:148 */     BigInteger whole = parseNextBigInteger(source, pos);
/*  74:149 */     if (whole == null)
/*  75:    */     {
/*  76:153 */       pos.setIndex(initialIndex);
/*  77:154 */       return null;
/*  78:    */     }
/*  79:158 */     parseAndIgnoreWhitespace(source, pos);
/*  80:    */     
/*  81:    */ 
/*  82:161 */     BigInteger num = parseNextBigInteger(source, pos);
/*  83:162 */     if (num == null)
/*  84:    */     {
/*  85:166 */       pos.setIndex(initialIndex);
/*  86:167 */       return null;
/*  87:    */     }
/*  88:170 */     if (num.compareTo(BigInteger.ZERO) < 0)
/*  89:    */     {
/*  90:172 */       pos.setIndex(initialIndex);
/*  91:173 */       return null;
/*  92:    */     }
/*  93:177 */     int startIndex = pos.getIndex();
/*  94:178 */     char c = parseNextCharacter(source, pos);
/*  95:179 */     switch (c)
/*  96:    */     {
/*  97:    */     case '\000': 
/*  98:183 */       return new BigFraction(num);
/*  99:    */     case '/': 
/* 100:    */       break;
/* 101:    */     default: 
/* 102:191 */       pos.setIndex(initialIndex);
/* 103:192 */       pos.setErrorIndex(startIndex);
/* 104:193 */       return null;
/* 105:    */     }
/* 106:197 */     parseAndIgnoreWhitespace(source, pos);
/* 107:    */     
/* 108:    */ 
/* 109:200 */     BigInteger den = parseNextBigInteger(source, pos);
/* 110:201 */     if (den == null)
/* 111:    */     {
/* 112:205 */       pos.setIndex(initialIndex);
/* 113:206 */       return null;
/* 114:    */     }
/* 115:209 */     if (den.compareTo(BigInteger.ZERO) < 0)
/* 116:    */     {
/* 117:211 */       pos.setIndex(initialIndex);
/* 118:212 */       return null;
/* 119:    */     }
/* 120:215 */     boolean wholeIsNeg = whole.compareTo(BigInteger.ZERO) < 0;
/* 121:216 */     if (wholeIsNeg) {
/* 122:217 */       whole = whole.negate();
/* 123:    */     }
/* 124:219 */     num = whole.multiply(den).add(num);
/* 125:220 */     if (wholeIsNeg) {
/* 126:221 */       num = num.negate();
/* 127:    */     }
/* 128:224 */     return new BigFraction(num, den);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setWholeFormat(NumberFormat format)
/* 132:    */   {
/* 133:234 */     if (format == null) {
/* 134:235 */       throw new NullArgumentException(LocalizedFormats.WHOLE_FORMAT, new Object[0]);
/* 135:    */     }
/* 136:237 */     this.wholeFormat = format;
/* 137:    */   }
/* 138:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.ProperBigFractionFormat
 * JD-Core Version:    0.7.0.1
 */
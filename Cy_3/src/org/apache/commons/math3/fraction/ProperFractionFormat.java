/*   1:    */ package org.apache.commons.math3.fraction;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.util.MathUtils;
/*   9:    */ 
/*  10:    */ public class ProperFractionFormat
/*  11:    */   extends FractionFormat
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = 760934726031766749L;
/*  14:    */   private NumberFormat wholeFormat;
/*  15:    */   
/*  16:    */   public ProperFractionFormat()
/*  17:    */   {
/*  18: 51 */     this(getDefaultNumberFormat());
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ProperFractionFormat(NumberFormat format)
/*  22:    */   {
/*  23: 61 */     this(format, (NumberFormat)format.clone(), (NumberFormat)format.clone());
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ProperFractionFormat(NumberFormat wholeFormat, NumberFormat numeratorFormat, NumberFormat denominatorFormat)
/*  27:    */   {
/*  28: 75 */     super(numeratorFormat, denominatorFormat);
/*  29: 76 */     setWholeFormat(wholeFormat);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public StringBuffer format(Fraction fraction, StringBuffer toAppendTo, FieldPosition pos)
/*  33:    */   {
/*  34: 93 */     pos.setBeginIndex(0);
/*  35: 94 */     pos.setEndIndex(0);
/*  36:    */     
/*  37: 96 */     int num = fraction.getNumerator();
/*  38: 97 */     int den = fraction.getDenominator();
/*  39: 98 */     int whole = num / den;
/*  40: 99 */     num %= den;
/*  41:101 */     if (whole != 0)
/*  42:    */     {
/*  43:102 */       getWholeFormat().format(whole, toAppendTo, pos);
/*  44:103 */       toAppendTo.append(' ');
/*  45:104 */       num = Math.abs(num);
/*  46:    */     }
/*  47:106 */     getNumeratorFormat().format(num, toAppendTo, pos);
/*  48:107 */     toAppendTo.append(" / ");
/*  49:108 */     getDenominatorFormat().format(den, toAppendTo, pos);
/*  50:    */     
/*  51:    */ 
/*  52:111 */     return toAppendTo;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public NumberFormat getWholeFormat()
/*  56:    */   {
/*  57:119 */     return this.wholeFormat;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Fraction parse(String source, ParsePosition pos)
/*  61:    */   {
/*  62:137 */     Fraction ret = super.parse(source, pos);
/*  63:138 */     if (ret != null) {
/*  64:139 */       return ret;
/*  65:    */     }
/*  66:142 */     int initialIndex = pos.getIndex();
/*  67:    */     
/*  68:    */ 
/*  69:145 */     parseAndIgnoreWhitespace(source, pos);
/*  70:    */     
/*  71:    */ 
/*  72:148 */     Number whole = getWholeFormat().parse(source, pos);
/*  73:149 */     if (whole == null)
/*  74:    */     {
/*  75:153 */       pos.setIndex(initialIndex);
/*  76:154 */       return null;
/*  77:    */     }
/*  78:158 */     parseAndIgnoreWhitespace(source, pos);
/*  79:    */     
/*  80:    */ 
/*  81:161 */     Number num = getNumeratorFormat().parse(source, pos);
/*  82:162 */     if (num == null)
/*  83:    */     {
/*  84:166 */       pos.setIndex(initialIndex);
/*  85:167 */       return null;
/*  86:    */     }
/*  87:170 */     if (num.intValue() < 0)
/*  88:    */     {
/*  89:172 */       pos.setIndex(initialIndex);
/*  90:173 */       return null;
/*  91:    */     }
/*  92:177 */     int startIndex = pos.getIndex();
/*  93:178 */     char c = parseNextCharacter(source, pos);
/*  94:179 */     switch (c)
/*  95:    */     {
/*  96:    */     case '\000': 
/*  97:183 */       return new Fraction(num.intValue(), 1);
/*  98:    */     case '/': 
/*  99:    */       break;
/* 100:    */     default: 
/* 101:191 */       pos.setIndex(initialIndex);
/* 102:192 */       pos.setErrorIndex(startIndex);
/* 103:193 */       return null;
/* 104:    */     }
/* 105:197 */     parseAndIgnoreWhitespace(source, pos);
/* 106:    */     
/* 107:    */ 
/* 108:200 */     Number den = getDenominatorFormat().parse(source, pos);
/* 109:201 */     if (den == null)
/* 110:    */     {
/* 111:205 */       pos.setIndex(initialIndex);
/* 112:206 */       return null;
/* 113:    */     }
/* 114:209 */     if (den.intValue() < 0)
/* 115:    */     {
/* 116:211 */       pos.setIndex(initialIndex);
/* 117:212 */       return null;
/* 118:    */     }
/* 119:215 */     int w = whole.intValue();
/* 120:216 */     int n = num.intValue();
/* 121:217 */     int d = den.intValue();
/* 122:218 */     return new Fraction((Math.abs(w) * d + n) * MathUtils.copySign(1, w), d);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setWholeFormat(NumberFormat format)
/* 126:    */   {
/* 127:227 */     if (format == null) {
/* 128:228 */       throw new NullArgumentException(LocalizedFormats.WHOLE_FORMAT, new Object[0]);
/* 129:    */     }
/* 130:230 */     this.wholeFormat = format;
/* 131:    */   }
/* 132:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.ProperFractionFormat
 * JD-Core Version:    0.7.0.1
 */
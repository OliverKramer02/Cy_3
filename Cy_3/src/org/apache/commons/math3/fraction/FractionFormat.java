/*   1:    */ package org.apache.commons.math3.fraction;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   8:    */ import org.apache.commons.math3.exception.MathParseException;
/*   9:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  10:    */ 
/*  11:    */ public class FractionFormat
/*  12:    */   extends AbstractFormat
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = 3008655719530972611L;
/*  15:    */   
/*  16:    */   public FractionFormat() {}
/*  17:    */   
/*  18:    */   public FractionFormat(NumberFormat format)
/*  19:    */   {
/*  20: 55 */     super(format);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public FractionFormat(NumberFormat numeratorFormat, NumberFormat denominatorFormat)
/*  24:    */   {
/*  25: 66 */     super(numeratorFormat, denominatorFormat);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static Locale[] getAvailableLocales()
/*  29:    */   {
/*  30: 75 */     return NumberFormat.getAvailableLocales();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static String formatFraction(Fraction f)
/*  34:    */   {
/*  35: 86 */     return getImproperInstance().format(f);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static FractionFormat getImproperInstance()
/*  39:    */   {
/*  40: 94 */     return getImproperInstance(Locale.getDefault());
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static FractionFormat getImproperInstance(Locale locale)
/*  44:    */   {
/*  45:103 */     return new FractionFormat(getDefaultNumberFormat(locale));
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static FractionFormat getProperInstance()
/*  49:    */   {
/*  50:111 */     return getProperInstance(Locale.getDefault());
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static FractionFormat getProperInstance(Locale locale)
/*  54:    */   {
/*  55:120 */     return new ProperFractionFormat(getDefaultNumberFormat(locale));
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected static NumberFormat getDefaultNumberFormat()
/*  59:    */   {
/*  60:130 */     return getDefaultNumberFormat(Locale.getDefault());
/*  61:    */   }
/*  62:    */   
/*  63:    */   public StringBuffer format(Fraction fraction, StringBuffer toAppendTo, FieldPosition pos)
/*  64:    */   {
/*  65:146 */     pos.setBeginIndex(0);
/*  66:147 */     pos.setEndIndex(0);
/*  67:    */     
/*  68:149 */     getNumeratorFormat().format(fraction.getNumerator(), toAppendTo, pos);
/*  69:150 */     toAppendTo.append(" / ");
/*  70:151 */     getDenominatorFormat().format(fraction.getDenominator(), toAppendTo, pos);
/*  71:    */     
/*  72:    */ 
/*  73:154 */     return toAppendTo;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  77:    */     throws FractionConversionException, MathIllegalArgumentException
/*  78:    */   {
/*  79:175 */     StringBuffer ret = null;
/*  80:177 */     if ((obj instanceof Fraction)) {
/*  81:178 */       ret = format((Fraction)obj, toAppendTo, pos);
/*  82:179 */     } else if ((obj instanceof Number)) {
/*  83:180 */       ret = format(new Fraction(((Number)obj).doubleValue()), toAppendTo, pos);
/*  84:    */     } else {
/*  85:182 */       throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION, new Object[0]);
/*  86:    */     }
/*  87:185 */     return ret;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Fraction parse(String source)
/*  91:    */     throws MathParseException
/*  92:    */   {
/*  93:197 */     ParsePosition parsePosition = new ParsePosition(0);
/*  94:198 */     Fraction result = parse(source, parsePosition);
/*  95:199 */     if (parsePosition.getIndex() == 0) {
/*  96:200 */       throw new MathParseException(source, parsePosition.getErrorIndex(), Fraction.class);
/*  97:    */     }
/*  98:202 */     return result;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public Fraction parse(String source, ParsePosition pos)
/* 102:    */   {
/* 103:214 */     int initialIndex = pos.getIndex();
/* 104:    */     
/* 105:    */ 
/* 106:217 */     parseAndIgnoreWhitespace(source, pos);
/* 107:    */     
/* 108:    */ 
/* 109:220 */     Number num = getNumeratorFormat().parse(source, pos);
/* 110:221 */     if (num == null)
/* 111:    */     {
/* 112:225 */       pos.setIndex(initialIndex);
/* 113:226 */       return null;
/* 114:    */     }
/* 115:230 */     int startIndex = pos.getIndex();
/* 116:231 */     char c = parseNextCharacter(source, pos);
/* 117:232 */     switch (c)
/* 118:    */     {
/* 119:    */     case '\000': 
/* 120:236 */       return new Fraction(num.intValue(), 1);
/* 121:    */     case '/': 
/* 122:    */       break;
/* 123:    */     default: 
/* 124:244 */       pos.setIndex(initialIndex);
/* 125:245 */       pos.setErrorIndex(startIndex);
/* 126:246 */       return null;
/* 127:    */     }
/* 128:250 */     parseAndIgnoreWhitespace(source, pos);
/* 129:    */     
/* 130:    */ 
/* 131:253 */     Number den = getDenominatorFormat().parse(source, pos);
/* 132:254 */     if (den == null)
/* 133:    */     {
/* 134:258 */       pos.setIndex(initialIndex);
/* 135:259 */       return null;
/* 136:    */     }
/* 137:262 */     return new Fraction(num.intValue(), den.intValue());
/* 138:    */   }
/* 139:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.FractionFormat
 * JD-Core Version:    0.7.0.1
 */
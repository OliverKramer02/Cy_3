/*   1:    */ package org.apache.commons.math3.fraction;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.text.FieldPosition;
/*   5:    */ import java.text.NumberFormat;
/*   6:    */ import java.text.ParsePosition;
/*   7:    */ import java.util.Locale;
/*   8:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   9:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  10:    */ 
/*  11:    */ public abstract class AbstractFormat
/*  12:    */   extends NumberFormat
/*  13:    */   implements Serializable
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = -6981118387974191891L;
/*  16:    */   private NumberFormat denominatorFormat;
/*  17:    */   private NumberFormat numeratorFormat;
/*  18:    */   
/*  19:    */   protected AbstractFormat()
/*  20:    */   {
/*  21: 50 */     this(getDefaultNumberFormat());
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected AbstractFormat(NumberFormat format)
/*  25:    */   {
/*  26: 59 */     this(format, (NumberFormat)format.clone());
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected AbstractFormat(NumberFormat numeratorFormat, NumberFormat denominatorFormat)
/*  30:    */   {
/*  31: 70 */     this.numeratorFormat = numeratorFormat;
/*  32: 71 */     this.denominatorFormat = denominatorFormat;
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected static NumberFormat getDefaultNumberFormat()
/*  36:    */   {
/*  37: 81 */     return getDefaultNumberFormat(Locale.getDefault());
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected static NumberFormat getDefaultNumberFormat(Locale locale)
/*  41:    */   {
/*  42: 92 */     NumberFormat nf = NumberFormat.getNumberInstance(locale);
/*  43: 93 */     nf.setMaximumFractionDigits(0);
/*  44: 94 */     nf.setParseIntegerOnly(true);
/*  45: 95 */     return nf;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public NumberFormat getDenominatorFormat()
/*  49:    */   {
/*  50:103 */     return this.denominatorFormat;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public NumberFormat getNumeratorFormat()
/*  54:    */   {
/*  55:111 */     return this.numeratorFormat;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setDenominatorFormat(NumberFormat format)
/*  59:    */   {
/*  60:120 */     if (format == null) {
/*  61:121 */       throw new NullArgumentException(LocalizedFormats.DENOMINATOR_FORMAT, new Object[0]);
/*  62:    */     }
/*  63:123 */     this.denominatorFormat = format;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setNumeratorFormat(NumberFormat format)
/*  67:    */   {
/*  68:132 */     if (format == null) {
/*  69:133 */       throw new NullArgumentException(LocalizedFormats.NUMERATOR_FORMAT, new Object[0]);
/*  70:    */     }
/*  71:135 */     this.numeratorFormat = format;
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected static void parseAndIgnoreWhitespace(String source, ParsePosition pos)
/*  75:    */   {
/*  76:146 */     parseNextCharacter(source, pos);
/*  77:147 */     pos.setIndex(pos.getIndex() - 1);
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected static char parseNextCharacter(String source, ParsePosition pos)
/*  81:    */   {
/*  82:158 */     int index = pos.getIndex();
/*  83:159 */     int n = source.length();
/*  84:160 */     char ret = '\000';
/*  85:162 */     if (index < n)
/*  86:    */     {
/*  87:    */       char c;
/*  88:    */       do
/*  89:    */       {
/*  90:165 */         c = source.charAt(index++);
/*  91:166 */       } while ((Character.isWhitespace(c)) && (index < n));
/*  92:167 */       pos.setIndex(index);
/*  93:169 */       if (index < n) {
/*  94:170 */         ret = c;
/*  95:    */       }
/*  96:    */     }
/*  97:174 */     return ret;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public StringBuffer format(double value, StringBuffer buffer, FieldPosition position)
/* 101:    */   {
/* 102:190 */     return format(Double.valueOf(value), buffer, position);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public StringBuffer format(long value, StringBuffer buffer, FieldPosition position)
/* 106:    */   {
/* 107:207 */     return format(Long.valueOf(value), buffer, position);
/* 108:    */   }
/* 109:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.AbstractFormat
 * JD-Core Version:    0.7.0.1
 */
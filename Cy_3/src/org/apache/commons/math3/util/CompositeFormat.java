/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.util.Locale;
/*   7:    */ 
/*   8:    */ public class CompositeFormat
/*   9:    */ {
/*  10:    */   public static NumberFormat getDefaultNumberFormat()
/*  11:    */   {
/*  12: 43 */     return getDefaultNumberFormat(Locale.getDefault());
/*  13:    */   }
/*  14:    */   
/*  15:    */   public static NumberFormat getDefaultNumberFormat(Locale locale)
/*  16:    */   {
/*  17: 54 */     NumberFormat nf = NumberFormat.getInstance(locale);
/*  18: 55 */     nf.setMaximumFractionDigits(2);
/*  19: 56 */     return nf;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static void parseAndIgnoreWhitespace(String source, ParsePosition pos)
/*  23:    */   {
/*  24: 68 */     parseNextCharacter(source, pos);
/*  25: 69 */     pos.setIndex(pos.getIndex() - 1);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static char parseNextCharacter(String source, ParsePosition pos)
/*  29:    */   {
/*  30: 81 */     int index = pos.getIndex();
/*  31: 82 */     int n = source.length();
/*  32: 83 */     char ret = '\000';
/*  33: 85 */     if (index < n)
/*  34:    */     {
/*  35:    */       char c;
/*  36:    */       do
/*  37:    */       {
/*  38: 88 */         c = source.charAt(index++);
/*  39: 89 */       } while ((Character.isWhitespace(c)) && (index < n));
/*  40: 90 */       pos.setIndex(index);
/*  41: 92 */       if (index < n) {
/*  42: 93 */         ret = c;
/*  43:    */       }
/*  44:    */     }
/*  45: 97 */     return ret;
/*  46:    */   }
/*  47:    */   
/*  48:    */   private static Number parseNumber(String source, double value, ParsePosition pos)
/*  49:    */   {
/*  50:111 */     Number ret = null;
/*  51:    */     
/*  52:113 */     StringBuilder sb = new StringBuilder();
/*  53:114 */     sb.append('(');
/*  54:115 */     sb.append(value);
/*  55:116 */     sb.append(')');
/*  56:    */     
/*  57:118 */     int n = sb.length();
/*  58:119 */     int startIndex = pos.getIndex();
/*  59:120 */     int endIndex = startIndex + n;
/*  60:121 */     if ((endIndex < source.length()) && 
/*  61:122 */       (source.substring(startIndex, endIndex).compareTo(sb.toString()) == 0))
/*  62:    */     {
/*  63:123 */       ret = Double.valueOf(value);
/*  64:124 */       pos.setIndex(endIndex);
/*  65:    */     }
/*  66:128 */     return ret;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static Number parseNumber(String source, NumberFormat format, ParsePosition pos)
/*  70:    */   {
/*  71:143 */     int startIndex = pos.getIndex();
/*  72:144 */     Number number = format.parse(source, pos);
/*  73:145 */     int endIndex = pos.getIndex();
/*  74:148 */     if (startIndex == endIndex)
/*  75:    */     {
/*  76:150 */       double[] special = { (0.0D / 0.0D), (1.0D / 0.0D), (-1.0D / 0.0D) };
/*  77:153 */       for (int i = 0; i < special.length; i++)
/*  78:    */       {
/*  79:154 */         number = parseNumber(source, special[i], pos);
/*  80:155 */         if (number != null) {
/*  81:    */           break;
/*  82:    */         }
/*  83:    */       }
/*  84:    */     }
/*  85:161 */     return number;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static boolean parseFixedstring(String source, String expected, ParsePosition pos)
/*  89:    */   {
/*  90:175 */     int startIndex = pos.getIndex();
/*  91:176 */     int endIndex = startIndex + expected.length();
/*  92:177 */     if ((startIndex >= source.length()) || (endIndex > source.length()) || (source.substring(startIndex, endIndex).compareTo(expected) != 0))
/*  93:    */     {
/*  94:181 */       pos.setIndex(startIndex);
/*  95:182 */       pos.setErrorIndex(startIndex);
/*  96:183 */       return false;
/*  97:    */     }
/*  98:187 */     pos.setIndex(endIndex);
/*  99:188 */     return true;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static StringBuffer formatDouble(double value, NumberFormat format, StringBuffer toAppendTo, FieldPosition pos)
/* 103:    */   {
/* 104:211 */     if ((Double.isNaN(value)) || (Double.isInfinite(value)))
/* 105:    */     {
/* 106:212 */       toAppendTo.append('(');
/* 107:213 */       toAppendTo.append(value);
/* 108:214 */       toAppendTo.append(')');
/* 109:    */     }
/* 110:    */     else
/* 111:    */     {
/* 112:216 */       format.format(value, toAppendTo, pos);
/* 113:    */     }
/* 114:218 */     return toAppendTo;
/* 115:    */   }
/* 116:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.CompositeFormat
 * JD-Core Version:    0.7.0.1
 */
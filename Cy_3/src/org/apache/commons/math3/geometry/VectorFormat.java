/*   1:    */ package org.apache.commons.math3.geometry;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.apache.commons.math3.exception.MathParseException;
/*   8:    */ import org.apache.commons.math3.util.CompositeFormat;
/*   9:    */ 
/*  10:    */ public abstract class VectorFormat<S extends Space>
/*  11:    */ {
/*  12:    */   public static final String DEFAULT_PREFIX = "{";
/*  13:    */   public static final String DEFAULT_SUFFIX = "}";
/*  14:    */   public static final String DEFAULT_SEPARATOR = "; ";
/*  15:    */   private final String prefix;
/*  16:    */   private final String suffix;
/*  17:    */   private final String separator;
/*  18:    */   private final String trimmedPrefix;
/*  19:    */   private final String trimmedSuffix;
/*  20:    */   private final String trimmedSeparator;
/*  21:    */   private final NumberFormat format;
/*  22:    */   
/*  23:    */   protected VectorFormat()
/*  24:    */   {
/*  25: 81 */     this("{", "}", "; ", CompositeFormat.getDefaultNumberFormat());
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected VectorFormat(NumberFormat format)
/*  29:    */   {
/*  30: 90 */     this("{", "}", "; ", format);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected VectorFormat(String prefix, String suffix, String separator)
/*  34:    */   {
/*  35:101 */     this(prefix, suffix, separator, CompositeFormat.getDefaultNumberFormat());
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected VectorFormat(String prefix, String suffix, String separator, NumberFormat format)
/*  39:    */   {
/*  40:114 */     this.prefix = prefix;
/*  41:115 */     this.suffix = suffix;
/*  42:116 */     this.separator = separator;
/*  43:117 */     this.trimmedPrefix = prefix.trim();
/*  44:118 */     this.trimmedSuffix = suffix.trim();
/*  45:119 */     this.trimmedSeparator = separator.trim();
/*  46:120 */     this.format = format;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static Locale[] getAvailableLocales()
/*  50:    */   {
/*  51:129 */     return NumberFormat.getAvailableLocales();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getPrefix()
/*  55:    */   {
/*  56:137 */     return this.prefix;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getSuffix()
/*  60:    */   {
/*  61:145 */     return this.suffix;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getSeparator()
/*  65:    */   {
/*  66:153 */     return this.separator;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public NumberFormat getFormat()
/*  70:    */   {
/*  71:161 */     return this.format;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String format(Vector<S> vector)
/*  75:    */   {
/*  76:170 */     return format(vector, new StringBuffer(), new FieldPosition(0)).toString();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public abstract StringBuffer format(Vector<S> paramVector, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*  80:    */   
/*  81:    */   protected StringBuffer format(StringBuffer toAppendTo, FieldPosition pos, double... coordinates)
/*  82:    */   {
/*  83:195 */     pos.setBeginIndex(0);
/*  84:196 */     pos.setEndIndex(0);
/*  85:    */     
/*  86:    */ 
/*  87:199 */     toAppendTo.append(this.prefix);
/*  88:202 */     for (int i = 0; i < coordinates.length; i++)
/*  89:    */     {
/*  90:203 */       if (i > 0) {
/*  91:204 */         toAppendTo.append(this.separator);
/*  92:    */       }
/*  93:206 */       CompositeFormat.formatDouble(coordinates[i], this.format, toAppendTo, pos);
/*  94:    */     }
/*  95:210 */     toAppendTo.append(this.suffix);
/*  96:    */     
/*  97:212 */     return toAppendTo;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public abstract Vector<S> parse(String paramString)
/* 101:    */     throws MathParseException;
/* 102:    */   
/* 103:    */   public abstract Vector<S> parse(String paramString, ParsePosition paramParsePosition);
/* 104:    */   
/* 105:    */   protected double[] parseCoordinates(int dimension, String source, ParsePosition pos)
/* 106:    */   {
/* 107:242 */     int initialIndex = pos.getIndex();
/* 108:243 */     double[] coordinates = new double[dimension];
/* 109:    */     
/* 110:    */ 
/* 111:246 */     CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 112:247 */     if (!CompositeFormat.parseFixedstring(source, this.trimmedPrefix, pos)) {
/* 113:248 */       return null;
/* 114:    */     }
/* 115:251 */     for (int i = 0; i < dimension; i++)
/* 116:    */     {
/* 117:254 */       CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 118:257 */       if ((i > 0) && 
/* 119:258 */         (!CompositeFormat.parseFixedstring(source, this.trimmedSeparator, pos))) {
/* 120:259 */         return null;
/* 121:    */       }
/* 122:264 */       CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 123:    */       
/* 124:    */ 
/* 125:267 */       Number c = CompositeFormat.parseNumber(source, this.format, pos);
/* 126:268 */       if (c == null)
/* 127:    */       {
/* 128:271 */         pos.setIndex(initialIndex);
/* 129:272 */         return null;
/* 130:    */       }
/* 131:276 */       coordinates[i] = c.doubleValue();
/* 132:    */     }
/* 133:281 */     CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 134:282 */     if (!CompositeFormat.parseFixedstring(source, this.trimmedSuffix, pos)) {
/* 135:283 */       return null;
/* 136:    */     }
/* 137:286 */     return coordinates;
/* 138:    */   }
/* 139:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.VectorFormat
 * JD-Core Version:    0.7.0.1
 */
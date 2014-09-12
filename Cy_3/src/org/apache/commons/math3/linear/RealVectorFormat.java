/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Locale;
/*   9:    */ import org.apache.commons.math3.exception.MathParseException;
/*  10:    */ import org.apache.commons.math3.util.CompositeFormat;
/*  11:    */ 
/*  12:    */ public class RealVectorFormat
/*  13:    */ {
/*  14:    */   private static final String DEFAULT_PREFIX = "{";
/*  15:    */   private static final String DEFAULT_SUFFIX = "}";
/*  16:    */   private static final String DEFAULT_SEPARATOR = "; ";
/*  17:    */   private final String prefix;
/*  18:    */   private final String suffix;
/*  19:    */   private final String separator;
/*  20:    */   private final String trimmedPrefix;
/*  21:    */   private final String trimmedSuffix;
/*  22:    */   private final String trimmedSeparator;
/*  23:    */   private final NumberFormat format;
/*  24:    */   
/*  25:    */   public RealVectorFormat()
/*  26:    */   {
/*  27: 73 */     this("{", "}", "; ", CompositeFormat.getDefaultNumberFormat());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public RealVectorFormat(NumberFormat format)
/*  31:    */   {
/*  32: 82 */     this("{", "}", "; ", format);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public RealVectorFormat(String prefix, String suffix, String separator)
/*  36:    */   {
/*  37: 93 */     this(prefix, suffix, separator, CompositeFormat.getDefaultNumberFormat());
/*  38:    */   }
/*  39:    */   
/*  40:    */   public RealVectorFormat(String prefix, String suffix, String separator, NumberFormat format)
/*  41:    */   {
/*  42:107 */     this.prefix = prefix;
/*  43:108 */     this.suffix = suffix;
/*  44:109 */     this.separator = separator;
/*  45:110 */     this.trimmedPrefix = prefix.trim();
/*  46:111 */     this.trimmedSuffix = suffix.trim();
/*  47:112 */     this.trimmedSeparator = separator.trim();
/*  48:113 */     this.format = format;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static Locale[] getAvailableLocales()
/*  52:    */   {
/*  53:122 */     return NumberFormat.getAvailableLocales();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getPrefix()
/*  57:    */   {
/*  58:130 */     return this.prefix;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getSuffix()
/*  62:    */   {
/*  63:138 */     return this.suffix;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getSeparator()
/*  67:    */   {
/*  68:146 */     return this.separator;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public NumberFormat getFormat()
/*  72:    */   {
/*  73:154 */     return this.format;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static RealVectorFormat getInstance()
/*  77:    */   {
/*  78:162 */     return getInstance(Locale.getDefault());
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static RealVectorFormat getInstance(Locale locale)
/*  82:    */   {
/*  83:171 */     return new RealVectorFormat(CompositeFormat.getDefaultNumberFormat(locale));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String format(RealVector v)
/*  87:    */   {
/*  88:181 */     return format(v, new StringBuffer(), new FieldPosition(0)).toString();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public StringBuffer format(RealVector vector, StringBuffer toAppendTo, FieldPosition pos)
/*  92:    */   {
/*  93:195 */     pos.setBeginIndex(0);
/*  94:196 */     pos.setEndIndex(0);
/*  95:    */     
/*  96:    */ 
/*  97:199 */     toAppendTo.append(this.prefix);
/*  98:202 */     for (int i = 0; i < vector.getDimension(); i++)
/*  99:    */     {
/* 100:203 */       if (i > 0) {
/* 101:204 */         toAppendTo.append(this.separator);
/* 102:    */       }
/* 103:206 */       CompositeFormat.formatDouble(vector.getEntry(i), this.format, toAppendTo, pos);
/* 104:    */     }
/* 105:210 */     toAppendTo.append(this.suffix);
/* 106:    */     
/* 107:212 */     return toAppendTo;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public ArrayRealVector parse(String source)
/* 111:    */   {
/* 112:224 */     ParsePosition parsePosition = new ParsePosition(0);
/* 113:225 */     ArrayRealVector result = parse(source, parsePosition);
/* 114:226 */     if (parsePosition.getIndex() == 0) {
/* 115:227 */       throw new MathParseException(source, parsePosition.getErrorIndex(), ArrayRealVector.class);
/* 116:    */     }
/* 117:231 */     return result;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public ArrayRealVector parse(String source, ParsePosition pos)
/* 121:    */   {
/* 122:242 */     int initialIndex = pos.getIndex();
/* 123:    */     
/* 124:    */ 
/* 125:245 */     CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 126:246 */     if (!CompositeFormat.parseFixedstring(source, this.trimmedPrefix, pos)) {
/* 127:247 */       return null;
/* 128:    */     }
/* 129:251 */     List<Number> components = new ArrayList();
/* 130:252 */     for (boolean loop = true; loop;)
/* 131:    */     {
/* 132:254 */       if (!components.isEmpty())
/* 133:    */       {
/* 134:255 */         CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 135:256 */         if (!CompositeFormat.parseFixedstring(source, this.trimmedSeparator, pos)) {
/* 136:257 */           loop = false;
/* 137:    */         }
/* 138:    */       }
/* 139:261 */       if (loop)
/* 140:    */       {
/* 141:262 */         CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 142:263 */         Number component = CompositeFormat.parseNumber(source, this.format, pos);
/* 143:264 */         if (component != null)
/* 144:    */         {
/* 145:265 */           components.add(component);
/* 146:    */         }
/* 147:    */         else
/* 148:    */         {
/* 149:269 */           pos.setIndex(initialIndex);
/* 150:270 */           return null;
/* 151:    */         }
/* 152:    */       }
/* 153:    */     }
/* 154:277 */     CompositeFormat.parseAndIgnoreWhitespace(source, pos);
/* 155:278 */     if (!CompositeFormat.parseFixedstring(source, this.trimmedSuffix, pos)) {
/* 156:279 */       return null;
/* 157:    */     }
/* 158:283 */     double[] data = new double[components.size()];
/* 159:284 */     for (int i = 0; i < data.length; i++) {
/* 160:285 */       data[i] = ((Number)components.get(i)).doubleValue();
/* 161:    */     }
/* 162:287 */     return new ArrayRealVector(data, false);
/* 163:    */   }
/* 164:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.RealVectorFormat
 * JD-Core Version:    0.7.0.1
 */
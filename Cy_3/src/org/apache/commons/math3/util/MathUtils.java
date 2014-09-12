/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.exception.MathArithmeticException;
/*   5:    */ import org.apache.commons.math3.exception.NotFiniteNumberException;
/*   6:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.util.Localizable;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ 
/*  10:    */ public final class MathUtils
/*  11:    */ {
/*  12:    */   public static final double TWO_PI = 6.283185307179586D;
/*  13:    */   
/*  14:    */   public static int hash(double value)
/*  15:    */   {
/*  16: 57 */     return new Double(value).hashCode();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static int hash(double[] value)
/*  20:    */   {
/*  21: 68 */     return Arrays.hashCode(value);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static double normalizeAngle(double a, double center)
/*  25:    */   {
/*  26: 91 */     return a - 6.283185307179586D * FastMath.floor((a + 3.141592653589793D - center) / 6.283185307179586D);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static double reduce(double a, double period, double offset)
/*  30:    */   {
/*  31:113 */     double p = FastMath.abs(period);
/*  32:114 */     return a - p * FastMath.floor((a - offset) / p) - offset;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static byte copySign(byte magnitude, byte sign)
/*  36:    */   {
/*  37:128 */     if (((magnitude >= 0) && (sign >= 0)) || ((magnitude < 0) && (sign < 0))) {
/*  38:130 */       return magnitude;
/*  39:    */     }
/*  40:131 */     if ((sign >= 0) && (magnitude == -128)) {
/*  41:133 */       throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
/*  42:    */     }
/*  43:135 */     return (byte)-magnitude;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static short copySign(short magnitude, short sign)
/*  47:    */   {
/*  48:150 */     if (((magnitude >= 0) && (sign >= 0)) || ((magnitude < 0) && (sign < 0))) {
/*  49:152 */       return magnitude;
/*  50:    */     }
/*  51:153 */     if ((sign >= 0) && (magnitude == -32768)) {
/*  52:155 */       throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
/*  53:    */     }
/*  54:157 */     return (short)-magnitude;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static int copySign(int magnitude, int sign)
/*  58:    */   {
/*  59:172 */     if (((magnitude >= 0) && (sign >= 0)) || ((magnitude < 0) && (sign < 0))) {
/*  60:174 */       return magnitude;
/*  61:    */     }
/*  62:175 */     if ((sign >= 0) && (magnitude == -2147483648)) {
/*  63:177 */       throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
/*  64:    */     }
/*  65:179 */     return -magnitude;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static long copySign(long magnitude, long sign)
/*  69:    */   {
/*  70:194 */     if (((magnitude >= 0L) && (sign >= 0L)) || ((magnitude < 0L) && (sign < 0L))) {
/*  71:196 */       return magnitude;
/*  72:    */     }
/*  73:197 */     if ((sign >= 0L) && (magnitude == -9223372036854775808L)) {
/*  74:199 */       throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
/*  75:    */     }
/*  76:201 */     return -magnitude;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static void checkFinite(double x)
/*  80:    */   {
/*  81:212 */     if ((Double.isInfinite(x)) || (Double.isNaN(x))) {
/*  82:213 */       throw new NotFiniteNumberException(Double.valueOf(x), new Object[0]);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static void checkFinite(double[] val)
/*  87:    */   {
/*  88:225 */     for (int i = 0; i < val.length; i++)
/*  89:    */     {
/*  90:226 */       double x = val[i];
/*  91:227 */       if ((Double.isInfinite(x)) || (Double.isNaN(x))) {
/*  92:228 */         throw new NotFiniteNumberException(LocalizedFormats.ARRAY_ELEMENT, Double.valueOf(x), new Object[] { Integer.valueOf(i) });
/*  93:    */       }
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static void checkNotNull(Object o, Localizable pattern, Object... args)
/*  98:    */   {
/*  99:244 */     if (o == null) {
/* 100:245 */       throw new NullArgumentException(pattern, args);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static void checkNotNull(Object o)
/* 105:    */     throws NullArgumentException
/* 106:    */   {
/* 107:257 */     if (o == null) {
/* 108:258 */       throw new NullArgumentException();
/* 109:    */     }
/* 110:    */   }
/* 111:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.MathUtils
 * JD-Core Version:    0.7.0.1
 */
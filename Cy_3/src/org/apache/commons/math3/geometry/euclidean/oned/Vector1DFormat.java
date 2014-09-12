/*   1:    */ package org.apache.commons.math3.geometry.euclidean.oned;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.apache.commons.math3.exception.MathParseException;
/*   8:    */ import org.apache.commons.math3.geometry.Vector;
/*   9:    */ import org.apache.commons.math3.geometry.VectorFormat;
/*  10:    */ import org.apache.commons.math3.util.CompositeFormat;
/*  11:    */ 
/*  12:    */ public class Vector1DFormat
/*  13:    */   extends VectorFormat<Euclidean1D>
/*  14:    */ {
/*  15:    */   public Vector1DFormat()
/*  16:    */   {
/*  17: 52 */     super("{", "}", "; ", CompositeFormat.getDefaultNumberFormat());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Vector1DFormat(NumberFormat format)
/*  21:    */   {
/*  22: 61 */     super("{", "}", "; ", format);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Vector1DFormat(String prefix, String suffix)
/*  26:    */   {
/*  27: 70 */     super(prefix, suffix, "; ", CompositeFormat.getDefaultNumberFormat());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Vector1DFormat(String prefix, String suffix, NumberFormat format)
/*  31:    */   {
/*  32: 82 */     super(prefix, suffix, "; ", format);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static Vector1DFormat getInstance()
/*  36:    */   {
/*  37: 90 */     return getInstance(Locale.getDefault());
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static Vector1DFormat getInstance(Locale locale)
/*  41:    */   {
/*  42: 99 */     return new Vector1DFormat(CompositeFormat.getDefaultNumberFormat(locale));
/*  43:    */   }
/*  44:    */   
/*  45:    */   public StringBuffer format(Vector<Euclidean1D> vector, StringBuffer toAppendTo, FieldPosition pos)
/*  46:    */   {
/*  47:106 */     Vector1D p1 = (Vector1D)vector;
/*  48:107 */     return format(toAppendTo, pos, new double[] { p1.getX() });
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Vector1D parse(String source)
/*  52:    */   {
/*  53:113 */     ParsePosition parsePosition = new ParsePosition(0);
/*  54:114 */     Vector1D result = parse(source, parsePosition);
/*  55:115 */     if (parsePosition.getIndex() == 0) {
/*  56:116 */       throw new MathParseException(source, parsePosition.getErrorIndex(), Vector1D.class);
/*  57:    */     }
/*  58:120 */     return result;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Vector1D parse(String source, ParsePosition pos)
/*  62:    */   {
/*  63:126 */     double[] coordinates = parseCoordinates(1, source, pos);
/*  64:127 */     if (coordinates == null) {
/*  65:128 */       return null;
/*  66:    */     }
/*  67:130 */     return new Vector1D(coordinates[0]);
/*  68:    */   }
/*  69:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.oned.Vector1DFormat
 * JD-Core Version:    0.7.0.1
 */
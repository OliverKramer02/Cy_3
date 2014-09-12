/*   1:    */ package org.apache.commons.math3.geometry.euclidean.twod;
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
/*  12:    */ public class Vector2DFormat
/*  13:    */   extends VectorFormat<Euclidean2D>
/*  14:    */ {
/*  15:    */   public Vector2DFormat()
/*  16:    */   {
/*  17: 52 */     super("{", "}", "; ", CompositeFormat.getDefaultNumberFormat());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Vector2DFormat(NumberFormat format)
/*  21:    */   {
/*  22: 61 */     super("{", "}", "; ", format);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Vector2DFormat(String prefix, String suffix, String separator)
/*  26:    */   {
/*  27: 72 */     super(prefix, suffix, separator, CompositeFormat.getDefaultNumberFormat());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Vector2DFormat(String prefix, String suffix, String separator, NumberFormat format)
/*  31:    */   {
/*  32: 85 */     super(prefix, suffix, separator, format);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static Vector2DFormat getInstance()
/*  36:    */   {
/*  37: 93 */     return getInstance(Locale.getDefault());
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static Vector2DFormat getInstance(Locale locale)
/*  41:    */   {
/*  42:102 */     return new Vector2DFormat(CompositeFormat.getDefaultNumberFormat(locale));
/*  43:    */   }
/*  44:    */   
/*  45:    */   public StringBuffer format(Vector<Euclidean2D> vector, StringBuffer toAppendTo, FieldPosition pos)
/*  46:    */   {
/*  47:109 */     Vector2D p2 = (Vector2D)vector;
/*  48:110 */     return format(toAppendTo, pos, new double[] { p2.getX(), p2.getY() });
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Vector2D parse(String source)
/*  52:    */   {
/*  53:116 */     ParsePosition parsePosition = new ParsePosition(0);
/*  54:117 */     Vector2D result = parse(source, parsePosition);
/*  55:118 */     if (parsePosition.getIndex() == 0) {
/*  56:119 */       throw new MathParseException(source, parsePosition.getErrorIndex(), Vector2D.class);
/*  57:    */     }
/*  58:123 */     return result;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Vector2D parse(String source, ParsePosition pos)
/*  62:    */   {
/*  63:129 */     double[] coordinates = parseCoordinates(2, source, pos);
/*  64:130 */     if (coordinates == null) {
/*  65:131 */       return null;
/*  66:    */     }
/*  67:133 */     return new Vector2D(coordinates[0], coordinates[1]);
/*  68:    */   }
/*  69:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.Vector2DFormat
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.geometry.euclidean.threed;
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
/*  12:    */ public class Vector3DFormat
/*  13:    */   extends VectorFormat<Euclidean3D>
/*  14:    */ {
/*  15:    */   public Vector3DFormat()
/*  16:    */   {
/*  17: 51 */     super("{", "}", "; ", CompositeFormat.getDefaultNumberFormat());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Vector3DFormat(NumberFormat format)
/*  21:    */   {
/*  22: 60 */     super("{", "}", "; ", format);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Vector3DFormat(String prefix, String suffix, String separator)
/*  26:    */   {
/*  27: 71 */     super(prefix, suffix, separator, CompositeFormat.getDefaultNumberFormat());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Vector3DFormat(String prefix, String suffix, String separator, NumberFormat format)
/*  31:    */   {
/*  32: 84 */     super(prefix, suffix, separator, format);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static Vector3DFormat getInstance()
/*  36:    */   {
/*  37: 92 */     return getInstance(Locale.getDefault());
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static Vector3DFormat getInstance(Locale locale)
/*  41:    */   {
/*  42:101 */     return new Vector3DFormat(CompositeFormat.getDefaultNumberFormat(locale));
/*  43:    */   }
/*  44:    */   
/*  45:    */   public StringBuffer format(Vector<Euclidean3D> vector, StringBuffer toAppendTo, FieldPosition pos)
/*  46:    */   {
/*  47:115 */     Vector3D v3 = (Vector3D)vector;
/*  48:116 */     return format(toAppendTo, pos, new double[] { v3.getX(), v3.getY(), v3.getZ() });
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Vector3D parse(String source)
/*  52:    */   {
/*  53:128 */     ParsePosition parsePosition = new ParsePosition(0);
/*  54:129 */     Vector3D result = parse(source, parsePosition);
/*  55:130 */     if (parsePosition.getIndex() == 0) {
/*  56:131 */       throw new MathParseException(source, parsePosition.getErrorIndex(), Vector3D.class);
/*  57:    */     }
/*  58:135 */     return result;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Vector3D parse(String source, ParsePosition pos)
/*  62:    */   {
/*  63:146 */     double[] coordinates = parseCoordinates(3, source, pos);
/*  64:147 */     if (coordinates == null) {
/*  65:148 */       return null;
/*  66:    */     }
/*  67:150 */     return new Vector3D(coordinates[0], coordinates[1], coordinates[2]);
/*  68:    */   }
/*  69:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.threed.Vector3DFormat
 * JD-Core Version:    0.7.0.1
 */
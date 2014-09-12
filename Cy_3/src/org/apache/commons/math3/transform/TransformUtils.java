/*   1:    */ package org.apache.commons.math3.transform;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.math3.complex.Complex;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ 
/*   9:    */ public class TransformUtils
/*  10:    */ {
/*  11: 38 */   private static final int[] POWERS_OF_TWO = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824 };
/*  12:    */   
/*  13:    */   public static double[] scaleArray(double[] f, double d)
/*  14:    */   {
/*  15: 62 */     for (int i = 0; i < f.length; i++) {
/*  16: 63 */       f[i] *= d;
/*  17:    */     }
/*  18: 65 */     return f;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static Complex[] scaleArray(Complex[] f, double d)
/*  22:    */   {
/*  23: 78 */     for (int i = 0; i < f.length; i++) {
/*  24: 79 */       f[i] = new Complex(d * f[i].getReal(), d * f[i].getImaginary());
/*  25:    */     }
/*  26: 81 */     return f;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static double[][] createRealImaginaryArray(Complex[] dataC)
/*  30:    */   {
/*  31: 99 */     double[][] dataRI = new double[2][dataC.length];
/*  32:100 */     double[] dataR = dataRI[0];
/*  33:101 */     double[] dataI = dataRI[1];
/*  34:102 */     for (int i = 0; i < dataC.length; i++)
/*  35:    */     {
/*  36:103 */       Complex c = dataC[i];
/*  37:104 */       dataR[i] = c.getReal();
/*  38:105 */       dataI[i] = c.getImaginary();
/*  39:    */     }
/*  40:107 */     return dataRI;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static Complex[] createComplexArray(double[][] dataRI)
/*  44:    */     throws DimensionMismatchException
/*  45:    */   {
/*  46:128 */     if (dataRI.length != 2) {
/*  47:129 */       throw new DimensionMismatchException(dataRI.length, 2);
/*  48:    */     }
/*  49:131 */     double[] dataR = dataRI[0];
/*  50:132 */     double[] dataI = dataRI[1];
/*  51:133 */     if (dataR.length != dataI.length) {
/*  52:134 */       throw new DimensionMismatchException(dataI.length, dataR.length);
/*  53:    */     }
/*  54:137 */     int n = dataR.length;
/*  55:138 */     Complex[] c = new Complex[n];
/*  56:139 */     for (int i = 0; i < n; i++) {
/*  57:140 */       c[i] = new Complex(dataR[i], dataI[i]);
/*  58:    */     }
/*  59:142 */     return c;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static int exactLog2(int n)
/*  63:    */     throws MathIllegalArgumentException
/*  64:    */   {
/*  65:157 */     int index = Arrays.binarySearch(POWERS_OF_TWO, n);
/*  66:158 */     if (index < 0) {
/*  67:159 */       throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, new Object[] { Integer.valueOf(n) });
/*  68:    */     }
/*  69:163 */     return index;
/*  70:    */   }
/*  71:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.transform.TransformUtils
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.complex;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*   6:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   7:    */ import org.apache.commons.math3.exception.ZeroException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ 
/*  11:    */ public class RootsOfUnity
/*  12:    */   implements Serializable
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = 20120201L;
/*  15:    */   private int omegaCount;
/*  16:    */   private double[] omegaReal;
/*  17:    */   private double[] omegaImaginaryCounterClockwise;
/*  18:    */   private double[] omegaImaginaryClockwise;
/*  19:    */   private boolean isCounterClockWise;
/*  20:    */   
/*  21:    */   public RootsOfUnity()
/*  22:    */   {
/*  23: 71 */     this.omegaCount = 0;
/*  24: 72 */     this.omegaReal = null;
/*  25: 73 */     this.omegaImaginaryCounterClockwise = null;
/*  26: 74 */     this.omegaImaginaryClockwise = null;
/*  27: 75 */     this.isCounterClockWise = true;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public synchronized boolean isCounterClockWise()
/*  31:    */     throws MathIllegalStateException
/*  32:    */   {
/*  33: 91 */     if (this.omegaCount == 0) {
/*  34: 92 */       throw new MathIllegalStateException(LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new Object[0]);
/*  35:    */     }
/*  36: 95 */     return this.isCounterClockWise;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public synchronized void computeRoots(int n)
/*  40:    */     throws ZeroException
/*  41:    */   {
/*  42:119 */     if (n == 0) {
/*  43:120 */       throw new ZeroException(LocalizedFormats.CANNOT_COMPUTE_0TH_ROOT_OF_UNITY, new Object[0]);
/*  44:    */     }
/*  45:124 */     this.isCounterClockWise = (n > 0);
/*  46:    */     
/*  47:    */ 
/*  48:127 */     int absN = FastMath.abs(n);
/*  49:129 */     if (absN == this.omegaCount) {
/*  50:130 */       return;
/*  51:    */     }
/*  52:134 */     double t = 6.283185307179586D / absN;
/*  53:135 */     double cosT = FastMath.cos(t);
/*  54:136 */     double sinT = FastMath.sin(t);
/*  55:137 */     this.omegaReal = new double[absN];
/*  56:138 */     this.omegaImaginaryCounterClockwise = new double[absN];
/*  57:139 */     this.omegaImaginaryClockwise = new double[absN];
/*  58:140 */     this.omegaReal[0] = 1.0D;
/*  59:141 */     this.omegaImaginaryCounterClockwise[0] = 0.0D;
/*  60:142 */     this.omegaImaginaryClockwise[0] = 0.0D;
/*  61:143 */     for (int i = 1; i < absN; i++)
/*  62:    */     {
/*  63:144 */       this.omegaReal[i] = (this.omegaReal[(i - 1)] * cosT - this.omegaImaginaryCounterClockwise[(i - 1)] * sinT);
/*  64:    */       
/*  65:146 */       this.omegaImaginaryCounterClockwise[i] = (this.omegaReal[(i - 1)] * sinT + this.omegaImaginaryCounterClockwise[(i - 1)] * cosT);
/*  66:    */       
/*  67:148 */       this.omegaImaginaryClockwise[i] = (-this.omegaImaginaryCounterClockwise[i]);
/*  68:    */     }
/*  69:150 */     this.omegaCount = absN;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public synchronized double getReal(int k)
/*  73:    */     throws MathIllegalStateException, MathIllegalArgumentException
/*  74:    */   {
/*  75:165 */     if (this.omegaCount == 0) {
/*  76:166 */       throw new MathIllegalStateException(LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new Object[0]);
/*  77:    */     }
/*  78:169 */     if ((k < 0) || (k >= this.omegaCount)) {
/*  79:170 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_ROOT_OF_UNITY_INDEX, Integer.valueOf(k), Integer.valueOf(0), Integer.valueOf(this.omegaCount - 1));
/*  80:    */     }
/*  81:177 */     return this.omegaReal[k];
/*  82:    */   }
/*  83:    */   
/*  84:    */   public synchronized double getImaginary(int k)
/*  85:    */     throws MathIllegalStateException, OutOfRangeException
/*  86:    */   {
/*  87:192 */     if (this.omegaCount == 0) {
/*  88:193 */       throw new MathIllegalStateException(LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new Object[0]);
/*  89:    */     }
/*  90:196 */     if ((k < 0) || (k >= this.omegaCount)) {
/*  91:197 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_ROOT_OF_UNITY_INDEX, Integer.valueOf(k), Integer.valueOf(0), Integer.valueOf(this.omegaCount - 1));
/*  92:    */     }
/*  93:204 */     return this.isCounterClockWise ? this.omegaImaginaryCounterClockwise[k] : this.omegaImaginaryClockwise[k];
/*  94:    */   }
/*  95:    */   
/*  96:    */   public synchronized int getNumberOfRoots()
/*  97:    */   {
/*  98:217 */     return this.omegaCount;
/*  99:    */   }
/* 100:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.complex.RootsOfUnity
 * JD-Core Version:    0.7.0.1
 */
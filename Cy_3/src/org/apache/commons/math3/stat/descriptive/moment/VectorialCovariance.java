/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.linear.MatrixUtils;
/*   7:    */ import org.apache.commons.math3.linear.RealMatrix;
/*   8:    */ 
/*   9:    */ public class VectorialCovariance
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = 4118372414238930270L;
/*  13:    */   private final double[] sums;
/*  14:    */   private final double[] productsSums;
/*  15:    */   private final boolean isBiasCorrected;
/*  16:    */   private long n;
/*  17:    */   
/*  18:    */   public VectorialCovariance(int dimension, boolean isBiasCorrected)
/*  19:    */   {
/*  20: 54 */     this.sums = new double[dimension];
/*  21: 55 */     this.productsSums = new double[dimension * (dimension + 1) / 2];
/*  22: 56 */     this.n = 0L;
/*  23: 57 */     this.isBiasCorrected = isBiasCorrected;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void increment(double[] v)
/*  27:    */     throws DimensionMismatchException
/*  28:    */   {
/*  29: 66 */     if (v.length != this.sums.length) {
/*  30: 67 */       throw new DimensionMismatchException(v.length, this.sums.length);
/*  31:    */     }
/*  32: 69 */     int k = 0;
/*  33: 70 */     for (int i = 0; i < v.length; i++)
/*  34:    */     {
/*  35: 71 */       this.sums[i] += v[i];
/*  36: 72 */       for (int j = 0; j <= i; j++) {
/*  37: 73 */         this.productsSums[(k++)] += v[i] * v[j];
/*  38:    */       }
/*  39:    */     }
/*  40: 76 */     this.n += 1L;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public RealMatrix getResult()
/*  44:    */   {
/*  45: 85 */     int dimension = this.sums.length;
/*  46: 86 */     RealMatrix result = MatrixUtils.createRealMatrix(dimension, dimension);
/*  47: 88 */     if (this.n > 1L)
/*  48:    */     {
/*  49: 89 */       double c = 1.0D / (this.n * (this.isBiasCorrected ? this.n - 1L : this.n));
/*  50: 90 */       int k = 0;
/*  51: 91 */       for (int i = 0; i < dimension; i++) {
/*  52: 92 */         for (int j = 0; j <= i; j++)
/*  53:    */         {
/*  54: 93 */           double e = c * (this.n * this.productsSums[(k++)] - this.sums[i] * this.sums[j]);
/*  55: 94 */           result.setEntry(i, j, e);
/*  56: 95 */           result.setEntry(j, i, e);
/*  57:    */         }
/*  58:    */       }
/*  59:    */     }
/*  60:100 */     return result;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public long getN()
/*  64:    */   {
/*  65:109 */     return this.n;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void clear()
/*  69:    */   {
/*  70:116 */     this.n = 0L;
/*  71:117 */     Arrays.fill(this.sums, 0.0D);
/*  72:118 */     Arrays.fill(this.productsSums, 0.0D);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public int hashCode()
/*  76:    */   {
/*  77:124 */     int prime = 31;
/*  78:125 */     int result = 1;
/*  79:126 */     result = 31 * result + (this.isBiasCorrected ? 1231 : 1237);
/*  80:127 */     result = 31 * result + (int)(this.n ^ this.n >>> 32);
/*  81:128 */     result = 31 * result + Arrays.hashCode(this.productsSums);
/*  82:129 */     result = 31 * result + Arrays.hashCode(this.sums);
/*  83:130 */     return result;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean equals(Object obj)
/*  87:    */   {
/*  88:136 */     if (this == obj) {
/*  89:137 */       return true;
/*  90:    */     }
/*  91:139 */     if (!(obj instanceof VectorialCovariance)) {
/*  92:140 */       return false;
/*  93:    */     }
/*  94:142 */     VectorialCovariance other = (VectorialCovariance)obj;
/*  95:143 */     if (this.isBiasCorrected != other.isBiasCorrected) {
/*  96:144 */       return false;
/*  97:    */     }
/*  98:146 */     if (this.n != other.n) {
/*  99:147 */       return false;
/* 100:    */     }
/* 101:149 */     if (!Arrays.equals(this.productsSums, other.productsSums)) {
/* 102:150 */       return false;
/* 103:    */     }
/* 104:152 */     if (!Arrays.equals(this.sums, other.sums)) {
/* 105:153 */       return false;
/* 106:    */     }
/* 107:155 */     return true;
/* 108:    */   }
/* 109:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.VectorialCovariance
 * JD-Core Version:    0.7.0.1
 */
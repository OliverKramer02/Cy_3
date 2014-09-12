/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ 
/*   7:    */ public class VectorialMean
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 8223009086481006892L;
/*  11:    */   private final Mean[] means;
/*  12:    */   
/*  13:    */   public VectorialMean(int dimension)
/*  14:    */   {
/*  15: 41 */     this.means = new Mean[dimension];
/*  16: 42 */     for (int i = 0; i < dimension; i++) {
/*  17: 43 */       this.means[i] = new Mean();
/*  18:    */     }
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void increment(double[] v)
/*  22:    */   {
/*  23: 53 */     if (v.length != this.means.length) {
/*  24: 54 */       throw new DimensionMismatchException(v.length, this.means.length);
/*  25:    */     }
/*  26: 56 */     for (int i = 0; i < v.length; i++) {
/*  27: 57 */       this.means[i].increment(v[i]);
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public double[] getResult()
/*  32:    */   {
/*  33: 66 */     double[] result = new double[this.means.length];
/*  34: 67 */     for (int i = 0; i < result.length; i++) {
/*  35: 68 */       result[i] = this.means[i].getResult();
/*  36:    */     }
/*  37: 70 */     return result;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public long getN()
/*  41:    */   {
/*  42: 78 */     return this.means.length == 0 ? 0L : this.means[0].getN();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int hashCode()
/*  46:    */   {
/*  47: 84 */     int prime = 31;
/*  48: 85 */     int result = 1;
/*  49: 86 */     result = 31 * result + Arrays.hashCode(this.means);
/*  50: 87 */     return result;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean equals(Object obj)
/*  54:    */   {
/*  55: 93 */     if (this == obj) {
/*  56: 94 */       return true;
/*  57:    */     }
/*  58: 96 */     if (!(obj instanceof VectorialMean)) {
/*  59: 97 */       return false;
/*  60:    */     }
/*  61: 99 */     VectorialMean other = (VectorialMean)obj;
/*  62:100 */     if (!Arrays.equals(this.means, other.means)) {
/*  63:101 */       return false;
/*  64:    */     }
/*  65:103 */     return true;
/*  66:    */   }
/*  67:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.VectorialMean
 * JD-Core Version:    0.7.0.1
 */
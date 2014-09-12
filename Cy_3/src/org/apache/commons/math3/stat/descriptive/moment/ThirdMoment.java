/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.util.MathUtils;
/*   6:    */ 
/*   7:    */ class ThirdMoment
/*   8:    */   extends SecondMoment
/*   9:    */   implements Serializable
/*  10:    */ {
/*  11:    */   private static final long serialVersionUID = -7818711964045118679L;
/*  12:    */   protected double m3;
/*  13:    */   protected double nDevSq;
/*  14:    */   
/*  15:    */   public ThirdMoment()
/*  16:    */   {
/*  17: 70 */     this.m3 = (0.0D / 0.0D);
/*  18: 71 */     this.nDevSq = (0.0D / 0.0D);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ThirdMoment(ThirdMoment original)
/*  22:    */   {
/*  23: 81 */     copy(original, this);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void increment(double d)
/*  27:    */   {
/*  28: 89 */     if (this.n < 1L) {
/*  29: 90 */       this.m3 = (this.m2 = this.m1 = 0.0D);
/*  30:    */     }
/*  31: 93 */     double prevM2 = this.m2;
/*  32: 94 */     super.increment(d);
/*  33: 95 */     this.nDevSq = (this.nDev * this.nDev);
/*  34: 96 */     double n0 = this.n;
/*  35: 97 */     this.m3 = (this.m3 - 3.0D * this.nDev * prevM2 + (n0 - 1.0D) * (n0 - 2.0D) * this.nDevSq * this.dev);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public double getResult()
/*  39:    */   {
/*  40:105 */     return this.m3;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void clear()
/*  44:    */   {
/*  45:113 */     super.clear();
/*  46:114 */     this.m3 = (0.0D / 0.0D);
/*  47:115 */     this.nDevSq = (0.0D / 0.0D);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public ThirdMoment copy()
/*  51:    */   {
/*  52:123 */     ThirdMoment result = new ThirdMoment();
/*  53:124 */     copy(this, result);
/*  54:125 */     return result;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static void copy(ThirdMoment source, ThirdMoment dest)
/*  58:    */     throws NullArgumentException
/*  59:    */   {
/*  60:138 */     MathUtils.checkNotNull(source);
/*  61:139 */     MathUtils.checkNotNull(dest);
/*  62:140 */     SecondMoment.copy(source, dest);
/*  63:141 */     dest.m3 = source.m3;
/*  64:142 */     dest.nDevSq = source.nDevSq;
/*  65:    */   }
/*  66:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.ThirdMoment
 * JD-Core Version:    0.7.0.1
 */
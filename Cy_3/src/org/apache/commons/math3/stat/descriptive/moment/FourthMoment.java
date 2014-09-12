/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.util.MathUtils;
/*   6:    */ 
/*   7:    */ class FourthMoment
/*   8:    */   extends ThirdMoment
/*   9:    */   implements Serializable
/*  10:    */ {
/*  11:    */   private static final long serialVersionUID = 4763990447117157611L;
/*  12:    */   private double m4;
/*  13:    */   
/*  14:    */   public FourthMoment()
/*  15:    */   {
/*  16: 69 */     this.m4 = (0.0D / 0.0D);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public FourthMoment(FourthMoment original)
/*  20:    */   {
/*  21: 80 */     copy(original, this);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void increment(double d)
/*  25:    */   {
/*  26: 88 */     if (this.n < 1L)
/*  27:    */     {
/*  28: 89 */       this.m4 = 0.0D;
/*  29: 90 */       this.m3 = 0.0D;
/*  30: 91 */       this.m2 = 0.0D;
/*  31: 92 */       this.m1 = 0.0D;
/*  32:    */     }
/*  33: 95 */     double prevM3 = this.m3;
/*  34: 96 */     double prevM2 = this.m2;
/*  35:    */     
/*  36: 98 */     super.increment(d);
/*  37:    */     
/*  38:100 */     double n0 = this.n;
/*  39:    */     
/*  40:102 */     this.m4 = (this.m4 - 4.0D * this.nDev * prevM3 + 6.0D * this.nDevSq * prevM2 + (n0 * n0 - 3.0D * (n0 - 1.0D)) * (this.nDevSq * this.nDevSq * (n0 - 1.0D) * n0));
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double getResult()
/*  44:    */   {
/*  45:111 */     return this.m4;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void clear()
/*  49:    */   {
/*  50:119 */     super.clear();
/*  51:120 */     this.m4 = (0.0D / 0.0D);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public FourthMoment copy()
/*  55:    */   {
/*  56:128 */     FourthMoment result = new FourthMoment();
/*  57:129 */     copy(this, result);
/*  58:130 */     return result;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static void copy(FourthMoment source, FourthMoment dest)
/*  62:    */     throws NullArgumentException
/*  63:    */   {
/*  64:143 */     MathUtils.checkNotNull(source);
/*  65:144 */     MathUtils.checkNotNull(dest);
/*  66:145 */     ThirdMoment.copy(source, dest);
/*  67:146 */     dest.m4 = source.m4;
/*  68:    */   }
/*  69:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.FourthMoment
 * JD-Core Version:    0.7.0.1
 */
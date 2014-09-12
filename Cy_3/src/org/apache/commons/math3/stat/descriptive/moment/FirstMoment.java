/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   6:    */ import org.apache.commons.math3.util.MathUtils;
/*   7:    */ 
/*   8:    */ class FirstMoment
/*   9:    */   extends AbstractStorelessUnivariateStatistic
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = 6112755307178490473L;
/*  13:    */   protected long n;
/*  14:    */   protected double m1;
/*  15:    */   protected double dev;
/*  16:    */   protected double nDev;
/*  17:    */   
/*  18:    */   public FirstMoment()
/*  19:    */   {
/*  20: 80 */     this.n = 0L;
/*  21: 81 */     this.m1 = (0.0D / 0.0D);
/*  22: 82 */     this.dev = (0.0D / 0.0D);
/*  23: 83 */     this.nDev = (0.0D / 0.0D);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public FirstMoment(FirstMoment original)
/*  27:    */   {
/*  28: 94 */     copy(original, this);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void increment(double d)
/*  32:    */   {
/*  33:102 */     if (this.n == 0L) {
/*  34:103 */       this.m1 = 0.0D;
/*  35:    */     }
/*  36:105 */     this.n += 1L;
/*  37:106 */     double n0 = this.n;
/*  38:107 */     this.dev = (d - this.m1);
/*  39:108 */     this.nDev = (this.dev / n0);
/*  40:109 */     this.m1 += this.nDev;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void clear()
/*  44:    */   {
/*  45:117 */     this.m1 = (0.0D / 0.0D);
/*  46:118 */     this.n = 0L;
/*  47:119 */     this.dev = (0.0D / 0.0D);
/*  48:120 */     this.nDev = (0.0D / 0.0D);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getResult()
/*  52:    */   {
/*  53:128 */     return this.m1;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public long getN()
/*  57:    */   {
/*  58:135 */     return this.n;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public FirstMoment copy()
/*  62:    */   {
/*  63:143 */     FirstMoment result = new FirstMoment();
/*  64:144 */     copy(this, result);
/*  65:145 */     return result;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static void copy(FirstMoment source, FirstMoment dest)
/*  69:    */     throws NullArgumentException
/*  70:    */   {
/*  71:158 */     MathUtils.checkNotNull(source);
/*  72:159 */     MathUtils.checkNotNull(dest);
/*  73:160 */     dest.setData(source.getDataRef());
/*  74:161 */     dest.n = source.n;
/*  75:162 */     dest.m1 = source.m1;
/*  76:163 */     dest.dev = source.dev;
/*  77:164 */     dest.nDev = source.nDev;
/*  78:    */   }
/*  79:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.FirstMoment
 * JD-Core Version:    0.7.0.1
 */
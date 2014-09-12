/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.util.MathUtils;
/*   6:    */ 
/*   7:    */ public class SecondMoment
/*   8:    */   extends FirstMoment
/*   9:    */   implements Serializable
/*  10:    */ {
/*  11:    */   private static final long serialVersionUID = 3942403127395076445L;
/*  12:    */   protected double m2;
/*  13:    */   
/*  14:    */   public SecondMoment()
/*  15:    */   {
/*  16: 61 */     this.m2 = (0.0D / 0.0D);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public SecondMoment(SecondMoment original)
/*  20:    */   {
/*  21: 71 */     super(original);
/*  22: 72 */     this.m2 = original.m2;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void increment(double d)
/*  26:    */   {
/*  27: 80 */     if (this.n < 1L) {
/*  28: 81 */       this.m1 = (this.m2 = 0.0D);
/*  29:    */     }
/*  30: 83 */     super.increment(d);
/*  31: 84 */     this.m2 += (this.n - 1.0D) * this.dev * this.nDev;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void clear()
/*  35:    */   {
/*  36: 92 */     super.clear();
/*  37: 93 */     this.m2 = (0.0D / 0.0D);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public double getResult()
/*  41:    */   {
/*  42:101 */     return this.m2;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public SecondMoment copy()
/*  46:    */   {
/*  47:109 */     SecondMoment result = new SecondMoment();
/*  48:110 */     copy(this, result);
/*  49:111 */     return result;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static void copy(SecondMoment source, SecondMoment dest)
/*  53:    */     throws NullArgumentException
/*  54:    */   {
/*  55:124 */     MathUtils.checkNotNull(source);
/*  56:125 */     MathUtils.checkNotNull(dest);
/*  57:126 */     FirstMoment.copy(source, dest);
/*  58:127 */     dest.m2 = source.m2;
/*  59:    */   }
/*  60:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.SecondMoment
 * JD-Core Version:    0.7.0.1
 */
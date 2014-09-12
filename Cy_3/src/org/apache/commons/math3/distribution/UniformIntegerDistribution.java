/*   1:    */ package org.apache.commons.math3.distribution;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*   6:    */ 
/*   7:    */ public class UniformIntegerDistribution
/*   8:    */   extends AbstractIntegerDistribution
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 20120109L;
/*  11:    */   private final int lower;
/*  12:    */   private final int upper;
/*  13:    */   
/*  14:    */   public UniformIntegerDistribution(int lower, int upper)
/*  15:    */     throws NumberIsTooLargeException
/*  16:    */   {
/*  17: 51 */     if (lower >= upper) {
/*  18: 52 */       throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Integer.valueOf(lower), Integer.valueOf(upper), false);
/*  19:    */     }
/*  20: 56 */     this.lower = lower;
/*  21: 57 */     this.upper = upper;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public double probability(int x)
/*  25:    */   {
/*  26: 62 */     if ((x < this.lower) || (x > this.upper)) {
/*  27: 63 */       return 0.0D;
/*  28:    */     }
/*  29: 65 */     return 1.0D / (this.upper - this.lower + 1);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public double cumulativeProbability(int x)
/*  33:    */   {
/*  34: 70 */     if (x < this.lower) {
/*  35: 71 */       return 0.0D;
/*  36:    */     }
/*  37: 73 */     if (x > this.upper) {
/*  38: 74 */       return 1.0D;
/*  39:    */     }
/*  40: 76 */     return (x - this.lower + 1.0D) / (this.upper - this.lower + 1.0D);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double getNumericalMean()
/*  44:    */   {
/*  45: 86 */     return 0.5D * (this.lower + this.upper);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public double getNumericalVariance()
/*  49:    */   {
/*  50: 96 */     double n = this.upper - this.lower + 1;
/*  51: 97 */     return (n * n - 1.0D) / 12.0D;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getSupportLowerBound()
/*  55:    */   {
/*  56:109 */     return this.lower;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int getSupportUpperBound()
/*  60:    */   {
/*  61:121 */     return this.upper;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean isSupportConnected()
/*  65:    */   {
/*  66:132 */     return true;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int sample()
/*  70:    */   {
/*  71:138 */     return this.randomData.nextInt(this.lower, this.upper);
/*  72:    */   }
/*  73:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.distribution.UniformIntegerDistribution
 * JD-Core Version:    0.7.0.1
 */
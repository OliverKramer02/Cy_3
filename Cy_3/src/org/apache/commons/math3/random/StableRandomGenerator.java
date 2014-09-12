/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   4:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.util.FastMath;
/*   7:    */ 
/*   8:    */ public class StableRandomGenerator
/*   9:    */   implements NormalizedRandomGenerator
/*  10:    */ {
/*  11:    */   private final RandomGenerator generator;
/*  12:    */   private final double alpha;
/*  13:    */   private final double beta;
/*  14:    */   private final double zeta;
/*  15:    */   
/*  16:    */   public StableRandomGenerator(RandomGenerator generator, double alpha, double beta)
/*  17:    */   {
/*  18: 57 */     if (generator == null) {
/*  19: 58 */       throw new NullArgumentException();
/*  20:    */     }
/*  21: 61 */     if ((alpha <= 0.0D) || (alpha > 2.0D)) {
/*  22: 62 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_LEFT, Double.valueOf(alpha), Integer.valueOf(0), Integer.valueOf(2));
/*  23:    */     }
/*  24: 66 */     if ((beta < -1.0D) || (beta > 1.0D)) {
/*  25: 67 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_SIMPLE, Double.valueOf(beta), Integer.valueOf(-1), Integer.valueOf(1));
/*  26:    */     }
/*  27: 71 */     this.generator = generator;
/*  28: 72 */     this.alpha = alpha;
/*  29: 73 */     this.beta = beta;
/*  30: 74 */     if ((alpha < 2.0D) && (beta != 0.0D)) {
/*  31: 75 */       this.zeta = (beta * FastMath.tan(3.141592653589793D * alpha / 2.0D));
/*  32:    */     } else {
/*  33: 77 */       this.zeta = 0.0D;
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double nextNormalizedDouble()
/*  38:    */   {
/*  39: 88 */     double omega = -FastMath.log(this.generator.nextDouble());
/*  40: 89 */     double phi = 3.141592653589793D * (this.generator.nextDouble() - 0.5D);
/*  41: 92 */     if (this.alpha == 2.0D) {
/*  42: 93 */       return FastMath.sqrt(2.0D * omega) * FastMath.sin(phi);
/*  43:    */     }
/*  44:    */     double x;
/*  45:    */     
/*  46: 99 */     if (this.beta == 0.0D)
/*  47:    */     {
/*  48:    */      
/*  49:101 */       if (this.alpha == 1.0D) {
/*  50:102 */         x = FastMath.tan(phi);
/*  51:    */       } else {
/*  52:104 */         x = FastMath.pow(omega * FastMath.cos((1.0D - this.alpha) * phi), 1.0D / this.alpha - 1.0D) * FastMath.sin(this.alpha * phi) / FastMath.pow(FastMath.cos(phi), 1.0D / this.alpha);
/*  53:    */       }
/*  54:    */     }
/*  55:    */     else
/*  56:    */     {
/*  57:111 */       double cosPhi = FastMath.cos(phi);
/*  58:    */       
/*  59:113 */       if (FastMath.abs(this.alpha - 1.0D) > 1.0E-008D)
/*  60:    */       {
/*  61:114 */         double alphaPhi = this.alpha * phi;
/*  62:115 */         double invAlphaPhi = phi - alphaPhi;
/*  63:116 */         x = (FastMath.sin(alphaPhi) + this.zeta * FastMath.cos(alphaPhi)) / cosPhi * (FastMath.cos(invAlphaPhi) + this.zeta * FastMath.sin(invAlphaPhi)) / FastMath.pow(omega * cosPhi, (1.0D - this.alpha) / this.alpha);
/*  64:    */       }
/*  65:    */       else
/*  66:    */       {
/*  67:120 */         double betaPhi = 1.570796326794897D + this.beta * phi;
/*  68:121 */         x = 0.6366197723675814D * (betaPhi * FastMath.tan(phi) - this.beta * FastMath.log(1.570796326794897D * omega * cosPhi / betaPhi));
/*  69:124 */         if (this.alpha != 1.0D) {
/*  70:125 */           x += this.beta * FastMath.tan(3.141592653589793D * this.alpha / 2.0D);
/*  71:    */         }
/*  72:    */       }
/*  73:    */     }
/*  74:129 */     return x;
/*  75:    */   }
/*  76:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.StableRandomGenerator
 * JD-Core Version:    0.7.0.1
 */
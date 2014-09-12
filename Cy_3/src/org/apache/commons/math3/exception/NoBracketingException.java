/*   1:    */ package org.apache.commons.math3.exception;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.util.Localizable;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ 
/*   6:    */ public class NoBracketingException
/*   7:    */   extends MathIllegalArgumentException
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = -3629324471511904459L;
/*  10:    */   private final double lo;
/*  11:    */   private final double hi;
/*  12:    */   private final double fLo;
/*  13:    */   private final double fHi;
/*  14:    */   
/*  15:    */   public NoBracketingException(double lo, double hi, double fLo, double fHi)
/*  16:    */   {
/*  17: 51 */     this(LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, lo, hi, fLo, fHi, new Object[0]);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public NoBracketingException(Localizable specific, double lo, double hi, double fLo, double fHi, Object... args)
/*  21:    */   {
/*  22: 68 */     super(specific, new Object[] { Double.valueOf(lo), Double.valueOf(hi), Double.valueOf(fLo), Double.valueOf(fHi), args });
/*  23: 69 */     this.lo = lo;
/*  24: 70 */     this.hi = hi;
/*  25: 71 */     this.fLo = fLo;
/*  26: 72 */     this.fHi = fHi;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public double getLo()
/*  30:    */   {
/*  31: 81 */     return this.lo;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getHi()
/*  35:    */   {
/*  36: 89 */     return this.hi;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public double getFLo()
/*  40:    */   {
/*  41: 97 */     return this.fLo;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public double getFHi()
/*  45:    */   {
/*  46:105 */     return this.fHi;
/*  47:    */   }
/*  48:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NoBracketingException
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public abstract class AbstractRandomGenerator
/*   7:    */   implements RandomGenerator
/*   8:    */ {
/*   9: 45 */   private double cachedNormalDeviate = (0.0D / 0.0D);
/*  10:    */   
/*  11:    */   public void clear()
/*  12:    */   {
/*  13: 62 */     this.cachedNormalDeviate = (0.0D / 0.0D);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public void setSeed(int seed)
/*  17:    */   {
/*  18: 67 */     setSeed(seed);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setSeed(int[] seed)
/*  22:    */   {
/*  23: 73 */     long prime = 4294967291L;
/*  24:    */     
/*  25: 75 */     long combined = 0L;
/*  26: 76 */     for (int s : seed) {
/*  27: 77 */       combined = combined * 4294967291L + s;
/*  28:    */     }
/*  29: 79 */     setSeed(combined);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public abstract void setSeed(long paramLong);
/*  33:    */   
/*  34:    */   public void nextBytes(byte[] bytes)
/*  35:    */   {
/*  36:107 */     int bytesOut = 0;
/*  37:108 */     while (bytesOut < bytes.length)
/*  38:    */     {
/*  39:109 */       int randInt = nextInt();
/*  40:110 */       for (int i = 0; i < 3; i++)
/*  41:    */       {
/*  42:111 */         if (i > 0) {
/*  43:112 */           randInt >>= 8;
/*  44:    */         }
/*  45:114 */         bytes[(bytesOut++)] = ((byte)randInt);
/*  46:115 */         if (bytesOut == bytes.length) {
/*  47:116 */           return;
/*  48:    */         }
/*  49:    */       }
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int nextInt()
/*  54:    */   {
/*  55:137 */     return (int)((2.0D * nextDouble() - 1.0D) * 2147483647.0D);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int nextInt(int n)
/*  59:    */   {
/*  60:157 */     if (n <= 0) {
/*  61:158 */       throw new NotStrictlyPositiveException(Integer.valueOf(n));
/*  62:    */     }
/*  63:160 */     int result = (int)(nextDouble() * n);
/*  64:161 */     return result < n ? result : n - 1;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public long nextLong()
/*  68:    */   {
/*  69:179 */     return (long) ((2.0D * nextDouble() - 1.0D) * 9.223372036854776E+018D);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean nextBoolean()
/*  73:    */   {
/*  74:197 */     return nextDouble() <= 0.5D;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public float nextFloat()
/*  78:    */   {
/*  79:215 */     return (float)nextDouble();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public abstract double nextDouble();
/*  83:    */   
/*  84:    */   public double nextGaussian()
/*  85:    */   {
/*  86:253 */     if (!Double.isNaN(this.cachedNormalDeviate))
/*  87:    */     {
/*  88:254 */       double dev = this.cachedNormalDeviate;
/*  89:255 */       this.cachedNormalDeviate = (0.0D / 0.0D);
/*  90:256 */       return dev;
/*  91:    */     }
/*  92:258 */     double v1 = 0.0D;
/*  93:259 */     double v2 = 0.0D;
/*  94:260 */     double s = 1.0D;
/*  95:261 */     while (s >= 1.0D)
/*  96:    */     {
/*  97:262 */       v1 = 2.0D * nextDouble() - 1.0D;
/*  98:263 */       v2 = 2.0D * nextDouble() - 1.0D;
/*  99:264 */       s = v1 * v1 + v2 * v2;
/* 100:    */     }
/* 101:266 */     if (s != 0.0D) {
/* 102:267 */       s = FastMath.sqrt(-2.0D * FastMath.log(s) / s);
/* 103:    */     }
/* 104:269 */     this.cachedNormalDeviate = (v2 * s);
/* 105:270 */     return v1 * s;
/* 106:    */   }
/* 107:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.AbstractRandomGenerator
 * JD-Core Version:    0.7.0.1
 */
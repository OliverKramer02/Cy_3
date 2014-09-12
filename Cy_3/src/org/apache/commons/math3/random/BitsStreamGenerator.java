/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   4:    */ import org.apache.commons.math3.util.FastMath;
/*   5:    */ 
/*   6:    */ public abstract class BitsStreamGenerator
/*   7:    */   implements RandomGenerator
/*   8:    */ {
/*   9:    */   private double nextGaussian;
/*  10:    */   
/*  11:    */   public BitsStreamGenerator()
/*  12:    */   {
/*  13: 36 */     this.nextGaussian = (0.0D / 0.0D);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public abstract void setSeed(int paramInt);
/*  17:    */   
/*  18:    */   public abstract void setSeed(int[] paramArrayOfInt);
/*  19:    */   
/*  20:    */   public abstract void setSeed(long paramLong);
/*  21:    */   
/*  22:    */   protected abstract int next(int paramInt);
/*  23:    */   
/*  24:    */   public boolean nextBoolean()
/*  25:    */   {
/*  26: 61 */     return next(1) != 0;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void nextBytes(byte[] bytes)
/*  30:    */   {
/*  31: 66 */     int i = 0;
/*  32: 67 */     int iEnd = bytes.length - 3;
/*  33: 68 */     while (i < iEnd)
/*  34:    */     {
/*  35: 69 */       int random = next(32);
/*  36: 70 */       bytes[i] = ((byte)(random & 0xFF));
/*  37: 71 */       bytes[(i + 1)] = ((byte)(random >> 8 & 0xFF));
/*  38: 72 */       bytes[(i + 2)] = ((byte)(random >> 16 & 0xFF));
/*  39: 73 */       bytes[(i + 3)] = ((byte)(random >> 24 & 0xFF));
/*  40: 74 */       i += 4;
/*  41:    */     }
/*  42: 76 */     int random = next(32);
/*  43: 77 */     while (i < bytes.length)
/*  44:    */     {
/*  45: 78 */       bytes[(i++)] = ((byte)(random & 0xFF));
/*  46: 79 */       random >>= 8;
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double nextDouble()
/*  51:    */   {
/*  52: 85 */     long high = next(26) << 26;
/*  53: 86 */     int low = next(26);
/*  54: 87 */     return (high | low) * 2.220446049250313E-016D;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public float nextFloat()
/*  58:    */   {
/*  59: 92 */     return next(23) * 1.192093E-007F;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public double nextGaussian()
/*  63:    */   {
/*  64:    */     double random;
/*  65: 99 */     if (Double.isNaN(this.nextGaussian))
/*  66:    */     {
/*  67:101 */       double x = nextDouble();
/*  68:102 */       double y = nextDouble();
/*  69:103 */       double alpha = 6.283185307179586D * x;
/*  70:104 */       double r = FastMath.sqrt(-2.0D * FastMath.log(y));
/*  71:105 */       random = r * FastMath.cos(alpha);
/*  72:106 */       this.nextGaussian = (r * FastMath.sin(alpha));
/*  73:    */     }
/*  74:    */     else
/*  75:    */     {
/*  76:109 */       random = this.nextGaussian;
/*  77:110 */       this.nextGaussian = (0.0D / 0.0D);
/*  78:    */     }
/*  79:113 */     return random;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int nextInt()
/*  83:    */   {
/*  84:119 */     return next(32);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public int nextInt(int n)
/*  88:    */     throws IllegalArgumentException
/*  89:    */   {
/*  90:138 */     if (n > 0)
/*  91:    */     {
/*  92:139 */       if ((n & -n) == n) {
/*  93:140 */         return (int)(n * next(31) >> 31);
/*  94:    */       }
/*  95:    */       int bits;
/*  96:    */       int val;
/*  97:    */       do
/*  98:    */       {
/*  99:145 */         bits = next(31);
/* 100:146 */         val = bits % n;
/* 101:147 */       } while (bits - val + (n - 1) < 0);
/* 102:148 */       return val;
/* 103:    */     }
/* 104:150 */     throw new NotStrictlyPositiveException(Integer.valueOf(n));
/* 105:    */   }
/* 106:    */   
/* 107:    */   public long nextLong()
/* 108:    */   {
/* 109:155 */     long high = next(32) << 32;
/* 110:156 */     long low = next(32) & 0xFFFFFFFF;
/* 111:157 */     return high | low;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void clear()
/* 115:    */   {
/* 116:165 */     this.nextGaussian = (0.0D / 0.0D);
/* 117:    */   }
/* 118:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.BitsStreamGenerator
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import java.util.Random;
/*   4:    */ 
/*   5:    */ public class RandomAdaptor
/*   6:    */   extends Random
/*   7:    */   implements RandomGenerator
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 2306581345647615033L;
/*  10:    */   private final RandomGenerator randomGenerator;
/*  11:    */   
/*  12:    */   private RandomAdaptor()
/*  13:    */   {
/*  14: 40 */     this.randomGenerator = null;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public RandomAdaptor(RandomGenerator randomGenerator)
/*  18:    */   {
/*  19: 48 */     this.randomGenerator = randomGenerator;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static Random createAdaptor(RandomGenerator randomGenerator)
/*  23:    */   {
/*  24: 59 */     return new RandomAdaptor(randomGenerator);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean nextBoolean()
/*  28:    */   {
/*  29: 73 */     return this.randomGenerator.nextBoolean();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void nextBytes(byte[] bytes)
/*  33:    */   {
/*  34: 86 */     this.randomGenerator.nextBytes(bytes);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public double nextDouble()
/*  38:    */   {
/*  39:100 */     return this.randomGenerator.nextDouble();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public float nextFloat()
/*  43:    */   {
/*  44:114 */     return this.randomGenerator.nextFloat();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double nextGaussian()
/*  48:    */   {
/*  49:129 */     return this.randomGenerator.nextGaussian();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int nextInt()
/*  53:    */   {
/*  54:143 */     return this.randomGenerator.nextInt();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int nextInt(int n)
/*  58:    */   {
/*  59:159 */     return this.randomGenerator.nextInt(n);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public long nextLong()
/*  63:    */   {
/*  64:173 */     return this.randomGenerator.nextLong();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setSeed(int seed)
/*  68:    */   {
/*  69:178 */     if (this.randomGenerator != null) {
/*  70:179 */       this.randomGenerator.setSeed(seed);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setSeed(int[] seed)
/*  75:    */   {
/*  76:185 */     if (this.randomGenerator != null) {
/*  77:186 */       this.randomGenerator.setSeed(seed);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setSeed(long seed)
/*  82:    */   {
/*  83:193 */     if (this.randomGenerator != null) {
/*  84:194 */       this.randomGenerator.setSeed(seed);
/*  85:    */     }
/*  86:    */   }
/*  87:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.RandomAdaptor
 * JD-Core Version:    0.7.0.1
 */
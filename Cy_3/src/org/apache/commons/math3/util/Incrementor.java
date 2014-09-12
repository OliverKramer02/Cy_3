/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   4:    */ 
/*   5:    */ public class Incrementor
/*   6:    */ {
/*   7:    */   private int maximalCount;
/*   8: 40 */   private int count = 0;
/*   9:    */   private final MaxCountExceededCallback maxCountCallback;
/*  10:    */   
/*  11:    */   public Incrementor()
/*  12:    */   {
/*  13: 52 */     this(0);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public Incrementor(int max)
/*  17:    */   {
/*  18: 61 */     this(max, new MaxCountExceededCallback()
/*  19:    */     {
/*  20:    */       public void trigger(int max)
/*  21:    */       {
/*  22: 65 */         throw new MaxCountExceededException(Integer.valueOf(max));
/*  23:    */       }
/*  24:    */     });
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Incrementor(int max, MaxCountExceededCallback cb)
/*  28:    */   {
/*  29: 79 */     this.maximalCount = max;
/*  30: 80 */     this.maxCountCallback = cb;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setMaximalCount(int max)
/*  34:    */   {
/*  35: 91 */     this.maximalCount = max;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public int getMaximalCount()
/*  39:    */   {
/*  40:100 */     return this.maximalCount;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public int getCount()
/*  44:    */   {
/*  45:109 */     return this.count;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean canIncrement()
/*  49:    */   {
/*  50:120 */     return this.count < this.maximalCount;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void incrementCount(int value)
/*  54:    */   {
/*  55:131 */     for (int i = 0; i < value; i++) {
/*  56:132 */       incrementCount();
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void incrementCount()
/*  61:    */   {
/*  62:150 */     if (++this.count > this.maximalCount) {
/*  63:151 */       this.maxCountCallback.trigger(this.maximalCount);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void resetCount()
/*  68:    */   {
/*  69:159 */     this.count = 0;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static abstract interface MaxCountExceededCallback
/*  73:    */   {
/*  74:    */     public abstract void trigger(int paramInt);
/*  75:    */   }
/*  76:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.Incrementor
 * JD-Core Version:    0.7.0.1
 */
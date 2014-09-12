/*   1:    */ package org.apache.commons.math3.geometry.partitioning;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.geometry.Space;
/*   4:    */ 
/*   5:    */ public abstract interface SubHyperplane<S extends Space>
/*   6:    */ {
/*   7:    */   public abstract SubHyperplane<S> copySelf();
/*   8:    */   
/*   9:    */   public abstract Hyperplane<S> getHyperplane();
/*  10:    */   
/*  11:    */   public abstract boolean isEmpty();
/*  12:    */   
/*  13:    */   public abstract double getSize();
/*  14:    */   
/*  15:    */   public abstract Side side(Hyperplane<S> paramHyperplane);
/*  16:    */   
/*  17:    */   public abstract SplitSubHyperplane<S> split(Hyperplane<S> paramHyperplane);
/*  18:    */   
/*  19:    */   public abstract SubHyperplane<S> reunite(SubHyperplane<S> paramSubHyperplane);
/*  20:    */   
/*  21:    */   public static class SplitSubHyperplane<U extends Space>
/*  22:    */   {
/*  23:    */     private final SubHyperplane<U> plus;
/*  24:    */     private final SubHyperplane<U> minus;
/*  25:    */     
/*  26:    */     public SplitSubHyperplane(SubHyperplane<U> plus, SubHyperplane<U> minus)
/*  27:    */     {
/*  28:105 */       this.plus = plus;
/*  29:106 */       this.minus = minus;
/*  30:    */     }
/*  31:    */     
/*  32:    */     public SubHyperplane<U> getPlus()
/*  33:    */     {
/*  34:113 */       return this.plus;
/*  35:    */     }
/*  36:    */     
/*  37:    */     public SubHyperplane<U> getMinus()
/*  38:    */     {
/*  39:120 */       return this.minus;
/*  40:    */     }
/*  41:    */   }
/*  42:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.SubHyperplane
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.stat.descriptive.rank;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ 
/*  5:   */ public class Median
/*  6:   */   extends Percentile
/*  7:   */   implements Serializable
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -3961477041290915687L;
/* 10:   */   
/* 11:   */   public Median()
/* 12:   */   {
/* 13:42 */     super(50.0D);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Median(Median original)
/* 17:   */   {
/* 18:52 */     super(original);
/* 19:   */   }
/* 20:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.rank.Median
 * JD-Core Version:    0.7.0.1
 */
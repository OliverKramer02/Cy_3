/*  1:   */ package org.apache.commons.math3.geometry.partitioning;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.geometry.Space;
/*  4:   */ 
/*  5:   */ class Characterization<S extends Space>
/*  6:   */ {
/*  7:   */   private SubHyperplane<S> in;
/*  8:   */   private SubHyperplane<S> out;
/*  9:   */   
/* 10:   */   public Characterization()
/* 11:   */   {
/* 12:37 */     this.in = null;
/* 13:38 */     this.out = null;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean hasIn()
/* 17:   */   {
/* 18:45 */     return (this.in != null) && (!this.in.isEmpty());
/* 19:   */   }
/* 20:   */   
/* 21:   */   public SubHyperplane<S> getIn()
/* 22:   */   {
/* 23:52 */     return this.in;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean hasOut()
/* 27:   */   {
/* 28:59 */     return (this.out != null) && (!this.out.isEmpty());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public SubHyperplane<S> getOut()
/* 32:   */   {
/* 33:66 */     return this.out;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void add(SubHyperplane<S> sub, boolean inside)
/* 37:   */   {
/* 38:75 */     if (inside)
/* 39:   */     {
/* 40:76 */       if (this.in == null) {
/* 41:77 */         this.in = sub;
/* 42:   */       } else {
/* 43:79 */         this.in = this.in.reunite(sub);
/* 44:   */       }
/* 45:   */     }
/* 46:82 */     else if (this.out == null) {
/* 47:83 */       this.out = sub;
/* 48:   */     } else {
/* 49:85 */       this.out = this.out.reunite(sub);
/* 50:   */     }
/* 51:   */   }
/* 52:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.Characterization
 * JD-Core Version:    0.7.0.1
 */
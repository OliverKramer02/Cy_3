/*  1:   */ package org.apache.commons.math3.util;
/*  2:   */ 
/*  3:   */ import java.util.EventObject;
/*  4:   */ 
/*  5:   */ public class IterationEvent
/*  6:   */   extends EventObject
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 20120128L;
/*  9:   */   protected final int iterations;
/* 10:   */   
/* 11:   */   public IterationEvent(Object source, int iterations)
/* 12:   */   {
/* 13:43 */     super(source);
/* 14:44 */     this.iterations = iterations;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public int getIterations()
/* 18:   */   {
/* 19:54 */     return this.iterations;
/* 20:   */   }
/* 21:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.IterationEvent
 * JD-Core Version:    0.7.0.1
 */
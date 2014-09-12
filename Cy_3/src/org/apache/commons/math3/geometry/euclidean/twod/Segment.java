/*  1:   */ package org.apache.commons.math3.geometry.euclidean.twod;
/*  2:   */ 
/*  3:   */ public class Segment
/*  4:   */ {
/*  5:   */   private final Vector2D start;
/*  6:   */   private final Vector2D end;
/*  7:   */   protected final Line line;
/*  8:   */   
/*  9:   */   public Segment(Vector2D start, Vector2D end, Line line)
/* 10:   */   {
/* 11:41 */     this.start = start;
/* 12:42 */     this.end = end;
/* 13:43 */     this.line = line;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Vector2D getStart()
/* 17:   */   {
/* 18:50 */     return this.start;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Vector2D getEnd()
/* 22:   */   {
/* 23:57 */     return this.end;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Line getLine()
/* 27:   */   {
/* 28:64 */     return this.line;
/* 29:   */   }
/* 30:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.Segment
 * JD-Core Version:    0.7.0.1
 */
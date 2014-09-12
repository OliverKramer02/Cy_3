/*  1:   */ package org.apache.commons.math3.optimization.linear;
/*  2:   */ 
/*  3:   */ public enum Relationship
/*  4:   */ {
/*  5:28 */   EQ("="),  LEQ("<="),  GEQ(">=");
/*  6:   */   
/*  7:   */   private final String stringValue;
/*  8:   */   
/*  9:   */   private Relationship(String stringValue)
/* 10:   */   {
/* 11:43 */     this.stringValue = stringValue;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String toString()
/* 15:   */   {
/* 16:49 */     return this.stringValue;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Relationship oppositeRelationship()
/* 20:   */   {
/* 21:57 */     switch (ordinal())
/* 22:   */     {
/* 23:   */     case 1: 
/* 24:59 */       return GEQ;
/* 25:   */     case 2: 
/* 26:61 */       return LEQ;
/* 27:   */     }
/* 28:63 */     return EQ;
/* 29:   */   }
/* 30:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.Relationship
 * JD-Core Version:    0.7.0.1
 */
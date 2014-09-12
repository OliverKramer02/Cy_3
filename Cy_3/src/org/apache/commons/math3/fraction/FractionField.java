/*  1:   */ package org.apache.commons.math3.fraction;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.Field;
/*  5:   */ import org.apache.commons.math3.FieldElement;
/*  6:   */ 
/*  7:   */ public class FractionField
/*  8:   */   implements Field<Fraction>, Serializable
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = -1257768487499119313L;
/* 11:   */   
/* 12:   */   public static FractionField getInstance()
/* 13:   */   {
/* 14:48 */     return LazyHolder.INSTANCE;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Fraction getOne()
/* 18:   */   {
/* 19:53 */     return Fraction.ONE;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Fraction getZero()
/* 23:   */   {
/* 24:58 */     return Fraction.ZERO;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Class<? extends FieldElement<Fraction>> getRuntimeClass()
/* 28:   */   {
/* 29:63 */     return Fraction.class;
/* 30:   */   }
/* 31:   */   
/* 32:   */   private static class LazyHolder
/* 33:   */   {
/* 34:71 */     private static final FractionField INSTANCE = new FractionField();
/* 35:   */   }
/* 36:   */   
/* 37:   */   private Object readResolve()
/* 38:   */   {
/* 39:80 */     return LazyHolder.INSTANCE;
/* 40:   */   }
/* 41:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.fraction.FractionField
 * JD-Core Version:    0.7.0.1
 */
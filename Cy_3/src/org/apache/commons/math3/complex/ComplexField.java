/*  1:   */ package org.apache.commons.math3.complex;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.Field;
/*  5:   */ import org.apache.commons.math3.FieldElement;
/*  6:   */ 
/*  7:   */ public class ComplexField
/*  8:   */   implements Field<Complex>, Serializable
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = -6130362688700788798L;
/* 11:   */   
/* 12:   */   public static ComplexField getInstance()
/* 13:   */   {
/* 14:48 */     return LazyHolder.INSTANCE;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Complex getOne()
/* 18:   */   {
/* 19:53 */     return Complex.ONE;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Complex getZero()
/* 23:   */   {
/* 24:58 */     return Complex.ZERO;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Class<? extends FieldElement<Complex>> getRuntimeClass()
/* 28:   */   {
/* 29:63 */     return Complex.class;
/* 30:   */   }
/* 31:   */   
/* 32:   */   private static class LazyHolder
/* 33:   */   {
/* 34:72 */     private static final ComplexField INSTANCE = new ComplexField();
/* 35:   */   }
/* 36:   */   
/* 37:   */   private Object readResolve()
/* 38:   */   {
/* 39:81 */     return LazyHolder.INSTANCE;
/* 40:   */   }
/* 41:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.complex.ComplexField
 * JD-Core Version:    0.7.0.1
 */
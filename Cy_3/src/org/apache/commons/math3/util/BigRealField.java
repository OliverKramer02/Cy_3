/*  1:   */ package org.apache.commons.math3.util;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.commons.math3.Field;
/*  5:   */ import org.apache.commons.math3.FieldElement;
/*  6:   */ 
/*  7:   */ public class BigRealField
/*  8:   */   implements Field<BigReal>, Serializable
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 4756431066541037559L;
/* 11:   */   
/* 12:   */   public static BigRealField getInstance()
/* 13:   */   {
/* 14:48 */     return LazyHolder.INSTANCE;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public BigReal getOne()
/* 18:   */   {
/* 19:53 */     return BigReal.ONE;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public BigReal getZero()
/* 23:   */   {
/* 24:58 */     return BigReal.ZERO;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Class<? extends FieldElement<BigReal>> getRuntimeClass()
/* 28:   */   {
/* 29:63 */     return BigReal.class;
/* 30:   */   }
/* 31:   */   
/* 32:   */   private static class LazyHolder
/* 33:   */   {
/* 34:72 */     private static final BigRealField INSTANCE = new BigRealField();
/* 35:   */   }
/* 36:   */   
/* 37:   */   private Object readResolve()
/* 38:   */   {
/* 39:81 */     return LazyHolder.INSTANCE;
/* 40:   */   }
/* 41:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.BigRealField
 * JD-Core Version:    0.7.0.1
 */
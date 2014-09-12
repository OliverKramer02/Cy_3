/*  1:   */ package org.apache.commons.math3.exception.util;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ 
/*  6:   */ public class ArgUtils
/*  7:   */ {
/*  8:   */   public static Object[] flatten(Object[] array)
/*  9:   */   {
/* 10:42 */     List<Object> list = new ArrayList();
/* 11:43 */     if (array != null) {
/* 12:44 */       for (Object o : array) {
/* 13:45 */         if ((o instanceof Object[])) {
/* 14:46 */           for (Object oR : flatten((Object[])o)) {
/* 15:47 */             list.add(oR);
/* 16:   */           }
/* 17:   */         } else {
/* 18:50 */           list.add(o);
/* 19:   */         }
/* 20:   */       }
/* 21:   */     }
/* 22:54 */     return list.toArray();
/* 23:   */   }
/* 24:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.util.ArgUtils
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.exception.util;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ 
/*  5:   */ public class DummyLocalizable
/*  6:   */   implements Localizable
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 8843275624471387299L;
/*  9:   */   private final String source;
/* 10:   */   
/* 11:   */   public DummyLocalizable(String source)
/* 12:   */   {
/* 13:39 */     this.source = source;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getSourceString()
/* 17:   */   {
/* 18:44 */     return this.source;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getLocalizedString(Locale locale)
/* 22:   */   {
/* 23:49 */     return this.source;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String toString()
/* 27:   */   {
/* 28:55 */     return this.source;
/* 29:   */   }
/* 30:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.util.DummyLocalizable
 * JD-Core Version:    0.7.0.1
 */
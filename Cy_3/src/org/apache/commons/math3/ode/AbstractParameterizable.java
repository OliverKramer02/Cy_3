/*  1:   */ package org.apache.commons.math3.ode;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Collection;
/*  5:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ 
/*  8:   */ public abstract class AbstractParameterizable
/*  9:   */   implements Parameterizable
/* 10:   */ {
/* 11:   */   private final Collection<String> parametersNames;
/* 12:   */   
/* 13:   */   protected AbstractParameterizable(String... names)
/* 14:   */   {
/* 15:40 */     this.parametersNames = new ArrayList();
/* 16:41 */     for (String name : names) {
/* 17:42 */       this.parametersNames.add(name);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected AbstractParameterizable(Collection<String> names)
/* 22:   */   {
/* 23:50 */     this.parametersNames = new ArrayList();
/* 24:51 */     this.parametersNames.addAll(names);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Collection<String> getParametersNames()
/* 28:   */   {
/* 29:56 */     return this.parametersNames;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isSupported(String name)
/* 33:   */   {
/* 34:61 */     for (String supportedName : this.parametersNames) {
/* 35:62 */       if (supportedName.equals(name)) {
/* 36:63 */         return true;
/* 37:   */       }
/* 38:   */     }
/* 39:66 */     return false;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void complainIfNotSupported(String name)
/* 43:   */     throws MathIllegalArgumentException
/* 44:   */   {
/* 45:76 */     if (!isSupported(name)) {
/* 46:77 */       throw new MathIllegalArgumentException(LocalizedFormats.UNKNOWN_PARAMETER, new Object[] { name });
/* 47:   */     }
/* 48:   */   }
/* 49:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.AbstractParameterizable
 * JD-Core Version:    0.7.0.1
 */
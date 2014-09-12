/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.Set;
/*   9:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  10:    */ 
/*  11:    */ public class TransformerMap
/*  12:    */   implements NumberTransformer, Serializable
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = 4605318041528645258L;
/*  15: 42 */   private NumberTransformer defaultTransformer = null;
/*  16: 47 */   private Map<Class<?>, NumberTransformer> map = null;
/*  17:    */   
/*  18:    */   public TransformerMap()
/*  19:    */   {
/*  20: 53 */     this.map = new HashMap();
/*  21: 54 */     this.defaultTransformer = new DefaultTransformer();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public boolean containsClass(Class<?> key)
/*  25:    */   {
/*  26: 63 */     return this.map.containsKey(key);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean containsTransformer(NumberTransformer value)
/*  30:    */   {
/*  31: 72 */     return this.map.containsValue(value);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public NumberTransformer getTransformer(Class<?> key)
/*  35:    */   {
/*  36: 82 */     return (NumberTransformer)this.map.get(key);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public NumberTransformer putTransformer(Class<?> key, NumberTransformer transformer)
/*  40:    */   {
/*  41: 94 */     return (NumberTransformer)this.map.put(key, transformer);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public NumberTransformer removeTransformer(Class<?> key)
/*  45:    */   {
/*  46:104 */     return (NumberTransformer)this.map.remove(key);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void clear()
/*  50:    */   {
/*  51:111 */     this.map.clear();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Set<Class<?>> classes()
/*  55:    */   {
/*  56:119 */     return this.map.keySet();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Collection<NumberTransformer> transformers()
/*  60:    */   {
/*  61:128 */     return this.map.values();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public double transform(Object o)
/*  65:    */     throws MathIllegalArgumentException
/*  66:    */   {
/*  67:142 */     double value = (0.0D / 0.0D);
/*  68:144 */     if (((o instanceof Number)) || ((o instanceof String)))
/*  69:    */     {
/*  70:145 */       value = this.defaultTransformer.transform(o);
/*  71:    */     }
/*  72:    */     else
/*  73:    */     {
/*  74:147 */       NumberTransformer trans = getTransformer(o.getClass());
/*  75:148 */       if (trans != null) {
/*  76:149 */         value = trans.transform(o);
/*  77:    */       }
/*  78:    */     }
/*  79:153 */     return value;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean equals(Object other)
/*  83:    */   {
/*  84:159 */     if (this == other) {
/*  85:160 */       return true;
/*  86:    */     }
/*  87:162 */     if ((other instanceof TransformerMap))
/*  88:    */     {
/*  89:163 */       TransformerMap rhs = (TransformerMap)other;
/*  90:164 */       if (!this.defaultTransformer.equals(rhs.defaultTransformer)) {
/*  91:165 */         return false;
/*  92:    */       }
/*  93:167 */       if (this.map.size() != rhs.map.size()) {
/*  94:168 */         return false;
/*  95:    */       }
/*  96:170 */       for (Map.Entry<Class<?>, NumberTransformer> entry : this.map.entrySet()) {
/*  97:171 */         if (!((NumberTransformer)entry.getValue()).equals(rhs.map.get(entry.getKey()))) {
/*  98:172 */           return false;
/*  99:    */         }
/* 100:    */       }
/* 101:175 */       return true;
/* 102:    */     }
/* 103:177 */     return false;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public int hashCode()
/* 107:    */   {
/* 108:183 */     int hash = this.defaultTransformer.hashCode();
/* 109:184 */     for (NumberTransformer t : this.map.values()) {
/* 110:185 */       hash = hash * 31 + t.hashCode();
/* 111:    */     }
/* 112:187 */     return hash;
/* 113:    */   }
/* 114:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.TransformerMap
 * JD-Core Version:    0.7.0.1
 */
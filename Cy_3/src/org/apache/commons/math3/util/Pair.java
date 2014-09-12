/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ public class Pair<K, V>
/*   4:    */ {
/*   5:    */   private final K key;
/*   6:    */   private final V value;
/*   7:    */   
/*   8:    */   public Pair(K k, V v)
/*   9:    */   {
/*  10: 43 */     this.key = k;
/*  11: 44 */     this.value = v;
/*  12:    */   }
/*  13:    */   
/*  14:    */   public Pair(Pair<? extends K, ? extends V> entry)
/*  15:    */   {
/*  16: 53 */     this.key = entry.getKey();
/*  17: 54 */     this.value = entry.getValue();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public K getKey()
/*  21:    */   {
/*  22: 63 */     return this.key;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public V getValue()
/*  26:    */   {
/*  27: 72 */     return this.value;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public boolean equals(Object o)
/*  31:    */   {
/*  32: 84 */     if (o == null) {
/*  33: 85 */       return false;
/*  34:    */     }
/*  35: 87 */     if (!(o instanceof Pair)) {
/*  36: 88 */       return false;
/*  37:    */     }
/*  38: 90 */     Pair<?, ?> oP = (Pair)o;
/*  39: 91 */     return (this.key == null ? oP.getKey() == null : this.key.equals(oP.getKey())) && (this.value == null ? oP.getValue() == null : this.value.equals(oP.getValue()));
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int hashCode()
/*  43:    */   {
/*  44:107 */     return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
/*  45:    */   }
/*  46:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.Pair
 * JD-Core Version:    0.7.0.1
 */
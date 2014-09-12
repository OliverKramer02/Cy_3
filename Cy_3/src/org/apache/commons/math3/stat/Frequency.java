/*   1:    */ package org.apache.commons.math3.stat;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Comparator;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.Set;
/*   9:    */ import java.util.TreeMap;

/*  10:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ 
/*  13:    */ public class Frequency
/*  14:    */   implements Serializable
/*  15:    */ {
/*  16:    */   private static final long serialVersionUID = -3845586908418844111L;
/*  17:    */   private final TreeMap<Comparable<?>, Long> freqTable;
/*  18:    */   
/*  19:    */   public Frequency()
/*  20:    */   {
/*  21: 61 */     this.freqTable = new TreeMap();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Frequency(Comparator<?> comparator)
/*  25:    */   {
/*  26: 71 */     this.freqTable = new TreeMap(comparator);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String toString()
/*  30:    */   {
/*  31: 82 */     NumberFormat nf = NumberFormat.getPercentInstance();
/*  32: 83 */     StringBuilder outBuffer = new StringBuilder();
/*  33: 84 */     outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
/*  34: 85 */     Iterator<Comparable<?>> iter = this.freqTable.keySet().iterator();
/*  35: 86 */     while (iter.hasNext())
/*  36:    */     {
/*  37: 87 */       Comparable<?> value = (Comparable)iter.next();
/*  38: 88 */       outBuffer.append(value);
/*  39: 89 */       outBuffer.append('\t');
/*  40: 90 */       outBuffer.append(getCount(value));
/*  41: 91 */       outBuffer.append('\t');
/*  42: 92 */       outBuffer.append(nf.format(getPct(value)));
/*  43: 93 */       outBuffer.append('\t');
/*  44: 94 */       outBuffer.append(nf.format(getCumPct(value)));
/*  45: 95 */       outBuffer.append('\n');
/*  46:    */     }
/*  47: 97 */     return outBuffer.toString();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void addValue(Comparable<?> v)
/*  51:    */   {
/*  52:111 */     Comparable<?> obj = v;
/*  53:112 */     if ((v instanceof Integer)) {
/*  54:113 */       obj = Long.valueOf(((Integer)v).longValue());
/*  55:    */     }
/*  56:    */     try
/*  57:    */     {
/*  58:116 */       Long count = (Long)this.freqTable.get(obj);
/*  59:117 */       if (count == null) {
/*  60:118 */         this.freqTable.put(obj, Long.valueOf(1L));
/*  61:    */       } else {
/*  62:120 */         this.freqTable.put(obj, Long.valueOf(count.longValue() + 1L));
/*  63:    */       }
/*  64:    */     }
/*  65:    */     catch (ClassCastException ex)
/*  66:    */     {
/*  67:124 */       throw new MathIllegalArgumentException(LocalizedFormats.INSTANCES_NOT_COMPARABLE_TO_EXISTING_VALUES, new Object[] { v.getClass().getName() });
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void addValue(int v)
/*  72:    */   {
/*  73:136 */     addValue(Long.valueOf(v));
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void addValue(long v)
/*  77:    */   {
/*  78:145 */     addValue(Long.valueOf(v));
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void addValue(char v)
/*  82:    */   {
/*  83:154 */     addValue(Character.valueOf(v));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void clear()
/*  87:    */   {
/*  88:159 */     this.freqTable.clear();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Iterator<Comparable<?>> valuesIterator()
/*  92:    */   {
/*  93:172 */     return this.freqTable.keySet().iterator();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public long getSumFreq()
/*  97:    */   {
/*  98:183 */     long result = 0L;
/*  99:184 */     Iterator<Long> iterator = this.freqTable.values().iterator();
/* 100:185 */     while (iterator.hasNext()) {
/* 101:186 */       result += ((Long)iterator.next()).longValue();
/* 102:    */     }
/* 103:188 */     return result;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public long getCount(Comparable<?> v)
/* 107:    */   {
/* 108:199 */     if ((v instanceof Integer)) {
/* 109:200 */       return getCount(((Integer)v).longValue());
/* 110:    */     }
/* 111:202 */     long result = 0L;
/* 112:    */     try
/* 113:    */     {
/* 114:204 */       Long count = (Long)this.freqTable.get(v);
/* 115:205 */       if (count != null) {
/* 116:206 */         result = count.longValue();
/* 117:    */       }
/* 118:    */     }
/* 119:    */     catch (ClassCastException ex) {}
/* 120:211 */     return result;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public long getCount(int v)
/* 124:    */   {
/* 125:221 */     return getCount(Long.valueOf(v));
/* 126:    */   }
/* 127:    */   
/* 128:    */   public long getCount(long v)
/* 129:    */   {
/* 130:231 */     return getCount(Long.valueOf(v));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public long getCount(char v)
/* 134:    */   {
/* 135:241 */     return getCount(Character.valueOf(v));
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int getUniqueCount()
/* 139:    */   {
/* 140:251 */     return this.freqTable.keySet().size();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public double getPct(Comparable<?> v)
/* 144:    */   {
/* 145:264 */     long sumFreq = getSumFreq();
/* 146:265 */     if (sumFreq == 0L) {
/* 147:266 */       return (0.0D / 0.0D);
/* 148:    */     }
/* 149:268 */     return getCount(v) / sumFreq;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public double getPct(int v)
/* 153:    */   {
/* 154:279 */     return getPct(Long.valueOf(v));
/* 155:    */   }
/* 156:    */   
/* 157:    */   public double getPct(long v)
/* 158:    */   {
/* 159:290 */     return getPct(Long.valueOf(v));
/* 160:    */   }
/* 161:    */   
/* 162:    */   public double getPct(char v)
/* 163:    */   {
/* 164:301 */     return getPct(Character.valueOf(v));
/* 165:    */   }
/* 166:    */   
/* 167:    */   public long getCumFreq(Comparable<?> v)
/* 168:    */   {
/* 169:315 */     if (getSumFreq() == 0L) {
/* 170:316 */       return 0L;
/* 171:    */     }
/* 172:318 */     if ((v instanceof Integer)) {
/* 173:319 */       return getCumFreq(((Integer)v).longValue());
/* 174:    */     }
/* 175:322 */     Comparator<Comparable<?>> c = (Comparator<Comparable<?>>) this.freqTable.comparator();
/* 176:323 */     if (c == null) {
/* 177:324 */       c = new NaturalComparator();
/* 178:    */     }
/* 179:326 */     long result = 0L;
/* 180:    */     try
/* 181:    */     {
/* 182:329 */       Long value = (Long)this.freqTable.get(v);
/* 183:330 */       if (value != null) {
/* 184:331 */         result = value.longValue();
/* 185:    */       }
/* 186:    */     }
/* 187:    */     catch (ClassCastException ex)
/* 188:    */     {
/* 189:334 */       return result;
/* 190:    */     }
/* 191:337 */     if (c.compare(v, this.freqTable.firstKey()) < 0) {
/* 192:338 */       return 0L;
/* 193:    */     }
/* 194:341 */     if (c.compare(v, this.freqTable.lastKey()) >= 0) {
/* 195:342 */       return getSumFreq();
/* 196:    */     }
/* 197:345 */     Iterator<Comparable<?>> values = valuesIterator();
/* 198:346 */     while (values.hasNext())
/* 199:    */     {
/* 200:347 */       Comparable<?> nextValue = (Comparable)values.next();
/* 201:348 */       if (c.compare(v, nextValue) > 0) {
/* 202:349 */         result += getCount(nextValue);
/* 203:    */       } else {
/* 204:351 */         return result;
/* 205:    */       }
/* 206:    */     }
/* 207:354 */     return result;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public long getCumFreq(int v)
/* 211:    */   {
/* 212:366 */     return getCumFreq(Long.valueOf(v));
/* 213:    */   }
/* 214:    */   
/* 215:    */   public long getCumFreq(long v)
/* 216:    */   {
/* 217:378 */     return getCumFreq(Long.valueOf(v));
/* 218:    */   }
/* 219:    */   
/* 220:    */   public long getCumFreq(char v)
/* 221:    */   {
/* 222:390 */     return getCumFreq(Character.valueOf(v));
/* 223:    */   }
/* 224:    */   
/* 225:    */   public double getCumPct(Comparable<?> v)
/* 226:    */   {
/* 227:407 */     long sumFreq = getSumFreq();
/* 228:408 */     if (sumFreq == 0L) {
/* 229:409 */       return (0.0D / 0.0D);
/* 230:    */     }
/* 231:411 */     return getCumFreq(v) / sumFreq;
/* 232:    */   }
/* 233:    */   
/* 234:    */   public double getCumPct(int v)
/* 235:    */   {
/* 236:424 */     return getCumPct(Long.valueOf(v));
/* 237:    */   }
/* 238:    */   
/* 239:    */   public double getCumPct(long v)
/* 240:    */   {
/* 241:437 */     return getCumPct(Long.valueOf(v));
/* 242:    */   }
/* 243:    */   
/* 244:    */   public double getCumPct(char v)
/* 245:    */   {
/* 246:450 */     return getCumPct(Character.valueOf(v));
/* 247:    */   }
/* 248:    */   
/* 249:    */   private static class NaturalComparator<T extends Comparable<T>>
/* 250:    */     implements Comparator<Comparable<T>>, Serializable
/* 251:    */   {
/* 252:    */     private static final long serialVersionUID = -3852193713161395148L;
/* 253:    */     
/* 254:    */     public int compare(Comparable<T> o1, Comparable<T> o2)
/* 255:    */     {
/* 256:477 */       return o1.compareTo((T) o2);
/* 257:    */     }
/* 258:    */   }
/* 259:    */   
/* 260:    */   public int hashCode()
/* 261:    */   {
/* 262:484 */     int prime = 31;
/* 263:485 */     int result = 1;
/* 264:486 */     result = 31 * result + (this.freqTable == null ? 0 : this.freqTable.hashCode());
/* 265:    */     
/* 266:488 */     return result;
/* 267:    */   }
/* 268:    */   
/* 269:    */   public boolean equals(Object obj)
/* 270:    */   {
/* 271:494 */     if (this == obj) {
/* 272:495 */       return true;
/* 273:    */     }
/* 274:497 */     if (!(obj instanceof Frequency)) {
/* 275:498 */       return false;
/* 276:    */     }
/* 277:500 */     Frequency other = (Frequency)obj;
/* 278:501 */     if (this.freqTable == null)
/* 279:    */     {
/* 280:502 */       if (other.freqTable != null) {
/* 281:503 */         return false;
/* 282:    */       }
/* 283:    */     }
/* 284:505 */     else if (!this.freqTable.equals(other.freqTable)) {
/* 285:506 */       return false;
/* 286:    */     }
/* 287:508 */     return true;
/* 288:    */   }
/* 289:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.Frequency
 * JD-Core Version:    0.7.0.1
 */
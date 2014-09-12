/*   1:    */ package org.apache.commons.math3.stat.descriptive.rank;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic;
/*   9:    */ import org.apache.commons.math3.util.FastMath;
/*  10:    */ import org.apache.commons.math3.util.MathUtils;
/*  11:    */ 
/*  12:    */ public class Percentile
/*  13:    */   extends AbstractUnivariateStatistic
/*  14:    */   implements Serializable
/*  15:    */ {
/*  16:    */   private static final long serialVersionUID = -8091216485095130416L;
/*  17:    */   private static final int MIN_SELECT_SIZE = 15;
/*  18:    */   private static final int MAX_CACHED_LEVELS = 10;
/*  19: 96 */   private double quantile = 0.0D;
/*  20:    */   private int[] cachedPivots;
/*  21:    */   
/*  22:    */   public Percentile()
/*  23:    */   {
/*  24:106 */     this(50.0D);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Percentile(double p)
/*  28:    */   {
/*  29:116 */     setQuantile(p);
/*  30:117 */     this.cachedPivots = null;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Percentile(Percentile original)
/*  34:    */   {
/*  35:127 */     copy(original, this);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setData(double[] values)
/*  39:    */   {
/*  40:133 */     if (values == null)
/*  41:    */     {
/*  42:134 */       this.cachedPivots = null;
/*  43:    */     }
/*  44:    */     else
/*  45:    */     {
/*  46:136 */       this.cachedPivots = new int[1023];
/*  47:137 */       Arrays.fill(this.cachedPivots, -1);
/*  48:    */     }
/*  49:139 */     super.setData(values);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setData(double[] values, int begin, int length)
/*  53:    */   {
/*  54:145 */     if (values == null)
/*  55:    */     {
/*  56:146 */       this.cachedPivots = null;
/*  57:    */     }
/*  58:    */     else
/*  59:    */     {
/*  60:148 */       this.cachedPivots = new int[1023];
/*  61:149 */       Arrays.fill(this.cachedPivots, -1);
/*  62:    */     }
/*  63:151 */     super.setData(values, begin, length);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double evaluate(double p)
/*  67:    */   {
/*  68:163 */     return evaluate(getDataRef(), p);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public double evaluate(double[] values, double p)
/*  72:    */   {
/*  73:193 */     test(values, 0, 0);
/*  74:194 */     return evaluate(values, 0, values.length, p);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public double evaluate(double[] values, int start, int length)
/*  78:    */   {
/*  79:223 */     return evaluate(values, start, length, this.quantile);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public double evaluate(double[] values, int begin, int length, double p)
/*  83:    */   {
/*  84:259 */     test(values, begin, length);
/*  85:261 */     if ((p > 100.0D) || (p <= 0.0D)) {
/*  86:262 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(100));
/*  87:    */     }
/*  88:264 */     if (length == 0) {
/*  89:265 */       return (0.0D / 0.0D);
/*  90:    */     }
/*  91:267 */     if (length == 1) {
/*  92:268 */       return values[begin];
/*  93:    */     }
/*  94:270 */     double n = length;
/*  95:271 */     double pos = p * (n + 1.0D) / 100.0D;
/*  96:272 */     double fpos = FastMath.floor(pos);
/*  97:273 */     int intPos = (int)fpos;
/*  98:274 */     double dif = pos - fpos;
/*  99:    */     int[] pivotsHeap;
/* 100:    */     double[] work;
/* 101:    */    
/* 102:277 */     if (values == getDataRef())
/* 103:    */     {
/* 104:278 */       work = getDataRef();
/* 105:279 */       pivotsHeap = this.cachedPivots;
/* 106:    */     }
/* 107:    */     else
/* 108:    */     {
/* 109:281 */       work = new double[length];
/* 110:282 */       System.arraycopy(values, begin, work, 0, length);
/* 111:283 */       pivotsHeap = new int[1023];
/* 112:284 */       Arrays.fill(pivotsHeap, -1);
/* 113:    */     }
/* 114:287 */     if (pos < 1.0D) {
/* 115:288 */       return select(work, pivotsHeap, 0);
/* 116:    */     }
/* 117:290 */     if (pos >= n) {
/* 118:291 */       return select(work, pivotsHeap, length - 1);
/* 119:    */     }
/* 120:293 */     double lower = select(work, pivotsHeap, intPos - 1);
/* 121:294 */     double upper = select(work, pivotsHeap, intPos);
/* 122:295 */     return lower + dif * (upper - lower);
/* 123:    */   }
/* 124:    */   
/* 125:    */   private double select(double[] work, int[] pivotsHeap, int k)
/* 126:    */   {
/* 127:311 */     int begin = 0;
/* 128:312 */     int end = work.length;
/* 129:313 */     int node = 0;
/* 130:315 */     while (end - begin > 15)
/* 131:    */     {
/* 132:    */       int pivot;
/* 133:    */      
/* 134:318 */       if ((node < pivotsHeap.length) && (pivotsHeap[node] >= 0))
/* 135:    */       {
/* 136:321 */         pivot = pivotsHeap[node];
/* 137:    */       }
/* 138:    */       else
/* 139:    */       {
/* 140:324 */         pivot = partition(work, begin, end, medianOf3(work, begin, end));
/* 141:325 */         if (node < pivotsHeap.length) {
/* 142:326 */           pivotsHeap[node] = pivot;
/* 143:    */         }
/* 144:    */       }
/* 145:330 */       if (k == pivot) {
/* 146:332 */         return work[k];
/* 147:    */       }
/* 148:333 */       if (k < pivot)
/* 149:    */       {
/* 150:335 */         end = pivot;
/* 151:336 */         node = Math.min(2 * node + 1, pivotsHeap.length);
/* 152:    */       }
/* 153:    */       else
/* 154:    */       {
/* 155:339 */         begin = pivot + 1;
/* 156:340 */         node = Math.min(2 * node + 2, pivotsHeap.length);
/* 157:    */       }
/* 158:    */     }
/* 159:347 */     insertionSort(work, begin, end);
/* 160:348 */     return work[k];
/* 161:    */   }
/* 162:    */   
/* 163:    */   int medianOf3(double[] work, int begin, int end)
/* 164:    */   {
/* 165:361 */     int inclusiveEnd = end - 1;
/* 166:362 */     int middle = begin + (inclusiveEnd - begin) / 2;
/* 167:363 */     double wBegin = work[begin];
/* 168:364 */     double wMiddle = work[middle];
/* 169:365 */     double wEnd = work[inclusiveEnd];
/* 170:367 */     if (wBegin < wMiddle)
/* 171:    */     {
/* 172:368 */       if (wMiddle < wEnd) {
/* 173:369 */         return middle;
/* 174:    */       }
/* 175:371 */       return wBegin < wEnd ? inclusiveEnd : begin;
/* 176:    */     }
/* 177:374 */     if (wBegin < wEnd) {
/* 178:375 */       return begin;
/* 179:    */     }
/* 180:377 */     return wMiddle < wEnd ? inclusiveEnd : middle;
/* 181:    */   }
/* 182:    */   
/* 183:    */   private int partition(double[] work, int begin, int end, int pivot)
/* 184:    */   {
/* 185:398 */     double value = work[pivot];
/* 186:399 */     work[pivot] = work[begin];
/* 187:    */     
/* 188:401 */     int i = begin + 1;
/* 189:402 */     int j = end - 1;
/* 190:403 */     while (i < j)
/* 191:    */     {
/* 192:404 */       while ((i < j) && (work[j] >= value)) {
/* 193:405 */         j--;
/* 194:    */       }
/* 195:407 */       while ((i < j) && (work[i] <= value)) {
/* 196:408 */         i++;
/* 197:    */       }
/* 198:411 */       if (i < j)
/* 199:    */       {
/* 200:412 */         double tmp = work[i];
/* 201:413 */         work[(i++)] = work[j];
/* 202:414 */         work[(j--)] = tmp;
/* 203:    */       }
/* 204:    */     }
/* 205:418 */     if ((i >= end) || (work[i] > value)) {
/* 206:419 */       i--;
/* 207:    */     }
/* 208:421 */     work[begin] = work[i];
/* 209:422 */     work[i] = value;
/* 210:423 */     return i;
/* 211:    */   }
/* 212:    */   
/* 213:    */   private void insertionSort(double[] work, int begin, int end)
/* 214:    */   {
/* 215:434 */     for (int j = begin + 1; j < end; j++)
/* 216:    */     {
/* 217:435 */       double saved = work[j];
/* 218:436 */       int i = j - 1;
/* 219:437 */       while ((i >= begin) && (saved < work[i]))
/* 220:    */       {
/* 221:438 */         work[(i + 1)] = work[i];
/* 222:439 */         i--;
/* 223:    */       }
/* 224:441 */       work[(i + 1)] = saved;
/* 225:    */     }
/* 226:    */   }
/* 227:    */   
/* 228:    */   public double getQuantile()
/* 229:    */   {
/* 230:452 */     return this.quantile;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setQuantile(double p)
/* 234:    */   {
/* 235:464 */     if ((p <= 0.0D) || (p > 100.0D)) {
/* 236:465 */       throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(100));
/* 237:    */     }
/* 238:467 */     this.quantile = p;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public Percentile copy()
/* 242:    */   {
/* 243:475 */     Percentile result = new Percentile();
/* 244:476 */     copy(this, result);
/* 245:477 */     return result;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public static void copy(Percentile source, Percentile dest)
/* 249:    */     throws NullArgumentException
/* 250:    */   {
/* 251:490 */     MathUtils.checkNotNull(source);
/* 252:491 */     MathUtils.checkNotNull(dest);
/* 253:492 */     dest.setData(source.getDataRef());
/* 254:493 */     if (source.cachedPivots != null) {
/* 255:494 */       System.arraycopy(source.cachedPivots, 0, dest.cachedPivots, 0, source.cachedPivots.length);
/* 256:    */     }
/* 257:496 */     dest.quantile = source.quantile;
/* 258:    */   }
/* 259:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.rank.Percentile
 * JD-Core Version:    0.7.0.1
 */
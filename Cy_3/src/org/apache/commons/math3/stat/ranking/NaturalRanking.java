/*   1:    */ package org.apache.commons.math3.stat.ranking;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;

/*   7:    */ import org.apache.commons.math3.exception.MathInternalError;
/*   8:    */ import org.apache.commons.math3.random.RandomData;
/*   9:    */ import org.apache.commons.math3.random.RandomDataImpl;
/*  10:    */ import org.apache.commons.math3.random.RandomGenerator;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public class NaturalRanking
/*  14:    */   implements RankingAlgorithm
/*  15:    */ {
/*  16: 74 */   public static final NaNStrategy DEFAULT_NAN_STRATEGY = NaNStrategy.MAXIMAL;
/*  17: 77 */   public static final TiesStrategy DEFAULT_TIES_STRATEGY = TiesStrategy.AVERAGE;
/*  18:    */   private final NaNStrategy nanStrategy;
/*  19:    */   private final TiesStrategy tiesStrategy;
/*  20:    */   private final RandomData randomData;
/*  21:    */   
/*  22:    */   public NaturalRanking()
/*  23:    */   {
/*  24: 93 */     this.tiesStrategy = DEFAULT_TIES_STRATEGY;
/*  25: 94 */     this.nanStrategy = DEFAULT_NAN_STRATEGY;
/*  26: 95 */     this.randomData = null;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public NaturalRanking(TiesStrategy tiesStrategy)
/*  30:    */   {
/*  31:105 */     this.tiesStrategy = tiesStrategy;
/*  32:106 */     this.nanStrategy = DEFAULT_NAN_STRATEGY;
/*  33:107 */     this.randomData = new RandomDataImpl();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public NaturalRanking(NaNStrategy nanStrategy)
/*  37:    */   {
/*  38:117 */     this.nanStrategy = nanStrategy;
/*  39:118 */     this.tiesStrategy = DEFAULT_TIES_STRATEGY;
/*  40:119 */     this.randomData = null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public NaturalRanking(NaNStrategy nanStrategy, TiesStrategy tiesStrategy)
/*  44:    */   {
/*  45:130 */     this.nanStrategy = nanStrategy;
/*  46:131 */     this.tiesStrategy = tiesStrategy;
/*  47:132 */     this.randomData = new RandomDataImpl();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public NaturalRanking(RandomGenerator randomGenerator)
/*  51:    */   {
/*  52:143 */     this.tiesStrategy = TiesStrategy.RANDOM;
/*  53:144 */     this.nanStrategy = DEFAULT_NAN_STRATEGY;
/*  54:145 */     this.randomData = new RandomDataImpl(randomGenerator);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public NaturalRanking(NaNStrategy nanStrategy, RandomGenerator randomGenerator)
/*  58:    */   {
/*  59:159 */     this.nanStrategy = nanStrategy;
/*  60:160 */     this.tiesStrategy = TiesStrategy.RANDOM;
/*  61:161 */     this.randomData = new RandomDataImpl(randomGenerator);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public NaNStrategy getNanStrategy()
/*  65:    */   {
/*  66:170 */     return this.nanStrategy;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public TiesStrategy getTiesStrategy()
/*  70:    */   {
/*  71:179 */     return this.tiesStrategy;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public double[] rank(double[] data)
/*  75:    */   {
/*  76:193 */     IntDoublePair[] ranks = new IntDoublePair[data.length];
/*  77:194 */     for (int i = 0; i < data.length; i++) {
/*  78:195 */       ranks[i] = new IntDoublePair(data[i], i);
/*  79:    */     }
/*  80:199 */     List<Integer> nanPositions = null;
/*  81:200 */     switch (this.nanStrategy.ordinal())
/*  82:    */     {
/*  83:    */     case 1: 
/*  84:202 */       recodeNaNs(ranks, (1.0D / 0.0D));
/*  85:203 */       break;
/*  86:    */     case 2: 
/*  87:205 */       recodeNaNs(ranks, (-1.0D / 0.0D));
/*  88:206 */       break;
/*  89:    */     case 3: 
/*  90:208 */       ranks = removeNaNs(ranks);
/*  91:209 */       break;
/*  92:    */     case 4: 
/*  93:211 */       nanPositions = getNanPositions(ranks);
/*  94:212 */       break;
/*  95:    */     default: 
/*  96:214 */       throw new MathInternalError();
/*  97:    */     }
/*  98:218 */     Arrays.sort(ranks);
/*  99:    */     
/* 100:    */ 
/* 101:    */ 
/* 102:222 */     double[] out = new double[ranks.length];
/* 103:223 */     int pos = 1;
/* 104:224 */     out[ranks[0].getPosition()] = pos;
/* 105:225 */     List<Integer> tiesTrace = new ArrayList();
/* 106:226 */     tiesTrace.add(Integer.valueOf(ranks[0].getPosition()));
/* 107:227 */     for (int i = 1; i < ranks.length; i++)
/* 108:    */     {
/* 109:228 */       if (Double.compare(ranks[i].getValue(), ranks[(i - 1)].getValue()) > 0)
/* 110:    */       {
/* 111:230 */         pos = i + 1;
/* 112:231 */         if (tiesTrace.size() > 1) {
/* 113:232 */           resolveTie(out, tiesTrace);
/* 114:    */         }
/* 115:234 */         tiesTrace = new ArrayList();
/* 116:235 */         tiesTrace.add(Integer.valueOf(ranks[i].getPosition()));
/* 117:    */       }
/* 118:    */       else
/* 119:    */       {
/* 120:238 */         tiesTrace.add(Integer.valueOf(ranks[i].getPosition()));
/* 121:    */       }
/* 122:240 */       out[ranks[i].getPosition()] = pos;
/* 123:    */     }
/* 124:242 */     if (tiesTrace.size() > 1) {
/* 125:243 */       resolveTie(out, tiesTrace);
/* 126:    */     }
/* 127:245 */     if (this.nanStrategy == NaNStrategy.FIXED) {
/* 128:246 */       restoreNaNs(out, nanPositions);
/* 129:    */     }
/* 130:248 */     return out;
/* 131:    */   }
/* 132:    */   
/* 133:    */   private IntDoublePair[] removeNaNs(IntDoublePair[] ranks)
/* 134:    */   {
/* 135:259 */     if (!containsNaNs(ranks)) {
/* 136:260 */       return ranks;
/* 137:    */     }
/* 138:262 */     IntDoublePair[] outRanks = new IntDoublePair[ranks.length];
/* 139:263 */     int j = 0;
/* 140:264 */     for (int i = 0; i < ranks.length; i++) {
/* 141:265 */       if (Double.isNaN(ranks[i].getValue()))
/* 142:    */       {
/* 143:267 */         for (int k = i + 1; k < ranks.length; k++) {
/* 144:268 */           ranks[k] = new IntDoublePair(ranks[k].getValue(), ranks[k].getPosition() - 1);
/* 145:    */         }
/* 146:    */       }
/* 147:    */       else
/* 148:    */       {
/* 149:272 */         outRanks[j] = new IntDoublePair(ranks[i].getValue(), ranks[i].getPosition());
/* 150:    */         
/* 151:274 */         j++;
/* 152:    */       }
/* 153:    */     }
/* 154:277 */     IntDoublePair[] returnRanks = new IntDoublePair[j];
/* 155:278 */     System.arraycopy(outRanks, 0, returnRanks, 0, j);
/* 156:279 */     return returnRanks;
/* 157:    */   }
/* 158:    */   
/* 159:    */   private void recodeNaNs(IntDoublePair[] ranks, double value)
/* 160:    */   {
/* 161:289 */     for (int i = 0; i < ranks.length; i++) {
/* 162:290 */       if (Double.isNaN(ranks[i].getValue())) {
/* 163:291 */         ranks[i] = new IntDoublePair(value, ranks[i].getPosition());
/* 164:    */       }
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   private boolean containsNaNs(IntDoublePair[] ranks)
/* 169:    */   {
/* 170:304 */     for (int i = 0; i < ranks.length; i++) {
/* 171:305 */       if (Double.isNaN(ranks[i].getValue())) {
/* 172:306 */         return true;
/* 173:    */       }
/* 174:    */     }
/* 175:309 */     return false;
/* 176:    */   }
/* 177:    */   
/* 178:    */   private void resolveTie(double[] ranks, List<Integer> tiesTrace)
/* 179:    */   {
/* 180:329 */     double c = ranks[((Integer)tiesTrace.get(0)).intValue()];
/* 181:    */     
/* 182:    */ 
/* 183:332 */     int length = tiesTrace.size();
/* 184:    */     Iterator<Integer> iterator = null;
/* 185:    */     long f = 0;
/* 186:334 */     switch (this.tiesStrategy.ordinal())
/* 187:    */     {
/* 188:    */     case 1: 
/* 189:336 */       fill(ranks, tiesTrace, (2.0D * c + length - 1.0D) / 2.0D);
/* 190:337 */       break;
/* 191:    */     case 2: 
/* 192:339 */       fill(ranks, tiesTrace, c + length - 1.0D);
/* 193:340 */       break;
/* 194:    */     case 3: 
/* 195:342 */       fill(ranks, tiesTrace, c);
/* 196:343 */       break;
/* 197:    */     case 4: 
/* 198:345 */       iterator = tiesTrace.iterator();
/* 199:346 */       f = FastMath.round(c);
/* 200:    */     case 5: 
/* 201:    */     default: 
/* 202:347 */       while (iterator.hasNext())
/* 203:    */       {
/* 204:348 */         ranks[((Integer)iterator.next()).intValue()] = this.randomData.nextLong(f, f + length - 1L); continue;       }
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   private void fill(double[] data, List<Integer> tiesTrace, double value)
/* 226:    */   {
/* 227:374 */     Iterator<Integer> iterator = tiesTrace.iterator();
/* 228:375 */     while (iterator.hasNext()) {
/* 229:376 */       data[((Integer)iterator.next()).intValue()] = value;
/* 230:    */     }
/* 231:    */   }
/* 232:    */   
/* 233:    */   private void restoreNaNs(double[] ranks, List<Integer> nanPositions)
/* 234:    */   {
/* 235:387 */     if (nanPositions.size() == 0) {
/* 236:388 */       return;
/* 237:    */     }
/* 238:390 */     Iterator<Integer> iterator = nanPositions.iterator();
/* 239:391 */     while (iterator.hasNext()) {
/* 240:392 */       ranks[((Integer)iterator.next()).intValue()] = (0.0D / 0.0D);
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:    */   private List<Integer> getNanPositions(IntDoublePair[] ranks)
/* 245:    */   {
/* 246:404 */     ArrayList<Integer> out = new ArrayList();
/* 247:405 */     for (int i = 0; i < ranks.length; i++) {
/* 248:406 */       if (Double.isNaN(ranks[i].getValue())) {
/* 249:407 */         out.add(Integer.valueOf(i));
/* 250:    */       }
/* 251:    */     }
/* 252:410 */     return out;
/* 253:    */   }
/* 254:    */   
/* 255:    */   private static class IntDoublePair
/* 256:    */     implements Comparable<IntDoublePair>
/* 257:    */   {
/* 258:    */     private final double value;
/* 259:    */     private final int position;
/* 260:    */     
/* 261:    */     public IntDoublePair(double value, int position)
/* 262:    */     {
/* 263:433 */       this.value = value;
/* 264:434 */       this.position = position;
/* 265:    */     }
/* 266:    */     
/* 267:    */     public int compareTo(IntDoublePair other)
/* 268:    */     {
/* 269:445 */       return Double.compare(this.value, other.value);
/* 270:    */     }
/* 271:    */     
/* 272:    */     public double getValue()
/* 273:    */     {
/* 274:453 */       return this.value;
/* 275:    */     }
/* 276:    */     
/* 277:    */     public int getPosition()
/* 278:    */     {
/* 279:461 */       return this.position;
/* 280:    */     }
/* 281:    */   }
/* 282:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.ranking.NaturalRanking
 * JD-Core Version:    0.7.0.1
 */
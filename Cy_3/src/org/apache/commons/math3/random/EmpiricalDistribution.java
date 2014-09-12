/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileReader;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStreamReader;
/*   8:    */ import java.io.Serializable;
/*   9:    */ import java.net.URL;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.List;
/*  12:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  13:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*  14:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*  15:    */ import org.apache.commons.math3.exception.ZeroException;
/*  16:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  17:    */ import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
/*  18:    */ import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
/*  19:    */ import org.apache.commons.math3.util.FastMath;
/*  20:    */ import org.apache.commons.math3.util.MathUtils;
/*  21:    */ 
/*  22:    */ public class EmpiricalDistribution
/*  23:    */   implements Serializable
/*  24:    */ {
/*  25:    */   public static final int DEFAULT_BIN_COUNT = 1000;
/*  26:    */   private static final long serialVersionUID = 5729073523949762654L;
/*  27:    */   private final List<SummaryStatistics> binStats;
/*  28: 96 */   private SummaryStatistics sampleStats = null;
/*  29: 99 */   private double max = (-1.0D / 0.0D);
/*  30:102 */   private double min = (1.0D / 0.0D);
/*  31:105 */   private double delta = 0.0D;
/*  32:    */   private final int binCount;
/*  33:111 */   private boolean loaded = false;
/*  34:114 */   private double[] upperBounds = null;
/*  35:    */   private final RandomDataImpl randomData;
/*  36:    */   
/*  37:    */   public EmpiricalDistribution()
/*  38:    */   {
/*  39:123 */     this(1000, new RandomDataImpl());
/*  40:    */   }
/*  41:    */   
/*  42:    */   public EmpiricalDistribution(int binCount)
/*  43:    */   {
/*  44:132 */     this(binCount, new RandomDataImpl());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public EmpiricalDistribution(int binCount, RandomGenerator generator)
/*  48:    */   {
/*  49:144 */     this.binCount = binCount;
/*  50:145 */     this.randomData = new RandomDataImpl(generator);
/*  51:146 */     this.binStats = new ArrayList();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public EmpiricalDistribution(RandomGenerator generator)
/*  55:    */   {
/*  56:157 */     this(1000, generator);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public EmpiricalDistribution(int binCount, RandomDataImpl randomData)
/*  60:    */   {
/*  61:169 */     this.binCount = binCount;
/*  62:170 */     this.randomData = randomData;
/*  63:171 */     this.binStats = new ArrayList();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public EmpiricalDistribution(RandomDataImpl randomData)
/*  67:    */   {
/*  68:182 */     this(1000, randomData);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void load(double[] in)
/*  72:    */     throws NullArgumentException
/*  73:    */   {
/*  74:193 */     DataAdapter da = new ArrayDataAdapter(in);
/*  75:    */     try
/*  76:    */     {
/*  77:195 */       da.computeStats();
/*  78:196 */       fillBinStats(in);
/*  79:    */     }
/*  80:    */     catch (IOException e)
/*  81:    */     {
/*  82:198 */       throw new MathIllegalStateException(e, LocalizedFormats.SIMPLE_MESSAGE, new Object[] { e.getLocalizedMessage() });
/*  83:    */     }
/*  84:200 */     this.loaded = true;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void load(URL url)
/*  88:    */     throws IOException, NullArgumentException
/*  89:    */   {
/*  90:212 */     MathUtils.checkNotNull(url);
/*  91:213 */     BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
/*  92:    */     try
/*  93:    */     {
/*  94:216 */       DataAdapter da = new StreamDataAdapter(in);
/*  95:217 */       da.computeStats();
/*  96:218 */       if (this.sampleStats.getN() == 0L) {
/*  97:219 */         throw new ZeroException(LocalizedFormats.URL_CONTAINS_NO_DATA, new Object[] { url });
/*  98:    */       }
/*  99:221 */       in = new BufferedReader(new InputStreamReader(url.openStream()));
/* 100:222 */       fillBinStats(in);
/* 101:223 */       this.loaded = true; return;
/* 102:    */     }
/* 103:    */     finally
/* 104:    */     {
/* 105:    */       try
/* 106:    */       {
/* 107:226 */         in.close();
/* 108:    */       }
/* 109:    */       catch (IOException ex) {}
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void load(File file)
/* 114:    */     throws IOException, NullArgumentException
/* 115:    */   {
/* 116:241 */     MathUtils.checkNotNull(file);
/* 117:242 */     BufferedReader in = new BufferedReader(new FileReader(file));
/* 118:    */     try
/* 119:    */     {
/* 120:244 */       DataAdapter da = new StreamDataAdapter(in);
/* 121:245 */       da.computeStats();
/* 122:246 */       in = new BufferedReader(new FileReader(file));
/* 123:247 */       fillBinStats(in);
/* 124:248 */       this.loaded = true; return;
/* 125:    */     }
/* 126:    */     finally
/* 127:    */     {
/* 128:    */       try
/* 129:    */       {
/* 130:251 */         in.close();
/* 131:    */       }
/* 132:    */       catch (IOException ex) {}
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   private abstract class DataAdapter
/* 137:    */   {
/* 138:    */     private DataAdapter() {}
/* 139:    */     
/* 140:    */     public abstract void computeBinStats()
/* 141:    */       throws IOException;
/* 142:    */     
/* 143:    */     public abstract void computeStats()
/* 144:    */       throws IOException;
/* 145:    */   }
/* 146:    */   
/* 147:    */   private class DataAdapterFactory
/* 148:    */   {
/* 149:    */     private DataAdapterFactory() {}
/* 150:    */     
/* 151:    */     public EmpiricalDistribution.DataAdapter getAdapter(Object in)
/* 152:    */     {
/* 153:293 */       if ((in instanceof BufferedReader))
/* 154:    */       {
/* 155:294 */         BufferedReader inputStream = (BufferedReader)in;
/* 156:295 */         return new EmpiricalDistribution.StreamDataAdapter(inputStream);
/* 157:    */       }
/* 158:296 */       if ((in instanceof double[]))
/* 159:    */       {
/* 160:297 */         double[] inputArray = (double[])in;
/* 161:298 */         return new EmpiricalDistribution.ArrayDataAdapter(inputArray);
/* 162:    */       }
/* 163:300 */       throw new MathIllegalArgumentException(LocalizedFormats.INPUT_DATA_FROM_UNSUPPORTED_DATASOURCE, new Object[] { in.getClass().getName(), BufferedReader.class.getName(), EmpiricalDistribution.StreamDataAdapter.class.getName() });
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   private class StreamDataAdapter
/* 168:    */     extends EmpiricalDistribution.DataAdapter
/* 169:    */   {
/* 170:    */     private BufferedReader inputStream;
/* 171:    */     
/* 172:    */     public StreamDataAdapter(BufferedReader in)
/* 173:    */     {
/* 174:321 */       super();
/* 175:322 */       this.inputStream = in;
/* 176:    */     }
/* 177:    */     
/* 178:    */     public void computeBinStats()
/* 179:    */       throws IOException
/* 180:    */     {
/* 181:328 */       String str = null;
/* 182:329 */       double val = 0.0D;
/* 183:330 */       while ((str = this.inputStream.readLine()) != null)
/* 184:    */       {
/* 185:331 */         val = Double.parseDouble(str);
/* 186:332 */         SummaryStatistics stats = (SummaryStatistics)EmpiricalDistribution.this.binStats.get(EmpiricalDistribution.this.findBin(val));
/* 187:333 */         stats.addValue(val);
/* 188:    */       }
/* 189:336 */       this.inputStream.close();
/* 190:337 */       this.inputStream = null;
/* 191:    */     }
/* 192:    */     
/* 193:    */     public void computeStats()
/* 194:    */       throws IOException
/* 195:    */     {
/* 196:343 */       String str = null;
/* 197:344 */       double val = 0.0D;
/* 198:345 */       EmpiricalDistribution.this.sampleStats = new SummaryStatistics();
/* 199:346 */       while ((str = this.inputStream.readLine()) != null)
/* 200:    */       {
/* 201:347 */         val = Double.valueOf(str).doubleValue();
/* 202:348 */         EmpiricalDistribution.this.sampleStats.addValue(val);
/* 203:    */       }
/* 204:350 */       this.inputStream.close();
/* 205:351 */       this.inputStream = null;
/* 206:    */     }
/* 207:    */   }
/* 208:    */   
/* 209:    */   private class ArrayDataAdapter
/* 210:    */     extends EmpiricalDistribution.DataAdapter
/* 211:    */   {
/* 212:    */     private double[] inputArray;
/* 213:    */     
/* 214:    */     public ArrayDataAdapter(double[] in)
/* 215:    */       throws NullArgumentException
/* 216:    */     {
/* 217:370 */       super();
/* 218:371 */       MathUtils.checkNotNull(in);
/* 219:372 */       this.inputArray = in;
/* 220:    */     }
/* 221:    */     
/* 222:    */     public void computeStats()
/* 223:    */       throws IOException
/* 224:    */     {
/* 225:378 */       EmpiricalDistribution.this.sampleStats = new SummaryStatistics();
/* 226:379 */       for (int i = 0; i < this.inputArray.length; i++) {
/* 227:380 */         EmpiricalDistribution.this.sampleStats.addValue(this.inputArray[i]);
/* 228:    */       }
/* 229:    */     }
/* 230:    */     
/* 231:    */     public void computeBinStats()
/* 232:    */       throws IOException
/* 233:    */     {
/* 234:387 */       for (int i = 0; i < this.inputArray.length; i++)
/* 235:    */       {
/* 236:388 */         SummaryStatistics stats = (SummaryStatistics)EmpiricalDistribution.this.binStats.get(EmpiricalDistribution.this.findBin(this.inputArray[i]));
/* 237:    */         
/* 238:390 */         stats.addValue(this.inputArray[i]);
/* 239:    */       }
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   private void fillBinStats(Object in)
/* 244:    */     throws IOException
/* 245:    */   {
/* 246:403 */     this.min = this.sampleStats.getMin();
/* 247:404 */     this.max = this.sampleStats.getMax();
/* 248:405 */     this.delta = ((this.max - this.min) / Double.valueOf(this.binCount).doubleValue());
/* 249:408 */     if (!this.binStats.isEmpty()) {
/* 250:409 */       this.binStats.clear();
/* 251:    */     }
/* 252:411 */     for (int i = 0; i < this.binCount; i++)
/* 253:    */     {
/* 254:412 */       SummaryStatistics stats = new SummaryStatistics();
/* 255:413 */       this.binStats.add(i, stats);
/* 256:    */     }
/* 257:417 */     DataAdapterFactory aFactory = new DataAdapterFactory();
/* 258:418 */     DataAdapter da = aFactory.getAdapter(in);
/* 259:419 */     da.computeBinStats();
/* 260:    */     
/* 261:    */ 
/* 262:422 */     this.upperBounds = new double[this.binCount];
/* 263:423 */     this.upperBounds[0] = (((SummaryStatistics)this.binStats.get(0)).getN() / this.sampleStats.getN());
/* 264:425 */     for (int i = 1; i < this.binCount - 1; i++) {
/* 265:426 */       this.upperBounds[i] = (this.upperBounds[(i - 1)] + ((SummaryStatistics)this.binStats.get(i)).getN() / this.sampleStats.getN());
/* 266:    */     }
/* 267:429 */     this.upperBounds[(this.binCount - 1)] = 1.0D;
/* 268:    */   }
/* 269:    */   
/* 270:    */   private int findBin(double value)
/* 271:    */   {
/* 272:439 */     return FastMath.min(FastMath.max((int)FastMath.ceil((value - this.min) / this.delta) - 1, 0), this.binCount - 1);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public double getNextValue()
/* 276:    */     throws MathIllegalStateException
/* 277:    */   {
/* 278:453 */     if (!this.loaded) {
/* 279:454 */       throw new MathIllegalStateException(LocalizedFormats.DISTRIBUTION_NOT_LOADED, new Object[0]);
/* 280:    */     }
/* 281:458 */     double x = this.randomData.nextUniform(0.0D, 1.0D);
/* 282:461 */     for (int i = 0; i < this.binCount; i++) {
/* 283:462 */       if (x <= this.upperBounds[i])
/* 284:    */       {
/* 285:463 */         SummaryStatistics stats = (SummaryStatistics)this.binStats.get(i);
/* 286:464 */         if (stats.getN() > 0L)
/* 287:    */         {
/* 288:465 */           if (stats.getStandardDeviation() > 0.0D) {
/* 289:466 */             return this.randomData.nextGaussian(stats.getMean(), stats.getStandardDeviation());
/* 290:    */           }
/* 291:469 */           return stats.getMean();
/* 292:    */         }
/* 293:    */       }
/* 294:    */     }
/* 295:474 */     throw new MathIllegalStateException(LocalizedFormats.NO_BIN_SELECTED, new Object[0]);
/* 296:    */   }
/* 297:    */   
/* 298:    */   public StatisticalSummary getSampleStats()
/* 299:    */   {
/* 300:486 */     return this.sampleStats;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public int getBinCount()
/* 304:    */   {
/* 305:495 */     return this.binCount;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public List<SummaryStatistics> getBinStats()
/* 309:    */   {
/* 310:506 */     return this.binStats;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public double[] getUpperBounds()
/* 314:    */   {
/* 315:523 */     double[] binUpperBounds = new double[this.binCount];
/* 316:524 */     binUpperBounds[0] = (this.min + this.delta);
/* 317:525 */     for (int i = 1; i < this.binCount - 1; i++) {
/* 318:526 */       binUpperBounds[i] = (binUpperBounds[(i - 1)] + this.delta);
/* 319:    */     }
/* 320:528 */     binUpperBounds[(this.binCount - 1)] = this.max;
/* 321:529 */     return binUpperBounds;
/* 322:    */   }
/* 323:    */   
/* 324:    */   public double[] getGeneratorUpperBounds()
/* 325:    */   {
/* 326:544 */     int len = this.upperBounds.length;
/* 327:545 */     double[] out = new double[len];
/* 328:546 */     System.arraycopy(this.upperBounds, 0, out, 0, len);
/* 329:547 */     return out;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public boolean isLoaded()
/* 333:    */   {
/* 334:556 */     return this.loaded;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public void reSeed(long seed)
/* 338:    */   {
/* 339:566 */     this.randomData.reSeed(seed);
/* 340:    */   }
/* 341:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.EmpiricalDistribution
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.random;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.net.MalformedURLException;
/*   7:    */ import java.net.URL;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*  10:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  11:    */ import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
/*  12:    */ 
/*  13:    */ public class ValueServer
/*  14:    */ {
/*  15:    */   public static final int DIGEST_MODE = 0;
/*  16:    */   public static final int REPLAY_MODE = 1;
/*  17:    */   public static final int UNIFORM_MODE = 2;
/*  18:    */   public static final int EXPONENTIAL_MODE = 3;
/*  19:    */   public static final int GAUSSIAN_MODE = 4;
/*  20:    */   public static final int CONSTANT_MODE = 5;
/*  21: 70 */   private int mode = 5;
/*  22: 73 */   private URL valuesFileURL = null;
/*  23: 76 */   private double mu = 0.0D;
/*  24: 79 */   private double sigma = 0.0D;
/*  25: 82 */   private EmpiricalDistribution empiricalDistribution = null;
/*  26: 85 */   private BufferedReader filePointer = null;
/*  27:    */   private final RandomDataImpl randomData;
/*  28:    */   
/*  29:    */   public ValueServer()
/*  30:    */   {
/*  31: 94 */     this.randomData = new RandomDataImpl();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public ValueServer(RandomDataImpl randomData)
/*  35:    */   {
/*  36:105 */     this.randomData = randomData;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public double getNext()
/*  40:    */     throws IOException
/*  41:    */   {
/*  42:116 */     switch (this.mode)
/*  43:    */     {
/*  44:    */     case 0: 
/*  45:117 */       return getNextDigest();
/*  46:    */     case 1: 
/*  47:118 */       return getNextReplay();
/*  48:    */     case 2: 
/*  49:119 */       return getNextUniform();
/*  50:    */     case 3: 
/*  51:120 */       return getNextExponential();
/*  52:    */     case 4: 
/*  53:121 */       return getNextGaussian();
/*  54:    */     case 5: 
/*  55:122 */       return this.mu;
/*  56:    */     }
/*  57:123 */     throw new MathIllegalStateException(LocalizedFormats.UNKNOWN_MODE, new Object[] { Integer.valueOf(this.mode), "DIGEST_MODE", Integer.valueOf(0), "REPLAY_MODE", Integer.valueOf(1), "UNIFORM_MODE", Integer.valueOf(2), "EXPONENTIAL_MODE", Integer.valueOf(3), "GAUSSIAN_MODE", Integer.valueOf(4), "CONSTANT_MODE", Integer.valueOf(5) });
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void fill(double[] values)
/*  61:    */     throws IOException
/*  62:    */   {
/*  63:139 */     for (int i = 0; i < values.length; i++) {
/*  64:140 */       values[i] = getNext();
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public double[] fill(int length)
/*  69:    */     throws IOException
/*  70:    */   {
/*  71:153 */     double[] out = new double[length];
/*  72:154 */     for (int i = 0; i < length; i++) {
/*  73:155 */       out[i] = getNext();
/*  74:    */     }
/*  75:157 */     return out;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void computeDistribution()
/*  79:    */     throws IOException
/*  80:    */   {
/*  81:173 */     computeDistribution(1000);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void computeDistribution(int binCount)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:192 */     this.empiricalDistribution = new EmpiricalDistribution(binCount, this.randomData);
/*  88:193 */     this.empiricalDistribution.load(this.valuesFileURL);
/*  89:194 */     this.mu = this.empiricalDistribution.getSampleStats().getMean();
/*  90:195 */     this.sigma = this.empiricalDistribution.getSampleStats().getStandardDeviation();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int getMode()
/*  94:    */   {
/*  95:205 */     return this.mode;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setMode(int mode)
/*  99:    */   {
/* 100:214 */     this.mode = mode;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public URL getValuesFileURL()
/* 104:    */   {
/* 105:224 */     return this.valuesFileURL;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setValuesFileURL(String url)
/* 109:    */     throws MalformedURLException
/* 110:    */   {
/* 111:235 */     this.valuesFileURL = new URL(url);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setValuesFileURL(URL url)
/* 115:    */   {
/* 116:244 */     this.valuesFileURL = url;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public EmpiricalDistribution getEmpiricalDistribution()
/* 120:    */   {
/* 121:253 */     return this.empiricalDistribution;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void resetReplayFile()
/* 125:    */     throws IOException
/* 126:    */   {
/* 127:262 */     if (this.filePointer != null) {
/* 128:    */       try
/* 129:    */       {
/* 130:264 */         this.filePointer.close();
/* 131:265 */         this.filePointer = null;
/* 132:    */       }
/* 133:    */       catch (IOException ex) {}
/* 134:    */     }
/* 135:270 */     this.filePointer = new BufferedReader(new InputStreamReader(this.valuesFileURL.openStream()));
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void closeReplayFile()
/* 139:    */     throws IOException
/* 140:    */   {
/* 141:279 */     if (this.filePointer != null)
/* 142:    */     {
/* 143:280 */       this.filePointer.close();
/* 144:281 */       this.filePointer = null;
/* 145:    */     }
/* 146:    */   }
/* 147:    */   
/* 148:    */   public double getMu()
/* 149:    */   {
/* 150:294 */     return this.mu;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setMu(double mu)
/* 154:    */   {
/* 155:305 */     this.mu = mu;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public double getSigma()
/* 159:    */   {
/* 160:318 */     return this.sigma;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void setSigma(double sigma)
/* 164:    */   {
/* 165:327 */     this.sigma = sigma;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void reSeed(long seed)
/* 169:    */   {
/* 170:337 */     this.randomData.reSeed(seed);
/* 171:    */   }
/* 172:    */   
/* 173:    */   private double getNextDigest()
/* 174:    */   {
/* 175:353 */     if ((this.empiricalDistribution == null) || (this.empiricalDistribution.getBinStats().size() == 0)) {
/* 176:355 */       throw new MathIllegalStateException(LocalizedFormats.DIGEST_NOT_INITIALIZED, new Object[0]);
/* 177:    */     }
/* 178:357 */     return this.empiricalDistribution.getNextValue();
/* 179:    */   }
/* 180:    */   
/* 181:    */   private double getNextReplay()
/* 182:    */     throws IOException
/* 183:    */   {
/* 184:379 */     String str = null;
/* 185:380 */     if (this.filePointer == null) {
/* 186:381 */       resetReplayFile();
/* 187:    */     }
/* 188:383 */     if ((str = this.filePointer.readLine()) == null)
/* 189:    */     {
/* 190:385 */       closeReplayFile();
/* 191:386 */       resetReplayFile();
/* 192:387 */       if ((str = this.filePointer.readLine()) == null) {
/* 193:388 */         throw new MathIllegalStateException(LocalizedFormats.URL_CONTAINS_NO_DATA, new Object[] { this.valuesFileURL });
/* 194:    */       }
/* 195:    */     }
/* 196:392 */     return Double.valueOf(str).doubleValue();
/* 197:    */   }
/* 198:    */   
/* 199:    */   private double getNextUniform()
/* 200:    */   {
/* 201:401 */     return this.randomData.nextUniform(0.0D, 2.0D * this.mu);
/* 202:    */   }
/* 203:    */   
/* 204:    */   private double getNextExponential()
/* 205:    */   {
/* 206:410 */     return this.randomData.nextExponential(this.mu);
/* 207:    */   }
/* 208:    */   
/* 209:    */   private double getNextGaussian()
/* 210:    */   {
/* 211:420 */     return this.randomData.nextGaussian(this.mu, this.sigma);
/* 212:    */   }
/* 213:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.ValueServer
 * JD-Core Version:    0.7.0.1
 */
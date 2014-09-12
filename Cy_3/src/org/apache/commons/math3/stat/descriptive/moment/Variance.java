/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
/*   7:    */ import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
/*   8:    */ import org.apache.commons.math3.util.MathUtils;
/*   9:    */ 
/*  10:    */ public class Variance
/*  11:    */   extends AbstractStorelessUnivariateStatistic
/*  12:    */   implements Serializable, WeightedEvaluation
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = -9111962718267217978L;
/*  15: 75 */   protected SecondMoment moment = null;
/*  16: 84 */   protected boolean incMoment = true;
/*  17: 91 */   private boolean isBiasCorrected = true;
/*  18:    */   
/*  19:    */   public Variance()
/*  20:    */   {
/*  21: 98 */     this.moment = new SecondMoment();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Variance(SecondMoment m2)
/*  25:    */   {
/*  26:112 */     this.incMoment = false;
/*  27:113 */     this.moment = m2;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Variance(boolean isBiasCorrected)
/*  31:    */   {
/*  32:125 */     this.moment = new SecondMoment();
/*  33:126 */     this.isBiasCorrected = isBiasCorrected;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Variance(boolean isBiasCorrected, SecondMoment m2)
/*  37:    */   {
/*  38:139 */     this.incMoment = false;
/*  39:140 */     this.moment = m2;
/*  40:141 */     this.isBiasCorrected = isBiasCorrected;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Variance(Variance original)
/*  44:    */   {
/*  45:151 */     copy(original, this);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void increment(double d)
/*  49:    */   {
/*  50:169 */     if (this.incMoment) {
/*  51:170 */       this.moment.increment(d);
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public double getResult()
/*  56:    */   {
/*  57:179 */     if (this.moment.n == 0L) {
/*  58:180 */       return (0.0D / 0.0D);
/*  59:    */     }
/*  60:181 */     if (this.moment.n == 1L) {
/*  61:182 */       return 0.0D;
/*  62:    */     }
/*  63:184 */     if (this.isBiasCorrected) {
/*  64:185 */       return this.moment.m2 / (this.moment.n - 1.0D);
/*  65:    */     }
/*  66:187 */     return this.moment.m2 / this.moment.n;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public long getN()
/*  70:    */   {
/*  71:196 */     return this.moment.getN();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void clear()
/*  75:    */   {
/*  76:204 */     if (this.incMoment) {
/*  77:205 */       this.moment.clear();
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public double evaluate(double[] values)
/*  82:    */   {
/*  83:227 */     if (values == null) {
/*  84:228 */       throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
/*  85:    */     }
/*  86:230 */     return evaluate(values, 0, values.length);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public double evaluate(double[] values, int begin, int length)
/*  90:    */   {
/*  91:256 */     double var = (0.0D / 0.0D);
/*  92:258 */     if (test(values, begin, length))
/*  93:    */     {
/*  94:259 */       clear();
/*  95:260 */       if (length == 1)
/*  96:    */       {
/*  97:261 */         var = 0.0D;
/*  98:    */       }
/*  99:262 */       else if (length > 1)
/* 100:    */       {
/* 101:263 */         Mean mean = new Mean();
/* 102:264 */         double m = mean.evaluate(values, begin, length);
/* 103:265 */         var = evaluate(values, m, begin, length);
/* 104:    */       }
/* 105:    */     }
/* 106:268 */     return var;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public double evaluate(double[] values, double[] weights, int begin, int length)
/* 110:    */   {
/* 111:316 */     double var = (0.0D / 0.0D);
/* 112:318 */     if (test(values, weights, begin, length))
/* 113:    */     {
/* 114:319 */       clear();
/* 115:320 */       if (length == 1)
/* 116:    */       {
/* 117:321 */         var = 0.0D;
/* 118:    */       }
/* 119:322 */       else if (length > 1)
/* 120:    */       {
/* 121:323 */         Mean mean = new Mean();
/* 122:324 */         double m = mean.evaluate(values, weights, begin, length);
/* 123:325 */         var = evaluate(values, weights, m, begin, length);
/* 124:    */       }
/* 125:    */     }
/* 126:328 */     return var;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public double evaluate(double[] values, double[] weights)
/* 130:    */   {
/* 131:370 */     return evaluate(values, weights, 0, values.length);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public double evaluate(double[] values, double mean, int begin, int length)
/* 135:    */   {
/* 136:402 */     double var = (0.0D / 0.0D);
/* 137:404 */     if (test(values, begin, length)) {
/* 138:405 */       if (length == 1)
/* 139:    */       {
/* 140:406 */         var = 0.0D;
/* 141:    */       }
/* 142:407 */       else if (length > 1)
/* 143:    */       {
/* 144:408 */         double accum = 0.0D;
/* 145:409 */         double dev = 0.0D;
/* 146:410 */         double accum2 = 0.0D;
/* 147:411 */         for (int i = begin; i < begin + length; i++)
/* 148:    */         {
/* 149:412 */           dev = values[i] - mean;
/* 150:413 */           accum += dev * dev;
/* 151:414 */           accum2 += dev;
/* 152:    */         }
/* 153:416 */         double len = length;
/* 154:417 */         if (this.isBiasCorrected) {
/* 155:418 */           var = (accum - accum2 * accum2 / len) / (len - 1.0D);
/* 156:    */         } else {
/* 157:420 */           var = (accum - accum2 * accum2 / len) / len;
/* 158:    */         }
/* 159:    */       }
/* 160:    */     }
/* 161:424 */     return var;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public double evaluate(double[] values, double mean)
/* 165:    */   {
/* 166:453 */     return evaluate(values, mean, 0, values.length);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public double evaluate(double[] values, double[] weights, double mean, int begin, int length)
/* 170:    */   {
/* 171:504 */     double var = (0.0D / 0.0D);
/* 172:506 */     if (test(values, weights, begin, length)) {
/* 173:507 */       if (length == 1)
/* 174:    */       {
/* 175:508 */         var = 0.0D;
/* 176:    */       }
/* 177:509 */       else if (length > 1)
/* 178:    */       {
/* 179:510 */         double accum = 0.0D;
/* 180:511 */         double dev = 0.0D;
/* 181:512 */         double accum2 = 0.0D;
/* 182:513 */         for (int i = begin; i < begin + length; i++)
/* 183:    */         {
/* 184:514 */           dev = values[i] - mean;
/* 185:515 */           accum += weights[i] * (dev * dev);
/* 186:516 */           accum2 += weights[i] * dev;
/* 187:    */         }
/* 188:519 */         double sumWts = 0.0D;
/* 189:520 */         for (int i = begin; i < begin + length; i++) {
/* 190:521 */           sumWts += weights[i];
/* 191:    */         }
/* 192:524 */         if (this.isBiasCorrected) {
/* 193:525 */           var = (accum - accum2 * accum2 / sumWts) / (sumWts - 1.0D);
/* 194:    */         } else {
/* 195:527 */           var = (accum - accum2 * accum2 / sumWts) / sumWts;
/* 196:    */         }
/* 197:    */       }
/* 198:    */     }
/* 199:531 */     return var;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public double evaluate(double[] values, double[] weights, double mean)
/* 203:    */   {
/* 204:576 */     return evaluate(values, weights, mean, 0, values.length);
/* 205:    */   }
/* 206:    */   
/* 207:    */   public boolean isBiasCorrected()
/* 208:    */   {
/* 209:583 */     return this.isBiasCorrected;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void setBiasCorrected(boolean biasCorrected)
/* 213:    */   {
/* 214:590 */     this.isBiasCorrected = biasCorrected;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public Variance copy()
/* 218:    */   {
/* 219:598 */     Variance result = new Variance();
/* 220:599 */     copy(this, result);
/* 221:600 */     return result;
/* 222:    */   }
/* 223:    */   
/* 224:    */   public static void copy(Variance source, Variance dest)
/* 225:    */     throws NullArgumentException
/* 226:    */   {
/* 227:613 */     MathUtils.checkNotNull(source);
/* 228:614 */     MathUtils.checkNotNull(dest);
/* 229:615 */     dest.setData(source.getDataRef());
/* 230:616 */     dest.moment = source.moment.copy();
/* 231:617 */     dest.isBiasCorrected = source.isBiasCorrected;
/* 232:618 */     dest.incMoment = source.incMoment;
/* 233:    */   }
/* 234:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.Variance
 * JD-Core Version:    0.7.0.1
 */
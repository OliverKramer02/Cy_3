/*   1:    */ package org.apache.commons.math3.optimization.univariate;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.TooManyEvaluationsException;
/*   7:    */ import org.apache.commons.math3.optimization.GoalType;
/*   8:    */ import org.apache.commons.math3.util.Incrementor;
/*   9:    */ 
/*  10:    */ public class BracketFinder
/*  11:    */ {
/*  12:    */   private static final double EPS_MIN = 9.999999999999999E-022D;
/*  13:    */   private static final double GOLD = 1.618034D;
/*  14:    */   private final double growLimit;
/*  15: 48 */   private final Incrementor evaluations = new Incrementor();
/*  16:    */   private double lo;
/*  17:    */   private double hi;
/*  18:    */   private double mid;
/*  19:    */   private double fLo;
/*  20:    */   private double fHi;
/*  21:    */   private double fMid;
/*  22:    */   
/*  23:    */   public BracketFinder()
/*  24:    */   {
/*  25: 79 */     this(100.0D, 50);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public BracketFinder(double growLimit, int maxEvaluations)
/*  29:    */   {
/*  30: 91 */     if (growLimit <= 0.0D) {
/*  31: 92 */       throw new NotStrictlyPositiveException(Double.valueOf(growLimit));
/*  32:    */     }
/*  33: 94 */     if (maxEvaluations <= 0) {
/*  34: 95 */       throw new NotStrictlyPositiveException(Integer.valueOf(maxEvaluations));
/*  35:    */     }
/*  36: 98 */     this.growLimit = growLimit;
/*  37: 99 */     this.evaluations.setMaximalCount(maxEvaluations);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void search(UnivariateFunction func, GoalType goal, double xA, double xB)
/*  41:    */   {
/*  42:113 */     this.evaluations.resetCount();
/*  43:114 */     boolean isMinim = goal == GoalType.MINIMIZE;
/*  44:    */     
/*  45:116 */     double fA = eval(func, xA);
/*  46:117 */     double fB = eval(func, xB);
/*  47:118 */     if (isMinim ? fA < fB : fA > fB)
/*  48:    */     {
/*  49:122 */       double tmp = xA;
/*  50:123 */       xA = xB;
/*  51:124 */       xB = tmp;
/*  52:    */       
/*  53:126 */       tmp = fA;
/*  54:127 */       fA = fB;
/*  55:128 */       fB = tmp;
/*  56:    */     }
/*  57:131 */     double xC = xB + 1.618034D * (xB - xA);
/*  58:132 */     double fC = eval(func, xC);
/*  59:134 */     while (isMinim ? fC < fB : fC > fB)
/*  60:    */     {
/*  61:135 */       double tmp1 = (xB - xA) * (fB - fC);
/*  62:136 */       double tmp2 = (xB - xC) * (fB - fA);
/*  63:    */       
/*  64:138 */       double val = tmp2 - tmp1;
/*  65:139 */       double denom = Math.abs(val) < 9.999999999999999E-022D ? 2.0E-021D : 2.0D * val;
/*  66:    */       
/*  67:141 */       double w = xB - ((xB - xC) * tmp2 - (xB - xA) * tmp1) / denom;
/*  68:142 */       double wLim = xB + this.growLimit * (xC - xB);
/*  69:    */       double fW;
/*  70:145 */       if ((w - xC) * (xB - w) > 0.0D)
/*  71:    */       {
/*  72:146 */         fW = eval(func, w);
/*  73:147 */         if (isMinim ? fW < fC : fW > fC)
/*  74:    */         {
/*  75:150 */           xA = xB;
/*  76:151 */           xB = w;
/*  77:152 */           fA = fB;
/*  78:153 */           fB = fW;
/*  79:154 */           break;
/*  80:    */         }
/*  81:155 */         if (isMinim ? fW > fB : fW < fB)
/*  82:    */         {
/*  83:158 */           xC = w;
/*  84:159 */           fC = fW;
/*  85:160 */           break;
/*  86:    */         }
/*  87:162 */         w = xC + 1.618034D * (xC - xB);
/*  88:163 */         fW = eval(func, w);
/*  89:    */       }
/*  90:    */       else
/*  91:    */       {
/*  92:    */        
/*  93:164 */         if ((w - wLim) * (wLim - xC) >= 0.0D)
/*  94:    */         {
/*  95:165 */           w = wLim;
/*  96:166 */           fW = eval(func, w);
/*  97:    */         }
/*  98:167 */         else if ((w - wLim) * (xC - w) > 0.0D)
/*  99:    */         {
/* 100:168 */           fW = eval(func, w);
/* 101:169 */           if (isMinim ? fW < fC : fW > fC)
/* 102:    */           {
/* 103:172 */             xB = xC;
/* 104:173 */             xC = w;
/* 105:174 */             w = xC + 1.618034D * (xC - xB);
/* 106:175 */             fB = fC;
/* 107:176 */             fC = fW;
/* 108:177 */             fW = eval(func, w);
/* 109:    */           }
/* 110:    */         }
/* 111:    */         else
/* 112:    */         {
/* 113:180 */           w = xC + 1.618034D * (xC - xB);
/* 114:181 */           fW = eval(func, w);
/* 115:    */         }
/* 116:    */       }
/* 117:184 */       xA = xB;
/* 118:185 */       fA = fB;
/* 119:186 */       xB = xC;
/* 120:187 */       fB = fC;
/* 121:188 */       xC = w;
/* 122:189 */       fC = fW;
/* 123:    */     }
/* 124:192 */     this.lo = xA;
/* 125:193 */     this.fLo = fA;
/* 126:194 */     this.mid = xB;
/* 127:195 */     this.fMid = fB;
/* 128:196 */     this.hi = xC;
/* 129:197 */     this.fHi = fC;
/* 130:199 */     if (this.lo > this.hi)
/* 131:    */     {
/* 132:200 */       double tmp = this.lo;
/* 133:201 */       this.lo = this.hi;
/* 134:202 */       this.hi = tmp;
/* 135:    */       
/* 136:204 */       tmp = this.fLo;
/* 137:205 */       this.fLo = this.fHi;
/* 138:206 */       this.fHi = tmp;
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public int getMaxEvaluations()
/* 143:    */   {
/* 144:214 */     return this.evaluations.getMaximalCount();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public int getEvaluations()
/* 148:    */   {
/* 149:221 */     return this.evaluations.getCount();
/* 150:    */   }
/* 151:    */   
/* 152:    */   public double getLo()
/* 153:    */   {
/* 154:229 */     return this.lo;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public double getFLo()
/* 158:    */   {
/* 159:237 */     return this.fLo;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public double getHi()
/* 163:    */   {
/* 164:245 */     return this.hi;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public double getFHi()
/* 168:    */   {
/* 169:253 */     return this.fHi;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public double getMid()
/* 173:    */   {
/* 174:261 */     return this.mid;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public double getFMid()
/* 178:    */   {
/* 179:269 */     return this.fMid;
/* 180:    */   }
/* 181:    */   
/* 182:    */   private double eval(UnivariateFunction f, double x)
/* 183:    */   {
/* 184:    */     try
/* 185:    */     {
/* 186:281 */       this.evaluations.incrementCount();
/* 187:    */     }
/* 188:    */     catch (MaxCountExceededException e)
/* 189:    */     {
/* 190:283 */       throw new TooManyEvaluationsException(e.getMax());
/* 191:    */     }
/* 192:285 */     return f.value(x);
/* 193:    */   }
/* 194:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.univariate.BracketFinder
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.stat.descriptive.moment;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   5:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   6:    */ import org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic;
/*   7:    */ import org.apache.commons.math3.util.MathUtils;
/*   8:    */ 
/*   9:    */ public class SemiVariance
/*  10:    */   extends AbstractUnivariateStatistic
/*  11:    */   implements Serializable
/*  12:    */ {
/*  13: 61 */   public static final Direction UPSIDE_VARIANCE = Direction.UPSIDE;
/*  14: 67 */   public static final Direction DOWNSIDE_VARIANCE = Direction.DOWNSIDE;
/*  15:    */   private static final long serialVersionUID = -2653430366886024994L;
/*  16: 76 */   private boolean biasCorrected = true;
/*  17: 81 */   private Direction varianceDirection = Direction.DOWNSIDE;
/*  18:    */   
/*  19:    */   public SemiVariance() {}
/*  20:    */   
/*  21:    */   public SemiVariance(boolean biasCorrected)
/*  22:    */   {
/*  23: 99 */     this.biasCorrected = biasCorrected;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SemiVariance(Direction direction)
/*  27:    */   {
/*  28:111 */     this.varianceDirection = direction;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SemiVariance(boolean corrected, Direction direction)
/*  32:    */   {
/*  33:127 */     this.biasCorrected = corrected;
/*  34:128 */     this.varianceDirection = direction;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public SemiVariance(SemiVariance original)
/*  38:    */   {
/*  39:139 */     copy(original, this);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public SemiVariance copy()
/*  43:    */   {
/*  44:148 */     SemiVariance result = new SemiVariance();
/*  45:149 */     copy(this, result);
/*  46:150 */     return result;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static void copy(SemiVariance source, SemiVariance dest)
/*  50:    */     throws NullArgumentException
/*  51:    */   {
/*  52:164 */     MathUtils.checkNotNull(source);
/*  53:165 */     MathUtils.checkNotNull(dest);
/*  54:166 */     dest.setData(source.getDataRef());
/*  55:167 */     dest.biasCorrected = source.biasCorrected;
/*  56:168 */     dest.varianceDirection = source.varianceDirection;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double evaluate(double[] values)
/*  60:    */   {
/*  61:183 */     if (values == null) {
/*  62:184 */       throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
/*  63:    */     }
/*  64:186 */     return evaluate(values, 0, values.length);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public double evaluate(double[] values, int start, int length)
/*  68:    */   {
/*  69:206 */     double m = new Mean().evaluate(values, start, length);
/*  70:207 */     return evaluate(values, m, this.varianceDirection, this.biasCorrected, 0, values.length);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public double evaluate(double[] values, Direction direction)
/*  74:    */   {
/*  75:222 */     double m = new Mean().evaluate(values);
/*  76:223 */     return evaluate(values, m, direction, this.biasCorrected, 0, values.length);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public double evaluate(double[] values, double cutoff)
/*  80:    */   {
/*  81:239 */     return evaluate(values, cutoff, this.varianceDirection, this.biasCorrected, 0, values.length);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public double evaluate(double[] values, double cutoff, Direction direction)
/*  85:    */   {
/*  86:256 */     return evaluate(values, cutoff, direction, this.biasCorrected, 0, values.length);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public double evaluate(double[] values, double cutoff, Direction direction, boolean corrected, int start, int length)
/*  90:    */   {
/*  91:280 */     test(values, start, length);
/*  92:281 */     if (values.length == 0) {
/*  93:282 */       return (0.0D / 0.0D);
/*  94:    */     }
/*  95:284 */     if (values.length == 1) {
/*  96:285 */       return 0.0D;
/*  97:    */     }
/*  98:287 */     boolean booleanDirection = direction.getDirection();
/*  99:    */     
/* 100:289 */     double dev = 0.0D;
/* 101:290 */     double sumsq = 0.0D;
/* 102:291 */     for (int i = start; i < length; i++) {
/* 103:292 */       if (values[i] > cutoff == booleanDirection)
/* 104:    */       {
/* 105:293 */         dev = values[i] - cutoff;
/* 106:294 */         sumsq += dev * dev;
/* 107:    */       }
/* 108:    */     }
/* 109:298 */     if (corrected) {
/* 110:299 */       return sumsq / (length - 1.0D);
/* 111:    */     }
/* 112:301 */     return sumsq / length;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean isBiasCorrected()
/* 116:    */   {
/* 117:313 */     return this.biasCorrected;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setBiasCorrected(boolean biasCorrected)
/* 121:    */   {
/* 122:322 */     this.biasCorrected = biasCorrected;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public Direction getVarianceDirection()
/* 126:    */   {
/* 127:331 */     return this.varianceDirection;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setVarianceDirection(Direction varianceDirection)
/* 131:    */   {
/* 132:340 */     this.varianceDirection = varianceDirection;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static enum Direction
/* 136:    */   {
/* 137:352 */     UPSIDE(true),  DOWNSIDE(false);
/* 138:    */     
/* 139:    */     private boolean direction;
/* 140:    */     
/* 141:    */     private Direction(boolean b)
/* 142:    */     {
/* 143:371 */       this.direction = b;
/* 144:    */     }
/* 145:    */     
/* 146:    */     boolean getDirection()
/* 147:    */     {
/* 148:380 */       return this.direction;
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.moment.SemiVariance
 * JD-Core Version:    0.7.0.1
 */
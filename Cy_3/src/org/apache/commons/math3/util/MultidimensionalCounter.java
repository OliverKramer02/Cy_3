/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;

/*   4:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   5:    */ import org.apache.commons.math3.exception.NotStrictlyPositiveException;
/*   6:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   7:    */ 
/*   8:    */ public class MultidimensionalCounter
/*   9:    */   implements Iterable<Integer>
/*  10:    */ {
/*  11:    */   private final int dimension;
/*  12:    */   private final int[] uniCounterOffset;
/*  13:    */   private final int[] size;
/*  14:    */   private final int totalSize;
/*  15:    */   private final int last;
/*  16:    */   
/*  17:    */   public class Iterator
/*  19:    */   {
/*  20: 74 */     private final int[] counter = new int[MultidimensionalCounter.this.dimension];
/*  21: 78 */     private int count = -1;
/*  22:    */     
/*  23:    */     Iterator()
/*  24:    */     {
/*  25: 85 */       this.counter[MultidimensionalCounter.this.last] = -1;
/*  26:    */     }
/*  27:    */     
/*  28:    */     public boolean hasNext()
/*  29:    */     {
/*  30: 92 */       for (int i = 0; i < MultidimensionalCounter.this.dimension; i++) {
/*  31: 93 */         if (this.counter[i] != MultidimensionalCounter.this.size[i] - 1) {
/*  32: 94 */           return true;
/*  33:    */         }
/*  34:    */       }
/*  35: 97 */       return false;
/*  36:    */     }
/*  37:    */     
/*  38:    */     public Integer next()
/*  39:    */     {
/*  40:105 */       for (int i = MultidimensionalCounter.this.last; i >= 0; i--) {
/*  41:106 */         if (this.counter[i] == MultidimensionalCounter.this.size[i] - 1)
/*  42:    */         {
/*  43:107 */           this.counter[i] = 0;
/*  44:    */         }
/*  45:    */         else
/*  46:    */         {
/*  47:109 */           this.counter[i] += 1;
/*  48:110 */           break;
/*  49:    */         }
/*  50:    */       }
/*  51:114 */       return Integer.valueOf(++this.count);
/*  52:    */     }
/*  53:    */     
/*  54:    */     public int getCount()
/*  55:    */     {
/*  56:123 */       return this.count;
/*  57:    */     }
/*  58:    */     
/*  59:    */     public int[] getCounts()
/*  60:    */     {
/*  61:131 */       return MathArrays.copyOf(this.counter);
/*  62:    */     }
/*  63:    */     
/*  64:    */     public int getCount(int dim)
/*  65:    */     {
/*  66:146 */       return this.counter[dim];
/*  67:    */     }
/*  68:    */     
/*  69:    */     public void remove()
/*  70:    */     {
/*  71:153 */       throw new UnsupportedOperationException();
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public MultidimensionalCounter(int... size)
/*  76:    */   {
/*  77:165 */     this.dimension = size.length;
/*  78:166 */     this.size = MathArrays.copyOf(size);
/*  79:    */     
/*  80:168 */     this.uniCounterOffset = new int[this.dimension];
/*  81:    */     
/*  82:170 */     this.last = (this.dimension - 1);
/*  83:171 */     int tS = size[this.last];
/*  84:172 */     for (int i = 0; i < this.last; i++)
/*  85:    */     {
/*  86:173 */       int count = 1;
/*  87:174 */       for (int j = i + 1; j < this.dimension; j++) {
/*  88:175 */         count *= size[j];
/*  89:    */       }
/*  90:177 */       this.uniCounterOffset[i] = count;
/*  91:178 */       tS *= size[i];
/*  92:    */     }
/*  93:180 */     this.uniCounterOffset[this.last] = 0;
/*  94:182 */     if (tS <= 0) {
/*  95:183 */       throw new NotStrictlyPositiveException(Integer.valueOf(tS));
/*  96:    */     }
/*  97:186 */     this.totalSize = tS;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public java.util.Iterator<Integer> iterator()
/* 101:    */   {
/* 102:195 */     return (java.util.Iterator<Integer>) new Iterator();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public int getDimension()
/* 106:    */   {
/* 107:204 */     return this.dimension;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int[] getCounts(int index)
/* 111:    */   {
/* 112:216 */     if ((index < 0) || (index >= this.totalSize)) {
/* 113:218 */       throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.totalSize));
/* 114:    */     }
/* 115:221 */     int[] indices = new int[this.dimension];
/* 116:    */     
/* 117:223 */     int count = 0;
/* 118:224 */     for (int i = 0; i < this.last; i++)
/* 119:    */     {
/* 120:225 */       int idx = 0;
/* 121:226 */       int offset = this.uniCounterOffset[i];
/* 122:227 */       while (count <= index)
/* 123:    */       {
/* 124:228 */         count += offset;
/* 125:229 */         idx++;
/* 126:    */       }
/* 127:231 */       idx--;
/* 128:232 */       count -= offset;
/* 129:233 */       indices[i] = idx;
/* 130:    */     }
/* 131:236 */     indices[this.last] = (index - count);
/* 132:    */     
/* 133:238 */     return indices;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public int getCount(int... c)
/* 137:    */     throws OutOfRangeException
/* 138:    */   {
/* 139:253 */     if (c.length != this.dimension) {
/* 140:254 */       throw new DimensionMismatchException(c.length, this.dimension);
/* 141:    */     }
/* 142:256 */     int count = 0;
/* 143:257 */     for (int i = 0; i < this.dimension; i++)
/* 144:    */     {
/* 145:258 */       int index = c[i];
/* 146:259 */       if ((index < 0) || (index >= this.size[i])) {
/* 147:261 */         throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.size[i] - 1));
/* 148:    */       }
/* 149:263 */       count += this.uniCounterOffset[i] * c[i];
/* 150:    */     }
/* 151:265 */     return count + c[this.last];
/* 152:    */   }
/* 153:    */   
/* 154:    */   public int getSize()
/* 155:    */   {
/* 156:274 */     return this.totalSize;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public int[] getSizes()
/* 160:    */   {
/* 161:282 */     return MathArrays.copyOf(this.size);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public String toString()
/* 165:    */   {
/* 166:290 */     StringBuilder sb = new StringBuilder();
/* 167:291 */     for (int i = 0; i < this.dimension; i++) {
/* 168:292 */       sb.append("[").append(getCount(new int[] { i })).append("]");
/* 169:    */     }
/* 170:294 */     return sb.toString();
/* 171:    */   }
/* 172:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.MultidimensionalCounter
 * JD-Core Version:    0.7.0.1
 */
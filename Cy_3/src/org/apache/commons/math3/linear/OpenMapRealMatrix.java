/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   5:    */ import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
/*   6:    */ import org.apache.commons.math3.util.OpenIntToDoubleHashMap.Iterator;
/*   7:    */ 
/*   8:    */ public class OpenMapRealMatrix
/*   9:    */   extends AbstractRealMatrix
/*  10:    */   implements SparseRealMatrix, Serializable
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = -5962461716457143437L;
/*  13:    */   private final int rows;
/*  14:    */   private final int columns;
/*  15:    */   private final OpenIntToDoubleHashMap entries;
/*  16:    */   
/*  17:    */   public OpenMapRealMatrix(int rowDimension, int columnDimension)
/*  18:    */   {
/*  19: 49 */     super(rowDimension, columnDimension);
/*  20: 50 */     long lRow = rowDimension;
/*  21: 51 */     long lCol = columnDimension;
/*  22: 52 */     if (lRow * lCol >= 2147483647L) {
/*  23: 53 */       throw new NumberIsTooLargeException(Long.valueOf(lRow * lCol), Integer.valueOf(2147483647), false);
/*  24:    */     }
/*  25: 55 */     this.rows = rowDimension;
/*  26: 56 */     this.columns = columnDimension;
/*  27: 57 */     this.entries = new OpenIntToDoubleHashMap(0.0D);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public OpenMapRealMatrix(OpenMapRealMatrix matrix)
/*  31:    */   {
/*  32: 66 */     this.rows = matrix.rows;
/*  33: 67 */     this.columns = matrix.columns;
/*  34: 68 */     this.entries = new OpenIntToDoubleHashMap(matrix.entries);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public OpenMapRealMatrix copy()
/*  38:    */   {
/*  39: 74 */     return new OpenMapRealMatrix(this);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public OpenMapRealMatrix createMatrix(int rowDimension, int columnDimension)
/*  43:    */   {
/*  44: 80 */     return new OpenMapRealMatrix(rowDimension, columnDimension);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int getColumnDimension()
/*  48:    */   {
/*  49: 86 */     return this.columns;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public OpenMapRealMatrix add(OpenMapRealMatrix m)
/*  53:    */   {
/*  54:100 */     MatrixUtils.checkAdditionCompatible(this, m);
/*  55:    */     
/*  56:102 */     OpenMapRealMatrix out = new OpenMapRealMatrix(this);
/*  57:103 */     for (OpenIntToDoubleHashMap.Iterator iterator = m.entries.iterator(); iterator.hasNext();)
/*  58:    */     {
/*  59:104 */       iterator.advance();
/*  60:105 */       int row = iterator.key() / this.columns;
/*  61:106 */       int col = iterator.key() - row * this.columns;
/*  62:107 */       out.setEntry(row, col, getEntry(row, col) + iterator.value());
/*  63:    */     }
/*  64:110 */     return out;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public OpenMapRealMatrix subtract(RealMatrix m)
/*  68:    */   {
/*  69:    */     try
/*  70:    */     {
/*  71:118 */       return subtract((OpenMapRealMatrix)m);
/*  72:    */     }
/*  73:    */     catch (ClassCastException cce) {}
/*  74:120 */     return (OpenMapRealMatrix)super.subtract(m);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public OpenMapRealMatrix subtract(OpenMapRealMatrix m)
/*  78:    */   {
/*  79:134 */     MatrixUtils.checkAdditionCompatible(this, m);
/*  80:    */     
/*  81:136 */     OpenMapRealMatrix out = new OpenMapRealMatrix(this);
/*  82:137 */     for (OpenIntToDoubleHashMap.Iterator iterator = m.entries.iterator(); iterator.hasNext();)
/*  83:    */     {
/*  84:138 */       iterator.advance();
/*  85:139 */       int row = iterator.key() / this.columns;
/*  86:140 */       int col = iterator.key() - row * this.columns;
/*  87:141 */       out.setEntry(row, col, getEntry(row, col) - iterator.value());
/*  88:    */     }
/*  89:144 */     return out;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public RealMatrix multiply(RealMatrix m)
/*  93:    */   {
/*  94:    */     try
/*  95:    */     {
/*  96:151 */       return multiply((OpenMapRealMatrix)m);
/*  97:    */     }
/*  98:    */     catch (ClassCastException cce)
/*  99:    */     {
/* 100:155 */       MatrixUtils.checkMultiplicationCompatible(this, m);
/* 101:    */       
/* 102:157 */       int outCols = m.getColumnDimension();
/* 103:158 */       BlockRealMatrix out = new BlockRealMatrix(this.rows, outCols);
/* 104:159 */       for (OpenIntToDoubleHashMap.Iterator iterator = this.entries.iterator(); iterator.hasNext();)
/* 105:    */       {
/* 106:160 */         iterator.advance();
/* 107:161 */         double value = iterator.value();
/* 108:162 */         int key = iterator.key();
/* 109:163 */         int i = key / this.columns;
/* 110:164 */         int k = key % this.columns;
/* 111:165 */         for (int j = 0; j < outCols; j++) {
/* 112:166 */           out.addToEntry(i, j, value * m.getEntry(k, j));
/* 113:    */         }
/* 114:    */       }
/* 115:170 */       return out;
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public OpenMapRealMatrix multiply(OpenMapRealMatrix m)
/* 120:    */   {
/* 121:185 */     MatrixUtils.checkMultiplicationCompatible(this, m);
/* 122:    */     
/* 123:187 */     int outCols = m.getColumnDimension();
/* 124:188 */     OpenMapRealMatrix out = new OpenMapRealMatrix(this.rows, outCols);
/* 125:189 */     for (OpenIntToDoubleHashMap.Iterator iterator = this.entries.iterator(); iterator.hasNext();)
/* 126:    */     {
/* 127:190 */       iterator.advance();
/* 128:191 */       double value = iterator.value();
/* 129:192 */       int key = iterator.key();
/* 130:193 */       int i = key / this.columns;
/* 131:194 */       int k = key % this.columns;
/* 132:195 */       for (int j = 0; j < outCols; j++)
/* 133:    */       {
/* 134:196 */         int rightKey = m.computeKey(k, j);
/* 135:197 */         if (m.entries.containsKey(rightKey))
/* 136:    */         {
/* 137:198 */           int outKey = out.computeKey(i, j);
/* 138:199 */           double outValue = out.entries.get(outKey) + value * m.entries.get(rightKey);
/* 139:201 */           if (outValue == 0.0D) {
/* 140:202 */             out.entries.remove(outKey);
/* 141:    */           } else {
/* 142:204 */             out.entries.put(outKey, outValue);
/* 143:    */           }
/* 144:    */         }
/* 145:    */       }
/* 146:    */     }
/* 147:210 */     return out;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public double getEntry(int row, int column)
/* 151:    */   {
/* 152:216 */     MatrixUtils.checkRowIndex(this, row);
/* 153:217 */     MatrixUtils.checkColumnIndex(this, column);
/* 154:218 */     return this.entries.get(computeKey(row, column));
/* 155:    */   }
/* 156:    */   
/* 157:    */   public int getRowDimension()
/* 158:    */   {
/* 159:224 */     return this.rows;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setEntry(int row, int column, double value)
/* 163:    */   {
/* 164:230 */     MatrixUtils.checkRowIndex(this, row);
/* 165:231 */     MatrixUtils.checkColumnIndex(this, column);
/* 166:232 */     if (value == 0.0D) {
/* 167:233 */       this.entries.remove(computeKey(row, column));
/* 168:    */     } else {
/* 169:235 */       this.entries.put(computeKey(row, column), value);
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void addToEntry(int row, int column, double increment)
/* 174:    */   {
/* 175:242 */     MatrixUtils.checkRowIndex(this, row);
/* 176:243 */     MatrixUtils.checkColumnIndex(this, column);
/* 177:244 */     int key = computeKey(row, column);
/* 178:245 */     double value = this.entries.get(key) + increment;
/* 179:246 */     if (value == 0.0D) {
/* 180:247 */       this.entries.remove(key);
/* 181:    */     } else {
/* 182:249 */       this.entries.put(key, value);
/* 183:    */     }
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void multiplyEntry(int row, int column, double factor)
/* 187:    */   {
/* 188:256 */     MatrixUtils.checkRowIndex(this, row);
/* 189:257 */     MatrixUtils.checkColumnIndex(this, column);
/* 190:258 */     int key = computeKey(row, column);
/* 191:259 */     double value = this.entries.get(key) * factor;
/* 192:260 */     if (value == 0.0D) {
/* 193:261 */       this.entries.remove(key);
/* 194:    */     } else {
/* 195:263 */       this.entries.put(key, value);
/* 196:    */     }
/* 197:    */   }
/* 198:    */   
/* 199:    */   private int computeKey(int row, int column)
/* 200:    */   {
/* 201:274 */     return row * this.columns + column;
/* 202:    */   }
/* 203:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.OpenMapRealMatrix
 * JD-Core Version:    0.7.0.1
 */
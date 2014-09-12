/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.Field;
/*   4:    */ import org.apache.commons.math3.FieldElement;
/*   5:    */ import org.apache.commons.math3.util.OpenIntToFieldHashMap;
/*   6:    */ 
/*   7:    */ public class SparseFieldMatrix<T extends FieldElement<T>>
/*   8:    */   extends AbstractFieldMatrix<T>
/*   9:    */ {
/*  10:    */   private final OpenIntToFieldHashMap<T> entries;
/*  11:    */   private final int rows;
/*  12:    */   private final int columns;
/*  13:    */   
/*  14:    */   public SparseFieldMatrix(Field<T> field)
/*  15:    */   {
/*  16: 45 */     super(field);
/*  17: 46 */     this.rows = 0;
/*  18: 47 */     this.columns = 0;
/*  19: 48 */     this.entries = new OpenIntToFieldHashMap(field);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public SparseFieldMatrix(Field<T> field, int rowDimension, int columnDimension)
/*  23:    */   {
/*  24: 63 */     super(field, rowDimension, columnDimension);
/*  25: 64 */     this.rows = rowDimension;
/*  26: 65 */     this.columns = columnDimension;
/*  27: 66 */     this.entries = new OpenIntToFieldHashMap(field);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public SparseFieldMatrix(SparseFieldMatrix<T> other)
/*  31:    */   {
/*  32: 75 */     super(other.getField(), other.getRowDimension(), other.getColumnDimension());
/*  33: 76 */     this.rows = other.getRowDimension();
/*  34: 77 */     this.columns = other.getColumnDimension();
/*  35: 78 */     this.entries = new OpenIntToFieldHashMap(other.entries);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public SparseFieldMatrix(FieldMatrix<T> other)
/*  39:    */   {
/*  40: 87 */     super(other.getField(), other.getRowDimension(), other.getColumnDimension());
/*  41: 88 */     this.rows = other.getRowDimension();
/*  42: 89 */     this.columns = other.getColumnDimension();
/*  43: 90 */     this.entries = new OpenIntToFieldHashMap(getField());
/*  44: 91 */     for (int i = 0; i < this.rows; i++) {
/*  45: 92 */       for (int j = 0; j < this.columns; j++) {
/*  46: 93 */         setEntry(i, j, other.getEntry(i, j));
/*  47:    */       }
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void addToEntry(int row, int column, T increment)
/*  52:    */   {
/*  53:101 */     checkRowIndex(row);
/*  54:102 */     checkColumnIndex(column);
/*  55:103 */     int key = computeKey(row, column);
/*  56:104 */     T value = (T)this.entries.get(key).add(increment);
/*  57:105 */     if (((FieldElement)getField().getZero()).equals(value)) {
/*  58:106 */       this.entries.remove(key);
/*  59:    */     } else {
/*  60:108 */       this.entries.put(key, value);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public FieldMatrix<T> copy()
/*  65:    */   {
/*  66:115 */     return new SparseFieldMatrix(this);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension)
/*  70:    */   {
/*  71:121 */     return new SparseFieldMatrix(getField(), rowDimension, columnDimension);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getColumnDimension()
/*  75:    */   {
/*  76:127 */     return this.columns;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public T getEntry(int row, int column)
/*  80:    */   {
/*  81:133 */     checkRowIndex(row);
/*  82:134 */     checkColumnIndex(column);
/*  83:135 */     return this.entries.get(computeKey(row, column));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int getRowDimension()
/*  87:    */   {
/*  88:141 */     return this.rows;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void multiplyEntry(int row, int column, T factor)
/*  92:    */   {
/*  93:147 */     checkRowIndex(row);
/*  94:148 */     checkColumnIndex(column);
/*  95:149 */     int key = computeKey(row, column);
/*  96:150 */     T value = (T)this.entries.get(key).multiply(factor);
/*  97:151 */     if (((FieldElement)getField().getZero()).equals(value)) {
/*  98:152 */       this.entries.remove(key);
/*  99:    */     } else {
/* 100:154 */       this.entries.put(key, value);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setEntry(int row, int column, T value)
/* 105:    */   {
/* 106:162 */     checkRowIndex(row);
/* 107:163 */     checkColumnIndex(column);
/* 108:164 */     if (((FieldElement)getField().getZero()).equals(value)) {
/* 109:165 */       this.entries.remove(computeKey(row, column));
/* 110:    */     } else {
/* 111:167 */       this.entries.put(computeKey(row, column), value);
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   private int computeKey(int row, int column)
/* 116:    */   {
/* 117:179 */     return row * this.columns + column;
/* 118:    */   }
/* 119:    */
@Override
public FieldMatrix<T> preMultiply(FieldMatrix<T> paramFieldMatrix) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public T[] preMultiply(T[] paramArrayOfT) {
	// TODO Auto-generated method stub
	return null;
} }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.SparseFieldMatrix
 * JD-Core Version:    0.7.0.1
 */
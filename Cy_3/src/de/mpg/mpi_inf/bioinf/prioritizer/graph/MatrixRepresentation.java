/*   1:    */ package de.mpg.mpi_inf.bioinf.prioritizer.graph;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ 
/*   7:    */ public abstract class MatrixRepresentation
/*   8:    */ {
/*   9:    */   protected final Map<String, Integer> n2i;
/*  10:    */   protected final double alpha;
/*  11:    */   protected double[][] mat;
/*  12:    */   
/*  13:    */   protected MatrixRepresentation(Map<String, Integer> n2i, double alpha)
/*  14:    */   {
/*  15: 36 */     if ((alpha < 0.0D) || (alpha > 1.0D)) {
/*  16: 37 */       throw new IllegalArgumentException("alpha must be a double between 0 and 1");
/*  17:    */     }
/*  18: 38 */     this.n2i = n2i;
/*  19: 39 */     this.alpha = alpha;
/*  20: 40 */     this.mat = new double[n2i.size()][n2i.size()];
/*  21:    */   }
/*  22:    */   
/*  23:    */   public final boolean isWeighted()
/*  24:    */   {
/*  25: 49 */     return this.alpha > 0.0D;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public final Map<String, Integer> getNodeIndices()
/*  29:    */   {
/*  30: 58 */     return this.n2i;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public final double get(int i, int j)
/*  34:    */   {
/*  35: 68 */     return this.mat[i][j];
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final double getColumnSum(int j)
/*  39:    */   {
/*  40: 77 */     double csum = 0.0D;
/*  41: 78 */     for (int i = 0; i < this.mat.length; i++) {
/*  42: 78 */       csum += this.mat[i][j];
/*  43:    */     }
/*  44: 79 */     return csum;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public final double getRowSum(int i)
/*  48:    */   {
/*  49: 88 */     double rsum = 0.0D;
/*  50: 89 */     for (int j = 0; j < this.mat.length; j++) {
/*  51: 89 */       rsum += this.mat[i][j];
/*  52:    */     }
/*  53: 90 */     return rsum;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public MatrixRepresentation elWisePower(double exp)
/*  57:    */   {
/*  58:100 */     for (int i = 0; i < this.mat.length; i++) {
/*  59:101 */       for (int j = 0; j < this.mat.length; j++) {
/*  60:102 */         if (this.mat[i][j] > 0.0D) {
/*  61:103 */           this.mat[i][j] = Math.pow(this.mat[i][j], exp);
/*  62:    */         }
/*  63:    */       }
/*  64:    */     }
/*  65:104 */     return this;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public MatrixRepresentation elWiseAdd(MatrixRepresentation mr)
/*  69:    */   {
/*  70:114 */     if (this.mat.length != mr.dim()) {
/*  71:114 */       throw new IllegalArgumentException();
/*  72:    */     }
/*  73:115 */     for (int i = 0; i < this.mat.length; i++) {
/*  74:116 */       for (int j = 0; j < this.mat.length; j++) {
/*  75:117 */         this.mat[i][j] += mr.get(i, j);
/*  76:    */       }
/*  77:    */     }
/*  78:118 */     return this;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public MatrixRepresentation elWiseMul(MatrixRepresentation mr)
/*  82:    */   {
/*  83:128 */     if (this.mat.length != mr.dim()) {
/*  84:128 */       throw new IllegalArgumentException();
/*  85:    */     }
/*  86:129 */     for (int i = 0; i < this.mat.length; i++) {
/*  87:130 */       for (int j = 0; j < this.mat.length; j++) {
/*  88:131 */         this.mat[i][j] *= mr.get(i, j);
/*  89:    */       }
/*  90:    */     }
/*  91:132 */     return this;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int dim()
/*  95:    */   {
/*  96:136 */     return this.mat.length;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public double[] getRow(int i)
/* 100:    */   {
/* 101:140 */     return this.mat[i];
/* 102:    */   }
/* 103:    */   
/* 104:    */   public double[] getColumn(int j)
/* 105:    */   {
/* 106:144 */     double[] c = new double[this.mat.length];
/* 107:145 */     for (int i = 0; i < this.mat.length; i++) {
/* 108:145 */       c[i] = this.mat[i][j];
/* 109:    */     }
/* 110:146 */     return c;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public List<Integer> getPosRowPos(int i)
/* 114:    */   {
/* 115:150 */     List<Integer> pospos = new ArrayList();
/* 116:151 */     for (int j = 0; j < this.mat[i].length; j++) {
/* 117:151 */       if (this.mat[i][j] > 0.0D) {
/* 118:151 */         pospos.add(Integer.valueOf(j));
/* 119:    */       }
/* 120:    */     }
/* 121:152 */     return pospos;
/* 122:    */   }
/* 123:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.graph.MatrixRepresentation
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.FastMath;
/*   4:    */ 
/*   5:    */ public class RectangularCholeskyDecomposition
/*   6:    */ {
/*   7:    */   private final RealMatrix root;
/*   8:    */   private int rank;
/*   9:    */   
/*  10:    */   public RectangularCholeskyDecomposition(RealMatrix matrix, double small)
/*  11:    */     throws NonPositiveDefiniteMatrixException
/*  12:    */   {
/*  13: 65 */     int order = matrix.getRowDimension();
/*  14: 66 */     double[][] c = matrix.getData();
/*  15: 67 */     double[][] b = new double[order][order];
/*  16:    */     
/*  17: 69 */     int[] swap = new int[order];
/*  18: 70 */     int[] index = new int[order];
/*  19: 71 */     for (int i = 0; i < order; i++) {
/*  20: 72 */       index[i] = i;
/*  21:    */     }
/*  22: 75 */     int r = 0;
/*  23: 76 */     for (boolean loop = true; loop;)
/*  24:    */     {
/*  25: 79 */       swap[r] = r;
/*  26: 80 */       for (int i = r + 1; i < order; i++)
/*  27:    */       {
/*  28: 81 */         int ii = index[i];
/*  29: 82 */         int isi = index[swap[i]];
/*  30: 83 */         if (c[ii][ii] > c[isi][isi]) {
/*  31: 84 */           swap[r] = i;
/*  32:    */         }
/*  33:    */       }
/*  34: 90 */       if (swap[r] != r)
/*  35:    */       {
/*  36: 91 */         int tmp = index[r];
/*  37: 92 */         index[r] = index[swap[r]];
/*  38: 93 */         index[swap[r]] = tmp;
/*  39:    */       }
/*  40: 97 */       int ir = index[r];
/*  41: 98 */       if (c[ir][ir] < small)
/*  42:    */       {
/*  43:100 */         if (r == 0) {
/*  44:101 */           throw new NonPositiveDefiniteMatrixException(c[ir][ir], ir, small);
/*  45:    */         }
/*  46:105 */         for (int i = r; i < order; i++) {
/*  47:106 */           if (c[index[i]][index[i]] < -small) {
/*  48:109 */             throw new NonPositiveDefiniteMatrixException(c[index[i]][index[i]], i, small);
/*  49:    */           }
/*  50:    */         }
/*  51:115 */         r++;
/*  52:116 */         loop = false;
/*  53:    */       }
/*  54:    */       else
/*  55:    */       {
/*  56:121 */         double sqrt = FastMath.sqrt(c[ir][ir]);
/*  57:122 */         b[r][r] = sqrt;
/*  58:123 */         double inverse = 1.0D / sqrt;
/*  59:124 */         for (int i = r + 1; i < order; i++)
/*  60:    */         {
/*  61:125 */           int ii = index[i];
/*  62:126 */           double e = inverse * c[ii][ir];
/*  63:127 */           b[i][r] = e;
/*  64:128 */           c[ii][ii] -= e * e;
/*  65:129 */           for (int j = r + 1; j < i; j++)
/*  66:    */           {
/*  67:130 */             int ij = index[j];
/*  68:131 */             double f = c[ii][ij] - e * b[j][r];
/*  69:132 */             c[ii][ij] = f;
/*  70:133 */             c[ij][ii] = f;
/*  71:    */           }
/*  72:    */         }
/*  73:138 */         r++;loop = r < order;
/*  74:    */       }
/*  75:    */     }
/*  76:143 */     this.rank = r;
/*  77:144 */     this.root = MatrixUtils.createRealMatrix(order, r);
/*  78:145 */     for (int i = 0; i < order; i++) {
/*  79:146 */       for (int j = 0; j < r; j++) {
/*  80:147 */         this.root.setEntry(index[i], j, b[i][j]);
/*  81:    */       }
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public RealMatrix getRootMatrix()
/*  86:    */   {
/*  87:160 */     return this.root;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getRank()
/*  91:    */   {
/*  92:171 */     return this.rank;
/*  93:    */   }
/*  94:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.RectangularCholeskyDecomposition
 * JD-Core Version:    0.7.0.1
 */
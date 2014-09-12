/*   1:    */ package org.apache.commons.math3.ode.nonstiff;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.apache.commons.math3.fraction.BigFraction;
/*   7:    */ import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
/*   8:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*   9:    */ import org.apache.commons.math3.linear.ArrayFieldVector;
/*  10:    */ import org.apache.commons.math3.linear.DecompositionSolver;
/*  11:    */ import org.apache.commons.math3.linear.FieldDecompositionSolver;
/*  12:    */ import org.apache.commons.math3.linear.FieldLUDecomposition;
/*  13:    */ import org.apache.commons.math3.linear.FieldMatrix;
/*  14:    */ import org.apache.commons.math3.linear.FieldVector;
/*  15:    */ import org.apache.commons.math3.linear.MatrixUtils;
/*  16:    */ import org.apache.commons.math3.linear.QRDecomposition;
/*  17:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  18:    */ 
/*  19:    */ public class AdamsNordsieckTransformer
/*  20:    */ {
/*  21:138 */   private static final Map<Integer, AdamsNordsieckTransformer> CACHE = new HashMap();
/*  22:    */   private final Array2DRowRealMatrix update;
/*  23:    */   private final double[] c1;
/*  24:    */   
/*  25:    */   private AdamsNordsieckTransformer(int nSteps)
/*  26:    */   {
/*  27:154 */     FieldMatrix<BigFraction> bigP = buildP(nSteps);
/*  28:155 */     FieldDecompositionSolver<BigFraction> pSolver = new FieldLUDecomposition(bigP).getSolver();
/*  29:    */     
/*  30:    */ 
/*  31:158 */     BigFraction[] u = new BigFraction[nSteps];
/*  32:159 */     Arrays.fill(u, BigFraction.ONE);
/*  33:160 */     BigFraction[] bigC1 = (BigFraction[])pSolver.solve(new ArrayFieldVector(u, false)).toArray();
/*  34:    */     
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38:    */ 
/*  39:166 */     BigFraction[][] shiftedP = (BigFraction[][])bigP.getData();
/*  40:167 */     for (int i = shiftedP.length - 1; i > 0; i--) {
/*  41:169 */       shiftedP[i] = shiftedP[(i - 1)];
/*  42:    */     }
/*  43:171 */     shiftedP[0] = new BigFraction[nSteps];
/*  44:172 */     Arrays.fill(shiftedP[0], BigFraction.ZERO);
/*  45:173 */     FieldMatrix<BigFraction> bigMSupdate = pSolver.solve(new Array2DRowFieldMatrix(shiftedP, false));
/*  46:    */     
/*  47:    */ 
/*  48:    */ 
/*  49:177 */     this.update = MatrixUtils.bigFractionMatrixToRealMatrix(bigMSupdate);
/*  50:178 */     this.c1 = new double[nSteps];
/*  51:179 */     for (int i = 0; i < nSteps; i++) {
/*  52:180 */       this.c1[i] = bigC1[i].doubleValue();
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static AdamsNordsieckTransformer getInstance(int nSteps)
/*  57:    */   {
/*  58:191 */     synchronized (CACHE)
/*  59:    */     {
/*  60:192 */       AdamsNordsieckTransformer t = (AdamsNordsieckTransformer)CACHE.get(Integer.valueOf(nSteps));
/*  61:193 */       if (t == null)
/*  62:    */       {
/*  63:194 */         t = new AdamsNordsieckTransformer(nSteps);
/*  64:195 */         CACHE.put(Integer.valueOf(nSteps), t);
/*  65:    */       }
/*  66:197 */       return t;
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getNSteps()
/*  71:    */   {
/*  72:207 */     return this.c1.length;
/*  73:    */   }
/*  74:    */   
/*  75:    */   private FieldMatrix<BigFraction> buildP(int nSteps)
/*  76:    */   {
/*  77:225 */     BigFraction[][] pData = new BigFraction[nSteps][nSteps];
/*  78:227 */     for (int i = 0; i < pData.length; i++)
/*  79:    */     {
/*  80:229 */       BigFraction[] pI = pData[i];
/*  81:230 */       int factor = -(i + 1);
/*  82:231 */       int aj = factor;
/*  83:232 */       for (int j = 0; j < pI.length; j++)
/*  84:    */       {
/*  85:233 */         pI[j] = new BigFraction(aj * (j + 2));
/*  86:234 */         aj *= factor;
/*  87:    */       }
/*  88:    */     }
/*  89:238 */     return new Array2DRowFieldMatrix(pData, false);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public Array2DRowRealMatrix initializeHighOrderDerivatives(double h, double[] t, double[][] y, double[][] yDot)
/*  93:    */   {
/*  94:259 */     double[][] a = new double[2 * (y.length - 1)][this.c1.length];
/*  95:260 */     double[][] b = new double[2 * (y.length - 1)][y[0].length];
/*  96:261 */     double[] y0 = y[0];
/*  97:262 */     double[] yDot0 = yDot[0];
/*  98:263 */     for (int i = 1; i < y.length; i++)
/*  99:    */     {
/* 100:265 */       double di = t[i] - t[0];
/* 101:266 */       double ratio = di / h;
/* 102:267 */       double dikM1Ohk = 1.0D / h;
/* 103:    */       
/* 104:    */ 
/* 105:    */ 
/* 106:271 */       double[] aI = a[(2 * i - 2)];
/* 107:272 */       double[] aDotI = a[(2 * i - 1)];
/* 108:273 */       for (int j = 0; j < aI.length; j++)
/* 109:    */       {
/* 110:274 */         dikM1Ohk *= ratio;
/* 111:275 */         aI[j] = (di * dikM1Ohk);
/* 112:276 */         aDotI[j] = ((j + 2) * dikM1Ohk);
/* 113:    */       }
/* 114:280 */       double[] yI = y[i];
/* 115:281 */       double[] yDotI = yDot[i];
/* 116:282 */       double[] bI = b[(2 * i - 2)];
/* 117:283 */       double[] bDotI = b[(2 * i - 1)];
/* 118:284 */       for (int j = 0; j < yI.length; j++)
/* 119:    */       {
/* 120:285 */         bI[j] = (yI[j] - y0[j] - di * yDot0[j]);
/* 121:286 */         yDotI[j] -= yDot0[j];
/* 122:    */       }
/* 123:    */     }
/* 124:294 */     QRDecomposition decomposition = new QRDecomposition(new Array2DRowRealMatrix(a, false));
/* 125:295 */     RealMatrix x = decomposition.getSolver().solve(new Array2DRowRealMatrix(b, false));
/* 126:296 */     return new Array2DRowRealMatrix(x.getData(), false);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public Array2DRowRealMatrix updateHighOrderDerivativesPhase1(Array2DRowRealMatrix highOrder)
/* 130:    */   {
/* 131:311 */     return this.update.multiply(highOrder);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void updateHighOrderDerivativesPhase2(double[] start, double[] end, Array2DRowRealMatrix highOrder)
/* 135:    */   {
/* 136:330 */     double[][] data = highOrder.getDataRef();
/* 137:331 */     for (int i = 0; i < data.length; i++)
/* 138:    */     {
/* 139:332 */       double[] dataI = data[i];
/* 140:333 */       double c1I = this.c1[i];
/* 141:334 */       for (int j = 0; j < dataI.length; j++) {
/* 142:335 */         dataI[j] += c1I * (start[j] - end[j]);
/* 143:    */       }
/* 144:    */     }
/* 145:    */   }
/* 146:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.nonstiff.AdamsNordsieckTransformer
 * JD-Core Version:    0.7.0.1
 */
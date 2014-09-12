/*   1:    */ package org.apache.commons.math3.optimization.linear;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   6:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*   7:    */ import org.apache.commons.math3.util.Precision;
/*   8:    */ 
/*   9:    */ public class SimplexSolver
/*  10:    */   extends AbstractLinearOptimizer
/*  11:    */ {
/*  12:    */   private static final double DEFAULT_EPSILON = 1.0E-006D;
/*  13:    */   private static final int DEFAULT_ULPS = 10;
/*  14:    */   private final double epsilon;
/*  15:    */   private final int maxUlps;
/*  16:    */   
/*  17:    */   public SimplexSolver()
/*  18:    */   {
/*  19: 51 */     this(1.0E-006D, 10);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public SimplexSolver(double epsilon, int maxUlps)
/*  23:    */   {
/*  24: 60 */     this.epsilon = epsilon;
/*  25: 61 */     this.maxUlps = maxUlps;
/*  26:    */   }
/*  27:    */   
/*  28:    */   private Integer getPivotColumn(SimplexTableau tableau)
/*  29:    */   {
/*  30: 70 */     double minValue = 0.0D;
/*  31: 71 */     Integer minPos = null;
/*  32: 72 */     for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getWidth() - 1; i++)
/*  33:    */     {
/*  34: 73 */       double entry = tableau.getEntry(0, i);
/*  35: 74 */       if (Precision.compareTo(entry, minValue, this.maxUlps) < 0)
/*  36:    */       {
/*  37: 75 */         minValue = entry;
/*  38: 76 */         minPos = Integer.valueOf(i);
/*  39:    */       }
/*  40:    */     }
/*  41: 79 */     return minPos;
/*  42:    */   }
/*  43:    */   
/*  44:    */   private Integer getPivotRow(SimplexTableau tableau, int col)
/*  45:    */   {
/*  46: 90 */     List<Integer> minRatioPositions = new ArrayList();
/*  47: 91 */     double minRatio = 1.7976931348623157E+308D;
/*  48: 92 */     for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++)
/*  49:    */     {
/*  50: 93 */       double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
/*  51: 94 */       double entry = tableau.getEntry(i, col);
/*  52: 96 */       if (Precision.compareTo(entry, 0.0D, this.maxUlps) > 0)
/*  53:    */       {
/*  54: 97 */         double ratio = rhs / entry;
/*  55: 98 */         int cmp = Precision.compareTo(ratio, minRatio, this.maxUlps);
/*  56: 99 */         if (cmp == 0)
/*  57:    */         {
/*  58:100 */           minRatioPositions.add(Integer.valueOf(i));
/*  59:    */         }
/*  60:101 */         else if (cmp < 0)
/*  61:    */         {
/*  62:102 */           minRatio = ratio;
/*  63:103 */           minRatioPositions = new ArrayList();
/*  64:104 */           minRatioPositions.add(Integer.valueOf(i));
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68:109 */     if (minRatioPositions.size() == 0) {
/*  69:110 */       return null;
/*  70:    */     }
/*  71:111 */     if (minRatioPositions.size() > 1) {
/*  72:114 */       for (Integer row : minRatioPositions) {
/*  73:115 */         for (int i = 0; i < tableau.getNumArtificialVariables(); i++)
/*  74:    */         {
/*  75:116 */           int column = i + tableau.getArtificialVariableOffset();
/*  76:117 */           double entry = tableau.getEntry(row.intValue(), column);
/*  77:118 */           if ((Precision.equals(entry, 1.0D, this.maxUlps)) && (row.equals(tableau.getBasicRow(column)))) {
/*  78:120 */             return row;
/*  79:    */           }
/*  80:    */         }
/*  81:    */       }
/*  82:    */     }
/*  83:125 */     return (Integer)minRatioPositions.get(0);
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected void doIteration(SimplexTableau tableau)
/*  87:    */     throws MaxCountExceededException, UnboundedSolutionException
/*  88:    */   {
/*  89:137 */     incrementIterationsCounter();
/*  90:    */     
/*  91:139 */     Integer pivotCol = getPivotColumn(tableau);
/*  92:140 */     Integer pivotRow = getPivotRow(tableau, pivotCol.intValue());
/*  93:141 */     if (pivotRow == null) {
/*  94:142 */       throw new UnboundedSolutionException();
/*  95:    */     }
/*  96:146 */     double pivotVal = tableau.getEntry(pivotRow.intValue(), pivotCol.intValue());
/*  97:147 */     tableau.divideRow(pivotRow.intValue(), pivotVal);
/*  98:150 */     for (int i = 0; i < tableau.getHeight(); i++) {
/*  99:151 */       if (i != pivotRow.intValue())
/* 100:    */       {
/* 101:152 */         double multiplier = tableau.getEntry(i, pivotCol.intValue());
/* 102:153 */         tableau.subtractRow(i, pivotRow.intValue(), multiplier);
/* 103:    */       }
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected void solvePhase1(SimplexTableau tableau)
/* 108:    */     throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException
/* 109:    */   {
/* 110:169 */     if (tableau.getNumArtificialVariables() == 0) {
/* 111:170 */       return;
/* 112:    */     }
/* 113:173 */     while (!tableau.isOptimal()) {
/* 114:174 */       doIteration(tableau);
/* 115:    */     }
/* 116:178 */     if (!Precision.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0.0D, this.epsilon)) {
/* 117:179 */       throw new NoFeasibleSolutionException();
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public PointValuePair doOptimize()
/* 122:    */     throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException
/* 123:    */   {
/* 124:187 */     SimplexTableau tableau = new SimplexTableau(getFunction(), getConstraints(), getGoalType(), restrictToNonNegative(), this.epsilon, this.maxUlps);
/* 125:    */     
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:195 */     solvePhase1(tableau);
/* 133:196 */     tableau.dropPhase1Objective();
/* 134:198 */     while (!tableau.isOptimal()) {
/* 135:199 */       doIteration(tableau);
/* 136:    */     }
/* 137:201 */     return tableau.getSolution();
/* 138:    */   }
/* 139:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.SimplexSolver
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.optimization.linear;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.ObjectOutputStream;
/*   6:    */ import java.io.Serializable;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.HashSet;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Set;
/*  12:    */ import org.apache.commons.math3.linear.Array2DRowRealMatrix;
/*  13:    */ import org.apache.commons.math3.linear.MatrixUtils;
/*  14:    */ import org.apache.commons.math3.linear.RealMatrix;
/*  15:    */ import org.apache.commons.math3.linear.RealVector;
/*  16:    */ import org.apache.commons.math3.optimization.GoalType;
/*  17:    */ import org.apache.commons.math3.optimization.PointValuePair;
/*  18:    */ import org.apache.commons.math3.util.Precision;
/*  19:    */ 
/*  20:    */ class SimplexTableau
/*  21:    */   implements Serializable
/*  22:    */ {
/*  23:    */   private static final String NEGATIVE_VAR_COLUMN_LABEL = "x-";
/*  24:    */   private static final int DEFAULT_ULPS = 10;
/*  25:    */   private static final long serialVersionUID = -1369660067587938365L;
/*  26:    */   private final LinearObjectiveFunction f;
/*  27:    */   private final List<LinearConstraint> constraints;
/*  28:    */   private final boolean restrictToNonNegative;
/*  29: 84 */   private final List<String> columnLabels = new ArrayList();
/*  30:    */   private transient RealMatrix tableau;
/*  31:    */   private final int numDecisionVariables;
/*  32:    */   private final int numSlackVariables;
/*  33:    */   private int numArtificialVariables;
/*  34:    */   private final double epsilon;
/*  35:    */   private final int maxUlps;
/*  36:    */   
/*  37:    */   SimplexTableau(LinearObjectiveFunction f, Collection<LinearConstraint> constraints, GoalType goalType, boolean restrictToNonNegative, double epsilon)
/*  38:    */   {
/*  39:117 */     this(f, constraints, goalType, restrictToNonNegative, epsilon, 10);
/*  40:    */   }
/*  41:    */   
/*  42:    */   SimplexTableau(LinearObjectiveFunction f, Collection<LinearConstraint> constraints, GoalType goalType, boolean restrictToNonNegative, double epsilon, int maxUlps)
/*  43:    */   {
/*  44:135 */     this.f = f;
/*  45:136 */     this.constraints = normalizeConstraints(constraints);
/*  46:137 */     this.restrictToNonNegative = restrictToNonNegative;
/*  47:138 */     this.epsilon = epsilon;
/*  48:139 */     this.maxUlps = maxUlps;
/*  49:140 */     this.numDecisionVariables = (f.getCoefficients().getDimension() + (restrictToNonNegative ? 0 : 1));
/*  50:    */     
/*  51:142 */     this.numSlackVariables = (getConstraintTypeCounts(Relationship.LEQ) + getConstraintTypeCounts(Relationship.GEQ));
/*  52:    */     
/*  53:144 */     this.numArtificialVariables = (getConstraintTypeCounts(Relationship.EQ) + getConstraintTypeCounts(Relationship.GEQ));
/*  54:    */     
/*  55:146 */     this.tableau = createTableau(goalType == GoalType.MAXIMIZE);
/*  56:147 */     initializeColumnLabels();
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void initializeColumnLabels()
/*  60:    */   {
/*  61:154 */     if (getNumObjectiveFunctions() == 2) {
/*  62:155 */       this.columnLabels.add("W");
/*  63:    */     }
/*  64:157 */     this.columnLabels.add("Z");
/*  65:158 */     for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
/*  66:159 */       this.columnLabels.add("x" + i);
/*  67:    */     }
/*  68:161 */     if (!this.restrictToNonNegative) {
/*  69:162 */       this.columnLabels.add("x-");
/*  70:    */     }
/*  71:164 */     for (int i = 0; i < getNumSlackVariables(); i++) {
/*  72:165 */       this.columnLabels.add("s" + i);
/*  73:    */     }
/*  74:167 */     for (int i = 0; i < getNumArtificialVariables(); i++) {
/*  75:168 */       this.columnLabels.add("a" + i);
/*  76:    */     }
/*  77:170 */     this.columnLabels.add("RHS");
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected RealMatrix createTableau(boolean maximize)
/*  81:    */   {
/*  82:181 */     int width = this.numDecisionVariables + this.numSlackVariables + this.numArtificialVariables + getNumObjectiveFunctions() + 1;
/*  83:    */     
/*  84:183 */     int height = this.constraints.size() + getNumObjectiveFunctions();
/*  85:184 */     Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(height, width);
/*  86:187 */     if (getNumObjectiveFunctions() == 2) {
/*  87:188 */       matrix.setEntry(0, 0, -1.0D);
/*  88:    */     }
/*  89:190 */     int zIndex = getNumObjectiveFunctions() == 1 ? 0 : 1;
/*  90:191 */     matrix.setEntry(zIndex, zIndex, maximize ? 1.0D : -1.0D);
/*  91:192 */     RealVector objectiveCoefficients = maximize ? this.f.getCoefficients().mapMultiply(-1.0D) : this.f.getCoefficients();
/*  92:    */     
/*  93:194 */     copyArray(objectiveCoefficients.toArray(), matrix.getDataRef()[zIndex]);
/*  94:195 */     matrix.setEntry(zIndex, width - 1, maximize ? this.f.getConstantTerm() : -1.0D * this.f.getConstantTerm());
/*  95:198 */     if (!this.restrictToNonNegative) {
/*  96:199 */       matrix.setEntry(zIndex, getSlackVariableOffset() - 1, getInvertedCoefficientSum(objectiveCoefficients));
/*  97:    */     }
/*  98:204 */     int slackVar = 0;
/*  99:205 */     int artificialVar = 0;
/* 100:206 */     for (int i = 0; i < this.constraints.size(); i++)
/* 101:    */     {
/* 102:207 */       LinearConstraint constraint = (LinearConstraint)this.constraints.get(i);
/* 103:208 */       int row = getNumObjectiveFunctions() + i;
/* 104:    */       
/* 105:    */ 
/* 106:211 */       copyArray(constraint.getCoefficients().toArray(), matrix.getDataRef()[row]);
/* 107:214 */       if (!this.restrictToNonNegative) {
/* 108:215 */         matrix.setEntry(row, getSlackVariableOffset() - 1, getInvertedCoefficientSum(constraint.getCoefficients()));
/* 109:    */       }
/* 110:220 */       matrix.setEntry(row, width - 1, constraint.getValue());
/* 111:223 */       if (constraint.getRelationship() == Relationship.LEQ) {
/* 112:224 */         matrix.setEntry(row, getSlackVariableOffset() + slackVar++, 1.0D);
/* 113:225 */       } else if (constraint.getRelationship() == Relationship.GEQ) {
/* 114:226 */         matrix.setEntry(row, getSlackVariableOffset() + slackVar++, -1.0D);
/* 115:    */       }
/* 116:230 */       if ((constraint.getRelationship() == Relationship.EQ) || (constraint.getRelationship() == Relationship.GEQ))
/* 117:    */       {
/* 118:232 */         matrix.setEntry(0, getArtificialVariableOffset() + artificialVar, 1.0D);
/* 119:233 */         matrix.setEntry(row, getArtificialVariableOffset() + artificialVar++, 1.0D);
/* 120:234 */         matrix.setRowVector(0, matrix.getRowVector(0).subtract(matrix.getRowVector(row)));
/* 121:    */       }
/* 122:    */     }
/* 123:238 */     return matrix;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public List<LinearConstraint> normalizeConstraints(Collection<LinearConstraint> originalConstraints)
/* 127:    */   {
/* 128:247 */     List<LinearConstraint> normalized = new ArrayList();
/* 129:248 */     for (LinearConstraint constraint : originalConstraints) {
/* 130:249 */       normalized.add(normalize(constraint));
/* 131:    */     }
/* 132:251 */     return normalized;
/* 133:    */   }
/* 134:    */   
/* 135:    */   private LinearConstraint normalize(LinearConstraint constraint)
/* 136:    */   {
/* 137:260 */     if (constraint.getValue() < 0.0D) {
/* 138:261 */       return new LinearConstraint(constraint.getCoefficients().mapMultiply(-1.0D), constraint.getRelationship().oppositeRelationship(), -1.0D * constraint.getValue());
/* 139:    */     }
/* 140:265 */     return new LinearConstraint(constraint.getCoefficients(), constraint.getRelationship(), constraint.getValue());
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected final int getNumObjectiveFunctions()
/* 144:    */   {
/* 145:274 */     return this.numArtificialVariables > 0 ? 2 : 1;
/* 146:    */   }
/* 147:    */   
/* 148:    */   private int getConstraintTypeCounts(Relationship relationship)
/* 149:    */   {
/* 150:283 */     int count = 0;
/* 151:284 */     for (LinearConstraint constraint : this.constraints) {
/* 152:285 */       if (constraint.getRelationship() == relationship) {
/* 153:286 */         count++;
/* 154:    */       }
/* 155:    */     }
/* 156:289 */     return count;
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected static double getInvertedCoefficientSum(RealVector coefficients)
/* 160:    */   {
/* 161:298 */     double sum = 0.0D;
/* 162:299 */     for (double coefficient : coefficients.toArray()) {
/* 163:300 */       sum -= coefficient;
/* 164:    */     }
/* 165:302 */     return sum;
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected Integer getBasicRow(int col)
/* 169:    */   {
/* 170:311 */     Integer row = null;
/* 171:312 */     for (int i = 0; i < getHeight(); i++)
/* 172:    */     {
/* 173:313 */       double entry = getEntry(i, col);
/* 174:314 */       if ((Precision.equals(entry, 1.0D, this.maxUlps)) && (row == null)) {
/* 175:315 */         row = Integer.valueOf(i);
/* 176:316 */       } else if (!Precision.equals(entry, 0.0D, this.maxUlps)) {
/* 177:317 */         return null;
/* 178:    */       }
/* 179:    */     }
/* 180:320 */     return row;
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected void dropPhase1Objective()
/* 184:    */   {
/* 185:328 */     if (getNumObjectiveFunctions() == 1) {
/* 186:329 */       return;
/* 187:    */     }
/* 188:332 */     List<Integer> columnsToDrop = new ArrayList();
/* 189:333 */     columnsToDrop.add(Integer.valueOf(0));
/* 190:336 */     for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++)
/* 191:    */     {
/* 192:337 */       double entry = this.tableau.getEntry(0, i);
/* 193:338 */       if (Precision.compareTo(entry, 0.0D, this.maxUlps) > 0) {
/* 194:339 */         columnsToDrop.add(Integer.valueOf(i));
/* 195:    */       }
/* 196:    */     }
/* 197:344 */     for (int i = 0; i < getNumArtificialVariables(); i++)
/* 198:    */     {
/* 199:345 */       int col = i + getArtificialVariableOffset();
/* 200:346 */       if (getBasicRow(col) == null) {
/* 201:347 */         columnsToDrop.add(Integer.valueOf(col));
/* 202:    */       }
/* 203:    */     }
/* 204:351 */     double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
/* 205:352 */     for (int i = 1; i < getHeight(); i++)
/* 206:    */     {
/* 207:353 */       int col = 0;
/* 208:354 */       for (int j = 0; j < getWidth(); j++) {
/* 209:355 */         if (!columnsToDrop.contains(Integer.valueOf(j))) {
/* 210:356 */           matrix[(i - 1)][(col++)] = this.tableau.getEntry(i, j);
/* 211:    */         }
/* 212:    */       }
/* 213:    */     }
/* 214:361 */     for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
/* 215:362 */       this.columnLabels.remove(((Integer)columnsToDrop.get(i)).intValue());
/* 216:    */     }
/* 217:365 */     this.tableau = new Array2DRowRealMatrix(matrix);
/* 218:366 */     this.numArtificialVariables = 0;
/* 219:    */   }
/* 220:    */   
/* 221:    */   private void copyArray(double[] src, double[] dest)
/* 222:    */   {
/* 223:374 */     System.arraycopy(src, 0, dest, getNumObjectiveFunctions(), src.length);
/* 224:    */   }
/* 225:    */   
/* 226:    */   boolean isOptimal()
/* 227:    */   {
/* 228:382 */     for (int i = getNumObjectiveFunctions(); i < getWidth() - 1; i++)
/* 229:    */     {
/* 230:383 */       double entry = this.tableau.getEntry(0, i);
/* 231:384 */       if (Precision.compareTo(entry, 0.0D, this.epsilon) < 0) {
/* 232:385 */         return false;
/* 233:    */       }
/* 234:    */     }
/* 235:388 */     return true;
/* 236:    */   }
/* 237:    */   
/* 238:    */   protected PointValuePair getSolution()
/* 239:    */   {
/* 240:397 */     int negativeVarColumn = this.columnLabels.indexOf("x-");
/* 241:398 */     Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
/* 242:399 */     double mostNegative = negativeVarBasicRow == null ? 0.0D : getEntry(negativeVarBasicRow.intValue(), getRhsOffset());
/* 243:    */     
/* 244:401 */     Set<Integer> basicRows = new HashSet();
/* 245:402 */     double[] coefficients = new double[getOriginalNumDecisionVariables()];
/* 246:403 */     for (int i = 0; i < coefficients.length; i++)
/* 247:    */     {
/* 248:404 */       int colIndex = this.columnLabels.indexOf("x" + i);
/* 249:405 */       if (colIndex < 0)
/* 250:    */       {
/* 251:406 */         coefficients[i] = 0.0D;
/* 252:    */       }
/* 253:    */       else
/* 254:    */       {
/* 255:409 */         Integer basicRow = getBasicRow(colIndex);
/* 256:410 */         if ((basicRow != null) && (basicRow.intValue() == 0))
/* 257:    */         {
/* 258:414 */           coefficients[i] = 0.0D;
/* 259:    */         }
/* 260:415 */         else if (basicRows.contains(basicRow))
/* 261:    */         {
/* 262:418 */           coefficients[i] = (0.0D - (this.restrictToNonNegative ? 0.0D : mostNegative));
/* 263:    */         }
/* 264:    */         else
/* 265:    */         {
/* 266:420 */           basicRows.add(basicRow);
/* 267:421 */           coefficients[i] = ((basicRow == null ? 0.0D : getEntry(basicRow.intValue(), getRhsOffset())) - (this.restrictToNonNegative ? 0.0D : mostNegative));
/* 268:    */         }
/* 269:    */       }
/* 270:    */     }
/* 271:426 */     return new PointValuePair(coefficients, this.f.getValue(coefficients));
/* 272:    */   }
/* 273:    */   
/* 274:    */   protected void divideRow(int dividendRow, double divisor)
/* 275:    */   {
/* 276:439 */     for (int j = 0; j < getWidth(); j++) {
/* 277:440 */       this.tableau.setEntry(dividendRow, j, this.tableau.getEntry(dividendRow, j) / divisor);
/* 278:    */     }
/* 279:    */   }
/* 280:    */   
/* 281:    */   protected void subtractRow(int minuendRow, int subtrahendRow, double multiple)
/* 282:    */   {
/* 283:456 */     this.tableau.setRowVector(minuendRow, this.tableau.getRowVector(minuendRow).subtract(this.tableau.getRowVector(subtrahendRow).mapMultiply(multiple)));
/* 284:    */   }
/* 285:    */   
/* 286:    */   protected final int getWidth()
/* 287:    */   {
/* 288:465 */     return this.tableau.getColumnDimension();
/* 289:    */   }
/* 290:    */   
/* 291:    */   protected final int getHeight()
/* 292:    */   {
/* 293:473 */     return this.tableau.getRowDimension();
/* 294:    */   }
/* 295:    */   
/* 296:    */   protected final double getEntry(int row, int column)
/* 297:    */   {
/* 298:482 */     return this.tableau.getEntry(row, column);
/* 299:    */   }
/* 300:    */   
/* 301:    */   protected final void setEntry(int row, int column, double value)
/* 302:    */   {
/* 303:492 */     this.tableau.setEntry(row, column, value);
/* 304:    */   }
/* 305:    */   
/* 306:    */   protected final int getSlackVariableOffset()
/* 307:    */   {
/* 308:500 */     return getNumObjectiveFunctions() + this.numDecisionVariables;
/* 309:    */   }
/* 310:    */   
/* 311:    */   protected final int getArtificialVariableOffset()
/* 312:    */   {
/* 313:508 */     return getNumObjectiveFunctions() + this.numDecisionVariables + this.numSlackVariables;
/* 314:    */   }
/* 315:    */   
/* 316:    */   protected final int getRhsOffset()
/* 317:    */   {
/* 318:516 */     return getWidth() - 1;
/* 319:    */   }
/* 320:    */   
/* 321:    */   protected final int getNumDecisionVariables()
/* 322:    */   {
/* 323:530 */     return this.numDecisionVariables;
/* 324:    */   }
/* 325:    */   
/* 326:    */   protected final int getOriginalNumDecisionVariables()
/* 327:    */   {
/* 328:539 */     return this.f.getCoefficients().getDimension();
/* 329:    */   }
/* 330:    */   
/* 331:    */   protected final int getNumSlackVariables()
/* 332:    */   {
/* 333:547 */     return this.numSlackVariables;
/* 334:    */   }
/* 335:    */   
/* 336:    */   protected final int getNumArtificialVariables()
/* 337:    */   {
/* 338:555 */     return this.numArtificialVariables;
/* 339:    */   }
/* 340:    */   
/* 341:    */   protected final double[][] getData()
/* 342:    */   {
/* 343:563 */     return this.tableau.getData();
/* 344:    */   }
/* 345:    */   
/* 346:    */   public boolean equals(Object other)
/* 347:    */   {
/* 348:570 */     if (this == other) {
/* 349:571 */       return true;
/* 350:    */     }
/* 351:574 */     if ((other instanceof SimplexTableau))
/* 352:    */     {
/* 353:575 */       SimplexTableau rhs = (SimplexTableau)other;
/* 354:576 */       return (this.restrictToNonNegative == rhs.restrictToNonNegative) && (this.numDecisionVariables == rhs.numDecisionVariables) && (this.numSlackVariables == rhs.numSlackVariables) && (this.numArtificialVariables == rhs.numArtificialVariables) && (this.epsilon == rhs.epsilon) && (this.maxUlps == rhs.maxUlps) && (this.f.equals(rhs.f)) && (this.constraints.equals(rhs.constraints)) && (this.tableau.equals(rhs.tableau));
/* 355:    */     }
/* 356:586 */     return false;
/* 357:    */   }
/* 358:    */   
/* 359:    */   public int hashCode()
/* 360:    */   {
/* 361:592 */     return Boolean.valueOf(this.restrictToNonNegative).hashCode() ^ this.numDecisionVariables ^ this.numSlackVariables ^ this.numArtificialVariables ^ Double.valueOf(this.epsilon).hashCode() ^ this.maxUlps ^ this.f.hashCode() ^ this.constraints.hashCode() ^ this.tableau.hashCode();
/* 362:    */   }
/* 363:    */   
/* 364:    */   private void writeObject(ObjectOutputStream oos)
/* 365:    */     throws IOException
/* 366:    */   {
/* 367:609 */     oos.defaultWriteObject();
/* 368:610 */     MatrixUtils.serializeRealMatrix(this.tableau, oos);
/* 369:    */   }
/* 370:    */   
/* 371:    */   private void readObject(ObjectInputStream ois)
/* 372:    */     throws ClassNotFoundException, IOException
/* 373:    */   {
/* 374:620 */     ois.defaultReadObject();
/* 375:621 */     MatrixUtils.deserializeRealMatrix(this, "tableau", ois);
/* 376:    */   }
/* 377:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.SimplexTableau
 * JD-Core Version:    0.7.0.1
 */
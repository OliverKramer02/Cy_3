/*   1:    */ package org.apache.commons.math3.linear;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   4:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   5:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.util.ExceptionContext;
/*   7:    */ import org.apache.commons.math3.util.IterationManager;
/*   8:    */ 
/*   9:    */ public class ConjugateGradient
/*  10:    */   extends PreconditionedIterativeLinearSolver
/*  11:    */ {
/*  12:    */   public static final String OPERATOR = "operator";
/*  13:    */   public static final String VECTOR = "vector";
/*  14:    */   private boolean check;
/*  15:    */   private final double delta;
/*  16:    */   
/*  17:    */   public ConjugateGradient(int maxIterations, double delta, boolean check)
/*  18:    */   {
/*  19:108 */     super(maxIterations);
/*  20:109 */     this.delta = delta;
/*  21:110 */     this.check = check;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ConjugateGradient(IterationManager manager, double delta, boolean check)
/*  25:    */   {
/*  26:124 */     super(manager);
/*  27:125 */     this.delta = delta;
/*  28:126 */     this.check = check;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public final boolean getCheck()
/*  32:    */   {
/*  33:136 */     return this.check;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public RealVector solveInPlace(RealLinearOperator a, RealLinearOperator minv, RealVector b, RealVector x0)
/*  37:    */     throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException
/*  38:    */   {
/*  39:145 */     checkParameters(a, minv, b, x0);
/*  40:146 */     IterationManager manager = getIterationManager();
/*  41:    */     
/*  42:148 */     manager.resetIterationCount();
/*  43:149 */     double rmax = this.delta * b.getNorm();
/*  44:150 */     RealVector bro = RealVector.unmodifiableRealVector(b);
/*  45:    */     
/*  46:    */ 
/*  47:153 */     manager.incrementIterationCount();
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51:157 */     RealVector x = x0;
/*  52:158 */     RealVector xro = RealVector.unmodifiableRealVector(x);
/*  53:159 */     RealVector p = x.copy();
/*  54:160 */     RealVector q = a.operate(p);
/*  55:    */     
/*  56:162 */     RealVector r = b.combine(1.0D, -1.0D, q);
/*  57:163 */     RealVector rro = RealVector.unmodifiableRealVector(r);
/*  58:164 */     double rnorm = r.getNorm();
/*  59:    */     RealVector z;
/*  60:    */    
/*  61:166 */     if (minv == null) {
/*  62:167 */       z = r;
/*  63:    */     } else {
/*  64:169 */       z = null;
/*  65:    */     }
/*  66:172 */     IterativeLinearSolverEvent evt = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm);
/*  67:    */     
/*  68:174 */     manager.fireInitializationEvent(evt);
/*  69:175 */     if (rnorm <= rmax)
/*  70:    */     {
/*  71:176 */       manager.fireTerminationEvent(evt);
/*  72:177 */       return x;
/*  73:    */     }
/*  74:179 */     double rhoPrev = 0.0D;
/*  75:    */     for (;;)
/*  76:    */     {
/*  77:181 */       manager.incrementIterationCount();
/*  78:182 */       evt = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm);
/*  79:    */       
/*  80:184 */       manager.fireIterationStartedEvent(evt);
/*  81:185 */       if (minv != null) {
/*  82:186 */         z = minv.operate(r);
/*  83:    */       }
/*  84:188 */       double rhoNext = r.dotProduct(z);
/*  85:189 */       if ((this.check) && (rhoNext <= 0.0D))
/*  86:    */       {
/*  87:191 */         NonPositiveDefiniteOperatorException e = new NonPositiveDefiniteOperatorException();
/*  88:192 */         ExceptionContext context = e.getContext();
/*  89:193 */         context.setValue("operator", minv);
/*  90:194 */         context.setValue("vector", r);
/*  91:195 */         throw e;
/*  92:    */       }
/*  93:197 */       if (manager.getIterations() == 2) {
/*  94:198 */         p.setSubVector(0, z);
/*  95:    */       } else {
/*  96:200 */         p.combineToSelf(rhoNext / rhoPrev, 1.0D, z);
/*  97:    */       }
/*  98:202 */       q = a.operate(p);
/*  99:203 */       double pq = p.dotProduct(q);
/* 100:204 */       if ((this.check) && (pq <= 0.0D))
/* 101:    */       {
/* 102:206 */         NonPositiveDefiniteOperatorException e = new NonPositiveDefiniteOperatorException();
/* 103:207 */         ExceptionContext context = e.getContext();
/* 104:208 */         context.setValue("operator", a);
/* 105:209 */         context.setValue("vector", p);
/* 106:210 */         throw e;
/* 107:    */       }
/* 108:212 */       double alpha = rhoNext / pq;
/* 109:213 */       x.combineToSelf(1.0D, alpha, p);
/* 110:214 */       r.combineToSelf(1.0D, -alpha, q);
/* 111:215 */       rhoPrev = rhoNext;
/* 112:216 */       rnorm = r.getNorm();
/* 113:217 */       evt = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm);
/* 114:    */       
/* 115:219 */       manager.fireIterationPerformedEvent(evt);
/* 116:220 */       if (rnorm <= rmax)
/* 117:    */       {
/* 118:221 */         manager.fireTerminationEvent(evt);
/* 119:222 */         return x;
/* 120:    */       }
/* 121:    */     }
/* 122:    */   }
/* 123:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.linear.ConjugateGradient
 * JD-Core Version:    0.7.0.1
 */
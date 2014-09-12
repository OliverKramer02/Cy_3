/*   1:    */ package org.apache.commons.math3.ode.events;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.analysis.UnivariateFunction;
/*   4:    */ import org.apache.commons.math3.analysis.solvers.AllowedSolution;
/*   5:    */ import org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
/*   6:    */ import org.apache.commons.math3.analysis.solvers.PegasusSolver;
/*   7:    */ import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
/*   8:    */ import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
/*   9:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*  10:    */ import org.apache.commons.math3.ode.sampling.StepInterpolator;
/*  11:    */ import org.apache.commons.math3.util.FastMath;
/*  12:    */ 
/*  13:    */ public class EventState
/*  14:    */ {
/*  15:    */   private final EventHandler handler;
/*  16:    */   private final double maxCheckInterval;
/*  17:    */   private final double convergence;
/*  18:    */   private final int maxIterationCount;
/*  19:    */   private double t0;
/*  20:    */   private double g0;
/*  21:    */   private boolean g0Positive;
/*  22:    */   private boolean pendingEvent;
/*  23:    */   private double pendingEventTime;
/*  24:    */   private double previousEventTime;
/*  25:    */   private boolean forward;
/*  26:    */   private boolean increasing;
/*  27:    */   private EventHandler.Action nextAction;
/*  28:    */   private final UnivariateSolver solver;
/*  29:    */   
/*  30:    */   public EventState(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver)
/*  31:    */   {
/*  32:103 */     this.handler = handler;
/*  33:104 */     this.maxCheckInterval = maxCheckInterval;
/*  34:105 */     this.convergence = FastMath.abs(convergence);
/*  35:106 */     this.maxIterationCount = maxIterationCount;
/*  36:107 */     this.solver = solver;
/*  37:    */     
/*  38:    */ 
/*  39:110 */     this.t0 = (0.0D / 0.0D);
/*  40:111 */     this.g0 = (0.0D / 0.0D);
/*  41:112 */     this.g0Positive = true;
/*  42:113 */     this.pendingEvent = false;
/*  43:114 */     this.pendingEventTime = (0.0D / 0.0D);
/*  44:115 */     this.previousEventTime = (0.0D / 0.0D);
/*  45:116 */     this.increasing = true;
/*  46:117 */     this.nextAction = EventHandler.Action.CONTINUE;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public EventHandler getEventHandler()
/*  50:    */   {
/*  51:125 */     return this.handler;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public double getMaxCheckInterval()
/*  55:    */   {
/*  56:132 */     return this.maxCheckInterval;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double getConvergence()
/*  60:    */   {
/*  61:139 */     return this.convergence;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int getMaxIterationCount()
/*  65:    */   {
/*  66:146 */     return this.maxIterationCount;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void reinitializeBegin(StepInterpolator interpolator)
/*  70:    */   {
/*  71:154 */     this.t0 = interpolator.getPreviousTime();
/*  72:155 */     interpolator.setInterpolatedTime(this.t0);
/*  73:156 */     this.g0 = this.handler.g(this.t0, interpolator.getInterpolatedState());
/*  74:157 */     if (this.g0 == 0.0D)
/*  75:    */     {
/*  76:171 */       double epsilon = FastMath.max(this.solver.getAbsoluteAccuracy(), FastMath.abs(this.solver.getRelativeAccuracy() * this.t0));
/*  77:    */       
/*  78:173 */       double tStart = this.t0 + 0.5D * epsilon;
/*  79:174 */       interpolator.setInterpolatedTime(tStart);
/*  80:175 */       this.g0 = this.handler.g(tStart, interpolator.getInterpolatedState());
/*  81:    */     }
/*  82:177 */     this.g0Positive = (this.g0 >= 0.0D);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public boolean evaluateStep(final StepInterpolator interpolator)
/*  86:    */     throws ConvergenceException
/*  87:    */   {
/*  88:190 */     this.forward = interpolator.isForward();
/*  89:191 */     double t1 = interpolator.getCurrentTime();
/*  90:192 */     double dt = t1 - this.t0;
/*  91:193 */     if (FastMath.abs(dt) < this.convergence) {
/*  92:195 */       return false;
/*  93:    */     }
/*  94:197 */     int n = FastMath.max(1, (int)FastMath.ceil(FastMath.abs(dt) / this.maxCheckInterval));
/*  95:198 */     double h = dt / n;
/*  96:    */     
/*  97:200 */     UnivariateFunction f = new UnivariateFunction()
/*  98:    */     {
/*  99:    */       public double value(double t)
/* 100:    */       {
/* 101:202 */         interpolator.setInterpolatedTime(t);
/* 102:203 */         return EventState.this.handler.g(t, interpolator.getInterpolatedState());
/* 103:    */       }
/* 104:206 */     };
/* 105:207 */     double ta = this.t0;
/* 106:208 */     double ga = this.g0;
/* 107:209 */     for (int i = 0; i < n; i++)
/* 108:    */     {
/* 109:212 */       double tb = this.t0 + (i + 1) * h;
/* 110:213 */       interpolator.setInterpolatedTime(tb);
/* 111:214 */       double gb = this.handler.g(tb, interpolator.getInterpolatedState());
/* 112:217 */       if ((this.g0Positive ^ gb >= 0.0D))
/* 113:    */       {
/* 114:221 */         this.increasing = (gb >= ga);
/* 115:    */         double root;
/* 116:    */         
/* 117:225 */         if ((this.solver instanceof BracketedUnivariateSolver))
/* 118:    */         {
/* 119:227 */           BracketedUnivariateSolver<UnivariateFunction> bracketing = (BracketedUnivariateSolver)this.solver;
/* 120:    */           
/* 121:229 */           root = this.forward ? bracketing.solve(this.maxIterationCount, f, ta, tb, AllowedSolution.RIGHT_SIDE) : bracketing.solve(this.maxIterationCount, f, tb, ta, AllowedSolution.LEFT_SIDE);
/* 122:    */         }
/* 123:    */         else
/* 124:    */         {
/* 125:233 */           double baseRoot = this.forward ? this.solver.solve(this.maxIterationCount, f, ta, tb) : this.solver.solve(this.maxIterationCount, f, tb, ta);
/* 126:    */           
/* 127:    */ 
/* 128:236 */           int remainingEval = this.maxIterationCount - this.solver.getEvaluations();
/* 129:237 */           BracketedUnivariateSolver<UnivariateFunction> bracketing = new PegasusSolver(this.solver.getRelativeAccuracy(), this.solver.getAbsoluteAccuracy());
/* 130:    */           
/* 131:239 */           root = this.forward ? UnivariateSolverUtils.forceSide(remainingEval, f, bracketing, baseRoot, ta, tb, AllowedSolution.RIGHT_SIDE) : UnivariateSolverUtils.forceSide(remainingEval, f, bracketing, baseRoot, tb, ta, AllowedSolution.LEFT_SIDE);
/* 132:    */         }
/* 133:246 */         if ((!Double.isNaN(this.previousEventTime)) && (FastMath.abs(root - ta) <= this.convergence) && (FastMath.abs(root - this.previousEventTime) <= this.convergence))
/* 134:    */         {
/* 135:251 */           ta = this.forward ? ta + this.convergence : ta - this.convergence;
/* 136:252 */           ga = f.value(ta);
/* 137:253 */           i--;
/* 138:    */         }
/* 139:    */         else
/* 140:    */         {
/* 141:254 */           if ((Double.isNaN(this.previousEventTime)) || (FastMath.abs(this.previousEventTime - root) > this.convergence))
/* 142:    */           {
/* 143:256 */             this.pendingEventTime = root;
/* 144:257 */             this.pendingEvent = true;
/* 145:258 */             return true;
/* 146:    */           }
/* 147:261 */           ta = tb;
/* 148:262 */           ga = gb;
/* 149:    */         }
/* 150:    */       }
/* 151:    */       else
/* 152:    */       {
/* 153:267 */         ta = tb;
/* 154:268 */         ga = gb;
/* 155:    */       }
/* 156:    */     }
/* 157:274 */     this.pendingEvent = false;
/* 158:275 */     this.pendingEventTime = (0.0D / 0.0D);
/* 159:276 */     return false;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public double getEventTime()
/* 163:    */   {
/* 164:285 */     return this.forward ? (1.0D / 0.0D) : this.pendingEvent ? this.pendingEventTime : (-1.0D / 0.0D);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void stepAccepted(double t, double[] y)
/* 168:    */   {
/* 169:298 */     this.t0 = t;
/* 170:299 */     this.g0 = this.handler.g(t, y);
/* 171:301 */     if ((this.pendingEvent) && (FastMath.abs(this.pendingEventTime - t) <= this.convergence))
/* 172:    */     {
/* 173:303 */       this.previousEventTime = t;
/* 174:304 */       this.g0Positive = this.increasing;
/* 175:305 */       this.nextAction = this.handler.eventOccurred(t, y, !(this.increasing ^ this.forward));
/* 176:    */     }
/* 177:    */     else
/* 178:    */     {
/* 179:307 */       this.g0Positive = (this.g0 >= 0.0D);
/* 180:308 */       this.nextAction = EventHandler.Action.CONTINUE;
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   public boolean stop()
/* 185:    */   {
/* 186:317 */     return this.nextAction == EventHandler.Action.STOP;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public boolean reset(double t, double[] y)
/* 190:    */   {
/* 191:329 */     if ((!this.pendingEvent) || (FastMath.abs(this.pendingEventTime - t) > this.convergence)) {
/* 192:330 */       return false;
/* 193:    */     }
/* 194:333 */     if (this.nextAction == EventHandler.Action.RESET_STATE) {
/* 195:334 */       this.handler.resetState(t, y);
/* 196:    */     }
/* 197:336 */     this.pendingEvent = false;
/* 198:337 */     this.pendingEventTime = (0.0D / 0.0D);
/* 199:    */     
/* 200:339 */     return (this.nextAction == EventHandler.Action.RESET_STATE) || (this.nextAction == EventHandler.Action.RESET_DERIVATIVES);
/* 201:    */   }
/* 202:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.events.EventState
 * JD-Core Version:    0.7.0.1
 */
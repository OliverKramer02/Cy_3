/*   1:    */ package org.apache.commons.math3.ode;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Comparator;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.SortedSet;
/*  10:    */ import java.util.TreeSet;

/*  11:    */ import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
/*  12:    */ import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
/*  13:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  14:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  15:    */ import org.apache.commons.math3.exception.MathIllegalStateException;
/*  16:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*  17:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  18:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  19:    */ import org.apache.commons.math3.ode.events.EventHandler;
/*  20:    */ import org.apache.commons.math3.ode.events.EventState;
/*  21:    */ import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
/*  22:    */ import org.apache.commons.math3.ode.sampling.StepHandler;
/*  23:    */ import org.apache.commons.math3.util.FastMath;
/*  24:    */ import org.apache.commons.math3.util.Incrementor;
/*  25:    */ import org.apache.commons.math3.util.Precision;
/*  26:    */ 
/*  27:    */ public abstract class AbstractIntegrator
/*  28:    */   implements FirstOrderIntegrator
/*  29:    */ {
/*  30:    */   protected Collection<StepHandler> stepHandlers;
/*  31:    */   protected double stepStart;
/*  32:    */   protected double stepSize;
/*  33:    */   protected boolean isLastStep;
/*  34:    */   protected boolean resetOccurred;
/*  35:    */   private Collection<EventState> eventsStates;
/*  36:    */   private boolean statesInitialized;
/*  37:    */   private final String name;
/*  38:    */   private Incrementor evaluations;
/*  39:    */   private transient ExpandableStatefulODE expandable;
/*  40:    */   
/*  41:    */   public AbstractIntegrator(String name)
/*  42:    */   {
/*  43: 86 */     this.name = name;
/*  44: 87 */     this.stepHandlers = new ArrayList();
/*  45: 88 */     this.stepStart = (0.0D / 0.0D);
/*  46: 89 */     this.stepSize = (0.0D / 0.0D);
/*  47: 90 */     this.eventsStates = new ArrayList();
/*  48: 91 */     this.statesInitialized = false;
/*  49: 92 */     this.evaluations = new Incrementor();
/*  50: 93 */     setMaxEvaluations(-1);
/*  51: 94 */     this.evaluations.resetCount();
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected AbstractIntegrator()
/*  55:    */   {
/*  56:100 */     this(null);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getName()
/*  60:    */   {
/*  61:105 */     return this.name;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void addStepHandler(StepHandler handler)
/*  65:    */   {
/*  66:110 */     this.stepHandlers.add(handler);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Collection<StepHandler> getStepHandlers()
/*  70:    */   {
/*  71:115 */     return Collections.unmodifiableCollection(this.stepHandlers);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void clearStepHandlers()
/*  75:    */   {
/*  76:120 */     this.stepHandlers.clear();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void addEventHandler(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount)
/*  80:    */   {
/*  81:128 */     addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, new BracketingNthOrderBrentSolver(convergence, 5));
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void addEventHandler(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver)
/*  85:    */   {
/*  86:139 */     this.eventsStates.add(new EventState(handler, maxCheckInterval, convergence, maxIterationCount, solver));
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Collection<EventHandler> getEventHandlers()
/*  90:    */   {
/*  91:145 */     List<EventHandler> list = new ArrayList();
/*  92:146 */     for (EventState state : this.eventsStates) {
/*  93:147 */       list.add(state.getEventHandler());
/*  94:    */     }
/*  95:149 */     return Collections.unmodifiableCollection(list);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void clearEventHandlers()
/*  99:    */   {
/* 100:154 */     this.eventsStates.clear();
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double getCurrentStepStart()
/* 104:    */   {
/* 105:159 */     return this.stepStart;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public double getCurrentSignedStepsize()
/* 109:    */   {
/* 110:164 */     return this.stepSize;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setMaxEvaluations(int maxEvaluations)
/* 114:    */   {
/* 115:169 */     this.evaluations.setMaximalCount(maxEvaluations < 0 ? 2147483647 : maxEvaluations);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public int getMaxEvaluations()
/* 119:    */   {
/* 120:174 */     return this.evaluations.getMaximalCount();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public int getEvaluations()
/* 124:    */   {
/* 125:179 */     return this.evaluations.getCount();
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected void initIntegration(double t0, double[] y0, double t)
/* 129:    */   {
/* 130:189 */     this.evaluations.resetCount();
/* 131:191 */     for (EventState state : this.eventsStates) {
/* 132:192 */       state.getEventHandler().init(t0, y0, t);
/* 133:    */     }
/* 134:195 */     for (StepHandler handler : this.stepHandlers) {
/* 135:196 */       handler.init(t0, y0, t);
/* 136:    */     }
/* 137:199 */     setStateInitialized(false);
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected void setEquations(ExpandableStatefulODE equations)
/* 141:    */   {
/* 142:207 */     this.expandable = equations;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
/* 146:    */     throws MathIllegalStateException, MathIllegalArgumentException
/* 147:    */   {
/* 148:215 */     if (y0.length != equations.getDimension()) {
/* 149:216 */       throw new DimensionMismatchException(y0.length, equations.getDimension());
/* 150:    */     }
/* 151:218 */     if (y.length != equations.getDimension()) {
/* 152:219 */       throw new DimensionMismatchException(y.length, equations.getDimension());
/* 153:    */     }
/* 154:223 */     ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
/* 155:224 */     expandableODE.setTime(t0);
/* 156:225 */     expandableODE.setPrimaryState(y0);
/* 157:    */     
/* 158:    */ 
/* 159:228 */     integrate(expandableODE, t);
/* 160:    */     
/* 161:    */ 
/* 162:231 */     System.arraycopy(expandableODE.getPrimaryState(), 0, y, 0, y.length);
/* 163:232 */     return expandableODE.getTime();
/* 164:    */   }
/* 165:    */   
/* 166:    */   public abstract void integrate(ExpandableStatefulODE paramExpandableStatefulODE, double paramDouble)
/* 167:    */     throws MathIllegalStateException, MathIllegalArgumentException;
/* 168:    */   
/* 169:    */   public void computeDerivatives(double t, double[] y, double[] yDot)
/* 170:    */     throws MaxCountExceededException
/* 171:    */   {
/* 172:264 */     this.evaluations.incrementCount();
/* 173:265 */     this.expandable.computeDerivatives(t, y, yDot);
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected void setStateInitialized(boolean stateInitialized)
/* 177:    */   {
/* 178:276 */     this.statesInitialized = stateInitialized;
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected double acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
/* 182:    */     throws MathIllegalStateException
/* 183:    */   {
/* 184:293 */     double previousT = interpolator.getGlobalPreviousTime();
/* 185:294 */     double currentT = interpolator.getGlobalCurrentTime();
/* 186:297 */     if (!this.statesInitialized)
/* 187:    */     {
/* 188:298 */       for (EventState state : this.eventsStates) {
/* 189:299 */         state.reinitializeBegin(interpolator);
/* 190:    */       }
/* 191:301 */       this.statesInitialized = true;
/* 192:    */     }
/* 193:305 */     final int orderingSign = interpolator.isForward() ? 1 : -1;
/* 194:306 */     SortedSet<EventState> occuringEvents = new TreeSet(new Comparator()
/* 195:    */     {
/* 196:    */       public int compare(EventState es0, EventState es1)
/* 197:    */       {
/* 198:310 */         return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
/* 199:    */       }
/* 200:    */

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}     });
/* 201:315 */     for (EventState state : this.eventsStates) {
/* 202:316 */       if (state.evaluateStep(interpolator)) {
/* 203:318 */         occuringEvents.add(state);
/* 204:    */       }
/* 205:    */     }
/* 206:322 */     while (!occuringEvents.isEmpty())
/* 207:    */     {
/* 208:325 */       Iterator<EventState> iterator = occuringEvents.iterator();
/* 209:326 */       EventState currentEvent = (EventState)iterator.next();
/* 210:327 */       iterator.remove();
/* 211:    */       
/* 212:    */ 
/* 213:330 */       double eventT = currentEvent.getEventTime();
/* 214:331 */       interpolator.setSoftPreviousTime(previousT);
/* 215:332 */       interpolator.setSoftCurrentTime(eventT);
/* 216:    */       
/* 217:    */ 
/* 218:335 */       interpolator.setInterpolatedTime(eventT);
/* 219:336 */       double[] eventY = (double[])interpolator.getInterpolatedState().clone();
/* 220:337 */       currentEvent.stepAccepted(eventT, eventY);
/* 221:338 */       this.isLastStep = currentEvent.stop();
/* 222:341 */       for (StepHandler handler : this.stepHandlers) {
/* 223:342 */         handler.handleStep(interpolator, this.isLastStep);
/* 224:    */       }
/* 225:345 */       if (this.isLastStep)
/* 226:    */       {
/* 227:347 */         System.arraycopy(eventY, 0, y, 0, y.length);
/* 228:348 */         for (EventState remaining : occuringEvents) {
/* 229:349 */           remaining.stepAccepted(eventT, eventY);
/* 230:    */         }
/* 231:351 */         return eventT;
/* 232:    */       }
/* 233:354 */       if (currentEvent.reset(eventT, eventY))
/* 234:    */       {
/* 235:357 */         System.arraycopy(eventY, 0, y, 0, y.length);
/* 236:358 */         computeDerivatives(eventT, y, yDot);
/* 237:359 */         this.resetOccurred = true;
/* 238:360 */         for (EventState remaining : occuringEvents) {
/* 239:361 */           remaining.stepAccepted(eventT, eventY);
/* 240:    */         }
/* 241:363 */         return eventT;
/* 242:    */       }
/* 243:367 */       previousT = eventT;
/* 244:368 */       interpolator.setSoftPreviousTime(eventT);
/* 245:369 */       interpolator.setSoftCurrentTime(currentT);
/* 246:372 */       if (currentEvent.evaluateStep(interpolator)) {
/* 247:374 */         occuringEvents.add(currentEvent);
/* 248:    */       }
/* 249:    */     }
/* 250:379 */     interpolator.setInterpolatedTime(currentT);
/* 251:380 */     double[] currentY = interpolator.getInterpolatedState();
/* 252:381 */     for (EventState state : this.eventsStates)
/* 253:    */     {
/* 254:382 */       state.stepAccepted(currentT, currentY);
/* 255:383 */       this.isLastStep = ((this.isLastStep) || (state.stop()));
/* 256:    */     }
/* 257:385 */     this.isLastStep = ((this.isLastStep) || (Precision.equals(currentT, tEnd, 1)));
/* 258:388 */     for (StepHandler handler : this.stepHandlers) {
/* 259:389 */       handler.handleStep(interpolator, this.isLastStep);
/* 260:    */     }
/* 261:392 */     return currentT;
/* 262:    */   }
/* 263:    */   
/* 264:    */   protected void sanityChecks(ExpandableStatefulODE equations, double t)
/* 265:    */     throws NumberIsTooSmallException
/* 266:    */   {
/* 267:404 */     double threshold = 1000.0D * FastMath.ulp(FastMath.max(FastMath.abs(equations.getTime()), FastMath.abs(t)));
/* 268:    */     
/* 269:406 */     double dt = FastMath.abs(equations.getTime() - t);
/* 270:407 */     if (dt <= threshold) {
/* 271:408 */       throw new NumberIsTooSmallException(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, Double.valueOf(dt), Double.valueOf(threshold), false);
/* 272:    */     }
/* 273:    */   }
/* 274:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.AbstractIntegrator
 * JD-Core Version:    0.7.0.1
 */
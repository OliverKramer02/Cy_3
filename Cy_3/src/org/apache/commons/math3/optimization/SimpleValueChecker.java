/*  1:   */ package org.apache.commons.math3.optimization;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.FastMath;
/*  4:   */ 
/*  5:   */ public class SimpleValueChecker
/*  6:   */   extends AbstractConvergenceChecker<PointValuePair>
/*  7:   */ {
/*  8:   */   public SimpleValueChecker() {}
/*  9:   */   
/* 10:   */   public SimpleValueChecker(double relativeThreshold, double absoluteThreshold)
/* 11:   */   {
/* 12:52 */     super(relativeThreshold, absoluteThreshold);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public boolean converged(int iteration, PointValuePair previous, PointValuePair current)
/* 16:   */   {
/* 17:75 */     double p = ((Double)previous.getValue()).doubleValue();
/* 18:76 */     double c = ((Double)current.getValue()).doubleValue();
/* 19:77 */     double difference = FastMath.abs(p - c);
/* 20:78 */     double size = FastMath.max(FastMath.abs(p), FastMath.abs(c));
/* 21:79 */     return (difference <= size * getRelativeThreshold()) || (difference <= getAbsoluteThreshold());
/* 22:   */   }
/* 23:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.SimpleValueChecker
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.optimization;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.FastMath;
/*  4:   */ import org.apache.commons.math3.util.Pair;
/*  5:   */ 
/*  6:   */ public class SimplePointChecker<PAIR extends Pair<double[], ? extends Object>>
/*  7:   */   extends AbstractConvergenceChecker<PAIR>
/*  8:   */ {
/*  9:   */   public SimplePointChecker() {}
/* 10:   */   
/* 11:   */   public SimplePointChecker(double relativeThreshold, double absoluteThreshold)
/* 12:   */   {
/* 13:56 */     super(relativeThreshold, absoluteThreshold);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean converged(int iteration, PAIR previous, PAIR current)
/* 17:   */   {
/* 18:79 */     double[] p = (double[])previous.getKey();
/* 19:80 */     double[] c = (double[])current.getKey();
/* 20:81 */     for (int i = 0; i < p.length; i++)
/* 21:   */     {
/* 22:82 */       double pi = p[i];
/* 23:83 */       double ci = c[i];
/* 24:84 */       double difference = FastMath.abs(pi - ci);
/* 25:85 */       double size = FastMath.max(FastMath.abs(pi), FastMath.abs(ci));
/* 26:86 */       if ((difference > size * getRelativeThreshold()) && (difference > getAbsoluteThreshold())) {
/* 27:88 */         return false;
/* 28:   */       }
/* 29:   */     }
/* 30:91 */     return true;
/* 31:   */   }
/* 32:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.SimplePointChecker
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package org.apache.commons.math3.optimization;
/*  2:   */ 
/*  3:   */ import org.apache.commons.math3.util.FastMath;
/*  4:   */ 
/*  5:   */ public class SimpleVectorValueChecker
/*  6:   */   extends AbstractConvergenceChecker<PointVectorValuePair>
/*  7:   */ {
/*  8:   */   public SimpleVectorValueChecker() {}
/*  9:   */   
/* 10:   */   public SimpleVectorValueChecker(double relativeThreshold, double absoluteThreshold)
/* 11:   */   {
/* 12:53 */     super(relativeThreshold, absoluteThreshold);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current)
/* 16:   */   {
/* 17:76 */     double[] p = previous.getValueRef();
/* 18:77 */     double[] c = current.getValueRef();
/* 19:78 */     for (int i = 0; i < p.length; i++)
/* 20:   */     {
/* 21:79 */       double pi = p[i];
/* 22:80 */       double ci = c[i];
/* 23:81 */       double difference = FastMath.abs(pi - ci);
/* 24:82 */       double size = FastMath.max(FastMath.abs(pi), FastMath.abs(ci));
/* 25:83 */       if ((difference > size * getRelativeThreshold()) && (difference > getAbsoluteThreshold())) {
/* 26:85 */         return false;
/* 27:   */       }
/* 28:   */     }
/* 29:88 */     return true;
/* 30:   */   }
/* 31:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.SimpleVectorValueChecker
 * JD-Core Version:    0.7.0.1
 */
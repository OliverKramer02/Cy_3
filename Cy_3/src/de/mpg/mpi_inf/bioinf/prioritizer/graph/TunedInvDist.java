/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.graph;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ 
/*  5:   */ public class TunedInvDist
/*  6:   */   implements GraphDistance
/*  7:   */ {
/*  8:   */   private final Map<String, Integer> n2i;
/*  9:   */   private final MatrixRepresentation mat;
/* 10:   */   private final double alpha;
/* 11:   */   
/* 12:   */   protected TunedInvDist(Map<String, Integer> n2i, MatrixRepresentation m, double alpha)
/* 13:   */   {
/* 14:31 */     this.n2i = n2i;
/* 15:32 */     this.mat = m;
/* 16:33 */     this.alpha = alpha;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public double getDistance(String a, String b)
/* 20:   */   {
/* 21:44 */     double d = 1.0D / Math.pow(this.mat.get(((Integer)this.n2i.get(a)).intValue(), ((Integer)this.n2i.get(b)).intValue()), this.alpha);
/* 22:45 */     return d == 0.0D ? (1.0D / 0.0D) : d;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getName()
/* 26:   */   {
/* 27:49 */     return "Tuned Inverted Distance";
/* 28:   */   }
/* 29:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.graph.TunedInvDist
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.graph;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ 
/*  5:   */ public class InvDist
/*  6:   */   implements GraphDistance
/*  7:   */ {
/*  8:   */   private final Map<String, Integer> n2i;
/*  9:   */   private final MatrixRepresentation mat;
/* 10:   */   
/* 11:   */   protected InvDist(Map<String, Integer> n2i, MatrixRepresentation m)
/* 12:   */   {
/* 13:26 */     this.n2i = n2i;
/* 14:27 */     this.mat = m;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public double getDistance(String a, String b)
/* 18:   */   {
/* 19:38 */     double d = 1.0D / this.mat.get(((Integer)this.n2i.get(a)).intValue(), ((Integer)this.n2i.get(b)).intValue());
/* 20:39 */     return d == 0.0D ? (1.0D / 0.0D) : d;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getName()
/* 24:   */   {
/* 25:43 */     return "Inverted Distance";
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.graph.InvDist
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.graph;
/*  2:   */ 
/*  3:   */ import de.mpg.mpi_inf.bioinf.prioritizer.Preferences;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public abstract class GDFactory
/*  7:   */ {
/*  8:   */   public static GraphDistance getGraphDistance(GDType gdt, Map<String, Integer> n2i, MatrixRepresentation adjmat)
/*  9:   */   {
/* 10:28 */     switch (gdt)
/* 11:   */     {
/* 12:   */     case INV: 
/* 13:29 */       return new StdDist(n2i, adjmat);
/* 14:   */     case STD: 
/* 15:30 */       return new InvDist(n2i, adjmat);
/* 16:   */     case TINV: 
/* 17:31 */       return new TunedInvDist(n2i, adjmat, Preferences.getAlpha());
/* 18:   */     }
/* 19:32 */     throw new RuntimeException(
/* 20:33 */       "Encountered invalid GraphDistanceType!");
/* 21:   */   }
/* 22:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.graph.GDFactory
 * JD-Core Version:    0.7.0.1
 */
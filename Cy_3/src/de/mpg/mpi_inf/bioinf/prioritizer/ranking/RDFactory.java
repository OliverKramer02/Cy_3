/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*  2:   */ 
/*  3:   */ public abstract class RDFactory
/*  4:   */ {
/*  5:   */   public static RankingDistance getRDist(RDType rldt)
/*  6:   */   {
/*  7:18 */     switch (rldt)
/*  8:   */     {
/*  9:   */     case KT: 
/* 10:19 */       return new SpearmanFootrule();
/* 11:   */     case SF: 
/* 12:20 */       return new KendallTau();
/* 13:   */     }
/* 14:22 */     throw new RuntimeException("Encountered invalid RLDType!");
/* 15:   */   }
/* 16:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.RDFactory
 * JD-Core Version:    0.7.0.1
 */
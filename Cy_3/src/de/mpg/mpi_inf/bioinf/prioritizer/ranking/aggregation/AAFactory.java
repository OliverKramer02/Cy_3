/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation;
/*  2:   */ 
/*  3:   */ public abstract class AAFactory
/*  4:   */ {
/*  5:   */   public static AggregationAlgorithm getAAlg(AAType aat)
/*  6:   */   {
/*  7:19 */     switch (aat)
/*  8:   */     {
/*  9:   */     case MRF: 
/* 10:20 */       return new WeightedBordaFuse();
/* 11:   */     case WASF: 
/* 12:21 */       return new WeightedAddScoreFuse();
/* 13:   */     case WBF: 
/* 14:22 */       return new MaxRankFuse();
/* 15:   */     }
/* 16:23 */     throw new RuntimeException("Encountered invalid AAType!");
/* 17:   */   }
/* 18:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.AAFactory
 * JD-Core Version:    0.7.0.1
 */
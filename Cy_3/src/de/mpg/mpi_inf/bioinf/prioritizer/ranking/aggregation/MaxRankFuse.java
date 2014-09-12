/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation;
/*  2:   */ 
/*  3:   */ import de.mpg.mpi_inf.bioinf.prioritizer.Preferences;
/*  4:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.DescScoreRanking;
/*  5:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;

/*  6:   */ import java.util.HashMap;
/*  7:   */ import java.util.Iterator;
/*  8:   */ import java.util.Map;
/*  9:   */ import java.util.Set;
/* 10:   */ 
/* 11:   */ public class MaxRankFuse
/* 12:   */   implements AggregationAlgorithm
/* 13:   */ {
/* 14:   */   public Ranking aggAll(Map<Ranking, Double> prs, String aggname)
/* 15:   */   {
/* 16:19 */     Map<String, Double> ns = new HashMap();
/* 17:   */     Ranking rl;
/* 18:   */     int r;
/* 19:20 */     for (Iterator localIterator1 = prs.keySet().iterator(); localIterator1.hasNext(); r ++)
/* 20:   */     {
/* 21:20 */       rl = (Ranking)localIterator1.next();
/* 22:21 */       r = 1; continue;     }
/* 34:23 */     return new DescScoreRanking(ns, aggname, Preferences.areInjective());
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getName()
/* 38:   */   {
/* 39:28 */     return AAType.MRF.description();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public boolean isWeighted()
/* 43:   */   {
/* 44:32 */     return false;
/* 45:   */   }
/* 46:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.MaxRankFuse
 * JD-Core Version:    0.7.0.1
 */
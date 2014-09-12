/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation;
/*  2:   */ 
/*  3:   */ import de.mpg.mpi_inf.bioinf.prioritizer.Preferences;
/*  4:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.AscScoreRanking;
/*  5:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;
/*  6:   */ import java.util.HashMap;
/*  7:   */ import java.util.Iterator;
/*  8:   */ import java.util.Map;
/*  9:   */ import java.util.Set;
/* 10:   */ 
/* 11:   */ public class WeightedAddScoreFuse
/* 12:   */   implements AggregationAlgorithm
/* 13:   */ {
/* 14:   */   public Ranking aggAll(Map<Ranking, Double> prs, String aggname)
/* 15:   */   {
/* 16:19 */     Map<String, Double> ns = new HashMap();
/* 17:   */     Ranking rl;
/* 18:   */     int r;
/* 19:20 */   //  for (Iterator localIterator1 = prs.keySet().iterator(); localIterator1.hasNext(); r <= rl.rankCount())
/* 20:   */    // {
/* 21:20 */     //  rl = (Ranking)localIterator1.next();
/* 22:21 */     //  r = 1; continue;
/* 23:21 */      // for (String s : rl.getSymbols(r)) {
/* 24:22 */       //  if (ns.containsKey(s)) {
/* 25:23 */        //   ns.put(s, Double.valueOf(((Double)ns.get(s)).doubleValue() + rl.getScore(r) * ((Double)prs.get(rl)).doubleValue()));
/* 26:   */         //} else {
/* 27:24 */         //  ns.put(s, Double.valueOf(rl.getScore(r) * ((Double)prs.get(rl)).doubleValue()));
/* 28:   */        // }
/* 29:   */      // }
/* 30:21 */      // r++;
/* 31:   */     //}
/* 32:25 */     return new AscScoreRanking(ns, aggname, Preferences.areInjective());
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getName()
/* 36:   */   {
/* 37:30 */     return AAType.WASF.description();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean isWeighted()
/* 41:   */   {
/* 42:34 */     return true;
/* 43:   */   }
/* 44:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.WeightedAddScoreFuse
 * JD-Core Version:    0.7.0.1
 */
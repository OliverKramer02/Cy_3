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
/* 11:   */ public class WeightedBordaFuse
/* 12:   */   implements AggregationAlgorithm
/* 13:   */ {
/* 14:   */   public Ranking aggAll(Map<Ranking, Double> prs, String aggname)
/* 15:   */   {
/* 16:19 */     Map<String, Double> ns = new HashMap();
/* 17:   */     int n;
/* 18:   */     int r;
/* 19:21 */   //  for (Iterator localIterator1 = prs.keySet().iterator(); localIterator1.hasNext(); r <= n)
/* 20:   */    // {
/* 21:21 */     //  Ranking rg = (Ranking)localIterator1.next();
/* 22:22 */      // n = rg.rankCount();
/* 23:23 */     //  r = 1; continue;
/* 24:23 */     //  for (String s : rg.getSymbols(r)) {
/* 25:24 */     //    if (ns.containsKey(s)) {
/* 26:25 */      //     ns.put(s, 
/* 27:26 */       //      Double.valueOf(((Double)ns.get(s)).doubleValue() + (n - r + 1.0D) * ((Double)prs.get(rg)).doubleValue()));
/* 28:   */       //  } else {
/* 29:27 */        //   ns.put(s, Double.valueOf((n - r + 1.0D) * ((Double)prs.get(rg)).doubleValue()));
/* 30:   */        // }
/* 31:   */      // }
/* 32:23 */      // r++;
/* 33:   */    // }
/* 34:   */     //String st;
/* 35:29 */     //for (Iterator localIterator1 = ns.keySet().iterator(); localIterator1.hasNext(); ns.put(st, Double.valueOf(((Double)ns.get(st)).doubleValue() / prs.size()))) {
/* 36:29 */      // st = (String)localIterator1.next();
/* 37:   */     //}
/* 38:30 */     return new AscScoreRanking(ns, aggname, Preferences.areInjective());
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String getName()
/* 42:   */   {
/* 43:35 */     return AAType.WBF.description();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public boolean isWeighted()
/* 47:   */   {
/* 48:39 */     return true;
/* 49:   */   }
/* 50:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.WeightedBordaFuse
 * JD-Core Version:    0.7.0.1
 */
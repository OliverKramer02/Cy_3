/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.Set;
/*  5:   */ 
/*  6:   */ public class SpearmanFootrule
/*  7:   */   implements RankingDistance
/*  8:   */ {
/*  9:   */   public double getDistance(Ranking a, Ranking b)
/* 10:   */   {
/* 11:21 */     int dsum = 0;
/* 12:23 */     for (int r = 1; r <= a.rankCount(); r++)
/* 13:   */     {
/* 14:24 */       Set<String> cs = a.getSymbols(r);
/* 15:   */       String s;
/* 16:25 */       for (Iterator localIterator = cs.iterator(); localIterator.hasNext(); dsum += Math.abs(r - b.getRank(s))) {
/* 17:25 */         s = (String)localIterator.next();
/* 18:   */       }
/* 19:   */     }
/* 20:27 */     return 2.0D * dsum / (a.size() * a.size());
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getName()
/* 24:   */   {
/* 25:32 */     return "Spearman Footrule Distance";
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.SpearmanFootrule
 * JD-Core Version:    0.7.0.1
 */
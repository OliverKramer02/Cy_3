/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.Set;
/*  5:   */ 
/*  6:   */ public class KendallTau
/*  7:   */   implements RankingDistance
/*  8:   */ {
/*  9:   */   public double getDistance(Ranking a, Ranking b)
/* 10:   */   {
	return 0;
/* 11:19 */     //int count = 0;
/* 12:20 */     //for (int i = 1; i <= a.rankCount(); i++)
/* 13:   */     //{
/* 14:   */       //String sai2;
/* 15:21 */       //if (a.getSymbols(i).size() > 1)
/* 16:   */       //{
/* 17:   */         //Iterator localIterator2;
/* 18:22 */         //for (Iterator localIterator1 = a.getSymbols(i).iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 19:   */         //{
/* 20:22 */           //String sai1 = (String)localIterator1.next();
/* 21:23 */          // localIterator2 = a.getSymbols(i).iterator(); continue;sai2 = (String)localIterator2.next();
/* 22:24 */           //if ((sai1 != sai2) && (b.getRank(sai1) != b.getRank(sai2))) {
/* 23:25 */         //    count++;
/* 24:   */       //    }
/* 25:   */     //    }
/* 26:   */   //    }
/* 27:26 */       //for (int j = i + 1; j <= a.rankCount(); j++)
/* 28:   */     //  {
/* 29:   */   //      Iterator localIterator3;
/* 30:27 */     //    for (sai2 = a.getSymbols(i).iterator(); sai2.hasNext(); localIterator3.hasNext())
/* 31:   */       //  {
/* 32:27 */        //   String sai = (String)sai2.next();
/* 33:28 */         //  localIterator3 = a.getSymbols(j).iterator(); continue;String saj = (String)localIterator3.next();
/* 34:29 */         //  if (b.getRank(sai) >= b.getRank(saj)) {
/* 35:29 */          //   count++;
/* 36:   */          // }
/* 37:   */        // }
/* 38:   */       //}
/* 39:   */     //}
/* 40:31 */     //return 2.0D * count / (a.size() * (a.size() - 1.0D));
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getName()
/* 44:   */   {
/* 45:36 */     return "Kendall Tau Distance";
/* 46:   */   }
/* 47:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.KendallTau
 * JD-Core Version:    0.7.0.1
 */
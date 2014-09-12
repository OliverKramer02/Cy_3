/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*  2:   */ 
/*  3:   */ import java.util.Comparator;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public class DescScoreRanking
/*  7:   */   extends Ranking
/*  8:   */ {
/*  9:   */   public DescScoreRanking(Map<String, Double> tbr, String name, boolean injective)
/* 10:   */   {
/* 11:28 */     super(tbr, new Comparator()
/* 12:   */     {
/* 13:   */       public int compare(String s1, String s2)
/* 14:   */       {
	return 0;
/* 15:26 */        // return ((Double)DescScoreRanking.this.get(s2)).compareTo((Double)DescScoreRanking.this.get(s1));
/* 16:   */       }
/* 17:28 */

@Override
public int compare(Object arg0, Object arg1) {
	// TODO Auto-generated method stub
	return 0;
}     }, name, injective);
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.DescScoreRanking
 * JD-Core Version:    0.7.0.1
 */
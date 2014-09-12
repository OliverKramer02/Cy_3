/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*  2:   */ 
/*  3:   */ import java.util.Comparator;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public class AscScoreRanking
/*  7:   */   extends Ranking
/*  8:   */ {
/*  9:   */   @SuppressWarnings("unchecked")
public AscScoreRanking(Map<String, Double> tbr, String name, boolean injective)
/* 10:   */   {
/* 11:28 */     super(tbr, new Comparator()
/* 12:   */     {
/* 13:   */       public int compare(String s1, String s2)
/* 14:   */       {
	return 0;
/* 15:26 */       //  return AscScoreRanking.this.get(s1).compareTo(AscScoreRanking.this.get(s2));
/* 16:   */       }
/* 17:28 */

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}     }, name, injective);
/* 18:   */   }
/* 19:   */

protected Double get(String s1) {
	// TODO Auto-generated method stub
	return null;
} }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.AscScoreRanking
 * JD-Core Version:    0.7.0.1
 */
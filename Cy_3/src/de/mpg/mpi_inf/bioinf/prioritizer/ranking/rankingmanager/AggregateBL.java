package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.AAFactory;
import de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.AggregationAlgorithm;
import de.mpg.mpi_inf.bioinf.prioritizer.*;


/* 13:   */ final class AggregateBL
/* 14:   */   implements ActionListener
/* 15:   */ {
/* 16:   */   private RankingManager rm;
/* 17:   */   
/* 18:   */   protected AggregateBL(RankingManager rm)
/* 19:   */   {
/* 20:30 */     this.rm = rm;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void actionPerformed(ActionEvent ae)
/* 24:   */   {
/* 25:43 */     AggregationAlgorithm aa = AAFactory.getAAlg(Preferences.getAAlg());
/* 26:44 */     this.rm.ra.setAlgorithm(aa);
/* 27:45 */   //  new PrimaryRankingSelection(this.rm.ra, this.rm.n2r.keySet());
/* 28:46 */     Map<String, Double> weights = this.rm.ra.getWeights();
/* 29:47 */     //if (weights.isEmpty())
/* 30:   */     //{
/* 31:48 */      // this.rm.ra.reset();
/* 32:49 */      // return;
/* 33:   */     //}
/* 34:51 */     //for (Ranking r : this.rm.n2r.values()) {
/* 35:52 */      // if ((!this.rm.ra.getAlgorithm().isWeighted()) || 
/* 36:53 */       //  (((Double)weights.get(r.getName())).doubleValue() > 0.0D)) {
/* 37:53 */        // this.rm.ra.addRanking(r);
/* 38:   */       //}
/* 39:   */     //}
/* 40:54 */     if (this.rm.ra.hasRanking())
/* 41:   */     {
/* 42:55 */       this.rm.addRanking(this.rm.ra.aggAll());
/* 43:56 */       this.rm.ra.reset();
/* 44:57 */       this.rm.createRankingPane(true);
/* 45:   */     }
/* 46:59 */     this.rm.ra.reset();
/* 47:   */   }
/* 48:   */
 }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.AggregateBL
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*  2:   */ 
/*  3:   */ /*  8:   */ import java.awt.event.ActionEvent;
/*  9:   */ import java.awt.event.ActionListener;

/* 11:   */ import javax.swing.JButton;
/* 12:   */ import javax.swing.JOptionPane;

/*  6:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;
/*  4:   */ 
/*  5:   */ 
/*  7:   */ 
/* 10:   */ 
/* 13:   */ 
/* 14:   */ final class MapRankingsBL
/* 15:   */   implements ActionListener
/* 16:   */ {
/* 17:   */   private RankingManager rm;
/* 18:   */   
/* 19:   */   protected MapRankingsBL(RankingManager rm)
/* 20:   */   {
/* 21:27 */     this.rm = rm;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void actionPerformed(ActionEvent ae)
/* 25:   */   {
/* 26:37 */    // new RankingSelection((RankingManager)
/* 27:38 */       //((JButton)ae.getSource()).getParent().getParent()
/* 28:39 */      // .getParent().getParent().getParent(), "map to nodes");
/* 29:40 */     if (this.rm.srs.length == 0) {
/* 30:40 */       return;
/* 31:   */     }
/* 32:41 */     for (String rg : this.rm.srs) {
/* 33:42 */       for (int r = 1; r <= ((Ranking)this.rm.n2r.get(rg)).rankCount(); r++) {
/* 34:43 */         for (String s : ((Ranking)this.rm.n2r.get(rg)).getSymbols(r))
/* 35:   */         {
/* 36:44 */         //  Cytoscape.getNodeAttributes().setAttribute(s, 
/* 37:45 */           //  ((Ranking)this.rm.n2r.get(rg)).getName() + "_score", 
/* 38:46 */            // Double.valueOf(((Ranking)this.rm.n2r.get(rg)).getScore(r)));
/* 39:47 */           //Cytoscape.getNodeAttributes().setAttribute(s, 
/* 40:48 */             //((Ranking)this.rm.n2r.get(rg)).getName() + "_rank", Integer.valueOf(r));
/* 41:   */         }
/* 42:   */       }
/* 43:   */     }
/* 44:50 */     this.rm.srs = new String[0];
/* 45:51 */     JOptionPane.showMessageDialog(this.rm, "Ranks and scores have successfully been mapped to the nodes.", 
/* 46:52 */       "Mapping complete", 
/* 47:53 */       1);
/* 48:   */   }
/* 49:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.MapRankingsBL
 * JD-Core Version:    0.7.0.1
 */
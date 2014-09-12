/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*  2:   */ 
/*  3:   */ /*  9:   */ import java.awt.event.ActionEvent;
/* 10:   */ import java.awt.event.ActionListener;

/* 12:   */ import javax.swing.JButton;

import de.mpg.mpi_inf.bioinf.prioritizer.Preferences;
/*  5:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.RDFactory;
/*  6:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;
/*  7:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.RankingDistance;
/*  4:   */ 
/*  8:   */ 
/* 11:   */ 
/* 13:   */ 
/* 14:   */ final class ComputeRDBL
/* 15:   */   implements ActionListener
/* 16:   */ {
/* 17:   */   private RankingManager rm;
/* 18:   */   
/* 19:   */   protected ComputeRDBL(RankingManager rm)
/* 20:   */   {
/* 21:29 */     this.rm = rm;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void actionPerformed(ActionEvent ae)
/* 25:   */   {
/* 26:39 */    // new RankingSelection((RankingManager)
/* 27:40 */     //  ((JButton)ae.getSource()).getParent().getParent()
/* 28:41 */     //  .getParent().getParent().getParent(), "compute distance");
/* 29:42 */     if (this.rm.srs.length == 0) {
/* 30:42 */       return;
/* 31:   */     }
/* 32:44 */     RankingDistance rd = RDFactory.getRDist(Preferences.getRDist());
/* 33:   */     
/* 34:   */ 
/* 35:   */ 
/* 36:   */ 
/* 37:   */ 
/* 38:   */ 
/* 39:   */ 
/* 40:   */ 
/* 41:   */ 
/* 42:   */ 
/* 43:   */ 
/* 44:   */ 
/* 45:   */ 
/* 46:58 */     double[][] dists = 
/* 47:59 */       new double[this.rm.srs.length][this.rm.srs.length];
/* 48:60 */     for (int i = 0; i < this.rm.srs.length; i++) {
/* 49:61 */       for (int j = 0; j < this.rm.srs.length; j++) {
/* 50:62 */         if (this.rm.srs[i].equals(this.rm.srs[j])) {
/* 51:62 */           dists[i][j] = 0.0D;
/* 52:   */         } else {
/* 53:63 */           dists[i][j] = rd.getDistance((Ranking)this.rm.n2r.get(this.rm.srs[i]), 
/* 54:64 */             (Ranking)this.rm.n2r.get(this.rm.srs[j]));
/* 55:   */         }
/* 56:   */       }
/* 57:   */     }
/* 58:67 */     String[][] matrix = new String[this.rm.srs.length + 1][this.rm.srs.length + 1];
/* 59:68 */     matrix[0][0] = "";
/* 60:69 */     for (int j = 0; j < this.rm.srs.length; j++) {
/* 61:69 */       matrix[0][(j + 1)] = this.rm.srs[j];
/* 62:   */     }
/* 63:70 */     for (int i = 1; i < matrix.length; i++) {
/* 64:71 */       for (int j = 0; j < matrix.length; j++) {
/* 65:72 */         if (j == 0) {
/* 66:72 */           matrix[i][j] = this.rm.srs[(i - 1)];
/* 67:   */         } else {
/* 68:73 */           matrix[i][j] = String.valueOf(
/* 69:74 */             Math.round(dists[(i - 1)][(j - 1)] * 100000000.0D) / 100000000.0D);
/* 70:   */         }
/* 71:   */       }
/* 72:   */     }
/* 73:76 */     this.rm.addRDMatrix(matrix);
/* 74:77 */     this.rm.srs = new String[0];
/* 75:   */   }
/* 76:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.ComputeRDBL
 * JD-Core Version:    0.7.0.1
 */
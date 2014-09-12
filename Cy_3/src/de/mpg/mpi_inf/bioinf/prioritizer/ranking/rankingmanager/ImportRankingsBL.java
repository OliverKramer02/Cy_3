/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*  2:   */ 
/*  3:   */ /*  5:   */ import java.awt.event.ActionEvent;
/*  6:   */ import java.awt.event.ActionListener;
/*  7:   */ import java.io.File;

/*  8:   */ import javax.swing.JFileChooser;

/*  4:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.ImportedRanking;
/*  9:   */ 
/* 10:   */ final class ImportRankingsBL
/* 11:   */   implements ActionListener
/* 12:   */ {
/* 13:   */   private RankingManager rm;
/* 14:   */   
/* 15:   */   protected ImportRankingsBL(RankingManager rm)
/* 16:   */   {
/* 17:27 */     this.rm = rm;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void actionPerformed(ActionEvent ae)
/* 21:   */   {
/* 22:37 */     JFileChooser fc = new JFileChooser();
/* 23:38 */     fc.setCurrentDirectory(new File("."));
/* 24:39 */     fc.setDialogTitle("Directory Selection");
/* 25:40 */     fc.setFileSelectionMode(1);
/* 26:41 */     //String path = fc.showDialog(Cytoscape.getDesktop(), null) == 0 ? 
/* 27:   */     
/* 28:43 */   //    fc.getSelectedFile().getAbsolutePath() : "";
/* 29:44 */    // File dir = new File(path);
/* 30:45 */    // if ((dir == null) || (!dir.isDirectory())) {
/* 31:45 */     //  return;
/* 32:   */     //}
/* 33:46 */     //for (String fname : dir.list()) {
/* 34:46 */      // if (fname.endsWith(".rlf")) {
/* 35:47 */       //  this.rm.addRanking(new ImportedRanking(dir + File.separator + fname));
/* 36:   */       //}
/* 37:   */     //}
/* 38:48 */     //this.rm.createRankingPane(true);
/* 39:   */   }
/* 40:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.ImportRankingsBL
 * JD-Core Version:    0.7.0.1
 */
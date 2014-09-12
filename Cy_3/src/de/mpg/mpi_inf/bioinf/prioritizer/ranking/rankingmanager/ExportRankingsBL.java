/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*  2:   */ 
/*  3:   */ /*  7:   */ import java.awt.event.ActionEvent;
/*  8:   */ import java.awt.event.ActionListener;
/*  9:   */ import java.io.File;
/* 10:   */ import java.io.FileWriter;
/* 11:   */ import java.io.IOException;
/* 12:   */ import java.io.PrintWriter;

/* 14:   */ import javax.swing.JButton;
/* 15:   */ import javax.swing.JFileChooser;
/* 16:   */ import javax.swing.JOptionPane;

/*  5:   */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;
/*  4:   */ 
/*  6:   */ 
/* 13:   */ 
/* 17:   */ 
/* 18:   */ final class ExportRankingsBL
/* 19:   */   implements ActionListener
/* 20:   */ {
/* 21:   */   private RankingManager rm;
/* 22:   */   
/* 23:   */   protected ExportRankingsBL(RankingManager rm)
/* 24:   */   {
/* 25:33 */     this.rm = rm;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void actionPerformed(ActionEvent ae)
/* 29:   */   {
/* 30:   */   //  try
/* 31:   */   //  {
/* 32:44 */   //    new RankingSelection((RankingManager)
/* 33:45 */   //      ((JButton)ae.getSource()).getParent().getParent()
/* 34:46 */   //      .getParent().getParent().getParent(), "export");
/* 35:47 */   //    if (this.rm.srs.length == 0) {
/* 36:47 */   //      return;
/* 37:   */    //   }
/* 38:48 */       JFileChooser fc = new JFileChooser();
/* 39:49 */       fc.setDialogTitle("Export Rankings");
/* 40:50 */       fc.setFileSelectionMode(1);
/* 41:51 */      // int fcoption = fc.showDialog(Cytoscape.getDesktop(), "Export");
/* 42:52 */      // if (fcoption == 1) {
/* 43:52 */       //  return;
/* 44:   */       //}
/* 45:53 */       String exportpath = fc.getSelectedFile().getAbsolutePath();
/* 46:56 */       for (String rg : this.rm.srs)
/* 47:   */       {
/* 48:57 */         File f = new File(exportpath + File.separator + rg + ".rlf");
/* 49:58 */         //if ((!f.exists()) || (JOptionPane.showConfirmDialog(
/* 50:59 */         //  Cytoscape.getDesktop(), "<html><div style=\"width:300px;text-align:justify;\">" + 
/* 51:60 */          // f.getName() + " already exists. Do " + 
/* 52:61 */          // "you want to overwrite it?</div></html>", 
/* 53:62 */          // "Confirm Ranking Overwrite", 
/* 54:63 */          // 0, 3) != 
/* 55:64 */           //1))
/* 56:   */         {
/* 57:66 */       //    PrintWriter rwriter = new PrintWriter(new FileWriter(f));
/* 58:67 */         //  for (int r = 1; r <= ((Ranking)this.rm.n2r.get(rg)).rankCount(); r++) {
/* 59:68 */           //  for (String s : ((Ranking)this.rm.n2r.get(rg)).getSymbols(r)) {
/* 60:69 */             //  rwriter.println(s + '\t' + ((Ranking)this.rm.n2r.get(rg)).getScore(r) + '\t' + r);
/* 61:   */             //}
/* 62:   */           //}
/* 63:70 */           //rwriter.close();
/* 64:   */         }
/* 65:   */       }
/* 66:   */     //}
/* 67:   */     //catch (IOException ioe)
/* 68:   */     //{
/* 69:71 */      // ioe.printStackTrace();
/* 70:   */     //}
/* 71:72 */     //this.rm.srs = new String[0];
/* 72:   */   }
/* 73:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.ExportRankingsBL
 * JD-Core Version:    0.7.0.1
 */
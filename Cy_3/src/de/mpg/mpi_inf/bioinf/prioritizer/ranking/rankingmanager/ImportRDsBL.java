/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*  4:   */ import java.awt.event.ActionEvent;
/*  5:   */ import java.awt.event.ActionListener;
/*  6:   */ import java.io.BufferedReader;
/*  7:   */ import java.io.File;
/*  8:   */ import java.io.FileReader;
/*  9:   */ import java.io.IOException;
/* 10:   */ import java.util.ArrayList;
/* 11:   */ import java.util.List;
/* 12:   */ import javax.swing.JFileChooser;
/* 13:   */ 
/* 14:   */ public class ImportRDsBL
/* 15:   */   implements ActionListener
/* 16:   */ {
/* 17:   */   private RankingManager rm;
/* 18:   */   
/* 19:   */   protected ImportRDsBL(RankingManager rm)
/* 20:   */   {
/* 21:26 */     this.rm = rm;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void actionPerformed(ActionEvent ae)
/* 25:   */   {
/* 26:31 */     JFileChooser fc = new JFileChooser();
/* 27:32 */     fc.setCurrentDirectory(new File("."));
/* 28:33 */     fc.setDialogTitle("Directory Selection");
/* 29:34 */     fc.setFileSelectionMode(1);
/* 30:35 */  //   String path = fc.showDialog(Cytoscape.getDesktop(), null) == 0 ? 
/* 31:   */     
/* 32:37 */    //   fc.getSelectedFile().getAbsolutePath() : "";
/* 33:38 */    // File dir = new File(path);
/* 34:39 */     //String dirpath = dir.getAbsolutePath();
/* 35:40 */     //if ((dir == null) || (!dir.isDirectory())) {
/* 36:40 */      // return;
/* 37:   */     //}
/* 38:43 */     List<String[]> rows = new ArrayList();
/* 39:   */   //  try
/* 40:   */    // {
/* 41:45 */       //for (String fname : dir.list()) {
/* 42:45 */         //if (fname.endsWith(".rdf"))
/* 43:   */        // {
/* 44:46 */         //  BufferedReader br = new BufferedReader(new FileReader(new File(
/* 45:47 */       //      dirpath + File.separator + fname)));
/* 46:   */          // String line;
/* 47:48 */          // while ((line = br.readLine()) != null)
/* 48:   */           //{
/* 49:   */            
/* 50:48 */             //rows.add(line.split("\t"));
/* 51:   */          // }
/* 52:49 */           //this.rm.addRDMatrix((String[][])rows.toArray(new String[rows.size()][rows.size()]));
/* 53:   */         //}
/* 54:   */       //}
/* 55:   */     //}
/* 56:   */     //catch (IOException ioe)
/* 57:   */     //{
/* 58:51 */       //ioe.printStackTrace();
/* 59:   */     //}
/* 60:   */   }
/* 61:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.ImportRDsBL
 * JD-Core Version:    0.7.0.1
 */
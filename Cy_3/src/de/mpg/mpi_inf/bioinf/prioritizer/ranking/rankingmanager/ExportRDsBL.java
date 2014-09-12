/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*  2:   */ 
/*  3:   */ /*  6:   */ import java.awt.event.ActionEvent;
/*  7:   */ import java.awt.event.ActionListener;
/*  8:   */ import java.io.File;
/*  9:   */ import java.io.FileWriter;
/* 10:   */ import java.io.IOException;
/* 11:   */ import java.io.PrintWriter;

/* 13:   */ import javax.swing.JButton;
/* 14:   */ import javax.swing.JFileChooser;
/* 15:   */ import javax.swing.JOptionPane;
/* 16:   */ import javax.swing.JTable;
/*  4:   */ 
/*  5:   */ 
/* 12:   */ 
/* 17:   */ 
/* 18:   */ 
/* 19:   */ final class ExportRDsBL
/* 20:   */   implements ActionListener
/* 21:   */ {
/* 22:   */   private RankingManager rm;
/* 23:   */   
/* 24:   */   protected ExportRDsBL(RankingManager rm)
/* 25:   */   {
/* 26:32 */     this.rm = rm;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void actionPerformed(ActionEvent ae)
/* 30:   */   {
/* 31:   */     try
/* 32:   */     {
/* 33:43 */     //  new RankingDistanceSelection((RankingManager)
/* 34:44 */       //  ((JButton)ae.getSource()).getParent().getParent()
/* 35:45 */        // .getParent().getParent().getParent(), "export");
/* 36:46 */       if (this.rm.sds.length == 0) {
/* 37:46 */         return;
/* 38:   */       }
/* 39:47 */       JFileChooser fc = new JFileChooser();
/* 40:48 */       fc.setDialogTitle("Export Distances");
/* 41:49 */       fc.setFileSelectionMode(1);
/* 42:50 */       //int fcoption = fc.showDialog(Cytoscape.getDesktop(), "Export");
/* 43:51 */       //if (fcoption == 1) {
/* 44:51 */        // return;
/* 45:   */       //}
/* 46:52 */       String exportpath = fc.getSelectedFile().getAbsolutePath();
/* 47:55 */       for (String dt : this.rm.sds)
/* 48:   */       {
/* 49:56 */         File f = new File(exportpath + File.separator + dt + ".rdf");
/* 50:57 */         //if ((!f.exists()) || (JOptionPane.showConfirmDialog(
/* 51:58 */         //  Cytoscape.getDesktop(), "<html><div style=\"width:300px;text-align:justify;\">" + 
/* 52:59 */           //f.getName() + " already exists. Do " + 
/* 53:60 */           //"you want to overwrite it?</div></html>", 
/* 54:61 */           //"Confirm Distance Overwrite", 
/* 55:62 */           //0, 
/* 56:63 */           //3) != 
/* 57:64 */           //1))
/* 58:   */         {
/* 59:66 */           PrintWriter dwriter = new PrintWriter(new FileWriter(f));
/* 60:67 */           dwriter.print('\t');
/* 61:68 */           for (int i = 0; i < ((JTable)this.rm.n2d.get(dt)).getRowCount(); i++) {
/* 62:69 */             dwriter.print(((JTable)this.rm.n2d.get(dt)).getModel().getValueAt(i, 0) + 
/* 63:70 */               "\t");
/* 64:   */           }
/* 65:71 */           dwriter.print('\n');
/* 66:72 */           for (int i = 0; i < ((JTable)this.rm.n2d.get(dt)).getRowCount(); i++)
/* 67:   */           {
/* 68:73 */             for (int j = 0; j < ((JTable)this.rm.n2d.get(dt)).getColumnCount(); j++) {
/* 69:74 */               dwriter.print(((JTable)this.rm.n2d.get(dt)).getModel()
/* 70:75 */                 .getValueAt(i, j) + "\t");
/* 71:   */             }
/* 72:76 */             dwriter.print('\n');
/* 73:   */           }
/* 74:78 */           dwriter.close();
/* 75:   */         }
/* 76:   */       }
/* 77:   */     }
/* 78:   */     catch (IOException ioe)
/* 79:   */     {
/* 80:79 */       ioe.printStackTrace();
/* 81:   */     }
/* 82:80 */     this.rm.sds = new String[0];
/* 83:   */   }
/* 84:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.ExportRDsBL
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*  2:   */ 
/*  3:   */ import java.awt.Container;
/*  4:   */ import java.awt.event.ActionEvent;
/*  5:   */ import java.awt.event.ActionListener;
/*  6:   */ import javax.swing.JButton;
/*  7:   */ 
/*  8:   */ final class CloseBL
/*  9:   */   implements ActionListener
/* 10:   */ {
/* 11:   */   public void actionPerformed(ActionEvent ae)
/* 12:   */   {
/* 13:20 */     ((JButton)ae.getSource()).getParent().getParent().getParent().getParent().setVisible(false);
/* 14:   */   }
/* 15:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.CloseBL
 * JD-Core Version:    0.7.0.1
 */
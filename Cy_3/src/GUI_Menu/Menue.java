/*  1:   */ package GUI_Menu;
/*  2:   */ 
/*  3:   */ import org.cytoscape.app.swing.AbstractCySwingApp;
/*  4:   */ import org.cytoscape.app.swing.CySwingAppAdapter;
/*  6:   */ 
/*  7:   */ public class Menue
/*  8:   */   extends AbstractCySwingApp
/*  9:   */ {
/* 10:   */   public Menue(CySwingAppAdapter adapter)
/* 11:   */   {
/* 12:12 */     super(adapter);
/* 13:   */     // Seed Node
/* 14:14 */     adapter.getCySwingApplication()
/* 15:15 */       .addAction(new TagfromSelection(adapter));
/* 16:16 */     adapter.getCySwingApplication()
/* 17:17 */       .addAction(new TagfromFile(adapter));
/* 18:18 */     adapter.getCySwingApplication()
/* 19:19 */       .addAction(new Removeseedtag(adapter));
/* 20:   */     
/* 21:   */ 	//Cc
/* 22:22 */     adapter.getCySwingApplication()
/* 23:23 */       .addAction(new Computecentralises(adapter));


				//Acc
				adapter.getCySwingApplication()
					.addAction(new Acc(adapter));
				//Aea
				adapter.getCySwingApplication()
					.addAction(new Aea(adapter));


     			//Ccmn
				adapter.getCySwingApplication()
					.addAction(new Ccm(adapter));

     			//RanmN
				adapter.getCySwingApplication()
					.addAction(new RanmN(adapter));
				
				//AmR
				adapter.getCySwingApplication()
					.addAction(new AmR(adapter));
				
				//Srm
				adapter.getCySwingApplication()
					.addAction(new Srm(adapter));
				
				//Pref
				adapter.getCySwingApplication()
					.addAction(new Pref(adapter));



				
 }
}


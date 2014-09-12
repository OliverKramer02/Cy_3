/*  1:   */ package GUI_Menu;
/*  2:   */ 
/*  3:   */ import java.awt.event.ActionEvent;
/*  4:   */ import org.cytoscape.app.CyAppAdapter;
/*  5:   */ import org.cytoscape.application.swing.AbstractCyAction;
/*  6:   */ 
/*  7:   */ public class Aea
/*  8:   */   extends AbstractCyAction
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 1L;
/* 11:   */   private final CyAppAdapter adapter;
/* 12:   */   
/* 13:   */   public Aea(CyAppAdapter adapter)
/* 14:   */   {
/* 15:20 */     super("According to some exting attribute", adapter.getCyApplicationManager(), "network", adapter.getCyNetworkViewManager());
/* 16:21 */     this.adapter = adapter;
/* 17:22 */     setPreferredMenu("Tools.Networkprioritizer.Ranke nodes");
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void actionPerformed(ActionEvent e) {}
/* 21:   */ }
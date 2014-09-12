/*  1:   */ package GUI_Menu;
/*  2:   */ 
/*  3:   */ import java.awt.event.ActionEvent;
/*  4:   */ import org.cytoscape.app.CyAppAdapter;
/*  5:   */ import org.cytoscape.application.swing.AbstractCyAction;
/*  6:   */ 
/*  7:   */ public class TagfromFile
/*  8:   */   extends AbstractCyAction
/*  9:   */ {
/* 10:   */   private static final long serialVersionUID = 1L;
/* 11:   */   private final CyAppAdapter adapter;
/* 12:   */   
/* 13:   */   public TagfromFile(CyAppAdapter adapter)
/* 14:   */   {
/* 15:20 */     super("Tag from File", adapter.getCyApplicationManager(), "network", adapter.getCyNetworkViewManager());
/* 16:21 */     this.adapter = adapter;
/* 17:22 */     setPreferredMenu("Tools.Networkprioritizer.SeedNode");
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void actionPerformed(ActionEvent e) {}
/* 21:   */ }


/* Location:           C:\Users\Olli\Desktop\GUI\
 * Qualified Name:     GUI_Menu.TagfromFile
 * JD-Core Version:    0.7.0.1
 */
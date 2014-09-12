/*   1:    */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager;
/*   2:    */ 
/*   3:    */ 
/*   4:    */ import de.mpg.mpi_inf.bioinf.prioritizer.Preferences;
/*   5:    */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.RDType;
/*   6:    */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;
/*   7:    */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.Aggregator;
/*   8:    */ import java.awt.Color;
/*   9:    */ import java.awt.Container;
/*  10:    */ import java.awt.GridBagConstraints;
/*  11:    */ import java.awt.GridBagLayout;
/*  12:    */ import java.util.Arrays;
/*  13:    */ import java.util.Collection;
/*  14:    */ import java.util.HashMap;
/*  15:    */ import java.util.HashSet;
/*  16:    */ import java.util.Iterator;
/*  17:    */ import java.util.Map;
/*  18:    */ import java.util.Set;
/*  19:    */ import javax.swing.JButton;
/*  20:    */ import javax.swing.JDialog;
/*  21:    */ import javax.swing.JLabel;
/*  22:    */ import javax.swing.JPanel;
/*  23:    */ import javax.swing.JScrollPane;
/*  24:    */ import javax.swing.JTabbedPane;
/*  25:    */ import javax.swing.JTable;
/*  26:    */ import javax.swing.table.DefaultTableModel;
/*  27:    */ import javax.swing.table.JTableHeader;
/*  28:    */ import javax.swing.table.TableModel;
/*  29:    */ 
/*  30:    */ public class RankingManager
/*  31:    */   extends JDialog
/*  32:    */ {
/*  33:    */   protected Map<Ranking, Boolean> rankings;
/*  34:    */   public Map<String, Ranking> n2r;
/*  35:    */   public String[] srs;
/*  36:    */   protected Map<String, JTable> n2d;
/*  37:    */   protected String[] sds;
/*  38:    */   protected JTabbedPane rpane;
/*  39:    */   protected JPanel bpane;
/*  40:    */   protected Aggregator ra;
/*  41:    */   private JButton close;
/*  42:    */   
/*  43:    */   public RankingManager()
/*  44:    */   {
/*  45: 61 */     //super(Cytoscape.getDesktop(), "Ranking Manager");
/*  46: 62 */     this.rankings = new HashMap();
/*  47: 63 */     this.n2r = new HashMap();
/*  48: 64 */     this.n2d = new HashMap();
/*  49: 65 */     this.srs = new String[0];
/*  50: 66 */     this.sds = new String[0];
/*  51: 67 */     this.ra = new Aggregator();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void display()
/*  55:    */   {
/*  56: 75 */     createRankingPane(false);
/*  57: 76 */     createButtonPane();
/*  58: 77 */     this.close = new JButton("Close");
/*  59: 78 */     this.close.addActionListener(new CloseBL());
/*  60: 79 */     performLayout();
/*  61:    */   }
/*  62:    */   
/*  63:    */   private void performLayout()
/*  64:    */   {
/*  65: 86 */     GridBagLayout layout = new GridBagLayout();
/*  66: 87 */     GridBagConstraints constraints = new GridBagConstraints();
/*  67: 88 */     constraints.gridx = 0;
/*  68: 89 */     constraints.gridy = 0;
/*  69: 90 */     constraints.weighty = 1.0D;
/*  70: 91 */     constraints.weightx = 1.0D;
/*  71: 92 */     constraints.fill = 1;
/*  72: 93 */     getContentPane().setLayout(layout);
/*  73: 94 */     constraints.gridheight = 3;
/*  74: 95 */     getContentPane().add(this.rpane, constraints);
/*  75: 96 */     constraints.gridheight = 1;
/*  76: 97 */     constraints.gridx += 1;
/*  77: 98 */     constraints.weightx = 0.0D;
/*  78: 99 */     constraints.weighty = 0.0D;
/*  79:100 */     constraints.fill = 0;
/*  80:101 */     getContentPane().add(this.bpane, constraints);
/*  81:102 */     constraints.gridy += 1;
/*  82:103 */     constraints.weighty = 1.0D;
/*  83:104 */     getContentPane().add(new JLabel(""), constraints);
/*  84:105 */     constraints.gridy += 1;
/*  85:106 */     constraints.weighty = 0.0D;
/*  86:107 */     getContentPane().add(this.close, constraints);
/*  87:108 */     pack();
/*  88:109 */     //setLocationRelativeTo(Cytoscape.getDesktop());
/*  89:110 */     setVisible(true);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void addRanking(Ranking r)
/*  93:    */   {
/*  94:119 */     this.rankings.put(r, Boolean.valueOf(false));
/*  95:120 */     this.n2r.put(r.getName(), r);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public String[] getRanklists()
/*  99:    */   {
/* 100:128 */     Set<String> rs = new HashSet();
/* 101:    */     Ranking r;
/* 102:129 */     for (Iterator localIterator = this.rankings.keySet().iterator(); localIterator.hasNext(); rs.add(r.getName())) {
/* 103:129 */       r = (Ranking)localIterator.next();
/* 104:    */     }
/* 105:130 */     return (String[])rs.toArray(new String[rs.size()]);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public String[] getRDs()
/* 109:    */   {
/* 110:140 */     Set<String> ds = new HashSet();
/* 111:    */     JTable d;
/* 112:141 */     for (Iterator localIterator = this.n2d.values().iterator(); localIterator.hasNext(); ds.add(d.getName())) {
/* 113:141 */       d = (JTable)localIterator.next();
/* 114:    */     }
/* 115:142 */     return (String[])ds.toArray(new String[ds.size()]);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setSelectedRankings(String[] selected)
/* 119:    */   {
/* 120:150 */     this.srs = selected;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setSelectedRDs(String[] selected)
/* 124:    */   {
/* 125:158 */     this.sds = selected;
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected void createRankingPane(boolean update)
/* 129:    */   {
/* 130:171 */     if (!update) {
/* 131:171 */       this.rpane = new JTabbedPane();
/* 132:    */     }
/* 133:175 */     for (Ranking rg : this.rankings.keySet()) {
/* 134:176 */       if (!((Boolean)this.rankings.get(rg)).booleanValue())
/* 135:    */       {
/* 136:177 */         String[] names = { "Symbol", "Score", "Rank" };
/* 137:178 */         String[][] data = new String[rg.size()][];
/* 138:179 */         int i = 0;
/* 139:180 */         for (int r = 1; r <= rg.rankCount(); r++) {
/* 140:180 */           for (String s : rg.getSymbols(r)) {
/* 141:181 */            // data[(i++)] = { s, Double.toString(rg.getScore(r)), Integer.toString(r) };
/* 143:    */           }
/* 144:    */         }
/* 145:183 */         JTable t = new JTable(new DefaultTableModel(data, names))
/* 146:    */         {
/* 147:    */           public boolean isCellEditable(int r, int c)
/* 148:    */           {
/* 149:186 */             return false;
/* 150:    */           }
/* 151:187 */         };
/* 152:188 */         t.getTableHeader().setBackground(Color.GRAY);
/* 153:189 */         JScrollPane tpane = new JScrollPane(t);
/* 154:190 */         this.rpane.addTab(rg.getName(), tpane);
/* 155:191 */         this.rankings.put(rg, Boolean.valueOf(true));
/* 156:    */       }
/* 157:    */     }
/* 158:193 */     if (update) {
/* 159:193 */       performLayout();
/* 160:    */     }
/* 161:    */   }
/* 162:    */   
/* 163:    */   private void createButtonPane()
/* 164:    */   {
/* 165:200 */     this.bpane = new JPanel();
/* 166:    */     
/* 167:202 */     JButton compdist = new JButton("Compute Ranking Distances");
/* 168:203 */     compdist.addActionListener(new ComputeRDBL(this));
/* 169:204 */     JButton aggregate = new JButton("Aggregate Rankings");
/* 170:205 */     aggregate.addActionListener(new AggregateBL(this));
/* 171:206 */     JButton importrs = new JButton("Import Rankings");
/* 172:207 */     importrs.addActionListener(new ImportRankingsBL(this));
/* 173:208 */     JButton exportrs = new JButton("Export Rankings");
/* 174:209 */     exportrs.addActionListener(new ExportRankingsBL(this));
/* 175:    */     
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:214 */     JButton maprankings = new JButton("Map Rankings to Nodes");
/* 180:215 */     maprankings.addActionListener(new MapRankingsBL(this));
/* 181:216 */     JButton importds = new JButton("Import Ranking Distances");
/* 182:217 */     importds.addActionListener(new ImportRDsBL(this));
/* 183:218 */     JButton exportds = new JButton("Export Ranking Distances");
/* 184:219 */     exportds.addActionListener(new ExportRDsBL(this));
/* 185:    */     
/* 186:    */ 
/* 187:222 */     GridBagLayout layout = new GridBagLayout();
/* 188:223 */     GridBagConstraints constraints = new GridBagConstraints();
/* 189:224 */     constraints.gridx = 0;
/* 190:225 */     constraints.gridy = 0;
/* 191:226 */     constraints.fill = 2;
/* 192:227 */     constraints.weighty = 0.0D;
/* 193:228 */     this.bpane.setLayout(layout);
/* 194:229 */     this.bpane.add(compdist, constraints);
/* 195:230 */     constraints.gridy += 1;
/* 196:231 */     this.bpane.add(aggregate, constraints);
/* 197:232 */     constraints.gridy += 1;
/* 198:233 */     this.bpane.add(importrs, constraints);
/* 199:234 */     constraints.gridy += 1;
/* 200:235 */     this.bpane.add(exportrs, constraints);
/* 201:236 */     constraints.gridy += 1;
/* 202:    */     
/* 203:    */ 
/* 204:239 */     this.bpane.add(importds, constraints);
/* 205:240 */     constraints.gridy += 1;
/* 206:241 */     this.bpane.add(exportds, constraints);
/* 207:242 */     constraints.gridy += 1;
/* 208:243 */     this.bpane.add(maprankings, constraints);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public Set<Ranking> getRankings()
/* 212:    */   {
/* 213:247 */     return this.rankings.keySet();
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void reset()
/* 217:    */   {
/* 218:251 */     this.rankings = new HashMap();
/* 219:252 */     this.n2r = new HashMap();
/* 220:253 */     this.n2d = new HashMap();
/* 221:254 */     this.srs = new String[0];
/* 222:255 */     this.sds = new String[0];
/* 223:256 */     this.ra = new Aggregator();
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void addRDMatrix(String[][] matrix)
/* 227:    */   {
/* 228:260 */     JTable t = new JTable(new DefaultTableModel(
/* 229:261 */       (Object[][])Arrays.copyOfRange(matrix, 1, matrix.length), matrix[0]));
/* 230:262 */     t.setName(Preferences.getRDist().description());
/* 231:263 */     t.getTableHeader().setBackground(Color.GRAY);
/* 232:264 */     this.rpane.addTab(t.getName(), new JScrollPane(t));
/* 233:265 */     this.n2d.put(t.getName(), t);
/* 234:266 */     createRankingPane(true);
/* 235:    */   }
/* 236:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.rankingmanager.RankingManager
 * JD-Core Version:    0.7.0.1
 */
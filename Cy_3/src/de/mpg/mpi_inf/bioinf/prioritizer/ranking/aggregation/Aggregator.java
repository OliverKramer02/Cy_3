/*   1:    */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation;
/*   2:    */ 
/*   3:    */ /*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;

/*   7:    */ import javax.swing.JOptionPane;

/*   4:    */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;
/*   8:    */ 
/*   9:    */ public class Aggregator
/*  10:    */ {
/*  11:    */   private AggregationAlgorithm aa;
/*  12:    */   private Map<Ranking, Double> wscheme;
/*  13:    */   private Map<String, Double> weights;
/*  14:    */   private String aggname;
/*  15:    */   
/*  16:    */   public Aggregator()
/*  17:    */   {
/*  18: 30 */     this.wscheme = new HashMap();
/*  19: 31 */     this.weights = new HashMap();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Ranking aggAll()
/*  23:    */   {
/*  24: 39 */     if (this.aa == null)
/*  25:    */     {
/*  26: 40 */       //JOptionPane.showMessageDialog(Cytoscape.getDesktop(), 
/*  27: 41 */         //"No rank aggregation algorithm has been selected. Please select one in the preferences.", 
/*  28:    */         //
/*  29: 43 */         //"No Rank Aggregation Algorithm Selected", 
/*  30: 44 */         //0);
/*  31: 45 */       return null;
/*  32:    */     }
/*  33: 47 */     if (this.wscheme.isEmpty())
/*  34:    */     {
/*  35: 48 */       //JOptionPane.showMessageDialog(Cytoscape.getDesktop(), 
/*  36: 49 */        // "<html><div style=\"width:300px;text-align:justify;\">There are no rankings to aggregate. Probably, something went wrong while selecting the primary rankings. Please make sure that there exist rankings and some of them are selected in the corresponding dialog.</div></html>", 
/*  37:    */         
/*  38:    */ 
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42: 55 */         //"No Rankings.", 0);
/*  43: 56 */       return null;
/*  44:    */     }
/*  45: 58 */     return this.aa.aggAll(this.wscheme, this.aggname);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setAlgorithm(AggregationAlgorithm aa)
/*  49:    */   {
/*  50: 67 */     this.aa = aa;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setWeights(Map<String, Double> weights)
/*  54:    */   {
/*  55: 75 */     this.weights = weights;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Map<String, Double> getWeights()
/*  59:    */   {
/*  60: 83 */     return this.weights;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public AggregationAlgorithm getAlgorithm()
/*  64:    */   {
/*  65: 93 */     return this.aa;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean addRanking(Ranking r)
/*  69:    */   {
/*  70:104 */     if ((this.aa.isWeighted()) && ((!this.weights.containsKey(r.getName())) || 
/*  71:105 */       (((Double)this.weights.get(r.getName())).doubleValue() == 0.0D))) {
/*  72:105 */       return false;
/*  73:    */     }
/*  74:106 */     this.wscheme.put(r, (Double)this.weights.get(r.getName()));
/*  75:107 */     return true;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void removeRanking(Ranking r)
/*  79:    */   {
/*  80:115 */     this.wscheme.remove(r);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void removeAllRankings()
/*  84:    */   {
/*  85:122 */     this.wscheme.clear();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void reset()
/*  89:    */   {
/*  90:130 */     this.wscheme.clear();
/*  91:131 */     this.weights.clear();
/*  92:132 */     this.aa = null;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean hasRanking()
/*  96:    */   {
/*  97:141 */     return !this.wscheme.isEmpty();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setAggregationName(String name)
/* 101:    */   {
/* 102:149 */     this.aggname = name;
/* 103:    */   }
/* 104:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.Aggregator
 * JD-Core Version:    0.7.0.1
 */
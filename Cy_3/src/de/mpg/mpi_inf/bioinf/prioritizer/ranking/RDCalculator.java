/*   1:    */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Set;
/*   7:    */ 
/*   8:    */ public class RDCalculator
/*   9:    */ {
/*  10:    */   private RankingDistance d;
/*  11:    */   private Set<String> rnameset;
/*  12:    */   private Map<String, Ranking> n2r;
/*  13:    */   private String[] rnamearray;
/*  14:    */   
/*  15:    */   public RDCalculator()
/*  16:    */   {
/*  17: 28 */     this.d = null;
/*  18: 29 */     this.rnameset = new HashSet();
/*  19: 30 */     this.n2r = new HashMap();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public RDCalculator(RankingDistance d)
/*  23:    */   {
/*  24: 39 */     this.d = d;
/*  25: 40 */     this.rnameset = new HashSet();
/*  26: 41 */     this.n2r = new HashMap();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean addRankingName(String rln)
/*  30:    */   {
/*  31: 51 */     if (this.rnameset.contains(rln)) {
/*  32: 51 */       return false;
/*  33:    */     }
/*  34: 52 */     this.rnameset.add(rln);
/*  35: 53 */     return true;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean addRanking(Ranking rl)
/*  39:    */   {
/*  40: 63 */     if (!this.rnameset.contains(rl.getName())) {
/*  41: 63 */       return false;
/*  42:    */     }
/*  43: 64 */     this.n2r.put(rl.getName(), rl);
/*  44: 65 */     return true;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double[][] calculateDistanceMatrix()
/*  48:    */   {
/*  49: 73 */     this.rnamearray = ((String[])this.n2r.keySet().toArray(
/*  50: 74 */       new String[this.n2r.keySet().size()]));
/*  51: 75 */     double[][] dists = new double[this.rnamearray.length][this.rnamearray.length];
/*  52: 76 */     for (int i = 0; i < this.rnamearray.length; i++) {
/*  53: 77 */       for (int j = 0; j < this.rnamearray.length; j++) {
/*  54: 78 */         if (this.rnamearray[i].equals(this.rnamearray[j])) {
/*  55: 78 */           dists[i][j] = 0.0D;
/*  56:    */         } else {
/*  57: 79 */           dists[i][j] = this.d.getDistance((Ranking)this.n2r.get(this.rnamearray[i]), 
/*  58: 80 */             (Ranking)this.n2r.get(this.rnamearray[j]));
/*  59:    */         }
/*  60:    */       }
/*  61:    */     }
/*  62: 82 */     return dists;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String[] getRankingNamesInMatrixOrder()
/*  66:    */   {
/*  67: 91 */     return this.rnamearray;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void removeAllRankings()
/*  71:    */   {
/*  72: 99 */     this.n2r.clear();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public RankingDistance getDistance()
/*  76:    */   {
/*  77:107 */     return this.d;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setDistance(RankingDistance d)
/*  81:    */   {
/*  82:115 */     this.d = d;
/*  83:    */   }
/*  84:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.RDCalculator
 * JD-Core Version:    0.7.0.1
 */
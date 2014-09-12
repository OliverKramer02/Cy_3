/*   1:    */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*   2:    */ 
/*   3:    */ import java.util.Comparator;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.PriorityQueue;
/*   9:    */ import java.util.Set;
/*  10:    */ 
/*  11:    */ public abstract class Ranking
/*  12:    */ {
/*  13:    */   private String[] elements;
/*  14:    */   private String name;
/*  15:    */   private Map<String, Double> scores;
/*  16:    */   private Map<String, Integer> e2r;
/*  17:    */   private Map<Integer, Set<String>> r2e;
/*  18:    */   private boolean inj;
/*  19:    */   
/*  20:    */   protected Ranking() {}
/*  21:    */   
/*  22:    */   public Ranking(Map<String, Double> tbr, Comparator<String> comp, String name, boolean inj)
/*  23:    */   {
/*  24: 50 */     this.name = name;
/*  25: 51 */     this.inj = inj;
/*  26: 52 */     this.elements = new String[tbr.size()];
/*  27: 53 */     this.scores = tbr;
/*  28: 54 */     this.e2r = new HashMap();
/*  29: 55 */     this.r2e = new HashMap();
/*  30: 56 */     PriorityQueue<String> q = new PriorityQueue(tbr.size(), comp);
/*  31:    */     String k;
/*  32: 57 */     for (Iterator localIterator = tbr.keySet().iterator(); localIterator.hasNext(); q.add(k)) {
/*  33: 57 */       k = (String)localIterator.next();
/*  34:    */     }
/*  35: 58 */     int i = q.size();
/*  36: 60 */     while (!q.isEmpty())
/*  37:    */     {
/*  38: 61 */       String s = (String)q.poll();
/*  39: 62 */       this.e2r.put(s, Integer.valueOf(i));
/*  40: 63 */       if (inj)
/*  41:    */       {
/*  42: 64 */         this.r2e.put(Integer.valueOf(i), new HashSet());
/*  43: 65 */         ((Set)this.r2e.get(Integer.valueOf(i))).add(s);
/*  44:    */       }
/*  45: 67 */       this.elements[(--i)] = s;
/*  46:    */     }
/*  47: 69 */     if (!inj)
/*  48:    */     {
/*  49: 70 */       double cs = ((Double)this.scores.get(this.elements[0])).doubleValue();
/*  50: 71 */       int j = 1;
/*  51: 72 */       this.r2e.put(Integer.valueOf(j), new HashSet());
/*  52: 73 */       for (int e = 0; e < this.elements.length; e++) {
/*  53: 74 */         if (((Double)this.scores.get(this.elements[e])).doubleValue() == cs)
/*  54:    */         {
/*  55: 75 */           this.e2r.put(this.elements[e], Integer.valueOf(j));
/*  56: 76 */           ((Set)this.r2e.get(Integer.valueOf(j))).add(this.elements[e]);
/*  57:    */         }
/*  58:    */         else
/*  59:    */         {
/*  60: 79 */           this.e2r.put(this.elements[e], Integer.valueOf(++j));
/*  61: 80 */           this.r2e.put(Integer.valueOf(j), new HashSet());
/*  62: 81 */           ((Set)this.r2e.get(Integer.valueOf(j))).add(this.elements[e]);
/*  63: 82 */           cs = ((Double)this.scores.get(this.elements[e])).doubleValue();
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean init(String name, String[] elements, Map<String, Double> scores, Map<String, Integer> e2r, Map<Integer, Set<String>> r2e, boolean inj)
/*  70:    */   {
/*  71:101 */     if ((this.name != null) || (this.elements != null) || (this.scores != null) || 
/*  72:102 */       (this.e2r != null) || (this.r2e != null)) {
/*  73:103 */       return false;
/*  74:    */     }
/*  75:104 */     this.name = name;
/*  76:105 */     this.elements = elements;
/*  77:106 */     this.scores = scores;
/*  78:107 */     this.e2r = e2r;
/*  79:108 */     this.r2e = r2e;
/*  80:109 */     this.inj = inj;
/*  81:110 */     return true;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public final double getScore(int r)
/*  85:    */   {
/*  86:119 */     return ((Double)this.scores.get(((Set)this.r2e.get(Integer.valueOf(r))).iterator().next())).doubleValue();
/*  87:    */   }
/*  88:    */   
/*  89:    */   public final int getRank(String s)
/*  90:    */   {
/*  91:129 */     return this.e2r.containsKey(s) ? ((Integer)this.e2r.get(s)).intValue() : -1;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public final Set<String> getSymbols(int r)
/*  95:    */   {
/*  96:138 */     return (Set)this.r2e.get(Integer.valueOf(r));
/*  97:    */   }
/*  98:    */   
/*  99:    */   public final String getName()
/* 100:    */   {
/* 101:146 */     return this.name;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setName(String name)
/* 105:    */   {
/* 106:154 */     this.name = name;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public final int size()
/* 110:    */   {
/* 111:162 */     return this.elements.length;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public final int rankCount()
/* 115:    */   {
/* 116:170 */     return this.r2e.size();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public final boolean isInjective()
/* 120:    */   {
/* 121:179 */     return this.inj;
/* 122:    */   }
/* 123:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*  2:   */ 
/*  3:   */ import java.io.BufferedReader;
/*  4:   */ import java.io.File;
/*  5:   */ import java.io.FileReader;
/*  6:   */ import java.io.IOException;
/*  7:   */ import java.util.ArrayList;
/*  8:   */ import java.util.HashMap;
/*  9:   */ import java.util.HashSet;
/* 10:   */ import java.util.List;
/* 11:   */ import java.util.Map;
/* 12:   */ import java.util.Set;
/* 13:   */ 
/* 14:   */ public class ImportedRanking
/* 15:   */   extends Ranking
/* 16:   */ {
/* 17:   */   public ImportedRanking(String rf)
/* 18:   */   {
/* 19:26 */     String title = rf.substring(rf.lastIndexOf('/') + 1, 
/* 20:27 */       rf.lastIndexOf('.'));
/* 21:28 */     List<String> elements = new ArrayList();
/* 22:29 */     Map<String, Double> scores = new HashMap();
/* 23:30 */     Map<String, Integer> e2r = new HashMap();
/* 24:31 */     Map<Integer, Set<String>> r2e = new HashMap();
/* 25:   */     int i;
/* 26:   */     try
/* 27:   */     {
/* 28:34 */       BufferedReader br = new BufferedReader(new FileReader(new File(rf)));
/* 29:   */       
/* 30:   */ 
/* 31:37 */       i = 0;int r = 0;
/* 32:   */       String line;
/* 33:38 */       while ((line = br.readLine()) != null)
/* 34:   */       {
/* 35:   */         
/* 36:39 */         String[] l = line.split("\t");
/* 37:40 */         elements.add(l[0]);
/* 38:41 */         scores.put(l[0], Double.valueOf(Double.parseDouble(l[1])));
/* 39:42 */         if (l.length == 3)
/* 40:   */         {
/* 41:43 */           e2r.put(l[0], Integer.valueOf(Integer.parseInt(l[2])));
/* 42:44 */           r = Integer.parseInt(l[2]);
/* 43:45 */           if (!r2e.containsKey(Integer.valueOf(r))) {
/* 44:45 */             r2e.put(Integer.valueOf(r), new HashSet());
/* 45:   */           }
/* 46:46 */           ((Set)r2e.get(Integer.valueOf(r))).add(l[0]);
/* 47:   */         }
/* 48:50 */         else if (l.length == 2)
/* 49:   */         {
/* 50:51 */           e2r.put(l[0], Integer.valueOf(++i));
/* 51:52 */           r2e.put(Integer.valueOf(i), new HashSet());
/* 52:53 */           ((Set)r2e.get(Integer.valueOf(i))).add(l[0]);
/* 53:   */         }
/* 54:   */         else
/* 55:   */         {
/* 56:56 */           br.close();
/* 57:57 */           throw new RuntimeException("The format of " + rf + " seems " + 
/* 58:58 */             "to be incorrect!");
/* 59:   */         }
/* 60:   */       }
/* 61:60 */       br.close();
/* 62:   */     }
/* 63:   */     catch (IOException ioe)
/* 64:   */     {
/* 65:61 */       ioe.printStackTrace();
/* 66:   */     }
/* 67:62 */     boolean inj = true;
/* 68:63 */     for (Integer r : r2e.keySet()) {
/* 69:64 */       if (((Set)r2e.get(r)).size() > 1)
/* 70:   */       {
/* 71:65 */         inj = false;
/* 72:66 */         break;
/* 73:   */       }
/* 74:   */     }
/* 75:69 */     if (!init(title, (String[])elements.toArray(new String[elements.size()]), scores, e2r, r2e, inj)) {
/* 76:70 */       throw new RuntimeException("Tried to initialize an existing RankList!");
/* 77:   */     }
/* 78:   */   }
/* 79:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.ImportedRanking
 * JD-Core Version:    0.7.0.1
 */
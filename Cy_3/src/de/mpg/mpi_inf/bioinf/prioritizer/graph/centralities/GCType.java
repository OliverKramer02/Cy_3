/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.graph.centralities;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum GCType
/*  7:   */ {
/*  8:13 */   GDEG("Generalized Degree Centrality"),  GSPB("Generalized Shortest Path Betweenness"),  GSPC("Generalized Shortest Path Closeness"),  GRWB("Generalized Random Walk Betweenness"),  GRWTC("Generalized Random Walk Transmitter Closeness"),  GRWRC("Generalized Random Walk Receiver Closeness");
/*  9:   */   
/* 10:   */   private final String description;
/* 11:27 */   private static final Map<String, GCType> DESC2CTYPE = new HashMap() {};
/* 12:   */   
/* 13:   */   private GCType(String description)
/* 14:   */   {
/* 15:38 */     this.description = description;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String description()
/* 19:   */   {
/* 20:46 */     return this.description;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static GCType getCentralityType(String description)
/* 24:   */   {
/* 25:57 */     return (GCType)DESC2CTYPE.get(description);
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.graph.centralities.GCType
 * JD-Core Version:    0.7.0.1
 */
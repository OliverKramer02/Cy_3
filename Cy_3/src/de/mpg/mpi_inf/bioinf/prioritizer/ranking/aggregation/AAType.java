/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum AAType
/*  7:   */ {
/*  8:13 */   WBF("Weighted Borda Fuse"),  WASF("Weighted AddScore Fuse"),  MRF("MaxRank Fuse");
/*  9:   */   
/* 10:   */   private final String description;
/* 11:23 */   private static final Map<String, AAType> DESC2TYPE = new HashMap() {};
/* 12:   */   
/* 13:   */   private AAType(String description)
/* 14:   */   {
/* 15:31 */     this.description = description;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String description()
/* 19:   */   {
/* 20:39 */     return this.description;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static AAType getType(String description)
/* 24:   */   {
/* 25:49 */     return (AAType)DESC2TYPE.get(description);
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.AAType
 * JD-Core Version:    0.7.0.1
 */
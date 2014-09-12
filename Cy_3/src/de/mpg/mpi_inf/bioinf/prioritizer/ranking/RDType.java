/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.ranking;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum RDType
/*  7:   */ {
/*  8:13 */   SF("Spearman Footrule"),  KT("Kendall Tau");
/*  9:   */   
/* 10:   */   private final String description;
/* 11:23 */   private static final Map<String, RDType> DESC2TYPE = new HashMap() {};
/* 12:   */   
/* 13:   */   private RDType(String description)
/* 14:   */   {
/* 15:31 */     this.description = description;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String description()
/* 19:   */   {
/* 20:39 */     return this.description;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static RDType getType(String description)
/* 24:   */   {
/* 25:49 */     return (RDType)DESC2TYPE.get(description);
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.RDType
 * JD-Core Version:    0.7.0.1
 */
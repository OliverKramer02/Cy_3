/*  1:   */ package de.mpg.mpi_inf.bioinf.prioritizer.graph;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum GDType
/*  7:   */ {
/*  8:13 */   STD("Standard Distance"),  INV("Inverted Distance"),  TINV("Tuned Inverted Distance");
/*  9:   */   
/* 10:   */   private final String description;
/* 11:23 */   private static final Map<String, GDType> DESC2TYPE = new HashMap() {};
/* 12:   */   
/* 13:   */   private GDType(String description)
/* 14:   */   {
/* 15:31 */     this.description = description;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String description()
/* 19:   */   {
/* 20:39 */     return this.description;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static GDType getType(String description)
/* 24:   */   {
/* 25:51 */     return (GDType)DESC2TYPE.get(description);
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.graph.GDType
 * JD-Core Version:    0.7.0.1
 */
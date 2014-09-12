/*   1:    */ package de.mpg.mpi_inf.bioinf.prioritizer;
/*   2:    */ 
/*   3:    */ import de.mpg.mpi_inf.bioinf.prioritizer.graph.GDType;
/*   4:    */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.RDType;
/*   5:    */ import de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.AAType;
/*   6:    */ 
/*   7:    */ public abstract class Preferences
/*   8:    */ {
/*   9: 22 */   private static GDType defgdist = GDType.TINV;
/*  10: 25 */   private static RDType defrdist = RDType.SF;
/*  11: 28 */   private static AAType defaggalg = AAType.WBF;
/*  12: 31 */   private static String defwatt = "none";
/*  13: 35 */   private static boolean definj = true;
/*  14: 39 */   private static boolean defwholeg = false;
/*  15:    */   
/*  16:    */   static
/*  17:    */   {
/*  18: 42 */     alpha = 0.8D;
/*  19: 43 */     gdist = defgdist;
/*  20: 44 */     rdist = defrdist;
/*  21: 45 */     aggalg = defaggalg;
/*  22: 46 */     inj = definj;
/*  23:    */   }
/*  24:    */   
/*  25: 47 */   private static String watt = defwatt;
/*  26: 48 */   private static boolean wholeg = defwholeg;
/*  27:    */   private static double alpha;
/*  28:    */   private static final double defaultalpha = 0.8D;
/*  29:    */   private static final boolean defaultweighted = false;
/*  30:    */   private static GDType gdist;
/*  31:    */   private static RDType rdist;
/*  32:    */   private static AAType aggalg;
/*  33:    */   private static boolean inj;
/*  34:    */   
/*  35:    */   public static void setAlpha(double newalpha)
/*  36:    */   {
/*  37: 56 */     alpha = newalpha;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static void setGDist(GDType newdist)
/*  41:    */   {
/*  42: 64 */     gdist = newdist;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static void setRLDist(RDType newdist)
/*  46:    */   {
/*  47: 72 */     rdist = newdist;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static void setRAAlg(AAType newaggalg)
/*  51:    */   {
/*  52: 80 */     aggalg = newaggalg;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void setWeightAtt(String attr)
/*  56:    */   {
/*  57: 88 */     watt = attr;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static double getAlpha()
/*  61:    */   {
/*  62: 96 */     return alpha;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static double getDefaultAlpha()
/*  66:    */   {
/*  67:104 */     return 0.8D;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static boolean getDefaultWeighted()
/*  71:    */   {
/*  72:112 */     return false;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static GDType getGDist()
/*  76:    */   {
/*  77:120 */     return gdist;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static GDType getDefaultGDist()
/*  81:    */   {
/*  82:128 */     return defgdist;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static RDType getRDist()
/*  86:    */   {
/*  87:136 */     return rdist;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static RDType getDefaultRLDist()
/*  91:    */   {
/*  92:144 */     return defrdist;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static String getWeightAtt()
/*  96:    */   {
/*  97:152 */     return watt;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static AAType getAAlg()
/* 101:    */   {
/* 102:160 */     return aggalg;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static AAType getDefaultRAAlg()
/* 106:    */   {
/* 107:168 */     return defaggalg;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public static boolean areInjective()
/* 111:    */   {
/* 112:177 */     return inj;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static void setInjective(Object object)
/* 116:    */   {
/* 117:186 */     inj = (boolean) object;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static String getDefaultWeightAtt()
/* 121:    */   {
/* 122:194 */     return defwatt;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static boolean getDefaultInjective()
/* 126:    */   {
/* 127:203 */     return definj;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static boolean compCentsForWholeGraph()
/* 131:    */   {
/* 132:213 */     return wholeg;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static void setCompCentsForWholeGraph(boolean b)
/* 136:    */   {
/* 137:223 */     wholeg = b;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static boolean getDefaultCompCentsForWholeGraph()
/* 141:    */   {
/* 142:232 */     return defwholeg;
/* 143:    */   }
/* 144:    */
public static void setInjective1(Object setSelected) {
	// TODO Auto-generated method stub
	
} }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.Preferences
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.geometry.euclidean.twod;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
/*   8:    */ import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
/*   9:    */ import org.apache.commons.math3.geometry.partitioning.Region;
/*  10:    */ import org.apache.commons.math3.geometry.partitioning.RegionFactory;
/*  11:    */ import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
/*  12:    */ 
/*  13:    */ class NestedLoops
/*  14:    */ {
/*  15:    */   private Vector2D[] loop;
/*  16:    */   private ArrayList<NestedLoops> surrounded;
/*  17:    */   private Region<Euclidean2D> polygon;
/*  18:    */   private boolean originalIsClockwise;
/*  19:    */   
/*  20:    */   public NestedLoops()
/*  21:    */   {
/*  22: 68 */     this.surrounded = new ArrayList();
/*  23:    */   }
/*  24:    */   
/*  25:    */   private NestedLoops(Vector2D[] loop)
/*  26:    */     throws MathIllegalArgumentException
/*  27:    */   {
/*  28: 78 */     if (loop[0] == null) {
/*  29: 79 */       throw new MathIllegalArgumentException(LocalizedFormats.OUTLINE_BOUNDARY_LOOP_OPEN, new Object[0]);
/*  30:    */     }
/*  31: 82 */     this.loop = loop;
/*  32: 83 */     this.surrounded = new ArrayList();
/*  33:    */     
/*  34:    */ 
/*  35: 86 */     ArrayList<SubHyperplane<Euclidean2D>> edges = new ArrayList();
/*  36: 87 */     Vector2D current = loop[(loop.length - 1)];
/*  37: 88 */     for (int i = 0; i < loop.length; i++)
/*  38:    */     {
/*  39: 89 */       Vector2D previous = current;
/*  40: 90 */       current = loop[i];
/*  41: 91 */       Line line = new Line(previous, current);
/*  42: 92 */       IntervalsSet region = new IntervalsSet(line.toSubSpace(previous).getX(), line.toSubSpace(current).getX());
/*  43:    */       
/*  44: 94 */       edges.add(new SubLine(line, region));
/*  45:    */     }
/*  46: 96 */     this.polygon = new PolygonsSet(edges);
/*  47: 99 */     if (Double.isInfinite(this.polygon.getSize()))
/*  48:    */     {
/*  49:100 */       this.polygon = new RegionFactory().getComplement(this.polygon);
/*  50:101 */       this.originalIsClockwise = false;
/*  51:    */     }
/*  52:    */     else
/*  53:    */     {
/*  54:103 */       this.originalIsClockwise = true;
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void add(Vector2D[] bLoop)
/*  59:    */     throws MathIllegalArgumentException
/*  60:    */   {
/*  61:114 */     add(new NestedLoops(bLoop));
/*  62:    */   }
/*  63:    */   
/*  64:    */   private void add(NestedLoops node)
/*  65:    */     throws MathIllegalArgumentException
/*  66:    */   {
/*  67:125 */     for (NestedLoops child : this.surrounded) {
/*  68:126 */       if (child.polygon.contains(node.polygon))
/*  69:    */       {
/*  70:127 */         child.add(node);
/*  71:128 */         return;
/*  72:    */       }
/*  73:    */     }
/*  74:133 */     for (Iterator<NestedLoops> iterator = this.surrounded.iterator(); iterator.hasNext();)
/*  75:    */     {
/*  76:134 */       NestedLoops child = (NestedLoops)iterator.next();
/*  77:135 */       if (node.polygon.contains(child.polygon))
/*  78:    */       {
/*  79:136 */         node.surrounded.add(child);
/*  80:137 */         iterator.remove();
/*  81:    */       }
/*  82:    */     }
/*  83:142 */     RegionFactory<Euclidean2D> factory = new RegionFactory();
/*  84:143 */     for (NestedLoops child : this.surrounded) {
/*  85:144 */       if (!factory.intersection(node.polygon, child.polygon).isEmpty()) {
/*  86:145 */         throw new MathIllegalArgumentException(LocalizedFormats.CROSSING_BOUNDARY_LOOPS, new Object[0]);
/*  87:    */       }
/*  88:    */     }
/*  89:149 */     this.surrounded.add(node);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void correctOrientation()
/*  93:    */   {
/*  94:159 */     for (NestedLoops child : this.surrounded) {
/*  95:160 */       child.setClockWise(true);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   private void setClockWise(boolean clockwise)
/* 100:    */   {
/* 101:170 */     if ((this.originalIsClockwise ^ clockwise))
/* 102:    */     {
/* 103:172 */       int min = -1;
/* 104:173 */       int max = this.loop.length;
/* 105:    */       for (;;)
/* 106:    */       {
/* 107:174 */         min++;
/* 108:174 */         if (min >= --max) {
/* 109:    */           break;
/* 110:    */         }
/* 111:175 */         Vector2D tmp = this.loop[min];
/* 112:176 */         this.loop[min] = this.loop[max];
/* 113:177 */         this.loop[max] = tmp;
/* 114:    */       }
/* 115:    */     }
/* 116:182 */     for (NestedLoops child : this.surrounded) {
/* 117:183 */       child.setClockWise(!clockwise);
/* 118:    */     }
/* 119:    */   }
/* 120:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.twod.NestedLoops
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.geometry.partitioning;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.geometry.Space;
/*   4:    */ 
/*   5:    */ public class RegionFactory<S extends Space>
/*   6:    */ {
/*   7:    */   private final RegionFactory<S>.NodesCleaner nodeCleaner;
/*   8:    */   
/*   9:    */   public RegionFactory()
/*  10:    */   {
/*  11: 36 */     this.nodeCleaner = new NodesCleaner();
/*  12:    */   }
/*  13:    */   
/*  14:    */   public Region<S> buildConvex(Hyperplane<S>... hyperplanes)
/*  15:    */   {
/*  16: 44 */     if ((hyperplanes == null) || (hyperplanes.length == 0)) {
/*  17: 45 */       return null;
/*  18:    */     }
/*  19: 49 */     Region<S> region = hyperplanes[0].wholeSpace();
/*  20:    */     
/*  21:    */ 
/*  22: 52 */     BSPTree<S> node = region.getTree(false);
/*  23: 53 */     node.setAttribute(Boolean.TRUE);
/*  24: 54 */     for (Hyperplane<S> hyperplane : hyperplanes) {
/*  25: 55 */       if (node.insertCut(hyperplane))
/*  26:    */       {
/*  27: 56 */         node.setAttribute(null);
/*  28: 57 */         node.getPlus().setAttribute(Boolean.FALSE);
/*  29: 58 */         node = node.getMinus();
/*  30: 59 */         node.setAttribute(Boolean.TRUE);
/*  31:    */       }
/*  32:    */     }
/*  33: 63 */     return region;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Region<S> union(Region<S> region1, Region<S> region2)
/*  37:    */   {
/*  38: 75 */     BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new UnionMerger());
/*  39:    */     
/*  40: 77 */     tree.visit(this.nodeCleaner);
/*  41: 78 */     return region1.buildNew(tree);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Region<S> intersection(Region<S> region1, Region<S> region2)
/*  45:    */   {
/*  46: 89 */     BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new IntersectionMerger());
/*  47:    */     
/*  48: 91 */     tree.visit(this.nodeCleaner);
/*  49: 92 */     return region1.buildNew(tree);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Region<S> xor(Region<S> region1, Region<S> region2)
/*  53:    */   {
/*  54:103 */     BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new XorMerger());
/*  55:    */     
/*  56:105 */     tree.visit(this.nodeCleaner);
/*  57:106 */     return region1.buildNew(tree);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Region<S> difference(Region<S> region1, Region<S> region2)
/*  61:    */   {
/*  62:117 */     BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new DifferenceMerger());
/*  63:    */     
/*  64:119 */     tree.visit(this.nodeCleaner);
/*  65:120 */     return region1.buildNew(tree);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Region<S> getComplement(Region<S> region)
/*  69:    */   {
/*  70:129 */     return region.buildNew(recurseComplement(region.getTree(false)));
/*  71:    */   }
/*  72:    */   
/*  73:    */   private BSPTree<S> recurseComplement(BSPTree<S> node)
/*  74:    */   {
/*  75:137 */     if (node.getCut() == null) {
/*  76:138 */       return new BSPTree(((Boolean)node.getAttribute()).booleanValue() ? Boolean.FALSE : Boolean.TRUE);
/*  77:    */     }
/*  78:142 */     BoundaryAttribute<S> attribute = (BoundaryAttribute)node.getAttribute();
/*  79:143 */     if (attribute != null)
/*  80:    */     {
/*  81:144 */       SubHyperplane<S> plusOutside = attribute.getPlusInside() == null ? null : attribute.getPlusInside().copySelf();
/*  82:    */       
/*  83:146 */       SubHyperplane<S> plusInside = attribute.getPlusOutside() == null ? null : attribute.getPlusOutside().copySelf();
/*  84:    */       
/*  85:148 */       attribute = new BoundaryAttribute(plusOutside, plusInside);
/*  86:    */     }
/*  87:151 */     return new BSPTree(node.getCut().copySelf(), recurseComplement(node.getPlus()), recurseComplement(node.getMinus()), attribute);
/*  88:    */   }
/*  89:    */   
/*  90:    */   private class UnionMerger
/*  91:    */     implements BSPTree.LeafMerger<S>
/*  92:    */   {
/*  93:    */     private UnionMerger() {}
/*  94:    */     
/*  95:    */     public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance)
/*  96:    */     {
/*  97:164 */       if (((Boolean)leaf.getAttribute()).booleanValue())
/*  98:    */       {
/*  99:166 */         leaf.insertInTree(parentTree, isPlusChild);
/* 100:167 */         return leaf;
/* 101:    */       }
/* 102:170 */       tree.insertInTree(parentTree, isPlusChild);
/* 103:171 */       return tree;
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   private class IntersectionMerger
/* 108:    */     implements BSPTree.LeafMerger<S>
/* 109:    */   {
/* 110:    */     private IntersectionMerger() {}
/* 111:    */     
/* 112:    */     public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance)
/* 113:    */     {
/* 114:181 */       if (((Boolean)leaf.getAttribute()).booleanValue())
/* 115:    */       {
/* 116:183 */         tree.insertInTree(parentTree, isPlusChild);
/* 117:184 */         return tree;
/* 118:    */       }
/* 119:187 */       leaf.insertInTree(parentTree, isPlusChild);
/* 120:188 */       return leaf;
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   private class XorMerger
/* 125:    */     implements BSPTree.LeafMerger<S>
/* 126:    */   {
/* 127:    */     private XorMerger() {}
/* 128:    */     
/* 129:    */     public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance)
/* 130:    */     {
/* 131:198 */       BSPTree<S> t = tree;
/* 132:199 */       if (((Boolean)leaf.getAttribute()).booleanValue()) {
/* 133:201 */         t = RegionFactory.this.recurseComplement(t);
/* 134:    */       }
/* 135:203 */       t.insertInTree(parentTree, isPlusChild);
/* 136:204 */       return t;
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   private class DifferenceMerger
/* 141:    */     implements BSPTree.LeafMerger<S>
/* 142:    */   {
/* 143:    */     private DifferenceMerger() {}
/* 144:    */     
/* 145:    */     public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance)
/* 146:    */     {
/* 147:214 */       if (((Boolean)leaf.getAttribute()).booleanValue())
/* 148:    */       {
/* 149:216 */         BSPTree<S> argTree = RegionFactory.this.recurseComplement(leafFromInstance ? tree : leaf);
/* 150:    */         
/* 151:218 */         argTree.insertInTree(parentTree, isPlusChild);
/* 152:219 */         return argTree;
/* 153:    */       }
/* 154:222 */       BSPTree<S> instanceTree = leafFromInstance ? leaf : tree;
/* 155:    */       
/* 156:224 */       instanceTree.insertInTree(parentTree, isPlusChild);
/* 157:225 */       return instanceTree;
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   private class NodesCleaner
/* 162:    */     implements BSPTreeVisitor<S>
/* 163:    */   {
/* 164:    */     private NodesCleaner() {}
/* 165:    */     
/* 166:    */     public BSPTreeVisitor.Order visitOrder(BSPTree<S> node)
/* 167:    */     {
/* 168:234 */       return BSPTreeVisitor.Order.PLUS_SUB_MINUS;
/* 169:    */     }
/* 170:    */     
/* 171:    */     public void visitInternalNode(BSPTree<S> node)
/* 172:    */     {
/* 173:239 */       node.setAttribute(null);
/* 174:    */     }
/* 175:    */     
/* 176:    */     public void visitLeafNode(BSPTree<S> node) {}
/* 177:    */   }
/* 178:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.RegionFactory
 * JD-Core Version:    0.7.0.1
 */
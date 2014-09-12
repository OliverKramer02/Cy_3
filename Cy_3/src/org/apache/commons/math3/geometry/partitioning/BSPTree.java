/*   1:    */ package org.apache.commons.math3.geometry.partitioning;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.geometry.Space;
/*   4:    */ import org.apache.commons.math3.geometry.Vector;
/*   5:    */ import org.apache.commons.math3.util.FastMath;
/*   6:    */ 
/*   7:    */ public class BSPTree<S extends Space>
/*   8:    */ {
/*   9:    */   private SubHyperplane<S> cut;
/*  10:    */   private BSPTree<S> plus;
/*  11:    */   private BSPTree<S> minus;
/*  12:    */   private BSPTree<S> parent;
/*  13:    */   private Object attribute;
/*  14:    */   
/*  15:    */   public BSPTree()
/*  16:    */   {
/*  17: 82 */     this.cut = null;
/*  18: 83 */     this.plus = null;
/*  19: 84 */     this.minus = null;
/*  20: 85 */     this.parent = null;
/*  21: 86 */     this.attribute = null;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public BSPTree(Object attribute)
/*  25:    */   {
/*  26: 93 */     this.cut = null;
/*  27: 94 */     this.plus = null;
/*  28: 95 */     this.minus = null;
/*  29: 96 */     this.parent = null;
/*  30: 97 */     this.attribute = attribute;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public BSPTree(SubHyperplane<S> cut, BSPTree<S> plus, BSPTree<S> minus, Object attribute)
/*  34:    */   {
/*  35:115 */     this.cut = cut;
/*  36:116 */     this.plus = plus;
/*  37:117 */     this.minus = minus;
/*  38:118 */     this.parent = null;
/*  39:119 */     this.attribute = attribute;
/*  40:120 */     plus.parent = this;
/*  41:121 */     minus.parent = this;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean insertCut(Hyperplane<S> hyperplane)
/*  45:    */   {
/*  46:149 */     if (this.cut != null)
/*  47:    */     {
/*  48:150 */       this.plus.parent = null;
/*  49:151 */       this.minus.parent = null;
/*  50:    */     }
/*  51:154 */     SubHyperplane<S> chopped = fitToCell(hyperplane.wholeHyperplane());
/*  52:155 */     if (chopped.isEmpty())
/*  53:    */     {
/*  54:156 */       this.cut = null;
/*  55:157 */       this.plus = null;
/*  56:158 */       this.minus = null;
/*  57:159 */       return false;
/*  58:    */     }
/*  59:162 */     this.cut = chopped;
/*  60:163 */     this.plus = new BSPTree();
/*  61:164 */     this.plus.parent = this;
/*  62:165 */     this.minus = new BSPTree();
/*  63:166 */     this.minus.parent = this;
/*  64:167 */     return true;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public BSPTree<S> copySelf()
/*  68:    */   {
/*  69:180 */     if (this.cut == null) {
/*  70:181 */       return new BSPTree(this.attribute);
/*  71:    */     }
/*  72:184 */     return new BSPTree(this.cut.copySelf(), this.plus.copySelf(), this.minus.copySelf(), this.attribute);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public SubHyperplane<S> getCut()
/*  76:    */   {
/*  77:193 */     return this.cut;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public BSPTree<S> getPlus()
/*  81:    */   {
/*  82:201 */     return this.plus;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public BSPTree<S> getMinus()
/*  86:    */   {
/*  87:209 */     return this.minus;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public BSPTree<S> getParent()
/*  91:    */   {
/*  92:216 */     return this.parent;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setAttribute(Object attribute)
/*  96:    */   {
/*  97:224 */     this.attribute = attribute;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Object getAttribute()
/* 101:    */   {
/* 102:234 */     return this.attribute;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void visit(BSPTreeVisitor<S> visitor)
/* 106:    */   {
/* 107:241 */     if (this.cut == null) {
/* 108:242 */       visitor.visitLeafNode(this);
/* 109:    */     } else {
/* 110:244 */       switch (visitor.visitOrder(this).ordinal())
/* 111:    */       {
/* 112:    */       case 1: 
/* 113:246 */         this.plus.visit(visitor);
/* 114:247 */         this.minus.visit(visitor);
/* 115:248 */         visitor.visitInternalNode(this);
/* 116:249 */         break;
/* 117:    */       case 2: 
/* 118:251 */         this.plus.visit(visitor);
/* 119:252 */         visitor.visitInternalNode(this);
/* 120:253 */         this.minus.visit(visitor);
/* 121:254 */         break;
/* 122:    */       case 3: 
/* 123:256 */         this.minus.visit(visitor);
/* 124:257 */         this.plus.visit(visitor);
/* 125:258 */         visitor.visitInternalNode(this);
/* 126:259 */         break;
/* 127:    */       case 4: 
/* 128:261 */         this.minus.visit(visitor);
/* 129:262 */         visitor.visitInternalNode(this);
/* 130:263 */         this.plus.visit(visitor);
/* 131:264 */         break;
/* 132:    */       case 5: 
/* 133:266 */         visitor.visitInternalNode(this);
/* 134:267 */         this.plus.visit(visitor);
/* 135:268 */         this.minus.visit(visitor);
/* 136:269 */         break;
/* 137:    */       case 6: 
/* 138:271 */         visitor.visitInternalNode(this);
/* 139:272 */         this.minus.visit(visitor);
/* 140:273 */         this.plus.visit(visitor);
/* 141:274 */         break;
/* 142:    */       default: 
/* 143:276 */         throw new RuntimeException("internal error");
/* 144:    */       }
/* 145:    */     }
/* 146:    */   }
/* 147:    */   
/* 148:    */   private SubHyperplane<S> fitToCell(SubHyperplane<S> sub)
/* 149:    */   {
/* 150:291 */     SubHyperplane<S> s = sub;
/* 151:292 */     for (BSPTree<S> tree = this; tree.parent != null; tree = tree.parent) {
/* 152:293 */       if (tree == tree.parent.plus) {
/* 153:294 */         s = s.split(tree.parent.cut.getHyperplane()).getPlus();
/* 154:    */       } else {
/* 155:296 */         s = s.split(tree.parent.cut.getHyperplane()).getMinus();
/* 156:    */       }
/* 157:    */     }
/* 158:299 */     return s;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public BSPTree<S> getCell(Vector<S> point)
/* 162:    */   {
/* 163:311 */     if (this.cut == null) {
/* 164:312 */       return this;
/* 165:    */     }
/* 166:316 */     double offset = this.cut.getHyperplane().getOffset(point);
/* 167:318 */     if (FastMath.abs(offset) < 1.0E-010D) {
/* 168:319 */       return this;
/* 169:    */     }
/* 170:320 */     if (offset <= 0.0D) {
/* 171:322 */       return this.minus.getCell(point);
/* 172:    */     }
/* 173:325 */     return this.plus.getCell(point);
/* 174:    */   }
/* 175:    */   
/* 176:    */   private void condense()
/* 177:    */   {
/* 178:335 */     if ((this.cut != null) && (this.plus.cut == null) && (this.minus.cut == null) && (((this.plus.attribute == null) && (this.minus.attribute == null)) || ((this.plus.attribute != null) && (this.plus.attribute.equals(this.minus.attribute)))))
/* 179:    */     {
/* 180:338 */       this.attribute = (this.plus.attribute == null ? this.minus.attribute : this.plus.attribute);
/* 181:339 */       this.cut = null;
/* 182:340 */       this.plus = null;
/* 183:341 */       this.minus = null;
/* 184:    */     }
/* 185:    */   }
/* 186:    */   
/* 187:    */   public BSPTree<S> merge(BSPTree<S> tree, LeafMerger<S> leafMerger)
/* 188:    */   {
/* 189:364 */     return merge(tree, leafMerger, null, false);
/* 190:    */   }
/* 191:    */   
/* 192:    */   private BSPTree<S> merge(BSPTree<S> tree, LeafMerger<S> leafMerger, BSPTree<S> parentTree, boolean isPlusChild)
/* 193:    */   {
/* 194:384 */     if (this.cut == null) {
/* 195:386 */       return leafMerger.merge(this, tree, parentTree, isPlusChild, true);
/* 196:    */     }
/* 197:387 */     if (tree.cut == null) {
/* 198:389 */       return leafMerger.merge(tree, this, parentTree, isPlusChild, false);
/* 199:    */     }
/* 200:392 */     BSPTree<S> merged = tree.split(this.cut);
/* 201:393 */     if (parentTree != null)
/* 202:    */     {
/* 203:394 */       merged.parent = parentTree;
/* 204:395 */       if (isPlusChild) {
/* 205:396 */         parentTree.plus = merged;
/* 206:    */       } else {
/* 207:398 */         parentTree.minus = merged;
/* 208:    */       }
/* 209:    */     }
/* 210:403 */     this.plus.merge(merged.plus, leafMerger, merged, true);
/* 211:404 */     this.minus.merge(merged.minus, leafMerger, merged, false);
/* 212:405 */     merged.condense();
/* 213:406 */     if (merged.cut != null) {
/* 214:407 */       merged.cut = merged.fitToCell(merged.cut.getHyperplane().wholeHyperplane());
/* 215:    */     }
/* 216:411 */     return merged;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public BSPTree<S> split(SubHyperplane<S> sub)
/* 220:    */   {
/* 221:488 */     if (this.cut == null) {
/* 222:489 */       return new BSPTree(sub, copySelf(), new BSPTree(this.attribute), null);
/* 223:    */     }
/* 224:493 */     Hyperplane<S> cHyperplane = this.cut.getHyperplane();
/* 225:494 */     Hyperplane<S> sHyperplane = sub.getHyperplane();
/* 226:495 */     switch (sub.side(cHyperplane).ordinal())
/* 227:    */     {
/* 228:    */     case 1: 
/* 229:498 */       BSPTree<S> split = this.plus.split(sub);
/* 230:499 */       if (this.cut.side(sHyperplane) == Side.PLUS)
/* 231:    */       {
/* 232:500 */         split.plus = new BSPTree(this.cut.copySelf(), split.plus, this.minus.copySelf(), this.attribute);
/* 233:    */         
/* 234:502 */         split.plus.condense();
/* 235:503 */         split.plus.parent = split;
/* 236:    */       }
/* 237:    */       else
/* 238:    */       {
/* 239:505 */         split.minus = new BSPTree(this.cut.copySelf(), split.minus, this.minus.copySelf(), this.attribute);
/* 240:    */         
/* 241:507 */         split.minus.condense();
/* 242:508 */         split.minus.parent = split;
/* 243:    */       }
/* 244:510 */       return split;
/* 245:    */     case 2: 
/* 246:514 */       BSPTree<S> split1 = this.minus.split(sub);
/* 247:515 */       if (this.cut.side(sHyperplane) == Side.PLUS)
/* 248:    */       {
/* 249:516 */         split1.plus = new BSPTree(this.cut.copySelf(), this.plus.copySelf(), split1.plus, this.attribute);
/* 250:    */         
/* 251:518 */         split1.plus.condense();
/* 252:519 */         split1.plus.parent = split1;
/* 253:    */       }
/* 254:    */       else
/* 255:    */       {
/* 256:521 */         split1.minus = new BSPTree(this.cut.copySelf(), this.plus.copySelf(), split1.minus, this.attribute);
/* 257:    */         
/* 258:523 */         split1.minus.condense();
/* 259:524 */         split1.minus.parent = split1;
/* 260:    */       }
/* 261:526 */       return split1;
/* 262:    */     case 3: 
/* 263:530 */       SubHyperplane.SplitSubHyperplane<S> cutParts = this.cut.split(sHyperplane);
/* 264:531 */       SubHyperplane.SplitSubHyperplane<S> subParts = sub.split(cHyperplane);
/* 265:532 */       BSPTree<S> split11 = new BSPTree(sub, this.plus.split(subParts.getPlus()), this.minus.split(subParts.getMinus()), null);
/* 266:    */       
/* 267:    */ 
/* 268:535 */       split11.plus.cut = cutParts.getPlus();
/* 269:536 */       split11.minus.cut = cutParts.getMinus();
/* 270:537 */       BSPTree<S> tmp = split11.plus.minus;
/* 271:538 */       split11.plus.minus = split11.minus.plus;
/* 272:539 */       split11.plus.minus.parent = split11.plus;
/* 273:540 */       split11.minus.plus = tmp;
/* 274:541 */       split11.minus.plus.parent = split11.minus;
/* 275:542 */       split11.plus.condense();
/* 276:543 */       split11.minus.condense();
/* 277:544 */       return split11;
/* 278:    */     }
/* 279:547 */     return cHyperplane.sameOrientationAs(sHyperplane) ? new BSPTree(sub, this.plus.copySelf(), this.minus.copySelf(), this.attribute) : new BSPTree(sub, this.minus.copySelf(), this.plus.copySelf(), this.attribute);
/* 280:    */   }
/* 281:    */   
/* 282:    */   public void insertInTree(BSPTree<S> parentTree, boolean isPlusChild)
/* 283:    */   {
/* 284:566 */     this.parent = parentTree;
/* 285:567 */     if (parentTree != null) {
/* 286:568 */       if (isPlusChild) {
/* 287:569 */         parentTree.plus = this;
/* 288:    */       } else {
/* 289:571 */         parentTree.minus = this;
/* 290:    */       }
/* 291:    */     }
/* 292:576 */     if (this.cut != null)
/* 293:    */     {
/* 294:579 */       for (BSPTree<S> tree = this; tree.parent != null; tree = tree.parent)
/* 295:    */       {
/* 296:582 */         Hyperplane<S> hyperplane = tree.parent.cut.getHyperplane();
/* 297:586 */         if (tree == tree.parent.plus)
/* 298:    */         {
/* 299:587 */           this.cut = this.cut.split(hyperplane).getPlus();
/* 300:588 */           this.plus.chopOffMinus(hyperplane);
/* 301:589 */           this.minus.chopOffMinus(hyperplane);
/* 302:    */         }
/* 303:    */         else
/* 304:    */         {
/* 305:591 */           this.cut = this.cut.split(hyperplane).getMinus();
/* 306:592 */           this.plus.chopOffPlus(hyperplane);
/* 307:593 */           this.minus.chopOffPlus(hyperplane);
/* 308:    */         }
/* 309:    */       }
/* 310:600 */       condense();
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   private void chopOffMinus(Hyperplane<S> hyperplane)
/* 315:    */   {
/* 316:613 */     if (this.cut != null)
/* 317:    */     {
/* 318:614 */       this.cut = this.cut.split(hyperplane).getPlus();
/* 319:615 */       this.plus.chopOffMinus(hyperplane);
/* 320:616 */       this.minus.chopOffMinus(hyperplane);
/* 321:    */     }
/* 322:    */   }
/* 323:    */   
/* 324:    */   private void chopOffPlus(Hyperplane<S> hyperplane)
/* 325:    */   {
/* 326:627 */     if (this.cut != null)
/* 327:    */     {
/* 328:628 */       this.cut = this.cut.split(hyperplane).getMinus();
/* 329:629 */       this.plus.chopOffPlus(hyperplane);
/* 330:630 */       this.minus.chopOffPlus(hyperplane);
/* 331:    */     }
/* 332:    */   }
/* 333:    */   
/* 334:    */   public static abstract interface LeafMerger<S extends Space>
/* 335:    */   {
/* 336:    */     public abstract BSPTree<S> merge(BSPTree<S> paramBSPTree1, BSPTree<S> paramBSPTree2, BSPTree<S> paramBSPTree3, boolean paramBoolean1, boolean paramBoolean2);
/* 337:    */   }
/* 338:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.BSPTree
 * JD-Core Version:    0.7.0.1
 */
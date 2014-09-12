/*   1:    */ package org.apache.commons.math3.geometry.partitioning;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Comparator;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.TreeSet;

/*   8:    */ import org.apache.commons.math3.geometry.Space;
/*   9:    */ import org.apache.commons.math3.geometry.Vector;
/*  10:    */ 
/*  11:    */ public abstract class AbstractRegion<S extends Space, T extends Space>
/*  12:    */   implements Region<S>
/*  13:    */ {
/*  14:    */   private BSPTree<S> tree;
/*  15:    */   private double size;
/*  16:    */   private Vector<S> barycenter;
/*  17:    */   
/*  18:    */   protected AbstractRegion()
/*  19:    */   {
/*  20: 50 */     this.tree = new BSPTree(Boolean.TRUE);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected AbstractRegion(BSPTree<S> tree)
/*  24:    */   {
/*  25: 66 */     this.tree = tree;
/*  26:    */   }
/*  27:    */   
/*  28:    */   @SuppressWarnings({ "rawtypes", "unchecked" })
protected AbstractRegion(Collection<SubHyperplane<S>> boundary)
/*  29:    */   {
/*  30: 90 */     if (boundary.size() == 0)
/*  31:    */     {
/*  32: 93 */       this.tree = new BSPTree(Boolean.TRUE);
/*  33:    */     }
/*  34:    */     else
/*  35:    */     {
/*  36:100 */       TreeSet<SubHyperplane<S>> ordered = new TreeSet(new Comparator()
/*  37:    */       {
/*  38:    */         public int compare(SubHyperplane<S> o1, SubHyperplane<S> o2)
/*  39:    */         {
/*  40:102 */           double size1 = o1.getSize();
/*  41:103 */           double size2 = o2.getSize();
/*  42:104 */           return o1 == o2 ? 0 : size2 < size1 ? -1 : 1;
/*  43:    */         }
/*  44:106 */

@Override
public int compare(Object o1, Object o2) {
	// TODO Auto-generated method stub
	return 0;
}       });
/*  45:107 */       ordered.addAll(boundary);
/*  46:    */       
/*  47:    */ 
/*  48:110 */       this.tree = new BSPTree();
/*  49:111 */       insertCuts(this.tree, ordered);
/*  50:    */       
/*  51:    */ 
/*  52:114 */       //this.tree.visit(new BSPTreeVisitor()
/*  53:    */      // {
/*  54:    */       //  public BSPTreeVisitor.Order visitOrder(BSPTree<S> node)
/*  55:    */        // {
/*  56:118 */          // return BSPTreeVisitor.Order.PLUS_SUB_MINUS;
/*  57:    */         //}


      
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public AbstractRegion(Hyperplane<S>[] hyperplanes)
/*  70:    */   {
/*  71:141 */     if ((hyperplanes == null) || (hyperplanes.length == 0))
/*  72:    */     {
/*  73:142 */       this.tree = new BSPTree(Boolean.FALSE);
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77:146 */       this.tree = hyperplanes[0].wholeSpace().getTree(false);
/*  78:    */       
/*  79:    */ 
/*  80:149 */       BSPTree<S> node = this.tree;
/*  81:150 */       node.setAttribute(Boolean.TRUE);
/*  82:151 */       for (Hyperplane<S> hyperplane : hyperplanes) {
/*  83:152 */         if (node.insertCut(hyperplane))
/*  84:    */         {
/*  85:153 */           node.setAttribute(null);
/*  86:154 */           node.getPlus().setAttribute(Boolean.FALSE);
/*  87:155 */           node = node.getMinus();
/*  88:156 */           node.setAttribute(Boolean.TRUE);
/*  89:    */         }
/*  90:    */       }
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public abstract AbstractRegion<S, T> buildNew(BSPTree<S> paramBSPTree);
/*  95:    */   
/*  96:    */   private void insertCuts(BSPTree<S> node, Collection<SubHyperplane<S>> boundary)
/*  97:    */   {
/*  98:175 */     Iterator<SubHyperplane<S>> iterator = boundary.iterator();
/*  99:    */     
/* 100:    */ 
/* 101:178 */     Hyperplane<S> inserted = null;
/* 102:179 */     while ((inserted == null) && (iterator.hasNext()))
/* 103:    */     {
/* 104:180 */       inserted = ((SubHyperplane)iterator.next()).getHyperplane();
/* 105:181 */       if (!node.insertCut(inserted.copySelf())) {
/* 106:182 */         inserted = null;
/* 107:    */       }
/* 108:    */     }
/* 109:186 */     if (!iterator.hasNext()) {
/* 110:187 */       return;
/* 111:    */     }
/* 112:191 */     ArrayList<SubHyperplane<S>> plusList = new ArrayList();
/* 113:192 */     ArrayList<SubHyperplane<S>> minusList = new ArrayList();
/* 114:193 */     while (iterator.hasNext())
/* 115:    */     {
/* 116:194 */       SubHyperplane<S> other = (SubHyperplane)iterator.next();
/* 117:195 */       switch (other.side(inserted).ordinal())
/* 118:    */       {
/* 119:    */       case 1: 
/* 120:197 */         plusList.add(other);
/* 121:198 */         break;
/* 122:    */       case 2: 
/* 123:200 */         minusList.add(other);
/* 124:201 */         break;
/* 125:    */       case 3: 
/* 126:203 */         SubHyperplane.SplitSubHyperplane<S> split = other.split(inserted);
/* 127:204 */         plusList.add(split.getPlus());
/* 128:205 */         minusList.add(split.getMinus());
/* 129:    */       }
/* 130:    */     }
/* 131:213 */     insertCuts(node.getPlus(), plusList);
/* 132:214 */     insertCuts(node.getMinus(), minusList);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public AbstractRegion<S, T> copySelf()
/* 136:    */   {
/* 137:220 */     return buildNew(this.tree.copySelf());
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean isEmpty()
/* 141:    */   {
/* 142:225 */     return isEmpty(this.tree);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public boolean isEmpty(BSPTree<S> node)
/* 146:    */   {
/* 147:235 */     if (node.getCut() == null) {
/* 148:237 */       return !((Boolean)node.getAttribute()).booleanValue();
/* 149:    */     }
/* 150:241 */     return (isEmpty(node.getMinus())) && (isEmpty(node.getPlus()));
/* 151:    */   }
/* 152:    */   
/* 153:    */   public boolean contains(Region<S> region)
/* 154:    */   {
/* 155:247 */     return new RegionFactory().difference(region, this).isEmpty();
/* 156:    */   }
/* 157:    */   
/* 158:    */   public Region.Location checkPoint(Vector<S> point)
/* 159:    */   {
/* 160:252 */     return checkPoint(this.tree, point);
/* 161:    */   }
/* 162:    */   
/* 163:    */   protected Region.Location checkPoint(BSPTree<S> node, Vector<S> point)
/* 164:    */   {
/* 165:263 */     BSPTree<S> cell = node.getCell(point);
/* 166:264 */     if (cell.getCut() == null) {
/* 167:266 */       return ((Boolean)cell.getAttribute()).booleanValue() ? Region.Location.INSIDE : Region.Location.OUTSIDE;
/* 168:    */     }
/* 169:270 */     Region.Location minusCode = checkPoint(cell.getMinus(), point);
/* 170:271 */     Region.Location plusCode = checkPoint(cell.getPlus(), point);
/* 171:272 */     return minusCode == plusCode ? minusCode : Region.Location.BOUNDARY;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public BSPTree<S> getTree(boolean includeBoundaryAttributes)
/* 175:    */   {
/* 176:278 */     if ((includeBoundaryAttributes) && (this.tree.getCut() != null) && (this.tree.getAttribute() == null)) {
/* 177:280 */       recurseBuildBoundary(this.tree);
/* 178:    */     }
/* 179:282 */     return this.tree;
/* 180:    */   }
/* 181:    */   
/* 182:    */   private void recurseBuildBoundary(BSPTree<S> node)
/* 183:    */   {
/* 184:289 */     if (node.getCut() != null)
/* 185:    */     {
/* 186:291 */       SubHyperplane<S> plusOutside = null;
/* 187:292 */       SubHyperplane<S> plusInside = null;
/* 188:    */       
/* 189:    */ 
/* 190:    */ 
/* 191:296 */       Characterization<S> plusChar = new Characterization();
/* 192:297 */       characterize(node.getPlus(), node.getCut().copySelf(), plusChar);
/* 193:299 */       if (plusChar.hasOut())
/* 194:    */       {
/* 195:304 */         Characterization<S> minusChar = new Characterization();
/* 196:305 */         characterize(node.getMinus(), plusChar.getOut(), minusChar);
/* 197:306 */         if (minusChar.hasIn()) {
/* 198:307 */           plusOutside = minusChar.getIn();
/* 199:    */         }
/* 200:    */       }
/* 201:311 */       if (plusChar.hasIn())
/* 202:    */       {
/* 203:316 */         Characterization<S> minusChar = new Characterization();
/* 204:317 */         characterize(node.getMinus(), plusChar.getIn(), minusChar);
/* 205:318 */         if (minusChar.hasOut()) {
/* 206:319 */           plusInside = minusChar.getOut();
/* 207:    */         }
/* 208:    */       }
/* 209:323 */       node.setAttribute(new BoundaryAttribute(plusOutside, plusInside));
/* 210:324 */       recurseBuildBoundary(node.getPlus());
/* 211:325 */       recurseBuildBoundary(node.getMinus());
/* 212:    */     }
/* 213:    */   }
/* 214:    */   
/* 215:    */   private void characterize(BSPTree<S> node, SubHyperplane<S> sub, Characterization<S> characterization)
/* 216:    */   {
/* 217:345 */     if (node.getCut() == null)
/* 218:    */     {
/* 219:347 */       boolean inside = ((Boolean)node.getAttribute()).booleanValue();
/* 220:348 */       characterization.add(sub, inside);
/* 221:    */     }
/* 222:    */     else
/* 223:    */     {
/* 224:350 */       Hyperplane<S> hyperplane = node.getCut().getHyperplane();
/* 225:351 */       switch (sub.side(hyperplane).ordinal())
/* 226:    */       {
/* 227:    */       case 1: 
/* 228:353 */         characterize(node.getPlus(), sub, characterization);
/* 229:354 */         break;
/* 230:    */       case 2: 
/* 231:356 */         characterize(node.getMinus(), sub, characterization);
/* 232:357 */         break;
/* 233:    */       case 3: 
/* 234:359 */         SubHyperplane.SplitSubHyperplane<S> split = sub.split(hyperplane);
/* 235:360 */         characterize(node.getPlus(), split.getPlus(), characterization);
/* 236:361 */         characterize(node.getMinus(), split.getMinus(), characterization);
/* 237:362 */         break;
/* 238:    */       default: 
/* 239:365 */         throw new RuntimeException("internal error");
/* 240:    */       }
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:    */   public double getBoundarySize()
/* 245:    */   {
/* 246:372 */     BoundarySizeVisitor<S> visitor = new BoundarySizeVisitor();
/* 247:373 */     getTree(true).visit(visitor);
/* 248:374 */     return visitor.getSize();
/* 249:    */   }
/* 250:    */   
/* 251:    */   public double getSize()
/* 252:    */   {
/* 253:379 */     if (this.barycenter == null) {
/* 254:380 */       computeGeometricalProperties();
/* 255:    */     }
/* 256:382 */     return this.size;
/* 257:    */   }
/* 258:    */   
/* 259:    */   protected void setSize(double size)
/* 260:    */   {
/* 261:389 */     this.size = size;
/* 262:    */   }
/* 263:    */   
/* 264:    */   public Vector<S> getBarycenter()
/* 265:    */   {
/* 266:394 */     if (this.barycenter == null) {
/* 267:395 */       computeGeometricalProperties();
/* 268:    */     }
/* 269:397 */     return this.barycenter;
/* 270:    */   }
/* 271:    */   
/* 272:    */   protected void setBarycenter(Vector<S> barycenter)
/* 273:    */   {
/* 274:404 */     this.barycenter = barycenter;
/* 275:    */   }
/* 276:    */   
/* 277:    */   protected abstract void computeGeometricalProperties();
/* 278:    */   
/* 279:    */   public Side side(Hyperplane<S> hyperplane)
/* 280:    */   {
/* 281:414 */     Sides sides = new Sides();
/* 282:415 */     recurseSides(this.tree, hyperplane.wholeHyperplane(), sides);
/* 283:416 */     return sides.minusFound() ? Side.MINUS : sides.plusFound() ? Side.PLUS : sides.minusFound() ? Side.BOTH : Side.HYPER;
/* 284:    */   }
/* 285:    */   
/* 286:    */   private void recurseSides(BSPTree<S> node, SubHyperplane<S> sub, Sides sides)
/* 287:    */   {
/* 288:438 */     if (node.getCut() == null)
/* 289:    */     {
/* 290:439 */       if (((Boolean)node.getAttribute()).booleanValue())
/* 291:    */       {
/* 292:441 */         sides.rememberPlusFound();
/* 293:442 */         sides.rememberMinusFound();
/* 294:    */       }
/* 295:444 */       return;
/* 296:    */     }
/* 297:447 */     Hyperplane<S> hyperplane = node.getCut().getHyperplane();
/* 298:448 */     switch (sub.side(hyperplane).ordinal())
/* 299:    */     {
/* 300:    */     case 1: 
/* 301:451 */       if (node.getCut().side(sub.getHyperplane()) == Side.PLUS)
/* 302:    */       {
/* 303:452 */         if (!isEmpty(node.getMinus())) {
/* 304:453 */           sides.rememberPlusFound();
/* 305:    */         }
/* 306:    */       }
/* 307:456 */       else if (!isEmpty(node.getMinus())) {
/* 308:457 */         sides.rememberMinusFound();
/* 309:    */       }
/* 310:460 */       if ((!sides.plusFound()) || (!sides.minusFound())) {
/* 311:461 */         recurseSides(node.getPlus(), sub, sides);
/* 312:    */       }
/* 313:    */       break;
/* 314:    */     case 2: 
/* 315:466 */       if (node.getCut().side(sub.getHyperplane()) == Side.PLUS)
/* 316:    */       {
/* 317:467 */         if (!isEmpty(node.getPlus())) {
/* 318:468 */           sides.rememberPlusFound();
/* 319:    */         }
/* 320:    */       }
/* 321:471 */       else if (!isEmpty(node.getPlus())) {
/* 322:472 */         sides.rememberMinusFound();
/* 323:    */       }
/* 324:475 */       if ((!sides.plusFound()) || (!sides.minusFound())) {
/* 325:476 */         recurseSides(node.getMinus(), sub, sides);
/* 326:    */       }
/* 327:    */       break;
/* 328:    */     case 3: 
/* 329:481 */       SubHyperplane.SplitSubHyperplane<S> split = sub.split(hyperplane);
/* 330:    */       
/* 331:    */ 
/* 332:484 */       recurseSides(node.getPlus(), split.getPlus(), sides);
/* 333:487 */       if ((!sides.plusFound()) || (!sides.minusFound())) {
/* 334:488 */         recurseSides(node.getMinus(), split.getMinus(), sides);
/* 335:    */       }
/* 336:    */       break;
/* 337:    */     default: 
/* 338:493 */       if (node.getCut().getHyperplane().sameOrientationAs(sub.getHyperplane()))
/* 339:    */       {
/* 340:494 */         if ((node.getPlus().getCut() != null) || (((Boolean)node.getPlus().getAttribute()).booleanValue())) {
/* 341:495 */           sides.rememberPlusFound();
/* 342:    */         }
/* 343:497 */         if ((node.getMinus().getCut() != null) || (((Boolean)node.getMinus().getAttribute()).booleanValue())) {
/* 344:498 */           sides.rememberMinusFound();
/* 345:    */         }
/* 346:    */       }
/* 347:    */       else
/* 348:    */       {
/* 349:501 */         if ((node.getPlus().getCut() != null) || (((Boolean)node.getPlus().getAttribute()).booleanValue())) {
/* 350:502 */           sides.rememberMinusFound();
/* 351:    */         }
/* 352:504 */         if ((node.getMinus().getCut() != null) || (((Boolean)node.getMinus().getAttribute()).booleanValue())) {
/* 353:505 */           sides.rememberPlusFound();
/* 354:    */         }
/* 355:    */       }
/* 356:    */       break;
/* 357:    */     }
/* 358:    */   }
/* 359:    */   
/* 360:    */   private static final class Sides
/* 361:    */   {
/* 362:    */     private boolean plusFound;
/* 363:    */     private boolean minusFound;
/* 364:    */     
/* 365:    */     public Sides()
/* 366:    */     {
/* 367:524 */       this.plusFound = false;
/* 368:525 */       this.minusFound = false;
/* 369:    */     }
/* 370:    */     
/* 371:    */     public void rememberPlusFound()
/* 372:    */     {
/* 373:531 */       this.plusFound = true;
/* 374:    */     }
/* 375:    */     
/* 376:    */     public boolean plusFound()
/* 377:    */     {
/* 378:538 */       return this.plusFound;
/* 379:    */     }
/* 380:    */     
/* 381:    */     public void rememberMinusFound()
/* 382:    */     {
/* 383:544 */       this.minusFound = true;
/* 384:    */     }
/* 385:    */     
/* 386:    */     public boolean minusFound()
/* 387:    */     {
/* 388:551 */       return this.minusFound;
/* 389:    */     }
/* 390:    */   }
/* 391:    */   
/* 392:    */   public SubHyperplane<S> intersection(SubHyperplane<S> sub)
/* 393:    */   {
/* 394:558 */     return recurseIntersection(this.tree, sub);
/* 395:    */   }
/* 396:    */   
/* 397:    */   private SubHyperplane<S> recurseIntersection(BSPTree<S> node, SubHyperplane<S> sub)
/* 398:    */   {
/* 399:569 */     if (node.getCut() == null) {
/* 400:570 */       return ((Boolean)node.getAttribute()).booleanValue() ? sub.copySelf() : null;
/* 401:    */     }
/* 402:573 */     Hyperplane<S> hyperplane = node.getCut().getHyperplane();
/* 403:574 */     switch (sub.side(hyperplane).ordinal())
/* 404:    */     {
/* 405:    */     case 1: 
/* 406:576 */       return recurseIntersection(node.getPlus(), sub);
/* 407:    */     case 2: 
/* 408:578 */       return recurseIntersection(node.getMinus(), sub);
/* 409:    */     case 3: 
/* 410:580 */       SubHyperplane.SplitSubHyperplane<S> split = sub.split(hyperplane);
/* 411:581 */       SubHyperplane<S> plus = recurseIntersection(node.getPlus(), split.getPlus());
/* 412:582 */       SubHyperplane<S> minus = recurseIntersection(node.getMinus(), split.getMinus());
/* 413:583 */       if (plus == null) {
/* 414:584 */         return minus;
/* 415:    */       }
/* 416:585 */       if (minus == null) {
/* 417:586 */         return plus;
/* 418:    */       }
/* 419:588 */       return plus.reunite(minus);
/* 420:    */     }
/* 421:591 */     return recurseIntersection(node.getPlus(), recurseIntersection(node.getMinus(), sub));
/* 422:    */   }
/* 423:    */   
/* 424:    */   public AbstractRegion<S, T> applyTransform(Transform<S, T> transform)
/* 425:    */   {
/* 426:608 */     return buildNew(recurseTransform(getTree(false), transform));
/* 427:    */   }
/* 428:    */   
/* 429:    */   private BSPTree<S> recurseTransform(BSPTree<S> node, Transform<S, T> transform)
/* 430:    */   {
/* 431:619 */     if (node.getCut() == null) {
/* 432:620 */       return new BSPTree(node.getAttribute());
/* 433:    */     }
/* 434:623 */     SubHyperplane<S> sub = node.getCut();
/* 435:624 */     SubHyperplane<S> tSub = ((AbstractSubHyperplane)sub).applyTransform(transform);
/* 436:625 */     BoundaryAttribute<S> attribute = (BoundaryAttribute)node.getAttribute();
/* 437:626 */     if (attribute != null)
/* 438:    */     {
/* 439:627 */       SubHyperplane<S> tPO = attribute.getPlusOutside() == null ? null : ((AbstractSubHyperplane)attribute.getPlusOutside()).applyTransform(transform);
/* 440:    */       
/* 441:629 */       SubHyperplane<S> tPI = attribute.getPlusInside() == null ? null : ((AbstractSubHyperplane)attribute.getPlusInside()).applyTransform(transform);
/* 442:    */       
/* 443:631 */       attribute = new BoundaryAttribute(tPO, tPI);
/* 444:    */     }
/* 445:634 */     return new BSPTree(tSub, recurseTransform(node.getPlus(), transform), recurseTransform(node.getMinus(), transform), attribute);
/* 446:    */   }
/* 447:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.AbstractRegion
 * JD-Core Version:    0.7.0.1
 */
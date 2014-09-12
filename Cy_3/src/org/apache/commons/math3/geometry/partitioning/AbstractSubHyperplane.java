/*   1:    */ package org.apache.commons.math3.geometry.partitioning;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.geometry.Space;
/*   4:    */ 
/*   5:    */ public abstract class AbstractSubHyperplane<S extends Space, T extends Space>
/*   6:    */   implements SubHyperplane<S>
/*   7:    */ {
/*   8:    */   private final Hyperplane<S> hyperplane;
/*   9:    */   private final Region<T> remainingRegion;
/*  10:    */   
/*  11:    */   protected AbstractSubHyperplane(Hyperplane<S> hyperplane, Region<T> remainingRegion)
/*  12:    */   {
/*  13: 52 */     this.hyperplane = hyperplane;
/*  14: 53 */     this.remainingRegion = remainingRegion;
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected abstract AbstractSubHyperplane<S, T> buildNew(Hyperplane<S> paramHyperplane, Region<T> paramRegion);
/*  18:    */   
/*  19:    */   public AbstractSubHyperplane<S, T> copySelf()
/*  20:    */   {
/*  21: 66 */     return buildNew(this.hyperplane, this.remainingRegion);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Hyperplane<S> getHyperplane()
/*  25:    */   {
/*  26: 73 */     return this.hyperplane;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Region<T> getRemainingRegion()
/*  30:    */   {
/*  31: 84 */     return this.remainingRegion;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getSize()
/*  35:    */   {
/*  36: 89 */     return this.remainingRegion.getSize();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public AbstractSubHyperplane<S, T> reunite(SubHyperplane<S> other)
/*  40:    */   {
/*  41: 95 */     AbstractSubHyperplane<S, T> o = (AbstractSubHyperplane)other;
/*  42: 96 */     return buildNew(this.hyperplane, new RegionFactory().union(this.remainingRegion, o.remainingRegion));
/*  43:    */   }
/*  44:    */   
/*  45:    */   public AbstractSubHyperplane<S, T> applyTransform(Transform<S, T> transform)
/*  46:    */   {
/*  47:111 */     Hyperplane<S> tHyperplane = transform.apply(this.hyperplane);
/*  48:112 */     BSPTree<T> tTree = recurseTransform(this.remainingRegion.getTree(false), tHyperplane, transform);
/*  49:    */     
/*  50:114 */     return buildNew(tHyperplane, this.remainingRegion.buildNew(tTree));
/*  51:    */   }
/*  52:    */   
/*  53:    */   private BSPTree<T> recurseTransform(BSPTree<T> node, Hyperplane<S> transformed, Transform<S, T> transform)
/*  54:    */   {
/*  55:126 */     if (node.getCut() == null) {
/*  56:127 */       return new BSPTree(node.getAttribute());
/*  57:    */     }
/*  58:131 */     BoundaryAttribute<T> attribute = (BoundaryAttribute)node.getAttribute();
/*  59:133 */     if (attribute != null)
/*  60:    */     {
/*  61:134 */       SubHyperplane<T> tPO = attribute.getPlusOutside() == null ? null : transform.apply(attribute.getPlusOutside(), this.hyperplane, transformed);
/*  62:    */       
/*  63:136 */       SubHyperplane<T> tPI = attribute.getPlusInside() == null ? null : transform.apply(attribute.getPlusInside(), this.hyperplane, transformed);
/*  64:    */       
/*  65:138 */       attribute = new BoundaryAttribute(tPO, tPI);
/*  66:    */     }
/*  67:141 */     return new BSPTree(transform.apply(node.getCut(), this.hyperplane, transformed), recurseTransform(node.getPlus(), transformed, transform), recurseTransform(node.getMinus(), transformed, transform), attribute);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public abstract Side side(Hyperplane<S> paramHyperplane);
/*  71:    */   
/*  72:    */   public abstract SubHyperplane.SplitSubHyperplane<S> split(Hyperplane<S> paramHyperplane);
/*  73:    */   
/*  74:    */   public boolean isEmpty()
/*  75:    */   {
/*  76:156 */     return this.remainingRegion.isEmpty();
/*  77:    */   }
/*  78:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane
 * JD-Core Version:    0.7.0.1
 */
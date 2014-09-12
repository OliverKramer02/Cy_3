/*   1:    */ package org.apache.commons.math3.geometry.euclidean.oned;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.geometry.Vector;
/*   4:    */ import org.apache.commons.math3.geometry.partitioning.Hyperplane;
/*   5:    */ 
/*   6:    */ public class OrientedPoint
/*   7:    */   implements Hyperplane<Euclidean1D>
/*   8:    */ {
/*   9:    */   private Vector1D location;
/*  10:    */   private boolean direct;
/*  11:    */   
/*  12:    */   public OrientedPoint(Vector1D location, boolean direct)
/*  13:    */   {
/*  14: 43 */     this.location = location;
/*  15: 44 */     this.direct = direct;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public OrientedPoint copySelf()
/*  19:    */   {
/*  20: 53 */     return this;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double getOffset(Vector<Euclidean1D> point)
/*  24:    */   {
/*  25: 58 */     double delta = ((Vector1D)point).getX() - this.location.getX();
/*  26: 59 */     return this.direct ? delta : -delta;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public SubOrientedPoint wholeHyperplane()
/*  30:    */   {
/*  31: 75 */     return new SubOrientedPoint(this, null);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public IntervalsSet wholeSpace()
/*  35:    */   {
/*  36: 83 */     return new IntervalsSet();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public boolean sameOrientationAs(Hyperplane<Euclidean1D> other)
/*  40:    */   {
/*  41: 88 */     return !(this.direct ^ ((OrientedPoint)other).direct);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Vector1D getLocation()
/*  45:    */   {
/*  46: 95 */     return this.location;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isDirect()
/*  50:    */   {
/*  51:103 */     return this.direct;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void revertSelf()
/*  55:    */   {
/*  56:109 */     this.direct = (!this.direct);
/*  57:    */   }
/*  58:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.optimization;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.util.Pair;
/*   4:    */ 
/*   5:    */ public class PointVectorValuePair
/*   6:    */   extends Pair<double[], double[]>
/*   7:    */ {
/*   8:    */   public PointVectorValuePair(double[] point, double[] value)
/*   9:    */   {
/*  10: 41 */     this(point, value, true);
/*  11:    */   }
/*  12:    */   
/*  13:    */   public PointVectorValuePair(double[] point, double[] value, boolean copyArray)
/*  14:    */   {
/*  15: 55 */     super(copyArray ? (double[])point.clone() : point == null ? null : point, copyArray ? (double[])value.clone() : value == null ? null : value);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public double[] getPoint()
/*  19:    */   {
/*  20: 71 */     double[] p = (double[])getKey();
/*  21: 72 */     return p == null ? null : (double[])p.clone();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public double[] getPointRef()
/*  25:    */   {
/*  26: 81 */     return (double[])getKey();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public double[] getValue()
/*  30:    */   {
/*  31: 91 */     double[] v = (double[])super.getValue();
/*  32: 92 */     return v == null ? null : (double[])v.clone();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public double[] getValueRef()
/*  36:    */   {
/*  37:102 */     return (double[])super.getValue();
/*  38:    */   }
/*  39:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.PointVectorValuePair
 * JD-Core Version:    0.7.0.1
 */
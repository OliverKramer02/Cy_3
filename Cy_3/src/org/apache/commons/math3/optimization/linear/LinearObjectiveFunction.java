/*   1:    */ package org.apache.commons.math3.optimization.linear;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.ObjectOutputStream;
/*   6:    */ import java.io.Serializable;
/*   7:    */ import org.apache.commons.math3.linear.ArrayRealVector;
/*   8:    */ import org.apache.commons.math3.linear.MatrixUtils;
/*   9:    */ import org.apache.commons.math3.linear.RealVector;
/*  10:    */ 
/*  11:    */ public class LinearObjectiveFunction
/*  12:    */   implements Serializable
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = -4531815507568396090L;
/*  15:    */   private final transient RealVector coefficients;
/*  16:    */   private final double constantTerm;
/*  17:    */   
/*  18:    */   public LinearObjectiveFunction(double[] coefficients, double constantTerm)
/*  19:    */   {
/*  20: 58 */     this(new ArrayRealVector(coefficients), constantTerm);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public LinearObjectiveFunction(RealVector coefficients, double constantTerm)
/*  24:    */   {
/*  25: 66 */     this.coefficients = coefficients;
/*  26: 67 */     this.constantTerm = constantTerm;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public RealVector getCoefficients()
/*  30:    */   {
/*  31: 75 */     return this.coefficients;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getConstantTerm()
/*  35:    */   {
/*  36: 83 */     return this.constantTerm;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public double getValue(double[] point)
/*  40:    */   {
/*  41: 92 */     return this.coefficients.dotProduct(new ArrayRealVector(point, false)) + this.constantTerm;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public double getValue(RealVector point)
/*  45:    */   {
/*  46:101 */     return this.coefficients.dotProduct(point) + this.constantTerm;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean equals(Object other)
/*  50:    */   {
/*  51:108 */     if (this == other) {
/*  52:109 */       return true;
/*  53:    */     }
/*  54:112 */     if ((other instanceof LinearObjectiveFunction))
/*  55:    */     {
/*  56:113 */       LinearObjectiveFunction rhs = (LinearObjectiveFunction)other;
/*  57:114 */       return (this.constantTerm == rhs.constantTerm) && (this.coefficients.equals(rhs.coefficients));
/*  58:    */     }
/*  59:117 */     return false;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int hashCode()
/*  63:    */   {
/*  64:123 */     return Double.valueOf(this.constantTerm).hashCode() ^ this.coefficients.hashCode();
/*  65:    */   }
/*  66:    */   
/*  67:    */   private void writeObject(ObjectOutputStream oos)
/*  68:    */     throws IOException
/*  69:    */   {
/*  70:132 */     oos.defaultWriteObject();
/*  71:133 */     MatrixUtils.serializeRealVector(this.coefficients, oos);
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void readObject(ObjectInputStream ois)
/*  75:    */     throws ClassNotFoundException, IOException
/*  76:    */   {
/*  77:143 */     ois.defaultReadObject();
/*  78:144 */     MatrixUtils.deserializeRealVector(this, "coefficients", ois);
/*  79:    */   }
/*  80:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.LinearObjectiveFunction
 * JD-Core Version:    0.7.0.1
 */
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
/*  11:    */ public class LinearConstraint
/*  12:    */   implements Serializable
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = -764632794033034092L;
/*  15:    */   private final transient RealVector coefficients;
/*  16:    */   private final Relationship relationship;
/*  17:    */   private final double value;
/*  18:    */   
/*  19:    */   public LinearConstraint(double[] coefficients, Relationship relationship, double value)
/*  20:    */   {
/*  21: 81 */     this(new ArrayRealVector(coefficients), relationship, value);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public LinearConstraint(RealVector coefficients, Relationship relationship, double value)
/*  25:    */   {
/*  26:100 */     this.coefficients = coefficients;
/*  27:101 */     this.relationship = relationship;
/*  28:102 */     this.value = value;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public LinearConstraint(double[] lhsCoefficients, double lhsConstant, Relationship relationship, double[] rhsCoefficients, double rhsConstant)
/*  32:    */   {
/*  33:127 */     double[] sub = new double[lhsCoefficients.length];
/*  34:128 */     for (int i = 0; i < sub.length; i++) {
/*  35:129 */       lhsCoefficients[i] -= rhsCoefficients[i];
/*  36:    */     }
/*  37:131 */     this.coefficients = new ArrayRealVector(sub, false);
/*  38:132 */     this.relationship = relationship;
/*  39:133 */     this.value = (rhsConstant - lhsConstant);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public LinearConstraint(RealVector lhsCoefficients, double lhsConstant, Relationship relationship, RealVector rhsCoefficients, double rhsConstant)
/*  43:    */   {
/*  44:158 */     this.coefficients = lhsCoefficients.subtract(rhsCoefficients);
/*  45:159 */     this.relationship = relationship;
/*  46:160 */     this.value = (rhsConstant - lhsConstant);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public RealVector getCoefficients()
/*  50:    */   {
/*  51:168 */     return this.coefficients;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Relationship getRelationship()
/*  55:    */   {
/*  56:176 */     return this.relationship;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public double getValue()
/*  60:    */   {
/*  61:184 */     return this.value;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean equals(Object other)
/*  65:    */   {
/*  66:191 */     if (this == other) {
/*  67:192 */       return true;
/*  68:    */     }
/*  69:195 */     if ((other instanceof LinearConstraint))
/*  70:    */     {
/*  71:196 */       LinearConstraint rhs = (LinearConstraint)other;
/*  72:197 */       return (this.relationship == rhs.relationship) && (this.value == rhs.value) && (this.coefficients.equals(rhs.coefficients));
/*  73:    */     }
/*  74:201 */     return false;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int hashCode()
/*  78:    */   {
/*  79:207 */     return this.relationship.hashCode() ^ Double.valueOf(this.value).hashCode() ^ this.coefficients.hashCode();
/*  80:    */   }
/*  81:    */   
/*  82:    */   private void writeObject(ObjectOutputStream oos)
/*  83:    */     throws IOException
/*  84:    */   {
/*  85:218 */     oos.defaultWriteObject();
/*  86:219 */     MatrixUtils.serializeRealVector(this.coefficients, oos);
/*  87:    */   }
/*  88:    */   
/*  89:    */   private void readObject(ObjectInputStream ois)
/*  90:    */     throws ClassNotFoundException, IOException
/*  91:    */   {
/*  92:229 */     ois.defaultReadObject();
/*  93:230 */     MatrixUtils.deserializeRealVector(this, "coefficients", ois);
/*  94:    */   }
/*  95:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.optimization.linear.LinearConstraint
 * JD-Core Version:    0.7.0.1
 */
/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.List;
/*   7:    */ 
/*   8:    */ public abstract class AbstractListChromosome<T>
/*   9:    */   extends Chromosome
/*  10:    */ {
/*  11:    */   private final List<T> representation;
/*  12:    */   
/*  13:    */   public AbstractListChromosome(List<T> representation)
/*  14:    */   {
/*  15: 43 */     checkValidity(representation);
/*  16: 44 */     this.representation = Collections.unmodifiableList(new ArrayList(representation));
/*  17:    */   }
/*  18:    */   
/*  19:    */   public AbstractListChromosome(T[] representation)
/*  20:    */   {
/*  21: 52 */     this(Arrays.asList(representation));
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected abstract void checkValidity(List<T> paramList)
/*  25:    */     throws InvalidRepresentationException;
/*  26:    */   
/*  27:    */   protected List<T> getRepresentation()
/*  28:    */   {
/*  29: 69 */     return this.representation;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getLength()
/*  33:    */   {
/*  34: 77 */     return getRepresentation().size();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public abstract AbstractListChromosome<T> newFixedLengthChromosome(List<T> paramList);
/*  38:    */   
/*  39:    */   public String toString()
/*  40:    */   {
/*  41:100 */     return String.format("(f=%s %s)", new Object[] { Double.valueOf(getFitness()), getRepresentation() });
/*  42:    */   }
/*  43:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.AbstractListChromosome
 * JD-Core Version:    0.7.0.1
 */
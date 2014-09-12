/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.random.RandomGenerator;
/*   8:    */ 
/*   9:    */ public abstract class BinaryChromosome
/*  10:    */   extends AbstractListChromosome<Integer>
/*  11:    */ {
/*  12:    */   public BinaryChromosome(List<Integer> representation)
/*  13:    */   {
/*  14: 39 */     super(representation);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public BinaryChromosome(Integer[] representation)
/*  18:    */   {
/*  19: 49 */     super(representation);
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected void checkValidity(List<Integer> chromosomeRepresentation)
/*  23:    */     throws InvalidRepresentationException
/*  24:    */   {
/*  25: 58 */     for (Iterator i$ = chromosomeRepresentation.iterator(); i$.hasNext();)
/*  26:    */     {
/*  27: 58 */       int i = ((Integer)i$.next()).intValue();
/*  28: 59 */       if ((i < 0) || (i > 1)) {
/*  29: 60 */         throw new InvalidRepresentationException(LocalizedFormats.INVALID_BINARY_DIGIT, new Object[] { Integer.valueOf(i) });
/*  30:    */       }
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static List<Integer> randomBinaryRepresentation(int length)
/*  35:    */   {
/*  36: 73 */     List<Integer> rList = new ArrayList(length);
/*  37: 74 */     for (int j = 0; j < length; j++) {
/*  38: 75 */       rList.add(Integer.valueOf(GeneticAlgorithm.getRandomGenerator().nextInt(2)));
/*  39:    */     }
/*  40: 77 */     return rList;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected boolean isSame(Chromosome another)
/*  44:    */   {
/*  45: 86 */     if (!(another instanceof BinaryChromosome)) {
/*  46: 87 */       return false;
/*  47:    */     }
/*  48: 89 */     BinaryChromosome anotherBc = (BinaryChromosome)another;
/*  49: 91 */     if (getLength() != anotherBc.getLength()) {
/*  50: 92 */       return false;
/*  51:    */     }
/*  52: 95 */     for (int i = 0; i < getRepresentation().size(); i++) {
/*  53: 96 */       if (!((Integer)getRepresentation().get(i)).equals(anotherBc.getRepresentation().get(i))) {
/*  54: 97 */         return false;
/*  55:    */       }
/*  56:    */     }
/*  57:101 */     return true;
/*  58:    */   }
/*  59:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.BinaryChromosome
 * JD-Core Version:    0.7.0.1
 */
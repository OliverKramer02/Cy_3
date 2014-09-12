/*  1:   */ package org.apache.commons.math3.genetics;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ import org.apache.commons.math3.random.RandomGenerator;
/*  8:   */ 
/*  9:   */ public class BinaryMutation
/* 10:   */   implements MutationPolicy
/* 11:   */ {
/* 12:   */   public Chromosome mutate(Chromosome original)
/* 13:   */   {
/* 14:41 */     if (!(original instanceof BinaryChromosome)) {
/* 15:42 */       throw new MathIllegalArgumentException(LocalizedFormats.INVALID_BINARY_CHROMOSOME, new Object[0]);
/* 16:   */     }
/* 17:45 */     BinaryChromosome origChrom = (BinaryChromosome)original;
/* 18:46 */     List<Integer> newRepr = new ArrayList(origChrom.getRepresentation());
/* 19:   */     
/* 20:   */ 
/* 21:49 */     int geneIndex = GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
/* 22:   */     
/* 23:51 */     newRepr.set(geneIndex, Integer.valueOf(((Integer)origChrom.getRepresentation().get(geneIndex)).intValue() == 0 ? 1 : 0));
/* 24:   */     
/* 25:53 */     Chromosome newChrom = origChrom.newFixedLengthChromosome(newRepr);
/* 26:54 */     return newChrom;
/* 27:   */   }
/* 28:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.BinaryMutation
 * JD-Core Version:    0.7.0.1
 */
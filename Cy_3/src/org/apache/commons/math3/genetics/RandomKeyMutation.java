/*  1:   */ package org.apache.commons.math3.genetics;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  6:   */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  7:   */ import org.apache.commons.math3.random.RandomGenerator;
/*  8:   */ 
/*  9:   */ public class RandomKeyMutation
/* 10:   */   implements MutationPolicy
/* 11:   */ {
/* 12:   */   public Chromosome mutate(Chromosome original)
/* 13:   */   {
/* 14:41 */     if (!(original instanceof RandomKey)) {
/* 15:42 */       throw new MathIllegalArgumentException(LocalizedFormats.RANDOMKEY_MUTATION_WRONG_CLASS, new Object[] { original.getClass().getSimpleName() });
/* 16:   */     }
/* 17:46 */     RandomKey<?> originalRk = (RandomKey)original;
/* 18:47 */     List<Double> repr = originalRk.getRepresentation();
/* 19:48 */     int rInd = GeneticAlgorithm.getRandomGenerator().nextInt(repr.size());
/* 20:   */     
/* 21:50 */     List<Double> newRepr = new ArrayList(repr);
/* 22:51 */     newRepr.set(rInd, Double.valueOf(GeneticAlgorithm.getRandomGenerator().nextDouble()));
/* 23:   */     
/* 24:53 */     return originalRk.newFixedLengthChromosome(newRepr);
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.RandomKeyMutation
 * JD-Core Version:    0.7.0.1
 */
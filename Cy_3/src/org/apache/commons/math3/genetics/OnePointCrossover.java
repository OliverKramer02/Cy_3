/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   7:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   8:    */ import org.apache.commons.math3.random.RandomGenerator;
/*   9:    */ 
/*  10:    */ public class OnePointCrossover<T>
/*  11:    */   implements CrossoverPolicy
/*  12:    */ {
/*  13:    */   public ChromosomePair crossover(Chromosome first, Chromosome second)
/*  14:    */   {
/*  15: 78 */     if ((!(first instanceof AbstractListChromosome)) || (!(second instanceof AbstractListChromosome))) {
/*  16: 79 */       throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
/*  17:    */     }
/*  18: 81 */     return crossover((AbstractListChromosome)first, (AbstractListChromosome)second);
/*  19:    */   }
/*  20:    */   
/*  21:    */   private ChromosomePair crossover(AbstractListChromosome<T> first, AbstractListChromosome<T> second)
/*  22:    */   {
/*  23: 95 */     int length = first.getLength();
/*  24: 96 */     if (length != second.getLength()) {
/*  25: 97 */       throw new DimensionMismatchException(second.getLength(), length);
/*  26:    */     }
/*  27:101 */     List<T> parent1Rep = first.getRepresentation();
/*  28:102 */     List<T> parent2Rep = second.getRepresentation();
/*  29:    */     
/*  30:104 */     ArrayList<T> child1Rep = new ArrayList(first.getLength());
/*  31:105 */     ArrayList<T> child2Rep = new ArrayList(second.getLength());
/*  32:    */     
/*  33:    */ 
/*  34:108 */     int crossoverIndex = 1 + GeneticAlgorithm.getRandomGenerator().nextInt(length - 2);
/*  35:111 */     for (int i = 0; i < crossoverIndex; i++)
/*  36:    */     {
/*  37:112 */       child1Rep.add(parent1Rep.get(i));
/*  38:113 */       child2Rep.add(parent2Rep.get(i));
/*  39:    */     }
/*  40:116 */     for (int i = crossoverIndex; i < length; i++)
/*  41:    */     {
/*  42:117 */       child1Rep.add(parent2Rep.get(i));
/*  43:118 */       child2Rep.add(parent1Rep.get(i));
/*  44:    */     }
/*  45:121 */     return new ChromosomePair(first.newFixedLengthChromosome(child1Rep), second.newFixedLengthChromosome(child2Rep));
/*  46:    */   }
/*  47:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.OnePointCrossover
 * JD-Core Version:    0.7.0.1
 */
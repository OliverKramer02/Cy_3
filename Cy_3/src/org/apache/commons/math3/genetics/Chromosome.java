/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ public abstract class Chromosome
/*   4:    */   implements Comparable<Chromosome>, Fitness
/*   5:    */ {
/*   6:    */   private static final double NO_FITNESS = (-1.0D / 0.0D);
/*   7: 33 */   private double fitness = (-1.0D / 0.0D);
/*   8:    */   
/*   9:    */   public double getFitness()
/*  10:    */   {
/*  11: 45 */     if (this.fitness == (-1.0D / 0.0D)) {
/*  12: 47 */       this.fitness = fitness();
/*  13:    */     }
/*  14: 49 */     return this.fitness;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public int compareTo(Chromosome another)
/*  18:    */   {
/*  19: 65 */     return Double.valueOf(getFitness()).compareTo(Double.valueOf(another.getFitness()));
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected boolean isSame(Chromosome another)
/*  23:    */   {
/*  24: 76 */     return false;
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected Chromosome findSameChromosome(Population population)
/*  28:    */   {
/*  29: 89 */     for (Chromosome anotherChr : population) {
/*  30: 90 */       if (isSame(anotherChr)) {
/*  31: 91 */         return anotherChr;
/*  32:    */       }
/*  33:    */     }
/*  34: 94 */     return null;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void searchForFitnessUpdate(Population population)
/*  38:    */   {
/*  39:104 */     Chromosome sameChromosome = findSameChromosome(population);
/*  40:105 */     if (sameChromosome != null) {
/*  41:106 */       this.fitness = sameChromosome.getFitness();
/*  42:    */     }
/*  43:    */   }
/*  44:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.Chromosome
 * JD-Core Version:    0.7.0.1
 */
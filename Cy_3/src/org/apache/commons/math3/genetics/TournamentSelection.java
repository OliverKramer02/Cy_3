/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.random.RandomGenerator;
/*   8:    */ 
/*   9:    */ public class TournamentSelection
/*  10:    */   implements SelectionPolicy
/*  11:    */ {
/*  12:    */   private int arity;
/*  13:    */   
/*  14:    */   public TournamentSelection(int arity)
/*  15:    */   {
/*  16: 45 */     this.arity = arity;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public ChromosomePair select(Population population)
/*  20:    */   {
/*  21: 58 */     return new ChromosomePair(tournament((ListPopulation)population), tournament((ListPopulation)population));
/*  22:    */   }
/*  23:    */   
/*  24:    */   private Chromosome tournament(ListPopulation population)
/*  25:    */   {
/*  26: 73 */     if (population.getPopulationSize() < this.arity) {
/*  27: 74 */       throw new MathIllegalArgumentException(LocalizedFormats.TOO_LARGE_TOURNAMENT_ARITY, new Object[] { Integer.valueOf(this.arity), Integer.valueOf(population.getPopulationSize()) });
/*  28:    */     }
/*  29: 78 */     ListPopulation tournamentPopulation = new ListPopulation(this.arity)
/*  30:    */     {
/*  31:    */       public Population nextGeneration()
/*  32:    */       {
/*  33: 81 */         return null;
/*  34:    */       }
/*  35: 85 */     };
/*  36: 86 */     List<Chromosome> chromosomes = new ArrayList(population.getChromosomes());
/*  37: 87 */     for (int i = 0; i < this.arity; i++)
/*  38:    */     {
/*  39: 89 */       int rind = GeneticAlgorithm.getRandomGenerator().nextInt(chromosomes.size());
/*  40: 90 */       tournamentPopulation.addChromosome((Chromosome)chromosomes.get(rind));
/*  41:    */       
/*  42: 92 */       chromosomes.remove(rind);
/*  43:    */     }
/*  44: 95 */     return tournamentPopulation.getFittestChromosome();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int getArity()
/*  48:    */   {
/*  49:104 */     return this.arity;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setArity(int arity)
/*  53:    */   {
/*  54:113 */     this.arity = arity;
/*  55:    */   }
/*  56:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.TournamentSelection
 * JD-Core Version:    0.7.0.1
 */
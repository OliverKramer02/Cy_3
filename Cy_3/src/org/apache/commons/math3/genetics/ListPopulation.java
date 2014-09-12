/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.commons.math3.exception.NotPositiveException;
/*   7:    */ import org.apache.commons.math3.exception.NumberIsTooLargeException;
/*   8:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   9:    */ 
/*  10:    */ public abstract class ListPopulation
/*  11:    */   implements Population
/*  12:    */ {
/*  13:    */   private List<Chromosome> chromosomes;
/*  14:    */   private int populationLimit;
/*  15:    */   
/*  16:    */   public ListPopulation(List<Chromosome> chromosomes, int populationLimit)
/*  17:    */   {
/*  18: 51 */     if (chromosomes.size() > populationLimit) {
/*  19: 52 */       throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, Integer.valueOf(chromosomes.size()), Integer.valueOf(populationLimit), false);
/*  20:    */     }
/*  21: 55 */     if (populationLimit <= 0) {
/*  22: 56 */       throw new NotPositiveException(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, Integer.valueOf(populationLimit));
/*  23:    */     }
/*  24: 59 */     this.chromosomes = chromosomes;
/*  25: 60 */     this.populationLimit = populationLimit;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ListPopulation(int populationLimit)
/*  29:    */   {
/*  30: 70 */     if (populationLimit <= 0) {
/*  31: 71 */       throw new NotPositiveException(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, Integer.valueOf(populationLimit));
/*  32:    */     }
/*  33: 73 */     this.populationLimit = populationLimit;
/*  34: 74 */     this.chromosomes = new ArrayList(populationLimit);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setChromosomes(List<Chromosome> chromosomes)
/*  38:    */   {
/*  39: 82 */     this.chromosomes = chromosomes;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public List<Chromosome> getChromosomes()
/*  43:    */   {
/*  44: 90 */     return this.chromosomes;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void addChromosome(Chromosome chromosome)
/*  48:    */   {
/*  49: 98 */     this.chromosomes.add(chromosome);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Chromosome getFittestChromosome()
/*  53:    */   {
/*  54:107 */     Chromosome bestChromosome = (Chromosome)this.chromosomes.get(0);
/*  55:108 */     for (Chromosome chromosome : this.chromosomes) {
/*  56:109 */       if (chromosome.compareTo(bestChromosome) > 0) {
/*  57:111 */         bestChromosome = chromosome;
/*  58:    */       }
/*  59:    */     }
/*  60:114 */     return bestChromosome;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getPopulationLimit()
/*  64:    */   {
/*  65:122 */     return this.populationLimit;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setPopulationLimit(int populationLimit)
/*  69:    */   {
/*  70:130 */     this.populationLimit = populationLimit;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getPopulationSize()
/*  74:    */   {
/*  75:138 */     return this.chromosomes.size();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public String toString()
/*  79:    */   {
/*  80:146 */     return this.chromosomes.toString();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Iterator<Chromosome> iterator()
/*  84:    */   {
/*  85:155 */     return this.chromosomes.iterator();
/*  86:    */   }
/*  87:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.ListPopulation
 * JD-Core Version:    0.7.0.1
 */
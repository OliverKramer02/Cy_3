/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   6:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   7:    */ import org.apache.commons.math3.util.FastMath;
/*   8:    */ 
/*   9:    */ public class ElitisticListPopulation
/*  10:    */   extends ListPopulation
/*  11:    */ {
/*  12: 36 */   private double elitismRate = 0.9D;
/*  13:    */   
/*  14:    */   public ElitisticListPopulation(List<Chromosome> chromosomes, int populationLimit, double elitismRate)
/*  15:    */   {
/*  16: 49 */     super(chromosomes, populationLimit);
/*  17: 50 */     this.elitismRate = elitismRate;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ElitisticListPopulation(int populationLimit, double elitismRate)
/*  21:    */   {
/*  22: 62 */     super(populationLimit);
/*  23: 63 */     this.elitismRate = elitismRate;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Population nextGeneration()
/*  27:    */   {
/*  28: 74 */     ElitisticListPopulation nextGeneration = new ElitisticListPopulation(getPopulationLimit(), getElitismRate());
/*  29:    */     
/*  30: 76 */     List<Chromosome> oldChromosomes = getChromosomes();
/*  31: 77 */     Collections.sort(oldChromosomes);
/*  32:    */     
/*  33:    */ 
/*  34: 80 */     int boundIndex = (int)FastMath.ceil((1.0D - getElitismRate()) * oldChromosomes.size());
/*  35: 81 */     for (int i = boundIndex; i < oldChromosomes.size(); i++) {
/*  36: 82 */       nextGeneration.addChromosome((Chromosome)oldChromosomes.get(i));
/*  37:    */     }
/*  38: 84 */     return nextGeneration;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setElitismRate(double elitismRate)
/*  42:    */   {
/*  43: 96 */     if ((elitismRate < 0.0D) || (elitismRate > 1.0D)) {
/*  44: 97 */       throw new OutOfRangeException(LocalizedFormats.ELITISM_RATE, Double.valueOf(elitismRate), Integer.valueOf(0), Integer.valueOf(1));
/*  45:    */     }
/*  46: 99 */     this.elitismRate = elitismRate;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public double getElitismRate()
/*  50:    */   {
/*  51:107 */     return this.elitismRate;
/*  52:    */   }
/*  53:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.ElitisticListPopulation
 * JD-Core Version:    0.7.0.1
 */
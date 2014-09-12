/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.OutOfRangeException;
/*   4:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*   5:    */ import org.apache.commons.math3.random.JDKRandomGenerator;
/*   6:    */ import org.apache.commons.math3.random.RandomGenerator;
/*   7:    */ 
/*   8:    */ public class GeneticAlgorithm
/*   9:    */ {
/*  10: 40 */   private static RandomGenerator randomGenerator = new JDKRandomGenerator();
/*  11:    */   private final CrossoverPolicy crossoverPolicy;
/*  12:    */   private final double crossoverRate;
/*  13:    */   private final MutationPolicy mutationPolicy;
/*  14:    */   private final double mutationRate;
/*  15:    */   private final SelectionPolicy selectionPolicy;
/*  16: 58 */   private int generationsEvolved = 0;
/*  17:    */   
/*  18:    */   public GeneticAlgorithm(CrossoverPolicy crossoverPolicy, double crossoverRate, MutationPolicy mutationPolicy, double mutationRate, SelectionPolicy selectionPolicy)
/*  19:    */   {
/*  20: 74 */     if ((crossoverRate < 0.0D) || (crossoverRate > 1.0D)) {
/*  21: 75 */       throw new OutOfRangeException(LocalizedFormats.CROSSOVER_RATE, Double.valueOf(crossoverRate), Integer.valueOf(0), Integer.valueOf(1));
/*  22:    */     }
/*  23: 78 */     if ((mutationRate < 0.0D) || (mutationRate > 1.0D)) {
/*  24: 79 */       throw new OutOfRangeException(LocalizedFormats.MUTATION_RATE, Double.valueOf(mutationRate), Integer.valueOf(0), Integer.valueOf(1));
/*  25:    */     }
/*  26: 82 */     this.crossoverPolicy = crossoverPolicy;
/*  27: 83 */     this.crossoverRate = crossoverRate;
/*  28: 84 */     this.mutationPolicy = mutationPolicy;
/*  29: 85 */     this.mutationRate = mutationRate;
/*  30: 86 */     this.selectionPolicy = selectionPolicy;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static synchronized void setRandomGenerator(RandomGenerator random)
/*  34:    */   {
/*  35: 95 */     randomGenerator = random;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static synchronized RandomGenerator getRandomGenerator()
/*  39:    */   {
/*  40:104 */     return randomGenerator;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Population evolve(Population initial, StoppingCondition condition)
/*  44:    */   {
/*  45:118 */     Population current = initial;
/*  46:119 */     this.generationsEvolved = 0;
/*  47:120 */     while (!condition.isSatisfied(current))
/*  48:    */     {
/*  49:121 */       current = nextGeneration(current);
/*  50:122 */       this.generationsEvolved += 1;
/*  51:    */     }
/*  52:124 */     return current;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Population nextGeneration(Population current)
/*  56:    */   {
/*  57:150 */     Population nextGeneration = current.nextGeneration();
/*  58:    */     
/*  59:152 */     RandomGenerator randGen = getRandomGenerator();
/*  60:154 */     while (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit())
/*  61:    */     {
/*  62:156 */       ChromosomePair pair = getSelectionPolicy().select(current);
/*  63:159 */       if (randGen.nextDouble() < getCrossoverRate()) {
/*  64:161 */         pair = getCrossoverPolicy().crossover(pair.getFirst(), pair.getSecond());
/*  65:    */       }
/*  66:165 */       if (randGen.nextDouble() < getMutationRate()) {
/*  67:167 */         pair = new ChromosomePair(getMutationPolicy().mutate(pair.getFirst()), getMutationPolicy().mutate(pair.getSecond()));
/*  68:    */       }
/*  69:173 */       nextGeneration.addChromosome(pair.getFirst());
/*  70:175 */       if (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
/*  71:177 */         nextGeneration.addChromosome(pair.getSecond());
/*  72:    */       }
/*  73:    */     }
/*  74:181 */     return nextGeneration;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public CrossoverPolicy getCrossoverPolicy()
/*  78:    */   {
/*  79:189 */     return this.crossoverPolicy;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public double getCrossoverRate()
/*  83:    */   {
/*  84:197 */     return this.crossoverRate;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public MutationPolicy getMutationPolicy()
/*  88:    */   {
/*  89:205 */     return this.mutationPolicy;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public double getMutationRate()
/*  93:    */   {
/*  94:213 */     return this.mutationRate;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public SelectionPolicy getSelectionPolicy()
/*  98:    */   {
/*  99:221 */     return this.selectionPolicy;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public int getGenerationsEvolved()
/* 103:    */   {
/* 104:232 */     return this.generationsEvolved;
/* 105:    */   }
/* 106:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.GeneticAlgorithm
 * JD-Core Version:    0.7.0.1
 */
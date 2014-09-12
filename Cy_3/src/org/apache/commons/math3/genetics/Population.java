package org.apache.commons.math3.genetics;

public abstract interface Population
  extends Iterable<Chromosome>
{
  public abstract int getPopulationSize();
  
  public abstract int getPopulationLimit();
  
  public abstract Population nextGeneration();
  
  public abstract void addChromosome(Chromosome paramChromosome);
  
  public abstract Chromosome getFittestChromosome();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.Population
 * JD-Core Version:    0.7.0.1
 */
package org.apache.commons.math3.genetics;

public abstract interface CrossoverPolicy
{
  public abstract ChromosomePair crossover(Chromosome paramChromosome1, Chromosome paramChromosome2);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.CrossoverPolicy
 * JD-Core Version:    0.7.0.1
 */
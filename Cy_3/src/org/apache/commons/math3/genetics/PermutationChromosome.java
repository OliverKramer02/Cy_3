package org.apache.commons.math3.genetics;

import java.util.List;

public abstract interface PermutationChromosome<T>
{
  public abstract List<T> decode(List<T> paramList);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.PermutationChromosome
 * JD-Core Version:    0.7.0.1
 */
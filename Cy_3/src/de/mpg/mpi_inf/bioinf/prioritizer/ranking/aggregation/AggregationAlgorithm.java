package de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation;

import de.mpg.mpi_inf.bioinf.prioritizer.ranking.Ranking;
import java.util.Map;

public abstract interface AggregationAlgorithm
{
  public abstract Ranking aggAll(Map<Ranking, Double> paramMap, String paramString);
  
  public abstract String getName();
  
  public abstract boolean isWeighted();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.AggregationAlgorithm
 * JD-Core Version:    0.7.0.1
 */
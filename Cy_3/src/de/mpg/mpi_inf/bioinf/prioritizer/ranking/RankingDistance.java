package de.mpg.mpi_inf.bioinf.prioritizer.ranking;

public abstract interface RankingDistance
{
  public abstract double getDistance(Ranking paramRanking1, Ranking paramRanking2);
  
  public abstract String getName();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     de.mpg.mpi_inf.bioinf.prioritizer.ranking.RankingDistance
 * JD-Core Version:    0.7.0.1
 */
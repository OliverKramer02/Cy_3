package org.apache.commons.math3.random;

public abstract interface RandomGenerator
{
  public abstract void setSeed(int paramInt);
  
  public abstract void setSeed(int[] paramArrayOfInt);
  
  public abstract void setSeed(long paramLong);
  
  public abstract void nextBytes(byte[] paramArrayOfByte);
  
  public abstract int nextInt();
  
  public abstract int nextInt(int paramInt);
  
  public abstract long nextLong();
  
  public abstract boolean nextBoolean();
  
  public abstract float nextFloat();
  
  public abstract double nextDouble();
  
  public abstract double nextGaussian();
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.random.RandomGenerator
 * JD-Core Version:    0.7.0.1
 */
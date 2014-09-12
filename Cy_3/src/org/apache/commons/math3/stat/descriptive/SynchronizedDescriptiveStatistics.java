/*   1:    */ package org.apache.commons.math3.stat.descriptive;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.NullArgumentException;
/*   4:    */ import org.apache.commons.math3.util.MathUtils;
/*   5:    */ 
/*   6:    */ public class SynchronizedDescriptiveStatistics
/*   7:    */   extends DescriptiveStatistics
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 1L;
/*  10:    */   
/*  11:    */   public SynchronizedDescriptiveStatistics()
/*  12:    */   {
/*  13: 44 */     this(-1);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public SynchronizedDescriptiveStatistics(int window)
/*  17:    */   {
/*  18: 52 */     super(window);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public SynchronizedDescriptiveStatistics(SynchronizedDescriptiveStatistics original)
/*  22:    */   {
/*  23: 61 */     copy(original, this);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public synchronized void addValue(double v)
/*  27:    */   {
/*  28: 69 */     super.addValue(v);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public synchronized double apply(UnivariateStatistic stat)
/*  32:    */   {
/*  33: 77 */     return super.apply(stat);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public synchronized void clear()
/*  37:    */   {
/*  38: 85 */     super.clear();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public synchronized double getElement(int index)
/*  42:    */   {
/*  43: 93 */     return super.getElement(index);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public synchronized long getN()
/*  47:    */   {
/*  48:101 */     return super.getN();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public synchronized double getStandardDeviation()
/*  52:    */   {
/*  53:109 */     return super.getStandardDeviation();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public synchronized double[] getValues()
/*  57:    */   {
/*  58:117 */     return super.getValues();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public synchronized int getWindowSize()
/*  62:    */   {
/*  63:125 */     return super.getWindowSize();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public synchronized void setWindowSize(int windowSize)
/*  67:    */   {
/*  68:133 */     super.setWindowSize(windowSize);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public synchronized String toString()
/*  72:    */   {
/*  73:141 */     return super.toString();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public synchronized SynchronizedDescriptiveStatistics copy()
/*  77:    */   {
/*  78:152 */     SynchronizedDescriptiveStatistics result = new SynchronizedDescriptiveStatistics();
/*  79:    */     
/*  80:154 */     copy(this, result);
/*  81:155 */     return result;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static void copy(SynchronizedDescriptiveStatistics source, SynchronizedDescriptiveStatistics dest)
/*  85:    */     throws NullArgumentException
/*  86:    */   {
/*  87:170 */     MathUtils.checkNotNull(source);
/*  88:171 */     MathUtils.checkNotNull(dest);
/*  89:172 */     synchronized (source)
/*  90:    */     {
/*  91:173 */       synchronized (dest)
/*  92:    */       {
/*  93:174 */         DescriptiveStatistics.copy(source, dest);
/*  94:    */       }
/*  95:    */     }
/*  96:    */   }
/*  97:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics
 * JD-Core Version:    0.7.0.1
 */
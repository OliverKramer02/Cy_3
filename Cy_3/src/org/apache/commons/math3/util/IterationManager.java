/*   1:    */ package org.apache.commons.math3.util;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.concurrent.CopyOnWriteArrayList;
/*   5:    */ import org.apache.commons.math3.exception.MaxCountExceededException;
/*   6:    */ 
/*   7:    */ public class IterationManager
/*   8:    */ {
/*   9:    */   private final Incrementor iterations;
/*  10:    */   private final Collection<IterationListener> listeners;
/*  11:    */   
/*  12:    */   public IterationManager(int maxIterations)
/*  13:    */   {
/*  14: 46 */     this.iterations = new Incrementor();
/*  15: 47 */     this.iterations.setMaximalCount(maxIterations);
/*  16: 48 */     this.listeners = new CopyOnWriteArrayList();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void addIterationListener(IterationListener listener)
/*  20:    */   {
/*  21: 57 */     this.listeners.add(listener);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void fireInitializationEvent(IterationEvent e)
/*  25:    */   {
/*  26: 67 */     for (IterationListener l : this.listeners) {
/*  27: 68 */       l.initializationPerformed(e);
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void fireIterationPerformedEvent(IterationEvent e)
/*  32:    */   {
/*  33: 79 */     for (IterationListener l : this.listeners) {
/*  34: 80 */       l.iterationPerformed(e);
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void fireIterationStartedEvent(IterationEvent e)
/*  39:    */   {
/*  40: 91 */     for (IterationListener l : this.listeners) {
/*  41: 92 */       l.iterationStarted(e);
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void fireTerminationEvent(IterationEvent e)
/*  46:    */   {
/*  47:103 */     for (IterationListener l : this.listeners) {
/*  48:104 */       l.terminationPerformed(e);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getIterations()
/*  53:    */   {
/*  54:115 */     return this.iterations.getCount();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getMaxIterations()
/*  58:    */   {
/*  59:124 */     return this.iterations.getMaximalCount();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void incrementIterationCount()
/*  63:    */     throws MaxCountExceededException
/*  64:    */   {
/*  65:137 */     this.iterations.incrementCount();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void removeIterationListener(IterationListener listener)
/*  69:    */   {
/*  70:149 */     this.listeners.remove(listener);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void resetIterationCount()
/*  74:    */   {
/*  75:157 */     this.iterations.resetCount();
/*  76:    */   }
/*  77:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.util.IterationManager
 * JD-Core Version:    0.7.0.1
 */
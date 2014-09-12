/*   1:    */ package org.apache.commons.math3.exception;
/*   2:    */ 
/*   3:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
/*   4:    */ import org.apache.commons.math3.util.MathArrays.OrderDirection;
/*   5:    */ 
/*   6:    */ public class NonMonotonicSequenceException
/*   7:    */   extends MathIllegalNumberException
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 3596849179428944575L;
/*  10:    */   private final MathArrays.OrderDirection direction;
/*  11:    */   private final boolean strict;
/*  12:    */   private final int index;
/*  13:    */   private final Number previous;
/*  14:    */   
/*  15:    */   public NonMonotonicSequenceException(Number wrong, Number previous, int index)
/*  16:    */   {
/*  17: 61 */     this(wrong, previous, index, MathArrays.OrderDirection.INCREASING, true);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public NonMonotonicSequenceException(Number wrong, Number previous, int index, MathArrays.OrderDirection direction, boolean strict)
/*  21:    */   {
/*  22: 80 */     super(strict ? LocalizedFormats.NOT_STRICTLY_DECREASING_SEQUENCE : direction == MathArrays.OrderDirection.INCREASING ? LocalizedFormats.NOT_INCREASING_SEQUENCE : strict ? LocalizedFormats.NOT_STRICTLY_INCREASING_SEQUENCE : LocalizedFormats.NOT_DECREASING_SEQUENCE, wrong, new Object[] { previous, Integer.valueOf(index), Integer.valueOf(index - 1) });
/*  23:    */     
/*  24:    */ 
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31: 89 */     this.direction = direction;
/*  32: 90 */     this.strict = strict;
/*  33: 91 */     this.index = index;
/*  34: 92 */     this.previous = previous;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public MathArrays.OrderDirection getDirection()
/*  38:    */   {
/*  39: 99 */     return this.direction;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean getStrict()
/*  43:    */   {
/*  44:105 */     return this.strict;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int getIndex()
/*  48:    */   {
/*  49:113 */     return this.index;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Number getPrevious()
/*  53:    */   {
/*  54:119 */     return this.previous;
/*  55:    */   }
/*  56:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.NonMonotonicSequenceException
 * JD-Core Version:    0.7.0.1
 */
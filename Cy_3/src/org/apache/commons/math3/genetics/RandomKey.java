/*   1:    */ package org.apache.commons.math3.genetics;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Comparator;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*  10:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ import org.apache.commons.math3.random.RandomGenerator;
/*  13:    */ 
/*  14:    */ public abstract class RandomKey<T>
/*  15:    */   extends AbstractListChromosome<Double>
/*  16:    */   implements PermutationChromosome<T>
/*  17:    */ {
/*  18:    */   private final List<Double> sortedRepresentation;
/*  19:    */   private final List<Integer> baseSeqPermutation;
/*  20:    */   
/*  21:    */   public RandomKey(List<Double> representation)
/*  22:    */   {
/*  23: 86 */     super(representation);
/*  24:    */     
/*  25: 88 */     List<Double> sortedRepr = new ArrayList(getRepresentation());
/*  26: 89 */     Collections.sort(sortedRepr);
/*  27: 90 */     this.sortedRepresentation = Collections.unmodifiableList(sortedRepr);
/*  28:    */     
/*  29: 92 */     this.baseSeqPermutation = Collections.unmodifiableList(decodeGeneric(baseSequence(getLength()), getRepresentation(), this.sortedRepresentation));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public RandomKey(Double[] representation)
/*  33:    */   {
/*  34:103 */     this(Arrays.asList(representation));
/*  35:    */   }
/*  36:    */   
/*  37:    */   public List<T> decode(List<T> sequence)
/*  38:    */   {
/*  39:110 */     return decodeGeneric(sequence, getRepresentation(), this.sortedRepresentation);
/*  40:    */   }
/*  41:    */   
/*  42:    */   private static <S> List<S> decodeGeneric(List<S> sequence, List<Double> representation, List<Double> sortedRepr)
/*  43:    */   {
/*  44:127 */     int l = sequence.size();
/*  45:130 */     if (representation.size() != l) {
/*  46:131 */       throw new DimensionMismatchException(representation.size(), l);
/*  47:    */     }
/*  48:133 */     if (sortedRepr.size() != l) {
/*  49:134 */       throw new DimensionMismatchException(sortedRepr.size(), l);
/*  50:    */     }
/*  51:138 */     List<Double> reprCopy = new ArrayList(representation);
/*  52:    */     
/*  53:    */ 
/*  54:141 */     List<S> res = new ArrayList(l);
/*  55:142 */     for (int i = 0; i < l; i++)
/*  56:    */     {
/*  57:143 */       int index = reprCopy.indexOf(sortedRepr.get(i));
/*  58:144 */       res.add(sequence.get(index));
/*  59:145 */       reprCopy.set(index, null);
/*  60:    */     }
/*  61:147 */     return res;
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected boolean isSame(Chromosome another)
/*  65:    */   {
/*  66:160 */     if (!(another instanceof RandomKey)) {
/*  67:161 */       return false;
/*  68:    */     }
/*  69:163 */     RandomKey<?> anotherRk = (RandomKey)another;
/*  70:165 */     if (getLength() != anotherRk.getLength()) {
/*  71:166 */       return false;
/*  72:    */     }
/*  73:171 */     List<Integer> thisPerm = this.baseSeqPermutation;
/*  74:172 */     List<Integer> anotherPerm = anotherRk.baseSeqPermutation;
/*  75:174 */     for (int i = 0; i < getLength(); i++) {
/*  76:175 */       if (thisPerm.get(i) != anotherPerm.get(i)) {
/*  77:176 */         return false;
/*  78:    */       }
/*  79:    */     }
/*  80:180 */     return true;
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected void checkValidity(List<Double> chromosomeRepresentation)
/*  84:    */     throws InvalidRepresentationException
/*  85:    */   {
/*  86:190 */     for (Iterator i$ = chromosomeRepresentation.iterator(); i$.hasNext();)
/*  87:    */     {
/*  88:190 */       double val = ((Double)i$.next()).doubleValue();
/*  89:191 */       if ((val < 0.0D) || (val > 1.0D)) {
/*  90:192 */         throw new InvalidRepresentationException(LocalizedFormats.OUT_OF_RANGE_SIMPLE, new Object[] { Double.valueOf(val), Integer.valueOf(0), Integer.valueOf(1) });
/*  91:    */       }
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static final List<Double> randomPermutation(int l)
/*  96:    */   {
/*  97:207 */     List<Double> repr = new ArrayList(l);
/*  98:208 */     for (int i = 0; i < l; i++) {
/*  99:209 */       repr.add(Double.valueOf(GeneticAlgorithm.getRandomGenerator().nextDouble()));
/* 100:    */     }
/* 101:211 */     return repr;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static final List<Double> identityPermutation(int l)
/* 105:    */   {
/* 106:222 */     List<Double> repr = new ArrayList(l);
/* 107:223 */     for (int i = 0; i < l; i++) {
/* 108:224 */       repr.add(Double.valueOf(i / l));
/* 109:    */     }
/* 110:226 */     return repr;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static <S> List<Double> comparatorPermutation(List<S> data, Comparator<S> comparator)
/* 114:    */   {
/* 115:244 */     List<S> sortedData = new ArrayList(data);
/* 116:245 */     Collections.sort(sortedData, comparator);
/* 117:    */     
/* 118:247 */     return inducedPermutation(data, sortedData);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static <S> List<Double> inducedPermutation(List<S> originalData, List<S> permutedData)
/* 122:    */   {
/* 123:270 */     if (originalData.size() != permutedData.size()) {
/* 124:271 */       throw new DimensionMismatchException(permutedData.size(), originalData.size());
/* 125:    */     }
/* 126:273 */     int l = originalData.size();
/* 127:    */     
/* 128:275 */     List<S> origDataCopy = new ArrayList(originalData);
/* 129:    */     
/* 130:277 */     Double[] res = new Double[l];
/* 131:278 */     for (int i = 0; i < l; i++)
/* 132:    */     {
/* 133:279 */       int index = origDataCopy.indexOf(permutedData.get(i));
/* 134:280 */       if (index == -1) {
/* 135:281 */         throw new MathIllegalArgumentException(LocalizedFormats.DIFFERENT_ORIG_AND_PERMUTED_DATA, new Object[0]);
/* 136:    */       }
/* 137:283 */       res[index] = Double.valueOf(i / l);
/* 138:284 */       origDataCopy.set(index, null);
/* 139:    */     }
/* 140:286 */     return Arrays.asList(res);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public String toString()
/* 144:    */   {
/* 145:294 */     return String.format("(f=%s pi=(%s))", new Object[] { Double.valueOf(getFitness()), this.baseSeqPermutation });
/* 146:    */   }
/* 147:    */   
/* 148:    */   private static List<Integer> baseSequence(int l)
/* 149:    */   {
/* 150:304 */     List<Integer> baseSequence = new ArrayList(l);
/* 151:305 */     for (int i = 0; i < l; i++) {
/* 152:306 */       baseSequence.add(Integer.valueOf(i));
/* 153:    */     }
/* 154:308 */     return baseSequence;
/* 155:    */   }
/* 156:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.genetics.RandomKey
 * JD-Core Version:    0.7.0.1
 */
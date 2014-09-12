/*   1:    */ package org.apache.commons.math3.ode;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.math3.exception.DimensionMismatchException;
/*   6:    */ 
/*   7:    */ public class ExpandableStatefulODE
/*   8:    */ {
/*   9:    */   private final FirstOrderDifferentialEquations primary;
/*  10:    */   private final EquationsMapper primaryMapper;
/*  11:    */   private double time;
/*  12:    */   private final double[] primaryState;
/*  13:    */   private final double[] primaryStateDot;
/*  14:    */   private List<SecondaryComponent> components;
/*  15:    */   
/*  16:    */   public ExpandableStatefulODE(FirstOrderDifferentialEquations primary)
/*  17:    */   {
/*  18: 73 */     int n = primary.getDimension();
/*  19: 74 */     this.primary = primary;
/*  20: 75 */     this.primaryMapper = new EquationsMapper(0, n);
/*  21: 76 */     this.time = (0.0D / 0.0D);
/*  22: 77 */     this.primaryState = new double[n];
/*  23: 78 */     this.primaryStateDot = new double[n];
/*  24: 79 */     this.components = new ArrayList();
/*  25:    */   }
/*  26:    */   
/*  27:    */   public FirstOrderDifferentialEquations getPrimary()
/*  28:    */   {
/*  29: 86 */     return this.primary;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getTotalDimension()
/*  33:    */   {
/*  34: 96 */     if (this.components.isEmpty()) {
/*  35: 98 */       return this.primaryMapper.getDimension();
/*  36:    */     }
/*  37:101 */     EquationsMapper lastMapper = ((SecondaryComponent)this.components.get(this.components.size() - 1)).mapper;
/*  38:102 */     return lastMapper.getFirstIndex() + lastMapper.getDimension();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void computeDerivatives(double t, double[] y, double[] yDot)
/*  42:    */   {
/*  43:114 */     this.primaryMapper.extractEquationData(y, this.primaryState);
/*  44:115 */     this.primary.computeDerivatives(t, this.primaryState, this.primaryStateDot);
/*  45:116 */     this.primaryMapper.insertEquationData(this.primaryStateDot, yDot);
/*  46:119 */     for (SecondaryComponent component : this.components)
/*  47:    */     {
/*  48:120 */       component.mapper.extractEquationData(y, component.state);
/*  49:121 */       component.equation.computeDerivatives(t, this.primaryState, this.primaryStateDot, component.state, component.stateDot);
/*  50:    */       
/*  51:123 */       component.mapper.insertEquationData(component.stateDot, yDot);
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int addSecondaryEquations(SecondaryEquations secondary)
/*  56:    */   {
/*  57:    */     int firstIndex;
/*  58:    */     
/*  59:135 */     if (this.components.isEmpty())
/*  60:    */     {
/*  61:137 */       this.components = new ArrayList();
/*  62:138 */       firstIndex = this.primary.getDimension();
/*  63:    */     }
/*  64:    */     else
/*  65:    */     {
/*  66:140 */       SecondaryComponent last = (SecondaryComponent)this.components.get(this.components.size() - 1);
/*  67:141 */       firstIndex = last.mapper.getFirstIndex() + last.mapper.getDimension();
/*  68:    */     }
/*  69:144 */     this.components.add(new SecondaryComponent(secondary, firstIndex));
/*  70:    */     
/*  71:146 */     return this.components.size() - 1;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public EquationsMapper getPrimaryMapper()
/*  75:    */   {
/*  76:155 */     return this.primaryMapper;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public EquationsMapper[] getSecondaryMappers()
/*  80:    */   {
/*  81:163 */     EquationsMapper[] mappers = new EquationsMapper[this.components.size()];
/*  82:164 */     for (int i = 0; i < mappers.length; i++) {
/*  83:165 */       mappers[i] = ((SecondaryComponent)this.components.get(i)).mapper;
/*  84:    */     }
/*  85:167 */     return mappers;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setTime(double time)
/*  89:    */   {
/*  90:174 */     this.time = time;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public double getTime()
/*  94:    */   {
/*  95:181 */     return this.time;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setPrimaryState(double[] primaryState)
/*  99:    */     throws DimensionMismatchException
/* 100:    */   {
/* 101:192 */     if (primaryState.length != this.primaryState.length) {
/* 102:193 */       throw new DimensionMismatchException(primaryState.length, this.primaryState.length);
/* 103:    */     }
/* 104:197 */     System.arraycopy(primaryState, 0, this.primaryState, 0, primaryState.length);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public double[] getPrimaryState()
/* 108:    */   {
/* 109:205 */     return (double[])this.primaryState.clone();
/* 110:    */   }
/* 111:    */   
/* 112:    */   public double[] getPrimaryStateDot()
/* 113:    */   {
/* 114:212 */     return (double[])this.primaryStateDot.clone();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setSecondaryState(int index, double[] secondaryState)
/* 118:    */     throws DimensionMismatchException
/* 119:    */   {
/* 120:226 */     double[] localArray = ((SecondaryComponent)this.components.get(index)).state;
/* 121:229 */     if (secondaryState.length != localArray.length) {
/* 122:230 */       throw new DimensionMismatchException(secondaryState.length, localArray.length);
/* 123:    */     }
/* 124:234 */     System.arraycopy(secondaryState, 0, localArray, 0, secondaryState.length);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public double[] getSecondaryState(int index)
/* 128:    */   {
/* 129:244 */     return (double[])((SecondaryComponent)this.components.get(index)).state.clone();
/* 130:    */   }
/* 131:    */   
/* 132:    */   public double[] getSecondaryStateDot(int index)
/* 133:    */   {
/* 134:253 */     return (double[])((SecondaryComponent)this.components.get(index)).stateDot.clone();
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void setCompleteState(double[] completeState)
/* 138:    */     throws DimensionMismatchException
/* 139:    */   {
/* 140:265 */     if (completeState.length != getTotalDimension()) {
/* 141:266 */       throw new DimensionMismatchException(completeState.length, getTotalDimension());
/* 142:    */     }
/* 143:270 */     this.primaryMapper.extractEquationData(completeState, this.primaryState);
/* 144:271 */     for (SecondaryComponent component : this.components) {
/* 145:272 */       component.mapper.extractEquationData(completeState, component.state);
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public double[] getCompleteState()
/* 150:    */   {
/* 151:285 */     double[] completeState = new double[getTotalDimension()];
/* 152:    */     
/* 153:    */ 
/* 154:288 */     this.primaryMapper.insertEquationData(this.primaryState, completeState);
/* 155:289 */     for (SecondaryComponent component : this.components) {
/* 156:290 */       component.mapper.insertEquationData(component.state, completeState);
/* 157:    */     }
/* 158:293 */     return completeState;
/* 159:    */   }
/* 160:    */   
/* 161:    */   private static class SecondaryComponent
/* 162:    */   {
/* 163:    */     private final SecondaryEquations equation;
/* 164:    */     private final EquationsMapper mapper;
/* 165:    */     private final double[] state;
/* 166:    */     private final double[] stateDot;
/* 167:    */     
/* 168:    */     public SecondaryComponent(SecondaryEquations equation, int firstIndex)
/* 169:    */     {
/* 170:317 */       int n = equation.getDimension();
/* 171:318 */       this.equation = equation;
/* 172:319 */       this.mapper = new EquationsMapper(firstIndex, n);
/* 173:320 */       this.state = new double[n];
/* 174:321 */       this.stateDot = new double[n];
/* 175:    */     }
/* 176:    */   }
/* 177:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.ode.ExpandableStatefulODE
 * JD-Core Version:    0.7.0.1
 */
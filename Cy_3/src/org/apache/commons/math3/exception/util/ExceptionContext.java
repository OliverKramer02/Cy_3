/*   1:    */ package org.apache.commons.math3.exception.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.ObjectOutputStream;
/*   6:    */ import java.io.Serializable;
/*   7:    */ import java.text.MessageFormat;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Locale;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Set;
/*  14:    */ 
/*  15:    */ public class ExceptionContext
/*  16:    */   implements Serializable
/*  17:    */ {
/*  18:    */   private static final long serialVersionUID = -6024911025449780478L;
/*  19:    */   private Throwable throwable;
/*  20:    */   private List<Localizable> msgPatterns;
/*  21:    */   private List<Object[]> msgArguments;
/*  22:    */   private Map<String, Object> context;
/*  23:    */   
/*  24:    */   public ExceptionContext(Throwable throwable)
/*  25:    */   {
/*  26: 65 */     this.throwable = throwable;
/*  27: 66 */     this.msgPatterns = new ArrayList();
/*  28: 67 */     this.msgArguments = new ArrayList();
/*  29: 68 */     this.context = new HashMap();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Throwable getThrowable()
/*  33:    */   {
/*  34: 75 */     return this.throwable;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void addMessage(Localizable pattern, Object... arguments)
/*  38:    */   {
/*  39: 87 */     this.msgPatterns.add(pattern);
/*  40: 88 */     this.msgArguments.add(ArgUtils.flatten(arguments));
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setValue(String key, Object value)
/*  44:    */   {
/*  45:100 */     this.context.put(key, value);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Object getValue(String key)
/*  49:    */   {
/*  50:110 */     return this.context.get(key);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Set<String> getKeys()
/*  54:    */   {
/*  55:119 */     return this.context.keySet();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getMessage()
/*  59:    */   {
/*  60:128 */     return getMessage(Locale.US);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getLocalizedMessage()
/*  64:    */   {
/*  65:137 */     return getMessage(Locale.getDefault());
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getMessage(Locale locale)
/*  69:    */   {
/*  70:147 */     return buildMessage(locale, ": ");
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String getMessage(Locale locale, String separator)
/*  74:    */   {
/*  75:159 */     return buildMessage(locale, separator);
/*  76:    */   }
/*  77:    */   
/*  78:    */   private String buildMessage(Locale locale, String separator)
/*  79:    */   {
/*  80:171 */     StringBuilder sb = new StringBuilder();
/*  81:172 */     int count = 0;
/*  82:173 */     int len = this.msgPatterns.size();
/*  83:174 */     for (int i = 0; i < len; i++)
/*  84:    */     {
/*  85:175 */       Localizable pat = (Localizable)this.msgPatterns.get(i);
/*  86:176 */       Object[] args = (Object[])this.msgArguments.get(i);
/*  87:177 */       MessageFormat fmt = new MessageFormat(pat.getLocalizedString(locale), locale);
/*  88:    */       
/*  89:179 */       sb.append(fmt.format(args));
/*  90:180 */       count++;
/*  91:180 */       if (count < len) {
/*  92:182 */         sb.append(separator);
/*  93:    */       }
/*  94:    */     }
/*  95:186 */     return sb.toString();
/*  96:    */   }
/*  97:    */   
/*  98:    */   private void writeObject(ObjectOutputStream out)
/*  99:    */     throws IOException
/* 100:    */   {
/* 101:197 */     out.writeObject(this.throwable);
/* 102:198 */     serializeMessages(out);
/* 103:199 */     serializeContext(out);
/* 104:    */   }
/* 105:    */   
/* 106:    */   private void readObject(ObjectInputStream in)
/* 107:    */     throws IOException, ClassNotFoundException
/* 108:    */   {
/* 109:211 */     this.throwable = ((Throwable)in.readObject());
/* 110:212 */     deSerializeMessages(in);
/* 111:213 */     deSerializeContext(in);
/* 112:    */   }
/* 113:    */   
/* 114:    */   private void serializeMessages(ObjectOutputStream out)
/* 115:    */     throws IOException
/* 116:    */   {
/* 117:225 */     int len = this.msgPatterns.size();
/* 118:226 */     out.writeInt(len);
/* 119:228 */     for (int i = 0; i < len; i++)
/* 120:    */     {
/* 121:229 */       Localizable pat = (Localizable)this.msgPatterns.get(i);
/* 122:    */       
/* 123:231 */       out.writeObject(pat);
/* 124:232 */       Object[] args = (Object[])this.msgArguments.get(i);
/* 125:233 */       int aLen = args.length;
/* 126:    */       
/* 127:235 */       out.writeInt(aLen);
/* 128:236 */       for (int j = 0; j < aLen; j++) {
/* 129:237 */         if ((args[j] instanceof Serializable)) {
/* 130:239 */           out.writeObject(args[j]);
/* 131:    */         } else {
/* 132:242 */           out.writeObject(nonSerializableReplacement(args[j]));
/* 133:    */         }
/* 134:    */       }
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   private void deSerializeMessages(ObjectInputStream in)
/* 139:    */     throws IOException, ClassNotFoundException
/* 140:    */   {
/* 141:259 */     int len = in.readInt();
/* 142:260 */     this.msgPatterns = new ArrayList(len);
/* 143:261 */     this.msgArguments = new ArrayList(len);
/* 144:263 */     for (int i = 0; i < len; i++)
/* 145:    */     {
/* 146:265 */       Localizable pat = (Localizable)in.readObject();
/* 147:266 */       this.msgPatterns.add(pat);
/* 148:    */       
/* 149:268 */       int aLen = in.readInt();
/* 150:269 */       Object[] args = new Object[aLen];
/* 151:270 */       for (int j = 0; j < aLen; j++) {
/* 152:272 */         args[j] = in.readObject();
/* 153:    */       }
/* 154:274 */       this.msgArguments.add(args);
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   private void serializeContext(ObjectOutputStream out)
/* 159:    */     throws IOException
/* 160:    */   {
/* 161:287 */     int len = this.context.keySet().size();
/* 162:288 */     out.writeInt(len);
/* 163:289 */     for (String key : this.context.keySet())
/* 164:    */     {
/* 165:291 */       out.writeObject(key);
/* 166:292 */       Object value = this.context.get(key);
/* 167:293 */       if ((value instanceof Serializable)) {
/* 168:295 */         out.writeObject(value);
/* 169:    */       } else {
/* 170:298 */         out.writeObject(nonSerializableReplacement(value));
/* 171:    */       }
/* 172:    */     }
/* 173:    */   }
/* 174:    */   
/* 175:    */   private void deSerializeContext(ObjectInputStream in)
/* 176:    */     throws IOException, ClassNotFoundException
/* 177:    */   {
/* 178:314 */     int len = in.readInt();
/* 179:315 */     this.context = new HashMap();
/* 180:316 */     for (int i = 0; i < len; i++)
/* 181:    */     {
/* 182:318 */       String key = (String)in.readObject();
/* 183:    */       
/* 184:320 */       Object value = in.readObject();
/* 185:321 */       this.context.put(key, value);
/* 186:    */     }
/* 187:    */   }
/* 188:    */   
/* 189:    */   private String nonSerializableReplacement(Object obj)
/* 190:    */   {
/* 191:333 */     return "[Object could not be serialized: " + obj.getClass().getName() + "]";
/* 192:    */   }
/* 193:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.util.ExceptionContext
 * JD-Core Version:    0.7.0.1
 */
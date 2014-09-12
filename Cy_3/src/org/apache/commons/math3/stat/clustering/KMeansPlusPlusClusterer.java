/*   1:    */ package org.apache.commons.math3.stat.clustering;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Random;
/*   8:    */ import org.apache.commons.math3.exception.ConvergenceException;
/*   9:    */ import org.apache.commons.math3.exception.MathIllegalArgumentException;
/*  10:    */ import org.apache.commons.math3.exception.NumberIsTooSmallException;
/*  11:    */ import org.apache.commons.math3.exception.util.LocalizedFormats;
/*  12:    */ import org.apache.commons.math3.stat.descriptive.moment.Variance;
/*  13:    */ import org.apache.commons.math3.util.MathUtils;
/*  14:    */ 
/*  15:    */ public class KMeansPlusPlusClusterer<T extends Clusterable<T>>
/*  16:    */ {
/*  17:    */   private final Random random;
/*  18:    */   private final EmptyClusterStrategy emptyStrategy;
/*  19:    */   
/*  20:    */   public static enum EmptyClusterStrategy
/*  21:    */   {
/*  22: 46 */     LARGEST_VARIANCE,  LARGEST_POINTS_NUMBER,  FARTHEST_POINT,  ERROR;
/*  23:    */     
/*  24:    */     private EmptyClusterStrategy() {}
/*  25:    */   }
/*  26:    */   
/*  27:    */   public KMeansPlusPlusClusterer(Random random)
/*  28:    */   {
/*  29: 73 */     this(random, EmptyClusterStrategy.LARGEST_VARIANCE);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public KMeansPlusPlusClusterer(Random random, EmptyClusterStrategy emptyStrategy)
/*  33:    */   {
/*  34: 83 */     this.random = random;
/*  35: 84 */     this.emptyStrategy = emptyStrategy;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public List<Cluster<T>> cluster(Collection<T> points, int k, int numTrials, int maxIterationsPerTrial)
/*  39:    */     throws MathIllegalArgumentException
/*  40:    */   {
/*  41:104 */     List<Cluster<T>> best = null;
/*  42:105 */     double bestVarianceSum = (1.0D / 0.0D);
/*  43:108 */     for (int i = 0; i < numTrials; i++)
/*  44:    */     {
/*  45:111 */       List<Cluster<T>> clusters = cluster(points, k, maxIterationsPerTrial);
/*  46:    */       
/*  47:    */ 
/*  48:114 */       double varianceSum = 0.0D;
/*  49:115 */       for (Cluster<T> cluster : clusters) {
/*  50:116 */         if (!cluster.getPoints().isEmpty())
/*  51:    */         {
/*  52:119 */           T center = cluster.getCenter();
/*  53:120 */           Variance stat = new Variance();
/*  54:121 */           for (T point : cluster.getPoints()) {
/*  55:122 */             stat.increment(point.distanceFrom(center));
/*  56:    */           }
/*  57:124 */           varianceSum += stat.getResult();
/*  58:    */         }
/*  59:    */       }
/*  60:129 */       if (varianceSum <= bestVarianceSum)
/*  61:    */       {
/*  62:131 */         best = clusters;
/*  63:132 */         bestVarianceSum = varianceSum;
/*  64:    */       }
/*  65:    */     }
/*  66:138 */     return best;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public List<Cluster<T>> cluster(Collection<T> points, int k, int maxIterations)
/*  70:    */     throws MathIllegalArgumentException
/*  71:    */   {
/*  72:158 */     MathUtils.checkNotNull(points);
/*  73:161 */     if (points.size() < k) {
/*  74:162 */       throw new NumberIsTooSmallException(Integer.valueOf(points.size()), Integer.valueOf(k), false);
/*  75:    */     }
/*  76:166 */     List<Cluster<T>> clusters = chooseInitialCenters(points, k, this.random);
/*  77:    */     
/*  78:    */ 
/*  79:    */ 
/*  80:170 */     int[] assignments = new int[points.size()];
/*  81:171 */     assignPointsToClusters(clusters, points, assignments);
/*  82:    */     
/*  83:    */ 
/*  84:174 */     int max = maxIterations < 0 ? 2147483647 : maxIterations;
/*  85:175 */     for (int count = 0; count < max; count++)
/*  86:    */     {
/*  87:176 */       boolean emptyCluster = false;
/*  88:177 */       List<Cluster<T>> newClusters = new ArrayList();
/*  89:178 */       for (Cluster<T> cluster : clusters)
/*  90:    */       {
/*  91:    */         T newCenter;
/*  92:180 */         if (cluster.getPoints().isEmpty())
/*  93:    */         {
/*  94:    */           
/*  95:181 */           switch (this.emptyStrategy.ordinal())
/*  96:    */           {
/*  97:    */           case 1: 
/*  98:183 */             newCenter = getPointFromLargestVarianceCluster(clusters);
/*  99:184 */             break;
/* 100:    */           case 2: 
/* 101:186 */             newCenter = getPointFromLargestNumberCluster(clusters);
/* 102:187 */             break;
/* 103:    */           case 3: 
/* 104:189 */             newCenter = getFarthestPoint(clusters);
/* 105:190 */             break;
/* 106:    */           default: 
/* 107:192 */             throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
/* 108:    */           }
/* 109:194 */           emptyCluster = true;
/* 110:    */         }
/* 111:    */         else
/* 112:    */         {
/* 113:196 */           newCenter = (T)cluster.getCenter().centroidOf(cluster.getPoints());
/* 114:    */         }
/* 115:198 */         newClusters.add(new Cluster(newCenter));
/* 116:    */       }
/* 117:200 */       int changes = assignPointsToClusters(newClusters, points, assignments);
/* 118:201 */       clusters = newClusters;
/* 119:205 */       if ((changes == 0) && (!emptyCluster)) {
/* 120:206 */         return clusters;
/* 121:    */       }
/* 122:    */     }
/* 123:209 */     return clusters;
/* 124:    */   }
/* 125:    */   
/* 126:    */   private static <T extends Clusterable<T>> int assignPointsToClusters(List<Cluster<T>> clusters, Collection<T> points, int[] assignments)
/* 127:    */   {
/* 128:224 */     int assignedDifferently = 0;
/* 129:225 */     int pointIndex = 0;
/* 130:226 */     for (T p : points)
/* 131:    */     {
/* 132:227 */       int clusterIndex = getNearestCluster(clusters, p);
/* 133:228 */       if (clusterIndex != assignments[pointIndex]) {
/* 134:229 */         assignedDifferently++;
/* 135:    */       }
/* 136:232 */       Cluster<T> cluster = (Cluster)clusters.get(clusterIndex);
/* 137:233 */       cluster.addPoint(p);
/* 138:234 */       assignments[(pointIndex++)] = clusterIndex;
/* 139:    */     }
/* 140:237 */     return assignedDifferently;
/* 141:    */   }
/* 142:    */   
/* 143:    */   private static <T extends Clusterable<T>> List<Cluster<T>> chooseInitialCenters(Collection<T> points, int k, Random random)
/* 144:    */   {
/* 145:254 */     List<T> pointList = Collections.unmodifiableList(new ArrayList(points));
/* 146:    */     
/* 147:    */ 
/* 148:257 */     int numPoints = pointList.size();
/* 149:    */     
/* 150:    */ 
/* 151:    */ 
/* 152:261 */     boolean[] taken = new boolean[numPoints];
/* 153:    */     
/* 154:    */ 
/* 155:264 */     List<Cluster<T>> resultSet = new ArrayList();
/* 156:    */     
/* 157:    */ 
/* 158:267 */     int firstPointIndex = random.nextInt(numPoints);
/* 159:    */     
/* 160:269 */     T firstPoint = (T)pointList.get(firstPointIndex);
/* 161:    */     
/* 162:271 */     resultSet.add(new Cluster(firstPoint));
/* 163:    */     
/* 164:    */ 
/* 165:274 */     taken[firstPointIndex] = true;
/* 166:    */     
/* 167:    */ 
/* 168:    */ 
/* 169:278 */     double[] minDistSquared = new double[numPoints];
/* 170:282 */     for (int i = 0; i < numPoints; i++) {
/* 171:283 */       if (i != firstPointIndex)
/* 172:    */       {
/* 173:284 */         double d = firstPoint.distanceFrom(pointList.get(i));
/* 174:285 */         minDistSquared[i] = (d * d);
/* 175:    */       }
/* 176:    */     }
/* 177:289 */     while (resultSet.size() < k)
/* 178:    */     {
/* 179:293 */       double distSqSum = 0.0D;
/* 180:295 */       for (int i = 0; i < numPoints; i++) {
int[] taken2 = null;
/* 181:296 */         if (taken2[i] == 0) {
/* 182:297 */           distSqSum += minDistSquared[i];
/* 183:    */         }
/* 184:    */       }
/* 185:303 */       double r = random.nextDouble() * distSqSum;
/* 186:    */       
/* 187:    */ 
/* 188:306 */       int nextPointIndex = -1;
/* 189:    */       
/* 190:    */ 
/* 191:    */ 
/* 192:310 */       double sum = 0.0D;
int[] taken2 = null;
/* 193:311 */       for (int i = 0; i < numPoints; i++) {
/* 194:312 */         if (taken2[i] == 0)
/* 195:    */         {
/* 196:313 */           sum += minDistSquared[i];
/* 197:314 */           if (sum >= r)
/* 198:    */           {
/* 199:315 */             nextPointIndex = i;
/* 200:316 */             break;
/* 201:    */           }
/* 202:    */         }
/* 203:    */       }
/* 204:324 */       if (nextPointIndex == -1) {
/* 205:325 */         for (int i = numPoints - 1; i >= 0; i--) {
/* 206:326 */           if (taken2[i] == 0)
/* 207:    */           {
/* 208:327 */             nextPointIndex = i;
/* 209:328 */             break;
/* 210:    */           }
/* 211:    */         }
/* 212:    */       }
/* 213:334 */       if (nextPointIndex < 0) {
/* 214:    */         break;
/* 215:    */       }
/* 216:336 */       T p = (T)pointList.get(nextPointIndex);
/* 217:    */       
/* 218:338 */       resultSet.add(new Cluster(p));
/* 219:    */       
/* 220:    */ 
/* 221:341 */       taken[nextPointIndex] = true;
/* 222:343 */       if (resultSet.size() < k) {
/* 223:346 */         for (int j = 0; j < numPoints; j++) {
/* 224:348 */           if (taken2[j] == 0)
/* 225:    */           {
/* 226:349 */             double d = p.distanceFrom(pointList.get(j));
/* 227:350 */             double d2 = d * d;
/* 228:351 */             if (d2 < minDistSquared[j]) {
/* 229:352 */               minDistSquared[j] = d2;
/* 230:    */             }
/* 231:    */           }
/* 232:    */         }
/* 233:    */       }
/* 234:    */     }
/* 235:366 */     return resultSet;
/* 236:    */   }
/* 237:    */   
/* 238:    */   private T getPointFromLargestVarianceCluster(Collection<Cluster<T>> clusters)
/* 239:    */   {
/* 240:377 */     double maxVariance = (-1.0D / 0.0D);
/* 241:378 */     Cluster<T> selected = null;
/* 242:379 */     for (Cluster<T> cluster : clusters) {
/* 243:380 */       if (!cluster.getPoints().isEmpty())
/* 244:    */       {
/* 245:383 */         T center = cluster.getCenter();
/* 246:384 */         Variance stat = new Variance();
/* 247:385 */         for (T point : cluster.getPoints()) {
/* 248:386 */           stat.increment(point.distanceFrom(center));
/* 249:    */         }
/* 250:388 */         double variance = stat.getResult();
/* 251:391 */         if (variance > maxVariance)
/* 252:    */         {
/* 253:392 */           maxVariance = variance;
/* 254:393 */           selected = cluster;
/* 255:    */         }
/* 256:    */       }
/* 257:    */     }
/* 258:400 */     if (selected == null) {
/* 259:401 */       throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
/* 260:    */     }
/* 261:405 */     List<T> selectedPoints = selected.getPoints();
/* 262:406 */     return (T)selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
/* 263:    */   }
/* 264:    */   
/* 265:    */   private T getPointFromLargestNumberCluster(Collection<Cluster<T>> clusters)
/* 266:    */   {
/* 267:418 */     int maxNumber = 0;
/* 268:419 */     Cluster<T> selected = null;
/* 269:420 */     for (Cluster<T> cluster : clusters)
/* 270:    */     {
/* 271:423 */       int number = cluster.getPoints().size();
/* 272:426 */       if (number > maxNumber)
/* 273:    */       {
/* 274:427 */         maxNumber = number;
/* 275:428 */         selected = cluster;
/* 276:    */       }
/* 277:    */     }
/* 278:434 */     if (selected == null) {
/* 279:435 */       throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
/* 280:    */     }
/* 281:439 */     List<T> selectedPoints = selected.getPoints();
/* 282:440 */     return (T)selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
/* 283:    */   }
/* 284:    */   
/* 285:    */   private T getFarthestPoint(Collection<Cluster<T>> clusters)
/* 286:    */   {
/* 287:452 */     double maxDistance = (-1.0D / 0.0D);
/* 288:453 */     Cluster<T> selectedCluster = null;
/* 289:454 */     int selectedPoint = -1;
/* 290:455 */     for (Cluster<T> cluster : clusters)
/* 291:    */     {
/* 292:458 */       T center = cluster.getCenter();
/* 293:459 */       List<T> points = cluster.getPoints();
/* 294:460 */       for (int i = 0; i < points.size(); i++)
/* 295:    */       {
/* 296:461 */         double distance = ((Clusterable)points.get(i)).distanceFrom(center);
/* 297:462 */         if (distance > maxDistance)
/* 298:    */         {
/* 299:463 */           maxDistance = distance;
/* 300:464 */           selectedCluster = cluster;
/* 301:465 */           selectedPoint = i;
/* 302:    */         }
/* 303:    */       }
/* 304:    */     }
/* 305:472 */     if (selectedCluster == null) {
/* 306:473 */       throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
/* 307:    */     }
/* 308:476 */     return (T)selectedCluster.getPoints().remove(selectedPoint);
/* 309:    */   }
/* 310:    */   
/* 311:    */   private static <T extends Clusterable<T>> int getNearestCluster(Collection<Cluster<T>> clusters, T point)
/* 312:    */   {
/* 313:490 */     double minDistance = 1.7976931348623157E+308D;
/* 314:491 */     int clusterIndex = 0;
/* 315:492 */     int minCluster = 0;
/* 316:493 */     for (Cluster<T> c : clusters)
/* 317:    */     {
/* 318:494 */       double distance = point.distanceFrom(c.getCenter());
/* 319:495 */       if (distance < minDistance)
/* 320:    */       {
/* 321:496 */         minDistance = distance;
/* 322:497 */         minCluster = clusterIndex;
/* 323:    */       }
/* 324:499 */       clusterIndex++;
/* 325:    */     }
/* 326:501 */     return minCluster;
/* 327:    */   }
/* 328:    */ }


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer
 * JD-Core Version:    0.7.0.1
 */